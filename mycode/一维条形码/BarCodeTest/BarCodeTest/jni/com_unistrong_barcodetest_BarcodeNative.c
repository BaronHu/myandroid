#include <jni.h>
#include <stdio.h>
#include <utils/Log.h>
#include <cutils/log.h>
#include <time.h>
#include <termios.h>
#include <fcntl.h>
#include "barcode.h"
#include <stdlib.h>

#include "com_unistrong_barcodetest_BarcodeNative.h"

#define SE955_NAME "/dev/char_se955"
static struct termios termios;
#define max_size 128
typedef struct {
	int pos;
	char check_buff[max_size];
}Back_st;

/*a memery size*/
static int Me_SIZE(char *p)
{
	int size_num=0;
	while(*p!='\0')
		{
			size_num++;
			p++;
			
		}
	return size_num;
}

/*uart_a init -----serial*/
static void UART_INIT(int fd)
{
		LOGI(">>>> UART_A_INIT..\n");
		int err;
		tcflush(fd, TCIOFLUSH);
		if ((err = tcgetattr(fd,&termios)) != 0)
		{
		LOGI("tcgetattr(%d) = %d\r\n",fd,err);
		close(fd);
		}
		termios.c_iflag &= ~(IGNBRK|BRKINT|PARMRK|ISTRIP|INLCR|IGNCR|ICRNL|IXON);
		termios.c_oflag &= ~OPOST;
		termios.c_lflag &= ~(ECHO|ECHONL|ICANON|ISIG|IEXTEN);
		termios.c_cflag &= ~(CSIZE|PARENB);
		termios.c_cflag |= CS8;
		termios.c_cflag &= ~CRTSCTS;//no flow control

		tcsetattr(fd, TCSANOW, &termios);
		tcflush(fd, TCIOFLUSH);
		tcsetattr(fd, TCSANOW, &termios);
		tcflush(fd, TCIOFLUSH);
		tcflush(fd, TCIOFLUSH);

		if (cfsetispeed(&termios,B9600))
		{
		LOGE("cfsetispeed.. errno..\r\n");
			close(fd);
			//return(-1);
		}

		// Set the output baud rates in the termios.
		if (cfsetospeed(&termios,B9600))
		{
		LOGE("cfsetispeed.. errno..\r\n");
			close(fd);
			//return(-1);
		}

		tcsetattr(fd,TCSANOW,&termios);

		LOGE("UART_A_INIT..\n");

		if (fd < 0) {
		LOGD("no gps emulation detected");
		LOGE("UART_A_INIT..-------------------\n");
		close(fd);
		return;
		}

}
/****uart read****/
char *barcode_Uart_Read(int cmd)
{
	static char buff[10];
	char err[]="open uart err";
	static Back_st back_value;
	back_value.pos=0;
	int fd=-1;
	int ret=-1;
	int n=0;
	memset(back_value.check_buff,0,sizeof(back_value.check_buff));
	switch(cmd)
		{
			case uart0:
				LOGD("write uart0\n");
				fd= open("/dev/s3c2410_serial0",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial0  ERROR..fd=%d\n",fd); 
				return err;
				}else
				LOGE("open port:/dev/s3c2410_serial0  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				while(1)
					{
					memset(buff,0,sizeof(buff));
					ret=read(fd,buff,4);
					LOGD("read buff=%s\n",buff);
					for(n=0;n<ret;n++)
						{
						back_value.check_buff[back_value.pos]=buff[n];
						back_value.pos+=1;
						if(buff[n]=='\n')
							{
								back_value.pos=0;
								break;
						}
						}
					if(back_value.pos==0)
						break;
					LOGD("back_value.check_buff=%s\n",back_value.check_buff);
					}
				break;
			case uart1:
				LOGD("write uart1\n");
				fd= open("/dev/s3c2410_serial1",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial1  ERROR..fd=%d\n",fd); 
				return err;
				}else
				LOGE("open port:/dev/s3c2410_serial1  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				while(1)
					{
					memset(buff,0,sizeof(buff));
					ret=read(fd,buff,4);
					LOGD("read buff=%s\n",buff);
					for(n=0;n<ret;n++)
						{
						back_value.check_buff[back_value.pos]=buff[n];
						back_value.pos+=1;
						if(buff[n]=='\n')
							{
								back_value.pos=0;
								break;
						}
						}
					if(back_value.pos==0)
						break;
					LOGD("back_value.check_buff=%s\n",back_value.check_buff);
					}

				break;
			case uart2:
				LOGD("write uart2\n");
				fd= open("/dev/s3c2410_serial2",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial2  ERROR..fd=%d\n",fd); 
				return err;
				}else
				LOGE("open port:/dev/s3c2410_serial2  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				while(1)
					{
					memset(buff,0,sizeof(buff));
					ret=read(fd,buff,4);
					LOGD("read buff=%s\n",buff);
					for(n=0;n<ret;n++)
						{
						back_value.check_buff[back_value.pos]=buff[n];
						back_value.pos+=1;
						if(buff[n]=='\n')
							{
								back_value.pos=0;
								break;
						}
						}
					if(back_value.pos==0)
						break;
					LOGD("back_value.check_buff=%s\n",back_value.check_buff);
					}

				break;
			case uart3:
				LOGD("read uart3\n");
				fd= open("/dev/s3c2410_serial3",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial3  ERROR..fd=%d\n",fd); 
				return err;
				}else
				LOGE("open port:/dev/s3c2410_serial3  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				while(1)
					{
					memset(buff,0,sizeof(buff));
					ret=read(fd,buff,4);
					for(n=0;n<ret;n++)
						{
						back_value.check_buff[back_value.pos]=buff[n];
						//LOGD("back_value.check_buff[%d]=%c\n",back_value.pos,back_value.check_buff[back_value.pos]);
						back_value.pos+=1;
						if(buff[n]=='\r'){
								back_value.pos=0;
								break; //break for
						 }
						}//end for
					 if(back_value.pos==0){
						  break; //break while(1)
					 }		
					}//end while(1)
					LOGD("back_value.check_buff=%s\n",back_value.check_buff);
				break;
			default:
				LOGD("write err uart\n");
				break;
	}
  
	close(fd);
	return (back_value.check_buff);
}
/******uart write****/
int Uart_Write(int cmd,char *buff)
{
	int fd=-1;
	int ret=-1;
	switch(cmd)
		{
			case uart0:
				LOGD("write uart0\n");
				fd= open("/dev/s3c2410_serial0",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial0  ERROR..fd=%d\n",fd); 
				return fd;
				}else
				LOGE("open port:/dev/s3c2410_serial0  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				LOGD("write string=%s\n",buff);
				ret=write(fd,buff,Me_SIZE(buff));
				break;
			case uart1:
				LOGD("write uart1\n");
				fd= open("/dev/s3c2410_serial1",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial1  ERROR..fd=%d\n",fd); 
				return fd;
				}else
				LOGE("open port:/dev/s3c2410_serial1  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				LOGD("write string=%s\n",buff);
				ret=write(fd,buff,Me_SIZE(buff));
				break;
			case uart2:
				LOGD("write uart2\n");
				fd= open("/dev/s3c2410_serial2",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial2  ERROR..fd=%d\n",fd); 
				return fd;
				}else
				LOGE("open port:/dev/s3c2410_serial2  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				LOGD("write string=%s\n",buff);
				ret=write(fd,buff,Me_SIZE(buff));
				break;
			case uart3:
				LOGD("write uart3\n");
				fd= open("/dev/s3c2410_serial3",O_RDWR|O_NOCTTY|O_NDELAY);
				if( fd < 0){
				LOGE("open port /dev/s3c2410_serial3  ERROR..fd=%d\n",fd); 
				return fd;
				}else
				LOGE("open port:/dev/s3c2410_serial3  succceed..fd=%d\n",fd);
				if(fcntl( fd,F_SETFL,0)<0)
				LOGE("fcntl F_SETFL\n");
				UART_INIT(fd);
				LOGD("write string=%s\n",buff);
				ret=write(fd,buff,Me_SIZE(buff));
				break;
			default:
				LOGD("write err uart\n");
				break;
	}
	close(fd);
	return ret;
}


/*
 * Class:     com_unistrong_barcodetest_BarcodeNative
 * Method:    initUart
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_unistrong_barcodetest_BarcodeNative_initUart
  (JNIEnv *env, jclass cls, jint cmd){
    int fd=-1;
    fd=open(SE955_NAME,O_RDWR);
    if(fd<0)
    	{
    			LOGD("open se955 fail!\n");
    			return ;
    		}
    	ioctl(fd,0,NULL);
    	close(fd);
    //open uart
    LOGD("open..........cmd=%d",cmd);
    
}

/*
 * Class:     com_unistrong_barcodetest_BarcodeNative
 * Method:    exit
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_unistrong_barcodetest_BarcodeNative_exit
  (JNIEnv *env, jclass cls, jint cmd){
    //close uart
    int fd=-1;
    fd=open(SE955_NAME,O_RDWR);
	  ioctl(fd,3,NULL);
	  close(fd);
    LOGD("EXIT........cmd=%d",cmd);
}

/*
 * Class:     com_unistrong_barcodetest_BarcodeNative
 * Method:    get
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_unistrong_barcodetest_BarcodeNative_get
  (JNIEnv *env, jclass cls, jint cmd){
	  char* str;
	  str = barcode_Uart_Read(cmd);
	  LOGD("str===%scmd=%d\n", str,cmd);  
	  return (*env)->NewStringUTF(env, str);
}

/*
 * Class:     com_unistrong_barcodetest_BarcodeNative
 * Method:    scan
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_unistrong_barcodetest_BarcodeNative_scan
  (JNIEnv *env, jclass cls, jint cmd){
	  //start scan
	  int fd=-1;
	  fd=open(SE955_NAME,O_RDWR);
	  ioctl(fd,1,NULL);
	  close(fd);
	  LOGD("fd====%d", fd);
}
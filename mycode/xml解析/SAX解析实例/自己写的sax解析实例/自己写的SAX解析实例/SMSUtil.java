package com.cellon.sdback.util;

/**
 * some fields of SMS ,these fields will be insert into database
 * @author Baron.Hu
 * @date 2011-07-14
 * */
public class SMSUtil {
	
	public int id;          //id
	
	public String address;   //phone number
	
	public String body;      //SMS body

	public int type;         //1:DELEVER(in), 2:SUBMIT(out)
	
	public String date;        //sent or receive date
	
	public String getAddress() {
		return address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

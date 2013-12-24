LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := barcodejni

# This is the target being built.
#编译的目标对象
LOCAL_MODULE:= libbarcodejni


# All of the source files that we will compile.
# 编译的源文件
LOCAL_SRC_FILES:= \
  com_unistrong_barcodetest_BarcodeNative.c

# All of the shared libraries we link against.
#连接时需要的外部库
LOCAL_SHARED_LIBRARIES := \
	libutils

# No static libraries.
LOCAL_STATIC_LIBRARIES :=

# Also need the JNI headers.
#需要包含的头文件目录
LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE)

# No special compiler flags.
LOCAL_CFLAGS +=

# Don't prelink this library.  For more efficient code, you may want
# to add this library to the prelink map and set this to true. However,
# it's difficult to do this for applications that are not supplied as
# part of a system image.
#是否需要prelink处理
LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)

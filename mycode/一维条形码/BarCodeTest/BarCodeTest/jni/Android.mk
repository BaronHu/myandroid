LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := barcodejni

# This is the target being built.
#�����Ŀ�����
LOCAL_MODULE:= libbarcodejni


# All of the source files that we will compile.
# �����Դ�ļ�
LOCAL_SRC_FILES:= \
  com_unistrong_barcodetest_BarcodeNative.c

# All of the shared libraries we link against.
#����ʱ��Ҫ���ⲿ��
LOCAL_SHARED_LIBRARIES := \
	libutils

# No static libraries.
LOCAL_STATIC_LIBRARIES :=

# Also need the JNI headers.
#��Ҫ������ͷ�ļ�Ŀ¼
LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE)

# No special compiler flags.
LOCAL_CFLAGS +=

# Don't prelink this library.  For more efficient code, you may want
# to add this library to the prelink map and set this to true. However,
# it's difficult to do this for applications that are not supplied as
# part of a system image.
#�Ƿ���Ҫprelink����
LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)

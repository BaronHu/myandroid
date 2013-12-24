LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES :=\
                        achartengine:achartengine-0.6.0.jar  \
                        android-support:android-support-v4.jar
include $(BUILD_MULTI_PREBUILT)


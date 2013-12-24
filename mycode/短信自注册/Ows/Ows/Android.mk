LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_STATIC_JAVA_LIBRARIES :=achartengine android-support

LOCAL_JAVA_LIBRARIES += qcnvitems qcrilhook

LOCAL_PACKAGE_NAME := Ows

LOCAL_CERTIFICATE := platform
LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))

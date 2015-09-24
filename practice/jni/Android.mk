LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE:= TestJni
LOCAL_SRC_FILES := hello.c
include $(BUILD_SHARED_LIBRARY)
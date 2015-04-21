LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := CompassNDKAndOpenCV
LOCAL_SRC_FILES := CompassNDKAndOpenCV.cpp

include $(BUILD_SHARED_LIBRARY)

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include C:/OpenCV4Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := CompassNDKAndOpenCV
LOCAL_SRC_FILES := jni_part.cpp
LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)


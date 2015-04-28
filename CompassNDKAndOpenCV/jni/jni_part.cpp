#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <android/log.h>

#define APPNAME "com.devsmobile.compassndkandopencv"


using namespace std;
using namespace cv;

extern "C" {
JNIEXPORT void JNICALL Java_com_devsmobile_compassndkandopencv_MainActivity_paintAzimuth(JNIEnv*, jobject, jlong addrRgba, jfloat balizaAzimuth, jfloat azimuth);

JNIEXPORT void JNICALL Java_com_devsmobile_compassndkandopencv_MainActivity_paintAzimuth(JNIEnv*, jobject, jlong addrRgba, jfloat balizaAzimuth, jfloat azimuth)
{

	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:baliza %f", balizaAzimuth);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:azimuth %f", azimuth);
    Mat& mRgb = *(Mat*)addrRgba;

    jfloat dif = balizaAzimuth - azimuth;

    if(dif<0){
    	dif = dif*-1;
    }

    if(dif<15.0){
    	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:diferencia %f", dif);
    	circle(mRgb, Point(150, 150), 100, Scalar(255,0,0,255));
    }
}
}

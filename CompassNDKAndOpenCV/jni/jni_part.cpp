#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <android/log.h>

#define APPNAME "com.devsmobile.compassndkandopencv"
#define PI 3.1416
//Assuming 60 degrees of vision in Nexus 4 camera
#define ANGLEOFVISION 60


using namespace std;
using namespace cv;

extern "C" {
JNIEXPORT void JNICALL Java_com_devsmobile_compassndkandopencv_MainActivity_paintAzimuth(JNIEnv*, jobject, jlong addrRgba, jint width, jint height, jfloat markerOrientation, jfloat orientation);

char heading[4];
JNIEXPORT void JNICALL Java_com_devsmobile_compassndkandopencv_MainActivity_paintAzimuth(JNIEnv*, jobject, jlong addrRgba, jint width, jint height, jfloat markerOrientation, jfloat orientation)
{

	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:marker %f", markerOrientation);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:orientation %f", orientation);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:width %d", width);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:height %d", height);
    Mat& mRgb = *(Mat*)addrRgba;

    jfloat dif = markerOrientation - orientation;
    __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:dif %f", dif);

    int margin = ANGLEOFVISION/2;

    if(-margin < dif && dif < margin){
    	int x = width/2 + (dif/margin)*(width/2);
    	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "NDK:FINAL X %d", x);
    	circle(mRgb, Point(x, height/2), 100, Scalar(255,0,0,255));
    }
	/*Mat& mRgb = *(Mat*)addrRgba;*/

	sprintf(heading, "%d", (int)orientation);
	putText(mRgb,heading,Point(width/2,height/2), FONT_HERSHEY_SIMPLEX, 2, Scalar(255,0,0,255), 6, 6, false);
}
}

package com.devsmobile.compassndkandopencv;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.devsmobile.compassndkandopencv.sensors.CompassSensor;

public class MainActivity extends Activity implements CvCameraViewListener2,CompassSensor.AzimuthListener {
	
	private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
	
	private static final String TAG = "CompassNDK::Activity";
	
	//------------ ACTIVITY LIFECYCLE COPIED FROM  OPENCV SAMPLES ---------------//
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    
                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("CompassNDKAndOpenCV");

                    
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main_activity);

        if (mIsJavaCamera){
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        } else {
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
        }

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        
        CompassSensor.getInstance(this.getApplicationContext()).stop();
        if (mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
        
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        CompassSensor.getInstance(this.getApplicationContext()).addListener(this).start();
    }

    public void onDestroy() {
        super.onDestroy();
        CompassSensor.getInstance(this.getApplicationContext()).stop();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    // ----------- TO IMPLEMENT BY CvCameraViewListener2 --------------// 
    
	@Override
	public void onCameraViewStarted(int width, int height) {
		mRgba = new Mat(height, width, CvType.CV_8UC4);
		this.width = width;
		this.height = height;
	}

	@Override
	public void onCameraViewStopped() {
		mRgba.release();
	}
	
	private int width;
	
    private int height;
	
	private Mat mRgba;
	
	private float balizaAzimuth = 100;
	
	private float currentAzimuth = 0;

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba = inputFrame.rgba();
		paintAzimuth(mRgba.getNativeObjAddr(), this.width, this.height, balizaAzimuth, currentAzimuth);
		return mRgba;
	}
	
	public native void paintAzimuth(long matAddrRgba, int width, int height, float markerOrientation, float orientation);

	@Override
	public void onAzimuthChange(float azimuth) {
		Log.d(TAG, "Current Azimith"+azimuth);
		this.currentAzimuth = azimuth;
	}
	
}

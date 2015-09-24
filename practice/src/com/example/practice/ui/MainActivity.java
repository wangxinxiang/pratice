package com.example.practice.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.widget.FrameLayout;
import com.example.practice.R;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
	private Camera mCamera ;
	private CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		if(!checkCameraHardware(this)){
			finish();
		}
		mCamera = getCameraInstance();
		if(mCamera==null){
			finish();
		}
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
		
		
        new Thread(){
        	public void run() {
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mCamera.takePicture(null, null, new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						try {
							FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"info.jpg"));
							fos.write(data);
							fos.close();
							//mCamera.startPreview();
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
        	}
		}.start();
        
//        findViewById(R.id.button_capture).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
	}

	private boolean checkCameraHardware(Context context) {
		// this device has a camera
// no camera on this device
		return context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA);
	}
	
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open();// ��ȡ�����ʵ��
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	
	@Override
	protected void onDestroy() {
		mCamera.release();
		mCamera = null;
		super.onDestroy();
	}
}

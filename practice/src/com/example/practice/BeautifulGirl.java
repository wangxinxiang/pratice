package com.example.practice;

import android.app.Activity;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class BeautifulGirl extends Activity{
	private ImageView iv;
	private Bitmap srcBitmap;
	private Bitmap copyBitmap;
	private Paint paint;
	private Canvas canvas;
    private SensorManager sensorManager;
    private MyLightListener lightListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        initView();

        dragGirl();

        initLightSenor();
	}

	private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
    }

    private void dragGirl() {
        srcBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.pre19);
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
                srcBitmap.getHeight(), srcBitmap.getConfig());
        canvas = new Canvas(copyBitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(srcBitmap, new Matrix(), paint);
        iv.setImageBitmap(copyBitmap);

        iv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            Log.d("onTouch --->", event.getX() + ";" + event.getY());
                            for (int i = -6; i < 7; i++) {
                                for (int j = -6; j < 7; j++) {
                                    if (Math.sqrt(i * i + j * j) <= 6) {
                                        copyBitmap.setPixel((int) event.getX() + i,
                                                (int) event.getY() + j,
                                                Color.TRANSPARENT);
                                    }
                                }
                            }
                            iv.setImageBitmap(copyBitmap);
                            break;
                    }

                    return true;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    private void initLightSenor(){
        lightListener = new MyLightListener();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(lightListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private class MyLightListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            float light = event.values[0];
            if (light < 10) {
                Toast.makeText(BeautifulGirl.this,"讨厌，不要靠近人家啊", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onDestroy() {
        //ע�����Ӧ
        sensorManager.unregisterListener(lightListener);
        lightListener = null;
        super.onDestroy();
    }
}

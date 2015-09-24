package com.example.practice.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.practice.R;

import java.io.File;
import java.io.FileOutputStream;

public class DrawSurface extends Activity {
	private ImageView iv;
	private Canvas canvas;
	private Paint paint;
	private Bitmap srcBitmap;
	private Bitmap copyBitmap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		iv = (ImageView) findViewById(R.id.iv);
// 设置一个灰白色的背景 ，在这个背景资源上作画。
		srcBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.background);
		// 创建原图的拷贝
		copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
				srcBitmap.getHeight(), srcBitmap.getConfig());
		canvas = new Canvas(copyBitmap);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawBitmap(srcBitmap, new Matrix(), paint);

		// canvas.drawLine(10, 10, 80, 80, paint);

		iv.setOnTouchListener(new View.OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 获取到触摸事件的类型。
				int action = event.getAction();
				// 按下，移动，离开
				switch (action) {
					case MotionEvent.ACTION_DOWN:// 按下
						// 记录下手指第一次按下的坐标
						startX = (int) event.getX();
						startY = (int) event.getY();
						System.out.println("按下：" + startX + "," + startY);
						break;
					case MotionEvent.ACTION_MOVE:// 移动
						// 得到移动后的新的坐标
						int newX = (int) event.getX();
						int newY = (int) event.getY();
						canvas.drawLine(startX, startY, newX, newY, paint);
						System.out.println("移动画线：" + startX + "," + startY + "~"
								+ newX + "," + newY);
						// 获取新的起点坐标
						startX = (int) event.getX();
						startY = (int) event.getY();
						iv.setImageBitmap(copyBitmap);
						break;
					case MotionEvent.ACTION_UP:// 离开
						break;
				}
				// True if the listener has consumed the event, false otherwise
				// true 代表监听器把事件处理完毕了，false代表没处理完毕
				return true;
			}
		});
		// 设置拷贝后的图片
		iv.setImageBitmap(copyBitmap);
	}

	public void clickBrush(View view) {
		// 设置画笔的宽度
		paint.setStrokeWidth(10);
	}

	public void clickRed(View view) {
		paint.setColor(Color.RED);
	}

	public void clickGreen(View view) {
		paint.setColor(Color.GREEN);
	}

	public void save(View view){
		try {
			File file = new File(Environment.getExternalStorageDirectory(),"haha.png");
			FileOutputStream stream = new FileOutputStream(file);
			copyBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			//欺骗图库应用。模拟一个sd卡挂载的广播消息。
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
			intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
			sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
		}
	}
}

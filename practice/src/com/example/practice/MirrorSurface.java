package com.example.practice;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;
import com.example.practice.until.Utility;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MirrorSurface extends Activity {
	//原图片
	private ImageView iv_src;
	//原图片内存空间的拷贝
	private ImageView iv_copy;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mirror);

		iv_src = (ImageView) findViewById(R.id.iv_src);
		iv_copy = (ImageView) findViewById(R.id.iv_copy);

		getImage();

	}

	private void getImage() {
		Intent intent1 = new Intent(Intent.ACTION_PICK);
		intent1.setType("image/*");
		startActivityForResult(intent1, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
			case 1:
//				ContentResolver resolver = getContentResolver();
//
//				BitmapFactory.Options opt = new BitmapFactory.Options();
//				opt.inPreferredConfig = Bitmap.Config.RGB_565;
//				opt.inPurgeable = true;
//				opt.inInputShareable = true;
//				InputStream input = null;
//				try {
//					input = resolver.openInputStream(data.getData());
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				Bitmap photo = BitmapFactory.decodeStream(input, null, opt);

				Bitmap photo = null;
				try {
					photo = Utility.getThumbnail(data.getData(), 1000, MirrorSurface.this);
				} catch (Exception e) {
					e.printStackTrace();
				}

				mirror(photo);
		}
	}

	private void mirror(Bitmap srcBitmap ) {
		//srcBitmap.setPixel(5, 5, Color.RED);
		iv_src.setImageBitmap(srcBitmap);
		//在内存空间中 创建原图的副本（拷贝）
		//1.创建一张空白的纸 ，材料保证完全一致。
		Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
		//2.临摹作画
		//创建画板 ,参考空白纸张的大小，把画板创建出来。
		Canvas canvas = new Canvas(copyBitmap);
		//3.作画
		//创建一个画笔
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);//设置默认的颜色。
		//作画
		//srcBitmap 原图，要临摹的图
		//matrix 变化矩阵
		//paint 画笔
		Matrix matrix = new Matrix();
		matrix.setScale(-1.0f, 1.0f);
		//只有采用post才可以让位置 移动立刻生效
		matrix.postTranslate(copyBitmap.getWidth(), 0);
		canvas.drawBitmap(srcBitmap,matrix , paint);
		iv_copy.setImageBitmap(copyBitmap);

	}
}

package com.example.practice.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.example.practice.R;
import com.example.practice.view.CircleImageDrawable;
import com.example.practice.view.RoundImageDrawable;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MeiTuXiuXiu extends Activity implements OnSeekBarChangeListener {
	private ImageView iv;
	private SeekBar sb_r;
	private SeekBar sb_g;
	private SeekBar sb_b;
	private Paint paint;
	private Canvas canvas ;
	private Bitmap srcBitmap ;
	private Bitmap copyBitmap ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meitu);
		sb_r = (SeekBar) findViewById(R.id.sb_r);
		sb_g = (SeekBar) findViewById(R.id.sb_g);
		sb_b = (SeekBar) findViewById(R.id.sb_b);
		sb_b.setOnSeekBarChangeListener(this);
		sb_r.setOnSeekBarChangeListener(this);
		sb_g.setOnSeekBarChangeListener(this);

		iv = (ImageView) findViewById(R.id.iv);

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
				ContentResolver resolver = getContentResolver();

				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inPreferredConfig = Bitmap.Config.RGB_565;
				opt.inPurgeable = true;
				opt.inInputShareable = true;
				InputStream input = null;
				try {
					input = resolver.openInputStream(data.getData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
                srcBitmap = BitmapFactory.decodeStream(input, null, opt);

				meiTu(srcBitmap);
		}
	}

	private void meiTu(Bitmap srcBitmap ) {
		// srcBitmap.setPixel(5, 5, Color.RED);
		// 在内存空间中 创建原图的副本（拷贝）
		// 1.创建一张空白的纸 ，材料保证完全一致。
		copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
				srcBitmap.getHeight(), srcBitmap.getConfig());
		// 2.临摹作画
		// 创建画板 ,参考空白纸张的大小，把画板创建出来。
		canvas = new Canvas(copyBitmap);
		// 3.作画
		// 创建一个画笔
		paint = new Paint();
		paint.setColor(Color.BLACK);// 设置默认的颜色。
		// 作画
		// srcBitmap 原图，要临摹的图
		// matrix 变化矩阵
		// paint 画笔
		canvas.drawBitmap(srcBitmap, new Matrix(), paint);
		iv.setImageDrawable(new RoundImageDrawable(copyBitmap));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int seebarid = seekBar.getId();
		int value = seekBar.getProgress();
		ColorMatrix cm = new ColorMatrix();
		float rf = 1.0f;
		float gf = 1.0f;
		float bf = 1.0f;
		
//		New Red Value = 1*128 + 0*128 + 0*128 + 0*0 + 0
//		New Blue Value = 0*128 + 1*128 + 0*128 + 0*0 + 0
//		New Green Value = 0*128 + 0*128 + 1*128 + 0*0 + 0
//		New Alpha Value = 0*128 + 0*128 + 0*128 + 1*0 + 0
	
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		switch (seebarid) {
		case R.id.sb_r:
			rf = value/128.0f;
			break;
		case R.id.sb_g:
			gf = value/128.0f;
			break;
		case R.id.sb_b:
			bf = value/128.0f;
			break;
		}
		cm.set(new float[] {
				rf, 0, 0, 0, 0,
				0, gf, 0, 0, 0,
				0, 0, bf, 0, 0,
				0, 0, 0, 1, 0
		});
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(srcBitmap, new Matrix(), paint);
		iv.setImageDrawable(new CircleImageDrawable(copyBitmap));
	}

}

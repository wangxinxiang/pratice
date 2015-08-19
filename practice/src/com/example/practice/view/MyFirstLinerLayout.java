package com.example.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.practice.R;

/**
 * Created by Administrator on 2015/6/29.
 */
public class MyFirstLinerLayout extends LinearLayout{
    public MyFirstLinerLayout(Context context) {
        super(context);
    }

    public MyFirstLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resource = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FirstView);
        ImageView imageView = new ImageView(context);
        TextView textView = new TextView(context);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.FirstView_Oriental:
                    resource = typedArray.getInt(R.styleable.FirstView_Oriental, 0);
                    this.setOrientation(resource == 1 ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
                    break;
                case R.styleable.FirstView_Src:
                    resource = typedArray.getResourceId(R.styleable.FirstView_Src, 0);
                    imageView.setImageResource(resource > 0 ? resource : R.drawable.a);
                    break;
                case R.styleable.FirstView_Text:
                    resource = typedArray.getResourceId(R.styleable.FirstView_Text, 0);
                    textView.setText(resource > 0 ? typedArray.getResources().getText(resource) : typedArray.getString(R.styleable.FirstView_Text) );
            }
        }

        addView(imageView);
        addView(textView);
        typedArray.recycle();
    }
}

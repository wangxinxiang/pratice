package com.example.practice.view;



import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.example.practice.R;

/**
 * Created by Administrator on 2015/5/24.
 */
public class FragmentDialog extends DialogFragment {
    private Button button1,button2;
    private DialogListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment, null);
        button1 = (Button) view.findViewById(R.id.dialog_btn1);
        button2 = (Button) view.findViewById(R.id.dialog_btn2);

        initListener();
        return view;
    }

    private void initListener() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null) {
                    listener.commit();
                }
            }
        });
    }

    public interface DialogListener {
        void commit();
    }


    public void setListener(DialogListener listener) {
        this.listener = listener;
    }
}

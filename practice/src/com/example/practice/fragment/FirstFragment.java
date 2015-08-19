package com.example.practice.fragment;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.practice.FirstSurfaceView;
import com.example.practice.R;
import com.example.practice.TestViewGroupActivity;
import com.example.practice.adapter.ListViewAdapter;
import com.example.practice.service.ContactInfoParser;
import com.example.practice.until.Constant;
import com.example.practice.until.Utility;
import com.example.practice.view.FragmentDialog;
import com.example.practice.view.FragmentDialog2;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/5/24.
 */
public class FirstFragment extends Fragment {
    private View view;
    private Button button1,button2,backHome,web,vibrator,popup_window,surface,test;
    private ArrayList<String> phones = new ArrayList<String>();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first_fragment, container, false);
        initView();
        return view;
    }

    private void initView() {

        button1 = (Button) view.findViewById(R.id.fragmentDialog);
        button2 =  (Button) view.findViewById(R.id.fragmentDialog2);
        backHome = (Button) view.findViewById(R.id.home);
        web = (Button) view.findViewById(R.id.web);
        vibrator = (Button) view.findViewById(R.id.vibrator);
        popup_window = (Button) view.findViewById(R.id.popup_window);
        surface = (Button) view.findViewById(R.id.surface);
        test = (Button) view.findViewById(R.id.test);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentDialog dialog = new FragmentDialog();
                dialog.setListener(new FragmentDialog.DialogListener(){
                    @Override
                    public void commit() {
                        Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentDialog2 dialog = new FragmentDialog2();
                dialog.show(getFragmentManager(), "");
            }
        });
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                String tel = "tel:18297608334";
                Uri uri = Uri.parse(tel);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String web = "http://kan.sina.com.cn/u/2608720237/3482229";
                Uri uri = Uri.parse(web);
                intent.setData(uri);
                 startActivity(intent);
            }
        });

        vibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator1 = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vibrator1.vibrate(1000);
            }
        });
        vibrator.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator vibrator1 = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vibrator1.vibrate(3000);
                return false;
            }
        });

        //popupWindow
        ViewGroup root = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.my_view, null);
        final PopupWindow popupWindow = new PopupWindow(root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置点击外部可dimiss
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));

        popup_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(button1);         //在view位置下出现的弹框
            }
        });

        surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FirstSurfaceView.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TestViewGroupActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), ContactInfoParser.getPhoneContacts(getActivity()));
        listView.setAdapter(adapter);
        listView.setFocusable(false);
//        Utility.setListViewHeightBasedOnChildren(listView);
    }


    public static void main(String arg[]) {
       Pattern pattern = Pattern.compile("\\d?");

       p("a123a".replaceAll("\\d+", "0"));
       p("a123a".replaceAll("\\d?", "0"));
       p("a123a".replaceAll("\\d*", "0"));
       p("a123a".replaceAll("\\d", "0"));
       p("a123a".replaceAll("[1,9]", "0"));
       p("a123a".replaceAll("[1,9]*", "0"));
       p("a123a".replaceAll("\\d*\\d", "0"));
       p("a123a".matches("\\D+"));
       p("12".matches("\\d*\\.?\\d*"));
       p("a123a".replaceAll("[^1\\d]{1}", "0"));
       p(" \n".matches("\\s*\\n"));
        p("18597608334".matches("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}"));
   }

    private static void p(Object str) {
        System.out.println(str);
    }
}

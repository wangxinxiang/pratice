package com.example.administrator.myapplication;

public class MainActivity {

    static {
        System.loadLibrary("hello");
    }

        public native String getStringFromNative();

}

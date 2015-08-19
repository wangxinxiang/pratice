package com.example.practice.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/15.
 */
public class GetAllAppService {

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager packageManager = context.getPackageManager();
        //��ȡ�ֻ�������Ӧ��
        List<PackageInfo> paklist = packageManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            //�ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ��  (����0ΪϵͳԤװӦ�ã�С�ڵ���0Ϊ��ϵͳӦ��)
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(pak);
            }
        }
        return apps;
    }
}

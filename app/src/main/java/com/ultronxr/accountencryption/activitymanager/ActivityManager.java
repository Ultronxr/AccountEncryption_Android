package com.ultronxr.accountencryption.activitymanager;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ActivityManager extends Application {

    //建立链表集合
    private static List<Activity> AMActivityList = new LinkedList<Activity>();

    private ActivityManager() {}

    //向链表中，添加Activity
    public static void addActivity(Activity activity) {
        AMActivityList.add(activity);
    }

    //结束整个应用程序
    public static void exit() {

        //遍历 链表，依次杀掉各个Activity
        for (Activity activity : AMActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        //杀掉，这个应用程序的进程，释放 内存
        int id = android.os.Process.myPid();
        if (id != 0) {
            android.os.Process.killProcess(id);
        }
    }

}

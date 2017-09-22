package com.google.zxing.client.android.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.zxing.client.android.MainActivity;
import com.google.zxing.client.android.zxing.activity.CaptureActivity;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
       // System.out.println("Throwable:"+ex);

        new Thread() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                    AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }.start();


    }


}
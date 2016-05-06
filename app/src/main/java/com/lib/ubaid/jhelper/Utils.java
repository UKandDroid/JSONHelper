package com.lib.ubaid.jhelper;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

/**
 * Created by Ubaid on 22/04/2016.
 */
public class Utils {
    private long iStartTime;                 // Timer variable
    private String strTimerTag;

    // INTERFACE - callback for code run on UI thread
    interface UIThread{
        public void execute();
    }
    // METHOD - Convert pixels to dp
    public static int pxToDp(Context con, int iPixels){
        DisplayMetrics displayMetrics = con.getResources().getDisplayMetrics();
        int dp = Math.round(iPixels / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
    // METHOD - Convert dp to pixels
    public static int dpToPx(Context con, int dp){
        Resources r =  con.getResources();
        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    // METHOD - runs code on main thread, use for updating UI from non-UI thread
    public static void updateUI(final UIThread code){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post( new Runnable() { @Override public void run() { code.execute();}});
    }
    // METHOD - executes delayed code on Main thread
    public static void updateDelayedUI(long iTime, final UIThread code){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                code.execute();
            }
        }, iTime);
    }
    // METHOD - executes delayed code
    public static void postDelayed(long iTime, final UIThread code){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                code.execute();
            }
        }, iTime);
    }

    // METHOD - sleep thread
    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {  e.printStackTrace();}
    }
    // METHOD - Starts timer
    public void startTimer(String strTag){
        strTimerTag = strTag;
        iStartTime = System.currentTimeMillis();
    }
    // METHOD - stops timer and returns time difference in millis
    public String stopTimer(){
        return  strTimerTag +" Time: " + (System.currentTimeMillis() - iStartTime)+"ms" ;
    }

}

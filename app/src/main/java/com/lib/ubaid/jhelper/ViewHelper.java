package com.lib.ubaid.jhelper;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ubaid on 15/04/2016.
 */
public class ViewHelper {
    private View rootView;
    private long iStartTime;                 // Timer variable
    private String strTimerTag;
    private int[] arId = new int[256];   // Store loaded arView arId, so they can checked against.
    private View[] arView = new View[256];// keeps reference of loaded views, so they are not loaded again..

    // INTERFACE - callback for code run on UI thread
    interface UIThread{
        public void update();
    }
    // CONSTRUCTORS
    public ViewHelper() {}
    public ViewHelper(View v){
        rootView = v;
    }
    public void setRootView(View v){
        rootView = v;
    }
    // METHODS - returns arView based on type
    public TextView textView(int id){ return (TextView)getView(id); }
    public EditText editText(int id){ return (EditText)getView(id); }
    public Button button(int id){ return (Button)getView(id);}
    // METHOD - sets Background for a arView
    public void setBackground(int id, int resource){
        getView(id).setBackgroundResource(resource);
    }
    // METHOD - sets visibility of view to Visible
    public void visibilityShow(int id){
        getView(id).setVisibility(View.VISIBLE);
    }
    // METHOD - sets visibility of view to InVisible
    public void visibilityHide(int id){
        getView(id).setVisibility(View.INVISIBLE);
    }
    // METHOD - sets visibility of view to InVisible
    public void visibilityGone(int id){
        getView(id).setVisibility(View.GONE);
    }
    // METHOD - sets visibility
    public void setVisibility(int id, int visibility){
        getView(id).setVisibility(visibility);
    }
    // METHOD - sets text for Button, TextView, EditText
    public void setViewText(int id, String sText){
        View  view = getView(id);
        if(view instanceof TextView) { // Button, EditText extends TextView
            ((TextView) view).setText(sText);
        }
    }
    // METHOD - returns text for Button, TextView, EditText
    public String getViewText(int id){
        View  view = getView(id);
       if(view instanceof TextView) { // Button, EditText extends TextView
           return ((TextView) view).getText().toString();
       }
        return "Wrong view type";
    }
    // METHOD - sets onClickListener
    public void setOnClickListener(int id, View.OnClickListener listener){
        getView(id).setOnClickListener(listener);
    }
    // METHOD - sets Tag for a arView
    public void setTag(int id, String tag){
        getView(id).setTag(tag);
    }
    // METHOD - returns Tag for a views
    public String getTag(int id){
        return (String)getView(id).getTag();
    }

    // METHOD - Returns view either from saved arView or by findViewById() method
    private  View getView(int id){
        byte index = (byte)id;
        if(arId[index] != id) {
            arId[index] = id;
            arView[index] = rootView.findViewById(id);
        }
        return arView[index];
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
    // METHOD - runs code on main thread, use for updating UI from non-UI thread
    public static void updateUI(Context context, final UIThread obj){
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post( new Runnable() { @Override public void run() { obj.update();}});
    }
    // METHOD - sleep thread
    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {  e.printStackTrace();}
    }

}

package com.github.faceit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * FaceIt
 * Created by robinonsay on 9/17/16.
 */
public class FaceItIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private static final String TAG = FaceItIME.class.getSimpleName();
    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;
    private LocalBroadcastManager localBroadcast;
    private InputConnection inputConnection;

    private boolean isFinished = false;
    private InputMethodManager imm;

    @Override
    public View onCreateInputView() {
//        return super.onCreateInputView();
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcast = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcast.registerReceiver(broadcastReceiver, new IntentFilter("faceit"));
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        inputConnection = getCurrentInputConnection();
        if(message != null){
            inputConnection.commitText(message, 1);
            message = null;
        }
    }

    @Override
    public void onFinishInput() {
        if(isFinished){
            Log.d(TAG, "onFinishInput: FINISH INPUT");
            super.onFinishInput();
            isFinished = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: DESTROY");
        localBroadcast.unregisterReceiver(broadcastReceiver);
    }


    private String message;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            message = intent.getStringExtra("message");
            imm.showSoftInput(FaceItIME.this.getWindow().getWindow().getCurrentFocus(), InputMethodManager.SHOW_FORCED);
            isFinished = true;
        }
    };

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch(primaryCode){
            case 0:
                Intent i = new Intent(this, CameraOpenActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
//                inputConnection.commitText("Hello World", 1);
                break;
            case 1:
                try {

                    final IBinder token = this.getWindow().getWindow().getAttributes().token;
                    //imm.setInputMethod(token, LATIN);
                    imm.switchToLastInputMethod(token);
                } catch (Throwable t) { // java.lang.NoSuchMethodError if API_level<11
                    Log.e(TAG,"cannot set the previous input method:");
                    t.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {
        Log.d(TAG, "onText: TEXT CALLED");

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}

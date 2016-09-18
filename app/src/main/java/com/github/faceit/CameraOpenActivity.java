package com.github.faceit;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraOpenActivity extends Activity{
    private static final String TAG = CameraOpenActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchTakeVideoIntent();
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            Log.d(TAG, "onActivityResult: " + videoUri.getPath());
            Intent intent = new Intent("faceit");
            intent.putExtra("message", "This is my message! " + videoUri.getPath());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            finish();
        }
    }
}

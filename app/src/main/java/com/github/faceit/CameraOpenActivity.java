package com.github.faceit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class CameraOpenActivity extends Activity{
    private static final String TAG = CameraOpenActivity.class.getSimpleName();

        static MediaMetadataRetriever retriever = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchTakeVideoIntent();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {


        File mediaFile =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myPhoto.jpg");


        Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri videoUri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myPhoto.jpg");

        Log.d(TAG, videoUri.getPath());
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) { takeVideoIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1); } else { takeVideoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1); }
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takeVideoIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    public static  Bitmap crupAndScale (Bitmap source,int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "myPhoto.jpg");
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = crupAndScale(bitmap, 300); // if you mind scaling
                Log.d(TAG, String.valueOf(bitmap));
                Intent intent = new Intent("faceit");
                intent.putExtra("message", "This is my message! " + bitmap);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                finish();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {



            }


//            File f = new File(videoUri.getPath());
//            Log.d(TAG, "onActivityResult: " + f.exists());
//            FFmpegMediaMetadataRetriever retriever = new  FFmpegMediaMetadataRetriever();
//            try {
//                Bitmap bmp = null;
//                retriever.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()
//                                + "/myvideo.mp4");
//                retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE);
//                bmp = retriever.getFrameAtTime(111,
//                        FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
////                int videoHeight = (int) (bmp.getHeight()*((float)getIntWidth()/bmp.getWidth()));
//                Log.d(TAG, "image: " + bmp);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            retriever.release();
//
//            Bitmap bitmap = null;
//            try {
//
//             retriever = new MediaMetadataRetriever();
//            if (Build.VERSION.SDK_INT >= 14)
//                retriever.setDataSource(videoUri.getPath(), new HashMap<String, String>());
//            else
//                retriever.setDataSource(videoUri.getPath());
//            //   mediaMetadataRetriever.setDataSource(videoPath);
//            bitmap = retriever.getFrameAtTime();
//                Log.d(TAG, "AB " + bitmap);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (retriever != null)
//            {
//                retriever.release();
//            }
//        }
//


        }

    }
}

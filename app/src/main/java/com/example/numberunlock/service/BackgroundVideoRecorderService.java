package com.example.numberunlock.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class BackgroundVideoRecorderService extends Service implements SurfaceHolder.Callback {

    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;

    public class BgVideoRecorderBinder extends Binder {
        public BackgroundVideoRecorderService getService() {
            return BackgroundVideoRecorderService.this;
        }
    }

    @Override
    public void onCreate() {
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Background Video Recorder")
                .setContentText("")
                .build();
        startForeground(1234, notification);

        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1, 1,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BgVideoRecorderBinder();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        System.out.println("onCreate()");

        System.out.println("Camera Start");
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        System.out.println("Support:" + camera.getParameters());
        Camera.Parameters parameters = camera.getParameters();
        camera.setParameters(parameters);
        // 设置录制视频的角度
        camera.setDisplayOrientation(90);

        // unlock 一定要在实例化MediaRecorder之前，这是天坑！
        camera.unlock();
        mediaRecorder = new MediaRecorder();

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));

        mediaRecorder.setOutputFile(
                Environment.getExternalStorageDirectory() + "/NumberUnlock/" +
                        DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) +
                        ".mp4"
        );
        // 设置输出视频的播放角度
        mediaRecorder.setOrientationHint(270);

        try {
            mediaRecorder.prepare();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder.start();


//        System.out.println("Camera Start");
//        camera = Camera.open();
//        mediaRecorder = new MediaRecorder();
//        camera.unlock();
//
//        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
//        mediaRecorder.setCamera(camera);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//
//        mediaRecorder.setOutputFile(
//                Environment.getExternalStorageDirectory() + "/" +
//                        DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) +
//                        ".mp4"
//        );
//
//        try {
//            mediaRecorder.prepare();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        mediaRecorder.start();
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        camera.lock();
        camera.release();

        windowManager.removeView(surfaceView);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}

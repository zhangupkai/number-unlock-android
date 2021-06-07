package com.example.numberunlock.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.numberunlock.panel.NumLockPanel;
import com.example.numberunlock.R;
import com.example.numberunlock.service.BackgroundVideoRecorderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BackgroundVideoRecorderService backgroundVideoRecorderService;
    private NumLockPanel numLockPanel;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundVideoRecorderService = ((BackgroundVideoRecorderService.BgVideoRecorderBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String correctPwd = getIntent().getStringExtra("inputPwd");
        String correctForce = getIntent().getStringExtra("inputForce");
        int heavyCount = getIntent().getIntExtra("heavyCount", 0);

        numLockPanel = findViewById(R.id.num_lock);
        numLockPanel.setInputListener(new NumLockPanel.InputListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
//            public void inputFinish(String resultPwd, String resultForce, List<Long> durationList) {
            public void inputFinish(String resultPwd, String resultForce) {

//                float sumDuration = 0;
//                for (Long duration : durationList) {
//                    sumDuration += duration;
//                }
//                float avgDuration = sumDuration / 4;
//                System.out.println("avg: " + avgDuration);
//
//                StringBuilder resultForceBuilder = new StringBuilder();
//                for (Long duration: durationList) {
//                    // 如果当前按压时长大于平均按压时长的50%，则认为其是重按
//                    if ((duration - avgDuration) / avgDuration > 0.5){
//                        resultForceBuilder.append("1");
//                    }
//                    else {
//                        resultForceBuilder.append("0");
//                    }
//                }
//                resultForce = resultForceBuilder.toString();
//                System.out.println("resultForceAfterCal: " + resultForce);

                if (resultPwd.equals(correctPwd) && resultForce.equals(correctForce)) {
                    Toast.makeText(MainActivity.this, R.string.prompt_info_correct_password, Toast.LENGTH_LONG).show();
                    numLockPanel.showCorrectStatus();
                }
                else {
                    // result为输入结果
                    Toast.makeText(MainActivity.this, resultPwd + ", " + resultForce, Toast.LENGTH_SHORT).show();
                    // 错误显示示例
                    numLockPanel.showErrorStatus();
                }
            }
        });

        Button button = (Button) findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });

        //
//        Button enterEyeButton = (Button) findViewById(R.id.enter_eye_button);
//        enterEyeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Enter Eye", Toast.LENGTH_LONG).show();
//
//                // 绑定Service
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, BackgroundVideoRecorderService.class);
//                bindService(intent, connection, Context.BIND_AUTO_CREATE);
//            }
//        });
//
//        Button exitEyeButton = (Button) findViewById(R.id.exit_eye_button);
//        exitEyeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Exit Eye", Toast.LENGTH_LONG).show();
//                unbindService(connection);
//            }
//        });
    }

}
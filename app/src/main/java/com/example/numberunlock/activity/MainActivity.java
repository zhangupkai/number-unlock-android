package com.example.numberunlock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.numberunlock.panel.NumLockPanel;
import com.example.numberunlock.R;
import com.example.numberunlock.service.BackgroundVideoRecorderService;

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

        numLockPanel = findViewById(R.id.num_lock);
        numLockPanel.setInputListener(new NumLockPanel.InputListener() {
            @Override
            public void inputFinish(String resultPwd, String resultForce) {
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
        Button enterEyeButton = (Button) findViewById(R.id.enter_eye_button);
        enterEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Enter Eye", Toast.LENGTH_LONG).show();

                // 绑定Service
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BackgroundVideoRecorderService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });

        Button exitEyeButton = (Button) findViewById(R.id.exit_eye_button);
        exitEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Exit Eye", Toast.LENGTH_LONG).show();
                unbindService(connection);
            }
        });
    }

}
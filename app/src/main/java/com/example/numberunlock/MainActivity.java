package com.example.numberunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NumLockPanel numLockPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        String correctPwd = getIntent().getStringExtra("inputPwd");
        String correctForce = getIntent().getStringExtra("inputForce");

        numLockPanel = (NumLockPanel) findViewById(R.id.num_lock);
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
    }
}
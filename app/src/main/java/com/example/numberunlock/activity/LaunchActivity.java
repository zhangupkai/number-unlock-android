package com.example.numberunlock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.numberunlock.R;
import com.example.numberunlock.panel.SetPasswordPanel;
import com.example.numberunlock.Utils;

public class LaunchActivity extends AppCompatActivity {

    private String inputPwd;
    private String inputForce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        SetPasswordPanel setPasswordPanel = findViewById(R.id.set_password);
        setPasswordPanel.setInputListener(new SetPasswordPanel.InputListener() {
            @Override
            public void inputFinish(String resultPwd, String resultForce, String duration) {
                inputPwd = resultPwd;
                inputForce = resultForce;
                Toast.makeText(LaunchActivity.this, inputPwd + ", " + inputForce, Toast.LENGTH_LONG).show();
                Utils.saveDataToFile(LaunchActivity.this, duration + "\r\n", "data");
            }
        });

        Button button = (Button) findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((inputPwd == null || inputPwd.length() < 4) || (inputForce == null || inputForce.length() < 4)) {
                    Toast.makeText(LaunchActivity.this, R.string.prompt_info_less_four, Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(LaunchActivity.this, MainActivity.class);
                    intent.putExtra("inputPwd", inputPwd);
                    intent.putExtra("inputForce", inputForce);
                    startActivity(intent);
                }
            }
        });
    }


}
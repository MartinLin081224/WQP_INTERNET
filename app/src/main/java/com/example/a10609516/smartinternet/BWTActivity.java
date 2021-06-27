package com.example.a10609516.smartinternet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a10609516.smartinternet.Tools.WQPToolsActivity;

public class BWTActivity extends WQPToolsActivity {
    private Button login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bwt);
        //動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction(){
        login_btn = (Button) findViewById(R.id.login_btn);
        register_btn = (Button) findViewById(R.id.register_btn);
    }

    /**
     *按壓事件監聽器
     */
    private void OnClickListener(){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(BWTActivity.this, LoginActivity.class);
                startActivity(login_intent);

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(BWTActivity.this, AccountActivity.class);
                startActivity(register_intent);

            }
        });
    }
}

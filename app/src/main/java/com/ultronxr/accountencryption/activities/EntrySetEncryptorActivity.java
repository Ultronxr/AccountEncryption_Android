package com.ultronxr.accountencryption.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;

public class EntrySetEncryptorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(EntrySetEncryptorActivity.this);

        setContentView(R.layout.entry_set_encryptor);

        Button btOk = findViewById(R.id.btOK),
                btCancel = findViewById(R.id.btCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inputEncPwdIntent = new Intent(EntrySetEncryptorActivity.this, MainActivity.class);
                startActivity(inputEncPwdIntent);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.exit();
            }
        });





    }

    //ToolBar绑定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //ToolBar事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            ActivityManager.exit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //返回按钮事件
    @Override
    public void onBackPressed(){
        ActivityManager.exit();
    }

}

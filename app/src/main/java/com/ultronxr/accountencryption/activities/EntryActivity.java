package com.ultronxr.accountencryption.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.global.Global;
import com.ultronxr.accountencryption.utils.MD5Hash;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;

import java.io.File;
import java.util.regex.Pattern;

public class EntryActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(EntryActivity.this);
        setContentView(R.layout.entry);

        //检查并申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);

        //Environment.getExternalStorageDirectory() ==> /storage/emulated/0
        File file = new File("/storage/emulated/0/Android/data/com.ultronxr.accountencryption/db/");
        if(!file.exists()) file.mkdirs();

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();

        Global.encryptor = sqLiteHelper.queryEncryptor(db);
        Log.v("数据库中的密码：", Global.encryptor);

        final TextView tvPwd1 = findViewById(R.id.pwd1);
        Button btOk = findViewById(R.id.btOK), btCancel = findViewById(R.id.btCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = tvPwd1.getText().toString();
                String msg = "";

                if(pwd1.equals(""))
                    msg = "请输入密码";
                else if(!Pattern.matches("^[A-Za-z0-9~!@#$%^&*()_]{4,20}$", pwd1))
                    msg = "密码格式错误";
                else if(!Global.encryptor.equals(MD5Hash.stringToMd5LowerCase(pwd1)))
                    msg = "密码错误";

                if(!msg.equals(""))
                    Toast.makeText(EntryActivity.this, msg, Toast.LENGTH_SHORT).show();
                else {
                    Intent mainIntent = new Intent(EntryActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.exit();
            }
        });



        if(sqLiteHelper.queryEncryptor(db).equals(""))
            startActivity(new Intent(EntryActivity.this, EntrySetEncryptorActivity.class));


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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 111 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EntryActivity.this, "READ_EXTERNAL_STORAGE权限申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityManager.exit();
                }
            }
            case 222 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EntryActivity.this, "WRITE_EXTERNAL_STORAGE权限申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityManager.exit();
                }
            }
        }
    }

}
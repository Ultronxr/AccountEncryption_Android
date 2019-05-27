package com.ultronxr.accountencryption.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorSpace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.utils.MD5Hash;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;
import com.ultronxr.accountencryption.utils.db.bean.Encryptor;

import java.util.regex.Pattern;

public class EntrySetEncryptorActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(EntrySetEncryptorActivity.this);
        setContentView(R.layout.entry_set_encryptor);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();

        final TextView tvPwd1 = findViewById(R.id.pwd1), tvPwd2 = findViewById(R.id.pwd2);
        Button btOk = findViewById(R.id.btOK), btCancel = findViewById(R.id.btCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = tvPwd1.getText().toString(), pwd2 = tvPwd2.getText().toString();
                String msg = "";

                if(pwd1.equals(""))
                    msg = "请输入密码";
                else if(pwd2.equals(""))
                    msg = "请重复输入密码";
                else if(!Pattern.matches("^[A-Za-z0-9~!@#$%^&*()_]{4,20}$", pwd1))
                    msg = "密码格式错误";
                else if(!pwd1.equals(pwd2))
                    msg = "两次输入的密码不一致";

                if(!msg.equals(""))
                    Toast.makeText(EntrySetEncryptorActivity.this, msg, Toast.LENGTH_SHORT).show();
                else{
                    sqLiteHelper.replaceEncryptor(db, new Encryptor(MD5Hash.stringToMd5LowerCase(pwd1)));
                    Intent mainIntent = new Intent(EntrySetEncryptorActivity.this, MainActivity.class);
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

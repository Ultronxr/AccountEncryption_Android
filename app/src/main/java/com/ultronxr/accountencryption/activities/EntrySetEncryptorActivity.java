package com.ultronxr.accountencryption.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.global.Global;
import com.ultronxr.accountencryption.utils.MD5Hash;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;
import com.ultronxr.accountencryption.utils.db.bean.Encryptor;
import com.ultronxr.accountencryption.utils.encrypt.AES128ECBPKCS5;

import java.util.regex.Pattern;

public class EntrySetEncryptorActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_set_encryptor);

        ActivityManager.addActivity(EntrySetEncryptorActivity.this);
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();

        final EditText etPwd1 = findViewById(R.id.pwd1), etPwd2 = findViewById(R.id.pwd2);
        Button btOk = findViewById(R.id.btOK), btCancel = findViewById(R.id.btCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = etPwd1.getText().toString(), pwd2 = etPwd2.getText().toString();
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
                    Global.encryptor = pwd1;
                    AES128ECBPKCS5.setSecretKey(pwd1);
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

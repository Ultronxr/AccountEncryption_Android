package com.ultronxr.accountencryption.activities;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;

public class ListItemActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        ActivityManager.addActivity(ListItemActivity.this);
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();



    }

    //返回按钮事件
    @Override
    public void onBackPressed() {
        //返回时结束该Activity实例，防止点击很多次ListItem之后创建很多实例浪费资源
        ListItemActivity.this.finish();
    }

}

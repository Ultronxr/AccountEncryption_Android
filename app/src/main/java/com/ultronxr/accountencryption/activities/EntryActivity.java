package com.ultronxr.accountencryption.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;

public class EntryActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(EntryActivity.this);
        setContentView(R.layout.entry);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getReadableDatabase();

        Button btOk = findViewById(R.id.btOK),
                btCancel = findViewById(R.id.btCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(mainIntent);
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

}

package com.ultronxr.accountencryption.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.global.Global;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long exitTime = 0;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    ArrayList<Map<String, Object>> item_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager.addActivity(MainActivity.this);
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //悬浮添加按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


            }
        });

        //侧边栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //读取数据库中所有的Records信息
        Global.records = sqLiteHelper.queryRecords(db, null);
        Log.v("Global.records内容：", Global.records.toString());

        //获取需要显示的信息，用simpleAdapter显示在ListView中
//        for(int i = 0; i < Global.records.size(); i++){
//            Map<String, Object> item = new HashMap<>();
//            item.put("id", Global.records.get(i).getId());
//            item.put("account_name", Global.records.get(i).getAccount_name());
//            item.put("account_num", Global.records.get(i).getAccount_num());
//            item.put("category", Global.records.get(i).getCategory());
//            item_list.add(item);
//        }
        for(int i = 0; i < 50; i++){
            Map<String, Object> item = new HashMap<>();
            item.put("id", i);
            item.put("account_name", "GitHub账号");
            item.put("account_num", "15257845702");
            item.put("category", "编程");
            item_list.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,
                                                        item_list,
                                                        R.layout.item_in_list,
                                                        new String[]{"account_name","account_num","category"},
                                                        new int[]{R.id.item_account_name, R.id.item_account_num, R.id.item_category});
        ListView listView = findViewById(R.id.main_record_list);
        listView.setAdapter(simpleAdapter);


    }

    //返回按钮事件
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if((System.currentTimeMillis() - exitTime) > 1500){
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                this.exitTime = System.currentTimeMillis();
            }

            else
                ActivityManager.exit();
        }
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

    //滑动侧边栏事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_update_encryptor){

        }
        else if(id == R.id.nav_clear_all){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

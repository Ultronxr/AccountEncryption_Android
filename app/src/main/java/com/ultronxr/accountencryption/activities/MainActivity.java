package com.ultronxr.accountencryption.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.global.Global;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;
import com.ultronxr.accountencryption.utils.db.bean.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long exitTime = 0;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    private ListView listView;
    private SimpleAdapter itemAdapter, categoryAdapter;
    //对应所有数据的列表、搜索之后的列表，分类之后的列表
    ArrayList<Map<String, Object>> allItemList = new ArrayList<>(), searchItemList = new ArrayList<>(), categoryItemList = new ArrayList<>();

    private SearchView searchView;

    private AlertDialog.Builder addDialog;
    private Map<String, EditText> mapEditTexts = new HashMap<>();
    private Map<String, String> mapEditTextTexts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager.addActivity(MainActivity.this);
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        db = sqLiteHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        listView = findViewById(R.id.main_record_list);
        searchView = findViewById(R.id.main_search);


        //设置新增记录弹框中的所有EditText
        //必须要写LayoutInflater，否则获取不到输入的数据
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View addDialogView = inflater.inflate(R.layout.add_dialog, null);
        mapEditTexts.put("category", (EditText)addDialogView.findViewById(R.id.add_category));
        mapEditTexts.put("accountName", (EditText)addDialogView.findViewById(R.id.add_account_name));
        mapEditTexts.put("accountNum", (EditText)addDialogView.findViewById(R.id.add_account_num));
        mapEditTexts.put("accountPwd", (EditText)addDialogView.findViewById(R.id.add_account_pwd));
        mapEditTexts.put("nick", (EditText)addDialogView.findViewById(R.id.add_nick));
        mapEditTexts.put("email", (EditText)addDialogView.findViewById(R.id.add_email));
        mapEditTexts.put("phone", (EditText)addDialogView.findViewById(R.id.add_phone));
        mapEditTexts.put("url", (EditText)addDialogView.findViewById(R.id.add_url));
        mapEditTexts.put("securityProblem", (EditText)addDialogView.findViewById(R.id.add_security_problem));
        mapEditTexts.put("securityAnswer", (EditText)addDialogView.findViewById(R.id.add_security_answer));
        mapEditTexts.put("note", (EditText)addDialogView.findViewById(R.id.add_note));

        //新建弹框，获取EditText中输入的数据
        addDialog = new AlertDialog.Builder(MainActivity.this);
        addDialog.setTitle("添加记录");
        addDialog.setView(addDialogView);
        addDialog.setNegativeButton("取消", null);
        addDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapEditTextTexts.clear();
                for(Map.Entry<String, EditText> entry : mapEditTexts.entrySet())
                    mapEditTextTexts.put(entry.getKey(), entry.getValue().getText().toString());
                if(addRecordToDB(mapEditTextTexts)){ //如果新增了记录，则刷新列表
                    Global.records = sqLiteHelper.queryRecord(db, null);
                    allItemList.clear();
                    for(int i = 0; i < Global.records.size(); i++){
                        Map<String, Object> item = new HashMap<>();
                        item.put("index", i);
                        item.put("id", Global.records.get(i).getId());
                        item.put("accountName", Global.records.get(i).getAccountName());
                        item.put("accountNum", Global.records.get(i).getAccountNum());
                        item.put("category", Global.records.get(i).getCategory());
                        allItemList.add(item);
                    }
                    setItemAdapter("ALL");
                }
            }
        });


        //侧边栏
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //悬浮添加按钮fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止出现The specified child already has a parent. You must call removeView() on the child's parent first.的报错
                ViewGroup parentViewGroup = (ViewGroup) addDialogView.getParent();
                if(parentViewGroup != null)
                    parentViewGroup.removeView(addDialogView);

                for(Map.Entry<String, EditText> entry : mapEditTexts.entrySet())
                    entry.getValue().setText("");
                addDialog.show();
            }
        });


        //ListView内部的Item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 应该让搜索框输入状态的点击动作不触发跳转ListItemActivity，提升操作体验，有空再写


                Intent listItemIntent = new Intent(MainActivity.this, ListItemActivity.class);
                startActivity(listItemIntent);
            }
        });
        //长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "长按了", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        //读取数据库中所有的Records信息
        Global.records = sqLiteHelper.queryRecord(db, null);
        //Log.v("Global.records内容：", Global.records.toString());

        //获取需要显示的信息，用SimpleAdapter显示在ListView中
        allItemList.clear();
        for(int i = 0; i < Global.records.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("index", i); //index用于操作数组
            item.put("id", Global.records.get(i).getId()); //id用于操作数据库
            item.put("accountName", Global.records.get(i).getAccountName());
            item.put("accountNum", Global.records.get(i).getAccountNum());
            item.put("category", Global.records.get(i).getCategory());
            allItemList.add(item);
        }
        setItemAdapter("ALL");


        //搜索栏监听事件
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    Toast.makeText(MainActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }
                else {
                    searchItemList.clear();
                    for(int i = 0; i < Global.records.size(); i++){
                        //查找范围为账户名、账号
                        if(Global.records.get(i).getAccountName().contains(query) ||
                                Global.records.get(i).getAccountNum().contains(query)){
                            Map<String, Object> item = new HashMap<>();
                            item.put("index", i);
                            item.put("id", Global.records.get(i).getId());
                            item.put("accountName", Global.records.get(i).getAccountName());
                            item.put("accountNum", Global.records.get(i).getAccountNum());
                            item.put("category", Global.records.get(i).getCategory());
                            searchItemList.add(item);
                        }
                    }
                    setItemAdapter("SEARCH");
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    setItemAdapter("ALL");
                }
                return true;
            }
        });


    }

    //全局Activity点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP : {
                searchView.clearFocus(); //移除SearchView的焦点
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
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

    //检查当前软键盘是否正在显示
    private boolean isKeybaordShowing(){
        int screenHeight = getWindow().getDecorView().getHeight();
        // 获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight > rect.bottom;
    }

    private void setItemAdapter(String str){
        if(str.equals("ALL"))
            itemAdapter = new SimpleAdapter(MainActivity.this,
                    allItemList,
                    R.layout.item_in_list,
                    new String[]{"accountName","accountNum","category"},
                    new int[]{R.id.item_account_name, R.id.item_account_num, R.id.item_category});
        else if(str.equals("SEARCH"))
            itemAdapter = new SimpleAdapter(MainActivity.this,
                    searchItemList,
                    R.layout.item_in_list,
                    new String[]{"accountName","accountNum","category"},
                    new int[]{R.id.item_account_name, R.id.item_account_num, R.id.item_category});
        listView.setAdapter(itemAdapter);
    }

    private boolean addRecordToDB(Map<String, String> map){
        String category = map.get("category"), accountName = map.get("accountName"), accountNum = map.get("accountNum"),
                accountPwd = map.get("accountPwd"), nick = map.get("accountPwd"), email = map.get("accountPwd"),
                phone = map.get("accountPwd"), url = map.get("accountPwd"), securityProblem = map.get("accountPwd"),
                securityAnswer = map.get("accountPwd"), note = map.get("accountPwd");

        if(accountName == null || accountName.equals("")){
            Toast.makeText(MainActivity.this, "请输入账户名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(category == null || category.equals(""))
            category = "未分类";

        Record record = new Record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                category, accountName, accountNum, accountPwd, nick, email, phone, url, securityProblem, securityAnswer, note);
        sqLiteHelper.insertRecord(db, record);

        return true;
    }


}

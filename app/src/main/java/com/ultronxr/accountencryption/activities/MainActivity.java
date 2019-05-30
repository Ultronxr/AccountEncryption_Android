package com.ultronxr.accountencryption.activities;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ultronxr.accountencryption.R;
import com.ultronxr.accountencryption.activitymanager.ActivityManager;
import com.ultronxr.accountencryption.global.Global;
import com.ultronxr.accountencryption.utils.db.SQLiteHelper;
import com.ultronxr.accountencryption.utils.db.bean.Record;
import com.ultronxr.accountencryption.utils.encrypt.AES128ECBPKCS5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long exitTime = 0;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    private ListView listView; //主列表
    private SimpleAdapter itemAdapter, categoryAdapter; //所有数据列表/搜索数据列表Adapter，分类数据列表Adapter
    ArrayList<Map<String, Object>> allItemList = new ArrayList<>(), searchItemList = new ArrayList<>(), categoryItemList = new ArrayList<>();

    private SearchView searchView; //搜索框
    private String searchQuery; //搜索内容
    boolean isSearching = false; //标识是否正在搜索，用来在搜索之后区分allItemList和searchItemList列表

    private LayoutInflater inflater; //设置弹框用的

    private AlertDialog.Builder addDialog; //弹框一：增加记录的弹框
    private Map<String, EditText> mapAddEditTexts = new HashMap<>(); //增加记录弹框中的所有EditText控件
    private Map<String, String> mapAddEditTextTexts = new HashMap<>(); //增加记录弹框中的所有EditText控件的文字内容
    private AlertDialog.Builder updateDialog; //弹框二：修改记录的弹框
    private Map<String, EditText> mapUpdateEditTexts = new HashMap<>();
    private Map<String, String> mapUpdateEditTextTexts = new HashMap<>();
    private AlertDialog.Builder showEncryptedDialog; //弹框三：显示加密信息的弹框

    private int updateIndex = -1;

    final String[] longClickMenuItems = new String[]{"显示加密的原数据", "删除"};
    AlertDialog.Builder longClickMenu; //长按某一项记录的弹框
    AlertDialog.Builder deleteEnsureMenu; //提示是否确认删除的弹框

    private List<String> drawerItemList = new ArrayList<>(); //侧边栏中显示的所有分类名称

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


        //初始化三个弹框
        inflater = LayoutInflater.from(MainActivity.this);
        final View addDialogView = inflater.inflate(R.layout.dialog_add_record, null);
        initAddAlertDialog(addDialogView);
        final View updateDialogView = inflater.inflate(R.layout.dialog_update_record, null);
        initUpdateAlertDialog(updateDialogView);
        final View showEncryptedDialogView = inflater.inflate(R.layout.dialog_show_encrypted_record, null);
        initShowEncryptedAlertDialog(showEncryptedDialogView);


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

                for(Map.Entry<String, EditText> entry : mapAddEditTexts.entrySet())
                    entry.getValue().setText("");
                addDialog.show();
            }
        });


        //ListView内部的Item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup parentViewGroup = (ViewGroup) updateDialogView.getParent();
                if(parentViewGroup != null)
                    parentViewGroup.removeView(updateDialogView);

                updateIndex = (int)id;
                int trueIndex = updateIndex;
                if(isSearching){
                    int tempId = (int)(searchItemList.get(updateIndex).get("id"));
                    for(int i = 0; i < allItemList.size(); i++){
                        if((int)(allItemList.get(i).get("id")) == tempId){
                            trueIndex = i;
                            break;
                        }
                    }
                }

                ((EditText)updateDialogView.findViewById(R.id.update_category)).setText(Global.records.get(trueIndex).getCategory());
                ((EditText)updateDialogView.findViewById(R.id.update_account_name)).setText(Global.records.get(trueIndex).getAccountName());
                ((EditText)updateDialogView.findViewById(R.id.update_account_num)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getAccountNum()));
                ((EditText)updateDialogView.findViewById(R.id.update_account_pwd)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getAccountPwd()));
                ((EditText)updateDialogView.findViewById(R.id.update_nick)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getNick()));
                ((EditText)updateDialogView.findViewById(R.id.update_email)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getEmail()));
                ((EditText)updateDialogView.findViewById(R.id.update_phone)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getPhone()));
                ((EditText)updateDialogView.findViewById(R.id.update_url)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getUrl()));
                ((EditText)updateDialogView.findViewById(R.id.update_security_problem)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getSecurityProblem()));
                ((EditText)updateDialogView.findViewById(R.id.update_security_answer)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getSecurityAnswer()));
                ((EditText)updateDialogView.findViewById(R.id.update_note)).setText(AES128ECBPKCS5.decryptString(Global.records.get(trueIndex).getNote()));

                updateDialog.show();
            }
        });
        //长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = (int)id;
                longClickMenu = new AlertDialog.Builder(MainActivity.this);
                longClickMenu.setTitle("选项").setItems(longClickMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            ViewGroup parentViewGroup = (ViewGroup) showEncryptedDialogView.getParent();
                            if(parentViewGroup != null)
                                parentViewGroup.removeView(showEncryptedDialogView);

                            int trueIndex = index;
                            if(isSearching){
                                int tempId = (int)(searchItemList.get(index).get("id"));
                                for(int i = 0; i < allItemList.size(); i++){
                                    if((int)(allItemList.get(i).get("id")) == tempId){
                                        trueIndex = i;
                                        break;
                                    }
                                }
                            }
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_category)).setText(Global.records.get(trueIndex).getCategory());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_account_name)).setText(Global.records.get(trueIndex).getAccountName());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_account_num)).setText(Global.records.get(trueIndex).getAccountNum());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_account_pwd)).setText(Global.records.get(trueIndex).getAccountPwd());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_nick)).setText(Global.records.get(trueIndex).getNick());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_email)).setText(Global.records.get(trueIndex).getEmail());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_phone)).setText(Global.records.get(trueIndex).getPhone());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_url)).setText(Global.records.get(trueIndex).getUrl());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_security_problem)).setText(Global.records.get(trueIndex).getSecurityProblem());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_security_answer)).setText(Global.records.get(trueIndex).getSecurityAnswer());
                            ((EditText)showEncryptedDialogView.findViewById(R.id.show_encrypted_note)).setText(Global.records.get(trueIndex).getNote());
                            showEncryptedDialog.show();
                        }
                        else if(which == 1){
                            deleteEnsureMenu = new AlertDialog.Builder(MainActivity.this);
                            deleteEnsureMenu.setTitle("确认删除该记录？");
                            deleteEnsureMenu.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(deleteRecordToDB(index)){
                                        Global.records = sqLiteHelper.queryRecord(db, null);
                                        flushAllItemList();
                                        flushSearchItemList(searchQuery);
                                    }
                                }
                            });
                            deleteEnsureMenu.setNegativeButton("取消", null);
                            deleteEnsureMenu.show();
                        }
                    }
                });
                longClickMenu.show();
                return true;
            }
        });


        //读取数据库中所有的Records信息
        Global.records = sqLiteHelper.queryRecord(db, null);

        //获取需要显示的信息，用SimpleAdapter显示在ListView中
        flushAllItemList();


        //搜索栏监听事件
        searchView.setIconifiedByDefault(false);
        //TODO 清除搜索栏下划线
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    Toast.makeText(MainActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }
                else {
                    isSearching = true;
                    searchQuery = query;
                    flushSearchItemList(query);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    setItemAdapter("ALL");
                    isSearching = false;
                    searchQuery = "";
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

    //设置ListView的Adapter
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

    private void flushAllItemList(){
        allItemList.clear();
        for(int i = 0; i < Global.records.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("id", Global.records.get(i).getId()); //id用于操作数据库
            item.put("category", Global.records.get(i).getCategory());
            item.put("accountName", Global.records.get(i).getAccountName());
            item.put("accountNum", AES128ECBPKCS5.decryptString(Global.records.get(i).getAccountNum()));
            allItemList.add(item);
        }
        setItemAdapter("ALL");
    }

    private void flushSearchItemList(String query){
        searchItemList.clear();
        for(int i = 0; i < Global.records.size(); i++){
            //查找范围为账户名、账号、分类
            if(Global.records.get(i).getAccountName().contains(query) ||
                    AES128ECBPKCS5.decryptString(Global.records.get(i).getAccountNum()).contains(query) ||
                    Global.records.get(i).getCategory().contains(query)){
                Map<String, Object> item = new HashMap<>();
                item.put("id", Global.records.get(i).getId());
                item.put("category", Global.records.get(i).getCategory());
                item.put("accountName", Global.records.get(i).getAccountName());
                item.put("accountNum", AES128ECBPKCS5.decryptString(Global.records.get(i).getAccountNum()));
                searchItemList.add(item);
            }
        }
        setItemAdapter("SEARCH");
    }

    //初始化弹框一：增加记录的弹框
    private void initAddAlertDialog(final View addDialogView){
        //设置新增记录弹框中的所有EditText
        //必须要写LayoutInflater，否则获取不到输入的数据
        mapAddEditTexts.put("category", (EditText)addDialogView.findViewById(R.id.add_category));
        mapAddEditTexts.put("accountName", (EditText)addDialogView.findViewById(R.id.add_account_name));
        mapAddEditTexts.put("accountNum", (EditText)addDialogView.findViewById(R.id.add_account_num));
        mapAddEditTexts.put("accountPwd", (EditText)addDialogView.findViewById(R.id.add_account_pwd));
        mapAddEditTexts.put("nick", (EditText)addDialogView.findViewById(R.id.add_nick));
        mapAddEditTexts.put("email", (EditText)addDialogView.findViewById(R.id.add_email));
        mapAddEditTexts.put("phone", (EditText)addDialogView.findViewById(R.id.add_phone));
        mapAddEditTexts.put("url", (EditText)addDialogView.findViewById(R.id.add_url));
        mapAddEditTexts.put("securityProblem", (EditText)addDialogView.findViewById(R.id.add_security_problem));
        mapAddEditTexts.put("securityAnswer", (EditText)addDialogView.findViewById(R.id.add_security_answer));
        mapAddEditTexts.put("note", (EditText)addDialogView.findViewById(R.id.add_note));

        addDialog = new AlertDialog.Builder(MainActivity.this);
        addDialog.setTitle("添加记录");
        addDialog.setView(addDialogView);
        addDialog.setNegativeButton("取消", null);
        addDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapAddEditTextTexts.clear();
                for(Map.Entry<String, EditText> entry : mapAddEditTexts.entrySet())
                    mapAddEditTextTexts.put(entry.getKey(), entry.getValue().getText().toString());
                if(insertRecordToDB(mapAddEditTextTexts)){ //如果新增了记录，则刷新列表
                    Global.records = sqLiteHelper.queryRecord(db, null);
                    flushAllItemList();
                    if(isSearching)
                        flushSearchItemList(searchQuery);
                }
            }
        });
    }

    //初始化弹框二：修改记录的弹框
    private void initUpdateAlertDialog(final View updateDialogView){
        mapUpdateEditTexts.put("category", (EditText)updateDialogView.findViewById(R.id.update_category));
        mapUpdateEditTexts.put("accountName", (EditText)updateDialogView.findViewById(R.id.update_account_name));
        mapUpdateEditTexts.put("accountNum", (EditText)updateDialogView.findViewById(R.id.update_account_num));
        mapUpdateEditTexts.put("accountPwd", (EditText)updateDialogView.findViewById(R.id.update_account_pwd));
        mapUpdateEditTexts.put("nick", (EditText)updateDialogView.findViewById(R.id.update_nick));
        mapUpdateEditTexts.put("email", (EditText)updateDialogView.findViewById(R.id.update_email));
        mapUpdateEditTexts.put("phone", (EditText)updateDialogView.findViewById(R.id.update_phone));
        mapUpdateEditTexts.put("url", (EditText)updateDialogView.findViewById(R.id.update_url));
        mapUpdateEditTexts.put("securityProblem", (EditText)updateDialogView.findViewById(R.id.update_security_problem));
        mapUpdateEditTexts.put("securityAnswer", (EditText)updateDialogView.findViewById(R.id.update_security_answer));
        mapUpdateEditTexts.put("note", (EditText)updateDialogView.findViewById(R.id.update_note));

        updateDialog = new AlertDialog.Builder(MainActivity.this);
        updateDialog.setTitle("修改记录");
        updateDialog.setView(updateDialogView);
        updateDialog.setNegativeButton("取消", null);
        updateDialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapUpdateEditTextTexts.clear();
                for(Map.Entry<String, EditText> entry : mapUpdateEditTexts.entrySet())
                    mapUpdateEditTextTexts.put(entry.getKey(), entry.getValue().getText().toString());

                if(updateRecordToDB(mapUpdateEditTextTexts, updateIndex)){ //如果修改了记录，则刷新列表
                    Global.records = sqLiteHelper.queryRecord(db, null);
                    flushAllItemList();
                    if(isSearching)
                        flushSearchItemList(searchQuery);
                }
            }
        });
    }

    //初始化弹框三：显示加密信息的弹框
    private void initShowEncryptedAlertDialog(final View showEncryptedDialogView){
        showEncryptedDialog = new AlertDialog.Builder(MainActivity.this);
        showEncryptedDialog.setTitle("修改记录");
        showEncryptedDialog.setView(showEncryptedDialogView);
        showEncryptedDialog.setPositiveButton("确定", null);
    }

    //向数据库中添加Record记录
    private boolean insertRecordToDB(Map<String, String> map){
        String[] infos = new String[]{map.get("category"), map.get("accountName"), map.get("accountNum"), //0/1/2
                                map.get("accountPwd"), map.get("nick"), map.get("email"), //3/4/5
                                map.get("phone"), map.get("url"), map.get("securityProblem"), //6/7/8
                                map.get("securityAnswer"), map.get("note")}; //9/10

        for(int i = 0; i < infos.length; i++)
            if(infos[i] == null) infos[i] = "";

        if(infos[1].equals("")){
            Toast.makeText(MainActivity.this, "请输入账户名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(infos[2].equals("")){
            Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(infos[0].equals(""))
            infos[0] = "未分类";

        Record record = new Record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                infos[0], infos[1], AES128ECBPKCS5.encryptString(infos[2]), AES128ECBPKCS5.encryptString(infos[3]),
                AES128ECBPKCS5.encryptString(infos[4]), AES128ECBPKCS5.encryptString(infos[5]), AES128ECBPKCS5.encryptString(infos[6]),
                AES128ECBPKCS5.encryptString(infos[7]), AES128ECBPKCS5.encryptString(infos[8]), AES128ECBPKCS5.encryptString(infos[9]),
                AES128ECBPKCS5.encryptString(infos[10]));
        sqLiteHelper.insertRecord(db, record);

        return true;
    }

    //更新数据库中的Record记录
    private boolean updateRecordToDB(Map<String, String> map, int updateIndex){
        String[] infos = new String[]{map.get("category"), map.get("accountName"), map.get("accountNum"), //0/1/2
                map.get("accountPwd"), map.get("nick"), map.get("email"), //3/4/5
                map.get("phone"), map.get("url"), map.get("securityProblem"), //6/7/8
                map.get("securityAnswer"), map.get("note")}; //9/10

        for(int i = 0; i < infos.length; i++)
            if(infos[i] == null) infos[i] = "";

        if(infos[1].equals("")){
            Toast.makeText(MainActivity.this, "请输入账户名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(infos[2].equals("")){
            Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(infos[0].equals(""))
            infos[0] = "未分类";

        int id = Global.records.get(updateIndex).getId();
        if(isSearching)
            id = (int)(searchItemList.get(updateIndex).get("id"));
        Record record = new Record(id, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                infos[0], infos[1], AES128ECBPKCS5.encryptString(infos[2]), AES128ECBPKCS5.encryptString(infos[3]),
                AES128ECBPKCS5.encryptString(infos[4]), AES128ECBPKCS5.encryptString(infos[5]), AES128ECBPKCS5.encryptString(infos[6]),
                AES128ECBPKCS5.encryptString(infos[7]), AES128ECBPKCS5.encryptString(infos[8]), AES128ECBPKCS5.encryptString(infos[9]),
                AES128ECBPKCS5.encryptString(infos[10]));
        sqLiteHelper.updateRecord(db, record);

        return true;
    }

    //删除数据库中的Record记录
    private boolean deleteRecordToDB(int index){
        int id = Global.records.get(index).getId();
        if(isSearching)
            id = (int)(searchItemList.get(index).get("id"));
        sqLiteHelper.deleteRecord(db, id);
        return true;
    }


}

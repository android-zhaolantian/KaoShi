package com.example.zhaolantian1507a20170821;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhaolantian1507a20170821.xList.XListView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 赵蓝天 2017-08-21 主页面
 */
public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {
    private XListView xListView;
    private int page = 1;
    private int pager = 10;
    private List<ClassBean.ResultsBean> list = new ArrayList<>();
    private MyAdapter adapter;
    private MyHelper helper = new MyHelper(MainActivity.this);
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xListView = (XListView) findViewById(R.id.myxlist);
        xListView.setXListViewListener(this);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        adapter = new MyAdapter(MainActivity.this);
        getData();
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {
        getData();
    }

    private void getData() {
        MyTask task = new MyTask();
        task.execute("");
    }
    //异步任务
    class MyTask extends AsyncTask<String, Intent, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String path = "http://gank.io/api/history/content/" + pager + "/" + page;
                System.out.println("path-->" + path);
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int code = connection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String str = "";
                    String paths = "";
                    BufferedReader reade = new BufferedReader(new InputStreamReader(inputStream));
                    while ((str = reade.readLine()) != null) {
                        paths += str;
                    }
                    System.out.println("paths-->" + paths);
                    return paths;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            ClassBean bean = gson.fromJson(s, ClassBean.class);
            List<ClassBean.ResultsBean> results = bean.getResults();
            if (bean != null && bean.isError() == false){
                results = bean.getResults();
                if (results != null && results.size() > 0){
                    adapter.addData(results);
                }
                xListView.stopLoadMore();
                xListView.stopRefresh();
            }
        }
    }

    //向数据库添加数据
    private void insertData(List<ClassBean.ResultsBean> results) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.size(); i++) {
            values.clear();
            values.put("_id", results.get(i).get_id());
            values.put("created_at", results.get(i).getCreated_at());
            values.put("publishedAt", results.get(i).getPublishedAt());
            values.put("rand_id", results.get(i).getRand_id());
            values.put("updated_at", results.get(i).getUpdated_at());
            values.put("content", results.get(i).getContent());
            long data = db.insert("data", null, values);
        }
    }
    //查找数据库内容
    private void quData() {
        db = helper.getWritableDatabase();
        Cursor query = db.query("data", null, null, null, null, null, null);
        while (query.moveToNext()) {
            String title = query.getString(query.getColumnIndex("title"));
            String content = query.getString(query.getColumnIndex("content"));
            String _id = query.getString(query.getColumnIndex("_id"));
            String created_at = query.getString(query.getColumnIndex("created_at"));
            String publishedAt = query.getString(query.getColumnIndex("publishedAt"));
            String rand_id = query.getString(query.getColumnIndex("rand_id"));
            String updated_at = query.getString(query.getColumnIndex("updated_at"));
            list.add(new ClassBean.ResultsBean(_id, content, created_at, publishedAt, rand_id, title, updated_at));
        }
    }

}

package com.example.zhaolantian1507a20170821;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜕变~成蝶 on 2017/8/21.
 */

public class MyAdapter extends BaseAdapter {
    private List<ClassBean.ResultsBean> list = new ArrayList<>();
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }

    //追加数据；
    public void addData(List<ClassBean.ResultsBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    //更新数据；
    public void updateData(List<ClassBean.ResultsBean> list){
        this.list.clear();
        addData(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.xlist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        ClassBean.ResultsBean bean = list.get(position);
        holder.title.setText(bean.getTitle());
        holder.content.setText(bean.getContent());

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView title;
        public TextView content;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.title = (TextView) rootView.findViewById(R.id.title);
            this.content = (TextView) rootView.findViewById(R.id.content);
        }

    }
}

package com.qwer.myfirstdeeplearningdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwer.myfirstdeeplearningdemo.Bean.PersonBean;
import com.qwer.myfirstdeeplearningdemo.R;

import java.util.List;

/**
 * Created by a on 2017/10/6.
 */
public class ShowPersonsAdapter extends BaseAdapter {

    private Context context;
    private List<PersonBean> list_person;

    public ShowPersonsAdapter(Context context, List<PersonBean> list_person) {
        this.context = context;
        this.list_person = list_person;
    }

    @Override
    public int getCount() {
        return list_person.size();
    }

    @Override
    public Object getItem(int position) {
        return list_person.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_show_persons, null);
            holder = new ViewHolder();
            holder.ivPersonIcon = (ImageView) convertView.findViewById(R.id.ivPersonIcon);
            holder.tvPersonName = (TextView) convertView.findViewById(R.id.tvPersonName);
            holder.tvPersonNewMsg = (TextView) convertView.findViewById(R.id.tvPersonNewMsg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivPersonIcon.setImageBitmap(list_person.get(position).getIcon());
        holder.tvPersonName.setText(list_person.get(position).getPersonName());
        holder.tvPersonNewMsg.setText(list_person.get(position).getNewMessage());

        return convertView;
    }

    class ViewHolder {
        ImageView ivPersonIcon;
        TextView tvPersonName;
        TextView tvPersonNewMsg;
    }
}

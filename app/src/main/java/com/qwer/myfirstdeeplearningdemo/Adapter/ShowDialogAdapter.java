package com.qwer.myfirstdeeplearningdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qwer.myfirstdeeplearningdemo.Bean.DialogBean;
import com.qwer.myfirstdeeplearningdemo.R;

import java.util.List;

/**
 * Created by a on 2017/9/27.
 */
public class ShowDialogAdapter extends BaseAdapter {

    private Context context;
    private List<DialogBean> list_dialog;
    private String objectName = "";

    public ShowDialogAdapter(Context context, List<DialogBean> list_dialog) {
        this.context = context;
        this.list_dialog = list_dialog;
    }

    @Override
    public int getCount() {
        return list_dialog.size();
    }

    @Override
    public Object getItem(int position) {
        return list_dialog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_show_dialog, null);
            holder = new ViewHolder();
            holder.robotWord = (RelativeLayout) convertView.findViewById(R.id.robotWord);
            holder.robotIcon = (ImageView) convertView.findViewById(R.id.robotIcon);
            holder.robotContext = (TextView) convertView.findViewById(R.id.robotContext);
            holder.customerWord = (RelativeLayout) convertView.findViewById(R.id.customerWord);
            holder.customerIcon = (ImageView) convertView.findViewById(R.id.customerIcon);
            holder.customerContext = (TextView) convertView.findViewById(R.id.customerContext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        objectName = list_dialog.get(position).getObjectName();
        switch (objectName) {
            case "robot":
                holder.robotWord.setVisibility(View.VISIBLE);
                holder.customerWord.setVisibility(View.GONE);
                holder.robotIcon.setImageBitmap(list_dialog.get(position).getIcon());
                holder.robotContext.setText(list_dialog.get(position).getDialogContent());
                break;
            case "customer":
                holder.robotWord.setVisibility(View.GONE);
                holder.customerWord.setVisibility(View.VISIBLE);
                holder.customerIcon.setImageBitmap(list_dialog.get(position).getIcon());
                holder.customerContext.setText(list_dialog.get(position).getDialogContent());
                break;
            default:
                break;
        }

        return convertView;
    }

    class ViewHolder {
        //智能机器人
        RelativeLayout robotWord;
        ImageView robotIcon;
        TextView robotContext;
        //用户
        RelativeLayout customerWord;
        ImageView customerIcon;
        TextView customerContext;
    }
}

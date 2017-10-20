package com.qwer.myfirstdeeplearningdemo.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qwer.myfirstdeeplearningdemo.Adapter.ShowPersonsAdapter;
import com.qwer.myfirstdeeplearningdemo.Bean.PersonBean;
import com.qwer.myfirstdeeplearningdemo.MainActivity;
import com.qwer.myfirstdeeplearningdemo.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private ListView listViewPersons;
    private ShowPersonsAdapter shopPersonsAdapter;
    private List<PersonBean> list_person = new ArrayList<>();
    private Bitmap zuozhuBitmap;
    private Bitmap chutianBitmap;
    private Bitmap zuomingBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        initView();
        initData();
    }

    private void initView() {
        listViewPersons = (ListView) findViewById(R.id.listViewPersons);
    }

    private void initData() {
        zuozhuBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zuozhu);
        chutianBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.chutian);
        zuomingBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zuoming);
        addPerson(list_person, "佐助", "你这吊车尾，哼");
        addPerson(list_person, "雏田", "我想你了");
        addPerson(list_person, "佐鸣之恋", "佐助:多年不见，你还是没长进");
        shopPersonsAdapter = new ShowPersonsAdapter(this, list_person);
        listViewPersons.setAdapter(shopPersonsAdapter);
        listViewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendsListActivity.this, MainActivity.class);
                intent.putExtra("personName", list_person.get(position).getPersonName());
                startActivity(intent);
            }
        });
    }

    //添加person
    private void addPerson(List<PersonBean> list_person, String personName, String personNewMsg) {
        PersonBean personBean = new PersonBean();
        switch (personName) {
            case "佐助":
                personBean.setIcon(zuozhuBitmap);
                personBean.setPersonName(personName);
                personBean.setNewMessage(personNewMsg);
                list_person.add(personBean);
                break;
            case "雏田":
                personBean.setIcon(chutianBitmap);
                personBean.setPersonName(personName);
                personBean.setNewMessage(personNewMsg);
                list_person.add(personBean);
                break;
            case "佐鸣之恋":
                personBean.setIcon(zuomingBitmap);
                personBean.setPersonName(personName);
                personBean.setNewMessage(personNewMsg);
                list_person.add(personBean);
                break;
            default:
                break;
        }


    }

}

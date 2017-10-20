package com.qwer.myfirstdeeplearningdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qwer.myfirstdeeplearningdemo.API.API;
import com.qwer.myfirstdeeplearningdemo.Adapter.ShowDialogAdapter;
import com.qwer.myfirstdeeplearningdemo.Bean.DialogBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import cn.bmob.v3.Bmob;
//import cn.bmob.v3.BmobConfig;

public class MainActivity extends AppCompatActivity {

    private ListView listViewDialog;
    private LinearLayout sendMessage;
    private EditText inputContent;
    private TextView tvMainTitle;
    private ImageView ivMainClose;

    //用于存放双方对话的内容数据
    private List<DialogBean> list_dialog = new ArrayList<>();
    private ShowDialogAdapter showDialogAdapter;
    private Bitmap robotBitmap;
    private Bitmap customerBitmap;
    private static final String ROBOT = "robot";
    private static final String CUSTOMER = "customer";
    private static final int MSG_WHAT_MAIN = 1;
    private static final int MSG_WHAT_CHILD = 2;
    private static final int MSG_WHAT_INPUTTING = 3;
    private String[] welcome_array;
    private RequestQueue mQueue;
    private String personName;
    //标记“佐鸣之恋”对话中上一句是谁说的
    private String personLastSpeak = "";
    //标记“佐鸣之恋”对话中上一句说的内容
    private String personLastSpeakContent = "";

    private Handler handler_main_to_child;
    private Handler handler_child_to_main = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_CHILD:// 2
                    //更新UI
                    tvMainTitle.setText(personName);
                    showDialogAdapter.notifyDataSetChanged();
                    if (personName.equals("佐鸣之恋")) {
                        //发消息给子线程
                        Message message = Message.obtain();
                        message.what = MSG_WHAT_MAIN; // 1
                        message.obj = personLastSpeakContent;
                        handler_main_to_child.sendMessage(message);//发送消息到CustomThread
                    }
                    break;
                case MSG_WHAT_INPUTTING:// 3
                    tvMainTitle.setText("对方正在输入");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config = new BmobConfig.Builder(this)
//                //设置appkey
//                .setApplicationId(getString(R.string.bmob_application_id))
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(15)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                //文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(604800)//七天有604800秒（单位为秒）
//                .build();
//        Bmob.initialize(config);
        setContentView(R.layout.activity_main);
        //禁止弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        ivMainClose = (ImageView) findViewById(R.id.ivMainClose);
        tvMainTitle = (TextView) findViewById(R.id.tvMainTitle);
        listViewDialog = (ListView) findViewById(R.id.listViewDialog);
        sendMessage = (LinearLayout) findViewById(R.id.sendMessage);
        inputContent = (EditText) findViewById(R.id.inputContent);
    }

    private void initData() {
        mQueue = Volley.newRequestQueue(this);

        personName = getIntent().getStringExtra("personName");
        tvMainTitle.setText(personName);

        switch (personName) {
            case "佐助":
                robotBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zuozhu);
                break;
            case "雏田":
                robotBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.chutian);
                break;
            case "佐鸣之恋":
                robotBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zuozhu);
                personLastSpeak = "佐助";
                break;
            default:
                robotBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zuozhu);
                break;
        }

        customerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mingren);

        //点击关闭当前页面
        ivMainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //添加智能机器人会话,用户第一次进入，随机获取欢迎语
        final String personLastSpeakContent = getRandomWelcomeTips();
        addDialog(list_dialog, ROBOT, personLastSpeakContent);

        showDialogAdapter = new ShowDialogAdapter(this, list_dialog);
        listViewDialog.setAdapter(showDialogAdapter);//此时显示了第一句话

        //新建并启动CustomThread
        new CustomThread().start();

        //此时是佐助发问候语给鸣人
        if (personName.equals("佐鸣之恋")) {
            SystemClock.sleep(500);
            //发消息给子线程
            Message message = Message.obtain();
            message.what = MSG_WHAT_MAIN; // 1
            message.obj = personLastSpeakContent;
            handler_main_to_child.sendMessage(message);//发送消息到CustomThread

        }

        //点击发送按钮触发监听事件
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputContent.getText().toString().isEmpty() || !inputContent.getText().toString().equals("")) {
                    String strInputContent = inputContent.getText().toString();
                    //添加用户会话
                    addDialog(list_dialog, CUSTOMER, inputContent.getText().toString());
                    inputContent.setText("");
                    //点击发送之后，关闭软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    showDialogAdapter.notifyDataSetChanged();
                    //发消息给子线程
                    Message message = Message.obtain();
                    message.what = MSG_WHAT_MAIN; // 1
                    message.obj = strInputContent;
                    handler_main_to_child.sendMessage(message);//发送消息到CustomThread
                }
            }
        });
    }

    /**
     * 用户第一次进入，随机获取欢迎语
     */
    private String getRandomWelcomeTips() {
        String welcome_tip = "";
        if (personName.equals("佐助") || personName.equals("佐鸣之恋")) {
            welcome_array = this.getResources()
                    .getStringArray(R.array.welcome_tips_zuozhu);
        } else if (personName.equals("雏田")) {
            welcome_array = this.getResources()
                    .getStringArray(R.array.welcome_tips_chutian);
        }

        int index = (int) (Math.random() * (welcome_array.length - 1));
        welcome_tip = welcome_array[index];
        return welcome_tip;
    }

    //在子线程中进行耗时操作,实现机器人回复
    private void robotReply(String message) {
        //添加智能机器人会话
        //模拟深度学习
        SystemClock.sleep(1000);
        if (!personName.equals("佐鸣之恋")) {
            //更新UI显示“对方正在输入”
            handler_child_to_main.sendEmptyMessage(MSG_WHAT_INPUTTING);// 3
        }
        SystemClock.sleep(1000);
        // 去掉空格
        String dropk = message.replace(" ", "");
        // 去掉回车
        String droph = dropk.replace("\n", "");
        String url = "";
        switch (personName) {
            case "佐助":
                url = API.url_zuozhu + droph;
                break;
            case "雏田":
                url = API.url_chutian + droph;
                break;
            case "佐鸣之恋":
                if (personLastSpeak.equals("佐助")) {
                    url = API.url_mingren + personLastSpeakContent;
                } else if (personLastSpeak.equals("鸣人")) {
                    url = API.url_zuozhu + personLastSpeakContent;
                }
                break;
            default:
                break;
        }
        //佐鸣之恋(机器人与机器人交互)
        if (!personLastSpeak.equals("")) {
            JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (personLastSpeak.equals("佐助")) {
                            addDialog(list_dialog, CUSTOMER, response.getString("text"));
                            personLastSpeak = "鸣人";
                            personLastSpeakContent = response.getString("text");
                        } else if (personLastSpeak.equals("鸣人")) {
                            addDialog(list_dialog, ROBOT, response.getString("text"));
                            personLastSpeak = "佐助";
                            personLastSpeakContent = response.getString("text");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addDialog(list_dialog, ROBOT, "不知道你在说什么");
                        personLastSpeak = "佐助";
                        personLastSpeakContent = "不知道你在说什么";
                    }
                    //发送到主线程更新UI
                    handler_child_to_main.sendEmptyMessage(MSG_WHAT_CHILD);// 2
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (personLastSpeak.equals("佐助")) {
                        addDialog(list_dialog, CUSTOMER, "你不联网，我怎么知道你说的是什么");
                        personLastSpeak = "鸣人";
                        personLastSpeakContent = "你不联网，我怎么知道你说的是什么";
                    } else if (personLastSpeak.equals("鸣人")) {
                        addDialog(list_dialog, ROBOT, "你不联网，我怎么知道你说的是什么");
                        personLastSpeak = "佐助";
                        personLastSpeakContent = "你不联网，我怎么知道你说的是什么";
                    }
                    //发送到主线程更新UI
                    handler_child_to_main.sendEmptyMessage(MSG_WHAT_CHILD);// 2
                }
            });
            mQueue.add(request);

        } else {  //人机交互
            JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        addDialog(list_dialog, ROBOT, response.getString("text"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addDialog(list_dialog, ROBOT, "不知道你在说什么");
                    }
                    //发送到主线程更新UI
                    handler_child_to_main.sendEmptyMessage(MSG_WHAT_CHILD);// 2
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    addDialog(list_dialog, ROBOT, "你不联网，我怎么知道你说的是什么");
                    //发送到主线程更新UI
                    handler_child_to_main.sendEmptyMessage(MSG_WHAT_CHILD);// 2
                }
            });
            mQueue.add(request);
        }


    }

    //添加会话内容
    private void addDialog(List<DialogBean> list_dialog, String objectName, String dialogContent) {
        if (objectName.equals(ROBOT)) {
            //智能机器人会话对象
            DialogBean robotBean = new DialogBean();
            switch (personName) {
                case "佐助":
                    robotBean.setIcon(robotBitmap);
                    robotBean.setObjectName(objectName);
                    robotBean.setDialogContent(dialogContent);
                    list_dialog.add(robotBean);
                    break;
                case "雏田":
                    robotBean.setIcon(robotBitmap);
                    robotBean.setObjectName(objectName);
                    robotBean.setDialogContent(dialogContent);
                    list_dialog.add(robotBean);
                    break;
                case "佐鸣之恋":
                    robotBean.setIcon(robotBitmap);
                    robotBean.setObjectName(objectName);
                    robotBean.setDialogContent(dialogContent);
                    list_dialog.add(robotBean);
                    break;
                default:
                    break;
            }

        } else {
            //用户会话对象
            DialogBean customerBean = new DialogBean();
            customerBean.setIcon(customerBitmap);
            customerBean.setObjectName(objectName);
            customerBean.setDialogContent(dialogContent);
            list_dialog.add(customerBean);
        }
        //只保留30条聊天记录
        if (list_dialog.size() > 30) {
            // 移除数据
            list_dialog.remove(0);
        }
    }

    //自定义子线程
    class CustomThread extends Thread {
        @Override
        public void run() {
            //建立消息循环的步骤
            Looper.prepare();//1、初始化Looper
            handler_main_to_child = new Handler() {//2、绑定handler到CustomThread实例的Looper对象
                public void handleMessage(Message msg) {//3、定义处理消息的方法
                    switch (msg.what) {
                        case MSG_WHAT_MAIN:// 1
                            //在子线程中进行耗时操作,实现机器人回复
                            robotReply((String) msg.obj);
                            break;
                        default:
                            break;
                    }
                }
            };
            Looper.loop();//4、启动消息循环
        }
    }


}

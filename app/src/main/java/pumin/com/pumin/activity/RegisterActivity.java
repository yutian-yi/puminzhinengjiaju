package pumin.com.pumin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pumin.com.pumin.R;
import pumin.com.pumin.context.UIConfig;
/*
* 注册页面
* */
public class RegisterActivity extends Activity implements View.OnClickListener {
    /**
     * The et Name
     */
    private EditText etName;

    /**
     * The btn GetCode
     */
    private Button btnGetCode;

    /**
     * The et Code
     */
    private EditText etCode;

    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The btn Rrgister
     */
    private Button btnRrgister;

    /**
     * The cb Laws
     */
    private CheckBox cbLaws;
    private String TAG = "RegisterActivity";
    private MyCountDownTimer myCountDownTimer;
    private ImageView mClose;
    private TextView mLoginTX;
    private TextView mLogin2TX;
    //    private GizWifiSDKListener mListener;
    // 实例化监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        initListener();
//        GizWifiSDKListener mListener = new GizWifiSDKListener() {
//            // 实现手机号注册用户回调
//            @Override
//            public void didRegisterUser(GizWifiErrorCode result, String uid, String token){
//                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//// 注册成功，处理注册成功的逻辑
//                    Log.d(TAG,"============"+result.toString());
//                } else {
//// 注册失败，处理注册失败的逻辑
//                    Log.d(TAG,"------------"+result.toString());
//                }
//            }
//        };
//// 注册监听器
//        GizWifiSDK.sharedInstance().setListener(mListener);
//// 调用SDK的手机号注册接口
//        GizWifiSDK.sharedInstance().registerUser("HelloGizwits", "12345678");
    }

    private void initListener() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000);

        btnGetCode.setOnClickListener(this);
        cbLaws.setOnClickListener(this);
        btnRrgister.setOnClickListener(this);
        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });
        mClose.setOnClickListener(this);
        mLoginTX.setOnClickListener(this);
        mLogin2TX.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        btnGetCode = (Button) findViewById(R.id.btnGetCode);
        etCode = (EditText) findViewById(R.id.etCode);
        etPsw = (EditText) findViewById(R.id.etPsw);
        btnRrgister = (Button) findViewById(R.id.btnRrgister);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        mClose = (ImageView) findViewById(R.id.Register_left_IMG);
        mLoginTX = (TextView)findViewById(R.id.LoginTX);
        mLogin2TX = (TextView)findViewById(R.id.Login2TX);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:// 获取验证码
                Editable text = etName.getText();
                if (text.length() != 0 && text.length() == 11) {
                    myCountDownTimer.start();
                    GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(UIConfig.app_secret, text.toString());
// 实现回调
                    GizWifiSDKListener mListener = new GizWifiSDKListener() {
                        @Override
                        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
                            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 请求成功
                                Log.d(TAG, token + "------------1 = " + result.toString());
                            } else {
// 请求失败
                                Log.d(TAG, token + "------------2 = " + result.toString());
                            }
                        }
                    };
                    GizWifiSDK.sharedInstance().setListener(mListener);
                } else {
                    Toast.makeText(this, "您输入的手机号有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRrgister://注册按钮
                Editable phoneNum = etName.getText();
                Editable yanzhengma = etCode.getText();
                Editable password = etPsw.getText();
                if (phoneNum.length()!=11){
                    Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }
                if (yanzhengma.length()!=6){
                    Toast.makeText(this,"请输入正确的验证码",Toast.LENGTH_SHORT).show();

                }
                if (password.length()>6&&yanzhengma.length()==6&&phoneNum.length()==11) {
                    boolean b = judgeContainsStr(password.toString());
                    boolean b1 = judgeContainsStr1(password.toString());
                    boolean containNumber = isContainNumber(password.toString());
                    if (!b1|!b|!containNumber){
                        Toast.makeText(RegisterActivity.this, "密码需要包含大小写字母和数字", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    GizWifiSDK.sharedInstance().registerUser(phoneNum.toString(), password.toString(), yanzhengma.toString(), GizUserAccountType.GizUserPhone);
// 实现回调

                    GizWifiSDKListener mListener = new GizWifiSDKListener() {
                        @Override
                        public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
                            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 注册成功
                                Log.d(TAG, token + "------------3 = " + result.toString());
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                            } else {
// 注册失败
                                Toast.makeText(RegisterActivity.this,"注册失败", Toast.LENGTH_SHORT).show();

                                Log.d(TAG, token + "------------4 = " + result.toString());
                            }
                        }
                    };
                    GizWifiSDK.sharedInstance().setListener(mListener);
                }
                break;
            case R.id.Register_left_IMG:
                RegisterActivity.this.finish();
                break;//333ddf0623174026ab3bfdb6ebab8120
            case R.id.LoginTX:
                finish();
                break;
            case R.id.Login2TX:
                finish();
                break;
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btnGetCode.setClickable(false);
            btnGetCode.setText(l / 1000 + "s");
            btnGetCode.setBackgroundColor(Color.parseColor("#aaaaaa"));

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnGetCode.setText("重新发送");
            //设置可点击
            btnGetCode.setClickable(true);
            btnGetCode.setBackgroundColor(Color.parseColor("#a7292a"));

        }
    }
    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     * @author fenggaopan 2015年7月21日 上午9:49:40
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex=".*[A-Z]+.*";
        Matcher m= Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }
    public boolean judgeContainsStr1(String cardNum) {
        String regex=".*[a-z]+.*";
        Matcher m= Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }
    public static boolean isContainNumber(String company) {

        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(company);
        if (m.find()) {
            return true;
        }
        return false;
    }
}

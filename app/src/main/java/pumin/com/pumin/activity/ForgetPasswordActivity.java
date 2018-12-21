package pumin.com.pumin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pumin.com.pumin.R;
import pumin.com.pumin.untils.SpUtils;
    // 修改密码
public class ForgetPasswordActivity extends Activity {

    private EditText mPassword;
    private EditText mNewPassword1;
    private EditText mNewPassword2;
    private Button mRegister;
    private ImageView mCloseIMG;
    private CheckBox cbLaws1,cbLaws2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mCloseIMG = (ImageView) findViewById(R.id.Register_left_IMG);
        mPassword = (EditText) findViewById(R.id.Forget_etName);
        mNewPassword1 = (EditText) findViewById(R.id.Forget_etCode);
        mNewPassword2 = (EditText) findViewById(R.id.Forget_etPsw);
        mRegister = (Button) findViewById(R.id.Forget_btnRrgister);

//        cbLaws1 = (CheckBox) findViewById(R.id.Forget_cbLaws1);
//        cbLaws2 = (CheckBox) findViewById(R.id.Forget_cbLaws2);

    }

    private void initListener() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Token = SpUtils.getString(ForgetPasswordActivity.this, "Token");
                Editable text = mPassword.getText();
                Editable text1 = mNewPassword1.getText();
                final Editable text2 = mNewPassword2.getText();
                String etPsws = SpUtils.getString(ForgetPasswordActivity.this, "etPsws");
                if (text.length() != 0) {

                    if (!text1.toString().equals(text2.toString())) {
                        Toast.makeText(ForgetPasswordActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();

                    } else {
                        GizWifiSDK.sharedInstance().changeUserPassword(Token, text.toString(), text1.toString());

// 实现回调
                        boolean b = judgeContainsStr(text1.toString());
                        boolean b1 = judgeContainsStr1(text1.toString());
                        boolean containNumber = isContainNumber(text1.toString());
                        if (!b1|!b|!containNumber){
                            Toast.makeText(ForgetPasswordActivity.this, "密码需要包含大小写字母和数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        GizWifiSDKListener mListener = new GizWifiSDKListener() {
                            public String TAG = "ForgetPasswordActivity";

                            @Override
                            public void didChangeUserPassword(GizWifiErrorCode result) {
                                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 修改成功
                                    Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    SpUtils.putString(ForgetPasswordActivity.this, "etPsws", text2.toString());
                                    finish();
                                } else {
// 修改失败
                                Toast.makeText(ForgetPasswordActivity.this, "修改失败"+result, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "---------------" + result);
                                    if (result.toString().equals("GIZ_OPENAPI_USERNAME_PASSWORD_ERROR")) {
                                        Toast.makeText(ForgetPasswordActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        };
                        GizWifiSDK.sharedInstance().setListener(mListener);
                    }
                }else {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();

                }

            }
        });
        mCloseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        cbLaws1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                String psw = mNewPassword1.getText().toString();
//
//                if (isChecked) {
//                    mNewPassword1.setInputType(0x90);
//                } else {
//                    mNewPassword1.setInputType(0x81);
//                }
//                mNewPassword1.setSelection(psw.length());
//            }
//        });
//        cbLaws2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                String psw = mNewPassword2.getText().toString();
//
//                if (isChecked) {
//                    mNewPassword2.setInputType(0x90);
//                } else {
//                    mNewPassword2.setInputType(0x81);
//                }
//                mNewPassword2
//                        .setSelection(psw.length());
//            }
//        });
    }

    private void initData() {

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

package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.bean.MessageEvent;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心  二维码校验 界面
 */
public class SecurityCheckActivity extends BasicActivity implements RequestView, SelectBackListener {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.et_phone_email)
    EditText etPhoneEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.bt_next)
    Button btNext;


    private String mRequestTag = "";
    private String mTempToken = "";
    private String mAuthCode = "";
    private String mSmsToken = "";


    private Map<String, Object> mShareMap;

    private KindSelectDialog mDialog;
    private  String type = "";

    private TimeCount mTimeCount;

    @Override
    public int getContentView() {
        return R.layout.activity_security_check;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        mTimeCount = new TimeCount(1 * 60 * 1000, 1000);

        mTitleText.setText("安全中心");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);

        Bundle bundle =getIntent().getExtras();
        if (bundle != null){
            type =bundle.getString("TYPE");
            String account= bundle.getString("DATA");
           /* if (type.equals("0")){
                etPhoneEmail.setHint("请输入邮箱帐号");
            }else {
                etPhoneEmail.setHint("请输入手机号");
            }*/
            etPhoneEmail.setHint("请输入手机/邮箱帐号");
            etPhoneEmail.setText(account);
        }

    }






    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.tv_code,R.id.bt_next})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.tv_code:
                //获取短信验证码
                if (UtilTools.empty(etPhoneEmail.getText().toString())){
                    showToastMsg("请输入手机/邮箱账号");
                    return;
                }
                mTimeCount.start();
                getMsgcodeAction();
                break;
            case R.id.bt_next:
                registAction();
                break;

        }
    }

    private void registAction() {
        if (UtilTools.empty(etPhoneEmail.getText().toString())){
            showToastMsg("请输入手机/邮箱账号");
            return;
        }
        if (UtilTools.empty(etCode.getText().toString())){
            showToastMsg("请输入验证码");
            return;
        }


        mRequestTag = MethodUrl.EDIT_ACCOUNT;
        Map<String,Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(SecurityCheckActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("account",etPhoneEmail.getText().toString());
        map.put("code",etCode.getText().toString());
        Map<String, String> mHeaderMap = new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.EDIT_ACCOUNT, map);

    }


    private void getMsgcodeAction() {
        mRequestTag = MethodUrl.REGIST_SMSCODE;
        Map<String, Object> map = new HashMap<>();
        map.put("account",etPhoneEmail.getText().toString());
        Map<String, String> mHeaderMap = new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REGIST_SMSCODE, map);
    }



    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    // TODO: 2019/8/29  修改手机号和邮箱
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType){
            case MethodUrl.REGIST_SMSCODE:
                showToastMsg(getResources().getString(R.string.code_phone_tip));
                etCode.setText(tData.get("data")+"");
                break;
            case MethodUrl.EDIT_ACCOUNT:

                switch (tData.get("code")+""){
                    case "0": //请求成功
                        showToastMsg("修改成功");

                        SPUtils.put(SecurityCheckActivity.this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT, etPhoneEmail.getText()+"");

                        //更新UserInfo
                        //Eventbus  发送事件
                        MessageEvent event = new MessageEvent();
                        event.setType(MbsConstans.MessageEventType.UPDATE_USERINFO);
                        Map<Object,Object> map = new HashMap<>();
                        event.setMessage(map);

                        EventBus.getDefault().post(event);

                        finish();

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(SecurityCheckActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }


                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    /**---------------------------------------------------------------------以下代码申请权限---------------------------------------------
     * Request permissions.
     */


    /**
     * activity销毁前触发的方法
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * activity恢复时触发的方法
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }



    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:
                String str = (String) map.get("name");

                break;

        }
    }



    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvCode.setText(getResources().getString(R.string.msg_code_again));
            tvCode.setClickable(true);
            MbsConstans.CURRENT_TIME = 0;
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvCode.setClickable(false);
            tvCode.setText(millisUntilFinished / 1000 + "秒");
            MbsConstans.CURRENT_TIME = (int) (millisUntilFinished / 1000);
        }
    }


}

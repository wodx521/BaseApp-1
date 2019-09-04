package com.lr.biyou.ui.moudle.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.fujianlian.klinechart.draw.MACDDraw;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.bean.MessageEvent;
import com.lr.biyou.db.IndexData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.UpdateDialog;
import com.lr.biyou.service.DownloadService;
import com.lr.biyou.ui.moudle2.fragment.ChatViewFragment;
import com.lr.biyou.ui.moudle3.fragment.HeYueFragment;
import com.lr.biyou.ui.moudle1.fragment.HomeFragment;
import com.lr.biyou.ui.moudle4.fragment.OTCFragment;
import com.lr.biyou.ui.moudle5.fragment.ZiChanFragment;
import com.lr.biyou.ui.temporary.activity.UserInfoActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MainActivity extends BasicActivity implements RequestView {
    @BindView(R.id.btn_cart)
    ImageView btnCart;
    @BindView(R.id.btn_cart2)
    ImageView btnCart2;
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;
    private ImageView[] mTabs;
    private TextView[] mTextViews;
    private HomeFragment mHomeFrament;
    private ChatViewFragment mIndexFragment;
    private HeYueFragment mBorrowFragment;
    private OTCFragment mRepaymentFragment;
    private ZiChanFragment mPersonFragment;
    private Fragment[] fragments;

    private TextView mAutoScrollTextView;

    private int index;
    private int currentTabIndex;
    public static MainActivity mInstance;

    private RelativeLayout mIndexBottomLay;
    private Snackbar snackbar;

    private String mRequestTag = "";

    private Intent mDownIntent;

    private IndexData mIndexData;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    /**
     * 主界面不需要支持滑动返回，重写该方法永久禁用当前界面的滑动返回功能
     *
     * @return
     */

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void init() {

        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
        registerReceiver(mBroadcastReceiver, intentFilter);

        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.MAIN_ACTIVITY);
        sendBroadcast(intent);

        SPUtils.put(this, MbsConstans.SharedInfoConstans.LOGIN_OUT, false);

        StatusBarUtil.setTranslucentForImageView(this, MbsConstans.ALPHA, null);
        initView();

        mDownIntent = new Intent(this, DownloadService.class);
        startService(mDownIntent);

        mIndexData = IndexData.getInstance();

        mHandler = new Handler(Looper.getMainLooper());
        // initPush();
        mInstance = this;

       // getAppVersion();
        getUserInfoAction();
       // getNameCodeInfo();

        //mAutoScrollTextView = findViewById(R.id.scroll_text_view);
        //mAutoScrollTextView.setSelected(true);

    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE)) {
                getUserInfoAction();
            }
        }
    };

    private void initView() {
        mIndexBottomLay = findViewById(R.id.btn_container_index);

        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        mTabs = new ImageView[5];
        mTabs[0] = (ImageView) findViewById(R.id.btn_conversation);
        mTabs[1] = (ImageView) findViewById(R.id.btn_address_list);
        mTabs[2] = (ImageView) findViewById(R.id.btn_cart);
        mTabs[3] = (ImageView) findViewById(R.id.btn_setting);
        mTabs[4] = (ImageView) findViewById(R.id.btn_zichan);

        mTextViews = new TextView[5];
        mTextViews[0] = (TextView) findViewById(R.id.one_tv);
        mTextViews[1] = (TextView) findViewById(R.id.two_tv);
        mTextViews[2] = (TextView) findViewById(R.id.three_tv);
        mTextViews[3] = (TextView) findViewById(R.id.four_tv);
        mTextViews[4] = (TextView) findViewById(R.id.five_tv);
        // select first tab
        mTabs[0].setSelected(true);
        mTextViews[0].setSelected(true);

        mHomeFrament = new HomeFragment();
        mIndexFragment = new ChatViewFragment();
        mBorrowFragment = new HeYueFragment();
        mRepaymentFragment = new OTCFragment();
        mPersonFragment = new ZiChanFragment();
        fragments = new Fragment[]{mHomeFrament, mIndexFragment, mBorrowFragment, mRepaymentFragment, mPersonFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mHomeFrament)
                .show(mHomeFrament)
                .commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取用户基本信息
     */
    public void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(MainActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }

    /**
     * 获取全局字典配置信息
     */
    public void getNameCodeInfo() {
        mRequestTag = MethodUrl.nameCode;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.nameCode, map);
    }

    /**
     * 获取app更新信息
     */
    public void getAppVersion() {
        mRequestTag = MethodUrl.appVersion;

        Map<String, String> map = new HashMap<>();
        map.put("appCode", MbsConstans.UPDATE_CODE);
        map.put("osType", "android");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.appVersion, map);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_container_index:
                index = 0;
                btnCart2.setSelected(false);
                break;
            case R.id.btn_container_get:
                btnCart2.setSelected(false);
                index = 1;
                break;
            case R.id.btn_container_return:
                index = 2;
                btnCart2.setSelected(true);
                break;
            case R.id.btn_container_person:
                btnCart2.setSelected(false);
                index = 3;
                break;
            case R.id.btn_container_zichan:
                btnCart2.setSelected(false);
                index = 4;
                break;

        }


        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            } else {
                switch (index) {// mIndexFragment, mBorrowFragment, mRepaymentFragment,mPersonFragment
                    case 0:
                        ((HomeFragment) fragments[index]).setBarTextColor();
                        break;
                    case 1:
                        ((ChatViewFragment) fragments[index]).setBarTextColor();
                        break;
                    case 2:
                        ((HeYueFragment) fragments[index]).setBarTextColor();
                        break;
                    case 3:
                        ((OTCFragment) fragments[index]).setBarTextColor();
                        break;
                    case 4:
                        ((ZiChanFragment) fragments[index]).setBarTextColor();
                        break;

                }
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);

        mTextViews[currentTabIndex].setSelected(false);
        mTextViews[index].setSelected(true);
        currentTabIndex = index;
    }


    @OnClick({R.id.btn_cart, R.id.btn_cart2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cart:
            case R.id.btn_cart2:
                btnCart2.setSelected(true);
                index = 2;
                if (currentTabIndex != index) {
                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments[currentTabIndex]);
                    if (!fragments[index].isAdded()) {
                        trx.add(R.id.fragment_container, fragments[index]);
                    } else {
                        switch (index) {// mIndexFragment, mBorrowFragment, mRepaymentFragment,mPersonFragment
                            case 0:
                                ((HomeFragment) fragments[index]).setBarTextColor();
                                break;
                            case 1:
                                ((ChatViewFragment) fragments[index]).setBarTextColor();
                                break;
                            case 2:
                                ((HeYueFragment) fragments[index]).setBarTextColor();
                                break;
                            case 3:
                                ((OTCFragment) fragments[index]).setBarTextColor();
                                break;
                            case 4:
                                ((ZiChanFragment) fragments[index]).setBarTextColor();
                                break;

                        }
                    }
                    trx.show(fragments[index]).commitAllowingStateLoss();
                }
                mTabs[currentTabIndex].setSelected(false);
                // set current tab selected
                mTabs[index].setSelected(true);

                mTextViews[currentTabIndex].setSelected(false);
                mTextViews[index].setSelected(true);
                currentTabIndex = index;
                break;
        }
    }




    private Handler mHandler;
    private boolean isOnKeyBacking;

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentTabIndex != 0) {
                mIndexBottomLay.performClick();
            } else {
                if (isOnKeyBacking) {
                    mHandler.removeCallbacks(onBackTimeRunnable);
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    closeAllActivity();
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                    return true;
                } else {
                    isOnKeyBacking = true;
                    if (snackbar == null) {
                        snackbar = Snackbar.make(findViewById(R.id.fragment_container), "再次点击退出应用", Snackbar.LENGTH_SHORT);
                        snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
                    }
                    snackbar.show();
                    mHandler.postDelayed(onBackTimeRunnable, 2000);
                    return true;
                }
            }
            return true;
        }
        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            if (snackbar != null) {
                snackbar.dismiss();
            }
        }
    };

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        // {"id":1,"appCode":"phb","downUrl":"http://ys.51zhir.cn/app_api/apk/dr20190514.apk","fileName":"dr20190514.apk",
        // "fileSize":null,"isMust":"0","md5Code":"722f70f68c262e9c585f7dd800ae327c",
        // "memo":null,"osType":"android","verCode":"1","verName":"V1.0.1 Beta","verUpdateMsg":"版本更新内容"}
        switch (mType) {
            case MethodUrl.appVersion:
                if (tData != null && !tData.isEmpty()) {
                    //网络版本号
                    MbsConstans.UpdateAppConstans.VERSION_NET_CODE = UtilTools.getIntFromStr(tData.get("verCode") + "");
                    //MbsConstans.UpdateAppConstans.VERSION_NET_CODE = 3;
                    //网络下载url
                    MbsConstans.UpdateAppConstans.VERSION_NET_APK_URL = tData.get("downUrl") + "";
                    //MbsConstans.UpdateAppConstans.VERSION_NET_APK_URL = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
                    //网络版本名称
                    MbsConstans.UpdateAppConstans.VERSION_NET_APK_NAME = tData.get("verName") + "";
                    //网络MD5值
                    MbsConstans.UpdateAppConstans.VERSION_MD5_CODE = tData.get("md5Code") + "";
                    //本次更新内容
                    MbsConstans.UpdateAppConstans.VERSION_NET_UPDATE_MSG = tData.get("verUpdateMsg") + "";

                    String mustUpdate = tData.get("isMust") + "";
                    if (mustUpdate.equals("0")) {
                        MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE = false;
                    } else {
                        MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE = true;
                    }
                    showUpdateDialog();
                }
                break;
            case MethodUrl.nameCode:
                String result = tData.get("result") + "";
                SPUtils.put(MainActivity.this, MbsConstans.SharedInfoConstans.NAME_CODE_DATA, result);
                break;
            case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        MbsConstans.USER_MAP = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(MbsConstans.USER_MAP)){
                            SPUtils.put(MainActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO, JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }

                //showUpdateDialog();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                for (String stag : mRequestTagList) {
                    switch (stag) {
                        case MethodUrl.USER_INFO:
                            getUserInfoAction();
                            break;
                        case MethodUrl.nameCode://{
                            getNameCodeInfo();
                            break;
                    }
                    mRequestTagList = new ArrayList<>();
                    break;
                }
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.appVersion:
                break;
        }
        dealFailInfo(map, mType);
    }


    private UpdateDialog mUpdateDialog;

    private void showUpdateDialog() {
        if (MbsConstans.UpdateAppConstans.VERSION_NET_CODE > MbsConstans.UpdateAppConstans.VERSION_APP_CODE) {
            mUpdateDialog = new UpdateDialog(this, true);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.cancel:
                            if (MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE) {
                                showToastMsg("本次升级为必须更新，请您更新！");
                            } else {
                                mUpdateDialog.dismiss();
                            }
                            break;
                        case R.id.confirm:
                            PermissionsUtils.requsetRunPermission(MainActivity.this, new RePermissionResultBack() {
                                @Override
                                public void requestSuccess() {
                                    mUpdateDialog.getProgressLay().setVisibility(View.VISIBLE);
                                    DownloadService.downNewFile(MbsConstans.UpdateAppConstans.VERSION_NET_APK_URL, 1003,
                                            MbsConstans.UpdateAppConstans.VERSION_NET_APK_NAME, MbsConstans.UpdateAppConstans.VERSION_MD5_CODE, MainActivity.this);
                                }

                                @Override
                                public void requestFailer() {

                                }
                            }, Permission.Group.STORAGE);

                            if (!MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE) {
                                mUpdateDialog.dismiss();
                            }
                            break;
                    }
                }
            };
            mUpdateDialog.setCanceledOnTouchOutside(false);
            mUpdateDialog.setCancelable(false);
            String ss = "";
            if (MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE) {
                ss = "必须更新";
            } else {
                ss = "建议更新";
            }
            mUpdateDialog.setOnClickListener(onClickListener);
            mUpdateDialog.initValue("检查新版本" + "(" + ss + ")", "更新内容:\n" + MbsConstans.UpdateAppConstans.VERSION_NET_UPDATE_MSG);
            mUpdateDialog.show();

            if (MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE) {
                mUpdateDialog.setCancelable(false);
                mUpdateDialog.tv_cancel.setEnabled(false);
                mUpdateDialog.tv_cancel.setVisibility(View.GONE);
            } else {
                mUpdateDialog.setCancelable(true);
                mUpdateDialog.tv_cancel.setEnabled(true);
                mUpdateDialog.tv_cancel.setVisibility(View.VISIBLE);
            }
            mUpdateDialog.getProgressLay().setVisibility(View.GONE);
            DownloadService.mProgressBar = mUpdateDialog.getProgressBar();
            DownloadService.mTextView = mUpdateDialog.getPrgText();
        }
    }


    /**
     * DownLoadManager 下载时EventBus更新UI
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MessageEvent event) {
        switch (event.getType()) {
            case 0:
                Map<Object, Object> map = event.getMessage();
                LogUtilDebug.i("show", "eventBus:" + map.get("size"));
                mUpdateDialog.update(map.get("max") + "", map.get("size") + "", "下载进度: " + map.get("progress") + "");
                break;

            case 1:
                LogUtilDebug.i("show", "eventBus: update UI" );
                getUserInfoAction();
                break;

        }
    }





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
    protected void onPause() {
        super.onPause();
        mIsRefresh = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }



}
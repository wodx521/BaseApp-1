package com.lr.biyou.ui.moudle4.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkun.xtablayout.XTabLayout;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle3.activity.CoinInfoDetailActivity;
import com.lr.biyou.ui.moudle4.activity.WeiTuoListActivity;
import com.lr.biyou.ui.moudle3.adapter.BuyAdapter;
import com.lr.biyou.ui.moudle4.adapter.EntrustListAdapter;
import com.lr.biyou.ui.moudle4.adapter.WeiTuoListAdapter;
import com.lr.biyou.ui.moudle3.adapter.SellAdapter;
import com.lr.biyou.ui.moudle3.adapter.TypeSelectAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.BigDecimalUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.wanou.framelibrary.okgoutil.websocket.WsManager;
import com.wanou.framelibrary.okgoutil.websocket.WsStatus;
import com.wanou.framelibrary.okgoutil.websocket.listener.WsStatusListener;
import com.wanou.framelibrary.utils.GsonUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * OTC  币币交易
 */
public class BBTradeFragment extends BasicFragment implements RequestView, ReLoadingData, SelectBackListener {
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.lay22)
    LinearLayout lay22;
    @BindView(R.id.rbBuy)
    RadioButton rbBuy;
    @BindView(R.id.rbSell)
    RadioButton rbSell;
    @BindView(R.id.rgBuySell)
    RadioGroup rgBuySell;
    @BindView(R.id.tvLimitPrice)
    TextView tvLimitPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.ivLess)
    ImageView ivLess;
    @BindView(R.id.view18)
    View view18;
    @BindView(R.id.ivPlus)
    ImageView ivPlus;
    @BindView(R.id.clPrice)
    ConstraintLayout clPrice;
    @BindView(R.id.tvMarketPrice)
    TextView tvMarketPrice;
    @BindView(R.id.tvCnyPrice)
    TextView tvCnyPrice;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.clNumber)
    ConstraintLayout clNumber;
    @BindView(R.id.tvAvailable)
    TextView tvAvailable;
    @BindView(R.id.coinCoinSeekBar)
    RadioGroup coinCoinSeekBar;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.tvTransactionAmount)
    TextView tvTransactionAmount;
    @BindView(R.id.clTransactionAmount)
    ConstraintLayout clTransactionAmount;
    @BindView(R.id.tvOperateCoin)
    TextView tvOperateCoin;
    @BindView(R.id.clLeft)
    ConstraintLayout clLeft;
    @BindView(R.id.textView59)
    TextView textView59;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.rvSell)
    RecyclerView rvSell;
    @BindView(R.id.tvCurrentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.tvCurrentCny)
    TextView tvCurrentCny;
    @BindView(R.id.rvBuy)
    RecyclerView rvBuy;
    @BindView(R.id.clRight)
    ConstraintLayout clRight;
    @BindView(R.id.tlEntrust)
    LinearLayout tlEntrust;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvEntrustDay)
    RecyclerView rvEntrustDay;
    @BindView(R.id.hsv)
    HorizontalScrollView hsv;
    @BindView(R.id.textView64)
    TextView textView64;
    @BindView(R.id.ivEmptyContent)
    ImageView ivEmptyContent;
    @BindView(R.id.tvEmptyContent)
    TextView tvEmptyContent;
    @BindView(R.id.clView)
    ConstraintLayout clView;
    @BindView(R.id.select_iv)
    ImageView selectIv;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.iv_toCoinInfo)
    ImageView ivToCoinInfo;
    @BindView(R.id.rb_number1)
    RadioButton rbNumber1;
    @BindView(R.id.rb_number2)
    RadioButton rbNumber2;
    @BindView(R.id.rb_number3)
    RadioButton rbNumber3;
    @BindView(R.id.rb_number4)
    RadioButton rbNumber4;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.rcv)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;


    private LoadingWindow mLoadingWindow;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private WeiTuoListAdapter mWeiTuoListAdapter;
    private SellAdapter sellAdapter;
    private BuyAdapter buyAdapter;
    private EntrustListAdapter entrustListAdapter;
    private TypeSelectAdapter mAdapter;


    private List<Map<String, Object>> mDataList3 = new ArrayList<>();
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<List<String>> mDataListBuy = new ArrayList<>();
    private List<List<String>> mDataListSell = new ArrayList<>();
    private List<Map<String, Object>> mDatas = new ArrayList<>();
    private List<Map<String, Object>> mtabsData = new ArrayList<>();
    private int precision = 2;

    List<Map<String,Object>> mapOneList = new ArrayList<>();

    private KindSelectDialog mDialog;

    private String mRequestTag = "";
    private String area ="USDT";
    private String symbol ="BTC";




    private DecimalFormat decimalFormat = new DecimalFormat();
    private DecimalFormat decimalFormat1 = new DecimalFormat();
    private DecimalFormat decimalFormat3 = new DecimalFormat();

    private int mPage = 1;
    private AnimUtil mAnimUtil;

    private WsManager wsManager;
    private Handler handler = new Handler();
    private String mSelectType = "0"; // 0 限价  1 市价
    private String mKindType = "0"; // 0 买入  1 卖出

    private String BTC_Account = "0";
    private String USDT_Account = "0";



    public BBTradeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_repayment;
    }


    /**
     * 只加载一次
     * -------------------------------------懒加载  start
     */
    boolean isViewInitiated;
    boolean isVisibleToUser;
    boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(true);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            //请求数据

            //查询交易区列表
            getAreaListAction();

            //查询交易区列表项
            getAreaListItemAction();

            //查询委托单
            getEntrustListAction();

            //账户当前交易区交易币可用
            getCurAreaAccountAction();

            setWebsocketListener();
            handler.post(runnable);
            LogUtilDebug.i("show", "BB懒加载数据");
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    /**
     * --------------------------------------懒加载     end
     */


    //开辟线程 轮询
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Map<String, Object> map = new HashMap<>();
            map.put("area", "USDT");
            map.put("method", "queryCoinDepth");
            map.put("symbol", "BTC");
            map.put("type", "1");

            //ws 关闭 连接深度ws
            if (!wsManager.isWsConnected()) {
                wsManager.startConnect();
            }

            wsManager.sendMessage(GsonUtils.toJson(map));
            try {
//                if (getCoinObject() == null) {
//                    // 如果查到有对应的币对, 添加对应币对的订阅
//                    currenctPriceWsParams.setArea(area);
//                    currenctPriceWsParams.setSymbol(symbol);
//                   LogUtilDebug.i("show","currenctPriceWsParams:"+GsonUtils.toJson(currenctPriceWsParams));
//                    currenctPrice16.sendMessage(GsonUtils.toJson(currenctPriceWsParams));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //每隔 0.5s 发送一次
            handler.postDelayed(this, MbsConstans.SECOND_TIME_500);
        }
    };

    @Override
    public void init() {
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
//        mTitleBarView.setLayoutParams(layoutParams);
//        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);
//        mTitleText.setText(getResources().getString(R.string.bottom_heyue));
//        mLeftBackLay.setVisibility(View.GONE);
        wsManager = BasicApplication.getWsManager();

        mAnimUtil = new AnimUtil();

        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        mLoadingWindow.showView();

        List<Map<String, Object>> mDataList2 = SelectDataUtil.getBBPriceType();
        mDialog = new KindSelectDialog(getActivity(), true, mDataList2, 10);
        mDialog.setSelectBackListener(this);

        initView();
        setBarTextColor();


        for (int i = 0; i < 15; i++) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", i);
            map.put("uid", i);
            map.put("symbol", "BTC");
            map.put("area", "郑州");
            map.put("price", "1000");
            map.put("average", "888");
            map.put("number", "26");
            map.put("surplus", "10");
            map.put("finish", "10");
            map.put("cancel", "6");
            map.put("total", "80999001");
            map.put("createTime", "2019/08/22 14:00");
            map.put("statusText", "交易中");
            map.put("directionText", "纵向");
            mDataList3.add(map);
        }

        rgBuySell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSell:
                        mKindType = "1";
                        tvOperateCoin.setText("卖出 BTC");
                        tvOperateCoin.setBackgroundResource(R.drawable.btn_next_red);
                        rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house3);
                        rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house3);

                        if (mSelectType.equals("0")){ //限价
                            clPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.GONE);
                            etPrice.setHint("价格");
                        }else { //市价
                            clPrice.setVisibility(View.GONE);
                            tvCnyPrice.setVisibility(View.VISIBLE);
                        }


                        break;
                    case R.id.rbBuy:
                        mKindType = "0";
                        tvOperateCoin.setText("买入 BTC");
                        tvOperateCoin.setBackgroundResource(R.drawable.btn_next_green);
                        rbNumber1.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber2.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber3.setBackgroundResource(R.drawable.selector_open_close_house2);
                        rbNumber4.setBackgroundResource(R.drawable.selector_open_close_house2);

                        if (mSelectType.equals("0")){ //限价
                            clPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.GONE);
                            etPrice.setHint("价格");
                        }else { //市价
                            clPrice.setVisibility(View.VISIBLE);
                            tvCnyPrice.setVisibility(View.GONE);
                            etPrice.setHint("金额");
                        }
                        break;
                }
            }
        });



        coinCoinSeekBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (UtilTools.empty(etPrice.getText().toString()) && UtilTools.empty(etPrice.getText().toString())){
                    showToastMsg("请输入价格");
                    return;
                }
                switch (checkedId){
                    case R.id.rb_number1: //10
                        if (mKindType.equals("0")){ //买入
                            float maxNumber = Float.parseFloat(USDT_Account)/Float.parseFloat(etPrice.getText().toString());
                            int number = (int) (maxNumber*0.1f);
                            etNumber.setText(number+"");
                        }else { //卖出
                            int number = (int) (Float.parseFloat(BTC_Account)*0.1f);
                            etNumber.setText(number+"");
                        }
                        break;
                    case R.id.rb_number2: //20
                        if (mKindType.equals("0")){ //买入
                            float maxNumber = Float.parseFloat(USDT_Account)/Float.parseFloat(etPrice.getText().toString());
                            int number = (int) (maxNumber*0.2f);
                            etNumber.setText(number+"");
                        }else { //卖出
                            int number = (int) (Float.parseFloat(BTC_Account)*0.2f);
                            etNumber.setText(number+"");
                        }
                        break;

                    case R.id.rb_number3: //50
                        if (mKindType.equals("0")){ //买入
                            float maxNumber = Float.parseFloat(USDT_Account)/Float.parseFloat(etPrice.getText().toString());
                            int number = (int) (maxNumber*0.5f);
                            etNumber.setText(number+"");
                        }else { //卖出
                            int number = (int) (Float.parseFloat(BTC_Account)*0.5f);
                            etNumber.setText(number+"");
                        }
                        break;

                    case R.id.rb_number4: //100
                        if (mKindType.equals("0")){ //买入
                            float maxNumber = Float.parseFloat(USDT_Account)/Float.parseFloat(etPrice.getText().toString());
                            int number = (int) (maxNumber*1.0f);
                            etNumber.setText(number+"");
                        }else { //卖出
                            int number = (int) (Float.parseFloat(BTC_Account)*1.0f);
                            etNumber.setText(number+"");
                        }
                        break;
                }
            }
        });

    }


//
//    @Override
//    public void onResume() {
//        super.onResume();
//       LogUtilDebug.i("show","onResume()");
//        if (getUserVisibleHint()) {
//            setWebsocketListener();
//            handler.post(runnable);
//        }
//    }

    private void setWebsocketListener() {
        if (!wsManager.isWsConnected()) {
            wsManager.startConnect();
        }
        wsManager.setWsStatusListener(wsStatusListener);
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);

        LinearLayoutManager manager = new LinearLayoutManager(getParentFragment().getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        rvEntrustDay.setHasFixedSize(true);
        rvEntrustDay.setNestedScrollingEnabled(false);
        entrustListAdapter = new EntrustListAdapter(getActivity());
        rvEntrustDay.setAdapter(entrustListAdapter);


        entrustListAdapter.setOrderList(mDataList3);

        rvSell.setHasFixedSize(true);
        rvSell.setNestedScrollingEnabled(false);
        sellAdapter = new SellAdapter(getActivity());
        rvSell.setAdapter(sellAdapter);
        sellAdapter.setOnItemClickListener(new BasicRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (sellAdapter.getBuySell().size() - 1 < position) {
                    etPrice.setText("0");
                } else {
                    List<String> strings = sellAdapter.getBuySell().get(position);
                    etPrice.setText(strings.get(0));
                }
            }
        });

        //sellAdapter.setSellTradeInfos(mDataListSell,precision);

        rvBuy.setHasFixedSize(true);
        rvBuy.setNestedScrollingEnabled(false);
        buyAdapter = new BuyAdapter(getActivity());
        rvBuy.setAdapter(buyAdapter);

        buyAdapter.setOnItemClickListener(new BasicRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (buyAdapter.getBuySell().size() - 1 < position) {
                    etPrice.setText("0");
                } else {
                    List<String> strings = buyAdapter.getBuySell().get(position);
                    etPrice.setText(strings.get(0));
                }
            }
        });

        // buyAdapter.setBuyTradeInfo(mDataListBuy,precision);


//        List<Map<String, Object>> tabs = SelectDataUtil.getTabValues3();
//        for (Map<String, Object> map : tabs) {
//            tlEntrust.addTab(tlEntrust.newTab().setText(map.get("name") + ""));
//        }
//        tlEntrust.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                // 委托信息
//                // getEntrustInfo(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                // 委托信息
//                //getEntrustInfo(tab.getPosition());
//            }
//        });

    }

    @OnClick({R.id.select_iv, R.id.select_tv, R.id.iv_toCoinInfo,R.id.tv_all,R.id.tvLimitPrice,R.id.tvOperateCoin})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.select_iv:
            case R.id.select_tv:
                initPopupWindow();
                break;
            case R.id.iv_toCoinInfo:
                intent = new Intent(getParentFragment().getActivity(), CoinInfoDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_all:
                intent = new Intent(getParentFragment().getActivity(), WeiTuoListActivity.class);
                startActivity(intent);
                break;
            case R.id.tvLimitPrice:
                mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tvOperateCoin:
                buyAndSellAction();
                break;
        }
    }

    private void  buyAndSellAction() {
        mRequestTag = MethodUrl.GUADAN_TRADE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token","a147ff5721babca2e7c7b976023af933");
        map.put("area",area);
        map.put("symbol",symbol);
        map.put("number",etNumber.getText()+"");
        map.put("direction",mKindType);
        map.put("type",mSelectType);
        if (mSelectType.equals("1") && mKindType.equals("0")){
            map.put("total",etPrice.getText()+"");
        }else {
            map.put("price",etPrice.getText()+"");
        }

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.GUADAN_TRADE, map);
    }


    private void getEntrustListAction() {
        mRequestTag = MethodUrl.ENTRUST_LIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(),MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("area",area);
        map.put("symbol",symbol);
        map.put("status","1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ENTRUST_LIST, map);

    }

    private void  getCurAreaAccountAction() {
        mRequestTag = MethodUrl.AREA_COIN_ACCOUNT;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(),MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("area",area);
        map.put("symbol",symbol);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.AREA_COIN_ACCOUNT, map);

    }



    private void getAreaListAction() {
        mRequestTag = MethodUrl.COIN_AREAALL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(),MbsConstans.ACCESS_TOKEN,"").toString();
        }
        //map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.COIN_AREAALL, map);

    }

    private void  getAreaListItemAction() {
        mRequestTag = MethodUrl.AREA_ITEM;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(),MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("area",area);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.AREA_ITEM, map);

    }





    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private XTabLayout tabLayout;
    private RecyclerView rcv;

    private void initPopupWindow() {

        if (mConditionDialog == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_bitype, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            initConditionDialog(popView);
            mConditionDialog.setClippingEnabled(false);

            int screenWidth = UtilTools.getScreenWidth(getActivity());
            int screenHeight = UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth((int) (screenWidth * 0.8));
            mConditionDialog.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            //设置background后在外点击才会消失
            // mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(),5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            mConditionDialog.setAnimationStyle(R.style.PopupAnimation);
            //mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),
                    Gravity.TOP | Gravity.LEFT, 0, 0);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        } else {
            mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),
                    Gravity.TOP | Gravity.LEFT, 0, 0);
            toggleBright();
        }
    }


    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        mAnimUtil.setValueAnimator(0.7f, 1f, 300);
        mAnimUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                float bgAlpha = bright ? progress : (1.7f - progress);//三目运算，应该挺好懂的。
                //bgAlpha = progress;//三目运算，应该挺好懂的。
                bgAlpha(bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        mAnimUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        mAnimUtil.startAnimator();
    }

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) getActivity()).getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private int tabPostion = 0;

    private void initConditionDialog(View view) {
        tabLayout = view.findViewById(R.id.tab_child);
        rcv = view.findViewById(R.id.rcv);
        LinearLayoutManager manager = new LinearLayoutManager(getParentFragment().getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcv.setLayoutManager(manager);
        if (mtabsData != null && mDatas != null ){
            for (Map<String,Object> map: mtabsData){
                tabLayout.addTab(tabLayout.newTab().setText(map.get("name")+""));
            }

            mAdapter = new TypeSelectAdapter(getParentFragment().getActivity(), mDatas, 10);
            rcv.setAdapter(mAdapter);
        }


        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                tabPostion = tab.getPosition();
                if (mtabsData != null && mtabsData.size()>0){
                    area = mtabsData.get(tabPostion).get("name")+"";

                    getAreaListItemAction();
                }

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });



        mAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
            @Override
            public void OnMyItemClickListener(View view, int position) {
                mConditionDialog.dismiss();
                symbol = mDatas.get(position).get("name")+"";
                selectTv.setText(area + "/" + symbol);

                getEntrustListAction();
            }
        });



    }


    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }



    private void responseData() {
        if (mWeiTuoListAdapter == null) {
            mWeiTuoListAdapter = new WeiTuoListAdapter(getActivity());
            mWeiTuoListAdapter.addAll(mDataList);

            AnimationAdapter adapter = new ScaleInAnimationAdapter(mWeiTuoListAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mWeiTuoListAdapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
                    .setHeight(R.dimen.dp_10)
                    .setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);


        } else {

            if (mPage == 1) {
                mWeiTuoListAdapter.clear();
            }
            mWeiTuoListAdapter.addAll(mDataList);
            mWeiTuoListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");

        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
            mPage++;
        }
        mRefreshListView.refreshComplete(10);
        mWeiTuoListAdapter.notifyDataSetChanged();

        if (mWeiTuoListAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }
    }


    @Override
    public void showProgress() {
        mLoadingWindow.show();
    }

    @Override
    public void disimissProgress() {
        mLoadingWindow.cancleView();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        //mPageView.showNetworkError();
        mLoadingWindow.cancleView();
        Intent intent;
        switch (mType) {
            case MethodUrl.ENTRUST_LIST:
                switch (tData.get("code")+""){
                    case "0":
                        mDataList = (List<Map<String, Object>>) tData.get("data");
                        if (mDataList != null && mDataList.size()>0){
                            for (Map<String,Object> map :mDataList){
                                map.put("status","1");
                            }
                            mPageView.showContent();
                            mapOneList.add(mDataList.get(0));
                            mDataList.clear();
                            mDataList.addAll(mapOneList);
                            responseData();

                        }else {
                            mPageView.showEmpty();
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;
            case MethodUrl.COIN_AREAALL:
                switch (tData.get("code")+""){
                    case "0":
                        mtabsData = (List<Map<String, Object>>) tData.get("data");
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;

            case MethodUrl.AREA_ITEM:
                switch (tData.get("code")+""){
                    case "0":
                        mDatas = (List<Map<String, Object>>) tData.get("data");
                        if (mAdapter != null && mDatas != null && rcv!= null){
                            mAdapter.setDatas(mDatas);
                            rcv.setAdapter(mAdapter);
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;

            case MethodUrl.AREA_COIN_ACCOUNT:
                switch ((tData.get("code") + "")) {
                    case "0":
                        Map<String,Object> mapData = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)){
                            BTC_Account = mapData.get("symbol")+"";
                            USDT_Account = mapData.get("area")+"";
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;
                    //mRefreshListView.refreshComplete(10);

            case MethodUrl.GUADAN_TRADE:
                switch ((tData.get("code") + "")) {
                    case "0":
                        showToastMsg(tData.get("msg")+"");
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                mLoadingWindow.showView();
                switch (mRequestTag) {
                    case MethodUrl.repaymentList:

                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mLoadingWindow.cancleView();
        switch (mType) {
            case MethodUrl.repaymentList:
//                if (mWeiTuoListAdapter != null) {
//                    if (mWeiTuoListAdapter.getDataList().size() <= 0) {
//                        mPageView.showNetworkError();
//                    } else {
//                        mPageView.showContent();
//                    }
//                    mRefreshListView.refreshComplete(10);
//                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
//                        @Override
//                        public void reload() {
//                            repaymentListAction();
//                        }
//                    });
//                }else {
//                    mPageView.showNetworkError();
//                }

                break;
        }
        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        mLoadingWindow.show();
        getEntrustListAction();
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10: //限价
                tvLimitPrice.setText(map.get("name") + "");
                if ((map.get("name") + "").equals("限价")) {
                    mSelectType = "0";
                    if(mKindType.equals("0")){ //买入
                        clPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.GONE);
                        etPrice.setHint("价格");
                    }else { //卖出
                        clPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.GONE);
                        etPrice.setHint("价格");
                    }


                } else {
                    mSelectType = "1";

                    if (mKindType.equals("0")){ //买入
                        clPrice.setVisibility(View.VISIBLE);
                        tvCnyPrice.setVisibility(View.GONE);
                        etPrice.setHint("金额");

                    }else { //卖出
                        clPrice.setVisibility(View.GONE);
                        tvCnyPrice.setVisibility(View.VISIBLE);
                        etPrice.setHint("价格");
                    }

                }
                break;

        }
    }

    //深度 ws 连接监听
    private WsStatusListener wsStatusListener = new WsStatusListener() {
        @Override
        public void onMessage(String text) {
            if (wsManager.getCurrentStatus() == WsStatus.CONNECTED) {
                Log.i("TAG", "深度 ws Message :" + text);
                try {
                    Map<String, Object> map = JSONUtil.getInstance().jsonMap(text);
                    String statusCode = map.get("status") + "";
                    //成功
                    if (!UtilTools.empty(statusCode) && statusCode.equals("1")) {
                        Map<String, Object> map1 = JSONUtil.getInstance().jsonMap(map.get("data") + "");

                        List<List<String>> mListBuy = JSONUtil.getInstance().jsonToListStr2(map1.get("buy") + "");
                        List<List<String>> mListSell = JSONUtil.getInstance().jsonToListStr2(map1.get("sell") + "");

                        // 设置深度信息
                        if (!UtilTools.empty(mListBuy) && mListBuy.size() > 0) {
                            mDataListBuy.clear();
                            for (List<String> strings : mListBuy) {
                                String price = strings.get(0);
                                String volume = strings.get(1);
                                decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                decimalFormat.setGroupingUsed(false);
                                String format;
                                if (precision >= 0) {
                                    decimalFormat.setMaximumFractionDigits(precision);
                                    format = decimalFormat.format(Double.parseDouble(price));
                                } else {
                                    decimalFormat.setMaximumFractionDigits(0);
                                    format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - precision).toString())), 0 - precision).toString();
                                }

                                if (mDataListBuy.size() > 0) {
                                    List<String> strings2 = mDataListBuy.get(mDataListBuy.size() - 1);
                                    String s = strings2.get(0);
                                    if (format.equals(s)) {
                                        strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                    } else {
                                        List<String> strings1 = new ArrayList<>();
                                        strings1.add(format);
                                        strings1.add(volume);
                                        mDataListBuy.add(strings1);
                                    }
                                } else {
                                    List<String> strings1 = new ArrayList<>();
                                    strings1.add(format);
                                    strings1.add(volume);
                                    mDataListBuy.add(strings1);
                                }
                            }
                            buyAdapter.setBuyTradeInfo(mDataListBuy, precision);
                        }

                        if (!UtilTools.empty(mListSell) && mListSell.size() > 0) {
                            mDataListSell.clear();
                            for (List<String> strings : mListSell) {
                                String price = strings.get(0);
                                String volume = strings.get(1);
                                decimalFormat.setRoundingMode(RoundingMode.CEILING);
                                decimalFormat.setGroupingUsed(false);
                                String format;
                                if (precision >= 0) {
                                    decimalFormat.setMaximumFractionDigits(precision);
                                    format = decimalFormat.format(Double.parseDouble(price));
                                } else {
                                    decimalFormat.setMaximumFractionDigits(0);
                                    format = BigDecimalUtils.mul(decimalFormat.format(Double.parseDouble(BigDecimalUtils.divide(price, RoundingMode.CEILING, 0 - precision).toString())), 0 - precision).toString();
                                }

                                if (mDataListSell.size() > 0) {
                                    List<String> strings2 = mDataListSell.get(mDataListSell.size() - 1);
                                    String s = strings2.get(0);
                                    if (format.equals(s)) {
                                        strings2.add(1, BigDecimalUtils.add(strings2.get(1), volume).toString());
                                    } else {
                                        List<String> strings1 = new ArrayList<>();
                                        strings1.add(format);
                                        strings1.add(volume);
                                        mDataListSell.add(strings1);
                                    }
                                } else {
                                    List<String> strings1 = new ArrayList<>();
                                    strings1.add(format);
                                    strings1.add(volume);
                                    mDataListSell.add(strings1);
                                }
                            }
                            sellAdapter.setSellTradeInfos(mDataListSell, precision);
                        }


                    }
                } catch (Exception e) {

                }
            }
        }
    };


    public void restartWs() {
        handler.removeCallbacks(runnable);

        setWebsocketListener();
        handler.post(runnable);
    }


    public void stopWs() {
        handler.removeCallbacks(runnable);
        if (wsManager.getWebSocket() != null) {
            wsManager.getWebSocket().cancel();
        }
        wsManager.stopConnect();


    }

    @Override
    public void onPause() {
        LogUtilDebug.i("show", "Pause()");
        super.onPause();
        wsManager.stopConnect();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        handler.removeCallbacks(runnable);
        if (getUserVisibleHint()) {
            setWebsocketListener();
            handler.post(runnable);
        } else {
            if (wsManager.getWebSocket() != null) {
                wsManager.getWebSocket().cancel();
            }
            wsManager.stopConnect();
        }


    }


}
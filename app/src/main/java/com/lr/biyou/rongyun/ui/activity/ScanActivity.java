package com.lr.biyou.rongyun.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.lr.biyou.R;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.model.Status;
import com.lr.biyou.rongyun.qrcode.SealQrCodeUISelector;
import com.lr.biyou.rongyun.ui.view.SealTitleBar;
import com.lr.biyou.rongyun.utils.NetworkUtils;
import com.lr.biyou.rongyun.utils.PhotoUtils;
import com.lr.biyou.rongyun.utils.ToastUtils;
import com.lr.biyou.rongyun.utils.log.SLog;
import com.lr.biyou.rongyun.utils.qrcode.QRCodeUtils;
import com.lr.biyou.rongyun.utils.qrcode.barcodescanner.BarcodeResult;
import com.lr.biyou.rongyun.utils.qrcode.barcodescanner.CaptureManager;
import com.lr.biyou.rongyun.utils.qrcode.barcodescanner.DecoratedBarcodeView;


/**
 * 扫一扫界面
 */
public class ScanActivity extends TitleBaseActivity implements View.OnClickListener {
    private final static String TAG = "ScanActivity";
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private TextView lightControlTv;
    private TextView selectPicTv;
    private TextView tipsTv;
    private PhotoUtils photoUtils;

    private boolean isCameraLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SealTitleBar titleBar = getTitleBar();
        titleBar.setTitle(R.string.seal_main_title_scan);
        titleBar.setOnBtnRightClickListener(getString(R.string.common_album), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromAlbum();
            }
        });

        initView(savedInstanceState);

        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                String result = QRCodeUtils.analyzeImage(uri.getPath());
                handleQrCode(result);
            }

            @Override
            public void onPhotoCancel() {
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        barcodeScannerView = initializeContent();
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setOnCaptureResultListener(new CaptureManager.OnCaptureResultListener() {
            @Override
            public void onCaptureResult(BarcodeResult result) {
                handleQrCode(result.toString());
            }
        });

        barcodeScannerView.getViewFinder().networkChange(!NetworkUtils.isNetWorkAvailable(this));
        if (!NetworkUtils.isNetWorkAvailable(this)) {
            capture.stopDecode();
        } else {
            capture.decode();
        }
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                lightControlTv.setText(R.string.zxing_close_light);
                isCameraLightOn = true;
            }

            @Override
            public void onTorchOff() {
                lightControlTv.setText(R.string.zxing_open_light);
                isCameraLightOn = false;
            }
        });
        lightControlTv = findViewById(R.id.zxing_open_light);
        lightControlTv.setOnClickListener(this);
        selectPicTv = findViewById(R.id.zxing_select_pic);
        selectPicTv.setOnClickListener(this);
        tipsTv = findViewById(R.id.zxing_user_tips);
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.zxing_capture);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    /**
     * 切换摄像头照明
     */
    private void switchCameraLight() {
        if (isCameraLightOn) {
            barcodeScannerView.setTorchOff();
        } else {
            barcodeScannerView.setTorchOn();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_open_light:
                switchCameraLight();
                break;
            case R.id.zxing_select_pic:
                scanFromAlbum();
                break;
        }
    }

    /**
     * 从相册中选中
     */
    public void scanFromAlbum() {
        photoUtils.selectPicture(this);
    }

    /**
     * 处理二维码结果，并跳转到相应界面
     *
     * @param qrCodeText
     */
    private void handleQrCode(String qrCodeText) {
        if (TextUtils.isEmpty(qrCodeText)) {
            SLog.d(TAG, "scanner result is null");
            ToastUtils.showToast(R.string.zxing_qr_can_not_recognized);
            return;
        }

        // 处理二维码结果
        SealQrCodeUISelector uiSelector = new SealQrCodeUISelector(this);
        LiveData<Resource<String>> resourceLiveData = uiSelector.handleUri(qrCodeText);
        resourceLiveData.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if(resource.status != Status.LOADING) {
                    resourceLiveData.removeObserver(this);
                }

                if(resource.status == Status.SUCCESS){
                    finish();
                } else if(resource.status == Status.ERROR){
                    ToastUtils.showToast(resource.data);
                    barcodeScannerView.getViewFinder().setAllowScanAnimation(false);
                    lightControlTv.setVisibility(View.INVISIBLE);
                    tipsTv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoUtils.onActivityResult(this, requestCode, resultCode ,data);
    }
}

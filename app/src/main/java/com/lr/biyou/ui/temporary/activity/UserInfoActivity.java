package com.lr.biyou.ui.temporary.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AppUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Permission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 用户基本信息
 */
public class UserInfoActivity extends BasicActivity implements RequestView{
    @BindView(R.id.head_img_lay)
    CardView mHeadImgLay;
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.head_img_view)
    CircleImageView mHeadImageView;
    @BindView(R.id.more_info_lay)
    CardView mMoreInfoLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.phone_tv)
    TextView mPhoneTv;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.idcard_value_tv)
    TextView mIdcardValueTv;
    @BindView(R.id.phone_tv2)
    TextView mPhoneTv2;
    @BindView(R.id.logout_lay)
    CardView mLogoutLay;
    @BindView(R.id.more_info_line)
    View mInfoLine;

    private String mRequestTag = "";

    private Map<String,Object> mHeadPath;

    @Override
    public int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.base_msg));

        if (MbsConstans.USER_MAP != null && !MbsConstans.USER_MAP.isEmpty()){
            String moreInfo = MbsConstans.USER_MAP.get("cmpl_info")+"";
            if (moreInfo.equals("1")){//是否完善信息（0：未完善，1：已完善）
                mMoreInfoLay.setVisibility(View.VISIBLE);
                mInfoLine.setVisibility(View.VISIBLE);
            }else {
                mMoreInfoLay.setVisibility(View.GONE);
                mInfoLine.setVisibility(View.GONE);
            }

            mPhoneTv.setText(MbsConstans.USER_MAP.get("tel")+"");
            mPhoneTv2.setText(MbsConstans.USER_MAP.get("tel")+"");
            mNameTv.setText(MbsConstans.USER_MAP.get("name")+"");
            mIdcardValueTv.setText(MbsConstans.USER_MAP.get("idno")+"");

            initHeadPic();
        }else {
            getUserInfoAction();
        }

        // GlideUtils.loadImage(UserInfoActivity.this,"http://tupian.qqjay.com/u/2017/1201/2_161641_2.jpg",mHeadImageView);


    }

    /**
     * 获取用户信息
     */
    private void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;
        Map<String, Object> map = new HashMap<>();
        Map<String,String> mHeaderMap = new HashMap<String,String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }
    private void logoutAction() {
        mRequestTag = MethodUrl.LOGIN_ACTION;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestDeleteToMap(mHeaderMap, MethodUrl.LOGIN_ACTION, map);
    }

    private void initHeadPic(){
        String headUrl = MbsConstans.USER_MAP.get("head_pic")+"";
        if (headUrl.contains("http")){
        }else {
            headUrl = MbsConstans.PIC_URL+headUrl;
        }
        GlideUtils.loadImage2(UserInfoActivity.this,headUrl,mHeadImageView,R.drawable.head);
    }


    @OnClick({R.id.head_img_lay, R.id.back_img, R.id.more_info_lay,R.id.logout_lay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_img_lay:
//                intent = new Intent(UserInfoActivity.this,ModifyFileActivity.class);
//                startActivity(intent);
                ActionSheetDialogNoTitle();
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.more_info_lay:
                String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
                if (kind.equals("1")) {
                    intent = new Intent(UserInfoActivity.this, QiyeInfoShowActivity.class);
                    intent.putExtra("backtype","2");
                    startActivity(intent);
                }else {
                    intent = new Intent(UserInfoActivity.this, MoreInfoManagerActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.logout_lay:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(R.string.title_dialog)
                        .setMessage("确定要退出登录吗？")
                        .setPositiveButton(R.string.but_sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutAction();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();


                break;
        }
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"从相册选择", "拍照"};
        final ActionSheetDialog dialog = new ActionSheetDialog(UserInfoActivity.this, stringItems, null);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:

                        PermissionsUtils.requsetRunPermission(UserInfoActivity.this, new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                toast(R.string.successfully);
                                localPic();
                            }

                            @Override
                            public void requestFailer() {
                                toast(R.string.failure);
                            }
                        },Permission.Group.STORAGE);
                        break;
                    case 1:

                        PermissionsUtils.requsetRunPermission(UserInfoActivity.this, new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                toast(R.string.successfully);
                                photoPic();
                            }

                            @Override
                            public void requestFailer() {
                                toast(R.string.failure);
                            }
                        },Permission.Group.STORAGE,Permission.Group.CAMERA);
                        break;
                }

            }
        });
    }

    private void photoPic() {
        /**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的，这个地方也错了，必须改正
         */
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(UserInfoActivity.this, AppUtil.getAppProcessName(this)+".fileProvider", new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        } else {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    private void localPic() {

        /**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         */

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }


    private Intent dataIntent = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri = null;
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }

                //			if (data==null) {
                //				return;
                //			}
                //			uri = data.getData();
                //			String[] proj = { MediaStore.Images.Media.DATA };
                //			Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null,null);
                //			//Cursor cursor =
                //			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //			cursor.moveToFirst();
                //			path = cursor.getString(column_index);// 图片在的路径
                //			Intent intent3=new Intent(getActivity(), ClipActivity.class);
                //			intent3.putExtra("path", path);
                //			startActivityForResult(intent3, 4);
                break;
            // 如果是调用相机拍照时
            case 2:

                File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
                if (temp.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(UserInfoActivity.this, AppUtil.getAppProcessName(UserInfoActivity.this)+".fileProvider", temp);
                    } else {
                        uri = Uri.fromFile(temp);
                    }
                    startPhotoZoom(uri);
                }

                //			path=photoSavePath+photoSaveName;
                //			uri = Uri.fromFile(new File(path));
                //			Intent intent2=new Intent(getActivity(), ClipActivity.class);
                //			intent2.putExtra("path", path);
                //			startActivityForResult(intent2, 4);
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只
                 * 在这个地方加下，大家可以根据不同情况在合适的
                 * 地方做判断处理类似情况
                 *
                 */
            /*    Toast.makeText(BusinessInfoActivity.this,"EEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",Toast.LENGTH_LONG).show();
                if(data != null){
                    Toast.makeText(BusinessInfoActivity.this,"QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ",Toast.LENGTH_LONG).show();
                    dataIntent = data;
                    setPicToView(data);
                }else {
                    Toast.makeText(BusinessInfoActivity.this,"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",Toast.LENGTH_LONG).show();

                }
*/

                // 将Uri图片转换为Bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    // TODO，将裁剪的bitmap显示在imageview控件上
                    Drawable dr = new BitmapDrawable(getResources(),bitmap);
                    saveCroppedImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                //ImageLoader.getmContext().displayImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage);
                // UtilTools.showImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage, R.drawable.no_def);

                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(),photo);
            uploadPic();
        }
    }

    private void uploadPic() {
        if (dataIntent != null) {
            Bundle extras = dataIntent.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo == null) {
                    return;
                }
                saveCroppedImage(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);

            }
        } else {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
        }
    }


    private String mHeadImgPath = "";
    private void saveCroppedImage(Bitmap bmp) {

        try {
            File saveFile = new File(MbsConstans.HEAD_IMAGE_PATH);

            mHeadImgPath = MbsConstans.HEAD_IMAGE_PATH + new Date().getTime() + ".png";
            File file = new File(mHeadImgPath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            File saveFile2 = new File(mHeadImgPath);

            FileOutputStream fos = new FileOutputStream(saveFile2);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // uploadAliPic(new Date().getTime()+".png",filepath);

            uploadPicAction();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void uploadPicAction() {
        mRequestTag = MethodUrl.UPLOAD_FILE;
        Map<String, Object> signMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("file",mHeadImgPath);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.UPLOAD_FILE,signMap, map,fileMap);
    }
    private void submitPicPath() {
        mRequestTag = MethodUrl.headPath;
        Map<String, String> map = new HashMap<>();
        map.put("remotepath",mHeadPath.get("remotepath")+"");
        map.put("filemd5",mHeadPath.get("filemd5")+"");
        Map<String, String> mHeaderMap = new HashMap<>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.headPath, map);
    }


    private String imgName = "";
    private Uri uritempFile;

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
    /*    Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);*/
        File file = new File(MbsConstans.BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        // intent.putExtra("noFaceDetection", true);
        // 宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);

        // intent.putExtra("scale", true);
        // intent.putExtra("return-data", true);
        // this.startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        imgName = System.currentTimeMillis() + ".jpg";
        uritempFile = Uri.parse("file:///"  + MbsConstans.BASE_PATH + "/" + imgName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);

    }







    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




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
        Intent intent;
        switch (mType) {
            case MethodUrl.USER_INFO:
                MbsConstans.USER_MAP = tData;
                SPUtils.put(UserInfoActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO, JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                initHeadPic();
                break;
            case MethodUrl.headPath:
                showToastMsg("上传头像成功");
                getUserInfoAction();
                intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
                sendBroadcast(intent);
                break;
            case MethodUrl.UPLOAD_FILE:
                mHeadPath = tData;
                submitPicPath();
                break;
            case MethodUrl.LOGIN_ACTION://{verify_type=FACE, state=0}
                closeAllActivity();
                MbsConstans.USER_MAP = null;
                MbsConstans.REFRESH_TOKEN = "";
                MbsConstans.ACCESS_TOKEN = "";
                SPUtils.put(UserInfoActivity.this, MbsConstans.SharedInfoConstans.LOGIN_OUT, true);
                intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.LOGIN_ACTION:
                        logoutAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);

    }
}
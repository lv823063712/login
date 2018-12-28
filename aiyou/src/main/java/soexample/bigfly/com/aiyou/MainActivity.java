package soexample.bigfly.com.aiyou;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soexample.bigfly.com.aiyou.bean.LoginBean;
import soexample.bigfly.com.aiyou.bean.UserInfoBean;
import soexample.bigfly.com.aiyou.ipresenter.IPresenterImpl;
import soexample.bigfly.com.aiyou.iview.IView;

public class MainActivity extends AppCompatActivity implements IView {

    private static final int TYPE_LOGIN = 0;
    private static final int TYPE_UPLOAD_IMG = TYPE_LOGIN + 1;
    private static final int TYPE_GET_USER_INFO = TYPE_UPLOAD_IMG + 1;
    @BindView(R.id.et_main_name)
    EditText etMainName;
    @BindView(R.id.et_main_pw)
    EditText etMainPw;
    @BindView(R.id.button_main_login)
    Button buttonMainLogin;
    @BindView(R.id.button_main_get_user_info)
    Button buttonMainGetUserInfo;
    private IPresenterImpl iPresenter;

    /**
     * Retrofit 2 基于OKHTTP
     * 他更适合应用到企业开发
     * Rajava里面重点咱们需要知道
     * Observer 观察者
     * Observable 被观察者
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //进行注解绑定
        ButterKnife.bind(this);
        iPresenter = new IPresenterImpl(this);
    }

    @OnClick({R.id.button_main_login, R.id.button_main_get_user_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_main_login:
                checkPermission(TYPE_LOGIN);
                break;
            case R.id.button_main_get_user_info:
                checkPermission(TYPE_GET_USER_INFO);
                break;
        }
    }

    private void checkPermission(int type) {
        //版本进行判断,更新版本必须大于之前版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //进行判断  自我检查权限是否开启  ,     网络权限不等于没有权限,然后执行下面的代码
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                //兼容       请求的权限     将请求的权限放入到集合中
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, type);
            } else {
                startRequest(type);
            }
        } else {
            startRequest(type);
        }
    }

    private void startRequest(int type) {
        switch (type) {
            case TYPE_LOGIN: // 点击进行取值 并将值放入到map集合中
                Map<String, String> map = new HashMap<>();
                map.put("mobile", etMainName.getText().toString().trim());
                map.put("password", etMainPw.getText().toString().trim());
                //此处将登录  拼接 进行解析
                iPresenter.startRequest(Contacts.USER_LOGIN, map, LoginBean.class);
                break;
            case TYPE_GET_USER_INFO:
                //此处将uid添加进map中
                Map<String, String> map1 = new HashMap<>();
                map1.put("uid", "23489");
                //此处将获取详细信息  拼接 进行解析
                iPresenter.startRequest(Contacts.USER_INFO, map1, UserInfoBean.class);

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //进行判断  如果没有获取权限  那就将回应码添加进去
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRequest(requestCode);
        }
    }

    @Override
    public void onSuccess(Object data) {
        //因为是复用一个处理的  用java的一个关键字instanceof处理一下即可
        if (data instanceof LoginBean) {
            Toast.makeText(this, ((LoginBean) data).getMsg(), Toast.LENGTH_SHORT).show();
        } else if (data instanceof UserInfoBean) {
            UserInfoBean bean = (UserInfoBean) data;
            Toast.makeText(this, bean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iPresenter != null) {
            iPresenter.onDatacth();
            iPresenter = null;
        }
    }
}

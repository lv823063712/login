package soexample.bigfly.com.uploadimg;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.UpLoad_Btn)
    Button UpLoadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.UpLoad_Btn)
    public void onViewClicked() {
        //创建一个hashmap
        Map<String, String> map = new HashMap<>();
        map.put("uid", "23585");      //创建图片的引入
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/e.jpg");
        //MediaType:图片的类型定义  parse解析
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        //上传图片需要 MultipartBody
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        //创建工具类
        RetrofitUtils.getInstance().upLoadImage(Contacts.UP_LOAD_IMAGE, map, body).setHttpListrner(new RetrofitUtils.HttpListrner() {
            @Override
            public void onSuccess(String jsonStr) {
                Log.e("onSuccess", jsonStr);
            }

            @Override
            public void onError(String Error) {
                Log.e("onSuccess", Error);
            }
        });
    }
}

package soexample.bigfly.com.uploadimg;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/28<p>
 * <p>更改时间：2018/12/28<p>
 * <p>版本号：1<p>
 */

public class RetrofitUtils {

    private final ApiService myApiService;

    private RetrofitUtils() {
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                //日志拦截
                .addInterceptor(loggingInterceptor)
                //连接失败时重试  true
                .retryOnConnectionFailure(true).build();
        //更新设置
        Retrofit retrofit = new Retrofit.Builder()
                //添加调用适配器工厂    Rx Java调用适配器工厂进行创建
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //添加转换器工厂     解析转换工厂进行创建
                .addConverterFactory(GsonConverterFactory.create())
                //将网址添加
                .baseUrl(Contacts.BASE_URL)
                .client(okHttpClient)
                .build();

        myApiService = retrofit.create(ApiService.class);
    }

    public static RetrofitUtils getInstance() {
        return RetrofitHolder.retro;
    }

    public static class RetrofitHolder {
        private static final RetrofitUtils retro = new RetrofitUtils();
    }

    public RetrofitUtils upLoadImage(String url, Map<String, String> map, MultipartBody.Part body) {
        myApiService.upLoadImage(url, map, body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }

    private Observer observer = new Observer<ResponseBody>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (httpListrner != null) {
                httpListrner.onError(e.getMessage());
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            if (httpListrner != null) {
                try {
                    httpListrner.onSuccess(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //写一个接口回调
    public interface HttpListrner {
        void onSuccess(String jsonStr);

        void onError(String Error);
    }

    private HttpListrner httpListrner;

    public void setHttpListrner(HttpListrner httpListrner) {
        this.httpListrner = httpListrner;
    }

}

package soexample.bigfly.com.aiyou.network;


import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import soexample.bigfly.com.aiyou.Contacts;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public class RetrofitUtils {

    private MyApiService myApiService;

    private RetrofitUtils() {
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //设置等级  BODY主要的:主题
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //创建一个新的httpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设定超市事件等
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //配置此客户端，以便在遇到连接问题时重试或不重试。默认情况下，
                //该客户端从以下问题中悄悄恢复
                .retryOnConnectionFailure(true)
                .build();
        //初始化Retrofit 并结合各种操作
        Retrofit retrofit = new Retrofit.Builder()
                //结合Gson解析
                .addConverterFactory(GsonConverterFactory.create())
                //结合Rxjava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Contacts.BASE_URL)
                .client(okHttpClient)
                .build();
        //通过Retrofit创建完 这个ApiService 就可以调用方法了
        myApiService = retrofit.create(MyApiService.class);
    }

    //
    public static RetrofitUtils getInstance() {
        return RetrofitHolder.retro;
    }

    public static class RetrofitHolder {
        private static final RetrofitUtils retro = new RetrofitUtils();
    }

    //封装Get方式  这里面采用构造者模式  就是调用这个方法有返回自己本身这个对象
    public RetrofitUtils get(String url, Map<String, String> map) {
        //这个订阅事件（处理网络请求）放生那个线程
        //Schedulers 线程调度器
        myApiService.get(url, map).subscribeOn(Schedulers.io())
                //io就是子线程   在主线程调用  observeOn观察者
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }
    /*表单post请求*/
    public RetrofitUtils post(String url, Map<String, String> map) {

        if (map == null) {
            map = new HashMap<>();

        }
        myApiService.post(url, map)
                //io子线程
                .subscribeOn(Schedulers.io())
                //主线程调用
                .observeOn(AndroidSchedulers.mainThread())
                //订阅
                .subscribe(observer);
        return RetrofitUtils.getInstance();
    }

    //子类使用
    /*private Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ResponseBody responseBody) {

        }
    };*/

    //重写一个观察者对象
    private Observer observer = new Observer<ResponseBody>() {
        @Override
        public void onCompleted() {

        }

        //网络处理失败
        @Override
        public void onError(Throwable e) {
            if (httpListener != null) {
                httpListener.onError(e.getMessage());
            }
        }

        //网络处理成功
        @Override
        public void onNext(ResponseBody responseBody) {
            if (httpListener != null) {
                try {
                    //成功后回调
                    httpListener.onSuccess(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    //写一个回调接口
    public interface HttpListener {
        void onSuccess(String jsonStr);

        void onError(String error);
    }

    private HttpListener httpListener;

    public void setHttpListener(HttpListener listener) {
        this.httpListener = listener;
    }


}

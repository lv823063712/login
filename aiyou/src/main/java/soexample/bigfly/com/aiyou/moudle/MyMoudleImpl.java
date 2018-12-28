package soexample.bigfly.com.aiyou.moudle;

import com.google.gson.Gson;

import java.util.Map;

import soexample.bigfly.com.aiyou.callback.MyCallBack;
import soexample.bigfly.com.aiyou.network.RetrofitUtils;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public class MyMoudleImpl implements MyMoudle {

    @Override
    public void requestData(String url, Map<String, String> params, final Class clazz, final MyCallBack callBack) {
        //建造者模式
        RetrofitUtils.getInstance().post(url, params).setHttpListener(new RetrofitUtils.HttpListener() {
            @Override
            public void onSuccess(String jsonStr) {
                //进行解析
                Gson gson = new Gson();
                Object o = gson.fromJson(jsonStr, clazz);
                callBack.onSuccess(o);

            }

            @Override
            public void onError(String error) {
                callBack.onFail(error);
            }
        });
    }
}

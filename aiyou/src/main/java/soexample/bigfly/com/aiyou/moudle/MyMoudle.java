package soexample.bigfly.com.aiyou.moudle;

import java.util.Map;

import soexample.bigfly.com.aiyou.callback.MyCallBack;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public interface MyMoudle {
    void requestData(String url, Map<String, String> params, Class clazz, MyCallBack callBack);
}

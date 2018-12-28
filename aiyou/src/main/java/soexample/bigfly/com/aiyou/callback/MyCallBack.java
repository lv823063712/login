package soexample.bigfly.com.aiyou.callback;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public interface MyCallBack<T> {
    void onSuccess(T t);

    void onFail(String error);
}

package soexample.bigfly.com.aiyou.ipresenter;

import java.util.Map;

import soexample.bigfly.com.aiyou.callback.MyCallBack;
import soexample.bigfly.com.aiyou.iview.IView;
import soexample.bigfly.com.aiyou.moudle.MyMoudleImpl;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public class IPresenterImpl implements IPresenter {

    private IView iView;
    private MyMoudleImpl myMoudle;

    public IPresenterImpl(IView iView) {
        this.iView = iView;
        myMoudle = new MyMoudleImpl();
    }

    @Override
    public void startRequest(String url, Map<String, String> params, Class clazz) {
        myMoudle.requestData(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                iView.onSuccess(o);
            }

            @Override
            public void onFail(String error) {
                iView.onError(error);
            }
        });
    }

    //防止内存泄漏
    public void onDatacth() {
        if (myMoudle != null) {
            myMoudle = null;
        }

        if (iView != null) {
            iView = null;
        }
    }

}

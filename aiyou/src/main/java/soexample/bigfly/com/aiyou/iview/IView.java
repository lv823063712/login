package soexample.bigfly.com.aiyou.iview;

public interface IView<T> {
    void onSuccess(T data);

    void onError(String error);
}

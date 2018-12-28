package soexample.bigfly.com.uploadimg;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/28<p>
 * <p>更改时间：2018/12/28<p>
 * <p>版本号：1<p>
 */

public interface ApiService {
    @Multipart
    @POST
    //此处导包需要为rx的ob包,否则开启子线程的方法会报错
    Observable<ResponseBody> upLoadImage(@Url String url, @QueryMap Map<String, String> map, @Part MultipartBody.Part file);
}

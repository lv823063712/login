package soexample.bigfly.com.aiyou.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * <p>文件描述：<p>
 * <p>作者：${lvf}<p>
 * <p>创建时间：2018/12/27<p>
 * <p>更改时间：2018/12/27<p>
 * <p>版本号：1<p>
 */

public class SqUtils {

    private static final String SP_NAME = "lf";

    /**
     * 保存
     *
     * @param context 上下文
     * @param key     key值
     * @param value   value值
     */
    public static void save(Context context, String key, String value) {
        //此处进行存储值
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, MODE_PRIVATE);
        //使用sp  存储完值后进行存储
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param key
     * @param defultValue 默认值
     * @return
     */
    public static String getString(Context context, String key, String defultValue) {
        //获取sp里面的值
        return context.getSharedPreferences(SP_NAME, MODE_PRIVATE).getString(key, defultValue);
    }

}

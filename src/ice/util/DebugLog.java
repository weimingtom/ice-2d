package ice.util;

import android.util.Log;

/**
 * User: Mike.Hu
 * Date: 12-3-6
 * Time: 下午3:47
 */
public class DebugLog {


    public static String TAG = "test";
    public static boolean debugState = true;

    public static void v(String text) {

        if (debugState) {
            Log.v(TAG, TAG + " " + text);
        }
    }

    public static void v(String tag, String text) {

        if (debugState) {
            Log.v(tag, tag + " " + text);
        }
    }
}

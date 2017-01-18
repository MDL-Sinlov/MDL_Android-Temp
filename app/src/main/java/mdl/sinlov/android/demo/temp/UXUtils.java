package mdl.sinlov.android.demo.temp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * UX Utils for Fast request default time is 3000 ms, you can set custom time and isCustomTime be true
 * <pre>
 *     sinlov
 *
 *     /\__/\
 *    /`    '\
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!
 *    \  --  /
 *   /        \
 *  /          \
 * |            |
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * </pre>
 * Created by sinlov on 17/1/17.
 */
public final class UXUtils {
    public static boolean DEBUG = false;
    public static boolean isShowDefaultUXMessage = true;
    public static final long DEFAULT_TIME_SHOT = 3000;
    private static long customTime = 5000;
    private static String TAG = "UXUtils";
    private static StringBuffer sb = new StringBuffer();

    private static long lastClickTime;
    private static long oneTime;

    private static long twoTime;
    private static String oldMsg;
    private static Toast toast;
    private static ProgressDialog pd;

    public static void setIsShowDefaultUXMessage(boolean isShowDefaultUXMessage) {
        UXUtils.isShowDefaultUXMessage = isShowDefaultUXMessage;
    }

    /**
     * default time is 5000 ms
     *
     * @param customTime long
     */
    public static void setCustomTime(long customTime) {
        UXUtils.customTime = customTime;
    }

    public static boolean fastRequest() {
        return fastRequest(false);
    }

    public static boolean fastRequest(boolean isCustomTime) {
        return fastRequest(isCustomTime, null, null);
    }

    public static boolean fastRequest(Context ctx) {
        return fastRequest(false, ctx, null);
    }

    public static boolean fastRequest(Context ctx, String toastMsg) {
        return fastRequest(false, ctx, toastMsg);
    }

    public static boolean fastRequest(boolean isCustomTime, Context ctx, String toastMsg) {
        long checkTime = System.currentTimeMillis() - UXUtils.lastClickTime;
        printMultiLog("fastRequest", "checkTime: " + checkTime);
        long betweenTIme = isCustomTime ? customTime : DEFAULT_TIME_SHOT;
        if (checkTime < betweenTIme) {
            if (null != ctx) {
                if (!TextUtils.isEmpty(toastMsg)) {
                    showSingleToast(ctx.getApplicationContext(), toastMsg);
                } else {
                    if (isShowDefaultUXMessage) {
                        showSingleToast(ctx.getApplicationContext(), R.string.toast_fast_request);
                    }
                }
            }
            printMultiLog("fastRequest", "true time: " + UXUtils.lastClickTime, "between: " + checkTime);
            return true;
        } else {
            UXUtils.lastClickTime = System.currentTimeMillis();
            printMultiLog("fastRequest", "false time: " + UXUtils.lastClickTime);
            return false;
        }
    }

    public static boolean fastRequestShowProgressDialog(Context ctx) {
        return fastRequestShowProgressDialog(ctx, false);
    }

    public static boolean fastRequestShowProgressDialog(Context ctx, String msg) {
        return fastRequestShowProgressDialog(ctx, msg, false);
    }

    public static boolean fastRequestShowProgressDialog(Context ctx, boolean isCustomTime) {
        return fastRequestShowProgressDialog(ctx, null, isCustomTime);
    }

    public static boolean fastRequestShowProgressDialog(Context ctx, String msg, boolean isCustomTime) {
        long checkTime = System.currentTimeMillis() - UXUtils.lastClickTime;
        printMultiLog("fastRequestShowProgressDialog", "checkTime: " + checkTime);
        long betweenTIme = isCustomTime ? customTime : DEFAULT_TIME_SHOT;
        if (checkTime < betweenTIme) {
            printMultiLog("fastRequestShowProgressDialog", "checkTime: " + checkTime, "betweenTime: " + betweenTIme);
            try {
                if (null != ctx) {
                    if (pd == null) {
                        pd = new ProgressDialog(ctx);
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        if (TextUtils.isEmpty(msg)) {
                            if (isShowDefaultUXMessage) {
                                pd.setMessage(ctx.getString(R.string.dialog_fast_request));
                            }
                        } else {
                            pd.setMessage(msg);
                        }
                        pd.setCancelable(false);
                        pd.setCanceledOnTouchOutside(false);
                        showProgressDialog2AutoClose(betweenTIme);
                    } else if (pd.isShowing()) {
                        showProgressDialog2AutoClose(betweenTIme);
                    }
                } else {
                    printMultiLog("show fastRequestShowProgressDialog null context");
                }
            } catch (Exception e) {
                printMultiLog("show fastRequestShowProgressDialog error");
                e.printStackTrace();
            }
            return true;
        } else {
            UXUtils.lastClickTime = System.currentTimeMillis();
            printMultiLog("fastRequestShowProgressDialog", "false time: " + UXUtils.lastClickTime);
            return false;
        }
    }

    private static void showProgressDialog2AutoClose(long betweenTIme) {
        pd.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                printMultiLog("fastRequestShowProgressDialog", "auto close start");
                if (pd != null) {
                    printMultiLog("fastRequestShowProgressDialog", "do auto close");
                    pd.hide();
                }
                printMultiLog("fastRequestShowProgressDialog", "auto close end");
            }
        }, betweenTIme);
    }

    public static void showSingleToast(Context ctx, @StringRes int msgID) {
        showSingleToast(ctx, msgID, 0);
    }

    public static void showSingleToast(Context ctx, @StringRes int msgID, int duration) {
        String msg = ctx.getString(msgID);
        int showDuration = duration <= Toast.LENGTH_SHORT ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        if (null == toast) {
            toast = Toast.makeText(ctx.getApplicationContext(), msgID, showDuration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > showDuration) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showSingleToast(Context ctx, String msg) {
        showSingleToast(ctx, msg, 0);
    }

    public static void showSingleToast(Context ctx, String msg, int duration) {
        int showDuration = duration <= Toast.LENGTH_SHORT ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        if (null == toast) {
            toast = Toast.makeText(ctx.getApplicationContext(), msg, showDuration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > showDuration) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void printMultiLog(String... msg) {
        if (msg != null && DEBUG) {
            sb.setLength(0);
            if (msg.length > 0) {
                for (String aMsg : msg) {
                    sb.append(aMsg);
                    sb.append(" ");
                }
            } else {
                sb.append("empty printMultiLog please check");
            }
            android.util.Log.d(TAG, sb.toString());
        }
    }

    private UXUtils() {
    }
}

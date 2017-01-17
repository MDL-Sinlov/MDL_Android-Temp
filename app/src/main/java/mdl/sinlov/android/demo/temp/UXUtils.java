package mdl.sinlov.android.demo.temp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * UX Utils for Fast request
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
    public static final long DEFAULT_TIME_SHOT = 2000;
    public static final long DEFAULT_TIME_LONG = 5000;
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

    public static boolean fastRequest() {
        return fastRequest(false);
    }

    public static boolean fastRequest(boolean isLongCheck) {
        return fastRequest(isLongCheck, null, null);
    }

    public static boolean fastRequest(boolean isLongCheck, Context ctx, String toastMsg) {
        long checkTime = System.currentTimeMillis() - UXUtils.lastClickTime;
        showLog("fastRequest", "checkTime: " + checkTime);
        long betweenTIme = isLongCheck ? DEFAULT_TIME_LONG : DEFAULT_TIME_SHOT;
        if (checkTime < betweenTIme) {
            if (null != ctx) {
                if (!TextUtils.isEmpty(toastMsg)) {
                    if (isShowDefaultUXMessage) {
                        showSingleToast(ctx.getApplicationContext(), toastMsg);
                    }
                } else {
                    if (isShowDefaultUXMessage) {
                        showSingleToast(ctx.getApplicationContext(), "请稍等片刻尝试");
                    }
                }
            }
            showLog("fastRequest", "true time: " + UXUtils.lastClickTime, "between" + checkTime);
            return true;
        } else {
            UXUtils.lastClickTime = System.currentTimeMillis();
            showLog("fastRequest", "false time: " + UXUtils.lastClickTime);
            return false;
        }
    }

    public static boolean isFastRequestShowProgressDialog(Context ctx) {
        return isFastRequestShowProgressDialog(ctx, false);
    }

    public static boolean isFastRequestShowProgressDialog(Context ctx, String msg) {
        return isFastRequestShowProgressDialog(ctx, msg, false);
    }

    public static boolean isFastRequestShowProgressDialog(Context ctx, boolean isLongCheck) {
        return isFastRequestShowProgressDialog(ctx, null, isLongCheck);
    }

    public static boolean isFastRequestShowProgressDialog(Context ctx, String meg, boolean isLongCheck) {
        long checkTime = System.currentTimeMillis() - UXUtils.lastClickTime;
        showLog("isFastRequestShowProgressDialog", "checkTime: " + checkTime);
        long betweenTIme = isLongCheck ? DEFAULT_TIME_LONG : DEFAULT_TIME_SHOT;
        if (checkTime < betweenTIme) {
            try {
                if (null != ctx) {
                    if (pd == null) {
                        pd = new ProgressDialog(ctx);
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        if (TextUtils.isEmpty(meg)) {
                            if (isShowDefaultUXMessage) {
                                pd.setMessage("执行中，请稍后再试!");
                            }
                        } else {
                            pd.setMessage(meg);
                        }
                        pd.setCancelable(false);
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showLog("isFastRequestShowProgressDialog", "auto close start");
                                if (pd != null) {
                                    showLog("isFastRequestShowProgressDialog", "do auto close");
                                    pd.hide();
                                }
                                showLog("isFastRequestShowProgressDialog", "auto close end");
                            }
                        }, betweenTIme);
                    } else if (pd.isShowing()) {
                        pd.dismiss();
                        showLog("isFastRequestShowProgressDialog", "dismiss time: " + UXUtils.lastClickTime);
                    }
                } else {
                    showLog("show isFastRequestShowProgressDialog null context");
                }
            } catch (Exception e) {
                showLog("show isFastRequestShowProgressDialog error");
                e.printStackTrace();
            }
            return true;
        } else {
            UXUtils.lastClickTime = System.currentTimeMillis();
            showLog("isFastRequestShowProgressDialog", "false time: " + UXUtils.lastClickTime);
            return false;
        }
    }

    private static void showSingleToast(Context ctx, String msg) {
        if (null == toast) {
            toast = Toast.makeText(ctx.getApplicationContext(), msg, Toast.LENGTH_LONG);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > 5000) {
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

    private static void showLog(String... msg) {
        if (msg != null && DEBUG) {
            sb.setLength(0);
            if (msg.length > 0) {
                for (String aMsg : msg) {
                    sb.append(aMsg);
                    sb.append(" ");
                }
            } else {
                sb.append("empty showLog please check");
            }
            android.util.Log.d(TAG, sb.toString());
        }
    }

    private UXUtils() {
    }
}

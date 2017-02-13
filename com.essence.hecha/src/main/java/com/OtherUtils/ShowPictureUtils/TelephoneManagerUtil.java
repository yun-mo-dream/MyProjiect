package com.OtherUtils.ShowPictureUtils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2017/1/22.
 */

public class TelephoneManagerUtil {
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }
}

package org.rmj.g3appdriver.dev;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class Telephony {

    private final Context mContext;

    public Telephony(Context context) {
        this.mContext = context;
    }

    @SuppressLint("HardwareIds")
    public String getDeviceID() {
        return Settings.Secure.getString(
                mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public String getMobilNumbers() {
        try {
            List<SubscriptionInfo> subInfoList;
            ArrayList<String> Numbers = new ArrayList<>();
            SubscriptionManager mSubscriptionManager = SubscriptionManager.from(mContext);
            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE);
            subInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
            if (subInfoList.size() > 0) {
                for (SubscriptionInfo subscriptionInfo : subInfoList) {
                    Numbers.add(subscriptionInfo.getNumber());
                }
                if(Numbers.get(0) != null){
                    String lsResult = "";
                    String MobileNo = Numbers.get(0);
                    if (!MobileNo.isEmpty()) {
                        if (MobileNo.substring(0, 3).equalsIgnoreCase("+63")) {
                            lsResult = MobileNo.replace("+63", "0");
                        } else if (MobileNo.substring(0, 1).equalsIgnoreCase("9")) {
                            lsResult = "0" + MobileNo;
                        } else if (MobileNo.substring(0, 2).equalsIgnoreCase("63")) {
                            lsResult = MobileNo.replace("63", "0");
                        } else if (MobileNo.substring(0, 2).equalsIgnoreCase("09")) {
                            lsResult = MobileNo;
                        } else {
                            MobileNo = MobileNo.replace("-", "");
                            MobileNo = MobileNo.replace("+", "");
                            String lsTarget = MobileNo.substring(0, 2);
                            lsResult = MobileNo.replace(lsTarget, "09");
                        }
                    }
                    return lsResult;
                } else if(Numbers.get(1) != null){
                    String lsResult = "";
                    String MobileNo = Numbers.get(1);
                    if (!MobileNo.isEmpty()) {
                        if (MobileNo.substring(0, 3).equalsIgnoreCase("+63")) {
                            lsResult = MobileNo.replace("+63", "0");
                        } else if (MobileNo.substring(0, 1).equalsIgnoreCase("9")) {
                            lsResult = "0" + MobileNo;
                        } else if (MobileNo.substring(0, 2).equalsIgnoreCase("63")) {
                            lsResult = MobileNo.replace("63", "0");
                        } else if (MobileNo.substring(0, 2).equalsIgnoreCase("09")) {
                            lsResult = MobileNo;
                        } else {
                            //this method is use for emulator testing...
                            MobileNo = MobileNo.replace("-", "");
                            MobileNo = MobileNo.replace("+", "");
                            String lsTarget = MobileNo.substring(0, 2);
                            lsResult = MobileNo.replace(lsTarget, "09");
                        }
                    }
                    return lsResult;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getFormattedMobileNo(String MobileNo){
        String result = "";
        if (!MobileNo.isEmpty()) {
            if (MobileNo.substring(0, 3).equalsIgnoreCase("+63")) {
                result = MobileNo.replace("+63", "0");
            } else if (MobileNo.substring(0, 1).equalsIgnoreCase("9")) {
                result = "0" + MobileNo;
            } else if (MobileNo.substring(0, 2).equalsIgnoreCase("63")) {
                result = MobileNo.replace("63", "0");
            } else if (MobileNo.substring(0, 2).equalsIgnoreCase("09")) {
                result = MobileNo;
            }
        }
        return result;
    }
}

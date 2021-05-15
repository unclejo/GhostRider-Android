/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.app
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.guanzongroup.ghostrider.epacss.Service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationRecipient;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationUser;
import org.rmj.g3appdriver.GRider.Database.Entities.ETokenInfo;
import org.rmj.g3appdriver.GRider.Database.Repositories.AppTokenManager;
import org.rmj.g3appdriver.GRider.Database.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.GRider.Http.HttpHeaders;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.WebClient;
import org.rmj.guanzongroup.ghostrider.notifications.Etc.RemoteMessageParser;
import org.rmj.guanzongroup.ghostrider.notifications.Etc.NotificationAssets;
import org.rmj.guanzongroup.ghostrider.notifications.Object.GNotifBuilder;

import java.util.Date;
import java.util.Objects;

import static org.rmj.g3appdriver.utils.WebApi.URL_SEND_RESPONSE;

public class GMessagingService extends FirebaseMessagingService {
    private static final String TAG = GMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        AppTokenManager poToken = new AppTokenManager(getApplication());
        ETokenInfo loToken = new ETokenInfo();
        loToken.setTokenInf(s);
        poToken.setTokenInfo(loToken);
        AppConfigPreference.getInstance(GMessagingService.this).setAppToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        new SendResponseTask(getApplication(),
                remoteMessage).execute();
    }

    private static class SendResponseTask extends AsyncTask<Void, Void, String>{
        private final Application instance;
        private final RemoteMessageParser loParser;
        private final ConnectionUtil loConn;
        private final HttpHeaders poHeaders;
        private final RNotificationInfo poNotification;

        public SendResponseTask(Application application, RemoteMessage remoteMessage) {
            this.instance = application;
            this.loParser = new RemoteMessageParser(remoteMessage);
            this.loConn = new ConnectionUtil(instance);
            this.poHeaders = HttpHeaders.getInstance(instance);
            this.poNotification = new RNotificationInfo(instance);
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(Void... voids) {
            String lsResponse = "";
            try{
                JSONObject data = new JSONObject();
                data.put("title", NotificationAssets.getAppTitle(loParser.getValueOf("msgmon"),
                        loParser.getDataValueOf("title"), loParser.getValueOf("srcenm")));
                data.put("message", loParser.getDataValueOf("message"));
                data.put("appsrce", loParser.getValueOf("appsrce"));
                data.put("msgtype", loParser.getValueOf("msgmon"));

                ENotificationMaster loMaster = new ENotificationMaster();
                loMaster.setTransNox(poNotification.getClientNextMasterCode());
                loMaster.setMesgIDxx(loParser.getValueOf("transno"));
                loMaster.setParentxx(loParser.getValueOf("parent"));
                loMaster.setCreatedx(loParser.getValueOf("stamp"));
                loMaster.setAppSrcex(loParser.getValueOf("appsrce"));
                loMaster.setCreatrID(loParser.getValueOf("srceid"));
                loMaster.setCreatrNm(loParser.getValueOf("srcenm"));
                loMaster.setMsgTitle(loParser.getDataValueOf("title"));
                loMaster.setMessagex(loParser.getDataValueOf("message"));
                loMaster.setMsgTypex(loParser.getValueOf("msgmon"));

                ENotificationRecipient loRecpnt = new ENotificationRecipient();
                loRecpnt.setTransNox(loParser.getValueOf("transno"));
                loRecpnt.setAppRcptx(loParser.getValueOf("apprcpt"));
                loRecpnt.setRecpntID(loParser.getValueOf("rcptid"));
                loRecpnt.setRecpntNm(loParser.getValueOf("rcptnm"));
                loRecpnt.setMesgStat(loParser.getValueOf("status"));
                loRecpnt.setReceived(AppConstants.DATE_MODIFIED);
                loRecpnt.setTimeStmp(AppConstants.DATE_MODIFIED);

                ENotificationUser loUser = new ENotificationUser();
                loUser.setUserIDxx(loParser.getValueOf("srceid"));
                loUser.setUserName(loParser.getValueOf("srcenm"));

                poNotification.insertNotificationInfo(loMaster, loRecpnt, loUser);

                if(loConn.isDeviceConnected()){
                    String lsMessageID = loMaster.getMesgIDxx();
                    JSONObject params = new JSONObject();
                    params.put("transno", lsMessageID);
                    params.put("status", "2");
                    params.put("stamp", AppConstants.DATE_MODIFIED);
                    params.put("infox", "");

                    String response = WebClient.httpsPostJSon(URL_SEND_RESPONSE, data.toString(), poHeaders.getHeaders());
                    JSONObject loJson = new JSONObject(Objects.requireNonNull(response));
                    Log.e(TAG, loJson.getString("result"));
                    String lsResult = loJson.getString("result");
                    if(lsResult.equalsIgnoreCase("success")){
                        poNotification.updateRecipientSendStat(lsMessageID, "2");
                    }
                } else {
                    lsResponse = AppConstants.NO_INTERNET();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return lsResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int lnChannelID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            GNotifBuilder.createNotification(instance,
                    loParser.getDataValueOf("title"),
                    loParser.getDataValueOf("message"),
                    lnChannelID).show();
        }
    }
}

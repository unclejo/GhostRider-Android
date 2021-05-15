/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver.GRider.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.MiscUtil;
import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Database.DbConnection;
import org.rmj.g3appdriver.GRider.Database.GGC_GriderDB;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DNotifications;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationRecipient;
import org.rmj.g3appdriver.GRider.Database.Entities.ENotificationUser;

import java.util.List;

public class RNotificationInfo {
    private final Application instance;
    private final DNotifications notificationDao;
    private final LiveData<List<DNotifications.ClientNotificationInfo>> clientNotificationList;
    private final LiveData<List<DNotifications.UserNotificationInfo>> userNotificationList;
    private final LiveData<List<DNotifications.UserNotificationInfo>> userMessageList;

    public RNotificationInfo(Application application){
        this.instance = application;
        GGC_GriderDB GGCGriderDB = GGC_GriderDB.getInstance(application);
        notificationDao = GGCGriderDB.NotificationDao();
        clientNotificationList = notificationDao.getClientNotificationList();
        userNotificationList = notificationDao.getUserNotificationList();
        userMessageList = notificationDao.getUserMessageList();
    }

    public LiveData<List<DNotifications.ClientNotificationInfo>> getClientNotificationList() {
        return clientNotificationList;
    }

    public LiveData<List<DNotifications.UserNotificationInfo>> getUserMessageList(){
        return userMessageList;
    }

    public LiveData<List<DNotifications.UserNotificationInfo>> getUserMessageListGroupByUser(){
        return notificationDao.getUserMessageListGroupByUser();
    }

    public LiveData<List<DNotifications.UserNotificationInfo>> getUserNotificationList() {
        return userNotificationList;
    }

    public void insertNotificationInfo(ENotificationMaster notificationMaster,
                                       ENotificationRecipient notificationRecipient,
                                       ENotificationUser notificationUser){
        notificationDao.insert(notificationMaster);
        notificationDao.insert(notificationRecipient);
        notificationDao.insert(notificationUser);
    }

    public void updateRecipientSendStat(String messageID, String status){
        notificationDao.updateRecipientStatus(messageID, AppConstants.DATE_MODIFIED, status);
    }

    public String getClientNextMasterCode(){
        String lsNextCode = "";
        GConnection loConn = DbConnection.doConnect(instance);
        try{
            lsNextCode = MiscUtil.getNextCode("Notification_Info_Master", "sTransNox", true, loConn.getConnection(), "", 12, false);
        } catch (Exception e){
            e.printStackTrace();
        }
        loConn = null;
        return lsNextCode;
    }
}

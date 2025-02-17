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
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.SQLUtil;
import org.rmj.g3appdriver.GRider.Database.GGC_GriderDB;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DMcTermCategory;
import org.rmj.g3appdriver.GRider.Database.DbConnection;
import org.rmj.g3appdriver.GRider.Database.Entities.EMcTermCategory;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class RMcTermCategory {
    private static final String TAG = "DB_McTerm_Category";
    private final Application application;
    private final DMcTermCategory mcTermCategoryDao;
    private LiveData<List<EMcTermCategory>> allMcTermCategory;

    public RMcTermCategory(Application application){
        this.application = application;
        GGC_GriderDB GGCGriderDB = GGC_GriderDB.getInstance(application);
        mcTermCategoryDao = GGCGriderDB.McTermCategoryDao();
        allMcTermCategory = mcTermCategoryDao.getAllMcTermCategory();
    }

    public void insertBulkData(List<EMcTermCategory> categories){
        mcTermCategoryDao.insertBulkData(categories);
    }

    public String getLatestDataTime(){
        return mcTermCategoryDao.getLatestDataTime();
    }

    public void saveTermCategory(JSONArray faJson) throws Exception {
        GConnection loConn = DbConnection.doConnect(application);

        if (loConn == null) {
            //Log.e(TAG, "Connection was not initialized.");
            return;
        }

        JSONObject loJson;
        String lsSQL;
        ResultSet loRS;

        for (int x = 0; x < faJson.length(); x++) {
            loJson = new JSONObject(faJson.getString(x));

            //check if record already exists on database
            lsSQL = "SELECT dTimeStmp FROM MC_Term_Category" +
                    " WHERE sMCCatIDx = " + SQLUtil.toSQL(loJson.getString("sMCCatIDx")) + "" +
                    " AND nAcctTerm = " + SQLUtil.toSQL(loJson.getString("nAcctTerm"));
            loRS = loConn.executeQuery(lsSQL);

            lsSQL = "";
            //record does not exists
            if (!loRS.next()) {
                //create insert statement
                lsSQL = "INSERT INTO MC_Term_Category" +
                        "(sMCCatIDx" +
                        ",nAcctTerm" +
                        ",nAcctThru" +
                        ",nFactorRt" +
                        ",dPricexxx" +
                        ",dTimeStmp)" +
                        " VALUES" +
                        "(" + SQLUtil.toSQL(loJson.getString("sMCCatIDx")) +
                        "," + SQLUtil.toSQL(loJson.getString("nAcctTerm")) +
                        "," + SQLUtil.toSQL(loJson.getString("nAcctThru")) +
                        "," + SQLUtil.toSQL(loJson.getString("nFactorRt")) +
                        "," + SQLUtil.toSQL(loJson.getString("dPricexxx")) +
                        "," + SQLUtil.toSQL(loJson.getString("dTimeStmp")) + ")";
            } else { //record already exists
                Date ldDate1 = SQLUtil.toDate(loRS.getString("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);

                //compare date if the record from API is newer than the database record
                if (!ldDate1.equals(ldDate2)) {
                    //create update statement
                    lsSQL = "UPDATE MC_Term_Category SET" +
                            "  nSelPrice = " + SQLUtil.toSQL(loJson.getString("sMCCatIDx")) +
                            ", nLastPrce = " + SQLUtil.toSQL(loJson.getString("nAcctTerm")) +
                            ", nDealrPrc = " + SQLUtil.toSQL(loJson.getString("nAcctThru")) +
                            ", nMinDownx = " + SQLUtil.toSQL(loJson.getString("nFactorRt")) +
                            ", sMCCatIDx = " + SQLUtil.toSQL(loJson.getString("dPricexxx")) +
                            " WHERE sModelIDx = " + SQLUtil.toSQL(loJson.getString("dTimeStmp"));
                }
            }

            if (!lsSQL.isEmpty()) {
                if (loConn.executeUpdate(lsSQL) <= 0) {
                    //Log.e(TAG, loConn.getMessage());
                } else {
                    //Log.d(TAG, "Mc Term Category info saved successfully.");
                }
            }
        }

        //terminate object connection
        loConn = null;
    }
}

package org.rmj.g3appdriver.GRider.Database.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.SQLUtil;
import org.rmj.g3appdriver.GRider.Database.AppDatabase;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DFileCode;
import org.rmj.g3appdriver.GRider.Database.DbConnection;
import org.rmj.g3appdriver.GRider.Database.Entities.EFileCode;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class RFileCode {
    private static final String TAG = RFileCode.class.getSimpleName();
    private LiveData<List<EFileCode>> allFileCode;
    private DFileCode fileCodeDao;
    private Application application;

    public RFileCode(Application application) {
        this.application=application;
        this.fileCodeDao = AppDatabase.getInstance(application).FileCodeDao();
        this.allFileCode = fileCodeDao.selectFileCodeList();
    }

    public LiveData<List<EFileCode>> getAllFileCode() {
        return allFileCode;
    }

    public boolean insertFileCodeData(JSONArray faJson) throws Exception{
        GConnection loConn = DbConnection.doConnect(application);
        boolean result = true;
        if (loConn == null){
            Log.e(TAG, "Connection was not initialized.");
            result = false;
        }

        JSONObject loJson;
        String lsSQL;
        ResultSet loRS;

        for(int x = 0; x < faJson.length(); x++){
            loJson = new JSONObject(faJson.getString(x));

            //check if record already exists on database
            lsSQL = "SELECT dTimeStmp FROM EDocSys_File" +
                    " WHERE sFileCode = " + SQLUtil.toSQL((String) loJson.get("sFileCode"));
            loRS = loConn.executeQuery(lsSQL);

            lsSQL = "";
            //record does not exists
            if (!loRS.next()){
                //check if the record is active
                if ("1".equals((String) loJson.get("cRecdStat"))){
                    //create insert statement
                    lsSQL = "INSERT INTO EDocSys_File" +
                            "(sFileCode" +
                            ",sBarrcode" +
                            ",sBriefDsc" +
                            ",cRecdStat" +
                            ",dTimeStmp)" +
                            " VALUES" +
                            "(" + SQLUtil.toSQL(loJson.getString("sFileCode")) +
                            "," + SQLUtil.toSQL(loJson.getString("sBarrcode")) +
                            "," + SQLUtil.toSQL(loJson.getString("sBriefDsc")) +
                            "," + SQLUtil.toSQL(loJson.getString("cRecdStat")) +
                            "," + SQLUtil.toSQL(loJson.getString("dTimeStmp")) + ")";
                }
            } else { //record already exists
                Date ldDate1 = SQLUtil.toDate(loRS.getString("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);

                //compare date if the record from API is newer than the database record
                if (!ldDate1.equals(ldDate2)){
                    //create update statement
                    lsSQL = "UPDATE EDocSys_File SET" +
                            "   sFileCode = " + SQLUtil.toSQL(loJson.getString("sFileCode")) +
                            ",  sBarrcode = " + SQLUtil.toSQL(loJson.getString("sBarrcode")) +
                            ",  sBriefDsc = " + SQLUtil.toSQL(loJson.getString("sBriefDsc")) +
                            ",  cRecdStat = " + SQLUtil.toSQL(loJson.getString("cRecdStat")) +
                            ",  dTimeStmp = " + SQLUtil.toSQL(loJson.getString("dTimeStmp"))  +
                            " WHERE sFileCode = " + SQLUtil.toSQL(loJson.getString("sFileCode"));
                }
            }

            if (!lsSQL.isEmpty()){
                //Log.d(TAG, lsSQL);
                if (loConn.executeUpdate(lsSQL) <= 0) {
                    Log.e(TAG, loConn.getMessage());
                    result = false;
                } else {
                    Log.d(TAG, "FileCode info saved successfully.");
                }
            } else {
                Log.d(TAG, "No record to update. FileCode info maybe on its latest on local database.");
            }
        }
        Log.e(TAG, "FileCode info has been save to local.");

        //terminate object connection
        loConn = null;
        return result;

    }
}
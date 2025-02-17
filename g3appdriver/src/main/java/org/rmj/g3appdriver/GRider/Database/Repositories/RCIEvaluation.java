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
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.MiscUtil;
import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DCIEvaluation;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DCreditApplication;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DCreditApplicationDocuments;
import org.rmj.g3appdriver.GRider.Database.DbConnection;
import org.rmj.g3appdriver.GRider.Database.Entities.EBranchLoanApplication;
import org.rmj.g3appdriver.GRider.Database.Entities.ECIEvaluation;
import org.rmj.g3appdriver.GRider.Database.Entities.ECreditApplication;
import org.rmj.g3appdriver.GRider.Database.Entities.ECreditApplicationDocuments;
import org.rmj.g3appdriver.GRider.Database.GGC_GriderDB;

import java.util.List;

public class RCIEvaluation {
    private final DCIEvaluation ciDao;
    private final Application app;
    public RCIEvaluation(Application application){
        this.app = application;
        GGC_GriderDB GGCGriderDB = GGC_GriderDB.getInstance(application);
        ciDao = GGCGriderDB.CIDao();
//        allCreditApplication = ciDao.getAllCreditApplication();
    }

    public LiveData<ECIEvaluation> getAllDoneCiInfo(String fsTransNo) {
        return ciDao.getAllDoneCiInfo(fsTransNo);
    }

    public LiveData<ECIEvaluation> getAllCIApplication(String TransNox){
        return ciDao.getCIInfoOfTransNox(TransNox);
    }

    public void insertNewCiApplication(ECIEvaluation eciEvaluation){
        ciDao.insertNewCiApplication(eciEvaluation);

    }

    public void insertCiApplication(ECIEvaluation eciEvaluation){
        new InsertTask(ciDao, "insert").execute(eciEvaluation);
    }
    public void updateCiResidence(String TransNox, String LandMark, String Ownershp, String OwnOther, String HouseTyp, String Garagexx,String Latitude, String Longitud){
//        ciDao.updateCIInfo(TransNox, LandMark, Ownershp, OwnOther, HouseTyp, Garagexx,Latitude, Longitud);
        new UpdateTask(ciDao, "update",TransNox, LandMark, Ownershp, OwnOther, HouseTyp, Garagexx,Latitude, Longitud).execute();
    }
    public void updateCiResidences(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updateCiNeighbor1(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updateCiNeighbor2(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updateCiNeighbor3(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }

    public void updateCiNeighbor(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updateCiDisbursement(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updaCharacterTraits(ECIEvaluation eciEvaluation){
        ciDao.update(eciEvaluation);
    }
    public void updateCiResidence(ECIEvaluation eciEvaluation){
        new InsertTask(ciDao, "update").execute(eciEvaluation);
    }
    private static class InsertTask extends AsyncTask<ECIEvaluation, Void, String>{
        private final DCIEvaluation ciDao;
        private String transInfo;
        public InsertTask(DCIEvaluation ciDao, String transInfo) {
            this.ciDao = ciDao;
            this.transInfo = transInfo;
        }

        @Override
        protected String doInBackground(ECIEvaluation... eciEvaluations) {
            if (transInfo.equalsIgnoreCase("insert")){
                ciDao.insert(eciEvaluations[0]);
            }
            else if(transInfo.equalsIgnoreCase("update")){
                ciDao.update(eciEvaluations[0]);
            }
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Void, Void, String>{
        private final DCIEvaluation ciDao;
        private String transInfo;
        private String TransNox, LandMark, Ownershp, OwnOther, HouseTyp, Garagexx,Latitude, Longitud;
        public UpdateTask(DCIEvaluation ciDao, String transInfo,String TransNox, String LandMark, String Ownershp, String OwnOther, String HouseTyp, String Garagexx,String Latitude, String Longitud) {
            this.ciDao = ciDao;
            this.transInfo = transInfo;
            this.TransNox = TransNox;
            this.LandMark = LandMark;
            this.Ownershp = Ownershp;
            this.OwnOther = OwnOther;
            this.HouseTyp = HouseTyp;
            this.Garagexx = Garagexx;
            this.Latitude = Latitude;
            this.Longitud = Longitud;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(transInfo.equalsIgnoreCase("update")){
                ciDao.updateCIInfo(TransNox, LandMark, Ownershp, OwnOther, HouseTyp, Garagexx,Latitude, Longitud);

            }
            return null;
        }
    }
    public String getGOCasNextCode(){
        String lsTransNox = "";
        GConnection loConn = DbConnection.doConnect(app);
        try{
            lsTransNox = MiscUtil.getNextCode("CI_Evaluation", "sTransNox", true, loConn.getConnection(), "", 12, false);

        } catch (Exception e){
            e.printStackTrace();
        }
        loConn = null;
        return lsTransNox;
    }
}

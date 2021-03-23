package org.rmj.g3appdriver.GRider.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Trace;

import androidx.lifecycle.LiveData;

import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.MiscUtil;
import org.rmj.g3appdriver.GRider.Database.AppDatabase;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DCreditApplicantInfo;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DEmployeeInfo;
import org.rmj.g3appdriver.GRider.Database.DbConnection;
import org.rmj.g3appdriver.GRider.Database.Entities.ECreditApplicantInfo;

import java.util.List;

public class RCreditApplicant {
    private final DCreditApplicantInfo creditApplicantInfoDao;
    private final LiveData<List<ECreditApplicantInfo>> creditApplicantList;

    private final Application app;

    public RCreditApplicant(Application application){
        this.app = application;
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        creditApplicantInfoDao = appDatabase.CreditApplicantDao();
        creditApplicantList = creditApplicantInfoDao.getCreditApplicantList();
    }

    public LiveData<ECreditApplicantInfo> getCreditApplicantInfoLiveData(String TransNox){
        return creditApplicantInfoDao.getCreditApplicantInfo(TransNox);
    }

    public void insertGOCasData(ECreditApplicantInfo creditApplicantInfo){
        new InsertNewApplicationTask(creditApplicantInfoDao).execute(creditApplicantInfo);
    }

    public void updateGOCasData(ECreditApplicantInfo creditApplicantInfo){
        new UpdateGOCasDataTask(creditApplicantInfoDao).execute(creditApplicantInfo);
    }

    public void updateCurrentGOCasData(String GOCasData, String TransNox){
        new UpdateGOCasData(creditApplicantInfoDao, GOCasData).execute(TransNox);
    }

    public void deleteGOCasData(ECreditApplicantInfo creditApplicantInfo){

    }
    public void deleteAllCredit(){
        new DeleteAllCreditTask(creditApplicantInfoDao).execute();
    }
    public LiveData<List<ECreditApplicantInfo>> getCreditApplicantList(){
        return creditApplicantList;
    }

    public LiveData<String> getAppMeansInfo(String TransNox){
        return creditApplicantInfoDao.getAppMeansInfo(TransNox);
    }

    private static class InsertNewApplicationTask extends AsyncTask<ECreditApplicantInfo, Void, Void>{
        private final DCreditApplicantInfo applicantInfo;

        public InsertNewApplicationTask(DCreditApplicantInfo applicantInfo){
            this.applicantInfo = applicantInfo;
        }

        @Override
        protected Void doInBackground(ECreditApplicantInfo... eCreditApplicantInfos) {
            applicantInfo.insert(eCreditApplicantInfos[0]);
            return null;
        }
    }

    private static class UpdateGOCasDataTask extends AsyncTask<ECreditApplicantInfo, Void, Void>{
        private final DCreditApplicantInfo applicantInfo;

        public UpdateGOCasDataTask(DCreditApplicantInfo applicantInfo){
            this.applicantInfo = applicantInfo;
        }

        @Override
        protected Void doInBackground(ECreditApplicantInfo... eCreditApplicantInfos) {
            applicantInfo.update(eCreditApplicantInfos[0]);
            return null;
        }
    }

    private static class UpdateGOCasData extends AsyncTask<String, Void, Void>{
        private final DCreditApplicantInfo applicantInfo;
        private final String GOCasData;

        public UpdateGOCasData(DCreditApplicantInfo applicantInfo, String GOCasData){
            this.applicantInfo = applicantInfo;
            this.GOCasData = GOCasData;
        }

        @Override
        protected Void doInBackground(String... transNox) {
            String TransNox = transNox[0];
            ECreditApplicantInfo loInfo = applicantInfo.getCurrentCreditApplicantInfo(TransNox);
            loInfo.setDetlInfo(GOCasData);
            applicantInfo.update(loInfo);
            return null;
        }
    }

    public static class DeleteAllCreditTask extends AsyncTask<Void, Void, Void>{
        private final DCreditApplicantInfo creditDao;

        public DeleteAllCreditTask(DCreditApplicantInfo creditDao) {
            this.creditDao = creditDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            creditDao.deleteAllCreditApp();
            return null;
        }
    }

    public String getGOCasNextCode(){
        String lsTransNox = "";
        GConnection loConn = DbConnection.doConnect(app);
        try{
            lsTransNox = MiscUtil.getNextCode("Credit_Applicant_Info", "sTransNox", true, loConn.getConnection(), "", 12, false);

        } catch (Exception e){
            e.printStackTrace();
        }
        loConn = null;
        return lsTransNox;
    }
}

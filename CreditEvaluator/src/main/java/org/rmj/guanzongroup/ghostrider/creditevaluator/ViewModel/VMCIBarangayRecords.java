/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.CreditEvaluator
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.guanzongroup.ghostrider.creditevaluator.ViewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.GRider.Database.Entities.ECIEvaluation;
import org.rmj.g3appdriver.GRider.Database.Entities.EImageInfo;
import org.rmj.g3appdriver.GRider.Database.Entities.ERelation;
import org.rmj.g3appdriver.GRider.Database.Repositories.RCIEvaluation;
import org.rmj.g3appdriver.GRider.Database.Repositories.RRelation;
import org.rmj.g3appdriver.GRider.Etc.SessionManager;
import org.rmj.guanzongroup.ghostrider.creditevaluator.Etc.ViewModelCallBack;
import org.rmj.guanzongroup.ghostrider.creditevaluator.Model.CIBarangayRecordInfoModel;

import java.util.ArrayList;
import java.util.List;


public class VMCIBarangayRecords extends AndroidViewModel {
    private static final String TAG = VMCIResidenceInfo.class.getSimpleName();
    private final Application instance;
    private final RCIEvaluation poCI;
    private final ECIEvaluation evaluation;
    private final RRelation rRelation;
    private final SessionManager poUser;

    private final MutableLiveData<ECIEvaluation> poCIDetail = new MutableLiveData<>();
    private final MutableLiveData<String> sTransNox = new MutableLiveData<>();
    private final MutableLiveData<String> nLatitude = new MutableLiveData<>();
    private final MutableLiveData<String> nLogitude = new MutableLiveData<>();

    private final MutableLiveData<String> sNeigbor1 = new MutableLiveData<>();
    private final MutableLiveData<String> sRel1 = new MutableLiveData<>();
    private final MutableLiveData<String> sMobile1 = new MutableLiveData<>();
    private final MutableLiveData<String> sFBRemark1 = new MutableLiveData<>();
    private final MutableLiveData<String> sFeedback1 = new MutableLiveData<>();
    private final MutableLiveData<String> sAddress1 = new MutableLiveData<>();

    private final MutableLiveData<String> sNeigbor2 = new MutableLiveData<>();
    private final MutableLiveData<String> sRel2 = new MutableLiveData<>();
    private final MutableLiveData<String> sMobile2 = new MutableLiveData<>();
    private final MutableLiveData<String> sFBRemark2 = new MutableLiveData<>();
    private final MutableLiveData<String> sFeedback2 = new MutableLiveData<>();
    private final MutableLiveData<String> sAddress2 = new MutableLiveData<>();

    private final MutableLiveData<String> sNeigbor3 = new MutableLiveData<>();
    private final MutableLiveData<String> sRel3 = new MutableLiveData<>();
    private final MutableLiveData<String> sMobile3 = new MutableLiveData<>();
    private final MutableLiveData<String> sFBRemark3 = new MutableLiveData<>();
    private final MutableLiveData<String> sFeedback3 = new MutableLiveData<>();
    private final MutableLiveData<String> sAddress3 = new MutableLiveData<>();


    private final MutableLiveData<String> sHasRecord = new MutableLiveData<>();
    private final MutableLiveData<String> sRemRecord = new MutableLiveData<>();

    private final MutableLiveData<String> relID1 = new MutableLiveData<>();
    private final MutableLiveData<String> relID2 = new MutableLiveData<>();
    private final MutableLiveData<String> relID3 = new MutableLiveData<>();


    private final LiveData<String[]> relations;
    private List<EImageInfo> imgInfo = new ArrayList<>();
    public VMCIBarangayRecords(@NonNull Application application) {
        super(application);
        this.instance = application;
        this.poCI = new RCIEvaluation(application);
        this.evaluation = new ECIEvaluation();
        this.rRelation = new RRelation(application);
        this.poUser = new SessionManager(application);
        this.sNeigbor1.setValue("");
        this.sRel1.setValue("");
        this.sMobile1.setValue("");
        this.sFBRemark1.setValue("");
        this.sFeedback1.setValue("");
        this.sAddress1.setValue("");
        this.sNeigbor2.setValue("");
        this.sRel2.setValue("");
        this.sMobile2.setValue("");
        this.sFBRemark2.setValue("");
        this.sFeedback2.setValue("");
        this.sAddress2.setValue("");
        this.sNeigbor3.setValue("");
        this.sRel3.setValue("");
        this.sMobile3.setValue("");
        this.sFBRemark3.setValue("");
        this.sFeedback3.setValue("");
        this.sAddress3.setValue("");
        this.sHasRecord.setValue("");
        this.sRemRecord.setValue("");
        relations = rRelation.getAllRelatnDs();
    }
    public interface OnSaveNeighbor{
        void onSuccessNeighbor(String args);
        void onFailedNeighbor(String message);
    }
    // Set field value after fetching from local database
    public void setCurrentCIDetail(ECIEvaluation detail){
        try {
            if(detail.getReltnCD1() != null){
                this.sRel1.setValue(detail.getReltnCD1());
            }
            if(detail.getNeighbr1() != null){
                this.sNeigbor1.setValue(detail.getNeighbr1());
            }
            if(detail.getMobileN1() != null){
                this.sMobile1.setValue(detail.getMobileN1());
            }
            if(detail.getFeedBck1() != null){
                this.sFeedback1.setValue(detail.getFeedBck1());
            }
            if(detail.getFBRemrk1() != null){
                this.sFBRemark1.setValue(detail.getFBRemrk1());
            }
            if(detail.getAddress1() != null){
                this.sAddress1.setValue(detail.getAddress1());
            }
//            Neighbor 2
            if(detail.getReltnCD2() != null){
                this.sRel2.setValue(detail.getReltnCD2());
            }
            if(detail.getNeighbr2() != null){
                this.sNeigbor2.setValue(detail.getNeighbr2());
            }
            if(detail.getMobileN2() != null){
                this.sMobile2.setValue(detail.getMobileN2());
            }
            if(detail.getFeedBck2() != null){
                this.sFeedback2.setValue(detail.getFeedBck2());
            }
            if(detail.getFBRemrk2() != null){
                this.sFBRemark2.setValue(detail.getFBRemrk2());
            }

            if(detail.getAddress2() != null){
                this.sAddress2.setValue(detail.getAddress2());
            }
//            Neighbor 3
            if(detail.getReltnCD3() != null){
                this.sRel3.setValue(detail.getReltnCD3());
            }
            if(detail.getNeighbr3() != null){
                this.sNeigbor3.setValue(detail.getNeighbr3());
            }
            if(detail.getMobileN3() != null){
                this.sMobile3.setValue(detail.getMobileN3());
            }
            if(detail.getFeedBck3() != null){
                this.sFeedback3.setValue(detail.getFeedBck3());
            }
            if(detail.getFBRemrk3() != null){
                this.sFBRemark3.setValue(detail.getFBRemrk3());
            }
            if(detail.getAddress3() != null){
                this.sAddress3.setValue(detail.getAddress3());
            }
            if(detail.getHasRecrd() != null){
                this.sHasRecord.setValue(detail.getHasRecrd());
            }
            if(detail.getRemRecrd() != null){
                this.sRemRecord.setValue(detail.getRemRecrd());
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        this.poCIDetail.setValue(detail);
    }
    public void setsTransNox(String transNox){
        this.sTransNox.setValue(transNox);
    }

    public LiveData<ECIEvaluation> getCIByTransNox(){
        return poCI.getAllCIApplication(sTransNox.getValue());
    }

    public void setRelatnID1(String relatnID) { this.relID1.setValue(relatnID); }
    public void setRelatnID2(String relatnID) { this.relID2.setValue(relatnID); }
    public void setRelatnID3(String relatnID) { this.relID3.setValue(relatnID); }

    public LiveData<List<ERelation>> getRelation(){
        return rRelation.getRelation();
    }
    public LiveData<String[]> getAllRelatnDs(){
        return relations;
    }
    public boolean saveNeighbor(CIBarangayRecordInfoModel infoModel, String btnText, ViewModelCallBack callback) {
        try {

            new UpdateTask(poCI, infoModel, btnText, callback).execute(poCIDetail.getValue());
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
//            callback.OnFailedResult(e.getMessage());
            callback.onFailedResult("NullPointerException error");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailedResult("Exception error");
            return false;
        }
    }

    //Added by Jonathan 2021/04/13
    //Need AsyncTask for background threading..
    //RoomDatabase requires background task in order to manipulate Tables...
    private  class UpdateTask extends AsyncTask<ECIEvaluation, Void, String> {
        private final RCIEvaluation poCIEvaluation;
        private final CIBarangayRecordInfoModel infoModel;
        private final ViewModelCallBack callback;
        private final String btnText;

        public UpdateTask(RCIEvaluation poCIEvaluation, CIBarangayRecordInfoModel infoModel,String btnText,ViewModelCallBack callback) {
            this.poCIEvaluation = poCIEvaluation;
            this.infoModel = infoModel;
            this.callback = callback;
            this.btnText = btnText;
        }

        @Override
        protected String doInBackground(ECIEvaluation... detail) {
            try {

                if(btnText.equalsIgnoreCase("Neighbor1")){
                    if (!infoModel.isValidNeigbor1()) {
                        return infoModel.getMessage();
                    } else {
                        ECIEvaluation loDetail = detail[0];
                        loDetail.setNeighbr1(infoModel.getNeighbr1());
                        loDetail.setReltnCD1(infoModel.getReltnCD1());
                        loDetail.setAddress1(infoModel.getAddress1());
                        loDetail.setFeedBck1(infoModel.getFeedBck1());
                        loDetail.setFBRemrk1(infoModel.getFBRemrk1());
                        loDetail.setMobileN1(infoModel.getMobileN1());
                        poCIEvaluation.updateCiNeighbor1(loDetail);
                    }
                }else if(btnText.equalsIgnoreCase("Neighbor2")){
                    if (!infoModel.isValidNeigbor2()) {
                        return infoModel.getMessage();
                    } else {
                        ECIEvaluation loDetail = detail[0];
                        loDetail.setNeighbr2(infoModel.getNeighbr2());
                        loDetail.setReltnCD2(infoModel.getReltnCD2());
                        loDetail.setFeedBck2(infoModel.getFeedBck2());
                        loDetail.setFBRemrk2(infoModel.getFBRemrk2());
                        loDetail.setMobileN2(infoModel.getMobileN2());
                        loDetail.setAddress2(infoModel.getAddress2());
                        poCIEvaluation.updateCiNeighbor2(loDetail);
                    }
                }else if(btnText.equalsIgnoreCase("Neighbor3")){
                    if (!infoModel.isValidNeigbor3()) {
                        return infoModel.getMessage();
                    } else {
                        ECIEvaluation loDetail = detail[0];
                        loDetail.setNeighbr3(infoModel.getNeighbr3());
                        loDetail.setReltnCD3(infoModel.getReltnCD3());
                        loDetail.setFeedBck3(infoModel.getFeedBck3());
                        loDetail.setFBRemrk3(infoModel.getFBRemrk3());
                        loDetail.setMobileN3(infoModel.getMobileN3());
                        loDetail.setAddress3(infoModel.getAddress3());
                        poCIEvaluation.updateCiNeighbor3(loDetail);
                    }
                }else {

                    if (!infoModel.isValidNeighbor()) {
                        return infoModel.getMessage();
                    }else{
                        ECIEvaluation loDetail = detail[0];
                        Log.e("Has record",infoModel.getHasRecrd());
                        Log.e("Rem record",infoModel.getRemRecrd());
                        loDetail.setRemRecrd(infoModel.getRemRecrd());
                        loDetail.setHasRecrd(infoModel.getHasRecrd());
                        poCIEvaluation.updateCiNeighbor(loDetail);
                    }
                }
                return "success";

            } catch (NullPointerException e){
                e.printStackTrace();
                return e.getMessage();
            }catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("success")){
                callback.onSaveSuccessResult(btnText);
            } else {
                callback.onFailedResult(s);
            }
        }
    }

    //  Getter  Neigbor  1
    public LiveData<String> getsNeigbor1() {
        return this.sNeigbor1;
    }

    public LiveData<String> getsRel1() {
        return this.sRel1;
    }

    public LiveData<String> getsMobile1() {
        return this.sMobile1;
    }

    public LiveData<String> getsFBRemark1() {

        return this.sFBRemark1;
    }

    public LiveData<String> getsFeedback1() {

        return this.sFeedback1;
    }
    public LiveData<String> getAddress1() {

        return this.sAddress1;
    }
    //  Getter  Neigbor  2
    public LiveData<String> getsNeigbor2() {
        return this.sNeigbor2;
    }

    public LiveData<String> getsRel2() {
        return this.sRel2;
    }

    public LiveData<String> getsMobile2() {
        return this.sMobile2;
    }

    public LiveData<String> getsFBRemark2() {
        return this.sFBRemark2;
    }

    public LiveData<String> getsFeedback2() {
        return this.sFeedback2;
    }
    public LiveData<String> getAddress2() {

        return this.sAddress2;
    }
    //  Getter  Neigbor  3
    public LiveData<String> getsNeigbor3() {
        return this.sNeigbor3;
    }

    public LiveData<String> getsRel3() {
        return this.sRel3;
    }

    public LiveData<String> getsMobile3() {
        return this.sMobile3;
    }

    public LiveData<String> getsFBRemark3() {
        return this.sFBRemark3;
    }

    public LiveData<String> getsFeedback3() {
        return this.sFeedback3;
    }
    public LiveData<String> getAddress3() {

        return this.sAddress3;
    }
    // Getter Record
    public LiveData<String> getsHasRecord() {
        return this.sHasRecord;
    }

    public LiveData<String> getsRemRecord() {
        return this.sRemRecord;
    }

}
package org.rmj.g3appdriver.GRider.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Database.AppDatabase;
import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DClientUpdate;
import org.rmj.g3appdriver.GRider.Database.Entities.EClientUpdate;

public class RClientUpdate {
    private static final String TAG = RClientUpdate.class.getSimpleName();
    private final DClientUpdate clientDao;

    private JSONObject poDetail;

    public RClientUpdate(Application application){
        this.clientDao = AppDatabase.getInstance(application).ClientUpdateDao();
    }

    public void insertClientUpdateInfo(EClientUpdate clientUpdate){
        this.clientDao.insertClientUpdateInfo(clientUpdate);
    }

    public LiveData<EClientUpdate> getClientUpdateInfo(String ClientID){
        return clientDao.getClientUpdateInfo(ClientID);
    }

    public void UpdateClientInfoStatus(String ClientID){
        this.clientDao.updateClientInfoImage(ClientID, AppConstants.DATE_MODIFIED);
    }

    public void UpdateClientImage(String ClientID, String ImageName){
        this.clientDao.updateClientInfoImage(ClientID, ImageName);
    }

    /**
     *
     * @param ClientID pass a unique key to determine which info must be converted to json...
     * @return returns JSON object that is being created by doInbackground method.
     *          JSONObject contains all info inside Client_Update_Request table...
     */
    public JSONObject getClientUpdateDetail(String ClientID){
        ClientUpdateToJsonTask loUpdate = new ClientUpdateToJsonTask(clientDao);
        loUpdate.execute(ClientID);
        return loUpdate.getDetail();
    }

    private static class ClientUpdateToJsonTask extends AsyncTask<String, Void, JSONObject>{
        private JSONObject loDetail;
        private DClientUpdate clientDao;

        public ClientUpdateToJsonTask(DClientUpdate clientDao) {
            this.clientDao = clientDao;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject loJson = new JSONObject();
            try{
                EClientUpdate loClient = clientDao.getClientUpdateInfo(strings[0]).getValue();
                loJson.put("sClientID", loClient.getClientID());
                loJson.put("sSourceCd", loClient.getSourceCd());
                loJson.put("sSourceNo", loClient.getSourceNo());
                loJson.put("sDtlSrcNo", loClient.getDtlSrcNo());
                loJson.put("sLastName", loClient.getLastName());
                loJson.put("sFrstName", loClient.getFrstName());
                loJson.put("sMiddName", loClient.getMiddName());
                loJson.put("sSuffixNm", loClient.getSuffixNm());
                loJson.put("sHouseNox", loClient.getHouseNox());
                loJson.put("sAddressx", loClient.getAddressx());
                loJson.put("sTownIDxx", loClient.getTownIDxx());
                loJson.put("cGenderxx", loClient.getGenderxx());
                loJson.put("cCivlStat", loClient.getCivlStat());
                loJson.put("dBirthDte", loClient.getBirthDte());
                loJson.put("dBirthPlc", loClient.getBirthPlc());
                loJson.put("sLandline", loClient.getLandline());
                loJson.put("sMobileNo", loClient.getMobileNo());
                loJson.put("sEmailAdd", loClient.getEmailAdd());
                loJson.put("sImageNme", loClient.getImageNme());
            } catch (Exception e){
                e.printStackTrace();
            }
            return loJson;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loDetail = jsonObject;
        }

        public JSONObject getDetail(){
            return loDetail;
        }
    }
}
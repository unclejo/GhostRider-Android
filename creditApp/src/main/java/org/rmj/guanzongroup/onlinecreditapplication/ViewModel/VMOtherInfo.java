package org.rmj.guanzongroup.onlinecreditapplication.ViewModel;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.GRider.Database.Entities.ECountryInfo;
import org.rmj.g3appdriver.GRider.Database.Entities.ECreditApplicantInfo;
import org.rmj.g3appdriver.GRider.Database.Entities.EProvinceInfo;
import org.rmj.g3appdriver.GRider.Database.Entities.ETownInfo;
import org.rmj.g3appdriver.GRider.Database.Repositories.RCountry;
import org.rmj.g3appdriver.GRider.Database.Repositories.RCreditApplicant;
import org.rmj.g3appdriver.GRider.Database.Repositories.RProvince;
import org.rmj.g3appdriver.GRider.Database.Repositories.RTown;
import org.rmj.gocas.base.GOCASApplication;
import org.rmj.guanzongroup.onlinecreditapplication.Etc.CreditAppConstants;
import org.rmj.guanzongroup.onlinecreditapplication.Model.OtherInfoModel;
import org.rmj.guanzongroup.onlinecreditapplication.Model.PersonalReferenceInfoModel;
import org.rmj.guanzongroup.onlinecreditapplication.Model.ViewModelCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VMOtherInfo extends AndroidViewModel {

    private static final String TAG = VMOtherInfo.class.getSimpleName();

    private String psTransNox;
    private final MutableLiveData<String> lsProvID = new MutableLiveData<>();

    private final MutableLiveData<List<PersonalReferenceInfoModel>> poReference = new MutableLiveData<>();

    private ECreditApplicantInfo poInfo;

    private final GOCASApplication poGoCas;
    private final RCreditApplicant poApplcnt;
    private final RProvince RProvince;
    private final RTown RTown;
    private final RCountry RCountry;
    private final LiveData<List<EProvinceInfo>> provinceInfoList;

    public VMOtherInfo(@NonNull Application application) {
        super(application);
        this.poApplcnt = new RCreditApplicant(application);
        RProvince = new RProvince(application);
        RTown = new RTown(application);
        RCountry = new RCountry(application);
        provinceInfoList = RProvince.getAllProvinceInfo();
        poGoCas = new GOCASApplication();
        poReference.setValue(new ArrayList<>());
    }

    public void setTransNox(String transNox){
        this.psTransNox = transNox;
    }

    public LiveData<ECreditApplicantInfo> getCreditApplicationInfo(){
        return poApplcnt.getCreditApplicantInfoLiveData(psTransNox);
    }

    public void setCreditApplicantInfo(ECreditApplicantInfo applicantInfo){
        try{
            poInfo = applicantInfo;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public LiveData<List<PersonalReferenceInfoModel>> getReferenceList(){
        return poReference;
    }

    public void addReference(PersonalReferenceInfoModel poInfo, AddPersonalInfoListener listener){
        try{
            if(poInfo.isDataValid()){
                Objects.requireNonNull(poReference.getValue()).add(poInfo);
                listener.OnSuccess();
            } else {
                listener.onFailed(poInfo.getMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
            listener.onFailed(e.getMessage());
        }
    }

    public LiveData<List<EProvinceInfo>> getProvinceInfoList(){
        return provinceInfoList;
    }

    public LiveData<List<ETownInfo>> getTownInfoList(){
        return RTown.getTownInfoFromProvince(lsProvID.getValue());
    }

    public LiveData<List<ECountryInfo>> getCountryInfoList(){
        return RCountry.getAllCountryInfo();
    }

    public LiveData<String[]> getProvinceNameList(){
        return RProvince.getAllProvinceNames();
    }

    public LiveData<String[]> getAllTownNames(){
        return RTown.getTownNamesFromProvince(lsProvID.getValue());
    }

    public void setProvID(String ProvID) { this.lsProvID.setValue(ProvID); }

    public ArrayAdapter<String> getUnitUser(){
        return new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.UNIT_USER);
    }

    public ArrayAdapter<String> getUnitPurpose(){
        return new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.UNIT_PURPOSE);
    }

    public ArrayAdapter<String> getUnitPayer(){
        return new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.UNIT_USER);
    }

    public ArrayAdapter<String> getPayerBuyer(){
        ArrayAdapter<String> adapter;
        if (poGoCas.ApplicantInfo().getCivilStatus().equalsIgnoreCase("1")){
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.UNIT_PAYER);
        }else{
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.UNIT_PAYER_NO_SPOUSE);
        }
        return adapter;
    }

    public ArrayAdapter<String> getIntCompanyInfoSource(){
        ArrayAdapter<String> adapter;
        if (poGoCas.ApplicantInfo().getCivilStatus().equalsIgnoreCase("1")){
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.INTO_US);
        }else{
            adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.INTO_US_NO_SPOUSE);
        }
        return adapter;
    }

    public boolean SubmitOtherInfo(OtherInfoModel otherInfo, ViewModelCallBack callBack){
        try {
            otherInfo.setPersonalReferences(poReference.getValue());
            if(otherInfo.isDataValid()){
                poGoCas.OtherInfo().setUnitUser(otherInfo.getUnitUser());
                poGoCas.OtherInfo().setPurpose(String.valueOf(otherInfo.getUnitPrps()));
                if (Integer.parseInt(otherInfo.getUnitPayr()) != 1){
                    poGoCas.OtherInfo().setUnitPayor(String.valueOf(otherInfo.getUnitPayr()));
                }else{
                    poGoCas.OtherInfo().setPayorRelation(String.valueOf(otherInfo.getPayrRltn()));
                }
                if (otherInfo.getSource().equalsIgnoreCase("Others")){
                    poGoCas.OtherInfo().setSourceInfo(otherInfo.getCompanyInfoSource());
                }else{
                    poGoCas.OtherInfo().setSourceInfo(otherInfo.getSource());
                }
                for(int x = 0; x < Objects.requireNonNull(poReference.getValue()).size(); x++){
                    PersonalReferenceInfoModel loRef = poReference.getValue().get(x);
                    poGoCas.OtherInfo().addReference();
                    poGoCas.OtherInfo().setPRName(x, loRef.getFullname());
                    poGoCas.OtherInfo().setPRTownCity(x, loRef.getTownCity());
                    poGoCas.OtherInfo().setPRMobileNo(x, loRef.getContactN());
                    poGoCas.OtherInfo().setPRAddress(x, loRef.getAddress1());
                }

                poInfo.setTransNox(Objects.requireNonNull(psTransNox));
                poInfo.setOthrInfo(poGoCas.OtherInfo().toJSONString());
                poApplcnt.updateGOCasData(poInfo);
                callBack.onSaveSuccessResult("Success");
                Log.e(TAG, "Other information result : " + poGoCas.OtherInfo().toJSONString());

                return true;
                } else {
                callBack.onFailedResult(otherInfo.getMessage());
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            callBack.onFailedResult(e.getMessage());
            return false;
        }
    }

    public interface ExpActionListener{
        void onSuccess(String message);
        void onFailed(String message);
    }

    public interface AddPersonalInfoListener{
        void OnSuccess();
        void onFailed(String message);
    }
}
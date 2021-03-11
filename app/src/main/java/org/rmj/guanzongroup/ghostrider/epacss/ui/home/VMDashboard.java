package org.rmj.guanzongroup.ghostrider.epacss.ui.home;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.rmj.g3appdriver.GRider.Database.Entities.EEmployeeInfo;
import org.rmj.g3appdriver.GRider.Database.Repositories.REmployee;
import org.rmj.g3appdriver.GRider.Database.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.dev.Telephony;
import org.rmj.g3appdriver.etc.SessionManager;

public class VMDashboard extends AndroidViewModel {

    private final SessionManager poSession;
    private final REmployee poEmploye;

    private final RNotificationInfo poNotify;

    private MutableLiveData<String> psEmailxx = new MutableLiveData<>();
    private MutableLiveData<String> psUserNme = new MutableLiveData<>();
    private MutableLiveData<String> psBranchx = new MutableLiveData<>();
    private MutableLiveData<String> psDeptNme = new MutableLiveData<>();
    private MutableLiveData<String> psMobleNo = new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public VMDashboard(@NonNull Application application) {
        super(application);
        poSession = new SessionManager(application);
        poEmploye = new REmployee(application);
        psMobleNo.setValue(new Telephony(application).getMobilNumbers());
        this.poNotify = new RNotificationInfo(application);
    }

    public LiveData<EEmployeeInfo> getEmployeeInfo(){
        return poEmploye.getEmployeeInfo();
    }

    public LiveData<String> getMobileNo() {
        return psMobleNo;
    }


}
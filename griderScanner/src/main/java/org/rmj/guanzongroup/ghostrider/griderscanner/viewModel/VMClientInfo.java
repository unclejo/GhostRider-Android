package org.rmj.guanzongroup.ghostrider.griderscanner.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GRider.Database.DataAccessObject.DCreditApplication;
import org.rmj.g3appdriver.GRider.Database.Entities.ECreditApplicationDocuments;
import org.rmj.g3appdriver.GRider.Database.Entities.EEmployeeInfo;
import org.rmj.g3appdriver.GRider.Database.Entities.EFileCode;
import org.rmj.g3appdriver.GRider.Database.Entities.EImageInfo;
import org.rmj.g3appdriver.GRider.Database.Repositories.RCreditApplication;
import org.rmj.g3appdriver.GRider.Database.Repositories.RCreditApplicationDocument;
import org.rmj.g3appdriver.GRider.Database.Repositories.REmployee;
import org.rmj.g3appdriver.GRider.Database.Repositories.RFileCode;
import org.rmj.g3appdriver.GRider.Database.Repositories.RImageInfo;
import org.rmj.g3appdriver.GRider.ImportData.ImportDataCallback;
import org.rmj.g3appdriver.GRider.ImportData.Import_LoanApplications;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VMClientInfo extends AndroidViewModel {
    private final Application instance;
    private final RFileCode peFileCode;
    private final LiveData<List<EFileCode>> fileCodeList;
    private final RCreditApplicationDocument poDocument;
    private final RImageInfo poImage;
    private List<EImageInfo> imgInfo = new ArrayList<>();
    private List<ECreditApplicationDocuments> documentInfo = new ArrayList<>();
    public VMClientInfo(@NonNull Application application) {
        super(application);
        this.instance = application;
        peFileCode = new RFileCode(application);
        this.fileCodeList = peFileCode.getAllFileCode();
        poDocument = new RCreditApplicationDocument(application);
        poImage = new RImageInfo(application);
    }
    public LiveData<List<EFileCode>> getFileCode(){
        return this.fileCodeList;
    }
    public void setImgInfo(List<EImageInfo> imgInfo) {
        this.imgInfo = imgInfo;
    }

    public LiveData<List<EImageInfo>>  getImgInfo(){
        return poImage.getAllImageInfo();
    }
    public void setDocumentInfo(List<ECreditApplicationDocuments> docInfo) {
        this.documentInfo = docInfo;
    }
    public List<ECreditApplicationDocuments> getDocInfo() {
        return this.documentInfo;
    }
//    public LiveData<List<ECreditApplicationDocuments>> getDocumentInfo(){
//        return poDocument.getDocumentInfo();
//    }
    public LiveData<List<ECreditApplicationDocuments>> getDocument(String TransNox, int EntryNox){
        return poDocument.getDocument(TransNox, EntryNox);
    }

    public void saveDocumentInfo(ECreditApplicationDocuments documentsInfo){
        try{
            poDocument.insertDocumentInfo(documentsInfo);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void saveImageInfo(EImageInfo foImage){
        try{
            boolean isImgExist = false;
            String tansNo = "";
            for (int i = 0; i < imgInfo.size(); i++){
                if(foImage.getSourceNo().equalsIgnoreCase(imgInfo.get(i).getSourceNo())
                        && foImage.getDtlSrcNo().equalsIgnoreCase(imgInfo.get(i).getDtlSrcNo())) {
                    tansNo = imgInfo.get(i).getTransNox();
//                    File finalFile = new File(getRealPathFromURI(imgInfo.get(i).getFileLoct()));
//                    finalFile.delete();
                    isImgExist = true;
                }
            }
            if (isImgExist){
                foImage.setTransNox(tansNo);
                Log.e("Img TransNox", tansNo);
                poImage.updateImageInfo(foImage);
                Log.e("VMClient ", "Image info has been updated!");
            }else{
                foImage.setTransNox(poImage.getImageNextCode());
                poImage.insertImageInfo(foImage);
                Log.e("VMClient ", "Image info has been save!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
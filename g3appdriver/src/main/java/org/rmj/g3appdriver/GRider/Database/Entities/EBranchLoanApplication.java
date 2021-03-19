package org.rmj.g3appdriver.GRider.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Credit_Online_Application_List")
public class EBranchLoanApplication {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sTransNox")
    private String TransNox;
    @ColumnInfo(name = "dTransact")
    private String Transact;
    @ColumnInfo(name = "sCredInvx")
    private String CredInvx;
    @ColumnInfo(name = "sCompnyNm")
    private String CompnyNm;
    @ColumnInfo(name = "sSpouseNm")
    private String SpouseNm;
    @ColumnInfo(name = "sAddressx")
    private String Addressx;
    @ColumnInfo(name = "sMobileNo")
    private String MobileNo;
    @ColumnInfo(name = "sQMAppCde")
    private String QMAppCde;
    @ColumnInfo(name = "sModelNme")
    private String ModelNme;
    @ColumnInfo(name = "nDownPaym")
    private String DownPaym;
    @ColumnInfo(name = "nAcctTerm")
    private String AcctTerm;
    @ColumnInfo(name = "cTranStat")
    private String TranStat;
    @ColumnInfo(name = "dTimeStmp")
    private String TimeStmp;

    public EBranchLoanApplication() {
    }

    @NonNull
    public String getTransNox() {
        return TransNox;
    }

    public void setTransNox(@NonNull String transNox) {
        TransNox = transNox;
    }

    public String getTransact() {
        return Transact;
    }

    public void setTransact(String transact) {
        Transact = transact;
    }

    public String getCredInvx() {
        return CredInvx;
    }

    public void setCredInvx(String credInvx) {
        CredInvx = credInvx;
    }

    public String getCompnyNm() {
        return CompnyNm;
    }

    public void setCompnyNm(String compnyNm) {
        CompnyNm = compnyNm;
    }

    public String getSpouseNm() {
        return SpouseNm;
    }

    public void setSpouseNm(String spouseNm) {
        SpouseNm = spouseNm;
    }

    public String getAddressx() {
        return Addressx;
    }

    public void setAddressx(String addressx) {
        Addressx = addressx;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getQMAppCde() {
        return QMAppCde;
    }

    public void setQMAppCde(String QMAppCde) {
        this.QMAppCde = QMAppCde;
    }

    public String getModelNme() {
        return ModelNme;
    }

    public void setModelNme(String modelNme) {
        ModelNme = modelNme;
    }

    public String getDownPaym() {
        return DownPaym;
    }

    public void setDownPaym(String downPaym) {
        DownPaym = downPaym;
    }

    public String getAcctTerm() {
        return AcctTerm;
    }

    public void setAcctTerm(String acctTerm) {
        AcctTerm = acctTerm;
    }

    public String getTranStat() {
        return TranStat;
    }

    public void setTranStat(String tranStat) {
        TranStat = tranStat;
    }

    public String getTimeStmp() {
        return TimeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        TimeStmp = timeStmp;
    }
}

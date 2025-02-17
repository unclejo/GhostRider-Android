/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.dailyCollectionPlan
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.guanzongroup.ghostrider.dailycollectionplan.Model;

import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Etc.DCP_Constants;

public class PaidTransactionModel {

    private String TransNo;
    private String EntryNo;
    private String RemCode;
    private String PrNoxxx;
    private String Payment;
    private String Remarks;
    private String Amountx;
    private String Dscount;
    private String Othersx;
    private String TotAmnt;

    private String BankNme;
    private String CheckDt;
    private String CheckNo;
    private String AccntNo;

    private String message;

    public PaidTransactionModel() {
    }

    public String getMessage(){
        return message;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public String getEntryNo() {
        return EntryNo;
    }

    public void setEntryNo(String entryNo) {
        EntryNo = entryNo;
    }

    public void setRemarksCode(String Remarks){
        this.RemCode = Remarks;
    }

    public String getPrNoxxx() {
        return PrNoxxx;
    }

    public void setPrNoxxx(String prNoxxx) {
        PrNoxxx = prNoxxx;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getAmountx() {
        return Amountx;
    }

    public void setAmountx(String amountx) {
        Amountx = amountx;
    }

    public String getBankNme() {
        return BankNme;
    }

    public void setBankNme(String bankNme) {
        BankNme = bankNme;
    }

    public String getCheckDt() {
        return CheckDt;
    }

    public void setCheckDt(String checkDt) {
        CheckDt = checkDt;
    }

    public String getCheckNo() {
        return CheckNo;
    }

    public void setCheckNo(String checkNo) {
        CheckNo = checkNo;
    }

    public String getAccntNo() {
        return AccntNo;
    }

    public void setAccntNo(String accntNo) {
        AccntNo = accntNo;
    }

    public String getDscount() {
        if(Dscount == null || Dscount.trim().isEmpty()){
            return "0";
        }
        return Dscount;
    }

    public void setDscount(String dscount) {
        Dscount = dscount;
    }

    public String getOthersx() {
        if(Othersx == null || Othersx.trim().isEmpty()){
            return "0";
        }
        return Othersx;
    }

    public void setOthersx(String othersx) {
        Othersx = othersx;
    }

    public String getTotAmnt() {
        return TotAmnt;
    }

    public void setTotAmnt(String totAmnt) {
        TotAmnt = totAmnt;
    }

    public boolean isDataValid(){
        return isPaymentTypeValid() && isPRNumberValid() && isAmountValid() && isRemarksValid();
    }

    private boolean isPaymentTypeValid(){
        if(Payment.equalsIgnoreCase("0")){
            message = "Please select payment type";
            return false;
        }
        return true;
    }

    private boolean isPRNumberValid(){
        if(PrNoxxx.trim().isEmpty()){
            message = "Please enter P.R number";
            return false;
        }
        return true;
    }

    private boolean isAmountValid(){
        if(Amountx.trim().isEmpty()){
            message = "Please enter amount";
            return false;
        }
        return true;
    }
    //                        Added by Jonathan 07/27/2021
    private boolean isRemarksValid(){
        if(Remarks.trim().isEmpty()){
            message = "Please enter remarks";
            return false;
        }
        return true;
    }

    public String getRemarksCode(){
        return DCP_Constants.getRemarksCode(RemCode);
    }
}

package org.rmj.guanzongroup.ghostrider.dailycollectionplan.Model;

public class PromiseToPayModel {

    private String ptpRemarks;
    private String ptpRemCode;
    private String ptpDate;
    private String ptpAppointmentUnit;
    private String ptpCollectorName;
    private String ptpBranch;
    private String ptpImgPath;

    private String message;

    public PromiseToPayModel() {
    }

    public String getMessage() { return message; }

    public String getPtpRemCode() { return this.ptpRemCode; }
    public void setPtpRemCode(String ptpRemCode) {
        this.ptpRemCode = ptpRemCode;
    }

    public String getPtpRemarks() { return this.ptpRemarks; }
    public void setPtpRemarks(String ptpRemarks) {
        this.ptpRemarks = ptpRemarks;
    }

    public String getPtpImgPath() { return this.ptpImgPath; }
    public void setPtpImgPath(String ptpImgPath) {
        this.ptpImgPath = ptpImgPath;
    }

    public String getPtpDate() { return this.ptpDate; }
    public void setPtpDate(String ptpDate) {
        this.ptpDate = ptpDate;
    }

    public String getPtpAppointmentUnit() { return ptpAppointmentUnit; }
    public void setPtpAppointmentUnit(String ptpAppointmentUnit) {
        this.ptpAppointmentUnit = ptpAppointmentUnit;
    }

    public String getPtpCollectorName() { return ptpCollectorName; }
    public void setPtpCollectorName(String ptpCollectorName) {
        this.ptpCollectorName = ptpCollectorName;
    }

    public String getPtpBranch() { return ptpBranch; }
    public void setPtpBranch(String ptpBranch) {
        this.ptpBranch = ptpBranch;
    }

    public boolean isDataValid(){
        return isPtpDate() && isPtpAppoint() && isRemarks() && isPtpImgPath();
    }
    public boolean isPtpDate(){
        if (ptpDate == null || ptpDate.trim().isEmpty()){
            message = "Please select Payment Date.";
            return false;
        }
        return true;
    }

    public boolean isPtpAppoint(){
        if (ptpAppointmentUnit.trim().isEmpty()){
            message = "Please select appointment unit.";
            return false;
        }
        if (ptpAppointmentUnit.equalsIgnoreCase("1")){
            return  isPtpAppointBranch();
        }
        return true;
    }
    public boolean isPtpAppointBranch(){
        if (ptpBranch.trim().isEmpty()){
            message = "Please enter branch.";
            return false;
        }
        return true;
    }
    public boolean isRemarks(){
        if (ptpRemarks.trim().isEmpty()){
            message = "Required fields!\nPlease enter remarks.";
            return false;
        }
        return true;
    }
    public boolean isPtpImgPath(){
        if (ptpImgPath == null || ptpImgPath.trim().isEmpty()){
            message = "empty";
            return false;
        }
        return true;
    }
}

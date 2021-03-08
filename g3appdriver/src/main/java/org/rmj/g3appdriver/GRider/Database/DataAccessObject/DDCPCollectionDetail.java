package org.rmj.g3appdriver.GRider.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.GRider.Database.Entities.EDCPCollectionDetail;

import java.util.List;

@Dao
public interface DDCPCollectionDetail {

    @Insert
    void insert(EDCPCollectionDetail collectionDetail);

    @Update
    void update(EDCPCollectionDetail collectionDetail);

    @Query("UPDATE LR_DCP_Collection_Detail " +
            "SET sRemCodex =:RemCode, " +
            "cSendStat = '0', " +
            "cTranStat = '1', " +
            "sImageNme = (SELECT a.sImageNme FROM Image_Information a LEFT JOIN LR_DCP_Collection_Detail b  ON a.sSourceNo = b.sTransNox AND a.sDtlSrcNo = sAcctNmbr WHERE a.sSourceNo = b.sTransNox AND b.nEntryNox =:EntryNox), " +
            "nLongitud = (SELECT a.nLongitud FROM Image_Information a LEFT JOIN LR_DCP_Collection_Detail b  ON a.sDtlSrcNo = sAcctNmbr WHERE a.sSourceNo = b.sTransNox), " +
            "nLatitude = (SELECT a.nLatitude FROM Image_Information a LEFT JOIN LR_DCP_Collection_Detail b  ON a.sDtlSrcNo = sAcctNmbr WHERE a.sSourceNo = b.sTransNox), " +
            "dModified =:DateModified " +
            "WHERE sTransNox = (SELECT sTransNox " +
            "FROM LR_DCP_Collection_Master ORDER BY dTransact DESC LIMIT 1) " +
            "AND nEntryNox =:EntryNox")
    void updateCollectionDetailInfo(int EntryNox, String RemCode, String DateModified);

    @Query("UPDATE LR_DCP_Collection_Detail " +
            "SET cSendStat='1', dModified=:DateEntry " +
            "WHERE sTransNox =:TransNox " +
            "AND nEntryNox =:EntryNox")
    void updateCollectionDetailStatus(String TransNox, int EntryNox, String DateEntry);

    @Delete
    void delete(EDCPCollectionDetail collectionDetail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulkData(List<EDCPCollectionDetail> collectionDetails);

    @Query("SELECT * FROM LR_DCP_Collection_Detail " +
            "WHERE cSendStat <> '1' ORDER BY dModified ASC")
    LiveData<List<EDCPCollectionDetail>> getCollectionDetailList();

    @Query("SELECT * FROM LR_DCP_Collection_Detail WHERE cTranStat = 1 AND cSendStat = 0")
    LiveData<List<EDCPCollectionDetail>> getCollectionDetailLog();

    @Query("SELECT * FROM LR_DCP_Collection_Detail " +
            "WHERE sTransNox = :TransNox " +
            "AND nEntryNox = :EntryNox")
    LiveData<EDCPCollectionDetail> getCollectionDetail(String TransNox, int EntryNox);

    @Query("SELECT * FROM LR_DCP_Collection_Detail ORDER BY nEntryNox DESC LIMIT 1")
    LiveData<EDCPCollectionDetail> getCollectionLastEntry();

    @Query("SELECT * FROM LR_DCP_Collection_Detail " +
            "WHERE sTransNox = (SELECT sTransNox FROM LR_DCP_Collection_Master ORDER BY dTransact DESC LIMIT 1) " +
            "AND sSerialNo =:SerialNo")
    LiveData<EDCPCollectionDetail> getDuplicateSerialEntry(String SerialNo);

    @Query("UPDATE LR_DCP_Collection_Detail " +
            "SET sImageNme= (SELECT a.sTransNox FROM Image_Information a " +
            "LEFT JOIN LR_DCP_Collection_Detail b  " +
            "ON a.sSourceNo = b.sTransNox AND a.sDtlSrcNo = sAcctNmbr " +
            "WHERE a.sSourceNo = b.sTransNox) " +
            "WHERE sAcctNmbr =:AccountNo " +
            "AND sTransNox = (SELECT sTransNox " +
            "FROM LR_DCP_Collection_Master " +
            "ORDER BY dTransact DESC LIMIT 1)")
    void updateCustomerDetailImage(String AccountNo);

    @Query("SELECT * FROM LR_DCP_Collection_Detail " +
            "WHERE sTransNox = (SELECT sTransNox FROM " +
            "LR_DCP_Collection_Master WHERE dTransact =:dTransact)")
    LiveData<List<EDCPCollectionDetail>> getCollectionDetailForDate(String dTransact);

    @Query("SELECT a.sTransNox, " +
            "a.nEntryNox, " +
            "a.sAcctNmbr, " +
            "a.sRemCodex, " +
            "a.xFullName, " +
            "a.sPRNoxxxx, " +
            "a.nTranAmtx, " +
            "a.nDiscount, " +
            "a.nOthersxx, " +
            "a.sRemarksx, " +
            "a.cTranType, " +
            "a.nTranTotl, " +
            "a.cApntUnit, " +
            "a.sBranchCd, " +
            "b.sTransNox AS sImageIDx, " +
            "b.sFileCode, " +
            "b.sSourceCd, " +
            "b.sImageNme, " +
            "b.sMD5Hashx, " +
            "b.sFileLoct, " +
            "b.nLongitud, " +
            "b.nLatitude, " +
            "c.sLastName, " +
            "c.sFrstName, " +
            "c.sMiddName, " +
            "c.sSuffixNm, " +
            "c.sHouseNox, " +
            "c.sAddressx, " +
            "c.sTownIDxx, " +
            "c.cGenderxx, " +
            "c.cCivlStat, " +
            "c.dBirthDte, " +
            "c.dBirthPlc, " +
            "c.sLandline, " +
            "c.sMobileNo, " +
            "c.sEmailAdd " +
            "FROM LR_DCP_Collection_Detail a " +
            "LEFT JOIN Image_Information b " +
            "ON a.sTransNox = b.sSourceNo " +
            "AND a.sAcctNmbr = b.sDtlSrcNo " +
            "LEFT JOIN Client_Update_Request c " +
            "ON a.sTransNox = c.sSourceNo " +
            "AND a.sAcctNmbr = c.sDtlSrcNo")
    LiveData<List<CollectionDetail>> getJoinCollectionDetailList();

    @Query("SELECT a.sTransNox, " +
            "a.nEntryNox, " +
            "a.sAcctNmbr, " +
            "a.sRemCodex, " +
            "a.xFullName, " +
            "a.sPRNoxxxx, " +
            "a.nTranAmtx, " +
            "a.nDiscount, " +
            "a.nOthersxx, " +
            "a.sRemarksx, " +
            "a.cTranType, " +
            "a.nTranTotl, " +
            "a.cApntUnit, " +
            "a.sBranchCd, " +
            "b.sTransNox AS sImageIDx, " +
            "b.sFileCode, " +
            "b.sSourceCd, " +
            "b.sImageNme, " +
            "b.sMD5Hashx, " +
            "b.sFileLoct, " +
            "b.nLongitud, " +
            "b.nLatitude, " +
            "c.sLastName, " +
            "c.sFrstName, " +
            "c.sMiddName, " +
            "c.sSuffixNm, " +
            "c.sHouseNox, " +
            "c.sAddressx, " +
            "c.sTownIDxx, " +
            "c.cGenderxx, " +
            "c.cCivlStat, " +
            "c.dBirthDte, " +
            "c.dBirthPlc, " +
            "c.sLandline, " +
            "c.sMobileNo, " +
            "c.sEmailAdd " +
            "FROM LR_DCP_Collection_Detail a " +
            "LEFT JOIN Image_Information b " +
            "ON a.sTransNox = b.sSourceNo " +
            "AND a.sAcctNmbr = b.sDtlSrcNo " +
            "LEFT JOIN Client_Update_Request c " +
            "ON a.sTransNox = c.sSourceNo " +
            "AND a.sAcctNmbr = c.sDtlSrcNo " +
            "WHERE a.cSendStat <> '1'")
    LiveData<List<CollectionDetail>> getCollectionDetailForPosting();

    class CollectionDetail{
        public String sTransNox;
        public int nEntryNox;
        public String sAcctNmbr;
        public String sRemCodex;

        public String xFullName;

        //PAY
        public String sPRNoxxxx;
        public String nTranAmtx;
        public String nDiscount;
        public String nOthersxx;
        public String sRemarksx;
        public String cTranType;
        public String nTranTotl;

        //PTP
        public String cApntUnit;
        public String sBranchCd;
        public String dPromised;

        //ImageInfo
        public String sImageIDx;
        public String sFileCode;
        public String sSourceCd;
        public String sImageNme;
        public String sMD5Hashx;
        public String sFileLoct;
        public String nLongitud;
        public String nLatitude;

        //LoanUnit || Transferred/Assumed || False Ownership
        public String sLastName;
        public String sFrstName;
        public String sMiddName;
        public String sSuffixNm;
        public String sHouseNox;
        public String sAddressx;
        public String sTownIDxx;
        public String cGenderxx;
        public String cCivlStat;
        public String dBirthDte;
        public String dBirthPlc;
        public String sLandline;
        public String sMobileNo;
        public String sEmailAdd;
    }
}

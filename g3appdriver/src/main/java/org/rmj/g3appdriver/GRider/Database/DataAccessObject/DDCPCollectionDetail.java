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
            "nLongitud = (SELECT nLongitud FROM Image_Information WHERE sDtlSrcNo = sAcctNmbr AND sSourceNo = sTransNox), " +
            "nLatitude = (SELECT nLatitude FROM Image_Information WHERE sDtlSrcNo = sAcctNmbr AND sSourceNo = sTransNox), " +
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

    @Query("SELECT * FROM LR_DCP_Collection_Detail WHERE cTranStat = 0")
    LiveData<List<EDCPCollectionDetail>> getCollectionDetailList();

    @Query("SELECT * FROM LR_DCP_Collection_Detail WHERE cTranStat = 1 AND cSendStat == 0")
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
            "SET sImageNme=:ImageID " +
            "WHERE sAcctNmbr =:AccountNo " +
            "AND sTransNox = (SELECT sTransNox " +
            "FROM LR_DCP_Collection_Master " +
            "ORDER BY dTransact DESC LIMIT 1)")
    void updateCustomerDetailImage(String ImageID, String AccountNo);
}

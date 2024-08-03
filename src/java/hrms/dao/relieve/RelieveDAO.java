package hrms.dao.relieve;

import hrms.common.Message;
import hrms.model.relieve.RelieveForm;
import java.util.List;

public interface RelieveDAO {
    
    public List getRelieveList(String empid);
    
    public int getRelieveListCount(String empid);
    
    public RelieveForm getRelieveData(String empid, int notid,String rlvid);
    
    public List getRelievedPostList(String empid,String rlvid,String transactionStatus,String rlvspc);
    
    public List getAddlChargeList(String empid);
    
    public boolean saveRelieve(RelieveForm erelieve,String entDeptCode,String entOffCode,String entSpc,String loginempid,String loginusername);
    
    public void deleteRelieve(String empid,RelieveForm erelieve);
    
    public boolean getUpdateStatus(String empid,int notid,String nottype);
    
    //public RelieveForm getTransactionStatus(RelieveForm erelieve);
    
    public String saveRelieveSBCorrection(RelieveForm erelieve,String entDeptCode,String entOffCode,String entSpc,String loginempid,String loginusername,int refcorrectionid);
    
    public RelieveForm getRelieveDataSBCorrectionDDO(String empid, int notid,String rlvid,int correctionid);
    
    public void approveRelieveDataSBCorrection(RelieveForm relieve,String entDeptCode,String entOffCode,String entSpc,String loginempid,String loginusername);
}

package hrms.dao.incrementsanction;

import hrms.model.incrementsanction.IncrementForm;
import java.util.List;

public interface IncrementSanctionDAO {

    public List getPayMatrixLevel();

    public List getPayMatrixCell();

    public void saveIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String payComm, String loginuserid);

    public void updateIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String payComm, String loginuserid) throws Exception;

    public int getIncrementListCount(String empid);

    public List getIncrementList(String empid);

    public IncrementForm getEmpIncRowData(String empid, int incrId);

    public IncrementForm getEmpIncRowDataForSB(String empId, int notId, String payComm);

    public boolean deleteIncrement(IncrementForm incrementForm);

    public void saveCancelIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String loginuserid);

    public void updateCancelIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String loginuserid);

    public IncrementForm editCancelIncRowData(String empid, int notid);

    public IncrementForm editSupersedeIncRowData(String empid, int notid);

    public void saveSupersedeIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String payComm, String loginuserid);

    public void updateSupersedeIncrement(IncrementForm incrementForm, String entdept, String entoff, String entauth, String payComm, String loginuserid) throws Exception;

    public String checkEmployeeLawStatus(String empid);
    
    public String checkEmployeeIPSStatus(String empid);

    public List getPayMatrixLevelForLAW();
    
    public List getPayMatrixLevelForIPS();

    public List getPayMatrixLevelAllForLAW();

    public List getPayMatrixCellForLAWLevel(String matrixLevel);

    public List getPayMatrixCellForGPLevel(String matrixLevel);
    
    public String saveIncrementSBCorrection(IncrementForm incrementForm, String entdept, String entoff, String entauth, String payComm, String loginuserid,int sbcorrectionid);
    
    public IncrementForm getEmpIncRowDataSBCorrection(String empid, int incrId, int correctionid);
    
    public void approveIncrementSBCorrection(IncrementForm incrementForm,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
    
    public String getEmployeeMaxIncrementDate(String empid);
}

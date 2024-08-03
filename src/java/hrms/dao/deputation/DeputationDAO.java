package hrms.dao.deputation;

import hrms.common.Message;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.joining.JoiningForm;
import java.util.List;

public interface DeputationDAO {

    public List getCadreStatusList(String type);

    public List getSubCadreStatusList(String cadrestat);

    public void saveDeputation(DeputationDataForm deputationForm, int notid);

    public void updateDeputation(DeputationDataForm deputationForm);

    public void deleteDeputation(String empid, int notId);

    public List getDeputationList(String empid);

    public int getDeputationListCount(String empid);

    public DeputationDataForm getEmpDeputationData(String empid, int notId);

    /**
     * ************************** AG Deputation ***************************
     */
    public List getAGDeputationList(String empid);

    public void saveAGDeputation(DeputationDataForm deputationForm, int notid);

    public void updateAGDeputation(DeputationDataForm deputationForm);

    public DeputationDataForm getEmpAGDeputationData(String empid, int notId);

    public void saveAGLeaveContribution(DeputationDataForm deputationForm);

    public List getEmpAGDeputationLeaveData(String empid, int notId, String type);

    public void deleteAGDeputation(String empid, String cId, int notId);

    public List getAllEmpDeputation(String deptCode, String offCode);

    public DeputationDataForm getEmpDeputation(String empId);

}

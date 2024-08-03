/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.tab;

import hrms.model.login.LoginUserBean;
import hrms.model.login.UserExpertise;
import hrms.model.tab.DepartmentTreeAttr;
import hrms.model.tab.DistrictTreeAttr;
import hrms.model.tab.OfficeTreeAttr;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface TabDAO {

    public List getOfficeListXML(String empid);

    public void getSubOfficeListXML(String ddoCode, String spc, ArrayList pOffObject);

    public String saveExpertise(UserExpertise ue);

    public OfficeTreeAttr getHODOfficeListXML(String parentOfficeCode);

    public DistrictTreeAttr getDistrictOfficeListXML(String districtCode);

    public ArrayList getContractualEmployeeList(String parentOfficeCode, String empId);

    public ArrayList getNonGovtAidedEmployeeList(String parentOfficeCode, String empId);

    public ArrayList getContractualEmployeeList6Years(String parentOfficeCode);

    public ArrayList getWorkchargedEmployeeList(String parentOfficeCode);

    public ArrayList getWagesEmployeeList(String parentOfficeCode);

    public ArrayList getLevelVExCadreEmployeeList(String parentOfficeCode);

    public ArrayList getRetiredEmployeeList(String parentOfficeCode);

    public ArrayList getDeceasedEmployeeList(String parentOfficeCode);

    public ArrayList getResignedEmployeeList(String parentOfficeCode);

    public DepartmentTreeAttr getCadreList(String deptcode, String cadreType);

    public OfficeTreeAttr getCadreListXML(String loginoffcode, String loginspc);

    public String saveVisitedData(LoginUserBean lub);

    public String sendMsgToDc(LoginUserBean lub);

    public String noOfDaysOfLastEntry(LoginUserBean lub);

    public ArrayList getSpecialcategoryEmpList(String parentOfficeCode);
    
     public ArrayList getdeputationEmployeeList(String deptcode);

    //public ArrayList getAdhocEmpList(String parentOfficeCode);

    //public ArrayList getRetiredEmpList(String parentOfficeCode);

}

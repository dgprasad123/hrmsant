/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Office;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Durga
 */
public interface OfficeDAO {

    public List getOtherOrganisationList();

    public List getReportingOfficeList(String offCode);

    public List getOfficeList(String deptcode, String offSearch, int page, int rows);

    public List getOfficeListCOWise(String coOffcode);

    public List getGranteeOfficeList(String deptcode, String offSearch, int page, int rows);

    public List getTotalOfficeList(String deptcode);

    public List getTotalDeputedOfficeList(String deptcode);

    public List getTotalDDOOfficeList(String deptcode);

    public List getTotalOfficeList(String deptcode, String distCode);

    public List getControllingOfficelist(String deptcode);

    public int getOfficeListCount(String deptcode, String offSearch);

    public List getFieldOffList(String offCode);

    public List getDistrictWiseOfficeList(String distCode);

    public Office getOfficeDetails(String offCode);

    public Office getOfficeName(String ddoCode);

    public boolean saveOfficeDetails(Office office);

    public boolean saveGranteeOfficeDetails(Office office);

    public boolean updateOfficeDetails(Office office);

    public boolean updateGranteeOfficeDetails(Office office);

    public List getOfficeListFilter(String offcode);

    public List getParentOffice(String poffCode);

    public List getOfficeListTreasuryWise(String trcode, String deptcode);

    public List getEmployeePostedOfficeList(String deptcode, String empid, String offcode);

    public List getDistrictWiseDDOList(String distcode);

    public List getControllingOfficelistToAdd(String deptcode);

    public ArrayList getCOList(String logindistcode, String offcode);

    public int insertCOOffice(Office office);

    public List getTotalOfficeListForBacklogEntry(String deptcode, String distCode);

    public List getTotalOfficeListForBacklogEntry(String deptcode);

    public Office getOfficeCodeDetails(String offCode);

    public boolean updateOfficeName(Office office);

    public int saveBacklogOffice(Office office);

    public int cntRefOffCode(String offCode);

    public List getOfficeLevelList();

    public int saveNewOffice(Office office);

    public String getMaxoffCode(String offCode);

    public int saveBackLogOffice(Office office);

    public Office getOfficeNameDeptName(String offCode);
    
    public List getDeptOfficeCode();
    
    public List getSuperannuatedDeptList(int year, int month, String acctType);
    
    public List getSGOOfficeList();
    
    public List getGOIOfficeList(String category);
    
    public List getInterStateOfficeList(String statecode);
    
    public List getOtherOrgOfficeList(String deptcode);
    
    public List getPoliceStations(String distcode);
    
    public List getBillGroupPrivilegeList(String offCode);
    
    public void deleteBillGroupPrivilegeList(String offCode);
    
    public int hasOfficePriv(String spc);

}

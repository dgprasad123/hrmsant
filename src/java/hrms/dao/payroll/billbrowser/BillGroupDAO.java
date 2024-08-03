/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.model.master.ChartOfAccount;
import hrms.model.payroll.billbrowser.BillGroup;
import hrms.model.payroll.billbrowser.BillGroupConfig;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface BillGroupDAO {
    

    public ArrayList getBillGroupList(String offcode);    

    public ArrayList getActiveBillGroupList(String offcode);

    public ArrayList getPlanStatusList();

    public ArrayList getSectorList();

    public ArrayList getPostClassList();

    public ArrayList getBillTypeList();

    public ArrayList getDemandList();

    public ArrayList getMajorHeadList(String demandno);

    public ArrayList getSubMajorHeadList(String demandNo, String majorhead);

    public ArrayList getMinorHeadList(String majorHead, String submajorhead);

    public ArrayList getSubMinorHeadList(String subMajorHead, String minorhead);

    public ArrayList getDetailHeadList(String minorhead, String subminorhead);

    public ArrayList getChargedVotedList(String detailhead, String subminorhead);

    public ArrayList getObjectHeadList(String detailhead);

    public BillGroup getBillGroupDetails(BigDecimal billGroupId);

    public ArrayList getSectionList(String billGroupId);

    public String getConfigurationLvl(BigDecimal billGroupId);

    public void saveGroupSection(String offCode, BillGroup BG);

    public int deleteGroupData(String offCode, String groupId);

    public BillGroup getBillGroupDetail(String offCode, BigDecimal groupId);

    public BillGroupConfig getBillConfigData(String offCode, BigDecimal groupId);

    public void saveBillConfigureddata(BillGroupConfig billGroupConfig);

    public ArrayList getCol6and7List();

    public ArrayList getColList(int colNumber);

    public void updateGroupSection(String offCode, BillGroup bg);

    public ArrayList getAssignedSectionList(BigDecimal bgId,String offCode,String isDDO); 

    public ArrayList getAvailableSectionList(String offCode);

    public String getBillName(BigDecimal billgrpId);

    public void mapSection(BigDecimal billgroupid, int sectionId);

    public void removeSection(int sectionId);

    public ArrayList getBillGroupName(String offcode);

    public ArrayList compareBillDetails(String offCode, BillGroup BG);

    public ArrayList getEmpwiseList(String offcode);

    public ArrayList empWiseGroupPrivilege(String offCode, String spc);

    public void assignBillPrivilege(String billgroupid, String spc, int type, String offcode);

    public void saveChartofAccount(ChartOfAccount chartOfAccount);

    public int excludeBillGroupFromAquitance(BigDecimal billgroupid, String status);

    public int excludeFromTreasury(BigDecimal billgroupid, String status);

    public List getCOList(String selectedddocode);

    public ArrayList getChartOfAccountList(String ddoCode);

    public ArrayList getNewChargedVotedList();

    public void saveNewChartofAccount(ChartOfAccount chartOfAccount);

    public void updateChartofAccount(ChartOfAccount chartOfAccount);

    public ChartOfAccount editChartOfAccountList(ChartOfAccount chartOfAccount);

    public void saveallNewChartofAccount(ChartOfAccount chartOfAccount);

    public ArrayList getDemandListDdowise(String hiddendeptCode);

    public void saveMajorHead(ChartOfAccount chartOfAccount);

    public void saveSubMajorHead(ChartOfAccount chartOfAccount);

    public void saveMinorHead(ChartOfAccount chartOfAccount);

    public void saveSubHead(ChartOfAccount chartOfAccount);

    public void saveDetailHead(ChartOfAccount chartOfAccount);

    public void saveObjectHead(ChartOfAccount chartOfAccount);

    public ArrayList getSubMajorHeadListmaster(String majorhead);

    public ArrayList getMinorHeadListmaster(String majorHead, String submajorhead);

    public ArrayList getPOfficeBillGroupList(String subOffcode);
    

}

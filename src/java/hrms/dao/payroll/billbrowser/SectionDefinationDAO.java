/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.model.master.SubstantivePost;
import hrms.model.payroll.billbrowser.SectionDefinition;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface SectionDefinationDAO {

    public boolean getAerReportSubmittedStatus(String offcode);

    public ArrayList getSectionList(String offcode);

    public ArrayList getSectionListExcludeCurrentSection(String offcode, int selectedSectionId);

    public ArrayList getSPCList(String sectionId);

    public ArrayList getBillGroupWiseSectionList(BigDecimal billgroupid);

    public ArrayList getSPCWiseEmpInSection(int sectionid);

    public ArrayList getSPCWiseEmpInSection(int sectionid, String offcode);

    public SectionDtlSPCWiseEmp getSPCEmpSection(String empId);

    public SectionDtlSPCWiseEmp getSPCWiseContEmp(String empId);

    public ArrayList getSPCWiseContEmpInSection(int sectionid);

    public ArrayList getSPCWiseEmpOnlyInSection(int sectionid);

    public String getSectionName(int sectionid);

    public String getBillType(int sectionid);

    public ArrayList getTotalAvailableEmp(String offcode);

    public ArrayList getTotalAvailableSixYearContractEmp(String offcode);

    public ArrayList getTotalAssignEmp(String offcode, int sectionid);

    public ArrayList getTotalAvailableContractEmp(String offcode);

    public ArrayList getTotalAvailSpecialCategoryEmp(String offcode);   

    public ArrayList getTotalAssignContractEmp(String offcode, int sectionid);

    public ArrayList getTotalAssignSpecialCategoryEmp(String offcode, int sectionid);

    public String verifyMapPost(int sectionid, String spc);

    public void mapPost(int sectionid, String spc);

    public void removePost(String spc);

    public void saveBillSection(String offCode, SectionDefinition SD);

    public SectionDefinition getBillSection(int billSectionId);

    public void updateBillSection(SectionDefinition SD);

    public void updatePosition(String positionNo);

    public void resetPostSlNumber(String offCode, int sectionId);

    public SubstantivePost getSpcDetails(String spc);

    public void updateSpcDetails(SubstantivePost sp);

    public List sectionwiseSubstantivePostList(int sectionId);

    public ArrayList getSPCWiseSpCategEmpInSection(int sectionid);
    
    public ArrayList getTotalAvailDeputationEmp(String deptCode);

    public ArrayList getTotalAssignDeputationEmp(int sectionid);
    
}

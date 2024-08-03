/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.SubstantivePost;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Durga
 */
public interface SubStantivePostDAO {

    public List getSPCList(String offcode);

    public List getEmployeeWithSPCList(String offcode);

    public List getAllSPCWithEmployee(String offcode);

    public List getGenericPostList(String offcode);

    public List getGPCWiseSPCList(String empid, String offcode, String gpc);

    public ArrayList getCadreWiseOfficeWiseSPC(String cadreCode, String offCode);

    public List getPostListPaging(String offcode, String postToSearch, int page, int rows);

    public int getPostListCountPaging(String offcode, String postToSearch);

    public List getGPCWiseEmployeeList(String postcode, String offcode);

    public List getOfficeWithSPCList(String offcode);

    public List getCadreWisePostList(String offcode, String cadrecode);

    public List getSanctioningSPCOfficeWiseList(String offcode);

    public List getEmployeeWithSPCOfficeWiseList(String offcode);

    public List getApprovingSPCOfficeWiseList(String offcode);

    public List getSanctioningSPCOfficeWiseList(String deptcode, String offcode);

    public SubstantivePost getSpcDetail(String spc);

    public List getGPCWiseEmployeeListOnlySPC(String postcode);

    public List getPostCodeOfficeWise(String deptcode);

    public List getSPCListWithEmployeeName(String offCode, String postCode);

    public List getEmployeeNameWithSPC(String offCode, String postCode);

    public int updateSubstantivePost(String offCode, String gpc, String spc, String payscale, String payscale_7th, String postgrp, String paylevel, String gp, String cadre, String chkGrantInAid, String teachingPost, String planOrNonPlan);

    public int removeSubstantivePost(String offCode, String gpc, String spc, String filepath, MultipartFile spcTerminateFile, String terminationOrderno, String terminationOrderdate);

    public int addSubstantivePost(String deptCode, String offCode, SubstantivePost substantivePost, String filepath);

    public List listSubPost(SubstantivePost substantivePost);

    public int savePostdataDetails(SubstantivePost substantivePost);

    public List getGPCWiseSectionWiseSPCList(String offcode, String gpc);

    public List getGPCWiseSPCList(String offcode, String gpc);

    public ArrayList getSPCList(String offCode, String postCode);

    public String getDeptName(String deptCode);

    public void changeSPCstatus(String columnName, String spc, String colStatus);

    public SubstantivePost postDetails(String officeCode, String postCode);

    public List getAuthorityGenericPostList(String offcode);

    public ArrayList getAuthoritySubstantivePostList(String offcode, String postcode);

    public ArrayList getSubstantivePostListBacklogEntry(String offcode, String postcode);

    public ArrayList getVacantSubstantivePostList(String offcode, String postcode);

    public List getDuplicatePostList(String offcode);

    public List getDuplicatePostDetails(String spc);

    public ArrayList getUnmappedSPCAll(String offcode, String postcode);
    
    public boolean updateBlankSpc(String newSpc,String empid,String oldSpc,String officeCode);
    
    public SubstantivePost getEmpDetails(String empid);
    
    public List getGOIOficeWisePostList(String offcode);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.posttermination;

import com.itextpdf.text.Document;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface PostTerminationDAO {

    public ArrayList getScheduleIIPostTerminationList(String offcode, String spc, String fy);

    public ArrayList viewPostTerminationScheduleII(int proposalId);

    public void downloadPDFScheduleIIA(Document document, int propsalId, String offCode, String fy);

    public ArrayList getScheduleIIPostTerminationPostList(String offcode, String spc);

    public void saveTerminationNoDataSchedule2(String fy, String offcode, String empid, String empSpc, String aoOffCode, String editPropsalId);

    public ArrayList getScheduleIIPostTerminationCheckedList(String offCode, AnnualEstablishmentReportPostTerminatonForm ae);

    public void insertPostTerminationAtCOData(String coOffCode, String coempid, String coSpc, String aoOffCode, AnnualEstablishmentReportPostTerminatonForm ae);

    public ArrayList coWiseSubmittedPostTerminationList(String fy);

    public ArrayList aoViewSubmittedPostTerminationList(String offCode, String fy);

    public List getTerminationPostSchedule3(String fyear, String offCode);

    public List getAERPOstDetailsSchedule3(String offCode);

    public void saveTerminationNoDataSchedule3(String fy, String offcode, String empid, String empSpc);

    public List getAERCheckedPOstDetailsSchedule3(String fyear, String offCode, String getChkReportId);

    public void saveTerminationDataschedule3(AnnualEstablishment bean);

    public List getviewTSchedule3(String fyear, String offCode, int termId);

    public List getTerminationPostFD(String fyear);

    public List viewDeptWiseTermination(String offCode, String fyear);

    public ArrayList COWiseDeptPostTerminationStatus();

    public void downloadPDFScheduleIIIA(Document document, int termId, String offname, String offcode, String fy);

    public List getlistingTerminationPost(String fyear, String offCode);

    public void saveTerminationData(AnnualEstablishment bean);

    public List getviewTSchedule1(String fyear, String offCode, int termId);

    public void saveTerminationNoDataSchedule1(String fy, String offcode, String empid, String empSpc);

    public List getAERCheckedPOstDetails(String fyear, String offCode, String checkedGpC);

    public List getAERPOstDetails(String offCode, String fy);

    public void downloadPDFScheduleIA(Document document, int termId, String offname, String offcode, String fy);

    public void approvePostTerminationForCO(int proposalId, String fy);

    public void declinePostTerminationForCO(int proposalId, String fy, String revertReason);

    public ArrayList AOWiseSummaryReport(String offCode, String fy);

    public void SubmitConsolidatedDataToFD(String offcode, String empid, String spc, String fy);

    public String coFDStatus(String offCode, String fyear);

    public ArrayList DeptWisePostTerminationReport();

    public ArrayList COViewPostTerminationList(String offCode, String fy);

    public void approvePostTerminationForDDO(int proposalId, String fy);

    public void declinePostTerminationForDDO(int proposalId, String fy, String revertReason);
    
    public void downloadPDFScheduleIAByCO(Document document, int termId, String offname, String offcode, String fy);
    
    public ArrayList COWiseSummaryReport(String offCode, String fy);

}

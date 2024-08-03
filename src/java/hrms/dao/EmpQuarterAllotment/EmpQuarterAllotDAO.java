/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpQuarterAllotment;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.EmpQuarterAllotment.FundSanctionOrderBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manoj PC
 */
public interface EmpQuarterAllotDAO {

    public void saveEmpQuarterDetails(EmpQuarterBean eqBean, int notId);

    public List getFundAllotmentLog(int phslno);

    public List getQuarterList();

    public List getEmpQuarterAllotList(String empId);

    public boolean getAllotedQuarterCount(String empId);

    public EmpQuarterBean getAllotedQuarterDetail(String qaId);

    public void updateEmpQuarterDetails(EmpQuarterBean eqBean);

    public void saveSurrenderQuarter(String qaId, String empId, int notId, String surrenderDate);

    public List getOfficeWiseQuarterList(String officeCode);

    public List getQuarterUnitAreaList();

    public List getEquarterUnitList();

    public List getQuarterUnitList(String loginName);

    public List getQuarterUnitListRentOff();

    /*SMS to Occupant of Quarter Method*/
    public String saveQuarterRepairOrder(FundSanctionOrderBean fundSanctionOrderBean);

    public String saveQuarterRepairOrderDetail(FundSanctionOrderBean fundSanctionOrderBean);

    public String updateQuarterRepairOrderSMS(FundSanctionOrderBean fundSanctionOrderBean);

    public String deleteQuarterRepairOrderDetail(int fundid);

    public String sendQuarterRepairOrderSMS(FundSanctionOrderBean fundSanctionOrderBean);

    public List getQuarterRepairOrderList();

    public List getQuarterRepairOrderDetailList(int allotmentOrderId);

    public FundSanctionOrderBean getQuarterRepairOrderDetail(int allotmentOrderId);
    /*SMS to Occupant of Quarter Method*/

    public List getUnitAreawiseQuarterType(String unitArea);

    public List getEquarterUnitAreawiseQuarterType(String unitArea);

    public List getQuarterData(String qrtrunit, String qrtrtype);

    public List getQuarterDataRentOfficer(String qrtrunit, String qrtrtype);

    public boolean updateQuarterAllotment(EmpQuarterBean empQuarterBean);

    public boolean updateEmpQuarterAllotment(String newempId, String qrtrunit, String qrtrtype, String quarterNo);

    public EmpQuarterBean getQuarterDetail(int qaId);

    public List getTransferList();

    public List getTransferList(String UserName);

    public List searchtransferCategory(String Octype);

    public List searchtransferCategory(String Octype, String UserName);

    public void SaveRetentionData(EmpQuarterBean qbean);

    public void GeneratePDFRetention(Document document, String empId, String consumerNo);

    public void GeneratePDFExtendedRetention(Document document, String empId, String consumerNo);

    public void VacationNotice(EmpQuarterBean qbean);

    public void GeneratePDFVacationNotice(Document document, String empId, String consumerNo);

    public void SaveVacationStatus(EmpQuarterBean qbean);

    public void GeneratePDFIntimation(Document document, String empId, String consumerNo);

    public void GeneratePDFOPP(Document document, String empId, String consumerNo);

    public void EvictionNotice(EmpQuarterBean qbean);

    public void GeneratePDFEvictionNotice(Document document, String empId, String consumerNo, int CaseId);

    public List getQuarterDetails(String empId);

    public void saveSplCaseRetention(EmpQuarterBean qbean);

    public EmpQuarterBean getAttachedFileforSPlCase(int trackid, String empid);

    public List getSplCaseDetailList();

    public void UpdateSplCaseStatus(EmpQuarterBean qbean);

    public void SaveExtensionRetentionData(EmpQuarterBean qbean);

    public List getOppRequisitionList();

    public void saveOccupationVacationQrtDetails(EmpQuarterBean qbean, String loginName);

    public List getoccupationVacationList(String vstatus, String loginName, String fdate, String tdate);

    public int maxRentOrderNo();

    public int trackingID(String cno, String empid);

    public void SaveAutoRetentionData(EmpQuarterBean qbean);

    public String retentionStatus(String cno, String empid);

    public List getNotVacatedQrtList(String fdate, String tdate);

    public int savenocRequest(EmpQuarterBean qbean, String loginId, String spc);

    public EmpQuarterBean getQrtNocUpload(String empId, String cno);

    public void saveuploadNOC(EmpQuarterBean qbean);

    public void downloadExtensionRequest(Document document, String empId, String consumerNo);

    public void DocumentSubmissionRequest(EmpQuarterBean qbean);

    public void savedocumentsubmission(EmpQuarterBean qbean);

    public EmpQuarterBean downloadsubmissionDocument(String emp_id, String consumer_no, int oppcaseid, String dstatus);

    public List getDocumentSubmissionList(String otype);

    public String getQuarterPoolName(String qid);

    // public List getEquarterUnitList();
    //  public List getEquarterUnitAreawiseQuarterType(String unitArea);
    public List getbuildingDetails(String unitArea, String unitType);

    public List getLedgerEmployee(String qrtrunit, String qrtrtype, String qrtno);

    public EmpQuarterBean getLedgeremployeeDetails(String occid);

    public List getLedgerDataEmployeewise(String occId);

    public EmpQuarterBean getextentionRequestDetails(String empId, String cno);

    public void downloadNoticeOPP(Document document, int caseId);

    public void savesaveshowcausereply(EmpQuarterBean qbean);

    public void UpdateOPPSendStatus(EmpQuarterBean qbean);

    public void DownloadFiveOneOrder(Document document, PdfWriter writer, int caseId);

    public void DownloadFiveOneNotice(Document document, int caseId);

    public void Testsms(EmpQuarterBean qbean);

    public void saveWateruploadClearance(EmpQuarterBean qbean);

    public void saveappeal(EmpQuarterBean qbean);

    public void DownloadAppealNotice(Document document, int caseId);

    public void HideRecord(EmpQuarterBean qbean);

    public ArrayList getqtrTypeList(String qrtrunit);

    public ArrayList getbuildingList(String qrtrunit, String qrtrtype);

    public List searchretiringCategoryResult(String Octype,String fdate, String tdate);
    
    public List getoccupationVacationListqms(String vstatus, String loginName, Date fdate, Date tdate);
}

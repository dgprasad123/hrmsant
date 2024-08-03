package hrms.dao.performanceappraisal;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.common.SMSThread;
import hrms.dao.propertystatement.PropertyStatementDAOImpl;
import hrms.model.common.EmpPersonalInfo;
import hrms.model.common.FileAttribute;
import hrms.model.master.LeaveTypeBean;
import hrms.model.master.Training;
import hrms.model.parmast.AbsenteeBean;
import hrms.model.parmast.AcceptingHelperBean;
import hrms.model.parmast.AchievementBean;
import hrms.model.parmast.G_NRC_PAR_Reason;
import hrms.model.parmast.HWAttachements;
import hrms.model.parmast.InitiateOtherPARForm;
import hrms.model.parmast.InitiateOtherPARListBean;
import hrms.model.parmast.PARReportBean;
import hrms.model.parmast.PARTemplateBean;
import hrms.model.parmast.ParAbsenteeBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParLeaveCause;
import hrms.model.parmast.ParMaster;
import hrms.model.parmast.ParMasterWS;
import hrms.model.parmast.ParOtherDetails;
import hrms.model.parmast.ParSubmitForm;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.parmast.ReviewingHelperBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.web.multipart.MultipartFile;

public class PARBrowserDAOImpl implements PARBrowserDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    //private String uploadPath;
    protected MaxPARIdDAOImpl maxPARIdDAO;

    protected MaxPARAbsenteeIDDAOImpl maxPARAbsenteeIdDAO;

    protected MaxPARAchievementIDDAOImpl maxPARAchievementIdDAO;

    protected MaxPAROtherDetailsIDDAOImpl maxPAROtherDetailsIdDAO;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setMaxPARIdDAO(MaxPARIdDAOImpl maxPARIdDAO) {
        this.maxPARIdDAO = maxPARIdDAO;
    }

    public void setMaxPARAbsenteeIdDAO(MaxPARAbsenteeIDDAOImpl maxPARAbsenteeIdDAO) {
        this.maxPARAbsenteeIdDAO = maxPARAbsenteeIdDAO;
    }

    public void setMaxPARAchievementIdDAO(MaxPARAchievementIDDAOImpl maxPARAchievementIdDAO) {
        this.maxPARAchievementIdDAO = maxPARAchievementIdDAO;
    }

    public void setMaxPAROtherDetailsIdDAO(MaxPAROtherDetailsIDDAOImpl maxPAROtherDetailsIdDAO) {
        this.maxPAROtherDetailsIdDAO = maxPAROtherDetailsIdDAO;
    }

    @Override
    public List getPARList(String fiscalyear, String empid) {

        List<ParMaster> parlist = new ArrayList();
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParMaster pmast = null;

        try {
            //String parRevertDate = "";
            //parRevertDate = "01-APR-2024";
            con = this.repodataSource.getConnection();

            Date parRevertDate = (new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-01"));
            //String sql = "SELECT PARID,PAR_STATUS,PERIOD_FROM,PERIOD_TO,SPN FROM (SELECT * FROM PAR_MASTER WHERE EMP_ID='" + empid + "' AND FISCAL_YEAR='" + fiscalyear + "')PAR_MASTER INNER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC";

            /*Written Previously to get List but if 2 or more authoruty revert , while search par it shows 2 row d.t - 26-04-2024
            
             String sql = "SELECT initiatedby,IS_CLOSED,auth_remarks_closed,is_closed_reporting,IS_ADVERSED,SPN,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO, "
             + " PAR_STATUS,PARMAST.SPC,POST,PARMAST.TASK_ID,STATUS_NAME,TASK_MASTER.STATUS_ID,par_type,IS_DELETED,initiated_on,PARMAST.CADRE_CODE,CADRE_NAME, "
             + " post,nrc_submitted_on,TASK_ACTION_DATE FROM"
             + " (SELECT initiatedby,IS_ADVERSED,PARID,EMP_ID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID,"
             + " par_type,IS_DELETED,nrc_submitted_on FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? and (par_type is null or par_type = ''))PARMAST"
             + " LEFT OUTER JOIN WORKfLOW_LOG ON PARMAST.parid = WORKfLOW_LOG.REF_ID"
             + " LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
             + " LEFT OUTER JOIN g_process_status ON PARMAST.PAR_STATUS=g_process_status.STATUS_ID"
             + " LEFT OUTER JOIN G_CADRE ON PARMAST.CADRE_CODE=G_CADRE.CADRE_CODE"
             + " LEFT OUTER JOIN task_master on parmast.task_id=task_master.task_id"
             + " LEFT OUTER JOIN FINANCIAL_YEAR ON PARMAST.FISCAL_YEAR=FINANCIAL_YEAR.FY";
             */
            String sql = "SELECT (select task_action_date from workflow_log where ref_id=PARMAST.parid order by task_action_date desc limit 1)task_action_date,initiatedby,IS_CLOSED,auth_remarks_closed,is_closed_reporting,IS_ADVERSED,SPN,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO, "
                    + "PAR_STATUS,PARMAST.SPC,POST,PARMAST.TASK_ID,STATUS_NAME,TASK_MASTER.STATUS_ID,par_type,IS_DELETED,initiated_on,PARMAST.CADRE_CODE,CADRE_NAME, "
                    + "post,nrc_submitted_on FROM "
                    + "(SELECT initiatedby,IS_ADVERSED,PARID,EMP_ID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID, "
                    + "par_type,IS_DELETED,nrc_submitted_on FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? and (par_type is null or par_type = ''))PARMAST "
                    + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN g_process_status ON PARMAST.PAR_STATUS=g_process_status.STATUS_ID "
                    + "LEFT OUTER JOIN G_CADRE ON PARMAST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN task_master on parmast.task_id=task_master.task_id "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON PARMAST.FISCAL_YEAR=FINANCIAL_YEAR.FY ";

            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                /* if (rs.getString("initiatedby") != null && !rs.getString("initiatedby").equals("")) {
                    
                 pmast = new ParMaster();
                 pmast.setParid(rs.getInt("PARID"));
                 pmast.setParstatus(rs.getInt("PAR_STATUS"));
                 pmast.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                 pmast.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                 pmast.setSpn(rs.getString("SPN"));
                 pmast.setIsClosed(rs.getString("IS_CLOSED"));
                 pmast.setAuthRemarksClosed(rs.getString("auth_remarks_closed"));
                 parlist.add(pmast);
                 }
                 else {*/
                pmast = new ParMaster();
                pmast.setParid(rs.getInt("PARID"));
                pmast.setParstatus(rs.getInt("PAR_STATUS"));
                pmast.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                pmast.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                pmast.setParType(rs.getString("par_type"));
                pmast.setIsDeleted(rs.getString("IS_DELETED"));
                pmast.setSpn(rs.getString("SPN"));
                pmast.setAppraiseePost(rs.getString("post"));

                if (rs.getInt("PARID") != 17 && (rs.getInt("PARID") == 16 || rs.getInt("PARID") == 18 || rs.getInt("PARID") == 19)) {
                    pmast.setIsClosed("N");
                } else {
                    pmast.setIsClosed(rs.getString("IS_CLOSED"));
                }
                pmast.setAuthRemarksClosed(rs.getString("auth_remarks_closed"));
                pmast.setReportingRemarksClosed(rs.getString("is_closed_reporting"));
                boolean isEditable = isPAREditableByAppraisee(rs.getInt("PARID"));
                if (isEditable == true) {
                    pmast.setIseditable("Y");
                } else {
                    pmast.setIseditable("N");
                }
                if (rs.getDate("INITIATED_ON") != null && rs.getInt("PAR_STATUS") != 17) {
                    pmast.setSubmittedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else if (rs.getDate("INITIATED_ON") == null && rs.getInt("PAR_STATUS") == 17) {
                    if (rs.getDate("nrc_submitted_on") != null && !rs.getDate("nrc_submitted_on").equals("")) {
                        pmast.setSubmittedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("nrc_submitted_on")));
                    } else {
                        pmast.setSubmittedOn("");
                    }
                } else if (rs.getDate("INITIATED_ON") == null && rs.getDate("nrc_submitted_on") != null && rs.getInt("PAR_STATUS") == 0) {
                    pmast.setSubmittedOn("NOT SUBMITTED");
                }

                /* Written while all year PAR is Open with 2023-24
                 if (rs.getDate("TASK_ACTION_DATE") != null && rs.getDate("TASK_ACTION_DATE").after(parRevertDate)) {
                 pmast.setIsResubmitRevertPAR("Yes");
                 } else {
                 pmast.setIsResubmitRevertPAR("No");
                 }
                 */
                if (rs.getString("auth_remarks_closed") != null && !rs.getString("auth_remarks_closed").equals("") && rs.getString("auth_remarks_closed").equals("N")) {
                    if (rs.getDate("TASK_ACTION_DATE") != null && rs.getDate("TASK_ACTION_DATE").after(parRevertDate)) {
                        pmast.setIsResubmitRevertPAR("Yes");
                    } else {
                        pmast.setIsResubmitRevertPAR("No");
                    }
                } else {
                    pmast.setIsResubmitRevertPAR("No");
                }

                pmast.setCadreName(rs.getString("cadre_name"));
                parlist.add(pmast);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parlist;
    }

    @Override
    public List getPARListForSI(String fiscalyear, String empid) {
        List<ParMaster> parlist = new ArrayList();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ParMaster pmast = null;

        try {
            con = this.repodataSource.getConnection();
            //String sql = "SELECT PARID,PAR_STATUS,PERIOD_FROM,PERIOD_TO,SPN FROM (SELECT * FROM PAR_MASTER WHERE EMP_ID='" + empid + "' AND FISCAL_YEAR='" + fiscalyear + "')PAR_MASTER INNER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC";
            String sql = "SELECT initiatedby,si_is_closed,si_auth_remarks_closed_reporting,IS_ADVERSED,SPN,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO, "
                    + " PAR_STATUS,PARMAST.SPC,POST,PARMAST.TASK_ID,STATUS_NAME,TASK_MASTER.STATUS_ID,par_type FROM"
                    + " (SELECT initiatedby,IS_ADVERSED,PARID,EMP_ID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID,"
                    + " par_type FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? and par_type = 'SiPar')PARMAST"
                    + " LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
                    + " LEFT OUTER JOIN g_process_status ON PARMAST.PAR_STATUS=g_process_status.STATUS_ID"
                    + " LEFT OUTER JOIN task_master on parmast.task_id=task_master.task_id"
                    + " LEFT OUTER JOIN FINANCIAL_YEAR ON PARMAST.FISCAL_YEAR=FINANCIAL_YEAR.FY";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {
                /* if (rs.getString("initiatedby") != null && !rs.getString("initiatedby").equals("")) {
                    
                 pmast = new ParMaster();
                 pmast.setParid(rs.getInt("PARID"));
                 pmast.setParstatus(rs.getInt("PAR_STATUS"));
                 pmast.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                 pmast.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                 pmast.setSpn(rs.getString("SPN"));
                 pmast.setIsClosed(rs.getString("IS_CLOSED"));
                 pmast.setAuthRemarksClosed(rs.getString("auth_remarks_closed"));
                 parlist.add(pmast);
                 }
                 else {*/
                pmast = new ParMaster();
                pmast.setParid(rs.getInt("PARID"));

                pmast.setParstatus(rs.getInt("PAR_STATUS"));
                pmast.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                pmast.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                pmast.setParType(rs.getString("par_type"));
                pmast.setSpn(rs.getString("SPN"));

                if (rs.getInt("PARID") != 17 && (rs.getInt("PARID") == 16 || rs.getInt("PARID") == 18 || rs.getInt("PARID") == 19)) {
                    pmast.setIsClosed("N");
                } else {
                    pmast.setIsClosed(rs.getString("si_is_closed"));
                }
                pmast.setAuthRemarksClosed(rs.getString("si_auth_remarks_closed_reporting"));
                boolean isEditable = isPAREditableByAppraisee(rs.getInt("PARID"));
                if (isEditable == true) {
                    pmast.setIseditable("Y");
                } else {
                    pmast.setIseditable("N");
                }
                parlist.add(pmast);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parlist;
    }

    @Override
    public boolean isPAREditableByAppraisee(int parid) {
        boolean isEditable = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count(*) as cnt FROM PAR_REPORTING_TRAN WHERE PAR_ID=?");
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") == 0) {
                    isEditable = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isEditable;
    }

    @Override
    public boolean isSubmitByAppraiseeBeforeDate(int parid) {
        boolean isSubmitBeforeNewDate = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select count (*) as cnt from workflow_log where ref_id=? and task_action_date::date > '01-01-2024' and task_status_id=16");
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isSubmitBeforeNewDate = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isSubmitBeforeNewDate;
    }

    @Override
    public List getPARList(String empid) {
        List<ParMasterWS> parlist = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ParMasterWS pmast = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT IS_CLOSED,auth_remarks_closed,is_closed_reporting,IS_ADVERSED,SPN,PARID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,PARMAST.SPC,POST,PARMAST.TASK_ID,STATUS_NAME,TASK_MASTER.STATUS_ID,FISCAL_YEAR FROM"
                    + " (SELECT IS_ADVERSED,PARID,EMP_ID,FISCAL_YEAR,PERIOD_FROM,PERIOD_TO,PAR_STATUS,SPC,CADRE_CODE,TASK_ID,FISCAL_YEAR FROM"
                    + " PAR_MASTER WHERE EMP_ID=?)PARMAST"
                    + " LEFT OUTER JOIN G_SPC ON G_SPC.SPC=PARMAST.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
                    + " LEFT OUTER JOIN g_process_status ON PARMAST.PAR_STATUS=g_process_status.STATUS_ID"
                    + " LEFT OUTER JOIN task_master on parmast.task_id=task_master.task_id"
                    + " LEFT OUTER JOIN FINANCIAL_YEAR ON PARMAST.FISCAL_YEAR=FINANCIAL_YEAR.FY");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                pmast = new ParMasterWS();
                pmast.setParid(rs.getInt("PARID"));
                pmast.setFiscalYear(rs.getString("FISCAL_YEAR"));
                pmast.setParstatus(rs.getInt("PAR_STATUS"));
                pmast.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                pmast.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                pmast.setSpn(rs.getString("SPN"));
                pmast.setIsClosed(rs.getString("IS_CLOSED"));
                pmast.setAuthRemarksClosed(rs.getString("auth_remarks_closed"));
                pmast.setReportingRemarksClosed(rs.getString("is_closed_reporting"));
                parlist.add(pmast);
            }
        } catch (Exception e) {

        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parlist;
    }

    @Override
    public int savePAR(int pageno, ParMaster parmaster, ParOtherDetails parOtherDetails, String filepath) {

        Connection con = null;

        MultipartFile nrcfile = parmaster.getNrcattchfile();
        int parid = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        String selfappraisal_wf = "";
        String specialcontribution_wf = "";
        String factors_wf = "";
        PreparedStatement ps = null;

        try {
            con = dataSource.getConnection();
            Long curtime = new Date().getTime();
            if (pageno == 1) {
                if (parmaster.getHidparid() != null && !parmaster.getHidparid().equals("0")) {
                    parid = Integer.parseInt(parmaster.getHidparid());
                    ps = con.prepareStatement("update par_master set period_from=?,period_to=?,spc=?,cadre_code=?,off_code=?,"
                            + "post_group=?,headquarter=?,nrcreason=?,remarks=?,nrc_submitted_on=?,par_type=?,si_officer_type=?,"
                            + "place_of_posting=? where parid=? and emp_id=?");
                    ps.setTimestamp(1, new Timestamp(parmaster.getPeriodfrom().getTime()));
                    ps.setTimestamp(2, new Timestamp(parmaster.getPeriodto().getTime()));
                    ps.setString(3, parmaster.getSpc());
                    ps.setString(4, parmaster.getCadreCode());
                    ps.setString(5, parmaster.getOffCode());
                    ps.setString(6, parmaster.getPostGrp());
                    ps.setString(7, parmaster.getHeadqtr());
                    ps.setString(8, parmaster.getNrcReason());
                    ps.setString(9, parmaster.getRemarks());
                    ps.setTimestamp(10, new Timestamp(curtime));
                    if (parmaster.getParType().equals("SiPar")) {
                        ps.setString(11, parmaster.getParType());
                        ps.setString(12, parmaster.getSubInspectorType());
                        ps.setString(13, parmaster.getPlaceOfPosting());
                    } else {
                        ps.setString(11, null);
                        ps.setString(12, null);
                        ps.setString(13, null);
                    }
                    ps.setInt(14, Integer.parseInt(parmaster.getHidparid()));
                    ps.setString(15, parmaster.getEmpid());
                    ps.execute();
                    DataBaseFunctions.closeSqlObjects(ps);

                    String cadreCode = "";
                    pst = con.prepareStatement("SELECT CUR_CADRE_CODE FROM EMP_MAST WHERE EMP_ID=?");
                    pst.setString(1, parmaster.getEmpid());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        cadreCode = rs.getString("CUR_CADRE_CODE");
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);
                    if (cadreCode == null || cadreCode.equals("")) {
                        pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_CADRE_CODE=? WHERE EMP_ID=?");
                        pst.setString(1, parmaster.getCadreCode());
                        pst.setString(2, parmaster.getEmpid());
                        pst.executeUpdate();
                        DataBaseFunctions.closeSqlObjects(pst);

                        /*pst = conora.prepareStatement("UPDATE EMP_MAST SET CUR_CADRE_CODE=? WHERE EMP_ID=?");
                         pst.setString(1, parmaster.getCadreCode());
                         pst.setString(2, parmaster.getEmpid());
                         pst.executeUpdate();*/
                    }
                } else {
                    ps = con.prepareStatement("insert into par_master (emp_id,fiscal_year,period_from,period_to,par_status,spc,cadre_code,off_code,"
                            + "post_group,headquarter,nrcreason,remarks,nrc_submitted_on,par_type, si_officer_type,place_of_posting) "
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, parmaster.getEmpid());
                    ps.setString(2, parmaster.getFiscalyear());
                    ps.setTimestamp(3, new Timestamp(parmaster.getPeriodfrom().getTime()));
                    ps.setTimestamp(4, new Timestamp(parmaster.getPeriodto().getTime()));
                    ps.setInt(5, parmaster.getParstatus());
                    ps.setString(6, parmaster.getSpc());
                    ps.setString(7, parmaster.getCadreCode());
                    ps.setString(8, parmaster.getOffCode());
                    ps.setString(9, parmaster.getPostGrp());
                    ps.setString(10, parmaster.getHeadqtr());
                    ps.setString(11, parmaster.getNrcReason());
                    ps.setString(12, parmaster.getRemarks());
                    ps.setTimestamp(13, new Timestamp(curtime));
                    if (parmaster.getParType().equals("SiPar")) {
                        ps.setString(14, parmaster.getParType());
                        ps.setString(15, parmaster.getSubInspectorType());
                        ps.setString(16, parmaster.getPlaceOfPosting());
                    } else {
                        ps.setString(14, null);
                        ps.setString(15, null);
                        ps.setString(16, null);
                    }
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();
                    rs.next();
                    parid = rs.getInt("parid");
                    DataBaseFunctions.closeSqlObjects(rs, ps);

                    String cadreCode = "";
                    pst = con.prepareStatement("SELECT CUR_CADRE_CODE FROM EMP_MAST WHERE EMP_ID=?");
                    pst.setString(1, parmaster.getEmpid());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        cadreCode = rs.getString("CUR_CADRE_CODE");
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);
                    if (cadreCode == null || cadreCode.equals("")) {
                        pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_CADRE_CODE=? WHERE EMP_ID=?");
                        pst.setString(1, parmaster.getCadreCode());
                        pst.setString(2, parmaster.getEmpid());
                        pst.executeUpdate();
                        DataBaseFunctions.closeSqlObjects(pst);

                        /*pst = conora.prepareStatement("UPDATE EMP_MAST SET CUR_CADRE_CODE=? WHERE EMP_ID=?");
                         pst.setString(1, parmaster.getCadreCode());
                         pst.setString(2, parmaster.getEmpid());
                         pst.executeUpdate();*/
                    }
                }

                if (parmaster.getParstatus() == 17) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (nrcfile != null && !nrcfile.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = parmaster.getEmpid() + "_" + parid + "_" + time;

                        pst = con.prepareStatement("INSERT INTO PAR_NRC_DOCUMENT (EMP_ID, PAR_ID, DISK_FILE_NAME, ORG_FILE_NAME, FILE_TYPE ) VALUES(?,?,?,?,?)");
                        pst.setString(1, parmaster.getEmpid());
                        pst.setInt(2, parid);
                        pst.setString(3, filename);
                        pst.setString(4, nrcfile.getOriginalFilename());
                        pst.setString(5, nrcfile.getContentType());
                        pst.executeUpdate();
                        DataBaseFunctions.closeSqlObjects(pst);

                        String dirpath = filepath + parmaster.getFiscalyear() + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = nrcfile.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                }
            } else if (pageno == 4) {

                //Remove MS Word XML Tags
                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                selfappraisal = policy.sanitize(parOtherDetails.getSelfappraisal());
                specialcontribution = policy.sanitize(parOtherDetails.getSpecialcontribution());
                factors = policy.sanitize(parOtherDetails.getHinderreason());
                //Remove MS Word XML Tags

                selfappraisal = "<h4>" + selfappraisal + "</h4>";
                specialcontribution = "<h4>" + specialcontribution + "</h4>";
                factors = "<h4>" + factors + "</h4>";

                parOtherDetails.setSelfappraisal(selfappraisal);
                parOtherDetails.setSpecialcontribution(specialcontribution);
                parOtherDetails.setHinderreason(factors);

                HtmlToPlainText htp = new HtmlToPlainText();
                selfappraisal_wf = htp.getPlainText(Jsoup.parse(selfappraisal));
                specialcontribution_wf = htp.getPlainText(Jsoup.parse(specialcontribution));
                factors_wf = htp.getPlainText(Jsoup.parse(factors));

                if (parOtherDetails.getHidpaptid() != null && !parOtherDetails.getHidpaptid().equals("")) {
                    parOtherDetails.setPaptid(Integer.parseInt(parOtherDetails.getHidpaptid()));

                    ps = con.prepareStatement("update par_appraisee_tran set cur_spc=?, spc_in_period=?, place=?, selfappraisal=?, hinderreason=?, specialcontributiion=?,selfappraisal_wf=?,specialcontribution_wf=?,hinderreason_wf=?,fivet_component_appraise=? where paptid=? and emp_id=?");
                    ps.setString(1, parOtherDetails.getSpc());
                    ps.setString(2, parOtherDetails.getSpcinperiod());
                    ps.setString(3, parOtherDetails.getPlace());
                    ps.setString(4, parOtherDetails.getSelfappraisal());
                    ps.setString(5, parOtherDetails.getHinderreason());
                    ps.setString(6, parOtherDetails.getSpecialcontribution());
                    ps.setString(7, selfappraisal_wf);
                    ps.setString(8, specialcontribution_wf);
                    ps.setString(9, factors_wf);
                    ps.setString(10, parOtherDetails.getFiveTComponentappraise());
                    ps.setInt(11, parOtherDetails.getPaptid());
                    ps.setString(12, parOtherDetails.getEmpid());
                    ps.execute();
                    DataBaseFunctions.closeSqlObjects(ps);

                } else {
                    ps = con.prepareStatement("insert into par_appraisee_tran (parid,cur_spc,emp_id,spc_in_period,place,selfappraisal,hinderreason,specialcontributiion,selfappraisal_wf,specialcontribution_wf,hinderreason_wf,fivet_component_appraise) values(?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setInt(1, parOtherDetails.getHidparid());
                    ps.setString(2, parOtherDetails.getSpc());
                    ps.setString(3, parOtherDetails.getEmpid());
                    ps.setString(4, parOtherDetails.getSpcinperiod());
                    ps.setString(5, parOtherDetails.getPlace());
                    ps.setString(6, parOtherDetails.getSelfappraisal());
                    ps.setString(7, parOtherDetails.getHinderreason());
                    ps.setString(8, parOtherDetails.getSpecialcontribution());
                    ps.setString(9, selfappraisal_wf);
                    ps.setString(10, specialcontribution_wf);
                    ps.setString(11, factors_wf);
                    ps.setString(12, parOtherDetails.getFiveTComponentappraise());
                    ps.execute();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parid;
    }

    @Override
    public List getAbsenteeList(String empid, int parid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List absenteelist = new ArrayList();
        ParAbsenteeBean pbean = null;
        try {
            con = this.repodataSource.getConnection();
//            String sql = "SELECT TAB2.PARID PARID,TAB2.PABID PABID,TAB2.TYPEOFLEAVE,TAB2.LEAVEPURPOSE,TAB2.FROMDATE FROMDATE, "
//                + "TAB2.TODATE TODATE,TAB2.TOL TOL,LEAVE_CAUSE,TRAINING_TYPE,TAB2.oth_purpose FROM "
//                + "(SELECT TAB1.PARID,TAB1.PABID PABID,TAB1.TYPEOFLEAVE,TAB1.LEAVEPURPOSE,TAB1.FROMDATE FROMDATE, TAB1.TODATE TODATE,TAB1.TOL TOL,"
//                + "LEAVE_CAUSE,TAB1.oth_purpose FROM (SELECT PARID,PABID,TYPEOFLEAVE,LEAVEPURPOSE,FROMDATE,TODATE,TOL,oth_purpose FROM "
//                + "(SELECT PAR_ABSENTEE.PARID,PABID,FROMDATE,TODATE,TYPEOFLEAVE,LEAVEPURPOSE,oth_purpose "
//                + "FROM (SELECT PARID FROM PAR_MASTER WHERE EMP_ID=?)PARMAST "
//                + "INNER JOIN PAR_ABSENTEE ON PARMAST.PARID=PAR_ABSENTEE.PARID AND PAR_ABSENTEE.PARID=?)TAB "
//                + "LEFT OUTER JOIN G_LEAVE_MASTER ON G_LEAVE_MASTER.TOL_ID=TAB.TYPEOFLEAVE)TAB1 "
//                + "LEFT OUTER JOIN PAR_LEAVE_CAUSE ON PAR_LEAVE_CAUSE.PAR_LEAVE_ID=TAB1.LEAVEPURPOSE)TAB2 "
//                + "LEFT OUTER JOIN hrmis2.G_TRAINING_TYPE ON G_TRAINING_TYPE.TRAINING_TYPE_ID=TAB2.TYPEOFLEAVE ORDER BY PABID";

            String sql = "SELECT TAB2.PARID PARID,TAB2.PABID PABID,TAB2.TYPEOFLEAVE,TAB2.LEAVEPURPOSE,TAB2.FROMDATE FROMDATE,"
                    + " TAB2.TODATE TODATE,TAB2.TOL TOL,LEAVE_CAUSE,TRAINING_TYPE FROM"
                    + " (SELECT TAB1.PARID,TAB1.PABID PABID,TAB1.TYPEOFLEAVE,TAB1.LEAVEPURPOSE,TAB1.FROMDATE FROMDATE, TAB1.TODATE TODATE,TAB1.TOL TOL,LEAVE_CAUSE FROM"
                    + " (SELECT PARID,PABID,TYPEOFLEAVE,LEAVEPURPOSE,FROMDATE,TODATE,TOL FROM(SELECT PAR_ABSENTEE.PARID,PABID,FROMDATE,TODATE,TYPEOFLEAVE,LEAVEPURPOSE FROM"
                    + " (SELECT PARID FROM PAR_MASTER WHERE EMP_ID=?)PARMAST"
                    + " INNER JOIN PAR_ABSENTEE ON PARMAST.PARID=PAR_ABSENTEE.PARID AND"
                    + " PAR_ABSENTEE.PARID=?)TAB"
                    + " LEFT OUTER JOIN G_LEAVE_MASTER ON G_LEAVE_MASTER.TOL_ID=TAB.TYPEOFLEAVE)TAB1"
                    + " LEFT OUTER JOIN PAR_LEAVE_CAUSE ON PAR_LEAVE_CAUSE.PAR_LEAVE_ID=TAB1.LEAVEPURPOSE)TAB2"
                    + " LEFT OUTER JOIN G_TRAINING_TYPE ON"
                    + " G_TRAINING_TYPE.TRAINING_TYPE_ID=TAB2.TYPEOFLEAVE ORDER BY PABID";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pbean = new ParAbsenteeBean();
                pbean.setPabid(rs.getInt("PABID"));
                pbean.setHidparid(rs.getInt("PARID"));
                pbean.setFromdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROMDATE")));
                pbean.setTodate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TODATE")));
                pbean.setLeavecause(rs.getString("LEAVE_CAUSE"));
                if (rs.getString("LEAVEPURPOSE") != null && rs.getString("LEAVEPURPOSE").equals("L")) {
                    pbean.setLeaveortrainingtype(rs.getString("TOL"));
                } else if (rs.getString("LEAVEPURPOSE") != null && rs.getString("LEAVEPURPOSE").equals("T")) {
                    pbean.setLeaveortrainingtype(rs.getString("TRAINING_TYPE"));
                }
                absenteelist.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absenteelist;
    }

    @Override
    public void saveAbsentee(ParAbsenteeBean parabsentee) {

        Connection con = null;
        PreparedStatement pst = null;

        String sql = "";
        int pabid = 0;
        try {
            con = dataSource.getConnection();
            if (parabsentee.getHidpabid() > 0) {
                parabsentee.setPabid(parabsentee.getHidpabid());
                //session.update(parabsentee);
                sql = "UPDATE PAR_ABSENTEE SET PARID=?,FROMDATE=?,TODATE=?,LEAVEPURPOSE=?,TYPEOFLEAVE=?,oth_purpose=? WHERE PABID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parabsentee.getHidparid());
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(parabsentee.getFromdate()).getTime()));
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(parabsentee.getTodate()).getTime()));
                pst.setString(4, parabsentee.getLeavecause());
                pst.setString(5, parabsentee.getLeaveortrainingtype());
                pst.setString(6, parabsentee.getAbsenceCauseOth());
                pst.setInt(7, parabsentee.getHidpabid());
                pst.executeUpdate();
                //DataBaseFunctions.closeSqlObjects(pst);
            } else {
                sql = "INSERT INTO PAR_ABSENTEE(PARID,FROMDATE,TODATE,LEAVEPURPOSE,TYPEOFLEAVE,oth_purpose) VALUES(?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parabsentee.getHidparid());
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(parabsentee.getFromdate()).getTime()));
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(parabsentee.getTodate()).getTime()));
                pst.setString(4, parabsentee.getLeavecause());
                pst.setString(5, parabsentee.getLeaveortrainingtype());
                pst.setString(6, parabsentee.getAbsenceCauseOth());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAchievementList(String empid, int parid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParAchievement pachvbean = null;
        List<ParAchievement> achievementlist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT PACID,PAR_MASTER.PARID,THETASK,TARGET,ACHIEVEMENTPERCENT,ACHIEVEMENT,ATT_ID,ACHIEVEPERCQUALITATIVE,SLNO FROM PAR_ACHIEVEMENT"
                    + " INNER JOIN PAR_MASTER ON PAR_ACHIEVEMENT.PARID=PAR_MASTER.PARID WHERE PAR_MASTER.EMP_ID=? AND PAR_ACHIEVEMENT.PARID=? ORDER BY SLNO ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pachvbean = new ParAchievement();
                pachvbean.setHidpacid(rs.getInt("PACID"));
                pachvbean.setHidparid(rs.getInt("PARID"));
                pachvbean.setSlno(rs.getInt("SLNO"));
                pachvbean.setTask(StringUtils.defaultString(rs.getString("THETASK")));
                pachvbean.setTarget(StringUtils.defaultString(rs.getString("TARGET")));
                pachvbean.setAchievement(StringUtils.defaultString(rs.getString("ACHIEVEMENT")));
                pachvbean.setAchievementpercent(StringUtils.defaultString(rs.getString("ACHIEVEMENTPERCENT")));
                achievementlist.add(pachvbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return achievementlist;
    }

    @Override
    public void saveAchievement(String empid, ParAchievement parachievement, String filepath) {

        Connection con = null;
        ResultSet res = null;
        MultipartFile file = parachievement.getAttchfile();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        //HWAttachements hwattach = new HWAttachements();
        int attid = 0;
        boolean isCreated = false;

        PreparedStatement pst = null;
        int pacid = 0;
        try {
            //session = this.sessionFactory.openSession();
            con = dataSource.getConnection();

            String achievement = StringUtils.defaultString(parachievement.getAchievement());
            achievement = StringUtils.replace(achievement, "'", "''");
            if (parachievement.getHidpacid() > 0) {
                pst = con.prepareStatement("UPDATE PAR_ACHIEVEMENT SET PARID=?,SLNO=?,THETASK=?,TARGET=?,ACHIEVEMENT=?,ACHIEVEMENTPERCENT=?,ACHIEVEPERCQUALITATIVE=? WHERE PACID=?");
                pst.setInt(1, parachievement.getHidparid());
                pst.setInt(2, parachievement.getSlno());
                pst.setString(3, parachievement.getTask());
                pst.setString(4, parachievement.getTarget());
                pst.setString(5, achievement);
                pst.setString(6, parachievement.getAchievementpercent());
                pst.setString(7, parachievement.getAchievementpersqualitative());
                pst.setInt(8, parachievement.getHidpacid());
                pacid = parachievement.getHidpacid();
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);
            } else {
                pst = con.prepareStatement("INSERT INTO PAR_ACHIEVEMENT(PARID,SLNO,THETASK,TARGET,ACHIEVEMENT,ACHIEVEMENTPERCENT,ACHIEVEPERCQUALITATIVE)VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, parachievement.getHidparid());
                pst.setInt(2, parachievement.getSlno());
                pst.setString(3, parachievement.getTask());
                pst.setString(4, parachievement.getTarget());
                pst.setString(5, achievement);
                pst.setString(6, parachievement.getAchievementpercent());
                pst.setString(7, parachievement.getAchievementpersqualitative());
                pst.executeUpdate();
                res = pst.getGeneratedKeys();
                res.next();
                pacid = res.getInt("pacid");
                DataBaseFunctions.closeSqlObjects(res, pst);
            }

            long time = System.currentTimeMillis();
            if (file != null && !file.isEmpty()) {
                String filename = empid + "_" + parachievement.getHidparid() + "_" + time;

                File newFile = null;
                String dirpath = filepath + parachievement.getFiscalyear() + "/";
                newFile = new File(dirpath);
                if (!newFile.exists()) {
                    //newFile.createNewFile();
                    newFile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = file.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                dirpath = filepath + parachievement.getFiscalyear() + "/" + filename;
                newFile = new File(dirpath);
                if (newFile.exists()) {
                    isCreated = true;
                }

                if (isCreated) {
                    attid = CommonFunctions.getMaxCodeInteger("HW_ATTACHMENTS", "ATT_ID", con);
                    pst = con.prepareStatement("INSERT INTO HW_ATTACHMENTS (ATT_ID, DISK_FILE_NAME, ORIGINAL_FILENAME, FILE_TYPE ) VALUES(?,?,?,?)");
                    pst.setInt(1, attid);
                    pst.setString(2, filename);
                    pst.setString(3, file.getOriginalFilename());
                    pst.setString(4, file.getContentType());
                    pst.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pst);

                    pst = con.prepareStatement("UPDATE PAR_ACHIEVEMENT SET ATT_ID=? WHERE PACID=?");
                    pst.setInt(1, attid);
                    pst.setInt(2, pacid);
                    pst.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ParMaster getAppraiseInfo(String empid, int parid) {
        ParMaster parmaster = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select parid,emp_id,fiscal_year,period_from,period_to,par_status,p.spc,spn,p.cadre_code,"
                    + "cadre_name,p.off_code,off_en,task_id,post_group,is_auth_set,ref_id_of_table,headquarter,remarks,nrcreason,NRC_SUBMITTED_ON,"
                    + "is_adversed, par_type, si_officer_type,place_of_posting,REASON_ID,REASON from par_master p "
                    + "left outer join g_spc g on p.spc=g.spc "
                    + "left outer join g_office go on p.off_code=go.off_code "
                    + "left outer join g_cadre gc on p.cadre_code=gc.cadre_code "
                    + "left outer join  G_PAR_NRC_REASON on p.nrcreason = G_PAR_NRC_REASON.reason_id "
                    + "where parid=? and p.emp_id=?");
            ps.setInt(1, parid);
            ps.setString(2, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                parmaster = new ParMaster();
                parmaster.setParid(rs.getInt("parid"));
                parmaster.setEmpid(rs.getString("emp_id"));
                parmaster.setFiscalyear(rs.getString("fiscal_year"));
                parmaster.setPeriodfrom(CommonFunctions.getFormattedOutputDate(rs.getDate("period_from")));
                parmaster.setPeriodto(CommonFunctions.getFormattedOutputDate(rs.getDate("period_to")));
                parmaster.setParstatus(rs.getInt("par_status"));
                parmaster.setSpc(rs.getString("spc"));
                parmaster.setSpn(rs.getString("spn"));
                parmaster.setCadreCode(rs.getString("cadre_code"));
                parmaster.setCadreName(rs.getString("cadre_name"));
                parmaster.setOffCode(rs.getString("off_code"));
                parmaster.setOffname(rs.getString("off_en"));
                parmaster.setTaskid(rs.getInt("task_id"));
                parmaster.setPostGrp(rs.getString("is_auth_set"));
                parmaster.setPostGroupAppraise(rs.getString("post_group"));
                parmaster.setHeadqtr(rs.getString("headquarter"));
                parmaster.setRemarks(rs.getString("remarks"));
                parmaster.setNrcReason(rs.getString("nrcreason"));
                parmaster.setSubmittedOn(rs.getString("nrc_submitted_on"));
                parmaster.setNrcReasonDetail(rs.getString("REASON"));

                parmaster.setParType(rs.getString("par_type"));
                parmaster.setSubInspectorType(rs.getString("si_officer_type"));
                parmaster.setPlaceOfPosting(rs.getString("place_of_posting"));

                boolean isEditable = isPAREditableByAppraisee(rs.getInt("PARID"));
                if (isEditable == true) {
                    parmaster.setIseditable("Y");
                } else {
                    parmaster.setIseditable("N");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parmaster;
    }

    @Override
    public ParOtherDetails getOtherDetails(String empid, int parid) {

        Connection con = null;

        ParOtherDetails parOtherDetails = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String selfappraisal = "";
        String specialcontribution = "";
        String factors = "";
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT PAPTID,PARID,CUR_SPC,EMP_ID,SPC_IN_PERIOD,SUBMITTED_ON,PLACE,SELFAPPRAISAL,HINDERREASON,SPECIALCONTRIBUTIION,fivet_component_appraise FROM PAR_APPRAISEE_TRAN WHERE PARID = ? AND EMP_ID = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                parOtherDetails = new ParOtherDetails();
                parOtherDetails.setPaptid(rs.getInt("PAPTID"));
                parOtherDetails.setHidparid(rs.getInt("PARID"));
                parOtherDetails.setSpc(rs.getString("CUR_SPC"));
                parOtherDetails.setEmpid(rs.getString("EMP_ID"));
                parOtherDetails.setPlace(rs.getString("PLACE"));
                parOtherDetails.setSelfappraisal(rs.getString("SELFAPPRAISAL"));
                parOtherDetails.setSpecialcontribution(rs.getString("SPECIALCONTRIBUTIION"));
                parOtherDetails.setHinderreason(rs.getString("HINDERREASON"));
                parOtherDetails.setFiveTComponentappraise(rs.getString("fivet_component_appraise"));

                PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                selfappraisal = policy.sanitize(parOtherDetails.getSelfappraisal());
                specialcontribution = policy.sanitize(parOtherDetails.getSpecialcontribution());
                factors = policy.sanitize(parOtherDetails.getHinderreason());

                parOtherDetails.setSelfappraisal(selfappraisal);
                parOtherDetails.setSpecialcontribution(specialcontribution);
                parOtherDetails.setHinderreason(factors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parOtherDetails;
    }

    @Override
    public List getAbsenceCauseList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List absencecauselist = new ArrayList();
        ParLeaveCause lc = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT PAR_LEAVE_ID,LEAVE_CAUSE FROM PAR_LEAVE_CAUSE ORDER BY LEAVE_CAUSE ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                lc = new ParLeaveCause();
                lc.setParleaveid(rs.getString("PAR_LEAVE_ID"));
                lc.setLeavecause(rs.getString("LEAVE_CAUSE"));
                absencecauselist.add(lc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absencecauselist;
    }

    @Override
    public List getLeaveTypeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List leavetypelist = new ArrayList();
        LeaveTypeBean glm = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT TOL_ID,TOL FROM G_LEAVE_MASTER ORDER BY TOL ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                glm = new LeaveTypeBean();
                glm.setTolid(rs.getString("TOL_ID"));
                glm.setTol(rs.getString("TOL"));
                leavetypelist.add(glm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leavetypelist;
    }

    @Override
    public String sendPar(ParSubmitForm pf) throws Exception {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean isBlank = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String tempfrmdt = "";
        String temptodt = "";
        String invalidperiod = "N";
        try {
            con = dataSource.getConnection();
            if (pf.getHidparstatus() == 0 || pf.getHidparstatus() == 16) {
                int hierarchyno = pf.getMaxhierarchyno();
                for (int i = 0; pf.getHidReportingEmpId() != null && i < pf.getHidReportingEmpId().length; i++) {
                    if (pf.getHidReportingEmpId()[i] != null && !pf.getHidReportingEmpId()[i].equals("")) {
                        tempfrmdt = pf.getTxtReportingAuthFrmDt()[i];
                        temptodt = pf.getTxtReportingAuthToDt()[i];
                        int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                        if (diffInDays >= 120) {
                            /**/
                            /*if (pf.getDateOfForceForwardFromReportingToReviewing() != null && !pf.getDateOfForceForwardFromReportingToReviewing().equals("")) {
                             pst = con.prepareStatement("INSERT INTO PAR_REPORTING_TRAN (PAR_ID,REPORTING_CUR_SPC,REPORTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE,is_completed,SUBMITTED_ON)VALUES(?,?,?,?,?,?,?,?)");
                             pst.setInt(1, pf.getHidparid());
                             pst.setString(2, pf.getHidReportingSpcCode()[i]);
                             pst.setString(3, pf.getHidReportingEmpId()[i]);
                             pst.setInt(4, hierarchyno);
                             pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                             pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                             pst.setString(7, "F");
                             pst.setTimestamp(8, new Timestamp(sdf.parse(pf.getDateOfForceForwardFromReportingToReviewing()).getTime()));
                             pst.executeUpdate(); */

                            pst = con.prepareStatement("INSERT INTO PAR_REPORTING_TRAN (PAR_ID,REPORTING_CUR_SPC,REPORTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, pf.getHidparid());
                            pst.setString(2, pf.getHidReportingSpcCode()[i]);
                            pst.setString(3, pf.getHidReportingEmpId()[i]);
                            pst.setInt(4, hierarchyno);
                            pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                            pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                            pst.executeUpdate();

                        } else if (diffInDays < 120) {
                            invalidperiod = "REP";
                        }
                        hierarchyno = hierarchyno + 1;
                    } else {
                        isBlank = true;
                    }
                }
                if (isBlank == true) {
                    pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                    pst.setInt(1, pf.getHidparid());
                    pst.setInt(2, pf.getMaxhierarchyno());
                    pst.executeUpdate();

                    for (int i = 0; pf != null && i < pf.getHidReviewingEmpId().length; i++) {
                        if (pf.getHidReviewingEmpId()[i] != null && !pf.getHidReviewingEmpId()[i].equals("")) {
                            tempfrmdt = pf.getTxtReviewingAuthFrmDt()[i];
                            temptodt = pf.getTxtReviewingAuthToDt()[i];
                            int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                            //if (diffInDays >= 120) {

                            pst = con.prepareStatement("INSERT INTO PAR_REVIEWING_TRAN (PAR_ID,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, pf.getHidparid());
                            pst.setString(2, pf.getHidReviewingpcCode()[i]);
                            pst.setString(3, pf.getHidReviewingEmpId()[i]);
                            pst.setInt(4, i + 1);
                            pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                            pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                            pst.executeUpdate();
                        } else {
                            isBlank = true;
                        }
                    }

                } else {
                    for (int i = 0; pf != null && i < pf.getHidReviewingEmpId().length; i++) {
                        if (pf.getHidReviewingEmpId()[i] != null && !pf.getHidReviewingEmpId()[i].equals("")) {
                            tempfrmdt = pf.getTxtReviewingAuthFrmDt()[i];
                            temptodt = pf.getTxtReviewingAuthToDt()[i];
                            int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                            //if (diffInDays >= 120) {

                            pst = con.prepareStatement("INSERT INTO PAR_REVIEWING_TRAN (PAR_ID,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, pf.getHidparid());
                            pst.setString(2, pf.getHidReviewingpcCode()[i]);
                            pst.setString(3, pf.getHidReviewingEmpId()[i]);
                            pst.setInt(4, i + 1);
                            pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                            pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                            pst.executeUpdate();

                            /*} else if (diffInDays < 120) {
                             if (invalidperiod.equals("N")) {
                             pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                             pst.setInt(1, pf.getHidparid());
                             pst.setInt(2, pf.getMaxhierarchyno());
                             pst.executeUpdate();

                             invalidperiod = "REV";
                             }
                             }*/
                        } else {
                            isBlank = true;
                        }
                    }

                    if (isBlank == true) {
                        pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                        pst.setInt(1, pf.getHidparid());
                        pst.setInt(2, pf.getMaxhierarchyno());
                        pst.executeUpdate();

                        pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                        pst.setInt(1, pf.getHidparid());
                        pst.executeUpdate();

                    } else {
                        for (int i = 0; i < pf.getHidAcceptingEmpId().length; i++) {
                            if (pf.getHidAcceptingEmpId()[i] != null && !pf.getHidAcceptingEmpId()[i].equals("")) {
                                tempfrmdt = pf.getTxtAcceptingAuthFrmDt()[i];
                                temptodt = pf.getTxtAcceptingAuthToDt()[i];
                                int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                                //if (diffInDays >= 120) {

                                pst = con.prepareStatement("INSERT INTO PAR_ACCEPTING_TRAN (PAR_ID,ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE) VALUES(?,?,?,?,?,?)");
                                pst.setInt(1, pf.getHidparid());
                                pst.setString(2, pf.getHidAcceptingSpcCode()[i]);
                                pst.setString(3, pf.getHidAcceptingEmpId()[i]);
                                pst.setInt(4, i + 1);
                                pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                                pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                                pst.executeUpdate();

                                /*} else if (diffInDays < 120) {
                                 if (invalidperiod.equals("N")) {
                                 pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                                 pst.setInt(1, pf.getHidparid());
                                 pst.setInt(2, pf.getMaxhierarchyno());
                                 pst.executeUpdate();

                                 pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                                 pst.setInt(1, pf.getHidparid());
                                 pst.executeUpdate();

                                 invalidperiod = "ACP";
                                 }
                                 }*/
                            } else {
                                isBlank = true;
                            }
                        }
                    }

                    if (isBlank == true) {
                        pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                        pst.setInt(1, pf.getHidparid());
                        pst.setInt(2, pf.getMaxhierarchyno());
                        pst.executeUpdate();

                        pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                        pst.setInt(1, pf.getHidparid());
                        pst.executeUpdate();

                        pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                        pst.setInt(1, pf.getHidparid());
                        pst.executeUpdate();

                    } else {
                        if (invalidperiod.equals("N")) {
                            pst = con.prepareStatement("UPDATE PAR_MASTER SET IS_AUTH_SET='Y' WHERE PARID=?");
                            pst.setInt(1, pf.getHidparid());
                            pst.executeUpdate();

                            startWorkFlow(con, pf.getHidparid(), pf.getHidtaskid());
                        } else {
                            pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                            pst.setInt(1, pf.getHidparid());
                            pst.setInt(2, pf.getMaxhierarchyno());
                            pst.executeUpdate();

                            pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                            pst.setInt(1, pf.getHidparid());
                            pst.executeUpdate();

                            pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                            pst.setInt(1, pf.getHidparid());
                            pst.executeUpdate();

                        }
                    }
                }
            } else if (pf.getHidparstatus() == 18) {

                int hierarchyno = getmaxhierachy(pf.getHidparid(), 16);
                for (int i = 0; pf.getHidReportingEmpId() != null && i < pf.getHidReportingEmpId().length; i++) {
                    if (pf.getHidReportingEmpId()[i] != null && !pf.getHidReportingEmpId()[i].equals("")) {
                        tempfrmdt = pf.getTxtReportingAuthFrmDt()[i];
                        temptodt = pf.getTxtReportingAuthToDt()[i];
                        int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                        if (diffInDays >= 120) {
                            pst = con.prepareStatement("INSERT INTO PAR_REPORTING_TRAN (PAR_ID,REPORTING_CUR_SPC,REPORTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, pf.getHidparid());
                            pst.setString(2, pf.getHidReportingSpcCode()[i]);
                            pst.setString(3, pf.getHidReportingEmpId()[i]);
                            pst.setInt(4, hierarchyno);
                            pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                            pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                            pst.executeUpdate();

                        } else if (diffInDays < 120) {
                            invalidperiod = "REP";
                        }
                        hierarchyno = hierarchyno + 1;
                    }
                }

                hierarchyno = pf.getMaxhierarchyno();
                for (int i = 0; pf.getHidReviewingEmpId() != null && i < pf.getHidReviewingEmpId().length; i++) {
                    if (pf.getHidReviewingEmpId()[i] != null && !pf.getHidReviewingEmpId()[i].equals("")) {
                        tempfrmdt = pf.getTxtReviewingAuthFrmDt()[i];
                        temptodt = pf.getTxtReviewingAuthToDt()[i];
                        int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                        //if (diffInDays >= 120) {

                        pst = con.prepareStatement("INSERT INTO PAR_REVIEWING_TRAN (PAR_ID,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                        pst.setInt(1, pf.getHidparid());
                        pst.setString(2, pf.getHidReviewingpcCode()[i]);
                        pst.setString(3, pf.getHidReviewingEmpId()[i]);
                        pst.setInt(4, hierarchyno);
                        pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                        pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                        pst.executeUpdate();

                        /*} else if (diffInDays < 120) {
                         if (invalidperiod.equals("N")) {
                         invalidperiod = "REV";
                         }
                         }*/
                        hierarchyno = hierarchyno + 1;
                    } else {
                        isBlank = true;
                    }
                }
                if (isBlank == true) {
                    pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                    pst.setInt(1, pf.getHidparid());
                    pst.setInt(2, pf.getMaxhierarchyno());
                    pst.executeUpdate();

                } else if (isBlank == false) {
                    for (int i = 0; i < pf.getHidAcceptingEmpId().length; i++) {
                        if (pf.getHidAcceptingEmpId()[i] != null && !pf.getHidAcceptingEmpId()[i].equals("")) {
                            tempfrmdt = pf.getTxtAcceptingAuthFrmDt()[i];
                            temptodt = pf.getTxtAcceptingAuthToDt()[i];
                            int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                            //if (diffInDays >= 120) {

                            pst = con.prepareStatement("INSERT INTO PAR_ACCEPTING_TRAN (PAR_ID,ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE) VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, pf.getHidparid());
                            pst.setString(2, pf.getHidAcceptingSpcCode()[i]);
                            pst.setString(3, pf.getHidAcceptingEmpId()[i]);
                            pst.setInt(4, i + 1);
                            pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                            pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                            pst.executeUpdate();

                            /*} else if (diffInDays < 120) {
                             if (invalidperiod.equals("N")) {
                             pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                             pst.setInt(1, pf.getHidparid());
                             pst.setInt(2, pf.getMaxhierarchyno());
                             pst.executeUpdate();

                             invalidperiod = "ACP";
                             }
                             }*/
                        } else {
                            isBlank = true;
                        }
                    }
                    if (isBlank == true) {
                        pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                        pst.setInt(1, pf.getHidparid());
                        pst.setInt(2, hierarchyno);
                        pst.executeUpdate();

                        pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                        pst.setInt(1, pf.getHidparid());
                        pst.executeUpdate();

                    } else {
                        if (invalidperiod.equals("N")) {
                            pst = con.prepareStatement("UPDATE PAR_MASTER SET IS_AUTH_SET='Y' WHERE PARID=?");
                            pst.setInt(1, pf.getHidparid());
                            pst.executeUpdate();

                            startWorkFlow(con, pf.getHidparid(), pf.getHidtaskid());
                        } else {
                            pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                            pst.setInt(1, pf.getHidparid());
                            pst.setInt(2, hierarchyno);
                            pst.executeUpdate();

                            pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                            pst.setInt(1, pf.getHidparid());
                            pst.executeUpdate();

                        }
                    }
                }
            } else if (pf.getHidparstatus() == 19) {
                int hierarchyno = getmaxhierachy(pf.getHidparid(), 18);
                for (int i = 0; pf.getHidReviewingEmpId() != null && i < pf.getHidReviewingEmpId().length; i++) {
                    if (pf.getHidReviewingEmpId()[i] != null && !pf.getHidReviewingEmpId()[i].equals("")) {
                        tempfrmdt = pf.getTxtReviewingAuthFrmDt()[i];
                        temptodt = pf.getTxtReviewingAuthToDt()[i];
                        int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                        //if (diffInDays >= 120) {

                        pst = con.prepareStatement("INSERT INTO PAR_REVIEWING_TRAN (PAR_ID,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                        pst.setInt(1, pf.getHidparid());
                        pst.setString(2, pf.getHidReviewingpcCode()[i]);
                        pst.setString(3, pf.getHidReviewingEmpId()[i]);
                        pst.setInt(4, hierarchyno);
                        pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                        pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                        pst.executeUpdate();

                        /*} else if (diffInDays < 120) {
                         if (invalidperiod.equals("N")) {
                         invalidperiod = "REV";
                         }
                         }*/
                        hierarchyno = hierarchyno + 1;

                    } else {
                        isBlank = true;
                    }
                }
                hierarchyno = pf.getMaxhierarchyno();
                for (int i = 0; i < pf.getHidAcceptingEmpId().length; i++) {
                    if (pf.getHidAcceptingEmpId()[i] != null && !pf.getHidAcceptingEmpId()[i].equals("")) {
                        tempfrmdt = pf.getTxtAcceptingAuthFrmDt()[i];
                        temptodt = pf.getTxtAcceptingAuthToDt()[i];
                        int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                        //if (diffInDays >= 120) {
                        pst = con.prepareStatement("INSERT INTO PAR_ACCEPTING_TRAN (PAR_ID,ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                        pst.setInt(1, pf.getHidparid());
                        pst.setString(2, pf.getHidAcceptingSpcCode()[i]);
                        pst.setString(3, pf.getHidAcceptingEmpId()[i]);
                        pst.setInt(4, hierarchyno);
                        pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                        pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                        pst.executeUpdate();
                        /*} else if (diffInDays < 120) {
                         if (invalidperiod.equals("N")) {
                         invalidperiod = "ACP";
                         }
                         }*/
                        hierarchyno = hierarchyno + 1;

                    } else {
                        isBlank = true;
                    }
                }
                if (isBlank == true) {
                    pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                    pst.setInt(1, pf.getHidparid());
                    pst.setInt(2, pf.getMaxhierarchyno());
                    pst.executeUpdate();

                } else if (isBlank == false) {
                    if (invalidperiod.equals("N")) {
                        pst = con.prepareStatement("UPDATE PAR_MASTER SET IS_AUTH_SET='Y' WHERE PARID=?");
                        pst.setInt(1, pf.getHidparid());
                        pst.executeUpdate();

                        startWorkFlow(con, pf.getHidparid(), pf.getHidtaskid());

                    } else {
                        pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                        pst.setInt(1, pf.getHidparid());
                        pst.setInt(2, pf.getMaxhierarchyno());
                        pst.executeUpdate();

                    }
                }
            }
            String sql = "SELECT * FROM PAR_MASTER WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pf.getHidparid());
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("PAR_STATUS") > 0) {
                    //sendSMStoAuthafterSubmit(con, pf.getHidparid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return invalidperiod;
    }

    private static void startWorkFlow(Connection con, int parId, int taskId) {

        int mcode;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        String isReportingcomplted = "Y";
        String isReviewingcomplted = "N";
        String isAcceptingcomplted = "N";
        String initiatedEmpId = "";
        String initiatedSPC = "";
        String parType = "";
        int reftableid = 0;
        String periodFromDate = "";
        String periodToDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            /*Get Reporting Auth List*/
            pst = con.prepareStatement("SELECT PRPTID,REPORTING_EMP_ID,REPORTING_CUR_SPC FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO");
            pst.setInt(1, parId);
            rs = pst.executeQuery();
            String authEmpId = "";
            String authCurSpc = "";
            if (rs.next()) {
                authEmpId = rs.getString("REPORTING_EMP_ID");
                authCurSpc = rs.getString("REPORTING_CUR_SPC");
                reftableid = rs.getInt("PRPTID");
                isReportingcomplted = "N";
            } else {
                isReportingcomplted = "Y";
            }

            System.out.println("isReportingcomplted ___________" + isReportingcomplted);
            /*Get Reporting Auth List*/
            /*Check Reporting Auth completed or Not*/
            if (isReportingcomplted.equalsIgnoreCase("N")) {
                System.out.println("inside isReportingcomplted ------------------------N");
                pst = con.prepareStatement("SELECT EMP_ID,SPC,par_type FROM PAR_MASTER WHERE PARID=?");
                pst.setInt(1, parId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    initiatedEmpId = rs.getString("EMP_ID");
                    initiatedSPC = rs.getString("SPC");
                    parType = rs.getString("par_type");
                }

                if (taskId == 0) {
                    System.out.println("while task_id = 0 ____________________________");
                    pst1 = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC,task_par_type) Values (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                    pst1.setInt(1, 3);
                    pst1.setString(2, initiatedEmpId);
                    pst1.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst1.setInt(4, 6);//SUBMITTED TO REPORTING AUTHORITY
                    pst1.setString(5, authEmpId);
                    pst1.setString(6, authEmpId);
                    pst1.setString(7, initiatedSPC);
                    pst1.setString(8, authCurSpc);
                    if (parType != null && !parType.equals("") && parType.equals("SiPar")) {
                        pst1.setString(9, "SIPAR");
                    } else {
                        pst1.setString(9, "");
                    }
                    pst1.executeUpdate();
                    rs = pst1.getGeneratedKeys();
                    rs.next();
                    int vtaskId = rs.getInt("TASK_ID");
                    DataBaseFunctions.closeSqlObjects(rs, pst1);

                    pst1 = con.prepareStatement("UPDATE PAR_MASTER SET TASK_ID=?,PAR_STATUS=6,REF_ID_OF_TABLE=? WHERE PARID=?");
                    pst1.setInt(1, vtaskId);
                    pst1.setInt(2, reftableid);
                    pst1.setInt(3, parId);
                    pst1.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pst1);
                } else if (taskId > 0) {
                    System.out.println("while task_id > 0 _____________!!!!!!!!!!!!!!!");
                    pst1 = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,APPLY_TO=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                    pst1.setString(1, authEmpId);
                    pst1.setString(2, authEmpId);
                    pst1.setString(3, authCurSpc);
                    pst1.setInt(4, 6);
                    pst1.setInt(5, taskId);
                    pst1.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pst1);

                    pst1 = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                    pst1.setInt(1, 6);
                    pst1.setInt(2, reftableid);
                    pst1.setInt(3, parId);
                    pst1.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pst1);
                }
            }
            /*Get Reviewing Auth List*/
            if (isReportingcomplted.equalsIgnoreCase("Y") && isReviewingcomplted.equalsIgnoreCase("N") && ((isAcceptingcomplted.equalsIgnoreCase("N") || isAcceptingcomplted.equalsIgnoreCase("F") || !isAcceptingcomplted.equalsIgnoreCase("Y")))) {
                boolean isReviewingAuthFound = false;
                pst = con.prepareStatement("SELECT PRVTID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,fromdate,todate FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO");
                pst.setInt(1, parId);
                rs = pst.executeQuery();

                while (rs.next()) {
                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");

                    if (isReviewingAuthFound == false) {
                        int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                        diffInDays = diffInDays + 1;
                        if (rs.getString("REVIEWING_EMP_ID").equals("00000000")) {
                            isReviewingcomplted = "Y";
                            isReviewingAuthFound = false;
                            reftableid = rs.getInt("PRVTID");

                            /*FORCE FORWARDED DUE TO NOT AVAILABILITY OF AUTHORITY*/
                            pst1 = con.prepareStatement("UPDATE par_reviewing_tran SET is_completed='T',SUBMITTED_ON=? WHERE PAR_ID=? and PRVTID=?");
                            pst1.setTimestamp(1, new Timestamp(curtime));
                            pst1.setInt(2, parId);
                            pst1.setInt(3, reftableid);
                            pst1.executeUpdate();
                        } else if (diffInDays >= 120) {
                            authEmpId = rs.getString("REVIEWING_EMP_ID");
                            authCurSpc = rs.getString("REVIEWING_CUR_SPC");
                            reftableid = rs.getInt("PRVTID");
                            isReviewingcomplted = "N";
                            isReviewingAuthFound = true;
                        } else if (diffInDays <= 120) {
                            isReviewingcomplted = "Y";
                            isReviewingAuthFound = false;
                        }
                    }
                }
                if (isReviewingAuthFound == false && isReviewingcomplted.equals("Y")) {
                    isReviewingcomplted = "Y";
                } else if (isReviewingAuthFound == true && isReviewingcomplted.equals("N")) {
                    isReviewingcomplted = "N";
                } else if (isReviewingAuthFound == false && isReviewingcomplted.equals("N")) {
                    isReviewingcomplted = "Y";
                }

                /* if (isReviewingAuthFound == false) {
                 isReviewingcomplted = "Y";
                 } */
                if (isReviewingcomplted.equalsIgnoreCase("N") && isAcceptingcomplted.equalsIgnoreCase("N")) {
                    pst1 = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                    pst1.setInt(1, 7);//SUBMITTED TO REVIEWING AUTHORITY
                    pst1.setString(2, authEmpId);
                    pst1.setString(3, authEmpId);
                    pst1.setString(4, authCurSpc);
                    pst1.setInt(5, taskId);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS='7',REF_ID_OF_TABLE=? WHERE PARID=?");
                    pst1.setInt(1, reftableid);
                    pst1.setInt(2, parId);
                    pst1.executeUpdate();

                }

            }
            if (isReportingcomplted.equalsIgnoreCase("Y") && isReviewingcomplted.equalsIgnoreCase("Y")) {
                boolean isAcceptingAuthFound = false;
                pst = con.prepareStatement("SELECT PACTID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,fromdate,todate FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO");
                pst.setInt(1, parId);
                rs = pst.executeQuery();

                while (rs.next()) {
                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");

                    if (isAcceptingAuthFound == false) {
                        int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                        diffInDays = diffInDays + 1;
                        if (rs.getString("ACCEPTING_EMP_ID").equals("00000000")) {
                            isAcceptingcomplted = "Y";
                            isAcceptingAuthFound = false;
                            reftableid = rs.getInt("PACTID");

                            /*FORCE FORWARDED DUE TO NOT AVAILABILITY OF AUTHORITY*/
                            pst1 = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET is_completed='T',SUBMITTED_ON=? WHERE PAR_ID=? and PACTID=?");
                            pst1.setTimestamp(1, new Timestamp(curtime));
                            pst1.setInt(2, parId);
                            pst1.setInt(3, reftableid);
                            pst1.executeUpdate();
                        } else if (diffInDays >= 120) {
                            authEmpId = rs.getString("ACCEPTING_EMP_ID");
                            authCurSpc = rs.getString("ACCEPTING_CUR_SPC");
                            reftableid = rs.getInt("PACTID");
                            isAcceptingcomplted = "N";
                            isAcceptingAuthFound = true;

                        } else if (diffInDays <= 120) {
                            isAcceptingcomplted = "Y";
                            isAcceptingAuthFound = false;
                        }

                    }
                }

                if (isAcceptingAuthFound == false && isAcceptingcomplted.equals("Y")) {
                    isAcceptingcomplted = "Y";
                } else if (isAcceptingAuthFound == true && isAcceptingcomplted.equals("N")) {
                    isAcceptingcomplted = "N";
                } else if (isAcceptingAuthFound == false && isAcceptingcomplted.equals("N")) {
                    isAcceptingcomplted = "Y";
                }

                if (isAcceptingcomplted.equalsIgnoreCase("N")) {
                    pst1 = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                    pst1.setInt(1, 8);//SUBMITTED TO ACCEPTING AUTHORITY
                    pst1.setString(2, authEmpId);
                    pst1.setString(3, authEmpId);
                    pst1.setString(4, authCurSpc);
                    pst1.setInt(5, taskId);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS='8',REF_ID_OF_TABLE=? WHERE PARID=?");
                    pst1.setInt(1, reftableid);
                    pst1.setInt(2, parId);
                    pst1.executeUpdate();

                }

            }
            /*String isReviewingCompletedPAR = "";
             String isaCCEPTINGCompletedPAR = "";

             if (isReviewingcomplted.equalsIgnoreCase("N")) {
             System.out.println("isReviewingCompletedPAR 1111111111111");    
             isReviewingCompletedPAR = "N";
             } else if (isReviewingcomplted.equalsIgnoreCase("Y") || isReviewingcomplted.equalsIgnoreCase("F")) {
             System.out.println("isReviewingCompletedPAR 2222222222");  
             isReviewingCompletedPAR = "Y";
             } 
             System.out.println("-------------------------------------------------------------------------");
             if (isAcceptingcomplted.equalsIgnoreCase("N")) {
             System.out.println("isReviewingCompletedPAR 3333333333333");  
             isaCCEPTINGCompletedPAR = "N";
             } else if (isAcceptingcomplted.equalsIgnoreCase("Y") || isAcceptingcomplted.equalsIgnoreCase("F")) {
             System.out.println("isReviewingCompletedPAR 444444444444");  
             isaCCEPTINGCompletedPAR = "Y";
             } */

            //System.out.println("isReviewingCompletedPAR is ----" + isReviewingCompletedPAR + "isaCCEPTINGCompletedPAR is -----" + isaCCEPTINGCompletedPAR);
            /*Get Reviewing Auth List*/
            if (isReportingcomplted.equalsIgnoreCase("Y") && isReviewingcomplted.equalsIgnoreCase("Y") && isAcceptingcomplted.equalsIgnoreCase("Y")) {
                pst1 = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst1.setInt(1, 9);//Completed
                pst1.setString(2, null);
                pst1.setString(3, null);
                pst1.setString(4, null);
                pst1.setInt(5, taskId);
                pst1.executeUpdate();

                pst1 = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS='9',REF_ID_OF_TABLE=null  WHERE PARID=?");
                pst1.setInt(1, parId);
                pst1.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            //DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getmaxhierachy(int parId, int parstatus) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int hierarchyno = 0;

        try {
            con = dataSource.getConnection();

            if (parstatus > 0 && parstatus == 18) {
                String sql = "SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    if (rs.getString("MAXH") != null) {
                        hierarchyno = rs.getInt("MAXH") + 1;
                    } else {
                        hierarchyno = 1;
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);
            } else if (parstatus > 0 && parstatus == 19) {
                String sql = "SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    if (rs.getString("MAXH") != null) {
                        hierarchyno = rs.getInt("MAXH") + 1;
                    } else {
                        hierarchyno = 1;
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);
            } else if (parstatus == 0 || parstatus == 16) {
                String sql = "SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_REPORTING_TRAN WHERE PAR_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("MAXH") != null) {
                        hierarchyno = rs.getInt("MAXH") + 1;
                    } else {
                        hierarchyno = 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hierarchyno;
    }

    @Override
    public ParAbsenteeBean getAbsenteeInfo(String empid, int pabid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParAbsenteeBean pabs = new ParAbsenteeBean();
        try {
            con = this.repodataSource.getConnection();

            //String sql = "SELECT * FROM PAR_ABSENTEE WHERE PABID='" + pabid + "'";
            String sql = "SELECT * FROM PAR_ABSENTEE"
                    + " INNER JOIN PAR_MASTER ON PAR_ABSENTEE.PARID=PAR_MASTER.PARID WHERE PABID=? AND PAR_MASTER.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pabid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                pabs.setHidpabid(pabid);
                pabs.setHidparid(rs.getInt("PARID"));
                pabs.setFromdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROMDATE")));
                pabs.setTodate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TODATE")));
                pabs.setLeavecause(rs.getString("LEAVEPURPOSE"));
                pabs.setLeaveortrainingtype(rs.getString("TYPEOFLEAVE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pabs;
    }

    @Override
    public ParAchievement getAchievementInfo(String empid, int pacid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParAchievement pach = null;
        String achievement = "";
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "SELECT ORIGINAL_FILENAME,PAR_ACHIEVEMENT.* FROM (SELECT * FROM PAR_ACHIEVEMENT WHERE PACID='" + pacid + "')PAR_ACHIEVEMENT"
             + " LEFT OUTER JOIN HW_ATTACHMENTS ON PAR_ACHIEVEMENT.ATT_ID=HW_ATTACHMENTS.ATT_ID";*/
            String sql = "SELECT ORIGINAL_FILENAME,PAR_ACHIEVEMENT.* FROM (SELECT * FROM PAR_ACHIEVEMENT WHERE PACID=?)PAR_ACHIEVEMENT"
                    + " INNER JOIN PAR_MASTER ON PAR_ACHIEVEMENT.PARID=PAR_MASTER.PARID"
                    + " LEFT OUTER JOIN HW_ATTACHMENTS ON PAR_ACHIEVEMENT.ATT_ID=HW_ATTACHMENTS.ATT_ID"
                    + " WHERE PAR_MASTER.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pacid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                pach = new ParAchievement();
                pach.setHidattchfilename(rs.getString("ORIGINAL_FILENAME"));
                pach.setHidattchid(rs.getInt("ATT_ID"));
                pach.setPacid(pacid);
                //pach.setTxtEmpId(empid);
                pach.setHidparid(rs.getInt("PARID"));
                pach.setSlno(rs.getInt("SLNO"));
                pach.setTask(rs.getString("THETASK"));
                pach.setTarget(rs.getString("TARGET"));
                pach.setAchievementpercent(rs.getString("ACHIEVEMENTPERCENT"));
                pach.setAchievementpersqualitative(rs.getString("ACHIEVEPERCQUALITATIVE"));
                achievement = StringUtils.defaultString(rs.getString("ACHIEVEMENT"));
                achievement = StringUtils.replace(achievement, "''", "'");
                pach.setAchievement(achievement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pach;
    }

    @Override
    public void deleteAbsentee(String empid, int pabid) {

        Connection con = null;

        PreparedStatement pst = null;
        try {

            con = dataSource.getConnection();
            String sql = "DELETE FROM PAR_ABSENTEE WHERE PABID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pabid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteAchievement(String empid, int pacid, String fiscalyear, String filepath) {

        Connection con = null;

        ResultSet rs = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;

        ParAchievement pach = null;
        HWAttachements hw = null;
        int attid = 0;
        try {

            con = dataSource.getConnection();

            String sql = "SELECT HW_ATTACHMENTS.* FROM (SELECT * FROM HW_ATTACHMENTS)HW_ATTACHMENTS INNER JOIN (SELECT * FROM PAR_ACHIEVEMENT WHERE PACID=?)PAR_ACHIEVEMENT ON HW_ATTACHMENTS.ATT_ID=PAR_ACHIEVEMENT.ATT_ID INNER JOIN PAR_MASTER ON PAR_ACHIEVEMENT.PARID=PAR_MASTER.PARID WHERE PAR_MASTER.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, pacid);
            pst.setString(2, empid);
            rs = pst.executeQuery();

            if (rs.next()) {
                attid = rs.getInt("ATT_ID");
                if (attid > 0) {
                    File f = null;
                    String dirpath = filepath + fiscalyear + "/" + rs.getString("DISK_FILE_NAME");
                    f = new File(dirpath);
                    if (f.exists()) {
                        f.delete();
                    }
                    pst1 = con.prepareStatement("DELETE FROM HW_ATTACHMENTS WHERE ATT_ID=?");
                    pst1.setInt(1, attid);
                    pst1.executeUpdate();

                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("DELETE FROM PAR_ACHIEVEMENT WHERE PACID=?");
            pst.setInt(1, pacid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int deleteAchievementAttachment(String empid, int pacid, int attid, String fiscalyear, String filepath) {

        Connection con = null;

        ResultSet rs = null;

        PreparedStatement pst = null;

        int retval1 = 0;
        int retval2 = 0;
        boolean isDeleted = false;
        try {
            con = dataSource.getConnection();

            //String sql = "SELECT * FROM HW_ATTACHMENTS WHERE ATT_ID=?";
            String sql = "SELECT * FROM HW_ATTACHMENTS"
                    + " INNER JOIN PAR_ACHIEVEMENT ON HW_ATTACHMENTS.ATT_ID=PAR_ACHIEVEMENT.ATT_ID"
                    + " INNER JOIN PAR_MASTER ON PAR_ACHIEVEMENT.PARID=PAR_MASTER.PARID WHERE HW_ATTACHMENTS.ATT_ID=? AND PAR_MASTER.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attid);
            pst.setString(2, empid);
            rs = pst.executeQuery();

            if (rs.next()) {
                File f = null;
                String dirpath = filepath + fiscalyear + "/" + rs.getString("DISK_FILE_NAME");
                f = new File(dirpath);
                if (f.exists()) {
                    System.out.println("11111-----------");
                    isDeleted = f.delete();
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (isDeleted) {
                pst = con.prepareStatement("DELETE FROM HW_ATTACHMENTS WHERE ATT_ID=?");
                pst.setInt(1, attid);
                retval1 = pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);
                System.out.println("222222222-------------");
                if (retval1 > 0) {
                    System.out.println("333333333333__________");
                    pst = con.prepareStatement("UPDATE PAR_ACHIEVEMENT SET ATT_ID=? WHERE PACID=?");
                    pst.setInt(1, 0);
                    pst.setInt(2, pacid);
                    retval2 = pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retval2;
    }

    @Override
    public List getTrainingypeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List trainingtypelist = new ArrayList();

        Training trn = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT TRAINING_TYPE_ID,TRAINING_TYPE FROM G_TRAINING_TYPE ORDER BY TRAINING_TYPE ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                trn = new Training();
                trn.setTrainingtypeid(rs.getString("TRAINING_TYPE_ID"));
                trn.setTrainingtype(rs.getString("TRAINING_TYPE"));
                trainingtypelist.add(trn);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingtypelist;
    }

    @Override
    public boolean isDuplicatePARPeriod(String empId, String parfrmdt, String partodt, String fiscalyear, String parId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean duplPeriod = false;
        boolean status = true;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String sql = "";
        String dbf1 = "";
        String dbt1 = "";
        try {
            Date fd1 = formatter.parse(parfrmdt);
            Date td1 = formatter.parse(partodt);
            con = dataSource.getConnection();

            if (parId != null && !parId.equals("")) {
                sql = "SELECT PERIOD_FROM,PERIOD_TO FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? AND PARID <> ? AND IS_DELETED ='N'";
                pst = con.prepareStatement(sql);
                pst.setString(1, empId);
                pst.setString(2, fiscalyear);
                pst.setInt(3, Integer.parseInt(parId));
            } else {
                sql = "SELECT PERIOD_FROM,PERIOD_TO FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? AND IS_DELETED ='N'";
                pst = con.prepareStatement(sql);
                pst.setString(1, empId);
                pst.setString(2, fiscalyear);
            }
            rs = pst.executeQuery();
            while (rs.next()) {

                dbf1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM"));
                dbt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO"));

                Date frs1 = formatter.parse(dbf1);
                Date trs1 = formatter.parse(dbt1);

                if (fd1.compareTo(frs1) > 0 && (fd1.compareTo(trs1) > 0 && td1.compareTo(trs1) > 0)) {
                    duplPeriod = true;

                } else if ((fd1.compareTo(frs1) < 0) && (td1.compareTo(frs1) < 0 && td1.compareTo(trs1) < 0)) {
                    duplPeriod = true;

                } else {
                    duplPeriod = false;

                }

                if (duplPeriod == false) {
                    status = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public boolean isDuplicatePARPeriodForInitiateOtherPAR(String empId, String parfrmdt, String partodt, String fiscalyear, String parId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean duplPeriod = false;
        boolean status = true;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String sql = "";
        String dbf1 = "";
        String dbt1 = "";
        try {
            Date fd1 = formatter.parse(parfrmdt);
            Date td1 = formatter.parse(partodt);
            con = dataSource.getConnection();

            if (parId != null && !parId.equals("")) {
                sql = "SELECT PERIOD_FROM,PERIOD_TO FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? AND PARID <> ? AND IS_DELETED ='N' AND PAR_STATUS !=0";
                pst = con.prepareStatement(sql);
                pst.setString(1, empId);
                pst.setString(2, fiscalyear);
                pst.setInt(3, Integer.parseInt(parId));
            } else {
                sql = "SELECT PERIOD_FROM,PERIOD_TO FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=? AND IS_DELETED ='N'";
                pst = con.prepareStatement(sql);
                pst.setString(1, empId);
                pst.setString(2, fiscalyear);
            }
            rs = pst.executeQuery();
            while (rs.next()) {

                dbf1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM"));
                dbt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO"));

                Date frs1 = formatter.parse(dbf1);
                Date trs1 = formatter.parse(dbt1);

                if (fd1.compareTo(frs1) > 0 && (fd1.compareTo(trs1) > 0 && td1.compareTo(trs1) > 0)) {
                    duplPeriod = true;

                } else if ((fd1.compareTo(frs1) < 0) && (td1.compareTo(frs1) < 0 && td1.compareTo(trs1) < 0)) {
                    duplPeriod = true;

                } else {
                    duplPeriod = false;

                }

                if (duplPeriod == false) {
                    status = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public ParDetail getPARDetails(String empid, int parId, int taskId, String authType) {

        ParDetail paf = new ParDetail();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        String appraisalsummary = "";
        String specialcontribution = "";
        String factors = "";
        String parDataQry = "";

        try {
            con = this.repodataSource.getConnection();
            if (taskId == 0) {
                parDataQry = "SELECT auth_remarks_closed,INITIATED_ON,HEADQUARTER,PAR1.par_type, PAR1.si_officer_type,PAR1.place_of_posting,PAR1.CADRE_CODE,PAR1.EMP_ID,"
                        + "FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,"
                        + "PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,HINDERREASON,fivet_component_appraise,SUBMITTED_ON,PAR_APPRAISEE_TRAN.PLACE,"
                        + "DOB,INITIALS,F_NAME,M_NAME,L_NAME,G_CADRE.CADRE_NAME,PAR1.POST_GROUP FROM "
                        + "(SELECT HEADQUARTER,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,"
                        + "GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,par_type,si_officer_type,place_of_posting FROM PAR_MASTER WHERE PARID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE "
                        + " LEFT OUTER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID"
                        + " LEFT OUTER JOIN FINANCIAL_YEAR ON PAR1.FISCAL_YEAR=FINANCIAL_YEAR.FY";
                pstmt = con.prepareStatement(parDataQry);
                pstmt.setInt(1, parId);
            } else {
                parDataQry = "SELECT auth_remarks_closed,INITIATED_ON,HEADQUARTER,PAR1.par_type,PAR1.si_officer_type,PAR1.place_of_posting,PAR1.CADRE_CODE,"
                        + "PAR1.EMP_ID,FISCAL_YEAR,PAR1.OFF_CODE,PAR1.PARID,PAR_STATUS,"
                        + "REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,APPRISE_APC,APPRISE_SPN,APPRISE_OFFICE,PAR1.TASK_ID,SPECIALCONTRIBUTIION,SELFAPPRAISAL,"
                        + "HINDERREASON,fivet_component_appraise,SUBMITTED_ON,PAR_APPRAISEE_TRAN.PLACE,DOB,INITIALS,F_NAME,M_NAME,L_NAME,"
                        + "G_CADRE.CADRE_NAME,PAR1.POST_GROUP FROM "
                        + "(SELECT HEADQUARTER,CADRE_CODE,EMP_ID,FISCAL_YEAR,OFF_CODE,PARID,PAR_STATUS,REF_ID_OF_TABLE,PERIOD_FROM,PERIOD_TO,SPC APPRISE_APC,"
                        + "GETSPN(SPC) APPRISE_SPN,GETOFFNAMEFROMSPC(SPC) APPRISE_OFFICE,TASK_ID,POST_GROUP,par_type, si_officer_type,place_of_posting "
                        + " FROM PAR_MASTER WHERE TASK_ID=?)PAR1 "
                        + "LEFT OUTER JOIN PAR_APPRAISEE_TRAN ON PAR1.PARID = PAR_APPRAISEE_TRAN.PARID "
                        + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = PAR1.EMP_ID "
                        + " LEFT OUTER JOIN G_CADRE ON PAR1.CADRE_CODE=G_CADRE.CADRE_CODE"
                        + " INNER JOIN TASK_MASTER ON PAR1.TASK_ID=TASK_MASTER.TASK_ID"
                        + " LEFT OUTER JOIN FINANCIAL_YEAR ON PAR1.FISCAL_YEAR=FINANCIAL_YEAR.FY";
                pstmt = con.prepareStatement(parDataQry);
                pstmt.setInt(1, taskId);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Date parSubmitDateAppraisee = (new SimpleDateFormat("yyyy-MM-dd").parse("2023-04-01"));

                appraisalsummary = StringUtils.defaultString(rs.getString("SELFAPPRAISAL"));
                specialcontribution = StringUtils.defaultString(rs.getString("SPECIALCONTRIBUTIION"));
                factors = StringUtils.defaultString(rs.getString("HINDERREASON"));

                /*HtmlToPlainText htp = new HtmlToPlainText();
                 appraisalsummary = htp.getPlainText(Jsoup.parse(appraisalsummary));
                 specialcontribution = htp.getPlainText(Jsoup.parse(specialcontribution));
                 factors = htp.getPlainText(Jsoup.parse(factors));*/
                paf.setFiveTComponentappraise(rs.getString("fivet_component_appraise"));
                paf.setParid(rs.getInt("PARID"));
                paf.setParstatus(rs.getInt("PAR_STATUS"));
                paf.setApprisespc(rs.getString("APPRISE_APC"));
                paf.setRefid(rs.getInt("REF_ID_OF_TABLE"));
                paf.setTaskid(rs.getInt("TASK_ID"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setIsClosedFiscalYearAuthority(getIsActiveForAuthority(con, rs.getString("FISCAL_YEAR")));
                paf.setParPeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setParPeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setApplicantempid(rs.getString("EMP_ID"));
                paf.setApplicant(StringUtils.defaultString(rs.getString("INITIALS")) + " " + StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME")));
                paf.setApprisespn(rs.getString("APPRISE_SPN"));
                paf.setEmpOffice(rs.getString("APPRISE_OFFICE"));
                paf.setSpecialcontribution(specialcontribution);
                paf.setSelfappraisal(appraisalsummary);
                paf.setReason(factors);
                if (rs.getString("INITIATED_ON") != null && !rs.getString("INITIATED_ON").equals("")) {
                    paf.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                } else {
                    paf.setSubmitted_on("");
                }
                /* Written while all year PAR is Open with 2023-24
                 if (rs.getString("FISCAL_YEAR") != null && !rs.getString("FISCAL_YEAR").equals("") && !rs.getString("FISCAL_YEAR").equals("2023-24")) {
                 if (rs.getDate("INITIATED_ON") != null && !rs.getDate("INITIATED_ON").equals("") && rs.getDate("INITIATED_ON").after(parSubmitDateAppraisee)) {
                 paf.setIsAuthorityAbleToGiveRemarks("Yes");
                 } else {
                 paf.setIsAuthorityAbleToGiveRemarks("No");
                 }
                 } else if(rs.getString("FISCAL_YEAR").equals("2023-24")){
                 paf.setIsAuthorityAbleToGiveRemarks("Yes");
                 }*/
                if (rs.getString("auth_remarks_closed") != null && !rs.getString("auth_remarks_closed").equals("") && rs.getString("auth_remarks_closed").equals("N")) {
                    System.out.println("111111111111");
                    if (rs.getDate("INITIATED_ON") != null && !rs.getDate("INITIATED_ON").equals("") && rs.getDate("INITIATED_ON").after(parSubmitDateAppraisee)) {
                        paf.setIsAuthorityAbleToGiveRemarks("Yes");
                    } else {
                        paf.setIsAuthorityAbleToGiveRemarks("No");
                    }
                } else {
                    System.out.println("2222222222222");
                    paf.setIsAuthorityAbleToGiveRemarks("No");
                }

                paf.setPlace(rs.getString("PLACE"));
                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                paf.setEmpGroup(rs.getString("POST_GROUP"));
                paf.setSltHeadQuarter(rs.getString("HEADQUARTER"));

                if (rs.getString("par_type") == null || rs.getString("par_type").equals("")) {
                    paf.setParType("");
                    paf.setSiType("");
                    paf.setPlaceOfPostingSi("");
                } else {
                    paf.setParType(rs.getString("par_type"));
                    switch (rs.getString("si_officer_type")) {
                        case "Si1":
                            paf.setSiType("Sub Inspector (Civil)");
                            break;
                        case "Si2":
                            paf.setSiType("Sub Inspector (Armed)");
                            break;
                        case "Si3":
                            paf.setSiType("Sub Inspector (Equivalent)");
                            break;
                    }
                    paf.setPlaceOfPostingSi(rs.getString("place_of_posting"));
//                    if(rs.getString("total_a")== null || rs.getString("total_a").equals("")){
//                        paf.setTotalofA(0);
//                        paf.setTotalofB(0);
//                        paf.setTotalofC(0);
//                        paf.setTotalofD(0);                   
//                    }else{
//                        paf.setTotalofA(rs.getInt("total_a"));
//                        paf.setTotalofB(rs.getInt("total_b"));
//                        paf.setTotalofC(rs.getInt("total_c"));
//                        paf.setTotalofD(rs.getInt("total_d"));
//                    }
                }

                paf.setLeaveAbsentee(getAbsenteeListforView(con, rs.getString("PARID")));
                paf.setAchivementList(getAchievementListforView(con, rs.getString("PARID")));
                paf.setReportingauth(getParAuthority(con, "REPORTING", paf.getParid(), paf.getTaskid()));
                paf.setReviewingauth(getParAuthority(con, "REVIEWING", paf.getParid(), paf.getTaskid()));
                paf.setAcceptingauth(getParAuthority(con, "ACCEPTING", paf.getParid(), paf.getTaskid()));
                if (authType != null && !authType.equals("")) {
                    paf.setParstatus(9);
                    if (authType.equals("REPORTING")) {
                        paf.setReportingdata(getReportingData(con, paf));
                    } else if (authType.equals("REVIEWING")) {
                        paf.setReportingdata(getReportingData(con, paf));
                        paf.setReviewingdata(getReviewingData(con, paf));
                    } else if (authType.equals("ACCEPTING")) {
                        paf.setReportingdata(getReportingData(con, paf));
                        paf.setReviewingdata(getReviewingData(con, paf));
                        paf.setAcceptingdata(getAcceptingData(con, paf));
                    }
                } else {
                    paf.setReportingdata(getReportingData(con, paf));
                    paf.setReviewingdata(getReviewingData(con, paf));
                    paf.setAcceptingdata(getAcceptingData(con, paf));
                }

                if (paf.getParstatus() == 7) {
                    paf.setIsreportingcompleted("Y");
                } else if (paf.getParstatus() == 8) {
                    paf.setIsreportingcompleted("Y");
                    paf.setIsreviewingcompleted("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    public String getIsActiveForAuthority(Connection con, String fiscalyear) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isClosed = "N";
        try {
            String sql = "SELECT AUTH_REMARKS_CLOSED FROM FINANCIAL_YEAR WHERE FY=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fiscalyear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("AUTH_REMARKS_CLOSED") != null && !rs.getString("AUTH_REMARKS_CLOSED").equals("")) {
                    isClosed = rs.getString("AUTH_REMARKS_CLOSED");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return isClosed;
    }

    public static ArrayList getAbsenteeListforView(Connection con, String parId) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList absenteeList = new ArrayList();
        AbsenteeBean pab = null;
        try {
            pst = con.prepareStatement("SELECT * FROM PAR_ABSENTEE WHERE PARID=?");
            pst.setInt(1, Integer.parseInt(parId));
            rs = pst.executeQuery();
            while (rs.next()) {
                pab = new AbsenteeBean();
                pab.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROMDATE")));
                pab.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TODATE")));
                pab.setAbsenceCause(rs.getString("LEAVEPURPOSE"));
                pab.setLeaveType(getLeaveTrainingName(con, rs.getString("LEAVEPURPOSE"), rs.getString("TYPEOFLEAVE")));
                absenteeList.add(pab);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return absenteeList;
    }

    public static ArrayList getAchievementListforView(Connection con, String parId) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList achievementList = new ArrayList();
        AchievementBean ab = null;

        String achievement = "";
        try {

            //rs = st.executeQuery("SELECT * FROM PAR_ACHIEVEMENT WHERE PARID='" + parId + "' ORDER BY SLNO");
            String sql = "SELECT PAR_ACHIEVEMENT.*,HW_ATTACHMENTS.ORIGINAL_FILENAME FROM (SELECT * FROM PAR_ACHIEVEMENT WHERE PARID=?"
                    + " ORDER BY SLNO)PAR_ACHIEVEMENT LEFT OUTER JOIN HW_ATTACHMENTS ON PAR_ACHIEVEMENT.ATT_ID="
                    + " HW_ATTACHMENTS.ATT_ID  ORDER BY SLNO ASC";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parId));
            rs = pst.executeQuery();
            while (rs.next()) {
                ab = new AchievementBean();
                ab.setSlno(rs.getString("SLNO"));
                ab.setTask(rs.getString("THETASK"));
                ab.setTarget(rs.getString("TARGET"));
                achievement = StringUtils.defaultString(rs.getString("ACHIEVEMENT"));
                achievement = StringUtils.replace(achievement, "''", "'");
                ab.setAchievement(achievement);
                ab.setPercentQualitative(rs.getString("ACHIEVEPERCQUALITATIVE"));
                ab.setPercentAchievement(rs.getString("ACHIEVEMENTPERCENT"));
                ab.setAttachmentname(rs.getString("ORIGINAL_FILENAME"));
                ab.setAttachmentId(rs.getString("ATT_ID"));
                achievementList.add(ab);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return achievementList;
    }

    public ArrayList getParAuthority(Connection con, String authtype, int parId, int taskId) throws Exception {
        ArrayList al = new ArrayList();

        PreparedStatement pstamt = null;
        PreparedStatement pstamtReviewing = null;
        PreparedStatement pstamtReporting = null;
        PreparedStatement pstamtAccepting = null;
        ResultSet resultset = null;
        ResultSet innerresult = null;
        try {
            if (authtype.equalsIgnoreCase("REPORTING")) {
                String spctype = "G";
                pstamt = con.prepareStatement("SELECT PRPTID,REPORTING_CUR_SPC FROM PAR_REPORTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    if (resultset.getString("REPORTING_CUR_SPC") != null && !resultset.getString("REPORTING_CUR_SPC").equals("")) {
                        if (resultset.getString("REPORTING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }
                    if (spctype.equalsIgnoreCase("G")) {
                        pstamtReporting = con.prepareStatement("SELECT REPORTING_EMP_ID,REPORTING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM(SELECT GETSPN(REPORTING_CUR_SPC)REPORTING_SPN,REPORTING_EMP_ID,FROMDATE,TODATE FROM PAR_REPORTING_TRAN WHERE PRPTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "INNER JOIN EMP_MAST ON T1.REPORTING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtReporting.setInt(1, resultset.getInt("PRPTID"));
                        innerresult = pstamtReporting.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            parhelper.setAuthorityspn(innerresult.getString("REPORTING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingReportingAuthority(isPendingReportingAuthority(con, innerresult.getString("REPORTING_EMP_ID"), taskId));

                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        pstamtReporting = con.prepareStatement("SELECT LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT REPORTING_CUR_SPC,REPORTING_EMP_ID,FROMDATE,TODATE FROM PAR_REPORTING_TRAN WHERE PRPTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.REPORTING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtReporting.setInt(1, resultset.getInt("PRPTID"));
                        innerresult = pstamtReporting.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            String deptNames = getLAMemberDeptName(con, innerresult.getInt("LMID"));
                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));
                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME") + "," + deptNames);
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingReportingAuthority(isPendingReportingAuthority(con, innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            } else if (authtype.equalsIgnoreCase("REVIEWING")) {
                String spctype = "G";
                pstamt = con.prepareStatement("SELECT PRVTID,REVIEWING_CUR_SPC FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    System.out.println("resultset.getString(\"REVIEWING_CUR_SPC\") -----" + resultset.getString("REVIEWING_CUR_SPC"));
                    if (resultset.getString("REVIEWING_CUR_SPC") != null && !resultset.getString("REVIEWING_CUR_SPC").equals("")) {
                        if (resultset.getString("REVIEWING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }

                    System.out.println("spctype ------" + spctype);
                    if (spctype.equalsIgnoreCase("G")) {
                        /*pstamt = con.prepareStatement("SELECT REVIEWING_EMP_ID,REVIEWING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM (SELECT GETSPN(REVIEWING_CUR_SPC)REVIEWING_SPN,REVIEWING_EMP_ID,FROMDATE,TODATE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO)T1 "
                         + "INNER JOIN EMP_MAST ON T1.REVIEWING_EMP_ID = EMP_MAST.EMP_ID");*/
                        pstamtReviewing = con.prepareStatement("SELECT REVIEWING_EMP_ID,REVIEWING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM (SELECT GETSPN(REVIEWING_CUR_SPC)REVIEWING_SPN,REVIEWING_EMP_ID,FROMDATE,TODATE FROM PAR_REVIEWING_TRAN WHERE PRVTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "LEFT OUTER JOIN EMP_MAST ON T1.REVIEWING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtReviewing.setInt(1, resultset.getInt("PRVTID"));
                        //resultset = pstamt.executeQuery();
                        innerresult = pstamtReviewing.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            System.out.println("resultset.getString(\"REVIEWING_EMP_ID\") ----" + innerresult.getString("REVIEWING_EMP_ID"));
                            if (innerresult.getString("REVIEWING_EMP_ID") != null && innerresult.getString("REVIEWING_EMP_ID").equals("00000000")) {
                                parhelper.setAuthorityname("NOT AVAILABLE");
                            } else {
                                parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            }
                            parhelper.setAuthorityspn(innerresult.getString("REVIEWING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingReviewingAuthority(isPendingReviewingAuthority(con, innerresult.getString("REVIEWING_EMP_ID"), taskId));
                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        System.out.println("Inside spc type m-------------");
                        pstamtReviewing = con.prepareStatement("SELECT LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT REVIEWING_CUR_SPC,REVIEWING_EMP_ID,FROMDATE,TODATE FROM PAR_REVIEWING_TRAN WHERE PRVTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.REVIEWING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtReviewing.setInt(1, resultset.getInt("PRVTID"));
                        innerresult = pstamtReviewing.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            String deptNames = getLAMemberDeptName(con, innerresult.getInt("LMID"));

                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));

                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME") + "," + deptNames);
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingReviewingAuthority(isPendingReviewingAuthority(con, innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            } else if (authtype.equalsIgnoreCase("ACCEPTING")) {

                String spctype = "G";
                pstamt = con.prepareStatement("SELECT PACTID,ACCEPTING_CUR_SPC FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO");
                pstamt.setInt(1, parId);
                resultset = pstamt.executeQuery();
                while (resultset.next()) {
                    if (resultset.getString("ACCEPTING_CUR_SPC") != null && !resultset.getString("ACCEPTING_CUR_SPC").equals("")) {
                        if (resultset.getString("ACCEPTING_CUR_SPC").length() > 2) {
                            spctype = "G";
                        } else {
                            spctype = "M";
                        }
                    }
                    if (spctype.equalsIgnoreCase("G")) {
                        /*pstamt = con.prepareStatement("SELECT ACCEPTING_EMP_ID,ACCEPTING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM(SELECT GETSPN(ACCEPTING_CUR_SPC)ACCEPTING_SPN,ACCEPTING_EMP_ID,FROMDATE,TODATE FROM PAR_ACCEPTING_TRAN WHERE PACTID=? ORDER BY HIERARCHY_NO)T1 "
                         + "INNER JOIN EMP_MAST ON T1.ACCEPTING_EMP_ID = EMP_MAST.EMP_ID");*/
                        pstamtAccepting = con.prepareStatement("SELECT ACCEPTING_EMP_ID,ACCEPTING_SPN,INITIALS,F_NAME,M_NAME,L_NAME,FROMDATE,TODATE FROM(SELECT GETSPN(ACCEPTING_CUR_SPC)ACCEPTING_SPN,ACCEPTING_EMP_ID,FROMDATE,TODATE FROM PAR_ACCEPTING_TRAN WHERE PACTID=? ORDER BY HIERARCHY_NO)T1 "
                                + "LEFT OUTER JOIN EMP_MAST ON T1.ACCEPTING_EMP_ID = EMP_MAST.EMP_ID");
                        pstamtAccepting.setInt(1, resultset.getInt("PACTID"));
                        innerresult = pstamtAccepting.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            if (innerresult.getString("ACCEPTING_EMP_ID") != null && innerresult.getString("ACCEPTING_EMP_ID").equals("00000000")) {
                                parhelper.setAuthorityname("NOT AVAILABLE");
                            } else {
                                parhelper.setAuthorityname(StringUtils.defaultString(innerresult.getString("INITIALS")) + " " + StringUtils.defaultString(innerresult.getString("F_NAME")) + " " + StringUtils.defaultString(innerresult.getString("M_NAME")) + " " + StringUtils.defaultString(innerresult.getString("L_NAME")));
                            }

                            parhelper.setAuthorityspn(innerresult.getString("ACCEPTING_SPN"));
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingAcceptingAuthority(isPendingAcceptingAuthority(con, innerresult.getString("ACCEPTING_EMP_ID"), taskId));
                            al.add(parhelper);
                        }
                    } else if (spctype.equalsIgnoreCase("M")) {
                        pstamtAccepting = con.prepareStatement("SELECT LMID,FULL_NAME,OFF_AS,OFF_NAME,FROMDATE,TODATE FROM ( SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME, OFF_AS,FROMDATE,TODATE FROM(SELECT ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,FROMDATE,TODATE FROM PAR_ACCEPTING_TRAN WHERE PACTID=? ORDER BY HIERARCHY_NO)T1"
                                + " INNER JOIN LA_MEMBERS ON T1.ACCEPTING_EMP_ID = LA_MEMBERS.LMID::VARCHAR) LA_MEMBERS"
                                + " INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID");
                        pstamtAccepting.setInt(1, resultset.getInt("PACTID"));
                        innerresult = pstamtAccepting.executeQuery();
                        if (innerresult.next()) {
                            Parauthorityhelperbean parhelper = new Parauthorityhelperbean();
                            String deptNames = getLAMemberDeptName(con, innerresult.getInt("LMID"));

                            parhelper.setAuthorityname(innerresult.getString("FULL_NAME"));

                            parhelper.setAuthorityspn(innerresult.getString("OFF_NAME") + "," + deptNames);
                            parhelper.setFromdt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("FROMDATE")));
                            parhelper.setTodt(CommonFunctions.getFormattedOutputDate1(innerresult.getDate("TODATE")));
                            parhelper.setIsPendingAcceptingAuthority(isPendingAcceptingAuthority(con, innerresult.getString("LMID"), taskId));
                            al.add(parhelper);
                        }
                    }
                }
            }
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstamt, pstamtReporting, pstamtReviewing, pstamtAccepting);
            DataBaseFunctions.closeSqlObjects(innerresult, resultset);
        }
        return al;
    }

    public ArrayList getReportingData(Connection con, ParDetail paf) throws Exception {

        String isrepcomp = "Y";
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList reportingbeans = new ArrayList();
        EmpPersonalInfo ei = null;

        try {

            String sql = "SELECT SUBMITTED_ON,REPORTING_CUR_SPC,IS_COMPLETED,PRPTID,REPORTING_EMP_ID,REPORTING_AUTH_NOTE,RATEQUALITYOFWORK,RATINGATTITUDE,"
                    + "RATINGCOMSKILL,RATINGCOORDINATION,RATINGDECISIONMAKING,RATINGINITIATIVE,RATINGITSKILL,RATINGLEADERSHIP,RATINGRESPONSIBILITY,"
                    + "TEAMWORKRATING,INADEQUACIESNOTE,INTEGRITYNOTE,tab1.GRADE_ID,GRADINGNOTE,fivet_charter_tenpercent,fivet_charter_fivepercent,fivet_mosarkar, "
                    + "rating_stsc_section,rating_quality_ofoutput,ratingeffectiveness_handling_work,penpicture_of_oficernote,stateof_health,sick_report_ondate, "
                    + "sick_details,total_a,total_b,total_c,total_d,orginal_filename_grading_document_reporting,disk_filename_grading_document_reporting,file_type_grading_document_reporting,par_id FROM PAR_REPORTING_TRAN tab1 WHERE PAR_ID=? ORDER BY HIERARCHY_NO";
            pst = con.prepareStatement(sql);
            pst.setInt(1, paf.getParid());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("N")) {
                    isrepcomp = "N";
                } else {
                    isrepcomp = rs.getString("IS_COMPLETED");
                }
                int reftableid = paf.getRefid();

                ReportingHelperBean rhb = new ReportingHelperBean();
                if (paf.getParstatus() > 6) {
                    paf.setIsreportingcompleted("Y");
                }
                if (paf.getParstatus() == 6 && rs.getInt("PRPTID") == reftableid) {
                    paf.setIsreportingcompleted(isrepcomp);
                    paf.setReportingempid(rs.getString("REPORTING_EMP_ID"));
                    paf.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    paf.setRatequalityofwork(rs.getInt("RATEQUALITYOFWORK"));
                    paf.setRatingattitude(rs.getInt("RATINGATTITUDE"));
                    paf.setRatingcomskill(rs.getInt("RATINGCOMSKILL"));
                    paf.setRatingcoordination(rs.getInt("RATINGCOORDINATION"));
                    paf.setRatingdecisionmaking(rs.getInt("RATINGDECISIONMAKING"));
                    paf.setRatinginitiative(rs.getInt("RATINGINITIATIVE"));
                    paf.setRatingitskill(rs.getInt("RATINGITSKILL"));
                    paf.setRatingleadership(rs.getInt("RATINGLEADERSHIP"));
                    paf.setRatingresponsibility(rs.getInt("RATINGRESPONSIBILITY"));
                    paf.setTeamworkrating(rs.getInt("TEAMWORKRATING"));
                    paf.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    paf.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    paf.setSltGrading(rs.getString("GRADE_ID"));
                    //paf.setSltGradingName(getGradingName(rs.getString("GRADE_ID")));
                    //paf.setSubmitted_on(CommonFunctions.getFormattedOutputDate(rs.getDate("SUBMITTED_ON")));
                    paf.setGradingNote(rs.getString("GRADINGNOTE"));

                    rhb.setRatingAttitudeStScSection(rs.getInt("rating_stsc_section"));
                    rhb.setRatingQualityOfOutput(rs.getInt("rating_quality_ofoutput"));
                    rhb.setRatingeffectivenessHandlingWork(rs.getInt("ratingeffectiveness_handling_work"));
                    rhb.setPenPictureOfOficerNote(rs.getString("penpicture_of_oficernote"));
                    rhb.setStateOfHealth(rs.getString("stateof_health"));
                    rhb.setSickReportOnDate(rs.getString("sick_report_ondate"));
                    rhb.setSickDetails(rs.getString("sick_details"));

                    rhb.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    rhb.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    rhb.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));

                    rhb.setPrtid(rs.getInt("PRPTID"));
                    rhb.setIscurrentreporting("Y");
                    rhb.setIsreportingcompleted(isrepcomp);
                    rhb.setReportingempid(rs.getString("REPORTING_EMP_ID"));

                    rhb.setReportingauthName(getAuthorityType(con, rs.getString("REPORTING_EMP_ID"), rs.getString("REPORTING_CUR_SPC")));
                    rhb.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    rhb.setRatequalityofwork(rs.getInt("RATEQUALITYOFWORK"));
                    rhb.setRatingattitude(rs.getInt("RATINGATTITUDE"));
                    rhb.setRatingcomskill(rs.getInt("RATINGCOMSKILL"));
                    rhb.setRatingcoordination(rs.getInt("RATINGCOORDINATION"));
                    rhb.setRatingdecisionmaking(rs.getInt("RATINGDECISIONMAKING"));
                    rhb.setRatinginitiative(rs.getInt("RATINGINITIATIVE"));
                    rhb.setRatingitskill(rs.getInt("RATINGITSKILL"));
                    rhb.setRatingleadership(rs.getInt("RATINGLEADERSHIP"));
                    rhb.setRatingresponsibility(rs.getInt("RATINGRESPONSIBILITY"));
                    rhb.setTeamworkrating(rs.getInt("TEAMWORKRATING"));
                    rhb.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    rhb.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    rhb.setSltGrading(rs.getString("GRADE_ID"));
                    rhb.setSltGradingName(getGradingName(rs.getString("GRADE_ID")));
                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    rhb.setGradingNote(rs.getString("GRADINGNOTE"));
                    rhb.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    rhb.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    rhb.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));

                    rhb.setTotalofA(rs.getInt("total_a"));
                    rhb.setTotalofB(rs.getInt("total_b"));
                    rhb.setTotalofC(rs.getInt("total_c"));
                    rhb.setTotalofD(rs.getInt("total_d"));

                    rhb.setOriginalFileNamegradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                    rhb.setDiskFileNameforgradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));
                    rhb.setFileTypeforgradingDocumentReporting(rs.getString("file_type_grading_document_reporting"));
                    rhb.setParId(rs.getInt("par_id"));

                } else {
                    rhb.setPrtid(rs.getInt("PRPTID"));
                    rhb.setIscurrentreporting("N");
                    rhb.setIsreportingcompleted(isrepcomp);
                    rhb.setReportingempid(rs.getString("REPORTING_EMP_ID"));
                    rhb.setReportingauthName(getAuthorityType(con, rs.getString("REPORTING_EMP_ID"), rs.getString("REPORTING_CUR_SPC")));
                    rhb.setAuthNote(rs.getString("REPORTING_AUTH_NOTE"));
                    rhb.setRatequalityofwork(rs.getInt("RATEQUALITYOFWORK"));
                    rhb.setRatingattitude(rs.getInt("RATINGATTITUDE"));
                    rhb.setRatingcomskill(rs.getInt("RATINGCOMSKILL"));
                    rhb.setRatingcoordination(rs.getInt("RATINGCOORDINATION"));
                    rhb.setRatingdecisionmaking(rs.getInt("RATINGDECISIONMAKING"));
                    rhb.setRatinginitiative(rs.getInt("RATINGINITIATIVE"));
                    rhb.setRatingitskill(rs.getInt("RATINGITSKILL"));
                    rhb.setRatingleadership(rs.getInt("RATINGLEADERSHIP"));
                    rhb.setRatingresponsibility(rs.getInt("RATINGRESPONSIBILITY"));
                    rhb.setTeamworkrating(rs.getInt("TEAMWORKRATING"));
                    rhb.setInadequaciesNote(rs.getString("INADEQUACIESNOTE"));
                    rhb.setIntegrityNote(rs.getString("INTEGRITYNOTE"));
                    rhb.setSltGrading(rs.getString("GRADE_ID"));
                    rhb.setSltGradingName(getGradingName(rs.getString("GRADE_ID")));
                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    rhb.setGradingNote(rs.getString("GRADINGNOTE"));
                    rhb.setFiveTChartertenpercent(rs.getString("fivet_charter_tenpercent"));
                    rhb.setFiveTCharterfivePercent(rs.getString("fivet_charter_fivepercent"));
                    rhb.setFiveTComponentmoSarkar(rs.getString("fivet_mosarkar"));

                    rhb.setRatingAttitudeStScSection(rs.getInt("rating_stsc_section"));
                    rhb.setRatingQualityOfOutput(rs.getInt("rating_quality_ofoutput"));
                    rhb.setRatingeffectivenessHandlingWork(rs.getInt("ratingeffectiveness_handling_work"));
                    rhb.setPenPictureOfOficerNote(rs.getString("penpicture_of_oficernote"));
                    rhb.setStateOfHealth(rs.getString("stateof_health"));
                    rhb.setSickReportOnDate(rs.getString("sick_report_ondate"));
                    rhb.setSickDetails(rs.getString("sick_details"));

                    rhb.setTotalofA(rs.getInt("total_a"));
                    rhb.setTotalofB(rs.getInt("total_b"));
                    rhb.setTotalofC(rs.getInt("total_c"));
                    rhb.setTotalofD(rs.getInt("total_d"));

                    rhb.setOriginalFileNamegradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                    rhb.setDiskFileNameforgradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));
                    rhb.setFileTypeforgradingDocumentReporting(rs.getString("file_type_grading_document_reporting"));
                    rhb.setParId(rs.getInt("par_id"));

                }
                reportingbeans.add(rhb);
            }
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);s
        }
        return reportingbeans;
    }

    public ArrayList getReviewingData(Connection con, ParDetail paf) throws Exception {

        String isrevcomp = "Y";
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList reviewingbeans = new ArrayList();
        EmpPersonalInfo ei = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String periodFromDate = "";
        String periodToDate = "";

        try {
            String sql = "SELECT SUBMITTED_ON,IS_COMPLETED,PRVTID,OVERALLGRADING,GENERALASSESSMENT,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,fromdate,todate, "
                    + "orginal_filename_grading_document_reviewing,disk_filename_grading_document_reviewing,file_type_grading_document_reviewing,PAR_ID "
                    + "FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO";
            pst = con.prepareStatement(sql);
            pst.setInt(1, paf.getParid());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("N")) {
                    isrevcomp = "N";
                } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("T")) {
                    isrevcomp = "T";
                } else {
                    isrevcomp = rs.getString("IS_COMPLETED");
                }

                ReviewingHelperBean rhb = new ReviewingHelperBean();
                int reftableid = paf.getRefid();
                //paf.setIsreviewingcompleted(isrevcomp);
                if (paf.getParstatus() == 7 && rs.getInt("PRVTID") == reftableid) {
                    paf.setSltReviewGrading(rs.getString("OVERALLGRADING"));
                    paf.setReviewingNote(rs.getString("GENERALASSESSMENT"));

                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");
                    int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                    diffInDays = diffInDays + 1;
                    if (diffInDays < 120) {
                        isrevcomp = "Y";
                    }
                    paf.setIsreviewingcompleted(isrevcomp);
                    rhb.setIscurrentreviewing("Y");
                    rhb.setIsreviewingcompleted(isrevcomp);
                    rhb.setSltReviewGrading(rs.getInt("OVERALLGRADING"));
                    rhb.setReviewGrading(getGradingName(rs.getString("OVERALLGRADING")));
                    rhb.setReviewingNote(rs.getString("GENERALASSESSMENT"));
                    if (rs.getString("REVIEWING_EMP_ID").equals("00000000")) {
                        rhb.setReviewingauthName("NOT AVAILABLE");
                    } else {
                        rhb.setReviewingauthName(getAuthorityType(con, rs.getString("REVIEWING_EMP_ID"), rs.getString("REVIEWING_CUR_SPC")));
                    }

                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    rhb.setOriginalFileNamegradingDocumentReviewing(rs.getString("orginal_filename_grading_document_reviewing"));
                    rhb.setDiskFileNameforgradingDocumentReviewing(rs.getString("disk_filename_grading_document_reviewing"));
                    rhb.setFileTypeforgradingDocumentReviewing(rs.getString("file_type_grading_document_reviewing"));
                    rhb.setParId(rs.getInt("par_id"));
                    rhb.setPrtid(rs.getInt("PRVTID"));
                } else {
                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");
                    int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                    diffInDays = diffInDays + 1;
                    if (rs.getString("IS_COMPLETED") == null || (!rs.getString("IS_COMPLETED").equals("T") && diffInDays < 120)) {
                        isrevcomp = "Y";
                    } else {
                        paf.setIsreviewingcompleted(isrevcomp);
                    }
                    rhb.setIscurrentreviewing("N");
                    rhb.setIsreviewingcompleted(isrevcomp);
                    rhb.setSltReviewGrading(rs.getInt("OVERALLGRADING"));
                    rhb.setReviewGrading(getGradingName(rs.getString("OVERALLGRADING")));
                    rhb.setReviewingNote(rs.getString("GENERALASSESSMENT"));
                    if (rs.getString("REVIEWING_EMP_ID").equals("00000000")) {
                        rhb.setReviewingauthName("NOT AVAILABLE");
                    } else {
                        rhb.setReviewingauthName(getAuthorityType(con, rs.getString("REVIEWING_EMP_ID"), rs.getString("REVIEWING_CUR_SPC")));
                    }
                    rhb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    rhb.setOriginalFileNamegradingDocumentReviewing(rs.getString("orginal_filename_grading_document_reviewing"));
                    rhb.setDiskFileNameforgradingDocumentReviewing(rs.getString("disk_filename_grading_document_reviewing"));
                    rhb.setFileTypeforgradingDocumentReviewing(rs.getString("file_type_grading_document_reviewing"));
                    rhb.setParId(rs.getInt("par_id"));
                    rhb.setPrtid(rs.getInt("PRVTID"));
                }
                reviewingbeans.add(rhb);
            }
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return reviewingbeans;
    }

    public ArrayList getAcceptingData(Connection con, ParDetail paf) throws Exception {

        String isacccomp = "Y";
        ArrayList acceptingbeans = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        EmpPersonalInfo ei = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String periodFromDate = "";
        String periodToDate = "";

        try {

            String sql = "SELECT SUBMITTED_ON,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,IS_COMPLETED,REMARK,PACTID,fromdate,todate,OVERALLGRADING, "
                    + "orginal_filename_grading_document_acceptting,disk_filename_grading_document_accepting,file_type_grading_document_accepting, "
                    + "file_path_grading_document_accepting,par_id,pactid FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? ORDER BY HIERARCHY_NO";
            pst = con.prepareStatement(sql);
            pst.setInt(1, paf.getParid());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("IS_COMPLETED") == null || rs.getString("IS_COMPLETED").equals("N")) {
                    isacccomp = "N";
                } else {
                    isacccomp = rs.getString("IS_COMPLETED");
                }

                int reftableid = paf.getRefid();
                paf.setIsacceptingcompleted(isacccomp);
                AcceptingHelperBean ahb = new AcceptingHelperBean();
                if (paf.getParstatus() == 8 && rs.getInt("PACTID") == reftableid) {
                    paf.setAcceptingNote(rs.getString("REMARK"));

                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");
                    int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                    diffInDays = diffInDays + 1;
                    if (diffInDays < 120) {
                        isacccomp = "Y";
                    }

                    ahb.setIscurrentaccepting("Y");
                    ahb.setIsacceptingcompleted(isacccomp);
                    ahb.setAcceptingNote(rs.getString("REMARK"));
                    if (rs.getString("ACCEPTING_EMP_ID").equals("00000000")) {
                        ahb.setAcceptingauthName("NOT AVAILABLE");
                    } else {
                        ahb.setAcceptingauthName(getAuthorityType(con, rs.getString("ACCEPTING_EMP_ID"), rs.getString("ACCEPTING_CUR_SPC")));
                    }

                    ahb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    ahb.setSltAcceptingGrading(rs.getInt("OVERALLGRADING"));
                    ahb.setOriginalFileNamegradingDocumentAccepting(rs.getString("orginal_filename_grading_document_acceptting"));
                    ahb.setDiskFileNameforgradingDocumentAccepting(rs.getString("disk_filename_grading_document_accepting"));
                    ahb.setFileTypeforgradingDocumentAccepting(rs.getString("file_type_grading_document_accepting"));
                    ahb.setFilepathforgradingDocumentAccepting(rs.getString("file_path_grading_document_accepting"));
                    ahb.setParId(rs.getInt("par_id"));
                    ahb.setPactid(rs.getInt("pactid"));

                } else {
                    periodFromDate = rs.getString("fromdate");
                    periodToDate = rs.getString("todate");
                    int diffInDays = (int) ((sdf.parse(periodToDate).getTime() - sdf.parse(periodFromDate).getTime()) / (1000 * 60 * 60 * 24));
                    diffInDays = diffInDays + 1;
                    if (rs.getString("IS_COMPLETED") == null || (!rs.getString("IS_COMPLETED").equals("T") && diffInDays < 120)) {
                        isacccomp = "Y";
                    }

                    ahb.setIsacceptingcompleted(isacccomp);
                    ahb.setAcceptingNote(rs.getString("REMARK"));
                    if (rs.getString("ACCEPTING_EMP_ID").equals("00000000")) {
                        ahb.setAcceptingauthName("NOT AVAILABLE");
                    } else {
                        ahb.setAcceptingauthName(getAuthorityType(con, rs.getString("ACCEPTING_EMP_ID"), rs.getString("ACCEPTING_CUR_SPC")));
                    }
                    ahb.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                    ahb.setSltAcceptingGrading1(getGradingName(rs.getString("OVERALLGRADING")));
                    ahb.setOriginalFileNamegradingDocumentAccepting(rs.getString("orginal_filename_grading_document_acceptting"));
                    ahb.setDiskFileNameforgradingDocumentAccepting(rs.getString("disk_filename_grading_document_accepting"));
                    ahb.setFileTypeforgradingDocumentAccepting(rs.getString("file_type_grading_document_accepting"));
                    ahb.setFilepathforgradingDocumentAccepting(rs.getString("file_path_grading_document_accepting"));
                    ahb.setParId(rs.getInt("par_id"));
                    ahb.setPactid(rs.getInt("pactid"));
                }
                acceptingbeans.add(ahb);
            }
        } catch (Exception sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return acceptingbeans;
    }

    public static String getLeaveTrainingName(Connection con, String leavecause, String leavetrainingType) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";
        String leavetraingLabel = "";
        try {
            if (leavecause != null && !leavecause.equals("")) {
                if (leavecause.equals("L")) {
                    sql = "SELECT TOL LEAVETRAININGNAME FROM G_LEAVE_MASTER WHERE TOL_ID=?";
                } else if (leavecause.equals("T")) {
                    sql = "SELECT TRAINING_TYPE LEAVETRAININGNAME FROM G_TRAINING_TYPE WHERE TRAINING_TYPE_ID=?";
                }
                if (sql != "") {
                    pst = con.prepareStatement(sql);
                    pst.setString(1, leavetrainingType);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        leavetraingLabel = rs.getString("LEAVETRAININGNAME");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return leavetraingLabel;
    }

    public String isPendingReportingAuthority(Connection con, String empid, int taskid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isPending = "N";
        try {
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT=? AND TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 6) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

    public String isPendingReviewingAuthority(Connection con, String empid, int taskid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isPending = "N";
        try {
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT=? AND TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 7) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

    public String isPendingAcceptingAuthority(Connection con, String empid, int taskid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isPending = "N";
        try {
            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE PENDING_AT=? AND TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 8) {
                    isPending = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return isPending;
    }

    public String getGradingName(String gradeid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String gradename = "";
        try {
            con = dataSource.getConnection();
            String sql = "";
            if (gradeid != null && !gradeid.equals("")) {
                sql = "SELECT GRADE_ID,GRADE_NAME FROM G_PAR_GRADE WHERE GRADE_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(gradeid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    gradename = rs.getString("GRADE_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradename;
    }

    public String getAuthorityType(Connection con, String empId, String spc) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;
        String name = "";

        try {
            if (empId != null) {
                if (empId.length() > 2) {
                    pst = con.prepareStatement("SELECT FULLNAME,SPN FROM ( "
                            + "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME,?::text cur_spc FROM EMP_MAST WHERE EMP_ID=?) EMP_MAST"
                            + " INNER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC");
                    pst.setString(1, spc);
                    pst.setString(2, empId);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        name = rs.getString("fullname");
                        if (rs.getString("spn") != null) {
                            name = name + " (" + rs.getString("SPN") + ")";
                        }

                    }
                } else if (empId.length() <= 2) {
                    pst = con.prepareStatement("select fullname,off_name from ( select ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') fullname,off_as from LA_MEMBERS where lmid=?) la left outer join g_officiating on la.off_as=g_officiating.off_id");
                    pst.setInt(1, Integer.parseInt(empId));
                    rs = pst.executeQuery();
                    if (rs.next()) {

                        name = rs.getString("fullname");
                        if (rs.getString("off_name") != null) {
                            name = name + " (" + rs.getString("off_name") + ")";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return name;
    }

    @Override
    public int getTaskid(String empid, int parid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int taskid = 0;
        try {
            con = dataSource.getConnection();

            String sql = "SELECT TASK_ID FROM PAR_MASTER WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);

            rs = pst.executeQuery();
            if (rs.next()) {
                taskid = rs.getInt("TASK_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskid;
    }

    @Override
    public int getPrptidReporting(String reportingempid, int parid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int prptid = 0;
        try {
            con = dataSource.getConnection();

            String sql = "select prptid from par_reporting_tran where reporting_emp_id=? AND par_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, reportingempid);
            pst.setInt(2, parid);

            rs = pst.executeQuery();
            if (rs.next()) {
                prptid = rs.getInt("prptid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prptid;
    }

    @Override
    public int getPrvtidReviewing(String reviewingempid, int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int prvtid = 0;
        try {
            con = dataSource.getConnection();

            String sql = "select prvtid from par_reviewing_tran where reviewing_emp_id=? AND par_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, reviewingempid);
            pst.setInt(2, parid);

            rs = pst.executeQuery();
            if (rs.next()) {
                prvtid = rs.getInt("prvtid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prvtid;
    }

    @Override
    public int getPactidAccepting(String acceptingempid, int parid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int pactid = 0;
        try {
            con = dataSource.getConnection();

            String sql = "select pactid from par_accepting_tran where accepting_emp_id=? AND par_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, acceptingempid);
            pst.setInt(2, parid);

            rs = pst.executeQuery();
            if (rs.next()) {
                pactid = rs.getInt("pactid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pactid;
    }

    public Font getDesired_PDF_Font(int fontsize, boolean isBold, boolean isUnderline) throws Exception {
        Font f = null;

        try {
            if (isBold == false && isUnderline == false) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.NORMAL);
            }
            if (isBold == true && isUnderline == false) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.BOLD);
            }
            if (isBold == true && isUnderline == true) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.BOLD | Font.UNDERLINE);
            }
            if (isBold == false && isUnderline == true) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, fontsize, Font.UNDERLINE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return f;
    }

    public String findAuthorityType(String empid, int parid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String authority = "";
        try {
            con = dataSource.getConnection();

            String sql = "SELECT REPORTING_EMP_ID FROM PAR_REPORTING_TRAN WHERE REPORTING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authority = "REPORTING";
            }

            sql = "SELECT REVIEWING_EMP_ID FROM PAR_REVIEWING_TRAN WHERE REVIEWING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authority = authority + "_REVIEWING";
            }

            sql = "SELECT ACCEPTING_EMP_ID FROM PAR_ACCEPTING_TRAN WHERE ACCEPTING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authority = authority + "_ACCEPTING";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    @Override
    public List getPARGradeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        ArrayList gradelist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT GRADE_ID,GRADE_NAME FROM G_PAR_GRADE ORDER BY GRADE_ID ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("GRADE_NAME"));
                so.setValue(rs.getString("GRADE_ID"));
                gradelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradelist;
    }

    @Override
    public void saveAndForwardPAR(ParDetail paf, String empid, String forwardbtn, String gradingPath) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        Date d1 = new Date();

        int maxhierarchyno = 0;
        int nexthierarchyno = 0;
        int prevAuthHierarchyno = 0;
        String curAuthType = "";
        String nextAuthType = "";
        String nextAuthEmpId = "";

        String diskfileName = null;
        String originalFileName = null;
        String contentType = null;

        try {
            con = dataSource.getConnection();
            String dirpath = "";
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String diskfilename = "";
            String diskfilenameReviewing = "";
            String diskfilenameAccepting = "";

            int prptid = getPrptidReporting(empid, paf.getParid());
            int prvtid = getPrvtidReviewing(empid, paf.getParid());
            int pactid = getPactidAccepting(empid, paf.getParid());

            if (paf.getGradingDocumentReporting() != null && !paf.getGradingDocumentReporting().isEmpty()) {
                long time = System.currentTimeMillis();
                diskfilename = paf.getParid() + "_" + paf.getReportingempid() + "_" + prptid + "_" + time;
                dirpath = gradingPath + paf.getFiscalYear() + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + diskfilename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = paf.getGradingDocumentReporting().getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
            }
            if (paf.getGradingDocumentReviewing() != null && !paf.getGradingDocumentReviewing().isEmpty()) {
                long time = System.currentTimeMillis();
                diskfilenameReviewing = paf.getParid() + "_" + empid + "_" + prvtid + "_" + time;
                dirpath = gradingPath + paf.getFiscalYear() + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + diskfilenameReviewing);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = paf.getGradingDocumentReviewing().getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
            }

            if (paf.getGradingDocumentAccepting() != null && !paf.getGradingDocumentAccepting().isEmpty()) {
                long time = System.currentTimeMillis();
                diskfilenameAccepting = paf.getParid() + "_" + empid + "_" + pactid + "_" + time;
                dirpath = gradingPath + paf.getFiscalYear() + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + diskfilenameAccepting);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = paf.getGradingDocumentAccepting().getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
            }

            if (forwardbtn.equals("Save")) {
                if ((paf.getIsreportingcompleted() == null || paf.getIsreportingcompleted().equals("") || paf.getIsreportingcompleted().equals("N")) && !paf.getIsreportingcompleted().equals("F")) {

                    pstmt = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET REPORTING_AUTH_NOTE=?,RATEQUALITYOFWORK=?,RATINGATTITUDE=?,RATINGCOMSKILL=?,"
                            + "RATINGCOORDINATION=?,RATINGDECISIONMAKING=?,RATINGINITIATIVE=?,RATINGITSKILL=?,RATINGLEADERSHIP=?,RATINGRESPONSIBILITY=?,"
                            + "TEAMWORKRATING=?,GRADE_ID=?,GRADINGNOTE=?,INADEQUACIESNOTE=?,INTEGRITYNOTE=?,fivet_charter_tenpercent=?,fivet_charter_fivepercent=?,"
                            + "fivet_mosarkar=?,rating_stsc_section=?,rating_quality_ofoutput=?, ratingeffectiveness_handling_work=?,penpicture_of_oficernote=?,"
                            + "stateof_health=?,sick_report_ondate=?,sick_details=?,total_a=?, total_b=?, total_c=?, total_d=?,orginal_filename_grading_document_reporting=?, "
                            + "disk_filename_grading_document_reporting=?,file_type_grading_document_reporting=?,file_path_grading_document_reporting=? WHERE PAR_ID=? AND REPORTING_EMP_ID=?");
                    pstmt.setString(1, paf.getAuthNote());
                    pstmt.setInt(2, paf.getRatequalityofwork());
                    pstmt.setInt(3, paf.getRatingattitude());
                    pstmt.setInt(4, paf.getRatingcomskill());
                    pstmt.setInt(5, paf.getRatingcoordination());
                    pstmt.setInt(6, paf.getRatingdecisionmaking());
                    pstmt.setInt(7, paf.getRatinginitiative());
                    pstmt.setInt(8, paf.getRatingitskill());
                    pstmt.setInt(9, paf.getRatingleadership());
                    pstmt.setInt(10, paf.getRatingresponsibility());
                    pstmt.setInt(11, paf.getTeamworkrating());
                    if (paf.getSltGrading() != null && !paf.getSltGrading().equals("")) {
                        pstmt.setInt(12, Integer.parseInt(paf.getSltGrading()));
                    } else {
                        pstmt.setString(12, null);
                    }
                    pstmt.setString(13, paf.getGradingNote());
                    pstmt.setString(14, paf.getInadequaciesNote());
                    pstmt.setString(15, paf.getIntegrityNote());
                    pstmt.setString(16, paf.getFiveTChartertenpercent());
                    pstmt.setString(17, paf.getFiveTCharterfivePercent());
                    pstmt.setString(18, paf.getFiveTComponentmoSarkar());

                    pstmt.setInt(19, paf.getRatingAttitudeStScSection());
                    pstmt.setInt(20, paf.getRatingQualityOfOutput());
                    pstmt.setInt(21, paf.getRatingeffectivenessHandlingWork());
                    pstmt.setString(22, paf.getPenPictureOfOficerNote());
                    pstmt.setString(23, paf.getStateOfHealth());
                    pstmt.setString(24, paf.getSickReportOnDate());
                    pstmt.setString(25, paf.getSickDetails());

                    pstmt.setInt(26, paf.getTotalofA());
                    pstmt.setInt(27, paf.getTotalofB());
                    pstmt.setInt(28, paf.getTotalofC());
                    pstmt.setInt(29, paf.getTotalofD());

                    if (paf.getGradingDocumentReporting() != null && !paf.getGradingDocumentReporting().isEmpty()) {
                        pstmt.setString(30, paf.getGradingDocumentReporting().getOriginalFilename());
                        pstmt.setString(31, diskfilename);
                        pstmt.setString(32, paf.getGradingDocumentReporting().getContentType());
                        pstmt.setString(33, dirpath);
                    } else {
                        pstmt.setString(30, null);
                        pstmt.setString(31, null);
                        pstmt.setString(32, null);
                        pstmt.setString(33, null);
                    }

                    pstmt.setInt(34, paf.getParid());
                    pstmt.setString(35, paf.getReportingempid());
                    pstmt.executeUpdate();

                } else if (paf.getIsreviewingcompleted() == null || paf.getIsreviewingcompleted().equals("") || paf.getIsreviewingcompleted().equals("N") && !paf.getIsreviewingcompleted().equals("F")) {
                    pstmt = con.prepareStatement("UPDATE PAR_REVIEWING_TRAN SET GENERALASSESSMENT=?,OVERALLGRADING=?,orginal_filename_grading_document_reviewing=?, "
                            + "disk_filename_grading_document_reviewing=?,file_type_grading_document_reviewing=?,file_path_grading_document_reviewing=? WHERE PAR_ID=? AND REVIEWING_EMP_ID=?");
                    pstmt.setString(1, paf.getReviewingNote());
                    pstmt.setInt(2, Integer.parseInt(paf.getSltReviewGrading()));
                    if (paf.getGradingDocumentReviewing() != null && !paf.getGradingDocumentReviewing().isEmpty()) {
                        pstmt.setString(3, paf.getGradingDocumentReviewing().getOriginalFilename());
                        pstmt.setString(4, diskfilenameReviewing);
                        pstmt.setString(5, paf.getGradingDocumentReviewing().getContentType());
                        pstmt.setString(6, dirpath);
                    } else {
                        pstmt.setString(3, null);
                        pstmt.setString(4, null);
                        pstmt.setString(5, null);
                        pstmt.setString(6, null);
                    }
                    pstmt.setInt(7, paf.getParid());
                    pstmt.setString(8, empid);
                    pstmt.executeUpdate();

                } else if (paf.getIsacceptingcompleted() == null || paf.getIsacceptingcompleted().equals("") || paf.getIsacceptingcompleted().equals("N")) {

                    pstmt = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET REMARK=?,overallgrading=?,orginal_filename_grading_document_acceptting=?, "
                            + "disk_filename_grading_document_accepting=?,file_type_grading_document_accepting=?,file_path_grading_document_accepting=? WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
                    pstmt.setString(1, paf.getAcceptingNote());
                    pstmt.setInt(2, Integer.parseInt(paf.getSltAcceptingGrading()));
                    if (paf.getGradingDocumentAccepting() != null && !paf.getGradingDocumentAccepting().isEmpty()) {
                        pstmt.setString(3, paf.getGradingDocumentAccepting().getOriginalFilename());
                        pstmt.setString(4, diskfilenameAccepting);
                        pstmt.setString(5, paf.getGradingDocumentAccepting().getContentType());
                        pstmt.setString(6, dirpath);
                    } else {
                        pstmt.setString(3, null);
                        pstmt.setString(4, null);
                        pstmt.setString(5, null);
                        pstmt.setString(6, null);
                    }
                    pstmt.setInt(7, paf.getParid());
                    pstmt.setString(8, empid);
                    int accptRes = pstmt.executeUpdate();

                }
            } else if (forwardbtn.equals("Submit")) {
                if ((paf.getIsreportingcompleted() == null || paf.getIsreportingcompleted().equals("") || paf.getIsreportingcompleted().equals("N")) && !paf.getIsreportingcompleted().equals("F")) {
                    if (paf.getGradingDocumentReporting() != null && !paf.getGradingDocumentReporting().isEmpty()) {
                        pstmt = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET REPORTING_AUTH_NOTE=?,RATEQUALITYOFWORK=?,RATINGATTITUDE=?,RATINGCOMSKILL=?,"
                                + "RATINGCOORDINATION=?,RATINGDECISIONMAKING=?,RATINGINITIATIVE=?,RATINGITSKILL=?,RATINGLEADERSHIP=?,RATINGRESPONSIBILITY=?,"
                                + "TEAMWORKRATING=?,GRADE_ID=?,GRADINGNOTE=?,INADEQUACIESNOTE=?,INTEGRITYNOTE=?,rating_stsc_section=?,rating_quality_ofoutput=?,"
                                + "ratingeffectiveness_handling_work=?,penpicture_of_oficernote=?,stateof_health=?,sick_report_ondate=?,sick_details=?,total_a=?, "
                                + "total_b=?, total_c=?, total_d=?,IS_COMPLETED=?,SUBMITTED_ON=?, fivet_charter_tenpercent=?,fivet_charter_fivepercent=?,"
                                + "fivet_mosarkar=?,orginal_filename_grading_document_reporting=?,disk_filename_grading_document_reporting=?, "
                                + "file_type_grading_document_reporting=?,file_path_grading_document_reporting=? WHERE PAR_ID=? AND REPORTING_EMP_ID=?");
                        pstmt.setString(1, paf.getAuthNote());
                        pstmt.setInt(2, paf.getRatequalityofwork());
                        pstmt.setInt(3, paf.getRatingattitude());
                        pstmt.setInt(4, paf.getRatingcomskill());
                        pstmt.setInt(5, paf.getRatingcoordination());
                        pstmt.setInt(6, paf.getRatingdecisionmaking());
                        pstmt.setInt(7, paf.getRatinginitiative());
                        pstmt.setInt(8, paf.getRatingitskill());
                        pstmt.setInt(9, paf.getRatingleadership());
                        pstmt.setInt(10, paf.getRatingresponsibility());
                        pstmt.setInt(11, paf.getTeamworkrating());
                        if (paf.getSltGrading() != null && !paf.getSltGrading().equals("")) {
                            pstmt.setInt(12, Integer.parseInt(paf.getSltGrading()));
                        } else {
                            pstmt.setString(12, null);
                        }
                        pstmt.setString(13, paf.getGradingNote());
                        pstmt.setString(14, paf.getInadequaciesNote());
                        pstmt.setString(15, paf.getIntegrityNote());

                        pstmt.setInt(16, paf.getRatingAttitudeStScSection());
                        pstmt.setInt(17, paf.getRatingQualityOfOutput());
                        pstmt.setInt(18, paf.getRatingeffectivenessHandlingWork());
                        pstmt.setString(19, paf.getPenPictureOfOficerNote());
                        pstmt.setString(20, paf.getStateOfHealth());
                        pstmt.setString(21, paf.getSickReportOnDate());
                        pstmt.setString(22, paf.getSickDetails());

                        pstmt.setInt(23, paf.getTotalofA());
                        pstmt.setInt(24, paf.getTotalofB());
                        pstmt.setInt(25, paf.getTotalofC());
                        pstmt.setInt(26, paf.getTotalofD());

                        pstmt.setString(27, "Y");
                        pstmt.setTimestamp(28, new Timestamp(d1.getTime()));
                        pstmt.setString(29, paf.getFiveTChartertenpercent());
                        pstmt.setString(30, paf.getFiveTCharterfivePercent());
                        pstmt.setString(31, paf.getFiveTComponentmoSarkar());

                        pstmt.setString(32, paf.getGradingDocumentReporting().getOriginalFilename());
                        pstmt.setString(33, diskfilename);
                        pstmt.setString(34, paf.getGradingDocumentReporting().getContentType());
                        pstmt.setString(35, dirpath);

                        pstmt.setInt(36, paf.getParid());
                        pstmt.setString(37, paf.getReportingempid());
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET REPORTING_AUTH_NOTE=?,RATEQUALITYOFWORK=?,RATINGATTITUDE=?,RATINGCOMSKILL=?,"
                                + "RATINGCOORDINATION=?,RATINGDECISIONMAKING=?,RATINGINITIATIVE=?,RATINGITSKILL=?,RATINGLEADERSHIP=?,RATINGRESPONSIBILITY=?,"
                                + "TEAMWORKRATING=?,GRADE_ID=?,GRADINGNOTE=?,INADEQUACIESNOTE=?,INTEGRITYNOTE=?,rating_stsc_section=?,rating_quality_ofoutput=?,"
                                + "ratingeffectiveness_handling_work=?,penpicture_of_oficernote=?,stateof_health=?,sick_report_ondate=?,sick_details=?,total_a=?, "
                                + "total_b=?, total_c=?, total_d=?,IS_COMPLETED=?,SUBMITTED_ON=?, fivet_charter_tenpercent=?,fivet_charter_fivepercent=?,"
                                + "fivet_mosarkar=? WHERE PAR_ID=? AND REPORTING_EMP_ID=?");

                        pstmt.setString(1, paf.getAuthNote());
                        pstmt.setInt(2, paf.getRatequalityofwork());
                        pstmt.setInt(3, paf.getRatingattitude());
                        pstmt.setInt(4, paf.getRatingcomskill());
                        pstmt.setInt(5, paf.getRatingcoordination());
                        pstmt.setInt(6, paf.getRatingdecisionmaking());
                        pstmt.setInt(7, paf.getRatinginitiative());
                        pstmt.setInt(8, paf.getRatingitskill());
                        pstmt.setInt(9, paf.getRatingleadership());
                        pstmt.setInt(10, paf.getRatingresponsibility());
                        pstmt.setInt(11, paf.getTeamworkrating());
                        if (paf.getSltGrading() != null && !paf.getSltGrading().equals("")) {
                            pstmt.setInt(12, Integer.parseInt(paf.getSltGrading()));
                        } else {
                            pstmt.setString(12, null);
                        }
                        pstmt.setString(13, paf.getGradingNote());
                        pstmt.setString(14, paf.getInadequaciesNote());
                        pstmt.setString(15, paf.getIntegrityNote());

                        pstmt.setInt(16, paf.getRatingAttitudeStScSection());
                        pstmt.setInt(17, paf.getRatingQualityOfOutput());
                        pstmt.setInt(18, paf.getRatingeffectivenessHandlingWork());
                        pstmt.setString(19, paf.getPenPictureOfOficerNote());
                        pstmt.setString(20, paf.getStateOfHealth());
                        pstmt.setString(21, paf.getSickReportOnDate());
                        pstmt.setString(22, paf.getSickDetails());

                        pstmt.setInt(23, paf.getTotalofA());
                        pstmt.setInt(24, paf.getTotalofB());
                        pstmt.setInt(25, paf.getTotalofC());
                        pstmt.setInt(26, paf.getTotalofD());

                        pstmt.setString(27, "Y");
                        pstmt.setTimestamp(28, new Timestamp(d1.getTime()));
                        pstmt.setString(29, paf.getFiveTChartertenpercent());
                        pstmt.setString(30, paf.getFiveTCharterfivePercent());
                        pstmt.setString(31, paf.getFiveTComponentmoSarkar());
                        System.out.println("inside the submit=====" + paf.getFiveTChartertenpercent() + "===" + paf.getFiveTCharterfivePercent() + "===" + paf.getFiveTComponentmoSarkar());
                        pstmt.setInt(32, paf.getParid());
                        pstmt.setString(33, paf.getReportingempid());
                        pstmt.executeUpdate();

                    }
                    startWorkFlow(con, paf.getParid(), paf.getTaskid());

                    //Start - SMS Code
                    pstmt = con.prepareStatement("SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_REPORTING_TRAN WHERE PAR_ID=?");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        maxhierarchyno = rs.getInt("MAXH");
                    }

                    pstmt = con.prepareStatement("SELECT HIERARCHY_NO,REPORTING_EMP_ID FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO ASC");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nextAuthType = "Reporting";//For Appriasee SMS
                        nexthierarchyno = rs.getInt("HIERARCHY_NO");//For Appriasee SMS
                        prevAuthHierarchyno = nexthierarchyno - 1;//For Next Authority SMS
                        nextAuthEmpId = rs.getString("REPORTING_EMP_ID");//For Next Authority SMS
                    } else {
                        nextAuthType = "Reviewing";//For Appriasee SMS
                        nexthierarchyno = 1;//For Appriasee SMS
                        pst = con.prepareStatement("SELECT HIERARCHY_NO,REVIEWING_EMP_ID FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO ASC");
                        pst.setInt(1, paf.getParid());
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            prevAuthHierarchyno = maxhierarchyno;//For Next Authority SMS
                            nextAuthEmpId = rs1.getString("REVIEWING_EMP_ID");//For Next Authority SMS
                        }
                    }
                    curAuthType = "Reporting";//For Next Authority SMS
                    //End - SMS Code

                } else if (paf.getIsreviewingcompleted() == null || paf.getIsreviewingcompleted().equals("") || paf.getIsreviewingcompleted().equals("N") && !paf.getIsreviewingcompleted().equals("F")) {
                    if (paf.getGradingDocumentReviewing() != null && !paf.getGradingDocumentReviewing().isEmpty()) {
                        pstmt = con.prepareStatement("UPDATE PAR_REVIEWING_TRAN SET GENERALASSESSMENT=?,OVERALLGRADING=?,SUBMITTED_ON=?,IS_COMPLETED=?,orginal_filename_grading_document_reviewing=?, "
                                + "disk_filename_grading_document_reviewing=?,file_type_grading_document_reviewing=?,file_path_grading_document_reviewing=? WHERE PAR_ID=? AND REVIEWING_EMP_ID=?");
                        pstmt.setString(1, paf.getReviewingNote());
                        pstmt.setInt(2, Integer.parseInt(paf.getSltReviewGrading()));
                        pstmt.setTimestamp(3, new Timestamp(d1.getTime()));
                        pstmt.setString(4, "Y");
                        pstmt.setString(5, paf.getGradingDocumentReviewing().getOriginalFilename());
                        pstmt.setString(6, diskfilenameReviewing);
                        pstmt.setString(7, paf.getGradingDocumentReviewing().getContentType());
                        pstmt.setString(8, dirpath);
                        pstmt.setInt(9, paf.getParid());
                        pstmt.setString(10, empid);
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE PAR_REVIEWING_TRAN SET GENERALASSESSMENT=?,OVERALLGRADING=?,SUBMITTED_ON=?,IS_COMPLETED=? WHERE PAR_ID=? AND REVIEWING_EMP_ID=?");
                        pstmt.setString(1, paf.getReviewingNote());
                        pstmt.setInt(2, Integer.parseInt(paf.getSltReviewGrading()));
                        pstmt.setTimestamp(3, new Timestamp(d1.getTime()));
                        pstmt.setString(4, "Y");
                        pstmt.setInt(5, paf.getParid());
                        pstmt.setString(6, empid);
                        pstmt.executeUpdate();
                    }
                    startWorkFlow(con, paf.getParid(), paf.getTaskid());

                    //Start - SMS Code
                    pstmt = con.prepareStatement("SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        maxhierarchyno = rs.getInt("MAXH");
                    }

                    pstmt = con.prepareStatement("SELECT HIERARCHY_NO,REVIEWING_EMP_ID FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO ASC");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nextAuthType = "Reviewing";//For Appriasee SMS
                        nexthierarchyno = rs.getInt("HIERARCHY_NO");//For Appriasee SMS
                        prevAuthHierarchyno = nexthierarchyno - 1;//For Next Authority SMS
                        nextAuthEmpId = rs.getString("REVIEWING_EMP_ID");//For Next Authority SMS
                    } else {
                        nexthierarchyno = 1;//For Appriasee SMS
                        nextAuthType = "Accepting";//For Appriasee SMS
                        pst = con.prepareStatement("SELECT HIERARCHY_NO,ACCEPTING_EMP_ID FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO ASC");
                        pst.setInt(1, paf.getParid());
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            prevAuthHierarchyno = maxhierarchyno;//For Next Authority SMS
                            nextAuthEmpId = rs1.getString("ACCEPTING_EMP_ID");//For Next Authority SMS
                        }
                    }
                    curAuthType = "Reviewing";
                    //End - SMS Code

                } else if (paf.getIsacceptingcompleted() == null || paf.getIsacceptingcompleted().equals("") || paf.getIsacceptingcompleted().equals("N")) {
                    if (paf.getGradingDocumentAccepting() != null && !paf.getGradingDocumentAccepting().isEmpty()) {
                        pstmt = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET REMARK=?,IS_COMPLETED=?,OVERALLGRADING=?,SUBMITTED_ON=?,orginal_filename_grading_document_acceptting=?, "
                                + "disk_filename_grading_document_accepting=?,file_type_grading_document_accepting=?,file_path_grading_document_accepting=? WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
                        pstmt.setString(1, paf.getAcceptingNote());
                        pstmt.setString(2, "Y");
                        pstmt.setInt(3, Integer.parseInt(paf.getSltAcceptingGrading()));
                        pstmt.setTimestamp(4, new Timestamp(d1.getTime()));
                        pstmt.setString(5, paf.getGradingDocumentAccepting().getOriginalFilename());
                        pstmt.setString(6, diskfilenameAccepting);
                        pstmt.setString(7, paf.getGradingDocumentAccepting().getContentType());

                        pstmt.setString(8, dirpath);
                        pstmt.setInt(9, paf.getParid());
                        pstmt.setString(10, empid);
                        pstmt.executeUpdate();
                    } else {
                        pstmt = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET REMARK=?,IS_COMPLETED=?,OVERALLGRADING=?,SUBMITTED_ON=? WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
                        pstmt.setString(1, paf.getAcceptingNote());
                        pstmt.setString(2, "Y");
                        pstmt.setInt(3, Integer.parseInt(paf.getSltAcceptingGrading()));
                        pstmt.setTimestamp(4, new Timestamp(d1.getTime()));
                        pstmt.setInt(5, paf.getParid());
                        pstmt.setString(6, empid);
                        pstmt.executeUpdate();
                    }
                    startWorkFlow(con, paf.getParid(), paf.getTaskid());

                    //Start - SMS Code
                    pstmt = con.prepareStatement("SELECT MAX(HIERARCHY_NO) MAXH FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        maxhierarchyno = rs.getInt("MAXH");
                    }

                    pstmt = con.prepareStatement("SELECT HIERARCHY_NO,ACCEPTING_EMP_ID FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND IS_COMPLETED IS NULL ORDER BY HIERARCHY_NO ASC");
                    pstmt.setInt(1, paf.getParid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nextAuthType = "Accepting";//For Appriasee SMS
                        nexthierarchyno = rs.getInt("HIERARCHY_NO");//For Appriasee SMS
                        prevAuthHierarchyno = nexthierarchyno - 1;//For Next Authority SMS
                        nextAuthEmpId = rs.getString("ACCEPTING_EMP_ID");//For Next Authority SMS
                    } else {
                        nextAuthType = "Custodian";
                        nextAuthEmpId = "Custodian";
                    }
                    curAuthType = "Accepting";
                    //End - SMS Code
                }
            }
            if (!nextAuthType.equals("")) {
                sendSMStoAppraisee(paf.getParid(), nextAuthType, nexthierarchyno, paf.getFiscalYear());
            }
            if (!nextAuthEmpId.equals("")) {
                sendSMStoAuthority(paf.getParid(), curAuthType, nextAuthEmpId, paf.getFiscalYear(), prevAuthHierarchyno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void viewPDFfunc(Document document, ParDetail paf, String empid, String filepath) throws Exception {

        try {
            /*Phrase phrs1 = new Phrase();
             ArrayList parts = HTMLWorker.parseToList(new StringReader(paf.getSelfappraisal()),null);
             for (int i = 0; i < parts.size(); i++) {
             Element e = (Element)parts.get(i);
             phrs1.add(e);
             }
            
             Phrase phrs2 = new Phrase();
             parts = HTMLWorker.parseToList(new StringReader(paf.getSpecialcontribution()),null);
             for (int i = 0; i < parts.size(); i++) {
             Element e = (Element)parts.get(i);
             phrs2.add(e);
             }
            
             Phrase phrs3 = new Phrase();
             parts = HTMLWorker.parseToList(new StringReader(paf.getReason()),null);
             for (int i = 0; i < parts.size(); i++) {
             Element e = (Element)parts.get(i);
             phrs3.add(e);
             }*/
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPTable innertable = null;

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            PdfPCell innercell = null;

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for Group A and B officers of Govt. of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission Record", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by Appraisee )", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("Financial Year : ", f1);
            Chunk c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            Chunk c3 = new Chunk(" (for the period from ", f1);
            Chunk c4 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c5 = new Chunk(" to ", f1);
            Chunk c6 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c7 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Name & Designation of the Officer Reported Upon     ", f1);
            c2 = new Chunk(paf.getApplicant(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service and Group (A/B) to which the  Officer belongs ", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group - " + StringUtils.defaultString(paf.getEmpGroup()), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Details of Transmission / Movement of PAR", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in at the time of transmission", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("by respective officer/staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission by", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Transmitted to whom (Name, Designation &Address)", f1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Appraisee", f1));
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reporting\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            ArrayList reportAuthlist = paf.getReportingauth();
            Parauthorityhelperbean parhelper = null;
            String reportAuthName = "";
            String reportAuthDesg = "";
            int slno = 0;
            for (int i = 0; i < reportAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reportAuthlist.get(i);
                slno = i + 1;
                if (reportAuthName == "") {
                    reportAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reportAuthName = reportAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reportAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList reviewAuthlist = paf.getReviewingauth();
            parhelper = null;
            String reviewAuthName = "";
            String reviewAuthDesg = "";
            slno = 0;
            for (int i = 0; i < reviewAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reviewAuthlist.get(i);
                slno = i + 1;
                if (reviewAuthName == "") {
                    reviewAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reviewAuthName = reviewAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reviewAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Reviewing\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList acceptAuthlist = paf.getAcceptingauth();
            parhelper = null;
            String acceptAuthName = "";
            String acceptAuthDesg = "";
            slno = 0;
            for (int i = 0; i < acceptAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) acceptAuthlist.get(i);
                slno = i + 1;
                if (acceptAuthName == "") {
                    acceptAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    acceptAuthName = acceptAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //acceptAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Accepting \nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            //Second Page
            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("PERFORMANCE APPRAISAL REPORT", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("for", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group A and Group B Officers of Govt. of Odisha.", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Report for the financial year ", f1);
            c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("(  Period from ", f1);
            c2 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" to ", f1);
            c4 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c5 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL DATA", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "PART-I", f1));
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 33) + "(To be filled in by the Appraisee )", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Full Name of the Officer:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApplicant(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            String filePath = filepath;
            String url = filePath + paf.getApplicantempid() + ".jpg";
            File f = null;
            f = new File(url);
            Image img1 = null;
            if (f.exists()) {
                img1 = Image.getInstance(url);
            }
            if (img1 != null) {
                //img1.scalePercent(15f);
                img1.scaleToFit(100f, 80f);
                cell = new PdfPCell(img1);
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(" 2. Date of Birth:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getDob(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            //cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3. Service to which the Officer belongs:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 4. Group to which the Officer belongs(A or B):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpGroup(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 5. Designation  during the period of Report:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 6. Office to which posted with Head Quarters:", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            String office = "";
            if (paf.getSltHeadQuarter() != null && !paf.getSltHeadQuarter().equals("")) {
                office = paf.getEmpOffice() + "," + paf.getSltHeadQuarter();
            } else {
                office = paf.getEmpOffice();
            }
            cell = new PdfPCell(new Phrase(office, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 7. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            AbsenteeBean pab = null;
            ArrayList leaveAbsentee = paf.getLeaveAbsentee();
            String leaveFrmDt = "";
            String leaveToDt = "";
            String leaves = "";
            for (int i = 0; i < leaveAbsentee.size(); i++) {
                pab = (AbsenteeBean) leaveAbsentee.get(i);
                innertable = new PdfPTable(2);
                innertable.setWidths(new int[]{2, 2});
                innertable.setWidthPercentage(100);
                //innercell = new
                if (pab.getFromDate() != null && !pab.getFromDate().equals("")) {
                    if (leaves == "") {
                        leaves = "From: " + pab.getFromDate() + " to: " + pab.getToDate();
                    } else {
                        leaves = leaves + "\nFrom: " + pab.getFromDate() + " to: " + pab.getToDate();
                    }
                }
                //leaveFrmDt = pab.getFromDate();
                //leaveToDt = pab.getToDate();
            }
            if (leaves != "") {
                cell = new PdfPCell(new Phrase(leaves, f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Nil", f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 8. Name & Designation of the Reporting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 9. Name & Designation of the Reviewing Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 10. Name & Designation of the Accepting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApplicant(), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of the Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            //Third Page
            table = new PdfPTable(5);
            table.setWidths(new int[]{1, 4, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("    PART-II" + StringUtils.repeat(" ", 51) + "SELF-APPRAISAL", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by the Appraisee )", f1));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Brief description of duties/tasks entrusted.(in about 200 words)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getSelfappraisal())));
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 2. Physical/Financial Targets & Achievements", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{2, 2, 2, 2, 2, 2});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("SL. No", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Task", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Target", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Qualitative % of Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("% of Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            ArrayList achievementList = paf.getAchivementList();
            if (achievementList != null && achievementList.size() > 0) {
                AchievementBean ab = null;
                for (int i = 0; i < achievementList.size(); i++) {
                    ab = (AchievementBean) achievementList.get(i);
                    innertable = new PdfPTable(6);
                    innertable.setWidths(new int[]{2, 2, 2, 2, 2, 2});
                    innertable.setWidthPercentage(100);
                    int j = i + 1;
                    innercell = new PdfPCell(new Phrase(j + "", f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getPercentQualitative(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getPercentAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setColspan(5);
                    //cell.setFixedHeight(130);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3. Significant work, if any, done", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getSpecialcontribution())));
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            if (paf.getReason() != null && !paf.getReason().equals("")) {
                //Reason Start
                cell = new PdfPCell(new Phrase(" 4. Hindrance", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getReason())));
                cell.setColspan(4);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                //Reason Stop
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                document.add(table);
                document.newPage();

                //Third Page
                table = new PdfPTable(5);
                table.setWidths(new int[]{1, 4, 5, 3, 3});
                table.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getApplicant()), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Place: " + StringUtils.defaultString(paf.getPlace()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date: " + paf.getSubmitted_on(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            String authorityType = findAuthorityType(empid, paf.getParid());
            if (authorityType != null && !authorityType.equals("")) {

                document.add(table);

                table = new PdfPTable(4);
                table.setWidths(new int[]{4, 2, 4, 2});
                table.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
                cell.setColspan(4);
                cell.setFixedHeight(30);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if ((authorityType.indexOf("REPORTING") > -1) || (authorityType.indexOf("REVIEWING") > -1) || (authorityType.indexOf("ACCEPTING") > -1)) {

                    cell = new PdfPCell(new Phrase("Remarks of Reporting Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    ReportingHelperBean rhb = null;
                    for (int i = 0; i < paf.getReportingdata().size(); i++) {
                        rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
                        if (i > 0) {
                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                        }
                        if (rhb.getIscurrentreporting().equals("Y") || rhb.getIsreportingcompleted().equals("Y")) {
                            cell = new PdfPCell(new Phrase("1. Assessment of work output, attributes and functional competencies.(This should be on a relative scale of 1-5, with 1 referring to the lowest level and 5 to the highest level. Please indicate your rating for the officer against each item.)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setFixedHeight(40);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(a)  Attitude to work :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingattitude() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(f) Co-ordination ability :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingcoordination() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(b)  Sense of responsibility:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingresponsibility() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(g) Ability to work in a team:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getTeamworkrating() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(c)  Communication skill :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingcomskill() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingitskill() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(d)  Leadership Qualities :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingleadership() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(i) Initiative :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatinginitiative() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(e)  Decision-making ability :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingdecisionmaking() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(j) Quality of Work :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatequalityofwork() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("2. General Assessment (Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public:)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getAuthNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(ii) Assessment Of Performance Of 5t (20%):)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getFiveTChartertenpercent()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse )", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getInadequaciesNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("4. Integrity (If integrity is doubtful or  adverse please write \"Not certified\" in the space below and justify your remarks in box 4 above)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getIntegrityNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("5. Overall Grading", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + rhb.getSltGradingName(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("6. For  Overall Grading  \"Below Average\" /  \"Outstanding\"  please provide justification in the   space below.:", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getGradingNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReportingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + rhb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                        }
                    }
                }

                if ((authorityType.indexOf("REVIEWING") > -1) || (authorityType.indexOf("ACCEPTING") > -1)) {

                    cell = new PdfPCell(new Phrase("Remarks of Reviewing Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    ReviewingHelperBean rhb = null;
                    for (int i = 0; i < paf.getReviewingdata().size(); i++) {
                        rhb = (ReviewingHelperBean) paf.getReviewingdata().get(i);
                        if (rhb.getIscurrentreviewing() != null && (rhb.getIscurrentreviewing().equals("Y") || rhb.getIsreviewingcompleted().equals("Y"))) {
                            cell = new PdfPCell(new Phrase("1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getReviewingNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + rhb.getReviewGrading(), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReviewingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + rhb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                        }
                    }
                }

                if (authorityType.indexOf("ACCEPTING") > -1) {

                    cell = new PdfPCell(new Phrase("Remarks of Accepting Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    AcceptingHelperBean ahb = null;
                    for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
                        ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);
                        if (((ahb.getIscurrentaccepting() != null && ahb.getIscurrentaccepting().equals("Y"))) || ahb.getIsacceptingcompleted().equals("Y")) {
                            /*cell = new PdfPCell(new Phrase(ahb.getAcceptingauthName(),pafunc.getDesired_PDF_Font(9,false,true)));
                             cell.setColspan(4);
                             cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                             cell.setBorderWidth(1f);
                             table.addCell(cell);*/

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(ahb.getAcceptingNote()), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(ahb.getAcceptingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + ahb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                        }

                        cell = new PdfPCell();
                        cell.setColspan(4);
                        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                        cell.setBorderWidth(1f);
                        table.addCell(cell);
                    }
                }
            } else {
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(30);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public int getParid(String empid, int taskid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int parid = 0;
        try {
            con = dataSource.getConnection();

            String sql = "SELECT PARID FROM PAR_MASTER WHERE TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, taskid);

            rs = pst.executeQuery();
            if (rs.next()) {
                parid = rs.getInt("PARID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parid;
    }

    @Override
    public int saveAcceptingAuthRemarksFromCombo(String empid, int parid, int taskid, String remarks
    ) {

        Connection con = null;

        PreparedStatement pst = null;

        int retVal = 0;

        Date curdate = new Date();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("UPDATE PAR_ACCEPTING_TRAN SET REMARK=?,IS_COMPLETED=?,SUBMITTED_ON=? WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?");
            pst.setString(1, remarks);
            pst.setString(2, "Y");
            pst.setTimestamp(3, new Timestamp(curdate.getTime()));
            pst.setInt(4, parid);
            pst.setString(5, empid);
            retVal = pst.executeUpdate();

            if (retVal > 0) {
                startWorkFlow(con, parid, taskid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public List getNRCReasonList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List nrcreasonlist = new ArrayList();
        G_NRC_PAR_Reason nrcreason = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT REASON_ID,REASON FROM G_PAR_NRC_REASON ORDER BY REASON_ID ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                nrcreason = new G_NRC_PAR_Reason();
                nrcreason.setReasonid(rs.getString("REASON_ID"));
                nrcreason.setReason(rs.getString("REASON"));
                nrcreasonlist.add(nrcreason);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nrcreasonlist;
    }

    @Override
    public List getNRCReasonListForMoreThan4Month() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List nrcreasonlist = new ArrayList();
        G_NRC_PAR_Reason nrcreason = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT REASON_ID,REASON FROM G_PAR_NRC_REASON WHERE REASON_ID in ('07','08')";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                nrcreason = new G_NRC_PAR_Reason();
                nrcreason.setReasonid(rs.getString("REASON_ID"));
                nrcreason.setReason(rs.getString("REASON"));
                nrcreasonlist.add(nrcreason);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nrcreasonlist;
    }

    @Override
    public String getNRCAttachedFileName(String empid, int parid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String filename = "";

        try {
            con = dataSource.getConnection();

            String sql = "SELECT ORG_FILE_NAME FROM PAR_NRC_DOCUMENT"
                    + " INNER JOIN PAR_MASTER ON PAR_NRC_DOCUMENT.PAR_ID=PAR_MASTER.PARID WHERE PAR_ID=? AND PAR_MASTER.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                filename = rs.getString("ORG_FILE_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return filename;
    }

    @Override
    public ParDetail getNRCDetails(String empid, int parid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParDetail paf = new ParDetail();
        try {
            con = this.repodataSource.getConnection();

            String sql = "select par_master.*,DISK_FILE_NAME,ORG_FILE_NAME,FILE_TYPE,cadre_name,REASON from (select * from par_master where parid=? and emp_id=?)par_master"
                    + " left outer join PAR_NRC_DOCUMENT on par_master.PARID=PAR_NRC_DOCUMENT.PAR_ID"
                    + " left outer join  G_PAR_NRC_REASON on par_master.nrcreason = G_PAR_NRC_REASON.reason_id"
                    + " inner JOIN G_CADRE ON par_master.CADRE_CODE=G_CADRE.CADRE_CODE";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                paf.setParid(rs.getInt("PARID"));
                paf.setParPeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_FROM")));
                paf.setParPeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("PERIOD_TO")));
                paf.setNrcattchname(rs.getString("ORG_FILE_NAME"));
                paf.setFiscalYear(rs.getString("FISCAL_YEAR"));
                paf.setApprisespc(rs.getString("SPC"));
                paf.setApprisespn(getEmpPost(con, parid));
                paf.setEmpOffice(getEmpOffice(con, parid));
                //paf.setSltGroupOfPost(rs.getString("POST_GROUP"));
                paf.setNrcreason(rs.getString("NRCREASON"));
                paf.setSltHeadQuarter(rs.getString("HEADQUARTER"));
                paf.setNrcremarks(rs.getString("REMARKS"));
                paf.setSubmitedonNRC(rs.getString("nrc_submitted_on"));
                paf.setPostGroupAppraise(rs.getString("POST_GROUP"));
                paf.setCadreNameAppraise(rs.getString("cadre_name"));
                paf.setNrcReasonDetail(rs.getString("REASON"));

            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT EMP_MAST.*,CADRE_NAME FROM (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,"
                    + " CUR_CADRE_CODE,POST_GRP_TYPE,CUR_CADRE_NAME,CUR_SPC,DOB,CUR_OFF_CODE FROM EMP_MAST"
                    + " WHERE EMP_ID=?)EMP_MAST LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                paf.setApplicantempid(empid);
                paf.setApplicant(rs.getString("EMPNAME"));
                paf.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                paf.setEmpGroup(rs.getString("POST_GRP_TYPE"));
                paf.setEmpService(rs.getString("CADRE_NAME"));
                //paf.setHidSpcCode(rs.getString("CUR_SPC"));
                //paf.setEmpOffice(rs.getString("CUR_OFF_CODE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return paf;
    }

    public String getEmpPost(Connection con, int parid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String authorityPost = null;

        try {

            /*pst = con.prepareStatement("SELECT POST FROM (SELECT GPC FROM G_SPC WHERE SPC=?)GSPC INNER JOIN G_POST ON "
             + "GSPC.GPC=G_POST.POST_CODE");*/
            String sql = "SELECT POST FROM (SELECT SPC FROM PAR_MASTER WHERE PARID=?)PAR_MASTER"
                    + " INNER JOIN G_SPC ON PAR_MASTER.SPC=G_SPC.SPC"
                    + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authorityPost = rs.getString("POST");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityPost;
    }

    @Override
    public void viewNRCPDFfunc(Document document, ParDetail paf, String empid) {

        try {
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(2);
            table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("REQUESTED FOR NRC", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year: " + StringUtils.defaultString(paf.getFiscalYear()), getDesired_PDF_Font(11, true, false)));
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From Date: " + StringUtils.defaultString(paf.getParPeriodFrom()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To Date: " + StringUtils.defaultString(paf.getParPeriodTo()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("HRMS ID", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getApplicantempid(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Full name of the officer", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getApplicant(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date of birth", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getDob(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (paf.getCadreNameAppraise() != null && !paf.getCadreNameAppraise().equals("")) {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getCadreNameAppraise()), f1));
            } else {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpService()), f1));
            }
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group to which the officer belongs", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (paf.getPostGroupAppraise() != null && !paf.getPostGroupAppraise().equals("")) {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getPostGroupAppraise()), f1));
            } else {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpGroup()), f1));
            }
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation during the period of report", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getApprisespn()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Office to which posted", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getEmpOffice()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Head Quarter(if any)", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(paf.getSltHeadQuarter()), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reason for NRC", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(getNRCReasonforDisplay(paf.getNrcreason())), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Remarks", getDesired_PDF_Font(9, true, false)));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getNrcremarks()), f1));
            cell.setColspan(2);
            cell.setMinimumHeight(50f);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date Of Submission", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + paf.getSubmitedonNRC(), f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String getNRCReasonforDisplay(String id) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String nrcstring = "";
        try {
            con = dataSource.getConnection();

            String sql = "SELECT REASON FROM G_PAR_NRC_REASON WHERE REASON_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                nrcstring = rs.getString("REASON");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nrcstring;
    }

    @Override
    public FileAttribute downloadachievementattachment(String filepath, int attid, String fiscalyear) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT * FROM HW_ATTACHMENTS WHERE ATT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, attid);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filepath + fiscalyear + "/" + rs.getString("DISK_FILE_NAME");
                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("DISK_FILE_NAME"));
                    fa.setOriginalFileName(rs.getString("ORIGINAL_FILENAME"));
                    fa.setFileType(rs.getString("FILE_TYPE"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public FileAttribute downloadNRCAttachment(int parid, String fiscalyear, String filepath) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT * FROM PAR_NRC_DOCUMENT WHERE PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filepath + fiscalyear + "/" + rs.getString("DISK_FILE_NAME");
                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("DISK_FILE_NAME"));
                    fa.setOriginalFileName(rs.getString("ORG_FILE_NAME"));
                    fa.setFileType(rs.getString("FILE_TYPE"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public String revertPAR(String loginempid, ParDetail parDetail) {

        Connection con = null;

        PreparedStatement pst1 = null;
        ResultSet rs = null;

        PreparedStatement pst = null;

        boolean retVal = false;

        String appraiseEmpid = "";
        String appraiseSpc = "";
        int logid = 0;
        String startTime = "";
        String revertAuth = "";
        try {
            con = dataSource.getConnection();

            String sql = "SELECT EMP_ID,SPC FROM PAR_MASTER WHERE PARID=?";
            pst1 = con.prepareStatement(sql);
            pst1.setInt(1, parDetail.getParid());
            rs = pst1.executeQuery();
            if (rs.next()) {
                appraiseEmpid = rs.getString("EMP_ID");
                appraiseSpc = rs.getString("SPC");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst1);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            if (parDetail.getParstatus() == 6) {

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 16);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "REPORTING AUTHORITY");
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND REPORTING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst.setInt(1, 16);//PAR IS RETURNED BY REPORTING AUTHORITY
                pst.setString(2, appraiseEmpid);
                pst.setString(3, appraiseEmpid);
                pst.setString(4, appraiseSpc);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 16);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                parDetail.setRevertdone("Y");
                revertAuth = "Reporting Authority";

            } else if (parDetail.getParstatus() == 7) {
                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND REVIEWING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_REVIEWING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=?");
                pst.setInt(1, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 18);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,APPLY_TO=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setInt(4, 18);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 18);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "REVIEWING AUTHORITY");
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                parDetail.setRevertdone("Y");
                revertAuth = "Reviewing Authority";
            } else if (parDetail.getParstatus() == 8) {
                int hierarchy = 0;
                sql = "SELECT HIERARCHY_NO FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND ACCEPTING_EMP_ID=?";
                pst1 = con.prepareStatement(sql);
                pst1.setInt(1, parDetail.getParid());
                pst1.setString(2, loginempid);
                rs = pst1.executeQuery();
                if (rs.next()) {
                    hierarchy = rs.getInt("HIERARCHY_NO");
                }

                DataBaseFunctions.closeSqlObjects(rs, pst1);

                pst = con.prepareStatement("DELETE FROM PAR_ACCEPTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO >= ?");
                pst.setInt(1, parDetail.getParid());
                pst.setInt(2, hierarchy);
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE PAR_MASTER SET PAR_STATUS=?,REF_ID_OF_TABLE=? WHERE PARID=?");
                pst.setInt(1, 19);
                pst.setInt(2, 0);
                pst.setInt(3, parDetail.getParid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,APPLY_TO=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setInt(4, 19);
                pst.setInt(5, parDetail.getTaskid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, parDetail.getParid());
                pst.setString(2, loginempid);
                pst.setString(3, appraiseSpc);
                pst.setString(4, appraiseEmpid);
                pst.setString(5, parDetail.getRevertremarks());
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, parDetail.getTaskid());
                pst.setInt(9, 19);
                pst.setString(10, "PAR_REVERT");
                pst.setString(11, "ACCEPTING AUTHORITY");
                pst.executeUpdate();

                parDetail.setRevertdone("Y");
                revertAuth = "Accepting Authority";
            }
            if (parDetail.getRevertdone() != null && !parDetail.getRevertdone().equals("")) {
                if (parDetail.getRevertdone().equals("Y")) {
                    String mobile = "";
                    String eol = System.getProperty("line.separator");

                    String msg = "Your PAR has been reverted back for the year " + parDetail.getFiscalYear() + " from " + revertAuth + " on " + startTime + ".HRMS Odisha";

                    pst = con.prepareStatement("SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=?");
                    pst.setString(1, appraiseEmpid);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        mobile = rs.getString("MOBILE");
                    }

                    if (mobile != null && !mobile.equals("")) {
                        SMSThread smsthread = new SMSThread(appraiseEmpid, mobile, "RP");
                        Thread t1 = new Thread(smsthread);
                        t1.start();
                        new SMSServices(mobile, msg, "1407169287446522105");
                    }

                    pst = con.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
                    pst.setInt(1, CommonFunctions.getMaxCode(con, "SMS_LOG", "MSG_ID"));
                    pst.setString(2, parDetail.getParid() + "");
                    pst.setString(3, msg);
                    pst.setString(4, "REVERT PAR");
                    pst.setString(5, mobile);
                    pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setString(7, "");
                    pst.executeUpdate();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parDetail.getRevertdone();
    }

    @Override
    public ParSubmitForm getAuthorityInfo(int parid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParSubmitForm psubmtfrm = new ParSubmitForm();

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT TASK_ID,PAR_STATUS,EMP_ID,PERIOD_FROM,PERIOD_TO,GETPOSTNAMEFROMSPC(SPC) APPRISESPN,IS_AUTH_SET FROM PAR_MASTER WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                /*psubmtfrm.setTxtEmpId(rs.getString("EMP_ID"));
                 psubmtfrm.setHidParId(parId);
                 psubmtfrm.setHidparstatus(rs.getString("PAR_STATUS"));
                 psubmtfrm.setHidtaskId(rs.getString("TASK_ID"));
                 psubmtfrm.setApprisespn(rs.getString("APPRISESPN"));
                 psubmtfrm.setApprisalfromdate(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_FROM")));
                 psubmtfrm.setApprisaltodate(CommonFunctions.getFormattedOutputDate(rs.getDate("PERIOD_TO")));
                 psubmtfrm.setIsauthset(rs.getString("IS_AUTH_SET"));*/

                psubmtfrm.setReportList(getParAuthority(con, "REPORTING", parid, rs.getInt("TASK_ID")));
                psubmtfrm.setReviewList(getParAuthority(con, "REVIEWING", parid, rs.getInt("TASK_ID")));
                psubmtfrm.setAcceptList(getParAuthority(con, "ACCEPTING", parid, rs.getInt("TASK_ID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return psubmtfrm;
    }

    @Override
    public String[] getRevertReason(int parid, int taskid) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;

        String[] revertreson = new String[4];
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "SELECT NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM("
             + "SELECT WORKfLOW_LOG.*,ROWNUM rn FROM"
             + "(SELECT TASK_ID,NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM WORKfLOW_LOG"
             + "WHERE REF_ID='" + parid + "' AND TASK_ID='" + taskid + "' ORDER BY LOG_ID DESC)WORKfLOW_LOG)"
             + "WHERE RN=1";*/
            String sql = "SELECT TASK_ID,NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM"
                    + " (SELECT TASK_ID,NOTE,ACTION_TAKEN_BY,AUTHORITY_TYPE,TASK_ACTION_DATE FROM WORKfLOW_LOG"
                    + " WHERE REF_ID=? AND TASK_ID=?)WORKfLOW_LOG LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setInt(2, taskid);
            rs = pst.executeQuery();
            if (rs.next()) {
                revertreson[0] = rs.getString("AUTHORITY_TYPE");
                revertreson[1] = rs.getString("NOTE");
                String empId = rs.getString("ACTION_TAKEN_BY");
                revertreson[3] = rs.getString("TASK_ACTION_DATE");

                if (empId != null) {
                    if (empId.length() > 2) {
                        pst1 = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME from emp_mast where emp_id=?");
                        pst1.setString(1, empId);
                        rs = pst1.executeQuery();

                        if (rs.next()) {
                            revertreson[2] = rs.getString("FULL_NAME");
                        }
                    } else if (empId.length() <= 2) {
                        pst1 = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME from LA_MEMBERS where lmid=?");
                        pst1.setString(1, empId);
                        rs = pst1.executeQuery();
                        if (rs.next()) {
                            revertreson[2] = rs.getString("FULL_NAME");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revertreson;
    }

    public String EmpName(String empId, Connection con) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;
        String empname = "";
        try {
            if (empId != null) {
                if (empId.length() > 2) {
                    pst = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME from emp_mast where emp_id=?");
                    pst.setString(1, empId);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        empname = rs.getString("fullname");
                    }
                } else if (empId.length() <= 2) {
                    pst = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') FULL_NAME from LA_MEMBERS where lmid=?");
                    pst.setString(1, empId);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        empname = rs.getString("fullname");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    public String getEmpOffice(Connection con, int parid) throws Exception {

        PreparedStatement pst = null;
        ResultSet rs = null;
        String authorityOff = null;
        try {
            /*pst = con.prepareStatement("SELECT OFF_EN FROM(SELECT CUR_OFF_CODE FROM EMP_MAST WHERE CUR_SPC=?) "
             + "EMPMAST INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");*/
            String sql = "SELECT OFF_EN FROM"
                    + " (SELECT OFF_CODE FROM PAR_MASTER WHERE PARID=?)PAR_MASTER"
                    + " INNER JOIN G_OFFICE ON PAR_MASTER.OFF_CODE=G_OFFICE.OFF_CODE";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authorityOff = rs.getString("OFF_EN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityOff;
    }

    @Override
    public String isFiscalYearClosed(String fyear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isclosed = "";
        try {
            con = dataSource.getConnection();

            String sql = "SELECT IS_CLOSED FROM FINANCIAL_YEAR WHERE FY=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fyear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("IS_CLOSED") != null && !rs.getString("IS_CLOSED").equals("")) {
                    isclosed = rs.getString("IS_CLOSED");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isclosed;
    }

    @Override
    public String isFiscalYearClosed4Si(String fyear) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String isclosed = "";

        try {
            con = dataSource.getConnection();

            String sql = "SELECT si_is_closed FROM FINANCIAL_YEAR WHERE FY=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fyear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("si_is_closed") != null && !rs.getString("si_is_closed").equals("")) {
                    isclosed = rs.getString("si_is_closed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isclosed;
    }

    @Override
    public void deleteNRC(int parid, String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM PAR_MASTER WHERE PARID=? AND EMP_ID=?");
            pstmt.setInt(1, parid);
            pstmt.setString(2, empId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String isAchievementDataPresent(int parid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDataPresent = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM PAR_ACHIEVEMENT WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                isDataPresent = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDataPresent;
    }

    @Override
    public String isOtherDetailsPresent(int parid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDataPresent = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM PAR_APPRAISEE_TRAN WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ParOtherDetails parOtherDetails = new ParOtherDetails();
                if (rs.getString("SELFAPPRAISAL") != null && !rs.getString("SELFAPPRAISAL").equals("")) {
                    isDataPresent = "Y";
                }
                if (rs.getString("SPECIALCONTRIBUTIION") != null && !rs.getString("SPECIALCONTRIBUTIION").equals("")) {
                    isDataPresent = "Y";
                }
                if (rs.getString("HINDERREASON") != null && !rs.getString("HINDERREASON").equals("")) {
                    isDataPresent = "Y";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDataPresent;
    }

    @Override
    public int deletePAR(String parid, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM PAR_ABSENTEE WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_ACHIEVEMENT WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_APPRAISEE_TRAN WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_MASTER WHERE EMP_ID=? AND PARID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(parid));
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    public int deletePARForReportingRevert(String parid, String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();
            String sql = "select count(*) FROM PAR_REPORTING_TRAN WHERE PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeQuery();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_ABSENTEE WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_ACHIEVEMENT WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_APPRAISEE_TRAN WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parid));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM PAR_MASTER WHERE EMP_ID=? AND PARID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(parid));
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public boolean isPARReverted(int parid, String empid) {

        Connection con = null;

        boolean isReverted = false;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT PARID,EMP_ID FROM (SELECT PARID,EMP_ID FROM PAR_MASTER WHERE PARID=? AND EMP_ID=?)PAR_MASTER"
                    + " INNER JOIN WORKFLOW_LOG ON PAR_MASTER.PARID=WORKFLOW_LOG.REF_ID";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("EMP_ID") != null && !rs.getString("EMP_ID").equals("")) {
                    isReverted = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isReverted;
    }

    @Override
    public List getPARReport(String fiscalyear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PARReportBean prbean = null;

        List parreportlist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT DEPARTMENT_CODE,DEPARTMENT_NAME FROM G_DEPARTMENT WHERE IF_ACTIVE='Y' ORDER BY DEPARTMENT_NAME ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                prbean = new PARReportBean();
                prbean.setDeptname(rs.getString("DEPARTMENT_NAME"));

                String deptcode = rs.getString("DEPARTMENT_CODE");

                String sql1 = "SELECT getsubmittedparcount('" + fiscalyear + "','" + deptcode + "') spar,getpendingreportingparcount('" + fiscalyear + "','" + deptcode + "') rppar,getpendingreviewingparcount('" + fiscalyear + "','" + deptcode + "') rvpar,getpendingacceptingparcount('" + fiscalyear + "','" + deptcode + "') acpar,getcompletedparcount('" + fiscalyear + "','" + deptcode + "') cpar";
                pst1 = con.prepareStatement(sql1);
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    prbean.setParapplied(rs1.getInt("spar"));
                    prbean.setReportingpending(rs1.getInt("rppar"));
                    prbean.setReviewingpending(rs1.getInt("rvpar"));
                    prbean.setAcceptingpending(rs1.getInt("acpar"));
                    prbean.setCompleted(rs1.getInt("cpar"));
                }
                parreportlist.add(prbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parreportlist;
    }

    public void sendSMStoAuthafterSubmit(Connection con, int parid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String fiscal_year = "";
        String appraiseeName = "";
        String reporting_emp_id = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());
        try {
            String sql = "SELECT FISCAL_YEAR,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME FROM PAR_MASTER"
                    + " INNER JOIN EMP_MAST ON PAR_MASTER.EMP_ID=EMP_MAST.EMP_ID WHERE PARID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                fiscal_year = rs.getString("FISCAL_YEAR");
                appraiseeName = rs.getString("FULL_NAME");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT REPORTING_EMP_ID FROM PAR_REPORTING_TRAN WHERE PAR_ID=? AND HIERARCHY_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, parid);
            pst.setInt(2, 1);
            rs = pst.executeQuery();
            if (rs.next()) {
                reporting_emp_id = rs.getString("REPORTING_EMP_ID");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, reporting_emp_id);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("MOBILE") != null && !rs.getString("MOBILE").equals("")) {
                    String msg = "PAR of " + appraiseeName + " for " + fiscal_year + " has been submitted at your end for remarks";

                    //SMSHttpPostClient smhttp = new SMSHttpPostClient(rs.getString("MOBILE"), msg);
                    //String deliverymsg = smhttp.send_sms();
                    SMSServices smhttp = new SMSServices(rs.getString("MOBILE"), msg, "1407161639804053058");

                    pst = con.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
                    pst.setInt(1, CommonFunctions.getMaxCode(con, "SMS_LOG", "MSG_ID"));
                    pst.setString(2, reporting_emp_id);
                    pst.setString(3, msg);
                    pst.setString(4, "FORWARD PAR/AUTHORITY/AFTER SUBMIT");
                    pst.setString(5, rs.getString("MOBILE"));
                    pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
                    pst.setString(7, "");
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void sendSMStoAppraisee(int parid, String authType, int hierarchyno, String fiscalyear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String msg = "";
        String eol = System.getProperty("line.separator");
        String mobile = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            if (!authType.equals("Custodian")) {
                msg = "PAR for " + fiscalyear + " has been forwarded to " + ordinal(hierarchyno) + " " + authType + " Authority.";
            } else {
                msg = "PAR for " + fiscalyear + " is completed";
            }
            msg = msg + eol + " hrmsodisha.gov.in";

            /* pst = con.prepareStatement("SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=(SELECT EMP_ID FROM PAR_MASTER WHERE PARID=?)");
             pst.setInt(1, parid);
             rs = pst.executeQuery();
             if (rs.next()) {
             mobile = rs.getString("MOBILE");
             }*/

            /*if (mobile != null && !mobile.equals("")) {

             //SMSHttpPostClient smhttp = new SMSHttpPostClient(mobile, msg);
             //String deliverymsg = smhttp.send_sms();
             SMSServices smhttp = new SMSServices(mobile, msg);

             pst = con.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
             pst.setInt(1, CommonFunctions.getMaxCode(con, "SMS_LOG", "MSG_ID"));
             pst.setString(2, parid + "");
             pst.setString(3, msg);
             pst.setString(4, "FORWARD PAR/APPRAISEE");
             pst.setString(5, mobile);
             pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
             pst.setString(7, "");
             pst.executeUpdate();
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void sendSMStoAuthority(int parid, String prevAuthType, String nextAuthEmpId, String fiscalyear, int prevhierarchyno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String mobile = "";
        String msg = "";
        String eol = System.getProperty("line.separator");
        String appraiseeName = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());
        try {
            if (nextAuthEmpId != null && !nextAuthEmpId.equals("") && !nextAuthEmpId.equals("Custodian")) {
                con = this.dataSource.getConnection();

                /*String sql = "SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=?";
                 pst = con.prepareStatement(sql);
                 pst.setString(1, nextAuthEmpId);
                 rs = pst.executeQuery();
                 if (rs.next()) {
                 mobile = rs.getString("MOBILE");
                 }

                 sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME FROM EMP_MAST WHERE EMP_ID=(SELECT EMP_ID FROM PAR_MASTER WHERE PARID=?)";
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, parid);
                 rs = pst.executeQuery();
                 if (rs.next()) {
                 appraiseeName = rs.getString("FULL_NAME");
                 }

                 msg = ordinal(prevhierarchyno) + " " + prevAuthType + " Authority has forwarded PAR of " + appraiseeName + " for " + fiscalyear + " at your end for giving remarks";
                 msg = msg + eol + " hrmsodisha.gov.in";
                 */

                /*if (mobile != null && !mobile.equals("")) {
                 //SMSHttpPostClient smhttp = new SMSHttpPostClient(mobile, msg);
                 //String deliverymsg = smhttp.send_sms();
                 SMSServices smhttp = new SMSServices(mobile, msg);

                 pst = con.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
                 pst.setInt(1, CommonFunctions.getMaxCode(con, "SMS_LOG", "MSG_ID"));
                 pst.setString(2, parid + "");
                 pst.setString(3, msg);
                 pst.setString(4, "FORWARD PAR/AUTHORITY");
                 pst.setString(5, mobile);
                 pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
                 pst.setString(7, "");
                 pst.executeUpdate();
                 }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public static String ordinal(int i) {
        int mod100 = i % 100;
        int mod10 = i % 10;
        if (mod10 == 1 && mod100 != 11) {
            return i + "st";
        } else if (mod10 == 2 && mod100 != 12) {
            return i + "nd";
        } else if (mod10 == 3 && mod100 != 13) {
            return i + "rd";
        } else {
            return i + "th";
        }
    }

    @Override
    public String isAuthRemarksClosed(String fyear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isClosed = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT AUTH_REMARKS_CLOSED FROM FINANCIAL_YEAR WHERE FY=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fyear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("AUTH_REMARKS_CLOSED") != null && !rs.getString("AUTH_REMARKS_CLOSED").equals("")) {
                    isClosed = rs.getString("AUTH_REMARKS_CLOSED");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isClosed;
    }

    @Override
    public boolean isAuthorizedtoDownloadPAR(String loggedinEmpid, int parid) {

        Connection con = null;

        boolean authorized = false;

        String authType = "";

        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            authType = findAuthorizedAuthorityType(loggedinEmpid, parid);
            if (authType == null || authType.equals("")) {
                sql = "SELECT * FROM PAR_MASTER WHERE PARID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, parid);
                pst.setString(2, loggedinEmpid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    authType = "APPRAISEE";
                }
            }

            if (authType == null || authType.equals("")) {
                authorized = false;
            } else if (authType.equalsIgnoreCase("APPRAISEE")) {
                authorized = true;
            } else if (authType.equalsIgnoreCase("REPORTING")) {
                authorized = true;
            } else if (authType.equalsIgnoreCase("REVIEWING")) {
                authorized = true;
            } else if (authType.equalsIgnoreCase("ACCEPTING")) {
                authorized = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorized;
    }

    private String findAuthorizedAuthorityType(String empid, int parid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String authority = "";
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT REPORTING_EMP_ID FROM PAR_REPORTING_TRAN WHERE REPORTING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authority = "REPORTING";
            }

            sql = "SELECT REVIEWING_EMP_ID FROM PAR_REVIEWING_TRAN WHERE REVIEWING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();
            if (rs.next()) {
                authority = "REVIEWING";
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT ACCEPTING_EMP_ID FROM PAR_ACCEPTING_TRAN WHERE ACCEPTING_EMP_ID=? AND PAR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, parid);
            rs = pst.executeQuery();

            if (rs.next()) {
                authority = "ACCEPTING";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    private String getLAMemberDeptName(Connection con, int lmid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String deptNames = "";
        try {
            String sql = "SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE LMID=? ORDER BY DEPARTMENT_NAME ASC";
            pst = con.prepareStatement(sql);
            pst.setInt(1, lmid);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (deptNames.equals("")) {
                    deptNames = rs.getString("DEPARTMENT_NAME");
                } else {
                    deptNames = deptNames + "," + rs.getString("DEPARTMENT_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            //DataBaseFunctions.closeSqlObjects(con);
        }
        return deptNames;
    }

    @Override
    public ArrayList getEmployeeListforInitiateOtherPAR(String offcode, String fiscalYear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        InitiateOtherPARListBean ibean = null;
        try {
            con = this.repodataSource.getConnection();
            if (offcode.equals("")) {
                offcode = null;
            }

            String[] yeartemp = fiscalYear.split("-");
            int year1 = Integer.parseInt(yeartemp[0]);
            int year2 = year1 + 1;

            String finyear1 = "01-APR-" + year1;
            String finyear2 = "31-MAR-" + year2;

            /*String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from emp_mast"
             + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where cur_off_code=? and dep_code='02' and is_regular='Y' and (post_grp_type='A' or post_grp_type='B') and"
             + " (if_retired is null or if_retired='N') order by f_name";*/
            String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn,F_NAME,(select count(*) cnt from par_master where emp_id=emp_mast.emp_id and fiscal_year=?) par_created from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where cur_off_code=? and dep_code='02' and is_regular='Y' and"
                    + " (post_grp_type='A' or post_grp_type='B') and (if_retired is null or if_retired='N')"
                    + " union"
                    + " SELECT EMP_RELIEVE.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,SPN,F_NAME,(select count(*) cnt from par_master where emp_id=emp_mast.emp_id and fiscal_year=?) par_created FROM EMP_MAST"
                    + " INNER JOIN"
                    + " (SELECT SPC,EMP_ID FROM EMP_RELIEVE WHERE RLV_DATE BETWEEN ? AND ? and SPC LIKE ?)EMP_RELIEVE ON"
                    + " emp_mast.emp_id=EMP_RELIEVE.emp_id"
                    + " LEFT OUTER JOIN G_SPC ON EMP_RELIEVE.SPC=G_SPC.SPC order by F_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, fiscalYear);
            pst.setString(2, offcode);
            pst.setString(3, fiscalYear);
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(finyear1).getTime()));
            pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(finyear2).getTime()));
            pst.setString(6, '%' + offcode + '%');
            rs = pst.executeQuery();
            while (rs.next()) {
                ibean = new InitiateOtherPARListBean();
                ibean.setEmpid(rs.getString("emp_id"));
                ibean.setEmpname(rs.getString("EMP_NAME"));
                ibean.setDesignation(rs.getString("spn"));
                ibean.setParCreated(rs.getString("par_created"));
                emplist.add(ibean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    /*@Override
     public int saveInitiateOtherPARRemarks(String loginid, String loginspc, InitiateOtherPARForm initiateOtherPARForm) {

     Connection con = null;

     PreparedStatement pst = null;
     ResultSet rs = null;

     int parid = 0;
     try {
     con = this.dataSource.getConnection();

     if (initiateOtherPARForm.getParid() == 0) {
     System.out.println("parid is 0 for initiate par ##########");
     pst = con.prepareStatement("insert into par_master (emp_id,fiscal_year,period_from,period_to,par_status,spc,cadre_code,off_code,post_group,headquarter,nrcreason,remarks,initiatedby) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
     pst.setString(1, initiateOtherPARForm.getAppraiseeEmpId());
     pst.setString(2, initiateOtherPARForm.getFiscalYear());
     pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(initiateOtherPARForm.getFromDate()).getTime()));
     pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(initiateOtherPARForm.getToDate()).getTime()));
     pst.setInt(5, 7);
     pst.setString(6, initiateOtherPARForm.getHidspc());
     pst.setString(7, initiateOtherPARForm.getHidcadrename());
     pst.setString(8, null);
     pst.setString(9, initiateOtherPARForm.getPostGroupType());
     pst.setString(10, null);
     pst.setString(11, null);
     pst.setString(12, null);
     pst.setString(13, loginid);
     pst.executeUpdate();

     rs = pst.getGeneratedKeys();
     rs.next();
     parid = rs.getInt("parid");

     } else {
     System.out.println("parid is not zero for initiate  par $$$$$$");
     parid = initiateOtherPARForm.getParid();
     }

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(rs, pst);
     DataBaseFunctions.closeSqlObjects(con);
     }
     return parid;
     } */
    @Override
    public int saveInitiateOtherPARRemarks(String loginid, String loginspc, InitiateOtherPARForm initiateOtherPARForm) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int parid = 0;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("insert into par_master (emp_id,fiscal_year,period_from,period_to,par_status,spc,cadre_code,off_code,post_group,headquarter,nrcreason,remarks,initiatedby) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, initiateOtherPARForm.getAppraiseeEmpId());
            pst.setString(2, initiateOtherPARForm.getFiscalYear());
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(initiateOtherPARForm.getFromDate()).getTime()));
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(initiateOtherPARForm.getToDate()).getTime()));
            pst.setInt(5, 7);
            pst.setString(6, initiateOtherPARForm.getHidspc());
            pst.setString(7, initiateOtherPARForm.getHidcadrename());
            pst.setString(8, null);
            pst.setString(9, initiateOtherPARForm.getPostGroupType());
            pst.setString(10, null);
            pst.setString(11, null);
            pst.setString(12, null);
            pst.setString(13, loginid);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            parid = rs.getInt("parid");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parid;
    }

    @Override
    public String submitInitiateOtherPar(String loginid, String loginspc, InitiateOtherPARForm initiateOtherPARForm) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String tempfrmdt = "";
        String temptodt = "";
        String invalidperiod = "N";
        try {
            con = dataSource.getConnection();
            for (int i = 0; i < initiateOtherPARForm.getHidReportingEmpId().length; i++) {
                if (initiateOtherPARForm.getHidReportingEmpId()[i] != null && !initiateOtherPARForm.getHidReportingEmpId()[i].equals("")) {
                    tempfrmdt = initiateOtherPARForm.getTxtReportingAuthFromDate()[i];
                    temptodt = initiateOtherPARForm.getTxtReportingAuthToDate()[i];
                    int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                    if (diffInDays >= 120) {

                        pst = con.prepareStatement("INSERT INTO PAR_REPORTING_TRAN (PAR_ID,REPORTING_CUR_SPC,REPORTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE,fivet_charter_tenpercent,fivet_charter_fivepercent,fivet_mosarkar)VALUES(?,?,?,?,?,?,?,?,?)");
                        pst.setInt(1, initiateOtherPARForm.getParid());
                        pst.setString(2, initiateOtherPARForm.getHidReportingSpcCode()[i]);
                        pst.setString(3, initiateOtherPARForm.getHidReportingEmpId()[i]);
                        pst.setInt(4, i + 1);
                        pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                        pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                        pst.setString(7, initiateOtherPARForm.getFiveTChartertenpercent());
                        pst.setString(8, initiateOtherPARForm.getFiveTCharterfivePercent());
                        pst.setString(9, initiateOtherPARForm.getFiveTComponentmoSarkar());
                        pst.executeUpdate();

                    } else if (diffInDays < 120) {
                        invalidperiod = "REP";
                    }
                }
            }

            DataBaseFunctions.closeSqlObjects(pst);

            for (int i = 0; i < initiateOtherPARForm.getHidReviewingEmpId().length; i++) {
                if (initiateOtherPARForm.getHidReviewingEmpId()[i] != null && !initiateOtherPARForm.getHidReviewingEmpId()[i].equals("")) {
                    tempfrmdt = initiateOtherPARForm.getTxtRevieiwingAuthFromDate()[i];
                    temptodt = initiateOtherPARForm.getTxtRevieiwingAuthToDate()[i];
                    int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                    //if (diffInDays >= 120) {                    
                    pst = con.prepareStatement("INSERT INTO PAR_REVIEWING_TRAN (PAR_ID,REVIEWING_CUR_SPC,REVIEWING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE)VALUES(?,?,?,?,?,?)");
                    pst.setInt(1, initiateOtherPARForm.getParid());
                    pst.setString(2, initiateOtherPARForm.getHidReviewingpcCode()[i]);
                    pst.setString(3, initiateOtherPARForm.getHidReviewingEmpId()[i]);
                    pst.setInt(4, i + 1);
                    pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                    pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                    pst.executeUpdate();
                }
            }

            DataBaseFunctions.closeSqlObjects(pst);

            for (int i = 0; i < initiateOtherPARForm.getHidAcceptingEmpId().length; i++) {
                if (initiateOtherPARForm.getHidAcceptingEmpId()[i] != null && !initiateOtherPARForm.getHidAcceptingEmpId()[i].equals("")) {
                    tempfrmdt = initiateOtherPARForm.getTxtAcceptingAuthFromDate()[i];
                    temptodt = initiateOtherPARForm.getTxtAcceptingAuthToDate()[i];
                    int diffInDays = (int) ((sdf.parse(temptodt).getTime() - sdf.parse(tempfrmdt).getTime()) / (1000 * 60 * 60 * 24));
                    //if (diffInDays >= 120) {
                    pst = con.prepareStatement("INSERT INTO PAR_ACCEPTING_TRAN (PAR_ID,ACCEPTING_CUR_SPC,ACCEPTING_EMP_ID,HIERARCHY_NO,FROMDATE,TODATE) VALUES(?,?,?,?,?,?)");
                    pst.setInt(1, initiateOtherPARForm.getParid());
                    pst.setString(2, initiateOtherPARForm.getHidAcceptingSpcCode()[i]);
                    pst.setString(3, initiateOtherPARForm.getHidAcceptingEmpId()[i]);
                    pst.setInt(4, i + 1);
                    pst.setTimestamp(5, new Timestamp(sdf.parse(tempfrmdt).getTime()));
                    pst.setTimestamp(6, new Timestamp(sdf.parse(temptodt).getTime()));
                    pst.executeUpdate();
                }
            }

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID,INITIATED_SPC) Values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, 3);
            pst.setString(2, loginid);
            pst.setTimestamp(3, new Timestamp(new Date().getTime()));
            pst.setInt(4, 6);
            pst.setString(5, loginspc);
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int taskid = rs.getInt("TASK_ID");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("UPDATE PAR_MASTER SET TASK_ID=? WHERE PARID=?");
            pst.setInt(1, taskid);
            pst.setInt(2, initiateOtherPARForm.getParid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE PAR_REPORTING_TRAN SET REPORTING_AUTH_NOTE=?,RATEQUALITYOFWORK=?,RATINGATTITUDE=?,RATINGCOMSKILL=?,RATINGCOORDINATION=?,"
                    + "RATINGDECISIONMAKING=?,RATINGINITIATIVE=?,RATINGITSKILL=?,RATINGLEADERSHIP=?,RATINGRESPONSIBILITY=?,TEAMWORKRATING=?,GRADE_ID=?,GRADINGNOTE=?,"
                    + "INADEQUACIESNOTE=?,INTEGRITYNOTE=?,IS_COMPLETED=?,SUBMITTED_ON=?,fivet_charter_tenpercent=?,fivet_charter_fivepercent=?,fivet_mosarkar=? WHERE PAR_ID=? AND REPORTING_EMP_ID=?");
            pst.setString(1, initiateOtherPARForm.getAuthNote());
            pst.setInt(2, initiateOtherPARForm.getRatequalityofwork());
            pst.setInt(3, initiateOtherPARForm.getRatingattitude());
            pst.setInt(4, initiateOtherPARForm.getRatingcomskill());
            pst.setInt(5, initiateOtherPARForm.getRatingcoordination());
            pst.setInt(6, initiateOtherPARForm.getRatingdecisionmaking());
            pst.setInt(7, initiateOtherPARForm.getRatinginitiative());
            pst.setInt(8, initiateOtherPARForm.getRatingitskill());
            pst.setInt(9, initiateOtherPARForm.getRatingleadership());
            pst.setInt(10, initiateOtherPARForm.getRatingresponsibility());
            pst.setInt(11, initiateOtherPARForm.getTeamworkrating());
            if (initiateOtherPARForm.getSltGrading() != null && !initiateOtherPARForm.getSltGrading().equals("")) {
                pst.setInt(12, Integer.parseInt(initiateOtherPARForm.getSltGrading()));
            } else {
                pst.setString(12, null);
            }
            pst.setString(13, initiateOtherPARForm.getGradingNote());
            pst.setString(14, initiateOtherPARForm.getInadequaciesNote());
            pst.setString(15, initiateOtherPARForm.getIntegrityNote());
            pst.setString(16, "Y");
            pst.setTimestamp(17, new Timestamp(new Date().getTime()));
            pst.setString(18, initiateOtherPARForm.getFiveTChartertenpercent());
            pst.setString(19, initiateOtherPARForm.getFiveTCharterfivePercent());
            pst.setString(20, initiateOtherPARForm.getFiveTComponentmoSarkar());
            pst.setInt(21, initiateOtherPARForm.getParid());
            pst.setString(22, loginid);
            pst.executeUpdate();

            startWorkFlow(con, initiateOtherPARForm.getParid(), taskid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return invalidperiod;
    }

    @Override
    public String getEmployeeName(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String empname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,SPN FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                empname = rs.getString("EMP_NAME");
                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    empname += ", " + rs.getString("SPN");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    @Override
    public String isInitiateOtherDuplicatePAR(InitiateOtherPARForm initiateOtherPARForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String parId = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM PAR_MASTER WHERE EMP_ID=? AND FISCAL_YEAR=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, initiateOtherPARForm.getAppraiseeEmpId());
            pst.setString(2, initiateOtherPARForm.getFiscalYear());
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("par_status") > 0) {
                    parId = "Y";
                }

                /* To get parid that have already initiated by appraisee(PAR Created but not submitted)
                 else if (rs.getInt("par_status") == 0) {
                 parId = rs.getString("parid");
                 }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parId;
    }

    @Override
    public SelectOption getPartialPARPeriod(String empid, String fiscalyear) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDuplicate = "N";
        String isFromDatePartialPeriodAvailable = "N";
        String isToDatePartialPeriodAvailable = "N";

        Date dbFromDate = null;
        Date dbToDate = null;

        SelectOption so = new SelectOption();
        try {
            con = this.dataSource.getConnection();

            String[] fyearArr = fiscalyear.split("-");
            int year1 = Integer.parseInt(fyearArr[0]);
            int year2 = year1 + 1;

            String tempfromDate = year1 + "-04-01";
            String temptoDate = year2 + "-03-31";

            Date tempfdate = new SimpleDateFormat("yyyy-MM-dd").parse(tempfromDate);
            Date temptdate = new SimpleDateFormat("yyyy-MM-dd").parse(temptoDate);

            String sql = "select period_from,period_to from par_master where EMP_ID=? AND FISCAL_YEAR=? ORDER BY period_from";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();

            while (rs.next()) {
                dbFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("period_from"));
                dbToDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("period_to"));

                if (tempfdate.compareTo(dbFromDate) == 0 && temptdate.compareTo(dbToDate) == 0) {
                    isDuplicate = "Y";
                } else {
                    long FromTimeDiff = dbFromDate.getTime() - tempfdate.getTime();
                    long FromDaysDiff = FromTimeDiff / (1000 * 60 * 60 * 24);
                    if (FromDaysDiff >= 120) {
                        isFromDatePartialPeriodAvailable = "Y";
                    } else {
                        isFromDatePartialPeriodAvailable = "N";
                    }
                    if (isFromDatePartialPeriodAvailable.equals("N")) {
                        long ToTimeDiff = temptdate.getTime() - dbToDate.getTime();
                        long ToDaysDiff = ToTimeDiff / (1000 * 60 * 60 * 24);
                        if (ToDaysDiff >= 120) {
                            isToDatePartialPeriodAvailable = "Y";
                        } else {
                            isToDatePartialPeriodAvailable = "N";
                        }
                    }
                }
            }
            if (isFromDatePartialPeriodAvailable.equals("Y")) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dbFromDate);
                cal.add(Calendar.DATE, -1);
                Date partialToDate = cal.getTime();

                String tempPartialToDate = new SimpleDateFormat("yyyy-MM-dd").format(partialToDate);
                //partialToDate = new SimpleDateFormat("yyyy-MM-dd").parse(tempPartialToDate);
                String tempdbFromDate = new SimpleDateFormat("yyyy-MM-dd").format(tempfdate);

                so.setValue(tempdbFromDate);
                so.setLabel(tempPartialToDate);
            } else if (isToDatePartialPeriodAvailable.equals("Y")) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dbToDate);
                cal.add(Calendar.DATE, 1);
                Date partialFromDate = cal.getTime();

                String tempPartialFromDate = new SimpleDateFormat("yyyy-MM-dd").format(partialFromDate);
                partialFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(tempPartialFromDate);

                so.setValue(tempPartialFromDate);
                so.setLabel(temptoDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return so;
    }

    @Override
    public void downloadPARDetailPDF(Document document, String empid, ParDetail paf, String filePath) {

        try {

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPTable innertable = null;

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            PdfPCell innercell = null;

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for Group A and B officers of Govt. of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission Record", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by Appraisee )", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("Financial Year : ", f1);
            Chunk c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            Chunk c3 = new Chunk(" (for the period from ", f1);
            Chunk c4 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c5 = new Chunk(" to ", f1);
            Chunk c6 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            Chunk c7 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Name & Designation of the Officer Reported Upon     ", f1);
            c2 = new Chunk(paf.getApplicant(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Service and Group (A/B) to which the  Officer belongs ", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group - " + StringUtils.defaultString(paf.getEmpGroup()), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Details of Transmission / Movement of PAR", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in at the time of transmission", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("by respective officer/staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Transmission by", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Transmitted to whom (Name, Designation &Address)", f1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            /*cell = new PdfPCell(new Phrase("Letter No & Date of\nTransmission", f1));
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("Signature of\nOfficer/Staff\nTransmitting the PAR", f1));
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Appraisee", f1));
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table.addCell(cell);
            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
             table.addCell(cell);*/

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
            table.addCell(cell);
            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("", f1));
             cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
             table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Reporting\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            ArrayList reportAuthlist = paf.getReportingauth();
            Parauthorityhelperbean parhelper = null;
            String reportAuthName = "";
            String reportAuthDesg = "";
            int slno = 0;
            for (int i = 0; i < reportAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reportAuthlist.get(i);
                slno = i + 1;
                if (reportAuthName == "") {
                    reportAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reportAuthName = reportAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reportAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList reviewAuthlist = paf.getReviewingauth();
            parhelper = null;
            String reviewAuthName = "";
            String reviewAuthDesg = "";
            slno = 0;
            for (int i = 0; i < reviewAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) reviewAuthlist.get(i);
                slno = i + 1;
                if (reviewAuthName == "") {
                    reviewAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    reviewAuthName = reviewAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //reviewAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Reviewing\nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            ArrayList acceptAuthlist = paf.getAcceptingauth();
            parhelper = null;
            String acceptAuthName = "";
            String acceptAuthDesg = "";
            slno = 0;
            for (int i = 0; i < acceptAuthlist.size(); i++) {
                parhelper = (Parauthorityhelperbean) acceptAuthlist.get(i);
                slno = i + 1;
                if (acceptAuthName == "") {
                    acceptAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                } else {
                    acceptAuthName = acceptAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
                }
                //acceptAuthDesg = parhelper.getAuthorityspn();
            }
            cell = new PdfPCell(new Phrase("Accepting \nAuthority", f1));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(3);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            //Second Page
            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("PERFORMANCE APPRAISAL REPORT", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("for", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group A and Group B Officers of Govt. of Odisha.", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Report for the financial year ", f1);
            c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("(  Period from ", f1);
            c2 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" to ", f1);
            c4 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c5 = new Chunk(")", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL DATA", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "PART-I", f1));
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 33) + "(To be filled in by the Appraisee )", f1));
            cell.setColspan(3);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Full Name of the Officer:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApplicant(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            String url = filePath + paf.getApplicantempid() + ".jpg";
            File f = null;
            f = new File(url);
            Image img1 = null;
            if (f.exists()) {
                img1 = Image.getInstance(url);
            }
            if (img1 != null) {
                //img1.scalePercent(15f);
                img1.scaleToFit(100f, 80f);
                cell = new PdfPCell(img1);
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setRowspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(" 2. Date of Birth:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getDob(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            //cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3. Service to which the Officer belongs:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 4. Group to which the Officer belongs(A or B):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getEmpGroup(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 5. Designation  during the period of Report:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 6. Office to which posted with Head Quarters:", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            String office = "";
            if (paf.getSltHeadQuarter() != null && !paf.getSltHeadQuarter().equals("")) {
                office = paf.getEmpOffice() + "," + paf.getSltHeadQuarter();
            } else {
                office = paf.getEmpOffice();
            }
            cell = new PdfPCell(new Phrase(office, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 7. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            AbsenteeBean pab = null;
            ArrayList leaveAbsentee = paf.getLeaveAbsentee();
            String leaveFrmDt = "";
            String leaveToDt = "";
            String leaves = "";
            for (int i = 0; i < leaveAbsentee.size(); i++) {
                pab = (AbsenteeBean) leaveAbsentee.get(i);
                innertable = new PdfPTable(2);
                innertable.setWidths(new int[]{2, 2});
                innertable.setWidthPercentage(100);
                //innercell = new
                if (pab.getFromDate() != null && !pab.getFromDate().equals("")) {
                    if (leaves == "") {
                        leaves = "From: " + pab.getFromDate() + " to: " + pab.getToDate();
                    } else {
                        leaves = leaves + "\nFrom: " + pab.getFromDate() + " to: " + pab.getToDate();
                    }
                }
                //leaveFrmDt = pab.getFromDate();
                //leaveToDt = pab.getToDate();
            }
            if (leaves != "") {
                cell = new PdfPCell(new Phrase(leaves, f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Nil", f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 8. Name & Designation of the Reporting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 9. Name & Designation of the Reviewing Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 10. Name & Designation of the Accepting Authority\n    and period worked under him/her:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(paf.getApplicant(), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of the Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            //Third Page
            table = new PdfPTable(5);
            table.setWidths(new int[]{1, 4, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("    PART-II" + StringUtils.repeat(" ", 51) + "SELF-APPRAISAL", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(To be filled in by the Appraisee )", f1));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Brief description of duties/tasks entrusted.(in about 200 words)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            String selfappraisal = paf.getSelfappraisal();
            /*selfappraisal = selfappraisal.replaceAll("<b>", "");
             selfappraisal = selfappraisal.replaceAll("</b>", "");
             selfappraisal = selfappraisal.replaceAll("<i>", "");
             selfappraisal = selfappraisal.replaceAll("</i>", "");
             selfappraisal = selfappraisal.replaceAll("<u>", "");
             selfappraisal = selfappraisal.replaceAll("</u>", "");*/
            ElementList elements = null;
            // elements = XMLWorkerHelper.parseToElementList(selfappraisal, "");
            Paragraph selfappraisalParagraph = new Paragraph();
            for (Element element : elements) {
                selfappraisalParagraph.add(element);
            }

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.addElement(selfappraisalParagraph);
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);

            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 2. Physical/Financial Targets & Achievements", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{2, 2, 2, 2, 2, 2});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("SL. No", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Task", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Target", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Qualitative % of Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //innercell.setBorder(Rectangle.LEFT);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("% of Achievement", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            ArrayList achievementList = paf.getAchivementList();
            if (achievementList != null && achievementList.size() > 0) {
                AchievementBean ab = null;
                for (int i = 0; i < achievementList.size(); i++) {
                    ab = (AchievementBean) achievementList.get(i);
                    innertable = new PdfPTable(6);
                    innertable.setWidths(new int[]{2, 2, 2, 2, 2, 2});
                    innertable.setWidthPercentage(100);
                    int j = i + 1;
                    innercell = new PdfPCell(new Phrase(j + "", f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getPercentQualitative(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(ab.getPercentAchievement(), f1));
                    innercell.setBorder(Rectangle.NO_BORDER);
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setColspan(5);
                    //cell.setFixedHeight(130);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3.(i) Significant work, if any, done", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            String specialcontribution = paf.getSpecialcontribution();
            /*specialcontribution = specialcontribution.replaceAll("<b>", "");
             specialcontribution = specialcontribution.replaceAll("</b>", "");
             specialcontribution = specialcontribution.replaceAll("<i>", "");
             specialcontribution = specialcontribution.replaceAll("</i>", "");
             specialcontribution = specialcontribution.replaceAll("<u>", "");
             specialcontribution = specialcontribution.replaceAll("</u>", "");*/

            //elements = XMLWorkerHelper.parseToElementList(specialcontribution, "");
            Paragraph specialcontributionParagraph = new Paragraph();
            for (Element element : elements) {
                specialcontributionParagraph.add(element);
            }

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            //cell = parseHtmlToParagraph(paf.getSpecialcontribution());
            cell = new PdfPCell();
            cell.addElement(specialcontributionParagraph);
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(ii) Work Done For Implementation of 5TS(Transparency,Teamwork,Technology,Transformation and Time):", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(paf.getFiveTComponentappraise(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            if (paf.getReason() != null && !paf.getReason().equals("")) {
                //Reason Start
                cell = new PdfPCell(new Phrase(" 4. Hindrance", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                String reason = paf.getReason();
                /*reason = reason.replaceAll("<b>", "");
                 reason = reason.replaceAll("</b>", "");
                 reason = reason.replaceAll("<i>", "");
                 reason = reason.replaceAll("</i>", "");
                 reason = reason.replaceAll("<u>", "");
                 reason = reason.replaceAll("</u>", "");*/

                // elements = XMLWorkerHelper.parseToElementList(reason, "");
                Paragraph reasonParagraph = new Paragraph();
                for (Element element : elements) {
                    reasonParagraph.add(element);
                }

                cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                //cell = new PdfPCell(parseHtmlToPDF(paf.getReason()));
                cell = new PdfPCell();
                cell.addElement(reasonParagraph);
                cell.setColspan(4);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                //Reason Stop
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                document.add(table);
                document.newPage();

                //Third Page
                table = new PdfPTable(5);
                table.setWidths(new int[]{1, 4, 5, 3, 3});
                table.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(50);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getApplicant()), f1));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Place: " + StringUtils.defaultString(paf.getPlace()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date: " + paf.getSubmitted_on(), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of Appraisee", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            String authorityType = findAuthorityType(empid, paf.getParid());
            if (authorityType != null && !authorityType.equals("")) {

                document.add(table);

                table = new PdfPTable(4);
                table.setWidths(new int[]{4, 2, 4, 2});
                table.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
                cell.setColspan(4);
                cell.setFixedHeight(30);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if ((authorityType.indexOf("REPORTING") > -1) || (authorityType.indexOf("REVIEWING") > -1) || (authorityType.indexOf("ACCEPTING") > -1)) {

                    cell = new PdfPCell(new Phrase("Remarks of Reporting Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    ReportingHelperBean rhb = null;
                    for (int i = 0; i < paf.getReportingdata().size(); i++) {
                        rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
                        if (i > 0) {
                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                        }
                        if (rhb.getIscurrentreporting().equals("Y") || rhb.getIsreportingcompleted().equals("Y")) {
                            cell = new PdfPCell(new Phrase("1. Assessment of work output, attributes and functional competencies.(This should be on a relative scale of 1-5, with 1 referring to the lowest level and 5 to the highest level. Please indicate your rating for the officer against each item.)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setFixedHeight(40);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Description", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Rating", getDesired_PDF_Font(9, false, true)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(a)  Attitude to work :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingattitude() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(f) Co-ordination ability :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingcoordination() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(b)  Sense of responsibility:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingresponsibility() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(g) Ability to work in a team:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getTeamworkrating() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(c)  Communication skill :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingcomskill() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject:", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingitskill() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(d)  Leadership Qualities :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingleadership() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(i) Initiative :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatinginitiative() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("(e)  Decision-making ability :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatingdecisionmaking() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("(j) Quality of Work :", getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getRatequalityofwork() + ""), getDesired_PDF_Font(9, false, false)));
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("2. General Assessment (Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public:)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getAuthNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse )", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getInadequaciesNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("4. Integrity (If integrity is doubtful or  adverse please write \"Not certified\" in the space below and justify your remarks in box 4 above)", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getIntegrityNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("5. Overall Grading", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + getGradingName(StringUtils.defaultString(rhb.getSltGrading())), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("6. For  Overall Grading  \"Below Average\" /  \"Outstanding\"  please provide justification in the   space below.:", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getGradingNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReportingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + rhb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                        }
                    }
                }

                if ((authorityType.indexOf("REVIEWING") > -1) || (authorityType.indexOf("ACCEPTING") > -1)) {

                    cell = new PdfPCell(new Phrase("Remarks of Reviewing Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    ReviewingHelperBean rhb = null;
                    for (int i = 0; i < paf.getReviewingdata().size(); i++) {
                        rhb = (ReviewingHelperBean) paf.getReviewingdata().get(i);
                        if (rhb.getIscurrentreviewing() != null && (rhb.getIscurrentreviewing().equals("Y") || rhb.getIsreviewingcompleted().equals("Y"))) {
                            cell = new PdfPCell(new Phrase("1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.", getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getReviewingNote()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + getGradingName(rhb.getSltReviewGrading() + ""), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReviewingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + rhb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);
                        }
                    }
                }

                if (authorityType.indexOf("ACCEPTING") > -1) {

                    cell = new PdfPCell(new Phrase("Remarks of Accepting Authority", getDesired_PDF_Font(9, true, true)));
                    cell.setColspan(4);
                    cell.setFixedHeight(20);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    AcceptingHelperBean ahb = null;
                    for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
                        ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);
                        if (((ahb.getIscurrentaccepting() != null && ahb.getIscurrentaccepting().equals("Y"))) || ahb.getIsacceptingcompleted().equals("Y")) {
                            /*cell = new PdfPCell(new Phrase(ahb.getAcceptingauthName(),pafunc.getDesired_PDF_Font(9,false,true)));
                             cell.setColspan(4);
                             cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                             cell.setBorderWidth(1f);
                             table.addCell(cell);*/

                            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(ahb.getAcceptingNote()), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Overall Grading Given By Accepting Authority  :" + getGradingName(ahb.getSltAcceptingGrading() + ""), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(ahb.getAcceptingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Overall Grading Given By Accepting Authority  :" + getGradingName(ahb.getSltAcceptingGrading() + ""), getDesired_PDF_Font(10, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell();
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase(StringUtils.defaultString(ahb.getAcceptingauthName()), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Submitted on- " + ahb.getSubmittedon(), getDesired_PDF_Font(9, false, false)));
                            cell.setColspan(4);
                            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            cell.setBorderWidth(1f);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                        }

                        cell = new PdfPCell();
                        cell.setColspan(4);
                        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                        cell.setBorderWidth(1f);
                        table.addCell(cell);
                    }
                }
            } else {
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(30);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private PdfPCell getChunkOfHTMLTagCode(String str) {

        PdfPCell pdfcell = new PdfPCell();
        try {
            String[] chunkedStr1 = str.split("<i>");

            Paragraph p1 = new Paragraph(chunkedStr1[0]);
            pdfcell.addElement(p1);

            String[] chunkedStr2 = chunkedStr1[1].split("</i>");

            Paragraph parseString = parseHtmlToPDF(chunkedStr2[0]);
            pdfcell.addElement(parseString);

            /*if(chunkedStr2[1] != null){
             Paragraph p2 = new Paragraph(chunkedStr2[1]);
             pdfcell.addElement(p2);
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfcell;
    }

    /*private PdfPCell parseHtmlToPDF(String str) throws IOException {
     StringReader body = new StringReader(str);
     final PdfPCell cell = new PdfPCell();

     XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
     @Override
     public void add(Writable w) {
     if (w instanceof WritableElement) {
     List<Element> elements = ((WritableElement) w).elements();
     for (Element e : elements) {
     cell.addElement(e);
     }
     }
     }
     }, body);

     return cell;
     }*/
    private Paragraph parseHtmlToPDF(String str) throws IOException {
        StringReader body = new StringReader(str);
        final Paragraph para = new Paragraph();

        XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
            @Override
            public void add(Writable w) {
                if (w instanceof WritableElement) {
                    List<Element> elements = ((WritableElement) w).elements();
                    for (Element e : elements) {
                        para.add(e);
                    }
                }
            }
        }, body);
        return para;
    }

    @Override
    public List getPARTemplateList(String templateHeading, String hrmsId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList templatelist = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT template_id,template_heading,template_text FROM par_template where template_heading=? and (hrms_id=? OR hrms_id is null) ORDER BY template_id ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, templateHeading);
            pst.setString(2, hrmsId);
            rs = pst.executeQuery();
            while (rs.next()) {
                templatelist.add(rs.getString("template_text"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return templatelist;
    }

    public void saveCustomForAuthNote(PARTemplateBean parTemplateBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("Insert into par_template(template_heading,template_text,hrms_id) values (?,?,?)");
            pstmt.setString(1, parTemplateBean.getTemplateHeading());
            pstmt.setString(2, parTemplateBean.getTemplateContent());
            pstmt.setString(3, parTemplateBean.getHrmsId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public HashMap getOtherDetailsOfAllFinancialYear(String empid, String fieldName) {
        Connection con = null;
        HashMap otherDetailsList = new HashMap();
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT fiscal_year,PAR_APPRAISEE_TRAN.PARID,CUR_SPC,PAR_APPRAISEE_TRAN.EMP_ID,PLACE,SELFAPPRAISAL, "
                    + " HINDERREASON,SPECIALCONTRIBUTIION,fivet_component_appraise from PAR_APPRAISEE_TRAN "
                    + " inner join par_master on PAR_APPRAISEE_TRAN.parid = par_master.parid WHERE PAR_APPRAISEE_TRAN.EMP_ID =? ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (fieldName.equals("briefDescription")) {
                    otherDetailsList.put(rs.getString("fiscal_year"), rs.getString("SELFAPPRAISAL"));
                } else if (fieldName.equals("significantWork")) {
                    otherDetailsList.put(rs.getString("fiscal_year"), rs.getString("SPECIALCONTRIBUTIION"));
                } else if (fieldName.equals("fivetImplementation")) {
                    otherDetailsList.put(rs.getString("fiscal_year"), rs.getString("fivet_component_appraise"));
                } else if (fieldName.equals("hinderedPerformance")) {
                    otherDetailsList.put(rs.getString("fiscal_year"), rs.getString("HINDERREASON"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return otherDetailsList;
    }

    @Override
    public ArrayList getAchievementDetailsOfAllFinancialYear(String fiscalyear, String empid) {
        Connection con = null;
        ArrayList achievementDetailsList = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT PAR_ACHIEVEMENT.parid,pacid,fiscal_year,ORIGINAL_FILENAME,thetask,target,achievementpercent,achievement,achievepercqualitative FROM PAR_ACHIEVEMENT "
                    + " INNER JOIN PAR_MASTER ON PAR_ACHIEVEMENT.PARID=PAR_MASTER.PARID "
                    + " LEFT OUTER JOIN HW_ATTACHMENTS ON PAR_ACHIEVEMENT.ATT_ID=HW_ATTACHMENTS.ATT_ID WHERE PAR_MASTER.EMP_ID=? and fiscal_year=? ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();
            while (rs.next()) {

                ParAchievement parachievement = new ParAchievement();
                parachievement.setHidpacid(rs.getInt("pacid"));
                parachievement.setTask(rs.getString("thetask"));
                parachievement.setTask(rs.getString("thetask"));
                parachievement.setTarget(rs.getString("target"));
                parachievement.setAchievement(rs.getString("achievement"));
                parachievement.setPercentAchievement(rs.getString("achievementpercent"));
                parachievement.setAchievementpersqualitative(rs.getString("achievepercqualitative"));
                achievementDetailsList.add(parachievement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return achievementDetailsList;
    }

    @Override
    public void savePreviousYearAchievement(int pacid, int hidparid) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement pst = null;
        //ParAchievement parachievement = new ParAchievement();
        //MultipartFile file = parachievement.getAttchfile();
        int tsino = 0;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select count(*) slno from par_achievement where parid=?");
            pst.setInt(1, hidparid);
            res = pst.executeQuery();
            if (res.next()) {
                tsino = res.getInt("slno") + 1;
            }
            pst = con.prepareStatement("insert into par_achievement (slno,parid,thetask,target,achievementpercent,achievement,achievepercqualitative) "
                    + " select " + tsino + "," + hidparid + ",thetask,target,achievementpercent,achievement,achievepercqualitative from par_achievement where par_achievement.pacid=?");
            pst.setInt(1, pacid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savePreviousYearAchievementList(int prevparid, int parid) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into par_achievement (slno,parid,thetask,target,achievementpercent,achievement,achievepercqualitative) "
                    + " select slno," + parid + ",thetask,target,achievementpercent,achievement,achievepercqualitative from par_achievement where par_achievement.parid=?");
            pst.setInt(1, prevparid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public HashMap getPreviousPARDetail(String empid) {
        Connection con = null;
        HashMap previousPARList = new HashMap();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "select parid, fiscal_year,to_char(period_from,'DD/Mon/yyyy')fromdate,to_char(period_to,'DD/Mon/yyyy')todate from par_master where emp_id=? and par_status>6 order by parid desc ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                previousPARList.put(rs.getInt("parid"), rs.getString("fiscal_year") + " (" + rs.getString("fromdate") + "-" + rs.getString("todate") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return previousPARList;
    }

    @Override
    public void savePreviousyrOtherDetails(int prevparid, int parid) {
        Connection con = null;
        ResultSet res = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into PAR_APPRAISEE_TRAN (PARID,CUR_SPC,EMP_ID,SPC_IN_PERIOD,SUBMITTED_ON,PLACE,SELFAPPRAISAL,HINDERREASON,SPECIALCONTRIBUTIION,fivet_component_appraise) "
                    + " select " + parid + ",CUR_SPC,EMP_ID,SPC_IN_PERIOD,SUBMITTED_ON,PLACE,SELFAPPRAISAL,HINDERREASON,SPECIALCONTRIBUTIION,fivet_component_appraise from PAR_APPRAISEE_TRAN where parid=?");
            pst.setInt(1, prevparid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getloginWiseSavedTemplateList(String templateHeading, String empid) {
        Connection con = null;
        ArrayList loginwisetemplateList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT template_id,template_heading,template_text,hrms_id FROM par_template where template_heading=? and hrms_id=? ORDER BY template_id");
            pstmt.setString(1, templateHeading);
            pstmt.setString(2, empid);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PARTemplateBean parTemplateBean = new PARTemplateBean();
                parTemplateBean.setTemplateId(rs.getInt("template_id"));
                parTemplateBean.setTemplateHeading(rs.getString("template_heading"));
                parTemplateBean.setTemplateContent(rs.getString("template_text"));
                parTemplateBean.setHrmsId(rs.getString("hrms_id"));
                loginwisetemplateList.add(parTemplateBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loginwisetemplateList;
    }

    @Override
    public void deleteloginWiseSavedTemplateList(int templateId) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM  par_template WHERE template_id=?");
            pst.setInt(1, templateId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ParSubmitForm getForceforwardLogDetails() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ParSubmitForm parfrm = new ParSubmitForm();

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT * FROM par_forceforward_log WHERE fiscal_year=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, parfrm.getFiscalYear());
            rs = pst.executeQuery();
            if (rs.next()) {
                parfrm.setDateOfForceForwardFromReportingToReviewing(rs.getString("forceforward_on"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return parfrm;
    }

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));

    }

    @Override
    public void generateQRCodeForPreviousYearPAR(int parId, String qrCodePathPAR) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str4 = "";
        String str = "";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT parid,initiated_on,par_master.EMP_ID,FISCAL_YEAR,STATUS_ID,period_from,"
                    + "period_to,INITIALS,F_NAME,M_NAME,L_NAME,SPN FROM par_master "
                    + "INNER JOIN EMP_MAST ON par_master.EMP_ID = EMP_MAST.EMP_ID "
                    + "inner join task_master on par_master.task_id = task_master.task_id "
                    + "LEFT OUTER JOIN G_SPC ON par_master.SPC = G_SPC.SPC "
                    + "WHERE parid=?");
            pstmt.setInt(1, parId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String str1 = rs.getString("EMP_ID");
                String str2 = rs.getString("FISCAL_YEAR");
                String str3 = Integer.toString(rs.getInt("STATUS_ID"));
                if (str3 != null && !str3.equals("") && str3.equals("9")) {
                    str4 = "COMPLETED";
                } else {
                    str4 = "NOT COMPLETED";
                }
                String str5 = rs.getString("initiated_on");
                String str6 = StringUtils.defaultString(rs.getString("INITIALS")) + " " + rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME");
                String str7 = Integer.toString(rs.getInt("parid"));
                str = "HRMS Id =" + str1 + ",Employee Name=" + str6 + ",financial year=" + str2 + " ,status = " + str4 + " ,submitted_on= " + str5 + "";

                //data that we want to store in the QR code  
                String parFilePath = qrCodePathPAR;
                String storedpath = qrCodePathPAR + str2 + CommonFunctions.getResourcePath();
                String path = storedpath + str1 + "-" + str7 + ".png";
                //Encoding charset to be used  
                String charset = "UTF-8";
                Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                //generates QR code with Low level(L) error correction capability  
                hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                //invoking the user-defined method that creates the QR code  
                generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly   
                //prints if the QR code is generated   
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (WriterException ex) {
            Logger.getLogger(PropertyStatementDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertyStatementDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ReportingHelperBean DownloadGradingAttchmentReporting(int parId, int prptid, String gradingPath) {
        ReportingHelperBean reportingHelperBean = new ReportingHelperBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT orginal_filename_grading_document_reporting,disk_filename_grading_document_reporting,file_type_grading_document_reporting, "
                    + "file_path_grading_document_reporting,fiscal_year FROM par_reporting_tran "
                    + "inner join PAR_MASTER ON par_reporting_tran.par_id = PAR_MASTER.parid WHERE par_reporting_tran.par_id=? and prptid=?");
            pst.setInt(1, parId);
            pst.setInt(2, prptid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                reportingHelperBean.setOriginalFileNamegradingDocumentReporting(rs.getString("disk_filename_grading_document_reporting"));
                reportingHelperBean.setDiskFileNameforgradingDocumentReporting(rs.getString("orginal_filename_grading_document_reporting"));
                reportingHelperBean.setFileTypeforgradingDocumentReporting("file_type_grading_document_reporting");
                filepath = rs.getString("file_path_grading_document_reporting");
                String fiscalyear = rs.getString("fiscal_year");
                //str2 + CommonFunctions.getResourcePath()
                String dirpath = gradingPath + fiscalyear + "/" + rs.getString("disk_filename_grading_document_reporting");
                File f = new File(dirpath);
                //File f = new File(filepath + File.separator + reportingHelperBean.getDiskFileNameforgradingDocumentReporting());
                reportingHelperBean.setFilecontent(FileUtils.readFileToByteArray(f));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return reportingHelperBean;
    }

    @Override
    public ReviewingHelperBean DownloadGradingAttchmentReviewing(int parId, int prvtid, String gradingPath) {
        ReviewingHelperBean reviewingHelperBean = new ReviewingHelperBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT orginal_filename_grading_document_reviewing,disk_filename_grading_document_reviewing,file_type_grading_document_reviewing, "
                    + "file_path_grading_document_reviewing,fiscal_year FROM par_reviewing_tran "
                    + "inner join PAR_MASTER ON par_reviewing_tran.par_id = PAR_MASTER.parid WHERE par_reviewing_tran.par_id=? and prvtid=?");
            pst.setInt(1, parId);
            pst.setInt(2, prvtid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                reviewingHelperBean.setOriginalFileNamegradingDocumentReviewing(rs.getString("orginal_filename_grading_document_reviewing"));
                reviewingHelperBean.setDiskFileNameforgradingDocumentReviewing(rs.getString("disk_filename_grading_document_reviewing"));
                reviewingHelperBean.setFileTypeforgradingDocumentReviewing("file_type_grading_document_reviewing");
                filepath = rs.getString("file_path_grading_document_reviewing");
                String fiscalyear = rs.getString("fiscal_year");
                String dirpath = gradingPath + fiscalyear + "/" + rs.getString("disk_filename_grading_document_reviewing");
                File f = new File(dirpath);
                reviewingHelperBean.setFilecontent(FileUtils.readFileToByteArray(f));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return reviewingHelperBean;
    }

    @Override
    public AcceptingHelperBean DownloadGradingAttchmentAccepting(int parId, int pactid, String gradingPath) {
        AcceptingHelperBean acceptingHelperBean = new AcceptingHelperBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT orginal_filename_grading_document_acceptting,disk_filename_grading_document_accepting,file_type_grading_document_accepting, "
                    + "file_path_grading_document_accepting,fiscal_year FROM par_accepting_tran "
                    + "inner join PAR_MASTER ON par_accepting_tran.par_id = PAR_MASTER.parid WHERE par_accepting_tran.par_id=? and pactid=?");
            pst.setInt(1, parId);
            pst.setInt(2, pactid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                acceptingHelperBean.setOriginalFileNamegradingDocumentAccepting(rs.getString("orginal_filename_grading_document_acceptting"));
                acceptingHelperBean.setDiskFileNameforgradingDocumentAccepting(rs.getString("disk_filename_grading_document_accepting"));
                acceptingHelperBean.setFileTypeforgradingDocumentAccepting("file_type_grading_document_accepting");
                filepath = rs.getString("file_path_grading_document_accepting");
                String fiscalyear = rs.getString("fiscal_year");
                String dirpath = gradingPath + fiscalyear + "/" + rs.getString("disk_filename_grading_document_accepting");
                File f = new File(dirpath);
                acceptingHelperBean.setFilecontent(FileUtils.readFileToByteArray(f));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return acceptingHelperBean;
    }

    @Override
    public void deleteGradingAttachmentReporting(ReportingHelperBean reportingHelperBean) {
        PreparedStatement pst = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select orginal_filename_grading_document_reporting, "
                    + " disk_filename_grading_document_reporting,file_type_grading_document_reporting,file_path_grading_document_reporting from par_reporting_tran where prptid=? and par_id=?");
            pst.setInt(1, reportingHelperBean.getPrtid());
            pst.setInt(2, reportingHelperBean.getParId());
            res = pst.executeQuery();
            if (res.next()) {
                String diskFileName = res.getString("disk_filename_grading_document_reporting");
                String file_path = res.getString("file_path_grading_document_reporting");
                File f = new File(file_path + File.separator + diskFileName);
                f.delete();
            }

            pst = con.prepareStatement("update par_reporting_tran set orginal_filename_grading_document_reporting=null,disk_filename_grading_document_reporting=null,file_type_grading_document_reporting=null,file_path_grading_document_reporting=null where prptid=? and par_id=?");
            pst.setInt(1, reportingHelperBean.getPrtid());
            pst.setInt(2, reportingHelperBean.getParId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteGradingAttachmentReviewing(ReviewingHelperBean reviewingHelperBean) {
        PreparedStatement pst = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select orginal_filename_grading_document_reviewing,disk_filename_grading_document_reviewing,file_type_grading_document_reviewing, "
                    + "file_path_grading_document_reviewing FROM par_reviewing_tran where prvtid=? and par_id=?");
            pst.setInt(1, reviewingHelperBean.getPrtid());
            pst.setInt(2, reviewingHelperBean.getParId());
            res = pst.executeQuery();
            if (res.next()) {
                String diskFileName = res.getString("disk_filename_grading_document_reviewing");
                String file_path = res.getString("file_path_grading_document_reviewing");
                File f = new File(file_path + File.separator + diskFileName);
                f.delete();
            }

            pst = con.prepareStatement("update par_reviewing_tran set orginal_filename_grading_document_reviewing=null,disk_filename_grading_document_reviewing=null,file_type_grading_document_reviewing=null,file_path_grading_document_reviewing=null where prvtid=? and par_id=?");
            pst.setInt(1, reviewingHelperBean.getPrtid());
            pst.setInt(2, reviewingHelperBean.getParId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteGradingAttachmentAccepting(AcceptingHelperBean acceptingHelperBean) {
        PreparedStatement pst = null;
        ResultSet res = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select orginal_filename_grading_document_acceptting,disk_filename_grading_document_accepting,file_type_grading_document_accepting, "
                    + "file_path_grading_document_accepting FROM par_accepting_tran where pactid=? and par_id=?");
            pst.setInt(1, acceptingHelperBean.getPactid());
            pst.setInt(2, acceptingHelperBean.getParId());
            res = pst.executeQuery();
            if (res.next()) {
                String diskFileName = res.getString("disk_filename_grading_document_accepting");
                String file_path = res.getString("file_path_grading_document_accepting");
                File f = new File(file_path + File.separator + diskFileName);
                f.delete();
            }

            pst = con.prepareStatement("update par_accepting_tran set orginal_filename_grading_document_acceptting=null,disk_filename_grading_document_accepting=null,file_type_grading_document_accepting=null,file_path_grading_document_accepting=null where pactid=? and par_id=?");
            pst.setInt(1, acceptingHelperBean.getPactid());
            pst.setInt(2, acceptingHelperBean.getParId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public boolean isCheckParPeriod(String parfrmdt, String partodt, String fiscalyear) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = null;
        boolean iscorrectPeriod = false;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            con = dataSource.getConnection();

            String[] finyear = fiscalyear.split("-");
            String min_year = finyear[0];
            String max_year = 20 + finyear[1];

            Date fromDateCalendar = formatter.parse(parfrmdt);
            Date toDateCalendar = formatter.parse(partodt);

            String fixFromDate = min_year + "-04-01";
            String fixToDate = max_year + "-03-31";

            Date tempfixFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(fixFromDate);
            Date tempfixToDate = new SimpleDateFormat("yyyy-MM-dd").parse(fixToDate);

            if (fromDateCalendar.before(tempfixFromDate)) {
                iscorrectPeriod = false;
            } else if (toDateCalendar.after(tempfixToDate)) {
                iscorrectPeriod = false;
            } else if (fromDateCalendar.before(tempfixFromDate) && toDateCalendar.after(tempfixToDate)) {
                iscorrectPeriod = false;
            } else {
                iscorrectPeriod = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return iscorrectPeriod;
    }

    @Override
    public boolean isParCreatedDateClosed(String empId, String fiscalyear) {
        boolean isclosed = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Date parPeriodclosingDate = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT par_period_for_appraisee FROM financial_year WHERE fy=?");
            pstmt.setString(1, fiscalyear);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parPeriodclosingDate = rs.getDate("par_period_for_appraisee");
            }

            Date curentDate = new Date();
            //if (parPeriodclosingDate.before(curentDate)) {
            if (curentDate.after(parPeriodclosingDate)) {
                isclosed = true;
                pstmt = con.prepareStatement("UPDATE financial_year SET is_closed ='Y' WHERE fy=?");
                pstmt.setString(1, fiscalyear);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return isclosed;
    }

}

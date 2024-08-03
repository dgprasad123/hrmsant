/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.discProceeding;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.FileDownload;
import hrms.model.discProceeding.CourtCaseBean;
import hrms.model.discProceeding.DefenceBean;
import hrms.model.discProceeding.DelinquentBean;
import hrms.model.discProceeding.DiscChargeBean;
import hrms.model.discProceeding.DiscProceedingBean;
import hrms.model.discProceeding.DiscWitnessBean;
import hrms.model.discProceeding.DispatchDetailsBean;
import hrms.model.discProceeding.DpViewBean;
import hrms.model.discProceeding.EnquiryBean;
import hrms.model.discProceeding.FileAttachBean;
import hrms.model.discProceeding.FinalOrder;
import hrms.model.discProceeding.IoBean;
import hrms.model.discProceeding.IoReportBean;
import hrms.model.discProceeding.NoticeBean;
import hrms.model.discProceeding.ProceedingBean;
import hrms.model.discProceeding.Rule15ChargeBean;
import hrms.model.discProceeding.SubmitProceedingBean;
import hrms.model.employee.Employee;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class DiscProceedingImpl implements DiscProceedingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    private Object dateFormat;
    private String uploadPath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public List getPostWithEmpList(String offcode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = null;

        try {
            con = this.dataSource.getConnection();
            String postQry = "select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id,POST_CODE,CUR_SPC from ("
                    + "select spc,gpc from g_spc where off_code='" + offcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST";
            ps = con.prepareStatement(postQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    so = new SelectOption();

                    so.setLabel(rs.getString("post") + " (" + rs.getString("EMPNAME") + ")");
                    so.setValue(rs.getString("CUR_SPC"));
                    li.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int saveRule15ForwardDP(String authHrmsId, String authEmpSpc, Rule15ChargeBean chargeBean) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        int logid = 0;
        int tmRes = 0;
        int wfRes = 0;
        int res = 0;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String taskMastInsQry = "INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,APPLY_TO,"
                    + "INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?,?)";
            pstmt1 = con.prepareStatement(taskMastInsQry, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setInt(1, 11);  //SAME AS (PROCESS_ID) OF TBALE G_WORKFLOW_PROCESS
            pstmt1.setString(2, authHrmsId);
            pstmt1.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt1.setInt(4, 56); //SAME AS (STATUS_ID) OF TBALE G_PROCESS_STATUS - for forward
            pstmt1.setString(5, chargeBean.getHidForwardHrmsId());
            pstmt1.setString(6, chargeBean.getHidForwardHrmsId());
            pstmt1.setString(7, authEmpSpc);
            pstmt1.setString(8, chargeBean.getHidPostCode());
            tmRes = pstmt1.executeUpdate();
            rs = pstmt1.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            String insQry = "INSERT INTO WORKFLOW_LOG(LOG_ID,REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,"
                    + "TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(insQry);
            logid = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
            pstmt.setInt(1, logid);
            pstmt.setInt(2, Integer.parseInt(chargeBean.getHidFowardDaId()));
            pstmt.setString(3, authHrmsId);
            pstmt.setString(4, chargeBean.getHidPostCode());
            pstmt.setString(5, chargeBean.getHidForwardHrmsId());
            pstmt.setString(6, "NA");
            pstmt.setString(7, authEmpSpc);
            pstmt.setTimestamp(8, new Timestamp(dateFormat.parse(startTime).getTime()));
            //NOTE,SPC_ONTIME,TASK_ACTION_DATE

            pstmt.setInt(9, taskId);
            pstmt.setInt(10, 56); //SAME AS (STATUS_ID) OF TBALE G_PROCESS_STATUS - for forward
            pstmt.setString(11, "DISCIPLINARY PROCEEDING");
            pstmt.setString(12, "NA");
            //TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE

            wfRes = pstmt.executeUpdate();

            if (tmRes == 1 && wfRes == 1) {
                pstmt2 = con.prepareStatement("UPDATE disciplinary_authority set dp_status=?,task_id=? WHERE daid=?");
                pstmt2.setString(1, "56");
                pstmt2.setInt(2, taskId);
                pstmt2.setInt(3, Integer.parseInt(chargeBean.getHidFowardDaId()));
                res = pstmt2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public void saveinitiatedDepartmentproceeding(SubmitProceedingBean ProceedingBean) {
        int empid = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO task_master(process_id, initiated_by, initiated_on,status_id,pending_at, note, apply_to,initiated_spc, pending_spc, apply_to_spc, is_seen ) VALUES(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            String[] temp = ProceedingBean.getApplyTo().split("-");
            pst.setInt(1, 11); //process_id is for Disciplinary Proceeding
            pst.setString(2, ProceedingBean.getInitiatedBy());
            pst.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
            pst.setInt(4, 72);
            pst.setString(5, temp[0]);
            pst.setString(6, ProceedingBean.getNote());
            pst.setString(7, temp[0]);
            pst.setString(8, ProceedingBean.getInitiatedSpc());
            pst.setString(9, temp[1]);
            pst.setString(10, temp[1]);
            pst.setString(11, "N");
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            pst = con.prepareStatement("UPDATE disciplinary_authority SET TASK_ID=?, DP_STATUS=62, AUTH_HRMSID=?, AUTH_SPC=? WHERE DAID=?");
            pst.setInt(1, taskId);
            pst.setString(2, temp[0]);
            pst.setString(3, temp[1]);
            pst.setInt(4, ProceedingBean.getDaId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveAddWitness(DiscWitnessBean WitnessBean) {

        int empid = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            int dacwid = 0;
            pst = con.prepareStatement("SELECT MAX(dacwid)+1 AS MAXDACWID from disciplinary_act_charge_witness ");
            rs = pst.executeQuery();

            if (rs.next()) {
                dacwid = rs.getInt("MAXDACWID");
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);
            pst = con.prepareStatement("INSERT INTO disciplinary_act_charge_witness(dacwid,dacid ,witness_hrmsid, witness_spc) VALUES(?,?,?,?)");
            for (int i = 0; i < WitnessBean.getSelectedHrmsid().length; i++) {
                String selectedemployee = WitnessBean.getSelectedHrmsid()[i];
                String[] arrOfStr = selectedemployee.split("-", 2);

                pst.setInt(1, dacwid);
                pst.setInt(2, WitnessBean.getDacid());
                pst.setString(3, arrOfStr[0]);
                pst.setString(4, arrOfStr[1]);
                pst.executeUpdate();
                dacwid++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteWitnessList(int[] selecteddacwid) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from disciplinary_act_charge_witness where dacwid=?");
            for (int i = 0; i < selecteddacwid.length; i++) {
                pst.setInt(1, selecteddacwid[i]);
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
    public List getWitnessList(int dacid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList witnessList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dacwid,witness_hrmsid, witness_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,spn,witness_name from disciplinary_act_charge_witness "
                    + "inner join emp_mast on disciplinary_act_charge_witness.witness_hrmsid=emp_mast.emp_id "
                    + "left outer join g_spc on disciplinary_act_charge_witness.witness_spc=g_spc.spc where dacid =?");

            pst.setInt(1, dacid);
            rs = pst.executeQuery();
            while (rs.next()) {
                DiscWitnessBean disc = new DiscWitnessBean();
                disc.setDacwid(rs.getInt("dacwid"));
                disc.setHrmsid(rs.getString("witness_hrmsid"));
                disc.setSpc(rs.getString("witness_spc"));
                disc.setEmpname(rs.getString("FULL_NAME"));
                disc.setSpn(rs.getString("spn"));
                disc.setWitnessName(rs.getString("witness_name"));
                witnessList.add(disc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return witnessList;
    }

    @Override
    public List getWitnessListfromdaid(int daId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList witnessList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dacwid,witness_hrmsid, witness_spc,f_name,m_name,l_name,spn from disciplinary_act_charge_witness "
                    + " inner join emp_mast on disciplinary_act_charge_witness.witness_hrmsid=emp_mast.emp_id "
                    + " left outer join g_spc on disciplinary_act_charge_witness.witness_spc=g_spc.spc "
                    + " inner join disciplinary_act_charge on disciplinary_act_charge_witness.dacid =  disciplinary_act_charge.dacid where daid =?");

            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                DiscWitnessBean disc = new DiscWitnessBean();
                disc.setDacwid(rs.getInt("dacwid"));
                disc.setHrmsid(rs.getString("witness_hrmsid"));
                disc.setSpc(rs.getString("witness_spc"));
                disc.setEmpname(rs.getString("f_name") + " " + rs.getString("m_name") + " " + rs.getString("l_name"));
                disc.setSpn(rs.getString("spn"));
                witnessList.add(disc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return witnessList;
    }

    public List getOtherWitnessList(int dacid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList witnessList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dacwid,at,po,ps,pin_code,mobile_no,email_id,dist,witness_name from disciplinary_act_charge_witness where dacid =? and witness_hrmsid is null");

            pst.setInt(1, dacid);
            rs = pst.executeQuery();
            while (rs.next()) {
                DiscWitnessBean disc = new DiscWitnessBean();
                disc.setDacwid(rs.getInt("dacwid"));
                disc.setAddressat(rs.getString("at"));
                disc.setAddresspo(rs.getString("po"));
                disc.setAddressps(rs.getString("ps"));
                disc.setPincode(rs.getString("pin_code"));
                disc.setMobile(rs.getString("mobile_no"));
                disc.setEmail(rs.getString("email_id"));
                disc.setDist(rs.getString("dist"));
                disc.setWitnessName(rs.getString("witness_name"));
                witnessList.add(disc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return witnessList;
    }

    @Override
    public String saveDiscCharge(DiscChargeBean chargeBean) {
        //int empid = 0;
        boolean flag = false;
        int result = 0;
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        String msg = "Sucessfully Saved";
        try {
            con = dataSource.getConnection();
            String articlesofChargeoriginalfilename = null;
            String statementOfImputationoriginalfilename = null;
            String memoofEvidenceoriginalfilename = null;
            String descriptionOfDocumentoriginalfilename = null;
            String articlesofChargediskFileName = null;
            String statementOfImputationdiskFileName = null;
            String memoofEvidencediskFileName = null;
            String descriptionOfDocumentdiskFileName = null;
            String articlesofChargeContentType = null;
            String statementOfImputationContentType = null;
            String memoofEvidenceContentType = null;
            String descriptionOfDocumentContentType = null;

            if (chargeBean.getDacid() == 0) {

                int dacid = 0;
                pstmt = con.prepareStatement("INSERT INTO disciplinary_act_charge (daid ,article_of_charge, statement_of_imputation,memo_of_evidence,description_of_document,off_name,designation,dadid) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, chargeBean.getDaId());
                pstmt.setString(2, chargeBean.getArticleOfCharge());
                pstmt.setString(3, chargeBean.getStatementOfImputation());
                pstmt.setString(4, chargeBean.getMemoOfEvidence());
                pstmt.setString(5, chargeBean.getBriefDescriptionOfDocument());
                pstmt.setString(6, chargeBean.getOffName());
                pstmt.setString(7, chargeBean.getOffHeadDesignation());
                pstmt.setInt(8, chargeBean.getDadid());
                pstmt.executeUpdate();
                //DataBaseFunctions.closeSqlObjects(pstmt);
                ResultSet maxIdrs = pstmt.getGeneratedKeys();
                if (maxIdrs.next()) {
                    dacid = maxIdrs.getInt("dacid");
                }

                if (chargeBean.getArticlesofChargeDocument() != null && !chargeBean.getArticlesofChargeDocument().isEmpty()) {
                    articlesofChargeoriginalfilename = chargeBean.getArticlesofChargeDocument().getOriginalFilename();
                    articlesofChargediskFileName = new Date().getTime() + "";
                    articlesofChargeContentType = chargeBean.getArticlesofChargeDocument().getContentType();
                    byte[] bytes = chargeBean.getArticlesofChargeDocument().getBytes();
                    File dir = new File(this.uploadPath + File.separator);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + articlesofChargediskFileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    pstmt = con.prepareStatement("UPDATE disciplinary_act_charge SET article_of_charge_original_filename=?, article_of_charge_disk_filename = ?,article_of_charge_file_type=? where dacid =?");
                    pstmt.setString(1, articlesofChargeoriginalfilename);
                    pstmt.setString(2, articlesofChargediskFileName);
                    pstmt.setString(3, articlesofChargeContentType);
                    pstmt.setInt(4, dacid);
                    pstmt.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pstmt);
                }
                if (chargeBean.getStatementOfImputationDocument() != null && !chargeBean.getStatementOfImputationDocument().isEmpty()) {
                    statementOfImputationoriginalfilename = chargeBean.getStatementOfImputationDocument().getOriginalFilename();
                    statementOfImputationdiskFileName = new Date().getTime() + "";
                    statementOfImputationContentType = chargeBean.getStatementOfImputationDocument().getContentType();
                    byte[] bytes = chargeBean.getStatementOfImputationDocument().getBytes();
                    File dir = new File(this.uploadPath + File.separator);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + statementOfImputationdiskFileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    pstmt = con.prepareStatement("UPDATE disciplinary_act_charge SET statement_of_imputation_original_filename=?, statement_of_imputation_disk_filename = ?,statement_of_imputation_file_type=? where dacid =?");
                    pstmt.setString(1, statementOfImputationoriginalfilename);
                    pstmt.setString(2, statementOfImputationdiskFileName);
                    pstmt.setString(3, statementOfImputationContentType);
                    pstmt.setInt(4, dacid);
                    pstmt.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pstmt);
                }
                if (chargeBean.getMemoofEvidenceDocument() != null && !chargeBean.getMemoofEvidenceDocument().isEmpty()) {
                    memoofEvidenceoriginalfilename = chargeBean.getMemoofEvidenceDocument().getOriginalFilename();
                    memoofEvidencediskFileName = new Date().getTime() + "";
                    memoofEvidenceContentType = chargeBean.getMemoofEvidenceDocument().getContentType();
                    byte[] bytes = chargeBean.getMemoofEvidenceDocument().getBytes();
                    File dir = new File(this.uploadPath + File.separator);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + memoofEvidencediskFileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    pstmt = con.prepareStatement("UPDATE disciplinary_act_charge SET memo_of_evidence_original_filename=?, memo_ofevidence_disk_filename = ?,memo_of_evidence_file_type=? where dacid =?");
                    pstmt.setString(1, memoofEvidenceoriginalfilename);
                    pstmt.setString(2, memoofEvidencediskFileName);
                    pstmt.setString(3, memoofEvidenceContentType);
                    pstmt.setInt(4, dacid);
                    pstmt.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pstmt);
                }
                if (chargeBean.getDescriptionOfDocument() != null && !chargeBean.getDescriptionOfDocument().isEmpty()) {
                    descriptionOfDocumentoriginalfilename = chargeBean.getDescriptionOfDocument().getOriginalFilename();
                    descriptionOfDocumentdiskFileName = new Date().getTime() + "";
                    descriptionOfDocumentContentType = chargeBean.getDescriptionOfDocument().getContentType();
                    byte[] bytes = chargeBean.getDescriptionOfDocument().getBytes();
                    File dir = new File(this.uploadPath + File.separator);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + descriptionOfDocumentdiskFileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    pstmt = con.prepareStatement("UPDATE disciplinary_act_charge SET Description_of_Document_original_filename=?, Description_of_Document_disk_filename = ?,Description_of_Document_file_type=? where dacid =?");
                    pstmt.setString(1, descriptionOfDocumentoriginalfilename);
                    pstmt.setString(2, descriptionOfDocumentdiskFileName);
                    pstmt.setString(3, descriptionOfDocumentContentType);
                    pstmt.setInt(4, dacid);
                    pstmt.executeUpdate();
                    DataBaseFunctions.closeSqlObjects(pstmt);
                }

            } else {
                pstmt = con.prepareStatement("UPDATE disciplinary_act_charge SET article_of_charge =?, statement_of_imputation =?,memo_of_evidence =?,description_of_document =?,off_name =?,designation=? where dacid=?");
                // pstmt.setInt(1, chargeBean.getDaId());
                pstmt.setString(1, chargeBean.getArticleOfCharge());
                pstmt.setString(2, chargeBean.getStatementOfImputation());
                pstmt.setString(3, chargeBean.getMemoOfEvidence());
                pstmt.setString(4, chargeBean.getMemoOfEvidence());
                pstmt.setString(5, chargeBean.getOffName());
                pstmt.setString(6, chargeBean.getOffHeadDesignation());
                pstmt.setInt(7, chargeBean.getDacid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            msg = "Error Occured";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public FileDownload getFileDownloadData(int dacid, String documentTypeName) {
        FileDownload fileDownload = new FileDownload();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select dacid,article_of_charge,statement_of_imputation,memo_of_evidence,description_of_document,defence_remark,article_of_Charge_original_filename,article_of_Charge_disk_filename,article_of_Charge_file_type, "
                    + "statement_Of_Imputation_original_filename,statement_Of_Imputation_disk_filename,statement_Of_Imputation_file_type,memo_of_Evidence_original_filename,"
                    + "memo_ofEvidence_disk_filename,memo_of_Evidence_file_type,Description_of_Document_original_filename,Description_of_Document_disk_filename,Description_of_Document_file_type from disciplinary_act_charge where dacid= ? ");
            pstmt.setInt(1, dacid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (documentTypeName.equals("articlecharge")) {
                    fileDownload.setOriginalfilename(rs.getString("article_of_Charge_original_filename"));
                    fileDownload.setFiletype(rs.getString("article_of_Charge_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("article_of_Charge_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("statementimputation")) {
                    fileDownload.setOriginalfilename(rs.getString("statement_Of_Imputation_original_filename"));
                    fileDownload.setFiletype(rs.getString("statement_Of_Imputation_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("statement_Of_Imputation_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("memoevidence")) {
                    fileDownload.setOriginalfilename(rs.getString("memo_of_Evidence_original_filename"));
                    fileDownload.setFiletype(rs.getString("memo_of_Evidence_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("memo_ofEvidence_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                } else if (documentTypeName.equals("documentdescription")) {
                    fileDownload.setOriginalfilename(rs.getString("Description_of_Document_original_filename"));
                    fileDownload.setFiletype(rs.getString("Description_of_Document_file_type"));
                    File f = new File(this.uploadPath + File.separator + rs.getString("Description_of_Document_disk_filename"));
                    fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return fileDownload;
    }

    @Override
    public void deleteDiscCharge(int dacid) {

        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM disciplinary_act_charge_witness where dacid=?");
            pst.setInt(1, dacid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("DELETE FROM disciplinary_act_charge where dacid=?");
            pst.setInt(1, dacid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("SELECT D_FILE_NAME,FILE_PATH FROM employee_attachment WHERE ref_type='DPCHARGE' AND ref_id=?");
            pst.setInt(1, dacid);
            rs = pst.executeQuery();
            while (rs.next()) {
                String filepath = rs.getString("FILE_PATH");
                File f = new File(filepath);
                if (f.exists()) {
                    f.delete();
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            pst = con.prepareStatement("DELETE FROM employee_attachment WHERE ref_type='DPCHARGE' AND ref_id=?");
            pst.setInt(1, dacid);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getDiscChargeList(int daId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList disccharge = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select daid,dacid,article_of_charge,statement_of_imputation,memo_of_evidence,description_of_document,defence_remark,article_of_Charge_original_filename,article_of_Charge_disk_filename,article_of_Charge_file_type, "
                    + "statement_Of_Imputation_original_filename,statement_Of_Imputation_disk_filename,statement_Of_Imputation_file_type,memo_of_Evidence_original_filename,"
                    + "memo_ofEvidence_disk_filename,memo_of_Evidence_file_type,Description_of_Document_original_filename,Description_of_Document_disk_filename,Description_of_Document_file_type,off_name,designation,dadid from disciplinary_act_charge where daid= ? ");
            // pst.setInt(1, dacid);
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                DiscChargeBean disc = new DiscChargeBean();
                disc.setDaId(rs.getInt("daid"));
                disc.setDacid(rs.getInt("dacid"));
                disc.setArticleOfCharge(rs.getString("article_of_charge"));
                disc.setArticlesofChargeoriginalfilename(rs.getString("article_of_Charge_original_filename"));
                disc.setArticlesofChargeContentType(rs.getString("article_of_Charge_file_type"));
                disc.setStatementOfImputation(rs.getString("statement_of_imputation"));
                disc.setStatementOfImputationoriginalfilename(rs.getString("statement_Of_Imputation_original_filename"));
                disc.setStatementOfImputationContentType(rs.getString("statement_Of_Imputation_file_type"));
                disc.setMemoOfEvidence(rs.getString("memo_of_evidence"));
                disc.setMemoofEvidenceoriginalfilename(rs.getString("memo_of_Evidence_original_filename"));
                disc.setMemoofEvidenceContentType(rs.getString("memo_of_Evidence_file_type"));
                disc.setBriefDescriptionOfDocument(rs.getString("description_of_document"));
                disc.setDescriptionOfDocumentoriginalfilename(rs.getString("Description_of_Document_original_filename"));
                disc.setDescriptionOfDocumentContentType(rs.getString("Description_of_Document_file_type"));
                disc.setOffName(rs.getString("off_name"));
                disc.setOffHeadDesignation(rs.getString("designation"));
                disc.setRemark(rs.getString("defence_remark"));
                disc.setDadid(rs.getInt("dadid"));

                disccharge.add(disc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return disccharge;
    }

    @Override
    public List getDiscChargeListWithWitness(int daId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList disccharge = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dacid,article_of_charge,statement_of_imputation,memo_of_evidence,description_of_document,defence_remark from disciplinary_act_charge "
                    + "LEFT OUTER JOIN (SELECT * FROM employee_attachment WHERE ref_type='DPCHARGE')employee_attachment ON disciplinary_act_charge.dacid = employee_attachment.ref_id where daid = ? ");

            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                DiscChargeBean disc = new DiscChargeBean();
                disc.setDacid(rs.getInt("dacid"));
                disc.setArticleOfCharge(rs.getString("article_of_charge"));
                disc.setStatementOfImputation(rs.getString("statement_of_imputation"));
                // disc.setUploadFileName(rs.getString("o_file_name"));
                //disc.setUploadFileId(rs.getInt("attachment_id"));
                disc.setRemark(rs.getString("defence_remark"));
                disc.setWitnessList(getWitnessList(disc.getDacid()));
                disccharge.add(disc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return disccharge;
    }

    @Override
    public DiscChargeBean getChargeDetails(int daId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DiscChargeBean discChargeBean = new DiscChargeBean();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dacid,daid,article_of_charge,statement_of_imputation,memo_of_evidence,description_of_document,defence_remark,article_of_Charge_original_filename,article_of_Charge_disk_filename,article_of_Charge_file_type, "
                    + "statement_Of_Imputation_original_filename,statement_Of_Imputation_disk_filename,statement_Of_Imputation_file_type,memo_of_Evidence_original_filename,"
                    + "memo_ofEvidence_disk_filename,memo_of_Evidence_file_type,Description_of_Document_original_filename,Description_of_Document_disk_filename,Description_of_Document_file_type,off_name,designation,dadid from disciplinary_act_charge where daid= ? ");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                discChargeBean.setDacid(rs.getInt("dacid"));
                discChargeBean.setDaId(rs.getInt("daid"));
                discChargeBean.setArticleOfCharge(rs.getString("article_of_charge"));
                discChargeBean.setArticlesofChargeoriginalfilename(rs.getString("article_of_Charge_original_filename"));
                discChargeBean.setArticlesofChargeContentType(rs.getString("article_of_Charge_file_type"));
                discChargeBean.setStatementOfImputation(rs.getString("statement_of_imputation"));
                discChargeBean.setStatementOfImputationoriginalfilename(rs.getString("statement_Of_Imputation_original_filename"));
                discChargeBean.setStatementOfImputationContentType(rs.getString("statement_Of_Imputation_file_type"));
                discChargeBean.setMemoOfEvidence(rs.getString("memo_of_evidence"));
                discChargeBean.setMemoofEvidenceoriginalfilename(rs.getString("memo_of_Evidence_original_filename"));
                discChargeBean.setMemoofEvidenceContentType(rs.getString("memo_of_Evidence_file_type"));
                discChargeBean.setBriefDescriptionOfDocument(rs.getString("description_of_document"));
                discChargeBean.setDescriptionOfDocumentoriginalfilename(rs.getString("Description_of_Document_original_filename"));
                discChargeBean.setDescriptionOfDocumentContentType(rs.getString("Description_of_Document_file_type"));
                discChargeBean.setOffName(rs.getString("off_name"));
                discChargeBean.setOffHeadDesignation(rs.getString("designation"));
                discChargeBean.setRemark(rs.getString("defence_remark"));
                discChargeBean.setDadid(rs.getInt("dadid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return discChargeBean;
    }

    @Override
    public void saveDelinquentOfficer(ProceedingBean dpBean) {

        int empid = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO disciplinary_delinquent( dadid,daid ,do_hrmsid, do_spc) VALUES(?,?,?,?)");

            for (int i = 0; i < dpBean.getDelinquent().length; i++) {
                pst.setInt(1, getMaxDadId());
                pst.setInt(2, dpBean.getDaId());
                pst.setString(3, dpBean.getDelinquent()[i]);
                pst.setString(4, getSPCFromHRMSId(dpBean.getDelinquent()[i]));
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
    public void saveCourtCaseDetail(CourtCaseBean courtCasebean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String courtDocumentorgFileName = null;
            String courtDocumentdiskFileName = null;
            String courtDocumentFileType = null;

            if (courtCasebean.getCourtDocumentForDP() != null && !courtCasebean.getCourtDocumentForDP().isEmpty()) {
                courtDocumentdiskFileName = new Date().getTime() + "";
                courtDocumentorgFileName = courtCasebean.getCourtDocumentForDP().getOriginalFilename();
                courtDocumentFileType = courtCasebean.getCourtDocumentForDP().getContentType();
                byte[] bytes = courtCasebean.getCourtDocumentForDP().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + courtDocumentdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("INSERT INTO disciplinary_court_case(case_name,financial_year ,court_org_filename, court_disk_filename,court_org_filetype,court_org_filepath,daid,case_no) VALUES(?,?,?,?,?,?,?,?)");
            pstmt.setString(1, courtCasebean.getCaseName());
            pstmt.setString(2, courtCasebean.getFiscalyear());
            pstmt.setString(3, courtDocumentorgFileName);
            pstmt.setString(4, courtDocumentdiskFileName);
            pstmt.setString(5, courtDocumentFileType);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, courtCasebean.getDaId());
            pstmt.setString(8, courtCasebean.getCaseNo());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveMemorandumDetails(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String memorandumoriginalFileName = null;
            String memorandumdiskFileName = null;
            String memorandumcontentType = null;

            if (dpBean.getMemorandumdocument() != null && !dpBean.getMemorandumdocument().isEmpty()) {
                memorandumdiskFileName = new Date().getTime() + "";
                memorandumoriginalFileName = dpBean.getMemorandumdocument().getOriginalFilename();
                memorandumcontentType = dpBean.getMemorandumdocument().getContentType();
                byte[] bytes = dpBean.getMemorandumdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + memorandumdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_authority set auth_hrmsid=?,auth_spc=?,dis_order_no =?, dis_order_date=?,dp_status=61,isapproved='Y',memorandum_org_filename=?,memorandum_disk_filename=?,"
                    + " memorandum_filetype=?,memorandum_filepath=? where daid=?");
            pstmt.setString(1, dpBean.getDahrmsid());
            pstmt.setString(2, dpBean.getDaspc());
            pstmt.setString(3, dpBean.getMemoNo());
            pstmt.setDate(4, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getMemoDate()).getTime()));
            pstmt.setString(5, memorandumoriginalFileName);
            pstmt.setString(6, memorandumdiskFileName);
            pstmt.setString(7, memorandumcontentType);
            pstmt.setString(8, this.uploadPath);
            pstmt.setInt(9, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveFirstShowCauseDetails(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String firstshowcauseoriginalFileName = null;
            String firstshowcausediskFileName = null;
            String firstshowcausecontentType = null;

            if (dpBean.getFirstshowcausedocument() != null && !dpBean.getFirstshowcausedocument().isEmpty()) {
                firstshowcausediskFileName = new Date().getTime() + "";
                firstshowcauseoriginalFileName = dpBean.getFirstshowcausedocument().getOriginalFilename();
                firstshowcausecontentType = dpBean.getFirstshowcausedocument().getContentType();
                byte[] bytes = dpBean.getFirstshowcausedocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + firstshowcausediskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_authority set notice1_send_date =?,dp_status=63,first_scn_original_filename=?,first_scn_disk_fileaname=?,first_scn_file_type=?,first_scn_file_path=?,has_send_notice='Y'  where daid=?");
            pstmt.setDate(1, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getShowCauseOrdDt()).getTime()));
            pstmt.setString(2, firstshowcauseoriginalFileName);
            pstmt.setString(3, firstshowcausediskFileName);
            pstmt.setString(4, firstshowcausecontentType);
            pstmt.setString(5, this.uploadPath);
            pstmt.setInt(6, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveDefenceStatementByDelinquentOfficerOngoingDP(DefenceBean defencebean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String defenceByDOoriginalFileName = null;
            String defenceByDOdiskFileName = null;
            String defenceByDOcontentType = null;

            if (defencebean.getDefenceByDOdocument() != null && !defencebean.getDefenceByDOdocument().isEmpty()) {
                defenceByDOdiskFileName = new Date().getTime() + "";
                defenceByDOoriginalFileName = defencebean.getDefenceByDOdocument().getOriginalFilename();
                defenceByDOcontentType = defencebean.getDefenceByDOdocument().getContentType();
                byte[] bytes = defencebean.getDefenceByDOdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + defenceByDOdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            String rule15MemoQry = ("insert into disciplinary_defence (defence_original_filename,defence_disk_filename,defence_filetype,defence_filepath,defence_statement_on_date,dadid,has_reply_delinquentofficer) values(?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, defenceByDOoriginalFileName);
            pstmt.setString(2, defenceByDOdiskFileName);
            pstmt.setString(3, defenceByDOcontentType);
            pstmt.setString(4, this.uploadPath);
            pstmt.setDate(5, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(defencebean.getWrittenStatemenyByDOOnDt()).getTime()));
            pstmt.setInt(6, defencebean.getDadid());
            pstmt.setString(7, "Y");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveIoDetailsOngoingDP(IoBean ioBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            pstmt = con.prepareStatement("insert into disciplinary_io(io_hrmsid,io_spc,io_appoint_memo_no,io_appoint_memo_date,has_io_appointed,daid,po_hrmsid,po_spc,apo_hrmsid,apo_spc) values(?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, ioBean.getIoEmpHrmsId());
            pstmt.setString(2, ioBean.getIoEmpSPC());
            pstmt.setString(3, ioBean.getIoAppoinmentOrdNo());
            pstmt.setDate(4, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(ioBean.getIoAppoinmentOrdDt()).getTime()));
            pstmt.setString(5, "Y");
            pstmt.setInt(6, ioBean.getDaId());
            pstmt.setString(7, ioBean.getPoHrmsId());
            pstmt.setString(8, ioBean.getPoSPC());
            pstmt.setString(9, ioBean.getApoHrmsId());
            pstmt.setString(10, ioBean.getApoSPC());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveIoRemarksDetailOngoingDP(IoBean ioBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            String remarksByIOoriginalFileName = null;
            String remarksByIOdiskFileName = null;
            String remarksByIOcontentType = null;

            if (ioBean.getRemarksByIOdocument() != null && !ioBean.getRemarksByIOdocument().isEmpty()) {
                remarksByIOdiskFileName = new Date().getTime() + "";
                remarksByIOoriginalFileName = ioBean.getRemarksByIOdocument().getOriginalFilename();
                remarksByIOcontentType = ioBean.getRemarksByIOdocument().getContentType();
                byte[] bytes = ioBean.getRemarksByIOdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + remarksByIOdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("insert into disciplinary_io_remark (daioid,io_remarks_memo_no,io_remarks_memo_date,io_original_filename,io_disk_filename,io_file_type,io_file_path,has_io_remarks,daid) values(?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, ioBean.getDaioid());
            pstmt.setString(2, ioBean.getIoRemarksOrdNo());
            pstmt.setDate(3, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(ioBean.getIoRemarksOrdDt()).getTime()));
            pstmt.setString(4, remarksByIOoriginalFileName);
            pstmt.setString(5, remarksByIOdiskFileName);
            pstmt.setString(6, remarksByIOcontentType);
            pstmt.setString(7, this.uploadPath);
            pstmt.setString(8, "Y");
            pstmt.setInt(9, ioBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveSecondShowCauseDetails(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String noticetoDOOnIoRemarkoriginalFileName = null;
            String noticetoDOOnIoRemarkdiskFileName = null;
            String noticetoDOOnIoRemarkfiletype = null;

            if (dpBean.getNoticetoDOOnIoRemarkdocument() != null && !dpBean.getNoticetoDOOnIoRemarkdocument().isEmpty()) {
                noticetoDOOnIoRemarkdiskFileName = new Date().getTime() + "";
                noticetoDOOnIoRemarkoriginalFileName = dpBean.getNoticetoDOOnIoRemarkdocument().getOriginalFilename();
                noticetoDOOnIoRemarkfiletype = dpBean.getNoticetoDOOnIoRemarkdocument().getContentType();
                byte[] bytes = dpBean.getNoticetoDOOnIoRemarkdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + noticetoDOOnIoRemarkdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            pstmt = con.prepareStatement("update disciplinary_authority set second_notice_send_date =?,has_send_second_notice='Y',ordno_for_notice_on_ioremark=?,orddate_for_notice_on_ioremark=?,orgfilename_for_notice_on_ioremark=?,"
                    + " diskfilename_for_notice_on_ioremark=?,filetype_for_notice_on_ioremark=?,filepath_for_notice_on_ioremark=? where daid=?");
            pstmt.setDate(1, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getSecondshowCauseOrdDt()).getTime()));
            pstmt.setString(2, dpBean.getOrdNoForNoticetoDOOnIoRemark());
            pstmt.setDate(3, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getOrdDtForNoticeOnTODOIoRemark()).getTime()));
            pstmt.setString(4, noticetoDOOnIoRemarkoriginalFileName);
            pstmt.setString(5, noticetoDOOnIoRemarkdiskFileName);
            pstmt.setString(6, noticetoDOOnIoRemarkfiletype);
            pstmt.setString(7, this.uploadPath);
            pstmt.setInt(8, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveRepresentationOfDOOnSecondShowCauseDetails(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            String secondshowcauseoriginalFileNameondoRepresentation = null;
            String secondshowcausediskFileNameondoRepresentation = null;
            String secondshowcausecontentTypeondoRepresentation = null;

            if (dpBean.getSecondshowcausedocumentondoRepresentation() != null && !dpBean.getSecondshowcausedocumentondoRepresentation().isEmpty()) {
                secondshowcausediskFileNameondoRepresentation = new Date().getTime() + "";
                secondshowcauseoriginalFileNameondoRepresentation = dpBean.getSecondshowcausedocumentondoRepresentation().getOriginalFilename();
                secondshowcausecontentTypeondoRepresentation = dpBean.getSecondshowcausedocumentondoRepresentation().getContentType();
                byte[] bytes = dpBean.getSecondshowcausedocumentondoRepresentation().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + secondshowcausediskFileNameondoRepresentation);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

                pstmt = con.prepareStatement("update disciplinary_defence set defence_on_second_showcause_original_filename=?,"
                        + "defence_on_second_showcause_disk_filename=?,defence_on_second_showcause_filetype=?,defence_on_second_showcause_filepath=? where dadid=?");
                pstmt.setString(1, secondshowcauseoriginalFileNameondoRepresentation);
                pstmt.setString(2, secondshowcausediskFileNameondoRepresentation);
                pstmt.setString(3, secondshowcausecontentTypeondoRepresentation);
                pstmt.setString(4, this.uploadPath);
                pstmt.setInt(5, dpBean.getDadid());
                pstmt.executeUpdate();
            }
            pstmt = con.prepareStatement("update disciplinary_defence set defence_statement_on_second_showcause_date=?,has_reply_delinquentofficer_on_second_showcause='Y',defence_statement_on_second_showcause_ordno=? where dadid=?");
            pstmt.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getDoRepresentationOnsecondshowCauseOrdDt()).getTime()));
            pstmt.setString(2, dpBean.getDoRepresentationOnsecondshowCauseOrdNo());
            pstmt.setInt(3, dpBean.getDadid());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveConsultationDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            String consultationOriginalfilenameOnRepresentationOfDoOnIoReport = null;
            String consultationdiskfilenameOnRepresentationOfDoOnIoReport = null;
            String consultationfiletypeOnRepresentationOfDoOnIoReport = null;

            if (dpBean.getConsultationdocumentOnRepresentationOfDoOnIoReport() != null && !dpBean.getConsultationdocumentOnRepresentationOfDoOnIoReport().isEmpty()) {
                consultationdiskfilenameOnRepresentationOfDoOnIoReport = new Date().getTime() + "";
                consultationOriginalfilenameOnRepresentationOfDoOnIoReport = dpBean.getConsultationdocumentOnRepresentationOfDoOnIoReport().getOriginalFilename();
                consultationfiletypeOnRepresentationOfDoOnIoReport = dpBean.getConsultationdocumentOnRepresentationOfDoOnIoReport().getContentType();
                byte[] bytes = dpBean.getConsultationdocumentOnRepresentationOfDoOnIoReport().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + consultationdiskfilenameOnRepresentationOfDoOnIoReport);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            pstmt = con.prepareStatement("update disciplinary_authority set consultation_ordno_on_representationofdo_on_ioreport=?,consultation_orddate_on_representationofdo_on_ioreport=?,consultation_originalfilename_on_representationofdo_on_ioreport=?,consultation_diskfilename_on_representationofdo_on_ioreport=?,"
                    + "consultation_filetype_on_representationofdo_on_ioreport=?,consultation_filepath_on_representationofdo_on_ioreport=? where daid=?");
            pstmt.setString(1, dpBean.getConsultationOrdnoOnRepresentationOfDoOnIoReport());
            pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getConsultationOrddateOnRepresentationOfDoOnIoReport()).getTime()));
            pstmt.setString(3, consultationOriginalfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(4, consultationdiskfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(5, consultationfiletypeOnRepresentationOfDoOnIoReport);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveConcurranceDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            String concurranceOriginalfilenameOnRepresentationOfDoOnIoReport = null;
            String concurrancediskfilenameOnRepresentationOfDoOnIoReport = null;
            String concurrancefiletypeOnRepresentationOfDoOnIoReport = null;

            if (dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport() != null && !dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().isEmpty()) {
                concurrancediskfilenameOnRepresentationOfDoOnIoReport = new Date().getTime() + "";
                concurranceOriginalfilenameOnRepresentationOfDoOnIoReport = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getOriginalFilename();
                concurrancefiletypeOnRepresentationOfDoOnIoReport = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getContentType();
                byte[] bytes = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + concurrancediskfilenameOnRepresentationOfDoOnIoReport);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_authority set concurrance_ordno_on_representationofdo_on_ioreport=?,concurrance_orddate_on_representationofdo_on_ioreport=?,concurrance_originalfilename_on_representationofdo_on_ioreport=?,concurrance_diskfilename_on_representationofdo_on_ioreport=?,"
                    + "concurrance_filetype_on_representationofdo_on_ioreport=?,concurrance_filepath_on_representationofdo_on_ioreport=? where daid=?");
            pstmt.setString(1, dpBean.getConcurranceOrdnoOnRepresentationOfDoOnIoReport());
            pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getConcurranceOrddateOnRepresentationOfDoOnIoReport()).getTime()));
            pstmt.setString(3, concurranceOriginalfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(4, concurrancediskfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(5, concurrancefiletypeOnRepresentationOfDoOnIoReport);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveFinalOrderDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            String finalOriginalfilenameOnRepresentationOfDoOnIoReport = null;
            String finaldiskfilenameOnRepresentationOfDoOnIoReport = null;
            String finalfiletypeOnRepresentationOfDoOnIoReport = null;

            if (dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport() != null && !dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().isEmpty()) {
                finaldiskfilenameOnRepresentationOfDoOnIoReport = new Date().getTime() + "";
                finalOriginalfilenameOnRepresentationOfDoOnIoReport = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getOriginalFilename();
                finalfiletypeOnRepresentationOfDoOnIoReport = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getContentType();
                byte[] bytes = dpBean.getConcurrancedocumentOnRepresentationOfDoOnIoReport().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + finaldiskfilenameOnRepresentationOfDoOnIoReport);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_authority set final_ordno_on_representationofdo_on_ioreport=?,final_orddate_on_representationofdo_on_ioreport=?,final_originalfilename_on_representationofdo_on_ioreport=?,final_diskfilename_on_representationofdo_on_ioreport=?,"
                    + "final_filetype_on_representationofdo_on_ioreport=?,final_filepath_on_representationofdo_on_ioreport=? where daid=?");
            pstmt.setString(1, dpBean.getFinalOrdnoOnRepresentationOfDoOnIoReport());
            pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getFinalOrddateOnRepresentationOfDoOnIoReport()).getTime()));
            pstmt.setString(3, finalOriginalfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(4, finaldiskfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(5, finalfiletypeOnRepresentationOfDoOnIoReport);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, dpBean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveExanaurationFinalOrderDetailsOnRepresentationofdoOnIoreport(ProceedingBean dpBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            String exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport = null;
            String exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport = null;
            String exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport = null;

            if (dpBean.getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport() != null && !dpBean.getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport().isEmpty()) {
                exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport = new Date().getTime() + "";
                exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport = dpBean.getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport().getOriginalFilename();
                exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport = dpBean.getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport().getContentType();
                byte[] bytes = dpBean.getExanaurationfinaldocumentOnRepresentationOfDoOnIoReport().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_authority set exanaurationfinal_ordno_on_representationofdo_on_ioreport=?,exanaurationfinal_orddate_on_representationofdo_on_ioreport=?,exanaurationfinal_orgfilename_on_representationofdo_on_ioreport=?,exanaurationfinal_diskfilename_on_representationofdo_on_iorepor=?,"
                    + "exanaurationfinal_filetype_on_representationofdo_on_ioreport=?,exanaurationfinal_filepath_on_representationofdo_on_ioreport=? where daid=?");
            pstmt.setString(1, dpBean.getExanaurationfinalOrdnoOnRepresentationOfDoOnIoReport());
            pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getExanaurationfinalOrddateOnRepresentationOfDoOnIoReport()).getTime()));
            pstmt.setString(3, exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(4, exanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(5, exanaurationfinalfiletypeOnRepresentationOfDoOnIoReport);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, dpBean.getDaId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void saveThirdShowCauseDetails(ProceedingBean pbean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String thirdNoticetoDOOnForPunishmentorgFileName = null;
            String thirdNoticetoDOOnForPunishmentdiskFileName = null;
            String thirdNoticetoDOOnForPunishmentFileType = null;

            if (pbean.getThirdNoticetoDOOnForPunishmentdocument() != null && !pbean.getThirdNoticetoDOOnForPunishmentdocument().isEmpty()) {
                thirdNoticetoDOOnForPunishmentdiskFileName = new Date().getTime() + "";
                thirdNoticetoDOOnForPunishmentorgFileName = pbean.getThirdNoticetoDOOnForPunishmentdocument().getOriginalFilename();
                thirdNoticetoDOOnForPunishmentFileType = pbean.getThirdNoticetoDOOnForPunishmentdocument().getContentType();
                byte[] bytes = pbean.getThirdNoticetoDOOnForPunishmentdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + thirdNoticetoDOOnForPunishmentdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            pstmt = con.prepareStatement("update disciplinary_authority set third_showcause_orddt =?,has_send_third_showcause='Y',third_noticeto_do_for_punishment_ord_no=?,third_noticeto_do_for_punishment_ord_dt=?,"
                    + " third_noticeto_do_for_punishment_orgfilename=?,third_noticeto_do_for_punishment_diskfilename=?,third_noticeto_do_for_punishment_filetype=?,third_noticeto_do_for_punishment_filepath=? where daid=?");
            pstmt.setDate(1, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(pbean.getThirdshowCauseOrdDt()).getTime()));
            pstmt.setString(2, pbean.getOrdNoForthirdNoticetoDOOnForPunishment());
            pstmt.setDate(3, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(pbean.getOrdDtForthirdNoticetoDOOnForPunishment()).getTime()));
            pstmt.setString(4, thirdNoticetoDOOnForPunishmentorgFileName);
            pstmt.setString(5, thirdNoticetoDOOnForPunishmentdiskFileName);
            pstmt.setString(6, thirdNoticetoDOOnForPunishmentFileType);
            pstmt.setString(7, this.uploadPath);
            pstmt.setInt(8, pbean.getDaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void saveThirdShowReplyByDelinquentOfficer(ProceedingBean pbean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();

            String thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport = null;
            String thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport = null;
            String thirdshowcausefiletypeOnRepresentationOfDoOnIoReport = null;

            if (pbean.getThirdshowcausedocumentondoRepresentation() != null && !pbean.getThirdshowcausedocumentondoRepresentation().isEmpty()) {
                thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport = new Date().getTime() + "";
                thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport = pbean.getThirdshowcausedocumentondoRepresentation().getOriginalFilename();
                thirdshowcausefiletypeOnRepresentationOfDoOnIoReport = pbean.getThirdshowcausedocumentondoRepresentation().getContentType();
                byte[] bytes = pbean.getThirdshowcausedocumentondoRepresentation().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            pstmt = con.prepareStatement("update disciplinary_defence set third_showcause_da_reply_ordno=?,third_showcause_da_reply_ondate=?,third_scn_original_filename=?,third_scn_disk_fileaname=?,"
                    + "third_scn_file_type=?,third_scn_file_path=?,has_reply_third_showcause='Y' where dadid=?");
            pstmt.setString(1, pbean.getThirdshowCauseReplyByDAordNo());
            pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(pbean.getThirdshowCauseReplyByDAOrdDt()).getTime()));
            pstmt.setString(3, thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(4, thirdshowcausediskfilenameOnRepresentationOfDoOnIoReport);
            pstmt.setString(5, thirdshowcausefiletypeOnRepresentationOfDoOnIoReport);
            pstmt.setString(6, this.uploadPath);
            pstmt.setInt(7, pbean.getDadid());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ProceedingBean getDiscProceedingData(int daId) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT  auth_hrmsid,auth_spc,dis_order_no,dis_order_date,dp_status,notice1_send_date,first_scn_original_filename,first_scn_disk_fileaname,first_scn_file_type,get_empname_from_type(auth_hrmsid,'G') as authname,getgpostnamefromspc(auth_spc) as authpost,"
                    + " isapproved,has_send_notice,second_notice_send_date,has_send_second_notice,third_showcause_orddt,has_send_third_showcause,ordno_for_notice_on_ioremark,orddate_for_notice_on_ioremark,orgfilename_for_notice_on_ioremark,"
                    + " diskfilename_for_notice_on_ioremark,filetype_for_notice_on_ioremark,third_noticeto_do_for_punishment_ord_no,third_noticeto_do_for_punishment_ord_dt,third_noticeto_do_for_punishment_orgfilename,third_noticeto_do_for_punishment_diskfilename,third_noticeto_do_for_punishment_filetype,"
                    + " memorandum_org_filename,memorandum_disk_filename,memorandum_filetype from disciplinary_authority WHERE daid = ?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setDaId(daId);
                if (rs.getString("authname") != null) {
                    pbean.setDisciplinaryauthority(rs.getString("authname") + "," + rs.getString("authpost"));
                }
                pbean.setDahrmsid(rs.getString("auth_hrmsid"));
                pbean.setDaspc(rs.getString("auth_spc"));
                pbean.setMemoNo(rs.getString("dis_order_no"));
                pbean.setMemoDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dis_order_date")));
                pbean.setDpStatus(rs.getString("dp_status"));
                pbean.setShowCauseOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("notice1_send_date")));
                pbean.setFirstshowcauseoriginalFileName(rs.getString("first_scn_original_filename"));
                pbean.setFirstshowcausediskFileName(rs.getString("first_scn_disk_fileaname"));
                pbean.setFirstshowcausecontentType(rs.getString("first_scn_file_type"));
                pbean.setIsAuthorityApprove(rs.getString("isapproved"));
                pbean.setHasSendNoticetoDO(rs.getString("has_send_notice"));
                pbean.setSecondshowCauseOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("second_notice_send_date")));
                pbean.setHasSendSecondNotice(rs.getString("has_send_second_notice"));
                pbean.setThirdshowCauseOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("third_showcause_orddt")));
                pbean.setHasSendthirdshowCause(rs.getString("has_send_third_showcause"));
                pbean.setOrdNoForNoticetoDOOnIoRemark(rs.getString("ordno_for_notice_on_ioremark"));
                pbean.setOrdDtForNoticeOnTODOIoRemark(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddate_for_notice_on_ioremark")));
                pbean.setNoticetoDOOnIoRemarkoriginalFileName(rs.getString("orgfilename_for_notice_on_ioremark"));
                pbean.setNoticetoDOOnIoRemarkdiskFileName(rs.getString("diskfilename_for_notice_on_ioremark"));
                pbean.setNoticetoDOOnIoRemarkfiletype(rs.getString("filetype_for_notice_on_ioremark"));
                pbean.setOrdNoForthirdNoticetoDOOnForPunishment(rs.getString("third_noticeto_do_for_punishment_ord_no"));
                pbean.setOrdDtForthirdNoticetoDOOnForPunishment(CommonFunctions.getFormattedOutputDate1(rs.getDate("third_noticeto_do_for_punishment_ord_dt")));
                pbean.setThirdNoticetoDOOnForPunishmentorgFileName(rs.getString("third_noticeto_do_for_punishment_orgfilename"));
                pbean.setThirdNoticetoDOOnForPunishmentdiskFileName(rs.getString("third_noticeto_do_for_punishment_diskfilename"));
                pbean.setThirdNoticetoDOOnForPunishmentFileType(rs.getString("third_noticeto_do_for_punishment_filetype"));
                pbean.setMemorandumoriginalFileName(rs.getString("memorandum_org_filename"));
                pbean.setMemorandumdiskFileName(rs.getString("memorandum_disk_filename"));
                pbean.setMemorandumcontentType(rs.getString("memorandum_filetype"));

            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT disciplinary_defence.dadid, defid,defence_original_filename,defence_disk_filename,defence_filetype,defence_statement_on_date,has_reply_delinquentofficer from disciplinary_defence "
                    + " inner join disciplinary_delinquent on disciplinary_defence.dadid = disciplinary_delinquent.dadid WHERE daid =?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setDadid(rs.getInt("dadid"));
                pbean.setDefid(rs.getInt("defid"));
                pbean.setDefenceByDOoriginalFileName(rs.getString("defence_original_filename"));
                pbean.setDefenceByDOdiskFileName(rs.getString("defence_disk_filename"));
                pbean.setDefenceByDOcontentType(rs.getString("defence_filetype"));
                pbean.setWrittenStatemenyByDOOnDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("defence_statement_on_date")));
                pbean.setHasReplyByDelinquentOfficer(rs.getString("has_reply_delinquentofficer"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT io_hrmsid,io_spc,io_appoint_memo_no,io_appoint_memo_date,daioid,has_io_appointed,po_hrmsid,po_spc,apo_hrmsid,apo_spc,get_empname_from_type(io_hrmsid,'G') as inquiryauthname,getgpostnamefromspc(io_spc) as inquiryauthpost,getgpostnamefromspc(po_spc) as presentingauthpost,getgpostnamefromspc(apo_spc) as addpresentingauthpost,get_empname_from_type(po_hrmsid,'G') as presentingauthname,get_empname_from_type(apo_hrmsid,'G') as addtionalpresentingauthname from disciplinary_io where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setIoEmpHrmsId(rs.getString("io_hrmsid"));
                pbean.setIoEmpSPC(rs.getString("io_spc"));
                pbean.setIoAppoinmentOrdNo(rs.getString("io_appoint_memo_no"));
                pbean.setIoAppoinmentOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("io_appoint_memo_date")));
                pbean.setDaioid(rs.getInt("daioid"));
                pbean.setHasIoAppointed(rs.getString("has_io_appointed"));
                pbean.setPoHrmsId(rs.getString("po_hrmsid"));
                pbean.setPoSPC(rs.getString("po_spc"));
                pbean.setApoHrmsId(rs.getString("apo_hrmsid"));
                pbean.setApoSPC(rs.getString("apo_spc"));
                if (rs.getString("inquiryauthname") != null) {
                    pbean.setInquiryauthority(rs.getString("inquiryauthname") + "," + rs.getString("inquiryauthpost"));
                }
                if (rs.getString("presentingauthname") != null) {
                    pbean.setPresentingauthority(rs.getString("presentingauthname") + "," + rs.getString("presentingauthpost"));
                }
                if (rs.getString("addtionalpresentingauthname") != null) {
                    pbean.setAdditionalpresentingauthority(rs.getString("addtionalpresentingauthname") + "," + rs.getString("addpresentingauthpost"));
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT io_remarks_memo_no,io_remarks_memo_date,io_original_filename,io_disk_filename,io_file_type,has_io_remarks,io_remark_id from disciplinary_io_remark where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setIoRemarksOrdNo(rs.getString("io_remarks_memo_no"));
                pbean.setIoRemarksOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("io_remarks_memo_date")));
                pbean.setRemarksByIOoriginalFileName(rs.getString("io_original_filename"));
                pbean.setRemarksByIOdiskFileName(rs.getString("io_disk_filename"));
                pbean.setRemarksByIOcontentType(rs.getString("io_file_type"));
                pbean.setHasIoRemarks(rs.getString("has_io_remarks"));
                pbean.setIoRemarksId(rs.getInt("io_remark_id"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            pstmt = con.prepareStatement("SELECT disciplinary_defence.dadid,defence_statement_on_second_showcause_date,has_reply_delinquentofficer_on_second_showcause,defence_statement_on_second_showcause_ordno,"
                    + " defence_on_second_showcause_original_filename,defence_on_second_showcause_disk_filename,defence_on_second_showcause_filetype from disciplinary_defence "
                    + " inner join disciplinary_delinquent on disciplinary_defence.dadid = disciplinary_delinquent.dadid WHERE daid =?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setDoRepresentationOnsecondshowCauseOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("defence_statement_on_second_showcause_date")));
                pbean.setHasDoRepresentOnsecondshowCause(rs.getString("has_reply_delinquentofficer_on_second_showcause"));
                pbean.setDoRepresentationOnsecondshowCauseOrdNo(rs.getString("defence_statement_on_second_showcause_ordno"));
                pbean.setSecondshowcauseoriginalFileNameondoRepresentation(rs.getString("defence_on_second_showcause_original_filename"));
                pbean.setSecondshowcausediskFileNameondoRepresentation(rs.getString("defence_on_second_showcause_disk_filename"));
                pbean.setSecondshowcausecontentTypeondoRepresentation(rs.getString("defence_on_second_showcause_filetype"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT exanaurationfinal_ordno_on_representationofdo_on_ioreport,exanaurationfinal_orddate_on_representationofdo_on_ioreport,exanaurationfinal_orgfilename_on_representationofdo_on_ioreport,"
                    + " exanaurationfinal_diskfilename_on_representationofdo_on_iorepor,exanaurationfinal_filetype_on_representationofdo_on_ioreport from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setExanaurationfinalOrdnoOnRepresentationOfDoOnIoReport(rs.getString("exanaurationfinal_ordno_on_representationofdo_on_ioreport"));
                pbean.setExanaurationfinalOrddateOnRepresentationOfDoOnIoReport(CommonFunctions.getFormattedOutputDate1(rs.getDate("exanaurationfinal_orddate_on_representationofdo_on_ioreport")));
                pbean.setExanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("exanaurationfinal_orgfilename_on_representationofdo_on_ioreport"));
                pbean.setExanaurationfinaldiskfilenameOnRepresentationOfDoOnIoReport(rs.getString("exanaurationfinal_diskfilename_on_representationofdo_on_iorepor"));
                pbean.setExanaurationfinalfiletypeOnRepresentationOfDoOnIoReport(rs.getString("exanaurationfinal_filetype_on_representationofdo_on_ioreport"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT consultation_ordno_on_representationofdo_on_ioreport,consultation_orddate_on_representationofdo_on_ioreport,consultation_originalfilename_on_representationofdo_on_ioreport,"
                    + " consultation_diskfilename_on_representationofdo_on_ioreport,consultation_filetype_on_representationofdo_on_ioreport from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setConsultationOrdnoOnRepresentationOfDoOnIoReport(rs.getString("consultation_ordno_on_representationofdo_on_ioreport"));
                pbean.setConsultationOrddateOnRepresentationOfDoOnIoReport(CommonFunctions.getFormattedOutputDate1(rs.getDate("consultation_orddate_on_representationofdo_on_ioreport")));
                pbean.setConsultationOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("consultation_originalfilename_on_representationofdo_on_ioreport"));
                pbean.setConsultationdiskfilenameOnRepresentationOfDoOnIoReport(rs.getString("consultation_diskfilename_on_representationofdo_on_ioreport"));
                pbean.setConsultationfiletypeOnRepresentationOfDoOnIoReport(rs.getString("consultation_filetype_on_representationofdo_on_ioreport"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            pstmt = con.prepareStatement("SELECT concurrance_ordno_on_representationofdo_on_ioreport,concurrance_orddate_on_representationofdo_on_ioreport,concurrance_originalfilename_on_representationofdo_on_ioreport,"
                    + " concurrance_diskfilename_on_representationofdo_on_ioreport,concurrance_filetype_on_representationofdo_on_ioreport from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pbean.setConcurranceOrdnoOnRepresentationOfDoOnIoReport(rs.getString("concurrance_ordno_on_representationofdo_on_ioreport"));
                pbean.setConcurranceOrddateOnRepresentationOfDoOnIoReport(CommonFunctions.getFormattedOutputDate1(rs.getDate("concurrance_orddate_on_representationofdo_on_ioreport")));
                pbean.setConcurranceOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("concurrance_originalfilename_on_representationofdo_on_ioreport"));
                pbean.setConcurrancediskfilenameOnRepresentationOfDoOnIoReport(rs.getString("concurrance_diskfilename_on_representationofdo_on_ioreport"));
                pbean.setConcurrancefiletypeOnRepresentationOfDoOnIoReport(rs.getString("concurrance_filetype_on_representationofdo_on_ioreport"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            pstmt = con.prepareStatement("SELECT final_ordno_on_representationofdo_on_ioreport,final_orddate_on_representationofdo_on_ioreport,final_originalfilename_on_representationofdo_on_ioreport,"
                    + " final_diskfilename_on_representationofdo_on_ioreport,final_filetype_on_representationofdo_on_ioreport from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pbean.setFinalOrdnoOnRepresentationOfDoOnIoReport(rs.getString("final_ordno_on_representationofdo_on_ioreport"));
                pbean.setFinalOrddateOnRepresentationOfDoOnIoReport(CommonFunctions.getFormattedOutputDate1(rs.getDate("final_orddate_on_representationofdo_on_ioreport")));
                pbean.setFinalOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("final_originalfilename_on_representationofdo_on_ioreport"));
                pbean.setFinaldiskfilenameOnRepresentationOfDoOnIoReport(rs.getString("final_diskfilename_on_representationofdo_on_ioreport"));
                pbean.setFinalfiletypeOnRepresentationOfDoOnIoReport(rs.getString("final_filetype_on_representationofdo_on_ioreport"));

            }
            DataBaseFunctions.closeSqlObjects(pstmt);

            pstmt = con.prepareStatement("SELECT disciplinary_defence.dadid,third_scn_original_filename,third_scn_disk_fileaname,third_scn_file_type,third_showcause_da_reply_ondate,"
                    + " third_showcause_da_reply_ordno,has_reply_third_showcause from disciplinary_defence"
                    + " inner join disciplinary_delinquent on disciplinary_defence.dadid = disciplinary_delinquent.dadid WHERE daid =?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pbean.setDadid(rs.getInt("dadid"));
                pbean.setThirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("third_scn_original_filename"));
                pbean.setThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport(rs.getString("third_scn_disk_fileaname"));
                pbean.setThirdshowcausefiletypeOnRepresentationOfDoOnIoReport(rs.getString("third_scn_file_type"));
                pbean.setThirdshowCauseReplyByDAOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("third_showcause_da_reply_ondate")));
                pbean.setThirdshowCauseReplyByDAordNo(rs.getString("third_showcause_da_reply_ordno"));
                pbean.setHasReplyByDothirdshowCause(rs.getString("has_reply_third_showcause"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return pbean;
    }

    @Override
    public ArrayList getCourtCaseDetail(int daId) {
        ArrayList courtcaseList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT case_id,case_no,case_name,financial_year,court_org_filename,court_disk_filename,court_org_filetype from disciplinary_court_case WHERE daid =?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CourtCaseBean courtCasebean = new CourtCaseBean();
                courtCasebean.setCaseId(rs.getInt("case_id"));
                courtCasebean.setCaseNo(rs.getString("case_no"));
                courtCasebean.setCaseName(rs.getString("case_name"));
                courtCasebean.setFiscalyear(rs.getString("financial_year"));
                courtCasebean.setCourtDocumentorgFileName(rs.getString("court_org_filename"));
                courtCasebean.setCourtDocumentdiskFileName(rs.getString("court_disk_filename"));
                courtCasebean.setCourtDocumentFileType(rs.getString("court_org_filetype"));
                courtcaseList.add(courtCasebean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return courtcaseList;
    }

    @Override
    public CourtCaseBean getAttachedFileForCourtCase(int caseId) {
        CourtCaseBean courtCaseBean = new CourtCaseBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT case_no,court_org_filename,court_disk_filename,court_org_filetype,court_org_filepath from disciplinary_court_case WHERE case_id=?");
            pst.setInt(1, caseId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                courtCaseBean.setCaseId(caseId);
                courtCaseBean.setCourtDocumentorgFileName(rs.getString("court_org_filename"));
                courtCaseBean.setCourtDocumentdiskFileName(rs.getString("court_disk_filename"));
                courtCaseBean.setCourtDocumentFileType("court_org_filetype");
                filepath = rs.getString("court_org_filepath");
            }
            File f = new File(filepath + File.separator + courtCaseBean.getCourtDocumentdiskFileName());
            courtCaseBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return courtCaseBean;
    }

    @Override
    public ProceedingBean getAttachedFileForMemorandumDetails(int daId) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT memorandum_org_filename,memorandum_disk_filename,memorandum_filetype,memorandum_filepath from disciplinary_authority WHERE daid=?");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setMemorandumoriginalFileName(rs.getString("memorandum_org_filename"));
                pbean.setMemorandumdiskFileName(rs.getString("memorandum_disk_filename"));
                pbean.setMemorandumcontentType("memorandum_filetype");
                filepath = rs.getString("memorandum_filepath");
            }
            File f = new File(filepath + File.separator + pbean.getMemorandumdiskFileName());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public ProceedingBean getAttachedFileForFirstShowCauseDetails(int daId) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT first_scn_original_filename,first_scn_disk_fileaname,first_scn_file_type,first_scn_file_path from disciplinary_authority WHERE daid=?");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setFirstshowcauseoriginalFileName(rs.getString("first_scn_original_filename"));
                pbean.setFirstshowcausediskFileName(rs.getString("first_scn_disk_fileaname"));
                pbean.setFirstshowcausecontentType("first_scn_file_type");
                filepath = rs.getString("first_scn_file_path");
            }
            File f = new File(filepath + File.separator + pbean.getFirstshowcausediskFileName());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public DefenceBean getAttachedFileForDefenceStatementByDelinquentOfficerDetails(int dadid) {
        DefenceBean defencebean = new DefenceBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT defence_original_filename,defence_disk_filename,defence_filetype,defence_filepath from disciplinary_defence WHERE dadid=?");
            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                defencebean.setDefenceByDOoriginalFileName(rs.getString("defence_original_filename"));
                defencebean.setDefenceByDOdiskFileName(rs.getString("defence_disk_filename"));
                defencebean.setDefenceByDOcontentType("defence_filetype");
                filepath = rs.getString("defence_filepath");
            }
            File f = new File(filepath + File.separator + defencebean.getDefenceByDOdiskFileName());
            defencebean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return defencebean;
    }

    @Override
    public IoBean getAttachedFileForIoRemarksDetailOngoingDPDetails(int daid) {
        IoBean ioBean = new IoBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT io_original_filename,io_disk_filename,io_file_type,io_file_path from disciplinary_io_remark where daid=?");
            pst.setInt(1, daid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                ioBean.setRemarksByIOoriginalFileName(rs.getString("io_original_filename"));
                ioBean.setRemarksByIOdiskFileName(rs.getString("io_disk_filename"));
                ioBean.setRemarksByIOcontentType("io_file_type");
                filepath = rs.getString("io_file_path");
            }
            File f = new File(filepath + File.separator + ioBean.getRemarksByIOdiskFileName());
            ioBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ioBean;
    }

    @Override
    public ProceedingBean getAttachedFileForSecondShowCauseDetails(int daId) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT orgfilename_for_notice_on_ioremark,diskfilename_for_notice_on_ioremark,filetype_for_notice_on_ioremark,filepath_for_notice_on_ioremark from disciplinary_authority WHERE daid=?");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setNoticetoDOOnIoRemarkoriginalFileName(rs.getString("orgfilename_for_notice_on_ioremark"));
                pbean.setNoticetoDOOnIoRemarkdiskFileName(rs.getString("diskfilename_for_notice_on_ioremark"));
                pbean.setNoticetoDOOnIoRemarkfiletype("filetype_for_notice_on_ioremark");
                filepath = rs.getString("filepath_for_notice_on_ioremark");
            }
            File f = new File(filepath + File.separator + pbean.getNoticetoDOOnIoRemarkdiskFileName());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public ProceedingBean getAttachedFileForRepresentationOfDOOnSecondShowCause(int dadid) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT defence_on_second_showcause_original_filename,defence_on_second_showcause_disk_filename,defence_on_second_showcause_filetype,defence_on_second_showcause_filepath from disciplinary_defence WHERE dadid=?");
            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setSecondshowcauseoriginalFileNameondoRepresentation(rs.getString("defence_on_second_showcause_original_filename"));
                pbean.setSecondshowcausediskFileNameondoRepresentation(rs.getString("defence_on_second_showcause_disk_filename"));
                pbean.setSecondshowcausecontentTypeondoRepresentation("defence_on_second_showcause_filetype");
                filepath = rs.getString("defence_on_second_showcause_filepath");
            }
            File f = new File(filepath + File.separator + pbean.getSecondshowcausediskFileNameondoRepresentation());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public ProceedingBean getAttachedFileForThirdShowCauseDetails(int daId) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT third_noticeto_do_for_punishment_orgfilename,third_noticeto_do_for_punishment_diskfilename,third_noticeto_do_for_punishment_filetype,third_noticeto_do_for_punishment_filepath from disciplinary_authority WHERE daid=?");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setThirdNoticetoDOOnForPunishmentorgFileName(rs.getString("third_noticeto_do_for_punishment_orgfilename"));
                pbean.setThirdNoticetoDOOnForPunishmentdiskFileName(rs.getString("third_noticeto_do_for_punishment_diskfilename"));
                pbean.setThirdNoticetoDOOnForPunishmentFileType("third_noticeto_do_for_punishment_filetype");
                filepath = rs.getString("third_noticeto_do_for_punishment_filepath");
            }
            File f = new File(filepath + File.separator + pbean.getThirdNoticetoDOOnForPunishmentdiskFileName());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public ProceedingBean getAttachedFileForThirdShowReplyByDelinquentOfficer(int dadid) {
        ProceedingBean pbean = new ProceedingBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT third_scn_original_filename,third_scn_disk_fileaname,third_scn_file_type,third_scn_file_path from disciplinary_defence WHERE dadid=?");
            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pbean.setThirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport(rs.getString("third_scn_original_filename"));
                pbean.setThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport(rs.getString("third_scn_disk_fileaname"));
                pbean.setThirdshowcausefiletypeOnRepresentationOfDoOnIoReport("third_scn_file_type");
                filepath = rs.getString("third_scn_file_path");
            }
            File f = new File(filepath + File.separator + pbean.getThirdshowcausediskfilenameOnRepresentationOfDoOnIoReport());
            pbean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pbean;
    }

    @Override
    public DefenceBean getDefenceStatementByDOOngoingDP(int dadid) {
        DefenceBean defencebean = new DefenceBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT defid,defence_original_filename,defence_disk_filename,defence_filetype,defence_statement_on_date,has_reply_delinquentofficer from disciplinary_defence WHERE dadid =?");
            pstmt.setInt(1, dadid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                defencebean.setDefid(rs.getInt("defid"));
                defencebean.setDefenceByDOoriginalFileName(rs.getString("defence_original_filename"));
                defencebean.setDefenceByDOdiskFileName(rs.getString("defence_disk_filename"));
                defencebean.setDefenceByDOcontentType(rs.getString("defence_filetype"));
                defencebean.setWrittenStatemenyByDOOnDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("defence_statement_on_date")));
                defencebean.setHasReplyByDelinquentOfficer(rs.getString("has_reply_delinquentofficer"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return defencebean;
    }

    @Override
    public IoBean getIoDetailsOngoingDP(int daid) {
        IoBean ioBean = new IoBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT io_appoint_memo_no,io_appoint_memo_date,daioid,has_io_remarks from disciplinary_io where daid=?");
            pstmt.setInt(1, daid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ioBean.setIoAppoinmentOrdNo(rs.getString("io_appoint_memo_no"));
                ioBean.setIoAppoinmentOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("io_appoint_memo_date")));
                ioBean.setDaioid(rs.getInt("daioid"));
                ioBean.setHasIoRemarks(rs.getString("has_io_remarks"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return ioBean;
    }

    public IoBean getIoRemarksDetailOngoingDP(int ioremarkId) {
        IoBean ioBean = new IoBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT io_original_filename,io_disk_filename,io_file_type from disciplinary_io_remark where io_remark_id=?");
            pstmt.setInt(1, ioremarkId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ioBean.setIoAppoinmentOrdNo(rs.getString("io_appoint_memo_no"));
                ioBean.setIoAppoinmentOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("io_appoint_memo_date")));
                ioBean.setDaioid(rs.getInt("daioid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return ioBean;
    }

    @Override
    public void deleteDelinquentOfficer(ProceedingBean dpBean
    ) {
        PreparedStatement pst = null;
        Connection con = null;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM disciplinary_delinquent WHERE daid=? AND do_hrmsid=?");
            for (int i = 0; i < dpBean.getDelinquent().length; i++) {
                pst.setInt(1, dpBean.getDaId());
                pst.setString(2, dpBean.getDelinquent()[i]);
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
    public ArrayList getInvestingOfficer(int daId
    ) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList investingOfficer = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select io_hrmsid,io_spc,f_name,m_name, l_name, spn,is_type   from disciplinary_io "
                    + "inner join emp_mast on disciplinary_io.io_hrmsid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_spc on disciplinary_io.io_spc = g_spc.spc where daid=? ");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                IoBean iobean = new IoBean();
                iobean.setIoEmpHrmsId(rs.getString("io_hrmsid"));
                iobean.setIoEmpSPC(rs.getString("io_spc"));
                String fullName = "";
                if (rs.getString("F_NAME") != null && !rs.getString("F_NAME").equals("")) {
                    fullName = fullName + " " + rs.getString("F_NAME");
                }
                if (rs.getString("M_NAME") != null && !rs.getString("M_NAME").equals("")) {
                    fullName = fullName + " " + rs.getString("M_NAME");
                }
                if (rs.getString("L_NAME") != null && !rs.getString("L_NAME").equals("")) {
                    fullName = fullName + " " + rs.getString("L_NAME");
                }
                iobean.setIoEmpName(fullName);
                iobean.setIoEmpCurDegn(rs.getString("spn"));
                iobean.setOfficertype(rs.getString("is_type"));
                investingOfficer.add(iobean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return investingOfficer;

    }
    /*unused*/

    @Override
    public Employee getDelinquentOfficerDtl(int daid, String empId
    ) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Employee emp = new Employee();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dadid, do_hrmsid,do_spc,initials,f_name,m_name, l_name, spn  from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_spc on disciplinary_delinquent.do_spc = g_spc.spc where daid=? and do_hrmsid=?");
            pst.setInt(1, daid);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                emp.setEmpid(rs.getString("do_hrmsid"));
                emp.setSpc(rs.getString("do_spc"));
                emp.setSpn(rs.getString("spn"));
                emp.setIntitals(rs.getString("initials"));
                emp.setFname(rs.getString("f_name"));
                emp.setMname(rs.getString("m_name"));
                emp.setLname(rs.getString("l_name"));
                emp.setRefid(rs.getString("dadid"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    @Override
    public Employee getDelinquentOfficerDtl(int dadid
    ) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Employee emp = new Employee();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dadid, do_hrmsid,do_spc,initials,f_name,m_name, l_name, spn  from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_spc on disciplinary_delinquent.do_spc = g_spc.spc where dadid=? ");
            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            while (rs.next()) {
                emp.setEmpid(rs.getString("do_hrmsid"));
                emp.setSpc(rs.getString("do_spc"));
                emp.setSpn(rs.getString("spn"));
                emp.setIntitals(rs.getString("initials"));
                emp.setFname(rs.getString("f_name"));
                emp.setMname(rs.getString("m_name"));
                emp.setLname(rs.getString("l_name"));
                emp.setRefid(rs.getString("dadid"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    public Employee[] getDelinquentOfficerForOngoing(int daId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Employee> delinquentofficerList = new ArrayList<Employee>();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dadid, do_hrmsid,do_spc,initials,f_name,m_name, l_name, spn  from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_spc on disciplinary_delinquent.do_spc = g_spc.spc where daid=? ");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setRefid(rs.getString("dadid"));
                emp.setEmpid(rs.getString("do_hrmsid"));
                emp.setSpc(rs.getString("do_spc"));
                emp.setSpn(rs.getString("spn"));
                emp.setIntitals(rs.getString("initials"));
                emp.setFname(rs.getString("f_name"));
                emp.setMname(rs.getString("m_name"));
                emp.setLname(rs.getString("l_name"));
                delinquentofficerList.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        Employee[] employees = new Employee[delinquentofficerList.size()];
        employees = delinquentofficerList.toArray(employees);
        return employees;
    }

    @Override
    public ArrayList getDelinquentOfficer(int daId
    ) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList delinquentOfficer = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dadid, do_hrmsid,do_spc,initials,f_name,m_name, l_name, spn  from disciplinary_delinquent "
                    + "inner join emp_mast on disciplinary_delinquent.do_hrmsid=emp_mast.emp_id "
                    + "LEFT OUTER JOIN g_spc on disciplinary_delinquent.do_spc = g_spc.spc where daid=? ");
            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmpid(rs.getString("do_hrmsid"));
                emp.setSpc(rs.getString("do_spc"));
                emp.setSpn(rs.getString("spn"));
                emp.setIntitals(rs.getString("initials"));
                emp.setFname(rs.getString("f_name"));
                emp.setMname(rs.getString("m_name"));
                emp.setLname(rs.getString("l_name"));
                emp.setRefid(rs.getString("dadid"));
                delinquentOfficer.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return delinquentOfficer;
    }

    @Override
    public int saveRule15MemoDetails(ProceedingBean dpBean) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        try {
            con = dataSource.getConnection();
            res = getMaxDaId();
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            String rule15MemoQry = "INSERT INTO disciplinary_authority (daid, init_hrmsid, init_spc,  dis_initiated_date , dis_type, under_rule, isactive, dp_status,task_id) VALUES (?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, res);
            pstmt.setString(2, dpBean.getInitHrmsId());
            pstmt.setString(3, dpBean.getInitSpc());
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setString(5, "DIS");
            pstmt.setString(6, dpBean.getRule());
            pstmt.setString(7, "Y");
            pstmt.setString(8, "0"); // 55 = Initiated 
            pstmt.setInt(9, 0);
            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public void updateRule15MemoDetails(ProceedingBean dpBean
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("update disciplinary_authority set dis_order_no=?, dis_order_date=?, irre_details=? where daid=?");
            pstmt.setString(1, dpBean.getMemoNo());
            if (dpBean.getMemoDate() != null && !dpBean.getMemoDate().equals("")) {
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(dpBean.getMemoDate()).getTime()));
            } else {
                pstmt.setTimestamp(2, null);
            }
            pstmt.setString(3, dpBean.getAnnex1Charge());
            pstmt.setInt(4, dpBean.getDaId());
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ProceedingBean getRule15MemoDetails(String offCode, String[] empids
    ) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ProceedingBean pbean = new ProceedingBean();

        try {
            con = dataSource.getConnection();
            String deptNameQry = "SELECT department_name FROM g_office OFFICE INNER JOIN g_department DEPT ON "
                    + "OFFICE.department_code=DEPT.department_code WHERE off_code=?";

            pstmt = con.prepareStatement(deptNameQry);
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                pbean.setDeptName(rs.getString("department_name"));
                pbean.setOffCode(offCode);
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            String empQry = "SELECT EMP_ID,INITIALS, F_NAME, M_NAME, L_NAME FROM EMP_MAST WHERE EMP_ID=?";
            pstmt = con.prepareStatement(empQry);
            ArrayList employeeList = new ArrayList();
            for (int i = 0; i < empids.length; i++) {
                pstmt.setString(1, empids[i]);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmpid(rs.getString("EMP_ID"));
                    employee.setIntitals(rs.getString("INITIALS"));
                    employee.setFname(rs.getString("F_NAME"));
                    employee.setMname(rs.getString("M_NAME"));
                    employee.setLname(rs.getString("L_NAME"));
                    employeeList.add(employee);
                }
            }
            pbean.setEmployees(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pbean;
    }

    @Override
    public List getDPForwadrEmpList(String offCode
    ) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        DiscProceedingBean dpBean = null;

        try {
            con = dataSource.getConnection();
            String empListQry = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post "
                    + "from g_office GE INNER JOIN emp_mast EM ON GE.off_code=EM.cur_off_code "
                    + "LEFT OUTER JOIN g_spc GS ON EM.cur_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where GE.department_code=? ORDER BY F_NAME";

            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dpBean = new DiscProceedingBean();

                //dpBean.setDoHrmsId(rs.getString("emp_id"));
                dpBean.setDoEmpName(rs.getString("EMPNAME"));
                dpBean.setDoEmpCurDegn(rs.getString("post"));

                empList.add(dpBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public List getPostWiseEmpList(String curSpc
    ) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        DiscProceedingBean dpBean = null;

        try {
            con = dataSource.getConnection();
            String empListQry = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME from emp_mast where cur_spc=?";
            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, curSpc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dpBean = new DiscProceedingBean();

                //dpBean.setDoHrmsId(rs.getString("emp_id"));
                dpBean.setDoEmpName(rs.getString("EMPNAME"));

                empList.add(dpBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public List getOffWiseEmpWitnessList(String offCode, String doHrmsId, int dacId, String mode
    ) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        DiscProceedingBean dpBean = null;
        String empListQry = "";

        try {
            con = dataSource.getConnection();
            if (mode.equals("E")) {
                empListQry = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_id,post,dacid from emp_mast emp "
                        + "LEFT OUTER JOIN disciplinary_act_charge_witness dw ON emp.emp_id=dw.witness_hrmsid "
                        + "LEFT OUTER JOIN g_spc GS ON emp.cur_spc=GS.spc "
                        + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc "
                        + "where cur_off_code=? and emp.emp_id!=? ORDER BY F_NAME";
                pstmt = con.prepareStatement(empListQry);
                pstmt.setString(1, offCode);
                pstmt.setString(2, doHrmsId);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    dpBean = new DiscProceedingBean();

                    String emHrmsId = rs.getString("emp_id");
                    if (rs.getInt("dacid") == dacId) {
                        dpBean.setChkVal(1);
                    }

                    //dpBean.setDoHrmsId(emHrmsId);
                    dpBean.setDoEmpName(rs.getString("EMPNAME"));
                    dpBean.setDoEmpCurDegn(rs.getString("post"));

                    empList.add(dpBean);
                }

            } else {
                empListQry = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post from emp_mast EM "
                        + "LEFT OUTER JOIN g_spc GS ON EM.cur_spc=GS.spc LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc "
                        + "where cur_off_code=? and emp_id!=? ORDER BY F_NAME";
                pstmt = con.prepareStatement(empListQry);
                pstmt.setString(1, offCode);
                pstmt.setString(2, doHrmsId);

                rs = pstmt.executeQuery();
                while (rs.next()) {
                    dpBean = new DiscProceedingBean();

                    //dpBean.setDoHrmsId(rs.getString("emp_id"));
                    dpBean.setChkVal(0);
                    dpBean.setDoEmpName(rs.getString("EMPNAME"));
                    dpBean.setDoEmpCurDegn(rs.getString("post"));

                    empList.add(dpBean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public List getDiscProcedingFinalList(String empId, int page, int rows) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        DiscProceedingBean dpBean = null;
        String dType = "";
        String nameAndDegn = "";
        String forwardNameDegn = "";
        try {
            String eol = System.getProperty("line.separator");
            con = dataSource.getConnection();
            int minlimit = rows * (page - 1);
            int maxlimit = rows;

            String doListQry = "select daid, dis_initiated_date,dis_type,under_rule,dp_status,dis_initiated_date,disciplinary_authority.task_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,spn,initiated_on,status_name,disc_proceeding_type from disciplinary_authority "
                    + "left outer join task_master on disciplinary_authority.task_id = task_master.task_id "
                    + "LEFT OUTER JOIN emp_mast EM ON task_master.apply_to=EM.emp_id "
                    + "LEFT OUTER JOIN g_spc GS ON task_master.apply_to_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc "
                    + "LEFT OUTER JOIN g_process_status ON task_master.status_id = g_process_status.status_id "
                    + "where (init_hrmsid=? or auth_hrmsid=?) AND isactive='Y' ORDER BY dis_initiated_date DESC ";
            pstmt = con.prepareStatement(doListQry);
            pstmt.setString(1, empId);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dpBean = new DiscProceedingBean();
                dpBean.setDaid(rs.getInt("daid"));

                dType = rs.getString("dis_type");
                if (dType.equals("DIS")) {
                    dpBean.setDisType("Disciplinary");
                } else {
                    dpBean.setDisType("Suspension");
                }
                dpBean.setTaskId(rs.getInt("task_id"));
                dpBean.setUnderRule(rs.getString("under_rule"));
                dpBean.setDpStatus(rs.getString("status_name"));
                dpBean.setMode(rs.getString("disc_proceeding_type"));
                dpBean.setDisciplinaryInitiatedOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dis_initiated_date")));
                String fullName = rs.getString("FULL_NAME");
                String spn = rs.getString("spn");
                String forwardNameAndDegn = "";
                if (fullName != null) {
                    forwardNameAndDegn = fullName;
                }
                if (spn != null) {
                    forwardNameAndDegn = spn;
                }
                if (fullName != null && spn != null) {
                    forwardNameAndDegn = fullName + ", " + spn;
                }

                dpBean.setForwardNameAndDegn(forwardNameAndDegn);
                dpBean.setForwardDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("initiated_on")));
                empList.add(dpBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public int getDiscProceedTotalCount(String empId
    ) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totCount = 0;

        try {
            con = dataSource.getConnection();
            String countQry = "select count(*) totalCount from disciplinary_authority DA INNER JOIN emp_mast EM1 ON EM1.emp_id=DA.do_hrmsid "
                    + "LEFT OUTER JOIN g_spc GS1 ON EM1.cur_spc=GS1.spc LEFT OUTER JOIN g_post GP1 ON GP1.post_code= GS1.gpc "
                    + "LEFT OUTER JOIN task_master TM ON TM.task_id=DA.task_id LEFT OUTER JOIN emp_mast EM2 ON EM2.emp_id=TM.APPLY_TO "
                    + "LEFT OUTER JOIN g_spc GS2 ON EM2.cur_spc=GS2.spc LEFT OUTER JOIN g_post GP2 ON GP2.post_code= GS2.gpc "
                    + "where init_hrmsid=? AND isactive='Y'";
            pstmt = con.prepareStatement(countQry);
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                totCount = rs.getInt("totalCount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totCount;
    }

    @Override
    public int getMaxDaId() {

        Connection con = null;
        int maxId = 0;
        try {
            con = dataSource.getConnection();
            maxId = CommonFunctions.getMaxCode(con, "disciplinary_authority", "daid");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxId;
    }

    public String getSPCFromHRMSId(String hrmsId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String spc = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CUR_SPC FROM EMP_MAST WHERE EMP_ID=?");
            pstmt.setString(1, hrmsId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                spc = rs.getString("CUR_SPC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return spc;
    }

    public int getMaxDadId() {

        Connection con = null;
        int maxId = 0;
        try {
            con = dataSource.getConnection();
            maxId = CommonFunctions.getMaxCode(con, "disciplinary_delinquent", "dadid");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxId;
    }

    private int getMaxDacId() {

        Connection con = null;
        int maxId = 0;
        try {
            con = dataSource.getConnection();
            maxId = CommonFunctions.getMaxCode(con, "disciplinary_act_charge", "dacid");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxId;
    }

    @Override
    public ArrayList getEmpComboDtls(String offCode, String doHrmsId) {
        ArrayList al = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String empListQry = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,cur_spc from emp_mast EM "
                    + "LEFT OUTER JOIN g_spc GS ON EM.cur_spc=GS.spc LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc"
                    + " where cur_off_code=? and emp_id!=?";

            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, offCode);
            pstmt.setString(2, doHrmsId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("EMPNAME") + "," + rs.getString("post"));
                so.setValue(rs.getString("emp_id") + "~" + rs.getString("cur_spc"));
                al.add(so);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public int saveRule15AddCharges(Rule15ChargeBean chargeBean, String filePath) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        int result = 0;
        MultipartFile docFile = chargeBean.getRule15Document();
        MultipartFile docFile1 = chargeBean.getRule15SubDoc();
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            String dirpath = filePath + "DP" + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }

            con = dataSource.getConnection();
            String chargesQry = "INSERT INTO disciplinary_act_charge (dacid, daid, charge, charge_details) VALUES(?,?,?,?)";
            pstmt = con.prepareStatement(chargesQry);

            int dacId = CommonFunctions.getMaxCode(con, "disciplinary_act_charge", "dacid");
            pstmt.setInt(1, dacId);
            pstmt.setInt(2, chargeBean.getHidChargeDaid());
            pstmt.setString(3, chargeBean.getRule15Articles());
            pstmt.setString(4, chargeBean.getRule15ChargDtls());

            result = pstmt.executeUpdate();

            String filename = "";
            long time = System.currentTimeMillis();

            String insDocQry = "INSERT INTO disciplinary_charge_document (dacid,do_hrmsid,disk_file_name,org_file_name,file_type,doc_type) VALUES(?,?,?,?,?,?)";

            if (docFile != null && !docFile.isEmpty()) {
                pstmt1 = con.prepareStatement(insDocQry);
                pstmt1.setInt(1, dacId);
                String dohrmsId = chargeBean.getHidChargeDoHrmsId();
                pstmt1.setString(2, dohrmsId);

                filename = dohrmsId + "_" + dacId + "_" + time;
                pstmt1.setString(3, filename);
                pstmt1.setString(4, docFile.getOriginalFilename());
                pstmt1.setString(5, docFile.getContentType());
                pstmt1.setString(6, "D");
                int onlyDocIns = pstmt1.executeUpdate();

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = docFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

            } else if (docFile1 != null && !docFile1.isEmpty()) {
                pstmt1 = con.prepareStatement(insDocQry);
                pstmt1.setInt(1, dacId);
                String dohrmsId = chargeBean.getHidChargeDoHrmsId();
                pstmt1.setString(2, dohrmsId);

                filename = dohrmsId + "_" + dacId + "_" + time;
                pstmt1.setString(3, filename);
                pstmt1.setString(4, docFile1.getOriginalFilename());
                pstmt1.setString(5, docFile1.getContentType());
                pstmt1.setString(6, "S");
                int onlySdocIns = pstmt1.executeUpdate();

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = docFile1.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public int saveRule15AddWitness(Rule15ChargeBean chargeBean) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;
        String witnessId = "";
        try {
            con = dataSource.getConnection();
            String witnessQry = "INSERT INTO disciplinary_act_charge_witness (dacwid, dacid, witness_hrmsid, witness_spc) VALUES(?,?,?,?)";
            pstmt = con.prepareStatement(witnessQry);

            String witnessHrmsIds = chargeBean.getHidWitnessIds();
            StringTokenizer stringTokenizer = new StringTokenizer(witnessHrmsIds, ",");
            while (stringTokenizer.hasMoreTokens()) {
                witnessId = stringTokenizer.nextToken().trim();
                pstmt.setInt(1, CommonFunctions.getMaxCode(con, "disciplinary_act_charge_witness", "dacwid"));
                pstmt.setInt(2, chargeBean.getHidWitnessDacId());
                pstmt.setString(3, witnessId);
                pstmt.setString(4, getCurSpc(witnessId));
                result = pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    private String getCurSpc(String witnessId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String curSpc = "";
        try {
            con = dataSource.getConnection();
            String curSpcQry = "select cur_spc from emp_mast where emp_id=?";
            pstmt = con.prepareStatement(curSpcQry);
            pstmt.setString(1, witnessId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                curSpc = rs.getString("cur_spc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return curSpc;
    }

    @Override
    public Rule15ChargeBean EditRule15ChargeData(Rule15ChargeBean chargeBean, int dacId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        List al = new ArrayList();
        List updAl = new ArrayList();

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String chargeQry = "select charge, charge_details from disciplinary_act_charge where dacid=?";
            pstmt = con.prepareStatement(chargeQry);
            pstmt.setInt(1, dacId);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                chargeBean.setRule15Articles(rs.getString("charge"));
                chargeBean.setRule15ChargDtls(rs.getString("charge_details"));
                al = getDPAttachedFileName(stmt, dacId);

                FileAttachBean faBean = null;
                //ChargeWitnesBean cwBean = null;
//                for(int i=0; i< al.size(); i++){
//                    cwBean = (ChargeWitnesBean)al.get(i);
//                    String fName=cwBean.getFname();
//                    String dType=cwBean.getDtype();

//                    
//                    faBean = new FileAttachBean();
//                    faBean.setOrgFileName(fName);
//                    faBean.setDocType(dType);
//                    updAl.add(faBean);
//                    
//                    chargeBean.setDocumentList(al);
//                    
//                }
                Iterator itr = al.iterator();
                while (itr.hasNext()) {

                    faBean = (FileAttachBean) itr.next();
                    String fName = faBean.getOrgFileName();
                    String dType = faBean.getDocType();

                    faBean.setOrgFileName(fName);
                    faBean.setDocType(dType);
                    updAl.add(faBean);

                    chargeBean.setDocumentList(updAl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chargeBean;
    }

    public List getDPAttachedFileName(Statement stmt, int dacId) {

        ResultSet rs = null;
        FileAttachBean faBean;
        List fileName = new ArrayList();
        try {
            String sql = "SELECT ORG_FILE_NAME, doc_type FROM disciplinary_charge_document WHERE dacid='" + dacId + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                faBean = new FileAttachBean();

                faBean.setOrgFileName(rs.getString("ORG_FILE_NAME"));
                faBean.setDocType(rs.getString("doc_type"));

                fileName.add(faBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public int updateRule15Charges(Rule15ChargeBean chargeBean) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            con = dataSource.getConnection();
            String updChargeQry = "update disciplinary_act_charge set charge=?, charge_details=? where dacid=?";
            pstmt = con.prepareStatement(updChargeQry);
            pstmt.setString(1, chargeBean.getRule15Articles());
            pstmt.setString(2, chargeBean.getRule15ChargDtls());
            pstmt.setInt(3, chargeBean.getHidChargeDacId());

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public int rule15UpdateWitness(Rule15ChargeBean chargeBean) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        int delRes = 0;
        int insRes = 0;
        String witnessId = "";
        try {
            con = dataSource.getConnection();
            int dacId = chargeBean.getHidWitnessDacId();

            String delWitnessQry = "delete from disciplinary_act_charge_witness where dacid=?";
            pstmt1 = con.prepareStatement(delWitnessQry);
            pstmt1.setInt(1, dacId);
            delRes = pstmt1.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pstmt1);

            String witnessQry = "INSERT INTO disciplinary_act_charge_witness (dacwid, dacid, witness_hrmsid, witness_spc) VALUES(?,?,?,?)";
            pstmt = con.prepareStatement(witnessQry);

            String witnessHrmsIds = chargeBean.getHidWitnessIds();
            StringTokenizer stringTokenizer = new StringTokenizer(witnessHrmsIds, ",");
            while (stringTokenizer.hasMoreTokens()) {

                witnessId = stringTokenizer.nextToken().trim();
                pstmt.setInt(1, CommonFunctions.getMaxCode(con, "disciplinary_act_charge_witness", "dacwid"));
                pstmt.setInt(2, dacId);
                pstmt.setString(3, witnessId);
                pstmt.setString(4, getCurSpc(witnessId));
                insRes = pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return insRes;
    }

    @Override
    public Rule15ChargeBean EditRule15WitnessData(Rule15ChargeBean chargeBean, int dacId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            String chargeQry = "select charge, charge_details from disciplinary_act_charge where dacid=?";
            pstmt = con.prepareStatement(chargeQry);
            pstmt.setInt(1, dacId);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                chargeBean.setRule15Articles(rs.getString("charge"));
                chargeBean.setRule15ChargDtls(rs.getString("charge_details"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chargeBean;
    }

    @Override
    public DpViewBean getDpdetail(int daId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DpViewBean dpViewBean = new DpViewBean();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select auth_hrmsid, auth_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,post,gender from disciplinary_authority DD"
                    + " INNER JOIN emp_mast EM ON DD.auth_hrmsid=EM.emp_id"
                    + " LEFT OUTER JOIN g_spc GS ON DD.auth_spc=GS.spc"
                    + " LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dpViewBean.setInitAuthHrmsId(rs.getString("auth_hrmsid"));
                dpViewBean.setInitAuthName(rs.getString("FULL_NAME"));
                dpViewBean.setInitAuthCurDegn(rs.getString("post"));
                if (rs.getString("gender") != null && rs.getString("gender").equals("M")) {
                    dpViewBean.setGender("he");
                    dpViewBean.setGender1("his");
                } else if (rs.getString("gender") != null && rs.getString("gender").equals("F")) {
                    dpViewBean.setGender("she");
                    dpViewBean.setGender1("her");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return dpViewBean;
    }

    public DpViewBean viewRule15DiscProceeding(int daId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DpViewBean dpViewBean = new DpViewBean();

        try {
            con = dataSource.getConnection();
            //auth_hrmsid  auth_spc
            pstmt = con.prepareStatement("select dis_order_no,dis_order_date, irre_details , init_hrmsid, auth_hrmsid, auth_spc,"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,post,getfullname(auth_hrmsid) as authfullname, getspn(auth_spc) as authdesign, gender,isapproved from disciplinary_authority DD"
                    + " INNER JOIN emp_mast EM ON DD.init_hrmsid=EM.emp_id"
                    + " LEFT OUTER JOIN g_spc GS ON DD.init_spc=GS.spc"
                    + " LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dpViewBean.setRule15OrderNo(rs.getString("dis_order_no"));
                dpViewBean.setRule15OrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dis_order_date")));
                dpViewBean.setIrrgularDetails(rs.getString("irre_details"));
                dpViewBean.setInitAuthHrmsId(rs.getString("init_hrmsid"));
                dpViewBean.setInitAuthName(rs.getString("FULL_NAME"));
                dpViewBean.setInitAuthCurDegn(rs.getString("post"));
                dpViewBean.setApproveAuthHrmsId(rs.getString("auth_hrmsid"));
                dpViewBean.setApproveAuthCurDegn(rs.getString("authdesign"));
                dpViewBean.setApproveAuthName(rs.getString("authfullname"));
                dpViewBean.setIsapprovedbyauthority(rs.getString("isapproved"));
                if (rs.getString("gender") != null && rs.getString("gender").equals("M")) {
                    dpViewBean.setGender("he");
                    dpViewBean.setGender1("his");
                } else if (rs.getString("gender") != null && rs.getString("gender").equals("F")) {
                    dpViewBean.setGender("she");
                    dpViewBean.setGender1("her");
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT do_hrmsid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post from disciplinary_delinquent DD "
                    + "INNER JOIN emp_mast EM ON DD.do_hrmsid=EM.emp_id "
                    + "LEFT OUTER JOIN g_spc GS ON DD.do_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();

            List delinquentList = new ArrayList();
            while (rs.next()) {
                DelinquentBean db = new DelinquentBean();
                db.setDoEmpHrmsId(rs.getString("do_hrmsid"));
                db.setDoEmpName(rs.getString("EMPNAME"));
                db.setDoEmpCurDegn(rs.getString("post"));

                delinquentList.add(db);
            }
            dpViewBean.setDelinquent(delinquentList);

            List chargeListOnly = new ArrayList();

            String chargeListQry = "select article_of_charge, statement_of_imputation,memo_of_evidence,description_of_document from disciplinary_act_charge where daid=? order by article_of_charge asc";
            pstmt = con.prepareStatement(chargeListQry);
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DiscChargeBean tBean = new DiscChargeBean();
                tBean.setArticleOfCharge(rs.getString("article_of_charge"));
                tBean.setStatementOfImputation(rs.getString("statement_of_imputation"));
                tBean.setMemoOfEvidence(rs.getString("memo_of_evidence"));
                tBean.setBriefDescriptionOfDocument(rs.getString("description_of_document"));
                chargeListOnly.add(tBean);
            }
            dpViewBean.setChargeListOnly(chargeListOnly);


            /*
             int dacId = 0;
             List chargeList = new ArrayList();
             String fileAndWitnessQry = "select DAC.dacid dacId,charge,org_file_name from disciplinary_act_charge DAC "
             + "INNER JOIN disciplinary_charge_document DCD ON DAC.dacid=DCD.dacid where daid=? order by charge asc";
             pstmt = con.prepareStatement(fileAndWitnessQry);
             pstmt.setInt(1, Integer.parseInt(daId));
             rs = pstmt.executeQuery();
             while (rs.next()) {
             tBean = new ChargeWitnesBean();

             dacId = rs.getInt("dacId");

             tBean.setCharge(rs.getString("charge"));
             tBean.setOrgFileName(rs.getString("org_file_name"));
             tBean.setWitnessName(getWitnessNames(con, dacId));

             chargeList.add(tBean);
             }
             dpViewBean.setChargeList(chargeList);
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return dpViewBean;
    }

    @Override
    public DpViewBean viewRule15DiscProceeding(String offCode, String daId, int taskId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DpViewBean dpViewBean = new DpViewBean();
        dpViewBean.setDaId(Integer.parseInt(daId));
        try {
            con = dataSource.getConnection();
            String deptNameQry = "SELECT department_name FROM g_office OFFICE INNER JOIN g_department DEPT ON "
                    + "OFFICE.department_code=DEPT.department_code WHERE off_code=?";

            pstmt = con.prepareStatement(deptNameQry);
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dpViewBean.setDeptName(rs.getString("department_name"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("select getspn(auth_spc) as authspn,dis_order_no,dis_order_date, irre_details , init_hrmsid, init_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,post,gender,isapproved,init_spc from disciplinary_authority DD"
                    + " INNER JOIN emp_mast EM ON DD.init_hrmsid=EM.emp_id"
                    + " LEFT OUTER JOIN g_spc GS ON DD.init_spc=GS.spc"
                    + " LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where daid=?");
            pstmt.setInt(1, Integer.parseInt(daId));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dpViewBean.setRule15OrderNo(rs.getString("dis_order_no"));
                dpViewBean.setRule15OrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dis_order_date")));
                dpViewBean.setIrrgularDetails(rs.getString("irre_details"));
                dpViewBean.setInitAuthHrmsId(rs.getString("init_hrmsid"));
                dpViewBean.setInitAuthName(rs.getString("FULL_NAME"));
                dpViewBean.setInitAuthCurSpc(rs.getString("init_spc"));
                dpViewBean.setInitAuthCurDegn(rs.getString("post"));
                dpViewBean.setApproveAuthCurDegn(rs.getString("authspn"));
                dpViewBean.setIsapprovedbyauthority(rs.getString("isapproved"));
                if (rs.getString("gender") != null && rs.getString("gender").equals("M")) {
                    dpViewBean.setGender("he");
                    dpViewBean.setGender1("him");
                    dpViewBean.setGender2("his");

                } else if (rs.getString("gender") != null && rs.getString("gender").equals("F")) {
                    dpViewBean.setGender("she");
                    dpViewBean.setGender1("her");
                    dpViewBean.setGender2("her");
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            pstmt = con.prepareStatement("SELECT dadid,OFF_EN,do_hrmsid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,DD.do_spc,gender from disciplinary_delinquent DD "
                    + "INNER JOIN emp_mast EM ON DD.do_hrmsid=EM.emp_id "
                    + "LEFT OUTER JOIN g_spc GS ON DD.do_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc "
                    + "LEFT OUTER JOIN G_OFFICE ON EM.CUR_OFF_CODE=G_OFFICE.OFF_CODE where daid=?");
            pstmt.setInt(1, Integer.parseInt(daId));
            rs = pstmt.executeQuery();

            List delinquentList = new ArrayList();
            while (rs.next()) {
                DelinquentBean db = new DelinquentBean();
                db.setDadid(rs.getInt("dadid"));
                db.setDoEmpHrmsId(rs.getString("do_hrmsid"));
                db.setDoEmpName(rs.getString("EMPNAME"));
                db.setDoEmpCurDegn(rs.getString("post"));
                db.setOfficeName(rs.getString("OFF_EN"));
                db.setDoEmpSpc(rs.getString("do_spc"));
                if (rs.getString("gender") != null && rs.getString("gender").equals("M")) {
                    db.setGender("he");
                    db.setGender1("him");
                    db.setGender2("his");

                } else if (rs.getString("gender") != null && rs.getString("gender").equals("F")) {
                    db.setGender("she");
                    db.setGender1("her");
                    db.setGender2("her");
                }
                delinquentList.add(db);
            }
            dpViewBean.setDelinquent(delinquentList);
            pstmt = con.prepareStatement(" select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,post,EM.emp_id,task_master.apply_to_spc from task_master "
                    + " INNER JOIN emp_mast EM ON task_master.apply_to=EM.emp_id"
                    + " LEFT OUTER JOIN g_spc GS ON task_master.apply_to_spc=GS.spc"
                    + " LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where task_id = ?");
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();

            //List delinquentList = new ArrayList();
            if (rs.next()) {
                dpViewBean.setApproveAuthCurSpc(rs.getString("apply_to_spc"));
                dpViewBean.setApproveAuthHrmsId(rs.getString("emp_id"));
                dpViewBean.setApproveAuthName(rs.getString("FULL_NAME"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement(" SELECT emp_id,daioid,io_hrmsid,io_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FULL_NAME,gender,post from disciplinary_io DI  "
                    + " INNER JOIN emp_mast EM ON DI.io_hrmsid=EM.emp_id "
                    + "LEFT OUTER JOIN g_spc GS ON DI.io_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where daioid = ?");
            pstmt.setInt(1, Integer.parseInt(daId));
            rs = pstmt.executeQuery();

            ArrayList ios = new ArrayList();
            while (rs.next()) {
                IoBean iobean = new IoBean();
                iobean.setIoEmpHrmsId(rs.getString("io_hrmsid"));
                iobean.setIoEmpSPC(rs.getString("io_spc"));
                iobean.setIoEmpHrmsId(rs.getString("emp_id"));
                iobean.setIoEmpName(rs.getString("FULL_NAME"));
                iobean.setIoEmpCurDegn(rs.getString("post"));
                ios.add(iobean);
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            dpViewBean.setIos(ios);

            List chargeListOnly = new ArrayList();

            String chargeListQry = "select dacid,article_of_charge,statement_of_imputation,memo_of_evidence,description_of_document from disciplinary_act_charge where daid=? order by article_of_charge asc";
            pstmt = con.prepareStatement(chargeListQry);
            pstmt.setInt(1, Integer.parseInt(daId));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DiscChargeBean tBean = new DiscChargeBean();
                tBean.setDacid(rs.getInt("dacid"));
                tBean.setArticleOfCharge(rs.getString("article_of_charge"));
                tBean.setStatementOfImputation(rs.getString("statement_of_imputation"));
                tBean.setMemoOfEvidence(rs.getString("memo_of_evidence"));
                tBean.setBriefDescriptionOfDocument(rs.getString("description_of_document"));
                chargeListOnly.add(tBean);
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            dpViewBean.setChargeListOnly(chargeListOnly);

            List penalty = new ArrayList();

            String penality = "select dopenaltyid,description from disciplinary_penalty  where daid = ? ";
            pstmt = con.prepareStatement(penality);
            pstmt.setInt(1, Integer.parseInt(daId));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                NoticeBean notice = new NoticeBean();

                notice.setDoPenaltyId(rs.getInt("dopenaltyid"));
                notice.setDescription(rs.getString("description"));
                penalty.add(notice);
            }
            dpViewBean.setPenalty(penalty);

            /*
             int dacId = 0;
             List chargeList = new ArrayList();
             String fileAndWitnessQry = "select DAC.dacid dacId,charge,org_file_name from disciplinary_act_charge DAC "
             + "INNER JOIN disciplinary_charge_document DCD ON DAC.dacid=DCD.dacid where daid=? order by charge asc";
             pstmt = con.prepareStatement(fileAndWitnessQry);
             pstmt.setInt(1, Integer.parseInt(daId));
             rs = pstmt.executeQuery();
             while (rs.next()) {
             tBean = new ChargeWitnesBean();

             dacId = rs.getInt("dacId");

             tBean.setCharge(rs.getString("charge"));
             tBean.setOrgFileName(rs.getString("org_file_name"));
             tBean.setWitnessName(getWitnessNames(con, dacId));

             chargeList.add(tBean);
             }
             dpViewBean.setChargeList(chargeList);
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return dpViewBean;
    }

    private String getWitnessNames(Connection con, int dacId) {

        PreparedStatement pstmt3 = null;
        ResultSet rs3 = null;
        String witness = "";

        try {
            String witnessQry = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME, spn from emp_mast EM "
                    + "INNER JOIN disciplinary_act_charge_witness DACW ON EM.emp_id=DACW.witness_hrmsid "
                    + "LEFT OUTER JOIN g_spc GS ON DACW.witness_spc=GS.spc where dacid=?";
            pstmt3 = con.prepareStatement(witnessQry);
            pstmt3.setInt(1, dacId);
            rs3 = pstmt3.executeQuery();
            while (rs3.next()) {

                String spn = rs3.getString("spn"); // spn means emp desgn with office address
                if (spn == null || spn.equals("null")) {
                    spn = "";
                }
                witness = witness + rs3.getString("EMPNAME") + " " + spn;
                witness = witness + "<br/>";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return witness;
    }

    @Override
    public int deleteChargeAndWitness(int dacId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        int delResult = 0;
        try {
            con = dataSource.getConnection();
            String delWitnessQry = "delete from disciplinary_act_charge_witness where dacid=?";
            pstmt = con.prepareStatement(delWitnessQry);
            pstmt.setInt(1, dacId);
            int wRes = pstmt.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pstmt);

            String delChargeQry = "delete from disciplinary_act_charge where dacid=?";
            pstmt1 = con.prepareStatement(delChargeQry);
            pstmt1.setInt(1, dacId);
            int cRes = pstmt1.executeUpdate();

            if (wRes == 1 && cRes == 1) {
                delResult = 1;
            } else {
                delResult = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return delResult;
    }

    @Override
    public int deleteWitnessOnly(int dacId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int delResult = 0;
        try {
            con = dataSource.getConnection();
            String delWitnessQry = "delete from disciplinary_act_charge_witness where dacid=?";
            pstmt = con.prepareStatement(delWitnessQry);
            pstmt.setInt(1, dacId);
            int wRes = pstmt.executeUpdate();

            if (wRes == 1) {
                delResult = 1;
            } else {
                delResult = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return delResult;
    }

    public void deleteRule15Proceeding(ProceedingBean pbean) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from disciplinary_act_charge where daid=?");
            pst.setInt(1, pbean.getDaId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("delete from disciplinary_authority where daid=?");
            pst.setInt(1, pbean.getDaId());

            pst.executeUpdate();

            pst = con.prepareStatement("delete from disciplinary_defence where dadid=?");
            pst.setInt(1, pbean.getDadid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("delete from disciplinary_io where daid=?");
            pst.setInt(1, pbean.getDaId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("delete from disciplinary_io_remark where daid=?");
            pst.setInt(1, pbean.getDaId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public int getDaidFromTaskid(int taskid) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int daid = 0;
        try {
            con = dataSource.getConnection();
            String curSpcQry = "select daid from disciplinary_authority where task_id=?";
            pstmt = con.prepareStatement(curSpcQry);
            pstmt.setInt(1, taskid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                daid = rs.getInt("daid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return daid;

    }

    public int getDaidFromDaioid(int daioid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int daid = 0;
        try {
            con = dataSource.getConnection();
            String curSpcQry = "select daid  from  disciplinary_authority  DA"
                    + " INNER JOIN disciplinary_io_remark DR ON DA .daid=DR.daioid where daioid = ?";

            pstmt = con.prepareStatement(curSpcQry);
            pstmt.setInt(1, daioid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                daid = rs.getInt("daid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return daid;

    }

    public int getRefidFromTaskid(int taskid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int refid = 0;
        try {
            con = dataSource.getConnection();
            String curSpcQry = "select ref_id from task_master where task_id=?";
            pstmt = con.prepareStatement(curSpcQry);
            pstmt.setInt(1, taskid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                refid = rs.getInt("ref_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return refid;
    }

    public int getDaidAndDefIdFromTaskid(int taskid) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int daid = 0;
        try {
            con = dataSource.getConnection();
            String curSpcQry = "select daid from task_master "
                    + "INNER JOIN disciplinary_defence ON task_master.REF_ID=disciplinary_defence.defid "
                    + "INNER JOIN disciplinary_delinquent ON disciplinary_defence.DADID=disciplinary_delinquent.DADID "
                    + "where task_master.task_id=? AND REF_DESCRIPTION='DEFID'";
            pstmt = con.prepareStatement(curSpcQry);
            pstmt.setInt(1, taskid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                daid = rs.getInt("daid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return daid;

    }

    @Override
    public String discProceedingActionUrl(int taskId, String empId) {
        String actionUrl = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return actionUrl;
    }

    @Override
    public int sendFYI(DpViewBean viewbean) {
        int messageId = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {

            con = dataSource.getConnection();

            pst = con.prepareStatement("UPDATE TASK_MASTER SET status_id=73 WHERE TASK_ID=?");
            pst.setInt(1, viewbean.getTaskId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);
            /*When Authority approves isapproved is set to Y*/

            pst = con.prepareStatement("update disciplinary_authority set dis_order_no =?, dis_order_date=?,dp_status=61,isapproved='Y' where daid=?");
            pst.setString(1, viewbean.getMemoNo());
            pst.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(viewbean.getMemoDate()).getTime()));
            pst.setInt(3, viewbean.getDaId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);
            /*When Authority approves isapproved is set to Y*/

            //dpViewBean.setApproveAuthCurSpc(rs.getString("apply_to_spc"));
            //dpViewBean.setApproveAuthHrmsId(rs.getString("emp_id"));
            pst = con.prepareStatement("INSERT INTO task_master (process_id, initiated_by, initiated_on, status_id, apply_to, initiated_spc,pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES(?, ?, ?,?,?,?,?,?,?,?,? )", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, 11);
            pst.setString(2, viewbean.getApproveAuthHrmsId());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(4, 69);//FYI status id for disciplinary proceeding
            pst.setString(5, viewbean.getInitAuthHrmsId());
            pst.setString(6, viewbean.getApproveAuthCurSpc());
            pst.setString(7, viewbean.getInitAuthHrmsId());
            pst.setString(8, viewbean.getInitAuthCurSpc());
            pst.setString(9, viewbean.getInitAuthCurSpc());
            pst.setInt(10, viewbean.getDaId());
            pst.setString(11, "DAID");
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");
            DataBaseFunctions.closeSqlObjects(rs, pst);

            List delinquentList = viewbean.getDelinquent();

            pst1 = con.prepareStatement("INSERT INTO disciplinary_defence (dadid, task_id) VALUES (?,?)");

            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean dobean = (DelinquentBean) delinquentList.get(i);
                pst.setInt(1, 11);
                pst.setString(2, viewbean.getApproveAuthHrmsId());
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, 70);//DEFENCE STATEMENT status id for disciplinary proceeding
                pst.setString(5, dobean.getDoEmpHrmsId());
                pst.setString(6, viewbean.getApproveAuthCurSpc());
                pst.setString(7, dobean.getDoEmpHrmsId());
                pst.setString(8, dobean.getDoEmpSpc());
                pst.setString(9, dobean.getDoEmpSpc());
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                taskId = rs.getInt("TASK_ID");

                pst1.setInt(1, dobean.getDadid());
                pst1.setInt(2, taskId);
                pst1.executeUpdate();

            }

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return messageId;
    }

    @Override
    public void saveIo(IoBean ioBean) {

        int empid = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        Connection con = null;
        int daId = 0;
        try {

            con = dataSource.getConnection();

            pst = con.prepareStatement("select ref_id from task_master where TASK_MASTER.task_id=?");
            pst.setInt(1, ioBean.getTaskId());
            rs = pst.executeQuery();
            DataBaseFunctions.closeSqlObjects(rs, pst);
            if (rs.next()) {
                daId = rs.getInt("ref_id");
            }
            pst = con.prepareStatement("INSERT INTO disciplinary_io (daioid, daid, io_hrmsid,  io_spc, is_type ) VALUES (?,?,?,?,?)");
            pst1 = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,REF_ID, REF_DESCRIPTION) VALUES (?,?,?,?,?,?,?,?,?,?,?)");

            for (int i = 0; i < ioBean.getDoio().length; i++) {
                int maxId = CommonFunctions.getMaxCode(con, "disciplinary_io", "daioid");
                String[] arrOfStr = ioBean.getDoio()[i].split("-");
                pst.setInt(1, maxId);
                pst.setInt(2, daId);
                pst.setString(3, arrOfStr[0]);
                pst.setString(4, arrOfStr[1]);
                pst.setString(5, arrOfStr[2]);
                pst.executeUpdate();

                String startTime = "";
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
                startTime = dateFormat.format(cal.getTime());

                pst1.setInt(1, 11);
                pst1.setString(2, ioBean.getIoAppointhrmsId());
                pst1.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst1.setInt(4, 75);
                pst1.setString(5, arrOfStr[0]);
                pst1.setString(6, ioBean.getIoAppointingspc());//initiated_spc
                pst1.setString(7, arrOfStr[0]);
                pst1.setString(8, arrOfStr[1]);
                pst1.setString(9, arrOfStr[1]);
                pst1.setInt(10, maxId);
                pst1.setString(11, "DAIOID");
                pst1.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int saveEnquiryReport(EnquiryBean enquirybean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();
            mcode = CommonFunctions.getMaxCodeInteger("disciplinary_charge_document", "docid", con);

            String rule15MemoQry = "INSERT INTO disciplinary_charge_document (dacid, docid, do_hrmsid,  disk_file_name , org_file_name, file_type, doc_type) VALUES (?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, enquirybean.getDaioid());
            pstmt.setInt(2, mcode);
            pstmt.setInt(3, enquirybean.getDohrmsid());
            pstmt.setString(4, enquirybean.getDiskfilename());
            pstmt.setString(5, enquirybean.getOrgFileName());
            pstmt.setString(6, enquirybean.getFiletpe());
            pstmt.setString(7, enquirybean.getDocType());

            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mcode;
    }

    @Override
    public void submitDefenceStatementRefeaing(DefenceBean defencebean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, defencebean.getDaId());
            res = pstmt.executeQuery();
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            if (res.next()) {
                defencebean.setInitHrmsId(res.getString("init_hrmsid"));
                defencebean.setInitSpc(res.getString("init_spc"));
                defencebean.setAuthHrmsId(res.getString("auth_hrmsid"));
                defencebean.setAuthSpc(res.getString("auth_spc"));
            }

            /*Get Higher Authority Details*/
            /*New Task created for higer authority for IO Apoointment*/
            //pst = con.prepareStatement("INSERT INTO task_master (task_id, process_id, initiated_by, initiated_on, status_id, apply_to, initiated_spc,pending_at, pending_spc, apply_to_spc ) VALUES(?, ?, ?, ?,?,?,?,?,?,? )");
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, defencebean.getInitHrmsId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 74);
            pstmt.setString(5, defencebean.getAuthHrmsId());
            pstmt.setString(6, defencebean.getInitSpc());//initiated_spc
            pstmt.setString(7, defencebean.getAuthHrmsId());
            pstmt.setString(8, defencebean.getAuthSpc());
            pstmt.setString(9, defencebean.getAuthSpc());
            pstmt.setInt(10, defencebean.getDaId());
            pstmt.setString(11, "DAID");
            pstmt.executeUpdate();

            /*New Task created for higer authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveDefenceStatementByDelinquentOfficer(DefenceBean defencebean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();

            String defenceByDOoriginalFileName = null;
            String defenceByDOdiskFileName = null;
            String defenceByDOcontentType = null;
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            if (defencebean.getDefenceByDOdocument() != null && !defencebean.getDefenceByDOdocument().isEmpty()) {
                defenceByDOdiskFileName = new Date().getTime() + "";
                defenceByDOoriginalFileName = defencebean.getDefenceByDOdocument().getOriginalFilename();
                defenceByDOcontentType = defencebean.getDefenceByDOdocument().getContentType();
                byte[] bytes = defencebean.getDefenceByDOdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + defenceByDOdiskFileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            String rule15MemoQry = ("insert into disciplinary_defence (defence_original_filename,defence_disk_filename,defence_filetype,defence_filepath,defence_statement_on_date,dadid,has_reply_delinquentofficer,remark) values(?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, defenceByDOoriginalFileName);
            pstmt.setString(2, defenceByDOdiskFileName);
            pstmt.setString(3, defenceByDOcontentType);
            pstmt.setString(4, this.uploadPath);
            pstmt.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(6, defencebean.getDadid());
            pstmt.setString(7, "Y");
            pstmt.setString(8, defencebean.getDefenceRemark());
            pstmt.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pstmt);

            pstmt = con.prepareStatement("UPDATE task_master SET is_completed='Y' WHERE task_id=?");
            pstmt.setInt(1, defencebean.getTaskId());
            pstmt.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, defencebean.getDaId());
            res = pstmt.executeQuery();
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            if (res.next()) {
                defencebean.setInitHrmsId(res.getString("init_hrmsid"));
                defencebean.setInitSpc(res.getString("init_spc"));
                defencebean.setAuthHrmsId(res.getString("AUTH_HRMSID"));
                defencebean.setAuthSpc(res.getString("AUTH_SPC"));
            }
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            /*Get Higher Authority Details*/
            /*New Task created for higer authority for IO Apoointment*/
            //pst = con.prepareStatement("INSERT INTO task_master (task_id, process_id, initiated_by, initiated_on, status_id, apply_to, initiated_spc,pending_at, pending_spc, apply_to_spc ) VALUES(?, ?, ?, ?,?,?,?,?,?,? )");
            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id, ref_description ) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, defencebean.getDefhrmsid());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 71);
            pstmt.setString(5, defencebean.getInitHrmsId());
            pstmt.setString(6, defencebean.getDefspc());//initiated_spc
            pstmt.setString(7, defencebean.getAuthHrmsId());
            pstmt.setString(8, defencebean.getAuthSpc());
            pstmt.setString(9, defencebean.getAuthHrmsId());
            pstmt.setInt(10, defencebean.getDefid());
            pstmt.setString(11, "DEFID");
            pstmt.executeUpdate();

            /*New Task created for higer authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void sendNotice1(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select * from disciplinary_io where daioid = ?");
            pstmt.setInt(1, noticeBean.getDaioid());
            res = pstmt.executeQuery();

            if (res.next()) {
                noticeBean.setDaId(res.getInt("daid"));

            }
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select do_hrmsid,do_spc from disciplinary_delinquent  where daid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setEmpHrmsId(res.getString("do_hrmsid"));
                noticeBean.setEmpSPC(res.getString("do_spc"));
            }
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setAuthHrmsId(res.getString("auth_hrmsid"));
                noticeBean.setAuthSpc(res.getString("auth_spc"));
            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("UPDATE disciplinary_authority SET notice1_send_date=? WHERE daid=?");
            pstmt.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(2, noticeBean.getDaId());
            pstmt.executeUpdate();
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, noticeBean.getAuthHrmsId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 78);
            pstmt.setString(5, noticeBean.getEmpHrmsId());
            pstmt.setString(6, noticeBean.getEmpSPC());
            pstmt.setString(7, noticeBean.getEmpHrmsId());
            pstmt.setString(8, noticeBean.getEmpSPC());
            pstmt.setString(9, noticeBean.getEmpSPC());
            pstmt.setInt(10, noticeBean.getDaId());
            pstmt.setString(11, "DAID");

            pstmt.executeUpdate();

            /*New Task created for higer authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveReplyNotice1(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select dadid from disciplinary_delinquent where daid=? and do_hrmsid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            pstmt.setString(2, noticeBean.getEmpHrmsId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                noticeBean.setDadid(rs.getInt("dadid"));

            }
            mcode = CommonFunctions.getMaxCodeInteger("disciplinary_defence", "defid", con);

            String rule15MemoQry = "INSERT INTO disciplinary_defence (dadid, defid,remark, task_id,defence_type) VALUES (?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, noticeBean.getDadid());
            pstmt.setInt(2, mcode);
            pstmt.setString(3, noticeBean.getRemark());
            pstmt.setInt(4, noticeBean.getTaskId());
            pstmt.setString(5, "N1");

            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveReplyNotice2(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select dadid from disciplinary_delinquent where daid=? and do_hrmsid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            pstmt.setString(2, noticeBean.getEmpHrmsId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                noticeBean.setDadid(rs.getInt("dadid"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            mcode = CommonFunctions.getMaxCodeInteger("disciplinary_defence", "defid", con);

            String rule15MemoQry = "INSERT INTO disciplinary_defence (dadid, defid,remark, task_id,defence_type) VALUES (?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, noticeBean.getDadid());
            pstmt.setInt(2, mcode);
            pstmt.setString(3, noticeBean.getRemark());
            pstmt.setInt(4, noticeBean.getTaskId());
            pstmt.setString(5, "N2");

            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getReplyNotice1(int dadid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList noticeBean = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select defid ,remark,task_id,defence_type from disciplinary_defence where defence_type = 'N1' and dadid = ? ");

            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            while (rs.next()) {
                NoticeBean notice = new NoticeBean();
                notice.setDefid(rs.getInt("defid"));
                notice.setRemark(rs.getString("remark"));
                notice.setTaskId(rs.getInt("task_id"));
                noticeBean.add(notice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noticeBean;
    }

    @Override
    public List getReplyNotice2(int dadid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList noticeBean = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select defid ,remark,task_id,defence_type from disciplinary_defence where defence_type = 'N2' and dadid = ? ");

            pst.setInt(1, dadid);
            rs = pst.executeQuery();
            while (rs.next()) {
                NoticeBean notice = new NoticeBean();
                notice.setDefid(rs.getInt("defid"));
                notice.setRemark(rs.getString("remark"));
                notice.setTaskId(rs.getInt("task_id"));
                noticeBean.add(notice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noticeBean;
    }

    @Override
    public void sendNotice2(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select * from disciplinary_io where daioid = ?");
            pstmt.setInt(1, noticeBean.getDaioid());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setDaId(res.getInt("daid"));

            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select do_hrmsid,do_spc from disciplinary_delinquent  where daid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setEmpHrmsId(res.getString("do_hrmsid"));
                noticeBean.setEmpSPC(res.getString("do_spc"));
            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setAuthHrmsId(res.getString("auth_hrmsid"));
                noticeBean.setAuthSpc(res.getString("auth_spc"));
            }
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, noticeBean.getAuthHrmsId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 79);
            pstmt.setString(5, noticeBean.getEmpHrmsId());
            pstmt.setString(6, noticeBean.getEmpSPC());
            pstmt.setString(7, noticeBean.getEmpHrmsId());
            pstmt.setString(8, noticeBean.getEmpSPC());
            pstmt.setString(9, noticeBean.getEmpSPC());
            pstmt.setInt(10, noticeBean.getDaId());
            pstmt.setString(11, "DAID");
            pstmt.executeUpdate();

            /*New Task created for higer authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void sendFYI(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dadid,do_hrmsid,do_spc from disciplinary_delinquent  where daid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();

            int dadid = 0;

            if (res.next()) {

                noticeBean.setEmpHrmsId(res.getString("do_hrmsid"));
                noticeBean.setEmpSPC(res.getString("do_spc"));
                dadid = res.getInt("dadid");

            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            if (res.next()) {
                noticeBean.setAuthHrmsId(res.getString("auth_hrmsid"));
                noticeBean.setAuthSpc(res.getString("auth_spc"));
            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, noticeBean.getEmpHrmsId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 80);
            pstmt.setString(5, noticeBean.getAuthHrmsId());
            pstmt.setString(6, noticeBean.getEmpSPC());
            pstmt.setString(7, noticeBean.getAuthHrmsId());
            pstmt.setString(8, noticeBean.getAuthSpc());
            pstmt.setString(9, noticeBean.getAuthSpc());
            pstmt.setInt(10, dadid);
            pstmt.setString(11, "DADID");
            pstmt.executeUpdate();

            /*New Task created for higher authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void sendFYI2(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dadid,do_hrmsid,do_spc from disciplinary_delinquent  where daid=?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();
            int dadid = 0;
            if (res.next()) {
                noticeBean.setEmpHrmsId(res.getString("do_hrmsid"));
                noticeBean.setEmpSPC(res.getString("do_spc"));
                dadid = res.getInt("dadid");

            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, noticeBean.getDaId());
            res = pstmt.executeQuery();

            if (res.next()) {
                noticeBean.setAuthHrmsId(res.getString("auth_hrmsid"));
                noticeBean.setAuthSpc(res.getString("auth_spc"));
            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pstmt = con.prepareStatement("INSERT INTO task_master (process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, 11);
            pstmt.setString(2, noticeBean.getEmpHrmsId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(4, 81);
            pstmt.setString(5, noticeBean.getAuthHrmsId());
            pstmt.setString(6, noticeBean.getEmpSPC());
            pstmt.setString(7, noticeBean.getAuthHrmsId());
            pstmt.setString(8, noticeBean.getAuthSpc());
            pstmt.setString(9, noticeBean.getAuthSpc());
            pstmt.setInt(10, dadid);
            pstmt.setString(11, "DADID");
            pstmt.executeUpdate();

            /*New Task created for higher authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void discFinalNoticePDF(Document document, DpViewBean viewbean) {
        try {

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            PdfPTable innertable = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new float[]{3f});
            table.setWidthPercentage(80);

            cell = new PdfPCell(new Phrase("Government of Odisha", f1));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Women and Child Development Department", f1));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Final Order", f1));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            List delinquentList = viewbean.getDelinquent();
            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);

                cell = new PdfPCell(new Phrase("Whereas, Charges Were Framed against " + delinquent.getDoEmpName() + " vide this Department Proceeding No........." + " Dtd.....;", f1));
                cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            String doEmpName = "";
            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);
                if (doEmpName != null && !doEmpName.equals("")) {
                    doEmpName = doEmpName + "," + delinquent.getDoEmpName();
                } else {
                    doEmpName = delinquent.getDoEmpName();
                }
            }
            List ioList = viewbean.getIos();
            for (int i = 0; i < ioList.size(); i++) {
                IoBean iobean = (IoBean) ioList.get(i);

                cell = new PdfPCell(new Phrase("Whereas,." + iobean.getIoEmpName() + " Who Was appointed as the Inquiring Officer, vide this Department Office Order No.....dt....furnished "
                        + "his/her Inquiry Report dated......;"
                        + "Government have been pleased to impose the Following Penalities against " + doEmpName, f1));
                cell.setFixedHeight(80);
                //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            List penalty = viewbean.getPenalty();
            for (int i = 0; i < penalty.size(); i++) {
                NoticeBean notice = (NoticeBean) penalty.get(i);

                cell = new PdfPCell(new Phrase("Name of the Penalty,." + notice.getDescription(), f1));
                cell.setFixedHeight(40);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Whereas, after careful consideration of the Charges, Findings of Inquiring Officer and the representation Submited by the delinquent Officer"
                    + "against the Findings of the Inquiring Officer u/r 15(15)(i)(a) and against the Proposed Penalty u/r 15(10)(i)(b) of OCS (CC&A) Rule 1962"
                    + "Government have been pleased to impose the Following Penalities against Sri/Smt.... ", f1));
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("By order Of the Governor", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Commissioner-cum-Secretary", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*innertable = new PdfPTable(2);
             innertable.setWidths(new float[]{1f, 1f});  
             innertable.setWidthPercentage(1000);
            
            

             innercell = new PdfPCell(new Phrase("Memorandum No:", f1));
             innercell.setFixedHeight(20);
             innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
             innercell.setBorder(Rectangle.NO_BORDER);
             innertable.addCell(innercell);
             innercell = new PdfPCell(new Phrase("Date:", f1));
             innercell.setFixedHeight(20);
             innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
             innercell.setBorder(Rectangle.NO_BORDER);
             innertable.addCell(innercell);
             document.add(innertable);
             /*cell = new PdfPCell(innertable);
             cell.setFixedHeight(10);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);*/
            //table.addCell(cell);
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFinalOrder(FinalOrder finalOrder) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dadid,do_hrmsid,do_spc from disciplinary_delinquent  where daid=?");
            pstmt.setInt(1, finalOrder.getDaId());
            res = pstmt.executeQuery();
            int dadid = 0;
            if (res.next()) {
                finalOrder.setDoEmpHrmsId(res.getString("do_hrmsid"));
                finalOrder.setDoEmpSPC(res.getString("do_spc"));
                dadid = res.getInt("dadid");

            }
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid = ?");
            pstmt.setInt(1, finalOrder.getDaId());
            res = pstmt.executeQuery();
            DataBaseFunctions.closeSqlObjects(res, pstmt);

            if (res.next()) {
                finalOrder.setInitHrmsId(res.getString("init_hrmsid"));
                finalOrder.setInitSpc(res.getString("init_spc"));
                finalOrder.setAuthHrmsId(res.getString("auth_hrmsid"));
                finalOrder.setAuthSpc(res.getString("auth_spc"));
            }

            DataBaseFunctions.closeSqlObjects(res, pstmt);

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);

            pstmt = con.prepareStatement("INSERT INTO task_master (task_id, process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc) VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, mcode);
            pstmt.setInt(2, 11);
            pstmt.setString(3, finalOrder.getAuthHrmsId());
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(5, 82);
            pstmt.setString(6, finalOrder.getInitHrmsId());
            pstmt.setString(7, finalOrder.getAuthSpc());
            pstmt.setString(8, finalOrder.getInitHrmsId());
            pstmt.setString(9, finalOrder.getInitSpc());
            pstmt.setString(10, finalOrder.getInitSpc());

            pstmt.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pstmt);

            mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);
            pstmt = con.prepareStatement("INSERT INTO task_master (task_id, process_id,initiated_by, initiated_on, status_id, apply_to, initiated_spc, pending_at, pending_spc, apply_to_spc) VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, mcode);
            pstmt.setInt(2, 11);
            pstmt.setString(3, finalOrder.getAuthHrmsId());
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(5, 82);
            pstmt.setString(6, finalOrder.getDoEmpHrmsId());
            pstmt.setString(7, finalOrder.getAuthSpc());
            pstmt.setString(8, finalOrder.getDoEmpHrmsId());
            pstmt.setString(9, finalOrder.getDoEmpSPC());
            pstmt.setString(10, finalOrder.getDoEmpSPC());
            pstmt.executeUpdate();


            /*New Task created for higher authority for IO Apoointment*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int savePenalty(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int dopenaltyid = 0;
        try {
            con = dataSource.getConnection();
            dopenaltyid = CommonFunctions.getMaxCode(con, "disciplinary_penalty", "dopenaltyid");
            String rule15MemoQry = "INSERT INTO disciplinary_penalty (daid,dopenaltyid,description ) VALUES (?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, noticeBean.getDaId());
            pstmt.setInt(2, dopenaltyid);
            pstmt.setString(3, noticeBean.getDescription());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dopenaltyid;
    }

    @Override
    public List getPenaltyList(NoticeBean noticeBean) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList penalty = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select dopenaltyid,description from disciplinary_penalty  where daid = ? ");

            pst.setInt(1, noticeBean.getDaId());
            rs = pst.executeQuery();
            while (rs.next()) {
                NoticeBean notice = new NoticeBean();

                notice.setDoPenaltyId(rs.getInt("dopenaltyid"));
                notice.setDescription(rs.getString("description"));

                penalty.add(notice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return penalty;
    }

    @Override
    public DefenceBean getDefenceDetailFromDadid(int dadid) {
        DefenceBean defencebean = new DefenceBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from disciplinary_defence  where dadid =?");
            pstmt.setInt(1, dadid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                defencebean.setDefenceRemark(rs.getString("remark"));
                defencebean.setDadid(dadid);
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT * FROM employee_attachment WHERE ref_type='DEFENCESTMT' and ref_id=?");
            pstmt.setInt(1, dadid);
            rs = pstmt.executeQuery();
            ArrayList attachments = new ArrayList();
            while (rs.next()) {
                FileAttachBean fio = new FileAttachBean();
                fio.setOrgFileName(rs.getString("O_FILE_NAME"));
                fio.setAttachmentId(rs.getInt("attachment_id"));
                attachments.add(fio);
            }
            defencebean.setAttachments(attachments);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return defencebean;
    }

    @Override
    public DefenceBean getDefenceDetailFromDaid(int daId) {
        DefenceBean defencebean = new DefenceBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select remark from disciplinary_delinquent "
                    + "inner join disciplinary_defence on disciplinary_delinquent.dadid = disciplinary_defence.dadid where daid =?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                defencebean.setDefenceRemark(rs.getString("remark"));
                defencebean.setDaId(daId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return defencebean;
    }

    @Override
    public DefenceBean getDefenceDetailFromTaskId(int taskId) {
        DefenceBean defencebean = new DefenceBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT daid,DEFID,disciplinary_defence.DADID,remark FROM disciplinary_defence "
                    + "inner join disciplinary_delinquent on disciplinary_defence.dadid = disciplinary_delinquent.dadid WHERE TASK_ID=?");
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                defencebean.setDaId(rs.getInt("daid"));
                defencebean.setDefid(rs.getInt("DEFID"));
                defencebean.setDadid(rs.getInt("DADID"));
                defencebean.setDefenceRemark(rs.getString("remark"));
                defencebean.setTaskId(taskId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return defencebean;
    }

    @Override
    public void discApprovedPDF(Document document, DpViewBean viewbean) {

        try {

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            PdfPTable innertable = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new float[]{3f});
            table.setWidthPercentage(80);

            cell = new PdfPCell(new Phrase("IRREGULARITIES", f1));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(viewbean.getDeptName() + " DEPARTMENT", f1));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            List delinquentList = viewbean.getDelinquent();
            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);

                cell = new PdfPCell(new Phrase(delinquent.getDoEmpName() + " is here by informed that it is proposed to take Departmental Action against" + (viewbean.getGender()) + " under Rule 16 of the Orissa Civil Services (Classification, Control and Appeal)\n"
                        + "Rules, 1962. The Substance of the imputation in respect of which the inquiry is proposed to be held is set out in the enclosed statement of articles of charges(Annexure-I)\n"
                        + "The Statement of imputations in support of the article of charges is enclosed(Annexure-II) along with Memo of Evidence (Annexure-III) .", f1));
                cell.setFixedHeight(80);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);

                cell = new PdfPCell(new Phrase("1. " + delinquent.getDoEmpName() + " is hereby given an opportunity to make representations as " + (delinquent.getGender()) + " wishes to make against the Proposed Disciplinary action .", f1));
                cell.setFixedHeight(40);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);
                cell = new PdfPCell(new Phrase("2. " + (delinquent.getGender()) + " may peruse the relevant records in the office  and take relevant extracts thereof to submit " + (delinquent.getGender2()) + " written statement of defence with permission from the competetent authority .", f1));

                cell.setFixedHeight(40);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            for (int i = 0; i < delinquentList.size(); i++) {
                DelinquentBean delinquent = (DelinquentBean) delinquentList.get(i);

                cell = new PdfPCell(new Phrase("3. If " + (delinquent.getGender() + " fails to submit" + (delinquent.getGender2()) + " representation within 15 days of the receipt of this Memorandum, it will be presumed that " + (delinquent.getGender()) + " has no representation to submit and orders as deemed proper will be passed against " + (delinquent.getGender1()) + " in accordance of the law ."), f1));
                cell.setFixedHeight(40);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("4. The receipt of the memorandum should be acknowledged  by sri " + (viewbean.getApproveAuthName()) + "," + (viewbean.getApproveAuthCurDegn()), f1));
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("By order Of " + (viewbean.getApproveAuthCurDegn()), f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memorandum No:", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date:", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*innertable = new PdfPTable(2);
             innertable.setWidths(new float[]{1f, 1f});  
             innertable.setWidthPercentage(1000);
            
            

             innercell = new PdfPCell(new Phrase("Memorandum No:", f1));
             innercell.setFixedHeight(20);
             innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
             innercell.setBorder(Rectangle.NO_BORDER);
             innertable.addCell(innercell);
             innercell = new PdfPCell(new Phrase("Date:", f1));
             innercell.setFixedHeight(20);
             innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
             innercell.setBorder(Rectangle.NO_BORDER);
             innertable.addCell(innercell);
             document.add(innertable);
             /*cell = new PdfPCell(innertable);
             cell.setFixedHeight(10);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);*/
            cell = new PdfPCell(new Phrase(" Copy Forwarded to " + (viewbean.getInitAuthName()) + "," + (viewbean.getInitAuthCurDegn()) + ".", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void uploadDefenceStatementAttachedFile(int messageId, String refType, MultipartFile file) {
        String diskfileName = new Date().getTime() + "";
        PreparedStatement pst = null;
        Connection con = null;
        if (!file.isEmpty()) {
            try {

                byte[] bytes = file.getBytes();
                File dir = new File(this.uploadPath + File.separator + "tmpFiles");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                con = dataSource.getConnection();
                pst = con.prepareStatement("INSERT INTO disciplinary_defence_document(disk_file_name,org_file_name,file_type) VALUES(?, ?, ?)");
                pst.setString(1, diskfileName);
                pst.setString(2, file.getOriginalFilename());
                pst.setString(3, file.getContentType());

                //pst.setString(5, dir.getAbsolutePath() + File.separator + diskfileName);
                //pst.setString(6, file.getContentType());
                pst.executeUpdate();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(pst);
                DataBaseFunctions.closeSqlObjects(con);
            }
        }
    }

    public void uploadIOAttachedFile(int messageId, String refType, MultipartFile file) {
        String diskfileName = new Date().getTime() + "";
        PreparedStatement pst = null;
        Connection con = null;
        if (!file.isEmpty()) {
            try {

                byte[] bytes = file.getBytes();

                File dir = new File(this.uploadPath + File.separator + "tmpFiles");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                con = dataSource.getConnection();
                pst = con.prepareStatement("INSERT INTO employee_attachment(o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES(?, ?, ?, ?, ?, ? )");
                pst.setString(1, file.getOriginalFilename());
                pst.setString(2, diskfileName);
                pst.setInt(3, messageId);
                pst.setString(4, refType);
                pst.setString(5, dir.getAbsolutePath() + File.separator + diskfileName);
                pst.setString(6, file.getContentType());
                pst.executeUpdate();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(pst);
                DataBaseFunctions.closeSqlObjects(con);
            }
        }
    }

    @Override
    public void saveOtherHrmsWitnessList(DiscWitnessBean discWitnessBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            String rule15MemoQry = "INSERT INTO disciplinary_act_charge_witness (dacwid,dacid,at , po , ps,  pin_code , mobile_no, email_id, is_in_hrms,dist,witness_name) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, CommonFunctions.getMaxCode(con, "disciplinary_act_charge_witness", "dacwid"));
            pstmt.setInt(2, discWitnessBean.getDacid());
            pstmt.setString(3, discWitnessBean.getAddressat());
            pstmt.setString(4, discWitnessBean.getAddresspo());
            pstmt.setString(5, discWitnessBean.getAddressps());
            pstmt.setString(6, discWitnessBean.getPincode());
            pstmt.setString(7, discWitnessBean.getMobile());
            pstmt.setString(8, discWitnessBean.getEmail());
            pstmt.setString(9, discWitnessBean.getIsInHrms());
            pstmt.setString(10, discWitnessBean.getDist());
            pstmt.setString(11, discWitnessBean.getWitnessName());

            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updatedefenceStatementRefeaing(DiscChargeBean discChargeBean) {
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();

            String rule15MemoQry = "UPDATE disciplinary_act_charge set defence_remark=? WHERE dacid=?";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, discChargeBean.getRemark());
            pstmt.setInt(2, discChargeBean.getDacid());
            pstmt.executeUpdate();

        } catch (Exception e) {
            res = 0;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateIoRemark(IoReportBean ioReportBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            String rule15MemoQry = "UPDATE disciplinary_io_remark set io_remark=? WHERE io_remark_id=?";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, ioReportBean.getIoRemark());
            pstmt.setInt(2, ioReportBean.getIoRemarkId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int saveIoReportDetails(IoReportBean ioReportBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int ioremarkid = 0;
        try {
            con = dataSource.getConnection();
            ioremarkid = CommonFunctions.getMaxCode(con, "disciplinary_io_remark", "io_remark_id");
            String rule15MemoQry = "INSERT INTO disciplinary_io_remark (io_remark_id,daioid,exihibits_type,remark,io_remark ) VALUES (?,?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setInt(1, ioremarkid);
            pstmt.setInt(2, ioReportBean.getDaioid());
            pstmt.setString(3, ioReportBean.getExihibitstype());
            pstmt.setString(4, ioReportBean.getRemark());
            pstmt.setString(5, ioReportBean.getIoRemark());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ioremarkid;
    }

    @Override
    public void submitIOReportBriefing(IoReportBean ioReportBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from disciplinary_io where daioid = ?");
            pstmt.setInt(1, ioReportBean.getDaioid());
            rs = pstmt.executeQuery();
            int daId = 0;
            if (rs.next()) {
                daId = rs.getInt("DAID");
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ioReportBean.setInitHrmsId(rs.getString("init_hrmsid"));
                ioReportBean.setInitSpc(rs.getString("init_spc"));
                ioReportBean.setAuthHrmsId(rs.getString("auth_hrmsid"));
                ioReportBean.setAuthSpc(rs.getString("auth_spc"));

            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String taskMastInsQry = "INSERT INTO task_master (task_id,process_id,initiated_by,initiated_on,status_id,pending_at,apply_to,initiated_spc,pending_spc,apply_to_spc,ref_id,ref_description ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);

            pstmt = con.prepareStatement(taskMastInsQry);

            pstmt.setInt(1, mcode);
            pstmt.setInt(2, 11);
            pstmt.setString(3, ioReportBean.getInitHrmsId());
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(5, 77);//STATUS_ID FOR REVIEW IO REPORT
            pstmt.setString(6, ioReportBean.getAuthHrmsId());
            pstmt.setString(7, ioReportBean.getAuthHrmsId());//initiated_spc
            pstmt.setString(8, ioReportBean.getInitSpc());
            pstmt.setString(9, ioReportBean.getAuthSpc());
            pstmt.setString(10, ioReportBean.getAuthSpc());
            pstmt.setInt(11, ioReportBean.getDaioid());
            pstmt.setString(12, "DAIOID");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public IoReportBean getIODetails(int daioid) {
        IoReportBean ioreportBean = new IoReportBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * from disciplinary_io WHERE daioid=?");
            pstmt.setInt(1, daioid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ioreportBean.setDaId(rs.getInt("daid"));
                ioreportBean.setIohrmsId(rs.getString("io_hrmsid"));
                ioreportBean.setIoSpc(rs.getString("io_spc"));
                ioreportBean.setIstype(rs.getString("is_type"));
                ioreportBean.setDaioid(daioid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return ioreportBean;
    }

    public ArrayList getIoDetailList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList investingreport = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from disciplinary_io "
                    + "INNER JOIN  disciplinary_io_remark on disciplinary_io.daioid= disciplinary_io_remark.io_remark_id");

            rs = pst.executeQuery();
            while (rs.next()) {
                IoReportBean ioreportbean = new IoReportBean();
                ioreportbean.setDaioid(rs.getInt("daioid"));
                ioreportbean.setDaId(rs.getInt("daid"));
                ioreportbean.setIohrmsId(rs.getString("io_hrmsid"));
                ioreportbean.setIoSpc(rs.getString("io_spc"));
                ioreportbean.setIstype(rs.getString("is_type"));
                ioreportbean.setIoRemarkId(rs.getInt("io_remark_id"));
                ioreportbean.setAttachments(getAttachments(rs.getInt("io_remark_id"), "IOSTMT"));
                ioreportbean.setExihibitstype(rs.getString("exihibits_type"));
                ioreportbean.setRemark(rs.getString("remark"));
                ioreportbean.setIoRemark(rs.getString("io_remark"));
                investingreport.add(ioreportbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return investingreport;

    }

    public ArrayList getIoDetailListfromdaid(int daId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList investingreport = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT disciplinary_io.daioid,daid,io_hrmsid,io_remark_id,io_spc,is_type,exihibits_type,remark,o_file_name,attachment_id,io_remark FROM disciplinary_io"
                    + " INNER JOIN disciplinary_io_remark ON disciplinary_io.DAIOID = disciplinary_io_remark.DAIOID"
                    + " LEFT OUTER JOIN (SELECT * FROM employee_attachment WHERE ref_type = 'IOSTMT') AS employee_attachment on disciplinary_io_remark.io_remark_id = employee_attachment.ref_id WHERE DAID=?;");

            pst.setInt(1, daId);
            rs = pst.executeQuery();
            while (rs.next()) {
                IoReportBean ioreportbean = new IoReportBean();
                ioreportbean.setDaioid(rs.getInt("daioid"));
                ioreportbean.setDaId(rs.getInt("daid"));
                ioreportbean.setIohrmsId(rs.getString("io_hrmsid"));
                ioreportbean.setIoSpc(rs.getString("io_spc"));
                ioreportbean.setIstype(rs.getString("is_type"));
                ioreportbean.setIoRemarkId(rs.getInt("io_remark_id"));
                ioreportbean.setAttachments(getAttachments(rs.getInt("io_remark_id"), "IOSTMT"));
                ioreportbean.setExihibitstype(rs.getString("exihibits_type"));
                ioreportbean.setRemark(rs.getString("remark"));
                ioreportbean.setIoRemark(rs.getString("io_remark"));
                investingreport.add(ioreportbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return investingreport;

    }

    public ArrayList getAttachments(int refId, String refType) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;

        ArrayList<FileAttachBean> attachmentList = new ArrayList<>();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT * FROM employee_attachment WHERE ref_id =? and ref_type=?");
            pst.setInt(1, refId);
            pst.setString(2, refType);
            rs = pst.executeQuery();
            while (rs.next()) {
                FileAttachBean fileAttachBean = new FileAttachBean();
                fileAttachBean.setAttachmentId(rs.getInt("attachment_id"));
                fileAttachBean.setOrgFileName(rs.getString("o_file_name"));
                attachmentList.add(fileAttachBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return attachmentList;
    }

    @Override
    public ArrayList getIoReportList(int daioid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag = false;
        ArrayList investingreport = new ArrayList();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select io_remark_id,exihibits_type,remark,o_file_name,attachment_id,io_remark  from disciplinary_io_remark"
                    + " LEFT OUTER JOIN (SELECT * FROM employee_attachment WHERE ref_type = 'IOSTMT') AS employee_attachment on disciplinary_io_remark.io_remark_id = employee_attachment.ref_id where daioid=?");
            pst.setInt(1, daioid);
            rs = pst.executeQuery();
            while (rs.next()) {
                IoReportBean ioreportbean = new IoReportBean();

                ioreportbean.setIoRemarkId(rs.getInt("io_remark_id"));
                ioreportbean.setDocumentName(rs.getString("o_file_name"));
                ioreportbean.setDocumentId(rs.getInt("attachment_id"));
                ioreportbean.setExihibitstype(rs.getString("exihibits_type"));
                ioreportbean.setRemark(rs.getString("remark"));
                ioreportbean.setIoRemark(rs.getString("io_remark"));
                ioreportbean.setAttachments(getAttachments(rs.getInt("io_remark_id"), "IOSTMT"));
                investingreport.add(ioreportbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return investingreport;

    }

    @Override
    public void submitIoReportDetails(IoReportBean ioReportBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            //taskId
            pstmt = con.prepareStatement("select * from disciplinary_io where daioid = ?");
            pstmt.setInt(1, ioReportBean.getDaioid());

            rs = pstmt.executeQuery();
            int daId = 0;
            if (rs.next()) {
                daId = rs.getInt("DAID");
                ioReportBean.setIohrmsId(rs.getString("io_hrmsid"));
                ioReportBean.setIoSpc(rs.getString("io_spc"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("select * from disciplinary_authority where daid=?");
            pstmt.setInt(1, daId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ioReportBean.setAuthHrmsId(rs.getString("init_hrmsid"));
                ioReportBean.setAuthSpc(rs.getString("init_spc"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            String taskMastInsQry = "INSERT INTO task_master (task_id,process_id,initiated_by,initiated_on,status_id,pending_at,apply_to,initiated_spc,pending_spc,apply_to_spc,ref_id,ref_description ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);
            pstmt = con.prepareStatement(taskMastInsQry);
            pstmt.setInt(1, mcode);
            pstmt.setInt(2, 11);
            pstmt.setString(3, ioReportBean.getIohrmsId());
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pstmt.setInt(5, 76);
            pstmt.setString(6, ioReportBean.getAuthHrmsId());
            pstmt.setString(7, ioReportBean.getAuthHrmsId());//initiated_spc
            pstmt.setString(8, ioReportBean.getIoSpc());
            pstmt.setString(9, ioReportBean.getAuthSpc());
            pstmt.setString(10, ioReportBean.getAuthSpc());
            pstmt.setInt(11, ioReportBean.getDaioid());
            pstmt.setString(12, "DAIOID");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public DispatchDetailsBean removeDispatchFile(int ddid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DispatchDetailsBean ddbean = new DispatchDetailsBean();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT ems_scan_filename,ems_scan_o_file_name,file_path, ems_scan_file_type,comm_type, comm_type_ref from disciplinary_dispatch_details where ddid=?");
            pst.setInt(1, ddid);
            rs = pst.executeQuery();
            DataBaseFunctions.closeSqlObjects(rs, pst);
            String scanFileName = null;
            if (rs.next()) {
                ddbean.setComTypeReference(rs.getInt("comm_type_ref"));
                ddbean.setComType(rs.getString("comm_type"));
                scanFileName = rs.getString("ems_scan_filename");
                File f = new File(this.uploadPath + scanFileName);
                FileUtils.forceDelete(f);
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (ddbean.getComType().equals("COMM_DELINQUENT")) {
                pst = con.prepareStatement("SELECT DAID FROM disciplinary_delinquent WHERE DADID=?");
                pst.setInt(1, ddbean.getComTypeReference());
                rs = pst.executeQuery();
                DataBaseFunctions.closeSqlObjects(rs, pst);
                if (rs.next()) {
                    ddbean.setDaId(rs.getInt("DAID"));
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);
            }
            pst = con.prepareStatement("DELETE from disciplinary_dispatch_details where ddid=?");
            pst.setInt(1, ddid);
            pst.executeUpdate();
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddbean;
    }

    @Override
    public DispatchDetailsBean getAttachedDispatchFile(int ddid) {
        DispatchDetailsBean ddbean = new DispatchDetailsBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT ems_scan_filename,ems_scan_o_file_name,file_path, ems_scan_file_type from disciplinary_dispatch_details where ddid=?");
            pst.setInt(1, ddid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ddbean.setScanfilename(rs.getString("ems_scan_filename"));
                ddbean.setEmsoFileName(rs.getString("ems_scan_o_file_name"));
                ddbean.setFiletype(rs.getString("ems_scan_file_type"));
            }

            File f = new File(this.uploadPath + ddbean.getScanfilename());
            ddbean.setFilecontent(FileUtils.readFileToByteArray(f));

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddbean;
    }

    @Override
    public void saveDispatchDetails(DispatchDetailsBean dispatchdetailsbean) {

        String ems_scan_filename = new Date().getTime() + "";
        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        int res = 0;
        String doCurSpc = null;
        int mcode = 0;
        if (!dispatchdetailsbean.getEmsCopyAttach().isEmpty()) {
            try {

                byte[] bytes = dispatchdetailsbean.getEmsCopyAttach().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + ems_scan_filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                con = dataSource.getConnection();
                mcode = CommonFunctions.getMaxCodeInteger("disciplinary_dispatch_details", "ddid", con);

                String rule15MemoQry = "INSERT INTO disciplinary_dispatch_details (ddid,post_offc_name, ems_no, ems_scan_filename,ems_scan_o_file_name,file_path,ems_scan_file_type,comm_type,comm_type_ref) VALUES (?,?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(rule15MemoQry);

                pstmt.setInt(1, mcode);
                pstmt.setString(2, dispatchdetailsbean.getPostoffcName());
                pstmt.setString(3, dispatchdetailsbean.getEmsNo());
                pstmt.setString(4, ems_scan_filename);
                pstmt.setString(5, dispatchdetailsbean.getEmsCopyAttach().getOriginalFilename());
                pstmt.setString(6, dir.getAbsolutePath() + File.separator + ems_scan_filename);
                pstmt.setString(7, dispatchdetailsbean.getEmsCopyAttach().getContentType());
                pstmt.setString(8, dispatchdetailsbean.getComType());
                pstmt.setInt(9, dispatchdetailsbean.getComTypeReference());
                pstmt.executeUpdate();

            } catch (Exception e) {
                res = 0;
                e.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(rs, pstmt);
                DataBaseFunctions.closeSqlObjects(con);
            }

        }

    }

    @Override
    public boolean isEditable(int daid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isEditable = false;

        try {
            con = dataSource.getConnection();
            String empListQry = "SELECT auth_hrmsid FROM disciplinary_authority WHERE daid=? and auth_hrmsid is null";
            pstmt = con.prepareStatement(empListQry);
            pstmt.setInt(1, daid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isEditable = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isEditable;
    }

    @Override
    public int sendDeclined(DpViewBean viewbean) {
        int messageId = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = null;

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {

            con = dataSource.getConnection();

            /*When Authority approves isapproved is set to Y*/
            pst = con.prepareStatement("update disciplinary_authority set isapproved='Y' where daid=?");
            pst.setInt(1, viewbean.getDaId());
            pst.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("select * from disciplinary_authority where daid=?");
            pst.setInt(1, viewbean.getDaId());
            rs = pst.executeQuery();
            if (rs.next()) {
                viewbean.setInitAuthHrmsId(rs.getString("init_hrmsid"));
                viewbean.setInitAuthCurSpc(rs.getString("init_spc"));
                viewbean.setApproveAuthHrmsId(rs.getString("auth_hrmsid"));
                viewbean.setApproveAuthCurSpc(rs.getString("auth_spc"));

            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            //dpViewBean.setApproveAuthCurSpc(rs.getString("apply_to_spc"));
            //dpViewBean.setApproveAuthHrmsId(rs.getString("emp_id"));
            int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);
            pst = con.prepareStatement("INSERT INTO task_master (task_id, process_id, initiated_by, initiated_on, status_id, apply_to, initiated_spc,pending_at, pending_spc, apply_to_spc,ref_id,ref_description) VALUES(?, ?, ?, ?,?,?,?,?,?,?,?,? )");
            pst.setInt(1, mcode);
            pst.setInt(2, 11);
            pst.setString(3, viewbean.getApproveAuthHrmsId());
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(5, 83);//Declined status for disciplinary proceeding
            pst.setString(6, viewbean.getInitAuthHrmsId());
            pst.setString(7, viewbean.getApproveAuthCurSpc());
            pst.setString(8, viewbean.getInitAuthHrmsId());
            pst.setString(9, viewbean.getInitAuthCurSpc());
            pst.setString(10, viewbean.getInitAuthCurSpc());
            pst.setInt(11, viewbean.getDaId());
            pst.setString(12, "DAID");
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return messageId;
    }

    @Override
    public List getDispatchList(int comTypeRef, String comType) {
        List dispatchList = new ArrayList();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        DispatchDetailsBean dispatchDetailsBean = null;

        try {
            con = dataSource.getConnection();
            String empListQry = "select ddid,post_offc_name,ems_no,ems_scan_filename,ems_scan_o_file_name,ems_trackreport_filename from disciplinary_dispatch_details where comm_type=? and comm_type_ref=?";
            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, comType);
            pstmt.setInt(2, comTypeRef);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dispatchDetailsBean = new DispatchDetailsBean();
                dispatchDetailsBean.setDdid(rs.getInt("ddid"));
                //dpBean.setDoHrmsId(rs.getString("emp_id"));
                dispatchDetailsBean.setPostoffcName(rs.getString("post_offc_name"));
                dispatchDetailsBean.setEmsNo(rs.getString("ems_no"));
                dispatchDetailsBean.setScanfilename(rs.getString("ems_scan_o_file_name"));
                dispatchDetailsBean.setScantrackReport(rs.getString("ems_trackreport_filename"));

                dispatchList.add(dispatchDetailsBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return dispatchList;
    }

}

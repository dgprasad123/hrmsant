/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpQuarterAllotment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.FileDownload;
import hrms.common.SMSServices;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.EmpQuarterAllotment.FundAllotmentSMSLog;
import hrms.model.EmpQuarterAllotment.FundSanctionOrderBean;
import hrms.model.notification.NotificationBean;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Manoj PC
 */
public class EmpQuarterAllotDAOImpl implements EmpQuarterAllotDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private String uploadPath;
    private String uploadPathqrt;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setUploadPathqrt(String uploadPathqrt) {
        this.uploadPathqrt = uploadPathqrt;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public void saveEmpQuarterDetails(EmpQuarterBean eqBean, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            int qaId = CommonFunctions.getMaxCode(con, "emp_qtr_allot", "qa_id");
            pst = con.prepareStatement("INSERT INTO emp_qtr_allot(qa_id,not_id,not_type,emp_id,quarter_no,allotment_date,possession_date,quarter_rent,unit_qtr,qtr_type,buildn_no,address,water_rent,q_id,sewerage_rent,is_get_hra,municipalty_tax) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)");
            pst.setInt(1, qaId);
            pst.setInt(2, notId);
            pst.setString(3, "QTR_ALLOT");
            pst.setString(4, eqBean.getEmpId());
            pst.setString(5, eqBean.getQuarterNo());
            if (eqBean.getAllotmentDate() != null && !eqBean.getAllotmentDate().equals("")) {

                pst.setTimestamp(6, new Timestamp(sdf.parse(eqBean.getAllotmentDate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (eqBean.getPossessionDate() != null && !eqBean.getPossessionDate().equals("")) {

                pst.setTimestamp(7, new Timestamp(sdf.parse(eqBean.getPossessionDate()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }

            pst.setInt(8, Integer.parseInt(eqBean.getQuarterRent()));
            pst.setString(9, eqBean.getQrtrunit());
            pst.setString(10, eqBean.getQrtrtype());
            pst.setString(11, eqBean.getQtrbldgno());
            pst.setString(12, eqBean.getAddress());
            pst.setInt(13, Integer.parseInt(eqBean.getWaterRent()));
            pst.setInt(14, Integer.parseInt(eqBean.getqId()));
            pst.setInt(15, Integer.parseInt(eqBean.getSewerageRent()));
            pst.setString(16, eqBean.getIsGetHra());
            if (eqBean.getMunicipalityTax() != null && !eqBean.getMunicipalityTax().equals("")) {
                pst.setInt(17, Integer.parseInt(eqBean.getMunicipalityTax()));
            } else {
                pst.setInt(17, 0);
            }
            n = pst.executeUpdate();
            String sbLang = sbDAO.getQtrAllotLangDetails(eqBean, Integer.toString(qaId), eqBean.getEmpId(), "QTR_ALLOT");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "QTR_ALLOT");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getQuarterList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List poolList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT q_id,pool_name from g_qtr_pool order by q_id";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("q_id"));
                so.setLabel(rs.getString("pool_name"));
                poolList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return poolList;
    }

    @Override
    public List getEmpQuarterAllotList(String empId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String isHra = null;
        String isSurrendered = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT EN.is_validated,EA.qa_id, EA.quarter_no, to_char(allotment_date, 'DD-Mon-YYYY') AS allotment_date, to_char(possession_date, 'DD-Mon-YYYY') AS possession_date"
                    + ", quarter_rent,unit_qtr,qtr_type,buildn_no, address, water_rent, is_get_hra, if_surrendered, sewerage_rent, EN.ordno, to_char(EN.orddt, 'DD-Mon-YYYY') AS order_date FROM emp_qtr_allot EA "
                    + " INNER JOIN emp_notification EN ON EA.not_id = EN.not_id"
                    + " WHERE EA.emp_id = '" + empId + "'");

            EmpQuarterBean eqBean = null;

            while (rs.next()) {
                isHra = "No";
                isSurrendered = "No";
                if (rs.getString("is_get_hra") != null && !rs.getString("is_get_hra").equals("")) {
                    if (rs.getString("is_get_hra").equals("Y")) {
                        isHra = "Yes";
                    }
                }
                if (rs.getString("if_surrendered") != null && !rs.getString("if_surrendered").equals("")) {
                    if (rs.getString("if_surrendered").equals("Y")) {
                        isSurrendered = "Yes";
                    }
                }
                eqBean = new EmpQuarterBean();
                eqBean.setAllotmentDate(rs.getString("allotment_date"));
                eqBean.setPossessionDate(rs.getString("possession_date"));
                eqBean.setQuarterNo(rs.getString("quarter_no"));
                eqBean.setQrtrunit(rs.getString("unit_qtr"));
                eqBean.setQrtrtype(rs.getString("qtr_type"));
                eqBean.setQtrbldgno(rs.getString("buildn_no"));
                // System.out.println("unit:"+ eqBean.getQrtrunit());
                eqBean.setAddress(rs.getString("address"));
                eqBean.setOrderNumber(rs.getString("ordno"));
                eqBean.setOrderDate(rs.getString("order_date"));
                eqBean.setQuarterRent(rs.getString("quarter_rent"));
                eqBean.setWaterRent(rs.getString("water_rent"));
                eqBean.setSewerageRent(rs.getString("sewerage_rent"));
                eqBean.setIsGetHra(isHra);
                eqBean.setIfSurrendered(isSurrendered);
                eqBean.setQaId(rs.getInt("qa_id"));

                eqBean.setIsValidated(rs.getString("is_validated"));

                li.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public List getOfficeWiseQuarterList(String officeCode) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String isHra = null;
        String isSurrendered = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT EA.qa_id, GP.post, EM.gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, EA.quarter_no, to_char(allotment_date, 'DD-Mon-YYYY') AS allotment_date, to_char(possession_date, 'DD-Mon-YYYY') AS possession_date"
                    + ", quarter_rent, address, water_rent, is_get_hra, if_surrendered, sewerage_rent, EN.ordno, to_char(EN.orddt, 'DD-Mon-YYYY') AS order_date FROM emp_qtr_allot EA "
                    + " INNER JOIN emp_notification EN ON EA.not_id = EN.not_id"
                    + " LEFT OUTER JOIN emp_mast EM ON EM.emp_id = EN.emp_id"
                    + " LEFT OUTER JOIN g_spc GS ON GS.spc = EM.cur_spc"
                    + " LEFT OUTER JOIN g_post GP on GP.post_code=GS.gpc"
                    + " WHERE EM.cur_off_code = '" + officeCode + "'"
            );

            EmpQuarterBean eqBean = null;

            while (rs.next()) {
                isSurrendered = "No";
                if (rs.getString("is_get_hra") != null && !rs.getString("is_get_hra").equals("")) {
                    if (rs.getString("is_get_hra").equals("Y")) {
                        isHra = "Yes";
                    }
                }
                if (rs.getString("if_surrendered") != null && !rs.getString("if_surrendered").equals("")) {
                    if (rs.getString("if_surrendered").equals("Y")) {
                        isSurrendered = "Yes";
                    }
                }
                eqBean = new EmpQuarterBean();
                eqBean.setAllotmentDate(rs.getString("allotment_date"));
                eqBean.setPossessionDate(rs.getString("possession_date"));
                eqBean.setQuarterNo(rs.getString("quarter_no"));
                eqBean.setAddress(rs.getString("address"));
                eqBean.setOrderNumber(rs.getString("ordno"));
                eqBean.setOrderDate(rs.getString("order_date"));
                eqBean.setQuarterRent(rs.getString("quarter_rent"));
                eqBean.setWaterRent(rs.getString("water_rent"));
                eqBean.setSewerageRent(rs.getString("sewerage_rent"));
                eqBean.setIsGetHra(isHra);
                eqBean.setIfSurrendered(isSurrendered);
                eqBean.setQaId(rs.getInt("qa_id"));
                eqBean.setGpfNo(rs.getString("gpf_no"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setDesignation(rs.getString("post"));
                if (isSurrendered.equals("No")) {
                    li.add(eqBean);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public boolean getAllotedQuarterCount(String empId) {
        int cnt = 0;
        int scount = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isSurrendered = false;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS cnt FROM emp_qtr_allot WHERE emp_id = ?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                cnt = rs.getInt("cnt");
            }

            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS cnt FROM emp_qtr_allot WHERE emp_id = ? AND if_surrendered = 'Y'");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                scount = rs.getInt("cnt");
            }
            if (cnt > scount) {
                isSurrendered = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isSurrendered;
    }

    @Override
    public EmpQuarterBean getAllotedQuarterDetail(String qaId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        EmpQuarterBean eqBean = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT EN.if_visible,EA.qa_id, EA.quarter_no, to_char(allotment_date, 'DD-Mon-YYYY') AS allotment_date, to_char(possession_date, 'DD-Mon-YYYY') AS possession_date"
                    + ", quarter_rent,unit_qtr,qtr_type,buildn_no, address, water_rent, q_id, is_get_hra, if_surrendered, sewerage_rent, EN.ordno, to_char(EN.orddt, 'DD-Mon-YYYY') AS order_date,municipalty_tax FROM emp_qtr_allot EA "
                    + " INNER JOIN emp_notification EN ON EA.not_id = EN.not_id"
                    + " WHERE EA.qa_id = " + qaId
            );

            String isHra = "No";
            String isSurrendered = "No";
            eqBean = new EmpQuarterBean();
            if (rs.next()) {
                if (rs.getString("is_get_hra") != null && !rs.getString("is_get_hra").equals("")) {
                    if (rs.getString("is_get_hra").equals("Y")) {
                        isHra = "Yes";
                    }
                }
                if (rs.getString("if_surrendered") != null && !rs.getString("if_surrendered").equals("")) {
                    if (rs.getString("if_surrendered").equals("Y")) {
                        isSurrendered = "Yes";
                    }
                }

                eqBean.setAllotmentDate(rs.getString("allotment_date"));
                eqBean.setPossessionDate(rs.getString("possession_date"));
                eqBean.setQuarterNo(rs.getString("quarter_no"));
                eqBean.setQrtrunit(rs.getString("unit_qtr"));
                eqBean.setQrtrtype(rs.getString("qtr_type"));
                eqBean.setQtrbldgno(rs.getString("buildn_no"));
                eqBean.setAddress(rs.getString("address"));
                eqBean.setOrderNumber(rs.getString("ordno"));
                eqBean.setOrderDate(rs.getString("order_date"));
                eqBean.setQuarterRent(rs.getString("quarter_rent"));
                eqBean.setWaterRent(rs.getString("water_rent"));
                eqBean.setSewerageRent(rs.getString("sewerage_rent"));
                eqBean.setIsGetHra(rs.getString("is_get_hra"));
                eqBean.setQaId(rs.getInt("qa_id"));
                eqBean.setqId(rs.getString("q_id"));
                eqBean.setIfSurrendered(rs.getString("if_surrendered"));
                eqBean.setMunicipalityTax(rs.getString("municipalty_tax"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    eqBean.setChkNotSBPrint("Y");
                }
                ps = con.prepareStatement("select if_visible,en.not_id,qa_id from\n"
                        + "(select * from emp_qtr_surrender)eqs\n"
                        + " inner join emp_notification en\n"
                        + " on eqs.not_id=en.not_id\n"
                        + " where qa_id=?");
                ps.setInt(1, Integer.parseInt(qaId));
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        eqBean.setChkSBPrintIfSurrender("Y");
                    }
                }
                //System.out.println("if visible surrender:" + eqBean.getChkSBPrintIfSurrender());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eqBean;
    }

    @Override
    public void updateEmpQuarterDetails(EmpQuarterBean eqBean) {
        int n = 0;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        String ifvisible = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE emp_qtr_allot SET quarter_no = ?"
                    + ",allotment_date = ?,possession_date = ?,quarter_rent = ?"
                    + ",unit_qtr=?,qtr_type=?,buildn_no=?,address = ?,water_rent = ?,sewerage_rent = ?"
                    + ",is_get_hra =?,q_id = ?,municipalty_tax=? WHERE qa_id = ?");
            pst.setString(1, eqBean.getQuarterNo());
            pst.setInt(4, Integer.parseInt(eqBean.getQuarterRent()));
            pst.setString(5, eqBean.getQrtrunit());
            pst.setString(6, eqBean.getQrtrtype());
            pst.setString(7, eqBean.getQtrbldgno());
            pst.setString(8, eqBean.getAddress());
            pst.setInt(9, Integer.parseInt(eqBean.getWaterRent()));
            pst.setInt(10, Integer.parseInt(eqBean.getSewerageRent()));
            pst.setString(11, eqBean.getIsGetHra());
            pst.setInt(12, Integer.parseInt(eqBean.getqId()));
            if (eqBean.getAllotmentDate() != null && !eqBean.getAllotmentDate().equals("")) {

                pst.setTimestamp(2, new Timestamp(sdf.parse(eqBean.getAllotmentDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (eqBean.getPossessionDate() != null && !eqBean.getPossessionDate().equals("")) {

                pst.setTimestamp(3, new Timestamp(sdf.parse(eqBean.getPossessionDate()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (eqBean.getMunicipalityTax() != null && !eqBean.getMunicipalityTax().equals("")) {
                pst.setInt(13, Integer.parseInt(eqBean.getMunicipalityTax()));
            } else {
                pst.setInt(13, 0);
            }
            pst.setInt(14, eqBean.getQaId());
            n = pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            //Now update order no in emp_notification table            
            int notificationId = 0;
            st = con.createStatement();

            rs = st.executeQuery("SELECT not_id FROM emp_qtr_allot"
                    + " WHERE qa_id = " + eqBean.getQaId()
            );

            if (rs.next()) {
                notificationId = rs.getInt("not_id");
            }

            DataBaseFunctions.closeSqlObjects(rs, st);

            //DataBaseFunctions.closeSqlObjects(rs, st);
            if (eqBean.getChkNotSBPrint() != null && eqBean.getChkNotSBPrint().equals("Y")) {
                ifvisible = "N";
            } else {
                ifvisible = "Y";
            }
            String sbQtrLang = sbDAO.getQtrAllotLangDetails(eqBean, Integer.toString(eqBean.getQaId()), eqBean.getEmpId(), "QTR_ALLOT");
            pst = con.prepareStatement("UPDATE emp_notification SET ordno = ?, orddt = ?,if_visible = ?,sb_description=? WHERE not_id = ?");
            pst.setString(1, eqBean.getOrderNumber());
            pst.setTimestamp(2, new Timestamp(sdf.parse(eqBean.getOrderDate()).getTime()));
            pst.setString(3, ifvisible);
            pst.setString(4, sbQtrLang);
            pst.setInt(5, notificationId);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            String sbLang = sbDAO.getQtrSurrenderLangDetails(eqBean, Integer.toString(eqBean.getQaId()), eqBean.getEmpId(), "QTR_SURRENDER");
            pst1 = con.prepareStatement("update emp_notification set if_visible=?,sb_description=? WHERE not_type='QTR_SURRENDER' "
                    + "and  NOT_ID =(select not_id from emp_qtr_surrender where qa_id=?)");

            if (eqBean.getChkSBPrintIfSurrender() != null && eqBean.getChkSBPrintIfSurrender().equals("Y")) {
                pst1.setString(1, "N");

            } else {
                pst1.setString(1, "Y");
            }
            pst1.setString(2, sbLang);
            pst1.setInt(3, eqBean.getQaId());
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveSurrenderQuarter(String qaId, String empId, int notId, String surrenderDate) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;

        EmpQuarterBean eqBean = new EmpQuarterBean();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            int qsId = CommonFunctions.getMaxCode(con, "emp_qtr_surrender", "qs_id");
            pst = con.prepareStatement("INSERT INTO emp_qtr_surrender(qs_id,qa_id,not_id,not_type,emp_id,surrender_date) VALUES(?, ?, ?, ?, ?, ?)");
            pst.setInt(1, qsId);
            pst.setInt(2, Integer.parseInt(qaId));
            pst.setInt(3, notId);
            pst.setString(4, "QTR_SURRENDER");
            pst.setString(5, empId);
            if (surrenderDate != null && !surrenderDate.equals("")) {

                pst.setTimestamp(6, new Timestamp(sdf.parse(surrenderDate).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }

            n = pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("UPDATE emp_qtr_allot SET if_surrendered = 'Y' WHERE qa_id = ?");
            pst.setInt(1, Integer.parseInt(qaId));
            pst.executeUpdate();

            String sbLang = sbDAO.getQtrSurrenderLangDetails(eqBean, qaId, empId, "QTR_SURRENDER");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "QTR_SURRENDER");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getQuarterUnitAreaList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT distinct unit_qt from consumer_ga where HRMSID is not null order by unit_qt";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("unit_qt"));
                so.setLabel(rs.getString("unit_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getEquarterUnitList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT distinct unit from equarter.qtrmast order by unit";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("unit"));
                so.setLabel(rs.getString("unit"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getQuarterUnitList(String loginName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT distinct unit_qt from consumer_ga where unit_ph=? or unit_qt=?   order by unit_qt";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginName);
            pstmt.setString(2, loginName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("unit_qt"));
                so.setLabel(rs.getString("unit_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getQuarterUnitListRentOff() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            String sql = "SELECT distinct unit_qt from consumer_ga order by unit_qt";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("unit_qt"));
                so.setLabel(rs.getString("unit_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt, con);
        }
        return unitAreaList;
    }

    @Override
    public String saveQuarterRepairOrder(FundSanctionOrderBean fundSanctionOrderBean) {
        String msg = "Sucess";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            Date orderdat = CommonFunctions.getDateFromString(fundSanctionOrderBean.getOrderDate(), "dd-MMM-yyyy");
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO quarter_fund_allotment_order (order_no, order_date, type_of_Work, financial_Year) values (?,?,?,?) ");
            pstmt.setString(1, fundSanctionOrderBean.getOrderNumber());
            pstmt.setTimestamp(2, new Timestamp(orderdat.getTime()));
            pstmt.setString(3, fundSanctionOrderBean.getTypeofWork());
            pstmt.setString(4, fundSanctionOrderBean.getFinancialYear());
            pstmt.execute();
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return msg;
    }

    @Override
    public String sendQuarterRepairOrderSMS(FundSanctionOrderBean fundSanctionOrderBean) {
        String msg = "Sucess";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE quarter_fund_allotment_order SET smssend = 'Y' WHERE  allotment_order_id=?");
            pstmt.setInt(1, fundSanctionOrderBean.getAllotmentOrderId());
            pstmt.execute();
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return msg;
    }

    @Override
    public List getQuarterRepairOrderDetailList(int allotmentOrderId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List quarterRepairOrderDetailList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select fund_id,quarter_fund_allotment.emp_id,unit_qt,type_qt,bldgno_qt,alloted_amt,smsstring,quarter_fund_allotment.mobile,sms_delivery_status,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from quarter_fund_allotment "
                    + "inner join consumer_ga on quarter_fund_allotment.phslno = consumer_ga.phslno "
                    + "left outer join emp_mast on quarter_fund_allotment.emp_id = emp_mast.emp_id "
                    + "where allotment_order_id=? order by fund_id");
            pstmt.setInt(1, allotmentOrderId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FundSanctionOrderBean fundSanctionOrderBean = new FundSanctionOrderBean();
                fundSanctionOrderBean.setFundid(rs.getInt("fund_id"));
                fundSanctionOrderBean.setEmpid(rs.getString("emp_id"));
                fundSanctionOrderBean.setEmpname(rs.getString("EMPNAME"));
                fundSanctionOrderBean.setQrtrunit(rs.getString("unit_qt"));
                fundSanctionOrderBean.setQrtrtype(rs.getString("type_qt"));
                fundSanctionOrderBean.setQuarterNo(rs.getString("bldgno_qt"));
                fundSanctionOrderBean.setSanctionamt(rs.getInt("alloted_amt"));
                fundSanctionOrderBean.setMobileno(rs.getString("mobile"));
                fundSanctionOrderBean.setSmsstring(rs.getString("smsstring"));
                fundSanctionOrderBean.setSmsDeliveryStatus(rs.getString("sms_delivery_status"));
                quarterRepairOrderDetailList.add(fundSanctionOrderBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterRepairOrderDetailList;
    }

    @Override
    public FundSanctionOrderBean getQuarterRepairOrderDetail(int allotmentOrderId) {
        FundSanctionOrderBean fundSanctionOrderBean = new FundSanctionOrderBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT allotment_order_id,order_no,to_char(order_date, 'dd-Mon-yyyy') as order_date,type_of_work,financial_year,smssend FROM quarter_fund_allotment_order where allotment_order_id=?");
            pstmt.setInt(1, allotmentOrderId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                fundSanctionOrderBean.setAllotmentOrderId(rs.getInt("allotment_order_id"));
                fundSanctionOrderBean.setOrderNumber(rs.getString("order_no"));
                fundSanctionOrderBean.setOrderDate(rs.getString("order_date"));
                fundSanctionOrderBean.setTypeofWork(rs.getString("type_of_work"));
                fundSanctionOrderBean.setFinancialYear(rs.getString("financial_year"));
                fundSanctionOrderBean.setSmssend(rs.getString("smssend"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return fundSanctionOrderBean;
    }

    @Override
    public List getQuarterRepairOrderList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List quarterRepairOrderList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT allotment_order_id,order_no,to_char(order_date, 'dd-Mon-yyyy') as order_date,type_of_work,"
                    + "financial_year,smssend FROM quarter_fund_allotment_order order by allotment_order_id desc");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FundSanctionOrderBean fundSanctionOrderBean = new FundSanctionOrderBean();
                fundSanctionOrderBean.setAllotmentOrderId(rs.getInt("allotment_order_id"));
                fundSanctionOrderBean.setOrderNumber(rs.getString("order_no"));
                fundSanctionOrderBean.setOrderDate(rs.getString("order_date"));
                fundSanctionOrderBean.setTypeofWork(rs.getString("type_of_work"));
                fundSanctionOrderBean.setFinancialYear(rs.getString("financial_year"));
                fundSanctionOrderBean.setSmssend(rs.getString("smssend"));
                quarterRepairOrderList.add(fundSanctionOrderBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterRepairOrderList;
    }

    @Override
    public List getUnitAreawiseQuarterType(String unitArea) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct type_qt from consumer_ga where unit_qt=?  order by type_qt");
            pstmt.setString(1, unitArea);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("type_qt"));
                so.setLabel(rs.getString("type_qt"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getEquarterUnitAreawiseQuarterType(String unitArea) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct qrtr_type from equarter.qtrmast where unit=?  order by qrtr_type");
            pstmt.setString(1, unitArea);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("qrtr_type"));
                so.setLabel(rs.getString("qrtr_type"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getQuarterData(String qrtrunit, String qrtrtype) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,transfer_id FROM consumer_ga "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID WHERE unit_qt=? AND type_qt=?");
            pstmt.setString(1, qrtrunit);
            pstmt.setString(2, qrtrtype);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setQaId(rs.getInt("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setTransferId(rs.getString("transfer_id"));
                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public List getQuarterDataRentOfficer(String qrtrunit, String qrtrtype) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,transfer_id FROM consumer_ga "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID WHERE unit_qt=? AND type_qt=?");
            pstmt.setString(1, qrtrunit);
            pstmt.setString(2, qrtrtype);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setQaId(rs.getInt("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setTransferId(rs.getString("transfer_id"));
                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public boolean updateQuarterAllotment(EmpQuarterBean empQuarterBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean isUpdated = true;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE consumer_ga SET HRMSID = ? WHERE PHSLNO=?");
            pstmt.setString(1, empQuarterBean.getNewempId());
            pstmt.setInt(2, empQuarterBean.getQaId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return isUpdated;
    }

    @Override
    public boolean updateEmpQuarterAllotment(String newempId, String qrtrunit, String qrtrtype, String quarterNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        boolean isUpdated = true;
        try {
            /*unit_qt character varying(50),
             type_qt character varying(50),
             bldgno_qt*/
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE consumer_ga SET HRMSID = ?,transfer_id=null,retention_status=null,vacation_notice=null,vacation_status=null WHERE unit_qt=? and type_qt=? and bldgno_qt=?");
            pstmt.setString(1, newempId);
            pstmt.setString(2, qrtrunit);
            pstmt.setString(3, qrtrtype);
            pstmt.setString(4, quarterNo);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return isUpdated;
    }

    @Override
    public EmpQuarterBean getQuarterDetail(int qaId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmpQuarterBean eqBean = new EmpQuarterBean();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM consumer_ga "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE phslno=?");
            pstmt.setInt(1, qaId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                eqBean.setQaId(rs.getInt("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setDesignation(rs.getString("SPN"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return eqBean;
    }

    @Override
    public String saveQuarterRepairOrderDetail(FundSanctionOrderBean fundSanctionOrderBean) {
        String msg = "S";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            String sms = "";
            if (fundSanctionOrderBean.getTypeofWork() != null && fundSanctionOrderBean.getTypeofWork().equals("")) {
                sms = "An amount of " + fundSanctionOrderBean.getSanctionamt() + " has been sanctioned for repair and renovation of "
                        + "your Quarter no:" + fundSanctionOrderBean.getQrtrunit() + "-" + fundSanctionOrderBean.getQrtrtype() + "/" + fundSanctionOrderBean.getQuarterNo();
            } else {
                String typeofwork = "";
                if (fundSanctionOrderBean.getTypeofWork().equals("C")) {
                    typeofwork = "Civil";
                } else if (fundSanctionOrderBean.getTypeofWork().equals("P")) {
                    typeofwork = "PH";
                } else if (fundSanctionOrderBean.getTypeofWork().equals("E")) {
                    typeofwork = "Electrical";
                }
                sms = "An amount of " + fundSanctionOrderBean.getSanctionamt() + " has been sanctioned towards " + typeofwork + " work for repair and renovation of "
                        + "your Quarter no:" + fundSanctionOrderBean.getQrtrunit() + "-" + fundSanctionOrderBean.getQrtrtype() + "/" + fundSanctionOrderBean.getQuarterNo() + " for the year " + fundSanctionOrderBean.getFinancialYear();
            }
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select phslno,mobile,hrmsid,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') from consumer_ga "
                    + "left outer join emp_mast on consumer_ga.hrmsid = emp_mast.emp_id "
                    + "where unit_qt=? and type_qt=? and bldgno_qt=?");
            pstmt.setString(1, fundSanctionOrderBean.getQrtrunit());
            pstmt.setString(2, fundSanctionOrderBean.getQrtrtype());
            pstmt.setString(3, fundSanctionOrderBean.getQuarterNo());
            res = pstmt.executeQuery();
            if (res.next()) {
                fundSanctionOrderBean.setPhslno(res.getInt("phslno"));
                fundSanctionOrderBean.setMobileno(res.getString("mobile"));
                fundSanctionOrderBean.setEmpid(res.getString("hrmsid"));
            }

            DataBaseFunctions.closeSqlObjects(pstmt);

            pstmt = con.prepareStatement("INSERT INTO quarter_fund_allotment(phslno,mobile,alloted_amt,smsstring,emp_id,allotment_order_id) VALUES (?,?,?,?,?,?)");
            pstmt.setInt(1, fundSanctionOrderBean.getPhslno());
            pstmt.setString(2, fundSanctionOrderBean.getMobileno());
            pstmt.setInt(3, fundSanctionOrderBean.getSanctionamt());
            pstmt.setString(4, sms);
            pstmt.setString(5, fundSanctionOrderBean.getEmpid());
            pstmt.setInt(6, fundSanctionOrderBean.getAllotmentOrderId());
            pstmt.executeUpdate();
            //SMSServices smhttp = new SMSServices(eqBean.getMobileno(), sms);
        } catch (Exception e) {
            msg = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt, con);
        }
        return msg;
    }

    @Override
    public String updateQuarterRepairOrderSMS(FundSanctionOrderBean fundSanctionOrderBean) {
        String msg = "S";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE quarter_fund_allotment SET smsstring=? WHERE fund_id=?");
            pstmt.setString(1, fundSanctionOrderBean.getSmsstring());
            pstmt.setInt(2, fundSanctionOrderBean.getFundid());
            pstmt.executeUpdate();
        } catch (Exception e) {
            msg = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return msg;
    }

    @Override
    public String deleteQuarterRepairOrderDetail(int fundid) {
        String msg = "S";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM quarter_fund_allotment WHERE fund_id=?");
            pstmt.setInt(1, fundid);
            pstmt.executeUpdate();
        } catch (Exception e) {
            msg = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return msg;
    }
//SMSServices smhttp = new SMSServices(eqBean.getMobileno(), sms);    

    @Override
    public List getFundAllotmentLog(int phslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List fundAllotmentLogList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select quarter_fund_allotment.mobile,order_no,order_date,alloted_amt,smsstring,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') as fullname,bldgno_qt,type_qt,unit_qt from quarter_fund_allotment "
                    + "LEFT OUTER JOIN EMP_MAST ON quarter_fund_allotment.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN consumer_ga ON quarter_fund_allotment.phslno = consumer_ga.phslno "
                    + "where quarter_fund_allotment.phslno=?");
            pstmt.setInt(1, phslno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FundAllotmentSMSLog fasl = new FundAllotmentSMSLog();
                fasl.setEmployeeName(rs.getString("fullname"));
                fasl.setMobileno(rs.getString("mobile"));
                fasl.setOrderno(rs.getString("order_no"));
                fasl.setOrder_date(rs.getString("order_date"));
                fasl.setAllotedAmt(rs.getInt("alloted_amt"));
                fundAllotmentLogList.add(fasl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
        return fundAllotmentLogList;
    }

    @Override
    public List getTransferList() {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            con = dataSource.getConnection();

            Date comparedDate = sdformat.parse("2018-01-01");
            Date relieveDate = null;

            pstmt = con.prepareStatement("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                    + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                    + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                    + " WHERE  hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND "
                    + " g_office.dist_code NOT IN ('2112','2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')  AND "
                    + " g_office.tr_code NOT IN ('0701','0791','1891','1892','1801') ORDER BY EMPNAME");

            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                String emp_id = rs.getString("hrmsid");

                pstm = con.prepareStatement("SELECT join_date,emp_join.not_id,rlv_date FROM emp_relieve LEFT OUTER JOIN emp_join ON emp_relieve.not_id=emp_join.not_id  "
                        + "WHERE emp_relieve.emp_id=? AND emp_relieve.not_type='TRANSFER' AND emp_relieve.emp_id=emp_join.emp_id AND join_date IS NOT NULL AND (rlv_date is NOT NULL)  ORDER BY rlv_date DESC LIMIT 1");
                pstm.setString(1, emp_id);
                rs1 = pstm.executeQuery();
                String jdate = "";
                String rdate = "";

                if (rs1.next()) {
                    jdate = rs1.getString("join_date");
                    rdate = rs1.getString("rlv_date");

                    relieveDate = sdformat.parse(rdate);

                    if (rs1.getString("join_date") != null && !rs1.getString("join_date").equals("")) {
                        jdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("join_date"));
                    }
                    if (rs1.getString("rlv_date") != null && !rs1.getString("rlv_date").equals("")) {
                        rdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("rlv_date"));
                    }

                }

//. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setTransferId("");
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));
                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setJoinDate(jdate);
                eqBean.setRelieveDate(rdate);
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                /*if (relieveDate != null && relieveDate.compareTo(comparedDate) > 0) {
                 quarterList.add(eqBean);
                 }*/
                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
            DataBaseFunctions.closeSqlObjects(rs1, pstm, con);
        }
        return quarterList;
    }

    @Override
    public List getTransferList(String UserName) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            con = dataSource.getConnection();

            Date comparedDate = sdformat.parse("2018-01-01");
            Date relieveDate = null;
            System.out.println("UserNmae: ========================" + UserName);
            String sectionNum = UserName.substring(13);
            System.out.println("sectionNum++++++++++++++++++:" + sectionNum);
            pstmt = con.prepareStatement("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga"
                    + " INNER JOIN emp_mast ON hrmsid=emp_id"
                    + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                    + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                    + " WHERE  hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND consumer_ga.section = '" + sectionNum + "' AND "
                    + " g_office.dist_code NOT IN ('2112','2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')  AND "
                    + " g_office.tr_code NOT IN ('0701','0791','1891','1892','1801') ORDER BY EMPNAME");

            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                String emp_id = rs.getString("hrmsid");

                pstm = con.prepareStatement("SELECT join_date,emp_join.not_id,rlv_date FROM emp_relieve LEFT OUTER JOIN emp_join ON emp_relieve.not_id=emp_join.not_id  "
                        + "WHERE emp_relieve.emp_id=? AND emp_relieve.not_type='TRANSFER' AND emp_relieve.emp_id=emp_join.emp_id AND join_date IS NOT NULL AND (rlv_date is NOT NULL)  ORDER BY rlv_date DESC LIMIT 1");
                pstm.setString(1, emp_id);
                rs1 = pstm.executeQuery();
                String jdate = "";
                String rdate = "";

                if (rs1.next()) {
                    jdate = rs1.getString("join_date");
                    rdate = rs1.getString("rlv_date");

                    relieveDate = sdformat.parse(rdate);

                    if (rs1.getString("join_date") != null && !rs1.getString("join_date").equals("")) {
                        jdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("join_date"));
                    }
                    if (rs1.getString("rlv_date") != null && !rs1.getString("rlv_date").equals("")) {
                        rdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("rlv_date"));
                    }

                }

//. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setTransferId("");
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));
                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setJoinDate(jdate);
                eqBean.setRelieveDate(rdate);
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                /*if (relieveDate != null && relieveDate.compareTo(comparedDate) > 0) {
                 quarterList.add(eqBean);
                 }*/
                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
            DataBaseFunctions.closeSqlObjects(rs1, pstm, con);
        }
        return quarterList;
    }

    @Override
    public List searchtransferCategory(String Octype) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = "";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            con = dataSource.getConnection();
            Date comparedDate = sdformat.parse("2018-01-01");
            Date relieveDate = null;

            if (Octype.equals("3") && Octype != null) {

                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE   hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND "
                        + " g_office.dist_code NOT IN ('2112','2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')  AND "
                        + " g_office.tr_code NOT IN ('0701','0791','1891','1892','1801') ORDER BY EMPNAME");
            }
            if (Octype.equals("2") && Octype != null) {

                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND "
                        + " g_office.dist_code  IN ('2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')   "
                        + "  ORDER BY EMPNAME");
            }
            if (Octype.equals("1") && Octype != null) {
                sql = ("select district2.dist_name dist_name,office2.off_en,document_submission,eviction_notice,vacation_notice,vacation_status,retention_status,phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,dos  from consumer_ga "
                        + " inner join emp_mast on consumer_ga.hrmsid=emp_mast.emp_id "
                        + " inner join emp_transfer on consumer_ga.hrmsid=emp_transfer.emp_id "
                        + " inner join emp_relieve on emp_transfer.not_id=emp_relieve.not_id "
                        + " left outer join g_spc spc1 on emp_relieve.spc=spc1.spc "
                        + " left outer join g_office office1 on spc1.off_code=office1.off_code "
                        + " left outer join g_district district1 on office1.dist_code=district1.dist_code "
                        + " left outer join g_spc spc2 on emp_transfer.next_spc=spc2.spc "
                        + " left outer join g_office office2 on spc2.off_code=office2.off_code "
                        + " left outer join g_district district2 on office2.dist_code=district2.dist_code "
                        + " where district1.dist_code in ('2112','2117') and district2.dist_code in ('2112','2117')"
                        + " and district1.dist_name<>district2.dist_name and (hide_show IS NULL AND consumer_ga.hrmsid is not null  and consumer_ga.hrmsid<>'') order by EMPNAME");

            }
            if (Octype.equals("4") && Octype != null) {
                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE hide_show IS NULL AND  hrmsid IS NOT NULL AND  dos < NOW() AND  EMP_MAST.dep_code !='10'  "
                        + "  ORDER BY EMPNAME");
            }
            if (Octype.equals("7") && Octype != null) {
                sql = ("SELECT vacation_status,document_submission,dos,dob,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE hide_show IS NULL AND  hrmsid IS NOT NULL AND    dos <= date_trunc('month', now()) + interval '4 month' AND dos >= now() AND  EMP_MAST.dep_code !='10'  "
                        + "  ORDER BY EMPNAME");
            }
            if (Octype.equals("5") && Octype != null) {
                sql = ("  SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name,date_of_deceased FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id "
                        + " INNER JOIN emp_deceased ON hrmsid=emp_deceased.emp_id "
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND EMP_MAST.dep_code ='10' ORDER BY EMPNAME	 ");
            }
            if (Octype.equals("6") && Octype != null) {
                sql = ("  SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name,wefd FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " INNER JOIN emp_suspension ON hrmsid=emp_suspension.emp_id "
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND EMP_MAST.dep_code ='05' ORDER BY EMPNAME 	 ");

            }

            //System.out.println("sql=" + sql);
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                //. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setTransferId("");
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));

                if ((Octype.equals("3") || Octype.equals("2") || Octype.equals("1")) && Octype != null) {
                    String emp_id = rs.getString("hrmsid");
                    pstm = con.prepareStatement("SELECT join_date,emp_join.not_id,rlv_date FROM emp_relieve LEFT OUTER JOIN emp_join ON emp_relieve.not_id=emp_join.not_id  "
                            + "WHERE emp_relieve.emp_id=? AND emp_relieve.not_type='TRANSFER' AND emp_relieve.emp_id=emp_join.emp_id AND join_date IS NOT NULL AND (rlv_date is NOT NULL)  ORDER BY rlv_date DESC LIMIT 1");

                    pstm.setString(1, emp_id);
                    rs1 = pstm.executeQuery();
                    String jdate = "";
                    String rdate = "";

                    while (rs1.next()) {
                        jdate = rs1.getString("join_date");
                        rdate = rs1.getString("rlv_date");
                        relieveDate = sdformat.parse(rdate);

                        if (rs1.getString("join_date") != null && !rs1.getString("join_date").equals("")) {
                            jdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("join_date"));
                        }
                        if (rs1.getString("rlv_date") != null && !rs1.getString("rlv_date").equals("")) {
                            rdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("rlv_date"));
                        }

                    }
                    eqBean.setJoinDate(jdate);
                    eqBean.setRelieveDate(rdate);
                }
                DataBaseFunctions.closeSqlObjects(rs1, pstm);

                if (Octype.equals("5") && Octype != null) {
                    String ddate = "";
                    if (rs.getString("date_of_deceased") != null && !rs.getString("date_of_deceased").equals("")) {
                        ddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_deceased"));
                    }
                    relieveDate = sdformat.parse(rs.getString("date_of_deceased"));
                    eqBean.setRelieveDate(ddate);
                }
                if (Octype.equals("6") && Octype != null) {
                    String wdate = "";
                    if (rs.getString("wefd") != null && !rs.getString("wefd").equals("")) {
                        wdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("wefd"));
                    }
                    relieveDate = sdformat.parse(rs.getString("wefd"));
                    eqBean.setRelieveDate(wdate);
                }

                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }
                if ((Octype.equals("7")) && Octype != null) {
                    if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                        eqBean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    } else {
                        eqBean.setDob("");
                    }
                }

                if ((Octype.equals("4") || Octype.equals("7")) && Octype != null) {
                    eqBean.setJoinDate("");
                    eqBean.setRelieveDate("");
                    relieveDate = sdformat.parse(rs.getString("dos"));
                }

                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                /*  if (relieveDate != null && relieveDate.compareTo(comparedDate) > 0) {
                 quarterList.add(eqBean);
                 }*/
                quarterList.add(eqBean);

                /* String sql1 = "UPDATE consumer_ga SET ocu_type=? WHERE consumer_no=? AND hrmsid=?";
                 pstmt = con.prepareStatement(sql1);
                 System.out.println("Octype.length()"+Octype.length());
                 pstmt.setString(1, Octype.trim());
                 pstmt.setString(2, rs.getString("CONSUMER_NO"));
                 pstmt.setString(3, rs.getString("hrmsid"));
                 pstmt.executeUpdate();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
            DataBaseFunctions.closeSqlObjects(rs1, pstm, con);
        }
        return quarterList;
    }

    @Override
    public List searchtransferCategory(String Octype, String UserName) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = "";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            con = dataSource.getConnection();
            Date comparedDate = sdformat.parse("2018-01-01");
            Date relieveDate = null;
            //  System.out.println("UserNmae: ========================"+UserName);
            String sectionNum = UserName.substring(13);
            //  System.out.println("sectionNum++++++++++++++++++:"+sectionNum);
            if (Octype.equals("3") && Octype != null) {

                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE   hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND consumer_ga.section = '" + sectionNum + "' AND "
                        + " g_office.dist_code NOT IN ('2112','2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')  AND "
                        + " g_office.tr_code NOT IN ('0701','0791','1891','1892','1801') ORDER BY EMPNAME");
            }
            if (Octype.equals("2") && Octype != null) {

                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND hrmsid IS NOT NULL AND  dos > NOW() AND  EMP_MAST.dep_code !='10' AND consumer_ga.section = '" + sectionNum + "' AND "
                        + " g_office.dist_code  IN ('2129','2124','2126','2120','2122','2121','2130','2127','2128','2125','2123')   "
                        + "  ORDER BY EMPNAME");
            }
            if (Octype.equals("1") && Octype != null) {
                sql = ("select district2.dist_name dist_name,office2.off_en,document_submission,eviction_notice,vacation_notice,vacation_status,retention_status,phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,dos  from consumer_ga "
                        + " inner join emp_mast on consumer_ga.hrmsid=emp_mast.emp_id "
                        + " inner join emp_transfer on consumer_ga.hrmsid=emp_transfer.emp_id "
                        + " inner join emp_relieve on emp_transfer.not_id=emp_relieve.not_id "
                        + " left outer join g_spc spc1 on emp_relieve.spc=spc1.spc "
                        + " left outer join g_office office1 on spc1.off_code=office1.off_code "
                        + " left outer join g_district district1 on office1.dist_code=district1.dist_code "
                        + " left outer join g_spc spc2 on emp_transfer.next_spc=spc2.spc "
                        + " left outer join g_office office2 on spc2.off_code=office2.off_code "
                        + " left outer join g_district district2 on office2.dist_code=district2.dist_code "
                        + " where district1.dist_code in ('2112','2117') and district2.dist_code in ('2112','2117') AND consumer_ga.section = '" + sectionNum + "'"
                        + " and district1.dist_name<>district2.dist_name and (hide_show IS NULL AND consumer_ga.hrmsid is not null  and consumer_ga.hrmsid<>'') order by EMPNAME");

            }
            if (Octype.equals("4") && Octype != null) {
                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE hide_show IS NULL AND  hrmsid IS NOT NULL AND  dos < NOW() AND  EMP_MAST.dep_code !='10'  AND consumer_ga.section = '" + sectionNum + "'"
                        + "  ORDER BY EMPNAME");
            }
            if (Octype.equals("7") && Octype != null) {
                sql = ("SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE hide_show IS NULL AND  hrmsid IS NOT NULL AND  dos <= date_trunc('month', now()) + interval '4 month' AND dos >= now() AND  EMP_MAST.dep_code !='10'  AND consumer_ga.section = '" + sectionNum + "'"
                        + "  ORDER BY EMPNAME");

            }
            if (Octype.equals("5") && Octype != null) {
                sql = ("  SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name,date_of_deceased FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id "
                        + " INNER JOIN emp_deceased ON hrmsid=emp_deceased.emp_id "
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND EMP_MAST.dep_code ='10' AND consumer_ga.section = '" + sectionNum + "' ORDER BY EMPNAME");
            }
            if (Octype.equals("6") && Octype != null) {
                sql = ("  SELECT vacation_status,document_submission,dos,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name,wefd FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " INNER JOIN emp_suspension ON hrmsid=emp_suspension.emp_id "
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE  hide_show IS NULL AND EMP_MAST.dep_code ='05' AND consumer_ga.section = '" + sectionNum + "' ORDER BY EMPNAME 	 ");

            }

            //System.out.println("sql=" + sql);
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                //. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setTransferId("");
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));

                if ((Octype.equals("3") || Octype.equals("2") || Octype.equals("1")) && Octype != null) {
                    String emp_id = rs.getString("hrmsid");
                    pstm = con.prepareStatement("SELECT join_date,emp_join.not_id,rlv_date FROM emp_relieve LEFT OUTER JOIN emp_join ON emp_relieve.not_id=emp_join.not_id  "
                            + "WHERE emp_relieve.emp_id=? AND emp_relieve.not_type='TRANSFER' AND emp_relieve.emp_id=emp_join.emp_id AND join_date IS NOT NULL AND (rlv_date is NOT NULL)  ORDER BY rlv_date DESC LIMIT 1");

                    pstm.setString(1, emp_id);
                    rs1 = pstm.executeQuery();
                    String jdate = "";
                    String rdate = "";

                    while (rs1.next()) {
                        jdate = rs1.getString("join_date");
                        rdate = rs1.getString("rlv_date");
                        relieveDate = sdformat.parse(rdate);

                        if (rs1.getString("join_date") != null && !rs1.getString("join_date").equals("")) {
                            jdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("join_date"));
                        }
                        if (rs1.getString("rlv_date") != null && !rs1.getString("rlv_date").equals("")) {
                            rdate = CommonFunctions.getFormattedOutputDate1(rs1.getDate("rlv_date"));
                        }

                    }
                    eqBean.setJoinDate(jdate);
                    eqBean.setRelieveDate(rdate);
                }
                DataBaseFunctions.closeSqlObjects(rs1, pstm);

                if (Octype.equals("5") && Octype != null) {
                    String ddate = "";
                    if (rs.getString("date_of_deceased") != null && !rs.getString("date_of_deceased").equals("")) {
                        ddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_deceased"));
                    }
                    relieveDate = sdformat.parse(rs.getString("date_of_deceased"));
                    eqBean.setRelieveDate(ddate);
                }
                if (Octype.equals("6") && Octype != null) {
                    String wdate = "";
                    if (rs.getString("wefd") != null && !rs.getString("wefd").equals("")) {
                        wdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("wefd"));
                    }
                    relieveDate = sdformat.parse(rs.getString("wefd"));
                    eqBean.setRelieveDate(wdate);
                }

                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }

                if ((Octype.equals("4") || Octype.equals("7")) && Octype != null) {
                    eqBean.setJoinDate("");
                    eqBean.setRelieveDate("");
                    relieveDate = sdformat.parse(rs.getString("dos"));
                }

                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                /*  if (relieveDate != null && relieveDate.compareTo(comparedDate) > 0) {
                 quarterList.add(eqBean);
                 }*/
                quarterList.add(eqBean);

                /* String sql1 = "UPDATE consumer_ga SET ocu_type=? WHERE consumer_no=? AND hrmsid=?";
                 pstmt = con.prepareStatement(sql1);
                 System.out.println("Octype.length()"+Octype.length());
                 pstmt.setString(1, Octype.trim());
                 pstmt.setString(2, rs.getString("CONSUMER_NO"));
                 pstmt.setString(3, rs.getString("hrmsid"));
                 pstmt.executeUpdate();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
            DataBaseFunctions.closeSqlObjects(rs1, pstm, con);
        }
        return quarterList;
    }

    @Override
    public List searchretiringCategoryResult(String Octype, String fdate, String tdate) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sql = "";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            con = dataSource.getConnection();
            Date comparedDate = sdformat.parse("2018-01-01");
            Date relieveDate = null;

            if (Octype.equals("7") && Octype != null) {
                sql = ("SELECT vacation_status,document_submission,dos,dob,eviction_notice,vacation_notice,retention_status,consumer_no,hrmsid,unit_qt,type_qt,bldgno_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,cur_off_code,g_office.off_en,g_district.dist_name FROM consumer_ga INNER JOIN emp_mast ON hrmsid=emp_id"
                        + " LEFT OUTER JOIN g_office ON g_office.off_code=emp_mast.cur_off_code "
                        + " LEFT OUTER JOIN g_district ON g_office.dist_code=g_district.dist_code "
                        + " WHERE hide_show IS NULL AND  hrmsid IS NOT NULL AND     (dos BETWEEN ? AND ?)  AND  EMP_MAST.dep_code !='10'  "
                        + "  ORDER BY EMPNAME");

            }

            System.out.println("sql=" + sql);
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                //. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setTransferId("");
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));

                DataBaseFunctions.closeSqlObjects(rs1, pstm);

                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }
                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    //System.out.println("DOB: "+CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    eqBean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    eqBean.setDob("");
                }

                if ((Octype.equals("4") || Octype.equals("7")) && Octype != null) {
                    eqBean.setJoinDate("");
                    eqBean.setRelieveDate("");
                    relieveDate = sdformat.parse(rs.getString("dos"));
                }

                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                /*  if (relieveDate != null && relieveDate.compareTo(comparedDate) > 0) {
                 quarterList.add(eqBean);
                 }*/
                quarterList.add(eqBean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
            DataBaseFunctions.closeSqlObjects(rs1, pstm, con);
        }
        return quarterList;
    }

    @Override
    public void SaveRetentionData(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String enddate = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT address1,address2,address3,bldgno_qt FROM consumer_ga WHERE CONSUMER_NO=? AND hrmsid=?");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("address1");
                unitqt = rs.getString("address2");
                typeqt = rs.getString("address3");
                bldgno = rs.getString("bldgno_qt");

            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            String otype = qbean.getOccupationTypes();
            String sql = "INSERT INTO quarter_tracking (order_no,order_date,retention_type, retention_subtype, hrms_id, qrt_no, qrt_type,unit_area,consumer_no, office_from_code, office_to_code,quarter_allowed_from,quarter_allowed_to,vacate_date,retiremnt_wef,retirement_cancelled_wef,post_from_office,post_to_office,licence_fee_date,otype) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, qbean.getOrderNumber());
            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, qbean.getRetentionType());
            pst.setString(4, qbean.getRetentionSubType());
            pst.setString(5, qbean.getEmpId());
            pst.setString(6, qaddress);
            pst.setString(7, unitqt);
            pst.setString(8, typeqt);
            pst.setString(9, qbean.getConsumerNo());

            if (otype.equals("1")) {
                pst.setString(10, qbean.getHidtfOffCode());
                pst.setString(11, qbean.getHidTTOffCode());
                if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                    enddate = qbean.getQuarterAllowedToDate();
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                pst.setTimestamp(14, null);
                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostTF());
                pst.setString(18, qbean.getGenericpostTT());
                if (qbean.getTransferOn() != null && !qbean.getTransferOn().equals("")) {
                    pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getTransferOn()).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }

            if (otype.equals("2")) {
                pst.setString(10, qbean.getHidtfOffCode());
                pst.setString(11, qbean.getHidTTOffCode());
                pst.setTimestamp(12, null);
                pst.setTimestamp(13, null);
                pst.setTimestamp(14, null);
                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostTF());
                pst.setString(18, qbean.getGenericpostTT());
                if (qbean.getWefFromDate() != null && !qbean.getWefFromDate().equals("")) {
                    pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getWefFromDate()).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }

            if (otype.equals("3")) {
                pst.setString(10, qbean.getHidtfOffCode());
                pst.setString(11, qbean.getHidTTOffCode());
                if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (qbean.getVacateQuartersDate() != null && !qbean.getVacateQuartersDate().equals("")) {
                    enddate = qbean.getVacateQuartersDate();
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getVacateQuartersDate()).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }

                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostTF());
                pst.setString(18, qbean.getGenericpostTT());
                if (qbean.getReliefDate() != null && !qbean.getReliefDate().equals("")) {
                    pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getReliefDate()).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }
            if (otype.equals("4")) {
                pst.setString(10, qbean.getHidooOffCode());
                pst.setString(11, "");
                if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (qbean.getVacateQuartersRetirement() != null && !qbean.getVacateQuartersRetirement().equals("")) {
                    enddate = qbean.getVacateQuartersRetirement();
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getVacateQuartersRetirement()).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }

                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostoo());
                pst.setString(18, "");
                pst.setTimestamp(19, null);

            }
            if (otype.equals("5")) {
                pst.setString(10, qbean.getHidooOffCode());
                pst.setString(11, "");
                if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }

                if (qbean.getVacateQuartersDeath() != null && !qbean.getVacateQuartersDeath().equals("")) {
                    enddate = qbean.getVacateQuartersDeath();
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getVacateQuartersDeath()).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }

                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostoo());
                pst.setString(18, "");
                pst.setTimestamp(19, null);

            }
            if (otype.equals("6")) {
                pst.setString(10, qbean.getHidooOffCode());
                pst.setString(11, "");
                if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                    pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                    enddate = qbean.getQuarterAllowedToDate();
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                pst.setTimestamp(14, null);

                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, qbean.getGenericpostoo());
                pst.setString(18, "");
                pst.setTimestamp(19, null);

            }
            pst.setString(20, otype);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            String sql1 = "UPDATE consumer_ga SET retention_status='Y' WHERE consumer_no=? AND hrmsid=?";
            pstmt1 = con.prepareStatement(sql1);
            pstmt1.setString(1, qbean.getConsumerNo());
            pstmt1.setString(2, qbean.getEmpId());
            pstmt1.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pstmt1);

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String createdDateTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sqllog);
            int slno = Integer.parseInt(qbean.getOrderNumber());
            pst.setString(1, qbean.getOrderNumber());

            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, "Retention");
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(5, qbean.getConsumerNo());
            pst.setString(6, qbean.getEmpId());
            pst.setInt(7, slno);
            pst.executeUpdate();
            if (otype.equals("6") || otype.equals("5") || otype.equals("4") || otype.equals("3")) {
                pstmt = con.prepareStatement("SELECT mobile FROM emp_mast WHERE  emp_id=?");
                pstmt.setString(1, qbean.getEmpId());
                rs = pstmt.executeQuery();
                String mobile = "";
                if (rs.next()) {
                    mobile = rs.getString("mobile");
                }
                if (mobile != null && !mobile.equals("")) {
                    String msg = "Sir/Madam, You are permitted to retain quarters till " + enddate + ".RENT OFFICER";
                    String msgId = "1407162157499204710";
                    String mobileno = mobile;
                    // String mobileno = "9132055693";
                    SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
                    insertQMSSMSLog(con, qbean.getEmpId(), msg, mobileno, "", "Retention Permission");
                } else {
                    String msg = "Sir/Madam, You are permitted to retain quarters till " + enddate + ".RENT OFFICER";
                    insertQMSSMSLog(con, qbean.getEmpId(), msg, mobile, "", "Retention Permission");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void GeneratePDFRetention(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";

        try {
            con = this.dataSource.getConnection();
           // ps = con.prepareStatement("select * from hw_loan_sanction_details HD INNER JOIN hw_emp_gpf_loan HL ON HD.taskid = HL.taskid WHERE HL.taskid = ?");
            //ps.setInt(1, taskId);

            //  rs = ps.executeQuery();
            //  rs.next();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getDate("extension_order_no") != null && !rs.getDate("extension_order_no").equals("")) {
                    orderNo = rs.getString("extension_order_no");
                    orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                    if (rs.getDate("extension_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from"));

                    }
                    if (rs.getDate("extension_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_to"));
                        String strDate = sdf.format(rs.getDate("extension_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                    }
                    if (rs.getDate("extension_vacate_date") != null) {
                        vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_vacate_date"));
                    }
                    if (rs.getDate("extension_relief_date") != null) {
                        lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_relief_date"));
                    }

                } else {
                    orderNo = rs.getString("order_no");
                    orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_order_date"));
                    if (rs.getDate("quarter_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                    }
                    if (rs.getDate("quarter_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                        String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                    }
                    if (rs.getDate("vacate_date") != null) {
                        vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                    }
                    if (rs.getDate("licence_fee_date") != null) {
                        lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("licence_fee_date"));
                    }

                }
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                Rtype = rs.getString("retention_type");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");

            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            String sql = (" SELECT  (SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_from, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }

            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo. " + orderNo + "/QMS.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + orderDate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            if (otype.equals("1")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase("      " + empName, dataHdrFont));
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase("      " + topostName, dataHdrFont));
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase("\nSub:     Retention of quarters on transfer to/from Cuttack/Bhubaneswar.  ", dataHdrFont));
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);

                document.add(table1);

                String msg1 = "\n         As per our record ,you have been transferred/relieved  from   "
                        + fromOfficeName + " (office).,   to " + toOfficeName + " (office)., on " + lfeedate;
                Paragraph point1 = new Paragraph(msg1, pFont);
                document.add(point1);
                // datacell = new PdfPCell(new Phrase(msg1, dataValFont));
                // datacell.setBorder(Rectangle.NO_BORDER);
                // table1.addCell(datacell);

                String msg2 = "\n         If you want to retain the Govt. Qrs. No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack "
                        + " you are required to apply before the undersigned along with the commutation permission from your Administrative Department/Head of Office within three months from this intimation,failing which your retention of quarters at the previous station shall be treated as unauthorised.";
                Paragraph point2 = new Paragraph(msg2, pFont);
                document.add(point2);

                Paragraph blank = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank);

            }

            if (otype.equals("2")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                String msg3 = "\n    Consequent  upon relief on transfer  to " + toOfficeName + ", EX- " + frompostName + ".,"
                        + " Bhubaneswar/Cuttack is hereby allowed to retain Government Quarters No  " + qaddress + " , " + unitqt + ", " + typeqt + " (area), on "
                        + " Bhubaneswar on payment of Flat Licence Fee w.e.f " + lfeedate + "  (i.e. the date of joining) till continuance his service in KBK districts as per G.A. Department Order No.24003 dtd.16.08.2001 and revised Resolution No.13925 dtd.12.09.2008";

                Paragraph point3 = new Paragraph(msg3, pFont);
                document.add(point3);

                Paragraph blank1 = new Paragraph("\n\n\n By order of the Principal  Secretary \n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank1.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank1);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }
            if (otype.equals("3")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                //String msg4 = "\n  Consequent upon relief on transfer to O/O " + toOfficeName + ", on " + lfeedate + ". " + empName + " EX - " + frompostName
               /* String msg4 = "\n  Consequent upon relief on transfer to O/O " + toOfficeName + ", on " + lfeedate +". "+ empName +  " EX - " + frompostName
                 + " Bhubaneswar/Cuttack is hereby allowed to retain Government Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar for the period from "
                 + periodFrom + " to " + periodto + " on  payment of Flat Licence Fee  and  from   " + wefdate + " to " + wefdate1 + " on  payment of Standard Licence  Fee "
                 + " . Allotment of the said quarters made in his favour is cancelled with effect from  " + wefdate2 + " and five times of Standard Licence Fee charged against him from the date of cancellation. ";
                 */
                String msg4 = "\n  Consequent upon relief on transfer to O/O " + toOfficeName + ", on " + lfeedate + ". " + empName + " EX - " + frompostName
                        + " Bhubaneswar/Cuttack is hereby allowed to retain Government Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar for the period from "
                        + periodFrom + " to " + periodto + "  on payment of  Licence Fee as admissible under Rules "
                        + " . Allotment of the said quarters made in his favour is cancelled with effect from  " + wefdate + " and five times of Standard Licence Fee charged against him from the date of cancellation. ";

                Paragraph point4 = new Paragraph(msg4, pFont);
                point4.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point4);

                String msg5 = "\n Further  he  is directed   to  vacate  the  quarters Immediately/intime failing which  eviction  proceedings   will  be  initiated  against  him  for  his  eviction  from  the  said public premises  under the provisions  of O.P.P. (EUO) Act. 1972.";
                Paragraph point5 = new Paragraph(msg5, pFont);
                point5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point5);

                String msg_new = "\n  If you want to vacate the quarters before the due date, you can do so and house licence fee as admisable would be charged till the date of vacation of quarters ";
                Paragraph point_new = new Paragraph(msg_new, pFont);
                point_new.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point_new);

                Paragraph blank1 = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank1.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank1);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }

            if (otype.equals("4")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                String msg4 = "\n  Consequent upon  retirement  from  Government service w.e.f " + dos + ", " + empName + ", EX - " + frompostName + ", " + fromOffice
                        + " is hereby allowed to retain Government Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar for the period from "
                        + periodFrom + " to " + periodto + ". "
                        + " The allotment of the said quarters made in his favour is cancelled with effect from  " + wefdate + " , followed with  charging of  5 (five)  times  of Standard Licence Fee from the date of its cancellation. ";

                Paragraph point4 = new Paragraph(msg4, pFont);
                document.add(point4);

                // String msg5 = "\n Further he is  directed to handover  the vacant  possession  of  the aforesaid   Government   quarters before  " + vacateperiod + " ,  failing  which proceedings  under provisions of  OPP (E.U.O.) Act. 1972  will  be  initiated against  him for his eviction from the said quarters.";
                String msg5 = "\n Further he is  directed to handover  the vacant  possession  of  the aforesaid   Government   quarters Immediately/intime ,  failing  which proceedings  under provisions of  OPP (E.U.O.) Act. 1972  will  be  initiated against  him for his eviction from the said quarters.";
                Paragraph point5 = new Paragraph(msg5, pFont);
                document.add(point5);

                Paragraph blank1 = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank1.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank1);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }

            if (otype.equals("5")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                String msg6 = "\n Consequent upon death of " + empName + ", EX - " + frompostName + ",O/O " + fromOffice
                        + " the family members of  Late " + empName + " is hereby allowed to retain Government Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar for the period from "
                        + periodFrom + " to " + periodto + " on payment of  Licence Fee as admissible under Rules"
                        + " . The allotment of the said quarters made in his favour is cancelled with effect from " + wefdate + " , followed with charging of 5 (five) times of Standard Licence Fee from the date of its cancellation. ";

                Paragraph point6 = new Paragraph(msg6, pFont);
                document.add(point6);

                String msg7 = "\n The family  members  are hereby  directed  to vacate  the above  Government Quarters immediately failing which eviction proceedings shall be initiated against them for their eviction from the said public premises under provisions of O.P.P. (E) Act. 1972.";

                Paragraph point7 = new Paragraph(msg7, pFont);
                document.add(point7);

                Paragraph blank2 = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank2.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank2);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }
            if (otype.equals("6")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                String msg8 = "\n Consequent upon dismissal from Govt. service " + empName + ", EX - " + frompostName + ",O/O " + fromOffice
                        + " is hereby charged house licence fee towards retention of Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack as per rules as follows:- "
                        + periodFrom + " to " + periodto + " on payment of Flat Licence Fee ";

                Paragraph point8 = new Paragraph(msg8, pFont);
                document.add(point8);

                /* String msg9 = "\n For a period of 07 (Seven) days i.e from " + periodFrom + " to " + periodto + " on payment of Flat Licence Fee ";

                 Paragraph point9 = new Paragraph(msg9, pFont);
                 document.add(point9);*/
                String msg10 = "\n For the period from " + wefdate + " to " + wefdate1 + " i.e. the date of vacation/eviction	of said Govt. Quarters on payment of 05(five) times of Standard Licence Fee. ";
                Paragraph point10 = new Paragraph(msg10, pFont);
                document.add(point10);

                Paragraph blank10 = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank10.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank10);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }

            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void VacationNotice(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE consumer_ga SET vacation_notice = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String mobile = "";
            pstmt = con.prepareStatement("SELECT mobile FROM emp_mast WHERE  emp_id=?");
            pstmt.setString(1, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
            if (mobile != null && !mobile.equals("")) {
                String msg = "Sir/Madam, Your quarters retention period is over. Kindly vacate the qtrs immediately. RENT OFFICER";
                String msgId = "1407162157505028034";
                String mobileno = mobile;
                //String mobileno = "9132055693";
                SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobileno, "", "Retention Over");
            } else {
                String msg = "Sir/Madam, Your quarters retention period is over. Kindly vacate the qtrs immediately. RENT OFFICER";
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobile, "", "Retention Over");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void GeneratePDFVacationNotice(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                Rtype = rs.getString("retention_type");
                orderNo = rs.getString("order_no");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                if (rs.getDate("licence_fee_date") != null) {
                    lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("licence_fee_date"));
                }

                if (rs.getDate("quarter_allowed_from") != null) {
                    periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                }
                if (rs.getDate("quarter_allowed_to") != null) {
                    periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                    String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                    c.setTime(sdf.parse(strDate));
                    //Incrementing the date by 1 day
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate = sdf.format(c.getTime());
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                    wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                    c.add(Calendar.DAY_OF_MONTH, 60);
                    String nextdate1 = sdf.format(c.getTime());

                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                    wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate2 = sdf.format(c.getTime());

                    Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                    wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                }
                if (rs.getDate("vacate_date") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                }

            }
            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate;

            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
                wefDate = wefdate;

            }

            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate2;

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo.______________________________" + "/QMS.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate._____________________________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // if (otype.equals("1")) {
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + empName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("      " + postName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + deptName + " Department,", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      In Govt.Qrs.No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area),", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      Bhubaneswar / Cuttack", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub:    Vacation of Govt. Quarters. ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nRef-:    This Department office order no. " + orderNo + ", dated " + orderDate, dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg1 = "\n         I am intimate you that the Goverment Qrs. No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack allotted in your favour has been cancelled w.e.f " + wefDate
                    + " vide the office order under reference and you have been occupying it unauthorizedly till date.";
            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);
            // datacell = new PdfPCell(new Phrase(msg1, dataValFont));
            // datacell.setBorder(Rectangle.NO_BORDER);
            // table1.addCell(datacell);

            String msg2 = "\n         It is therefore requested that, the said quarters may please be vacated immediately. ";

            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            Paragraph blank = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void SaveVacationStatus(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();
            String vacationStatus = qbean.getVacateStatus();
            String vtype = "";

            pst = con.prepareStatement("UPDATE consumer_ga SET vacation_status = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, vacationStatus);
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            if (vacationStatus.equals("Yes")) {
                vtype = "Intimation to DDO";
                pstmt = con.prepareStatement("UPDATE quarter_tracking SET vacation_status = ?,intimation_orderno=?,intimation_order_date=?,ddo_office_code=?,ddo_post_name=?,vacate_date=?,quarter_amount=?"
                        + " WHERE CONSUMER_NO=? AND hrms_id=?");
                pstmt.setString(1, vacationStatus);
                pstmt.setString(2, qbean.getOrderNumber());
                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pstmt.setTimestamp(3, null);
                }
                pstmt.setString(4, qbean.getHidooOffCode());
                pstmt.setString(5, qbean.getGenericpostoo());
                if (qbean.getVacationDate() != null && !qbean.getVacationDate().equals("")) {
                    pstmt.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getVacationDate()).getTime()));
                } else {
                    pstmt.setTimestamp(6, null);
                }
                pstmt.setInt(7, qbean.getQrtFee());
                pstmt.setString(8, qbean.getConsumerNo());
                pstmt.setString(9, qbean.getEmpId());
                pstmt.executeUpdate();

                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                String createdDateTime = dateFormat.format(cal.getTime());
                Long curtime = new Date().getTime();

                String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sqllog);
                int slno = Integer.parseInt(qbean.getOrderNumber());
                pst.setString(1, qbean.getOrderNumber());

                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, vtype);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                pst.setString(5, qbean.getConsumerNo());
                pst.setString(6, qbean.getEmpId());
                pst.setInt(7, slno);
                pst.executeUpdate();

            }

            if (vacationStatus.equals("No")) {
                vtype = "OPP Case";
                pstmt = con.prepareStatement("UPDATE quarter_tracking SET vacation_status = ?,opp_orderno=?,opp_order_date=?,opp_cancelled_wef=?,opp_display=?"
                        + " WHERE CONSUMER_NO=? AND hrms_id=?");
                pstmt.setString(1, vacationStatus);
                pstmt.setString(2, qbean.getoPPorderNumber());
                if (qbean.getoPPorderDate() != null && !qbean.getoPPorderDate().equals("")) {
                    pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getoPPorderDate()).getTime()));
                } else {
                    pstmt.setTimestamp(3, null);
                }

                if (qbean.getWefFromDate() != null && !qbean.getWefFromDate().equals("")) {
                    pstmt.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getWefFromDate()).getTime()));
                } else {
                    pstmt.setTimestamp(4, null);
                }
                pstmt.setString(5, "N");
                pstmt.setString(6, qbean.getConsumerNo());
                pstmt.setString(7, qbean.getEmpId());
                pstmt.executeUpdate();
            }

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String createdDateTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sqllog);
            int slno = Integer.parseInt(qbean.getOrderNumber());
            pst.setString(1, qbean.getOrderNumber());

            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, vtype);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(5, qbean.getConsumerNo());
            pst.setString(6, qbean.getEmpId());
            pst.setInt(7, slno);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void GeneratePDFIntimation(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String ddoOffice = "";
        String frompost = "";
        String topost = "";
        String ddopost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String ddoOfficeName = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";
        int qrtFee = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");

                orderNo = rs.getString("intimation_orderno");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("intimation_order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                ddoOffice = rs.getString("ddo_office_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                ddopost = rs.getString("ddo_post_name");
                qrtFee = rs.getInt("quarter_amount");
                if (rs.getDate("vacate_date") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                }

            }
            String sql = (" SELECT  (SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_from, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_to, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_ddo, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, ddoOffice);
            pstmt.setString(4, frompost);
            pstmt.setString(5, topost);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                ddoOfficeName = rs.getString("off_ddo");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";

            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;

            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;

            }

            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo." + orderNo + "/QMS.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + orderDate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // if (otype.equals("1")) {
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + "DDO/Head of Office" + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + ddoOfficeName + " ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub:    Intimation regarding vacation of Goverment quarters. ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg1 = "\n         I am directed to intimate that" + empName + "," + postName + " ,O/o " + deptName + " has vacated the Goverment Qrs. No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack on  " + vacateperiod
                    + " and Rs." + qrtFee + ".00" + " amount towards house licence fee is outstanding against him/her till the date of vacation";
            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);

            Paragraph blank = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void GeneratePDFOPP(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String ddoOffice = "";
        String frompost = "";
        String topost = "";
        String ddopost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String ddoOfficeName = "";

        String opporderno = "";
        String opporderdate = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";
        int qrtFee = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");

                orderNo = rs.getString("order_no");
                opporderno = rs.getString("opp_orderno");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                opporderdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");

                if (rs.getDate("opp_cancelled_wef") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_cancelled_wef"));
                }

            }
            String sql = (" SELECT  (SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_from, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String ostatus = "";
            if (otype.equals("1") || otype.equals("2")) {

                deptName = toOfficeName;
                postName = topostName;
                ostatus = "Transfer";

            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;

            }

            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                ostatus = "Transfer";

            }
            if (otype.equals("4")) {
                ostatus = "Retirement";
            }
            if (otype.equals("5")) {
                ostatus = "Death";
            }
            if (otype.equals("6")) {
                ostatus = "Dismissal";
            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo." + opporderno + "/QMS.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + opporderdate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // if (otype.equals("1")) {
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));

            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + "In the Court of Estate Officer," + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + "Bhubaneswar.", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub:    Initiation of proceedings against the unauthorised occupants of public premises in New Capital, Bhubaneswar. ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg1 = "\n         I am to submit that allotment of quarters   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack made in favour of   " + empName + "," + postName + " ,O / o " + deptName + " , has been cancelled w.e.f " + vacateperiod + " vide this Department order No. " + orderNo + " dated " + orderDate + " consequent upon his/her  " + ostatus + " under provision of Special Accommodation Rules 1959 appended to Odisha Service Code. "
                    + " He/She has not yet vacated the quarters and is continuing is unauthorised occupation in the said public premises.";
            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);
            // datacell = new PdfPCell(new Phrase(msg1, dataValFont));on payment of Flat Licence Fee
            // datacell.setBorder(Rectangle.NO_BORDER);
            // table1.addCell(datacell);

            String msg2 = "\n         It is therefore, prayed that aproceedings under O.P.P (E) Act may kindly be initiated against him/her for his/her eviction and recovery of reasonable damages as per rules. ";

            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            Paragraph blank = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void EvictionNotice(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE consumer_ga SET eviction_notice = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void GeneratePDFEvictionNotice(Document document, String empId, String consumerNo, int CaseId) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        PreparedStatement pst = null;
        String noticeDate = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            String evictionNotice = "";
            ps1 = con.prepareStatement("SELECT eviction_notice"
                    + " FROM consumer_ga where estate_five_two=?");
            ps1.setInt(1, CaseId);
            rs = ps1.executeQuery();

            if (rs.next()) {
                evictionNotice = rs.getString("eviction_notice");
            }
            //if (evictionNotice.equals("") || evictionNotice == null ) {
            int orderAutoNo = maxRentOrderNo();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            String formattedDate = myDateObj.format(myFormatObj);
            formattedDate = formattedDate.toUpperCase();
            String orderNoString = String.valueOf(orderAutoNo);
            orderNoString = StringUtils.leftPad(orderNoString, 8, "0");
            String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
            ps1 = con.prepareStatement(sqllog);
            ps1.setString(1, orderNoString);
            ps1.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(formattedDate).getTime()));

            ps1.setString(3, "Notice 5(2)");
            ps1.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps1.setString(5, consumerNo);
            ps1.setString(6, empId);
            ps1.setInt(7, orderAutoNo);
            ps1.executeUpdate();

            pst = con.prepareStatement("UPDATE consumer_ga SET eviction_notice = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, consumerNo);
            pst.setString(3, empId);
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE quarter_tracking SET five_two_notice_no = ?,five_two_notice_date=?"
                    + " WHERE CONSUMER_NO=? AND hrms_id=?");
            pst.setString(1, orderNoString);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(formattedDate).getTime()));
            pst.setString(3, consumerNo);
            pst.setString(4, empId);
            pst.executeUpdate();

            //}
            pstmt = con.prepareStatement("SELECT *,b.order_five_two_date,b.case_no FROM quarter_tracking INNER JOIN equarter.case_master b  ON b.emp_id = quarter_tracking.hrms_id AND b.consumer_no = quarter_tracking.consumer_no"
                    + " WHERE quarter_tracking.consumer_no=? AND quarter_tracking.hrms_id=? AND b.order_five_two=? AND b.case_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            pstmt.setString(3, "Y");
            pstmt.setInt(4, CaseId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                noticeDate = rs.getString("five_two_notice_no");
                orderNo = rs.getString("case_no");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_two_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");

                if (rs.getDate("five_two_notice_date") != null) {
                    lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("five_two_notice_date"));
                }

                if (rs.getDate("quarter_allowed_from") != null) {
                    periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                }
                if (rs.getDate("quarter_allowed_to") != null) {
                    periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                    String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                    c.setTime(sdf.parse(strDate));
                    //Incrementing the date by 1 day
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate = sdf.format(c.getTime());
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                    wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                    c.add(Calendar.DAY_OF_MONTH, 60);
                    String nextdate1 = sdf.format(c.getTime());

                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                    wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String nextdate2 = sdf.format(c.getTime());

                    Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                    wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                }
                if (rs.getDate("vacate_date") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                }

            }
            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate;

            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
                wefDate = wefdate;

            }

            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                wefDate = wefdate2;

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nQMS-" + noticeDate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + lfeedate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // if (otype.equals("1")) {
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + empName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("      " + postName + ", ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + deptName + " Department,", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      In Govt.Qrs.No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area),", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      Bhubaneswar / Cuttack", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub:    Vacation of Govt. Quarters. ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nRef-:    This Department office order no. " + orderNo + ", dated " + orderDate, dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg1 = "\n         I am intimate you that the Goverment Qrs. No.   " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar/Cuttack allotted in your favour has been cancelled w.e.f " + wefDate
                    + " vide this Department Office Order under reference and you have been occupying it unauthorisedly";
            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);
            // datacell = new PdfPCell(new Phrase(msg1, dataValFont));
            // datacell.setBorder(Rectangle.NO_BORDER);
            // table1.addCell(datacell);

            String msg2 = "\n         In the meanwhile, the undersigned has been authorised by the Estate Officer, Bhubaneswar U/S 5(2) of OPP Act to evict you from the premises of aforesaid Goverment quarters. ";

            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            String msg3 = "\n        It is, therefore, requested that the said quarters may please be vacated immediately, failing which eviction would be carried out at any time without any further notice. ";

            Paragraph point3 = new Paragraph(msg3, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point3);

            Paragraph blank = new Paragraph("\n\n\n RENT OFFICER\n G.A. & P.G.(Rent) Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getQuarterDetails(String empId) {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            // pstmt = con.prepareStatement("SELECT a.*,b.*,c.*,d.status as dsstatus,d.disk_file_name as ds_disk_file_name,d.org_file_name as ds_org_file_name  from consumer_ga a LEFT OUTER JOIN quarter_tracking b ON  a.hrmsid=b.hrms_id AND a.consumer_no=b.consumer_no AND retention_type IS NOT NULL LEFT OUTER JOIN quarter_noc c  ON a.hrmsid=c.emp_id AND a.consumer_no=c.consumer_no LEFT OUTER JOIN quarter_logs d ON  a.hrmsid=d.emp_id AND a.consumer_no=d.consumer_no  AND log_type='Document Submission' where a.hrmsid=?");
            // System.out.println("empid=" + empId);
            pstmt = con.prepareStatement("SELECT a.*,b.*,c.*  from consumer_ga a LEFT OUTER JOIN quarter_tracking b ON  a.hrmsid=b.hrms_id AND a.consumer_no=b.consumer_no AND retention_type IS NOT NULL LEFT OUTER JOIN quarter_noc c  ON a.hrmsid=c.emp_id AND a.consumer_no=c.consumer_no  where a.hrmsid=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            if (rs.next()) {
                // System.out.println(rs.getString("CONSUMER_NO")+"sdfsd");    
                eqBean = new EmpQuarterBean();                //. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setNocfor(rs.getString("noc_for"));
                eqBean.setRetentionType(rs.getString("retention_type"));
                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setTrackingId(rs.getInt("tracking_id"));
                eqBean.setQrtFee(rs.getInt("quarter_amount"));
                String address = rs.getString("address1") + " " + rs.getString("address2") + " " + rs.getString("address3");
                eqBean.setAddress(address);
                if (rs.getString("extension_allowed_from") != null && !rs.getString("extension_allowed_from").equals("")) {
                    eqBean.setExtensionFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from")));
                } else {
                    eqBean.setExtensionFromDate("");
                }
                eqBean.setOrderNumber(rs.getString("extension_order_no"));
                eqBean.setTransferId(rs.getString("transfer_id"));
                eqBean.setOriginalFilename(rs.getString("org_file_name"));
                eqBean.setPhdNocStatus("phd_noc_status");
                eqBean.setPhdoriginalFilename(rs.getString("phd_org_file_name"));
                eqBean.setPhddiskFileName(rs.getString("phd_disk_file_name"));
                eqBean.setPhdNocReason(rs.getString("phd_noc_reason"));
                eqBean.setPhdNocStatus(rs.getString("phd_noc_status"));
                eqBean.setGedNocStatus(rs.getString("ged_noc_status"));
                eqBean.setGedoriginalFilename(rs.getString("ged_org_file_name"));
                eqBean.setGeddiskFileName(rs.getString("ged_disk_file_name"));
                eqBean.setGedNocReason(rs.getString("ged_noc_reason"));
                eqBean.setPhdNocStatus(rs.getString("phd_noc_status"));
                eqBean.setNocId(rs.getInt("noc_id_qtr"));
                eqBean.setNocRequest(rs.getString("noc_request_from"));
                eqBean.setDocumentSubmission(rs.getString("document_submission"));
                if (rs.getString("ocu_type") != null && !rs.getString("ocu_type").equals("")) {
                    eqBean.setOccupationTypes(rs.getString("ocu_type"));
                } else {
                    eqBean.setOccupationTypes("3");
                }
                eqBean.setSplCaseStatus(rs.getInt("spl_case_status") + "");

                //eqBean.setDsStatus(rs.getString("dsstatus"));
                // eqBean.setDs_disk_file_name(rs.getString("ds_disk_file_name"));
                //  eqBean.setDs_org_file_name(rs.getString("ds_org_file_name"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setOppCaseId(rs.getInt("opp_case_id"));
                eqBean.setShowCauseReply(rs.getString("show_cause_reply"));
                eqBean.setElectricityNocStatus(rs.getString("electricity_noc_status"));
                eqBean.setElectricitydiskFileName(rs.getString("electricity_disk_file_name"));
                eqBean.setElectricityoriginalFilename(rs.getString("electricity_org_file_name"));
                eqBean.setOrderFiveOne(rs.getInt("estate_five_one"));
                eqBean.setNoticeFiveOne(rs.getInt("estate_notice"));
                eqBean.setEstateAppeal(rs.getString("estate_appeal"));
                eqBean.setAppealNotice(rs.getString("appeal_notice"));
                alist.add(eqBean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public void saveSplCaseRetention(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String uploaddiskfileName = null;
        PreparedStatement pst = null;
        String uploadoriginalFileName = null;
        String uploadcontentType = null;

        try {
            if (qbean.getUploadDocument() != null && !qbean.getUploadDocument().isEmpty()) {
                uploaddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                uploadoriginalFileName = qbean.getUploadDocument().getOriginalFilename();
                uploadcontentType = qbean.getUploadDocument().getContentType();
                byte[] bytes = qbean.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + uploaddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            ps = con.prepareStatement("update quarter_tracking set disk_file_name = ?,org_file_name = ?, file_type = ?,file_path=?,upload_date=?,extension_allowed_from=?,extension_allowed_to=?,extension_reason=?,spl_case_status=? where hrms_id = ? AND tracking_Id=?");
            ps.setString(1, uploaddiskfileName);
            ps.setString(2, uploadoriginalFileName);
            ps.setString(3, uploadcontentType);
            ps.setString(4, this.uploadPath);
            ps.setTimestamp(5, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                ps.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
            } else {
                ps.setTimestamp(6, null);
            }
            if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                ps.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
            } else {
                ps.setTimestamp(7, null);
            }
            ps.setString(8, qbean.getExtensionReason());
            ps.setInt(9, 0);
            ps.setString(10, qbean.getEmpId());
            ps.setInt(11, qbean.getTrackingId());
            ps.executeUpdate();

            pstmt = con.prepareStatement("INSERT INTO quarter_logs (consumer_no,emp_id, doe, log_type,otype,status,extension_allowed_from,extension_allowed_to,notes) values (?,?,?,?,?,?,?,?,?) ");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setString(4, "Request for Extension ");
            pstmt.setString(5, qbean.getOccupationTypes());
            pstmt.setString(6, "N");
            if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                pstmt.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
            } else {
                pstmt.setTimestamp(7, null);
            }
            if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                pstmt.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
            } else {
                pstmt.setTimestamp(8, null);
            }
            pstmt.setString(9, qbean.getExtensionReason());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public EmpQuarterBean getAttachedFileforSPlCase(int trackid, String empid) {
        EmpQuarterBean eBean = new EmpQuarterBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from quarter_tracking where tracking_id = ? AND hrms_id=?");
            pst.setInt(1, trackid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                eBean.setOriginalFilename(rs.getString("org_file_name"));
                eBean.setDiskFileName(rs.getString("disk_file_name"));
                eBean.setContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + eBean.getDiskFileName());
            eBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return eBean;
    }

    @Override
    public List getSplCaseDetailList() {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT a.*,b.*, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from consumer_ga a,quarter_tracking b,emp_mast c WHERE c.emp_id=a.hrmsid AND a.hrmsid=b.hrms_id AND org_file_name IS NOT NULL ORDER BY upload_date DESC");
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                // System.out.println(rs.getString("CONSUMER_NO")+"sdfsd");    
                eqBean = new EmpQuarterBean();                //. eqBean.setQaId(rs.getString("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setRetentionType(rs.getString("retention_type"));
                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setTrackingId(rs.getInt("tracking_id"));

                String address = rs.getString("address1") + " " + rs.getString("address2") + " " + rs.getString("address3");
                eqBean.setAddress(address);
                eqBean.setTransferId(rs.getString("transfer_id"));
                eqBean.setOriginalFilename(rs.getString("org_file_name"));
                // System.out.println("======="+rs.getInt("spl_case_status"));
                eqBean.setSplCaseStatus(rs.getInt("spl_case_status") + "");
                //  System.out.println("===spl===="+eqBean.getSplCaseStatus());
                String dor = CommonFunctions.getFormattedOutputDate1(rs.getDate("upload_date"));
                eqBean.setWefFromDate(dor);
                if (rs.getString("extension_allowed_from") != null && !rs.getString("extension_allowed_from").equals("")) {
                    eqBean.setExtensionFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from")));
                } else {
                    eqBean.setExtensionFromDate("");
                }
                eqBean.setOrderNumber(rs.getString("extension_order_no"));
                alist.add(eqBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public void UpdateSplCaseStatus(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        try {
            con = this.dataSource.getConnection();
            // System.out.println("1111qbean.getSplCaseStatus()==="+qbean.getSplCaseStatus());
            if (qbean.getSplCaseStatus() != null && qbean.getSplCaseStatus().equals("1")) {
                // System.out.println("qbean.getSplCaseStatus()==="+qbean.getSplCaseStatus());
                pst = con.prepareStatement("UPDATE quarter_tracking SET spl_case_status = ?,extension_order_no=?"
                        + " WHERE tracking_id=? AND hrms_id=?");
                pst.setInt(1, Integer.parseInt(qbean.getSplCaseStatus()));
                pst.setString(2, "");
                pst.setInt(3, qbean.getTrackingId());
                pst.setString(4, qbean.getEmpId());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE consumer_ga SET vacation_status = ?"
                        + " WHERE hrmsid=?");
                pst.setString(1, "");
                pst.setString(2, qbean.getEmpId());
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE quarter_tracking SET spl_case_status = ?"
                        + " WHERE tracking_id=? AND hrms_id=?");
                pst.setInt(1, Integer.parseInt(qbean.getSplCaseStatus()));
                pst.setInt(2, qbean.getTrackingId());
                pst.setString(3, qbean.getEmpId());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void SaveExtensionRetentionData(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            con = this.dataSource.getConnection();

            String sql = "UPDATE quarter_tracking SET extension_order_no=?,extension_order_date=?,extension_allowed_from=?,extension_allowed_to=?,oswas_fileno=?,licence_fee_type=? WHERE tracking_id=? AND hrms_id=?";
            pst = con.prepareStatement(sql);

            pst.setString(1, qbean.getOrderNumber());
            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }

            if (qbean.getQuarterAllowedFromDate() != null && !qbean.getQuarterAllowedFromDate().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedFromDate()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (qbean.getQuarterAllowedToDate() != null && !qbean.getQuarterAllowedToDate().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getQuarterAllowedToDate()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.setString(5, qbean.getOswasFileNo());
            pst.setString(6, qbean.getLicenceFeeType());
            pst.setInt(7, qbean.getTrackingId());
            pst.setString(8, qbean.getEmpId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getOppRequisitionList() {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            String sql = (" SELECT A.* FROM (SELECT DISTINCT  retention_type,retention_subtype,eviction_notice,vacation_notice,consumer_ga.vacation_status,retention_status,phslno,consumer_ga.CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,dos,transfer_id   ,"
                    + "(SELECT off_en FROM emp_join INNER JOIN g_office  ON auth_off=g_office.off_code  WHERE emp_id=EMP_MAST .EMP_ID  ORDER BY join_date DESC LIMIT 1)    ,"
                    + " (SELECT dist_name FROM emp_join INNER JOIN g_office  ON auth_off=g_office.off_code INNER JOIN g_district ON g_office.dist_code=g_district.dist_code  WHERE emp_id=EMP_MAST .EMP_ID  ORDER BY join_date DESC LIMIT 1)     "
                    + "  FROM consumer_ga INNER  JOIN quarter_tracking ON consumer_ga.hrmsid=quarter_tracking.hrms_id AND consumer_ga.CONSUMER_NO=quarter_tracking.CONSUMER_NO     INNER  JOIN emp_join ON consumer_ga.hrmsid=emp_join.emp_id     INNER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST .EMP_ID   "
                    + " INNER JOIN g_office ON emp_join.auth_off=g_office.off_code INNER JOIN g_district ON g_office.dist_code=g_district.dist_code     "
                    + "  WHERE   consumer_ga.vacation_status='No' ) as A ORDER BY EMPNAME 	 ");

            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setTransferId(rs.getString("transfer_id"));
                eqBean.setDistName(rs.getString("dist_name"));
                eqBean.setOffName(rs.getString("off_en"));
                eqBean.setJoinDate("");
                eqBean.setRelieveDate("");
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }

                eqBean.setRetentionStatus(rs.getString("retention_status"));
                eqBean.setVacateNotice(rs.getString("vacation_notice"));
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setEvictionNotice(rs.getString("eviction_notice"));
                eqBean.setRetentionType(rs.getString("retention_type"));
                eqBean.setRetentionSubType(rs.getString("retention_subtype"));
                quarterList.add(eqBean);

                // quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public void saveOccupationVacationQrtDetails(EmpQuarterBean qbean, String loginName) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String cno = "";
        String unitqt = "";
        String typeqt = "";
        String qrtno = "";
        String hrmsid = "";
        String name = "";
        String mobile = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();
        String vstatus = qbean.getVacateStatus();
        try {

            con = this.dataSource.getConnection();
            if (vstatus != null && vstatus.equals("Vacation")) {
                pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM consumer_ga "
                        + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE phslno=?");
                pstmt.setInt(1, qbean.getQaId());
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    cno = rs.getString("CONSUMER_NO");
                    hrmsid = rs.getString("hrmsid");
                    unitqt = rs.getString("unit_qt");
                    typeqt = rs.getString("type_qt");
                    qrtno = rs.getString("bldgno_qt");

                }

                String sql = "INSERT INTO quarter_occupation_vacation (consumer_no,entery_id,status, doe, dos, qrs_no, type_qt,unit_area, emp_id) VALUES(?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, cno);
                pst.setString(2, loginName);
                pst.setString(3, qbean.getVacateStatus());
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                //  System.out.println("gdfgdf"+qbean.getOrderDate());
                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, qrtno);
                pst.setString(7, typeqt);
                pst.setString(8, unitqt);
                pst.setString(9, qbean.getEmpId());
                pst.executeUpdate();

                String sql1 = "UPDATE consumer_ga SET name=null,hrmsid=null,transfer_id=null,retention_status=null,vacation_notice=null,vacation_status=null,noc_status=null,opp_status=null,eviction_notice=null,document_submission=null,show_cause_reply=null,estate_five_one=null,estate_notice=null,estate_appeal=null,appeal_notice=null,hide_show=null,estate_five_two=null,drop_case_id=null WHERE phslno=?";
                pstmt = con.prepareStatement(sql1);
                pstmt.setInt(1, qbean.getQaId());
                pstmt.executeUpdate();

            }
            if (vstatus != null && vstatus.equals("Occupation")) {
                pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,emp_id FROM emp_mast WHERE emp_id=?");
                pstmt.setString(1, qbean.getEmpId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    mobile = rs.getString("mobile");
                    hrmsid = rs.getString("emp_id");
                    name = rs.getString("EMPNAME");

                }
                String sql1 = "UPDATE consumer_ga SET name=?,hrmsid=?,transfer_id=null,retention_status=null,vacation_notice=null,vacation_status=null,noc_status=null,opp_status=null,eviction_notice=null,document_submission=null,show_cause_reply=null,estate_five_one=null,estate_notice=null,estate_appeal=null,appeal_notice=null,hide_show=null,estate_five_two=null,drop_case_id=null WHERE phslno=?";
                pstmt = con.prepareStatement(sql1);
                pstmt.setString(1, name);
                pstmt.setString(2, hrmsid);
                pstmt.setInt(3, qbean.getQaId());
                pstmt.executeUpdate();

                pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM consumer_ga "
                        + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE phslno=?");
                pstmt.setInt(1, qbean.getQaId());
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    cno = rs.getString("CONSUMER_NO");
                    hrmsid = rs.getString("hrmsid");
                    unitqt = rs.getString("unit_qt");
                    typeqt = rs.getString("type_qt");
                    qrtno = rs.getString("bldgno_qt");

                }

                String sql = "INSERT INTO quarter_occupation_vacation (consumer_no,entery_id,status, doe, dos, qrs_no, type_qt,unit_area, emp_id) VALUES(?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, cno);
                pst.setString(2, loginName);
                pst.setString(3, qbean.getVacateStatus());
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                //  System.out.println("gdfgdf"+qbean.getOrderDate());
                if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, qrtno);
                pst.setString(7, typeqt);
                pst.setString(8, unitqt);
                pst.setString(9, qbean.getEmpId());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getoccupationVacationList(String vstatus, String loginName, String fdate, String tdate) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            con = dataSource.getConnection();
            if (loginName == null || loginName.equals("")) {
                pstmt = con.prepareStatement("SELECT consumer_no,entery_id,status, doe, quarter_occupation_vacation.dos, qrs_no, type_qt,unit_area, quarter_occupation_vacation.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM quarter_occupation_vacation  "
                        + "LEFT OUTER JOIN EMP_MAST ON quarter_occupation_vacation.emp_id = EMP_MAST.EMP_ID  "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status=? AND "
                        + " (quarter_occupation_vacation.doe BETWEEN ? AND ?) ORDER BY  log_id DESC");

                pstmt.setString(1, vstatus);
                //pstmt.setString(2, fdate);
                //pstmt.setString(3, tdate);
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
                rs = pstmt.executeQuery();
            }

            if (loginName != null && !loginName.equals("")) {
                String unit_qt = "";
                pstmt1 = con.prepareStatement("SELECT distinct unit_qt from consumer_ga   where unit_ph=? LIMIT 1 ");
                pstmt1.setString(1, loginName);
                rs1 = pstmt1.executeQuery();
                if (rs1.next()) {
                    unit_qt = rs1.getString("unit_qt");
                }
                // System.out.println("loginName1=="+loginName);

                pstmt = con.prepareStatement("SELECT consumer_no,entery_id,status, doe, quarter_occupation_vacation.dos, qrs_no, type_qt,unit_area, quarter_occupation_vacation.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM quarter_occupation_vacation  "
                        + "LEFT OUTER JOIN EMP_MAST ON quarter_occupation_vacation.emp_id = EMP_MAST.EMP_ID  "
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status=?  AND (entery_id=? OR unit_area=? ) AND  (quarter_occupation_vacation.doe BETWEEN ? AND ?) ORDER BY  log_id DESC ");
                pstmt.setString(1, vstatus);
                pstmt.setString(2, loginName);
                pstmt.setString(3, unit_qt);
                // pstmt.setString(4, fdate);
                // pstmt.setString(5, tdate);
                pstmt.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
                rs = pstmt.executeQuery();
            }

            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("emp_id"));
                eqBean.setQuarterNo(rs.getString("qrs_no"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_area"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setDesignation(rs.getString("SPN"));
                eqBean.setVacateStatus(rs.getString("status"));
                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    eqBean.setDos("");
                }
                if (rs.getString("doe") != null && !rs.getString("doe").equals("")) {
                    eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                } else {
                    eqBean.setDos("");
                }
                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public int maxRentOrderNo() {
        String maxQueryService = "SELECT MAX(cast ( slno as BIGINT ))+1 MaxId FROM quarter_office_orders";
        Statement stamt = null;
        ResultSet resultset = null;
        String temp = "";
        Connection con = null;
        int maxId = 1;
        try {
            con = dataSource.getConnection();
            stamt = con.createStatement();
            resultset = stamt.executeQuery(maxQueryService);

            if (resultset.next()) {
                temp = resultset.getString("MaxId");
                if (temp != null && !temp.equals("")) {
                    maxId = Integer.parseInt(temp);
                } else {
                    maxId = 1000;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, stamt, con);
        }

        return maxId;
    }

    @Override
    public int trackingID(String cno, String empid) {
        String maxQueryService = "SELECT (cast ( tracking_id as BIGINT )) MaxId FROM quarter_tracking WHERE consumer_no=? AND hrms_id=?";
        Statement stamt = null;
        ResultSet resultset = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        String temp = "";
        Connection con = null;
        int maxId = 1;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT (cast ( tracking_id as BIGINT )) MaxId FROM quarter_tracking WHERE consumer_no=? AND hrms_id=?");
            pstmt.setString(1, cno);
            pstmt.setString(2, empid);
            resultset = pstmt.executeQuery();

            if (resultset.next()) {
                temp = resultset.getString("MaxId");
                if (temp != null && !temp.equals("")) {
                    maxId = Integer.parseInt(temp);
                } else {
                    maxId = 1;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, pstmt, con);
        }

        return maxId;
    }

    @Override
    public void SaveAutoRetentionData(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String fromoffice = "";
        String tooffice = "";
        String fromPost = "";
        String toPost = "";
        String relivedate = "";
        String endDate = "";
        String mobile = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT address1,address2,address3,bldgno_qt FROM consumer_ga WHERE CONSUMER_NO=? AND hrmsid=?");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qaddress = rs.getString("address1");
                unitqt = rs.getString("address2");
                typeqt = rs.getString("address3");
                bldgno = rs.getString("bldgno_qt");

            }

            String otype = qbean.getOccupationTypes();
            String sql = "INSERT INTO quarter_tracking (order_no,order_date,retention_type, retention_subtype, hrms_id, qrt_no, qrt_type,unit_area,consumer_no, office_from_code, office_to_code,quarter_allowed_from,quarter_allowed_to,vacate_date,retiremnt_wef,retirement_cancelled_wef,post_from_office,post_to_office,licence_fee_date,otype) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, qbean.getOrderNumber());
            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, qbean.getRetentionType());
            pst.setString(4, qbean.getRetentionSubType());
            pst.setString(5, qbean.getEmpId());
            pst.setString(6, qaddress);
            pst.setString(7, unitqt);
            pst.setString(8, typeqt);
            pst.setString(9, qbean.getConsumerNo());
            String strDate = null;
            if (otype.equals("3") || otype.equals("2") || otype.equals("1")) {

                String sql1 = (" SELECT join_date,rlv_date,g_spc1.gpc fromgpc,g_spc1.off_code fromOffcode,g_spc2.gpc togpc,emp_transfer.off_code toOffcode  "
                        + " from emp_relieve inner join emp_transfer on emp_relieve.not_id=emp_transfer.not_id "
                        + " inner join emp_join  on emp_join.not_id=emp_relieve.not_id "
                        + " inner join g_spc g_spc1 on emp_relieve.spc=g_spc1.spc     "
                        + "  inner join g_spc g_spc2 on emp_transfer.next_spc=g_spc2.spc  "
                        + " where emp_relieve.emp_id=?  AND emp_relieve.not_type='TRANSFER' AND emp_join.not_type='TRANSFER' "
                        + " AND join_date IS NOT NULL AND (rlv_date is NOT NULL) ORDER BY emp_relieve.rlv_date DESC LIMIT 1 ");

                pstmt1 = con.prepareStatement(sql1);
                pstmt1.setString(1, qbean.getEmpId());
                rs2 = pstmt1.executeQuery();
                while (rs2.next()) {
                    fromoffice = rs2.getString("fromoffcode");
                    tooffice = rs2.getString("tooffcode");
                    fromPost = rs2.getString("fromgpc");
                    toPost = rs2.getString("togpc");
                    if (rs2.getString("rlv_date") != null && !rs2.getString("rlv_date").equals("")) {
                        //relivedate = CommonFunctions.getFormattedOutputDate1(rs2.getDate("rlv_date"));
                        relivedate = sdf.format(rs2.getDate("rlv_date"));
                        strDate = relivedate;
                        //  System.out.println("Hi");
                    } else {
                        relivedate = sdf.format(rs2.getDate("join_date"));
                        strDate = relivedate;
                        //  System.out.println("Hi1111");
                    }

                }
                //  System.out.println("==="+strDate);
                c.setTime(sdf.parse(strDate));
                //Incrementing the date by 1 day
                c.add(Calendar.DAY_OF_MONTH, 30);
                String nextdate = sdf.format(c.getTime());
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                endDate = CommonFunctions.getFormattedOutputDate1(date1);

                pst.setString(10, fromoffice);
                pst.setString(11, tooffice);

            }

            if (otype.equals("3")) {
                if (strDate != null && !strDate.equals("")) {
                    pst.setTimestamp(12, new Timestamp(sdf.parse(strDate).getTime()));
                } else {
                    pst.setTimestamp(12, null);
                }
                if (endDate != null && !endDate.equals("")) {
                    pst.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(endDate).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (endDate != null && !endDate.equals("")) {
                    pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(endDate).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }

                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, fromPost);
                pst.setString(18, toPost);
                if (strDate != null && !strDate.equals("")) {
                    pst.setTimestamp(19, new Timestamp(sdf.parse(strDate).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }
            if (otype.equals("2")) {
                pst.setTimestamp(12, null);
                pst.setTimestamp(13, null);
                pst.setTimestamp(14, null);
                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, fromPost);
                pst.setString(18, toPost);
                if (endDate != null && !endDate.equals("")) {
                    pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(endDate).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }
            if (otype.equals("1")) {
                pst.setTimestamp(12, null);
                pst.setTimestamp(13, null);
                pst.setTimestamp(14, null);
                pst.setTimestamp(15, null);
                pst.setTimestamp(16, null);
                pst.setString(17, fromPost);
                pst.setString(18, toPost);
                if (strDate != null && !strDate.equals("")) {
                    pst.setTimestamp(19, new Timestamp(sdf.parse(strDate).getTime()));
                } else {
                    pst.setTimestamp(19, null);
                }

            }
            pst.setString(20, otype);
            pst.executeUpdate();

            String sql1 = "UPDATE consumer_ga SET retention_status='Y' WHERE consumer_no=? AND hrmsid=?";
            pstmt = con.prepareStatement(sql1);
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            pstmt.executeUpdate();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String createdDateTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            String sqllog = "INSERT INTO quarter_office_orders (file_no,file_date,file_type, entry_date,consumer_no,hrmsid,slno) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sqllog);
            int slno = Integer.parseInt(qbean.getOrderNumber());
            pst.setString(1, qbean.getOrderNumber());

            if (qbean.getOrderDate() != null && !qbean.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, "Auto Retention");
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(5, qbean.getConsumerNo());
            pst.setString(6, qbean.getEmpId());
            pst.setInt(7, slno);
            pst.executeUpdate();
            /**
             * *********************** Message sent
             * *********************************
             */
            pstmt = con.prepareStatement("SELECT mobile FROM emp_mast WHERE  emp_id=?");
            pstmt.setString(1, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
            if (mobile != null && !mobile.equals("")) {
                String msg = "Sir/Madam, You are permitted to retain quarters till " + endDate + ".RENT OFFICER";
                String msgId = "1407162157499204710";
                String mobileno = mobile;
                SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobileno, "", "Auto Retention");
            } else {
                String msg = "Sir/Madam, You are permitted to retain quarters till " + endDate + ".RENT OFFICER";
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobile, "", "Auto Retention");
            }
            /**
             * *********************** Message sent end
             * *********************************
             */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String retentionStatus(String cno, String empid) {
        String maxQueryService = "SELECT retention_status FROM consumer_ga WHERE consumer_no=? AND hrmsid=?";
        Statement stamt = null;
        ResultSet resultset = null;
        String temp = "";
        Connection con = null;
        String status = "";
        PreparedStatement pstmt1 = null;
        try {
            con = dataSource.getConnection();
            pstmt1 = con.prepareStatement(maxQueryService);
            pstmt1.setString(1, cno);
            pstmt1.setString(2, empid);
            resultset = pstmt1.executeQuery();

            if (resultset.next()) {
                temp = resultset.getString("retention_status");
                if (temp != null && !temp.equals("")) {
                    status = temp;
                } else {
                    status = "N";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, stamt, con);
        }

        return status;
    }

    @Override
    public List getNotVacatedQrtList(String fdate, String tdate) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT dob,consumer_ga.*,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN,otype,opp_display,opp_orderno FROM consumer_ga  "
                    + "INNER JOIN quarter_tracking ON consumer_ga.hrmsid = quarter_tracking.hrms_id   "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID  "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE hrmsid NOT IN(SELECT emp_id FROM quarter_occupation_vacation WHERE status='Vacation') AND vacation_notice=? AND  (opp_order_date BETWEEN ? AND ?) AND consumer_ga.vacation_status=? ORDER BY  opp_order_date DESC ");
            pstmt.setString(1, "Y");
            //  pstmt.setString(2, fdate);
            // pstmt.setString(3, tdate);
            pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            pstmt.setString(4, "No");
            rs = pstmt.executeQuery();

            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setDesignation(rs.getString("SPN"));
                eqBean.setOrderNumber(rs.getString("opp_orderno"));
                eqBean.setOccupationTypes(rs.getString("otype"));
                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    eqBean.setDos("");
                }
                eqBean.setVacateStatus(rs.getString("vacation_status"));
                eqBean.setOpp_display(rs.getString("opp_display"));
                eqBean.setMobileno(rs.getString("mobile"));

                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public int savenocRequest(EmpQuarterBean qbean, String loginId, String spc) {
        PreparedStatement pst = null;
        Connection con = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        int nocID = 0;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO quarter_noc(emp_id,off_code,dor,noc_for,noc_request_from,login_id,consumer_no,tvd,electricity_noc_status,water_rent_noc_status) VALUES(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, qbean.getEmpId());
            pst.setString(2, qbean.getOffcode());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(4, qbean.getNocfor());
            pst.setString(5, qbean.getNocRequest());
            pst.setString(6, loginId);
            pst.setString(7, qbean.getConsumerNo());
            if (qbean.getTvd() != null && !qbean.getTvd().equals("")) {
                pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getTvd()).getTime()));
            } else {
                pst.setTimestamp(8, null);
            }
            pst.setString(9, "N");
            pst.setString(10, "N");
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            nocID = rs.getInt("noc_id_qtr");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nocID;
    }

    @Override
    public EmpQuarterBean getQrtNocUpload(String empId, String cno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmpQuarterBean eqBean = new EmpQuarterBean();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT phslno,CONSUMER_NO,hrmsid,bldgno_qt,type_qt,unit_qt,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mobile,SPN FROM consumer_ga "
                    + "LEFT OUTER JOIN EMP_MAST ON consumer_ga.hrmsid = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE CONSUMER_NO=? AND hrmsid=?");
            pstmt.setString(1, cno);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                eqBean.setQaId(rs.getInt("phslno"));
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setMobileno(rs.getString("mobile"));
                eqBean.setDesignation(rs.getString("SPN"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return eqBean;
    }

    @Override
    public void saveuploadNOC(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String phddiskfileName = null;
        PreparedStatement pst = null;
        String phdoriginalFileName = null;
        String phdcontentType = null;

        try {
            if (qbean.getUploadDocumentphd() != null && !qbean.getUploadDocumentphd().isEmpty()) {
                phddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                phdoriginalFileName = qbean.getUploadDocumentphd().getOriginalFilename();
                phdcontentType = qbean.getUploadDocumentphd().getContentType();
                byte[] bytes = qbean.getUploadDocumentphd().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + phddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();

            if (qbean.getNocType().equals("PHD")) {
                ps = con.prepareStatement("update quarter_noc set phd_disk_file_name = ?,phd_org_file_name = ?, phd_file_type = ?,phd_file_path=?,phd_noc_reason=?,phd_noc_status='Y',phd_doe=? where noc_id_qtr = ?");
                ps.setString(1, phddiskfileName);
                ps.setString(2, phdoriginalFileName);
                ps.setString(3, phdcontentType);
                ps.setString(4, this.uploadPath);
                ps.setString(5, qbean.getPhdNocReason());
                ps.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setInt(7, qbean.getNocId());
                ps.executeUpdate();
            } else if (qbean.getNocType().equals("GED")) {
                ps = con.prepareStatement("update quarter_noc set ged_disk_file_name = ?,ged_org_file_name = ?, ged_file_type = ?,ged_file_path=?,ged_noc_reason=?,ged_noc_status='Y',ged_doe=? where noc_id_qtr = ?");
                ps.setString(1, phddiskfileName);
                ps.setString(2, phdoriginalFileName);
                ps.setString(3, phdcontentType);
                ps.setString(4, this.uploadPath);
                ps.setString(5, qbean.getPhdNocReason());
                ps.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setInt(7, qbean.getNocId());

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void downloadExtensionRequest(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodextFrom = "";
        String perioextdto = "";

        String periodFrom = "";
        String periodto = "";

        String reason = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                perioextdto = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_to"));
                periodextFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from"));

                periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));
                lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("upload_date"));
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                Rtype = rs.getString("retention_type");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                reason = rs.getString("extension_reason");

            }

            String sql = (" SELECT  (SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_from, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("To\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("       The Principal Secretary to Goverment, ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("     GA & PG Department.", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSub: Request for extension of retention of Govt. " + qaddress + " , " + typeqt + " , " + unitqt, dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("\nSir/Madam, ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            document.add(table1);

            String msg4 = "\n  I  " + empName + ", " + qaddress + " , " + typeqt + " , " + unitqt + " beg to state that after my transfer from Bhubaneswar/cuttack to " + toOfficeName + ", I was allowed to retain the Quarters for a period  of 3  months from  " + periodFrom + " to " + periodto + " .But, due to  " + reason + " I am unable to vacate the aforesaid quarters immediately";

            Paragraph point4 = new Paragraph(msg4, pFont);
            document.add(point4);

            String msg5 = "\n Under the above circumstances, I may kindly be permitted to retain the said quarters from    " + periodextFrom + " to " + perioextdto + " for which i will be obliged.";

            Paragraph point5 = new Paragraph(msg5, pFont);
            document.add(point5);

            Paragraph blank1 = new Paragraph("\n\n\n Yours Faithfully, \n ", bottomRule);
            blank1.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank1);

            Paragraph blank2 = new Paragraph(empName, bottomRule);
            blank2.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank2);

            Paragraph blank3 = new Paragraph(topostName, bottomRule);
            blank3.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank3);

            Paragraph blank4 = new Paragraph(lfeedate, bottomRule);
            blank4.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank4);


            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void DocumentSubmissionRequest(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE consumer_ga SET document_submission = ?,ocu_type=?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getOccupationTypes());
            pst.setString(3, qbean.getConsumerNo());
            pst.setString(4, qbean.getEmpId());
            pst.executeUpdate();

            pstmt = con.prepareStatement("INSERT INTO quarter_logs (consumer_no,emp_id, doe, log_type,otype,status) values (?,?,?,?,?,?) ");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setString(4, "Document Submission");
            pstmt.setString(5, qbean.getOccupationTypes());
            pstmt.setString(6, "N");
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void savedocumentsubmission(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String uploaddiskfileName = null;
        PreparedStatement pst = null;
        String uploadoriginalFileName = null;
        String uploadcontentType = null;

        try {
            if (qbean.getUploadDocument() != null && !qbean.getUploadDocument().isEmpty()) {
                uploaddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                uploadoriginalFileName = qbean.getUploadDocument().getOriginalFilename();
                uploadcontentType = qbean.getUploadDocument().getContentType();
                byte[] bytes = qbean.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + uploaddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            String Octype = qbean.getOccupationTypes();
            if (Octype.equals("1") && Octype != null) {
                pst = con.prepareStatement("UPDATE consumer_ga SET document_submission = ?,ocu_type=?"
                        + " WHERE CONSUMER_NO=? AND hrmsid=?");
                pst.setString(1, "Y");
                pst.setString(2, qbean.getOccupationTypes());
                pst.setString(3, qbean.getConsumerNo());
                pst.setString(4, qbean.getEmpId());
                pst.executeUpdate();

                ps = con.prepareStatement("INSERT INTO quarter_logs (consumer_no,emp_id, doe, log_type,status,disk_file_name,org_file_name,file_type,file_path,dos,case_opp_id,otype) values (?,?,?,?,?,?,?,?,?,?,?,?) ");
                ps.setString(1, qbean.getConsumerNo());
                ps.setString(2, qbean.getEmpId());
                ps.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setString(4, "Document Submission");
                ps.setString(5, "Y");
                ps.setString(6, uploaddiskfileName);
                ps.setString(7, uploadoriginalFileName);
                ps.setString(8, uploadcontentType);
                ps.setString(9, this.uploadPath);
                ps.setTimestamp(10, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setInt(11, qbean.getOppCaseId());
                ps.setString(12, qbean.getOccupationTypes());
                ps.execute();
            }
            if ((Octype.equals("4") || Octype.equals("7")) && Octype != null) {
                ps = con.prepareStatement("update quarter_logs set disk_file_name = ?,org_file_name = ?, file_type = ?,file_path=?,dos=?,status=?,case_opp_id=? where emp_id = ? AND consumer_no=?");
                ps.setString(1, uploaddiskfileName);
                ps.setString(2, uploadoriginalFileName);
                ps.setString(3, uploadcontentType);
                ps.setString(4, this.uploadPath);
                ps.setTimestamp(5, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setString(6, "Y");
                ps.setInt(7, qbean.getOppCaseId());
                ps.setString(8, qbean.getEmpId());
                ps.setString(9, qbean.getConsumerNo());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public EmpQuarterBean downloadsubmissionDocument(String emp_id, String consumer_no, int oppcaseId, String dstatus) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            if (dstatus.equals("S")) {
                dstatus = "Show Cause Reply";
            }
            if (dstatus.equals("A")) {
                dstatus = "Appeal Document Upload";
            }
            pst = con.prepareStatement("select org_file_name,disk_file_name,file_type,file_path from quarter_logs where emp_id = ? AND consumer_no=? AND case_opp_id=? AND log_type=? ");
            pst.setString(1, emp_id);
            pst.setString(2, consumer_no);
            pst.setInt(3, oppcaseId);
            pst.setString(4, dstatus);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                qbean.setPhdoriginalFilename(rs.getString("org_file_name"));
                qbean.setPhddiskFileName(rs.getString("disk_file_name"));
                qbean.setGetphdContentType("file_type");
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath + File.separator + qbean.getPhddiskFileName());
            qbean.setFilecontent(FileUtils.readFileToByteArray(f));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qbean;

    }

    @Override
    public List getDocumentSubmissionList(String otype) {
        Connection con = null;

        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT a.*,b.* from consumer_ga a,quarter_logs b WHERE a.hrmsid=b.emp_id AND a.consumer_no=b.consumer_no  AND b.case_opp_id=0 AND b.otype=? ORDER BY doe DESC");
            pstmt.setString(1, otype);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                eqBean.setEmpId(rs.getString("hrmsid"));
                eqBean.setEmpName(rs.getString("name"));
                eqBean.setQuarterNo(rs.getString("bldgno_qt"));
                eqBean.setQrtrtype(rs.getString("type_qt"));
                eqBean.setQrtrunit(rs.getString("unit_qt"));
                String address = rs.getString("address1") + " " + rs.getString("address2") + " " + rs.getString("address3");
                eqBean.setAddress(address);
                eqBean.setOriginalFilename(rs.getString("org_file_name"));
                String dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
                String doe = CommonFunctions.getFormattedOutputDate1(rs.getDate("doe"));
                eqBean.setDos(dos);
                eqBean.setDor(doe);
                eqBean.setDsStatus(rs.getString("status"));

                alist.add(eqBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public String getQuarterPoolName(String qid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String poolname = "";

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT q_id,pool_name from g_qtr_pool where q_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(qid));
            rs = pst.executeQuery();
            if (rs.next()) {
                poolname = rs.getString("pool_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return poolname;
    }

    /* @Override
     public List getEquarterUnitList() {
     Connection con = null;
     Statement stmt = null;
     ResultSet rs = null;

     List unitAreaList = new ArrayList();
     try {
     con = dataSource.getConnection();
     String sql = "SELECT distinct unit from equarter.qtrmast order by unit";
     stmt = con.createStatement();
     rs = stmt.executeQuery(sql);
     while (rs.next()) {
     SelectOption so = new SelectOption();
     so.setValue(rs.getString("unit"));
     so.setLabel(rs.getString("unit"));
     unitAreaList.add(so);
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(rs, stmt, con);
     }
     return unitAreaList;
     }

     @Override
     public List getEquarterUnitAreawiseQuarterType(String unitArea) {
     Connection con = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;

     List unitAreaList = new ArrayList();
     try {
     con = dataSource.getConnection();
     pstmt = con.prepareStatement("SELECT distinct qrtr_type from equarter.qtrmast where unit=?  order by qrtr_type");
     pstmt.setString(1, unitArea);
     rs = pstmt.executeQuery();
     while (rs.next()) {
     SelectOption so = new SelectOption();
     so.setValue(rs.getString("qrtr_type"));
     so.setLabel(rs.getString("qrtr_type"));
     unitAreaList.add(so);
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
     }
     return unitAreaList;
     }*/
    @Override
    public List getbuildingDetails(String unitArea, String unitType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List unitAreaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct bldg_no from equarter.qtrmast where unit=? AND qrtr_type=? order by bldg_no");
            pstmt.setString(1, unitArea);
            pstmt.setString(2, unitType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("bldg_no"));
                so.setLabel(rs.getString("bldg_no"));
                unitAreaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return unitAreaList;
    }

    @Override
    public List getLedgerEmployee(String qrtrunit, String qrtrtype, String qrtno) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT *,ARRAY_TO_STRING(ARRAY[ocup_fname, ocup_lname], ' ') EMPNAME FROM equarter.ocupants "
                    + " WHERE unit=? AND qrtr_type=? AND bldg_no=?");
            pstmt.setString(1, qrtrunit);
            pstmt.setString(2, qrtrtype);
            pstmt.setString(3, qrtno);
            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                eqBean.setOccuId(rs.getString("ocu_cd"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setOrderNumber(rs.getString("ord_no"));
                if (rs.getString("dt_ocupation") != null && !rs.getString("dt_ocupation").equals("")) {
                    eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_ocupation")));
                } else {
                    eqBean.setOrderDate("");
                }
                if (rs.getString("dt_order") != null && !rs.getString("dt_order").equals("")) {
                    eqBean.setAllotmentDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_order")));
                } else {
                    eqBean.setAllotmentDate("");
                }

                if (rs.getString("dt_vacation") != null && !rs.getString("dt_vacation").equals("")) {
                    eqBean.setRelieveDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_vacation")));
                } else {
                    eqBean.setRelieveDate("");
                }
                if (rs.getString("dt_retire") != null && !rs.getString("dt_retire").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_retire")));
                } else {
                    eqBean.setDos("");
                }

                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public EmpQuarterBean getLedgeremployeeDetails(String occid) {
        EmpQuarterBean eqBean = new EmpQuarterBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT *,ARRAY_TO_STRING(ARRAY[ocup_fname, ocup_lname], ' ') EMPNAME FROM equarter.ocupants where ocu_cd = ?");
            pst.setString(1, occid);
            rs = pst.executeQuery();
            if (rs.next()) {
                eqBean.setOccuId(rs.getString("ocu_cd"));
                eqBean.setEmpName(rs.getString("EMPNAME"));
                eqBean.setOrderNumber(rs.getString("ord_no"));
                if (rs.getString("dt_ocupation") != null && !rs.getString("dt_ocupation").equals("")) {
                    eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_ocupation")));
                } else {
                    eqBean.setOrderDate("");
                }
                if (rs.getString("dt_order") != null && !rs.getString("dt_order").equals("")) {
                    eqBean.setAllotmentDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_order")));
                } else {
                    eqBean.setAllotmentDate("");
                }

                if (rs.getString("dt_vacation") != null && !rs.getString("dt_vacation").equals("")) {
                    eqBean.setRelieveDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_vacation")));
                } else {
                    eqBean.setRelieveDate("");
                }
                if (rs.getString("dt_retire") != null && !rs.getString("dt_retire").equals("")) {
                    eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_retire")));
                } else {
                    eqBean.setDos("");
                }
                eqBean.setQuarterNo(rs.getString("bldg_no"));
                eqBean.setQrtrtype(rs.getString("qrtr_type"));
                eqBean.setQrtrunit(rs.getString("unit"));
                eqBean.setSection(rs.getString("sec"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eqBean;

    }

    @Override
    public List getLedgerDataEmployeewise(String occId) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM equarter.ledgernew WHERE ocu_cd=? ORDER BY financial_year, mon_cd ASC");
            pstmt.setString(1, occId);

            rs = pstmt.executeQuery();
            EmpQuarterBean eqBean = null;
            while (rs.next()) {
                eqBean = new EmpQuarterBean();
                if (rs.getString("dt_real") != null && !rs.getString("dt_real").equals("")) {
                    eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_real")));
                } else {
                    eqBean.setOrderDate("");
                }
                int monthNumber = rs.getInt("mon_cd");
                String mName = Month.of(monthNumber).name();
                eqBean.setStrmonth(mName);
                eqBean.setYear(rs.getInt("financial_year"));
                eqBean.setOpbla(rs.getInt("opbal"));
                eqBean.setAssessment(rs.getInt("assessment"));
                eqBean.setCbalance(rs.getInt("clsbal"));
                eqBean.setReal(rs.getInt("realization"));
                eqBean.setNocType(rs.getString("remarks"));

                quarterList.add(eqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

    @Override
    public EmpQuarterBean getextentionRequestDetails(String empId, String cno) {
        EmpQuarterBean eqBean = new EmpQuarterBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pst.setString(1, cno);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            String perioextdto = "";
            String periodextFrom = "";
            String periodFrom = "";
            String periodto = "";

            if (rs.next()) {
                perioextdto = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_to"));
                periodextFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from"));
                eqBean.setQuarterAllowedFromDate(periodextFrom);
                eqBean.setQuarterAllowedToDate(perioextdto);

                if (rs.getDate("extension_allowed_from") != null && !rs.getDate("extension_allowed_from").equals("")) {

                    if (rs.getDate("extension_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from"));

                    }
                    if (rs.getDate("extension_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_to"));
                        String strDate = sdf.format(rs.getDate("extension_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);
                    }

                } else {
                    if (rs.getDate("quarter_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                    }
                    if (rs.getDate("quarter_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                        String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                    }

                }
                eqBean.setReliefDate(wefdate2);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eqBean;

    }

    @Override
    public void GeneratePDFExtendedRetention(Document document, String empId, String consumerNo) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String periodFrom = "";
        String periodto = "";
        String vacateperiod = "";

        String empName = "";
        String dos = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String wefdate = "";
        String wefdate1 = "";
        String wefdate2 = "";
        String oswasfile = "";
        String paytype = "";

        try {
            con = this.dataSource.getConnection();
           // ps = con.prepareStatement("select * from hw_loan_sanction_details HD INNER JOIN hw_emp_gpf_loan HL ON HD.taskid = HL.taskid WHERE HL.taskid = ?");
            //ps.setInt(1, taskId);

            //  rs = ps.executeQuery();
            //  rs.next();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT * FROM quarter_tracking WHERE consumer_no=? AND hrms_id=? ORDER BY tracking_id DESC LIMIT 1");
            pstmt.setString(1, consumerNo);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();
            String rtype = "normal";

            if (rs.next()) {
                rtype = "extended";
                if (rs.getDate("extension_order_no") != null && !rs.getDate("extension_order_no").equals("")) {
                    orderNo = rs.getString("extension_order_no");
                    orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                    if (rs.getDate("extension_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_from"));

                    }
                    if (rs.getDate("extension_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_allowed_to"));
                        String strDate = sdf.format(rs.getDate("extension_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                    }
                    if (rs.getDate("extension_vacate_date") != null) {
                        vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_vacate_date"));
                    }
                    if (rs.getDate("extension_relief_date") != null) {
                        lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_relief_date"));
                    }

                } else {
                    orderNo = rs.getString("order_no");
                    orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("extension_order_date"));
                    if (rs.getDate("quarter_allowed_from") != null) {
                        periodFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_from"));

                    }
                    if (rs.getDate("quarter_allowed_to") != null) {
                        periodto = CommonFunctions.getFormattedOutputDate1(rs.getDate("quarter_allowed_to"));
                        String strDate = sdf.format(rs.getDate("quarter_allowed_to"));
                        c.setTime(sdf.parse(strDate));
                        //Incrementing the date by 1 day
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate = sdf.format(c.getTime());
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate);
                        wefdate = CommonFunctions.getFormattedOutputDate1(date1);

                        c.add(Calendar.DAY_OF_MONTH, 60);
                        String nextdate1 = sdf.format(c.getTime());

                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate1);
                        wefdate1 = CommonFunctions.getFormattedOutputDate1(date2);

                        c.add(Calendar.DAY_OF_MONTH, 1);
                        String nextdate2 = sdf.format(c.getTime());

                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(nextdate2);
                        wefdate2 = CommonFunctions.getFormattedOutputDate1(date3);

                    }
                    if (rs.getDate("vacate_date") != null) {
                        vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("vacate_date"));
                    }
                    if (rs.getDate("licence_fee_date") != null) {
                        lfeedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("licence_fee_date"));
                    }

                }
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                Rtype = rs.getString("retention_type");
                otype = rs.getString("otype");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                oswasfile = rs.getString("oswas_fileno");
                paytype = rs.getString("licence_fee_type");

            }

            String sql = (" SELECT  (SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_from, "
                    + " ( SELECT off_en FROM g_office  WHERE  off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");
                dos = CommonFunctions.getFormattedOutputDate1(rs.getDate("emp_dos"));

            }
            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration & Public Grievance(Rent) Department ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bhubaneswar ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo. " + orderNo + "/QMS.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\nDate. " + orderDate, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            if (otype.equals("3")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new int[]{5});
                table1.setWidthPercentage(100);
                PdfPCell datacell;

                datacell = new PdfPCell(new Phrase("\nOFFICE ORDER ", dataHdrFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(datacell);
                document.add(table1);

                String msg4 = "\n  Consequent upon relief on transfer to O/O " + toOfficeName + ", on " + lfeedate + ". " + empName + ", EX - " + frompostName
                        + " Bhubaneswar/Cuttack is hereby allowed to retain Government Quarters No " + qaddress + " , " + unitqt + ", " + typeqt + " (area), Bhubaneswar for the period from "
                        + periodFrom + " to " + periodto + " on  payment of " + paytype + " Licence  Fee  "
                        + " . Allotment of the said quarters made in his favour is cancelled with effect from  " + wefdate + " and five times of Standard Licence Fee charged against him from the date of cancellation. ";
                Paragraph point4 = new Paragraph(msg4, pFont);
                point4.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point4);

                // String msg5 = "\n Further  he  is directed   to  vacate  the  quarters  before " + wefdate + " failing which  eviction  proceedings   will  be  initiated  against  him  for  his  eviction  from  the  said public premises  under the provisions  of O.P.P. (EUO) Act. 1972.";
                String msg5 = "\n Further  he  is directed   to  vacate  the  quarters Immediately failing which  eviction  proceedings   will  be  initiated  against  him  for  his  eviction  from  the  said public premises  under the provisions  of O.P.P. (EUO) Act. 1972.";
                Paragraph point5 = new Paragraph(msg5, pFont);
                point5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point5);

                String msg_new = "\n  If you want to vacate the quarters before the due date, you can do so and house licence fee as admisable would be charged till the date of vacation of quarters ";
                Paragraph point_new = new Paragraph(msg_new, pFont);
                point_new.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point_new);

                String msg_newfile = "\n  Goverment approval have been obtained in OSWAS file No.  " + oswasfile;
                Paragraph point_newfile = new Paragraph(msg_newfile, pFont);
                point_newfile.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point_newfile);

                Paragraph blank1 = new Paragraph("\n\n\n Rent Officer \n G.A. & P.G.(Rent) Department", bottomRule);
                blank1.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank1);

                Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void downloadNoticeOPP(Document document, int caseId) {
        EmpQuarterBean qbean = new EmpQuarterBean();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String caseInitiatedDate = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String oppcaseno = "";
        String oppcasedate = "";
        String show_cause_date = "";

        String empName = "";
        String empId = "";
        String caseno = "";

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT a.*,b.* FROM quarter_tracking a INNER JOIN equarter.case_master b ON a.consumer_no=b.consumer_no AND a.hrms_id=b.emp_id  WHERE  b.case_id=? LIMIT 1");
            // pstmt.setString(1, consumerNo);
            // pstmt.setString(2, empId);
            pstmt.setInt(1, caseId);
            rs = pstmt.executeQuery();
            // System.out.println("caseId===" + caseId);

            if (rs.next()) {
                caseno = rs.getString("case_no");
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                otype = rs.getString("otype");
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                empId = rs.getString("emp_id");
                if (rs.getDate("show_cause_date") != null) {
                    show_cause_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("show_cause_date"));
                }
                oppcaseno = rs.getString("requisation_no");
                if (rs.getDate("opp_req_date") != null) {
                    oppcasedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_req_date"));
                }
                if (rs.getDate("case_initiated_date") != null) {
                    caseInitiatedDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("case_initiated_date"));
                } else {
                    caseInitiatedDate = oppcasedate;
                }

            }

            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
            }
            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }
            if (otype.equals("4")) {
                Rtype = "Retirement";
            }
            if (otype.equals("5")) {
                Rtype = "Death";
            }
            if (otype.equals("6")) {
                Rtype = "Dismissal";
            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Form-A", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("[See Rule 3-(a)] ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Form of notice under Sub-section(1) of section 4 of the orissa public premises(Eviction of Unauthorised Occupants) ACt, 1972", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + empName + " ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("      " + postName + " ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("      " + deptName + " DEPARTMENT", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            document.add(table1);

            String msg_opp = "(Estate Court Case No: " + caseno + " dt." + caseInitiatedDate + ")";
            Paragraph point_opp = new Paragraph(msg_opp, hdrTextFont);
            point_opp.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point_opp);

            String msg1 = "\n        Whereas I, the undersigned, am of opinion that you are in un-authorised occupation of the public premises mentioned in the schedule below and that you should be evicted from the said premises on the follwoing grounds: ";

            Paragraph point1 = new Paragraph(msg1, pFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);

            PdfPTable table2 = null;
            table2 = new PdfPTable(1);
            table2.setWidths(new int[]{5});
            table2.setWidthPercentage(100);

            PdfPCell cell2 = null;

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Grounds", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);
            document.add(table2);

            String msg2 = "\n         It is reported by the Rent Officer, GA & P.G Department that you are now un-authorisedly occupying Goverment quarters no  " + qaddress + " , " + unitqt + ", " + typeqt + " (area),  after your " + Rtype + ".";
            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            String msg3 = "\n        I am, therefore, of the opinion that your occupation over said Public Premises is un-authorised and you are liable for eviction from the said Public  Premises.  ";
            Paragraph point3 = new Paragraph(msg3, pFont);
            point3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point3);

            String msg4 = "\n        Now, therefore, in pursuance of Sub-section(1) of Section 4 of the Act I hereby call upon you and all persons concerned that is to say, the public premises to show cause on the date" + show_cause_date + " at 11:30AM "
                    + "all persons who are, or may be, in occupation of, or claim interest in, *why such an order of eveiction should not be made.  ";
            Paragraph point4 = new Paragraph(msg4, pFont);
            point4.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point4);

            PdfPTable table4 = new PdfPTable(1);
            table4.setWidths(new int[]{5});
            table4.setWidthPercentage(100);
            PdfPCell datace4;

            datace4 = new PdfPCell(new Phrase("\n\n\nQtrs No:" + qaddress + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            datacell = new PdfPCell(new Phrase("Type:" + unitqt + " ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Unit:" + typeqt + " ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Bhubaneswar/Cuttack" + " ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datacell);

            document.add(table4);

            Paragraph blank = new Paragraph("\n\n\n Date", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            Paragraph blank2 = new Paragraph("\n\n Signature and seal of the Estate Officer", bottomRule);
            blank2.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank2);

            Paragraph blank1 = new Paragraph("\n\n *.The date should be a date not earlier than fifteen days from the date of dervice of the notice.", bottomRule);
            blank1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank1);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void savesaveshowcausereply(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String uploaddiskfileName = null;
        PreparedStatement pst = null;
        String uploadoriginalFileName = null;
        String uploadcontentType = null;

        try {
            if (qbean.getUploadDocument() != null && !qbean.getUploadDocument().isEmpty()) {
                uploaddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                uploadoriginalFileName = qbean.getUploadDocument().getOriginalFilename();
                uploadcontentType = qbean.getUploadDocument().getContentType();
                byte[] bytes = qbean.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + uploaddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement("UPDATE consumer_ga SET show_cause_reply = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            pstmt = con.prepareStatement("INSERT INTO quarter_logs (consumer_no,emp_id, doe, log_type,status,disk_file_name,org_file_name,file_type,file_path,dos,case_opp_id) values (?,?,?,?,?,?,?,?,?,?,?) ");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setString(4, "Show Cause Reply");
            pstmt.setString(5, "Y");
            pstmt.setString(6, uploaddiskfileName);
            pstmt.setString(7, uploadoriginalFileName);
            pstmt.setString(8, uploadcontentType);
            pstmt.setString(9, this.uploadPath);
            pstmt.setTimestamp(10, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setInt(11, qbean.getOppCaseId());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void UpdateOPPSendStatus(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();
            // System.out.println("1111qbean.getSplCaseStatus()==="+qbean.getSplCaseStatus());
            pst = con.prepareStatement("UPDATE quarter_tracking SET opp_display = ?,opp_send_court_date=?"
                    + " WHERE consumer_no=? AND hrms_id=?");
            pst.setString(1, "Y");
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(3, qbean.getConsumerNo());

            pst.setString(4, qbean.getEmpId());
            pst.executeUpdate();

            String mobile = "";
            pstmt = con.prepareStatement("SELECT mobile FROM emp_mast WHERE  emp_id=?");
            pstmt.setString(1, qbean.getEmpId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
            if (mobile != null && !mobile.equals("")) {
                String msg = "Sir/Madam, Inspite of our request,you are not vacating the Qtrs and retaining Quarters unauthorisedly.Eviction proceeding is being initiated in the court of Estate Officer,Bhubaneswar. RENT OFFICER";
                String msgId = "1407162746535716158";
                String mobileno = mobile;
                //String mobileno = "9132055693";
                SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobileno, "", "Unauthorized Retention");
            } else {
                String msg = "Sir/Madam, Inspite of our request,you are not vacating the Qtrs and retaining Quarters unauthorisedly.Eviction proceeding is being initiated in the court of Estate Officer,Bhubaneswar. RENT OFFICER ";
                insertQMSSMSLog(con, qbean.getEmpId(), msg, mobile, "", "Unauthorized Retention");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void DownloadFiveOneOrder(Document document, PdfWriter writer, int caseId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String tdate = "";

        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String vacateperiod = "";

        String oppcaseno = "";
        String oppcasedate = "";
        String show_cause_date = "";
        String orderdetails = "";
        String empName = "";
        String empId = "";
        String caseno = "";
        String caseInitiatedDate = "";
        String OpStatus = "";
        String opVacatMonth = "";
        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT a.*,b.* FROM quarter_tracking a INNER JOIN equarter.case_master b ON a.consumer_no=b.consumer_no AND a.hrms_id=b.emp_id  WHERE  b.case_id=? LIMIT 1");
            // pstmt.setString(1, consumerNo);
            // pstmt.setString(2, empId);
            pstmt.setInt(1, caseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                caseno = rs.getString("case_no") + "/" + rs.getString("case_year");
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                otype = rs.getString("otype");
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                empId = rs.getString("emp_id");
                OpStatus = rs.getString("op_order_status");
                opVacatMonth = rs.getString("op_vacate_month");

                orderNo = rs.getString("order_no");
                orderDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date"));

                if (rs.getDate("opp_cancelled_wef") != null) {
                    vacateperiod = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_cancelled_wef"));
                }
                if (rs.getDate("show_cause_date") != null) {
                    show_cause_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("show_cause_date"));
                }
                if (rs.getDate("order_five_date") != null) {
                    tdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_date"));
                }
                oppcaseno = rs.getString("requisation_no");
                if (rs.getDate("opp_req_date") != null) {
                    oppcasedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_req_date"));
                }
                if (rs.getDate("case_initiated_date") != null) {
                    caseInitiatedDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("case_initiated_date"));
                } else {
                    caseInitiatedDate = oppcasedate;
                }
                orderdetails = rs.getString("op_order_details");

            }

            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
            }
            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }
            if (otype.equals("4")) {
                Rtype = "Retirement";
            }
            if (otype.equals("5")) {
                Rtype = "Death";
            }
            if (otype.equals("6")) {
                Rtype = "Dismissal";
            }

            String oppmess = "OP is absent and the case is taken up on merit.";
            // System.out.println("OpStatus="+OpStatus);
            if (OpStatus.equals("Present")) {
                oppmess = "OP is present and he/she stated that he/she sill vacate quarters within " + opVacatMonth + " months";

            }

            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;
            if (orderdetails != null && !orderdetails.equals("")) {
                orderdetails = "<html><head></head><body>" + orderdetails + "</body></html>";
                XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                InputStream is = new ByteArrayInputStream(orderdetails.getBytes(StandardCharsets.UTF_8));
                worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
            } else {

                String msg_opp = "Estate Court OPP Case No: " + caseno + "(Qr)";
                Paragraph point_opp = new Paragraph(msg_opp, hdrTextFont);
                point_opp.setAlignment(Element.ALIGN_RIGHT);
                document.add(point_opp);

                String msg1 = "\n   Case is taken up today. Notice U/S 4(1) of OPP Act was duly served. " + oppmess;
                Paragraph point1 = new Paragraph(msg1, pFont);
                point1.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point1);

                String msg2 = "\n   Brief fact of the case is that the allotment order of quarters in favour of OP " + empName + " ," + postName + " ," + deptName + " DEPARTMENT  i.e. Qrs.No- " + qaddress + " , " + unitqt + ", " + typeqt + ", has been cancelled w.e.f  " + vacateperiod + " vide order No. " + orderNo + ", Dt. " + orderDate + " of G.A & P.G (rent) Deptt.consequent upon his/her " + Rtype + " under provision of Special Accommodation Rules,1959 appended to Odisha Service Code. since Op has no authority to continue in the quarters subsequent to the cancellation of order, I treat OP as an unauthorized occupant and direct him to vacate the quarters within 15 days of receipt of the order.";
                Paragraph point2 = new Paragraph(msg2, pFont);
                point2.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point2);

                String msg3 = "\n   Office to ensure that the order copy is served immediately.  ";
                Paragraph point3 = new Paragraph(msg3, pFont);
                point3.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point3);

                String msg4 = "\n   Pronounced the Order in the Open Court today I.e. " + tdate + ".  ";
                Paragraph point4 = new Paragraph(msg4, pFont);
                point4.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point4);

                String msg5 = "\n   Put up after 15 days of receipt of the order. ";
                Paragraph point5 = new Paragraph(msg5, pFont);
                point5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point5);

                Paragraph blank2 = new Paragraph("\n\n\n Estate Officer", bottomRule);
                blank2.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank2);

                Paragraph blank = new Paragraph("\n G.A & P.G Department", bottomRule);
                blank.setAlignment(Element.ALIGN_RIGHT);
                document.add(blank);

                Paragraph blank5 = new Paragraph("\n\n *.This is a computer-generated document. No signature is required.", bottomRule);
                blank5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(blank5);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void DownloadFiveOneNotice(Document document, int caseId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String tdate = "";
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String oppcaseno = "";
        String oppcasedate = "";
        String show_cause_date = "";

        String empName = "";
        String empId = "";
        String caseno = "";
        String caseInitiatedDate = "";
        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT a.*,b.* FROM quarter_tracking a INNER JOIN equarter.case_master b ON a.consumer_no=b.consumer_no AND a.hrms_id=b.emp_id  WHERE  b.case_id=? LIMIT 1");
            // pstmt.setString(1, consumerNo);
            // pstmt.setString(2, empId);
            pstmt.setInt(1, caseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                caseno = rs.getString("case_no") + "/" + rs.getString("case_year");
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                otype = rs.getString("otype");
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                empId = rs.getString("emp_id");
                if (rs.getDate("show_cause_date") != null) {
                    show_cause_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("show_cause_date"));
                }
                oppcaseno = rs.getString("requisation_no");
                if (rs.getDate("opp_req_date") != null) {
                    oppcasedate = CommonFunctions.getFormattedOutputDate1(rs.getDate("opp_req_date"));
                }
                if (rs.getDate("case_initiated_date") != null) {
                    caseInitiatedDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("case_initiated_date"));
                } else {
                    caseInitiatedDate = oppcasedate;
                }
                if (rs.getDate("order_five_date") != null) {
                    tdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_date"));
                }

            }

            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";
            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
            }
            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }
            if (otype.equals("4")) {
                Rtype = "Retirement";
            }
            if (otype.equals("5")) {
                Rtype = "Death";
            }
            if (otype.equals("6")) {
                Rtype = "Dismissal";
            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("IN THE COURT OF THE ESTATE OFFICER, BHUBANESWAR ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OPP CASE NO- " + caseno + "(Qr)", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            String msg2 = "\n   Order  under Sub-Section (1) of Section-5 of the Orissa Public Premises (Eviction of Unauthorized Occupants)Act " + empName + " ," + postName + " ," + deptName + " DEPARTMENT, Bhubaneswar is in unauthorized occupation of the public premises specified in the schedule below.";
            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            PdfPTable table2 = null;
            table2 = new PdfPTable(1);
            table2.setWidths(new int[]{5});
            table2.setWidthPercentage(100);

            PdfPCell cell2 = null;

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("REASONS", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("(A COPY OF THE ORDER DTD " + tdate + " IS ENCLOSED) ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);
            document.add(table2);
            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            String msg3 = "\n   Now, therefore, in exercise of the power conferred on me under sub-Section (1) Section-5 of the Orissa Public Premises (Eviction of unauthorized Occupants) Act,1972, I hereby order that said " + empName + " and all persons who may be in occupation of the said premises within 15 days of the date of receipt of this order."
                    + " In the event of refusal or failure to comply with this order within the period specified above, the said " + empName + " and all other persons concerned are liable to be evicted from the said premises if needed by use of such force as may be necessary.";
            Paragraph point3 = new Paragraph(msg3, pFont);
            point3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point3);

            PdfPTable table3 = null;
            table3 = new PdfPTable(1);
            table3.setWidths(new int[]{5});
            table3.setWidthPercentage(100);

            PdfPCell cell3 = null;

            cell3 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("SCHEDULE", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            PdfPTable table4 = new PdfPTable(1);
            table4.setWidths(new int[]{5});
            table4.setWidthPercentage(100);
            PdfPCell datace4;

            datace4 = new PdfPCell(new Phrase("\n\n\nQtrs No:" + qaddress + " ", dataHdrFont));
            datace4.setBorder(Rectangle.NO_BORDER);
            table4.addCell(datace4);

            cell2 = new PdfPCell(new Phrase("Type:" + unitqt + " ", dataHdrFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Unit:" + typeqt + " ", dataHdrFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Bhubaneswar/Cuttack" + " ", dataHdrFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            table4.addCell(cell2);
            document.add(table4);

            Paragraph blank2 = new Paragraph("\n\n\n Estate Officer", bottomRule);
            blank2.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank2);

            Paragraph blank = new Paragraph("\n G.A & P.G Department", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void Testsms(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();
        String vstatus = qbean.getVacateStatus();
        try {

            con = this.dataSource.getConnection();
            String msg = "Sir/Madam, Your quarters retention period is over. Kindly vacate the qtrs immediately. RENT OFFICER";
            String msgId = "1407162157505028034";
            String mobileno = "9853222814,9937340481,9437337822,7978008944,9439714225,9437152273,8594959336,6370722817,9078092971,9437258592,9438137507,9438024070,9853291981,9437392692";
            SMSServices smhttp = new SMSServices(mobileno, msg, msgId);
            //  insertQMSSMSLog(con, "12345", msg, mobileno, "", "Notice u/s 5(1)");
            // System.out.println("SMSServices=="+SMSServices);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void insertQMSSMSLog(Connection conpsql, String empid, String msg, String mobile, String deliverymsg, String msgtype) {

        PreparedStatement pst = null;

        String startTime = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = conpsql.prepareStatement("INSERT INTO qms_sms_log(EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?)");
            pst.setString(1, empid);
            pst.setString(2, msg);
            pst.setString(3, msgtype);
            pst.setString(4, mobile);
            pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(6, deliverymsg);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }

    @Override
    public void saveWateruploadClearance(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String phddiskfileName = "";
        PreparedStatement pst = null;
        String phdoriginalFileName = "";
        String phdcontentType = "";

        try {
            // System.out.println("qbean.getPhdNocReason()====" + qbean.getPhdNocReason());
            if (qbean.getUploadDocumentphd() != null && !qbean.getUploadDocumentphd().isEmpty()) {
                phddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                phdoriginalFileName = qbean.getUploadDocumentphd().getOriginalFilename();
                phdcontentType = qbean.getUploadDocumentphd().getContentType();
                byte[] bytes = qbean.getUploadDocumentphd().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + phddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();

            ps = con.prepareStatement("update quarter_noc set water_rent_disk_file_name = ?,water_rent_noc_file_name = ?, water_rent_file_type = ?,water_rent_file_path=?,water_rent_noc_reason=?,water_rent_noc_status='Y',water_rent_doe=? where noc_id_qtr = ?");
            ps.setString(1, phddiskfileName);
            ps.setString(2, phdoriginalFileName);
            ps.setString(3, phdcontentType);
            ps.setString(4, this.uploadPath);
            ps.setString(5, qbean.getPhdNocReason());
            ps.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setInt(7, qbean.getNocId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveappeal(EmpQuarterBean qbean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String uploaddiskfileName = null;
        PreparedStatement pst = null;
        String uploadoriginalFileName = null;
        String uploadcontentType = null;

        try {
            if (qbean.getUploadDocument() != null && !qbean.getUploadDocument().isEmpty()) {
                uploaddiskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                uploadoriginalFileName = qbean.getUploadDocument().getOriginalFilename();
                uploadcontentType = qbean.getUploadDocument().getContentType();
                byte[] bytes = qbean.getUploadDocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + uploaddiskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement("UPDATE consumer_ga SET estate_appeal = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

            pstmt = con.prepareStatement("INSERT INTO quarter_logs (consumer_no,emp_id, doe, log_type,status,disk_file_name,org_file_name,file_type,file_path,dos,case_opp_id) values (?,?,?,?,?,?,?,?,?,?,?) ");
            pstmt.setString(1, qbean.getConsumerNo());
            pstmt.setString(2, qbean.getEmpId());
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setString(4, "Appeal Document Upload");
            pstmt.setString(5, "Y");
            pstmt.setString(6, uploaddiskfileName);
            pstmt.setString(7, uploadoriginalFileName);
            pstmt.setString(8, uploadcontentType);
            pstmt.setString(9, this.uploadPath);
            pstmt.setTimestamp(10, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pstmt.setInt(11, qbean.getOppCaseId());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void DownloadAppealNotice(Document document, int caseId) {
        // OppRequisition qbean = new OppRequisition();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String Rtype = "";
        String orderNo = "";
        String orderDate = "";
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";
        String otype = "";
        String empname = "";
        String designation = "";
        String fromOffice = "";
        String toOffice = "";
        String frompost = "";
        String topost = "";

        String fromOfficeName = "";
        String toOfficeName = "";
        String frompostName = "";
        String topostName = "";
        String lfeedate = "";

        String appealcaseno = "";
        String order_five_date = "";
        String appeal_case_date = "";

        String empName = "";
        String empId = "";
        String caseno = "";
        String caseInitiatedDate = "";
        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            pstmt = con.prepareStatement("SELECT a.*,b.* FROM quarter_tracking a INNER JOIN equarter.case_master b ON a.consumer_no=b.consumer_no AND a.hrms_id=b.emp_id  WHERE  b.case_id=? AND appeal_status=? LIMIT 1");
            // pstmt.setString(1, consumerNo);
            // pstmt.setString(2, empId);
            pstmt.setInt(1, caseId);
            pstmt.setString(2, "Y");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                caseno = rs.getString("case_no");
                qaddress = rs.getString("qrt_no");
                unitqt = rs.getString("unit_area");
                typeqt = rs.getString("qrt_type");
                otype = rs.getString("otype");
                fromOffice = rs.getString("office_from_code");
                toOffice = rs.getString("office_to_code");
                frompost = rs.getString("post_from_office");
                topost = rs.getString("post_to_office");
                empId = rs.getString("emp_id");
                if (rs.getDate("appeal_case_date") != null) {
                    appeal_case_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("appeal_case_date"));
                }
                appealcaseno = rs.getString("appeal_case_no");
                if (rs.getDate("order_five_date") != null) {
                    order_five_date = CommonFunctions.getFormattedOutputDate1(rs.getDate("order_five_date"));
                }

            }

            String sql = (" SELECT  (SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_from, "
                    + " ( SELECT department_name FROM g_office ,  g_department  WHERE    g_office.department_code=g_department.department_code  AND   g_office.off_code=?) AS off_en_to, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS from_post, "
                    + " ( SELECT post FROM g_post WHERE post_code=?) AS to_post, "
                    + " ( SELECT dos  FROM emp_mast WHERE emp_id=?) AS emp_dos, "
                    + " ( SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') fullname FROM emp_mast WHERE emp_id=?) AS emp_name     ");
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fromOffice);
            pstmt.setString(2, toOffice);
            pstmt.setString(3, frompost);
            pstmt.setString(4, topost);
            pstmt.setString(5, empId);
            pstmt.setString(6, empId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                fromOfficeName = rs.getString("off_en_from");
                toOfficeName = rs.getString("off_en_to");
                frompostName = rs.getString("from_post");
                topostName = rs.getString("to_post");
                empName = rs.getString("emp_name");

            }
            String deptName = "";
            String postName = "";
            String wefDate = "";

            if (otype.equals("1") || otype.equals("2")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }

            if (otype.equals("4") || otype.equals("5") || otype.equals("6")) {
                deptName = fromOfficeName;
                postName = frompostName;
            }
            if (otype.equals("3")) {
                deptName = toOfficeName;
                postName = topostName;
                Rtype = "Transfer";
            }
            if (otype.equals("4")) {
                Rtype = "Retirement";
                postName = frompostName;
            }
            if (otype.equals("5")) {
                Rtype = "Death";
            }
            if (otype.equals("6")) {
                Rtype = "Dismissal";
            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("IN THE COURT OF SHRI S.K.Mohanty, IAS, DIRECTOR OF ESTATE, ODISHA, BHUBANESWAR", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("APPEAL CASE NO. " + appealcaseno + "(Q)", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            PdfPTable table1 = new PdfPTable(1);
            table1.setWidths(new int[]{5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("\nTo\n", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("    " + empName + " ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("    " + postName + " ", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("    " + deptName + " DEPARTMENT", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(datacell);
            document.add(table1);

            String msg1 = "    " + qaddress + " , " + unitqt + ", " + typeqt + " (area).  APPELLANT ";
            Paragraph point1 = new Paragraph(msg1, dataValFont);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);

            PdfPTable table2 = null;
            table2 = new PdfPTable(1);
            table2.setWidths(new int[]{5});
            table2.setWidthPercentage(100);

            PdfPCell cell2 = null;

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("VRS ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Rent Officerr/Estate Officer G.A.& P.G Department ", dataValFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", dataValFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("NOTICE ", dataValFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell2);

            document.add(table2);

            String msg2 = "\n  The aforementioned appeal case is fixed to Dt." + appeal_case_date + "."
                    + " for hearing on admission. You are hereby informed to appear in person or through authorized repressentatives on the date fixed.";
            Paragraph point2 = new Paragraph(msg2, pFont);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            Paragraph blank2 = new Paragraph("\n\n Estate Officer", bottomRule);
            blank2.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank2);

            Paragraph blank5 = new Paragraph("\n *.This is a computer-generated document. No signature is required.", bottomRule);
            blank5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(blank5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // DataBaseFunctions.closeSqlObjects(ps, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void HideRecord(EmpQuarterBean qbean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String qaddress = "";
        String unitqt = "";
        String typeqt = "";
        String bldgno = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE consumer_ga SET hide_show = ?"
                    + " WHERE CONSUMER_NO=? AND hrmsid=?");
            pst.setString(1, "Y");
            pst.setString(2, qbean.getConsumerNo());
            pst.setString(3, qbean.getEmpId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getqtrTypeList(String qrtrunit) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList qtrtypeList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct type_qt from consumer_ga where unit_qt=?  order by type_qt");
            pstmt.setString(1, qrtrunit);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("type_qt"));
                so.setLabel(rs.getString("type_qt"));
                qtrtypeList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return qtrtypeList;
    }

    @Override
    public ArrayList getbuildingList(String qrtrunit, String qrtrtype) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList buildingList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT distinct bldgno_qt FROM consumer_ga where unit_qt= ? and type_qt=? order by bldgno_qt");
            pstmt.setString(1, qrtrunit);
            pstmt.setString(2, qrtrtype);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("bldgno_qt"));
                so.setLabel(rs.getString("bldgno_qt"));
                buildingList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return buildingList;
    }

    @Override
    public List getoccupationVacationListqms(String vstatus, String loginName, Date fdate, Date tdate) {
        List quarterList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            con = dataSource.getConnection();
            if (vstatus != null && vstatus.equals("Vacation")) {
                pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[ocup_fname, ocup_lname], ' ') EMPNAME ,hrms_id,EMP_MAST.mobile,G_SPC.SPN, unit,qrtr_type,bldg_no,dt_vacation,insert_date,dt_vacation,vacation_entry_date,dt_order,status  FROM qmsv2.tbl_ocupants  a "
                        + " LEFT OUTER JOIN EMP_MAST ON a.hrms_id = EMP_MAST.EMP_ID "
                        + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status = 'V' AND (a.dt_vacation::date BETWEEN ? AND ? ) ORDER BY  dt_vacation DESC");

                pstmt.setTimestamp(1, new Timestamp(fdate.getTime()));
                pstmt.setTimestamp(2, new Timestamp(tdate.getTime()));
                 // System.out.println("from ==="+fdate);
                // System.out.println("to ==="+tdate);
                rs = pstmt.executeQuery();

                EmpQuarterBean eqBean = null;
                while (rs.next()) {

                    eqBean = new EmpQuarterBean();
                    //eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                    eqBean.setEmpId(rs.getString("hrms_id"));
                    eqBean.setQuarterNo(rs.getString("unit"));
                    eqBean.setQrtrtype(rs.getString("qrtr_type"));
                    eqBean.setQrtrunit(rs.getString("bldg_no"));
                    eqBean.setEmpName(rs.getString("EMPNAME"));
                    eqBean.setMobileno(rs.getString("mobile"));
                    eqBean.setDesignation(rs.getString("SPN"));
                    eqBean.setVacateStatus(rs.getString("status"));
                    if (rs.getString("dt_vacation") != null && !rs.getString("dt_vacation").equals("")) {
                        eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_vacation")));
                    } else {
                        eqBean.setDos("");
                    }

                    if (rs.getString("vacation_entry_date") != null && !rs.getString("vacation_entry_date").equals("")) {
                        eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("vacation_entry_date")));
                    } else {
                        eqBean.setOrderDate("");
                    }
                    quarterList.add(eqBean);

                }
            } else if (vstatus != null && vstatus.equals("Occupation")) {
                pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[ocup_fname, ocup_lname], ' ') EMPNAME ,hrms_id,EMP_MAST.mobile,G_SPC.SPN, unit,qrtr_type,bldg_no,dt_ocupation,insert_date,dt_order,status  FROM qmsv2.tbl_ocupants  a "
                        + " LEFT OUTER JOIN EMP_MAST ON a.hrms_id = EMP_MAST.EMP_ID "
                        + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE status = 'O' AND (a.dt_ocupation::date BETWEEN ? AND ? ) ORDER BY  dt_ocupation DESC");

                //pstmt.setString(1, vstatus);
                //pstmt.setString(2, fdate);
                //pstmt.setString(3, tdate);
                pstmt.setTimestamp(1, new Timestamp(fdate.getTime()));
                pstmt.setTimestamp(2, new Timestamp(tdate.getTime()));
                rs = pstmt.executeQuery();

                EmpQuarterBean eqBean = null;
                while (rs.next()) {

                    eqBean = new EmpQuarterBean();
                    //eqBean.setConsumerNo(rs.getString("CONSUMER_NO"));
                    eqBean.setEmpId(rs.getString("hrms_id"));
                    eqBean.setQuarterNo(rs.getString("unit"));
                    eqBean.setQrtrtype(rs.getString("qrtr_type"));
                    eqBean.setQrtrunit(rs.getString("bldg_no"));
                    eqBean.setEmpName(rs.getString("EMPNAME"));
                    eqBean.setMobileno(rs.getString("mobile"));
                    eqBean.setDesignation(rs.getString("SPN"));
                    eqBean.setVacateStatus(rs.getString("status"));
                    if (rs.getString("dt_ocupation") != null && !rs.getString("dt_ocupation").equals("")) {
                        eqBean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dt_ocupation")));
                    } else {
                        eqBean.setDos("");
                    }

                    if (rs.getString("insert_date") != null && !rs.getString("insert_date").equals("")) {
                        eqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("insert_date")));
                    } else {
                        eqBean.setOrderDate("");
                    }
                    quarterList.add(eqBean);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return quarterList;
    }

}

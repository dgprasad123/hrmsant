/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.AGPension;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.AGPension.SearchApplBean;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lenovo
 */
public class PensionNOCDAOImpl implements PensionNOCDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    private String uploadPath;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList PensionerNocList(String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select cur_off_code,spc,gpc,post_code,emp_mast.emp_id,dob,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn,noc_for,noc_request_from, "
                    + "extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,vigilance_noc_status,vigilance_org_file_name,vigilance_disk_file_name,"
                    + "vigilance_noc_reason,cbranch_noc_status,vigilance_noc_status,cb_disk_file_name,cb_org_file_name,cb_file_type,cb_file_path,cb_noc_reason  from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code LEFT OUTER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id  AND noc_for='PENSION'"
                    + " where cur_off_code IN ( SELECT OFF_CODE FROM G_OFFICE WHERE DDO_CODE in (SELECT ddo_code FROM g_office WHERE ddo_hrmsid=?) AND ONLINE_BILL_SUBMISSION='Y')  AND   dos <= date_trunc('month', now()) + interval '6 month'    order by dos";
            //and   dos > date_trunc('month', now())
// System.out.println(sql);
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, loginempid);

            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs1.getString("emp_id"));
                spbean.setGpfNo(rs1.getString("gpf_no"));
                spbean.setName(rs1.getString("EMPNAME"));
                spbean.setPost(rs1.getString("post"));
                spbean.setOffcode(rs1.getString("cur_off_code"));
                spbean.setNocfor(rs1.getString("noc_for"));
                spbean.setNocId(rs1.getInt("noc_id"));
                if (rs1.getString("vigilance_noc_status") == ("Y")) {
                    spbean.setvNocStatus("Accepted");

                } else {
                    spbean.setvNocStatus("Rejected");
                }
                //spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setDoj(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dos")));
                spbean.setVigilanceoriginalFilename(rs1.getString("vigilance_org_file_name"));
                spbean.setVigilancediskFileName(rs1.getString("vigilance_disk_file_name"));
                spbean.setVigilanceNocReason(rs1.getString("vigilance_noc_reason"));
                spbean.setCboriginalFilename(rs1.getString("cb_org_file_name"));
                spbean.setCbdiskFileName(rs1.getString("cb_disk_file_name"));
                spbean.setCbNocReason(rs1.getString("cb_noc_reason"));
                spbean.setvNocStatus(rs1.getString("vigilance_noc_status"));
                spbean.setcNocStatus(rs1.getString("cbranch_noc_status"));
                spbean.setNocRequest(rs1.getString("noc_request_from"));
                emplist.add(spbean);
            }
            pst1.close();
            rs1.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList NocList(String offcode, String loginId) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select  doe,spc,gpc,post_code,emp_mast.emp_id,dob,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn,noc_for,noc_request_from, "
                    + " extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,vigilance_noc_status,vigilance_org_file_name,vigilance_disk_file_name,"
                    + "vigilance_noc_reason,cbranch_noc_status,vigilance_noc_status,cb_disk_file_name,cb_org_file_name,cb_file_type,cb_file_path,cb_noc_reason  from emp_mast"
                    + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code "
                    //+ " where pension_noc.off_code=?   AND  (noc_for='PENSION' OR noc_for='PROMOTION') AND cadre_auth_spc IS NULL  order by doe DESC";
                    + " where pension_noc.off_code IN  (SELECT off_code FROM g_office WHERE ddo_hrmsid=?)  AND  (noc_for='PENSION' OR noc_for='PROMOTION') AND cadre_auth_spc IS NULL  order by doe DESC";
            System.out.println(sql);
            System.out.println("offcode" + offcode);
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, loginId);

            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs1.getString("emp_id"));
                spbean.setGpfNo(rs1.getString("gpf_no"));
                spbean.setName(rs1.getString("EMPNAME"));
                spbean.setPost(rs1.getString("post"));
                spbean.setOffcode(offcode);
                spbean.setNocfor(rs1.getString("noc_for"));
                spbean.setNocId(rs1.getInt("noc_id"));
                spbean.setNocRequest(rs1.getString("noc_request_from"));
                if (rs1.getString("vigilance_noc_status") == ("Y")) {
                    spbean.setvNocStatus("Accepted");

                } else {
                    spbean.setvNocStatus("Rejected");
                }
                //spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setDoj(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dob")));

                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dos")));
                spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs1.getDate("doe")));
                spbean.setVigilanceoriginalFilename(rs1.getString("vigilance_org_file_name"));
                spbean.setVigilancediskFileName(rs1.getString("vigilance_disk_file_name"));
                spbean.setVigilanceNocReason(rs1.getString("vigilance_noc_reason"));
                spbean.setCboriginalFilename(rs1.getString("cb_org_file_name"));
                spbean.setCbdiskFileName(rs1.getString("cb_disk_file_name"));
                spbean.setCbNocReason(rs1.getString("cb_noc_reason"));
                spbean.setvNocStatus(rs1.getString("vigilance_noc_status"));
                spbean.setcNocStatus(rs1.getString("cbranch_noc_status"));

                emplist.add(spbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getEmployeeListForPension(String loginempid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplistforpension = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_mast.emp_id,cur_off_code,cur_spc,POST,noc_for,noc_request_from,doe from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER  JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id AND noc_for='PROMOTION' "
                    + "where cur_off_code in (SELECT off_code FROM g_office WHERE ddo_hrmsid=?) and (dep_code='02' or dep_code='05')  ORDER BY EMPNAME ASC, noc_id DESC ");
            // System.out.println("offcode="+offCode);
            pstmt.setString(1, loginempid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PensionNOCBean pensionNOCBean = new PensionNOCBean();
                pensionNOCBean.setAppraiseeName(rs.getString("EMPNAME"));
                pensionNOCBean.setAppraiseeId(rs.getString("emp_id"));
                pensionNOCBean.setAppraiseeSpc(rs.getString("cur_spc"));
                pensionNOCBean.setAppraiseePost(rs.getString("POST"));
                pensionNOCBean.setNocfor(rs.getString("noc_for"));
                pensionNOCBean.setOffcode(rs.getString("cur_off_code"));
                pensionNOCBean.setNocRequest(rs.getString("noc_request_from"));
                int daysBetween = 0;

                if (rs.getString("doe") != null && !rs.getString("doe").equals("")) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String doe1 = rs.getString("doe");
                    Date doe = formatter.parse(doe1);
                    Date curdate = new Date();
                    String curdate1 = formatter.format(curdate);
                    curdate = formatter.parse(curdate1);
                    long difference = curdate.getTime() - doe.getTime();
                    daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
                    if (daysBetween > 180) {
                        daysBetween = 1;
                    } else {
                        daysBetween = 0;
                    }
                }
                pensionNOCBean.setNoofdays(daysBetween);

                emplistforpension.add(pensionNOCBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplistforpension;
    }

    @Override
    public List getEmployeeListForPensionfromOtherOffice(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplistforpension = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_mast.emp_id,cur_off_code,cur_spc,POST,noc_for,noc_request_from,doe from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER  JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id AND noc_for='PROMOTION' "
                    + "where cur_off_code=? and (dep_code='02' or dep_code='05')  ORDER BY EMPNAME ASC, noc_id DESC");
            // System.out.println("offcode="+offCode);
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PensionNOCBean pensionNOCBean = new PensionNOCBean();
                pensionNOCBean.setAppraiseeName(rs.getString("EMPNAME"));
                pensionNOCBean.setAppraiseeId(rs.getString("emp_id"));
                pensionNOCBean.setAppraiseeSpc(rs.getString("cur_spc"));
                pensionNOCBean.setAppraiseePost(rs.getString("POST"));
                pensionNOCBean.setNocfor(rs.getString("noc_for"));
                pensionNOCBean.setOffcode(rs.getString("cur_off_code"));
                pensionNOCBean.setNocRequest(rs.getString("noc_request_from"));
                int daysBetween = 0;

                if (rs.getString("doe") != null && !rs.getString("doe").equals("")) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String doe1 = rs.getString("doe");
                    Date doe = formatter.parse(doe1);
                    Date curdate = new Date();
                    String curdate1 = formatter.format(curdate);
                    curdate = formatter.parse(curdate1);
                    long difference = curdate.getTime() - doe.getTime();
                    daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
                    if (daysBetween > 180) {
                        daysBetween = 1;
                    } else {
                        daysBetween = 0;
                    }
                }
                pensionNOCBean.setNoofdays(daysBetween);
                emplistforpension.add(pensionNOCBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplistforpension;
    }

    @Override
    public int saveEmployeeListForPension(PensionNOCBean pensionNOCBean, String loginId) {
        PreparedStatement pst = null;
        Connection con = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        int nocID = 0;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO pension_noc(emp_id,off_code,doe,noc_for,noc_request_from,login_id) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, pensionNOCBean.getAppraiseeId());
            pst.setString(2, pensionNOCBean.getOffcode());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(4, pensionNOCBean.getNocfor());
            pst.setString(5, pensionNOCBean.getNocRequest());
            pst.setString(6, loginId);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            nocID = rs.getInt("noc_id");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nocID;
    }

    @Override
    public void requestForNoc(PensionNOCBean pensionNOCBean, String loginId) {
        Connection con = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("INSERT INTO pension_noc(emp_id,off_code,doe,noc_for,noc_request_from,login_id)VALUES(?,?,?,?,?,?)");
            ps.setString(1, pensionNOCBean.getHrmsid());
            ps.setString(2, pensionNOCBean.getOffcode());
            ps.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            ps.setString(4, "PENSION");
            ps.setString(5, pensionNOCBean.getNocRequest());
            ps.setString(6, loginId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void savePensionDetails(PensionNOCBean pensionNOCBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        Long curtime = new Date().getTime();

        String vigilancediskfileName = null;
        PreparedStatement pst = null;
        String vigilanceoriginalFileName = null;
        String vigilancecontentType = null;
        //PensionNOCBean pensionNOCBean = new PensionNOCBean();
        try {
            if (pensionNOCBean.getUploadDocumentvigilance() != null && !pensionNOCBean.getUploadDocumentvigilance().isEmpty()) {
                vigilancediskfileName = new Date().getTime() + "";
                // System.out.println("this.uploadPath" + this.uploadPath);
                vigilanceoriginalFileName = pensionNOCBean.getUploadDocumentvigilance().getOriginalFilename();
                vigilancecontentType = pensionNOCBean.getUploadDocumentvigilance().getContentType();
                byte[] bytes = pensionNOCBean.getUploadDocumentvigilance().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + vigilancediskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
           // System.out.println("pensionNOCBean.getNocId()" + pensionNOCBean.getNocId());

            // System.out.println("pensionNOCBean.getAuthorityType()" + pensionNOCBean.getAuthorityType());
            if (pensionNOCBean.getAuthorityType().equals("vigilanceadmin")) {
                ps = con.prepareStatement("update pension_noc set vigilance_disk_file_name = ?,vigilance_org_file_name = ?, vigilance_file_type = ?,vigilance_file_path=?,vigilance_noc_reason=?,vigilance_noc_status='Y',vigilance_doe=? where noc_id = ?");
                ps.setString(1, vigilancediskfileName);
                ps.setString(2, vigilanceoriginalFileName);
                ps.setString(3, vigilancecontentType);
                ps.setString(4, this.uploadPath);
                ps.setString(5, pensionNOCBean.getVigilanceNocReason());
                ps.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setInt(7, pensionNOCBean.getNocId());
                ps.executeUpdate();
            } else if (pensionNOCBean.getAuthorityType().equals("cbadmin")) {
                ps = con.prepareStatement("update pension_noc set cb_disk_file_name = ?,cb_org_file_name = ?, cb_file_type = ?,cb_file_path=?,cb_noc_reason=?,cbranch_noc_status='Y',cb_doe=? where noc_id = ?");
                ps.setString(1, vigilancediskfileName);
                ps.setString(2, vigilanceoriginalFileName);
                ps.setString(3, vigilancecontentType);
                ps.setString(4, this.uploadPath);
                ps.setString(5, pensionNOCBean.getVigilanceNocReason());
                ps.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps.setInt(7, pensionNOCBean.getNocId());

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
    public ArrayList VigilancePensionerNocList(String NocStatus) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,gpc,dob,post_code,dept_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                    + " WHERE vigilance_noc_status=? AND (noc_request_from='Both' OR noc_request_from='Vigilance' ) order by doe DESC";
            pst = con.prepareStatement(sql);
            pst.setString(1, NocStatus);

            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("EMPNAME"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(rs.getInt("noc_id"));
                spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                spbean.setNocfor(rs.getString("noc_for"));
                spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setDepartmentName(getDeptName(rs.getString("dept_code")));
                // System.out.println("rs.getString(\"dept_code            \")"+rs.getString("dept_code"));
                String empId = rs.getString("emp_id");
                //Now get the father/husband name of the employee
                String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                pst1 = con.prepareStatement(sql1);
                pst1.setString(1, empId);
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    spbean.setRelation(rs1.getString("relation"));
                    spbean.setFatherName(rs1.getString("FNAME"));
                }

                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    /////////////////////
    @Override
    public ArrayList SearchVigilancePensionerNocList(String NocStatus, SearchApplBean sab) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String searchBy = sab.getSearchBy();
            searchBy = searchBy.trim();
            System.out.println("searchBy:" + searchBy);
            String keyword = sab.getKeyword();
            System.out.println("Keyword:" + keyword);
            String postName = sab.getPostcode();
            System.out.println("postName:" + postName);
            String dob = sab.getDob();
            System.out.println("Date of Birth:" + dob);

            String date_from = sab.getDate_from();
            System.out.println("date_from:" + date_from);

            String date_to = sab.getDate_to();
            System.out.println("date_to:" + date_to);

            String sql = null;
            /*String postcode = sab.getPostcode();
             String date_from = sab.getDate_from();
             String date_to = sab.getDate_to();
             String dob = sab.getDob();
             String dept_name = sab.getDept_name();*/

            if (searchBy.equals("hrmsid")) {
                sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                        + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                        + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                        + " WHERE vigilance_noc_status='" + NocStatus + "' and emp_mast.emp_id = '" + keyword + "'"
                        + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
                        + " ORDER BY doe DESC limit 200";
                System.out.println("HRMSsqlhrmsid:" + sql);
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    spbean = new PensionNOCBean();
                    spbean.setHrmsid(rs.getString("emp_id"));
                    spbean.setGpfNo(rs.getString("gpf_no"));
                    spbean.setName(rs.getString("EMPNAME"));
                    spbean.setPost(rs.getString("post"));
                    spbean.setNocId(rs.getInt("noc_id"));
                    spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                    spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                    spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                    spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                    spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                    spbean.setNocfor(rs.getString("noc_for"));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setDepartmentName(getDeptName(rs.getString("dep_code")));
                    String empId = rs.getString("emp_id");
                    //Now get the father/husband name of the employee
                    String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, empId);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        spbean.setRelation(rs1.getString("relation"));
                        spbean.setFatherName(rs1.getString("FNAME"));
                    }

                    emplist.add(spbean);
                }
            }
            //Search by post
            if (searchBy.equals("post")) {
                sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                        + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                        + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                        + " WHERE vigilance_noc_status='" + NocStatus + "' and spn LIKE '%" + postName + "%'"
                        + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
                        + " ORDER BY doe DESC limit 200";
                System.out.println("HRMSsqlpost:" + sql);
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    spbean = new PensionNOCBean();
                    spbean.setHrmsid(rs.getString("emp_id"));
                    spbean.setGpfNo(rs.getString("gpf_no"));
                    spbean.setName(rs.getString("EMPNAME"));
                    spbean.setPost(rs.getString("post"));
                    spbean.setNocId(rs.getInt("noc_id"));
                    spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                    spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                    spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                    spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                    spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                    spbean.setNocfor(rs.getString("noc_for"));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setDepartmentName(getDeptName(rs.getString("dep_code")));
                    String empId = rs.getString("emp_id");
                    //Now get the father/husband name of the employee
                    String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, empId);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        spbean.setRelation(rs1.getString("relation"));
                        spbean.setFatherName(rs1.getString("FNAME"));
                    }

                    emplist.add(spbean);
                }
            }

            //Search by Name
            if (searchBy.equals("name")) {
                sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                        + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                        + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                        + " WHERE vigilance_noc_status='" + NocStatus + "' and (F_NAME LIKE '%" + keyword + "%' OR M_NAME LIKE '%" + keyword + "%' OR L_NAME LIKE '%" + keyword + "%')"
                        + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
                        + " ORDER BY doe DESC limit 200";
                System.out.println("HRMSsqlempName:" + sql);
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    spbean = new PensionNOCBean();
                    spbean.setHrmsid(rs.getString("emp_id"));
                    spbean.setGpfNo(rs.getString("gpf_no"));
                    spbean.setName(rs.getString("EMPNAME"));
                    spbean.setPost(rs.getString("post"));
                    spbean.setNocId(rs.getInt("noc_id"));
                    spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                    spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                    spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                    spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                    spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                    spbean.setNocfor(rs.getString("noc_for"));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setDepartmentName(getDeptName(rs.getString("dep_code")));
                    String empId = rs.getString("emp_id");
                    //Now get the father/husband name of the employee
                    String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, empId);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        spbean.setRelation(rs1.getString("relation"));
                        spbean.setFatherName(rs1.getString("FNAME"));
                    }

                    emplist.add(spbean);
                }
            }
            //Search by Date of Birth
            if (searchBy.equals("dob")) {
                sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                        + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                        + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                        + " WHERE vigilance_noc_status='" + NocStatus + "' and dob = '" + dob + "'"
                        + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
                        + " ORDER BY doe DESC limit 200";
                System.out.println("HRMSsqlDOB:" + sql);
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    spbean = new PensionNOCBean();
                    spbean.setHrmsid(rs.getString("emp_id"));
                    spbean.setGpfNo(rs.getString("gpf_no"));
                    spbean.setName(rs.getString("EMPNAME"));
                    spbean.setPost(rs.getString("post"));
                    spbean.setNocId(rs.getInt("noc_id"));
                    spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                    spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                    spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                    spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                    spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                    spbean.setNocfor(rs.getString("noc_for"));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setDepartmentName(getDeptName(rs.getString("dep_code")));
                    String empId = rs.getString("emp_id");
                    //Now get the father/husband name of the employee
                    String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, empId);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        spbean.setRelation(rs1.getString("relation"));
                        spbean.setFatherName(rs1.getString("FNAME"));
                    }

                    emplist.add(spbean);
                }
            }

            //Search by Date Range(Date of application)
            if (searchBy.equals("dtrange")) {
                sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
                        + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                        + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                        + " WHERE vigilance_noc_status='" + NocStatus + "' and doe BETWEEN  '" + date_from + "' AND '" + date_to + "'"
                        + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
                        + " ORDER BY doe DESC limit 200";
                System.out.println("HRMSsqlDate Range:" + sql);
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    spbean = new PensionNOCBean();
                    spbean.setHrmsid(rs.getString("emp_id"));
                    spbean.setGpfNo(rs.getString("gpf_no"));
                    spbean.setName(rs.getString("EMPNAME"));
                    spbean.setPost(rs.getString("post"));
                    spbean.setNocId(rs.getInt("noc_id"));
                    spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                    spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                    spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                    spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                    spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                    spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                    spbean.setNocfor(rs.getString("noc_for"));
                    spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                    spbean.setDepartmentName(getDeptName(rs.getString("dep_code")));
                    String empId = rs.getString("emp_id");
                    //Now get the father/husband name of the employee
                    String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, empId);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        spbean.setRelation(rs1.getString("relation"));
                        spbean.setFatherName(rs1.getString("FNAME"));
                    }

                    emplist.add(spbean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        /*String sql = "SELECT spc,gpc,dob,post_code,dep_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast "
         + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
         + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
         + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
         + " WHERE vigilance_noc_status=?"
         + " AND (noc_request_from='Both' OR noc_request_from='Vigilance' )"
         + " ORDER BY doe DESC limit 200";*/
        return emplist;
    }
    /////////////////////

    @Override
    public List getPostList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList postlist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select DISTINCT post from hrmis2.emp_mast  left outer join hrmis2.g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN hrmis2.g_post ON g_spc.gpc=g_post.post_code INNER JOIN hrmis2.pension_noc ON emp_mast.emp_id=pension_noc.emp_id WHERE (noc_request_from='Both' OR noc_request_from='Vigilance') order by POST";
            //System.out.println("getPostList sql:" + sql);
            pst = con.prepareStatement(sql);
            //pst.setString(1, NocStatus);

            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setPost(rs.getString("post"));
                postlist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return postlist;
    }

    public String getDeptName(String deptId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String deptName = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM g_department WHERE department_code = ?");
            ps.setString(1, deptId);

            rs = ps.executeQuery();
            while (rs.next()) {
                deptName = rs.getString("department_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return deptName;
    }

    @Override
    public ArrayList CBPensionerNocList(String NocStatus) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select dept_code,spc,gpc,dob,post_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,cbranch_noc_status,cb_disk_file_name,cb_org_file_name,cb_noc_reason,noc_for,cbranch_noc_status  from emp_mast "
                    + " LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code"
                    + " INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                    + " WHERE cbranch_noc_status=? AND (noc_request_from='Both' OR noc_request_from='CB' ) order by doe DESC";
            //   System.out.println("sql**********************:" + sql);
            //  System.out.println("NocStatus**********************:" + NocStatus);
            pst = con.prepareStatement(sql);
            pst.setString(1, NocStatus);
            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("EMPNAME"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(rs.getInt("noc_id"));
                spbean.setcNocStatus(rs.getString("cbranch_noc_status"));
                //spbean.setcNocFileName(rs.getString("cbranch_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setCboriginalFilename(rs.getString("cb_org_file_name"));
                spbean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                spbean.setCbNocReason(rs.getString("cb_noc_reason"));
                spbean.setNocfor(rs.getString("noc_for"));
                spbean.setcNocStatus(rs.getString("cbranch_noc_status"));
                spbean.setDoe(rs.getString("doe"));
                spbean.setDepartmentName(getDeptName(rs.getString("dept_code")));
                //Now get the father name from emp_relation table
                String empId = rs.getString("emp_id");
                String sql1 = "SELECT relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') RNAME FROM emp_relation WHERE emp_id = ? and (relation = 'HUSBAND' OR relation = 'FATHER') ";
                pst1 = con.prepareStatement(sql1);
                pst1.setString(1, empId);
                //  System.out.println("empId===="+empId);
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    spbean.setFatherName(rs1.getString("RNAME"));
                    spbean.setRelation(rs1.getString("relation"));
                }
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList VigilanceNocUpload(int nocId, String hrmsid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT vigilance_noc_status,vigilance_noc_reason,vigilance_noc_file_name,A.emp_id,department_name,A.mobile, C.POST, E.off_en,E.ddo_code,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname, A.gpf_no,A.gp,acct_type ,A.dob,A.dos,H.address,ARRAY_TO_STRING(ARRAY[R.INITIALS, R.F_NAME, R.M_NAME,R.L_NAME], ' ') Rfullname FROM   EMP_MAST A  "
                    + " INNER JOIN pension_noc ON A.emp_id=pension_noc.emp_id  LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " LEFT outer join emp_address H on H.emp_id=A.emp_id AND address_type='PRESENT'  "
                    + " LEFT outer join emp_relation R on R.emp_id=A.emp_id AND relation='FATHER'   "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E  "
                    + " WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? AND noc_id=? LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, nocId);

            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setAddress(rs.getString("address"));
                spbean.setMobile(rs.getString("mobile"));
                spbean.setFatherName(rs.getString("Rfullname"));
                spbean.setDdoCode(rs.getString("ddo_code"));
                spbean.setOffname(rs.getString("off_en"));
                spbean.setDepartmentName(rs.getString("department_name"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("fullname"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(nocId);
                spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList CBNocUpload(int nocId, String hrmsid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT cbranch_noc_status,cbranch_noc_file_name,A.emp_id,department_name,A.mobile, C.POST, E.off_en,E.ddo_code,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname, A.gpf_no,A.gp,acct_type ,A.dob,A.dos,H.address,ARRAY_TO_STRING(ARRAY[R.INITIALS, R.F_NAME, R.M_NAME,R.L_NAME], ' ') Rfullname FROM   EMP_MAST A  "
                    + " INNER JOIN pension_noc ON A.emp_id=pension_noc.emp_id  LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " LEFT outer join emp_address H on H.emp_id=A.emp_id AND address_type='PRESENT'  "
                    + " LEFT outer join emp_relation R on R.emp_id=A.emp_id AND relation='FATHER'   "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E  "
                    + " WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? AND noc_id=? ";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, nocId);

            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setAddress(rs.getString("address"));
                spbean.setMobile(rs.getString("mobile"));
                spbean.setFatherName(rs.getString("Rfullname"));
                spbean.setDdoCode(rs.getString("ddo_code"));
                spbean.setOffname(rs.getString("off_en"));
                spbean.setDepartmentName(rs.getString("department_name"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("fullname"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(nocId);
                spbean.setcNocStatus(rs.getString("cbranch_noc_status"));
                spbean.setcNocFileName(rs.getString("cbranch_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public PensionNOCBean getAttachedFileforNOC(int nocId) {
        PensionNOCBean pensionNOCBean = new PensionNOCBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select vigilance_org_file_name,vigilance_disk_file_name,vigilance_file_type,vigilance_file_path from pension_noc where noc_id = ?");
            pst.setInt(1, nocId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                pensionNOCBean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                pensionNOCBean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                pensionNOCBean.setGetvigilanceContentType("vigilance_file_type");
                filepath = rs.getString("vigilance_file_path");
            }
            File f = new File(filepath + File.separator + pensionNOCBean.getVigilancediskFileName());
            pensionNOCBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pensionNOCBean;
    }

    @Override
    public PensionNOCBean getAttachedFileforCB(int nocId) {
        PensionNOCBean pensionNOCBean = new PensionNOCBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select cb_org_file_name,cb_disk_file_name,cb_file_type,cb_file_path from pension_noc where noc_id = ?");
            pst.setInt(1, nocId);
            rs = pst.executeQuery();
            String filepath = null;

            if (rs.next()) {
                pensionNOCBean.setCboriginalFilename(rs.getString("cb_org_file_name"));
                pensionNOCBean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                pensionNOCBean.setGetvigilanceContentType("cb_file_type");
                filepath = rs.getString("cb_file_path");
            }
            File f = new File(filepath + File.separator + pensionNOCBean.getCbdiskFileName());
            pensionNOCBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return pensionNOCBean;
    }

   @Override
    public void GeneratevNoc(Document document, int nocId, String hrmsid, String logopath) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String fullname = "";
        String vdoe = "";
        String cbdoe = null;
        String gpfno = "";
        String dob = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            String sql = "SELECT vigilance_noc_status,vigilance_noc_reason,vigilance_noc_file_name,A.emp_id,department_name,A.mobile, C.POST, E.off_en,E.ddo_code,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname, A.gpf_no,A.gp,acct_type ,A.dob,A.dos,vigilance_doe,cb_doe FROM   EMP_MAST A  "
                    + " INNER JOIN pension_noc ON A.emp_id=pension_noc.emp_id  LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E  "
                    + " WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? AND noc_id=? AND vigilance_noc_status='Y' LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, nocId);

            rs = pst.executeQuery();
            if (rs.next()) {
                fullname = rs.getString("fullname");
                vdoe = CommonFunctions.getFormattedOutputDate1(rs.getDate("vigilance_doe"));
                cbdoe = CommonFunctions.getFormattedOutputDate1(rs.getDate("cb_doe"));
                gpfno = rs.getString("gpf_no");
                dob = CommonFunctions.getFormattedOutputDate1(rs.getDate("dob"));

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(2);
            //table.setWidths(new int[]{5});
            float[] columnWidths2 = new float[]{10f, 10f};
            table.setWidths(columnWidths2);
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            String url = logopath + "/odgovt.gif";
            File f = null;
            f = new File(url);
            Image img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("VIGILANCE NOC STATUS\n\n\n", hdrTextFont));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date:" + vdoe, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name:" + fullname, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GPF NO:" + gpfno, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("DOB:" + dob, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Case/File reference:NO Case\n\n", dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("It is here by declared that NOC is approved for the above employee as on date " + vdoe + ".", dataHdrFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);


            
            String str = "HRMS Id ="+ hrmsid + ",Employee Name=" + fullname + ",Date of Birth=" + dob + ",GPF NO=" + gpfno + ",Date=" + vdoe  ;
            BarcodeQRCode qrcode = new BarcodeQRCode(str, 150, 150, null);
            Image qrImage = qrcode.getImage();
            //document.add(qrImage);
            

            if (qrImage != null) {
             //qrImage.scalePercent(15f);
             cell = new PdfPCell(qrImage);
             cell.setColspan(2);
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             cell.setFixedHeight(150);
             table.addCell(cell);           

            }
            
                 cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);
            
            
             cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("This is a computer generated NOC by HRMS so no Signature is required.", dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            

            document.add(table);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void GenerateNoc(Document document, int nocId, String hrmsid, String logopath) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        String fullname = "";
        String vdoe = "";
        String cbdoe = null;
        String gpfno = "";
        String dob = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFontnew = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 9, Font.UNDERLINE, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            String sql = "SELECT vigilance_noc_status,vigilance_noc_reason,vigilance_noc_file_name,A.emp_id,department_name,A.mobile, C.POST, E.off_en,E.ddo_code,ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname, A.gpf_no,A.gp,acct_type ,A.dob,A.dos,vigilance_doe,cb_doe FROM   EMP_MAST A  "
                    + " INNER JOIN pension_noc ON A.emp_id=pension_noc.emp_id  LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E  "
                    + " WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? AND noc_id=? AND cbranch_noc_status='Y' LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, nocId);

            rs = pst.executeQuery();
            if (rs.next()) {
                fullname = rs.getString("fullname");
                vdoe = CommonFunctions.getFormattedOutputDate1(rs.getDate("vigilance_doe"));
                cbdoe = CommonFunctions.getFormattedOutputDate1(rs.getDate("cb_doe"));
                gpfno = rs.getString("gpf_no");
                dob = CommonFunctions.getFormattedOutputDate1(rs.getDate("dob"));

            }

            //System.out.println("otype="+otype+"  empName=="+empName);
            PdfPTable table = null;
            table = new PdfPTable(2);
            //table.setWidths(new int[]{5});
            float[] columnWidths2 = new float[]{10f, 10f};
            table.setWidths(columnWidths2);
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            String url = logopath + "/odgovt.gif";
            File f = null;
            f = new File(url);
            Image img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("CRIME BRANCH NOC STATUS\n\n\n", hdrTextFont));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date:" + cbdoe, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name:" + fullname, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GPF/PRAN No:" + gpfno, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("DOB:" + dob, dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Case/File reference:NO Case\n\n", dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("It is here by declared that NOC is approved for the above employee as on date " + cbdoe + ".", dataHdrFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);     

            cell = new PdfPCell(new Phrase("This is a computer generated NOC by HRMS so no Signature is required.", dataValFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

           // }
            /*PdfPTable table1 = new PdfPTable(2);
             table1.setWidths(new int[]{5, 5});
             table1.setWidthPercentage(100);
             PdfPCell datacell;*/
            //  Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt);
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList CadreNocList(String spc, String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,gpc,post_code,emp_mast.emp_id,dob,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn,noc_for,noc_request_from, "
                    + " extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,vigilance_noc_status,vigilance_org_file_name,vigilance_disk_file_name,"
                    + "vigilance_noc_reason,cbranch_noc_status,vigilance_noc_status,cb_disk_file_name,cb_org_file_name,cb_file_type,cb_file_path,cb_noc_reason  from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code LEFT OUTER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id"
                    + " where pension_noc.cadre_auth_spc=?    order by doe";
            //  System.out.println(sql);
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, spc);

            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs1.getString("emp_id"));
                spbean.setGpfNo(rs1.getString("gpf_no"));
                spbean.setName(rs1.getString("EMPNAME"));
                spbean.setPost(rs1.getString("post"));
                spbean.setOffcode(offcode);
                spbean.setNocfor(rs1.getString("noc_for"));
                spbean.setNocId(rs1.getInt("noc_id"));
                spbean.setNocRequest(rs1.getString("noc_request_from"));
                if (rs1.getString("vigilance_noc_status") == ("Y")) {
                    spbean.setvNocStatus("Accepted");

                } else {
                    spbean.setvNocStatus("Rejected");
                }
                //spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setDoj(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs1.getDate("dos")));
                spbean.setVigilanceoriginalFilename(rs1.getString("vigilance_org_file_name"));
                spbean.setVigilancediskFileName(rs1.getString("vigilance_disk_file_name"));
                spbean.setVigilanceNocReason(rs1.getString("vigilance_noc_reason"));
                spbean.setCboriginalFilename(rs1.getString("cb_org_file_name"));
                spbean.setCbdiskFileName(rs1.getString("cb_disk_file_name"));
                spbean.setCbNocReason(rs1.getString("cb_noc_reason"));
                spbean.setvNocStatus(rs1.getString("vigilance_noc_status"));
                spbean.setcNocStatus(rs1.getString("cbranch_noc_status"));
                emplist.add(spbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List cadreList(String spc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List cadrelist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select par_authority_admin.cadre_code,cadre_name,department_name from par_authority_admin  inner join g_cadre on par_authority_admin.cadre_code = g_cadre.cadre_code inner join g_department on g_cadre.department_code = g_department.department_code where spc=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setCadreValue(rs.getString("cadre_code"));
                String cadre_code_name = rs.getString("cadre_name") + "(" + rs.getString("department_name") + ")";
                spbean.setCadreName(cadre_code_name);
                cadrelist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadrelist;
    }

    @Override
    public List getCadreEmployeeListforPromotion(String cadreValue, String nocFor) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList emplistforpension = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,emp_mast.emp_id,cur_spc,POST,noc_for from emp_mast "
                    + "INNER JOIN G_SPC ON emp_mast.CUR_SPC = G_SPC.SPC "
                    + "INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER  JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id AND noc_for=? "
                    + "where cur_cadre_code=?    ORDER BY EMPNAME");
            //   System.out.println("offcode="+offCode);
            pstmt.setString(1, nocFor);
            pstmt.setString(2, cadreValue);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PensionNOCBean pensionNOCBean = new PensionNOCBean();
                pensionNOCBean.setAppraiseeName(rs.getString("EMPNAME"));
                pensionNOCBean.setAppraiseeId(rs.getString("emp_id"));
                pensionNOCBean.setAppraiseeSpc(rs.getString("cur_spc"));
                pensionNOCBean.setAppraiseePost(rs.getString("POST"));
                pensionNOCBean.setNocfor(rs.getString("noc_for"));

                emplistforpension.add(pensionNOCBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return emplistforpension;
    }

    @Override
    public int saveCadreEmployeeListForPension(PensionNOCBean pensionNOCBean, String loginId, String spc) {
        PreparedStatement pst = null;
        Connection con = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        int nocID = 0;
        try {
            Long curtime = new Date().getTime();
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO pension_noc(emp_id,off_code,doe,noc_for,noc_request_from,login_id,cadre_auth_spc) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, pensionNOCBean.getAppraiseeId());
            pst.setString(2, pensionNOCBean.getOffcode());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.setString(4, pensionNOCBean.getNocfor());
            pst.setString(5, pensionNOCBean.getNocRequest());
            pst.setString(6, loginId);
            pst.setString(7, spc);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            nocID = rs.getInt("noc_id");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nocID;
    }

    @Override
    public ArrayList VigilancePensionerNocListByDate(String NocStatus, String fdate, String tdate) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,gpc,dob,post_code,dept_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, \n"
                    + "extract (month from dos) super_month, acct_type, \n"
                    + "post_grp_type,post,noc_id,doe,vigilance_noc_status,vigilance_noc_file_name,vigilance_org_file_name,vigilance_disk_file_name,\n"
                    + "vigilance_noc_reason,noc_for,vigilance_noc_status  from emp_mast\n"
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code\n"
                    + "INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id\n"
                    + "WHERE vigilance_noc_status=? AND (noc_request_from='Both' OR noc_request_from='Vigilance' ) \n"
                    + "and doe between ? and ? order by doe DESC";
            pst = con.prepareStatement(sql);
            pst.setString(1, NocStatus);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(fdate).getTime()));
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(tdate).getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("EMPNAME"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(rs.getInt("noc_id"));
                spbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setvNocFileName(rs.getString("vigilance_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setVigilanceoriginalFilename(rs.getString("vigilance_org_file_name"));
                spbean.setVigilancediskFileName(rs.getString("vigilance_disk_file_name"));
                spbean.setVigilanceNocReason(rs.getString("vigilance_noc_reason"));
                spbean.setNocfor(rs.getString("noc_for"));
                spbean.setvNocStatus(rs.getString("vigilance_noc_status"));
                spbean.setDepartmentName(getDeptName(rs.getString("dept_code")));
                // System.out.println("rs.getString(\"dept_code            \")"+rs.getString("dept_code"));
                String empId = rs.getString("emp_id");
                //Now get the father/husband name of the employee
                String sql1 = "select relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FNAME from emp_relation where (relation = 'FATHER' OR relation = 'HUSBAND') AND emp_id = ?";
                pst1 = con.prepareStatement(sql1);
                pst1.setString(1, empId);
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    spbean.setRelation(rs1.getString("relation"));
                    spbean.setFatherName(rs1.getString("FNAME"));
                }
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList CBPensionerNocListByDate(String NocStatus, String fdate, String tdate) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ArrayList emplist = new ArrayList();
        PensionNOCBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            System.out.println("fromdate:" + fdate);

            String sql = "select dept_code,spc,gpc,dob,post_code,emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, \n"
                    + "extract (month from dos) super_month, acct_type, post_grp_type,post,noc_id,doe,cbranch_noc_status,cb_disk_file_name,cb_org_file_name,cb_noc_reason,noc_for,\n"
                    + "cbranch_noc_status  from emp_mast\n"
                    + "LEFT OUTER JOIN g_spc on emp_mast.cur_spc=g_spc.spc\n"
                    + "LEFT OUTER JOIN g_post ON g_spc.gpc=g_post.post_code\n"
                    + "INNER JOIN pension_noc ON emp_mast.emp_id=pension_noc.emp_id\n"
                    + "WHERE cbranch_noc_status=? AND (noc_request_from='Both' OR noc_request_from='CB' ) \n"
                    + "and doe between ? and ? order by doe DESC";
            //   System.out.println("sql**********************:" + sql);
            //  System.out.println("NocStatus**********************:" + NocStatus);
            pst = con.prepareStatement(sql);
            pst.setString(1, NocStatus);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(fdate).getTime()));
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(tdate).getTime()));

            //pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(tdate).getTime()));
            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new PensionNOCBean();
                spbean.setHrmsid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setName(rs.getString("EMPNAME"));
                spbean.setPost(rs.getString("post"));
                spbean.setNocId(rs.getInt("noc_id"));
                spbean.setcNocStatus(rs.getString("cbranch_noc_status"));
                //spbean.setcNocFileName(rs.getString("cbranch_noc_file_name"));
                spbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setCboriginalFilename(rs.getString("cb_org_file_name"));
                spbean.setCbdiskFileName(rs.getString("cb_disk_file_name"));
                spbean.setCbNocReason(rs.getString("cb_noc_reason"));
                spbean.setNocfor(rs.getString("noc_for"));
                spbean.setcNocStatus(rs.getString("cbranch_noc_status"));
                spbean.setDoe(rs.getString("doe"));
                spbean.setDepartmentName(getDeptName(rs.getString("dept_code")));
                //Now get the father name from emp_relation table
                String empId = rs.getString("emp_id");
                String sql1 = "SELECT relation, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') RNAME FROM emp_relation WHERE emp_id = ? and (relation = 'HUSBAND' OR relation = 'FATHER') ";
                pst1 = con.prepareStatement(sql1);
                pst1.setString(1, empId);
                //  System.out.println("empId===="+empId);
                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    spbean.setFatherName(rs1.getString("RNAME"));
                    spbean.setRelation(rs1.getString("relation"));
                }
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

}

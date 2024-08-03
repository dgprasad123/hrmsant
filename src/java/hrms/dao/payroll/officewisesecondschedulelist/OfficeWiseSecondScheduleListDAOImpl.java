package hrms.dao.payroll.officewisesecondschedulelist;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleForm;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class OfficeWiseSecondScheduleListDAOImpl implements OfficeWiseSecondScheduleListDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getEmployeeList(String offCode, String billGrpId, int rows, int page) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();

        OfficeWiseSecondScheduleListBean offBean = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,G_POST.POST,existing_pay_scale,EMP_PAY_REVISED_2016.GP,OPTION_CHOSEN FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON EMP_MAST.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + " LEFT OUTER JOIN PAY_REVISION_OPTION ON EMP_MAST.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + " WHERE EMP_MAST.CUR_OFF_CODE=? ORDER BY F_NAME ASC LIMIT ? OFFSET ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setInt(2, maxlimit);
            pst.setInt(3, minlimit);
            rs = pst.executeQuery();
            while (rs.next()) {
                offBean = new OfficeWiseSecondScheduleListBean();
                offBean.setEmpid(rs.getString("EMP_ID"));
                offBean.setEmpname(rs.getString("FULL_NAME"));
                if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                    offBean.setPost(rs.getString("POST"));
                } else {
                    offBean.setPost("");
                }
                offBean.setPayscale(rs.getString("existing_pay_scale"));
                offBean.setGp(rs.getInt("GP"));

                offBean.setOptionChosen(StringUtils.defaultString(rs.getString("OPTION_CHOSEN")));
                emplist.add(offBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public int getEmployeeListCount(String offCode, String billGrpId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int total = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT COUNT(*) CNT FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " LEFT OUTER JOIN PAY_REVISION_OPTION ON EMP_MAST.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + " WHERE EMP_MAST.CUR_OFF_CODE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt("CNT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    @Override
    public OfficeWiseSecondScheduleForm getSecondScheduleData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        OfficeWiseSecondScheduleForm oForm = new OfficeWiseSecondScheduleForm();

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,CUR_OFF_CODE,"
                    + " G_POST.POST POST1,EMP_PAY_REVISED_2016.POST POST2,EMP_PAY_REVISED_2016.GP,EMP_PAY_REVISED_2016.EXISTING_PAY_SCALE FROM EMP_MAST"
                    + " LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON EMP_MAST.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + " LEFT OUTER JOIN G_POST ON EMP_PAY_REVISED_2016.POST=G_POST.POST_CODE"
                    + " WHERE EMP_MAST.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                oForm.setEmpid(empid);
                oForm.setEmpname(rs.getString("FULL_NAME"));
                oForm.setOffcode(rs.getString("CUR_OFF_CODE"));

                String sql1 = "SELECT * FROM PAY_REVISION_OPTION WHERE EMP_ID=?";
                pst1 = con.prepareStatement(sql1);
                pst1.setString(1, empid);
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    oForm.setHidPostCode(rs1.getString("GPC"));
                    oForm.setDesignation(getPostNameFromPostCode(rs1.getString("GPC")));
                    oForm.setHidPayScale(rs1.getString("PAY_SCALE"));
                    oForm.setHidGP(rs1.getInt("GP"));
                    oForm.setRdOptionChosen(rs1.getString("option_chosen"));
                    oForm.setTxtDateEntered(CommonFunctions.getFormattedOutputDate1(rs1.getDate("entered_date")));
                } else {
                    oForm.setHidPostCode(rs.getString("POST2"));
                    oForm.setDesignation(rs.getString("POST1"));
                    oForm.setHidPayScale(rs.getString("EXISTING_PAY_SCALE"));
                    oForm.setHidGP(rs.getInt("GP"));
                    oForm.setRdOptionChosen("");
                    oForm.setTxtDateEntered("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return oForm;
    }

    @Override
    public Message saveSecondScheduleData(OfficeWiseSecondScheduleForm oForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Message msg = new Message();
        msg.setStatus("Success");

        boolean isDuplicate = false;
        String startTime = "";

        String spc = "";
        String postcode = "";
        String hasDDOChanged = "N";
        String postChanged = "N";
        try {
            con = this.dataSource.getConnection();

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat1.format(cal.getTime());

            postcode = oForm.getHidPostCode();

            String sql = "SELECT * FROM PAY_REVISION_OPTION WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, oForm.getEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                isDuplicate = true;
                if (rs.getString("GPC") != null && !rs.getString("GPC").equals("")) {
                    if (!rs.getString("GPC").equals(oForm.getHidPostCode())) {
                        hasDDOChanged = "Y";
                        spc = "";
                    }
                }
            } else {
                hasDDOChanged = "Y";
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);
            sql = "SELECT POST,existing_pay_scale,GP FROM EMP_PAY_REVISED_2016 WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, oForm.getEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                String temppostcode = rs.getString("POST");
                String tempspc = getSPCFromGPC(con, postcode);
                if (temppostcode != null && !temppostcode.equals("")) {
                    if (!temppostcode.equals(postcode)) {
                        hasDDOChanged = "Y";
                        postChanged = "Y";
                    } else {
                        spc = tempspc;
                    }
                }
                if (rs.getString("existing_pay_scale") != null && !rs.getString("existing_pay_scale").equals("")) {
                    if (!rs.getString("existing_pay_scale").equals(oForm.getPayscale())) {
                        hasDDOChanged = "Y";
                    }
                } else {
                    hasDDOChanged = "Y";
                }
                if (rs.getString("gp") != null && !rs.getString("gp").equals("")) {
                    if (!rs.getString("gp").equals(oForm.getGp())) {
                        hasDDOChanged = "Y";
                    }
                } else {
                    hasDDOChanged = "Y";
                }
            } else {
                hasDDOChanged = "Y";
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);
            if (isDuplicate == true) {
                sql = "UPDATE pay_revision_option SET option_chosen=?,pay_scale=?,GP=?,entered_date=?,off_code=?,spc=?,gpc=?,has_ddo_changed=?,date_of_submission=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, oForm.getRdOptionChosen());
                pst.setString(2, oForm.getPayscale());
                pst.setInt(3, oForm.getGp());

                if (oForm.getTxtDateEntered() != null && !oForm.getTxtDateEntered().equals("")) {
                    pst.setTimestamp(4, new Timestamp(dateFormat.parse(oForm.getTxtDateEntered()).getTime()));
                } else {
                    if (oForm.getRdOptionChosen() != null && oForm.getRdOptionChosen().equals("1")) {
                        pst.setTimestamp(4, new Timestamp(dateFormat.parse("01-JAN-2016").getTime()));
                    } else {
                        pst.setTimestamp(4, null);
                    }
                }

                pst.setString(5, oForm.getOffcode());
                pst.setString(6, spc);
                pst.setString(7, postcode);
                pst.setString(8, hasDDOChanged);
                pst.setTimestamp(9, new Timestamp(dateFormat1.parse(startTime).getTime()));
                pst.setString(10, oForm.getEmpid());
                pst.executeUpdate();
            } else {
                sql = "INSERT INTO pay_revision_option(emp_id,date_of_submission,option_chosen,pay_scale,GP,entered_date,off_code,spc,gpc,has_employee_changed,has_ddo_changed) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, oForm.getEmpid());
                pst.setTimestamp(2, new Timestamp(dateFormat1.parse(startTime).getTime()));
                pst.setString(3, oForm.getRdOptionChosen());
                pst.setString(4, oForm.getPayscale());
                pst.setInt(5, oForm.getGp());

                if (oForm.getTxtDateEntered() != null && !oForm.getTxtDateEntered().equals("")) {
                    pst.setTimestamp(6, new Timestamp(dateFormat.parse(oForm.getTxtDateEntered()).getTime()));
                } else {
                    if (oForm.getRdOptionChosen() != null && oForm.getRdOptionChosen().equals("1")) {
                        pst.setTimestamp(6, new Timestamp(dateFormat.parse("01-JAN-2016").getTime()));
                    } else {
                        pst.setTimestamp(6, null);
                    }
                }
                pst.setString(7, oForm.getOffcode());
                pst.setString(8, spc);
                pst.setString(9, postcode);
                pst.setString(10, "N");
                pst.setString(11, hasDDOChanged);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    private String getSPCFromGPC(Connection con, String gpc) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String spc = "";

        try {
            String sql = "SELECT SPC FROM G_SPC WHERE GPC=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                spc = rs.getString("SPC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return spc;
    }

    private String getPostNameFromPostCode(String gpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String postname = "";

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT POST FROM G_POST WHERE POST_CODE=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                postname = rs.getString("POST");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postname;
    }

    @Override
    public List getSectionList(String billGrpId) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList sectionList = new ArrayList();
        OfficeWiseSecondScheduleListBean oBean = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=?");
            pstmt.setDouble(1, Double.parseDouble(billGrpId));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                oBean = new OfficeWiseSecondScheduleListBean();
                oBean.setSectionId(rs.getString("SECTION_ID"));
                sectionList.add(oBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sectionList;
    }

    @Override
    public List getSectionWiseEmpList(String sectionId, int rows, int page) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();

        OfficeWiseSecondScheduleListBean oBean = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT EMP_MAST1.EMP_ID,EMP_MAST1.GPF_NO,ARRAY_TO_STRING(ARRAY[EMP_MAST1.INITIALS, EMP_MAST1.F_NAME, EMP_MAST1.M_NAME,EMP_MAST1.L_NAME], ' ') EMPNAME,PAY_REVISION_OPTION.PAY_SCALE,PAY_REVISION_OPTION.GP,G_POST.POST,OPTION_CHOSEN,is_submitted_revisioning_auth,entered_date,EMP_MAST1.doe_gov,ARRAY_TO_STRING(ARRAY[EMP_MAST2.INITIALS, EMP_MAST2.F_NAME, EMP_MAST2.M_NAME,EMP_MAST2.L_NAME], ' ') PFAUTHNAME,ARRAY_TO_STRING(ARRAY[EMP_MAST3.INITIALS, EMP_MAST3.F_NAME, EMP_MAST3.M_NAME,EMP_MAST3.L_NAME], ' ') CHKAUTHNAME,ARRAY_TO_STRING(ARRAY[EMP_MAST4.INITIALS, EMP_MAST4.F_NAME, EMP_MAST4.M_NAME,EMP_MAST4.L_NAME], ' ') VFAUTHNAME,is_submitted_revisioning_auth,is_submitted_checking_auth,is_submitted_verifying_auth FROM SECTION_POST_MAPPING"
                    + " INNER JOIN EMP_MAST EMP_MAST1 ON SECTION_POST_MAPPING.SPC=EMP_MAST1.CUR_SPC"
                    + " LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON EMP_MAST1.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST1.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " LEFT OUTER JOIN PAY_REVISION_OPTION ON EMP_MAST1.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + " LEFT OUTER JOIN EMP_MAST EMP_MAST2 ON PAY_REVISION_OPTION.REVISIONING_AUTH_EMP_ID=EMP_MAST2.EMP_ID"
                    + " LEFT OUTER JOIN EMP_MAST EMP_MAST3 ON PAY_REVISION_OPTION.CHECKING_AUTH_EMP_ID=EMP_MAST3.EMP_ID"
                    + " LEFT OUTER JOIN EMP_MAST EMP_MAST4 ON PAY_REVISION_OPTION.VERIFYING_AUTH_EMP_ID=EMP_MAST4.EMP_ID"
                    + " WHERE SECTION_POST_MAPPING.SECTION_ID=? ORDER BY EMP_MAST1.F_NAME,EMP_MAST1.L_NAME ASC LIMIT ? OFFSET ?");
            pst.setInt(1, Integer.parseInt(sectionId));
            pst.setInt(2, maxlimit);
            pst.setInt(3, minlimit);
            rs = pst.executeQuery();
            while (rs.next()) {
                oBean = new OfficeWiseSecondScheduleListBean();
                oBean.setEmpid(rs.getString("EMP_ID"));
                oBean.setEmpname(rs.getString("EMPNAME"));
                oBean.setGpfno(rs.getString("GPF_NO"));
                oBean.setPost(rs.getString("POST"));
                oBean.setPayscale(rs.getString("PAY_SCALE"));
                oBean.setGp(rs.getInt("GP"));
                if (rs.getString("OPTION_CHOSEN") != null && !rs.getString("OPTION_CHOSEN").equals("")) {
                    oBean.setOptionChosen(rs.getString("OPTION_CHOSEN"));
                } else {
                    oBean.setOptionChosen("");
                }
                if (rs.getString("is_submitted_revisioning_auth") != null && !rs.getString("is_submitted_revisioning_auth").equals("")) {
                    oBean.setSubmittedAuth(rs.getString("is_submitted_revisioning_auth"));
                } else {
                    oBean.setSubmittedAuth("");
                }
                if (rs.getString("OPTION_CHOSEN") != null && !rs.getString("OPTION_CHOSEN").equals("")) {
                    if (rs.getString("OPTION_CHOSEN").equals("2")) {
                        oBean.setEnteredDate(StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("entered_date"))));
                    } else if (rs.getString("OPTION_CHOSEN").equals("1")) {
                        oBean.setEnteredDate("01-JAN-2016");
                    }
                }
                if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                    Date doj = sdf.parse(rs.getString("doe_gov"));
                    if (doj.compareTo(payRevDt) > 0) {
                        oBean.setOptionChosen("2");
                        oBean.setEnteredDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                    }
                }

                if (rs.getString("is_submitted_verifying_auth") != null && rs.getString("is_submitted_verifying_auth").equals("Y")) {
                    oBean.setPendingAt("Verifying Authority");
                    oBean.setPendingAuthName(rs.getString("VFAUTHNAME"));
                } else if (rs.getString("is_submitted_checking_auth") != null && rs.getString("is_submitted_checking_auth").equals("Y")) {
                    oBean.setPendingAt("Checking Authority");
                    oBean.setPendingAuthName(rs.getString("CHKAUTHNAME"));
                } else if (rs.getString("is_submitted_revisioning_auth") != null && rs.getString("is_submitted_revisioning_auth").equals("Y")) {
                    oBean.setPendingAt("Pay Fixation Authority");
                    oBean.setPendingAuthName(rs.getString("PFAUTHNAME"));
                }

                emplist.add(oBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public int getSectionWiseEmpListCount(String sectionId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int total = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT COUNT(EMP_MAST.EMP_ID) CNT FROM SECTION_POST_MAPPING"
                    + " INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC"
                    + " WHERE SECTION_POST_MAPPING.SECTION_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(sectionId));
            rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt("CNT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    @Override
    public String[] getHeaderData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] data = new String[2];
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT date_of_submission,has_ddo_changed,has_employee_changed FROM PAY_REVISION_OPTION WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                data[0] = CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_submission"));
                if (rs.getString("has_ddo_changed") != null && rs.getString("has_ddo_changed").equals("Y")) {
                    data[1] = "DDO";
                } else if (rs.getString("has_employee_changed") != null && rs.getString("has_employee_changed").equals("Y")) {
                    data[1] = "EMPLOYEE";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return data;
    }

    @Override
    public Message saveRevisionAuthData(String chkEmp, String authEmp) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Message msg = new Message();
        msg.setStatus("Success");

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat1.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();

            String[] authEmpArr = authEmp.split("-");
            String authEmpId = authEmpArr[0];
            String authSpc = authEmpArr[1];

            String sql = "UPDATE PAY_REVISION_OPTION SET is_submitted_revisioning_auth=?,revisioning_auth_emp_id=?,revisioning_auth_spc=?,ddo_submitted_date=? WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);

            String[] chkEmpArr = chkEmp.split(",");
            for (int i = 0; i < chkEmpArr.length; i++) {
                verifyDOJ(chkEmpArr[i]);
                pst.setString(1, "Y");
                pst.setString(2, authEmpId);
                pst.setString(3, authSpc);
                pst.setTimestamp(4, new Timestamp(dateFormat1.parse(startTime).getTime()));
                pst.setString(5, chkEmpArr[i]);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    private void verifyDOJ(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            con = this.dataSource.getConnection();

            String sql = "SELECT DOE_GOV,CUR_SALARY,EMP_MAST.GP,CUR_SPC,GPC,CUR_OFF_CODE FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC WHERE EMP_ID=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("DOE_GOV") != null && !rs.getString("DOE_GOV").equals("")) {
                    Date doj = sdf.parse(rs.getString("DOE_GOV"));
                    if (doj.compareTo(payRevDt) > 0) {
                        insertPayRevisionDataForJoinigAfter7thPayDate(empid, rs.getDate("DOE_GOV"), rs.getString("CUR_OFF_CODE"), rs.getString("CUR_SPC"), rs.getString("GPC"), rs.getString("CUR_SALARY"), rs.getInt("GP"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    private void insertPayRevisionDataForJoinigAfter7thPayDate(String empid, Date doj, String offcode, String spc, String gpc, String payscale, int gp) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isDuplicate = false;
        String startTime = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat1.format(cal.getTime());

            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM PAY_REVISION_OPTION WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                isDuplicate = true;
            }
            if (isDuplicate == false) {
                sql = "INSERT INTO pay_revision_option(emp_id,date_of_submission,option_chosen,pay_scale,GP,entered_date,off_code,spc,gpc,has_employee_changed,has_ddo_changed) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                pst.setTimestamp(2, new Timestamp(dateFormat1.parse(startTime).getTime()));
                pst.setString(3, "2");
                pst.setString(4, payscale);
                pst.setInt(5, gp);
                pst.setTimestamp(6, new Timestamp(doj.getTime()));
                pst.setString(7, offcode);
                pst.setString(8, spc);
                pst.setString(9, gpc);
                pst.setString(10, "N");
                pst.setString(11, "Y");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

package hrms.dao.payroll.thirdschedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.model.payroll.thirdschedule.ThirdScheduleBean;
import hrms.model.payroll.thirdschedule.ThirdScheduleForm;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class ThirdScheduleDAOImpl implements ThirdScheduleDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getEmployeeList(String empid, int rows, int page) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        ThirdScheduleBean tbean = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT PAY_REVISION_OPTION.EMP_ID,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,G_POST.POST,CUR_SALARY,PAY_REVISION_OPTION.PAY_SCALE,PAY_REVISION_OPTION.GP,IS_APPROVED,is_submitted_checking_auth,checking_auth_emp_id,pay_revision_option.gpc,entry_gpc FROM PAY_REVISION_OPTION"
                    + " INNER JOIN EMP_MAST ON PAY_REVISION_OPTION.EMP_ID=EMP_MAST.EMP_ID"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE PAY_REVISION_OPTION.revisioning_auth_emp_id=? ORDER BY F_NAME,L_NAME ASC LIMIT ? OFFSET ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, maxlimit);
            pst.setInt(3, minlimit);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new ThirdScheduleBean();
                tbean.setEmpId(rs.getString("EMP_ID"));
                tbean.setGpfno(rs.getString("GPF_NO"));
                tbean.setEmpname(rs.getString("FULL_NAME"));
                if (rs.getString("gpc") != null && !rs.getString("gpc").equals("")) {
                    tbean.setDesignation(CommonFunctions.getPostName(con, rs.getString("gpc")));
                } else if (rs.getString("entry_gpc") != null && !rs.getString("entry_gpc").equals("")) {
                    tbean.setDesignation(CommonFunctions.getPostName(con, rs.getString("entry_gpc")));
                } else {
                    tbean.setDesignation(rs.getString("POST"));
                }
                tbean.setPayscale(rs.getString("PAY_SCALE"));
                tbean.setGp(rs.getInt("GP"));
                tbean.setIsApproved(StringUtils.defaultString(rs.getString("IS_APPROVED")));
                tbean.setIsCheckingAuthSubmitted(StringUtils.defaultString(rs.getString("is_submitted_checking_auth")));
                if (rs.getString("is_submitted_checking_auth") != null && rs.getString("is_submitted_checking_auth").equals("Y")) {
                    tbean.setChkAuthName(getCheckingAuthName(rs.getString("checking_auth_emp_id")));
                } else {
                    tbean.setChkAuthName("");
                }
                emplist.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public int getEmployeeListCount(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int total = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT count(PAY_REVISION_OPTION.EMP_ID) CNT FROM PAY_REVISION_OPTION"
                    + " INNER JOIN EMP_MAST ON PAY_REVISION_OPTION.EMP_ID=EMP_MAST.EMP_ID"
                    + " WHERE PAY_REVISION_OPTION.revisioning_auth_emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
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
    public void thirdSchedulePDF(Document document, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int fixedheight = 40;

        String incrDt = null;
        int revisedBasic = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            PdfPTable innertable = null;
            PdfPCell innercell = null;

            table = new PdfPTable(4);
            table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
            table.setWidthPercentage(80);

            innertable = new PdfPTable(2);
            innertable.setWidths(new float[]{0.05f, 0.5f});
            innertable.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("THIRD SCHEDULE", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Form for fixation of pay under the Odisha Revised Scales", getDesired_PDF_Font(13, true, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("of Pay Rules,2017", getDesired_PDF_Font(13, true, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(See rule-7)", getDesired_PDF_Font(12, false, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            String sql = "select EMP_ID,officiating, EMP_NAME,PAY_REVISION_OPTION.department_code,hoo_spc, g_post2.post hoo_post_name,cur_basic_salary,CUR_SALARY,CUR_GP,PAY_REVISION_OPTION.POST,option_chosen,cur_post_paymatrix_level,entry_gpc,entry_paymatrix_level,nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,"
                    + " entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,"
                    + " PAY_REVISION_OPTION.basic,ddoPayScale,ddoGP,revised_basic,da,is_approved,revision_pay_scale,"
                    + " revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,previous_pay_scale,previous_gp,g_post3.post entryPost,cur_spc,g_post4.post curPost,doe_gov,mon_basic from ("
                    + " SELECT PAY_REVISION_OPTION.EMP_ID,officiating,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,g_office.department_code,hoo_spc,cur_basic_salary,CUR_SALARY,EMP_MAST.GP CUR_GP,G_POST1.POST,option_chosen,cur_post_paymatrix_level,entry_gpc,entry_paymatrix_level,nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,PAY_REVISION_OPTION.basic,pay_scale ddoPayScale,PAY_REVISION_OPTION.gp ddoGP,revised_basic,da,is_approved,revision_pay_scale,revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,previous_pay_scale,previous_gp,cur_spc,doe_gov,mon_basic FROM EMP_MAST"
                    + "                    INNER JOIN PAY_REVISION_OPTION ON EMP_MAST.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + "                    LEFT OUTER JOIN g_office on EMP_MAST.cur_off_code=g_office.off_code"
                    + "                    LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON PAY_REVISION_OPTION.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + "                    LEFT OUTER JOIN G_POST G_POST1 ON PAY_REVISION_OPTION.GPC=G_POST1.POST_CODE"
                    + "                    WHERE PAY_REVISION_OPTION.EMP_ID=?) PAY_REVISION_OPTION"
                    + "                    left outer join g_post g_post2 on PAY_REVISION_OPTION.hoo_spc=g_post2.post_code"
                    + "                    left outer join g_post g_post3 on PAY_REVISION_OPTION.entry_gpc=g_post3.post_code"
                    + "                    left outer join g_spc on PAY_REVISION_OPTION.cur_spc=g_spc.spc"
                    + "                    left outer join g_post g_post4 on g_spc.gpc=g_post4.post_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {

                int revBasic = 0;
                int revDA = 0;

                if (rs.getInt("basic") > 0) {
                    revBasic = rs.getInt("basic");
                } else if (rs.getInt("mon_basic") > 0) {
                    revBasic = rs.getInt("mon_basic");
                }

                if (rs.getInt("prerev_emo_da") > 0) {
                    revDA = rs.getInt("prerev_emo_da");
                } else if (rs.getInt("da") > 0) {
                    revDA = rs.getInt("da");
                }
                int total = revBasic + rs.getInt("ddoGP") + revDA;

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("1.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Name of the Employee", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getString("EMP_NAME"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("2.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Name of the Head of Office (Designation Only)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("hoo_post_name")), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("3.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Post held by the employee (Substantive/Officiating)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getString("officiating"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String prevPayScale = "";
                String prevGp = "";
                if (rs.getString("previous_pay_scale") != null && !rs.getString("previous_pay_scale").equals("")) {
                    prevPayScale = rs.getString("previous_pay_scale");
                } else {
                    prevPayScale = rs.getString("ddoPayScale");
                }

                if (rs.getString("previous_gp") != null && !rs.getString("previous_gp").equals("")) {
                    prevGp = rs.getString("previous_gp");
                } else {
                    prevGp = rs.getString("ddoGP");
                }

                cell = new PdfPCell(new Phrase("4.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Pay Band and Grade Pay of the Post", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(prevPayScale) + "\n" + StringUtils.defaultString(prevGp), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("5.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Corresponding Level in the Pay Matrix of the Pay Band and Grade Pay of the present post", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("cur_post_paymatrix_level")), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("6.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Entry grade post and its corresponding Level in Pay Matrix", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("entryPost")) + "\n" + StringUtils.defaultString(rs.getString("entry_paymatrix_level")), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String revPayScale = "";
                String revGp = "";
                if (rs.getString("revision_pay_scale") != null && !rs.getString("revision_pay_scale").equals("")) {
                    revPayScale = rs.getString("revision_pay_scale");
                } else {
                    revPayScale = rs.getString("ddoPayScale");
                }

                if (rs.getString("revision_gp") != null && !rs.getString("revision_gp").equals("")) {
                    revGp = rs.getString("revision_gp");
                } else {
                    revGp = rs.getString("ddoGP");
                }

                cell = new PdfPCell(new Phrase("7.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Pay Band and Grade Pay in which pay is drawn (As per RACPS, if availed)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(revPayScale) + "\n" + StringUtils.defaultString(revGp), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("8.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Number of RACP availed", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("nos_racp_availed") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("9.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Number of Promotion availed", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("nos_promotion_availed") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("10.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Number of RACP availed before promotion", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("nos_racp_before_promotion") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("11.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Number of RACP availed after promotion", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("nos_racp_after_promotion") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("12.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Basic Pay (Pay + Grade Pay)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("revision_cur_basic") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("13.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay to be fixed in the Level of Pay Matrix (Attached to the post or as per MACPS entitlement)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("basicpay_fix_paymatrix") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String enteredDate = "";
                String isAfter = "N";

                if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                    Date doj = sdf.parse(rs.getString("doe_gov"));
                    if (doj.compareTo(payRevDt) > 0) {
                        isAfter = "Y";
                        enteredDate = (CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                    }
                }

                if (isAfter.equals("N")) {
                    if (rs.getString("option_chosen") != null && rs.getString("option_chosen").equals("1")) {
                        enteredDate = "01-JAN-2016";
                    } else {
                        enteredDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("entered_date"));
                    }
                }
                cell = new PdfPCell(new Phrase("14.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" Date from which option exercised to come over to revised pay structure", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(enteredDate, f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("", f1));
                cell.setColspan(4);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("", f1));
                cell.setColspan(4);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("15.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Emoulments in the existing Pay band and Grade pay on the date from which revised pay is opted.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innercell = new PdfPCell(new Phrase("(a)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Pay(including Personal Pay)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(revBasic + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(b)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Grade Pay", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("ddoGP") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(c)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("D.A as on " + enteredDate, f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(revDA + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(d)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Total emoulments (a to c)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(total + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("16.", f1));
                cell.setFixedHeight(60);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay fixed in the Revised Scale of pay structure by multiplying the existing basic pay (Cl.No.12) by a factor of 2.57 and rounded off to the nearest rupee.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("payrev_257_basicpay") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("17.", f1));
                cell.setFixedHeight(80);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("The Pay Cell in the appropriate Level in which the amount arrived at Sl. No. 16 is exactly fitted, if no such Cell exact to the amount is available then the next above Cell in that Level.\n or \n If the amount so arrived is less than the first Cell in the Level then the pay is fitted at the first Cell of the Level.(Cell No. and the amount of pay be mentioned)", f1));
                cell.setFixedHeight(120);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" Cell Number: " + rs.getInt("payrev_paycell") + "\n Appropriate Level: " + rs.getString("payrev_fitted_level") + "\n Fitted Amount: " + rs.getString("payrev_fitted_amount"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String pp = "";

                ArrayList revisedList = getNoOfIncrement(rs.getString("EMP_ID"));
                hrms.model.payroll.thirdschedule.ThirdScheduleBean tbean = null;
                if (revisedList != null && revisedList.size() > 0) {
                    Iterator incrItr = revisedList.iterator();
                    while (incrItr.hasNext()) {
                        tbean = (ThirdScheduleBean) incrItr.next();
                        if (tbean.getPp() > 0) {
                            pp = pp + "Personal Pay - Rs." + tbean.getPp() + "\n";
                        }
                    }
                    /*tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(0);
                     incrDt = tbean.getIncrDt();
                     revisedBasic = tbean.getRevisedbasic();*/
                }

                cell = new PdfPCell(new Phrase("18.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Date of next increment ", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_incr"))), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("19.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" Any other relevant information", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("other_info")) + "\n" + pp, f1));
                cell.setFixedHeight(120);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new float[]{1, 1.5f, 0.5f});
                table.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay in the Cell in the Level after Increment", getDesired_PDF_Font(12, false, true)));
                cell.setBorder(Rectangle.NO_BORDER);
                //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new int[]{1, 2, 2});
                table.setWidthPercentage(80);

                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setFixedHeight(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Date of Increment", f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Cell No. and Pay", f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Level", f1));
                cell.setFixedHeight(20);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new int[]{1, 2, 2});
                table.setWidthPercentage(80);
                tbean = null;
                for (int i = 0; i < revisedList.size(); i++) {
                    tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(i);

                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getIncrDt()), f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getCell() + "") + "-" + StringUtils.defaultString(tbean.getRevisedbasic() + ""), f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getLevel() + "") + "", f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
                document.add(table);
                // End Revised Table

                table = new PdfPTable(4);
                table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
                table.setWidthPercentage(80);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(60);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("  Date", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Signature & Designation of Head  of Office/Competent Authority", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("  Office: ", f1));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private Font getDesired_PDF_Font(int fontsize, boolean isBold, boolean isUnderline) throws Exception {
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

    private ArrayList getNoOfIncrement(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        hrms.model.payroll.thirdschedule.ThirdScheduleBean tbean = null;
        ArrayList revisedPayList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT INCR_DATE,REVISED_BASIC,LEVEL,CELL,PP FROM emp_pay_revised_increment_2016 WHERE EMP_ID=? ORDER BY INCR_DATE ASC");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new hrms.model.payroll.thirdschedule.ThirdScheduleBean();
                tbean.setIncrDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("INCR_DATE")));
                tbean.setRevisedbasic(rs.getInt("REVISED_BASIC"));
                tbean.setLevel(rs.getString("LEVEL"));
                tbean.setCell(rs.getInt("CELL"));
                tbean.setPp(rs.getInt("PP"));
                revisedPayList.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revisedPayList;
    }

    @Override
    public ThirdScheduleForm getThirdScheduleData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ThirdScheduleForm tform = new ThirdScheduleForm();

        DateFormat iasDtFormat = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            con = this.dataSource.getConnection();

            String sql = "select EMP_ID,officiating, EMP_NAME,PAY_REVISION_OPTION.department_code,g_post5.department_code g_dept_code,hoo_spc, g_post2.post hoo_post_name,"
                    + "cur_basic_salary,CUR_SALARY,CUR_GP,PAY_REVISION_OPTION.POST,option_chosen,cur_post_paymatrix_level,entry_paymatrix_level,"
                    + "nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,PAY_REVISION_OPTION.basic,ddoPayScale,ddoGP,revised_basic,da,is_approved,revision_pay_scale,"
                    + "revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,CUR_SPC,previous_pay_scale,previous_gp,entry_gpc,g_post3.post entryPost,cur_spc,g_post4.post curPost,doe_gov,g_post5.department_code,mon_basic,cur_cadre_code,junior_name,pay_substantive,pay_personal,np_allowance,cadre_type from (SELECT PAY_REVISION_OPTION.EMP_ID,officiating,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,g_office.department_code,hoo_spc,cur_basic_salary,CUR_SALARY,EMP_MAST.GP CUR_GP,G_POST1.POST,option_chosen,cur_post_paymatrix_level,entry_gpc,entry_paymatrix_level,nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,PAY_REVISION_OPTION.basic,pay_scale ddoPayScale,PAY_REVISION_OPTION.gp ddoGP,revised_basic,da,is_approved,revision_pay_scale,CUR_SPC,revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,previous_pay_scale,previous_gp,doe_gov,mon_basic,cur_cadre_code,junior_name,pay_substantive,pay_personal,np_allowance,other_eligibility cadre_type FROM EMP_MAST"
                    + "                    INNER JOIN PAY_REVISION_OPTION ON EMP_MAST.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + "                    LEFT OUTER JOIN g_office on EMP_MAST.cur_off_code=g_office.off_code"
                    + "                    LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON PAY_REVISION_OPTION.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + "                    LEFT OUTER JOIN G_POST g_post1 ON PAY_REVISION_OPTION.GPC=G_POST1.POST_CODE"
                    + "                    WHERE PAY_REVISION_OPTION.EMP_ID=?) PAY_REVISION_OPTION"
                    + "                    left outer join g_post g_post2 on PAY_REVISION_OPTION.hoo_spc=g_post2.post_code"
                    + "                    left outer join g_post g_post3 on PAY_REVISION_OPTION.entry_gpc=g_post3.post_code"
                    + "                    left outer join g_spc on PAY_REVISION_OPTION.cur_spc=g_spc.spc"
                    + "                    left outer join g_post g_post4 on g_spc.gpc=g_post4.post_code"
                    + "                    left outer join g_post g_post5 on PAY_REVISION_OPTION.entry_gpc=g_post5.post_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                tform.setEmpid(rs.getString("EMP_ID"));
                tform.setEmpname(rs.getString("EMP_NAME"));
                if (rs.getString("cur_cadre_code") != null && !rs.getString("cur_cadre_code").equals("")) {
                    tform.setIsIASCadre(verifyIASCadre(rs.getString("cur_cadre_code")));
                } else {
                    tform.setIsIASCadre("N");
                }
                if (rs.getString("cadre_type") != null && rs.getString("cadre_type").equals("UGC")) {
                    tform.setIsUGCCadre("Y");
                } else {
                    tform.setIsUGCCadre("N");
                }
                //tform.setPost(rs.getString("POST"));
                tform.setCurBasic(rs.getString("cur_basic_salary"));
                if (rs.getInt("revision_cur_basic") > 0) {
                    tform.setRevisionCurBasic(rs.getInt("revision_cur_basic") + "");
                } else {
                    tform.setRevisionCurBasic(rs.getInt("cur_basic_salary") + rs.getInt("CUR_GP") + "");
                }
                tform.setOfficiating(rs.getString("officiating"));

                if (rs.getString("previous_pay_scale") != null && !rs.getString("previous_pay_scale").equals("")) {
                    tform.setPreviousPayScale(rs.getString("previous_pay_scale"));
                } else {
                    tform.setPreviousPayScale(rs.getString("ddoPayScale"));
                }
                if (rs.getInt("previous_gp") > 0) {
                    tform.setPreviousGp(rs.getString("previous_gp"));
                } else {
                    tform.setPreviousGp(rs.getString("ddoGP"));
                }
                tform.setDeptCodeIAS(getDeptCodeFromGPC(rs.getString("hoo_spc")));
                tform.setEntryDeptCode(rs.getString("g_dept_code"));
                tform.setDeptCode(rs.getString("department_code"));
                tform.setHooSpc(rs.getString("hoo_spc"));
                tform.setHooPostName(rs.getString("hoo_post_name"));
                tform.setCurPayScale(rs.getString("CUR_SALARY"));
                tform.setCurGp(rs.getString("CUR_GP"));

                tform.setCurPost(rs.getString("curPost"));
                tform.setCurPostPaymatrixLevel(rs.getString("cur_post_paymatrix_level"));
                //tform.setEntrDeptCode(rs.getString("department_code"));
                tform.setEntryPost(rs.getString("entryPost"));
                tform.setEntryGpc(rs.getString("entry_gpc"));
                tform.setEntryPaymatrixLevel(rs.getString("entry_paymatrix_level"));

                if (rs.getString("revision_pay_scale") != null && !rs.getString("revision_pay_scale").equals("")) {
                    tform.setRevisionPayScale(rs.getString("revision_pay_scale"));
                } else {
                    tform.setRevisionPayScale(rs.getString("ddoPayScale"));
                }
                if (rs.getInt("revision_gp") > 0) {
                    tform.setRevisionGP(rs.getString("revision_gp"));
                } else {
                    tform.setRevisionGP(rs.getString("ddoGP"));
                }

                tform.setNosRacpAvailed(rs.getString("nos_racp_availed"));
                tform.setNosPromotionAvailed(rs.getString("nos_promotion_availed"));
                tform.setNosRacpBeforePromotion(rs.getString("nos_racp_before_promotion"));
                tform.setNosRacpAfterPromotion(rs.getString("nos_racp_after_promotion"));

                tform.setBasicpayFixPaymatrix(rs.getString("basicpay_fix_paymatrix"));

                String isAfter = "N";

                if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                    Date doj = sdf.parse(rs.getString("doe_gov"));
                    if (doj.compareTo(payRevDt) > 0) {
                        isAfter = "Y";
                        tform.setDateOptionExercised(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                    }
                }
                if (isAfter.equals("N")) {
                    if (rs.getString("option_chosen") != null && rs.getString("option_chosen").equals("1")) {
                        if (tform.getIsIASCadre() != null && tform.getIsIASCadre().equals("Y")) {
                            tform.setDateOptionExercised(iasDtFormat.format(payRevDt));
                        } else {
                            tform.setDateOptionExercised("01-JAN-2016");
                        }
                    } else {
                        if (tform.getIsIASCadre() != null && tform.getIsIASCadre().equals("Y")) {
                            tform.setDateOptionExercised(iasDtFormat.format(rs.getDate("entered_date")));
                        } else {
                            tform.setDateOptionExercised(CommonFunctions.getFormattedOutputDate1(rs.getDate("entered_date")));
                        }
                    }
                }
                int revBasic = 0;
                int revDA = 0;

                if (rs.getInt("basic") > 0) {
                    revBasic = rs.getInt("basic");
                } else if (rs.getInt("mon_basic") > 0) {
                    revBasic = rs.getInt("mon_basic");
                }
                tform.setBasic(revBasic + "");
                tform.setGp(rs.getString("ddoGP"));

                if (rs.getInt("prerev_emo_da") > 0) {
                    revDA = rs.getInt("prerev_emo_da");
                } else if (rs.getInt("da") > 0) {
                    revDA = rs.getInt("da");
                }

                tform.setDa(revDA + "");
                if (tform.getIsIASCadre() != null && tform.getIsIASCadre().equals("Y")) {
                    tform.setTotalpay(revBasic + revDA + "");
                } else {
                    tform.setTotalpay(revBasic + rs.getInt("ddoGP") + revDA + "");
                }

                if (rs.getString("payrev_257_basicpay") != null && !rs.getString("payrev_257_basicpay").equals("")) {
                    tform.setPayrev257Basicpay(rs.getString("payrev_257_basicpay"));
                } else {
                    tform.setPayrev257Basicpay(rs.getString("revised_basic"));
                }
                tform.setPayrevPaycell(rs.getString("payrev_paycell"));
                tform.setPayrevFittedLevel(rs.getString("payrev_fitted_level"));
                tform.setPayrevFittedAmount(rs.getString("payrev_fitted_amount"));
                tform.setDoeIncr(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_incr")));
                tform.setOtherInfo(rs.getString("other_info"));
                tform.setIsApproved(rs.getString("is_approved"));

                tform.setJuniorName(rs.getString("junior_name"));
                tform.setPaySubstantive(rs.getString("pay_substantive"));
                tform.setPayPersonal(rs.getString("pay_personal"));
                tform.setNpAllowance(rs.getString("np_allowance"));
            }

            tform.setIncrementList(getNoOfIncrement(empid));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tform;
    }

    @Override
    public Message saveThirdScheduleData(ThirdScheduleForm tform, String approve) {

        Connection con = null;

        PreparedStatement pst = null;

        Message msg = new Message();
        msg.setStatus("Success");

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        int total = 0;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (tform.getIsIASCadre() != null && tform.getIsIASCadre().equals("Y")) {
                sql = "UPDATE PAY_REVISION_OPTION SET hoo_spc=?,officiating=?,revision_pay_scale=?,revision_gp=?,basic=?,prerev_emo_da=?,prerev_emo_totalpay=?,revision_cur_basic=?,cur_post_paymatrix_level=?,payrev_257_basicpay=?,payrev_paycell=?,payrev_fitted_level=?,payrev_fitted_amount=?,junior_name=?,pay_substantive=?,pay_personal=?,np_allowance=?,doe_incr=?,other_info=?,is_approved=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, tform.getHooSpc());
                pst.setString(2, tform.getOfficiating());
                pst.setString(3, tform.getRevisionPayScale());
                if (tform.getRevisionGP() != null && !tform.getRevisionGP().equals("")) {
                    pst.setInt(4, Integer.parseInt(tform.getRevisionGP()));
                } else {
                    pst.setInt(4, 0);
                }
                if (tform.getBasic() != null && !tform.getBasic().equals("")) {
                    pst.setInt(5, Integer.parseInt(tform.getBasic()));
                } else {
                    pst.setInt(5, 0);
                }
                if (tform.getDa() != null && !tform.getDa().equals("")) {
                    pst.setInt(6, Integer.parseInt(tform.getDa()));
                } else {
                    pst.setInt(6, 0);
                }
                if (tform.getTotalpay() != null && !tform.getTotalpay().equals("")) {
                    pst.setInt(7, Integer.parseInt(tform.getTotalpay()));
                } else {
                    pst.setInt(7, 0);
                }
                if (tform.getRevisionCurBasic() != null && !tform.getRevisionCurBasic().equals("")) {
                    pst.setInt(8, Integer.parseInt(tform.getRevisionCurBasic()));
                } else {
                    pst.setInt(8, 0);
                }
                if (tform.getCurPostPaymatrixLevel() != null && !tform.getCurPostPaymatrixLevel().equals("")) {
                    pst.setString(9, tform.getCurPostPaymatrixLevel());
                } else {
                    pst.setString(9, null);
                }
                if (tform.getPayrev257Basicpay() != null && !tform.getPayrev257Basicpay().equals("")) {
                    pst.setInt(10, Integer.parseInt(tform.getPayrev257Basicpay()));
                } else {
                    pst.setInt(10, 0);
                }
                if (tform.getPayrevPaycell() != null && !tform.getPayrevPaycell().equals("")) {
                    pst.setInt(11, Integer.parseInt(tform.getPayrevPaycell()));
                } else {
                    pst.setInt(11, 0);
                }
                pst.setString(12, tform.getPayrevFittedLevel());
                pst.setString(13, tform.getPayrevFittedAmount());
                pst.setString(14, tform.getJuniorName());
                if (tform.getPaySubstantive() != null && !tform.getPaySubstantive().equals("")) {
                    pst.setInt(15, Integer.parseInt(tform.getPaySubstantive()));
                } else {
                    pst.setInt(15, 0);
                }
                if (tform.getPayPersonal() != null && !tform.getPayPersonal().equals("")) {
                    pst.setInt(16, Integer.parseInt(tform.getPayPersonal()));
                } else {
                    pst.setInt(16, 0);
                }
                if (tform.getNpAllowance() != null && !tform.getNpAllowance().equals("")) {
                    pst.setInt(17, Integer.parseInt(tform.getNpAllowance()));
                } else {
                    pst.setInt(17, 0);
                }
                if (tform.getDoeIncr() != null && !tform.getDoeIncr().equals("")) {
                    pst.setTimestamp(18, new Timestamp(dateFormat.parse(tform.getDoeIncr()).getTime()));
                } else {
                    pst.setTimestamp(18, null);
                }
                pst.setString(19, tform.getOtherInfo());
                pst.setString(20, approve);
                pst.setString(21, tform.getEmpid());
                pst.executeUpdate();
            } else {

                sql = "UPDATE PAY_REVISION_OPTION SET cur_post_paymatrix_level=?,entry_paymatrix_level=?,revision_pay_scale=?,revision_gp=?,nos_racp_availed=?,nos_promotion_availed=?, nos_racp_before_promotion=?,nos_racp_after_promotion=?,basicpay_fix_paymatrix=?,basic=?,gp=?,prerev_emo_da=?,prerev_emo_totalpay=?,payrev_257_basicpay=?,payrev_paycell=?,doe_incr=?,other_info=?,is_approved=?,revision_cur_basic=?,payrev_fitted_amount=?,payrev_fitted_level=?,officiating=?,hoo_spc=?,previous_pay_scale=?,previous_gp=?,entry_gpc=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);

                if (tform.getCurPostPaymatrixLevel() != null && !tform.getCurPostPaymatrixLevel().equals("")) {
                    pst.setString(1, tform.getCurPostPaymatrixLevel());
                } else {
                    pst.setString(1, null);
                }
                if (tform.getEntryPaymatrixLevel() != null && !tform.getEntryPaymatrixLevel().equals("")) {
                    pst.setInt(2, Integer.parseInt(tform.getEntryPaymatrixLevel()));
                } else {
                    pst.setInt(2, 0);
                }
                pst.setString(3, tform.getRevisionPayScale());
                if (tform.getRevisionGP() != null && !tform.getRevisionGP().equals("")) {
                    pst.setInt(4, Integer.parseInt(tform.getRevisionGP()));
                } else {
                    pst.setInt(4, 0);
                }
                if (tform.getNosRacpAvailed() != null && !tform.getNosRacpAvailed().equals("")) {
                    pst.setInt(5, Integer.parseInt(tform.getNosRacpAvailed()));
                } else {
                    pst.setInt(5, 0);
                }
                if (tform.getNosPromotionAvailed() != null && !tform.getNosPromotionAvailed().equals("")) {
                    pst.setInt(6, Integer.parseInt(tform.getNosPromotionAvailed()));
                } else {
                    pst.setInt(6, 0);
                }
                if (tform.getNosRacpBeforePromotion() != null && !tform.getNosRacpBeforePromotion().equals("")) {
                    pst.setInt(7, Integer.parseInt(tform.getNosRacpBeforePromotion()));
                } else {
                    pst.setInt(7, 0);
                }
                if (tform.getNosRacpAfterPromotion() != null && !tform.getNosRacpAfterPromotion().equals("")) {
                    pst.setInt(8, Integer.parseInt(tform.getNosRacpAfterPromotion()));
                } else {
                    pst.setInt(8, 0);
                }
                if (tform.getBasicpayFixPaymatrix() != null && !tform.getBasicpayFixPaymatrix().equals("")) {
                    pst.setInt(9, Integer.parseInt(tform.getBasicpayFixPaymatrix()));
                } else {
                    pst.setInt(9, 0);
                }
                if (tform.getBasic() != null && !tform.getBasic().equals("")) {
                    pst.setInt(10, Integer.parseInt(tform.getBasic()));
                } else {
                    pst.setInt(10, 0);
                }
                if (tform.getGp() != null && !tform.getGp().equals("")) {
                    pst.setInt(11, Integer.parseInt(tform.getGp()));
                } else {
                    pst.setInt(11, 0);
                }
                if (tform.getDa() != null && !tform.getDa().equals("")) {
                    pst.setInt(12, Integer.parseInt(tform.getDa()));
                } else {
                    pst.setInt(12, 0);
                }
                if (tform.getTotalpay() != null && !tform.getTotalpay().equals("")) {
                    pst.setInt(13, Integer.parseInt(tform.getTotalpay()));
                } else {
                    pst.setInt(13, 0);
                }
                if (tform.getPayrev257Basicpay() != null && !tform.getPayrev257Basicpay().equals("")) {
                    pst.setInt(14, Integer.parseInt(tform.getPayrev257Basicpay()));
                } else {
                    pst.setInt(14, 0);
                }
                if (tform.getPayrevPaycell() != null && !tform.getPayrevPaycell().equals("")) {
                    pst.setInt(15, Integer.parseInt(tform.getPayrevPaycell()));
                } else {
                    pst.setInt(15, 0);
                }
                if (tform.getDoeIncr() != null && !tform.getDoeIncr().equals("")) {
                    pst.setTimestamp(16, new Timestamp(dateFormat.parse(tform.getDoeIncr()).getTime()));
                } else {
                    pst.setTimestamp(16, null);
                }
                pst.setString(17, tform.getOtherInfo());
                pst.setString(18, approve);
                if (tform.getRevisionCurBasic() != null && !tform.getRevisionCurBasic().equals("")) {
                    pst.setInt(19, Integer.parseInt(tform.getRevisionCurBasic()));
                } else {
                    pst.setInt(19, 0);
                }
                pst.setString(20, tform.getPayrevFittedAmount());
                pst.setString(21, tform.getPayrevFittedLevel());
                pst.setString(22, tform.getOfficiating());
                pst.setString(23, tform.getHooSpc());
                pst.setString(24, tform.getPreviousPayScale());
                if (tform.getPreviousGp() != null && !tform.getPreviousGp().equals("")) {
                    pst.setInt(25, Integer.parseInt(tform.getPreviousGp()));
                } else {
                    pst.setInt(25, 0);
                }
                pst.setString(26, tform.getEntryGpc());
                pst.setString(27, tform.getEmpid());
                pst.executeUpdate();
            }
            if (tform.getRevisedbasic() != null) {
                pst = con.prepareStatement("DELETE FROM emp_pay_revised_increment_2016 WHERE EMP_ID=?");
                pst.setString(1, tform.getEmpid());
                pst.executeUpdate();

                sql = "insert into emp_pay_revised_increment_2016(emp_id,incr_date,REVISED_BASIC,level,cell,pp) values(?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);

                String[] incrDt = tform.getIncrDt();
                String[] revPay = tform.getRevisedbasic();
                String[] incrLevel = tform.getIncrLevel();
                String[] incrCell = tform.getIncrCell();
                String[] pp = tform.getPp();
                for (int i = 0; i < revPay.length; i++) {

                    if (revPay[i] != null && !revPay[i].equals("")) {
                        pst.setString(1, tform.getEmpid());
                        if (incrDt.length > 0 && incrDt[i] != null && !incrDt[i].equals("")) {
                            pst.setTimestamp(2, new Timestamp(dateFormat.parse(incrDt[i]).getTime()));
                        } else {
                            pst.setTimestamp(2, null);
                        }
                        if (revPay[i] != null && !revPay[i].equals("")) {
                            pst.setInt(3, Integer.parseInt(revPay[i]));
                        } else {
                            pst.setInt(3, 0);
                        }
                        if (incrLevel.length > 0 && incrLevel[i] != null && !incrLevel[i].equals("")) {
                            pst.setString(4, incrLevel[i]);
                        } else {
                            pst.setString(4, null);
                        }
                        if (incrCell.length > 0 && incrCell[i] != null && !incrCell[i].equals("")) {
                            pst.setInt(5, Integer.parseInt(incrCell[i]));
                        } else {
                            pst.setInt(5, 0);
                        }
                        if (pp != null && !pp.equals("") && pp.length > 0) {
                            if (pp[i] != null && !pp[i].equals("")) {
                                pst.setInt(6, Integer.parseInt(pp[i]));
                            } else {
                                pst.setInt(6, 0);
                            }
                        } else {
                            pst.setInt(6, 0);
                        }
                        pst.executeUpdate();
                    }
                }
            } else {
                pst = con.prepareStatement("DELETE FROM emp_pay_revised_increment_2016 WHERE EMP_ID=?");
                pst.setString(1, tform.getEmpid());
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

    @Override
    public Message saveCheckingAuthData(String chkEmp, String authEmp) {

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

            String sql = "UPDATE PAY_REVISION_OPTION SET is_submitted_checking_auth=?,checking_auth_emp_id=?,checking_auth_spc=?,pay_fixation_auth_submitted_date=? WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);

            String[] chkEmpArr = chkEmp.split(",");
            for (int i = 0; i < chkEmpArr.length; i++) {
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

    @Override
    public Message revertPayFixationAuthData(String chkEmp) {

        Connection con = null;

        PreparedStatement pst = null;

        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE PAY_REVISION_OPTION SET is_submitted_revisioning_auth=?,revisioning_auth_emp_id=?,revisioning_auth_spc=?,is_approved=? WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);

            String[] chkEmpArr = chkEmp.split(",");
            for (int i = 0; i < chkEmpArr.length; i++) {
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setString(4, null);
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

    @Override
    public void thirdScheduleIASPDF(Document document, ThirdScheduleForm tform) {

        int fixedheight = 40;

        try {
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            PdfPTable innertable = null;
            PdfPCell innercell = null;

            table = new PdfPTable(4);
            table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
            table.setWidthPercentage(80);

            innertable = new PdfPTable(2);
            innertable.setWidths(new float[]{0.05f, 0.5f});
            innertable.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("THIRD SCHEDULE", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Statement of fixation of pay under Central Civil Service (Revised Pay) Rules,2016", getDesired_PDF_Font(13, true, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name of the Employee", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getEmpname(), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Designation of the post in which pay is to be fixed as on January, 2016", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getHooPostName(), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Status(Substantive/Officiating)", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getOfficiating(), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("4.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pre-revised Pay Band and Grade Pay or Scale", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getRevisionPayScale()) + "\n" + StringUtils.defaultString(tform.getRevisionGP()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("5.", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Existing emoluments", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            innercell = new PdfPCell(new Phrase("(a)", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Basic Pay (Pay in the applicable Pay Band plus applicable Grade Pay or basic pay in the applicable scale) in the pre-revised structure as on " + tform.getDateOptionExercised(), f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            cell = new PdfPCell(innertable);
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getBasic(), f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            innertable = new PdfPTable(2);
            innertable.setWidths(new float[]{0.05f, 0.5f});
            innertable.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            innercell = new PdfPCell(new Phrase("(b)", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Dearness Allowance sanctioned w.e.f " + tform.getDateOptionExercised(), f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            cell = new PdfPCell(innertable);
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getDa()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            innertable = new PdfPTable(2);
            innertable.setWidths(new float[]{0.05f, 0.5f});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("(c)", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Existing Emoluments (a + b)", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            cell = new PdfPCell(innertable);
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getTotalpay(), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("6.", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Basic Pay (Pay in the applicable Pay Band plus applicable Grade Pay or basic pay in the applicable scale) in the pre-revised structure as on " + tform.getDateOptionExercised(), f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getRevisionCurBasic()), f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("7.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Applicable Level in the Pay Matrix corresponding to the Pay Band and Grade Pay or Scale shown at S.No. 4", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getCurPostPaymatrixLevel()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("8.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Amount arrived at by multiplying Sl.No.5 by 2.57", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getPayrev257Basicpay()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("9.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Applicable Cell in the Level either equal to or just above the Amount at Sl.No.8", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getPayrevPaycell() + "\n" + tform.getPayrevFittedLevel(), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("10.", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Revised Basic Pay (as to Sl.No.9)", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tform.getPayrevFittedAmount(), f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("11.", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Stepped up with reference to the revised Pay of Junior, if applicable [Rule 7(8) and 7(10) of CCS(RP) Rules,2016].Name and Pay of the Junior also to be indicated distinctly.", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getJuniorName()), f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("12.", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Revised Pay with reference to the Substantive Pay in cases where the Pay fixed in the Officiating Post is lower than the Pay fixed in the Substantive Post if applicable [Rule 7(11)]", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getPaySubstantive()), f1));
            cell.setFixedHeight(60);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("13.", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Personal Pay, if any [Rule 7(7) and 7(8)]", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getPayPersonal()), f1));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("14.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Non-Practicing Allowance as admissible at present in the existing pre-revised structure (in terms of para 4 of this OM)", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getNpAllowance()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("15.", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of next increment (Rule 10) and Pay after grant of increment", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getDoeIncr()), f1));
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            ArrayList revisedList = getNoOfIncrement(tform.getEmpid());
            hrms.model.payroll.thirdschedule.ThirdScheduleBean tbean = null;
            if (revisedList.size() > 0) {
                tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(0);
            }

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{1, 2});
            table.setWidthPercentage(80);

            cell = new PdfPCell(new Phrase("Date of increment", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay after increment in applicable Level of Pay Matrix", getDesired_PDF_Font(12, false, true)));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            for (int i = 0; i < revisedList.size(); i++) {
                tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(i);

                cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getIncrDt()), f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Rs." + StringUtils.defaultString(tbean.getRevisedbasic() + "/-(Cell-" + StringUtils.defaultString(tbean.getCell() + "") + ", Level-" + StringUtils.defaultString(tbean.getLevel() + "") + ")"), f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);

            table = new PdfPTable(4);
            table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
            table.setWidthPercentage(80);

            cell = new PdfPCell(new Phrase("", f1));
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("16.", f1));
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Any other relevant information", f1));
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", f1));
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(tform.getOtherInfo()), f1));
            cell.setFixedHeight(100);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            // End Revised Table

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String verifyIASCadre(String cadreCode) {

        String isIASCadre = "N";

        try {
            if (cadreCode.equals("1101") || cadreCode.equals("9103")
                    || cadreCode.equals("5801") || cadreCode.equals("1165")
                    || cadreCode.equals("1007") || cadreCode.equals("9105")
                    || cadreCode.equals("1166") || cadreCode.equals("9106") || cadreCode.equals("1167") || cadreCode.equals("0728")) {
                isIASCadre = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isIASCadre;
    }

    private String getCheckingAuthName(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String chkAuthName = "";

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                chkAuthName = rs.getString("FULL_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return chkAuthName;
    }

    @Override
    public String getEmployeeOfficeName(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offName = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT OFF_EN FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                offName = rs.getString("OFF_EN");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offName;
    }

    private String getDeptCodeFromGPC(String gpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String deptcode = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select department_code from g_post where post_code=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                deptcode = rs.getString("department_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptcode;
    }

    @Override
    public void thirdScheduleUGCPDF(Document document, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int fixedheight = 40;

        String incrDt = null;
        int revisedBasic = 0;

        String level = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            PdfPTable innertable = null;
            PdfPCell innercell = null;

            table = new PdfPTable(4);
            table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
            table.setWidthPercentage(80);

            innertable = new PdfPTable(2);
            innertable.setWidths(new float[]{0.05f, 0.5f});
            innertable.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("THIRD SCHEDULE", getDesired_PDF_Font(13, true, true)));
            cell.setColspan(4);
            cell.setFixedHeight(fixedheight);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Form for fixation of Pay under the Odisha Revised Scales of Pay (For", getDesired_PDF_Font(13, true, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("College Teachers) Rules 2019", getDesired_PDF_Font(13, true, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(See rule-7)", getDesired_PDF_Font(12, false, false)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            String sql = "select EMP_ID,officiating, EMP_NAME,PAY_REVISION_OPTION.department_code,hoo_spc, g_post2.post hoo_post_name,cur_basic_salary,CUR_SALARY,CUR_GP,PAY_REVISION_OPTION.POST,option_chosen,cur_post_paymatrix_level,entry_gpc,entry_paymatrix_level,nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,"
                    + " entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,"
                    + " PAY_REVISION_OPTION.basic,ddoPayScale,ddoGP,revised_basic,da,is_approved,revision_pay_scale,"
                    + " revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,previous_pay_scale,previous_gp,g_post3.post entryPost,cur_spc,g_post4.post curPost,doe_gov,mon_basic from ("
                    + " SELECT PAY_REVISION_OPTION.EMP_ID,officiating,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,g_office.department_code,hoo_spc,cur_basic_salary,CUR_SALARY,EMP_MAST.GP CUR_GP,G_POST1.POST,option_chosen,cur_post_paymatrix_level,entry_gpc,entry_paymatrix_level,nos_racp_availed,nos_promotion_availed,nos_racp_before_promotion,nos_racp_after_promotion,basicpay_fix_paymatrix,basicpay_fix_paymatrix_date,entered_date,prerev_emo_basicpay,prerev_emo_gp,prerev_emo_da,prerev_emo_totalpay,payrev_257_basicpay,payrev_paycell,doe_incr,other_info,PAY_REVISION_OPTION.basic,pay_scale ddoPayScale,PAY_REVISION_OPTION.gp ddoGP,revised_basic,da,is_approved,revision_pay_scale,revision_gp,revision_cur_basic,payrev_fitted_amount,payrev_fitted_level,previous_pay_scale,previous_gp,cur_spc,doe_gov,mon_basic FROM EMP_MAST"
                    + "                    INNER JOIN PAY_REVISION_OPTION ON EMP_MAST.EMP_ID=PAY_REVISION_OPTION.EMP_ID"
                    + "                    LEFT OUTER JOIN g_office on EMP_MAST.cur_off_code=g_office.off_code"
                    + "                    LEFT OUTER JOIN EMP_PAY_REVISED_2016 ON PAY_REVISION_OPTION.EMP_ID=EMP_PAY_REVISED_2016.EMP_ID"
                    + "                    LEFT OUTER JOIN G_POST G_POST1 ON PAY_REVISION_OPTION.GPC=G_POST1.POST_CODE"
                    + "                    WHERE PAY_REVISION_OPTION.EMP_ID=?) PAY_REVISION_OPTION"
                    + "                    left outer join g_post g_post2 on PAY_REVISION_OPTION.hoo_spc=g_post2.post_code"
                    + "                    left outer join g_post g_post3 on PAY_REVISION_OPTION.entry_gpc=g_post3.post_code"
                    + "                    left outer join g_spc on PAY_REVISION_OPTION.cur_spc=g_spc.spc"
                    + "                    left outer join g_post g_post4 on g_spc.gpc=g_post4.post_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {

                int revBasic = 0;
                int revDA = 0;

                if (rs.getInt("basic") > 0) {
                    revBasic = rs.getInt("basic");
                } else if (rs.getInt("mon_basic") > 0) {
                    revBasic = rs.getInt("mon_basic");
                }

                if (rs.getInt("prerev_emo_da") > 0) {
                    revDA = rs.getInt("prerev_emo_da");
                } else if (rs.getInt("da") > 0) {
                    revDA = rs.getInt("da");
                }
                int total = revBasic + rs.getInt("ddoGP") + revDA;

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("1.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Name of the Employee", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getString("EMP_NAME"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("2.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Name of the Head of Office (Designation Only)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("hoo_post_name")), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("3.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Post held by the employee (Substantive/Officiating)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getString("officiating"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String prevPayScale = "";
                String prevGp = "";
                if (rs.getString("previous_pay_scale") != null && !rs.getString("previous_pay_scale").equals("")) {
                    prevPayScale = rs.getString("previous_pay_scale");
                } else {
                    prevPayScale = rs.getString("ddoPayScale");
                }

                if (rs.getString("previous_gp") != null && !rs.getString("previous_gp").equals("")) {
                    prevGp = rs.getString("previous_gp");
                } else {
                    prevGp = rs.getString("ddoGP");
                }

                cell = new PdfPCell(new Phrase("4.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Pay Band and Academic Grade Pay of the Post", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(prevPayScale) + "\n" + StringUtils.defaultString(prevGp), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("5.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Corresponding Level in the Pay Matrix of the Pay Band and Academic Grade Pay of the present post", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                level = getLevelForUGC(rs.getString("cur_post_paymatrix_level"));
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(level), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("6.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Entry grade post and its corresponding Level in Pay Matrix", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("entryPost")) + "\n" + StringUtils.defaultString(rs.getString("entry_paymatrix_level")), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String revPayScale = "";
                String revGp = "";
                if (rs.getString("revision_pay_scale") != null && !rs.getString("revision_pay_scale").equals("")) {
                    revPayScale = rs.getString("revision_pay_scale");
                } else {
                    revPayScale = rs.getString("ddoPayScale");
                }

                if (rs.getString("revision_gp") != null && !rs.getString("revision_gp").equals("")) {
                    revGp = rs.getString("revision_gp");
                } else {
                    revGp = rs.getString("ddoGP");
                }

                cell = new PdfPCell(new Phrase("7.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Pay Band and Academic Grade Pay in which pay is drawn (As per RACPS, if availed)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(revPayScale) + "\n" + StringUtils.defaultString(revGp), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("8.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Existing Basic Pay (Pay + Academic Grade Pay)", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("revision_cur_basic") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("9.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay to be fixed in the Level of Pay Matrix", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                level = getLevelForUGC(rs.getString("basicpay_fix_paymatrix"));
                cell = new PdfPCell(new Phrase(level, f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String enteredDate = "";
                String isAfter = "N";

                if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                    Date doj = sdf.parse(rs.getString("doe_gov"));
                    if (doj.compareTo(payRevDt) > 0) {
                        isAfter = "Y";
                        enteredDate = (CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                    }
                }

                if (isAfter.equals("N")) {
                    if (rs.getString("option_chosen") != null && rs.getString("option_chosen").equals("1")) {
                        enteredDate = "01-JAN-2016";
                    } else {
                        enteredDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("entered_date"));
                    }
                }
                cell = new PdfPCell(new Phrase("10.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" Date from which option exercised to come over to revised pay structure", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(enteredDate, f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("11.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Emoulments in the existing Pay band and Academic Grade pay on the date from which revised pay is opted.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innercell = new PdfPCell(new Phrase("(a)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Pay(including Personal Pay)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(revBasic + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(b)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Grade Pay", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("ddoGP") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(c)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("D.A as on " + enteredDate, f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(revDA + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                innertable = new PdfPTable(2);
                innertable.setWidths(new float[]{0.05f, 0.5f});
                innertable.setWidthPercentage(100);

                innercell = new PdfPCell(new Phrase("(d)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase("Total emoulments (a to c)", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(innercell);
                cell = new PdfPCell(innertable);
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(total + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("12.", f1));
                cell.setFixedHeight(60);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay fixed in the Revised Scale of pay structure by multiplying the existing basic pay (Cl.No.12) by a factor of 2.57 and rounded off to the nearest rupee.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rs.getInt("payrev_257_basicpay") + "", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("13.", f1));
                cell.setFixedHeight(80);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("The Pay Cell in the appropriate Level in which the amount arrived at Sl. No. 16 is exactly fitted, if no such Cell exact to the amount is available then the next above Cell in that Level.\n or \n If the amount so arrived is less than the first Cell in the Level then the pay is fitted at the first Cell of the Level.(Cell No. and the amount of pay be mentioned)", f1));
                cell.setFixedHeight(120);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                level = getLevelForUGC(rs.getString("payrev_fitted_level"));
                cell = new PdfPCell(new Phrase(" Cell Number: " + rs.getInt("payrev_paycell") + "\n Appropriate Level: " + level + "\n Fitted Amount: " + rs.getString("payrev_fitted_amount"), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                String pp = "";

                ArrayList revisedList = getNoOfIncrement(rs.getString("EMP_ID"));
                hrms.model.payroll.thirdschedule.ThirdScheduleBean tbean = null;
                if (revisedList != null && revisedList.size() > 0) {
                    Iterator incrItr = revisedList.iterator();
                    while (incrItr.hasNext()) {
                        tbean = (ThirdScheduleBean) incrItr.next();
                        if (tbean.getPp() > 0) {
                            pp = pp + "Personal Pay - Rs." + tbean.getPp() + "\n";
                        }
                    }
                    /*tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(0);
                     incrDt = tbean.getIncrDt();
                     revisedBasic = tbean.getRevisedbasic();*/
                }

                cell = new PdfPCell(new Phrase("14.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Date of next increment ", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_incr"))), f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("15.", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" Any other relevant information", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(":", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rs.getString("other_info")) + "\n" + pp, f1));
                cell.setFixedHeight(120);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new float[]{1, 1.5f, 0.5f});
                table.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pay in the Cell in the Level after Increment", getDesired_PDF_Font(12, false, true)));
                cell.setBorder(Rectangle.NO_BORDER);
                //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new int[]{1, 2, 2});
                table.setWidthPercentage(80);

                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setFixedHeight(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Date of Increment", f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Cell No. and Pay", f1));
                cell.setFixedHeight(20);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Level", f1));
                cell.setFixedHeight(20);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(3);
                table.setWidths(new int[]{1, 2, 2});
                table.setWidthPercentage(80);
                tbean = null;
                for (int i = 0; i < revisedList.size(); i++) {
                    tbean = (hrms.model.payroll.thirdschedule.ThirdScheduleBean) revisedList.get(i);

                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getIncrDt()), f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(tbean.getCell() + "") + "-" + StringUtils.defaultString(tbean.getRevisedbasic() + ""), f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    level = getLevelForUGC(tbean.getLevel());
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(level) + "", f1));
                    cell.setFixedHeight(20);
                    //cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
                document.add(table);
                // End Revised Table

                table = new PdfPTable(4);
                table.setWidths(new float[]{0.3f, 3, 0.5f, 3});
                table.setWidthPercentage(80);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(60);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("  Date", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Signature & Designation of Head  of Office/Competent Authority", f1));
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setFixedHeight(fixedheight);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("  Office: ", f1));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", f1));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    private String getLevelForUGC(String level) {
        String modifiedlevel = "";
        if (level != null && !level.equals("")) {
            if (level.equals("13A")) {
                modifiedlevel = level;
            } else if (level.equals("13")) {
                modifiedlevel = level + "A";
            } else {
                modifiedlevel = level;
            }
        }
        return modifiedlevel;
    }

    @Override
    public List getBillGroupWiseEmployeeList(String billgroupid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        ThirdScheduleBean tbean = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT EMP_MAST.EMP_ID,EMP_MAST.GPF_NO,ARRAY_TO_STRING(ARRAY[EMP_MAST.INITIALS, EMP_MAST.F_NAME, EMP_MAST.M_NAME,EMP_MAST.L_NAME], ' ') EMPNAME,"
                    + " EMP_MAST.doe_gov FROM BILL_SECTION_MAPPING"
                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID=SECTION_POST_MAPPING.SECTION_ID"
                    + " INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC"
                    + " INNER JOIN EMP_REGULARIZATION_CONTRACTUAL ON EMP_MAST.EMP_ID=EMP_REGULARIZATION_CONTRACTUAL.EMP_ID"
                    + " WHERE BILL_SECTION_MAPPING.BILL_GROUP_ID=? ORDER BY EMP_MAST.F_NAME,EMP_MAST.L_NAME";
            pst = con.prepareStatement(sql);
            pst.setBigDecimal(1, new BigDecimal(billgroupid));
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new ThirdScheduleBean();
                tbean.setEmpId(rs.getString("EMP_ID"));
                tbean.setGpfno(rs.getString("GPF_NO"));
                tbean.setEmpname(rs.getString("EMPNAME"));
                emplist.add(tbean);
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
    public ThirdScheduleForm getCont6YrsToRegularData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ThirdScheduleForm tform = new ThirdScheduleForm();

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT is_previous_cont6,EMP_MAST.EMP_ID,EMP_MAST.GPF_NO,ARRAY_TO_STRING(ARRAY[EMP_MAST.INITIALS, EMP_MAST.F_NAME, EMP_MAST.M_NAME,EMP_MAST.L_NAME], ' ') EMPNAME,g_office.department_code,pay_revision_option.*"
                    + " from emp_mast"
                    + " LEFT OUTER JOIN g_office on EMP_MAST.cur_off_code=g_office.off_code"
                    + " left outer join pay_revision_option on emp_mast.emp_id=pay_revision_option.emp_id"
                    + " where EMP_MAST.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                tform.setEmpid(empid);
                tform.setEmpname(rs.getString("EMPNAME"));
                tform.setHooSpc(rs.getString("hoo_spc"));
                tform.setOfficiating(rs.getString("officiating"));
                tform.setPreviousPayScale(rs.getString("previous_pay_scale"));
                tform.setPreviousGp(rs.getString("previous_gp"));
                tform.setBasicpayFixPaymatrix(rs.getString("basicpay_fix_paymatrix"));
                tform.setDoeIncr(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_incr")));
                tform.setOtherInfo(rs.getString("other_info"));
                tform.setIsApproved(rs.getString("is_approved"));
                if (rs.getString("is_previous_cont6") == null || (rs.getString("is_previous_cont6") != null && rs.getString("is_previous_cont6").equals("N"))) {
                    unlockApproveStatus(empid);
                    tform.setIsApproved(null);
                }
                tform.setDeptCode(rs.getString("department_code"));

                tform.setIncrementList(getNoOfIncrementForCont6YrsToRegular(empid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tform;
    }

    @Override
    public String saveContractual6YrsToRegularThirdScheduleData(ThirdScheduleForm tform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            con = this.dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat1.format(cal.getTime());

            if (checkPayRevisionOptionEntry(tform.getEmpid()) == false) {
                String sql = "INSERT INTO PAY_REVISION_OPTION(EMP_ID,hoo_spc,officiating,previous_pay_scale,previous_gp,basicpay_fix_paymatrix,doe_incr,other_info,is_previous_cont6,date_of_submission) VALUES(?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, tform.getEmpid());
                pst.setString(2, tform.getHooSpc());
                pst.setString(3, tform.getOfficiating());
                pst.setString(4, tform.getPreviousPayScale());
                if (tform.getPreviousGp() != null && !tform.getPreviousGp().equals("")) {
                    pst.setInt(5, Integer.parseInt(tform.getPreviousGp()));
                } else {
                    pst.setInt(5, 0);
                }
                if (tform.getBasicpayFixPaymatrix() != null && !tform.getBasicpayFixPaymatrix().equals("")) {
                    pst.setInt(6, Integer.parseInt(tform.getBasicpayFixPaymatrix()));
                } else {
                    pst.setInt(6, 0);
                }
                if (tform.getDoeIncr() != null && !tform.getDoeIncr().equals("")) {
                    pst.setTimestamp(7, new Timestamp(dateFormat.parse(tform.getDoeIncr()).getTime()));
                } else {
                    pst.setTimestamp(7, null);
                }
                pst.setString(8, tform.getOtherInfo());
                pst.setString(9, "Y");
                pst.setTimestamp(10, new Timestamp(dateFormat1.parse(startTime).getTime()));
                pst.executeUpdate();
            } else {
                String sql = "UPDATE PAY_REVISION_OPTION SET hoo_spc=?,officiating=?,previous_pay_scale=?,previous_gp=?,basicpay_fix_paymatrix=?,doe_incr=?,other_info=?,is_previous_cont6='Y' WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, tform.getHooSpc());
                pst.setString(2, tform.getOfficiating());
                pst.setString(3, tform.getPreviousPayScale());
                if (tform.getPreviousGp() != null && !tform.getPreviousGp().equals("")) {
                    pst.setInt(4, Integer.parseInt(tform.getPreviousGp()));
                } else {
                    pst.setInt(4, 0);
                }
                if (tform.getBasicpayFixPaymatrix() != null && !tform.getBasicpayFixPaymatrix().equals("")) {
                    pst.setInt(5, Integer.parseInt(tform.getBasicpayFixPaymatrix()));
                } else {
                    pst.setInt(5, 0);
                }
                if (tform.getDoeIncr() != null && !tform.getDoeIncr().equals("")) {
                    pst.setTimestamp(6, new Timestamp(dateFormat.parse(tform.getDoeIncr()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                pst.setString(7, tform.getOtherInfo());
                pst.setString(8, tform.getEmpid());
                pst.executeUpdate();
            }

            if (tform.getRevisedbasic() != null) {
                pst = con.prepareStatement("DELETE FROM emp_pay_revised_increment_2016 WHERE EMP_ID=? and is_previous_cont6='Y'");
                pst.setString(1, tform.getEmpid());
                pst.executeUpdate();

                String sql = "insert into emp_pay_revised_increment_2016(emp_id,incr_date,REVISED_BASIC,level,cell,is_previous_cont6) values(?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);

                String[] incrDt = tform.getIncrDt();
                String[] revPay = tform.getRevisedbasic();
                String[] incrLevel = tform.getIncrLevel();
                String[] incrCell = tform.getIncrCell();
                for (int i = 0; i < revPay.length; i++) {

                    if (revPay[i] != null && !revPay[i].equals("")) {
                        pst.setString(1, tform.getEmpid());
                        if (incrDt.length > 0 && incrDt[i] != null && !incrDt[i].equals("")) {
                            pst.setTimestamp(2, new Timestamp(dateFormat.parse(incrDt[i]).getTime()));
                        } else {
                            pst.setTimestamp(2, null);
                        }
                        if (revPay[i] != null && !revPay[i].equals("")) {
                            pst.setInt(3, Integer.parseInt(revPay[i]));
                        } else {
                            pst.setInt(3, 0);
                        }
                        if (incrLevel.length > 0 && incrLevel[i] != null && !incrLevel[i].equals("")) {
                            pst.setString(4, incrLevel[i]);
                        } else {
                            pst.setString(4, null);
                        }
                        if (incrCell.length > 0 && incrCell[i] != null && !incrCell[i].equals("")) {
                            pst.setInt(5, Integer.parseInt(incrCell[i]));
                        } else {
                            pst.setInt(5, 0);
                        }
                        pst.setString(6, "Y");
                        pst.executeUpdate();
                    }
                }
            } else {
                pst = con.prepareStatement("DELETE FROM emp_pay_revised_increment_2016 WHERE EMP_ID=? and is_previous_cont6='Y'");
                pst.setString(1, tform.getEmpid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return null;
    }

    private boolean checkPayRevisionOptionEntry(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isPresent = false;

        try {
            con = this.repodataSource.getConnection();

            String sql = "select count(*) cnt from pay_revision_option where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isPresent = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isPresent;
    }

    private ArrayList getNoOfIncrementForCont6YrsToRegular(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        hrms.model.payroll.thirdschedule.ThirdScheduleBean tbean = null;
        ArrayList revisedPayList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT INCR_DATE,REVISED_BASIC,LEVEL,CELL,PP FROM emp_pay_revised_increment_2016 WHERE EMP_ID=? and is_previous_cont6='Y' ORDER BY INCR_DATE ASC");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new hrms.model.payroll.thirdschedule.ThirdScheduleBean();
                tbean.setIncrDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("INCR_DATE")));
                tbean.setRevisedbasic(rs.getInt("REVISED_BASIC"));
                tbean.setLevel(rs.getString("LEVEL"));
                tbean.setCell(rs.getInt("CELL"));
                tbean.setPp(rs.getInt("PP"));
                revisedPayList.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revisedPayList;
    }

    @Override
    public String updateContractual6YrsToRegularPayData(ThirdScheduleForm tform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int maxbasic = 0;
        int maxcell = 0;
        int maxlevel = 0;

        try {
            con = this.dataSource.getConnection();

            if (tform.getRevisedbasic() != null) {

                String[] revPay = tform.getRevisedbasic();
                String[] incrLevel = tform.getIncrLevel();
                String[] incrCell = tform.getIncrCell();

                if (revPay.length > 0 && revPay[0] != null && !revPay[0].equals("")) {
                    maxbasic = Integer.parseInt(revPay[0]);
                    maxlevel = Integer.parseInt(incrLevel[0]);
                    maxcell = Integer.parseInt(incrCell[0]);
                }

                for (int i = 0; i < revPay.length; i++) {
                    if (revPay[i] != null && !revPay[i].equals("")) {
                        if (maxbasic < Integer.parseInt(revPay[i])) {
                            maxbasic = Integer.parseInt(revPay[i]);
                            maxlevel = Integer.parseInt(incrLevel[i]);
                            maxcell = Integer.parseInt(incrCell[i]);
                        }
                    }
                }
            }

            String sql = "UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, CUR_BASIC_SALARY=?,pay_commission=7 WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, maxlevel);
            pst.setInt(2, maxcell);
            pst.setDouble(3, maxbasic);
            pst.setString(4, tform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return null;
    }

    @Override
    public void approveContractual6YrsToRegularThirdSchedule(ThirdScheduleForm tform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());

            String sql = "UPDATE PAY_REVISION_OPTION SET is_approved='Y',date_of_submission=? WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            if (tform.getDoeIncr() != null && !tform.getDoeIncr().equals("")) {
                pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            } else {
                pst.setTimestamp(1, null);
            }
            pst.setString(2, tform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private void unlockApproveStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE PAY_REVISION_OPTION SET IS_APPROVED='N' WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String validatePayAgainstLevelAndCell(int level, int cell, int basic) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String validationstatus = "Y";
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT amt FROM pay_matrix_2017 WHERE gp_level = ? AND level_slno = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, level);
            pst.setInt(2, cell);
            rs = pst.executeQuery();
            if (rs.next()) {
                int correctbasic = rs.getInt("amt");
                if (basic != correctbasic) {
                    validationstatus = "N";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return validationstatus;
    }

    @Override
    public int revertCont6YrsThirdScheduleDC(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        PreparedStatement updatepst = null;
        
        int retval = 0;
        try {
            con = this.dataSource.getConnection();
            
            updatepst = con.prepareStatement("UPDATE PAY_REVISION_OPTION SET is_approved=null WHERE EMP_ID=?");
            
            String sql = "SELECT IS_PREVIOUS_CONT6 FROM PAY_REVISION_OPTION WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString("IS_PREVIOUS_CONT6") != null && rs.getString("IS_PREVIOUS_CONT6").equals("Y")){
                    updatepst.setString(1,empid);
                    retval = updatepst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(updatepst);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return retval;  
    }
}

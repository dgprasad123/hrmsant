package hrms.dao.annualBudget;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.annualBudget.Annexture11APart1;
import hrms.model.annualBudget.Annexture11APart2EmployeeDetails;
import hrms.model.annualBudget.Annexture11B;
import hrms.model.annualBudget.Annexture11BPostDetails;
import hrms.model.annualBudget.Annexure1a;
import hrms.model.annualBudget.Annexure1b;
import hrms.model.annualBudget.Annexure1c;
import hrms.model.annualBudget.Annexure2;
import hrms.model.annualBudget.AnnexureIIAPart1GroupDetails;
import hrms.model.annualBudget.AnnualBudgetBean;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AnnualBudgetDAOImpl implements AnnualBudgetDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getFinancialYearList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<SelectOption> fiscalyearlist = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FY FROM FINANCIAL_YEAR ORDER BY FY DESC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("FY"));
                so.setValue(rs.getString("FY"));
                fiscalyearlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getAnnualBudgetList(String ddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List budgetlist = new ArrayList();
        AnnualBudgetBean abean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from hrmis.annual_budget where ddo_code=? order by fy";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            rs = pst.executeQuery();
            while (rs.next()) {
                abean = new AnnualBudgetBean();
                abean.setBudgetid(rs.getString("budget_id"));
                abean.setFy(rs.getString("fy"));
                abean.setCreatedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_date")));
                abean.setStatus(rs.getString("status"));
                abean.setIsLocked(rs.getString("is_locked"));
                abean.setLockdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lock_date")));
                abean.setIsSubmitted(rs.getString("is_submitted"));
                abean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                abean.setIsReverted(rs.getString("is_reverted"));
                abean.setRevertDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("revert_date")));
                abean.setRevertReason(rs.getString("revert_reason"));
                budgetlist.add(abean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return budgetlist;
    }

    @Override
    public void processBudgetData(String fy, String ddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement pst3 = null;
        ResultSet rs3 = null;

        PreparedStatement insertpst = null;

        String chartAcc = "";

        //String officecode = "";
        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String objectHead = "";

        String[] postGroupType = {"A", "B", "C", "D"};

        int sancStrength = 0;
        int menInPosition = 0;
        int vacancy = 0;
        int anticipatedvacancy = 0;
        int totalvacancy = 0;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select budget_id from hrmis.annual_budget where ddo_code=? and fy=?");
            pst.setString(1, ddocode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                int createdbudgetid = rs.getInt("budget_id");
                if (createdbudgetid > 0) {
                    pst1 = con.prepareStatement("delete from hrmis.budget_annexture_1a where budget_id=?");
                    pst1.setInt(1, createdbudgetid);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("delete from hrmis.budget_annexture_1b where budget_id=?");
                    pst1.setInt(1, createdbudgetid);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("delete from hrmis.budget_annexture_1c where budget_id=?");
                    pst1.setInt(1, createdbudgetid);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("delete from hrmis.budget_annexture_2 where budget_id=?");
                    pst1.setInt(1, createdbudgetid);
                    pst1.executeUpdate();

                    pst1 = con.prepareStatement("delete from hrmis.annual_budget where budget_id=?");
                    pst1.setInt(1, createdbudgetid);
                    pst1.executeUpdate();
                }
            }

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            String sql = "insert into hrmis.annual_budget(ddo_code,fy,status,is_locked,created_date) values(?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, ddocode);
            pst.setString(2, fy);
            pst.setString(3, "Not Submitted");
            pst.setString(4, "N");
            pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int budgetMaxId = rs.getInt("budget_id");

            /*Annexure IA*/
            String sql2 = "select bill_group_master.bill_group_id from bill_group_master\n"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id\n"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                    + " inner join g_office on g_section.off_code=g_office.off_code\n"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id\n"
                    + " inner join g_spc on section_post_mapping.spc=g_spc.spc\n"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') \n"
                    + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and "
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and g_spc.post_grp=? and bill_group_master.pay_head=? "
                    + " group by bill_group_master.bill_group_id";
            pst2 = con.prepareStatement(sql2);

            String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                    + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head "
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";

            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                //officecode = rs1.getString("off_code").trim();
                demandNo = rs1.getString("DEMAND_NO").trim();
                majorHead = rs1.getString("MAJOR_HEAD").trim();
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                minorHead = rs1.getString("MINOR_HEAD").trim();
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                plan = rs1.getString("PLAN").trim();
                sector = rs1.getString("SECTOR").trim();
                objectHead = rs1.getString("pay_head").trim();

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                for (int i = 0; i < postGroupType.length; i++) {

                    sancStrength = 0;
                    menInPosition = 0;

                    String tempPostGroup = postGroupType[i];

                    pst2.setString(1, ddocode);
                    pst2.setString(2, demandNo);
                    pst2.setString(3, majorHead);
                    pst2.setString(4, subMajorHead);
                    pst2.setString(5, minorHead);
                    pst2.setString(6, minorHead1);
                    pst2.setString(7, minorHead2);
                    pst2.setString(8, minorHead3);
                    pst2.setString(9, plan);
                    pst2.setString(10, sector);
                    pst2.setString(11, tempPostGroup);
                    pst2.setString(12, objectHead);
                    rs2 = pst2.executeQuery();
                    while (rs2.next()) {
                        sancStrength = sancStrength + getSanctionedStrength(con, ddocode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                        menInPosition = menInPosition + getMenInPosition(con, ddocode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                    }
                    //System.out.println(chartAcc + " and " + tempPostGroup + " and sancStrength is: " + sancStrength);
                    //System.out.println(chartAcc + " and " + tempPostGroup + " and menInPosition is: " + menInPosition);
                    vacancy = sancStrength - menInPosition;
                    anticipatedvacancy = vacancy;
                    totalvacancy = vacancy + anticipatedvacancy;
                    //System.out.println("chartAcc is: " + chartAcc);
                    String insertquery = "insert into hrmis.budget_annexture_1a(ddo_code,chart_of_account,group_details,sanction_strength,vacancy,anticipated_vacancy,total_vacancy,men_in_position,vacancy_to_be_filled,anticipated_men_in_position,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                    insertpst = con.prepareStatement(insertquery);
                    insertpst.setString(1, ddocode);
                    insertpst.setString(2, chartAcc.trim());
                    insertpst.setString(3, tempPostGroup);
                    insertpst.setInt(4, sancStrength);
                    insertpst.setInt(5, vacancy);
                    insertpst.setInt(6, anticipatedvacancy);
                    insertpst.setInt(7, totalvacancy);
                    insertpst.setInt(8, menInPosition);
                    insertpst.setInt(9, vacancy);
                    insertpst.setInt(10, menInPosition);
                    insertpst.setInt(11, budgetMaxId);
                    if (chartAcc != null && chartAcc.length() == 36) {
                        insertpst.executeUpdate();
                    }
                }
            }

            /*Annexure IB and IC*/
            String postgrp = "";
            String empname = "";
            String empid = "";
            double grade = 0;
            int paycommission = 0;
            double pay = 0;
            double payNextYear = 0;
            double totalYearlyIncome = 0;
            double arrear_pay_855 = 0;
            double da = 0;
            double hra_403 = 0;
            double oa_523 = 0;
            double rcm_516 = 0;
            double total = 0;

            double basic_pay_3 = 0;
            double total_yearly_requirement_3 = 0;
            double arrear_pay_855_3 = 0;
            double da_156_3 = 0;
            double hra_403_3 = 0;
            double oa_523_3 = 0;
            double rcm_516_3 = 0;
            double total_3 = 0;

            String sql3 = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,f_name,post_grp_type,post_grp,cur_basic_salary,fixedvalue,emp_mast.gp,emp_mast.pay_commission from emp_mast"
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " left outer join (select fixedvalue,updation_ref_code from update_ad_info where where_updated='E' and ref_ad_code='53')update_ad_info on emp_mast.emp_id=update_ad_info.updation_ref_code"
                    + " where bill_group_master.bill_group_id=? order by f_name";
            pst3 = con.prepareStatement(sql3);

            sql2 = "select bill_group_master.bill_group_id from bill_group_master\n"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id\n"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                    + " inner join g_office on g_section.off_code=g_office.off_code\n"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id\n"
                    + " inner join g_spc on section_post_mapping.spc=g_spc.spc\n"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') \n"
                    + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and "
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and bill_group_master.pay_head=? "
                    + " group by bill_group_master.bill_group_id";

            pst2 = con.prepareStatement(sql2);

            sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                    + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head"
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";

            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                //officecode = rs1.getString("off_code").trim();
                demandNo = rs1.getString("DEMAND_NO").trim();
                majorHead = rs1.getString("MAJOR_HEAD").trim();
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                minorHead = rs1.getString("MINOR_HEAD").trim();
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                plan = rs1.getString("PLAN").trim();
                sector = rs1.getString("SECTOR").trim();
                objectHead = rs1.getString("pay_head").trim();

                basic_pay_3 = 0;
                total_yearly_requirement_3 = 0;
                arrear_pay_855_3 = 0;
                da_156_3 = 0;
                hra_403_3 = 0;
                oa_523_3 = 0;
                rcm_516_3 = 0;
                total_3 = 0;

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, ddocode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                pst2.setString(11, objectHead);

                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    int ctr = 0;
                    //System.out.println("Second Sheet: Chart of Account is: " + chartAcc + " and Bill Group Id is: " + rs2.getString("bill_group_id"));
                    //ArrayList emplist = getEmployeeList(con, new BigDecimal(rs2.getString("bill_group_id")));

                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    while (rs3.next()) {

                        postgrp = rs3.getString("post_grp");
                        empname = rs3.getString("EMPNAME");
                        empid = rs3.getString("emp_id");

                        paycommission = rs3.getInt("pay_commission");

                        grade = rs3.getInt("gp");
                        pay = rs3.getInt("cur_basic_salary");
                        payNextYear = pay;
                        rcm_516 = 2000;

                        totalYearlyIncome = 0;
                        if (rs3.getString("cur_basic_salary") != null && !rs3.getString("cur_basic_salary").equals("")) {
                            totalYearlyIncome = Double.parseDouble(rs3.getString("cur_basic_salary"));
                        }
                        totalYearlyIncome = totalYearlyIncome * 12;

                        da = 0;
                        if (totalYearlyIncome > 0) {
                            if (paycommission == 7) {
                                da = pay * 0.55;
                            } else if (paycommission == 6) {
                                da = (pay + grade) * 2.14;
                            } else if (paycommission == 5) {
                                da = (pay + grade) * 2.64;
                            }
                        }
                        da = da * 12;

                        hra_403 = rs3.getInt("fixedvalue");
                        hra_403 = hra_403 * 12;

                        total = totalYearlyIncome + arrear_pay_855 + da + hra_403 + oa_523 + rcm_516;

                        String insertquery = "insert into hrmis.budget_annexture_1b(ddo_code,chart_of_account,group_details,name_of_increment,hrms_id,grade,basic_pay,total_yearly,arrear_pay_855,da_156,hra_403,oa_523,rcm_516,total,budget_id,basic_pay_next_year) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        insertpst = con.prepareStatement(insertquery);
                        insertpst.setString(1, ddocode);
                        insertpst.setString(2, chartAcc);
                        insertpst.setString(3, postgrp);
                        insertpst.setString(4, empname);
                        insertpst.setString(5, empid);
                        insertpst.setInt(6, (int) grade);
                        insertpst.setInt(7, (int) pay);
                        insertpst.setInt(8, (int) totalYearlyIncome);
                        insertpst.setInt(9, (int) arrear_pay_855);
                        insertpst.setInt(10, (int) da);
                        insertpst.setInt(11, (int) hra_403);
                        insertpst.setInt(12, (int) oa_523);
                        insertpst.setInt(13, (int) rcm_516);
                        insertpst.setInt(14, (int) total);
                        insertpst.setInt(15, budgetMaxId);
                        insertpst.setInt(16, (int) pay);
                        if (chartAcc != null && chartAcc.length() == 36) {
                            insertpst.executeUpdate();
                        }

                        basic_pay_3 = basic_pay_3 + pay;
                        total_yearly_requirement_3 = total_yearly_requirement_3 + totalYearlyIncome;
                        arrear_pay_855_3 = arrear_pay_855_3 + arrear_pay_855;
                        da_156_3 = da_156_3 + da;
                        hra_403_3 = hra_403_3 + hra_403;
                        oa_523_3 = oa_523_3 + oa_523;
                        rcm_516_3 = rcm_516_3 + rcm_516;
                        total_3 = total_3 + total;

                        postgrp = "";
                        empname = "";
                        empid = "";
                        grade = 0;
                        pay = 0;
                        totalYearlyIncome = 0;
                        arrear_pay_855 = 0;
                        da = 0;
                        hra_403 = 0;
                        oa_523 = 0;
                        rcm_516 = 0;
                        total = 0;
                    }
                }
                String insertquery = "insert into hrmis.budget_annexture_1c(ddo_code,chart_of_account,exclusion_for_incubents,basic_pay,total_yearly_requirement,arrear_pay_855,da_156,hra_403,oa_523,rcm_516,total,budget_id,additional_amount) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                insertpst = con.prepareStatement(insertquery);
                insertpst.setString(1, ddocode);
                insertpst.setString(2, chartAcc);
                insertpst.setInt(3, 0);
                insertpst.setInt(4, (int) basic_pay_3);
                insertpst.setInt(5, (int) total_yearly_requirement_3);
                insertpst.setInt(6, (int) arrear_pay_855_3);
                insertpst.setInt(7, (int) da_156_3);
                insertpst.setInt(8, (int) hra_403_3);
                insertpst.setInt(9, (int) oa_523_3);
                insertpst.setInt(10, (int) rcm_516_3);
                insertpst.setInt(11, (int) total_3);
                insertpst.setInt(12, budgetMaxId);
                insertpst.setInt(13, 0);
                if (chartAcc != null && chartAcc.length() == 36) {
                    insertpst.executeUpdate();
                }
            }

            /*Annexure IIB*/
            sql3 = "select post,count(*) cnt from emp_mast"
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where bill_group_master.bill_group_id=? group by post order by post";
            pst3 = con.prepareStatement(sql3);

            sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " inner join g_spc on section_post_mapping.spc=g_spc.spc"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONT6_REG'"
                    + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and bill_group_master.pay_head=?"
                    + " group by bill_group_master.bill_group_id";

            pst2 = con.prepareStatement(sql2);

            sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONT6_REG'"
                    + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head "
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR ";

            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                //officecode = rs1.getString("off_code").trim();
                demandNo = rs1.getString("DEMAND_NO").trim();
                majorHead = rs1.getString("MAJOR_HEAD").trim();
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                minorHead = rs1.getString("MINOR_HEAD").trim();
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                plan = rs1.getString("PLAN").trim();
                sector = rs1.getString("SECTOR").trim();
                objectHead = rs1.getString("pay_head").trim();

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, ddocode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                pst2.setString(11, objectHead);

                rs2 = pst2.executeQuery();
                while (rs2.next()) {

                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                    while (rs3.next()) {

                        int menInPositionContractual = rs3.getInt("cnt");
                        int noOfPostIncrDecr = rs3.getInt("cnt");
                        int totalMenInPosition = menInPositionContractual + noOfPostIncrDecr;

                        String insertquery = "insert into hrmis.budget_annexture_2(ddo_code,chart_of_account,post_name,men_in_position,no_of_post_incr_decr,total_men_in_position,actual_expecting_previous_year,actual_expecting_current_year,revised_estimated,be_next_year,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                        insertpst = con.prepareStatement(insertquery);
                        insertpst.setString(1, ddocode);
                        insertpst.setString(2, chartAcc);
                        insertpst.setString(3, rs3.getString("post"));
                        insertpst.setInt(4, menInPositionContractual);
                        insertpst.setInt(5, noOfPostIncrDecr);
                        insertpst.setInt(6, totalMenInPosition);
                        insertpst.setInt(7, 0);
                        insertpst.setInt(8, 0);
                        insertpst.setInt(9, 0);
                        insertpst.setInt(10, 0);
                        insertpst.setInt(11, budgetMaxId);
                        if (chartAcc != null && chartAcc.length() == 36) {
                            insertpst.executeUpdate();
                        }
                    }
                }
            }

            sql3 = "select post_nomenclature from emp_mast"
                    + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where bill_group_master.bill_group_id=? group by post_nomenclature order by post_nomenclature";
            pst3 = con.prepareStatement(sql3);

            sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " inner join emp_mast on section_post_mapping.spc=emp_mast.emp_id"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONTRACTUAL'"
                    + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and pay_head=? group by bill_group_master.bill_group_id";

            pst2 = con.prepareStatement(sql2);

            sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONTRACTUAL'"
                    + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head"
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, ddocode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                //officecode = rs1.getString("off_code").trim();
                demandNo = rs1.getString("DEMAND_NO").trim();
                majorHead = rs1.getString("MAJOR_HEAD").trim();
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                minorHead = rs1.getString("MINOR_HEAD").trim();
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                plan = rs1.getString("PLAN").trim();
                sector = rs1.getString("SECTOR").trim();
                objectHead = rs1.getString("pay_head").trim();

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, ddocode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                pst2.setString(11, objectHead);

                rs2 = pst2.executeQuery();
                while (rs2.next()) {

                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                    while (rs3.next()) {
                        int noOfPost = 0;
                        int noOfPostIncrDecr = 0;

                        noOfPost = getContractualNoOfPost(con, new BigDecimal(rs2.getString("bill_group_id")), rs3.getString("post_nomenclature"), ddocode);
                        noOfPostIncrDecr = noOfPost;

                        int totalMenInPosition = noOfPost + noOfPostIncrDecr;

                        String insertquery = "insert into hrmis.budget_annexture_2(ddo_code,chart_of_account,post_name,men_in_position,no_of_post_incr_decr,total_men_in_position,actual_expecting_previous_year,actual_expecting_current_year,revised_estimated,be_next_year,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                        insertpst = con.prepareStatement(insertquery);
                        insertpst.setString(1, ddocode);
                        insertpst.setString(2, chartAcc);
                        insertpst.setString(3, rs3.getString("post_nomenclature"));
                        insertpst.setInt(4, noOfPost);
                        insertpst.setInt(5, noOfPostIncrDecr);
                        insertpst.setInt(6, totalMenInPosition);
                        insertpst.setInt(7, 0);
                        insertpst.setInt(8, 0);
                        insertpst.setInt(9, 0);
                        insertpst.setInt(10, 0);
                        insertpst.setInt(11, budgetMaxId);
                        if (chartAcc != null && chartAcc.length() == 36) {
                            insertpst.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(insertpst);
            DataBaseFunctions.closeSqlObjects(rs3, pst3);
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private static int getSanctionedStrength(Connection con, String ddocode, String postgrp, BigDecimal billgroupid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            //String sql = "select count(*) sanc_strength from g_spc where off_code=? and post_grp=? and is_sanctioned='Y'";
            String sql = "select count(*) sanc_strength from g_spc\n"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc\n"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id\n"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id\n"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                    + " inner join g_office on g_section.off_code=g_office.off_code\n"
                    + " where g_office.ddo_code=? and post_grp=? and bill_section_mapping.bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, postgrp);
            pst.setBigDecimal(3, billgroupid);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("sanc_strength");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return count;
    }

    private static int getMenInPosition(Connection con, String ddocode, String postgrp, BigDecimal billgroupid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            /*String sql = "select count(*) men_in_position from emp_mast"
             + " inner join g_spc on emp_mast.cur_spc=g_spc.spc where cur_off_code=? and post_grp_type=? and is_regular='Y' and is_sanctioned='Y'";*/
            String sql = "select count(*) men_in_position from emp_mast\n"
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc\n"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc\n"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id\n"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id\n"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                    + " inner join g_office on g_section.off_code=g_office.off_code\n"
                    + " where g_office.ddo_code=? and post_grp=? and is_regular in ('Y','G','W') and bill_section_mapping.bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, postgrp);
            pst.setBigDecimal(3, billgroupid);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("men_in_position");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return count;
    }

    private static int getContractualNoOfPost(Connection con, BigDecimal billgroupid, String postname, String ddocode) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {

            /*String sql = "select count(*) cnt from emp_mast"
             + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
             + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
             + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
             + " where bill_group_master.bill_group_id=? and post_nomenclature=? group by post_nomenclature";*/
            String sql = "select count(*) cnt from emp_mast"
                    + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " inner join g_office on g_section.off_code=g_office.off_code"
                    + " where g_office.ddo_code=? and bill_group_master.bill_group_id=? and post_nomenclature=? group by post_nomenclature";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setBigDecimal(2, billgroupid);
            pst.setString(3, postname);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return count;
    }

    @Override
    public List getAnnexureIIAData(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Annexture11APart1 a2part1 = null;

        List a2part1List = new ArrayList();
        String chartacc = "";

        List grouplist = new ArrayList();
        List employeelist = new ArrayList();

        double basicnextyeartotal = 0;

        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select chart_of_account from hrmis.budget_annexture_1a where budget_id=? group by chart_of_account order by chart_of_account";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                rs = pst.executeQuery();
                while (rs.next()) {
                    a2part1 = new Annexture11APart1();

                    chartacc = rs.getString("chart_of_account");
                    a2part1.setChartOfAcc(chartacc);

                    grouplist = new ArrayList();
                    grouplist = getAnnexureIIAPart1GroupDetails(budgetid, chartacc);
                    a2part1.setAnnexureIIAPart1GroupDetails(grouplist);

                    employeelist = new ArrayList();
                    employeelist = getAnnexureIIAPart2EmployeeDetails(budgetid, chartacc);
                    a2part1.setAnnexureIIAPart2EmployeeDetails(employeelist);

                    basicnextyeartotal = 0;

                    if (employeelist != null && employeelist.size() > 0) {
                        Annexture11APart2EmployeeDetails a2part2 = null;
                        for (int i = 0; i < employeelist.size(); i++) {

                            a2part2 = (Annexture11APart2EmployeeDetails) employeelist.get(i);

                            double basicnextyear = 0;

                            if (a2part2.getBasic0103nextyear() != null && !a2part2.getBasic0103nextyear().equals("")) {
                                basicnextyear = Double.parseDouble(a2part2.getBasic0103nextyear());
                            }

                            basicnextyeartotal = basicnextyeartotal + basicnextyear;
                        }
                    }
                    a2part1.setBasic0103nextyearTotal((Double.valueOf(basicnextyeartotal)).longValue() + "");
                    getAnnexureIIACData(a2part1, budgetid, chartacc);

                    //a2part1.setAnnexureIIAPart3Details(a2part1List);
                    a2part1List.add(a2part1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a2part1List;
    }

    private List getAnnexureIIAPart1GroupDetails(String budgetid, String chartacc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        AnnexureIIAPart1GroupDetails a2part1groupdata = null;

        int vacancy = 0;
        int anticipatedvacancy = 0;

        List annexureIIAPart1GroupList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select * from hrmis.budget_annexture_1a where budget_id=? and chart_of_account=? order by group_details";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                pst.setString(2, chartacc);
                rs = pst.executeQuery();
                while (rs.next()) {
                    a2part1groupdata = new AnnexureIIAPart1GroupDetails();
                    a2part1groupdata.setGroupdetailid(rs.getString("annexture1a_id"));
                    a2part1groupdata.setGroup(rs.getString("group_details"));
                    a2part1groupdata.setSanctionedStrength(rs.getString("sanction_strength"));
                    a2part1groupdata.setVacancy01032020(rs.getString("vacancy"));
                    a2part1groupdata.setAnticipatedVacancy01032020(rs.getString("anticipated_vacancy"));
                    if (a2part1groupdata.getVacancy01032020() != null && !a2part1groupdata.getVacancy01032020().equals("")) {
                        vacancy = Integer.parseInt(a2part1groupdata.getVacancy01032020());
                    }
                    if (a2part1groupdata.getAnticipatedVacancy01032020() != null && !a2part1groupdata.getAnticipatedVacancy01032020().equals("")) {
                        anticipatedvacancy = Integer.parseInt(a2part1groupdata.getAnticipatedVacancy01032020());
                    }
                    //int totalVacancy = vacancy + anticipatedvacancy;
                    a2part1groupdata.setTotalVacany(rs.getString("total_vacancy"));

                    a2part1groupdata.setMenInPosition01032020(rs.getString("men_in_position"));
                    a2part1groupdata.setVacancytobefilled(rs.getString("vacancy_to_be_filled"));
                    a2part1groupdata.setAnticipatedMenInPosition(rs.getString("anticipated_men_in_position"));
                    annexureIIAPart1GroupList.add(a2part1groupdata);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureIIAPart1GroupList;
    }

    private List getAnnexureIIAPart2EmployeeDetails(String budgetid, String chartacc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Annexture11APart2EmployeeDetails a2part2 = null;

        List a2part2List = new ArrayList();

        double curbasic = 0;
        double basicnextyear = 0;
        double totalYearlyRequirement = 0;
        double arrearpay = 0;
        double da = 0;
        double hra = 0;
        double oa = 0;
        double rcm = 0;
        double total = 0;

        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select * from hrmis.budget_annexture_1b where budget_id=? and chart_of_account=? order by name_of_increment";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                pst.setString(2, chartacc);
                rs = pst.executeQuery();
                while (rs.next()) {
                    curbasic = 0;
                    basicnextyear = 0;
                    totalYearlyRequirement = 0;
                    arrearpay = 0;
                    da = 0;
                    hra = 0;
                    oa = 0;
                    rcm = 0;
                    total = 0;

                    a2part2 = new Annexture11APart2EmployeeDetails();
                    a2part2.setDetailid(rs.getString("annexture1b_id"));
                    a2part2.setEmpid(rs.getString("hrms_id"));
                    a2part2.setEmpname(rs.getString("name_of_increment"));
                    a2part2.setGroup(rs.getString("group_details"));
                    a2part2.setGp(rs.getString("grade"));
                    a2part2.setCurBasicPay(rs.getString("basic_pay"));
                    a2part2.setBasic0103nextyear(rs.getString("basic_pay_next_year"));

                    if (a2part2.getCurBasicPay() != null && !a2part2.getCurBasicPay().equals("")) {
                        curbasic = Double.parseDouble(a2part2.getCurBasicPay());
                    }

                    if (a2part2.getBasic0103nextyear() != null && !a2part2.getBasic0103nextyear().equals("")) {
                        basicnextyear = Double.parseDouble(a2part2.getBasic0103nextyear());
                    }

                    //totalYearlyRequirement = curbasic * 12;
                    //a2part2.setTotalYearlyRequirementPay136(totalYearlyRequirement + "");
                    a2part2.setTotalYearlyRequirementPay136(rs.getString("total_yearly"));
                    if (a2part2.getTotalYearlyRequirementPay136() != null && !a2part2.getTotalYearlyRequirementPay136().equals("")) {
                        totalYearlyRequirement = Double.parseDouble(a2part2.getTotalYearlyRequirementPay136());
                    }

                    a2part2.setArrearPay855(rs.getString("arrear_pay_855"));
                    if (a2part2.getArrearPay855() != null && !a2part2.getArrearPay855().equals("")) {
                        arrearpay = Double.parseDouble(a2part2.getArrearPay855());
                    }
                    a2part2.setDa156(rs.getString("da_156"));
                    if (a2part2.getDa156() != null && !a2part2.getDa156().equals("")) {
                        da = Double.parseDouble(a2part2.getDa156());
                    }
                    a2part2.setHra403(rs.getString("hra_403"));
                    if (a2part2.getHra403() != null && !a2part2.getHra403().equals("")) {
                        hra = Double.parseDouble(a2part2.getHra403());
                    }
                    a2part2.setOa523(rs.getString("oa_523"));
                    if (a2part2.getOa523() != null && !a2part2.getOa523().equals("")) {
                        oa = Double.parseDouble(a2part2.getOa523());
                    }
                    a2part2.setRcm516(rs.getString("rcm_516"));
                    if (a2part2.getRcm516() != null && !a2part2.getRcm516().equals("")) {
                        rcm = Double.parseDouble(a2part2.getRcm516());
                    }
                    //total = totalYearlyRequirement + arrearpay + da + hra + oa + rcm;
                    a2part2.setTotal(rs.getString("total"));

                    a2part2List.add(a2part2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a2part2List;
    }

    private void getAnnexureIIACData(Annexture11APart1 a2part1, String budgetid, String chartacc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String a2acid = "";

        double additionalAmount = 0;
        double exlusionAmount = 0;
        double totalAmount = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from hrmis.budget_annexture_1c where budget_id=? and chart_of_account=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(budgetid));
            pst.setString(2, chartacc);
            rs = pst.executeQuery();
            if (rs.next()) {
                a2acid = rs.getString("annexure1c_id");
                a2part1.setAnnexureIIAPart3DetailId(a2acid);

                a2part1.setCurBasicTotal(Double.valueOf(rs.getString("basic_pay")).longValue() + "");
                a2part1.setTotalYearlyRequirementPay136Total(Double.valueOf(rs.getString("total_yearly_requirement")).longValue() + "");
                a2part1.setArrearPay855Total(Double.valueOf(rs.getString("arrear_pay_855")).longValue() + "");
                a2part1.setDa156Total(Double.valueOf(rs.getString("da_156")).longValue() + "");
                a2part1.setHra403Total(Double.valueOf(rs.getString("hra_403")).longValue() + "");
                a2part1.setOa523Total(Double.valueOf(rs.getString("oa_523")).longValue() + "");
                a2part1.setRcm516Total(Double.valueOf(rs.getString("rcm_516")).longValue() + "");
                a2part1.setTotalTotal(Double.valueOf(rs.getString("total")).longValue() + "");

                a2part1.setAdditionalAmount(rs.getString("additional_amount"));
                if (a2part1.getAdditionalAmount() != null && !a2part1.getAdditionalAmount().equals("")) {
                    additionalAmount = Integer.parseInt(a2part1.getAdditionalAmount());
                }
                a2part1.setExclusionAmount(rs.getString("exclusion_for_incubents"));
                if (a2part1.getExclusionAmount() != null && !a2part1.getExclusionAmount().equals("")) {
                    exlusionAmount = Integer.parseInt(a2part1.getExclusionAmount());
                }
                totalAmount = additionalAmount + exlusionAmount;
                a2part1.setTotalAdditionalExclusionAmount(Double.valueOf(totalAmount).longValue() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAnnexureIIBData(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureIIBdataList = new ArrayList();
        Annexture11B annexure2b = null;
        List annexureIIBpostlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select chart_of_account from hrmis.budget_annexture_2 where budget_id=? group by chart_of_account";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                rs = pst.executeQuery();
                while (rs.next()) {
                    annexure2b = new Annexture11B();
                    annexure2b.setChartOfAcc(rs.getString("chart_of_account"));

                    annexureIIBpostlist = new ArrayList();
                    annexureIIBpostlist = getAnnexureIIBPostList(budgetid, rs.getString("chart_of_account"));

                    annexure2b.setAnnexureIIBpostlist(annexureIIBpostlist);

                    annexureIIBdataList.add(annexure2b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureIIBdataList;
    }

    private List getAnnexureIIBPostList(String budgetid, String chartacc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureIIBpostlist = new ArrayList();
        Annexture11BPostDetails annexure2bpostdetails = null;
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select * from hrmis.budget_annexture_2 where budget_id=? and chart_of_account=? order by post_name";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                pst.setString(2, chartacc);
                rs = pst.executeQuery();
                while (rs.next()) {
                    annexure2bpostdetails = new Annexture11BPostDetails();
                    annexure2bpostdetails.setDetailid(rs.getString("annexture2_id"));
                    annexure2bpostdetails.setPostname(rs.getString("post_name"));
                    annexure2bpostdetails.setNoOfPost31032020(rs.getString("men_in_position"));
                    annexure2bpostdetails.setIncreaseDecreaseMenInPosition01042020(rs.getString("no_of_post_incr_decr"));
                    annexure2bpostdetails.setTotalMenInPosition01042021(rs.getString("total_men_in_position"));
                    annexure2bpostdetails.setActualExpenditure201920(rs.getString("actual_expecting_previous_year"));
                    annexure2bpostdetails.setActualExpenditure202021(rs.getString("actual_expecting_current_year"));
                    annexure2bpostdetails.setRevisedEstimate202021(rs.getString("revised_estimated"));
                    annexure2bpostdetails.setBe202122(rs.getString("be_next_year"));
                    annexureIIBpostlist.add(annexure2bpostdetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureIIBpostlist;
    }

    @Override
    public void lockDDOBudget(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "update hrmis.annual_budget set is_locked='Y',lock_date=? where budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(2, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAnnexureIEmployeeBudgetData(String budgetid, String detailid, String empid, String arrear, String oa, String rcm, String basicNextYear, String daparam, String hradata) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int basicNextYeardata = 0;
        int dadata = 0;

        int arreardata = 0;
        int oadata = 0;
        int rcmdata = 0;
        int hraData = 0;
        try {
            con = this.dataSource.getConnection();

            if (basicNextYear != null && !basicNextYear.equals("")) {
                basicNextYeardata = Integer.parseInt(basicNextYear);
            }
            if (daparam != null && !daparam.equals("")) {
                dadata = Integer.parseInt(daparam);
            }

            if (arrear != null && !arrear.equals("")) {
                arreardata = Integer.parseInt(arrear);
            }
            if (oa != null && !oa.equals("")) {
                oadata = Integer.parseInt(oa);
            }
            if (rcm != null && !rcm.equals("")) {
                rcmdata = Integer.parseInt(rcm);
            }
            if (hradata != null && !hradata.equals("")) {
                hraData = Integer.parseInt(hradata);
            }
            if (detailid != null && !detailid.equals("")) {
                String sql = "update hrmis.budget_annexture_1b set arrear_pay_855=?,oa_523=?,rcm_516=?,da_156=?,basic_pay_next_year=?, hra_403=? where annexture1b_id=? and hrms_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, arreardata);
                pst.setInt(2, oadata);
                pst.setInt(3, rcmdata);
                pst.setInt(4, dadata);
                pst.setInt(5, basicNextYeardata);
                pst.setInt(6, hraData);
                pst.setInt(7, Integer.parseInt(detailid));
                pst.setString(8, empid);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String chartacc = "";

                sql = "select chart_of_account from hrmis.budget_annexture_1b where budget_id=? and annexture1b_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                pst.setInt(2, Integer.parseInt(detailid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    chartacc = rs.getString("chart_of_account");
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);
                if (chartacc != null && !chartacc.equals("")) {

                    double basic_pay_3 = 0;
                    double total_yearly_requirement_3 = 0;
                    double arrear_pay_855_3 = 0;
                    double da_156_3 = 0;
                    double hra_403_3 = 0;
                    double oa_523_3 = 0;
                    double rcm_516_3 = 0;
                    double total_3 = 0;

                    double pay = 0;
                    double totalYearlyIncome = 0;
                    double arrear_pay_855 = 0;
                    double da = 0;
                    double hra_403 = 0;
                    double oa_523 = 0;
                    double rcm_516 = 0;
                    double total = 0;

                    sql = "select * from hrmis.budget_annexture_1b where budget_id=? and chart_of_account=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.setString(2, chartacc);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        int annexture_1b_id = rs.getInt("annexture1b_id");

                        pay = rs.getInt("basic_pay");
                        totalYearlyIncome = rs.getInt("total_yearly");
                        arrear_pay_855 = rs.getInt("arrear_pay_855");
                        da = rs.getInt("da_156");
                        hra_403 = rs.getInt("hra_403");
                        oa_523 = rs.getInt("oa_523");
                        rcm_516 = rs.getInt("rcm_516");
                        //total = rs.getInt("total");

                        total = totalYearlyIncome + arrear_pay_855 + da + hra_403 + oa_523 + rcm_516;

                        pst1 = con.prepareStatement("update hrmis.budget_annexture_1b set total=? where annexture1b_id=?");
                        pst1.setInt(1, (int) total);
                        pst1.setInt(2, annexture_1b_id);
                        pst1.executeUpdate();

                        basic_pay_3 = basic_pay_3 + pay;
                        total_yearly_requirement_3 = total_yearly_requirement_3 + totalYearlyIncome;
                        arrear_pay_855_3 = arrear_pay_855_3 + arrear_pay_855;
                        da_156_3 = da_156_3 + da;
                        hra_403_3 = hra_403_3 + hra_403;
                        oa_523_3 = oa_523_3 + oa_523;
                        rcm_516_3 = rcm_516_3 + rcm_516;
                        total_3 = total_3 + total;
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    sql = "update hrmis.budget_annexture_1c set basic_pay=?,total_yearly_requirement=?,arrear_pay_855=?,da_156=?,hra_403=?,oa_523=?,rcm_516=?,total=? where budget_id=? and chart_of_account=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, (int) basic_pay_3);
                    pst.setInt(2, (int) total_yearly_requirement_3);
                    pst.setInt(3, (int) arrear_pay_855_3);
                    pst.setInt(4, (int) da_156_3);
                    pst.setInt(5, (int) hra_403_3);
                    pst.setInt(6, (int) oa_523_3);
                    pst.setInt(7, (int) rcm_516_3);
                    pst.setInt(8, (int) total_3);
                    pst.setInt(9, Integer.parseInt(budgetid));
                    pst.setString(10, chartacc);
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAdditionalAmountData(String budgetid, String detailid, String amount) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int additionalamount = 0;

        try {
            con = this.dataSource.getConnection();

            if (amount != null && !amount.equals("")) {
                additionalamount = Integer.parseInt(amount);
            }

            if (detailid != null && !detailid.equals("")) {
                String sql = "update hrmis.budget_annexture_1c set additional_amount=? where annexure1c_id=? and budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, additionalamount);
                pst.setInt(2, Integer.parseInt(detailid));
                pst.setInt(3, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateExclusionsData(String budgetid, String detailid, String amount) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int exclusionamount = 0;

        try {
            con = this.dataSource.getConnection();

            if (amount != null && !amount.equals("")) {
                exclusionamount = Integer.parseInt(amount);
            }

            if (detailid != null && !detailid.equals("")) {
                String sql = "update hrmis.budget_annexture_1c set exclusion_for_incubents=? where annexure1c_id=? and budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, exclusionamount);
                pst.setInt(2, Integer.parseInt(detailid));
                pst.setInt(3, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateContractualData(String budgetid, String detailid, String incrDcrMenInPositionData, String actualExp201920Data, String actualExp202021Data, String revisedEstimate202021Data, String be202122Data, String txtTotalMenInPositionData) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int incrDcrMenInPosition = 0;
        int totalMenInPosition = 0;
        int actualExpPreviousYear = 0;
        int actualExpCurrentYear = 0;
        int revisedEstimateCurrentYear = 0;
        int beNextYear = 0;

        try {
            con = this.dataSource.getConnection();

            if (incrDcrMenInPositionData != null && !incrDcrMenInPositionData.equals("")) {
                incrDcrMenInPosition = Integer.parseInt(incrDcrMenInPositionData);
            }
            if (txtTotalMenInPositionData != null && !txtTotalMenInPositionData.equals("")) {
                totalMenInPosition = Integer.parseInt(txtTotalMenInPositionData);
            }
            if (actualExp201920Data != null && !actualExp201920Data.equals("")) {
                actualExpPreviousYear = Integer.parseInt(actualExp201920Data);
            }
            if (actualExp202021Data != null && !actualExp202021Data.equals("")) {
                actualExpCurrentYear = Integer.parseInt(actualExp202021Data);
            }
            if (revisedEstimate202021Data != null && !revisedEstimate202021Data.equals("")) {
                revisedEstimateCurrentYear = Integer.parseInt(revisedEstimate202021Data);
            }
            if (be202122Data != null && !be202122Data.equals("")) {
                beNextYear = Integer.parseInt(be202122Data);
            }

            if (detailid != null && !detailid.equals("")) {
                String sql = "update hrmis.budget_annexture_2 set no_of_post_incr_decr=?,total_men_in_position=?,actual_expecting_previous_year=?,actual_expecting_current_year=?,revised_estimated=?,be_next_year=? where annexture2_id=? and budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, incrDcrMenInPosition);
                pst.setInt(2, totalMenInPosition);
                pst.setInt(3, actualExpPreviousYear);
                pst.setInt(4, actualExpCurrentYear);
                pst.setInt(5, revisedEstimateCurrentYear);
                pst.setInt(6, beNextYear);
                pst.setInt(7, Integer.parseInt(detailid));
                pst.setInt(8, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /* =====================================  FOR rest service code is to be start HERE  =============================*/
    @Override
    public List<Annexure1a> getAnnexure1Adata(String ddoCode, String fy) {
        List<Annexure1a> annexure1a = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Annexure1a a1a = new Annexure1a();
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("select ab1a.ddo_code, chart_of_account, group_details, sanction_strength, vacancy, "
                    + " anticipated_vacancy, total_vacancy, men_in_position, vacancy_to_be_filled, anticipated_men_in_position "
                    + " from hrmis.annual_budget ab "
                    + " inner join hrmis.budget_annexture_1a ab1a on ab.budget_id=ab1a.budget_id "
                    + " where ab.ddo_code=? and fy=? ");

            ps.setString(1, ddoCode);
            ps.setString(2, fy);
            rs = ps.executeQuery();
            while (rs.next()) {
                a1a = new Annexure1a();
                a1a.setDdoCode(rs.getString("ddo_code"));
                a1a.setHoa(rs.getString("chart_of_account"));
                a1a.setGroupDetails(rs.getString("group_details"));
                a1a.setSanctionStrength(rs.getString("sanction_strength"));
                a1a.setVacancy(rs.getString("vacancy"));
                a1a.setAnticipatedVacancy(rs.getString("anticipated_vacancy"));
                a1a.setTotalVacancy(rs.getString("total_vacancy"));
                a1a.setMenInPosition(rs.getString("men_in_position"));
                a1a.setVacancyNextYear(rs.getString("vacancy_to_be_filled"));
                a1a.setMenInPositionAnticipated(rs.getString("anticipated_men_in_position"));
                annexure1a.add(a1a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return annexure1a;
    }

    @Override
    public List<Annexure1b> getAnnexure1Bdata(String ddoCode, String fy) {
        List<Annexure1b> annexure1b = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Annexure1b a1a = new Annexure1b();
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select b1.ddo_code, chart_of_account, group_details, name_of_increment, hrms_id, grade, basic_pay, total_yearly,  "
                    + "	arrear_pay_855, da_156, hra_403, oa_523, rcm_516, total,basic_pay_next_year from hrmis.annual_budget ab  "
                    + " inner join hrmis.budget_annexture_1b b1 on ab.budget_id=b1.budget_id  "
                    + " where ab.ddo_code=? and fy=? ");
            ps.setString(1, ddoCode);
            ps.setString(2, fy);
            rs = ps.executeQuery();
            while (rs.next()) {
                a1a = new Annexure1b();
                a1a.setDdoCode(rs.getString("ddo_code"));
                a1a.setHoa(rs.getString("chart_of_account"));
                a1a.setGroupDetails(rs.getString("group_details"));
                a1a.setIncumbentName(rs.getString("name_of_increment"));
                a1a.setEmployeeId(rs.getString("hrms_id"));
                a1a.setGrade(rs.getString("grade"));
                a1a.setBasicPay(rs.getString("basic_pay"));
                a1a.setTotalPayRequired(rs.getString("total_yearly"));
                a1a.setArrear(rs.getString("arrear_pay_855"));
                a1a.setDa(rs.getString("da_156"));
                a1a.setHra(rs.getString("hra_403"));
                a1a.setOa(rs.getString("oa_523"));
                a1a.setRcm(rs.getString("rcm_516"));
                a1a.setTotal(rs.getString("total"));
                a1a.setBasicPayNextYear(rs.getString("basic_pay_next_year"));
                annexure1b.add(a1a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return annexure1b;
    }

    @Override
    public List<Annexure1c> getAnnexure1Cdata(String ddoCode, String fy) {
        List<Annexure1c> annexure1c = new ArrayList<Annexure1c>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Annexure1c a1c = new Annexure1c();
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select ab.ddo_code, chart_of_account, additional_amount,"
                    + " exclusion_for_incubents, basic_pay, total_yearly_requirement,"
                    + " arrear_pay_855, da_156, hra_403, oa_523, rcm_516, total from hrmis.annual_budget ab "
                    + " inner join hrmis.budget_annexture_1c c1 on ab.budget_id=c1.budget_id "
                    + " where ab.ddo_code=? and fy=? ");
            ps.setString(1, ddoCode);
            ps.setString(2, fy);
            rs = ps.executeQuery();
            while (rs.next()) {

                a1c = new Annexure1c();
                a1c.setDdoCode(rs.getString("ddo_code"));
                a1c.setHoa(rs.getString("chart_of_account"));
                a1c.setAdditionalAmount(rs.getString("additional_amount"));
                a1c.setExclusions(rs.getString("exclusion_for_incubents"));
                a1c.setBasicPay(rs.getString("basic_pay"));
                a1c.setTotalPayRequirement(rs.getString("total_yearly_requirement"));
                a1c.setArrearPay(rs.getString("arrear_pay_855"));
                a1c.setDa(rs.getString("da_156"));
                a1c.setHra(rs.getString("hra_403"));
                a1c.setOa(rs.getString("oa_523"));
                a1c.setRcm(rs.getString("rcm_516"));
                a1c.setTotal(rs.getString("total"));
                a1c.setBasicPayNextYear("0");                
                annexure1c.add(a1c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexure1c;
    }

    @Override
    public List<Annexure2> getAnnexure2data(String ddoCode, String fy) {
        List<Annexure2> annexure2 = new ArrayList<Annexure2>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Annexure2 a2 = new Annexure2();
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement(" select ab.ddo_code, chart_of_account, post_name, no_of_post_incr_decr, men_in_position,  "
                    + " total_men_in_position, actual_expecting_previous_year, actual_expecting_current_year, revised_estimated, be_next_year "
                    + " from hrmis.annual_budget ab  "
                    + " inner join hrmis.budget_annexture_2 ab1a on ab.budget_id=ab1a.budget_id  "
                    + " where ab.ddo_code=? and fy=? ");
            ps.setString(1, ddoCode);
            ps.setString(2, fy);
            rs = ps.executeQuery();
            while (rs.next()) {
                a2 = new Annexure2();
                a2.setDdoCode(rs.getString("ddo_code"));
                a2.setHoa(rs.getString("chart_of_account"));
                a2.setPostName(rs.getString("post_name"));
                a2.setNoOfPost(rs.getString("men_in_position"));
                a2.setManInPositionChange(rs.getString("no_of_post_incr_decr"));
                a2.setManInPositionTotal(rs.getString("total_men_in_position"));
                a2.setActualExpenditure1(rs.getString("actual_expecting_previous_year"));
                a2.setActualExpenditure2(rs.getString("actual_expecting_current_year"));
                a2.setRevisedEstimate(rs.getString("revised_estimated"));
                a2.setBe(rs.getString("be_next_year"));
                annexure2.add(a2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexure2;
    }

    @Override
    public String sendBudgetDataToIFMS(String jsonstring, String fileKeyPath) {
        String finalMsg = "";
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("***" + jsonstring);
        try {
            final byte[] secretKey = CommonFunctions.readKeyFromFile(fileKeyPath);
            System.out.println("key " + CommonFunctions.getBase64String(secretKey));
            /*------*/
            final String hmac = CommonFunctions.getBase64String(CommonFunctions.genHmac(jsonstring.getBytes("UTF-8"), secretKey));
            System.out.println("hmac***" + hmac);
            String encryptedText = CommonFunctions.getBase64String(CommonFunctions.encrypt(jsonstring.getBytes("UTF-8"), secretKey));
            finalMsg = "";
            Map<String, String> map = new HashMap<String, String>();
            map.put("data", encryptedText);
            map.put("hmac", hmac);
            finalMsg = mapper.writeValueAsString(map);
            System.out.println(finalMsg);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return finalMsg;
    }

    @Override
    public String getDDOBudgetLockStatus(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String lockstatus = "N";
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select is_locked from hrmis.annual_budget where budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    lockstatus = rs.getString("is_locked");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lockstatus;
    }

    @Override
    public String getDDOBudgetFinancialYear(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String fy = "";
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "select fy from hrmis.annual_budget where budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    fy = rs.getString("fy");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fy;
    }

    @Override
    public void updateBudgetAbstractData(String budgetid, String detailid, String anticipatedVacancyData, String vacancytobefilledData, String anticipatedMenInPositionData, String currentVacancyData,String sanctionedStrengthData) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        int sanctionedStrength = 0;
        int currentVacancy = 0;
        int anticipatedVacancy = 0;
        int vacancytobefilled = 0;
        int anticipatedMenInPosition = 0;
        
        int totalvacancy = 0;
        int meninposition = 0;
        
        try {
            con = this.dataSource.getConnection();
            
            if (sanctionedStrengthData != null && !sanctionedStrengthData.equals("")) {
                sanctionedStrength = Integer.parseInt(sanctionedStrengthData);
            }
            if (currentVacancyData != null && !currentVacancyData.equals("")) {
                currentVacancy = Integer.parseInt(currentVacancyData);
            }
            if (anticipatedVacancyData != null && !anticipatedVacancyData.equals("")) {
                anticipatedVacancy = Integer.parseInt(anticipatedVacancyData);
            }
            if (vacancytobefilledData != null && !vacancytobefilledData.equals("")) {
                vacancytobefilled = Integer.parseInt(vacancytobefilledData);
            }
            if (anticipatedMenInPositionData != null && !anticipatedMenInPositionData.equals("")) {
                anticipatedMenInPosition = Integer.parseInt(anticipatedMenInPositionData);
            }
            
            totalvacancy = currentVacancy + anticipatedVacancy;
            meninposition = sanctionedStrength - totalvacancy;
            
            if (detailid != null && !detailid.equals("")) {
                String sql = "update hrmis.budget_annexture_1a set anticipated_vacancy=?,vacancy_to_be_filled=?,anticipated_men_in_position=?,vacancy=?,total_vacancy=?,men_in_position=? where annexture1a_id=? and budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, anticipatedVacancy);
                pst.setInt(2, vacancytobefilled);
                pst.setInt(3, anticipatedMenInPosition);
                pst.setInt(4, currentVacancy);
                pst.setInt(5, totalvacancy);
                pst.setInt(6, meninposition);
                pst.setInt(7, Integer.parseInt(detailid));
                pst.setInt(8, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateDDOBudgetAfterSubmit(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "update hrmis.annual_budget set is_submitted='Y',submitted_on=?,status=?,is_reverted='N',revert_date=null,revert_reason=null where budget_id=?";
                pst = con.prepareStatement(sql);
                pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(2, "Submitted");
                pst.setInt(3, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteDDOAnnualBudget(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            if (budgetid != null && !budgetid.equals("")) {
                pst = con.prepareStatement("select budget_id from hrmis.annual_budget where budget_id=?");
                pst.setInt(1, Integer.parseInt(budgetid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1a where budget_id=?");
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1b where budget_id=?");
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1c where budget_id=?");
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.budget_annexture_2 where budget_id=?");
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.annual_budget where budget_id=?");
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void ReProcessBudgetAnnexureIIAData(String fy, String ddocode, String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement pst3 = null;
        ResultSet rs3 = null;

        PreparedStatement insertpst = null;

        String chartAcc = "";

        //String officecode = "";
        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String objectHead = "";

        String[] postGroupType = {"A", "B", "C", "D"};

        int sancStrength = 0;
        int menInPosition = 0;
        int vacancy = 0;
        int anticipatedvacancy = 0;
        int totalvacancy = 0;
        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {

                int previousBudgetId = Integer.parseInt(budgetid);

                pst = con.prepareStatement("select budget_id from hrmis.annual_budget where budget_id=?");
                pst.setInt(1, previousBudgetId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1a where budget_id=?");
                    pst.setInt(1, previousBudgetId);
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1b where budget_id=?");
                    pst.setInt(1, previousBudgetId);
                    pst.executeUpdate();

                    pst = con.prepareStatement("delete from hrmis.budget_annexture_1c where budget_id=?");
                    pst.setInt(1, previousBudgetId);
                    pst.executeUpdate();

                    /*Annexure IA*/
                    String sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                            + " inner join g_spc on section_post_mapping.spc=g_spc.spc"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                            + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                            + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and g_spc.post_grp=? and bill_group_master.pay_head=?"
                            + " group by bill_group_master.bill_group_id";
                    pst2 = con.prepareStatement(sql2);

                    String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head from emp_mast"
                            + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                            + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                            + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head "
                            + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";

                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, ddocode);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        //officecode = rs1.getString("off_code").trim();
                        demandNo = rs1.getString("DEMAND_NO").trim();
                        majorHead = rs1.getString("MAJOR_HEAD").trim();
                        subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                        minorHead = rs1.getString("MINOR_HEAD").trim();
                        minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                        minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                        minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                        plan = rs1.getString("PLAN").trim();
                        sector = rs1.getString("SECTOR").trim();
                        objectHead = rs1.getString("pay_head").trim();

                        chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                        for (int i = 0; i < postGroupType.length; i++) {

                            sancStrength = 0;
                            menInPosition = 0;

                            String tempPostGroup = postGroupType[i];

                            pst2.setString(1, ddocode);
                            pst2.setString(2, demandNo);
                            pst2.setString(3, majorHead);
                            pst2.setString(4, subMajorHead);
                            pst2.setString(5, minorHead);
                            pst2.setString(6, minorHead1);
                            pst2.setString(7, minorHead2);
                            pst2.setString(8, minorHead3);
                            pst2.setString(9, plan);
                            pst2.setString(10, sector);
                            pst2.setString(11, tempPostGroup);
                            pst2.setString(12, objectHead);
                            rs2 = pst2.executeQuery();
                            while (rs2.next()) {
                                sancStrength = sancStrength + getSanctionedStrength(con, ddocode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                                menInPosition = menInPosition + getMenInPosition(con, ddocode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                            }
                            //System.out.println(chartAcc + " and " + tempPostGroup + " and sancStrength is: " + sancStrength);
                            //System.out.println(chartAcc + " and " + tempPostGroup + " and menInPosition is: " + menInPosition);
                            vacancy = sancStrength - menInPosition;
                            anticipatedvacancy = vacancy;
                            totalvacancy = vacancy + anticipatedvacancy;
                            //System.out.println("chartAcc is: " + chartAcc);
                            //System.out.println("chartAcc length is: " + chartAcc.length());
                            String insertquery = "insert into hrmis.budget_annexture_1a(ddo_code,chart_of_account,group_details,sanction_strength,vacancy,anticipated_vacancy,total_vacancy,men_in_position,vacancy_to_be_filled,anticipated_men_in_position,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                            insertpst = con.prepareStatement(insertquery);
                            insertpst.setString(1, ddocode);
                            insertpst.setString(2, chartAcc.trim());
                            insertpst.setString(3, tempPostGroup);
                            insertpst.setInt(4, sancStrength);
                            insertpst.setInt(5, vacancy);
                            insertpst.setInt(6, anticipatedvacancy);
                            insertpst.setInt(7, totalvacancy);
                            insertpst.setInt(8, menInPosition);
                            insertpst.setInt(9, vacancy);
                            insertpst.setInt(10, menInPosition);
                            insertpst.setInt(11, previousBudgetId);
                            if (chartAcc != null && chartAcc.length() == 36) {
                                insertpst.executeUpdate();
                            }
                        }
                    }

                    /*Annexure IB and IC*/
                    String postgrp = "";
                    String empname = "";
                    String empid = "";
                    double grade = 0;
                    int paycommission = 0;
                    double pay = 0;
                    double payNextYear = 0;
                    double totalYearlyIncome = 0;
                    double arrear_pay_855 = 0;
                    double da = 0;
                    double hra_403 = 0;
                    double oa_523 = 0;
                    double rcm_516 = 0;
                    double total = 0;

                    double basic_pay_3 = 0;
                    double total_yearly_requirement_3 = 0;
                    double arrear_pay_855_3 = 0;
                    double da_156_3 = 0;
                    double hra_403_3 = 0;
                    double oa_523_3 = 0;
                    double rcm_516_3 = 0;
                    double total_3 = 0;

                    String sql3 = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,f_name,post_grp_type,post_grp,cur_basic_salary,fixedvalue,emp_mast.gp,emp_mast.pay_commission from emp_mast"
                            + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                            + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                            + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                            + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                            + " left outer join (select fixedvalue,updation_ref_code from update_ad_info where where_updated='E' and ref_ad_code='53')update_ad_info on emp_mast.emp_id=update_ad_info.updation_ref_code"
                            + " where bill_group_master.bill_group_id=? order by f_name";
                    pst3 = con.prepareStatement(sql3);

                    sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                            + " inner join g_spc on section_post_mapping.spc=g_spc.spc"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                            + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                            + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and bill_group_master.pay_head=?"
                            + " group by bill_group_master.bill_group_id";
                    pst2 = con.prepareStatement(sql2);

                    sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head from emp_mast"
                            + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY')"
                            + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                            + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR, bill_group_master.pay_head"
                            + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, ddocode);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        //officecode = rs1.getString("off_code").trim();
                        demandNo = rs1.getString("DEMAND_NO").trim();
                        majorHead = rs1.getString("MAJOR_HEAD").trim();
                        subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                        minorHead = rs1.getString("MINOR_HEAD").trim();
                        minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                        minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                        minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                        plan = rs1.getString("PLAN").trim();
                        sector = rs1.getString("SECTOR").trim();
                        objectHead = rs1.getString("pay_head").trim();

                        basic_pay_3 = 0;
                        total_yearly_requirement_3 = 0;
                        arrear_pay_855_3 = 0;
                        da_156_3 = 0;
                        hra_403_3 = 0;
                        oa_523_3 = 0;
                        rcm_516_3 = 0;
                        total_3 = 0;

                        chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                        //String postGroup = postGroupTypeArr[i];
                        pst2.setString(1, ddocode);
                        pst2.setString(2, demandNo);
                        pst2.setString(3, majorHead);
                        pst2.setString(4, subMajorHead);
                        pst2.setString(5, minorHead);
                        pst2.setString(6, minorHead1);
                        pst2.setString(7, minorHead2);
                        pst2.setString(8, minorHead3);
                        pst2.setString(9, plan);
                        pst2.setString(10, sector);
                        pst2.setString(11, objectHead);

                        rs2 = pst2.executeQuery();
                        while (rs2.next()) {
                            int ctr = 0;

                            pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                            rs3 = pst3.executeQuery();
                            while (rs3.next()) {

                                postgrp = rs3.getString("post_grp");
                                empname = rs3.getString("EMPNAME");
                                empid = rs3.getString("emp_id");

                                paycommission = rs3.getInt("pay_commission");

                                grade = rs3.getInt("gp");
                                pay = rs3.getInt("cur_basic_salary");
                                payNextYear = pay;
                                rcm_516 = 2000;

                                totalYearlyIncome = 0;
                                if (rs3.getString("cur_basic_salary") != null && !rs3.getString("cur_basic_salary").equals("")) {
                                    totalYearlyIncome = Double.parseDouble(rs3.getString("cur_basic_salary"));
                                }
                                totalYearlyIncome = totalYearlyIncome * 12;

                                da = 0;
                                if (totalYearlyIncome > 0) {
                                    if (paycommission == 7) {
                                        da = pay * 0.55;
                                    } else if (paycommission == 6) {
                                        da = (pay + grade) * 2.14;
                                    } else if (paycommission == 5) {
                                        da = (pay + grade) * 2.64;
                                    }
                                }
                                da = da * 12;

                                hra_403 = rs3.getInt("fixedvalue");
                                hra_403 = hra_403 * 12;

                                total = totalYearlyIncome + arrear_pay_855 + da + hra_403 + oa_523 + rcm_516;

                                String insertquery = "insert into hrmis.budget_annexture_1b(ddo_code,chart_of_account,group_details,name_of_increment,hrms_id,grade,basic_pay,total_yearly,arrear_pay_855,da_156,hra_403,oa_523,rcm_516,total,budget_id,basic_pay_next_year) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                insertpst = con.prepareStatement(insertquery);
                                insertpst.setString(1, ddocode);
                                insertpst.setString(2, chartAcc);
                                insertpst.setString(3, postgrp);
                                insertpst.setString(4, empname);
                                insertpst.setString(5, empid);
                                insertpst.setInt(6, (int) grade);
                                insertpst.setInt(7, (int) pay);
                                insertpst.setInt(8, (int) totalYearlyIncome);
                                insertpst.setInt(9, (int) arrear_pay_855);
                                insertpst.setInt(10, (int) da);
                                insertpst.setInt(11, (int) hra_403);
                                insertpst.setInt(12, (int) oa_523);
                                insertpst.setInt(13, (int) rcm_516);
                                insertpst.setInt(14, (int) total);
                                insertpst.setInt(15, previousBudgetId);
                                insertpst.setInt(16, (int) pay);
                                if (chartAcc != null && chartAcc.length() == 36) {
                                    insertpst.executeUpdate();
                                }

                                basic_pay_3 = basic_pay_3 + pay;
                                total_yearly_requirement_3 = total_yearly_requirement_3 + totalYearlyIncome;
                                arrear_pay_855_3 = arrear_pay_855_3 + arrear_pay_855;
                                da_156_3 = da_156_3 + da;
                                hra_403_3 = hra_403_3 + hra_403;
                                oa_523_3 = oa_523_3 + oa_523;
                                rcm_516_3 = rcm_516_3 + rcm_516;
                                total_3 = total_3 + total;

                                postgrp = "";
                                empname = "";
                                empid = "";
                                grade = 0;
                                pay = 0;
                                totalYearlyIncome = 0;
                                arrear_pay_855 = 0;
                                da = 0;
                                hra_403 = 0;
                                oa_523 = 0;
                                rcm_516 = 0;
                                total = 0;
                            }
                        }
                        String insertquery = "insert into hrmis.budget_annexture_1c(ddo_code,chart_of_account,exclusion_for_incubents,basic_pay,total_yearly_requirement,arrear_pay_855,da_156,hra_403,oa_523,rcm_516,total,budget_id,additional_amount) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        insertpst = con.prepareStatement(insertquery);
                        insertpst.setString(1, ddocode);
                        insertpst.setString(2, chartAcc);
                        insertpst.setInt(3, 0);
                        insertpst.setInt(4, (int) basic_pay_3);
                        insertpst.setInt(5, (int) total_yearly_requirement_3);
                        insertpst.setInt(6, (int) arrear_pay_855_3);
                        insertpst.setInt(7, (int) da_156_3);
                        insertpst.setInt(8, (int) hra_403_3);
                        insertpst.setInt(9, (int) oa_523_3);
                        insertpst.setInt(10, (int) rcm_516_3);
                        insertpst.setInt(11, (int) total_3);
                        insertpst.setInt(12, previousBudgetId);
                        insertpst.setInt(13, 0);
                        if (chartAcc != null && chartAcc.length() == 36) {
                            insertpst.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void ReProcessBudgetAnnexureIIBData(String fy, String ddocode, String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement pst3 = null;
        ResultSet rs3 = null;

        PreparedStatement insertpst = null;

        String chartAcc = "";

        //String officecode = "";
        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String objectHead = "";

        String[] postGroupType = {"A", "B", "C", "D"};

        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {

                int previousBudgetId = Integer.parseInt(budgetid);

                pst = con.prepareStatement("select budget_id from hrmis.annual_budget where budget_id=?");
                pst.setInt(1, previousBudgetId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("delete from hrmis.budget_annexture_2 where budget_id=?");
                    pst.setInt(1, previousBudgetId);
                    pst.executeUpdate();

                    /*Annexure IIB*/
                    String sql3 = "select post,count(*) cnt from emp_mast"
                            + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                            + " inner join g_post on g_spc.gpc=g_post.post_code"
                            + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                            + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                            + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                            + " where bill_group_master.bill_group_id=? group by post order by post";
                    pst3 = con.prepareStatement(sql3);

                    String sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                            + " inner join g_spc on section_post_mapping.spc=g_spc.spc"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONT6_REG'"
                            + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                            + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and bill_group_master.pay_head=?"
                            + " group by bill_group_master.bill_group_id";
                    pst2 = con.prepareStatement(sql2);

                    String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head from emp_mast"
                            + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONT6_REG'"
                            + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                            + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head "
                            + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR ";

                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, ddocode);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        //officecode = rs1.getString("off_code").trim();
                        demandNo = rs1.getString("DEMAND_NO").trim();
                        majorHead = rs1.getString("MAJOR_HEAD").trim();
                        subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                        minorHead = rs1.getString("MINOR_HEAD").trim();
                        minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                        minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                        minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                        plan = rs1.getString("PLAN").trim();
                        sector = rs1.getString("SECTOR").trim();
                        objectHead = rs1.getString("pay_head").trim();

                        chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                        //String postGroup = postGroupTypeArr[i];
                        pst2.setString(1, ddocode);
                        pst2.setString(2, demandNo);
                        pst2.setString(3, majorHead);
                        pst2.setString(4, subMajorHead);
                        pst2.setString(5, minorHead);
                        pst2.setString(6, minorHead1);
                        pst2.setString(7, minorHead2);
                        pst2.setString(8, minorHead3);
                        pst2.setString(9, plan);
                        pst2.setString(10, sector);
                        pst2.setString(11, objectHead);

                        rs2 = pst2.executeQuery();
                        while (rs2.next()) {

                            pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                            rs3 = pst3.executeQuery();
                            //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                            while (rs3.next()) {

                                int menInPositionContractual = rs3.getInt("cnt");
                                int noOfPostIncrDecr = rs3.getInt("cnt");
                                int totalMenInPosition = menInPositionContractual + noOfPostIncrDecr;

                                String insertquery = "insert into hrmis.budget_annexture_2(ddo_code,chart_of_account,post_name,men_in_position,no_of_post_incr_decr,total_men_in_position,actual_expecting_previous_year,actual_expecting_current_year,revised_estimated,be_next_year,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                                insertpst = con.prepareStatement(insertquery);
                                insertpst.setString(1, ddocode);
                                insertpst.setString(2, chartAcc);
                                insertpst.setString(3, rs3.getString("post"));
                                insertpst.setInt(4, menInPositionContractual);
                                insertpst.setInt(5, noOfPostIncrDecr);
                                insertpst.setInt(6, totalMenInPosition);
                                insertpst.setInt(7, 0);
                                insertpst.setInt(8, 0);
                                insertpst.setInt(9, 0);
                                insertpst.setInt(10, 0);
                                insertpst.setInt(11, previousBudgetId);
                                if (chartAcc != null && chartAcc.length() == 36) {
                                    insertpst.executeUpdate();
                                }
                            }
                        }
                    }

                    sql3 = "select post_nomenclature from emp_mast"
                            + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
                            + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                            + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                            + " where bill_group_master.bill_group_id=? group by post_nomenclature order by post_nomenclature";
                    pst3 = con.prepareStatement(sql3);

                    sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                            + " inner join emp_mast on section_post_mapping.spc=emp_mast.emp_id"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONTRACTUAL'"
                            + " and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                            + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and pay_head=? group by bill_group_master.bill_group_id";
                    pst2 = con.prepareStatement(sql2);

                    sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head from emp_mast"
                            + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " inner join g_office on g_section.off_code=g_office.off_code"
                            + " where g_office.ddo_code=? and online_bill_submission='Y' and g_section.bill_type='CONTRACTUAL'"
                            + " and (SUB_MINOR_HEAD3 is not null and SUB_MINOR_HEAD3<>'') and is_deleted='N'"
                            + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR,bill_group_master.pay_head"
                            + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, ddocode);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        //officecode = rs1.getString("off_code").trim();
                        demandNo = rs1.getString("DEMAND_NO").trim();
                        majorHead = rs1.getString("MAJOR_HEAD").trim();
                        subMajorHead = rs1.getString("SUB_MAJOR_HEAD").trim();
                        minorHead = rs1.getString("MINOR_HEAD").trim();
                        minorHead1 = rs1.getString("SUB_MINOR_HEAD1").trim();
                        minorHead2 = rs1.getString("SUB_MINOR_HEAD2").trim();
                        minorHead3 = rs1.getString("SUB_MINOR_HEAD3").trim();
                        plan = rs1.getString("PLAN").trim();
                        sector = rs1.getString("SECTOR").trim();
                        objectHead = rs1.getString("pay_head").trim();

                        chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + objectHead + "-" + plan + "-" + minorHead3 + "-" + sector;

                        //String postGroup = postGroupTypeArr[i];
                        pst2.setString(1, ddocode);
                        pst2.setString(2, demandNo);
                        pst2.setString(3, majorHead);
                        pst2.setString(4, subMajorHead);
                        pst2.setString(5, minorHead);
                        pst2.setString(6, minorHead1);
                        pst2.setString(7, minorHead2);
                        pst2.setString(8, minorHead3);
                        pst2.setString(9, plan);
                        pst2.setString(10, sector);
                        pst2.setString(11, objectHead);

                        rs2 = pst2.executeQuery();
                        while (rs2.next()) {

                            pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                            rs3 = pst3.executeQuery();
                            //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                            while (rs3.next()) {
                                int noOfPost = 0;
                                int noOfPostIncrDecr = 0;

                                noOfPost = getContractualNoOfPost(con, new BigDecimal(rs2.getString("bill_group_id")), rs3.getString("post_nomenclature"), ddocode);
                                noOfPostIncrDecr = noOfPost;

                                int totalMenInPosition = noOfPost + noOfPostIncrDecr;

                                String insertquery = "insert into hrmis.budget_annexture_2(ddo_code,chart_of_account,post_name,men_in_position,no_of_post_incr_decr,total_men_in_position,actual_expecting_previous_year,actual_expecting_current_year,revised_estimated,be_next_year,budget_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                                insertpst = con.prepareStatement(insertquery);
                                insertpst.setString(1, ddocode);
                                insertpst.setString(2, chartAcc);
                                insertpst.setString(3, rs3.getString("post_nomenclature"));
                                insertpst.setInt(4, noOfPost);
                                insertpst.setInt(5, noOfPostIncrDecr);
                                insertpst.setInt(6, totalMenInPosition);
                                insertpst.setInt(7, 0);
                                insertpst.setInt(8, 0);
                                insertpst.setInt(9, 0);
                                insertpst.setInt(10, 0);
                                insertpst.setInt(11, previousBudgetId);
                                if (chartAcc != null && chartAcc.length() == 36) {
                                    insertpst.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getDDOAnnualBudgetDataDC(String ddocode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List budgetlist = new ArrayList();
        AnnualBudgetBean abean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from hrmis.annual_budget where ddo_code=? and fy=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                abean = new AnnualBudgetBean();
                abean.setBudgetid(rs.getString("budget_id"));
                abean.setFy(rs.getString("fy"));
                abean.setCreatedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("created_date")));
                abean.setStatus(rs.getString("status"));
                abean.setIsLocked(rs.getString("is_locked"));
                abean.setLockdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lock_date")));
                abean.setIsSubmitted(rs.getString("is_submitted"));
                abean.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                budgetlist.add(abean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return budgetlist;
    }

    @Override
    public void unlockDDOBudgetDC(String budgetid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (budgetid != null && !budgetid.equals("")) {
                String sql = "update hrmis.annual_budget set is_locked='N',lock_date=null where budget_id=? and is_submitted='N'";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(budgetid));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getDDOCodeDistrictDC(String logindistcode, String ddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String distcode = "";
        String viewstatus = "Y";
        try {
            con = this.dataSource.getConnection();

            if (ddocode != null && !ddocode.equals("")) {
                String sql = "select dist_code from g_office where ddo_code=? group by dist_code";
                pst = con.prepareStatement(sql);
                pst.setString(1, ddocode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    distcode = rs.getString("dist_code");
                }
                if (logindistcode != null && !logindistcode.equals("")) {
                    if (distcode != null && !distcode.equals("")) {
                        if (!logindistcode.equals(distcode)) {
                            viewstatus = "N";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return viewstatus;
    }

    @Override
    public String checkDDOAnnualBudgetSubmitStatus(String ddocode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String submitStatus = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from hrmis.annual_budget where ddo_code=? and fy=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_submitted") != null && rs.getString("is_submitted").equals("Y")) {
                    submitStatus = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return submitStatus;
    }

    @Override
    public void downloadAnnualBudgetDataExcel(WritableWorkbook workbook, String budgetid, String chartacc, String type) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;

        String dedtype = type.trim();
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(type, 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont headformat2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat innerheadcell = new WritableCellFormat(headformat2);
            innerheadcell.setAlignment(Alignment.CENTRE);
            innerheadcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innerheadcell.setWrap(true);
            innerheadcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            if (type.equals("PAYNEXT")) {
                dedtype = "Basic Pay Next Year";
            } else if (type.equals("ARR")) {
                dedtype = "Arrear Pay";
            }

            Label label = new Label(0, row, "Key Id", innerheadcell);
            sheet.addCell(label);
            label = new Label(1, row, "HRMS ID", innerheadcell);
            sheet.setColumnView(1, 15);
            sheet.addCell(label);
            label = new Label(2, row, "Name", innerheadcell);
            sheet.setColumnView(2, 30);
            sheet.addCell(label);
            label = new Label(3, row, dedtype, innerheadcell);
            sheet.addCell(label);

            Number num = null;
            
            String sql = "select * from hrmis.budget_annexture_1b where budget_id=? and chart_of_account=? order by name_of_increment";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(budgetid));
            pst.setString(2, chartacc);
            rs = pst.executeQuery();
            while (rs.next()) {
                row = row + 1;

                num = new Number(0, row, rs.getInt("annexture1b_id"), innercell);
                sheet.addCell(num);
                label = new Label(1, row, rs.getString("hrms_id"), innercell);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("name_of_increment"), innercell);
                sheet.addCell(label);
                if (type.equals("PAYNEXT")) {
                    num = new Number(3, row, rs.getInt("basic_pay_next_year"), innercell);
                    sheet.addCell(num);
                } else if (type.equals("ARR")) {
                    num = new Number(3, row, rs.getInt("arrear_pay_855"), innercell);
                    sheet.addCell(num);
                } else if (type.equals("DA")) {
                    num = new Number(3, row, rs.getInt("da_156"), innercell);
                    sheet.addCell(num);
                } else if (type.equals("HRA")) {
                    num = new Number(3, row, rs.getInt("hra_403"), innercell);
                    sheet.addCell(num);
                } else if (type.equals("OA")) {
                    num = new Number(3, row, rs.getInt("oa_523"), innercell);
                    sheet.addCell(num);
                } else if (type.equals("RCM")) {
                    num = new Number(3, row, rs.getInt("rcm_516"), innercell);
                    sheet.addCell(num);
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void uploadAnnualBudgetDataExcel(Workbook workbook, String budgetid, String type, String ddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        String dedtype = "";
        int amount = 0;

        int updateid = 0;
        String empid = "";

        try {
            con = this.dataSource.getConnection();

            String sql = "";

            if (type.equals("PAYNEXT")) {
                sql = "update hrmis.budget_annexture_1b set basic_pay_next_year=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            } else if (type.equals("ARR")) {
                sql = "update hrmis.budget_annexture_1b set arrear_pay_855=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            } else if (type.equals("DA")) {
                sql = "update hrmis.budget_annexture_1b set da_156=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            } else if (type.equals("HRA")) {
                sql = "update hrmis.budget_annexture_1b set hra_403=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            } else if (type.equals("OA")) {
                sql = "update hrmis.budget_annexture_1b set oa_523=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            } else if (type.equals("RCM")) {
                sql = "update hrmis.budget_annexture_1b set rcm_516=? where annexture1b_id=? and budget_id=? and hrms_id=? and ddo_code=?";
            }
            //System.out.println("SQL is: "+sql);
            if (!sql.equals("")) {
                pst = con.prepareStatement(sql);
            }

            Sheet sheet = workbook.getSheet(0);

            Cell cell = sheet.getCell(3, 0);
            CellType celltype = cell.getType();
            if (celltype == CellType.LABEL) {
                if (cell.getContents() != null && !cell.getContents().equals("")) {
                    dedtype = cell.getContents();
                }
            }
            //System.out.println("dedtype is: " + dedtype);
            for (int i = 1; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    cell = sheet.getCell(j, i);
                    celltype = cell.getType();
                    if (celltype != CellType.EMPTY) {
                        if (celltype == CellType.LABEL) {
                            if (j == 0) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    updateid = Integer.parseInt(cell.getContents());
                                }
                            }
                            if (j == 1) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    empid = cell.getContents();
                                }
                            }
                            if (j == 3) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    amount = Integer.parseInt(cell.getContents());
                                }
                            }
                        }

                        if (celltype == CellType.NUMBER) {
                            if (j == 0) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    updateid = Integer.parseInt(cell.getContents());
                                }
                            }
                            if (j == 1) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    empid = cell.getContents();
                                }
                            }
                            if (j == 3) {
                                if (cell.getContents() != null && !cell.getContents().equals("")) {
                                    amount = Integer.parseInt(cell.getContents());
                                }
                            }
                        }
                        /*System.out.println("updateid is: "+updateid);
                         System.out.println("amount is: "+amount);
                         System.out.println("budgetid is: "+budgetid);
                         System.out.println("empid is: "+empid);
                         System.out.println("ddocode is: "+ddocode);*/
                    }
                }
                //System.out.println("updateid is: " + updateid);
                //System.out.println("amount is: " + amount);
                if (!sql.equals("")) {
                    pst.setInt(1, amount);
                    pst.setInt(2, updateid);
                    pst.setInt(3, Integer.parseInt(budgetid));
                    pst.setString(4, empid);
                    pst.setString(5, ddocode);
                    pst.executeUpdate();
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);
            if (updateid > 0) {
                String chartofacc = "";

                sql = "select chart_of_account from hrmis.budget_annexture_1b where annexture1b_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, updateid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    chartofacc = rs.getString("chart_of_account");
                }
                DataBaseFunctions.closeSqlObjects(rs, pst);
                if (chartofacc != null && !chartofacc.equals("")) {
                    //double basic_pay_3 = 0;
                    double basic_pay_next_year_3 = 0;
                    //double total_yearly_requirement_3 = 0;
                    double arrear_pay_855_3 = 0;
                    double da_156_3 = 0;
                    double hra_403_3 = 0;
                    double oa_523_3 = 0;
                    double rcm_516_3 = 0;
                    double total_3 = 0;

                    //double pay = 0;
                    double pay_next_year = 0;
                    double totalYearlyIncome = 0;
                    double arrear_pay_855 = 0;
                    double da = 0;
                    double hra_403 = 0;
                    double oa_523 = 0;
                    double rcm_516 = 0;
                    double total = 0;

                    sql = "select * from hrmis.budget_annexture_1b where budget_id=? and chart_of_account=? and ddo_code=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(budgetid));
                    pst.setString(2, chartofacc);
                    pst.setString(3, ddocode);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        int annexture_1b_id = rs.getInt("annexture1b_id");

                        //pay = rs.getInt("basic_pay");
                        pay_next_year = rs.getInt("basic_pay_next_year");
                        totalYearlyIncome = rs.getInt("total_yearly");
                        arrear_pay_855 = rs.getInt("arrear_pay_855");
                        da = rs.getInt("da_156");
                        hra_403 = rs.getInt("hra_403");
                        oa_523 = rs.getInt("oa_523");
                        rcm_516 = rs.getInt("rcm_516");
                        //total = rs.getInt("total");

                        total = totalYearlyIncome + arrear_pay_855 + da + hra_403 + oa_523 + rcm_516;

                        pst1 = con.prepareStatement("update hrmis.budget_annexture_1b set total=? where annexture1b_id=?");
                        pst1.setInt(1, (int) total);
                        pst1.setInt(2, annexture_1b_id);
                        pst1.executeUpdate();

                        if (type.equals("PAYNEXT")) {
                            basic_pay_next_year_3 = basic_pay_next_year_3 + pay_next_year;
                        } else if (type.equals("ARR")) {
                            arrear_pay_855_3 = arrear_pay_855_3 + arrear_pay_855;
                        } else if (type.equals("DA")) {
                            da_156_3 = da_156_3 + da;
                        } else if (type.equals("HRA")) {
                            hra_403_3 = hra_403_3 + hra_403;
                        } else if (type.equals("OA")) {
                            oa_523_3 = oa_523_3 + oa_523;
                        } else if (type.equals("RCM")) {
                            rcm_516_3 = rcm_516_3 + rcm_516;
                        }

                        total_3 = total_3 + total;
                    }
                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    if (type.equals("ARR")) {
                        sql = "update hrmis.budget_annexture_1c set arrear_pay_855=?,total=? where budget_id=? and chart_of_account=?";
                        pst2 = con.prepareStatement(sql);
                        pst2.setInt(1, (int) arrear_pay_855_3);
                        pst2.setInt(2, (int) total_3);
                        pst2.setInt(3, Integer.parseInt(budgetid));
                        pst2.setString(4, chartofacc);
                        pst2.executeUpdate();
                    } else if (type.equals("DA")) {
                        sql = "update hrmis.budget_annexture_1c set da_156=?,total=? where budget_id=? and chart_of_account=?";
                        pst2 = con.prepareStatement(sql);
                        pst2.setInt(1, (int) da_156_3);
                        pst2.setInt(2, (int) total_3);
                        pst2.setInt(3, Integer.parseInt(budgetid));
                        pst2.setString(4, chartofacc);
                        pst2.executeUpdate();
                    } else if (type.equals("HRA")) {
                        sql = "update hrmis.budget_annexture_1c set hra_403=?,total=? where budget_id=? and chart_of_account=?";
                        pst2 = con.prepareStatement(sql);
                        pst2.setInt(1, (int) hra_403_3);
                        pst2.setInt(2, (int) total_3);
                        pst2.setInt(3, Integer.parseInt(budgetid));
                        pst2.setString(4, chartofacc);
                        pst2.executeUpdate();
                    } else if (type.equals("OA")) {
                        sql = "update hrmis.budget_annexture_1c set oa_523=?,total=? where budget_id=? and chart_of_account=?";
                        pst2 = con.prepareStatement(sql);
                        pst2.setInt(1, (int) oa_523_3);
                        pst2.setInt(2, (int) total_3);
                        pst2.setInt(3, Integer.parseInt(budgetid));
                        pst2.setString(4, chartofacc);
                        pst2.executeUpdate();
                    } else if (type.equals("RCM")) {
                        sql = "update hrmis.budget_annexture_1c set rcm_516=?,total=? where budget_id=? and chart_of_account=?";
                        pst2 = con.prepareStatement(sql);
                        pst2.setInt(1, (int) rcm_516_3);
                        pst2.setInt(2, (int) total_3);
                        pst2.setInt(3, Integer.parseInt(budgetid));
                        pst2.setString(4, chartofacc);
                        pst2.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1, pst2);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getDDOCodeSelectedOfficeWise(String offcode) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String ddocode = "";
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select ddo_code from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if(rs.next()){
                ddocode = rs.getString("ddo_code");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return ddocode; 
    }

    @Override
    public String checkDDOAnnualBudgetCreateStatus(String ddocode, String fy) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String createStatus = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from hrmis.annual_budget where ddo_code=? and fy=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ddocode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    createStatus = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return createStatus;
    }
}

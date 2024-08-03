package hrms.common;

import hrms.SelectOption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang.StringUtils;

public class BudgetProposal {
    public static int rowFor1st = 0;
    public static int row = 2;
    public static int globalCtr=0;
    public static void main(String args[]) {

        Connection con = null;

        PreparedStatement offcodepst = null;
        ResultSet offcoders = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        //String[] offcodeArr = {"OLSFIN0010000", "OLSHOM0010000", "OLSREV0010000", "OLSHUD0010000", "OLSWAT0010000"};
        String[] offcodeArr = {"OLSHOM0010000"};

        //String offcode = "OLSHOM0010000";
        String offcode = "";

        String chartAcc = "";

        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String[] postGroupType = {"A", "B", "C", "D"};

        
        int slno = 0;

        int sancStrength = 0;
        int menInPosition = 0;
        int vacancy = 0;

        OutputStream out = null;

        ArrayList ddolist = new ArrayList();
        SelectOption so = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://172.16.1.16/hrmis", "hrmis2", "cmgi");
            //con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");

            /*offcodepst = con.prepareStatement("select ddo_code,off_code,off_en from g_office where is_ddo='Y' "
                    + " and dist_code in ( '2115','2124','2108','2101','2100','2109','2122','2112','2104','2114') order by ddo_code ");*/
            offcodepst = con.prepareStatement("select ddo_code,off_code,off_en from g_office where is_ddo='Y' "
                    + " and dist_code in ( '2125') order by ddo_code ");
            offcoders = offcodepst.executeQuery();
            while (offcoders.next()) {
                so = new SelectOption();
                so.setLabel(offcoders.getString("ddo_code"));
                so.setValue(offcoders.getString("off_code"));
                ddolist.add(so);
            }

            slno = 0;
           

            out = new FileOutputStream(new File("D:/Budget_Proposal_Dist/BudgetProposal_NUAPADA.xls"));

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Budget Proposal", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            Number num = null;

            Label label = new Label(1, rowFor1st++, "BUDGET PROPOSAL REPORT", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, 0, 10, 0);

            label = new Label(0, rowFor1st, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, rowFor1st, "DDO Code", headcell);
            sheet.addCell(label);
            label = new Label(2, rowFor1st, "Chart of Account", headcell);
            sheet.addCell(label);
            label = new Label(3, rowFor1st, "Group Details", headcell);
            sheet.addCell(label);
            label = new Label(4, rowFor1st, "Sanctioned Strength", headcell);
            sheet.addCell(label);
            label = new Label(5, rowFor1st, "Vacancy as on 01.03.2019", headcell);
            sheet.addCell(label);
            label = new Label(6, rowFor1st, "Anticipated Vacancy from 01.03.2019 to 01.03.2020", headcell);
            sheet.addCell(label);
            label = new Label(7, rowFor1st, "Total Vacancy(4+5)", headcell);
            sheet.addCell(label);
            label = new Label(8, rowFor1st, "Men in Position as on 01.03.2020", headcell);
            sheet.addCell(label);
            label = new Label(9, rowFor1st, "Vacany likely to be filled up(+)/\narise due to retirement estc.(-) during the next Year", headcell);
            sheet.addCell(label);
            label = new Label(10, rowFor1st, "Anticipated Men in Position for whom\nbudget provision is proposed", headcell);
            sheet.addCell(label);

            if (ddolist != null && ddolist.size() > 0) {
                so = null;
                for (int m = 0; m < ddolist.size(); m++) {
                    so = (SelectOption) ddolist.get(m);

                    //slno = 0;

                    //offcode = offcodeArr[j];
                    offcode = so.getValue();
                    //System.out.println("First Sheet,DDO Code is: " + so.getLabel());
                    

                    /*label = new Label(1, row, offcode, headcell);//column,row
                    sheet.addCell(label);
                    sheet.mergeCells(1, row, 10, row);

                    row += 1;*/

                    String sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                            + " inner join emp_mast on section_post_mapping.spc=emp_mast.cur_spc"
                            + " where off_code=? and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                            + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? and emp_mast.post_grp_type=? group by bill_group_master.bill_group_id";
                    pst2 = con.prepareStatement(sql2);

                    String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR from emp_mast"
                            + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                            + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " where cur_off_code=? and g_section.bill_type='REGULAR'"
                            + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR"
                            + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
                    
                    pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, offcode);
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        demandNo = rs1.getString("DEMAND_NO");
                        majorHead = rs1.getString("MAJOR_HEAD");
                        subMajorHead = rs1.getString("SUB_MAJOR_HEAD");
                        minorHead = rs1.getString("MINOR_HEAD");
                        minorHead1 = rs1.getString("SUB_MINOR_HEAD1");
                        minorHead2 = rs1.getString("SUB_MINOR_HEAD2");
                        minorHead3 = rs1.getString("SUB_MINOR_HEAD3");
                        plan = rs1.getString("PLAN");
                        sector = rs1.getString("SECTOR");

                        chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + plan + "-" + minorHead3 + "-" + sector;

                        

                        for (int i = 0; i < postGroupType.length; i++) {

                            rowFor1st += 1;
                            slno += 1;

                            sancStrength = 0;
                            menInPosition = 0;

                            String tempPostGroup = postGroupType[i];

                            pst2.setString(1, offcode);
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
                            rs2 = pst2.executeQuery();
                            while (rs2.next()) {
                                if (chartAcc.equals("05-2052-00-090-0488-01003-11-1-0")) {
                                    //System.out.println(chartAcc + " and Bill Group Id " + rs2.getString("bill_group_id"));
                                }
                                
                                sancStrength = sancStrength + getSanctionedStrength(con, offcode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                                menInPosition = menInPosition + getMenInPosition(con, offcode, tempPostGroup, new BigDecimal(rs2.getString("bill_group_id")));
                            }
                            //System.out.println(chartAcc + " and " + tempPostGroup + " and sancStrength is: " + sancStrength);
                            //System.out.println(chartAcc + " and " + tempPostGroup + " and menInPosition is: " + menInPosition);
                            vacancy = sancStrength - menInPosition;

                            num = new Number(0, rowFor1st, slno, innercell);
                            sheet.addCell(num);
                            label = new Label(1, rowFor1st, so.getLabel(), innercell);
                            sheet.addCell(label);
                            label = new Label(2, rowFor1st, chartAcc, innercell);
                            sheet.addCell(label);
                            label = new Label(3, rowFor1st, tempPostGroup, innercell);
                            sheet.addCell(label);
                            num = new Number(4, rowFor1st, sancStrength, innercell);
                            sheet.addCell(num);
                            num = new Number(5, rowFor1st, vacancy, innercell);
                            sheet.addCell(num);
                            num = new Number(6, rowFor1st, 0, innercell);
                            sheet.addCell(num);
                            num = new Number(7, rowFor1st, vacancy, innercell);
                            sheet.addCell(num);
                            num = new Number(8, rowFor1st, menInPosition, innercell);
                            sheet.addCell(num);
                            num = new Number(9, rowFor1st, 0, innercell);
                            sheet.addCell(num);
                            num = new Number(10, rowFor1st, 0, innercell);
                            sheet.addCell(num);
                        }
                    }

                    //createSecondSheet(con, offcode, workbook, sheet, headcell, innercell, label, num, so.getLabel());
                    //createThirdSheet(con, offcode, workbook, sheet, headcell, innercell, label, num, so.getLabel());
                }
            }

            sheet = workbook.createSheet("Employee List", 1);
            
            slno = 0;
            if (ddolist != null && ddolist.size() > 0) {
                so = null;
                for (int m = 0; m < ddolist.size(); m++) {
                    so = (SelectOption) ddolist.get(m);

                    offcode = so.getValue();
                    //System.out.println("Second Sheet,DDO Code is: " + so.getLabel());

                    createSecondSheet(slno, con, offcode, workbook, sheet, headcell, innercell, label, num, so.getLabel());
                }
            }

            sheet = workbook.createSheet("CONTRACTUAL", 2);
            row = 0;
            slno = 0;
            if (ddolist != null && ddolist.size() > 0) {
                so = null;
                for (int m = 0; m < ddolist.size(); m++) {
                    so = (SelectOption) ddolist.get(m);

                    offcode = so.getValue();
                    //System.out.println("Third Sheet,DDO Code is: " + so.getLabel());

                    createThirdSheet(row, slno, con, offcode, workbook, sheet, headcell, innercell, label, num, so.getLabel());
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private static int getSanctionedStrength(Connection con, String offcode, String postgrp, BigDecimal billgroupid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            //String sql = "select count(*) sanc_strength from g_spc where off_code=? and post_grp=? and is_sanctioned='Y'";
            String sql = "select count(*) sanc_strength from g_spc"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where g_spc.off_code=? and post_grp=? and bill_section_mapping.bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
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

    private static int getMenInPosition(Connection con, String offcode, String postgrp, BigDecimal billgroupid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            /*String sql = "select count(*) men_in_position from emp_mast"
             + " inner join g_spc on emp_mast.cur_spc=g_spc.spc where cur_off_code=? and post_grp_type=? and is_regular='Y' and is_sanctioned='Y'";*/
            String sql = "select count(*) men_in_position from emp_mast"
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where cur_off_code=? and post_grp=? and is_regular='Y' and bill_section_mapping.bill_group_id=?";
            
         
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
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

    private static void createSecondSheet(int slno, Connection con, String offcode, WritableWorkbook workbook, WritableSheet sheet, WritableCellFormat headcell, WritableCellFormat innercell, Label label, Number num, String ddocode) throws WriteException, SQLException {

        //int row = 0;
        //int slno = 0;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement pst3 = null;
        ResultSet rs3 = null;

        String chartAcc = "";

        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String[] postGroupTypeArr = {"A", "B", "C", "D"};
        try {
            //sheet = workbook.createSheet("Employee List", 1);
           
            num = null;
           

            
                label = new Label(1, 1, "EMPLOYEE LIST", headcell);//column,row
                sheet.addCell(label);
                sheet.mergeCells(1, 0, 10, 0);

                label = new Label(0, 2, "SL No", headcell);
                sheet.addCell(label);
                label = new Label(1, 2, "DDO Code", headcell);
                sheet.addCell(label);
                label = new Label(2, 2, "Chart of Account", headcell);
                sheet.addCell(label);
                label = new Label(3, 2, "Group Details", headcell);
                sheet.addCell(label);
                label = new Label(4, 2, "Name of the incumbent", headcell);
                sheet.addCell(label);
                label = new Label(5, 2, "Employee Id", headcell);
                sheet.addCell(label);
                label = new Label(6, 2, "Grade", headcell);
                sheet.addCell(label);
                label = new Label(7, 2, "Basic Pay(as on 1st March Next Year)", headcell);
                sheet.addCell(label);
                label = new Label(8, 2, "Total Yearly Requirement under Pay(136)(Col.5*12)", headcell);
                sheet.addCell(label);
                label = new Label(9, 2, "DA\n156", headcell);
                sheet.addCell(label);
                label = new Label(10, 2, "HRA\n403", headcell);
                sheet.addCell(label);
                label = new Label(11, 2, "OA\n523", headcell);
                sheet.addCell(label);
                label = new Label(12, 2, "RCM\n516", headcell);
                sheet.addCell(label);
                label = new Label(13, 2, "Total", headcell);
                sheet.addCell(label);
            
            String sql3 = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,f_name,post_grp_type,cur_basic_salary,fixedvalue from emp_mast"
                    + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " left outer join (select fixedvalue,updation_ref_code from update_ad_info where where_updated='E' and ref_ad_code='53')update_ad_info on emp_mast.emp_id=update_ad_info.updation_ref_code"
                    + " where bill_group_master.bill_group_id=? order by f_name";
            pst3 = con.prepareStatement(sql3);

            String sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " inner join emp_mast on section_post_mapping.spc=emp_mast.cur_spc"
                    + " where off_code=? and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? group by bill_group_master.bill_group_id";
            pst2 = con.prepareStatement(sql2);

            String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " where cur_off_code=? and g_section.bill_type='REGULAR'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR"
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, offcode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                demandNo = rs1.getString("DEMAND_NO");
                majorHead = rs1.getString("MAJOR_HEAD");
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD");
                minorHead = rs1.getString("MINOR_HEAD");
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1");
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2");
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3");
                plan = rs1.getString("PLAN");
                sector = rs1.getString("SECTOR");

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, offcode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    int ctr=0;
                    //System.out.println("Second Sheet: Chart of Account is: " + chartAcc + " and Bill Group Id is: " + rs2.getString("bill_group_id"));
                    //ArrayList emplist = getEmployeeList(con, new BigDecimal(rs2.getString("bill_group_id")));
                    
                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    while (rs3.next()) {
                        
                        
                        row ++;
                        globalCtr ++;

                        num = new Number(0, row, globalCtr, innercell);
                        sheet.addCell(num);
                        label = new Label(1, row, ddocode, innercell);
                        sheet.addCell(label);
                        label = new Label(2, row, chartAcc, innercell);
                        sheet.addCell(label);
                        label = new Label(3, row, rs3.getString("post_grp_type"), innercell);
                        sheet.addCell(label);
                        label = new Label(4, row, rs3.getString("EMPNAME"), innercell);
                        sheet.addCell(label);
                        label = new Label(5, row, rs3.getString("emp_id"), innercell);
                        sheet.addCell(label);
                        label = new Label(6, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(7, row, rs3.getString("cur_basic_salary"), innercell);
                        sheet.addCell(label);

                        double totalYearlyIncome = 0;
                        if (rs3.getString("cur_basic_salary") != null && !rs3.getString("cur_basic_salary").equals("")) {
                            totalYearlyIncome = Double.parseDouble(rs3.getString("cur_basic_salary"));
                        }
                        totalYearlyIncome = totalYearlyIncome * 12;
                        label = new Label(8, row, totalYearlyIncome + "", innercell);
                        sheet.addCell(label);

                        double da = 0;
                        if (totalYearlyIncome > 0) {
                            da = totalYearlyIncome * 0.24;
                        }
                        label = new Label(9, row, da + "", innercell);
                        sheet.addCell(label);

                        label = new Label(10, row, rs3.getString("fixedvalue"), innercell);
                        sheet.addCell(label);
                        label = new Label(11, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(12, row, "", innercell);
                        sheet.addCell(label);

                        double total = totalYearlyIncome + da + rs3.getInt("fixedvalue");
                        label = new Label(13, row, total + "", innercell);
                        sheet.addCell(label);
                    }
                    //System.out.println(row+"****"+ctr+"====="+rs2.getString("bill_group_id"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs3, pst3);
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
        }
    }

    

    private static void createThirdSheet(int row, int slno, Connection con, String offcode, WritableWorkbook workbook, WritableSheet sheet, WritableCellFormat headcell, WritableCellFormat innercell, Label label, Number num, String ddocode) throws WriteException, SQLException {

        //int row = 0;
        //int slno = 0;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement pst3 = null;
        ResultSet rs3 = null;

        String chartAcc = "";

        String demandNo = "";
        String majorHead = "";
        String subMajorHead = "";
        String minorHead = "";
        String minorHead1 = "";
        String minorHead2 = "";
        String minorHead3 = "";
        String plan = "";
        String sector = "";
        String[] postGroupTypeArr = {"A", "B", "C", "D"};
        try {
            //sheet = workbook.createSheet("CONTRACTUAL", 2);

            num = null;

            /*label = new Label(1, row++, "CONTRACTUAL", headcell);//column,row
             sheet.addCell(label);
             sheet.mergeCells(1, 0, 10, 0);*/
            row += 1;

            if (row == 1) {
                label = new Label(0, row, "SL No", headcell);
                sheet.addCell(label);
                label = new Label(1, row, "DDO Code", headcell);
                sheet.addCell(label);
                label = new Label(2, row, "Chart of Account", headcell);
                sheet.addCell(label);
                label = new Label(3, row, "Name of the Posts", headcell);
                sheet.addCell(label);
                label = new Label(4, row, "No. of post as on 31.03.2019", headcell);
                sheet.addCell(label);
                label = new Label(5, row, "Increase(+) or Decrease(-)\n in Men in Position\nduring 01.04.2019 to 31.03.2020", headcell);
                sheet.addCell(label);
                label = new Label(6, row, "Total Men in Position\nas on 01.04.20", headcell);
                sheet.addCell(label);
                label = new Label(7, row, "Actual Exp during 2018-19", headcell);
                sheet.addCell(label);
                label = new Label(8, row, "Actual Exp during 2019-20 upto", headcell);
                sheet.addCell(label);
                label = new Label(9, row, "2019-20 Revised Estimate", headcell);
                sheet.addCell(label);
                label = new Label(10, row, "2020-21 B.E.", headcell);
                sheet.addCell(label);
            }
            /*String sql3 = "select post_nomenclature from emp_mast" +
             " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc" +
             " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id" +
             " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id" +
             " where bill_group_master.bill_group_id=? order by post_nomenclature";*/
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
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " inner join emp_mast on section_post_mapping.spc=emp_mast.cur_spc"
                    + " where off_code=? and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? group by bill_group_master.bill_group_id";
            pst2 = con.prepareStatement(sql2);

            String sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " where cur_off_code=? and g_section.bill_type='CONT6_REG'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR"
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, offcode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                demandNo = rs1.getString("DEMAND_NO");
                majorHead = rs1.getString("MAJOR_HEAD");
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD");
                minorHead = rs1.getString("MINOR_HEAD");
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1");
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2");
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3");
                plan = rs1.getString("PLAN");
                sector = rs1.getString("SECTOR");

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, offcode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {

                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                    while (rs3.next()) {
                        row += 1;
                        slno += 1;

                        num = new Number(0, row, slno, innercell);
                        sheet.addCell(num);
                        label = new Label(1, row, ddocode, innercell);
                        sheet.addCell(label);
                        label = new Label(2, row, chartAcc, innercell);
                        sheet.addCell(label);
                        label = new Label(3, row, rs3.getString("post"), innercell);
                        sheet.addCell(label);
                        label = new Label(4, row, rs3.getString("cnt"), innercell);
                        sheet.addCell(label);
                        label = new Label(5, row, rs3.getString("cnt"), innercell);
                        sheet.addCell(label);
                        label = new Label(6, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(7, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(8, row, "" + "", innercell);
                        sheet.addCell(label);
                        label = new Label(9, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(10, row, "", innercell);
                        sheet.addCell(label);
                    }
                }

            }

            sql3 = "select post_nomenclature from emp_mast"
                    + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where bill_group_master.bill_group_id=? order by post_nomenclature";
            pst3 = con.prepareStatement(sql3);

            sql2 = "select bill_group_master.bill_group_id from bill_group_master"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join section_post_mapping on bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " inner join emp_mast on section_post_mapping.spc=emp_mast.cur_spc"
                    + " where off_code=? and DEMAND_NO=? and MAJOR_HEAD=? and SUB_MAJOR_HEAD=? and MINOR_HEAD=? and SUB_MINOR_HEAD1=? and SUB_MINOR_HEAD2=? and"
                    + " SUB_MINOR_HEAD3=? and PLAN=? and SECTOR=? group by bill_group_master.bill_group_id";
            pst2 = con.prepareStatement(sql2);

            sql1 = "select DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR from emp_mast"
                    + " inner join bill_group_master on emp_mast.cur_off_code=bill_group_master.off_code"
                    + " inner join bill_section_mapping on bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                    + " where cur_off_code=? and g_section.bill_type='CONTRACTUAL'"
                    + " group by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR"
                    + " order by DEMAND_NO,MAJOR_HEAD,SUB_MAJOR_HEAD,MINOR_HEAD,SUB_MINOR_HEAD1,SUB_MINOR_HEAD2,SUB_MINOR_HEAD3,PLAN,SECTOR";
            pst1 = con.prepareStatement(sql1);
            pst1.setString(1, offcode);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                demandNo = rs1.getString("DEMAND_NO");
                majorHead = rs1.getString("MAJOR_HEAD");
                subMajorHead = rs1.getString("SUB_MAJOR_HEAD");
                minorHead = rs1.getString("MINOR_HEAD");
                minorHead1 = rs1.getString("SUB_MINOR_HEAD1");
                minorHead2 = rs1.getString("SUB_MINOR_HEAD2");
                minorHead3 = rs1.getString("SUB_MINOR_HEAD3");
                plan = rs1.getString("PLAN");
                sector = rs1.getString("SECTOR");

                chartAcc = demandNo + "-" + majorHead + "-" + subMajorHead + "-" + minorHead + "-" + minorHead1 + "-" + minorHead2 + "-" + plan + "-" + minorHead3 + "-" + sector;

                //String postGroup = postGroupTypeArr[i];
                pst2.setString(1, offcode);
                pst2.setString(2, demandNo);
                pst2.setString(3, majorHead);
                pst2.setString(4, subMajorHead);
                pst2.setString(5, minorHead);
                pst2.setString(6, minorHead1);
                pst2.setString(7, minorHead2);
                pst2.setString(8, minorHead3);
                pst2.setString(9, plan);
                pst2.setString(10, sector);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {

                    pst3.setBigDecimal(1, new BigDecimal(rs2.getString("bill_group_id")));
                    rs3 = pst3.executeQuery();
                    //System.out.println("Contractual Bill Group is: " + rs2.getString("bill_group_id"));
                    while (rs3.next()) {
                        row += 1;
                        slno += 1;

                        int noOfPost = 0;

                        num = new Number(0, row, slno, innercell);
                        sheet.addCell(num);
                        label = new Label(1, row, offcode, innercell);
                        sheet.addCell(label);
                        label = new Label(2, row, chartAcc, innercell);
                        sheet.addCell(label);
                        label = new Label(3, row, rs3.getString("post_nomenclature"), innercell);
                        sheet.addCell(label);

                        noOfPost = getContractualNoOfPost(con, new BigDecimal(rs2.getString("bill_group_id")), rs3.getString("post_nomenclature"));
                        label = new Label(4, row, noOfPost + "", innercell);
                        sheet.addCell(label);
                        label = new Label(5, row, noOfPost + "", innercell);
                        sheet.addCell(label);
                        label = new Label(6, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(7, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(8, row, "" + "", innercell);
                        sheet.addCell(label);
                        label = new Label(9, row, "", innercell);
                        sheet.addCell(label);
                        label = new Label(10, row, "", innercell);
                        sheet.addCell(label);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs3, pst3);
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
        }
    }

    private static int getContractualNoOfPost(Connection con, BigDecimal billgroupid, String postname) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {

            String sql = "select count(*) cnt from emp_mast"
                    + " inner join section_post_mapping on emp_mast.emp_id=section_post_mapping.spc"
                    + " inner join bill_section_mapping on section_post_mapping.section_id=bill_section_mapping.section_id"
                    + " inner join bill_group_master on bill_section_mapping.bill_group_id=bill_group_master.bill_group_id"
                    + " where bill_group_master.bill_group_id=? and post_nomenclature=? group by post_nomenclature";
            pst = con.prepareStatement(sql);
            pst.setBigDecimal(1, billgroupid);
            pst.setString(2, postname);
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
}

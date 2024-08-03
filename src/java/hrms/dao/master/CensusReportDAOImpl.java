/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Madhusmita
 */
public class CensusReportDAOImpl implements CensusReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadCensusReportOfficeWise(OutputStream out, String fileName, WritableWorkbook workbook, String offCode, String fiyear) {
        ResultSet rs, rs1 = null;
        //int colCnt = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String[] post_group = {"A", "B", "C", "D"};
        String dob1 = null;
        String dob2 = null;
        String dob3 = null;
        String dob4 = null;
        String dob5 = null;

        try {

            con = this.dataSource.getConnection();

            int heightInPoints = 44 * 20;
            System.out.println("Financial Year::::" + fiyear);

            if (fiyear.equals("2019")) {
                dob1 = "1969.04.01";
                dob2 = "2001.03.31";

                dob3 = "1959.04.01";
                dob4 = "1969.03.31";

                dob5 = "1959.04.01";
            } else if (fiyear.equals("2020")) {
                dob1 = "1970.04.01";
                dob2 = "2002.03.31";

                dob3 = "1960.04.01";
                dob4 = "1970.03.31";

                dob5 = "1960.04.01";
            } else if (fiyear.equals("2021")) {
                dob1 = "1971.04.01";
                dob2 = "2003.03.31";

                dob3 = "1961.04.01";
                dob4 = "1971.03.31";

                dob5 = "1961.04.01";
            } else if (fiyear.equals("2022")) {
                dob1 = "1972.04.01";
                dob2 = "2004.03.31";

                dob3 = "1962.04.01";
                dob4 = "1972.03.31";

                dob5 = "1962.04.01";
            } else if (fiyear.equals("2023")) {
                dob1 = "1973.04.01";
                dob2 = "2005.03.31";

                dob3 = "1963.04.01";
                dob4 = "1973.03.31";

                dob5 = "1963.04.01";
            }

            WritableFont titleFont0 = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
            WritableCellFormat titleformat0 = new WritableCellFormat(titleFont0);
            titleformat0.setAlignment(Alignment.CENTRE);
            titleformat0.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat0.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat0.setWrap(true);
            WritableFont titleFont01 = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
            WritableCellFormat titleformat01 = new WritableCellFormat(titleFont01);
            titleformat01.setBackground(Colour.BROWN);
            WritableFont titleFont4 = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD, true);
            int col = 2;
            int widthInChars = 5;
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            //titleformat4.setWrap(true);
            WritableCellFormat titleformat5 = new WritableCellFormat(titleFont4);
            titleformat5.setAlignment(Alignment.CENTRE);
            titleformat5.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat5.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat5.setWrap(false);
            WritableFont titleFont3 = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
            WritableCellFormat titleformat3 = new WritableCellFormat(titleFont3);
            titleformat3.setAlignment(Alignment.CENTRE);
            titleformat3.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat3.setVerticalAlignment(VerticalAlignment.BOTTOM);
            titleformat3.setWrap(true);
            titleformat3.setOrientation(Orientation.PLUS_90);
            WritableFont titleFont = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, true);
            WritableCellFormat titleformat = new WritableCellFormat(titleFont);
            titleformat.setAlignment(Alignment.LEFT);
            WritableFont titleFont1 = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
            WritableCellFormat titleformat1 = new WritableCellFormat(titleFont1);
            titleformat1.setAlignment(Alignment.CENTRE);
            /*ps2 = con.prepareStatement("Select ddo_code from g_office where off_code=?");
             ps2.setString(1, offCode);
             ResultSet rs2 = ps2.executeQuery();
             while (rs2.next()) {
             workbook = Workbook.createWorkbook(new File("D:/CensusReport/" + rs2.getString("ddo_code") + ".xls"));
             }*/

            WritableSheet sheet = workbook.createSheet("CensusReport", 0);
            sheet.mergeCells(0, 0, 40, 0);
            Label label1 = new Label(0, 0, "DIRECTORATE OF ECONOMICS AND STATISTICS, ODISHA, BHUBANESWAR", titleformat1);
            sheet.addCell(label1);
            sheet.mergeCells(0, 1, 40, 1);
            Label label2 = new Label(0, 1, "CENSUS OF EMPLOYEES IN THE GOVERNMENT OFFICES IN BHUBANESWAR", titleformat1);
            sheet.addCell(label2);
            sheet.mergeCells(0, 2, 40, 2);
            Label label3 = new Label(0, 2, "STATE GOVERNMENT ESTABLISHMENT AS ON 31.03.".concat(fiyear), titleformat1);
            sheet.addCell(label3);
            WritableFont titleFont2 = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
            WritableCellFormat titleformat2 = new WritableCellFormat(titleFont2);
            titleformat2.setAlignment(Alignment.LEFT);
            sheet.mergeCells(0, 3, 40, 3);
            Label label4 = new Label(0, 3, "I.IDENTIFICATION PARTICULARS", titleformat);
            sheet.addCell(label4);
            ps1 = con.prepareStatement("select t1.off_code,t1.off_en,t1.department_name,t1.co_off_code hod_code,t1.ddo_code,t1.pincode,t1.dist_name,g4.off_en hod_name from\n"
                    + " (select g1.off_code,g1.off_en,g1.department_name,aer.co_off_code,g1.ddo_code, g1.pincode,g3.dist_name from\n"
                    + " (select off_code,off_en,department_name,ddo_code,pincode,dist_code from\n"
                    + " (select * from g_office where off_code=?)goff\n"
                    + " left outer join g_department gdpt on gdpt.department_code=goff.department_code)g1\n"
                    + " left outer join aer_report_submit aer on aer.off_code=g1.off_code\n"
                    + " left outer join g_district g3 on g1.dist_code=g3.dist_code)t1\n"
                    + " left outer join g_office g4 on g4.off_code=t1.co_off_code;");
            ps1.setString(1, offCode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                //sheet.mergeCells(0, 4, 38, 4);
                //sheet.getMergedCells("A4:A38");
                sheet.mergeCells(0, 4, 40, 4);
                Label label5 = new Label(0, 4, "1. Name of the Department :" + rs1.getString("department_name"), titleformat);
                sheet.addCell(label5);
                sheet.mergeCells(0, 5, 40, 5);
                Label label6 = new Label(0, 5, "2. Name of Heads of Department :" + rs1.getString("hod_name"), titleformat);
                sheet.addCell(label6);
                sheet.mergeCells(0, 6, 40, 6);
                Label label7 = new Label(0, 6, "3. Name of the Office :" + rs1.getString("off_en"), titleformat);
                sheet.addCell(label7);
                sheet.mergeCells(0, 7, 40, 7);
                Label label8 = new Label(0, 7, "4. Name of District :" + rs1.getString("dist_name"), titleformat);
                sheet.addCell(label8);
                sheet.mergeCells(0, 8, 40, 8);
                Label label9 = new Label(0, 8, "5. Address with PIN Code :" + rs1.getString("pincode"), titleformat);
                sheet.addCell(label9);
            }
            sheet.mergeCells(0, 9, 40, 9);
            Label label10 = new Label(0, 9, "II. PARTICULARS OF EMPLOYEES IN POSITION AS ON 31.03.".concat(fiyear), titleformat);
            sheet.addCell(label10);
            for (int c = 0; c < 41; c++) {
                String ColCnt = Integer.toString(c + 1);
                Label labelcolCnt = new Label(c, 10, ColCnt, titleformat01);
                //sheet.addCell(labelcolCnt);
            }

            sheet.mergeCells(2, 11, 11, 11);
            Label label11 = new Label(2, 11, "Employees in position(Nos.) as on 31.03.".concat(fiyear).concat(" :"), titleformat0);
            sheet.addCell(label11);
            sheet.mergeCells(15, 11, 17, 12);
            Label label12 = new Label(15, 11, "No of Employees according to Date of Birth(Nos.)", titleformat0);
            sheet.addCell(label12);
            sheet.setRowView(11, heightInPoints);
            sheet.mergeCells(18, 11, 21, 12);
            Label label13 = new Label(18, 11, "No.of Employees Education Qualification(Nos.)", titleformat0);
            sheet.addCell(label13);
            sheet.mergeCells(22, 11, 28, 12);
            Label label14 = new Label(22, 11, "Designation by Profession of the Post(Nos)", titleformat0);
            sheet.addCell(label14);
            sheet.mergeCells(29, 11, 32, 12);
            Label label15 = new Label(29, 11, "Nature of Work(Nos.)", titleformat0);
            sheet.addCell(label15);
            sheet.mergeCells(33, 11, 37, 12);
            Label label16 = new Label(33, 11, "Total Emoulments per month(Nos.)", titleformat0);
            sheet.addCell(label16);
            sheet.mergeCells(38, 11, 40, 12);
            Label label17 = new Label(38, 11, "Non-regular Employees(Nos.)", titleformat0);
            sheet.addCell(label17);

            sheet.mergeCells(0, 11, 0, 13);
            Label labelc1 = new Label(0, 11, "Category (Group)", titleformat3);
            sheet.addCell(labelc1);
            sheet.setColumnView(0, widthInChars);
            sheet.mergeCells(1, 11, 1, 13);
            Label labelc2 = new Label(1, 11, "Sanctioned Strength", titleformat3);
            sheet.addCell(labelc2);
            sheet.setColumnView(1, widthInChars);
            sheet.mergeCells(2, 12, 3, 12);
            Label label18 = new Label(2, 12, "S.C.", titleformat0);
            sheet.addCell(label18);
            sheet.mergeCells(4, 12, 5, 12);
            Label label19 = new Label(4, 12, "S.T.", titleformat0);
            sheet.addCell(label19);
            sheet.mergeCells(6, 12, 7, 12);
            Label label20SEBC = new Label(6, 12, "SEBC", titleformat0);
            sheet.addCell(label20SEBC);
            sheet.mergeCells(8, 12, 9, 12);
            Label label20 = new Label(8, 12, "GENERAL", titleformat0);
            sheet.addCell(label20);
            sheet.mergeCells(10, 12, 11, 12);
            Label label21 = new Label(10, 12, "TOTAL", titleformat0);
            sheet.addCell(label21);
            Label m1 = new Label(2, 13, "Male", titleformat3);
            sheet.addCell(m1);
            sheet.setColumnView(2, widthInChars);
            Label f1 = new Label(3, 13, "Female", titleformat3);
            sheet.addCell(f1);
            sheet.setColumnView(3, widthInChars);
            Label m2 = new Label(4, 13, "Male", titleformat3);
            sheet.addCell(m2);
            sheet.setColumnView(4, widthInChars);
            Label f2 = new Label(5, 13, "Female", titleformat3);
            sheet.addCell(f2);
            sheet.setColumnView(5, widthInChars);
            Label m22 = new Label(6, 13, "Male", titleformat3);
            sheet.addCell(m22);
            sheet.setColumnView(6, widthInChars);
            Label f22 = new Label(7, 13, "Female", titleformat3);
            sheet.addCell(f22);
            sheet.setColumnView(7, widthInChars);
            Label m3 = new Label(8, 13, "Male", titleformat3);
            sheet.addCell(m3);
            sheet.setColumnView(8, widthInChars);
            Label f3 = new Label(9, 13, "Female", titleformat3);
            sheet.addCell(f3);
            sheet.setColumnView(9, widthInChars);
            Label m4 = new Label(10, 13, "Male", titleformat3);
            sheet.addCell(m4);
            sheet.setColumnView(10, widthInChars);
            Label f4 = new Label(11, 13, "Female", titleformat3);
            sheet.addCell(f4);
            sheet.setColumnView(11, widthInChars);
            sheet.mergeCells(12, 11, 12, 13);
            Label label22 = new Label(12, 11, "Vacancy Position", titleformat3);
            sheet.addCell(label22);
            sheet.setColumnView(12, widthInChars);
            sheet.mergeCells(13, 11, 13, 13);
            Label label23 = new Label(13, 11, "Physically Handicapped", titleformat3);
            sheet.addCell(label23);
            sheet.setColumnView(13, widthInChars);
            sheet.mergeCells(14, 11, 14, 13);
            Label label24 = new Label(14, 11, "Rehabilitation Assistance", titleformat3);
            sheet.addCell(label24);
            sheet.setColumnView(14, widthInChars);
            //Label label25 = new Label(15, 13, "01.04.1969 to 31.03.2001(18 to 50)", titleformat3);
            Label label25 = new Label(15, 13, dob1.concat(" to ").concat(dob2).concat("(18 to 50)"), titleformat3);
            sheet.addCell(label25);
            sheet.setColumnView(15, widthInChars);
            //Label label26 = new Label(16, 13, "01.04.1959 to 31.03.1969 (50 to 60)", titleformat3);
            Label label26 = new Label(16, 13, dob3.concat(" to ").concat(dob4).concat("(51 to 60)"), titleformat3);
            sheet.addCell(label26);
            sheet.setColumnView(16, widthInChars);
            Label label27 = new Label(17, 13, "Before ".concat(dob5).concat("(60Yrs. Above)"), titleformat3);
            sheet.addCell(label27);
            sheet.setColumnView(17, widthInChars);
            Label label28 = new Label(18, 13, "Below Matriculate", titleformat3);
            sheet.addCell(label28);
            sheet.setColumnView(18, widthInChars);
            Label label29 = new Label(19, 13, "Matriculate/Intermediate/Technical", titleformat3);
            sheet.addCell(label29);
            sheet.setColumnView(19, widthInChars);
            Label label30 = new Label(20, 13, "Graduate/Technical", titleformat3);
            sheet.addCell(label30);
            sheet.setColumnView(20, widthInChars);
            Label label31 = new Label(21, 13, "Post Graduate/Technical", titleformat3);
            sheet.addCell(label31);
            sheet.setColumnView(21, widthInChars);
            Label label32 = new Label(22, 13, "Health Personnel/Doctor/Paracetamol Workers", titleformat3);
            sheet.addCell(label32);
            sheet.setColumnView(22, widthInChars);
            Label label33 = new Label(23, 13, "Teaching Personnel(Professor/Reader/Lecturer/Teacher)", titleformat3);
            sheet.addCell(label33);
            sheet.setColumnView(23, widthInChars);
            Label label34 = new Label(24, 13, "Agricultural/Veterinary/Fisheries Personnel", titleformat3);
            sheet.addCell(label34);
            sheet.setColumnView(24, widthInChars);
            Label label35 = new Label(25, 13, "Engineering Personnel/Degree/Diploma holder", titleformat3);
            sheet.addCell(label35);
            sheet.setColumnView(25, widthInChars);
            Label label36 = new Label(26, 13, "Legal Personnel", titleformat3);
            sheet.addCell(label36);
            sheet.setColumnView(26, widthInChars);
            Label label37 = new Label(27, 13, "Statistical Personnel", titleformat3);
            sheet.addCell(label37);
            sheet.setColumnView(27, widthInChars);
            Label label38 = new Label(28, 13, "Others", titleformat3);
            sheet.addCell(label38);
            sheet.setColumnView(28, widthInChars);
            Label label39 = new Label(29, 13, "Administrative-cum-Executive", titleformat3);
            sheet.addCell(label39);
            sheet.setColumnView(29, widthInChars);
            Label label40 = new Label(30, 13, "Technical/Professional", titleformat3);
            sheet.addCell(label40);
            sheet.setColumnView(30, widthInChars);
            Label label41 = new Label(31, 13, "Ministerial/Other Group-C Employees", titleformat3);
            sheet.addCell(label41);
            sheet.setColumnView(31, widthInChars);
            Label label42 = new Label(32, 13, "Grade-'D' Employees(Peons)/Choukidars etc.", titleformat3);
            sheet.addCell(label42);
            sheet.setColumnView(32, widthInChars);

            Label label43 = new Label(33, 13, "Up to Level-2", titleformat3);
            sheet.addCell(label43);
            sheet.setColumnView(33, widthInChars);
            Label label44 = new Label(34, 13, "Level-3 to Level-8", titleformat3);
            sheet.addCell(label44);
            sheet.setColumnView(34, widthInChars);
            Label label45 = new Label(35, 13, "Level-9 to Level-11", titleformat3);
            sheet.addCell(label45);
            sheet.setColumnView(35, widthInChars);
            Label label46 = new Label(36, 13, "Level-12 to Level-14", titleformat3);
            sheet.addCell(label46);
            sheet.setColumnView(36, widthInChars);
            Label label47 = new Label(37, 13, "Level-15 to Level-17", titleformat3);
            sheet.addCell(label47);
            sheet.setColumnView(37, widthInChars);

            Label label48 = new Label(38, 13, "No. of new employees joinned during the year", titleformat3);
            sheet.addCell(label48);
            sheet.setColumnView(38, widthInChars);

            Label label49 = new Label(39, 13, "No. of employees retired during the year", titleformat3);
            sheet.addCell(label49);
            sheet.setColumnView(39, widthInChars);

            Label label50 = new Label(40, 13, "Total no. of employees", titleformat3);
            sheet.addCell(label50);
            sheet.setColumnView(40, widthInChars);

            for (int c = 0; c < 41; c++) {
                String ColCnt = Integer.toString(c + 1);
                Label labelcolCnt = new Label(c, 14, ColCnt, titleformat0);
                sheet.addCell(labelcolCnt);
            }
            //offCode = "BBSPCD0010000";
            for (int i = 0; i < post_group.length; i++) {
                //String[] post_group = {"A", "B", "C", "D"};
                //System.out.println(post_group[i] + ":" + i);
                String postgrp = post_group[i];
                ps = con.prepareStatement("select getsanctionstrength_23('" + offCode + "','" + postgrp + "')sanc_strength,"
                        + "getcategorywisedata_23('" + offCode + "','SC','M','" + postgrp + "')sc_m,"
                        + "getcategorywisedata_23('" + offCode + "','SC','F','" + postgrp + "')sc_f,"
                        + "getcategorywisedata_23('" + offCode + "','ST','M','" + postgrp + "')st_m,"
                        + "getcategorywisedata_23('" + offCode + "','ST','F','" + postgrp + "')st_f,"
                        + "getcategorywisedata_23('" + offCode + "','SEBC','M','" + postgrp + "')sebc_m,"
                        + "getcategorywisedata_23('" + offCode + "','SEBC','F','" + postgrp + "')sebc_f,"
                        + "getcategorywisedata_23('" + offCode + "','OBC','M','" + postgrp + "')obc_m,"
                        + "getcategorywisedata_23('" + offCode + "','OBC','F','" + postgrp + "')obc_f,"
                        + "getbblankcategorywisedata_23('" + offCode + "','M','" + postgrp + "')nullcatg_m,"
                        + "getbblankcategorywisedata_23('" + offCode + "','F','" + postgrp + "')nullcatg_f,"
                        + "getcategorywisedata_23('" + offCode + "','GENERAL','M','" + postgrp + "')gen_m,"
                        + "getcategorywisedata_23('" + offCode + "','GENERAL','F','" + postgrp + "')gen_f,"
                        + "getdobwisedatad_23('" + offCode + "','" + postgrp + "','" + dob1 + "','" + dob2 + "')age18_50,"
                        + "getdobwisedata2d_23('" + offCode + "','" + postgrp + "','" + dob3 + "','" + dob4 + "')age51_60,"
                        + "getdobwisedata3d_23('" + offCode + "','" + postgrp + "','" + dob5 + "')ageabove60,"
                        //+ "getcensusamt1_23('" + offCode + "','" + postgrp + "')tot_emol1,"
                        //+ "getcensusamt2_23('" + offCode + "','" + postgrp + "')tot_emol2,"
                        //+ "getcensusamt3_23('" + offCode + "','" + postgrp + "')tot_emol3,"
                        //+ "getcensusamt4_23('" + offCode + "','" + postgrp + "')tot_emol4,"
                        //+ "getcensusamt5_23('" + offCode + "','" + postgrp + "')tot_emol5,"
                        + "getmatrixlevel_D('" + offCode + "','" + postgrp + "')tot_emol1,"
                        + "getmatrixlevel_C('" + offCode + "','" + postgrp + "')tot_emol2,"
                        + "getmatrixlevel_B('" + offCode + "','" + postgrp + "')tot_emol3,"
                        + "getmatrixlevel_A1('" + offCode + "','" + postgrp + "')tot_emol4,"
                        + "getmatrixlevel_A2('" + offCode + "','" + postgrp + "')tot_emol5,"
                        + "getempqualification1_23('" + offCode + "','" + postgrp + "')und_matric,"
                        + "getempqualification2_23('" + offCode + "','" + postgrp + "')mat_tech_inter,"
                        + "getempqualification3_23('" + offCode + "','" + postgrp + "')grad_tech,"
                        + "getempqualification4_23('" + offCode + "','" + postgrp + "')postgrad_tech,"
                        + "getempjoined('" + offCode + "','" + postgrp + "','" + fiyear + "')empjoined,"
                        + "getempretired('" + offCode + "','" + postgrp + "','" + fiyear + "')empretired");

                rs = ps.executeQuery();
                while (rs.next()) {
                    for (int c = 0; c < 39; c++) {
                        int r = i + 15;
                        Label lblcat_grp = new Label(0, r, postgrp, titleformat4);
                        sheet.addCell(lblcat_grp);
                        Label lblsanc_strength = new Label(1, r, rs.getString("sanc_strength"), titleformat4);
                        sheet.addCell(lblsanc_strength);
                        Label lblsc_m = new Label(2, r, rs.getString("sc_m"), titleformat4);
                        sheet.addCell(lblsc_m);
                        Label lblsc_f = new Label(3, r, rs.getString("sc_f"), titleformat4);
                        sheet.addCell(lblsc_f);
                        Label lblst_m = new Label(4, r, rs.getString("st_m"), titleformat4);
                        sheet.addCell(lblst_m);
                        Label lblst_f = new Label(5, r, rs.getString("st_f"), titleformat4);
                        sheet.addCell(lblst_f);
                        int sebc_male = Integer.parseInt(rs.getString("sebc_m")) + Integer.parseInt(rs.getString("obc_m"));
                        int sebc_female = Integer.parseInt(rs.getString("sebc_f")) + Integer.parseInt(rs.getString("obc_f"));
                        Label lblsebc_m = new Label(6, r, Integer.toString(sebc_male), titleformat4);
                        sheet.addCell(lblsebc_m);
                        Label lblsebc_f = new Label(7, r, Integer.toString(sebc_female), titleformat4);
                        sheet.addCell(lblsebc_f);
                        int gen_male = Integer.parseInt(rs.getString("gen_m")) + Integer.parseInt(rs.getString("nullcatg_m"));
                        int gen_female = Integer.parseInt(rs.getString("gen_f")) + Integer.parseInt(rs.getString("nullcatg_f"));
                        Label lblgen_m = new Label(8, r, Integer.toString(gen_male), titleformat4);
                        sheet.addCell(lblgen_m);
                        Label lblgen_f = new Label(9, r, Integer.toString(gen_female), titleformat4);
                        sheet.addCell(lblgen_f);
                        int tot_male = Integer.parseInt(rs.getString("sc_m")) + Integer.parseInt(rs.getString("st_m")) + sebc_male + gen_male;
                        Label lbltot_male = new Label(10, r, Integer.toString(tot_male), titleformat4);
                        sheet.addCell(lbltot_male);
                        int tot_female = Integer.parseInt(rs.getString("sc_f")) + Integer.parseInt(rs.getString("st_f")) + sebc_female + gen_female;
                        Label lbltot_female = new Label(11, r, Integer.toString(tot_female), titleformat4);
                        sheet.addCell(lbltot_female);
                        int tot_no_naturework = tot_male + tot_female;
                        int vacancy = Integer.parseInt(rs.getString("sanc_strength")) - (tot_male + tot_female);
                        Label lblvac_pos = new Label(12, r, Integer.toString(vacancy), titleformat4);
                        sheet.addCell(lblvac_pos);
                        Label lblph_handi = new Label(13, r, "", titleformat4);
                        sheet.addCell(lblph_handi);
                        Label lblrehab_asst = new Label(14, r, "", titleformat4);
                        sheet.addCell(lblrehab_asst);
                        Label lbl18_50 = new Label(15, r, rs.getString("age18_50"), titleformat4);
                        sheet.addCell(lbl18_50);
                        Label lbl50_60 = new Label(16, r, rs.getString("age51_60"), titleformat4);
                        sheet.addCell(lbl50_60);
                        Label lbl60above = new Label(17, r, rs.getString("ageabove60"), titleformat4);
                        sheet.addCell(lbl60above);
                        Label lblbelowmatric = new Label(18, r, rs.getString("und_matric"), titleformat4);
                        sheet.addCell(lblbelowmatric);
                        Label lblmat_int_tech = new Label(19, r, rs.getString("mat_tech_inter"), titleformat4);
                        sheet.addCell(lblmat_int_tech);
                        Label lblgrad_tech = new Label(20, r, rs.getString("grad_tech"), titleformat4);
                        sheet.addCell(lblgrad_tech);
                        Label lblpostgrad_tech = new Label(21, r, rs.getString("postgrad_tech"), titleformat4);
                        sheet.addCell(lblpostgrad_tech);
                        Label lblhp_dct_pmw = new Label(22, r, "", titleformat4);
                        sheet.addCell(lblhp_dct_pmw);
                        Label lblteaching_pers = new Label(23, r, "", titleformat4);
                        sheet.addCell(lblteaching_pers);
                        Label lblagr_vet_fish = new Label(24, r, "", titleformat4);
                        sheet.addCell(lblagr_vet_fish);
                        Label lbleng_degr_fish = new Label(25, r, "", titleformat4);
                        sheet.addCell(lbleng_degr_fish);
                        Label lbllegal_pers = new Label(26, r, "", titleformat4);
                        sheet.addCell(lbllegal_pers);
                        Label lblsta_pers = new Label(27, r, "", titleformat4);
                        sheet.addCell(lblsta_pers);
                        Label lbloth_prof = new Label(28, r, "", titleformat4);
                        sheet.addCell(lbloth_prof);
                        if (postgrp.equals("A")) {
                            Label lbladm_exe = new Label(29, r, Integer.toString(tot_no_naturework), titleformat4);
                            sheet.addCell(lbladm_exe);
                        } else {
                            Label lbladm_exe = new Label(29, r, "", titleformat4);
                            sheet.addCell(lbladm_exe);
                        }
                        Label lbltech_profe = new Label(30, r, "", titleformat4);
                        sheet.addCell(lbltech_profe);
                        if (postgrp.equals("C")) {
                            Label lblminst_grpc_emp = new Label(31, r, Integer.toString(tot_no_naturework), titleformat4);
                            sheet.addCell(lblminst_grpc_emp);
                        } else {
                            Label lblminst_grpc_emp = new Label(31, r, "", titleformat4);
                            sheet.addCell(lblminst_grpc_emp);
                        }
                        if (postgrp.equals("D")) {
                            Label lblgradD_chouki = new Label(32, r, Integer.toString(tot_no_naturework), titleformat4);
                            sheet.addCell(lblgradD_chouki);
                        } else {
                            Label lblgradD_chouki = new Label(32, r, "", titleformat4);
                            sheet.addCell(lblgradD_chouki);
                        }
                        Label lbl22000 = new Label(33, r, rs.getString("tot_emol1"), titleformat4);
                        sheet.addCell(lbl22000);
                        Label lbl22001_35000 = new Label(34, r, rs.getString("tot_emol2"), titleformat4);
                        sheet.addCell(lbl22001_35000);
                        Label lbl35001_65000 = new Label(35, r, rs.getString("tot_emol3"), titleformat4);
                        sheet.addCell(lbl35001_65000);
                        Label lbl65001_1200000 = new Label(36, r, rs.getString("tot_emol4"), titleformat4);
                        sheet.addCell(lbl65001_1200000);
                        Label lbl1200000abv = new Label(37, r, rs.getString("tot_emol5"), titleformat4);
                        sheet.addCell(lbl1200000abv);

                        Label lblcont_appt = new Label(38, r, rs.getString("empjoined"), titleformat4);
                        sheet.addCell(lblcont_appt);

                        Label lblcont_appt1 = new Label(39, r, rs.getString("empretired"), titleformat4);
                        sheet.addCell(lblcont_appt1);
                        Label lblcont_appt2 = new Label(40, r, "", titleformat4);
                        sheet.addCell(lblcont_appt2);

                    }

                    Label lblcont_appt = new Label(26, 24, "Signature and Designation of the forewarding authority", titleformat5);
                    sheet.addCell(lblcont_appt);
                }
            }
            System.out.println("office code:" + offCode);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}

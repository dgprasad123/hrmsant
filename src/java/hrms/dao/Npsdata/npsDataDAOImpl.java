/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.Npsdata;

import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.model.NpsData.NpsDataForm;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.CellView;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.UnderlineStyle;
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
public class npsDataDAOImpl implements npsDataDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getTreasuryList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        PreparedStatement pst = null;
        ArrayList treasuryList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select tr_code,tr_name from g_treasury order by tr_name");
            while (rs.next()) {
                NpsDataForm npsform = new NpsDataForm();
                npsform.setTrCode(rs.getString("tr_code"));
                npsform.setTrName(rs.getString("tr_name"));
                //npsform.setHidtrName(rs.getString("tr_name"));
                treasuryList.add(npsform);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
    }

    @Override
    public void downloadNpsData(OutputStream out, String fileName, WritableWorkbook workbook, NpsDataForm npsForm) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement selectpst = null;
        ResultSet rs = null;
        String nps_start_month_year = null;

        int row = 0;

        try {
            con = this.dataSource.getConnection();
            String monarry[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            WritableSheet sheet = workbook.createSheet("npsdata", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            CellView cellView = new CellView();
            headcell.setAlignment(Alignment.CENTRE);

            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setBorder(Border.ALL, BorderLineStyle.NONE);

            headcell.setWrap(true);

            WritableFont titleFont0 = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD);
            WritableCellFormat titleformat0 = new WritableCellFormat(titleFont0);
            titleformat0.setAlignment(Alignment.CENTRE);
            titleformat0.setBorder(Border.ALL, BorderLineStyle.NONE);
            //titleformat0.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleFont0.setUnderlineStyle(UnderlineStyle.SINGLE);
            titleformat0.setWrap(true);

            WritableFont bodyformat = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);

            WritableCellFormat bodycell = new WritableCellFormat(bodyformat);
            headcell.setAlignment(Alignment.CENTRE);

            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setBorder(Border.ALL, BorderLineStyle.NONE);

            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            row = row + 1;

            Label label = new Label(0, row, "HRMS NPS DATA", titleformat0);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 19, row);

            row = row + 1;

            label = new Label(0, row, "Emp_Id", headcell);
            sheet.addCell(label);
            sheet.setColumnView(0, 12);
            label = new Label(1, row, "Emp_Name", headcell);
            sheet.addCell(label);
            sheet.setColumnView(1, 50);
            label = new Label(2, row, "DOB", headcell);
            sheet.addCell(label);
            sheet.setColumnView(2, 15);
            label = new Label(3, row, "DOJ", headcell);
            sheet.addCell(label);
            sheet.setColumnView(3, 15);
            label = new Label(4, row, "DOS", headcell);
            sheet.addCell(label);
            sheet.setColumnView(4, 15);
            label = new Label(5, row, "Pran No", headcell);
            sheet.addCell(label);
            sheet.setColumnView(5, 17);
            label = new Label(6, row, "Bill_Id", headcell);
            sheet.addCell(label);
            sheet.setColumnView(6, 10);
            label = new Label(7, row, "Ddo_Code", headcell);
            sheet.addCell(label);
            sheet.setColumnView(7, 15);

            label = new Label(8, row, "Ddo_Reg_No", headcell);
            sheet.addCell(label);
            sheet.setColumnView(8, 18);
            label = new Label(9, row, "BASIC", headcell);
            sheet.addCell(label);
            sheet.setColumnView(9, 15);
            label = new Label(10, row, "GP", headcell);
            sheet.addCell(label);
            sheet.setColumnView(10, 15);
            label = new Label(11, row, "DA", headcell);
            sheet.addCell(label);
            sheet.setColumnView(11, 15);
            label = new Label(12, row, "CPF", headcell);
            sheet.addCell(label);
            sheet.setColumnView(12, 15);
            label = new Label(13, row, "CPF_14", headcell);
            sheet.addCell(label);
            sheet.setColumnView(13, 15);
            label = new Label(14, row, "CPF_Diff", headcell);
            sheet.addCell(label);
            sheet.setColumnView(14, 15);
            label = new Label(15, row, "Nps_Loan_Amt", headcell);
            sheet.addCell(label);
            sheet.setColumnView(15, 15);
            label = new Label(16, row, "Nps_Loan_Installment", headcell);
            sheet.addCell(label);
            sheet.setColumnView(16, 17);
            label = new Label(17, row, "Nps_Start_Month_Year", headcell);
            sheet.addCell(label);
            sheet.setColumnView(17, 17);
            label = new Label(18, row, "TV_No", headcell);
            sheet.addCell(label);
            sheet.setColumnView(18, 10);
            label = new Label(19, row, "TV_Date", headcell);
            sheet.addCell(label);
            sheet.setColumnView(19, 15);
            if (npsForm.getSltYear() != null && (npsForm.getSltYear().equals("2021") || npsForm.getSltYear().equals("2022") || npsForm.getSltYear().equals("2023"))) {

                String sql = "select distinct emp_id,emp_name,to_char(dob,'dd-MON-yyyy')dob,to_char(DOE_GOV,'dd-MON-yyyy')doe_gov,to_char(DOS,'dd-MON-yyyy')dos"
                        + ",gpf_no,b.bill_no,b.ddo_code,go.ddo_reg_no,a.cur_basic,\n"
                        + "(select ad_amt from hrmis2.aq_dtls  where ad_code='GP' and aqsl_no=d.aqsl_no  and ad_amt>0 limit 1) gp,\n"
                        + "(select ad_amt from hrmis2.aq_dtls where ad_code='DA' and aqsl_no=d.aqsl_no  and ad_amt>0 limit 1)da,\n"
                        + "(select ad_amt from hrmis2.aq_dtls  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)CPF,\n"
                        + "(select tot_rec_amt from hrmis2.aq_dtls  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)CPF_14,\n"
                        + "((select tot_rec_amt from hrmis2.aq_dtls  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)-\n"
                        + "(select ad_amt from hrmis2.aq_dtls  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1))diffCpf,\n"
                        + "b.tr_code,tb.tr_name,\n"
                        + "(select ad_amt from hrmis2.aq_dtls where ad_code='NPSL' and aqsl_no=d.aqsl_no and ad_amt>0 limit 1)NPSL,\n"
                        + "(select ref_desc from hrmis2.aq_dtls where aqsl_no = d.aqsl_no and ad_code IN ('GP','DA','CPF','NPSL')\n"
                        + "and ad_amt > 0 and (ref_desc !='' or ref_desc is null) ORDER BY ad_code DESC limit 1) ref_desc,\n"
                        + "a.AQ_MONTH,a.AQ_YEAR,\n"
                        + "(select concat(AA.aq_year, '/', AA.aq_month) FROM\n"
                        + "(select aq_year, aq_month from hrmis.aq_dtls1 WHERE emp_code = d.emp_code and ad_code IN ('CPF','NPSL')\n"
                        + "and ad_amt >0 and aq_year > 2018\n"
                        + "UNION\n"
                        + "select  aq_year, aq_month from hrmis2.aq_dtls where ad_code IN\n"
                        + "('CPF','NPSL') and emp_code=d.emp_code and aq_year>2018\n"
                        + "and ad_amt>0 order by aq_year, aq_month\n"
                        + "LIMIT 1) AS AA) AS start_date,b.vch_no,to_char(b.VCH_DATE,'dd-MON-yyyy')vch_date\n"
                        + "from hrmis2.aq_dtls d\n"
                        + "inner join aq_mast a on d.aqsl_no=a.aqsl_no\n"
                        + "inner join emp_mast e on a.emp_code=e.emp_id\n"
                        + "inner join bill_mast b on a.bill_no=b.bill_no\n"
                        + "inner join g_bill_status gb on b.bill_status_id=gb.id\n"
                        + "inner join g_treasury tb on b.tr_code=tb.tr_code\n"
                        + "inner join g_office go on b.off_code=go.off_code\n"
                        + "where e.acct_type='PRAN' AND d.AQ_MONTH=? AND\n"
                        + "d.AQ_YEAR=? and ad_code IN ('GP','DA','CPF','NPSL')\n"
                        + "and if_gpf_assumed='N' and bill_status_id=7 and a.cur_basic>0\n"
                        + "and (ref_desc !='' or ref_desc is null)\n"
                        + "and bill_type=? and vch_no is not null\n"
                        + "and ddo_reg_no is not null and b.tr_code=?\n"
                        + "order by aq_month ";
                selectpst = con.prepareStatement(sql);
                selectpst.setInt(1, Integer.parseInt(npsForm.getSltMonth()));
                selectpst.setInt(2, Integer.parseInt(npsForm.getSltYear()));
                selectpst.setString(3, npsForm.getBilltype());
                selectpst.setString(4, npsForm.getTrCode());
                rs = selectpst.executeQuery();
            } else if (npsForm.getSltYear().equals("2019") || npsForm.getSltYear().equals("2020")) {
                String sql = "select distinct emp_id,emp_name,to_char(dob,'dd-MON-yyyy')dob,to_char(DOE_GOV,'dd-MON-yyyy')doe_gov,to_char(DOS,'dd-MON-yyyy')dos"
                        + ",gpf_no,b.bill_no,b.ddo_code,go.ddo_reg_no,a.cur_basic,\n"
                        + "(select ad_amt from hrmis.aq_dtls1  where ad_code='GP' and aqsl_no=d.aqsl_no  and ad_amt>0 limit 1) gp,\n"
                        + "(select ad_amt from hrmis.aq_dtls1 where ad_code='DA' and aqsl_no=d.aqsl_no  and ad_amt>0 limit 1)da,\n"
                        + "(select ad_amt from hrmis.aq_dtls1  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)CPF,\n"
                        + "(select tot_rec_amt from hrmis.aq_dtls1  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)CPF_14,\n"
                        + "((select tot_rec_amt from hrmis.aq_dtls1  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1)-\n"
                        + "(select ad_amt from hrmis.aq_dtls1  where ad_code='CPF' and aqsl_no=d.aqsl_no  AND ad_amt>0 limit 1))diffCpf,\n"
                        + "b.tr_code,tb.tr_name,\n"
                        + "(select ad_amt from hrmis.aq_dtls1 where ad_code='NPSL' and aqsl_no=d.aqsl_no and ad_amt>0 limit 1)NPSL,\n"
                        + "(select ref_desc from hrmis.aq_dtls1 where aqsl_no = d.aqsl_no and ad_code IN ('GP','DA','CPF','NPSL')\n"
                        + "and ad_amt > 0 and (ref_desc !='' or ref_desc is null) ORDER BY ad_code DESC limit 1) ref_desc,\n"
                        + "a.AQ_MONTH,a.AQ_YEAR,\n"
                        + "(select concat(AA.aq_year, '/', AA.aq_month) FROM\n"
                        + "(select aq_year, aq_month from hrmis.aq_dtls1 WHERE emp_code = d.emp_code and ad_code IN ('CPF','NPSL')\n"
                        + "and ad_amt >0 and aq_year > 2018\n"
                        + "UNION\n"
                        + "select  aq_year, aq_month from hrmis2.aq_dtls where ad_code IN\n"
                        + "('CPF','NPSL') and emp_code=d.emp_code and aq_year>2018\n"
                        + "and ad_amt>0 order by aq_year, aq_month\n"
                        + "LIMIT 1) AS AA) AS start_date,b.vch_no,to_char(b.VCH_DATE,'dd-MON-yyyy')vch_date\n"
                        + "from hrmis.aq_dtls1 d\n"
                        + "inner join aq_mast a on d.aqsl_no=a.aqsl_no\n"
                        + "inner join emp_mast e on a.emp_code=e.emp_id\n"
                        + "inner join bill_mast b on a.bill_no=b.bill_no\n"
                        + "inner join g_bill_status gb on b.bill_status_id=gb.id\n"
                        + "inner join g_treasury tb on b.tr_code=tb.tr_code\n"
                        + "inner join g_office go on b.off_code=go.off_code\n"
                        + "where e.acct_type='PRAN' AND d.AQ_MONTH=? AND\n"
                        + "d.AQ_YEAR=? and ad_code IN ('GP','DA','CPF','NPSL')\n"
                        + "and if_gpf_assumed='N' and bill_status_id=7 and a.cur_basic>0\n"
                        + "and (ref_desc !='' or ref_desc is null)\n"
                        + "and bill_type =? and vch_no is not null\n"
                        + "and ddo_reg_no is not null and b.tr_code=?\n"
                        + "order by aq_month";
                selectpst = con.prepareStatement(sql);
                selectpst.setInt(1, Integer.parseInt(npsForm.getSltMonth()));
                selectpst.setInt(2, Integer.parseInt(npsForm.getSltYear()));
                selectpst.setString(3, npsForm.getBilltype());
                selectpst.setString(4, npsForm.getTrCode());
                rs = selectpst.executeQuery();

            }

            while (rs.next()) {
                row = row + 1;
                if (rs.getString("start_date") != null && !rs.getString("start_date").equals("")) {
                    String startdate = rs.getString("start_date");
                    String[] output = startdate.split("/");
                    nps_start_month_year = output[0] + " / " + CalendarCommonMethods.getMonthAsString(Integer.parseInt(output[1]));
                }

                label = new Label(0, row, rs.getString("emp_id"), bodycell);
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("emp_name"), bodycell);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("dob"), bodycell);
                sheet.addCell(label);
                label = new Label(3, row, rs.getString("doe_gov"), bodycell);
                sheet.addCell(label);
                label = new Label(4, row, rs.getString("dos"), bodycell);
                sheet.addCell(label);
                label = new Label(5, row, rs.getString("gpf_no"), bodycell);
                sheet.addCell(label);
                label = new Label(6, row, rs.getString("bill_no"), bodycell);
                sheet.addCell(label);

                label = new Label(7, row, rs.getString("ddo_code"), bodycell);
                sheet.addCell(label);
                label = new Label(8, row, rs.getString("ddo_reg_no"), bodycell);
                sheet.addCell(label);
                label = new Label(9, row, rs.getString("cur_basic"), bodycell);
                sheet.addCell(label);
                label = new Label(10, row, rs.getString("gp"), bodycell);
                sheet.addCell(label);
                label = new Label(11, row, rs.getString("da"), bodycell);
                sheet.addCell(label);
                label = new Label(12, row, rs.getString("cpf"), bodycell);
                sheet.addCell(label);
                
                 label = new Label(13, row, rs.getString("cpf_14"), bodycell);
                sheet.addCell(label);
                label = new Label(14, row, rs.getString("diffCpf"), bodycell);
                sheet.addCell(label);
                
                label = new Label(15, row, rs.getString("npsl"), bodycell);
                sheet.addCell(label);
                label = new Label(16, row, rs.getString("ref_desc"), bodycell);
                sheet.addCell(label);
                label = new Label(17, row, nps_start_month_year, bodycell);
                sheet.addCell(label);
                label = new Label(18, row, rs.getString("vch_no"), bodycell);
                sheet.addCell(label);
                label = new Label(19, row, rs.getString("vch_date"), bodycell);
                sheet.addCell(label);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadnonNpsData(OutputStream out, String fileName, WritableWorkbook workbook, NpsDataForm nonnpsform) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement selectpst = null;
        ResultSet rs = null;
        String nps_start_month_year = null;

        int row = 0;

        try {
            con = this.dataSource.getConnection();
            String monarry[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            WritableSheet sheet = workbook.createSheet("nonnpsdata", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            CellView cellView = new CellView();
            headcell.setAlignment(Alignment.CENTRE);

            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setBorder(Border.ALL, BorderLineStyle.NONE);

            headcell.setWrap(true);

            WritableFont titleFont0 = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD);
            WritableCellFormat titleformat0 = new WritableCellFormat(titleFont0);
            titleformat0.setAlignment(Alignment.CENTRE);
            titleformat0.setBorder(Border.ALL, BorderLineStyle.NONE);
            //titleformat0.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleFont0.setUnderlineStyle(UnderlineStyle.SINGLE);
            titleformat0.setWrap(true);

            WritableFont bodyformat = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);

            WritableCellFormat bodycell = new WritableCellFormat(bodyformat);
            headcell.setAlignment(Alignment.CENTRE);

            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setBorder(Border.ALL, BorderLineStyle.NONE);

            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            row = row + 1;

            Label label = new Label(0, row, "HRMS NON-NPS DATA", titleformat0);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, row, 17, row);

            row = row + 1;

            label = new Label(0, row, "Bill_id", headcell);
            sheet.addCell(label);
            sheet.setColumnView(0, 15);
            label = new Label(1, row, "Emp_Name", headcell);
            sheet.addCell(label);
            sheet.setColumnView(1, 50);
            label = new Label(2, row, "Emp_Code", headcell);
            sheet.addCell(label);
            sheet.setColumnView(2, 15);
            label = new Label(3, row, "Gpf_acc_no", headcell);
            sheet.addCell(label);
            sheet.setColumnView(3, 17);
            label = new Label(4, row, "Office_Code", headcell);
            sheet.addCell(label);
            sheet.setColumnView(4, 20);
            label = new Label(5, row, "Doe-gov", headcell);
            sheet.addCell(label);
            sheet.setColumnView(5, 17);
            label = new Label(6, row, "DDO Code", headcell);
            sheet.addCell(label);
            sheet.setColumnView(6, 17);
            label = new Label(7, row, "Office_Name", headcell);
            sheet.addCell(label);
            sheet.setColumnView(7, 55);
            label = new Label(8, row, "Treasury_code", headcell);
            sheet.addCell(label);
            sheet.setColumnView(8, 19);

            if (nonnpsform.getBilltype().equals("allbill") && nonnpsform.getTrCode().equals("alltreasury")) {
                String sql = "select aq_mast.BILL_NO,aq_mast.emp_name,aq_mast.emp_code,\n"
                        + "aq_mast.gpf_acc_no,aq_mast.off_code,to_char(emp_mast.doe_gov, 'dd-MON-yyyy') doegov,\n"
                        + "aq_mast.acct_type,g_office.ddo_code,g_office.off_en,bill_mast.tr_code\n"
                        + "from bill_mast\n"
                        + "inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no\n"
                        + "inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id\n"
                        + "inner join g_office on aq_mast.off_code=g_office.off_code\n"
                        + "where emp_mast.if_gpf_assumed='Y' and bill_mast.bill_status_id not in (3,4,5,7,8)\n"
                        + "and bill_mast.aq_year= ? and bill_mast.aq_month =? and is_regular NOT IN('N')\n"
                        + "and emp_non_pran='N' AND aq_mast.ACCT_TYPE='PRAN' and bill_type not in('43')\n"
                        + "ORDER BY aq_mast.off_code";
                selectpst = con.prepareStatement(sql);

                selectpst.setInt(1, Integer.parseInt(nonnpsform.getSltYear()));
                selectpst.setInt(2, Integer.parseInt(nonnpsform.getSltMonth()));
                //selectpst.setString(3, nonnpsform.getBilltype());
                //selectpst.setString(4, nonnpsform.getTrCode());
                rs = selectpst.executeQuery();

            } else {
                String sql = "select aq_mast.BILL_NO,aq_mast.emp_name,aq_mast.emp_code,\n"
                        + "aq_mast.gpf_acc_no,aq_mast.off_code,to_char(emp_mast.doe_gov, 'dd-MON-yyyy') doegov,\n"
                        + "aq_mast.acct_type,g_office.ddo_code,g_office.off_en,bill_mast.tr_code\n"
                        + "from bill_mast\n"
                        + "inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no\n"
                        + "inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id\n"
                        + "inner join g_office on aq_mast.off_code=g_office.off_code\n"
                        + "where emp_mast.if_gpf_assumed='Y' and bill_mast.bill_status_id not in (3,4,5,7,8)\n"
                        + "and bill_mast.aq_year = ? and bill_mast.aq_month = ? and is_regular NOT IN('N')\n"
                        + "and emp_non_pran='N' AND aq_mast.ACCT_TYPE='PRAN' and bill_type =? and bill_mast.tr_code=?\n"
                        + "ORDER BY aq_mast.off_code ";
                selectpst = con.prepareStatement(sql);

                selectpst.setInt(1, Integer.parseInt(nonnpsform.getSltYear()));
                selectpst.setInt(2, Integer.parseInt(nonnpsform.getSltMonth()));
                selectpst.setString(3, nonnpsform.getBilltype());
                selectpst.setString(4, nonnpsform.getTrCode());
                rs = selectpst.executeQuery();
            }
            while (rs.next()) {
                row = row + 1;

                label = new Label(0, row, rs.getString("bill_no"), bodycell);
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("emp_name"), bodycell);
                sheet.addCell(label);
                label = new Label(2, row, rs.getString("emp_code"), bodycell);
                sheet.addCell(label);
                label = new Label(3, row, rs.getString("gpf_acc_no"), bodycell);
                sheet.addCell(label);
                label = new Label(4, row, rs.getString("off_code"), bodycell);
                sheet.addCell(label);
                label = new Label(5, row, rs.getString("doegov"), bodycell);
                sheet.addCell(label);
                label = new Label(6, row, rs.getString("ddo_code"), bodycell);
                sheet.addCell(label);
                label = new Label(7, row, rs.getString("off_en"), bodycell);
                sheet.addCell(label);
                label = new Label(8, row, rs.getString("tr_code"), bodycell);
                sheet.addCell(label);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

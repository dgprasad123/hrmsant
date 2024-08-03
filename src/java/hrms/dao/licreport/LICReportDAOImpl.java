package hrms.dao.licreport;

import hrms.SelectOption;
import hrms.common.AqFunctionalities;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.licreport.LICDivisionWiseBean;
import hrms.model.licreport.LICTreasuryWiseBean;
import hrms.model.payroll.billbrowser.BillBean;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class LICReportDAOImpl implements LICReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadExcelLICTreasuryWise(HttpServletResponse response, String treasury, ArrayList paybilllist) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        BufferedOutputStream output = null;
        String excelname = "";

        String aqdtlsTblName = "AQ_DTLS";

        LICTreasuryWiseBean licTreasuryBean = null;
        ArrayList licdata = new ArrayList();
        try {
            con = dataSource.getConnection();

            if ((treasury != null && !treasury.equals(""))) {

                excelname = "LIC_" + treasury;
                excelname = excelname.replace(" ", "_");
                String fileName = excelname + ".xls";

                output = new BufferedOutputStream(response.getOutputStream());

                WritableWorkbook workbook = Workbook.createWorkbook(output);

                WritableSheet sheet = workbook.createSheet("TREASURYWISE_LIC_REPORT", 0);

                WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat headcell = new WritableCellFormat(headformat);
                WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                WritableCellFormat innercell = new WritableCellFormat(cellformat);

                Label label = new Label(0, 0, "SL NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(1, 0, "HRMS ID", headcell);//column,row
                sheet.addCell(label);
                label = new Label(2, 0, "EMPL NAME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(3, 0, "POLICY NUMBER", headcell);//column,row
                sheet.addCell(label);
                label = new Label(4, 0, "AMOUNT", headcell);//column,row
                sheet.addCell(label);
                label = new Label(5, 0, "SALARY MONTH", headcell);//column,row
                sheet.addCell(label);
                label = new Label(6, 0, "DUE TO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(7, 0, "TREASURY NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(8, 0, "EMPLOYEE LOCATION CODE", headcell);//column,row
                sheet.addCell(label);
                label = new Label(9, 0, "MOBILE NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(10, 0, "PRESENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(11, 0, "PERMANENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(12, 0, "ADDRESS LINE-3", headcell);//column,row
                sheet.addCell(label);
                label = new Label(13, 0, "ADDRESS LINE-4", headcell);//column,row
                sheet.addCell(label);

                /*String sql = "SELECT EMP_ID,EMP_NAME,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN,MOBILE,PRM_ADDRESS,VILLAGE_NAME,DIST_NAME,PRM_PIN FROM ("
                 + " SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,AQ_MAST.TR_CODE,OFF_EN,AQ_MAST.DDO_CODE,MOBILE,ACC_NO,AD_AMT,PRM_ADDRESS,PRM_VILL_CODE,PRM_DIST_CODE,PRM_PIN FROM ("
                 + " SELECT EMP_CODE,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN FROM ("
                 + " SELECT AQSL_NO,AQ_MAST.OFF_CODE,OFF_EN,DDO_CODE,EMP_CODE,TR_CODE FROM ("
                 + " SELECT OFF_CODE,OFF_EN,TR_CODE,DDO_CODE FROM G_OFFICE WHERE TR_CODE=? ORDER BY OFF_EN) G_OFFICE"
                 + " INNER JOIN (SELECT EMP_CODE,AQSL_NO,OFF_CODE FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and emp_code is not null)"
                 + " AQ_MAST ON G_OFFICE.OFF_CODE=AQ_MAST.OFF_CODE) AQ_MAST"
                 + " INNER JOIN (SELECT AQSL_NO,ACC_NO,AD_AMT,SCHEDULE,AQ_YEAR,AQ_MONTH FROM AQ_DTLS WHERE AQ_YEAR=? AND AQ_MONTH=? AND SCHEDULE='LIC'"
                 + " AND AD_AMT>0) AQ_DTLS"
                 + " ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO) AQ_MAST"
                 + " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID ORDER BY TRIM(F_NAME)) EMP_MAST"
                 + " LEFT OUTER JOIN G_VILLAGE ON EMP_MAST.PRM_VILL_CODE=G_VILLAGE.VILL_CODE"
                 + " LEFT OUTER JOIN G_DISTRICT ON EMP_MAST.PRM_DIST_CODE=G_DISTRICT.DIST_CODE";*/
                /*select aq_mast.aqsl_no, bill_mast.off_code, aq_mast.emp_code, ad_amt from bill_mast 
                 inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no
                 inner join aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no
                 and aq_mast.aq_year=aq_dtls.aq_year and aq_mast.aq_month=aq_dtls.aq_month
                 INNER JOIN emp_mast mast ON aq_mast.emp_code=mast.emp_id
                 LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code
                 LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code
                 inner join g_office on aq_mast.off_code=g_office.off_code
                 where bill_mast.aq_year=2018 and bill_mast.aq_month=9 and bill_mast.tr_code='1891' and ad_amt>0 and ad_code='LIC'*/
                /*String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,mast.prm_pin,gv.village_name,gd.dist_name,mast.MOBILE,mast.PRM_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en FROM g_office goffice"
                 + " INNER JOIN aq_mast aqmast on goffice.off_code=aqmast.off_code"
                 + " INNER JOIN " + aqdtlsTblName + " dtls ON aqmast.aqsl_no=dtls.aqsl_no and aqmast.aq_year=dtls.aq_year and aqmast.aq_month=dtls.aq_month"
                 + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                 + " LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code"
                 + " LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code"
                 + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? and dtls.aq_year=? and dtls.aq_month=? ORDER BY TRIM(F_NAME)";*/
                /*String sql = " select emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "+
                 " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en from bill_mast " +
                 " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no " +
                 " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no " +
                 " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id " +
                 " inner join g_office on aq_mast.off_code=g_office.off_code " +
                 " where bill_mast.aq_year=? and bill_mast.aq_month=? and bill_mast.tr_code=? and aq_mast.AQ_YEAR=? "+
                 " and aq_mast.AQ_MONTH=? AND aq_mast.emp_code is not null and ad_amt>0 and ad_code=? ORDER BY TRIM(F_NAME)";*/
                /*String sql = " select emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "+
                 " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en from bill_mast " +
                 " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no " +
                 " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no " +
                 " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id " +
                 " inner join g_office on aq_mast.off_code=g_office.off_code " +
                 " where extract (year from bill_mast.vch_date)=? and extract (month from vch_date)=? and bill_mast.tr_code=? "+
                 " AND aq_mast.emp_code is not null and ad_amt>0 and ad_code=? ORDER BY TRIM(F_NAME)";*/
                for (int i = 0; i < paybilllist.size(); i++) {
                    aqdtlsTblName = "AQ_DTLS";
                    BillBean bb = (BillBean) paybilllist.get(i);

                    aqdtlsTblName = AqFunctionalities.getAQBillDtlsTable(bb.getBillMonth(), bb.getBillYear());
                    String sql = " select bill_mast.aq_month,bill_mast.aq_year,emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "
                            + " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en,bt_id,tab1.present,tab1.permanent from bill_mast "
                            + " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no "
                            + " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no "
                            + " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id "
                            + " inner join g_office on aq_mast.off_code=g_office.off_code "
                             + "inner join (select  emp_id,max(address) filter (where address_type='PERMANENT') as PERMANENT,max(address) filter (where address_type='PRESENT') as PRESENT "
                            + "from emp_address  group by emp_id)tab1 on  tab1.emp_id=emp_mast.emp_id "
                            + " where bill_mast.bill_no=? and bill_mast.aq_year=? and bill_mast.aq_month=? and aq_mast.emp_code is not null and ad_amt>0 and bt_id=? ORDER BY TRIM(F_NAME)";
                    pst = con.prepareStatement(sql);

                    pst.setInt(1, Integer.parseInt(bb.getBillno()));
                    pst.setInt(2, bb.getBillYear());
                    pst.setInt(3, bb.getBillMonth());
                    pst.setString(4, "55832");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        licTreasuryBean = new LICTreasuryWiseBean();
                        licTreasuryBean.setEmpid(rs.getString("EMP_ID"));
                        licTreasuryBean.setEmpname(rs.getString("EMP_NAME"));
                        licTreasuryBean.setAccNo(rs.getString("ACC_NO"));
                        licTreasuryBean.setAdAmt(rs.getString("AD_AMT"));
                        licTreasuryBean.setTrCode(rs.getString("TR_CODE"));
                        licTreasuryBean.setOffName(rs.getString("OFF_EN"));
                        licTreasuryBean.setDdocode(rs.getString("DDO_CODE"));
                        licTreasuryBean.setMobile(rs.getString("MOBILE"));
                         licTreasuryBean.setPresentAddr(rs.getString("present"));
                        licTreasuryBean.setPrmAdress(rs.getString("permanent"));
                        licTreasuryBean.setDistName("");
                        licTreasuryBean.setPrmPin("");
                        licTreasuryBean.setSalaryMonth(CommonFunctions.getMonthAsString(rs.getInt("aq_month")) + "-" + rs.getInt("aq_year"));
                        licdata.add(licTreasuryBean);
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                int row = 1;
                int slno = 0;
                if (licdata != null && licdata.size() > 0) {
                    licTreasuryBean = null;
                    for (int i = 0; i < licdata.size(); i++) {
                        licTreasuryBean = (LICTreasuryWiseBean) licdata.get(i);

                        row += 1;
                        slno = slno + 1;

                        label = new Label(0, row, slno + "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(1, row, licTreasuryBean.getEmpid(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(1, 10);

                        label = new Label(2, row, licTreasuryBean.getEmpname(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(2, 50);

                        label = new Label(3, row, licTreasuryBean.getAccNo(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(3, 20);

                        label = new Label(4, row, licTreasuryBean.getAdAmt(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(4, 10);

                        label = new Label(5, row, licTreasuryBean.getSalaryMonth(), innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(6, row, "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(7, row, licTreasuryBean.getTrCode(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(7, 10);

                        label = new Label(8, row, licTreasuryBean.getOffName() + "(" + licTreasuryBean.getDdocode() + ")", innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(8, 25);

                        label = new Label(9, row, licTreasuryBean.getMobile(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(9, 15);

                         label = new Label(10, row, licTreasuryBean.getPresentAddr(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(10, 20);

                        label = new Label(11, row, licTreasuryBean.getPrmAdress(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(11, 20);

                        label = new Label(12, row, licTreasuryBean.getDistName(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(12, 20);

                        label = new Label(13, row, licTreasuryBean.getPrmPin(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(13, 15);
                    }
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                workbook.write();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getYearList() {

        SelectOption so = new SelectOption();

        ArrayList yearList = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            int year2 = year;
            int year1 = year - 1;

            so.setValue(year1 + "");
            so.setLabel(year1 + "");
            yearList.add(so);

            so = new SelectOption();
            so.setValue(year2 + "");
            so.setLabel(year2 + "");
            yearList.add(so);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return yearList;
    }

    @Override
    public List getDivisionList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList divisionlist = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DIV_NAME,DIV_CODE FROM G_LIC_DIVISION GROUP BY DIV_CODE,DIV_NAME ORDER BY DIV_NAME ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("DIV_CODE"));
                so.setLabel(rs.getString("DIV_NAME"));
                divisionlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return divisionlist;
    }

    @Override
    public List getDivisionWiseTreasuryList(String division
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList treasurylist = new ArrayList();

        LICDivisionWiseBean lbean = null;
        try {
            con = this.dataSource.getConnection();

            if (division != null && !division.equals("")) {
                String sql = "select tr_code,tr_name from g_lic_division where div_code=? order by tr_name asc";
                pst = con.prepareStatement(sql);
                pst.setString(1, division);
                rs = pst.executeQuery();
                while (rs.next()) {
                    lbean = new LICDivisionWiseBean();
                    lbean.setTreasuryName(rs.getString("tr_name"));
                    lbean.setTreasuryCode(rs.getString("tr_code"));
                    treasurylist.add(lbean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasurylist;
    }

    @Override
    public void downloadExcelLICDivisionWise(HttpServletResponse response, String treasury, String year, String month
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        BufferedOutputStream output = null;
        String excelname = "";

        String[] treasuryCode = treasury.split(",");

        String aqdtlsTblName = "AQ_DTLS";
        try {
            con = this.dataSource.getConnection();

            aqdtlsTblName = AqFunctionalities.getAQBillDtlsTable(Integer.parseInt(month), Integer.parseInt(year));
            excelname = "LIC_" + CommonFunctions.getTreasuryName(con, treasuryCode[0]) + "_" + CalendarCommonMethods.getMonthAsString(Integer.parseInt(month)) + "_" + year;
            excelname = excelname.replace(" ", "_");
            String fileName = excelname + ".xls";

            //if((lform.getSltTreasury() != null && !lform.getSltTreasury().equals("")) && (lform.getSltYear() != null && !lform.getSltYear().equals("")) && (lform.getSltMonth() != null && !lform.getSltMonth().equals(""))){
            if (treasuryCode != null && treasuryCode.length > 0) {
                output = new BufferedOutputStream(response.getOutputStream());

                WritableWorkbook workbook = Workbook.createWorkbook(output);

                WritableSheet sheet = workbook.createSheet("DIVISIONWISE_LIC_REPORT", 0);

                WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat headcell = new WritableCellFormat(headformat);
                WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                WritableCellFormat innercell = new WritableCellFormat(cellformat);

                Label label = new Label(0, 0, "SL NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(1, 0, "HRMS ID", headcell);//column,row
                sheet.addCell(label);
                label = new Label(2, 0, "EMPL NAME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(3, 0, "POLICY NUMBER", headcell);//column,row
                sheet.addCell(label);
                label = new Label(4, 0, "AMOUNT", headcell);//column,row
                sheet.addCell(label);
                label = new Label(5, 0, "DUE FROM", headcell);//column,row
                sheet.addCell(label);
                label = new Label(6, 0, "DUE TO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(7, 0, "TREASURY NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(8, 0, "EMPLOYEE LOCATION CODE", headcell);//column,row
                sheet.addCell(label);
                label = new Label(9, 0, "MOBILE NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(10, 0, "PERMANENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(11, 0, "VILLAGE NME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(12, 0, "DIST NAME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(13, 0, "PERMANENT PIN", headcell);//column,row
                sheet.addCell(label);
                label = new Label(14, 0, "PRESENT ADDRESS", headcell);//column,row
                sheet.addCell(label);

                /*String sql = "SELECT EMP_ID,EMP_NAME,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN,MOBILE,PRM_ADDRESS,VILLAGE_NAME,DIST_NAME,PRM_PIN FROM (" + 
                 " SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,AQ_MAST.TR_CODE,OFF_EN,AQ_MAST.DDO_CODE,MOBILE,ACC_NO,AD_AMT,PRM_ADDRESS,PRM_VILL_CODE,PRM_DIST_CODE,PRM_PIN FROM (" + 
                 " SELECT EMP_CODE,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN FROM (" + 
                 " SELECT AQSL_NO,AQ_MAST.OFF_CODE,OFF_EN,DDO_CODE,EMP_CODE,TR_CODE FROM (" + 
                 " SELECT OFF_CODE,OFF_EN,TR_CODE,DDO_CODE FROM G_OFFICE WHERE TR_CODE=? ORDER BY OFF_EN) G_OFFICE" + 
                 " INNER JOIN (SELECT EMP_CODE,AQSL_NO,OFF_CODE FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and emp_code is not null)" + 
                 " AQ_MAST ON G_OFFICE.OFF_CODE=AQ_MAST.OFF_CODE) AQ_MAST" + 
                 " INNER JOIN (SELECT AQSL_NO,ACC_NO,AD_AMT,SCHEDULE,AQ_YEAR,AQ_MONTH FROM AQ_DTLS WHERE AQ_YEAR=? AND AQ_MONTH=? AND SCHEDULE='LIC'" + 
                 " AND AD_AMT>0) AQ_DTLS" + 
                 " ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO) AQ_MAST" + 
                 " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID ORDER BY TRIM(F_NAME)) EMP_MAST" + 
                 " LEFT OUTER JOIN G_VILLAGE ON EMP_MAST.PRM_VILL_CODE=G_VILLAGE.VILL_CODE" + 
                 " LEFT OUTER JOIN G_DISTRICT ON EMP_MAST.PRM_DIST_CODE=G_DISTRICT.DIST_CODE";*/
                /*String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,mast.prm_pin,gv.village_name,gd.dist_name,mast.MOBILE,mast.PRM_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en FROM g_office goffice"
                 + " INNER JOIN aq_mast aqmast on goffice.off_code=aqmast.off_code"
                 + " INNER JOIN " + aqdtlsTblName + " dtls ON aqmast.aqsl_no=dtls.aqsl_no"
                 + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                 + " LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code"
                 + " LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code"
                 + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? ORDER BY TRIM(F_NAME)";*/
                String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,EMP_ADDRESS.pin PRM_PIN,gv.village_name,gd.dist_name,mast.MOBILE,EMP_ADDRESS.ADDRESS PRM_ADDRESS,EMP_ADDRESS1.ADDRESS PRESENT_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en from bill_mast"
                        + " inner join aq_mast aqmast on bill_mast.bill_no=aqmast.bill_no"
                        + " inner join aq_dtls dtls on aqmast.aqsl_no=dtls.aqsl_no"
                        + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                        + " LEFT OUTER JOIN (SELECT * FROM EMP_ADDRESS WHERE ADDRESS_TYPE='PERMANENT')EMP_ADDRESS ON mast.EMP_ID=EMP_ADDRESS.EMP_ID "
                        + "  LEFT OUTER JOIN (SELECT * FROM EMP_ADDRESS WHERE ADDRESS_TYPE='PRESENT')EMP_ADDRESS1 ON mast.EMP_ID=EMP_ADDRESS1.EMP_ID "
                        + " LEFT OUTER JOIN g_village gv ON EMP_ADDRESS.vill_code=gv.vill_code"
                        + " LEFT OUTER JOIN g_district gd ON EMP_ADDRESS.dist_code=gd.dist_code"
                        + " inner join g_office goffice on aqmast.off_code=goffice.off_code"
                        + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? ORDER BY TRIM(F_NAME)";
                pst = con.prepareStatement(sql);

                int row = 1;
                int slno = 0;

                for (int i = 0; i < treasuryCode.length; i++) {

                    pst.setInt(1, Integer.parseInt(month));
                    pst.setInt(2, Integer.parseInt(year));
                    pst.setString(3, "LIC");
                    pst.setString(4, treasuryCode[i]);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        row += 1;
                        slno = slno + 1;

                        label = new Label(0, row, slno + "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(1, row, rs.getString("EMP_ID"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(1, 10);

                        label = new Label(2, row, rs.getString("EMP_NAME"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(2, 50);

                        label = new Label(3, row, rs.getString("ACC_NO"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(3, 20);

                        label = new Label(4, row, rs.getString("AD_AMT"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(4, 10);

                        label = new Label(5, row, "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(6, row, "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(7, row, rs.getString("TR_CODE"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(7, 10);

                        label = new Label(8, row, rs.getString("OFF_EN") + "(" + rs.getString("DDO_CODE") + ")", innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(8, 25);

                        label = new Label(9, row, rs.getString("MOBILE"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(9, 15);

                        label = new Label(10, row, rs.getString("PRM_ADDRESS"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(10, 20);

                        label = new Label(11, row, rs.getString("VILLAGE_NAME"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(11, 20);

                        label = new Label(12, row, rs.getString("DIST_NAME"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(12, 20);

                        label = new Label(13, row, rs.getString("PRM_PIN"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(13, 15);
                        label = new Label(14, row, rs.getString("PRESENT_ADDRESS"), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(14, 20);
                    }
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                workbook.write();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void createExcelLICTreasuryWise(OutputStream output, String treasury, ArrayList paybilllist) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String excelname = "";

        String aqdtlsTblName = "AQ_DTLS";

        LICTreasuryWiseBean licTreasuryBean = null;
        ArrayList licdata = new ArrayList();
        try {
            con = dataSource.getConnection();

            if ((treasury != null && !treasury.equals(""))) {

                excelname = "LIC_" + treasury;
                excelname = excelname.replace(" ", "_");
                String fileName = excelname + ".xls";

                WritableWorkbook workbook = Workbook.createWorkbook(output);

                WritableSheet sheet = workbook.createSheet("TREASURYWISE_LIC_REPORT", 0);

                WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat headcell = new WritableCellFormat(headformat);
                WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                WritableCellFormat innercell = new WritableCellFormat(cellformat);

                Label label = new Label(0, 0, "SL NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(1, 0, "HRMS ID", headcell);//column,row
                sheet.addCell(label);
                label = new Label(2, 0, "EMPL NAME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(3, 0, "POLICY NUMBER", headcell);//column,row
                sheet.addCell(label);
                label = new Label(4, 0, "AMOUNT", headcell);//column,row
                sheet.addCell(label);
                label = new Label(5, 0, "SALARY MONTH", headcell);//column,row
                sheet.addCell(label);
                label = new Label(6, 0, "DUE TO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(7, 0, "TREASURY NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(8, 0, "EMPLOYEE LOCATION CODE", headcell);//column,row
                sheet.addCell(label);
                label = new Label(9, 0, "MOBILE NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(10, 0, "PRESENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(11, 0, "PERMANENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(12, 0, "ADDRESS LINE-3", headcell);//column,row
                sheet.addCell(label);
                label = new Label(13, 0, "ADDRESS LINE-4", headcell);//column,row
                sheet.addCell(label);

                /*String sql = "SELECT EMP_ID,EMP_NAME,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN,MOBILE,PRM_ADDRESS,VILLAGE_NAME,DIST_NAME,PRM_PIN FROM ("
                 + " SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,AQ_MAST.TR_CODE,OFF_EN,AQ_MAST.DDO_CODE,MOBILE,ACC_NO,AD_AMT,PRM_ADDRESS,PRM_VILL_CODE,PRM_DIST_CODE,PRM_PIN FROM ("
                 + " SELECT EMP_CODE,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN FROM ("
                 + " SELECT AQSL_NO,AQ_MAST.OFF_CODE,OFF_EN,DDO_CODE,EMP_CODE,TR_CODE FROM ("
                 + " SELECT OFF_CODE,OFF_EN,TR_CODE,DDO_CODE FROM G_OFFICE WHERE TR_CODE=? ORDER BY OFF_EN) G_OFFICE"
                 + " INNER JOIN (SELECT EMP_CODE,AQSL_NO,OFF_CODE FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and emp_code is not null)"
                 + " AQ_MAST ON G_OFFICE.OFF_CODE=AQ_MAST.OFF_CODE) AQ_MAST"
                 + " INNER JOIN (SELECT AQSL_NO,ACC_NO,AD_AMT,SCHEDULE,AQ_YEAR,AQ_MONTH FROM AQ_DTLS WHERE AQ_YEAR=? AND AQ_MONTH=? AND SCHEDULE='LIC'"
                 + " AND AD_AMT>0) AQ_DTLS"
                 + " ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO) AQ_MAST"
                 + " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID ORDER BY TRIM(F_NAME)) EMP_MAST"
                 + " LEFT OUTER JOIN G_VILLAGE ON EMP_MAST.PRM_VILL_CODE=G_VILLAGE.VILL_CODE"
                 + " LEFT OUTER JOIN G_DISTRICT ON EMP_MAST.PRM_DIST_CODE=G_DISTRICT.DIST_CODE";*/
                /*select aq_mast.aqsl_no, bill_mast.off_code, aq_mast.emp_code, ad_amt from bill_mast 
                 inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no
                 inner join aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no
                 and aq_mast.aq_year=aq_dtls.aq_year and aq_mast.aq_month=aq_dtls.aq_month
                 INNER JOIN emp_mast mast ON aq_mast.emp_code=mast.emp_id
                 LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code
                 LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code
                 inner join g_office on aq_mast.off_code=g_office.off_code
                 where bill_mast.aq_year=2018 and bill_mast.aq_month=9 and bill_mast.tr_code='1891' and ad_amt>0 and ad_code='LIC'*/
                /*String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,mast.prm_pin,gv.village_name,gd.dist_name,mast.MOBILE,mast.PRM_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en FROM g_office goffice"
                 + " INNER JOIN aq_mast aqmast on goffice.off_code=aqmast.off_code"
                 + " INNER JOIN " + aqdtlsTblName + " dtls ON aqmast.aqsl_no=dtls.aqsl_no and aqmast.aq_year=dtls.aq_year and aqmast.aq_month=dtls.aq_month"
                 + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                 + " LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code"
                 + " LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code"
                 + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? and dtls.aq_year=? and dtls.aq_month=? ORDER BY TRIM(F_NAME)";*/
                /*String sql = " select emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "+
                 " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en from bill_mast " +
                 " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no " +
                 " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no " +
                 " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id " +
                 " inner join g_office on aq_mast.off_code=g_office.off_code " +
                 " where bill_mast.aq_year=? and bill_mast.aq_month=? and bill_mast.tr_code=? and aq_mast.AQ_YEAR=? "+
                 " and aq_mast.AQ_MONTH=? AND aq_mast.emp_code is not null and ad_amt>0 and ad_code=? ORDER BY TRIM(F_NAME)";*/
                /*String sql = " select emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "+
                 " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en from bill_mast " +
                 " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no " +
                 " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no " +
                 " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id " +
                 " inner join g_office on aq_mast.off_code=g_office.off_code " +
                 " where extract (year from bill_mast.vch_date)=? and extract (month from vch_date)=? and bill_mast.tr_code=? "+
                 " AND aq_mast.emp_code is not null and ad_amt>0 and ad_code=? ORDER BY TRIM(F_NAME)";*/
               
                for (int i = 0; i < paybilllist.size(); i++) {
                    aqdtlsTblName = "AQ_DTLS";
                    BillBean bb = (BillBean) paybilllist.get(i);

                    aqdtlsTblName = AqFunctionalities.getAQBillDtlsTable(bb.getBillMonth(), bb.getBillYear());
                  
                    String sql = "  select bill_mast.bill_no,bill_mast.aq_month,bill_mast.aq_year,emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "
                            + " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en,bt_id,tab1.present,tab1.permanent from bill_mast "
                            + "inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no "
                            + "inner join  " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no "
                            + "INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id "
                            + "inner join g_office on aq_mast.off_code=g_office.off_code "
                            + "inner join (select  emp_id,max(address) filter (where address_type='PERMANENT') as PERMANENT,max(address) filter (where address_type='PRESENT') as PRESENT "
                            + "from emp_address  group by emp_id)tab1 on  tab1.emp_id=emp_mast.emp_id "
                            + "where bill_mast.bill_no=? and bill_mast.aq_year=? and bill_mast.aq_month=? and aq_mast.emp_code is not null "
                            + "and ad_amt>0 and bt_id=? ORDER BY TRIM(F_NAME)";

                    pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(bb.getBillno()));
                    pst.setInt(2, bb.getBillYear());
                    pst.setInt(3, bb.getBillMonth());
                    pst.setString(4, "55832");
                    //pst.setString(4, "LIC");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        licTreasuryBean = new LICTreasuryWiseBean();
                        licTreasuryBean.setEmpid(rs.getString("EMP_ID"));
                        licTreasuryBean.setEmpname(rs.getString("EMP_NAME"));
                        licTreasuryBean.setAccNo(rs.getString("ACC_NO"));
                        licTreasuryBean.setAdAmt(rs.getString("AD_AMT"));
                        licTreasuryBean.setTrCode(rs.getString("TR_CODE"));
                        licTreasuryBean.setOffName(rs.getString("OFF_EN"));
                        licTreasuryBean.setDdocode(rs.getString("DDO_CODE"));
                        licTreasuryBean.setMobile(rs.getString("MOBILE"));
                        licTreasuryBean.setPresentAddr(rs.getString("present"));
                        licTreasuryBean.setPrmAdress(rs.getString("permanent"));
                        licTreasuryBean.setDistName("");
                        licTreasuryBean.setPrmPin("");
                        licTreasuryBean.setSalaryMonth(CommonFunctions.getMonthAsString(rs.getInt("aq_month")) + "-" + rs.getInt("aq_year"));
                        licdata.add(licTreasuryBean);
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                int row = 1;
                int slno = 0;
                if (licdata != null && licdata.size() > 0) {
                    licTreasuryBean = null;
                    for (int i = 0; i < licdata.size(); i++) {
                        licTreasuryBean = (LICTreasuryWiseBean) licdata.get(i);

                        row += 1;
                        slno = slno + 1;

                        label = new Label(0, row, slno + "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(1, row, licTreasuryBean.getEmpid(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(1, 10);

                        label = new Label(2, row, licTreasuryBean.getEmpname(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(2, 50);

                        label = new Label(3, row, licTreasuryBean.getAccNo(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(3, 20);

                        label = new Label(4, row, licTreasuryBean.getAdAmt(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(4, 10);

                        label = new Label(5, row, licTreasuryBean.getSalaryMonth(), innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(6, row, "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(7, row, licTreasuryBean.getTrCode(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(7, 10);

                        label = new Label(8, row, licTreasuryBean.getOffName() + "(" + licTreasuryBean.getDdocode() + ")", innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(8, 25);

                        label = new Label(9, row, licTreasuryBean.getMobile(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(9, 15);

                        label = new Label(10, row, licTreasuryBean.getPresentAddr(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(10, 20);

                        label = new Label(11, row, licTreasuryBean.getPrmAdress(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(11, 20);

                        label = new Label(12, row, licTreasuryBean.getDistName(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(12, 20);

                        label = new Label(13, row, licTreasuryBean.getPrmPin(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(13, 15);
                    }
                }

                workbook.write();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void createExcelLICDivisionWise(OutputStream output, String treasury, String year, String month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String excelname = "";

        String[] treasuryCode = treasury.split(",");

        String aqdtlsTblName = "AQ_DTLS";

        LICDivisionWiseBean licdivisionbean = null;
        ArrayList licdivisionwisedata = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            //if((lform.getSltTreasury() != null && !lform.getSltTreasury().equals("")) && (lform.getSltYear() != null && !lform.getSltYear().equals("")) && (lform.getSltMonth() != null && !lform.getSltMonth().equals(""))){
            if (treasuryCode != null && treasuryCode.length > 0) {

                WritableWorkbook workbook = Workbook.createWorkbook(output);

                WritableSheet sheet = workbook.createSheet("DIVISIONWISE_LIC_REPORT", 0);

                WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat headcell = new WritableCellFormat(headformat);
                WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                WritableCellFormat innercell = new WritableCellFormat(cellformat);

                Label label = new Label(0, 0, "SL NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(1, 0, "HRMS ID", headcell);//column,row
                sheet.addCell(label);
                label = new Label(2, 0, "EMPL NAME", headcell);//column,row
                sheet.addCell(label);
                label = new Label(3, 0, "POLICY NUMBER", headcell);//column,row
                sheet.addCell(label);
                label = new Label(4, 0, "AMOUNT", headcell);//column,row
                sheet.addCell(label);
                label = new Label(5, 0, "SALARY MONTH", headcell);//column,row
                sheet.addCell(label);
                label = new Label(6, 0, "DUE TO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(7, 0, "TREASURY NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(8, 0, "EMPLOYEE LOCATION CODE", headcell);//column,row
                sheet.addCell(label);
                label = new Label(9, 0, "MOBILE NO", headcell);//column,row
                sheet.addCell(label);
                label = new Label(10, 0, "PRESENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(11, 0, "PERMANENT ADDRESS", headcell);//column,row
                sheet.addCell(label);
                label = new Label(12, 0, "ADDRESS LINE-3", headcell);//column,row
                sheet.addCell(label);
                label = new Label(13, 0, "ADDRESS LINE-4", headcell);//column,row
                sheet.addCell(label);

                /*String sql = "SELECT EMP_ID,EMP_NAME,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN,MOBILE,PRM_ADDRESS,VILLAGE_NAME,DIST_NAME,PRM_PIN FROM (" + 
                 " SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,AQ_MAST.TR_CODE,OFF_EN,AQ_MAST.DDO_CODE,MOBILE,ACC_NO,AD_AMT,PRM_ADDRESS,PRM_VILL_CODE,PRM_DIST_CODE,PRM_PIN FROM (" + 
                 " SELECT EMP_CODE,ACC_NO,AD_AMT,TR_CODE,DDO_CODE,OFF_EN FROM (" + 
                 " SELECT AQSL_NO,AQ_MAST.OFF_CODE,OFF_EN,DDO_CODE,EMP_CODE,TR_CODE FROM (" + 
                 " SELECT OFF_CODE,OFF_EN,TR_CODE,DDO_CODE FROM G_OFFICE WHERE TR_CODE=? ORDER BY OFF_EN) G_OFFICE" + 
                 " INNER JOIN (SELECT EMP_CODE,AQSL_NO,OFF_CODE FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? and emp_code is not null)" + 
                 " AQ_MAST ON G_OFFICE.OFF_CODE=AQ_MAST.OFF_CODE) AQ_MAST" + 
                 " INNER JOIN (SELECT AQSL_NO,ACC_NO,AD_AMT,SCHEDULE,AQ_YEAR,AQ_MONTH FROM AQ_DTLS WHERE AQ_YEAR=? AND AQ_MONTH=? AND SCHEDULE='LIC'" + 
                 " AND AD_AMT>0) AQ_DTLS" + 
                 " ON AQ_MAST.AQSL_NO=AQ_DTLS.AQSL_NO) AQ_MAST" + 
                 " INNER JOIN EMP_MAST ON AQ_MAST.EMP_CODE=EMP_MAST.EMP_ID ORDER BY TRIM(F_NAME)) EMP_MAST" + 
                 " LEFT OUTER JOIN G_VILLAGE ON EMP_MAST.PRM_VILL_CODE=G_VILLAGE.VILL_CODE" + 
                 " LEFT OUTER JOIN G_DISTRICT ON EMP_MAST.PRM_DIST_CODE=G_DISTRICT.DIST_CODE";*/
                /*String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,mast.prm_pin,gv.village_name,gd.dist_name,mast.MOBILE,mast.PRM_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en FROM g_office goffice"
                 + " INNER JOIN aq_mast aqmast on goffice.off_code=aqmast.off_code"
                 + " INNER JOIN " + aqdtlsTblName + " dtls ON aqmast.aqsl_no=dtls.aqsl_no"
                 + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                 + " LEFT OUTER JOIN g_village gv ON mast.prm_vill_code=gv.vill_code"
                 + " LEFT OUTER JOIN g_district gd ON mast.prm_dist_code=gd.dist_code"
                 + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? ORDER BY TRIM(F_NAME)";*/
                /*String sql = "SELECT mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,EMP_ADDRESS.pin PRM_PIN,gv.village_name,gd.dist_name,mast.MOBILE,EMP_ADDRESS.ADDRESS PRM_ADDRESS,dtls.ad_amt,dtls.acc_no,goffice.tr_code,goffice.ddo_code,goffice.off_en from bill_mast"
                 + " inner join aq_mast aqmast on bill_mast.bill_no=aqmast.bill_no"
                 + " inner join aq_dtls dtls on aqmast.aqsl_no=dtls.aqsl_no"
                 + " INNER JOIN emp_mast mast ON aqmast.emp_code=mast.emp_id"
                 + " LEFT OUTER JOIN (SELECT * FROM EMP_ADDRESS WHERE ADDRESS_TYPE='PERMANENT')EMP_ADDRESS ON mast.EMP_ID=EMP_ADDRESS.EMP_ID "
                 + " LEFT OUTER JOIN g_village gv ON EMP_ADDRESS.vill_code=gv.vill_code"
                 + " LEFT OUTER JOIN g_district gd ON EMP_ADDRESS.dist_code=gd.dist_code"
                 + " inner join g_office goffice on aqmast.off_code=goffice.off_code"
                 + " WHERE aqmast.AQ_MONTH=? AND aqmast.AQ_YEAR=? and aqmast.emp_code is not null AND dtls.SCHEDULE=? AND dtls.ad_amt>0 AND goffice.TR_CODE=? ORDER BY TRIM(F_NAME)";*/
                int row = 1;
                int slno = 0;

                for (int i = 0; i < treasuryCode.length; i++) {

                    ArrayList payBillList = getPayBillList(Integer.parseInt(year), Integer.parseInt(month), treasuryCode[i]);
                    for (int j = 0; j < payBillList.size(); j++) {
                        aqdtlsTblName = "AQ_DTLS";
                        BillBean bb = (BillBean) payBillList.get(j);

                        aqdtlsTblName = AqFunctionalities.getAQBillDtlsTable(bb.getBillMonth(), bb.getBillYear());

                        String sql = " select bill_mast.aq_month,bill_mast.aq_year,emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME, "
                                + " emp_mast.MOBILE,aq_dtls.ad_amt,aq_dtls.acc_no,g_office.tr_code,g_office.ddo_code,g_office.off_en,bt_id,tab1.present,tab1.permanent from bill_mast "
                                + " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no "
                                + " inner join " + aqdtlsTblName + " aq_dtls on aq_mast.aqsl_no=aq_dtls.aqsl_no "
                                + " INNER JOIN emp_mast ON aq_mast.emp_code=emp_mast.emp_id "
                                + " inner join g_office on aq_mast.off_code=g_office.off_code "
                                + "inner join (select  emp_id,max(address) filter (where address_type='PERMANENT') as PERMANENT,max(address) filter (where address_type='PRESENT') as PRESENT "
                                + "from emp_address  group by emp_id)tab1 on  tab1.emp_id=emp_mast.emp_id "
                                + " where bill_mast.bill_no=? and bill_mast.aq_year=? and bill_mast.aq_month=? and aq_mast.emp_code is not null and ad_amt>0 and bt_id=? ORDER BY TRIM(F_NAME)";
                        pst = con.prepareStatement(sql);
                        pst.setInt(1, Integer.parseInt(bb.getBillno()));
                        pst.setInt(2, bb.getBillYear());
                        pst.setInt(3, bb.getBillMonth());
                        pst.setString(4, "55832");
                        rs = pst.executeQuery();

                        while (rs.next()) {

                            licdivisionbean = new LICDivisionWiseBean();
                            licdivisionbean.setEmpid(rs.getString("EMP_ID"));
                            licdivisionbean.setEmpname(rs.getString("EMP_NAME"));
                            licdivisionbean.setAccNo(rs.getString("ACC_NO"));
                            licdivisionbean.setAdAmt(rs.getString("AD_AMT"));
                            licdivisionbean.setTrCode(rs.getString("TR_CODE"));
                            licdivisionbean.setOffName(rs.getString("OFF_EN"));
                            licdivisionbean.setDdocode(rs.getString("DDO_CODE"));
                            licdivisionbean.setMobile(rs.getString("MOBILE"));
                            licdivisionbean.setPresentAddr(rs.getString("present"));
                            licdivisionbean.setPrmAdress(rs.getString("permanent"));
                            licdivisionbean.setDistName("");
                            licdivisionbean.setPrmPin("");
                            licdivisionbean.setSalaryMonth(CommonFunctions.getMonthAsString(rs.getInt("aq_month")));
                            licdivisionwisedata.add(licdivisionbean);
                        }
                    }
                }
                if (licdivisionwisedata != null && licdivisionwisedata.size() > 0) {
                    licdivisionbean = null;
                    for (int i = 0; i < licdivisionwisedata.size(); i++) {
                        licdivisionbean = (LICDivisionWiseBean) licdivisionwisedata.get(i);
                        row += 1;
                        slno = slno + 1;

                        label = new Label(0, row, slno + "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(1, row, licdivisionbean.getEmpid(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(1, 10);

                        label = new Label(2, row, licdivisionbean.getEmpname(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(2, 50);

                        label = new Label(3, row, licdivisionbean.getAccNo(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(3, 20);

                        label = new Label(4, row, licdivisionbean.getAdAmt(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(4, 10);

                        label = new Label(5, row, licdivisionbean.getSalaryMonth(), innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(6, row, "", innercell);//column,row
                        sheet.addCell(label);

                        label = new Label(7, row, licdivisionbean.getTrCode(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(7, 10);

                        label = new Label(8, row, licdivisionbean.getOffName() + "(" + licdivisionbean.getDdocode() + ")", innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(8, 25);

                        label = new Label(9, row, licdivisionbean.getMobile(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(9, 15);

                        label = new Label(10, row, licdivisionbean.getPresentAddr(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(10, 20);

                        label = new Label(11, row, licdivisionbean.getPrmAdress(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(11, 20);

                        label = new Label(12, row, licdivisionbean.getDistName(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(12, 20);

                        label = new Label(13, row, licdivisionbean.getPrmPin(), innercell);//column,row
                        sheet.addCell(label);
                        sheet.setColumnView(13, 15);
                    }
                }
                workbook.write();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrayList getPayBillList(int year, int month, String treasuryCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList billList = new ArrayList();

        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT aq_month,aq_year,BILL_NO,major_head,sub_major_head,minor_head,sub_minor_head1,sub_minor_head2,sub_minor_head3,VCH_NO,VCH_DATE,extract (year from vch_date) vch_year, EXTRACT(MONTH FROM vch_date) vch_month,EXTRACT(DAY FROM vch_date) vch_day,ag_treasury_code,DDO_CODE FROM BILL_MAST bill "
                    + "inner join g_treasury treasury on bill.tr_code=treasury.tr_code WHERE extract (year from vch_date)=? AND extract (month from vch_date)=? AND bill.TR_CODE=? AND bill.BILL_STATUS_ID=7");
            pstmt.setInt(1, year);
            pstmt.setInt(2, (month + 1));
            pstmt.setString(3, treasuryCode);

            rs = pstmt.executeQuery();

            String financialYear = "";
            if (month > 2) {
                financialYear = year + "-" + (year + 1);
            } else {
                financialYear = (year - 1) + "-" + year;
            }
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                BillBean bb = new BillBean();
                bb.setBillMonth(rs.getInt("aq_month"));
                bb.setBillYear(rs.getInt("aq_year"));
                bb.setAdjYear(rs.getInt("vch_year"));
                bb.setBillno(rs.getString("BILL_NO"));
                bb.setVoucherno(rs.getString("VCH_NO"));

                if (rs.getDate("VCH_DATE") != null) {
                    bb.setVoucherdate(DATE_FORMAT.format(rs.getDate("VCH_DATE")));
                }
                bb.setVouchermonth(rs.getString("vch_month"));
                bb.setFinyear(financialYear);
                bb.setTreasurycode(rs.getString("ag_treasury_code"));
                bb.setDdocode(rs.getString("DDO_CODE"));
                bb.setMajorhead(rs.getString("major_head"));
                bb.setVoucherDay(rs.getInt("vch_day"));
                billList.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public String getTreasuryNameFromDivision(String divCode, String trCodes) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String trName = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select tr_name from g_lic_division where div_code=? and tr_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, divCode);
            pst.setString(2, trCodes);
            rs = pst.executeQuery();
            if (rs.next()) {
                trName = rs.getString("tr_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trName;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.arrear;

import hrms.SelectOption;
import hrms.common.AqFunctionalities;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.common.CommonReportParamBean;
import hrms.model.employee.Employee;
import hrms.model.payroll.arrear.ArrAqDtlsChildModel;
import hrms.model.payroll.arrear.ArrAqDtlsModel;
import hrms.model.payroll.arrear.ArrAqList;
import hrms.model.payroll.arrear.ArrAqMastModel;
import hrms.model.payroll.arrear.OAClaimModel;
import hrms.model.payroll.arrear.PayRevisionIncrement;
import hrms.model.payroll.arrear.PayRevisionOption;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Manas
 */
public class ArrmastDAOImpl implements ArrmastDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getRepodataSource() {
        return repodataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public String saveArrmastdata(ArrAqMastModel arrAqMastModel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String aqslNo = "";
        try {
            con = dataSource.getConnection();
            aqslNo = arrAqMastModel.getAqGroup() + "_" + arrAqMastModel.getPayMonth() + "_" + arrAqMastModel.getPayYear() + "_" + arrAqMastModel.getSlno();
            pstmt = con.prepareStatement("INSERT INTO ARR_MAST (AQSL_NO,EMP_ID,OFF_CODE,BILL_NO,ARR_TYPE,CHOICE_DATE,EMP_NAME,AQ_GROUP,AQ_GROUP_DESC,P_MONTH,P_YEAR,CUR_DESG,CUR_BASIC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            pstmt.setString(1, aqslNo);
            pstmt.setString(2, arrAqMastModel.getEmpCode());
            pstmt.setString(3, arrAqMastModel.getOffCode());
            pstmt.setInt(4, arrAqMastModel.getBillNo());
            pstmt.setString(5, "PAY");
            pstmt.setDate(6, new java.sql.Date(arrAqMastModel.getChoiceDate().getTime()));
            pstmt.setString(7, arrAqMastModel.getEmpName());
            pstmt.setBigDecimal(8, arrAqMastModel.getAqGroup());
            pstmt.setString(9, arrAqMastModel.getAqGroupDesc());
            pstmt.setInt(10, arrAqMastModel.getPayMonth());
            pstmt.setInt(11, arrAqMastModel.getPayYear());
            pstmt.setString(12, arrAqMastModel.getCurDesg());
            pstmt.setInt(13, arrAqMastModel.getCurBasic());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqslNo;
    }

    @Override
    public int getCalcUniqueNo(String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int calcUniqueNo = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select calc_unique_no from arr_dtls"
                    + " WHERE aqsl_no = ? group by calc_unique_no"
                    + " order by calc_unique_no desc");
            pstmt.setString(1, aqslno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                calcUniqueNo = rs.getInt("calc_unique_no");
            }
            calcUniqueNo++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return calcUniqueNo;
    }

    @Override
    public String addEmployeeToBill(ArrAqMastModel arrAqMastModel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String aqslNo = "";
        try {
            int dataExist = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM ARR_MAST WHERE BILL_NO=?");
            pstmt.setInt(1, arrAqMastModel.getBillNo());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dataExist = 1;
            }
            if (dataExist == 1) {
                pstmt = con.prepareStatement("SELECT MAX(CAST(split_part(AQSL_NO,'_',4) as INTEGER))+1  AS slno FROM ARR_MAST WHERE BILL_NO=?");
                pstmt.setInt(1, arrAqMastModel.getBillNo());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    arrAqMastModel.setSlno(rs.getInt("slno"));
                }
            } else {
                arrAqMastModel.setSlno(1);
            }
            pstmt = con.prepareStatement("SELECT * FROM BILL_MAST WHERE BILL_NO=?");
            pstmt.setInt(1, arrAqMastModel.getBillNo());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                arrAqMastModel.setOffCode(rs.getString("OFF_CODE"));
                arrAqMastModel.setAqGroup(rs.getBigDecimal("BILL_GROUP_ID"));
                arrAqMastModel.setPayMonth(rs.getInt("AQ_MONTH"));
                arrAqMastModel.setPayYear(rs.getInt("AQ_YEAR"));
                arrAqMastModel.setFromMonth(rs.getInt("from_month") + "");
                arrAqMastModel.setFromYear(rs.getInt("from_year") + "");
                arrAqMastModel.setToMonth(rs.getInt("to_month") + "");
                arrAqMastModel.setToYear(rs.getInt("to_year") + "");

                arrAqMastModel.setArrtype(rs.getString("TYPE_OF_BILL"));
                arrAqMastModel.setPercentageArraer(rs.getInt("arrear_percent"));
            }
            boolean isDuplicate = false;
            if (arrAqMastModel.getArrtype() != null && !arrAqMastModel.getArrtype().equals("")) {
                if (arrAqMastModel.getArrtype().equals("ARREAR_J")) {
                    isDuplicate = isJudiciaryFirst25BillAlreadyPrepared(arrAqMastModel.getEmpCode(), arrAqMastModel.getBillNo());
                } else if (arrAqMastModel.getArrtype().equals("ARREAR_6_J")) {
                    isDuplicate = isJudiciarySecond25BillAlreadyPrepared(arrAqMastModel.getEmpCode(), arrAqMastModel.getPercentageArraer(), arrAqMastModel.getBillNo());
                }
            }
            if (isDuplicate == false) {
                pstmt = con.prepareStatement("SELECT F_NAME,M_NAME,L_NAME, POST, CUR_BASIC_SALARY, EMP_MAST.ACCT_TYPE, EMP_MAST.GPF_NO FROM EMP_MAST "
                        + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE EMP_ID=? ");
                pstmt.setString(1, arrAqMastModel.getEmpCode());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    arrAqMastModel.setAccType(rs.getString("ACCT_TYPE"));
                    arrAqMastModel.setGpfAccNo(rs.getString("GPF_NO"));
                    arrAqMastModel.setGpfType(getGPFSeries(arrAqMastModel.getGpfAccNo()));
                    arrAqMastModel.setCurBasic(rs.getInt("CUR_BASIC_SALARY"));
                    arrAqMastModel.setCurDesg(rs.getString("POST"));

                    arrAqMastModel.setEmpName((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                }
                arrAqMastModel.setChoiceDate(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(arrAqMastModel.getInputChoiceDate()));
                aqslNo = arrAqMastModel.getBillNo() + "_" + arrAqMastModel.getPayMonth() + "_" + arrAqMastModel.getPayYear() + "_" + (arrAqMastModel.getSlno() + 1);
                pstmt = con.prepareStatement("INSERT INTO ARR_MAST (AQSL_NO,EMP_ID,OFF_CODE,BILL_NO,ARR_TYPE,CHOICE_DATE,EMP_NAME,AQ_GROUP,AQ_GROUP_DESC,P_MONTH,"
                        + "P_YEAR,CUR_DESG,CUR_BASIC,EMP_TYPE,  gpf_type, acct_type, gpf_acc_no, from_month, from_year, to_month, to_year) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

                pstmt.setString(1, aqslNo);
                pstmt.setString(2, arrAqMastModel.getEmpCode());
                pstmt.setString(3, arrAqMastModel.getOffCode());
                pstmt.setInt(4, arrAqMastModel.getBillNo());
                pstmt.setString(5, arrAqMastModel.getArrtype());
                pstmt.setDate(6, new java.sql.Date(arrAqMastModel.getChoiceDate().getTime()));
                pstmt.setString(7, arrAqMastModel.getEmpName());
                pstmt.setBigDecimal(8, arrAqMastModel.getAqGroup());
                pstmt.setString(9, arrAqMastModel.getAqGroupDesc());
                pstmt.setInt(10, arrAqMastModel.getPayMonth());
                pstmt.setInt(11, arrAqMastModel.getPayYear());
                pstmt.setString(12, arrAqMastModel.getCurDesg());
                pstmt.setInt(13, arrAqMastModel.getCurBasic());
                pstmt.setString(14, "R");

                pstmt.setString(15, arrAqMastModel.getGpfType());
                pstmt.setString(16, arrAqMastModel.getAccType());
                pstmt.setString(17, arrAqMastModel.getGpfAccNo());
                pstmt.setInt(18, Integer.parseInt(arrAqMastModel.getFromMonth()));
                pstmt.setInt(19, Integer.parseInt(arrAqMastModel.getFromYear()));
                pstmt.setInt(20, Integer.parseInt(arrAqMastModel.getToMonth()));
                pstmt.setInt(21, Integer.parseInt(arrAqMastModel.getToYear()));

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqslNo;
    }

    @Override
    public void saveArrdtlsdata(List arrAqDtlsList, String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO ARR_DTLS (AQSL_NO,P_MONTH,P_YEAR,AD_TYPE,ALREADY_PAID, TO_BE_PAID, DRAWN_VIDE_BILLNO, REF_AQSL_NO,CALC_UNIQUE_NO,BT_ID)VALUES (?,?,?,?,?,?,?,?,?,?) ");
            for (int i = 0; i < arrAqDtlsList.size(); i++) {
                ArrAqDtlsModel arrAqDtlsBean = (ArrAqDtlsModel) arrAqDtlsList.get(i);
                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "PAY");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnPayAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDuePayAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();

                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "GP");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnGpAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDueGpAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();

                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "DA");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnDaAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDueDaAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveArrdtlsdata(ArrAqDtlsModel[] arrAqDtlsModels, int calc_unique_no) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO ARR_DTLS (AQSL_NO,P_MONTH,P_YEAR,AD_TYPE,ALREADY_PAID, TO_BE_PAID, DRAWN_VIDE_BILLNO, REF_AQSL_NO,CALC_UNIQUE_NO,BT_ID)VALUES (?,?,?,?,?,?,?,?,?,?) ");
            for (int i = 0; i < arrAqDtlsModels.length; i++) {
                ArrAqDtlsModel arrAqDtlsModel = arrAqDtlsModels[i];
                pstmt.setString(1, arrAqDtlsModel.getAqslno());
                pstmt.setInt(2, arrAqDtlsModel.getPayMonth());
                pstmt.setInt(3, arrAqDtlsModel.getPayYear());
                pstmt.setString(4, arrAqDtlsModel.getAdType());
                pstmt.setInt(5, arrAqDtlsModel.getDrawnAMt());
                pstmt.setInt(6, arrAqDtlsModel.getDueAmt());
                pstmt.setString(7, arrAqDtlsModel.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsModel.getRefaqslno());
                pstmt.setInt(9, calc_unique_no);
                pstmt.setString(10, arrAqDtlsModel.getBtid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getArrearBillDtls(int billno, String aqMonth, String aqYear) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List arrEmpList = new ArrayList();
        ArrAqMastModel arrAqBean = null;
        int slNo = 1;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQSL_NO, EMP_NAME,CUR_DESG, FULL_ARREAR_PAY, ARREAR_PAY,CPF_HEAD,INCTAX,PT,REMARK,GPF_NO,other_recovery FROM ARR_MAST "
                    + "INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID = EMP_MAST.EMP_ID WHERE BILL_NO = ? order by EMP_NAME");
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arrAqBean = new ArrAqMastModel();
                arrAqBean.setSlno(slNo);
                arrAqBean.setAqSlNo(rs.getString("AQSL_NO"));
                arrAqBean.setEmpName(rs.getString("EMP_NAME"));
                arrAqBean.setCurDesg(rs.getString("CUR_DESG"));
                arrAqBean.setGrandTotArr100(rs.getInt("FULL_ARREAR_PAY"));
                arrAqBean.setGrandTotArr40(rs.getInt("ARREAR_PAY"));
                arrAqBean.setCpfHead(rs.getInt("CPF_HEAD") + "");
                arrAqBean.setIncomeTaxAmt(rs.getInt("INCTAX") + "");
                arrAqBean.setProfessionalTax(rs.getInt("PT"));
                arrAqBean.setRemark(rs.getString("REMARK"));
                arrAqBean.setGpfAccNo(rs.getString("GPF_NO"));
                arrAqBean.setOtherRecovery(rs.getString("other_recovery"));
                arrEmpList.add(arrAqBean);
                slNo++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrEmpList;
    }

    @Override
    public List getOtherArrearBillDtls(int billno, String aqMonth, String aqYear) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List arrEmpList = new ArrayList();
        ArrAqMastModel arrAqBean = null;
        int slNo = 1;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQSL_NO, EMP_NAME,CUR_DESG, FULL_ARREAR_PAY, getarradtotal(AQSL_NO,'PAY') AS PAY_TOTAL, getarradtotal(AQSL_NO,'GP') AS GP_TOTAL, "
                    + "getarradtotal(AQSL_NO,'DA') AS DA_TOTAL, getarradtotal(AQSL_NO,'HRA') AS HRA_TOTAL, getarradtotal(AQSL_NO,'OA') AS OA_TOTAL, "
                    + "getarradtotal(AQSL_NO,'IR') AS IR_TOTAL, ARREAR_PAY,CPF_HEAD,INCTAX,PT,REMARK,GPF_NO,other_recovery,ddo_recovery FROM ARR_MAST "
                    + "INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID = EMP_MAST.EMP_ID WHERE BILL_NO = ? order by EMP_NAME");
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arrAqBean = new ArrAqMastModel();
                arrAqBean.setSlno(slNo);
                arrAqBean.setAqSlNo(rs.getString("AQSL_NO"));
                arrAqBean.setEmpName(rs.getString("EMP_NAME"));
                arrAqBean.setCurDesg(rs.getString("CUR_DESG"));
                arrAqBean.setGrandTotArr100(rs.getInt("FULL_ARREAR_PAY"));
                arrAqBean.setGrandTotArr40(rs.getInt("ARREAR_PAY"));
                arrAqBean.setCpfHead(rs.getInt("CPF_HEAD") + "");
                arrAqBean.setIncomeTaxAmt(rs.getInt("INCTAX") + "");
                arrAqBean.setProfessionalTax(rs.getInt("PT"));
                arrAqBean.setRemark(rs.getString("REMARK"));
                arrAqBean.setGpfAccNo(rs.getString("GPF_NO"));
                arrAqBean.setPayTotal(rs.getInt("PAY_TOTAL"));
                arrAqBean.setGpTotal(rs.getInt("GP_TOTAL"));
                arrAqBean.setDaTotal(rs.getInt("DA_TOTAL"));
                arrAqBean.setHraTotal(rs.getInt("HRA_TOTAL"));
                arrAqBean.setOaTotal(rs.getInt("OA_TOTAL"));
                arrAqBean.setIrTotal(rs.getInt("IR_TOTAL"));
                arrAqBean.setOtherRecovery(rs.getString("other_recovery"));
                arrAqBean.setDdoRecovery(rs.getString("ddo_recovery"));
                arrEmpList.add(arrAqBean);
                slNo++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrEmpList;
    }

    @Override
    public List getDaArrearBillDtls(int billno, String aqMonth, String aqYear) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List arrEmpList = new ArrayList();
        ArrAqMastModel arrAqBean = null;
        int slNo = 1;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQSL_NO, EMP_NAME,CUR_DESG, FULL_ARREAR_PAY, ARREAR_PAY, CPF_HEAD,INCTAX,PT,REMARK,GPF_NO FROM ARR_MAST "
                    + "INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID = EMP_MAST.EMP_ID WHERE BILL_NO = ? order by EMP_NAME");
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arrAqBean = new ArrAqMastModel();
                arrAqBean.setSlno(slNo);
                arrAqBean.setAqSlNo(rs.getString("AQSL_NO"));
                arrAqBean.setEmpName(rs.getString("EMP_NAME"));
                arrAqBean.setCurDesg(rs.getString("CUR_DESG"));
                arrAqBean.setGrandTotArr100(rs.getInt("FULL_ARREAR_PAY"));
                arrAqBean.setGrandTotArr40(rs.getInt("ARREAR_PAY"));
                arrAqBean.setCpfHead(rs.getInt("CPF_HEAD") + "");
                arrAqBean.setIncomeTaxAmt(rs.getInt("INCTAX") + "");
                arrAqBean.setProfessionalTax(rs.getInt("PT"));
                arrAqBean.setRemark(rs.getString("REMARK"));
                arrAqBean.setGpfAccNo(rs.getString("GPF_NO"));
                arrEmpList.add(arrAqBean);
                slNo++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrEmpList;
    }

    @Override
    public List getArrearAcquaintance(int billNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List arrAqList = new ArrayList();

        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("select emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt from arr_mast "
             + "where bill_no = ? order by emp_name");*/
            pstmt = con.prepareStatement("select acct_type,emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt,other_recovery,ddo_recovery,"
                    + " getarradtotal(AQSL_NO,'PAY') AS PAY_TOTAL,getarradtotal(AQSL_NO,'DA') AS DA_TOTAL,getarradtotal(AQSL_NO,'IR') AS IR_TOTAL, arrear_percent from arr_mast"
                    + " where bill_no = ? order by emp_name");

            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            while (res.next()) {

                ArrAqMastModel aqBean = new ArrAqMastModel();

                aqBean.setEmpCode(res.getString("EMP_ID"));
                aqBean.setEmpName(res.getString("EMP_NAME"));
                aqBean.setCurDesg(res.getString("CUR_DESG"));
                aqBean.setAqSlNo(res.getString("aqsl_no"));
                aqBean.setArrearpay(res.getInt("full_arrear_pay"));
                aqBean.setArrear40(res.getInt("arrear_pay"));
                aqBean.setCurBasic(res.getInt("cur_basic"));
                aqBean.setIncomeTaxAmt(res.getString("inctax"));
                //aqBean.setCpfHead(res.getString("cpf_head"));
                if (res.getInt("arrear_percent") == 30 || res.getInt("arrear_percent") == 20) {
                    aqBean.setCpfHead("0");
                } else {
                    if (res.getInt("cpf_head") > 0) {
                        aqBean.setCpfHead(res.getString("cpf_head"));
                    } else if (res.getInt("cpf_head") == 0) {
                        aqBean.setCpfHead(res.getString("cpf_head"));
                    } else if (res.getString("acct_type") != null && res.getString("acct_type").equals("PRAN")) {
                        double cpfAmt = res.getInt("PAY_TOTAL") + res.getInt("DA_TOTAL") + res.getInt("IR_TOTAL");
                        cpfAmt = cpfAmt * 0.1;
                        aqBean.setCpfHead(Math.round(cpfAmt) + "");
                    }
                }
                aqBean.setPt(res.getString("pt"));
                aqBean.setOtherRecovery(res.getString("other_recovery"));
                aqBean.setDdoRecovery(res.getString("ddo_recovery"));
                aqBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqList;
    }

    @Override
    public List getLeaveArrearAcquaintance(int billNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        List arrAqList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT LEAVE_ARREAR.EMP_ID,F_NAME,M_NAME,L_NAME,POST FROM (SELECT EMP_ID from ARR_MAST WHERE BILL_NO=? GROUP BY EMP_ID)LEAVE_ARREAR "
                    + "INNER JOIN EMP_MAST ON LEAVE_ARREAR.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE");
            pstmt.setInt(1, billNo);
            result = pstmt.executeQuery();
            while (result.next()) {
                ArrAqMastModel aqBean = new ArrAqMastModel();
                String fullName = "";
                if (result.getString("F_NAME") != null && !result.getString("F_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("F_NAME");
                }
                if (result.getString("M_NAME") != null && !result.getString("M_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("M_NAME");
                }
                if (result.getString("L_NAME") != null && !result.getString("L_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("L_NAME");
                }
                aqBean.setEmpCode(result.getString("EMP_ID"));
                aqBean.setEmpName(fullName);
                aqBean.setCurDesg(result.getString("POST"));

                arrAqList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqList;
    }

    @Override
    public List get6pArrearAcquaintanceDtls(String aqSlNo, int j) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<ArrAqDtlsModel> arrAqDtlsList = new ArrayList<>();

        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT auto_incr_id,p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no FROM arr_dtls WHERE aqsl_no =? order by p_year,p_month,calc_unique_no,drawn_vide_billno,ad_type");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            int i = 0;
            ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {
                i++;

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);

                arrAqDtlsBean.setPayMonth(pmonth);
                arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);
                arrAqDtlsBean.setAdType(res.getString("ad_type"));

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                }

                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setAutoincrid(res.getInt("auto_incr_id"));
                int totDueAmt = arrAqDtlsBean.getDuePayAmt() + arrAqDtlsBean.getDueGpAmt() + arrAqDtlsBean.getDueDaAmt();
                arrAqDtlsBean.setDueTotalAmt(totDueAmt);

                int totDrawnAmt = arrAqDtlsBean.getDrawnPayAmt() + arrAqDtlsBean.getDrawnGpAmt() + arrAqDtlsBean.getDrawnDaAmt();
                arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

                int arrear100 = totDueAmt - totDrawnAmt;
                arrAqDtlsBean.setArrear100(arrear100);

                if (i == 3) {
                    //arrAqDtlsBean.setCalcuniqueno(j);

                    arrAqDtlsList.add(arrAqDtlsBean);
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    i = 0;
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsList;
    }

    @Override
    public List getOtherArrearAcquaintanceDtls(String aqSlNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List arrAqDtlsList = new ArrayList();
        List rowsNumList = new ArrayList();

        try {
            int rows = 0;
            int rownum = 0;
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select count(*) as noofrows from arr_dtls WHERE aqsl_no = ? group by p_year,p_month, calc_unique_no order by p_year,p_month,calc_unique_no ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            while (res.next()) {

                rows = res.getInt("noofrows");

                rowsNumList.add(rows + "");
            }

            pstmt = con.prepareStatement("SELECT p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no,bt_id FROM arr_dtls WHERE aqsl_no =? order by p_year,p_month,calc_unique_no,ad_type");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            int i = 0;
            ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                rows = Integer.parseInt((String) rowsNumList.get(rownum));

                i++;

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);
                arrAqDtlsBean.setPayMonth(pmonth);
                arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);
                arrAqDtlsBean.setBtid(res.getString("bt_id"));

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("HRA")) {
                    arrAqDtlsBean.setDueHraAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnHraAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("OA")) {
                    arrAqDtlsBean.setDueOaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnOaAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("IR")) {
                    arrAqDtlsBean.setDueIrAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnIrAmt(res.getInt("already_paid"));
                }
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                int totDueAmt = arrAqDtlsBean.getDuePayAmt() + arrAqDtlsBean.getDueGpAmt() + arrAqDtlsBean.getDueDaAmt() + arrAqDtlsBean.getDueHraAmt() + arrAqDtlsBean.getDueOaAmt() + arrAqDtlsBean.getDueIrAmt();
                arrAqDtlsBean.setDueTotalAmt(totDueAmt);

                int totDrawnAmt = arrAqDtlsBean.getDrawnPayAmt() + arrAqDtlsBean.getDrawnGpAmt() + arrAqDtlsBean.getDrawnDaAmt() + arrAqDtlsBean.getDrawnHraAmt() + arrAqDtlsBean.getDrawnOaAmt() + arrAqDtlsBean.getDrawnIrAmt();
                arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

                int arrear100 = totDueAmt - totDrawnAmt;
                arrAqDtlsBean.setArrear100(arrear100);

                if (i == rows) {
                    rownum++;
                    arrAqDtlsList.add(arrAqDtlsBean);
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    i = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsList;
    }

    @Override
    public List<ArrAqDtlsModel> getArrearAcquaintanceDtls(String aqSlNo, int j) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<ArrAqDtlsModel> arrAqDtlsList = new ArrayList<>();

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("SELECT  p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no FROM arr_dtls WHERE aqsl_no =? order by p_year,p_month,calc_unique_no,drawn_vide_billno,ad_type");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            int i = 0;
            ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {
                i++;

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);

                arrAqDtlsBean.setPayMonth(pmonth);
                arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);
                arrAqDtlsBean.setAdType(res.getString("ad_type"));

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                }

                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));

                int totDueAmt = arrAqDtlsBean.getDuePayAmt() + arrAqDtlsBean.getDueGpAmt() + arrAqDtlsBean.getDueDaAmt();
                arrAqDtlsBean.setDueTotalAmt(totDueAmt);

                int totDrawnAmt = arrAqDtlsBean.getDrawnPayAmt() + arrAqDtlsBean.getDrawnGpAmt() + arrAqDtlsBean.getDrawnDaAmt();
                arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

                int arrear100 = totDueAmt - totDrawnAmt;
                arrAqDtlsBean.setArrear100(arrear100);

                if (i == 3) {
                    arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));

                    arrAqDtlsList.add(arrAqDtlsBean);
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    i = 0;
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsList;
    }

    @Override
    public ArrAqMastModel getArrearAcquaintanceData(String aqSlNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        String acctType = "GPF";
        String ifGPFAssumed = "N";
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, arrear_pay, full_arrear_pay, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqMastBean.setArrearpay(res.getInt("arrear_pay"));
                arrAqMastBean.setFullArrearpay(res.getInt("full_arrear_pay"));
            }
            int j = 1;
            List arrAqDtlsList = getArrearAcquaintanceDtls(aqSlNo, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(grandTotArr100);
            arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * 0.4));

            double percent = .1;
            if (arrAqMastBean.getPercentageArraer() == 10) {
                percent = 0.1;
            } else if (arrAqMastBean.getPercentageArraer() == 50) {
                percent = 0.5;
            } else if (arrAqMastBean.getPercentageArraer() == 60) {
                percent = 0.6;
            } else if (arrAqMastBean.getPercentageArraer() == 30) {
                percent = 0.3;
            } else if (arrAqMastBean.getPercentageArraer() == 20) {
                int billno = 0;
                pst1 = con.prepareStatement("SELECT bill_no,IF_GPF_ASSUMED,ARR_MAST.ACCT_TYPE FROM ARR_MAST"
                        + " INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID=EMP_MAST.EMP_ID"
                        + " WHERE AQSL_NO=?");
                pst1.setString(1, aqSlNo);
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    ifGPFAssumed = rs1.getString("IF_GPF_ASSUMED");
                    acctType = rs1.getString("ACCT_TYPE");
                    billno = rs1.getInt("bill_no");
                }
                if (acctType.equals("PRAN") && ifGPFAssumed.equals("N")) {
                    percent = 0.15;
                    arrAqMastBean.setPercentageArraer(15);
                } else {
                    percent = 0.2;
                    arrAqMastBean.setPercentageArraer(20);
                }
                if (billno == 54377954) {
                    percent = 0.05;
                }
            }

            arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));

            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    public ArrAqMastModel get6ArrearAcquaintanceData(String aqSlNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        String acctType = "GPF";
        String ifGPFAssumed = "N";

        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
            }
            int j = 1;
            List arrAqDtlsList = get6pArrearAcquaintanceDtls(aqSlNo, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(grandTotArr100);
            arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * 0.4));

            double percent = .1;
            if (arrAqMastBean.getPercentageArraer() == 10) {
                percent = 0.1;
            } else if (arrAqMastBean.getPercentageArraer() == 50) {
                percent = 0.5;
            } else if (arrAqMastBean.getPercentageArraer() == 60) {
                percent = 0.6;
            } else if (arrAqMastBean.getPercentageArraer() == 30) {
                percent = 0.3;
            } else if (arrAqMastBean.getPercentageArraer() == 20) {
                //pst1 = con.prepareStatement("SELECT ACCT_TYPE FROM ARR_MAST WHERE AQSL_NO=?");
                pst1 = con.prepareStatement("SELECT IF_GPF_ASSUMED,ARR_MAST.ACCT_TYPE FROM ARR_MAST"
                        + " INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID=EMP_MAST.EMP_ID"
                        + " WHERE AQSL_NO=?");
                pst1.setString(1, aqSlNo);
                rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    acctType = rs1.getString("ACCT_TYPE");
                    ifGPFAssumed = rs1.getString("IF_GPF_ASSUMED");
                }
                if (acctType.equals("PRAN") && ifGPFAssumed.equals("N")) {
                    percent = 0.15;
                    arrAqMastBean.setPercentageArraer(15);
                } else {
                    percent = 0.2;
                }
            }

            arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));

            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }
    /*
     Arrear Aquitance data for leave arrear
     */

    @Override
    public List<ArrAqMastModel> getArrearAcquaintanceData(int billNo, String empCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<ArrAqMastModel> arrAqMastList = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * from ARR_MAST WHERE BILL_NO=? and EMP_ID=? ORDER BY P_YEAR,P_MONTH ");
            pstmt.setInt(1, billNo);
            pstmt.setString(2, empCode);
            res = pstmt.executeQuery();
            while (res.next()) {
                ArrAqMastModel arrAqMastModel = new ArrAqMastModel();
                arrAqMastModel.setPayMonth(res.getInt("P_MONTH"));
                arrAqMastModel.setPayYear(res.getInt("P_YEAR"));
                arrAqMastModel.setCurBasic(res.getInt("CUR_BASIC"));
                arrAqMastList.add(arrAqMastModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastList;
    }

    public boolean isIRHeadPresent(String aqSlNo) {
        boolean irheadpresent = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select count(*) as noofrows from (select distinct ad_type from arr_dtls WHERE aqsl_no =?) t where ad_type = 'IR'");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                if (res.getInt("noofrows") > 0) {
                    irheadpresent = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return irheadpresent;
    }

    @Override
    public ArrAqMastModel getOtherArrearAcquaintanceData(String aqSlNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type,other_recovery from arr_mast \n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setOtherRecovery(res.getString("other_recovery"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));

            }
            List arrAqDtlsList = getOtherArrearAcquaintanceDtls(aqSlNo);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(grandTotArr100);
            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    @Override
    public List getArrearAqHeaderData(int billNo
    ) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        List arrAqList = new ArrayList();

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String arrHdrQry = "select bill_desc,from_month, from_year, to_month, to_year from bill_mast where bill_no = '" + billNo + "'";
            res = stmt.executeQuery(arrHdrQry);
            while (res.next()) {

                ArrAqMastModel aqBean = new ArrAqMastModel();

                aqBean.setBillDesc(res.getString("bill_desc"));
                aqBean.setFromMonth(res.getString("from_month"));
                aqBean.setFromYear(res.getString("from_year"));
                aqBean.setToMonth(res.getString("to_month"));
                aqBean.setToYear(res.getString("to_year"));

                arrAqList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqList;
    }

    @Override
    public int updateArrMastItData(int billNo, String aqSlNo, int incTax
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("update arr_mast set inctax=? where aqsl_no=? and bill_no=?");

            pstmt.setInt(1, incTax);
            pstmt.setString(2, aqSlNo);
            pstmt.setInt(3, billNo);

            res = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int updateArrMastCpfData(int billNo, String aqSlNo, int cpfAmt
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("update arr_mast set cpf_head=? where aqsl_no=? and bill_no=?");

            pstmt.setInt(1, cpfAmt);
            pstmt.setString(2, aqSlNo);
            pstmt.setInt(3, billNo);

            res = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int updateArrMastPtData(int billNo, String aqSlNo, int ptAmt
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("update arr_mast set pt=? where aqsl_no=? and bill_no=?");

            pstmt.setInt(1, ptAmt);
            pstmt.setString(2, aqSlNo);
            pstmt.setInt(3, billNo);

            res = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int reCalculateArrMast(int billNo) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;

        ResultSet resultSet = null;
        ResultSet rs2 = null;

        int res = 0;
        String aqsl_no = "";

        int frommonth = 0;
        int fromyear = 0;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String acctType = "GPF";

        try {
            con = this.dataSource.getConnection();

            pstmt1 = con.prepareStatement("SELECT TYPE_OF_BILL, arrear_percent,from_month,from_year FROM BILL_MAST WHERE BILL_NO=?");
            pstmt1.setInt(1, billNo);
            resultSet = pstmt1.executeQuery();
            String typeOfBill = "ARREAR";
            int arrearPercent = 0;
            if (resultSet.next()) {
                typeOfBill = resultSet.getString("TYPE_OF_BILL");
                arrearPercent = resultSet.getInt("arrear_percent");
                frommonth = resultSet.getInt("from_month");
                fromyear = resultSet.getInt("from_year");
            }

            ps4 = con.prepareStatement("UPDATE ARR_MAST SET ACCT_TYPE=?, BANK_ACC_NO=?, BANK_NAME=?, BRANCH_NAME=?, IFSC_CODE=? , gpf_acc_no=?, gpf_type=? WHERE AQSL_NO=?");

            ps3 = con.prepareStatement(" select emp_mast.gpf_no, getgpfseries(emp_mast.gpf_no) gpf_series, gpf_type, arr_mast.aqsl_no, arr_mast.emp_id, g_branch.bank_code, g_branch.branch_code, emp_mast.acct_type, emp_mast.bank_acc_no, g_branch.ifsc_code from emp_mast "
                    + " inner join arr_mast on emp_mast.emp_id=arr_mast.emp_id "
                    + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code "
                    + " where  arr_mast.bill_no=? ");

            ps3.setInt(1, billNo);
            rs2 = ps3.executeQuery();
            while (rs2.next()) {
                ps4.setString(1, rs2.getString("acct_type"));
                ps4.setString(2, rs2.getString("bank_acc_no"));
                ps4.setString(3, rs2.getString("bank_code"));
                ps4.setString(4, rs2.getString("branch_code"));
                ps4.setString(5, rs2.getString("ifsc_code"));
                ps4.setString(6, rs2.getString("gpf_no"));
                if (rs2.getString("gpf_type") != null && !rs2.getString("gpf_type").equals("")) {
                    ps4.setString(7, rs2.getString("gpf_type"));
                } else {
                    ps4.setString(7, rs2.getString("gpf_series"));
                }

                ps4.setString(8, rs2.getString("aqsl_no"));
                ps4.executeUpdate();
            }

            if (typeOfBill.equals("OTHER_ARREAR")) {
                pstmt1 = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("update arr_mast set arrear_pay = full_arrear_pay WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=10 WHERE AQSL_NO=?");
                System.out.println("frommonth is: " + frommonth + " and fromyear is: " + fromyear);
                if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                    pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=14 WHERE AQSL_NO=?");

                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(getgross_arrear_for_cpf(aqsl_no)*0.1) AS CPFAMT,ROUND(getgross_arrear_for_cpf(aqsl_no)*0.14) AS GCPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("GCPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                } else {
                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(getgross_arrear_for_cpf(aqsl_no)*0.1) AS CPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("CPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                }
                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            } else if (typeOfBill.equals("ARREAR")) {
                pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            } else if (typeOfBill.equals("ARREAR_J")) {
                pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = round(full_arrear_pay*0.25) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=10 WHERE AQSL_NO=?");
                if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                    pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=14 WHERE AQSL_NO=?");

                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(arrear_pay*0.1) AS CPFAMT,ROUND(arrear_pay*0.14) AS GCPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("GCPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                } else {
                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(arrear_pay*0.1) AS CPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("CPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                }

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            } else if (typeOfBill.equals("ARREAR_6_J")) {
                pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=10 WHERE AQSL_NO=?");
                if ((frommonth >= 4 && fromyear == 2019) || fromyear >= 2020) {
                    pstmt2 = con.prepareStatement("UPDATE ARR_MAST SET cpf_head=?,cpf_head_gc=?,percent_cpf_head=14 WHERE AQSL_NO=?");

                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(arrear_pay*0.1) AS CPFAMT,ROUND(arrear_pay*0.14) AS GCPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("GCPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                } else {
                    pstmt1 = con.prepareStatement("SELECT AQSL_NO,ROUND(arrear_pay*0.1) AS CPFAMT FROM ARR_MAST "
                            + "INNER JOIN EMP_MAST ON ARR_MAST.emp_id=EMP_MAST.emp_id "
                            + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE REF_AD_CODE='76') AS update_ad_info ON ARR_MAST.emp_id = update_ad_info.updation_ref_code "
                            + "WHERE BILL_NO=? AND EMP_MAST.acct_type='PRAN' AND FIXEDVALUE IS NULL");
                    pstmt1.setInt(1, billNo);
                    resultSet = pstmt1.executeQuery();
                    while (resultSet.next()) {
                        pstmt2.setInt(1, resultSet.getInt("CPFAMT"));
                        pstmt2.setInt(2, resultSet.getInt("CPFAMT"));
                        pstmt2.setString(3, resultSet.getString("AQSL_NO"));
                        pstmt2.executeUpdate();
                    }
                }

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            } else if (typeOfBill.equals("ARREAR_6")) {
                pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE bill_no = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                double percent = .1;
                if (arrearPercent == 10) {
                    percent = 0.1;
                } else if (arrearPercent == 50) {
                    percent = 0.5;
                } else if (arrearPercent == 60) {
                    percent = 0.6;
                } else if (arrearPercent == 30) {
                    percent = 0.3;
                } else if (arrearPercent == 20) {

                    pst1 = con.prepareStatement("select ACCT_TYPE from arr_mast where bill_no=? GROUP BY ACCT_TYPE");
                    pst1.setInt(1, billNo);
                    rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        acctType = rs1.getString("ACCT_TYPE");
                    }
                    if (acctType.equals("PRAN")) {
                        percent = 0.15;

                        pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " from emp_mast WHERE bill_no = ? and arr_mast.emp_id=emp_mast.emp_id and emp_mast.if_gpf_assumed='N'");
                        pstmt1.setInt(1, billNo);
                        pstmt1.executeUpdate();

                        percent = 0.2;

                        pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " from emp_mast WHERE bill_no = ? and arr_mast.emp_id=emp_mast.emp_id and emp_mast.if_gpf_assumed='Y'");
                        pstmt1.setInt(1, billNo);
                        pstmt1.executeUpdate();

                    } else {
                        percent = 0.2;
                        pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " WHERE bill_no = ? ");
                        pstmt1.setInt(1, billNo);
                        pstmt1.executeUpdate();
                    }
                }

                if (arrearPercent != 20) {
                    pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay*" + percent + " WHERE bill_no = ? ");
                    pstmt1.setInt(1, billNo);
                    pstmt1.executeUpdate();
                }

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = getbilltotgross_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = getbilltotded_arrear(BILL_NO) WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            } else if (typeOfBill.equals("ARREAR_NPS")) {
                /*pstmt1 = con.prepareStatement("UPDATE arr_mast set full_arrear_pay = getgross_nps_arrear(aqsl_no) WHERE bill_no = ? ");
                 pstmt1.setInt(1, billNo);
                 pstmt1.executeUpdate();

                 pstmt1 = con.prepareStatement("UPDATE arr_mast set arrear_pay = full_arrear_pay WHERE bill_no = ?");
                 pstmt1.setInt(1, billNo);
                 pstmt1.executeUpdate();*/

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET GROSS_AMT = 0 WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                pstmt1.executeUpdate();

                pstmt1 = con.prepareStatement("UPDATE BILL_MAST SET DED_AMT = 0 WHERE BILL_NO = ? ");
                pstmt1.setInt(1, billNo);
                res = pstmt1.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, rs2);
            DataBaseFunctions.closeSqlObjects(pstmt1, pstmt2);
            DataBaseFunctions.closeSqlObjects(ps3, ps4);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int updateOtherArrDtlsData(ArrAqDtlsModel arrAqDtlsModel
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {

            con = dataSource.getConnection();
            /*
             pstmt = con.prepareStatement("DELETE FROM arr_dtls where aqsl_no = ? and p_month = ? and p_year = ?");
             pstmt.setString(4, arrAqDtlsModel.getAqslno());
             pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
             pstmt.setInt(6, arrAqDtlsModel.getPayYear());
             res = pstmt.executeUpdate();
             */

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnPayAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDuePayAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "PAY");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnGpAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueGpAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "GP");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnDaAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueDaAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "DA");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnHraAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueHraAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "HRA");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnOaAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueOaAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "OA");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnIrAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueIrAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "IR");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int updateArrDtlsData(ArrAqDtlsModel arrAqDtlsModel
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {

            con = dataSource.getConnection();
            /*
             pstmt = con.prepareStatement("DELETE FROM arr_dtls where aqsl_no = ? and p_month = ? and p_year = ?");
             pstmt.setString(4, arrAqDtlsModel.getAqslno());
             pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
             pstmt.setInt(6, arrAqDtlsModel.getPayYear());
             res = pstmt.executeUpdate();
             */

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnPayAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDuePayAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "PAY");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnGpAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueGpAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "GP");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnDaAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueDaAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "DA");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int deleteArrDtlsData(ArrAqDtlsModel arrDtlsBean
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int deleted = 0;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("DELETE FROM arr_dtls WHERE aqsl_no = ? and p_month = ? and p_year = ? and calc_unique_no=?");
            pstmt.setString(1, arrDtlsBean.getAqslno());
            pstmt.setInt(2, arrDtlsBean.getPayMonth());
            pstmt.setInt(3, arrDtlsBean.getPayYear());
            pstmt.setInt(4, arrDtlsBean.getCalcuniqueno());
            deleted = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deleted;
    }

    @Override
    public ArrAqDtlsModel getArrearAcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrAqDtlsModel arrAqDtlsBean = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from arr_dtls where aqsl_no = ? and p_month= ? and p_year= ? and calc_unique_no=?");
            pstmt.setString(1, aqSlNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            pstmt.setInt(4, calcuniqueno);
            res = pstmt.executeQuery();
            int i = 0;
            arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);
                arrAqDtlsBean.setPayMonth(pmonth);
                //arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                }
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                arrAqDtlsBean.setAutoincrid(res.getInt("auto_incr_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsBean;
    }

    public ArrAqDtlsModel getOtherArrearAcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrAqDtlsModel arrAqDtlsBean = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from arr_dtls where aqsl_no = ? and p_month= ? and p_year= ? and calc_unique_no=?");
            pstmt.setString(1, aqSlNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            pstmt.setInt(4, calcuniqueno);
            res = pstmt.executeQuery();
            int i = 0;
            arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);
                arrAqDtlsBean.setPayMonth(pmonth);
                //arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("HRA")) {
                    arrAqDtlsBean.setDueHraAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnHraAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("OA")) {
                    arrAqDtlsBean.setDueOaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnOaAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("IR")) {
                    arrAqDtlsBean.setDueIrAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnIrAmt(res.getInt("already_paid"));
                }
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsBean;
    }

    @Override
    public int updateArrAqMastItData(int billNo, String aqSlNo, int incTax) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getArrearAcqEmpDet(String billNo) {
        List list = null;
        List outlist = new ArrayList();
        ArrAqDtlsModel arrAqDtlsBean = null;
        ArrAqList arrempList = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        ResultSet rs1 = null;
        PreparedStatement pst1 = null;

        Connection con = null;
        String aqSlNo = null;
        try {

            con = dataSource.getConnection();
            pst1 = con.prepareStatement("SELECT DISTINCT ( a.aqsl_no),emp_name FROM arr_dtls b,arr_mast a WHERE a.aqsl_no=b.aqsl_no  AND a.bill_no = '" + billNo + "'  ");
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                list = new ArrayList();
                aqSlNo = rs1.getString("aqsl_no");

                pst = con.prepareStatement("SELECT p_month,p_year FROM arr_dtls WHERE  aqsl_no = '" + aqSlNo + "' GROUP BY p_year,p_month ORDER BY p_year,p_month ");
                rs = pst.executeQuery();
                while (rs.next()) {
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    // arrAqDtlsBean.setPayMonth(rs.getInt("p_month"));
                    arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(rs.getInt("p_month")));
                    arrAqDtlsBean.setPayYear(rs.getInt("p_year"));
                    arrAqDtlsBean.setPayList(getArrearAcqEmpPayDetails(aqSlNo, rs.getInt("p_month"), rs.getInt("p_year")));
                    list.add(arrAqDtlsBean);
                }
                arrempList = new ArrAqList();
                arrempList.setEmpName(rs1.getString("emp_name"));
                arrempList.setEmpList(list);

                outlist.add(arrempList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outlist;
    }

    public List<ArrAqDtlsModel> getArrearAcqEmpPayDetails(String aqSlNo, int month, int year) {
        List listPay = null;
        ArrAqDtlsChildModel arrAqDtlsBean = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;

        try {
            listPay = new ArrayList();
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT ad_type,already_paid,to_be_paid FROM arr_dtls WHERE  aqsl_no = '" + aqSlNo + "' AND p_month='" + month + "' AND  p_year='" + year + "'  ORDER BY p_year,p_month ");
            rs = pst.executeQuery();
            while (rs.next()) {
                arrAqDtlsBean = new ArrAqDtlsChildModel();
                arrAqDtlsBean.setAdType(rs.getString("ad_type"));
                arrAqDtlsBean.setAlreadyPaid(rs.getInt("already_paid"));
                arrAqDtlsBean.setToBePaid(rs.getInt("to_be_paid"));
                arrAqDtlsBean.setAqSlNo(aqSlNo);
                listPay.add(arrAqDtlsBean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return listPay;
    }

    @Override
    public void downloadArrearAcqEmpExcel(OutputStream out, String offcode, WritableWorkbook workbook, String billNo) throws Exception {

        Connection con = null;
        List list = null;
        List outlist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;

        ResultSet rs1 = null;
        PreparedStatement pst1 = null;

        ArrAqDtlsModel arrAqDtlsBean = null;
        ArrAqDtlsChildModel arrChildBean = null;
        String aqSlNo = null;

        int row = 0;
        try {
            con = dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(offcode, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableFont textformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setAlignment(Alignment.CENTRE);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat textcell = new WritableCellFormat(textformat);
            textcell.setAlignment(Alignment.CENTRE);
            textcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            textcell.setWrap(true);
            textcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat headcel2 = new WritableCellFormat(headformat);
            headcel2.setAlignment(Alignment.LEFT);
            headcel2.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcel2.setWrap(true);
            headcel2.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setAlignment(Alignment.CENTRE);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            row = row + 1;

            Label label = new Label(1, row, "ARREAR REPORT", headcell3);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, row, 15, row);

            Label childlabel = null;
            con = dataSource.getConnection();
            pst1 = con.prepareStatement("SELECT DISTINCT ( a.aqsl_no),emp_name FROM arr_dtls b,arr_mast a WHERE a.aqsl_no=b.aqsl_no  AND a.bill_no = '" + billNo + "'  ");
            rs1 = pst1.executeQuery();
            int cnt = 0;
            while (rs1.next()) {
                cnt++;
                list = new ArrayList();
                aqSlNo = rs1.getString("aqsl_no");

                pst = con.prepareStatement("SELECT p_month,p_year FROM arr_dtls WHERE aqsl_no = ? GROUP BY p_year,p_month ORDER BY p_year,p_month ");
                pst.setString(1, aqSlNo);
                rs = pst.executeQuery();

                int firstcol = 1;
                int lastcol = 3;
                row = cnt * 4;
                int printCol = 1;
                int empCount = 0;
                while (rs.next()) {
                    empCount = empCount + 1;
                    int headingCol = firstcol;
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    // arrAqDtlsBean.setPayMonth(rs.getInt("p_month"));
                    arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(rs.getInt("p_month")));
                    arrAqDtlsBean.setPayYear(rs.getInt("p_year"));
                    arrAqDtlsBean.setPayList(getArrearAcqEmpPayDetails(aqSlNo, rs.getInt("p_month"), rs.getInt("p_year")));

                    if (cnt == 1) {
                        label = new Label(firstcol, row - 1, arrAqDtlsBean.getPayMonthName() + "-" + arrAqDtlsBean.getPayYear(), headcell3);//column,row
                        sheet.addCell(label);
                        sheet.mergeCells(firstcol, row - 1, lastcol, row - 1);
                        // row = row + 1;

                        childlabel = new Label(headingCol, row, "Type", headcell);//column,row
                        sheet.addCell(childlabel);

                        headingCol += 1;
                        childlabel = new Label(headingCol, row, "Already paid", headcell);//column,row
                        sheet.addCell(childlabel);

                        headingCol += 1;

                        childlabel = new Label(headingCol, row, "To Be paid", headcell);//column,row
                        sheet.addCell(childlabel);

                    }
                    row = row + 1;
                    if (empCount == 1) {
                        label = new Label(firstcol, row, rs1.getString("emp_name"), headcel2);//column,row
                        sheet.addCell(label);
                        sheet.mergeCells(firstcol, row, 60, row);
                    }

                    int childCol = printCol;
                    Number num = null;
                    row += 1;
                    List childArrList = arrAqDtlsBean.getPayList();

                    /*  childlabel = new Label(childCol, row,"Type", headcell);//column,row
                     sheet.addCell(childlabel);

                     childCol += 1;
                     childlabel = new Label(childCol, row,"ALready paid", headcell);//column,row
                     sheet.addCell(childlabel);

                     childCol += 1;

                     childlabel = new Label(childCol, row,"To Be paid", headcell);//column,row
                     sheet.addCell(childlabel);

                     childCol += 1;
                     row += 1;*/
                    if (childArrList != null && childArrList.size() > 0) {
                        for (int i = 0; i < arrAqDtlsBean.getPayList().size(); i++) {

                            arrChildBean = (ArrAqDtlsChildModel) childArrList.get(i);

                            childlabel = new Label(childCol, row, arrChildBean.getAdType(), textcell);//column,row
                            sheet.addCell(childlabel);

                            childCol += 1;

                            num = new Number(childCol, row, arrChildBean.getAlreadyPaid(), textcell);
                            sheet.addCell(num);

                            childCol += 1;

                            num = new Number(childCol, row, arrChildBean.getToBePaid(), textcell);//column,row
                            sheet.addCell(num);

                            childCol = printCol;

                            row += 1;
                        }
                    }
                    firstcol += 3;
                    lastcol += 3;

                    row = (cnt * 4);

                    printCol = printCol + 3;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int deleteArrMastData(String aqSlNo, int billNo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from arr_mast where aqsl_no = ? and bill_no = ?");

            pstmt.setString(1, aqSlNo);
            pstmt.setInt(2, billNo);

            res = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public PayRevisionOption searchEmployee(String searchemp) {
        PayRevisionOption payRevisionOption = new PayRevisionOption();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("SELECT * FROM ARR_MAST WHERE EMP_ID=?");
             pstmt.setString(1, searchemp);
             res = pstmt.executeQuery();
             if (res.next()) {
             payRevisionOption.setMsgcode(1);
             payRevisionOption.setMessage("Employee Already Exist in another bill");
             }*/
            //if (payRevisionOption.getMsgcode() == 0) {
            pstmt = con.prepareStatement("SELECT entered_date,PAYREV_FITTED_AMOUNT,is_approved FROM pay_revision_option WHERE emp_id = ?");
            pstmt.setString(1, searchemp);
            res = pstmt.executeQuery();
            if (res.next()) {
                payRevisionOption.setChoiceDate(res.getDate("entered_date"));
                payRevisionOption.setPayrevisionbasic(res.getInt("PAYREV_FITTED_AMOUNT"));
                payRevisionOption.setIsapproved(res.getString("is_approved"));
            } else {
                payRevisionOption.setMsgcode(2);
                payRevisionOption.setMessage("Pay Fixation of this Employee is not Done in HRMS");
            }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payRevisionOption;
    }

    @Override
    public void giveFullArrear(String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE arr_mast SET arrear_pay = full_arrear_pay WHERE aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void reprocessLeaveArrAqMast(CommonReportParamBean crb, String empCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int arrearFromYear = 0;
        int arrearFromMonth = 0;
        int arrearToMonth = 0;
        int arrearToYear = 0;
        try {
            con = dataSource.getConnection();
            /*Delete Previous data in this bill*/
            pstmt = con.prepareStatement("delete from arr_mast where bill_no=? AND EMP_ID=?");
            pstmt.setInt(1, Integer.parseInt(crb.getBillNo()));
            pstmt.setString(2, empCode);
            pstmt.executeUpdate();
            /*Delete Previous data in this bill*/
            /*Select from and to period of that arrear*/
            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year from bill_mast where bill_no=?");
            pstmt.setInt(1, Integer.parseInt(crb.getBillNo()));
            res = pstmt.executeQuery();
            if (res.next()) {
                arrearFromMonth = res.getInt("from_month");
                arrearFromYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
            }
            String date1 = arrearFromMonth + "-" + arrearFromYear;
            String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
            Calendar beginCalendar = Calendar.getInstance();
            Calendar finishCalendar = Calendar.getInstance();
            DateFormat formater = new SimpleDateFormat("M-yyyy");
            try {
                beginCalendar.setTime(formater.parse(date1));
                finishCalendar.setTime(formater.parse(date2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int slno = 0;
            Employee employee = getEmployeePayProfile(empCode);
            while (beginCalendar.before(finishCalendar)) {
                double dapercentage = 0;
                slno++;
                int month = beginCalendar.get(Calendar.MONTH);
                int year = beginCalendar.get(Calendar.YEAR);
                HashMap<String, Integer> payleaveday = getPayLeaveDays(empCode, month, year);
                ArrAqMastModel arrAqMastModel = new ArrAqMastModel();
                arrAqMastModel.setSlno(slno);
                arrAqMastModel.setAqGroup(crb.getBillgroupid());
                arrAqMastModel.setEmpCode(employee.getEmpid());
                arrAqMastModel.setEmpName(employee.getFullname());
                arrAqMastModel.setGpfAccNo(employee.getGpfno());
                arrAqMastModel.setAccType(employee.getAccttype());
                arrAqMastModel.setCurDesg(employee.getPost());
                arrAqMastModel.setOffCode(employee.getOfficecode());
                arrAqMastModel.setCurBasic(employee.getBasic());
                arrAqMastModel.setGpfType(getGPFSeries(employee.getGpfno()));
                arrAqMastModel.setArrtype(crb.getBillType());
                arrAqMastModel.setChoiceDate(null);
                arrAqMastModel.setBillNo(Integer.parseInt(crb.getBillNo()));
                arrAqMastModel.setPayMonth(month);
                arrAqMastModel.setPayYear(year);
                arrAqMastModel.setEmpType("R");
                arrAqMastModel.setFromMonth(arrearFromMonth + "");
                arrAqMastModel.setFromYear(arrearFromYear + "");
                arrAqMastModel.setToMonth(arrearToMonth + "");
                arrAqMastModel.setToYear(arrearToYear + "");
                arrAqMastModel.setPayday(payleaveday.get("payday"));
                String aqslno = saveArrmastdata2(arrAqMastModel);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (SQLException e) {
            //status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getGPFSeries(String gpfaccno) {
        String gpfseries = "";
        if (gpfaccno != null) {
            gpfseries = gpfaccno.replaceAll("[^A-Z]", "");
        }
        return gpfseries;
    }

    public Employee getEmployeePayProfile(String empCode) {
        Employee employee = new Employee();

        return employee;
    }

    public String saveArrmastdata2(ArrAqMastModel arrAqMastModel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String aqslNo = "";
        try {
            con = dataSource.getConnection();
            aqslNo = arrAqMastModel.getBillNo() + "_" + arrAqMastModel.getEmpCode() + "_" + arrAqMastModel.getPayMonth() + "_" + arrAqMastModel.getPayYear() + "_" + arrAqMastModel.getSlno();
            pstmt = con.prepareStatement("INSERT INTO ARR_MAST (AQSL_NO,EMP_ID,OFF_CODE,BILL_NO,ARR_TYPE,CHOICE_DATE,EMP_NAME,AQ_GROUP,"
                    + "AQ_GROUP_DESC,P_MONTH,P_YEAR,CUR_DESG,CUR_BASIC,EMP_TYPE,from_month,from_year,to_month,to_year,pay_day,gpf_acc_no,"
                    + "acct_type, gpf_type ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            pstmt.setString(1, aqslNo);
            pstmt.setString(2, arrAqMastModel.getEmpCode());
            pstmt.setString(3, arrAqMastModel.getOffCode());
            pstmt.setInt(4, arrAqMastModel.getBillNo());
            pstmt.setString(5, arrAqMastModel.getArrtype());
            if (arrAqMastModel.getChoiceDate() != null) {
                pstmt.setDate(6, new java.sql.Date(arrAqMastModel.getChoiceDate().getTime()));
            } else {
                pstmt.setDate(6, null);
            }
            pstmt.setString(7, arrAqMastModel.getEmpName());
            pstmt.setBigDecimal(8, arrAqMastModel.getAqGroup());
            pstmt.setString(9, arrAqMastModel.getAqGroupDesc());
            pstmt.setInt(10, arrAqMastModel.getPayMonth());
            pstmt.setInt(11, arrAqMastModel.getPayYear());
            pstmt.setString(12, arrAqMastModel.getCurDesg());
            pstmt.setInt(13, arrAqMastModel.getCurBasic());
            pstmt.setString(14, arrAqMastModel.getEmpType());
            pstmt.setInt(15, Integer.parseInt(arrAqMastModel.getFromMonth()));
            pstmt.setInt(16, Integer.parseInt(arrAqMastModel.getFromYear()));
            pstmt.setInt(17, Integer.parseInt(arrAqMastModel.getToMonth()));
            pstmt.setInt(18, Integer.parseInt(arrAqMastModel.getToYear()));
            pstmt.setInt(19, arrAqMastModel.getPayday());
            pstmt.setString(20, arrAqMastModel.getGpfAccNo());
            pstmt.setString(21, arrAqMastModel.getEmpType());
            pstmt.setString(22, getGPFSeries(arrAqMastModel.getGpfAccNo()));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqslNo;
    }

    public HashMap getPayLeaveDays(String empId, int calcMonth, int calYear) {
        HashMap<String, Integer> payleaveday = new HashMap<String, Integer>();
        int v_payday = 0;
        int v_workday = 0;
        payleaveday.put("payday", v_payday);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Calendar myCalendar = new GregorianCalendar(calYear, calcMonth, 1);
        Date monstartDate = myCalendar.getTime();
        //1st date of the month
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int daysInMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date monendDate = myCalendar.getTime();
        long diff = monendDate.getTime() - monstartDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        try {
            con = dataSource.getConnection();
            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            int v_totalAbsent = 0;
            int v_totalSanction = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strmonstartDate = simpleDateFormat.format(monstartDate);
            String strmonendDate = simpleDateFormat.format(monendDate);
            pstmt = con.prepareStatement("SELECT ABS_FROM,ABS_TO FROM EMP_ABSENTEE where EMP_ID = ? and (ABS_FROM BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD') or ABS_TO BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD'))");
            pstmt.setString(1, empId);
            pstmt.setString(2, strmonstartDate);
            pstmt.setString(3, strmonendDate);
            pstmt.setString(4, strmonstartDate);
            pstmt.setString(5, strmonendDate);
            rs = pstmt.executeQuery();
            Date tmonstartDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonstartDate);
            Date tmonendDate = new SimpleDateFormat("yyyy-MM-dd").parse(strmonendDate);
            while (rs.next()) {
                Date absfrom = rs.getDate("ABS_FROM");
                Date absto = rs.getDate("ABS_TO");
                if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) <= 0) {//Absent from and to with in this month
                    diff = absto.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                    v_totalAbsent++;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = absto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalAbsent = v_totalAbsent + (int) days;
                }
            }
            /**
             * CALCULATING TOTAL DAYS ABSENT*
             */
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */

            pstmt = con.prepareStatement("SELECT FDATE,TDATE FROM emp_leave where EMP_ID = ? and (FDATE BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD') or TDATE BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD'))");
            pstmt.setString(1, empId);
            pstmt.setString(2, strmonstartDate);
            pstmt.setString(3, strmonendDate);
            pstmt.setString(4, strmonstartDate);
            pstmt.setString(5, strmonendDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Date absfrom = rs.getDate("FDATE");
                Date absto = rs.getDate("TDATE");
                if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) <= 0) {//Absent from and to with in this month
                    diff = absto.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalSanction = v_totalSanction + (int) days;
                    v_totalSanction++;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) <= 0) {//absent from beftore this month but absent to in this month
                    diff = absto.getTime() - monstartDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    v_totalSanction = v_totalSanction + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) >= 0 && absto.compareTo(tmonendDate) > 0) {//absent from in this month but absent to after this month
                    diff = monendDate.getTime() - absfrom.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalSanction = v_totalSanction + (int) days;
                } else if (absfrom.compareTo(tmonstartDate) < 0 && absto.compareTo(tmonendDate) > 0) {//absent from beftore this month but absent to after this month
                    diff = monstartDate.getTime() - monendDate.getTime();
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
                    v_totalSanction = v_totalSanction + (int) days;
                }
            }
            /**
             * CALCULATING TOTAL DAYS SANCTIONED*
             */
            v_payday = v_totalAbsent - (v_totalAbsent - v_totalSanction);
            payleaveday.put("payday", v_payday);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payleaveday;
    }

    public int getAllowanceAmt(String refaqslno, String adType, String aqDTLS) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int amt = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE=? ");
            pstmt.setString(1, refaqslno);
            pstmt.setString(2, adType);
            res = pstmt.executeQuery();
            if (res.next()) {
                amt = res.getInt("AD_AMT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return amt;
    }

    public void addAllowanceToArrear(String aqslno, String adType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement insertpstmt = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            insertpstmt = con.prepareStatement("INSERT INTO ARR_DTLS (AQSL_NO,P_MONTH,P_YEAR,AD_TYPE,ALREADY_PAID, TO_BE_PAID, DRAWN_VIDE_BILLNO, REF_AQSL_NO,CALC_UNIQUE_NO,BT_ID)VALUES (?,?,?,?,?,?,?,?,?,?) ");
            pstmt = con.prepareStatement("SELECT DISTINCT p_year,p_month,calc_unique_no,drawn_vide_billno,ref_aqsl_no FROM arr_dtls WHERE aqsl_no =?");
            pstmt.setString(1, aqslno);
            res = pstmt.executeQuery();
            while (res.next()) {
                int p_year = res.getInt("p_year");
                int p_month = res.getInt("p_month");
                int calc_unique_no = res.getInt("calc_unique_no");
                String drawn_vide_billno = res.getString("drawn_vide_billno");
                String ref_aqsl_no = res.getString("ref_aqsl_no");

                String aqDTLS = "AQ_DTLS";

                aqDTLS = AqFunctionalities.getAQBillDtlsTable(p_month, p_year);

                insertpstmt.setString(1, aqslno);
                insertpstmt.setInt(2, p_month);
                insertpstmt.setInt(3, p_year);
                insertpstmt.setString(4, adType);
                insertpstmt.setInt(5, getAllowanceAmt(ref_aqsl_no, adType, aqDTLS));
                insertpstmt.setInt(6, 0);
                insertpstmt.setString(7, drawn_vide_billno);
                insertpstmt.setString(8, ref_aqsl_no);
                insertpstmt.setInt(9, calc_unique_no);
                insertpstmt.setString(10, "136");
                insertpstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void reprocessOtherArrAqMast(int billNo, String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;
        try {
            con = dataSource.getConnection();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date payRevDt = sdf.parse("2016-01-01");

            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
            }
            ArrAqMastModel arrAqMastModel = getOtherArrearAcquaintanceData(aqslno);

            Date doj = sdf.parse(getEmployeeDateOfBirth(arrAqMastModel.getEmpCode()));

            pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=? WHERE emp_id=?");
            ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                    + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
            ps2.setString(1, arrAqMastModel.getEmpCode());
            rs2 = ps2.executeQuery();
            if (rs2.next()) {

                pstmt.setString(1, rs2.getString("bank_acc_no"));
                pstmt.setString(2, rs2.getString("bank_code"));
                pstmt.setString(3, rs2.getString("branch_code"));
                pstmt.setString(4, rs2.getString("ifsc_code"));
                pstmt.setString(5, arrAqMastModel.getEmpCode());
                pstmt.execute();
            }
            if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int calc_unique_no = 0;
                PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());
                while (beginCalendar.before(finishCalendar)) {
                    double dapercentage = 0;
                    calc_unique_no++;
                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);
                    String isPayRevised = "N";
                    if (po.getChoiceDate() != null) {
                        isPayRevised = "Y";
                    }
                    if (isPayRevised.equals("N")) {
                        String isCont6RegularAfter7thPayRevision = checkContractual6YearsToRegularAfter7thPayrevision(arrAqMastModel.getEmpCode());
                        if (isCont6RegularAfter7thPayRevision.equals("Y")) {
                            isPayRevised = "Y";
                        }
                    }
                    if (doj.compareTo(payRevDt) > 0) {
                        isPayRevised = "Y";
                    }
                    if (isPayRevised.equals("Y")) {
                        if (month <= 5 && year == 2016) {
                            dapercentage = 0;
                        } else if (month > 5 && month <= 11 && year == 2016) {
                            dapercentage = 2;
                        } else if (month <= 5 && year == 2017) {
                            dapercentage = 4;
                        } else if (month > 5 && month <= 11 && year == 2017) {
                            dapercentage = 5;
                        } else if (month >= 0 && month < 6 && year == 2018) {
                            dapercentage = 7;
                        } else if ((month > 7 && month <= 11 && year == 2018) || ((month < 2 && year == 2019))) {
                            dapercentage = 9;
                        } else if (month >= 2 && month <= 5 && year == 2019) {
                            dapercentage = 12;
//                        } else if ((month >= 6 && month <= 11 && year == 2019) || (month >= 0 && year >= 2020)) {
//                            dapercentage = 17;
//                        } 
                       /*start new code for da as 34%*/
                        } else if ((month >= 6 && month <= 11 && year == 2019) || (month >= 0 && month <= 5 && year == 2021)) {
                            dapercentage = 17;
                        } else if ((month >= 6 && month <= 11 && year == 2021)) {
                            dapercentage = 31;
                        } else if ((month < 6 && year == 2022)) {
                            dapercentage = 34;// da hike 3%
                        } else if ((month <= 11 && year == 2022)) {
                            dapercentage = 38;// da hike 4%
                        } else if (month < 6 && year == 2023) {
                            dapercentage = 42;// da hike 4%
                        } else if (month >= 6 && year == 2023) {
                            dapercentage = 46;// da hike 4%
                        } else if (month < 2 && year == 2024) {
                            dapercentage = 50;// da hike 4%
                        }
                        /*end new code for da as 34%*/
                    } else {
                        if (month >= 3) {
                            dapercentage = 1.42;
                        } else {
                            dapercentage = 1.39;
                        }
                    }
                    System.out.println("dapercentage is: " + dapercentage);
                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForOtherArrear(po, month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno);
                    saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);
                }
                pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
                pstmt.setString(1, aqslno);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
                pstmt.setString(1, aqslno);
                pstmt.executeUpdate();

            } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                int calc_unique_no = 0;
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = arrearToMonth + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                while (beginCalendar.before(finishCalendar)) {
                    int dapercentage = 0;
                    calc_unique_no++;

                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);
                    if (month <= 5 && year == 2016) {
                        dapercentage = 0;
                    } else if (month > 5 && month <= 11 && year == 2016) {
                        dapercentage = 2;
                    } else if (month <= 5 && year == 2017) {
                        dapercentage = 4;
                    } else if (month > 5 && month <= 11 && year == 2017) {
                        dapercentage = 5;
                    }
                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForContractual(arrAqMastModel.getChoiceDate(), month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno, arrAqMastModel.getPayrevisiongp());
                    saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);

                }
            }
        } catch (Exception e) {
            //status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void reprocessArrAqMast(int billNo, String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
            }
            ArrAqMastModel arrAqMastModel = getArrearAcquaintanceData(aqslno);
            if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int calc_unique_no = 0;
                PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());

                pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=? WHERE emp_id=?  and bill_no =?");
                ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                        + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
                ps2.setString(1, arrAqMastModel.getEmpCode());
                rs2 = ps2.executeQuery();
                if (rs2.next()) {

                    pstmt.setString(1, rs2.getString("bank_acc_no"));
                    pstmt.setString(2, rs2.getString("bank_code"));
                    pstmt.setString(3, rs2.getString("branch_code"));
                    pstmt.setString(4, rs2.getString("ifsc_code"));
                    pstmt.setString(5, arrAqMastModel.getEmpCode());
                    pstmt.setInt(6, billNo);
                    pstmt.execute();
                }
                while (beginCalendar.before(finishCalendar)) {
                    double dapercentage = 0;
                    calc_unique_no++;
                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);
                    String isPayRevised = "N";
                    if (po.getChoiceDate() != null) {
                        isPayRevised = "Y";
                    }
                    if (isPayRevised.equals("Y")) {
                        if (month <= 5 && year == 2016) {
                            dapercentage = 0;
                        } else if (month > 5 && month <= 11 && year == 2016) {
                            dapercentage = 2;
                        } else if (month <= 5 && year == 2017) {
                            dapercentage = 4;
                        } else if (month > 5 && month <= 11 && year == 2017) {
                            dapercentage = 5;
                        } else {
                            dapercentage = 7;
                        }
                    } else {
                        if (month >= 3) {
                            dapercentage = 1.42;
                        } else {
                            dapercentage = 1.39;
                        }
                    }

                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceList(po, month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno);
                    saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);

                }
                pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no), acct_type=? WHERE aqsl_no=?");
                pstmt.setString(1, arrAqMastModel.getAccType());
                pstmt.setString(2, aqslno);
                pstmt.executeUpdate();
                pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
                pstmt.setString(1, aqslno);
                pstmt.executeUpdate();

            } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                int calc_unique_no = 0;
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = arrearToMonth + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=? WHERE emp_id=?  and bill_no =?");
                ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                        + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
                ps2.setString(1, arrAqMastModel.getEmpCode());
                rs2 = ps2.executeQuery();
                if (rs2.next()) {

                    pstmt.setString(1, rs2.getString("bank_acc_no"));
                    pstmt.setString(2, rs2.getString("bank_code"));
                    pstmt.setString(3, rs2.getString("branch_code"));
                    pstmt.setString(4, rs2.getString("ifsc_code"));
                    pstmt.setString(5, arrAqMastModel.getEmpCode());
                    pstmt.setInt(6, billNo);
                    pstmt.execute();
                }

                while (beginCalendar.before(finishCalendar)) {
                    int dapercentage = 0;
                    calc_unique_no++;

                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);
                    if (month <= 5 && year == 2016) {
                        dapercentage = 0;
                    } else if (month > 5 && month <= 11 && year == 2016) {
                        dapercentage = 2;
                    } else if (month <= 5 && year == 2017) {
                        dapercentage = 4;
                    } else if (month > 5 && month <= 11 && year == 2017) {
                        dapercentage = 5;
                    }
                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForContractual(arrAqMastModel.getChoiceDate(), month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno, arrAqMastModel.getPayrevisiongp());
                    saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);

                }
            }
        } catch (SQLException e) {
            //status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void reprocess6ArrAqMast(int billNo, String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;
        int arrearPercent = 0;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year,arrear_percent from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
                arrearPercent = res.getInt("arrear_percent");
            }
            ArrAqMastModel arrAqMastModel = get6ArrearAcquaintanceData(aqslno);

            pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=?, acct_type=?, arrear_percent=? WHERE emp_id=? and bill_no =? ");
            ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code, acct_type from emp_mast "
                    + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=? ");
            ps2.setString(1, arrAqMastModel.getEmpCode());

            rs2 = ps2.executeQuery();
            if (rs2.next()) {

                pstmt.setString(1, rs2.getString("bank_acc_no"));
                pstmt.setString(2, rs2.getString("bank_code"));
                pstmt.setString(3, rs2.getString("branch_code"));
                pstmt.setString(4, rs2.getString("ifsc_code"));
                pstmt.setString(5, rs2.getString("acct_type"));
                pstmt.setInt(6, arrearPercent);
                pstmt.setString(7, arrAqMastModel.getEmpCode());
                pstmt.setInt(8, billNo);
                pstmt.execute();
            }
            if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                    pstmt = con.prepareStatement("SELECT * FROM ARR_MAST "
                            + " inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no "
                            + " WHERE EMP_ID=? AND ARR_TYPE='ARREAR'");
                    pstmt.setString(1, arrAqMastModel.getEmpCode());
                    res = pstmt.executeQuery();
                    String aqsl_no = "";
                    int ctr = 0;
                    int j = 1;
                    while (res.next()) {
                        ctr++;
                        aqsl_no = res.getString("AQSL_NO");
                        if (!aqsl_no.equals("")) {
                            if (ctr == 1) {
                                j = 1;
                            } else if (ctr == 2) {
                                j = 21;
                            } else if (ctr == 3) {
                                j = 41;
                            } else {
                                j = 61;
                            }
                            List arrAqDtlsList = get6pArrearAcquaintanceDtls(aqsl_no, j);
                            saveArrdtlsdata(arrAqDtlsList, aqslno);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int calc_unique_no = 0;
                PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());
            } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                int calc_unique_no = 0;
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = arrearToMonth + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                    pstmt = con.prepareStatement("SELECT AQSL_NO FROM ARR_MAST WHERE EMP_ID=? AND ARR_TYPE='ARREAR'");
                    pstmt.setString(1, arrAqMastModel.getEmpCode());
                    res = pstmt.executeQuery();
                    String aqsl_no = "";
                    if (res.next()) {
                        aqsl_no = res.getString("AQSL_NO");
                    }
                    int j = 1;
                    if (!aqsl_no.equals("")) {
                        List arrAqDtlsList = get6pArrearAcquaintanceDtls(aqsl_no, j);
                        saveArrdtlsdata(arrAqDtlsList, aqslno);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                while (beginCalendar.before(finishCalendar)) {
                    int dapercentage = 0;
                    calc_unique_no++;

                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public PayRevisionOption getChoiceDate(String empCode) {
        PayRevisionOption po = new PayRevisionOption();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            con = repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT entered_date,PAYREV_FITTED_AMOUNT FROM pay_revision_option WHERE emp_id = ? AND is_approved = 'Y'");
            pstmt.setString(1, empCode);
            res = pstmt.executeQuery();
            if (res.next()) {
                po.setChoiceDate(res.getDate("entered_date"));
                po.setPayrevisionbasic(res.getInt("PAYREV_FITTED_AMOUNT"));
            }
            pstmt = con.prepareStatement("SELECT incr_date,revised_basic FROM emp_pay_revised_increment_2016 WHERE emp_id=? order by incr_date");
            pstmt.setString(1, empCode);
            res = pstmt.executeQuery();
            List payRevisionIncrements = new ArrayList();
            while (res.next()) {
                PayRevisionIncrement princ = new PayRevisionIncrement();
                princ.setIncrementDate(res.getDate("incr_date"));
                princ.setIncrementedbasic(res.getInt("revised_basic"));
                payRevisionIncrements.add(princ);
            }
            po.setPayRevisionIncrements(payRevisionIncrements);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return po;
    }

    public int getRevisedPay(Date choiceDate, int month, int year, int[] contractualMatrix) {
        Calendar incCal = Calendar.getInstance();
        int revisedPay = 0;
        if (choiceDate != null) {
            incCal.setTime(choiceDate);
            int startYear = incCal.get(Calendar.YEAR);
            int startMonth = incCal.get(Calendar.MONTH);

            int endMonth = startMonth;
            int endYear = startYear + 1;
            if ((month >= startMonth && year == startYear) || (month <= endMonth && year == endYear)) {
                revisedPay = contractualMatrix[0];
            }
        }
        return revisedPay;
    }

    @Override
    public int[] getPayMatrix(int gp) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int[] contractualMatrix = new int[12];
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AMT FROM PAY_MATRIX_CONT_2017 WHERE GP=? ORDER BY YEAR");
            pstmt.setInt(1, gp);
            res = pstmt.executeQuery();
            int i = 0;
            while (res.next()) {
                contractualMatrix[i] = res.getInt("AMT");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return contractualMatrix;
    }

    @Override
    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForContractual(Date choiceDate, int month, int year, String empCode, int dapercentage, String aqslno, int gp) {
        ArrayList<ArrAqDtlsModel> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtAqDtls = null;
        ResultSet res = null;
        ResultSet resAqdtls = null;
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Date payRevisionDate = sdf.parse("01-JAN-2016");
            long diff = payRevisionDate.getTime() - choiceDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            int yearofservice = 0;
            if (days > 365 && days < 730) {
                yearofservice = 0;
            } else {
                yearofservice = 1;
            }
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT MON_BASIC,BILL_DESC,AQSL_NO FROM AQ_MAST INNER JOIN BILL_MAST ON AQ_MAST.BILL_NO = BILL_MAST.BILL_NO WHERE AQ_MAST.AQ_MONTH=? AND AQ_MAST.AQ_YEAR=? AND EMP_CODE=?");
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setString(3, empCode);
            res = pstmt.executeQuery();

            String refaqslno = "";
            String billdesc = "";
            //int revisedPay = getRevisedPay(choiceDate, month, year, contractualMatrix);
            int dbbasic = 0;
            if (res.next()) {
                refaqslno = res.getString("AQSL_NO");
                /*Get Drawn GP from Aq Dtls*/
                String aqDTLS = "AQ_DTLS";

                aqDTLS = AqFunctionalities.getAQBillDtlsTable(month, year);

                pstmtAqDtls = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='GP' ");
                pstmtAqDtls.setString(1, refaqslno);
                resAqdtls = pstmtAqDtls.executeQuery();
                int drawngp = 0;
                int revisedPay = 0;
                if (resAqdtls.next()) {
                    drawngp = resAqdtls.getInt("AD_AMT");
                    int[] contractualMatrix = getPayMatrix(drawngp);
                    revisedPay = contractualMatrix[yearofservice];
                }

                /*Get Drawn GP from Aq Dtls*/
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("MON_BASIC"));
                arrAqDtls.setAdType("PAY");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(res.getString("BILL_DESC"));
                arrAqDtls.setDueAmt(revisedPay);
                dbbasic = arrAqDtls.getDrawnAMt();
                refaqslno = arrAqDtls.getRefaqslno();
                billdesc = arrAqDtls.getDrawnBillNo();
                list.add(arrAqDtls);

                arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(drawngp);
                arrAqDtls.setAdType("GP");
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(gp);
                list.add(arrAqDtls);

                arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("DA");
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);

            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("PAY");
                arrAqDtls.setRefaqslno(null);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDueAmt(0);
                dbbasic = arrAqDtls.getDrawnAMt();
                refaqslno = arrAqDtls.getRefaqslno();
                billdesc = arrAqDtls.getDrawnBillNo();
                list.add(arrAqDtls);

                arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("GP");
                arrAqDtls.setRefaqslno(null);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDueAmt(gp);
                list.add(arrAqDtls);

                arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("DA");
                arrAqDtls.setRefaqslno(null);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(resAqdtls, pstmtAqDtls);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ArrAqDtlsModel arrAqDtlsModels[] = list.toArray(new ArrAqDtlsModel[list.size()]);
        return arrAqDtlsModels;
    }

    @Override
    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceList(PayRevisionOption po, int month, int year, String empCode, double dapercentage, String aqslno) {

        ArrayList<ArrAqDtlsModel> list = new ArrayList<>();

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {

            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("SELECT MON_BASIC,BILL_DESC,AQSL_NO FROM AQ_MAST INNER JOIN BILL_MAST ON AQ_MAST.BILL_NO = BILL_MAST.BILL_NO WHERE AQ_MAST.AQ_MONTH=? AND AQ_MAST.AQ_YEAR=? AND EMP_CODE=?");
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setString(3, empCode);
            res = pstmt.executeQuery();

            String refaqslno = "";
            String billdesc = "";
            int revisedPay = getRevisedPay(po, month, year);
            int dbbasic = 0;
            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("MON_BASIC"));
                arrAqDtls.setAdType("PAY");
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(res.getString("BILL_DESC"));
                arrAqDtls.setDueAmt(revisedPay);
                dbbasic = arrAqDtls.getDrawnAMt();
                refaqslno = arrAqDtls.getRefaqslno();
                billdesc = arrAqDtls.getDrawnBillNo();
                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("PAY");
                refaqslno = "ADJUSTED";
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(revisedPay);
                list.add(arrAqDtls);
            }
            String aqDTLS = "AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(month, year);

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='GP' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            int gp = 0;

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(0);
                gp = arrAqDtls.getDrawnAMt();
                list.add(arrAqDtls);
            } else {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("GP");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='DA'");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            if (res.next()) {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                int correctDA = getPrevDA(arrAqDtls.getDrawnAMt(), dbbasic + gp, month, year);
                arrAqDtls.setDrawnAMt(correctDA);
                arrAqDtls.setDueAmt(new Long(Math.round((revisedPay / 100) * dapercentage)).intValue());

                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("DA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ArrAqDtlsModel arrAqDtlsModels[] = list.toArray(new ArrAqDtlsModel[list.size()]);
        return arrAqDtlsModels;
    }

    @Override
    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForJudiciaryArrear(PayRevisionOption po, int month, int year, String empCode, double dapercentage, String aqslno) {

        ArrayList<ArrAqDtlsModel> list = new ArrayList<>();

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {

            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("SELECT MON_BASIC,BILL_DESC,AQSL_NO FROM AQ_MAST INNER JOIN BILL_MAST ON AQ_MAST.BILL_NO = BILL_MAST.BILL_NO WHERE AQ_MAST.AQ_MONTH=? AND AQ_MAST.AQ_YEAR=? AND EMP_CODE=?");
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setString(3, empCode);
            res = pstmt.executeQuery();

            String refaqslno = "";
            String billdesc = "";
            int revisedPay = getRevisedPay(po, month, year);
            int dbbasic = 0;
            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("MON_BASIC"));
                arrAqDtls.setAdType("PAY");
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(res.getString("BILL_DESC"));
                arrAqDtls.setDueAmt(revisedPay);
                dbbasic = arrAqDtls.getDrawnAMt();
                refaqslno = arrAqDtls.getRefaqslno();
                billdesc = arrAqDtls.getDrawnBillNo();
                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("PAY");
                refaqslno = "ADJUSTED";
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(revisedPay);
                list.add(arrAqDtls);
            }
            String aqDTLS = "AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(month, year);

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='GP' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            int gp = 0;

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(0);
                gp = arrAqDtls.getDrawnAMt();
                list.add(arrAqDtls);
            } else {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("GP");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='DA'");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            if (res.next()) {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                int correctDA = getPrevDA(arrAqDtls.getDrawnAMt(), dbbasic + gp, month, year);
                arrAqDtls.setDrawnAMt(correctDA);
                arrAqDtls.setDueAmt(new Long(Math.round((revisedPay / 100) * dapercentage)).intValue());

                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("DA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='IR'");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            if (res.next()) {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);

                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("IR");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                list.add(arrAqDtls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ArrAqDtlsModel arrAqDtlsModels[] = list.toArray(new ArrAqDtlsModel[list.size()]);
        return arrAqDtlsModels;
    }

    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForOtherArrear(PayRevisionOption po, int month, int year, String empCode, double dapercentage, String aqslno) {
        ArrayList<ArrAqDtlsModel> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            int revisedPay = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT CUR_BASIC_SALARY FROM EMP_MAST WHERE EMP_ID=?");
            pstmt.setString(1, empCode);
            res = pstmt.executeQuery();
            if (res.next()) {
                revisedPay = res.getInt("CUR_BASIC_SALARY");
            }
            pstmt = con.prepareStatement("SELECT MON_BASIC,BILL_DESC,AQSL_NO FROM AQ_MAST INNER JOIN BILL_MAST ON AQ_MAST.BILL_NO = BILL_MAST.BILL_NO WHERE AQ_MAST.AQ_MONTH=? AND AQ_MAST.AQ_YEAR=? AND EMP_CODE=?");
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setString(3, empCode);
            res = pstmt.executeQuery();

            String refaqslno = "";
            String billdesc = "";
            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("MON_BASIC"));
                revisedPay = res.getInt("MON_BASIC");
                arrAqDtls.setAdType("PAY");
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(res.getString("BILL_DESC"));
                arrAqDtls.setDueAmt(revisedPay);
                arrAqDtls.setBtid("136");
                refaqslno = arrAqDtls.getRefaqslno();
                billdesc = arrAqDtls.getDrawnBillNo();
                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("PAY");
                refaqslno = "ADJUSTED";
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(revisedPay);
                arrAqDtls.setBtid("136");
                list.add(arrAqDtls);
            }
            String aqDTLS = "AQ_DTLS";

            aqDTLS = AqFunctionalities.getAQBillDtlsTable(month, year);

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='GP' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            int gp = 0;

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(0);
                arrAqDtls.setBtid("136");
                gp = arrAqDtls.getDrawnAMt();
                list.add(arrAqDtls);
            } else {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("GP");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                arrAqDtls.setBtid("136");
                list.add(arrAqDtls);
            }
            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='DA'");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();
            if (res.next()) {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                //arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                if ((month >= 6 && month <= 11) && (year == 2021 || year >= 2022)) {
                    int dapercentage46 = 46;
                    arrAqDtls.setDrawnAMt(new Long(Math.round((revisedPay / 100) * dapercentage46)).intValue());
                } else {
                    arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                }
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(res.getString("AQSL_NO"));
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(new Long(Math.round((revisedPay / 100) * dapercentage)).intValue());
                arrAqDtls.setBtid("156");
                list.add(arrAqDtls);
            } else {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("DA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                arrAqDtls.setBtid("156");
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT AQSL_NO,AD_CODE,AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND AD_CODE='HRA' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType(res.getString("AD_CODE"));
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(res.getInt("AD_AMT"));
                arrAqDtls.setBtid("403");
                list.add(arrAqDtls);
            } else {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("HRA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                arrAqDtls.setBtid("403");
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AS AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND SCHEDULE='OA' and ad_code<>'IR' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType("OA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(res.getInt("AD_AMT"));
                arrAqDtls.setBtid("523");
                list.add(arrAqDtls);
            } else {

                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setAdType("OA");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(null);
                arrAqDtls.setDrawnAMt(0);
                arrAqDtls.setDueAmt(0);
                arrAqDtls.setBtid("523");
                list.add(arrAqDtls);
            }

            pstmt = con.prepareStatement("SELECT SUM(AD_AMT) AS AD_AMT FROM " + aqDTLS + " WHERE AQSL_NO=? AND SCHEDULE='OA' and ad_code='IR' ");
            pstmt.setString(1, refaqslno);
            res = pstmt.executeQuery();

            if (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setDrawnAMt(res.getInt("AD_AMT"));
                arrAqDtls.setAdType("IR");
                arrAqDtls.setRefaqslno(refaqslno);
                arrAqDtls.setDrawnBillNo(billdesc);
                arrAqDtls.setDueAmt(res.getInt("AD_AMT"));
                arrAqDtls.setBtid("136");
                list.add(arrAqDtls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ArrAqDtlsModel arrAqDtlsModels[] = list.toArray(new ArrAqDtlsModel[list.size()]);
        return arrAqDtlsModels;
    }

    public int getPrevDA(int currentda, int dbbasic, int month, int year) {
        int correctDa = 0;
        if (month >= 0 && month <= 5 && year == 2016) {
            int tcorrectDa = new Double(Math.round(dbbasic * 1.25)).intValue();
            if (tcorrectDa > currentda) {
                correctDa = tcorrectDa;
            } else {
                correctDa = currentda;
            }
        } else if ((month > 5 && month <= 11 && year == 2016) || (month >= 0 && month <= 7 && year == 2017)) {
            int tcorrectDa = new Double(Math.round(dbbasic * 1.32)).intValue();
            if (tcorrectDa > currentda) {
                correctDa = tcorrectDa;
            } else {
                correctDa = currentda;
            }
        }
        return correctDa;
    }

    public int getRevisedPay(PayRevisionOption po, int month, int year) {
        int revisedPay = po.getPayrevisionbasic();
        List payRevisionIncrements = po.getPayRevisionIncrements();
        for (int i = 0; i < payRevisionIncrements.size(); i++) {
            PayRevisionIncrement princ = (PayRevisionIncrement) payRevisionIncrements.get(i);
            Date incrementDate = princ.getIncrementDate();
            Calendar incCal = Calendar.getInstance();
            if (incrementDate != null) {
                incCal.setTime(incrementDate);
                int incYear = incCal.get(Calendar.YEAR);
                int incMonth = incCal.get(Calendar.MONTH);
                if ((month >= incMonth && year >= incYear) || (month < incMonth && year > incYear)) {
                    revisedPay = princ.getIncrementedbasic();
                }
            }
            /*if(month >= incMonth && year >= incYear){
             revisedPay = princ.getIncrementedbasic();
             }*/

        }
        return revisedPay;
    }

    @Override
    public void insertIntoPayRevisionOption(String inputChoiceDate, int payrevisionbasic, String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            Date choiceDate = (new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(inputChoiceDate));
            pstmt = con.prepareStatement("INSERT INTO pay_revision_option (entered_date,PAYREV_FITTED_AMOUNT,is_approved,emp_id) VALUES (?,?,?,?) ");
            pstmt.setDate(1, new java.sql.Date(choiceDate.getTime()));
            pstmt.setInt(2, payrevisionbasic);
            pstmt.setString(3, "Y");
            pstmt.setString(4, empid);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadFullArrDataReportExcel(Workbook workbook, String billNo, String typeOfBill) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList outerlist = new ArrayList();
        ArrayList aqslnoList = new ArrayList();

        int headerrow = 0;
        int subheaderrow = 0;
        int dataheaderrow = 0;
        int datarow = 0;
        try {
            con = this.repodataSource.getConnection();

            Sheet sheet = workbook.createSheet("Arrear Data");

            Font headerFont = workbook.createFont();
            //headerFont.setBoldweight(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            String sql = "select aqsl_no from arr_mast where bill_no=? order by emp_name asc";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                aqslnoList.add(rs.getString("aqsl_no"));
            }

            if (aqslnoList != null && aqslnoList.size() > 0) {
                for (int i = 0; i < aqslnoList.size(); i++) {
                    String aqslno = (String) aqslnoList.get(i);
                    if (typeOfBill.equals("ARREAR")) {
                        ArrAqMastModel arrAqMastBean = getArrearAcquaintanceData(aqslno);
                        outerlist.add(arrAqMastBean);
                    } else {
                        ArrAqMastModel arrAqMastBean = getOtherArrearAcquaintanceData(aqslno);
                        outerlist.add(arrAqMastBean);
                    }
                }
            }

            if (outerlist != null && outerlist.size() > 0) {
                ArrAqMastModel arrAqMastBean = null;
                for (int i = 0; i < outerlist.size(); i++) {
                    arrAqMastBean = (ArrAqMastModel) outerlist.get(i);
                    Row empHeaderRow = sheet.createRow(headerrow);

                    sheet.addMergedRegion(new CellRangeAddress(headerrow, headerrow, 0, 5));
                    sheet.addMergedRegion(new CellRangeAddress(headerrow, headerrow, 6, 10));

                    Cell cell = empHeaderRow.createCell(0);
                    cell.setCellValue("Name: " + arrAqMastBean.getEmpName() + "(" + arrAqMastBean.getEmpCode() + ")");
                    cell.setCellStyle(headerCellStyle);

                    cell = empHeaderRow.createCell(6);
                    cell.setCellValue("Designation: " + StringUtils.defaultString(arrAqMastBean.getCurDesg()));
                    cell.setCellStyle(headerCellStyle);

                    subheaderrow = headerrow + 1;

                    Row subHeaderRow = sheet.createRow(subheaderrow);

                    cell = subHeaderRow.createCell(0);
                    cell.setCellValue("Sl No");
                    cell.setCellStyle(headerCellStyle);

                    cell = subHeaderRow.createCell(1);
                    cell.setCellValue("Month");
                    cell.setCellStyle(headerCellStyle);

                    sheet.addMergedRegion(new CellRangeAddress(subheaderrow, subheaderrow, 2, 7));

                    cell = subHeaderRow.createCell(2);
                    cell.setCellValue("Due");
                    cell.setCellStyle(headerCellStyle);

                    sheet.addMergedRegion(new CellRangeAddress(subheaderrow, subheaderrow, 8, 13));

                    cell = subHeaderRow.createCell(8);
                    cell.setCellValue("Drawn");
                    cell.setCellStyle(headerCellStyle);

                    cell = subHeaderRow.createCell(14);
                    cell.setCellValue("Drawn Vide Bill No.");
                    cell.setCellStyle(headerCellStyle);

                    cell = subHeaderRow.createCell(15);
                    cell.setCellValue("Arrear 100%");
                    cell.setCellStyle(headerCellStyle);

                    dataheaderrow = subheaderrow + 1;

                    Row dataHeaderRow = sheet.createRow(dataheaderrow);

                    cell = dataHeaderRow.createCell(2);
                    cell.setCellValue("Pay");

                    cell = dataHeaderRow.createCell(3);
                    cell.setCellValue("G.P");

                    cell = dataHeaderRow.createCell(4);
                    cell.setCellValue("D.A");

                    cell = dataHeaderRow.createCell(5);
                    cell.setCellValue("HRA");

                    cell = dataHeaderRow.createCell(6);
                    cell.setCellValue("OA");

                    cell = dataHeaderRow.createCell(7);
                    cell.setCellValue("Total");

                    cell = dataHeaderRow.createCell(8);
                    cell.setCellValue("Pay");

                    cell = dataHeaderRow.createCell(9);
                    cell.setCellValue("G.P");

                    cell = dataHeaderRow.createCell(10);
                    cell.setCellValue("D.A");

                    cell = dataHeaderRow.createCell(11);
                    cell.setCellValue("HRA");

                    cell = dataHeaderRow.createCell(12);
                    cell.setCellValue("OA");

                    cell = dataHeaderRow.createCell(13);
                    cell.setCellValue("Total");

                    datarow = dataheaderrow + 1;

                    if (arrAqMastBean.getArrDetails() != null && arrAqMastBean.getArrDetails().size() > 0) {
                        ArrAqDtlsModel arrAqDtlsBean = null;
                        //datarow = 3;
                        for (int j = 0; j < arrAqMastBean.getArrDetails().size(); j++) {
                            arrAqDtlsBean = (ArrAqDtlsModel) arrAqMastBean.getArrDetails().get(j);

                            Row dataRow = sheet.createRow(datarow);

                            dataRow.createCell(0).setCellValue(j + 1);

                            dataRow.createCell(1).setCellValue(arrAqDtlsBean.getPayMonthName() + "-" + arrAqDtlsBean.getPayYear());

                            dataRow.createCell(2).setCellValue(arrAqDtlsBean.getDuePayAmt());

                            dataRow.createCell(3).setCellValue(arrAqDtlsBean.getDueGpAmt());

                            dataRow.createCell(4).setCellValue(arrAqDtlsBean.getDueDaAmt());

                            dataRow.createCell(5).setCellValue(arrAqDtlsBean.getDueHraAmt());

                            dataRow.createCell(6).setCellValue(arrAqDtlsBean.getDueOaAmt());

                            dataRow.createCell(7).setCellValue(arrAqDtlsBean.getDueTotalAmt());

                            dataRow.createCell(8).setCellValue(arrAqDtlsBean.getDrawnPayAmt());

                            dataRow.createCell(9).setCellValue(arrAqDtlsBean.getDrawnGpAmt());

                            dataRow.createCell(10).setCellValue(arrAqDtlsBean.getDrawnDaAmt());

                            dataRow.createCell(11).setCellValue(arrAqDtlsBean.getDrawnHraAmt());

                            dataRow.createCell(12).setCellValue(arrAqDtlsBean.getDrawnOaAmt());

                            dataRow.createCell(13).setCellValue(arrAqDtlsBean.getDrawnTotalAmt());

                            dataRow.createCell(14).setCellValue(arrAqDtlsBean.getDrawnBillNo());

                            dataRow.createCell(15).setCellValue(arrAqDtlsBean.getArrear100());

                            datarow += 1;

                            if (j == arrAqMastBean.getArrDetails().size() - 1) {
                                Row grandTotalDataRow = sheet.createRow(datarow);
                                grandTotalDataRow.createCell(14).setCellValue("Total");

                                grandTotalDataRow.createCell(15).setCellValue(arrAqMastBean.getGrandTotArr100());
                            }
                        }
                    }
                    headerrow = datarow + 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getOaClaimEmployeeList(String billGroupId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList outList = new ArrayList();
        StringTokenizer stringTokenizer;

        try {
            con = dataSource.getConnection();

            String empQry = "SELECT EM.EMP_ID empId,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EM.GP,POST,claim_amt,OBJECT_HEAD_NAME,"
                    + "OBJECT_HEAD,FROM_PERIOD,TO_PERIOD FROM bill_section_mapping BM "
                    + "INNER JOIN section_post_mapping SPM ON SPM.section_id = BM.section_id "
                    + "INNER JOIN emp_mast EM ON EM.cur_spc = SPM.spc "
                    + "LEFT OUTER JOIN OA_CLAIM_DTLS oa on em.emp_id= oa.hrms_id "
                    + "LEFT OUTER JOIN G_CADRE ON EM.CUR_CADRE_CODE = G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_SPC ON EM.CUR_SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE WHERE BM.BILL_GROUP_ID = ? order by SPM.POST_SL_NO";
            ps = con.prepareStatement(empQry);
            ps.setBigDecimal(1, new BigDecimal(billGroupId));
            rs = ps.executeQuery();
            while (rs.next()) {

                OAClaimModel claimModel = new OAClaimModel();

                claimModel.setBillGroupName(CommonFunctions.encodedTxt(billGroupId));
                claimModel.setHrmsId(rs.getString("empId"));
                String mName = "";
                if (rs.getString("M_NAME") != null && !rs.getString("M_NAME").equals("")) {
                    mName = rs.getString("M_NAME");
                }
                claimModel.setEmpName(rs.getString("F_NAME") + " " + mName + " " + rs.getString("L_NAME"));
                claimModel.setPost(rs.getString("POST"));
                claimModel.setBasic(rs.getInt("CUR_BASIC_SALARY") + "");
                claimModel.setGp(rs.getInt("GP") + "");
                claimModel.setTxtOaAmount(rs.getString("claim_amt"));

                String fromMonth = "";
                String fromYear = "";
                String fromMonthYear = rs.getString("FROM_PERIOD");
                if (fromMonthYear != null && !fromMonthYear.equals("")) {
                    stringTokenizer = new StringTokenizer(fromMonthYear, "-");
                    while (stringTokenizer.hasMoreTokens()) {
                        fromMonth = CommonFunctions.getMonthAsString(Integer.parseInt(stringTokenizer.nextToken().trim()));
                        fromYear = stringTokenizer.nextToken().trim();
                    }
                    claimModel.setFromMonth(fromMonth + "-" + fromYear);
                } else {
                    claimModel.setFromMonth("");
                }

                String toMonth = "";
                String toYear = "";

                String toMonthYear = rs.getString("TO_PERIOD");
                if (fromMonthYear != null && !fromMonthYear.equals("")) {
                    stringTokenizer = new StringTokenizer(toMonthYear, "-");
                    while (stringTokenizer.hasMoreTokens()) {
                        toMonth = CommonFunctions.getMonthAsString(Integer.parseInt(stringTokenizer.nextToken().trim()));
                        toYear = stringTokenizer.nextToken().trim();
                    }
                    claimModel.setToMonth(toMonth + "-" + toYear);
                } else {
                    claimModel.setToMonth("");
                }

                outList.add(claimModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return outList;
    }

    public List getObjectHeadList() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList objHeadList = new ArrayList();
        SelectOption so;

        try {
            con = dataSource.getConnection();

            String objHeadQry = "select ad_code, ad_desc,schedule,ad_code_name from g_ad_list where schedule = ? order by ad_desc";
            ps = con.prepareStatement(objHeadQry);
            ps.setString(1, "OA");
            rs = ps.executeQuery();
            while (rs.next()) {

                so = new SelectOption();

                so.setLabel(rs.getString("ad_desc") + " - " + rs.getString("ad_code"));
                so.setValue(rs.getString("ad_code_name") + "-" + rs.getString("ad_code"));

                objHeadList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return objHeadList;
    }

    public OAClaimModel getOaClaimEmpData(String empId, String bgId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fromMonth = "";
        String fromYear = "";
        String toMonth = "";
        String toYear = "";
        StringTokenizer stringTokenizer;
        OAClaimModel claimModel = new OAClaimModel();

        try {
            con = dataSource.getConnection();

            claimModel.setBillGroupName(bgId);
            String empOaDataQry = "select FROM_PERIOD, TO_PERIOD, object_head_name, object_head, claim_amt from OA_CLAIM_DTLS where hrms_id = ?";
            ps = con.prepareStatement(empOaDataQry);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {

                claimModel.setHidHrmsId(empId);
                String fromMonthYear = rs.getString("FROM_PERIOD");
                if (fromMonthYear != null && !fromMonthYear.equals("")) {
                    stringTokenizer = new StringTokenizer(fromMonthYear, "-");
                    while (stringTokenizer.hasMoreTokens()) {
                        fromMonth = stringTokenizer.nextToken().trim();
                        fromYear = stringTokenizer.nextToken().trim();
                    }
                    claimModel.setFromMonth(fromMonth);
                    claimModel.setFromYear(fromYear);
                } else {
                    claimModel.setFromMonth("");
                    claimModel.setFromYear("");
                }

                String toMonthYear = rs.getString("TO_PERIOD");
                if (fromMonthYear != null && !fromMonthYear.equals("")) {
                    stringTokenizer = new StringTokenizer(toMonthYear, "-");
                    while (stringTokenizer.hasMoreTokens()) {
                        toMonth = stringTokenizer.nextToken().trim();
                        toYear = stringTokenizer.nextToken().trim();
                    }
                    claimModel.setToMonth(toMonth);
                    claimModel.setToYear(toYear);
                } else {
                    claimModel.setToMonth("");
                    claimModel.setToYear("");
                }

                claimModel.setObjectHead(rs.getString("object_head_name") + "-" + rs.getString("object_head"));
                claimModel.setTxtOaAmount(rs.getString("claim_amt"));
                claimModel.setHidType("U");

            } else {
                claimModel.setHidType("I");
                claimModel.setHidHrmsId(empId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return claimModel;
    }

    @Override
    public String insertOaClaimData(OAClaimModel claimModel, String offCode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int insSts = 0;
        String outputRes = "";
        String oaAmt = "";
        String period = "";

        try {
            con = dataSource.getConnection();
            String objHeadDtls = claimModel.getObjectHead();
            String objHeadName = "";
            String objHead = "";
            StringTokenizer stringTokenizer = new StringTokenizer(objHeadDtls, "-");
            while (stringTokenizer.hasMoreTokens()) {
                objHeadName = stringTokenizer.nextToken().trim();
                objHead = stringTokenizer.nextToken().trim();
            }
            String oaClaimQry = "INSERT INTO OA_CLAIM_DTLS (HRMS_ID,claim_amt,OBJECT_HEAD_NAME,OBJECT_HEAD,FROM_PERIOD,TO_PERIOD,OFF_CODE,IS_PAID) "
                    + "VALUES (?,?,?,?,?,?,?,?) ";
            pstmt = con.prepareStatement(oaClaimQry);

            pstmt.setString(1, claimModel.getHidHrmsId());
            oaAmt = claimModel.getTxtOaAmount();
            pstmt.setInt(2, Integer.parseInt(oaAmt));
            pstmt.setString(3, objHeadName);
            pstmt.setString(4, objHead);
            period = CommonFunctions.getMonthAsString(Integer.parseInt(claimModel.getFromMonth())) + "-" + claimModel.getFromYear() + " to " + CommonFunctions.getMonthAsString(Integer.parseInt(claimModel.getToMonth())) + "-" + claimModel.getToYear();
            pstmt.setString(5, claimModel.getFromMonth() + "-" + claimModel.getFromYear());
            pstmt.setString(6, claimModel.getToMonth() + "-" + claimModel.getToYear());
            pstmt.setString(7, offCode);
            pstmt.setString(8, "Y");

            insSts = pstmt.executeUpdate();
            if (insSts == 1) {
                outputRes = period + "@" + oaAmt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputRes;
    }

    @Override
    public String updateOaClaimData(OAClaimModel claimModel, String offCode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int updSts = 0;
        String outputRes = "";
        String oaAmt = "";
        String period = "";

        try {
            con = dataSource.getConnection();
            String objHeadDtls = claimModel.getObjectHead();
            String objHeadName = "";
            String objHead = "";
            StringTokenizer stringTokenizer = new StringTokenizer(objHeadDtls, "-");
            while (stringTokenizer.hasMoreTokens()) {
                objHeadName = stringTokenizer.nextToken().trim();
                objHead = stringTokenizer.nextToken().trim();
            }

            String updQry = "update OA_CLAIM_DTLS set claim_amt=?,OBJECT_HEAD_NAME=?,OBJECT_HEAD=?,FROM_PERIOD=?,to_period=? "
                    + "where hrms_id =? and OFF_CODE=?";
            pstmt = con.prepareStatement(updQry);

            oaAmt = claimModel.getTxtOaAmount();
            pstmt.setInt(1, Integer.parseInt(oaAmt));
            pstmt.setString(2, objHeadName);
            pstmt.setString(3, objHead);
            period = CommonFunctions.getMonthAsString(Integer.parseInt(claimModel.getFromMonth())) + "-" + claimModel.getFromYear() + " to " + CommonFunctions.getMonthAsString(Integer.parseInt(claimModel.getToMonth())) + "-" + claimModel.getToYear();
            pstmt.setString(4, claimModel.getFromMonth() + "-" + claimModel.getFromYear());
            pstmt.setString(5, claimModel.getToMonth() + "-" + claimModel.getToYear());
            pstmt.setString(6, claimModel.getHidHrmsId());
            pstmt.setString(7, offCode);

            updSts = pstmt.executeUpdate();
            if (updSts == 1) {
                outputRes = period + "@" + oaAmt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outputRes;
    }

    @Override
    public String getAcctType(int billno, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String accttype = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select acct_type from arr_mast where bill_no=? and emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billno);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("acct_type") != null && !rs.getString("acct_type").equals("") && rs.getString("acct_type").equals("GPF")) {
                    accttype = "GPF";
                } else {
                    accttype = "CPF";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return accttype;
    }

    @Override
    public List getArrear40VerificationData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List billList = new ArrayList();

        ArrAqMastModel arrmodel = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select bill_mast.ddo_code,g_bill_status.bill_status,bill_status_id,bill_mast.bill_no,bill_desc,bill_mast.aq_year,bill_mast.aq_month,bill_mast.from_year,\n"
                    + "bill_mast.from_month,bill_mast.to_year,bill_mast.to_month,arr_mast.aqsl_no,bill_mast.vch_no,bill_mast.vch_date from arr_mast\n"
                    + "inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no\n"
                    + "left outer join g_bill_status on bill_mast.bill_status_id=g_bill_status.id\n"
                    + "where emp_id=? and arr_type='ARREAR' order by bill_mast.bill_no desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                arrmodel = new ArrAqMastModel();
                arrmodel.setBillNo(rs.getInt("bill_no"));
                arrmodel.setBillDesc(rs.getString("bill_desc"));
                arrmodel.setFromMonth(rs.getString("from_month"));
                arrmodel.setFromYear(rs.getString("from_year"));
                arrmodel.setToMonth(rs.getString("to_month"));
                arrmodel.setToYear(rs.getString("to_year"));
                arrmodel.setAqSlNo(rs.getString("aqsl_no"));
                arrmodel.setPayMonth(rs.getInt("aq_month") + 1);
                arrmodel.setPayYear(rs.getInt("aq_year"));
                arrmodel.setBillstatusid(rs.getInt("bill_status_id"));
                arrmodel.setBillstatus(rs.getString("bill_status"));
                arrmodel.setOffDdo(rs.getString("ddo_code"));
                arrmodel.setVchno(rs.getString("vch_no"));
                billList.add(arrmodel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    @Override
    public void lockArrear40Bill(String checkedBill) {

        Connection con = null;

        PreparedStatement updatepst = null;

        try {
            con = this.dataSource.getConnection();

            String updatesql = "update bill_mast set bill_status_id=2 where bill_no=?";
            updatepst = con.prepareStatement(updatesql);

            if (checkedBill != null && checkedBill.length() > 0) {
                String[] checkedBillArr = checkedBill.split(",");
                for (int i = 0; i < checkedBillArr.length; i++) {
                    int billNo = Integer.parseInt(checkedBillArr[i]);
                    updatepst.setInt(1, billNo);
                    updatepst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(updatepst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteArrear40Bill(String billno, String empid) {

        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement deletepst = null;

        int billStatusId = 0;
        String vchNo = null;
        try {
            con = this.dataSource.getConnection();

            String selectsql = "select bill_status_id,* from bill_mast where bill_no=?";
            selectpst = con.prepareStatement(selectsql);
            selectpst.setInt(1, Integer.parseInt(billno));
            selectrs = selectpst.executeQuery();
            if (selectrs.next()) {
                billStatusId = selectrs.getInt("bill_status_id");
                vchNo = selectrs.getString("vch_no");
            }

            if (billStatusId < 2) {
                if (vchNo == null || vchNo.equals("")) {
                    String sql = "delete from arr_mast where bill_no=? and emp_id=?";
                    deletepst = con.prepareStatement(sql);
                    deletepst.setInt(1, Integer.parseInt(billno));
                    deletepst.setString(2, empid);
                    if (empid != null && !empid.equals("")) {
                        deletepst.executeUpdate();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(deletepst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void deleteArrear25Bill(String billno, String empid) {

        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement deletepst = null;

        int billStatusId = 0;
        String vchNo = null;
        try {
            con = this.dataSource.getConnection();

            String selectsql = "select bill_status_id,* from bill_mast where bill_no=?";
            selectpst = con.prepareStatement(selectsql);
            selectpst.setInt(1, Integer.parseInt(billno));
            selectrs = selectpst.executeQuery();
            if (selectrs.next()) {
                billStatusId = selectrs.getInt("bill_status_id");
                vchNo = selectrs.getString("vch_no");
            }

            if (billStatusId < 2) {
                if (vchNo == null || vchNo.equals("")) {
                    String sql = "delete from arr_mast where bill_no=? and emp_id=?";
                    deletepst = con.prepareStatement(sql);
                    deletepst.setInt(1, Integer.parseInt(billno));
                    deletepst.setString(2, empid);
                    if (empid != null && !empid.equals("")) {
                        deletepst.executeUpdate();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(deletepst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrAqMastModel getNPSArrearAquitanceData(String aqSlNo) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, arrear_pay, full_arrear_pay, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqMastBean.setArrearpay(res.getInt("arrear_pay"));
                arrAqMastBean.setFullArrearpay(res.getInt("full_arrear_pay"));
            }
            int j = 1;
            List arrAqDtlsList = getNPSArrearAquitanceDtls(aqSlNo, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(grandTotArr100);
            arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * 0.4));

            /*double percent = .1;
             if (arrAqMastBean.getPercentageArraer() == 10) {
             percent = 0.1;
             } else if (arrAqMastBean.getPercentageArraer() == 50) {
             percent = 0.5;
             } else if (arrAqMastBean.getPercentageArraer() == 60) {
             percent = 0.6;
             }

             arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));*/
            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    /*private List<ArrAqDtlsModel> getNPSArrearAquitanceDtls(String aqSlNo, int j) {

     Connection con = null;

     PreparedStatement pstmt = null;
     ResultSet res = null;

     List<ArrAqDtlsModel> arrAqDtlsList = new ArrayList<>();

     try {
     con = this.repodataSource.getConnection();

     pstmt = con.prepareStatement("SELECT p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no FROM arr_dtls WHERE aqsl_no =? order by p_year,p_month,calc_unique_no,ad_type");
     pstmt.setString(1, aqSlNo);
     res = pstmt.executeQuery();
     int i = 0;
     ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
     while (res.next()) {
     i++;

     int pmonth = res.getInt("p_month");
     int pyear = res.getInt("p_year");

     arrAqDtlsBean.setAqslno(aqSlNo);

     arrAqDtlsBean.setPayMonth(pmonth);
     arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
     arrAqDtlsBean.setPayYear(pyear);
     arrAqDtlsBean.setAdType(res.getString("ad_type"));

     if (res.getString("ad_type").equals("PAY")) {
     arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
     arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

     } else if (res.getString("ad_type").equals("GP")) {
     arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
     arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

     } else if (res.getString("ad_type").equals("DA")) {
     arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
     arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

     } else if (res.getString("ad_type").equals("CPF")) {
     arrAqDtlsBean.setDueCPFAmt(res.getInt("to_be_paid"));
     arrAqDtlsBean.setDrawnCPFAmt(res.getInt("already_paid"));

     }

     arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));

     int totDueAmt = arrAqDtlsBean.getDueCPFAmt();
     arrAqDtlsBean.setDueTotalAmt(totDueAmt);

     int totDrawnAmt = arrAqDtlsBean.getDrawnCPFAmt();
     arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

     int arrear100 = totDueAmt - totDrawnAmt;
     arrAqDtlsBean.setArrear100(arrear100);

     if (i == 3) {
     arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));

     arrAqDtlsList.add(arrAqDtlsBean);
     arrAqDtlsBean = new ArrAqDtlsModel();
     i = 0;
     j++;
     }
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(res, pstmt);
     DataBaseFunctions.closeSqlObjects(con);
     }
     return arrAqDtlsList;
     }*/
    @Override
    public void reprocessNPSArrAqMast(int billNo, String aqslno) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
            }
            ArrAqMastModel arrAqMastModel = getNPSArrearAquitanceData(aqslno);

            pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=? WHERE emp_id=?");

            ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                    + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
            ps2.setString(1, arrAqMastModel.getEmpCode());
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                pstmt.setString(1, rs2.getString("bank_acc_no"));
                pstmt.setString(2, rs2.getString("bank_code"));
                pstmt.setString(3, rs2.getString("branch_code"));
                pstmt.setString(4, rs2.getString("ifsc_code"));
                pstmt.setString(5, arrAqMastModel.getEmpCode());
                pstmt.execute();
            }
            if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int calc_unique_no = 0;
                PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());
                while (beginCalendar.before(finishCalendar)) {

                    calc_unique_no++;

                    String date = formater.format(beginCalendar.getTime()).toUpperCase();

                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);

                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForNPSArrear(month, year, arrAqMastModel.getEmpCode(), aqslno);
                    saveNPSArrearArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);
                }

                saveArrmastNPSArreardata(arrAqMastModel, aqslno);

                /*pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_nps_arrear(aqsl_no) WHERE aqsl_no=?");
                 pstmt.setString(1, aqslno);
                 pstmt.executeUpdate();
                 pstmt = con.prepareStatement("update arr_mast set arrear_pay = full_arrear_pay WHERE aqsl_no=?");
                 pstmt.setString(1, aqslno);
                 pstmt.executeUpdate();*/
            } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                int calc_unique_no = 0;
                String date1 = choiceMonth + "-" + choiceYear;
                String date2 = arrearToMonth + "-" + arrearToYear;
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();
                DateFormat formater = new SimpleDateFormat("M-yyyy");
                try {
                    beginCalendar.setTime(formater.parse(date1));
                    finishCalendar.setTime(formater.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                while (beginCalendar.before(finishCalendar)) {
                    int dapercentage = 0;
                    calc_unique_no++;

                    String date = formater.format(beginCalendar.getTime()).toUpperCase();
                    int month = beginCalendar.get(Calendar.MONTH);
                    int year = beginCalendar.get(Calendar.YEAR);
                    if (month <= 5 && year == 2016) {
                        dapercentage = 0;
                    } else if (month > 5 && month <= 11 && year == 2016) {
                        dapercentage = 2;
                    } else if (month <= 5 && year == 2017) {
                        dapercentage = 4;
                    } else if (month > 5 && month <= 11 && year == 2017) {
                        dapercentage = 5;
                    }
                    ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForContractual(arrAqMastModel.getChoiceDate(), month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno, arrAqMastModel.getPayrevisiongp());
                    saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                    beginCalendar.add(Calendar.MONTH, 1);

                }
            }
        } catch (SQLException e) {
            //status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrAqDtlsModel[] getAqDtlsModelFromAllowanceListForNPSArrear(int month, int year, String empCode, String aqslno) throws SQLException {

        ArrayList<ArrAqDtlsModel> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;

        ResultSet res = null;

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("SELECT AD_TYPE,ALREADY_PAID,TO_BE_PAID,DRAWN_VIDE_BILLNO,REF_AQSL_NO FROM ARR_DTLS"
                    + " INNER JOIN ARR_MAST ON ARR_DTLS.AQSL_NO=ARR_MAST.AQSL_NO"
                    + " INNER JOIN BILL_MAST ON ARR_MAST.BILL_NO=BILL_MAST.BILL_NO"
                    + " WHERE EMP_ID=? AND ARR_DTLS.P_YEAR=? AND ARR_DTLS.P_MONTH=? AND ARR_TYPE='ARREAR'");
            pstmt.setString(1, empCode);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            res = pstmt.executeQuery();
            while (res.next()) {
                ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
                arrAqDtls.setAqslno(aqslno);
                arrAqDtls.setPayMonth(month);
                arrAqDtls.setPayYear(year);
                arrAqDtls.setAdType(res.getString("AD_TYPE"));
                arrAqDtls.setDrawnAMt(res.getInt("ALREADY_PAID"));
                arrAqDtls.setDueAmt(res.getInt("TO_BE_PAID"));
                arrAqDtls.setDrawnBillNo(res.getString("DRAWN_VIDE_BILLNO"));
                arrAqDtls.setRefaqslno(res.getString("REF_AQSL_NO"));
                list.add(arrAqDtls);
            }

            /*ArrAqDtlsModel arrAqDtls = new ArrAqDtlsModel();
             arrAqDtls.setAqslno(aqslno);
             arrAqDtls.setPayMonth(month);
             arrAqDtls.setPayYear(year);
             arrAqDtls.setAdType("PAY");
             arrAqDtls.setDueAmt(0);
             arrAqDtls.setBtid("136");
             list.add(arrAqDtls);

             arrAqDtls = new ArrAqDtlsModel();
             arrAqDtls.setAqslno(aqslno);
             arrAqDtls.setPayMonth(month);
             arrAqDtls.setPayYear(year);
             arrAqDtls.setAdType("GP");
             arrAqDtls.setDueAmt(0);
             arrAqDtls.setBtid("136");
             list.add(arrAqDtls);

             arrAqDtls = new ArrAqDtlsModel();
             arrAqDtls.setAqslno(aqslno);
             arrAqDtls.setPayMonth(month);
             arrAqDtls.setPayYear(year);
             arrAqDtls.setAdType("DA");
             arrAqDtls.setDueAmt(0);
             arrAqDtls.setBtid("156");
             list.add(arrAqDtls);*/
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        ArrAqDtlsModel arrAqDtlsModels[] = list.toArray(new ArrAqDtlsModel[list.size()]);
        return arrAqDtlsModels;
    }

    public void saveNPSArrearArrdtlsdata(ArrAqDtlsModel[] arrAqDtlsModels, int calcUniqueNo) {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO ARR_DTLS (AQSL_NO,P_MONTH,P_YEAR,AD_TYPE,ALREADY_PAID, TO_BE_PAID, DRAWN_VIDE_BILLNO, REF_AQSL_NO, CALC_UNIQUE_NO, BT_ID)VALUES (?,?,?,?,?,?,?,?,?,?) ");
            for (int i = 0; i < arrAqDtlsModels.length; i++) {
                ArrAqDtlsModel arrAqDtlsModel = arrAqDtlsModels[i];

                pstmt.setString(1, arrAqDtlsModel.getAqslno());
                pstmt.setInt(2, arrAqDtlsModel.getPayMonth());
                pstmt.setInt(3, arrAqDtlsModel.getPayYear());
                pstmt.setString(4, arrAqDtlsModel.getAdType());
                pstmt.setInt(5, arrAqDtlsModel.getDrawnAMt());
                pstmt.setInt(6, arrAqDtlsModel.getDueAmt());
                pstmt.setString(7, arrAqDtlsModel.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsModel.getRefaqslno());
                pstmt.setInt(9, calcUniqueNo);
                pstmt.setString(10, arrAqDtlsModel.getBtid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getNPSArrearAcquaintance(int billNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        List arrAqList = new ArrayList();

        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("select emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt from arr_mast "
             + "where bill_no = ? order by emp_name");*/
            pstmt = con.prepareStatement("select acct_type,emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt,"
                    + " getarradtotal(AQSL_NO,'PAY') AS PAY_TOTAL,getarradtotal(AQSL_NO,'DA') AS DA_TOTAL,getarradtotal(AQSL_NO,'GP') AS GP_TOTAL, arrear_percent from arr_mast"
                    + " where bill_no = ? order by emp_name");

            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            while (res.next()) {

                ArrAqMastModel aqBean = new ArrAqMastModel();

                aqBean.setEmpCode(res.getString("EMP_ID"));
                aqBean.setEmpName(res.getString("EMP_NAME"));
                aqBean.setCurDesg(res.getString("CUR_DESG"));
                aqBean.setAqSlNo(res.getString("aqsl_no"));
                aqBean.setArrearpay(res.getInt("full_arrear_pay"));
                aqBean.setArrear40(res.getInt("arrear_pay"));
                aqBean.setCurBasic(res.getInt("cur_basic"));
                //aqBean.setIncomeTaxAmt(res.getString("inctax"));
                //aqBean.setCpfHead(res.getString("cpf_head"));
                if (res.getInt("cpf_head") > 0) {
                    aqBean.setCpfHead(res.getString("cpf_head"));
                } else if (res.getInt("cpf_head") == 0) {
                    aqBean.setCpfHead(res.getString("cpf_head"));
                } else if (res.getString("acct_type") != null && res.getString("acct_type").equals("PRAN")) {
                    double cpfAmt = res.getInt("PAY_TOTAL") + res.getInt("DA_TOTAL") + res.getInt("GP_TOTAL");
                    cpfAmt = cpfAmt * 0.5;
                    cpfAmt = cpfAmt * 0.1;
                    aqBean.setCpfHead(Math.round(cpfAmt) + "");
                }

                //aqBean.setPt(res.getString("pt"));
                aqBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqList;
    }

    public void saveArrmastNPSArreardata(ArrAqMastModel arrAqMastModel, String aqslno) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        String arrearaqslno = "";

        int grandTotArr100 = 0;
        try {
            con = this.dataSource.getConnection();

            /*pstmt = con.prepareStatement("SELECT AQSL_NO FROM ARR_MAST WHERE EMP_ID=? AND ARR_TYPE='ARREAR'");
             pstmt.setString(1, arrAqMastModel.getEmpCode());
             res = pstmt.executeQuery();
             while (res.next()) {
             arrearaqslno = res.getString("AQSL_NO");

             int j = 1;
             List arrAqDtlsList = getArrearAcquaintanceDtls(arrearaqslno, j);

             ArrAqDtlsModel obj = null;
             if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
             obj = new ArrAqDtlsModel();
             for (int i = 0; i < arrAqDtlsList.size(); i++) {
             obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

             grandTotArr100 = grandTotArr100 + obj.getArrear100();
             }
             }
             }*/
            int j = 1;
            List arrAqDtlsList = getNPSArrearAquitanceDtls(aqslno, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                }
            }

            if (grandTotArr100 > 0) {
                double npsarrearAmt50 = grandTotArr100 * 0.5;
                double npsarrearAmt10 = npsarrearAmt50 * 0.1;

                pstmt = con.prepareStatement("UPDATE ARR_MAST SET FULL_ARREAR_PAY=?,ARREAR_PAY=?,CPF_HEAD=?,CPF_HEAD_GC=? WHERE AQSL_NO=?");
                pstmt.setDouble(1, grandTotArr100);
                pstmt.setDouble(2, npsarrearAmt50);
                pstmt.setDouble(3, npsarrearAmt10);
                pstmt.setDouble(4, npsarrearAmt10);
                pstmt.setString(5, aqslno);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private List<ArrAqDtlsModel> getNPSArrearAquitanceDtls(String aqSlNo, int j) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        List<ArrAqDtlsModel> arrAqDtlsList = new ArrayList<>();

        List rowsNumList = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            int rows = 0;
            int rownum = 0;

            /*pstmt = con.prepareStatement("select count(*) as noofrows from arr_dtls WHERE aqsl_no = ? group by p_year,p_month, calc_unique_no order by p_year,p_month,calc_unique_no ");
             pstmt.setString(1, aqSlNo);
             res = pstmt.executeQuery();
             while (res.next()) {
             rows = res.getInt("noofrows");
             rowsNumList.add(rows + "");
             }*/
            //DataBaseFunctions.closeSqlObjects(res, pstmt);
            //pstmt = con.prepareStatement("SELECT p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no FROM arr_dtls WHERE aqsl_no=? order by p_year,p_month,calc_unique_no,ad_type");
            pstmt = con.prepareStatement("SELECT p_month, p_year, ad_type, sum(to_be_paid) to_be_paid, sum(already_paid) already_paid, calc_unique_no FROM arr_dtls WHERE aqsl_no=?"
                    + " group by p_year,p_month,ad_type,calc_unique_no"
                    + " order by p_year,p_month,ad_type,calc_unique_no");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            int i = 0;
            ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                //rows = Integer.parseInt((String) rowsNumList.get(rownum));
                i++;

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);

                arrAqDtlsBean.setPayMonth(pmonth);
                arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);
                arrAqDtlsBean.setAdType(res.getString("ad_type"));

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                }

                //arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                int totDueAmt = arrAqDtlsBean.getDuePayAmt() + arrAqDtlsBean.getDueGpAmt() + arrAqDtlsBean.getDueDaAmt();
                arrAqDtlsBean.setDueTotalAmt(totDueAmt);

                int totDrawnAmt = arrAqDtlsBean.getDrawnPayAmt() + arrAqDtlsBean.getDrawnGpAmt() + arrAqDtlsBean.getDrawnDaAmt();
                arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

                int arrear100 = totDueAmt - totDrawnAmt;
                arrAqDtlsBean.setArrear100(arrear100);

                if (i == 3) {

                    //rownum++;
                    arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));

                    arrAqDtlsList.add(arrAqDtlsBean);
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    i = 0;
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsList;
    }

    private ArrayList employeelist20percent(int billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            String sql = "select arr_mast.emp_id,if_gpf_assumed,arr_mast.ACCT_TYPE from arr_mast"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " where bill_no=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, billno);
            rs = pst.executeQuery();
            while (rs.next()) {
                emplist.add(rs.getString("emp_id") + "-" + rs.getString("if_gpf_assumed"));
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
    public ArrAqDtlsModel getArrear6AcquaintanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno, int autoincrid) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrAqDtlsModel arrAqDtlsBean = null;

        try {
            con = this.dataSource.getConnection();
            //pstmt = con.prepareStatement("select * from arr_dtls where aqsl_no = ? and p_month= ? and p_year= ? and calc_unique_no=? and auto_incr_id=?");
            pstmt = con.prepareStatement("select * from arr_dtls where aqsl_no = ? and p_month= ? and p_year= ? and calc_unique_no=?");
            pstmt.setString(1, aqSlNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            pstmt.setInt(4, calcuniqueno);
            //pstmt.setInt(5, autoincrid);
            res = pstmt.executeQuery();
            int i = 0;
            arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);
                arrAqDtlsBean.setPayMonth(pmonth);
                //arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));

                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));

                }
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                arrAqDtlsBean.setAutoincrid(res.getInt("auto_incr_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsBean;
    }

    @Override
    public int updateArr6DtlsData(ArrAqDtlsModel arrAqDtlsModel) {

        Connection con = null;
        PreparedStatement pstmt = null;
        int res = 0;
        try {

            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnPayAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDuePayAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "PAY");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            //pstmt.setInt(9, arrAqDtlsModel.getAutoincrid());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnGpAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueGpAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "GP");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            //pstmt.setInt(9, arrAqDtlsModel.getAutoincrid());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_dtls set already_paid = ? ,to_be_paid = ?, drawn_vide_billno=? where aqsl_no = ? and p_month = ? and p_year = ? and ad_type=? and calc_unique_no = ?");

            pstmt.setInt(1, arrAqDtlsModel.getDrawnDaAmt());
            pstmt.setInt(2, arrAqDtlsModel.getDueDaAmt());
            pstmt.setString(3, arrAqDtlsModel.getDrawnBillNo());
            pstmt.setString(4, arrAqDtlsModel.getAqslno());
            pstmt.setInt(5, arrAqDtlsModel.getPayMonth());
            pstmt.setInt(6, arrAqDtlsModel.getPayYear());
            pstmt.setString(7, "DA");
            pstmt.setInt(8, arrAqDtlsModel.getCalcuniqueno());
            //pstmt.setInt(9, arrAqDtlsModel.getAutoincrid());
            res = pstmt.executeUpdate();

            pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
            pstmt.setString(1, arrAqDtlsModel.getAqslno());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    @Override
    public int updateArrMastORData(int billNo, String aqSlNo, int orAmt) {

        Connection con = null;

        PreparedStatement pstmt = null;
        int res = 0;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("update arr_mast set other_recovery=? where aqsl_no=? and bill_no=?");
            pstmt.setInt(1, orAmt);
            pstmt.setString(2, aqSlNo);
            pstmt.setInt(3, billNo);
            res = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return res;
    }

    private String checkContractual6YearsToRegularAfter7thPayrevision(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isCont6RegularAfter7thPayrevision = "N";

        try {
            con = this.repodataSource.getConnection();

            Date payrevisiondate = new SimpleDateFormat("YYYY-MM-DD").parse("2016-01-01");

            String sql = "select date_of_regularization,extract(year from date_of_regularization) regularization_year from emp_regularization_contractual"
                    + " inner join emp_mast on emp_regularization_contractual.emp_id=emp_mast.emp_id"
                    + " where date_of_regularization is not null and emp_regularization_contractual.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                Date regularizationdate = new SimpleDateFormat("YYYY-MM-DD").parse(rs.getString("date_of_regularization"));

                if (regularizationdate.compareTo(payrevisiondate) > 0) {
                    isCont6RegularAfter7thPayrevision = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isCont6RegularAfter7thPayrevision;
    }

    @Override
    public int reprocessJudiciaryArrAqMast(int billNo, String aqslno) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;

        int msg = 1;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
            }

            ArrAqMastModel arrAqMastModel = getJudiciaryArrearAcquaintanceData(aqslno);
            if (isJudiciaryFirst25BillAlreadyPrepared(arrAqMastModel.getEmpCode(), billNo) == true) {
                msg = 2;
            } else {
                if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                    String date1 = choiceMonth + "-" + choiceYear;
                    String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();
                    DateFormat formater = new SimpleDateFormat("M-yyyy");
                    try {
                        beginCalendar.setTime(formater.parse(date1));
                        finishCalendar.setTime(formater.parse(date2));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int calc_unique_no = 0;
                    PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());

                    pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=?,arrear_percent=? WHERE emp_id=? and bill_no=?");
                    ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                            + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
                    ps2.setString(1, arrAqMastModel.getEmpCode());
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {

                        pstmt.setString(1, rs2.getString("bank_acc_no"));
                        pstmt.setString(2, rs2.getString("bank_code"));
                        pstmt.setString(3, rs2.getString("branch_code"));
                        pstmt.setString(4, rs2.getString("ifsc_code"));
                        pstmt.setInt(5, 25);
                        pstmt.setString(6, arrAqMastModel.getEmpCode());
                        pstmt.setInt(7, billNo);
                        pstmt.execute();
                    }
                    while (beginCalendar.before(finishCalendar)) {
                        double dapercentage = 0;
                        calc_unique_no++;
                        String date = formater.format(beginCalendar.getTime()).toUpperCase();
                        int month = beginCalendar.get(Calendar.MONTH);
                        int year = beginCalendar.get(Calendar.YEAR);
                        String isPayRevised = "N";
                        if (po.getChoiceDate() != null) {
                            isPayRevised = "Y";
                        }
                        if (isPayRevised.equals("Y")) {
                            if (month <= 5 && year == 2016) {
                                dapercentage = 0;
                            } else if (month > 5 && month <= 11 && year == 2016) {
                                dapercentage = 2;
                            } else if (month <= 5 && year == 2017) {
                                dapercentage = 4;
                            } else if (month > 5 && month <= 11 && year == 2017) {
                                dapercentage = 5;
                            } else {
                                dapercentage = 7;
                            }
                        } else {
                            if (month >= 3) {
                                dapercentage = 1.42;
                            } else {
                                dapercentage = 1.39;
                            }
                        }

                        //ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceList(po, month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno);
                        ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForJudiciaryArrear(po, month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno);
                        saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                        beginCalendar.add(Calendar.MONTH, 1);

                    }
                    pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no), acct_type=? WHERE aqsl_no=?");
                    pstmt.setString(1, arrAqMastModel.getAccType());
                    pstmt.setString(2, aqslno);
                    pstmt.executeUpdate();
                    pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.25) WHERE aqsl_no=?");
                    pstmt.setString(1, aqslno);
                    pstmt.executeUpdate();

                } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                    int calc_unique_no = 0;
                    String date1 = choiceMonth + "-" + choiceYear;
                    String date2 = arrearToMonth + "-" + arrearToYear;
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();
                    DateFormat formater = new SimpleDateFormat("M-yyyy");
                    try {
                        beginCalendar.setTime(formater.parse(date1));
                        finishCalendar.setTime(formater.parse(date2));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=? WHERE emp_id=?  and bill_no =?");
                    ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code from emp_mast "
                            + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=?");
                    ps2.setString(1, arrAqMastModel.getEmpCode());
                    rs2 = ps2.executeQuery();
                    if (rs2.next()) {

                        pstmt.setString(1, rs2.getString("bank_acc_no"));
                        pstmt.setString(2, rs2.getString("bank_code"));
                        pstmt.setString(3, rs2.getString("branch_code"));
                        pstmt.setString(4, rs2.getString("ifsc_code"));
                        pstmt.setString(5, arrAqMastModel.getEmpCode());
                        pstmt.setInt(6, billNo);
                        pstmt.execute();
                    }

                    while (beginCalendar.before(finishCalendar)) {
                        int dapercentage = 0;
                        calc_unique_no++;

                        String date = formater.format(beginCalendar.getTime()).toUpperCase();
                        int month = beginCalendar.get(Calendar.MONTH);
                        int year = beginCalendar.get(Calendar.YEAR);
                        if (month <= 5 && year == 2016) {
                            dapercentage = 0;
                        } else if (month > 5 && month <= 11 && year == 2016) {
                            dapercentage = 2;
                        } else if (month <= 5 && year == 2017) {
                            dapercentage = 4;
                        } else if (month > 5 && month <= 11 && year == 2017) {
                            dapercentage = 5;
                        }
                        ArrAqDtlsModel[] arrAqDtlsModels = getAqDtlsModelFromAllowanceListForContractual(arrAqMastModel.getChoiceDate(), month, year, arrAqMastModel.getEmpCode(), dapercentage, aqslno, arrAqMastModel.getPayrevisiongp());
                        saveArrdtlsdata(arrAqDtlsModels, calc_unique_no);
                        beginCalendar.add(Calendar.MONTH, 1);

                    }
                }
            }
        } catch (SQLException e) {
            //status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(rs2, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public boolean isJudiciaryFirst25BillAlreadyPrepared(String empid, int billno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isBillPrepared = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from arr_mast where emp_id=? and arr_type='ARREAR_J' and bill_no<>?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, billno);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isBillPrepared = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isBillPrepared;
    }

    public boolean isJudiciarySecond25BillAlreadyPrepared(String empid, int arrearpercent, int billNo) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isBillPrepared = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from arr_mast where emp_id=? and arr_type='ARREAR_6_J' and arrear_percent=? and bill_no<>?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, arrearpercent);
            pst.setInt(3, billNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isBillPrepared = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isBillPrepared;
    }

    @Override
    public ArrAqMastModel getJudiciaryArrearAcquaintanceData(String aqSlNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        String acctType = "GPF";
        String ifGPFAssumed = "N";
        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, arrear_pay, full_arrear_pay, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqMastBean.setArrearpay(res.getInt("arrear_pay"));
                arrAqMastBean.setFullArrearpay(res.getInt("full_arrear_pay"));
            }
            int j = 1;
            List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqSlNo, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(grandTotArr100);
            //arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * 0.25));

            double percent = 1.0;
            if (arrAqMastBean.getPercentageArraer() == 25) {
                percent = 0.25;
            } else if (arrAqMastBean.getPercentageArraer() == 50) {
                percent = 0.5;
            }
            arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * percent));
            arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));

            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    @Override
    public ArrAqDtlsModel getJudiciaryArrearAquitanceDataList(String aqSlNo, int aqMonth, int aqYear, int calcuniqueno) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        ArrAqDtlsModel arrAqDtlsBean = null;

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("select * from arr_dtls where aqsl_no = ? and p_month= ? and p_year= ? and calc_unique_no=?");
            pstmt.setString(1, aqSlNo);
            pstmt.setInt(2, aqMonth);
            pstmt.setInt(3, aqYear);
            pstmt.setInt(4, calcuniqueno);
            res = pstmt.executeQuery();
            int i = 0;
            arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);
                arrAqDtlsBean.setPayMonth(pmonth);
                //arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("IR")) {
                    arrAqDtlsBean.setDueIrAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnIrAmt(res.getInt("already_paid"));
                }
                arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));
                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));
                arrAqDtlsBean.setAutoincrid(res.getInt("auto_incr_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsBean;
    }

    public List<ArrAqDtlsModel> getJudiciaryArrearAquitanceDtls(String aqSlNo, int j) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        List<ArrAqDtlsModel> arrAqDtlsList = new ArrayList<>();

        List rowsNumList = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            int rows = 0;
            int rownum = 0;

            pstmt = con.prepareStatement("select count(*) as noofrows from arr_dtls WHERE aqsl_no = ? group by p_year,p_month, calc_unique_no order by p_year,p_month,calc_unique_no ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            while (res.next()) {
                rows = res.getInt("noofrows");
                rowsNumList.add(rows + "");
            }

            pstmt = con.prepareStatement("SELECT p_month, p_year, ad_type, to_be_paid, already_paid, drawn_vide_billno,calc_unique_no FROM arr_dtls WHERE aqsl_no=? order by p_year,p_month,calc_unique_no,drawn_vide_billno,ad_type");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            int i = 0;
            ArrAqDtlsModel arrAqDtlsBean = new ArrAqDtlsModel();
            while (res.next()) {
                rows = Integer.parseInt((String) rowsNumList.get(rownum));

                i++;

                int pmonth = res.getInt("p_month");
                int pyear = res.getInt("p_year");

                arrAqDtlsBean.setAqslno(aqSlNo);

                arrAqDtlsBean.setPayMonth(pmonth);
                arrAqDtlsBean.setPayMonthName(CalendarCommonMethods.getMonthAsString(pmonth));
                arrAqDtlsBean.setPayYear(pyear);
                arrAqDtlsBean.setAdType(res.getString("ad_type"));

                if (res.getString("ad_type").equals("PAY")) {
                    arrAqDtlsBean.setDuePayAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnPayAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("GP")) {
                    arrAqDtlsBean.setDueGpAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnGpAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("DA")) {
                    arrAqDtlsBean.setDueDaAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnDaAmt(res.getInt("already_paid"));
                } else if (res.getString("ad_type").equals("IR")) {
                    arrAqDtlsBean.setDueIrAmt(res.getInt("to_be_paid"));
                    arrAqDtlsBean.setDrawnIrAmt(res.getInt("already_paid"));
                }

                arrAqDtlsBean.setDrawnBillNo(res.getString("drawn_vide_billno"));

                int totDueAmt = arrAqDtlsBean.getDuePayAmt() + arrAqDtlsBean.getDueGpAmt() + arrAqDtlsBean.getDueDaAmt() + arrAqDtlsBean.getDueIrAmt();
                arrAqDtlsBean.setDueTotalAmt(totDueAmt);

                int totDrawnAmt = arrAqDtlsBean.getDrawnPayAmt() + arrAqDtlsBean.getDrawnGpAmt() + arrAqDtlsBean.getDrawnDaAmt() + arrAqDtlsBean.getDrawnIrAmt();
                arrAqDtlsBean.setDrawnTotalAmt(totDrawnAmt);

                int arrear100 = totDueAmt - totDrawnAmt;
                arrAqDtlsBean.setArrear100(arrear100);

                if (i == rows) {
                    rownum++;
                    arrAqDtlsBean.setCalcuniqueno(res.getInt("calc_unique_no"));

                    arrAqDtlsList.add(arrAqDtlsBean);
                    arrAqDtlsBean = new ArrAqDtlsModel();
                    i = 0;
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqDtlsList;
    }

    /*@Override
     public void reprocessJudiciaryArr6AqMast(int billNo, String aqslno) {

     Connection con = null;

     PreparedStatement pstmt = null;
     ResultSet res = null;

     PreparedStatement ps2 = null;
     ResultSet rs2 = null;
        
     PreparedStatement updatepst = null;
        
     int choiceYear = 2016;
     int choiceMonth = 1;
     int arrearToMonth = 7;
     int arrearToYear = 2017;
     int arrearPercent = 0;
     try {
     con = this.dataSource.getConnection();
            
     updatepst = con.prepareStatement("UPDATE ARR_MAST SET ARREAR_PAY=?,FULL_ARREAR_PAY=? WHERE AQSL_NO=?");
            
     pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
     pstmt.setString(1, aqslno);
     pstmt.executeUpdate();
     pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year,arrear_percent from bill_mast where bill_no=?");
     pstmt.setInt(1, billNo);
     res = pstmt.executeQuery();
     if (res.next()) {
     choiceMonth = res.getInt("from_month");
     choiceYear = res.getInt("from_year");
     arrearToMonth = res.getInt("to_month");
     arrearToYear = res.getInt("to_year");
     arrearPercent = res.getInt("arrear_percent");
     }
     ArrAqMastModel arrAqMastModel = getJudiciary6ArrearAquitanceData(aqslno);

     pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=?, acct_type=?, arrear_percent=? WHERE emp_id=? and bill_no =? ");
     ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code, acct_type from emp_mast "
     + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=? ");
     ps2.setString(1, arrAqMastModel.getEmpCode());
     rs2 = ps2.executeQuery();
     if (rs2.next()) {

     pstmt.setString(1, rs2.getString("bank_acc_no"));
     pstmt.setString(2, rs2.getString("bank_code"));
     pstmt.setString(3, rs2.getString("branch_code"));
     pstmt.setString(4, rs2.getString("ifsc_code"));
     pstmt.setString(5, rs2.getString("acct_type"));
     pstmt.setInt(6, arrearPercent);
     pstmt.setString(7, arrAqMastModel.getEmpCode());
     pstmt.setInt(8, billNo);
     pstmt.execute();
     }
     if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
     String date1 = choiceMonth + "-" + choiceYear;
     String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
     Calendar beginCalendar = Calendar.getInstance();
     Calendar finishCalendar = Calendar.getInstance();
     DateFormat formater = new SimpleDateFormat("M-yyyy");
     try {
     beginCalendar.setTime(formater.parse(date1));
     finishCalendar.setTime(formater.parse(date2));
     pstmt = con.prepareStatement("SELECT * FROM ARR_MAST "
     + " inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no "
     + " WHERE EMP_ID=? AND ARR_TYPE='ARREAR_J'");
     pstmt.setString(1, arrAqMastModel.getEmpCode());
     res = pstmt.executeQuery();
     String aqsl_no = "";
     int arrearpay = 0;
     int fullarrearpay = 0;
     int ctr = 0;
     int j = 1;
     while (res.next()) {
     ctr++;
     aqsl_no = res.getString("AQSL_NO");
     arrearpay = res.getInt("arrear_pay");
     fullarrearpay = res.getInt("full_arrear_pay");
     if (!aqsl_no.equals("")) {
     if (ctr == 1) {
     j = 1;
     } else if (ctr == 2) {
     j = 21;
     } else if (ctr == 3) {
     j = 41;
     } else {
     j = 61;
     }
     List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqsl_no, j);
     saveArrdtlsJudiciary6data(arrAqDtlsList, aqslno);
                            
     updatepst.setInt(1,arrearpay);
     updatepst.setInt(2,fullarrearpay);
     updatepst.setString(3,aqslno);
     updatepst.executeUpdate();
     }
     }

     } catch (ParseException e) {
     e.printStackTrace();
     }
     int calc_unique_no = 0;
     PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());
     } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
     int calc_unique_no = 0;
     String date1 = choiceMonth + "-" + choiceYear;
     String date2 = arrearToMonth + "-" + arrearToYear;
     Calendar beginCalendar = Calendar.getInstance();
     Calendar finishCalendar = Calendar.getInstance();
     DateFormat formater = new SimpleDateFormat("M-yyyy");
     try {
     beginCalendar.setTime(formater.parse(date1));
     finishCalendar.setTime(formater.parse(date2));
     pstmt = con.prepareStatement("SELECT AQSL_NO FROM ARR_MAST WHERE EMP_ID=? AND ARR_TYPE='ARREAR'");
     pstmt.setString(1, arrAqMastModel.getEmpCode());
     res = pstmt.executeQuery();
     String aqsl_no = "";
     if (res.next()) {
     aqsl_no = res.getString("AQSL_NO");
     }
     int j = 1;
     if (!aqsl_no.equals("")) {
     List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqsl_no, j);
     saveArrdtlsdata(arrAqDtlsList, aqslno);
     }
     } catch (ParseException e) {
     e.printStackTrace();
     }
     while (beginCalendar.before(finishCalendar)) {
     int dapercentage = 0;
     calc_unique_no++;

     String date = formater.format(beginCalendar.getTime()).toUpperCase();
     int month = beginCalendar.get(Calendar.MONTH);
     int year = beginCalendar.get(Calendar.YEAR);

     }
     }
     } catch (SQLException e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(pstmt);
     DataBaseFunctions.closeSqlObjects(con);
     }

     }*/
    public int reprocessJudiciaryArr6AqMast(int billNo, String aqslno) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        PreparedStatement updatepst = null;

        int choiceYear = 2016;
        int choiceMonth = 1;
        int arrearToMonth = 7;
        int arrearToYear = 2017;
        int arrearPercent = 0;

        int msg = 1;
        try {
            con = this.dataSource.getConnection();

            updatepst = con.prepareStatement("UPDATE ARR_MAST SET ARREAR_PAY=?,FULL_ARREAR_PAY=? WHERE AQSL_NO=?");

            pstmt = con.prepareStatement("delete from arr_dtls where aqsl_no=?");
            pstmt.setString(1, aqslno);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement("select from_month,from_year,to_month,to_year,arrear_percent from bill_mast where bill_no=?");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                choiceMonth = res.getInt("from_month");
                choiceYear = res.getInt("from_year");
                arrearToMonth = res.getInt("to_month");
                arrearToYear = res.getInt("to_year");
                arrearPercent = res.getInt("arrear_percent");
            }
            ArrAqMastModel arrAqMastModel = getJudiciary6ArrearAquitanceData(aqslno);
            if (isJudiciarySecond25BillAlreadyPrepared(arrAqMastModel.getEmpCode(), arrAqMastModel.getPercentageArraer(), billNo) == true) {
                msg = 2;
            } else {
                pstmt = con.prepareStatement("update arr_mast set bank_acc_no = ?, bank_name=?, branch_name=?, ifsc_code=?, acct_type=?, arrear_percent=? WHERE emp_id=? and bill_no =? ");
                ps2 = con.prepareStatement(" select bank_acc_no, emp_mast.bank_code, emp_mast.branch_code, ifsc_code, acct_type from emp_mast "
                        + " inner join g_branch on emp_mast.branch_code=g_branch.branch_code where emp_id=? ");
                ps2.setString(1, arrAqMastModel.getEmpCode());
                rs2 = ps2.executeQuery();
                if (rs2.next()) {

                    pstmt.setString(1, rs2.getString("bank_acc_no"));
                    pstmt.setString(2, rs2.getString("bank_code"));
                    pstmt.setString(3, rs2.getString("branch_code"));
                    pstmt.setString(4, rs2.getString("ifsc_code"));
                    pstmt.setString(5, rs2.getString("acct_type"));
                    pstmt.setInt(6, arrearPercent);
                    pstmt.setString(7, arrAqMastModel.getEmpCode());
                    pstmt.setInt(8, billNo);
                    pstmt.execute();
                }
                if (arrAqMastModel.getEmpType().equalsIgnoreCase("R")) {
                    String date1 = choiceMonth + "-" + choiceYear;
                    String date2 = (arrearToMonth + 1) + "-" + arrearToYear;
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();
                    DateFormat formater = new SimpleDateFormat("M-yyyy");
                    try {
                        beginCalendar.setTime(formater.parse(date1));
                        finishCalendar.setTime(formater.parse(date2));
                        int arrearpay = 0;
                        int fullarrearpay = 0;
                        if (arrAqMastModel.getPercentageArraer() == 50) {
                            pstmt = con.prepareStatement("SELECT AQSL_NO,ARREAR_PAY,FULL_ARREAR_PAY FROM ARR_MAST WHERE EMP_ID=? AND ARR_TYPE in ('ARREAR_J','ARREAR_6_J') and ARREAR_PERCENT <> 50");
                            pstmt.setString(1, arrAqMastModel.getEmpCode());
                            res = pstmt.executeQuery();
                            String aqsl_no = "";
                            while (res.next()) {
                                aqsl_no = res.getString("AQSL_NO");
                                arrearpay = arrearpay + res.getInt("ARREAR_PAY");
                                fullarrearpay = res.getInt("FULL_ARREAR_PAY");

                                if (!aqsl_no.equals("")) {
                                    List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqsl_no, 0);
                                    saveArrdtlsJudiciary6data(arrAqDtlsList, aqslno);
                                }
                                //Double doublearrearpay = fullarrearpay*0.5;
                                int intarrearpay = (int) Math.round(fullarrearpay * 0.5);
                                //updatepst.setInt(1, arrearpay);
                                updatepst.setInt(1, intarrearpay);
                                updatepst.setInt(2, fullarrearpay);
                                updatepst.setString(3, aqslno);
                                updatepst.executeUpdate();
                            }
                        } else {
                            pstmt = con.prepareStatement("SELECT * FROM ARR_MAST "
                                    + " inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no "
                                    + " WHERE EMP_ID=? AND ARR_TYPE='ARREAR_J'");
                            pstmt.setString(1, arrAqMastModel.getEmpCode());
                            res = pstmt.executeQuery();
                            String aqsl_no = "";
                            arrearpay = 0;
                            fullarrearpay = 0;
                            int ctr = 0;
                            int j = 1;
                            while (res.next()) {
                                ctr++;
                                aqsl_no = res.getString("AQSL_NO");
                                arrearpay = res.getInt("arrear_pay");
                                fullarrearpay = res.getInt("full_arrear_pay");
                                if (!aqsl_no.equals("")) {
                                    if (ctr == 1) {
                                        j = 1;
                                    } else if (ctr == 2) {
                                        j = 21;
                                    } else if (ctr == 3) {
                                        j = 41;
                                    } else {
                                        j = 61;
                                    }
                                    List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqsl_no, j);
                                    saveArrdtlsJudiciary6data(arrAqDtlsList, aqslno);

                                    updatepst.setInt(1, arrearpay);
                                    updatepst.setInt(2, fullarrearpay);
                                    updatepst.setString(3, aqslno);
                                    updatepst.executeUpdate();
                                }
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int calc_unique_no = 0;
                    PayRevisionOption po = getChoiceDate(arrAqMastModel.getEmpCode());
                } else if (arrAqMastModel.getEmpType().equalsIgnoreCase("C")) {
                    int calc_unique_no = 0;
                    String date1 = choiceMonth + "-" + choiceYear;
                    String date2 = arrearToMonth + "-" + arrearToYear;
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();
                    DateFormat formater = new SimpleDateFormat("M-yyyy");
                    try {
                        beginCalendar.setTime(formater.parse(date1));
                        finishCalendar.setTime(formater.parse(date2));
                        pstmt = con.prepareStatement("SELECT AQSL_NO FROM ARR_MAST WHERE EMP_ID=? AND ARR_TYPE='ARREAR'");
                        pstmt.setString(1, arrAqMastModel.getEmpCode());
                        res = pstmt.executeQuery();
                        String aqsl_no = "";
                        if (res.next()) {
                            aqsl_no = res.getString("AQSL_NO");
                        }
                        int j = 1;
                        if (!aqsl_no.equals("")) {
                            List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqsl_no, j);
                            saveArrdtlsdata(arrAqDtlsList, aqslno);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    while (beginCalendar.before(finishCalendar)) {
                        int dapercentage = 0;
                        calc_unique_no++;

                        String date = formater.format(beginCalendar.getTime()).toUpperCase();
                        int month = beginCalendar.get(Calendar.MONTH);
                        int year = beginCalendar.get(Calendar.YEAR);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public ArrAqMastModel getJudiciary6ArrearAquitanceData(String aqSlNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {
                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
            }
            /*int j = 1;
             List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqSlNo, j);

             ArrAqDtlsModel obj = null;
             if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
             obj = new ArrAqDtlsModel();
             for (int i = 0; i < arrAqDtlsList.size(); i++) {
             obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

             grandTotArr100 = grandTotArr100 + obj.getArrear100();
             arrAqMastBean.setAqSlNo(aqSlNo);
             }
             }
             arrAqMastBean.setGrandTotArr100(grandTotArr100);
             arrAqMastBean.setGrandTotArr40(Math.round(grandTotArr100 * 0.4));

             double percent = .1;
             if (arrAqMastBean.getPercentageArraer() == 25) {
             percent = 0.25;
             } else if (arrAqMastBean.getPercentageArraer() == 50) {
             percent = 0.5;
             }

             arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));

             arrAqMastBean.setArrDetails(arrAqDtlsList);*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    @Override
    public List getJudiciaryArrearBillDtls(int billno, String aqMonth, String aqYear, String typeofbill) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List arrEmpList = new ArrayList();
        ArrAqMastModel arrAqBean = null;
        int slNo = 1;

        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT arr_mast.arrear_percent,AQSL_NO, EMP_NAME,CUR_DESG, FULL_ARREAR_PAY, getarradtotal(AQSL_NO,'PAY') AS PAY_TOTAL, getarradtotal(AQSL_NO,'GP') AS GP_TOTAL, "
                    + "getarradtotal(AQSL_NO,'DA') AS DA_TOTAL, getarradtotal(AQSL_NO,'HRA') AS HRA_TOTAL, getarradtotal(AQSL_NO,'OA') AS OA_TOTAL, getarradtotal(AQSL_NO,'IR') AS IR_TOTAL, ARREAR_PAY,CPF_HEAD,INCTAX,PT,REMARK,GPF_NO,other_recovery FROM ARR_MAST "
                    + "INNER JOIN EMP_MAST ON ARR_MAST.EMP_ID = EMP_MAST.EMP_ID WHERE BILL_NO = ? order by EMP_NAME");
            pstmt.setInt(1, billno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arrAqBean = new ArrAqMastModel();
                arrAqBean.setSlno(slNo);
                arrAqBean.setAqSlNo(rs.getString("AQSL_NO"));
                arrAqBean.setEmpName(rs.getString("EMP_NAME"));
                arrAqBean.setCurDesg(rs.getString("CUR_DESG"));
                arrAqBean.setGrandTotArr100(rs.getInt("FULL_ARREAR_PAY"));
                arrAqBean.setGrandTotArr40(rs.getInt("ARREAR_PAY"));
                if (typeofbill != null && typeofbill.equals("ARREAR_J")) {
                    //arrAqBean.setGrandTotArr40(rs.getInt("ARREAR_PAY"));
                } else if (typeofbill != null && typeofbill.equals("ARREAR_6_J")) {
                    if (rs.getInt("arrear_percent") == 50) {
                        arrAqBean.setGrandTotArr40(Math.round(rs.getInt("FULL_ARREAR_PAY") * 0.5));
                    }
                }
                arrAqBean.setCpfHead(rs.getInt("CPF_HEAD") + "");
                arrAqBean.setIncomeTaxAmt(rs.getInt("INCTAX") + "");
                arrAqBean.setProfessionalTax(rs.getInt("PT"));
                arrAqBean.setRemark(rs.getString("REMARK"));
                arrAqBean.setGpfAccNo(rs.getString("GPF_NO"));
                arrAqBean.setPayTotal(rs.getInt("PAY_TOTAL"));
                arrAqBean.setGpTotal(rs.getInt("GP_TOTAL"));
                arrAqBean.setDaTotal(rs.getInt("DA_TOTAL"));
                arrAqBean.setHraTotal(rs.getInt("HRA_TOTAL"));
                arrAqBean.setOaTotal(rs.getInt("OA_TOTAL"));
                arrAqBean.setIrTotal(rs.getInt("IR_TOTAL"));
                arrAqBean.setOtherRecovery(rs.getString("other_recovery"));
                arrEmpList.add(arrAqBean);
                slNo++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrEmpList;
    }

    @Override
    public ArrAqMastModel getJudiciaryArrear6AcquaintanceData(String aqSlNo) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        int grandTotArr100 = 0;
        ArrAqMastModel arrAqMastBean = new ArrAqMastModel();

        try {
            con = this.repodataSource.getConnection();

            pstmt = con.prepareStatement("select arr_mast.emp_id, emp_name,arr_mast.arrear_percent, arrear_pay, full_arrear_pay, CUR_DESG, aqsl_no, p_month, p_year, inctax, cpf_head, pt, bill_no,EMP_TYPE,CHOICE_DATE, emp_mast.acct_type from arr_mast\n"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id "
                    + " where aqsl_no =? ");
            pstmt.setString(1, aqSlNo);
            res = pstmt.executeQuery();
            if (res.next()) {

                arrAqMastBean.setEmpCode(res.getString("emp_id"));
                arrAqMastBean.setEmpName(res.getString("emp_name"));
                arrAqMastBean.setCurDesg(res.getString("cur_desg"));
                arrAqMastBean.setIncomeTaxAmt(res.getString("inctax"));
                arrAqMastBean.setCpfHead(res.getString("cpf_head"));
                arrAqMastBean.setPt(res.getString("pt"));
                arrAqMastBean.setBillNo(res.getInt("bill_no"));
                arrAqMastBean.setEmpType(res.getString("EMP_TYPE"));
                arrAqMastBean.setChoiceDate(res.getDate("CHOICE_DATE"));
                arrAqMastBean.setAccType(res.getString("acct_type"));
                arrAqMastBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqMastBean.setArrearpay(res.getInt("arrear_pay"));
                arrAqMastBean.setFullArrearpay(res.getInt("full_arrear_pay"));
            }
            int j = 1;
            List arrAqDtlsList = getJudiciaryArrearAquitanceDtls(aqSlNo, j);

            ArrAqDtlsModel obj = null;
            if (arrAqDtlsList != null && arrAqDtlsList.size() > 0) {
                obj = new ArrAqDtlsModel();
                for (int i = 0; i < arrAqDtlsList.size(); i++) {
                    obj = (ArrAqDtlsModel) arrAqDtlsList.get(i);

                    grandTotArr100 = grandTotArr100 + obj.getArrear100();
                    arrAqMastBean.setAqSlNo(aqSlNo);
                }
            }
            arrAqMastBean.setGrandTotArr100(arrAqMastBean.getFullArrearpay());
            arrAqMastBean.setGrandTotArr40(arrAqMastBean.getArrearpay());

            double percent = .1;
            if (arrAqMastBean.getPercentageArraer() == 25) {
                percent = 0.25;
            } else if (arrAqMastBean.getPercentageArraer() == 50) {
                percent = 0.5;
            }

            //arrAqMastBean.setGrandTotArr60(Math.round(grandTotArr100 * percent));
            //arrAqMastBean.setGrandTotArr60(Math.round(arrAqMastBean.getGrandTotArr100() * percent));
            //arrAqMastBean.setGrandTotArr60(Math.round(arrAqMastBean.getGrandTotArr100() - arrAqMastBean.getGrandTotArr40()));
            /*if (arrAqMastBean.getPercentageArraer() == 50) {
             arrAqMastBean.setGrandTotArr60(Math.round(arrAqMastBean.getGrandTotArr100() * percent));
             } else {*/
            arrAqMastBean.setGrandTotArr60(Math.round(arrAqMastBean.getGrandTotArr40()));
            //}

            arrAqMastBean.setArrDetails(arrAqDtlsList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqMastBean;
    }

    public void saveArrdtlsJudiciary6data(List arrAqDtlsList, String aqslno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("INSERT INTO ARR_DTLS (AQSL_NO,P_MONTH,P_YEAR,AD_TYPE,ALREADY_PAID, TO_BE_PAID, DRAWN_VIDE_BILLNO, REF_AQSL_NO,CALC_UNIQUE_NO,BT_ID)VALUES (?,?,?,?,?,?,?,?,?,?) ");
            for (int i = 0; i < arrAqDtlsList.size(); i++) {
                ArrAqDtlsModel arrAqDtlsBean = (ArrAqDtlsModel) arrAqDtlsList.get(i);
                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "PAY");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnPayAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDuePayAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();

                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "GP");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnGpAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDueGpAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();

                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "DA");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnDaAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDueDaAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();

                pstmt.setString(1, aqslno);
                pstmt.setInt(2, arrAqDtlsBean.getPayMonth());
                pstmt.setInt(3, arrAqDtlsBean.getPayYear());
                pstmt.setString(4, "IR");
                pstmt.setInt(5, arrAqDtlsBean.getDrawnIrAmt());
                pstmt.setInt(6, arrAqDtlsBean.getDueIrAmt());
                pstmt.setString(7, arrAqDtlsBean.getDrawnBillNo());
                pstmt.setString(8, arrAqDtlsBean.getRefaqslno());
                pstmt.setInt(9, arrAqDtlsBean.getCalcuniqueno());
                pstmt.setString(10, arrAqDtlsBean.getBtid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int updateArrMastddoRecovryData(int billNo, String aqSlNo, int drAmt) {
        Connection con = null;

        PreparedStatement pstmt = null;
        int resultData = 0;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("update arr_mast set ddo_recovery=? where aqsl_no=? and bill_no=?");
            pstmt.setInt(1, drAmt);
            pstmt.setString(2, aqSlNo);
            pstmt.setInt(3, billNo);
            resultData = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return resultData;
    }

    @Override
    public List getJudiciaryArrearAquitance(int billNo, CommonReportParamBean crb) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet res = null;

        List arrAqList = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            /*pstmt = con.prepareStatement("select emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt from arr_mast "
             + "where bill_no = ? order by emp_name");*/
            pstmt = con.prepareStatement("select acct_type,emp_id, emp_name, cur_desg, aqsl_no, cur_basic, inctax, cpf_head, full_arrear_pay, arrear_pay, pt,other_recovery,ddo_recovery,"
                    + " getarradtotal(AQSL_NO,'PAY') AS PAY_TOTAL,getarradtotal(AQSL_NO,'DA') AS DA_TOTAL,getarradtotal(AQSL_NO,'IR') AS IR_TOTAL, arrear_percent from arr_mast"
                    + " where bill_no = ? order by emp_name");
            pstmt.setInt(1, billNo);
            res = pstmt.executeQuery();
            while (res.next()) {
                ArrAqMastModel aqBean = new ArrAqMastModel();

                aqBean.setEmpCode(res.getString("EMP_ID"));
                aqBean.setEmpName(res.getString("EMP_NAME"));
                aqBean.setCurDesg(res.getString("CUR_DESG"));
                aqBean.setAqSlNo(res.getString("aqsl_no"));
                aqBean.setArrearpay(res.getInt("full_arrear_pay"));
                if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("ARREAR_6_J")) {
                    if (res.getInt("arrear_percent") == 50) {
                        aqBean.setArrear40(Math.round(aqBean.getArrearpay() * 0.5));
                    } else {
                        aqBean.setArrear40(res.getInt("arrear_pay"));
                    }
                } else {
                    aqBean.setArrear40(res.getInt("arrear_pay"));
                }
                aqBean.setCurBasic(res.getInt("cur_basic"));
                aqBean.setIncomeTaxAmt(res.getString("inctax"));

                if (res.getInt("cpf_head") > 0) {
                    aqBean.setCpfHead(res.getString("cpf_head"));
                } else if (res.getInt("cpf_head") == 0) {
                    aqBean.setCpfHead(res.getString("cpf_head"));
                } else if (res.getString("acct_type") != null && res.getString("acct_type").equals("PRAN")) {
                    double cpfAmt = res.getInt("PAY_TOTAL") + res.getInt("DA_TOTAL") + res.getInt("IR_TOTAL");
                    cpfAmt = cpfAmt * 0.1;
                    aqBean.setCpfHead(Math.round(cpfAmt) + "");
                }

                aqBean.setPt(res.getString("pt"));
                aqBean.setOtherRecovery(res.getString("other_recovery"));
                aqBean.setDdoRecovery(res.getString("ddo_recovery"));
                aqBean.setPercentageArraer(res.getInt("arrear_percent"));
                arrAqList.add(aqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrAqList;
    }

    @Override
    public List getArrear25VerificationData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List billList = new ArrayList();

        ArrAqMastModel arrmodel = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select bill_mast.ddo_code,g_bill_status.bill_status,bill_status_id,bill_mast.bill_no,bill_desc,bill_mast.aq_year,bill_mast.aq_month,bill_mast.from_year,\n"
                    + "bill_mast.from_month,bill_mast.to_year,bill_mast.to_month,arr_mast.aqsl_no,arr_mast.arrear_percent,arr_mast.arr_type,bill_mast.vch_no,bill_mast.vch_date from arr_mast\n"
                    + "inner join bill_mast on arr_mast.bill_no=bill_mast.bill_no\n"
                    + "left outer join g_bill_status on bill_mast.bill_status_id=g_bill_status.id\n"
                    + "where emp_id=? and arr_type in ('ARREAR_J','ARREAR_6_J') order by bill_mast.bill_no desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                arrmodel = new ArrAqMastModel();
                arrmodel.setBillNo(rs.getInt("bill_no"));
                arrmodel.setBillDesc(rs.getString("bill_desc"));
                arrmodel.setFromMonth(rs.getString("from_month"));
                arrmodel.setFromYear(rs.getString("from_year"));
                arrmodel.setToMonth(rs.getString("to_month"));
                arrmodel.setToYear(rs.getString("to_year"));
                arrmodel.setAqSlNo(rs.getString("aqsl_no"));
                arrmodel.setPayMonth(rs.getInt("aq_month") + 1);
                arrmodel.setPayYear(rs.getInt("aq_year"));
                arrmodel.setBillstatusid(rs.getInt("bill_status_id"));
                arrmodel.setBillstatus(rs.getString("bill_status"));
                arrmodel.setOffDdo(rs.getString("ddo_code"));
                arrmodel.setVchno(rs.getString("vch_no"));
                if (rs.getString("arr_type").equals("ARREAR_J")) {
                    arrmodel.setArrtype("First 25%");
                } else if (rs.getString("arr_type").equals("ARREAR_6_J")) {
                    arrmodel.setArrtype("Second 25%");
                }
                arrmodel.setPercentageArraer(rs.getInt("arrear_percent"));
                billList.add(arrmodel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billList;
    }

    private String getEmployeeDateOfBirth(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String doegov = "";
        try {
            con = this.repodataSource.getConnection();
            String sql = "SELECT DOE_GOV FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                doegov = rs.getString("DOE_GOV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return doegov;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.services;

import hrms.common.CalendarCommonMethods;
import hrms.dao.payroll.aqmast.AqmastDAO;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.schedule.BillFrontpageDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lenovo
 */
@Service
public class SalaryBillService {

    AqReportDAOImpl aqReportDao;
    PayBillDMPDAO paybillDmpDao;
    BillFrontpageDAO billFrontPageDmpDao;

    public void setAqReportDao(AqReportDAOImpl aqReportDao) {
        this.aqReportDao = aqReportDao;
    }

    public void setPaybillDmpDao(PayBillDMPDAO paybillDmpDao) {
        this.paybillDmpDao = paybillDmpDao;
    }

    public void setBillFrontPageDmpDao(BillFrontpageDAO billFrontPageDmpDao) {
        this.billFrontPageDmpDao = billFrontPageDmpDao;
    }
    

    /*public byte[] getSinglePageByteArray(String billNo) {
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
        BillChartOfAccount billChartOfAccount = aqReportDao.getBillChartOfAccount(billNo);
        BillObjectHead boha = aqReportDao.getBillObjectHeadAndAmt(billNo, crb.getAqyear(), crb.getAqmonth());
        ArrayList scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(billNo, crb.getAqmonth(), crb.getAqyear());
        ArrayList scheduleListTR = billFrontPageDmpDao.getScheduleListWithADCodeTR(billNo, crb.getAqmonth(), crb.getAqyear());
        List oaList = aqReportDao.getAllowanceDetails(billNo, crb.getAqyear(), crb.getAqmonth());

        int spAmt = billFrontPageDmpDao.getSpecialPayAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());
        int irAmt = billFrontPageDmpDao.getIrAmount(Integer.parseInt(billNo), crb.getAqmonth(), crb.getAqyear());
        int payAmt = aqReportDao.getPayAmt(Integer.parseInt(billNo));
        String payHead = billFrontPageDmpDao.getPayHead(Integer.parseInt(billNo));

        String monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
        monthName = monthName + " - " + crb.getAqyear();

        byte[] pdfinbytearray = billFrontPageDmpDao.singlebillFrontPagePDF(monthName, billNo, billChartOfAccount, boha, scheduleList, scheduleListTR, oaList, spAmt, irAmt, payAmt, payHead, crb.getOffcode(), crb.getAqmonth(), crb.getAqyear());
        return pdfinbytearray;
    }*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpQuarterAllotment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSHttpPostClient;
import hrms.common.SMSServices;
import hrms.model.EmpQuarterAllotment.FundSanctionOrderBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Manas
 */
public class SendEquarterSMS implements Runnable {

    private List quarterRepairOrderDetailList = null;

    public SendEquarterSMS(List quarterRepairOrderDetailList) {
        this.quarterRepairOrderDetailList = quarterRepairOrderDetailList;
    }

    @Override
    public void run() {
        Connection conpsql = null;        
        try {
            Class.forName("org.postgresql.Driver");
            conpsql = DriverManager.getConnection("jdbc:postgresql://172.16.1.14:6432/hrmis", "hrmis2", "cmgi");
            //conpsql = DriverManager.getConnection("jdbc:postgresql://192.168.1.19:5432/hrmis", "hrmis2", "cmgi");
            for (int i = 0; i < quarterRepairOrderDetailList.size(); i++) {
                FundSanctionOrderBean fundSanctionOrderBean = (FundSanctionOrderBean) quarterRepairOrderDetailList.get(i);
                System.out.println(fundSanctionOrderBean.getMobileno() + "****" + fundSanctionOrderBean.getSmsstring());
                if (fundSanctionOrderBean.getMobileno() != null && !fundSanctionOrderBean.getMobileno().equals("")) {
                    SMSServices smsServices = new SMSServices(fundSanctionOrderBean.getMobileno(), fundSanctionOrderBean.getSmsstring(),"");
                    //SMSHttpPostClient smhttp = new SMSHttpPostClient(fundSanctionOrderBean.getMobileno(), fundSanctionOrderBean.getSmsstring());
                    //String deliverymsg = smhttp.send_sms();
                    insertSMSData(conpsql, fundSanctionOrderBean.getEmpid(), fundSanctionOrderBean.getSmsstring(), fundSanctionOrderBean.getMobileno(), "Y", "EQUARTER/FUND/FROM_THREAD", fundSanctionOrderBean.getFundid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(conpsql);

        }

    }

    public void insertSMSData(Connection conpsql, String empid, String msg, String mobile, String deliverymsg, String msgtype, int fund_id) {
        PreparedStatement pst = null;
        String startTime = "";
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = conpsql.prepareStatement("INSERT INTO SMS_LOG(MSG_ID,EMP_ID,MESSAGE_TEXT,MESSAGE_TYPE,MOBILE,SENT_ON,STATUS) VALUES(?,?,?,?,?,?,?)");
            pst.setInt(1, CommonFunctions.getMaxCode(conpsql, "SMS_LOG", "MSG_ID"));
            pst.setString(2, empid);
            pst.setString(3, msg);
            pst.setString(4, msgtype);
            pst.setString(5, mobile);
            pst.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(7, deliverymsg);
            pst.executeUpdate();
            pst = conpsql.prepareStatement("UPDATE quarter_fund_allotment SET sms_delivery_status='Y' WHERE fund_id=?");
            pst.setInt(1, fund_id);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
}

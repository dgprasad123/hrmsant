/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.paybill;

import hrms.service.GenerateErrorService;
import static hrms.thread.paybill.CallableTokenArrearBill.threadStatus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Surendra
 */
public class CallableErrorBill implements Runnable {

    public static int threadStatus = 0;

    public static boolean toggle = false;
    GenerateErrorService generateError;

    public void setGenerateError(GenerateErrorService generateError) {
        this.generateError = generateError;
    }

    @Override
    public void run() {

        try {
            threadStatus = 1;
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String startTime = "";
            String endTime = "";
            toggle = true;
            startTime = dateFormat.format(cal.getTime()); // thread start time captured

            String remarks = generateError.downloadFromTreasuryReadXML();
            endTime = dateFormat.format(cal.getTime()); // thread end time captured
            generateError.insertLog(startTime, endTime, remarks);
            threadStatus = 0;
        } catch (Exception e) {
            threadStatus = 0;
            e.printStackTrace();
        } finally {
        }
    }
    
    public int getThreadStatus() {
        return this.threadStatus;
    }

}

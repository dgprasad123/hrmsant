package hrms.thread.paybill;

import hrms.service.GenerateVoucherService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallableVoucherBill implements Runnable {
    
    public static int threadStatus = 0;

    public static boolean toggle = false;
    GenerateVoucherService generateVoucher;

    public void setGenerateVoucher(GenerateVoucherService generateVoucher) {
        this.generateVoucher = generateVoucher;
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

            String remarks = generateVoucher.processVoucher();
            endTime = dateFormat.format(cal.getTime()); // thread end time captured
            generateVoucher.insertLog(startTime, endTime, remarks);
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

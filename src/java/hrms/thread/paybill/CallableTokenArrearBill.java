package hrms.thread.paybill;

import hrms.service.GenerateTokenArrearBillService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallableTokenArrearBill implements Runnable {

    public static int threadStatus = 0;

    public static boolean toggle = false;

    GenerateTokenArrearBillService generateTokenArrearBillService;

    public void setGenerateTokenArrearBillService(GenerateTokenArrearBillService generateTokenArrearBillService) {
        this.generateTokenArrearBillService = generateTokenArrearBillService;
    }
    
    @Override
    public void run() {

        int i = 1;
        try {
            
            threadStatus = 1;
            
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String startTime = "";
            String endTime = "";
            
            startTime = dateFormat.format(cal.getTime()); // thread start time captured
                /* invoke service here*/
            String remarks = generateTokenArrearBillService.updateToken();
            endTime = dateFormat.format(cal.getTime()); // thread end time captured
            generateTokenArrearBillService.insertLog(startTime, endTime, remarks);
            
            threadStatus = 0;
            //Thread.sleep(5000);
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

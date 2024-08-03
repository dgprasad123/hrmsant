package hrms.thread.paybill;

import hrms.service.UploadBillService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallableUploadBill implements Runnable {
    
    public static int threadStatus = 0;

    public static boolean toggle = false;
    
    UploadBillService uploadBillService;

    public void setUploadBillService(UploadBillService uploadBillService) {
        this.uploadBillService = uploadBillService;
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
            String remarks = uploadBillService.uploadThroughSftp();
            endTime = dateFormat.format(cal.getTime()); // thread end time captured
            
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

package hrms.thread.paybill;

import hrms.service.DownloadFilesFromiOTMSService;
import static hrms.thread.paybill.CallableUploadBill.threadStatus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallableDownloadFiles implements Runnable {

    public static int threadStatus = 0;

    public static boolean toggle = false;

    DownloadFilesFromiOTMSService downloadBillService;

    public void setDownloadBillService(DownloadFilesFromiOTMSService downloadBillService) {
        this.downloadBillService = downloadBillService;
    }

    public void run() {
        try {
            threadStatus = 1;

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String startTime = "";
            String endTime = "";
            toggle = true;
            startTime = dateFormat.format(cal.getTime()); // thread start time captured

            /* invoke service here*/
            // String remarks=downloadFromTreasuryReadXML(conn);
            String remarks = downloadBillService.downloadthroughSFTP();
            endTime = dateFormat.format(cal.getTime()); // thread end time captured

            downloadBillService.insertLog(startTime, endTime, remarks);
            
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

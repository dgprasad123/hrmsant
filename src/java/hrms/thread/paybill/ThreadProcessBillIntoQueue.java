/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.paybill;

import hrms.service.ProcessBillIntoQueueService;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Madhusmita
 */
@Service
public class ThreadProcessBillIntoQueue implements Runnable {

    @Autowired
    public ProcessBillIntoQueueService processBillService;

    private int month;
    private int year;
    private String billgroupid;
    private int billno;
    public static int threadstarted = 0;
    private String processValue;
    Calendar cal = Calendar.getInstance();

    @Override
    public void run() {
        System.out.println("process thread running======");
        try {
            threadstarted = 1;
            //month = cal.get(Calendar.MONTH);
            //year = cal.get(Calendar.YEAR);
            processBillService.processIntoPaybiltask(month, year,processValue, billgroupid,billno);            

            System.out.println("process thread stopped::"+threadstarted);

        } catch (Exception e) {
            threadstarted = 0;
            e.printStackTrace();
        }
    }

    public int getThreadStatus() {
        return this.threadstarted;
    }

    public void setThreadValue(int aqmonth, int aqyear,String processValue, String billgroupid, int billno) {
        this.month = aqmonth;
        this.year = aqyear;
        this.billgroupid = billgroupid;
        this.billno=billno;
        this.processValue=processValue;
    }

}

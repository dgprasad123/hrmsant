/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.paybill;

import hrms.model.payroll.paybilltask.PaybillTask;
import hrms.service.ProcessPayBillService;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public class CallableRegularPayBill implements Runnable {

    public static int threadStatus = 0;

    ProcessPayBillService processPayBillService;

    public void setProcessPayBillService(ProcessPayBillService processPayBillService) {
        this.processPayBillService = processPayBillService;
    }

    PaybillTask paybillTask;
    /*
    public CallableRegularPayBill() {
    }

    public CallableRegularPayBill(PaybillTask paybillTask) {
        System.out.println(">>" + paybillTask.getTaskid() + " submitted");
        this.paybillTask = paybillTask;
    }
*/
    @Override
    public void run() {        
        System.out.println(">> started");
        
        System.out.println(">> started ????");
        try {
            ArrayList taskList = processPayBillService.getPayBillTaskList("PAY");
            threadStatus = 1;
            for(int i=0;i<taskList.size();i++){
                paybillTask = (PaybillTask)taskList.get(i);
                Thread.sleep(5000);
                System.out.println("paybillTask:"+paybillTask.getTaskid());
                processPayBillService.processBill(paybillTask);
            }
            //Thread.sleep(5000);
            System.out.println("Slow task executed");
            threadStatus = 0;
            //return "Task finished";
            //processPayBillService.processBill(paybillTask);
        } catch (Exception exe) {
            threadStatus = 0;
            exe.printStackTrace();
        }
        //return status;
    }

    public int getThreadStatus() {
        return this.threadStatus;
    }
}

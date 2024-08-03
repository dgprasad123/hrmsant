/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.paybill;

import hrms.model.payroll.paybilltask.PaybillTask;
import hrms.service.ProcessArrearBillService;
import hrms.service.ProcessPayBillService;
import static hrms.thread.paybill.CallableRegularPayBill.threadStatus;
import java.util.ArrayList;

/**
 *
 * @author Manas
 */
public class CallableArrearPayBill implements Runnable {

    public static int threadStatus = 0;
    ProcessArrearBillService processArrearBillService;
    ProcessPayBillService processPayBillService;

    public void setProcessPayBillService(ProcessPayBillService processPayBillService) {
        this.processPayBillService = processPayBillService;
    }

    public void setProcessArrearBillService(ProcessArrearBillService processArrearBillService) {
        this.processArrearBillService = processArrearBillService;
    }

    PaybillTask paybillTask;

    @Override
    public void run() {
        System.out.println(">> started");
        System.out.println(">> started ????");
        try {
            threadStatus = 1;
            processArrearBillService.updateThreadStatus(threadStatus);
            ArrayList taskList = processPayBillService.getPayBillTaskList("ARREAR");
            
            for (int i = 0; i < taskList.size(); i++) {
                paybillTask = (PaybillTask) taskList.get(i);
                Thread.sleep(5000);
                System.out.println("paybillTask:" + paybillTask.getTaskid());
                processArrearBillService.processPayFixationArrearBill(paybillTask);
            }            
            System.out.println("Slow task executed");
            threadStatus = 0;
            processArrearBillService.updateThreadStatus(threadStatus);
        } catch (Exception exe) {
            threadStatus = 0;
            processArrearBillService.updateThreadStatus(threadStatus);
            exe.printStackTrace();
        }        
    }
    
    public int getThreadStatus() {
        return this.threadStatus;
    }
}

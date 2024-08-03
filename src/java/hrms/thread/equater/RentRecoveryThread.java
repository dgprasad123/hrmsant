/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.equater;

import hrms.common.LogMessage;
import hrms.service.RentRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas Jena
 */
@Service
public class RentRecoveryThread implements Runnable{
    @Autowired
    public RentRecoveryService rentRecoveryService;
    private int aqmonth;
    private int aqyear;
    private String distcode;
    public static int threadStarted = 0;
    public void setThreadValue(int aqmonth, int aqyear, String distcode) {
        this.aqmonth = aqmonth;
        this.aqyear = aqyear;
        this.distcode = distcode;
    }
    @Override
    public void run() {
        try {
            //if (!this.isAlive()) {
            //threadStarted = 1;  
            LogMessage.reset();
            LogMessage.setMessage("aqmonth:"+aqmonth+" aqyear:"+ aqyear+" distcode:"+distcode);
            rentRecoveryService.replicateRentInfo(aqmonth, aqyear, distcode);
            //threadStarted = 0;
            //}
        } catch (Exception e) {
            threadStarted = 0;
            e.printStackTrace();
        }
    }
    
    public int getThreadStatus() {
        return this.threadStarted;
    }
    
    
}

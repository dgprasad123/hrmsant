/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.ag;

import hrms.common.LogMessage;
import hrms.service.LongTermAdvanceService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas Jena
 */
@Service
public class LTAThread implements Runnable {

    @Autowired
    public LongTermAdvanceService ltaService;

    private int aqmonth;
    private int aqyear;
    private String schedule;
    public static int threadStarted = 0;

    @Override
    public void run() {
        
        //ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/WEB-INF/applicationContext*.xml");
        System.out.println(" is running");
        try {
            //if (!this.isAlive()) {
            //threadStarted = 1;
            LogMessage.reset();
            LogMessage.setMessage("aqmonth:"+aqmonth+" aqyear:"+ aqyear+" schedule:"+schedule);
            //ltaService.createXMLFile(aqmonth, aqyear, schedule);
            //ltaService.createTXTFile(aqmonth, aqyear, schedule);
            ltaService.createPDFFile(aqmonth, aqyear, schedule);
            
            System.out.println(" is stopped");
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

    public void setThreadValue(int aqmonth, int aqyear, String schedule) {
        this.aqmonth = aqmonth;
        this.aqyear = aqyear;
        this.schedule = schedule;
    }
    
}

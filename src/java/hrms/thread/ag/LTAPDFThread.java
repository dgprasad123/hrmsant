package hrms.thread.ag;

import hrms.service.LongTermAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LTAPDFThread implements Runnable{
    
    @Autowired
    public LongTermAdvanceService ltaService;

    private int aqmonth;
    private int aqyear;
    private String schedule;
    
    public static int threadStarted = 0;
    
    @Override
    public void run() {
        
        System.out.println(" is running");
        try {
            
            ltaService.createLTAScheduleBulkPDFFileForAG(aqmonth, aqyear, schedule);
            
            System.out.println(" is stopped");
            
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

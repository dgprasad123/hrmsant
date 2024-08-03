package hrms.thread.ag;

import hrms.service.LongTermAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LTAAQReportPDFThread implements Runnable{
    
    @Autowired
    public LongTermAdvanceService ltaService;

    private int aqmonth;
    private int aqyear;
    private String agTrCode;
    
    public static int threadStarted = 0;
    
    @Override
    public void run() {
        
        System.out.println("AQ Report Thread PDF is running");
        try {
            
            ltaService.createLTAAQReportPDFFileForAG(aqmonth, aqyear, agTrCode);
            
            System.out.println("AQ Report Thread is stopped");
            
        } catch (Exception e) {
            threadStarted = 0;
            e.printStackTrace();
        }
    }
    
    public int getThreadStatus() {
        return this.threadStarted;
    }

    public void setThreadValue(int aqmonth, int aqyear, String agTrCode) {
        this.aqmonth = aqmonth;
        this.aqyear = aqyear;
        this.agTrCode = agTrCode;
    }
}

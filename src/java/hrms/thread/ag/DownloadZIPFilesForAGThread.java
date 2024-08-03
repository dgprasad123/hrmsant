package hrms.thread.ag;

import hrms.service.LongTermAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadZIPFilesForAGThread implements Runnable {
    
    @Autowired
    public LongTermAdvanceService ltaService;

    private int vchmonth;
    private int vchyear;
    private String type;
    
    public static int threadStarted = 0;
    
    @Override
    public void run() {
        System.out.println("ZIP Files Thread is running");
        try {
            
            ltaService.DownloadZIPFiles(type,vchyear, vchmonth);
            
            System.out.println("ZIP Files Thread is stopped");
            
        } catch (Exception e) {
            threadStarted = 0;
            e.printStackTrace();
        }
    }
    
    public int getThreadStatus() {
        return this.threadStarted;
    }

    public void setThreadValue(int vchmonth, int vchyear, String type) {
        this.vchmonth = vchmonth;
        this.vchyear = vchyear;
        this.type = type;
    }
}

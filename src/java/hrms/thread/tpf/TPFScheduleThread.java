package hrms.thread.tpf;

import hrms.service.TPFScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPFScheduleThread implements Runnable{
    
    @Autowired
    TPFScheduleService tpfscheduleservice;
    
    private int vchyear;
    private int vchmonth;
    private String trcode;
    
    public static int threadstated = 0;
    
    @Override
    public void run() {
        
        System.out.println("TPF Schedule Thread is running.");
        
        try{
            tpfscheduleservice.createTPFSchedulePDF(vchyear, vchmonth, trcode);
            System.out.println("TPF Schedule Thread is stopped.");
            threadstated = 1;
        }catch(Exception e){
            threadstated = 0;
            e.printStackTrace();
        }        
    }
    
    public int getThreadStatus(){
        return this.threadstated;
    }
    
    public void setThreadValue(int vchyear,int vchmonth,String trcode){
        this.vchyear = vchyear;
        this.vchmonth = vchmonth;
        this.trcode = trcode;
    }
}

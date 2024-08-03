package hrms.thread.leave;

import hrms.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveThread implements Runnable {

    @Autowired
    public LeaveService leaveService;

    private String empid;
    private int taskid;

    @Override
    public void run() {
        System.out.println("Leave Thread is running");
        try {

            leaveService.sendLeaveDatatoOSWAS(empid, taskid);

            //System.out.println("Leave Thread is stopped");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setThreadValue(String empid, int taskid) {
        this.empid = empid;
        this.taskid = taskid;
    }

    /*public static void main(String args[]) {
        LeaveThread leaveThread = new LeaveThread();
        leaveThread.setThreadValue("59002860", 11);
        Thread t = new Thread(leaveThread);
        t.start();
    }*/
}

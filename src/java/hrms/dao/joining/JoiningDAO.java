package hrms.dao.joining;

import hrms.common.Message;
import hrms.model.joining.JoiningForm;
import java.util.List;

public interface JoiningDAO {

    public List getJoiningList(String empid);

    public int getJoiningListCount(String empid);

    public JoiningForm getJoiningData(String empid, int notid, String rlvid,String leaveid,String addl,String jid);

    public void saveJoining(JoiningForm jform,String entDeptCode,String entOffCode,String entSpc,String loginuserid);
    
    public void deleteJoining(JoiningForm jform);
    
    public void saveFieldOfficeData(String empid,String offcode);
    
    public JoiningForm getFieldOfficeData(String empid);
    
    public String saveJoiningSBCorrection(JoiningForm jform,String entDeptCode,String entOffCode,String entSpc,String loginuserid,int sbcorrectionid);
    
    public JoiningForm getJoiningDataSBCorrection(String empid, int notid, String rlvid,String leaveid,String addl,String jid,int sbcorrectionid);
    
    public void approveJoiningSBCorrection(JoiningForm jform,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
    
}

package hrms.controller.heldup;

import hrms.common.Message;
import hrms.dao.heldup.ContractualHeldUpDAO;
import hrms.model.login.LoginUserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class ContractualHelpUpController {
    
    @Autowired
    public ContractualHeldUpDAO contractualHeldupDAO;
    
    @RequestMapping(value = "ContractualEmpList")
    public String getContractualEmployeeList(){
        
        return "/heldup/ContractualHeldUp";
        
    }
    
    @ResponseBody
    @RequestMapping(value = "ContractualEmpListJSON")
    public void getContractualEmployeeListJSON(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("page") int page,@RequestParam("rows") int rows){
        
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        
        List emplist = null;
        int total = 0;
        try{
            emplist = contractualHeldupDAO.getContractualEmpList(lub.getLoginoffcode(),page,rows);
            total = contractualHeldupDAO.getTotalEmployeeCount(lub.getLoginoffcode());
            
            json.put("rows", emplist);
            json.put("total", total);
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "EmpHeldUpForm")
    public void EmpHeldUpForm(HttpServletResponse response,@RequestParam("empid") String empid) throws IOException,JSONException{
        
        response.setContentType("application/json");
        PrintWriter out = null;
        try{
            String tempempid = "{hidempid:"+empid+"}";
            JSONObject job = new JSONObject(tempempid);
            out = response.getWriter();
            out.write(job.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "saveheldupdata")
    public void saveheldupdata(HttpServletResponse response,@RequestParam("hidempid") String empid,@RequestParam("heldupdt") String heldupdate){
        
        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        
        try{
            
            
            msg = contractualHeldupDAO.saveHeldUpData(empid,heldupdate);
            
            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }
    }
}

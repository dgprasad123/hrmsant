package hrms.controller.report.groupwisesanctionedstrength;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.report.groupwisesanctionedstrength.GroupWiseSanctionedStrengthDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GroupWiseSanctionedStrengthController {
    
    @Autowired
    DepartmentDAO departmentDao;
    
    @Autowired
    GroupWiseSanctionedStrengthDAO groupWiseSanctionedStrengthDAO;
    
    @RequestMapping(value = "getDepartmentListWithData")
    public String getDepartmentListWithData(Model model){
        
        List deptlist = null;
        
        try{
            deptlist = groupWiseSanctionedStrengthDAO.getDepartmentWiseData();
            model.addAttribute("deptlist", deptlist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return "report/GroupWiseDepartmentSanctionedStrength";  
    }
    
    @RequestMapping(value = "getOfficeWiseSanctionedPostData")
    public String getOfficeWiseSanctionedPostData(Model model,@RequestParam("deptCode") String deptCode){
        
        List deptlist = null;
        
        try{
            deptlist = groupWiseSanctionedStrengthDAO.getDepartmentWiseData();
            model.addAttribute("deptlist", deptlist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return "report/GroupWiseSanctionedStrength";  
    }
    
    @RequestMapping(value = "getOfficeWiseMenInPosition")
    public String getOfficeWiseMenInPosition(Model model,@RequestParam("deptCode") String deptCode){
        
        List deptlist = null;
        
        try{
            deptlist = groupWiseSanctionedStrengthDAO.getDepartmentWiseData();
            model.addAttribute("deptlist", deptlist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return "report/GroupWiseSanctionedStrength";  
    }
}

package hrms.controller.report.districtWiseEmployeeData;

import hrms.dao.report.districtwiseemployeedata.DistrictWiseEmployeeDataDAO;
import hrms.model.login.LoginUserBean;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class DistrictWiseEmployeeDataController {
    
    @Autowired
    public DistrictWiseEmployeeDataDAO districtWiseEmployeeData;
    
    @RequestMapping(value = "DownloadDistrictWiseEmployeeDataExcel")
    public void downloadDistrictWiseEmployeeDataExcel(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub){
        
        try{
            districtWiseEmployeeData.downloadDistrictWiseEmployeeDataExcel(response, lub.getLogindistrictcode());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
}

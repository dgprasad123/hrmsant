package hrms.controller.payroll.arrear;

import hrms.dao.payroll.arrear.ArrmastDAO;
import hrms.dao.payroll.arrear.BeneficiaryListArrearDAO;
import hrms.model.login.LoginUserBean;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class BeneficiaryListArrearController {
    
    @Autowired
    public BeneficiaryListArrearDAO beneficiaryListArrearDAO;
    
    @RequestMapping(value = "BeneficiaryListArrearExcel")
    public void BankAccountScheduleArrear(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("billNo") String billNo){
        
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        
        try{
            String fileName = "BankStatement_" + lub.getLoginoffcode() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            
            beneficiaryListArrearDAO.downloadBeneficiaryListArrearExcel(out, fileName, workbook, billNo);
            
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        
    }
    
}

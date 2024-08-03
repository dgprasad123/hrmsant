/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.schedule;

import hrms.dao.payroll.schedule.ScheduleDAOImpl;
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

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PayExcelDownloadController {
    @Autowired
    public ScheduleDAOImpl comonScheduleDao;
    @RequestMapping(value = "DownloadBillDetailExcel")
    public void BankAccountScheduleArrear(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("billNo") String billNo){
        
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        
        try{
            String fileName = "BillStatement_" + lub.getLoginoffcode() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            
            comonScheduleDao.downloadPayDetailExcel(out, fileName, workbook, billNo);
            
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        
    }
}

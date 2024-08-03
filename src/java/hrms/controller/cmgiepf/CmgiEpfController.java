package hrms.controller.cmgiepf;

import hrms.dao.cmgiepf.CmgiEpfDAO;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CmgiEpfController {
    
    @Autowired
    public CmgiEpfDAO cmgiepfDAO;
    
    @RequestMapping(value = "CmgiEpfExcel")
    public void cmgiEpfExcel(HttpServletResponse response){
        
        String fileName = "CMGI_EPF.xls";
        try{
            OutputStream out = response.getOutputStream();
            WritableWorkbook workbook = cmgiepfDAO.downloadEPFExcel(out);
            
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        
    }
    
}

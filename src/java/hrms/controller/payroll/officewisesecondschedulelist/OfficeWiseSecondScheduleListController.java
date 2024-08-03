package hrms.controller.payroll.officewisesecondschedulelist;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Message;
import hrms.dao.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleForm;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListBean;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class OfficeWiseSecondScheduleListController {
    
    @Autowired
    public OfficeWiseSecondScheduleListDAO officewisesecondschedulelistDAO;
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @RequestMapping(value = "GetOfficeWiseSecondScheduleEmployeeList")
    public String getOfficeWiseSecondScheduleEmployeeList(){
        
        return "/payroll/OfficeWiseSecondScheduleEmpList";
    }
    
    @RequestMapping(value = "GetOfficeWiseSecondScheduleEmployeeListJSON")
    public void getOfficeWiseSecondScheduleEmployeeListJSON(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("rows") int rows,@RequestParam("page") int page,@RequestParam("biliGrpId") String biliGrpId){
        
        response.setContentType("application/json");
        
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        
        //List emplist = null;
        int total = 0;
        
        OfficeWiseSecondScheduleListBean oBean = null;
        List sectionWiseEmpList = new ArrayList();
        try{
            List sectionIdList = officewisesecondschedulelistDAO.getSectionList(biliGrpId);
            
            for(int i = 0; i < sectionIdList.size(); i++){
                oBean = (OfficeWiseSecondScheduleListBean)sectionIdList.get(i);
                
                String sectionid = oBean.getSectionId();
                
                List tempemplist = officewisesecondschedulelistDAO.getSectionWiseEmpList(sectionid,rows,page);
                
                if(tempemplist != null && tempemplist.size() > 0){
                    Iterator itr = tempemplist.iterator();
                    while(itr.hasNext()){
                        oBean = (OfficeWiseSecondScheduleListBean)itr.next();
                        sectionWiseEmpList.add(oBean);
                    }
                }
                
                int temptotal = officewisesecondschedulelistDAO.getSectionWiseEmpListCount(sectionid);
                total = total + temptotal;
            }
            
            //emplist = officewisesecondschedulelistDAO.getEmployeeList(lub.getOffcode(),biliGrpId,rows,page);
            //total = officewisesecondschedulelistDAO.getEmployeeListCount(lub.getOffcode(),biliGrpId);
            
            json.put("rows",sectionWiseEmpList);
            json.put("total",total);
            
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "editSecondSchedule")
    public void EditSecondSchedule(HttpServletResponse response,@RequestParam("empid") String empid) throws Exception{
        
        response.setContentType("application/json");
        PrintWriter out = null;
        OfficeWiseSecondScheduleForm oForm = null;
        try {

            oForm = officewisesecondschedulelistDAO.getSecondScheduleData(empid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject job = new JSONObject(oForm);
        out = response.getWriter();
        out.write(job.toString());
    }
    
    @RequestMapping(value = "saveSecondScheduleData")
    public void SaveSecondScheduleData(HttpServletResponse response,@ModelAttribute("officeWiseSecondScheduleForm") OfficeWiseSecondScheduleForm officeWiseSecondScheduleForm){
        
        response.setContentType("application/json");
        PrintWriter out = null;
        
        Message msg = null;
        
        try{
            
            msg = officewisesecondschedulelistDAO.saveSecondScheduleData(officeWiseSecondScheduleForm);
            
            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "officeWiseSecondSchedulePDF")
    public void OfficeWiseSecondSchedulePDF(HttpServletResponse response,@RequestParam("empid") String empid){
        
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        PdfWriter writer = null;
        try{
            String[] data = officewisesecondschedulelistDAO.getHeaderData(empid);
            
            response.setHeader("Content-Disposition", "attachment; filename=PAY_REVISION_OPTION_" + empid + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new OfficeWiseHeaderFooter(data[0],data[1]));
            
            document.open();
            
            comonScheduleDao.secondSchedulePDF(document,empid);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            document.close();
        }
    }
	
	@RequestMapping(value = "saveAuthData")
    public void SaveAuthData(HttpServletResponse response,@RequestParam Map<String,String> requestParams) {
        
        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String)requestParams.get("chkEmp");
            String authEmp = (String)requestParams.get("authEmp");
            
            
            
            
            msg = officewisesecondschedulelistDAO.saveRevisionAuthData(chkEmp,authEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.schedule;

import hrms.dao.payroll.schedule.ScheduleDAO;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PeriodicAbsentStmtController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "PeriodicAbsntStmtHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView PeriodicAbsntStmtHTMLController(@RequestParam("billNo") String billNo){

        ModelAndView mav = null;
        List empList = null;
        try{
            mav = new ModelAndView();
            empList = comonScheduleDao.getPeriodicAbsenteeStatementScheduleEmpList(billNo);
             
            mav.addObject("ASEmpList",empList);           
            mav.setViewName("/payroll/schedule/AbsenteeStatemet");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
}

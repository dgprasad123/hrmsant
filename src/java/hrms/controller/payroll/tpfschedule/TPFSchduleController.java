package hrms.controller.payroll.tpfschedule;

import hrms.SelectOption;
import hrms.common.CalendarCommonMethods;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.tpschedule.TPFScheduleDAOImpl;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.tpfschedule.TPFScheduleBean;
import hrms.model.payroll.tpfschedule.TpfTypeBean;
import hrms.thread.tpf.TPFScheduleThread;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TPFSchduleController implements ServletContextAware {
    
    @Autowired
    public TPFScheduleDAOImpl tpfScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @Autowired
    AqReportDAOImpl AqReportDAO;
    
    @Autowired
    public TPFScheduleThread tpfSchedulePDFThread;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "TPFScheduleHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView TPFScheduleHTML(@RequestParam("billNo") String billNo){
        
        ModelAndView mav = null;
        List tpfEmpList = null;
        List tpfAbstract = null;
        int amount = 0;
        String amountString ="";
        try{
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            tpfEmpList = tpfScheduleDao.getEmployeeWiseTPFList(billNo, crb.getAqmonth(), crb.getAqyear());
            tpfAbstract = tpfScheduleDao.getTPFAbstract(billNo, crb.getAqmonth(), crb.getAqyear());
            
            TPFScheduleBean obj = null;
            if(tpfAbstract != null && tpfAbstract.size() > 0){
                obj = new TPFScheduleBean();
                for(int i = 0; i < tpfAbstract.size(); i++){
                    obj = (TPFScheduleBean)tpfAbstract.get(i);
                    
                        amount = Integer.parseInt(obj.getCarryForward1()) - 1;
                        amountString = Numtowordconvertion.convertNumber((int) amount);
                }
            }
            
            mav.addObject("showAmt", amount);
            mav.addObject("showAmtText", amountString);
            
            mav.addObject("billNo", billNo);
            mav.addObject("billDesc", crb.getBilldesc());
            mav.addObject("billMonth", CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth()));
            mav.addObject("billYear", crb.getAqyear());
            mav.addObject("ddodesg", crb.getDdoname());
            mav.addObject("offname", crb.getOfficename());
            
            mav.addObject("TPFList", tpfEmpList);
            mav.addObject("TPFAbstract", tpfAbstract);
            
            mav.addObject("vchNo", crb.getVchNo());
            mav.addObject("vchDate", crb.getVchDate());
            
            mav.setViewName("/payroll/tpfschedule/TPFSchedule");
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    @RequestMapping(value = "downloadQuarterITExcel")
    public void downloadApplicantExcel(HttpServletResponse response,@RequestParam("billNo") String billNo){
        
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        
        try{
            String fileName = "QuarterITExcel_"+billNo+".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            BillConfigObj billConfig = new BillConfigObj();
            billConfig = AqReportDAO.getBillConfig(billNo);            
            tpfScheduleDao.downloadQuarterITExcel(out, fileName, workbook, billNo, billConfig);
            
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        
    } 
    @RequestMapping(value = "TPFSchedulePDF",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView TPFSchedulePDF(@RequestParam("billNo") String billNo){
        
        ModelAndView mav = null;
        List tpfEmpList = null;
        List tpfAbstract = null;
        int amount = 0;
        String amountString ="";
        try{
            mav = new ModelAndView("tpfSchedulePDFView");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            tpfEmpList = tpfScheduleDao.getEmployeeWiseTPFList(billNo, crb.getAqmonth(), crb.getAqyear());
            tpfAbstract = tpfScheduleDao.getTPFAbstract(billNo, crb.getAqmonth(), crb.getAqyear());
            
            TPFScheduleBean obj = null;
            if(tpfAbstract != null && tpfAbstract.size() > 0){
                obj = new TPFScheduleBean();
                for(int i = 0; i < tpfAbstract.size(); i++){
                    obj = (TPFScheduleBean)tpfAbstract.get(i);
                    
                        amount = Integer.parseInt(obj.getCarryForward1()) - 1;
                        amountString = Numtowordconvertion.convertNumber((int) amount);
                }
            }
            
            mav.addObject("showAmt", amount);
            
            mav.addObject("showAmtText", amountString);
            mav.addObject("billNo", billNo);
            mav.addObject("billDesc", crb.getBilldesc());
            mav.addObject("billMonth", CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth()));
            mav.addObject("billYear", crb.getAqyear());
            mav.addObject("ddodesg", crb.getDdoname());
            mav.addObject("offname", crb.getOfficename());
            mav.addObject("TPFList", tpfEmpList);
            mav.addObject("TPFAbstract", tpfAbstract);
           
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "TPFScheduleBulkPDFCOA")
    public ModelAndView LTAScheduleBulkPDFforAG(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        ModelAndView mav = new ModelAndView();

        SelectOption so = new SelectOption();
        List yearlist = new ArrayList();
        try {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            so.setLabel(year + "");
            so.setValue(year + "");
            yearlist.add(so);

            for (int i = 0; i < 4; i++) {
                int tempyear = year - 1;
                year = tempyear;
                so = new SelectOption();
                so.setLabel(year + "");
                so.setValue(year + "");
                yearlist.add(so);
            }
            mav.addObject("yearlist", yearlist);
            mav.setViewName("payroll/tpfschedule/CreateTPFScheduleBulkPDFCOA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "createTPFScheduleBulkPDFforCOA")
    public String createTPFScheduleBulkPDFforCOA(@ModelAttribute("billBrowserForm") BillBrowserbean bbbean) {

        try {
            if (tpfSchedulePDFThread.getThreadStatus() == 0) {                
                tpfSchedulePDFThread.setThreadValue(bbbean.getSltYear(),bbbean.getSltMonth(), null);
                Thread t = new Thread(tpfSchedulePDFThread);
                t.start();
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/TPFScheduleBulkPDFCOA.htm";
    }
}

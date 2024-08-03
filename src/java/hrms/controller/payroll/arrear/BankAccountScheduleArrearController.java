package hrms.controller.payroll.arrear;

import hrms.dao.payroll.arrear.BankAccountScheduleArrearDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.BankAcountScheduleBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BankAccountScheduleArrearController {
    
    @Autowired
    public BankAccountScheduleArrearDAO bankAccountScheduleArrearDAO;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @RequestMapping(value = "BankAccountScheduleArrear")
    public ModelAndView BankAccountScheduleArrear(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        List empList = null;
        List empList1 = null;
        String total = "";
        String totalDdo = "";
        int netAmount = 0;
        int totRel = 0;
        int othDepo = 0;
        BankAcountScheduleBean bankAccBean=null;
        try{
            mav = new ModelAndView();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            bankAccBean = bankAccountScheduleArrearDAO.getBankAcountScheduleHeaderDetails(billNo);
            empList = bankAccountScheduleArrearDAO.getBankAcountScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth(),crb.getTypeofBill());
            //empList1 = comonScheduleDao.getBankNameScheduleList(billNo, crb.getAqyear(), crb.getAqmonth());
            
            BankAcountScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (BankAcountScheduleBean) empList.get(i);
                    netAmount = netAmount + Integer.parseInt(obj.getNetAmount());
                }
            }
        
            mav.addObject("BankAcHeader", bankAccBean);     
            mav.addObject("BankAccList", empList); 
            mav.addObject("NetTotal", netAmount);
            mav.setViewName("/payroll/arrear/BankAccountScheduleArrear");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
}

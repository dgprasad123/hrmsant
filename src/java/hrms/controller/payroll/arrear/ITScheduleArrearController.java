package hrms.controller.payroll.arrear;

import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.arrear.ITScheduleArrearDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.arrear.ITScheduleArrearBean;
import hrms.model.payroll.schedule.ItScheduleBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ITScheduleArrearController {

    @Autowired
    public ITScheduleArrearDAO itScheduleArrearDAO;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @Autowired
    public ScheduleDAO comonScheduleDao;

    @RequestMapping(value = "ITScheduleArrear")
    public ModelAndView getITScheduleArrearData(@RequestParam("billNo") String billNo) {
        
        ModelAndView mav = null;
        
        ItScheduleBean itBean = null;
        List itEmpList = null;

        double dedutAmt = 0.0;
        double totDedutAmt = 0.0;
        String totalFig = null;
        try {
            mav = new ModelAndView();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            itBean = itScheduleArrearDAO.getITScheduleHeaderDetails(billNo, "IT");
            itEmpList = itScheduleArrearDAO.getITScheduleEmployeeList(billNo, crb.getAqmonth(), crb.getAqyear());

            ITScheduleArrearBean obj = null;
            if (itEmpList != null && itEmpList.size() > 0) {
                obj = new ITScheduleArrearBean();
                for (int i = 0; i < itEmpList.size(); i++) {
                    obj = (ITScheduleArrearBean) itEmpList.get(i);
                    dedutAmt = Double.parseDouble(obj.getEmpDedutAmount()+"");
                    totDedutAmt = totDedutAmt + dedutAmt;
                    totalFig = Numtowordconvertion.convertNumber((int) totDedutAmt);
                }
            }

            mav.addObject("offName", comonScheduleDao.getCommonReportParameter(billNo).getOfficeen());
            mav.addObject("ITEmpList", itEmpList);
            mav.addObject("Schedule", "IT");
            mav.addObject("ITHeader", itBean);
            mav.addObject("DedutAmount", totDedutAmt);
            mav.addObject("TotalFig", totalFig);
            mav.setViewName("/payroll/arrear/ITScheduleArrear");
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }

}

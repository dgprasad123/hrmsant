package hrms.controller.payroll.arrear;

import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.arrear.GPFArrearDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.GPFScheduleBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GPFScheduleArrearController {

    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public GPFArrearDAO gpfArrearDAO;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    @RequestMapping(value = "GPFScheduleArrear")
    public ModelAndView GPFScheduleArrear(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List gpfTypeList = null;
        List gpfAbstractList = null;
        GPFScheduleBean gpfHeader = null;
        GPFScheduleBean obj2 = null;
        double sal = 0.0;
        double totAmt = 0.0;
        String totalFig = null;

        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            gpfHeader = gpfArrearDAO.getGPFScheduleHeaderDetails(billNo);

            gpfTypeList = gpfArrearDAO.getEmpGpfDetails(billNo);
            //gpfAbstractList = gpfArrearDAO.getGPFScheduleAbstractList(billNo, crb.getAqmonth(), crb.getAqyear());

            GPFScheduleBean obj = null;
            if (gpfTypeList != null && gpfTypeList.size() > 0) {
                for (int i = 0; i < gpfTypeList.size(); i++) {
                    obj = (GPFScheduleBean) gpfTypeList.get(i);
                    sal = Double.parseDouble(obj.getTotalReleased()+"");
                    
                    totAmt = totAmt + sal;
                    totalFig = Numtowordconvertion.convertNumber((int) totAmt);
                }
            }

            mav.addObject("GPFHeader", gpfHeader);
            mav.addObject("GPFTypeList", gpfTypeList);
            //mav.addObject("GPFAbstractList", gpfAbstractList);
            mav.addObject("TotAmt", totAmt);
            mav.addObject("TotFig", totalFig);

            mav.setViewName("/payroll/arrear/GPFScheduleArrear");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}

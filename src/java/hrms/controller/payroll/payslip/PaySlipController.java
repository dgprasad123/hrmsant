package hrms.controller.payroll.payslip;

import hrms.common.Numtowordconvertion;
import hrms.dao.deputation.DeputationDAO;
import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PaySlipController {

    @Autowired
    public PaySlipDAO payslipDao;

    @Autowired
    public DeputationDAO deputationDAO;

    @RequestMapping(value = "PaySlipList")
    public String PaySlipList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("payslipform") PaySlipListBean payslipform) {

        if (payslipform.getEmpId() != null && !payslipform.getEmpId().equals("")) {
            model.addAttribute("empid", payslipform.getEmpId());
        } else {
            model.addAttribute("empid", lub.getLoginempid());
        }
        return "/payroll/payslip/PaySlipList";

    }

    @RequestMapping(value = "ClickedOnEmployeePaySlipList")
    public String PaySlipList(Model model, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payslipform") PaySlipListBean payslipform) {

        model.addAttribute("empid", selectedEmpObj.getEmpId());
        return "/payroll/payslip/PaySlipList";

    }

    @ResponseBody
    @RequestMapping(value = "GetPaySlipListJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void GetPaySlipListJSON(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");

        JSONObject json = new JSONObject();
        PrintWriter out = null;

        List emppayslip = null;
        try {
            String empid = requestParams.get("empid");
            if ((requestParams.get("year") != null && !requestParams.get("year").equals(""))
                    && (requestParams.get("month") != null && !requestParams.get("month").equals(""))) {

                int year = Integer.parseInt(requestParams.get("year"));
                int month = Integer.parseInt(requestParams.get("month"));

                String billNo = payslipDao.getTokenGeneratedBillNo(empid, year, month);

                if (billNo != null && !billNo.equals("")) {
                    emppayslip = payslipDao.getPaySlip(empid, year, month);
                }
            }
            json.put("total", 1);
            json.put("rows", emppayslip);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetPaySlip")
    public ModelAndView getPaySlip(Model model, @RequestParam Map<String, String> requestParams, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("payslipform") PaySlipListBean payslipform) {

        List emppayslip = null;

        ModelAndView mav = new ModelAndView();
        try {

            if ((payslipform.getSltYear() != null && !payslipform.getSltYear().equals(""))
                    && (payslipform.getSltMonth() != null && !payslipform.getSltMonth().equals(""))) {

                String empid = payslipform.getEmpId();
                int year = Integer.parseInt(payslipform.getSltYear());
                int month = Integer.parseInt(payslipform.getSltMonth());

                String billNo = payslipDao.getTokenGeneratedBillNo(empid, year, month);
                //System.out.println("lub.getLoginAsForeignbody():" + lub.getLoginAsForeignbody() + ":" + billNo);

                if (billNo != null && !billNo.equals("")) {
                    if (lub.getLoginAsForeignbody() != null && lub.getLoginAsForeignbody().equals("Y")) {
                        emppayslip = payslipDao.getPaySlipForeignBody(empid, year, month, Integer.parseInt(billNo));
                    } else {
                        emppayslip = payslipDao.getPaySlip(empid, year, month);
                    }

                    //model.addAttribute("emppayslip", emppayslip);
                }
            }
            payslipform.setIsforeignBody(lub.getLoginAsForeignbody());
            mav = new ModelAndView("/payroll/payslip/PaySlipList", "payslipform", payslipform);
            mav.addObject("emppayslip", emppayslip);
            DeputationDataForm dform = deputationDAO.getEmpDeputation(payslipform.getEmpId());
            mav.addObject("employeeDetails", dform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PaySlipDetail")
    public String PaySlipDetail(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams, @ModelAttribute("payslipform") PaySlipListBean payslipform) {
        String empid = requestParams.get("empid");
        String aqslno = requestParams.get("aqlsno");
        int year = 0;
        int month = 0;
        if (payslipform.getSltYear() != null && !payslipform.getSltYear().equals("")) {
            year = Integer.parseInt(payslipform.getSltYear());
        }
        if (payslipform.getSltMonth() != null && !payslipform.getSltMonth().equals("")) {
            month = Integer.parseInt(payslipform.getSltMonth());
        }

        String path = "";

        PaySlipDetailBean pbeandetail = null;
        ADDetails ad = null;

        ADDetails[] allowancelist = null;
        ADDetails[] deductionlist = null;
        List privateDeductionlist = null;
        List loanList = null;

        double totalAllowance = 0;
        double totalDeduction = 0;
        double totalPrivateDeduction = 0;
        double totalLoan = 0;

        int gross = 0;
        int net = 0;
        int netded = 0;
        try {
            pbeandetail = payslipDao.getEmployeeData(aqslno, year, month);

            allowancelist = payslipDao.getAllowanceDeductionList(aqslno, "A", year, month);
            for (int i = 0; i < allowancelist.length; i++) {
                ad = (ADDetails) allowancelist[i];
                totalAllowance = ad.getAdAmount();
            }

            deductionlist = payslipDao.getAllowanceDeductionList(aqslno, "D", year, month);
            for (int i = 0; i < deductionlist.length; i++) {
                ad = (ADDetails) deductionlist[i];
                totalDeduction = ad.getAdAmount();
            }

            privateDeductionlist = payslipDao.getPrivateDedeuctionList(aqslno, year, month);
            for (int i = 0; i < privateDeductionlist.size(); i++) {
                ad = (ADDetails) privateDeductionlist.get(i);
                totalPrivateDeduction = ad.getAdAmount();
            }

            loanList = payslipDao.getLoanList(aqslno, year, month);
            for (int i = 0; i < loanList.size(); i++) {
                ad = (ADDetails) loanList.get(i);
                totalLoan = ad.getAdAmount();
            }

            model.addAttribute("empdata", pbeandetail);
            model.addAttribute("allowancelist", allowancelist);
            model.addAttribute("deductionlist", deductionlist);
            model.addAttribute("privateDeductionlist", privateDeductionlist);
            model.addAttribute("loanList", loanList);

            model.addAttribute("totalAllowance", totalAllowance);
            model.addAttribute("totalDeduction", totalDeduction + totalLoan);
            model.addAttribute("totalPrivateDeduction", totalPrivateDeduction);
            model.addAttribute("totalLoan", totalLoan);

            if (pbeandetail.getCurBasic() != null && !pbeandetail.getCurBasic().equals("")) {
                gross = Integer.parseInt(pbeandetail.getCurBasic()) + (int) totalAllowance;
                net = gross - ((int) totalDeduction + (int) totalLoan);
                netded = net - (int) totalPrivateDeduction;
            }

            model.addAttribute("gross", gross);
            model.addAttribute("netded", netded);
            model.addAttribute("netAmtWords", Numtowordconvertion.convertNumber(netded));

            path = "/payroll/payslip/PaySlipDetail";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "paySlipPdf.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView paySlipPdfView(@RequestParam Map<String, String> requestParams) {
        String empid = requestParams.get("empid");
        int year = Integer.parseInt(requestParams.get("aqyear"));
        int month = Integer.parseInt(requestParams.get("aqmonth"));
        ModelAndView mv = new ModelAndView();
        String aqslno = payslipDao.getAQSLNo(empid, year, month);
        PaySlipDetailBean pbeandetail = payslipDao.getEmployeeData(aqslno, year, month);
        ADDetails[] allowancelist = payslipDao.getAllowanceDeductionList(aqslno, "A", year, month);
        ADDetails[] deductionlist = payslipDao.getAllowanceDeductionList(aqslno, "D", year, month);
        List privateDeductionlist = payslipDao.getPrivateDedeuctionList(aqslno, year, month);
        List loanList = payslipDao.getLoanList(aqslno, year, month);

        mv.addObject("empdata", pbeandetail);
        mv.addObject("allowancelist", allowancelist);
        mv.addObject("deductionlist", deductionlist);
        mv.addObject("privateDeductionlist", privateDeductionlist);
        mv.addObject("loanList", loanList);
        mv.setViewName("paySlipPdfView");
        return mv;
    }

    @RequestMapping(value = "DeputationEmployeePaySlipList")
    public ModelAndView DeputationEmployeePaySlipList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean loginbean, @RequestParam("empid") String empid,
            @ModelAttribute("payslipform") PaySlipListBean payslipform) {

        ModelAndView mav = new ModelAndView();
        try {

            payslipform.setEmpId(empid);            
            payslipform.setIsforeignBody(loginbean.getLoginAsForeignbody());
            mav = new ModelAndView("/payroll/payslip/PaySlipList", "payslipform", payslipform);
            DeputationDataForm dform = deputationDAO.getEmpDeputation(empid);
            mav.addObject("employeeDetails", dform);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
     @RequestMapping(value = "DeputationPaySlipContribution")
    public ModelAndView paySlipContribution(Model model, @RequestParam("empid") String empid, @ModelAttribute("contribution") PaySlipListBean payslipform) {
        ModelAndView mav = new ModelAndView();
        try {
            DeputationDataForm employeeDetails = deputationDAO.getEmpDeputation(empid);
            mav.addObject("employeeDetails", employeeDetails);
            mav.setViewName("/payroll/payslip/Contributiondetails");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
     @RequestMapping(value = "PayslipContribution")
    public ModelAndView getContributionDetails(Model model, @RequestParam Map<String, String> requestParams, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("contribution") PaySlipListBean contribution) {

        ModelAndView mav = new ModelAndView();
        try {
            if ((contribution.getSltYear() != null && !contribution.getSltYear().equals(""))
                    && (contribution.getSltMonth() != null && !contribution.getSltMonth().equals(""))) {
                String empid = contribution.getEmpId();
                int year = Integer.parseInt(contribution.getSltYear());
                int month = Integer.parseInt(contribution.getSltMonth());
                PaySlipDetailBean contributionDetails = payslipDao.getContributionDetails(empid, year, month);

                contribution.setIsforeignBody(lub.getLoginAsForeignbody());
                mav = new ModelAndView("/payroll/payslip/Contributiondetails", "contribution", contribution);
                mav.addObject("contributionDetails", contributionDetails);
                DeputationDataForm employeeDetails = deputationDAO.getEmpDeputation(contribution.getEmpId());
                mav.addObject("employeeDetails", employeeDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }



}

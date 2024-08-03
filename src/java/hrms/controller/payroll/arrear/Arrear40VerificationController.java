package hrms.controller.payroll.arrear;

import hrms.dao.payroll.arrear.ArrmastDAO;
import hrms.model.payroll.arrear.ArrAqMastModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Arrear40VerificationController {

    @Autowired
    public ArrmastDAO arrmastDAO;

    @RequestMapping(value = "Arrear40Verification")
    public String getArrear40Verification(@ModelAttribute("command") ArrAqMastModel arrmodel) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/Arrear40Verification";
    }
    
    @RequestMapping(value = "Arrear25Verification")
    public String getArrear25Verification(@ModelAttribute("command") ArrAqMastModel arrmodel) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/Arrear25Verification";
    }

    @RequestMapping(value = "Arrear40VerificationDetailData", params = "btnArrear40=Get Data")
    public String getArrear40VerificationDetailData(Model model, @ModelAttribute("command") ArrAqMastModel arrmodel) {
        try {
            String empid = arrmodel.getEmpCode();
            List billList = arrmastDAO.getArrear40VerificationData(empid);
            model.addAttribute("billList", billList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/Arrear40Verification";
    }
    
      @RequestMapping(value = "Arrear25VerificationDetailData", params = "btnArrear25=Get Data")
    public String getArrear25VerificationDetailData(Model model, @ModelAttribute("command") ArrAqMastModel arrmodel) {
        try {
            String empid = arrmodel.getEmpCode();
            List billList = arrmastDAO.getArrear25VerificationData(empid);
            model.addAttribute("billList", billList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/Arrear25Verification";
    }

    @RequestMapping(value = "Arrear40VerificationDetailData", params = "btnArrear40=Lock")
    public String lockArrear40VerificationDetailData(@ModelAttribute("command") ArrAqMastModel arrmodel, @RequestParam("chkBill") String chkBill) {

        try {
            arrmastDAO.lockArrear40Bill(chkBill);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/arrear/Arrear40Verification";
    }

    @RequestMapping(value = "DeleteArrear40Data")
    public String deleteArrear40Data(ModelMap model, @ModelAttribute("command") ArrAqMastModel arrmodel, @RequestParam("billNo") String billNo, @RequestParam("empid") String empid) {

        try {
            arrmastDAO.deleteArrear40Bill(billNo, empid);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/Arrear40VerificationDetailData.htm?btnArrear40=Get Data&empCode=" + empid;
    }
    
     @RequestMapping(value = "DeleteArrear25Data")
    public String deleteArrear25Data(ModelMap model, @ModelAttribute("command") ArrAqMastModel arrmodel, @RequestParam("billNo") String billNo, @RequestParam("empid") String empid) {

        try {
            arrmastDAO.deleteArrear25Bill(billNo, empid);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/Arrear25Verification.htm?btnArrear25=Get Data&empCode=" + empid;
    }

}

package hrms.controller.payroll.managePvtDeduction;

import hrms.dao.master.BankDAO;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.payroll.managePvtDeduction.ManagePvtDeductionDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionBean;
import hrms.model.payroll.managePvtDeduction.ManagePvtDeductionForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagePvtDeductionController {

    @Autowired
    ManagePvtDeductionDAO managePvtDeductionDAO;

    @Autowired
    public BankDAO bankDAO;

    @Autowired
    public BranchDAO branchDAO;
    
    @Autowired
    DistrictDAO districtDAO;
    
    @Autowired
    OfficeDAO officeDao;
    
    @RequestMapping(value = "GetPvtDeductionData")
    public String GetPvtDeductionData(Model model, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform, @RequestParam("billNo") String billNo) {

        try {
            pvtdednform.setBillNo(Integer.parseInt(billNo));
            
            ArrayList pvtlist = managePvtDeductionDAO.getCurrentDDOData(billNo);
            model.addAttribute("pvtdednlist", pvtlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/managePvtDeduction/ManagePvtDeductionList";
    }

    @RequestMapping(value = "AddPvtDeductionAccount",params = {"btnListAccount=Add More Account"})
    public ModelAndView AddPvtDeductionAccount(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        ModelAndView mav = new ModelAndView();
        
        try {
            pvtdednform.setDdoAmount(pvtdednform.getDdoAmount());
            
            double ddoAmount = managePvtDeductionDAO.getDDOPvtDednAmount(pvtdednform.getBillNo());
            double addedTotalAmount = managePvtDeductionDAO.getAddedAccountDataAmount(pvtdednform.getBillNo());
            double restAmount = ddoAmount - addedTotalAmount;
            
            mav = new ModelAndView("/payroll/managePvtDeduction/AddMoreAccountPvtDeduction","command",pvtdednform);
            
            mav.addObject("restAmount", restAmount);
            
            ArrayList districtList = districtDAO.getDistrictList();
            mav.addObject("districtList", districtList);
            
            List bankList = bankDAO.getBankList();
            mav.addObject("bankList", bankList);
            
            /*List branchList = branchDAO.getBranchList(emp.getSltBank());
            mav.addObject("branchList", branchList);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "AddPvtDeductionAccount",params = {"btnListAccount=Back"})
    public String backPvtDeductionAccountList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        String path = "";
        try {
            String monthyear = managePvtDeductionDAO.getCurrentBillMonthYear(pvtdednform.getBillNo());
            String[] monthyearArr = monthyear.split("-");
            String typeOfBill = monthyearArr[0];
            int month = Integer.parseInt(monthyearArr[1]);
            int year = Integer.parseInt(monthyearArr[2]);
            //path = "redirect:/billBrowserAction.htm";
            path = "redirect:/getPayBillList.htm?txtbilltype="+typeOfBill+"&sltMonth="+month+"&sltYear="+year;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    @RequestMapping(value = "savePvtDeductionAccount",params = {"btnAddAccount=Save"})
    public String savePvtDeductionAccount(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        String path = "";
        try {
            managePvtDeductionDAO.saveManagePvtDeductionData(pvtdednform);
            path = "redirect:/GetPvtDeductionData.htm?billNo="+pvtdednform.getBillNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    @RequestMapping(value = "savePvtDeductionAccount",params = {"btnAddAccount=Delete"})
    public String deletePvtDeductionAccount(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        String path = "";
        try {
            managePvtDeductionDAO.deleteManagePvtDeductionData(Integer.parseInt(pvtdednform.getPvtdednid()));
            path = "redirect:/GetPvtDeductionData.htm?billNo="+pvtdednform.getBillNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    @RequestMapping(value = "savePvtDeductionAccount",params = {"btnAddAccount=Back"})
    public String backPvtDeductionAccount(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        String path = "";
        try {
            path = "redirect:/GetPvtDeductionData.htm?billNo="+pvtdednform.getBillNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    @ResponseBody
    @RequestMapping(value = "getDistrictWiseDDOList")
    public void getDistrictWiseDDOList(HttpServletRequest request, HttpServletResponse response,@RequestParam("distCode") String distCode) throws IOException {
        
        response.setContentType("application/json");
        PrintWriter out = null;
        
        List ddoList = officeDao.getDistrictWiseDDOList(distCode);
        
        JSONArray json = new JSONArray(ddoList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @RequestMapping(value = "editManagePvtDednData")
    public ModelAndView editManagePvtDednData(@ModelAttribute("command") ManagePvtDeductionForm pvtdednform) {
        
        ModelAndView mav = null;
        try {
            pvtdednform = managePvtDeductionDAO.editManagePvtDedData(pvtdednform,Integer.parseInt(pvtdednform.getPvtdednid()));
            
            mav = new ModelAndView("/payroll/managePvtDeduction/AddMoreAccountPvtDeduction","command",pvtdednform);
            
            ArrayList districtList = districtDAO.getDistrictList();
            mav.addObject("districtList", districtList);
            
            List ddoList = officeDao.getDistrictWiseDDOList(pvtdednform.getSltDistrict());
            mav.addObject("ddoList", ddoList);
            
            List bankList = bankDAO.getBankList();
            mav.addObject("bankList", bankList);
            
            List branchList = branchDAO.getBranchList(pvtdednform.getSltBank());
            mav.addObject("branchList", branchList);
            
            int billNo = managePvtDeductionDAO.getBillNo(Integer.parseInt(pvtdednform.getPvtdednid()));
            
            pvtdednform.setBillNo(billNo);
            
            double ddoAmount = managePvtDeductionDAO.getDDOPvtDednAmount(billNo);
            double addedTotalAmount = managePvtDeductionDAO.getAddedAccountDataAmount(billNo);
            double editEnteredAmount = Double.parseDouble(pvtdednform.getAmount());
            double restAmount = ddoAmount - addedTotalAmount;
            restAmount = restAmount + editEnteredAmount;
            mav.addObject("restAmount", restAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

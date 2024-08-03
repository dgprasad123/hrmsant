package hrms.controller.transfer;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.transfer.TransferContractualDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.transfer.TransferForm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class TransferContractualController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public TransferContractualDAO transferContractualDAO;
    
    @Autowired
    public DistrictDAO districtDAO;

    @RequestMapping(value = "TransferContractualList")
    public ModelAndView TransferList(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("transferForm") TransferForm transferForm, HttpServletRequest request) {
        List transferlist = null;
        ModelAndView mav = null;
        try {
            transferForm.setEmpid(selectedEmpObj.getEmpId());
            transferlist = transferContractualDAO.getTransferContractualList(transferForm.getEmpid());
            mav = new ModelAndView("/transfer/TransferContractualList", "transferForm", transferForm);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newTransferContractual")
    public ModelAndView NewTransfer(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("transferForm") TransferForm transferForm) throws IOException {
        ModelAndView mav = null;
        try {
            mav = new ModelAndView("/transfer/NewTransferContractual", "transferForm", transferForm);

            mav.addObject("postnomenclature", transferContractualDAO.getPostNomenclature(selectedEmpObj.getEmpId()));

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveTransferContractual", params = {"btnTransferCntr=Transfer"})
    public String saveTransferContractual(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("transferForm") TransferForm transferform) throws ParseException {

        try {
            transferContractualDAO.saveTransferContractual(transferform, lub.getLoginempid(), selectedEmpObj.getEmpId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/TransferContractualList.htm";
    }

    @RequestMapping(value = "saveTransferContractual", params = {"btnTransferCntr=Update Transfer"})
    public String updateTransferContractual(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("transferForm") TransferForm transferform) throws ParseException {

        try {
            transferContractualDAO.updateTransferContractual(transferform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/TransferContractualList.htm";
    }
    
    @RequestMapping(value = "saveTransferContractual", params = {"btnTransferCntr=Back"})
    public String backTransferContractual() throws ParseException {
        
        return "redirect:/TransferContractualList.htm";
    }

    @RequestMapping(value = "editTransferContractual")
    public ModelAndView editTransferContractual(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        TransferForm trform = new TransferForm();

        ModelAndView mav = null;

        try {
            trform.setEmpid(u.getEmpId());

            String transferId = requestParams.get("transferId");

            trform = transferContractualDAO.getEmpTransferData(trform, Integer.parseInt(transferId));

            trform.setEmpid(u.getEmpId());
            trform.setTransferId(transferId);

            mav = new ModelAndView("/transfer/NewTransferContractual", "transferForm", trform);

            mav.addObject("postnomenclature", trform.getPostedPostName());

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(trform.getHidPostedDeptCode());
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}

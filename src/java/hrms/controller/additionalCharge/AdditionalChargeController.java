package hrms.controller.additionalCharge;

import hrms.dao.additionalCharge.AdditionalChargeDAO;
import hrms.dao.joining.JoiningDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.additionalCharge.AdditionalCharge;
import hrms.model.joining.JoiningForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class AdditionalChargeController {

    @Autowired
    public AdditionalChargeDAO additionalChargeDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public JoiningDAO joiningDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public ServiceBookLanguageDAO sbDAO;

    @RequestMapping(value = "AdditionalCharge")
    public ModelAndView AdditionalChargeList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionalChargeForm") AdditionalCharge additionChargeForm) {

        ModelAndView mav = null;
        List additionalchargelist = null;
        try {
            additionalchargelist = additionalChargeDAO.findAllAdditionalCharge(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/additionalCharge/AdditionalCharge", "additionalChargeForm", additionChargeForm);

            mav.addObject("additionalchargelist", additionalchargelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newAdditionalCharge")
    public ModelAndView NewAdditionalCharge(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionalChargeForm") AdditionalCharge additionChargeForm) throws IOException {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("/additionalCharge/NewAdditionalCharge", "additionalChargeForm", additionChargeForm);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAdditionalChargeData", params = {"btnAdCharge=Save Additional Charge"})
    public ModelAndView saveAdditionalChargeData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm) throws ParseException {

        ModelAndView mav = null;

        NotificationBean nb = new NotificationBean();
        JoiningForm joinform = new JoiningForm();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        List additionalchargelist = null;
        try {

            nb.setNottype("ADDITIONAL_CHARGE");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(additionChargeForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(additionChargeForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(additionChargeForm.getHidAuthDeptCode());
            nb.setSancOffCode(additionChargeForm.getHidAuthOffCode());
            nb.setSancAuthCode(additionChargeForm.getAuthSpc());
            nb.setEntryDeptCode(lub.getLogindeptcode());
            nb.setEntryOffCode(lub.getLoginoffcode());
            nb.setEntryAuthCode(lub.getLoginspc());
            nb.setNote(additionChargeForm.getNote());

            joinform.setNotType("ADDITIONAL_CHARGE");
            joinform.setJempid(selectedEmpObj.getEmpId());
            joinform.setJoiningDt(additionChargeForm.getTxtJoinDt());
            joinform.setSltJoiningTime(additionChargeForm.getSltJoinTime());
            joinform.setHidDeptCode(additionChargeForm.getHidPostedDeptCode());
            joinform.setHidOffCode(additionChargeForm.getHidPostedOffCode());
            joinform.setSltFieldOffice(additionChargeForm.getSltPostedFieldOff());
            joinform.setJspc(additionChargeForm.getPostedspc());
            //joinform.setHidAddition("Y");
            joinform.setNote(additionChargeForm.getNote());
            joinform.setRadpostingauthtype("GOO");
            if (additionChargeForm.getChkNotSBPrint() != null && additionChargeForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
                joinform.setHidAddition("N");
            } else {
                nb.setIfVisible("Y");
                joinform.setHidAddition("Y");
            }

            if (additionChargeForm.getNotId() > 0) {
                nb.setNotid(additionChargeForm.getNotId());
                notificationDao.modifyNotificationData(nb);
                joinform.setNotId(additionChargeForm.getNotId());
                joinform.setJoinId(additionChargeForm.getJoinid());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                joinform.setNotId(notid);

            }
            joiningDAO.saveJoining(joinform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginuserid());

            String sbLang = sbDAO.getAdditionalChargeLangDetails(additionChargeForm, additionChargeForm.getNotId());
            notificationDao.saveServiceBookLanguage(sbLang, additionChargeForm.getNotId(), "ADDITIONAL_CHARGE");

            //additionChargeForm.setEmpid(selectedEmpObj.getEmpId());
            //additionalChargeDAO.insertAdditionalChargeData(additionChargeForm);
            additionalchargelist = additionalChargeDAO.findAllAdditionalCharge(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/additionalCharge/AdditionalCharge", "additionalChargeForm", additionChargeForm);
            mav.addObject("additionalchargelist", additionalchargelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editadditionalCharge")
    public ModelAndView editadditionalCharge(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, 
            @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        AdditionalCharge additionalform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            additionalform = additionalChargeDAO.editAdditionalCharge(empid, notId);

            mav = new ModelAndView("/additionalCharge/NewAdditionalCharge", "additionalChargeForm", additionalform);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewadditionalCharge")
    public ModelAndView viewadditionalCharge(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = new ModelAndView();

        AdditionalCharge additionalform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            additionalform = additionalChargeDAO.editAdditionalCharge(empid, notId);

            mav.addObject("additionalform", additionalform);

            String sancdeptname = deptDAO.getDeptName(additionalform.getHidAuthDeptCode());
            mav.addObject("sancdeptname", sancdeptname);

            Office office = offDAO.getOfficeDetails(additionalform.getHidAuthOffCode());
            mav.addObject("sancoffice", office.getOffEn());

            String posteddeptname = deptDAO.getDeptName(additionalform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);

            office = offDAO.getOfficeDetails(additionalform.getHidPostedOffCode());
            mav.addObject("postedoffice", office.getOffEn());

            office = offDAO.getOfficeDetails(additionalform.getSltPostedFieldOff());
            mav.addObject("fieldoffice", office.getOffEn());

            mav.setViewName("/additionalCharge/ViewAdditionalCharge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAdditionalChargeData", params = {"btnAdCharge=Cancel Additional Charge"})
    public ModelAndView Delete(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm) throws ParseException {

        ModelAndView mav = null;

        List additionalchargelist = null;

        try {
            System.out.println("add spc:"+additionChargeForm.getHidTempPostedspc());
            additionalChargeDAO.deleteAdditionalCharge(selectedEmpObj.getEmpId(), additionChargeForm.getNotId(),additionChargeForm.getHidTempPostedspc());

            additionalchargelist = additionalChargeDAO.findAllAdditionalCharge(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/additionalCharge/AdditionalCharge", "additionalChargeForm", additionChargeForm);
            mav.addObject("additionalchargelist", additionalchargelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "updateAdditionalChargeData")
    public void updateAdditionalChargeData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm) throws ParseException {
        int additionalcharge = 0;
        try {
            additionChargeForm.setEmpid(selectedEmpObj.getEmpId());
            additionalcharge = additionalChargeDAO.updateAdditionalChargeData(additionChargeForm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "additionalChargeForOfficeDetails")
    public ModelAndView additionalChargeForOfficeDetails(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm) throws ParseException {
        ModelAndView mav = new ModelAndView();
        try {
            if (lub.getLoginusertype().equalsIgnoreCase("A") || (lub.getLoginusertype().equalsIgnoreCase("D") && lub.getLoginuserid().contains("dc"))) {
                List deptlist = deptDAO.getDepartmentList();
                mav.addObject("deptlist", deptlist);
                mav.setViewName("/additionalCharge/SetAdditionalChargeForOfficeDetails");
            } else {
                //path = "under_const";
                mav.setViewName("/under_const");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAdditionalChargeForOfficeDetails")
    public String saveAdditionalChargeForOfficeDetails(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("additionChargeForm") AdditionalCharge additionChargeForm) throws ParseException {
        ModelAndView mav = new ModelAndView();
        try {
            additionalChargeDAO.updateDDOInOfficeDetails(additionChargeForm.getHidAuthOffCode(), additionChargeForm.getAuthSpc(), additionChargeForm.getEmpid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/additionalChargeForOfficeDetails.htm";
    }
}

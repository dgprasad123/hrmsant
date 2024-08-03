package hrms.controller.redesignation;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.dao.redesignation.RedesignationDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.redesignation.Redesignation;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RedesignationController {

    @Autowired
    public RedesignationDAO redesignationDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    SectionDefinationDAO sectionDefinationDAO;

    @RequestMapping(value = "RedesignationList")
    public ModelAndView RedesignationList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redesignationForm") Redesignation redesignationForm) {

        ModelAndView mav = null;
        try {

            List redesignationList = redesignationDAO.findAllRedesignation(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/redesignation/Redesignation", "redesignationForm", redesignationForm);
            mav.addObject("redesignationList", redesignationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newRedesignation")
    public ModelAndView NewRedesignation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redesignationForm") Redesignation redesignationForm) {

        ModelAndView mav = null;
        try {
            redesignationForm.setEmpid(selectedEmpObj.getEmpId());

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();

            mav = new ModelAndView("/redesignation/NewRedesignation", "redesignationForm", redesignationForm);

            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);

            List fieldofflist = offDAO.getFieldOffList(selectedEmpObj.getOffcode());
            mav.addObject("fieldofflist", fieldofflist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRedesignationData", params = "action=Save Redesignation")
    public ModelAndView saveRedesignationData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redesignationForm") Redesignation redesignationForm) throws ParseException {

        ModelAndView mav = null;

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            redesignationForm.setNotType("REDESIGNATION");
            nb.setNottype("REDESIGNATION");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(redesignationForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(redesignationForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(redesignationForm.getHidAuthDeptCode());
            nb.setSancOffCode(redesignationForm.getHidAuthOffCode());
            nb.setSancAuthCode(redesignationForm.getAuthSpc());
            nb.setEntryDeptCode(lub.getLogindeptcode());
            nb.setEntryOffCode(lub.getLoginoffcode());
            nb.setEntryAuthCode(lub.getLoginspc());
            nb.setNote(redesignationForm.getNote());

            if (redesignationForm.getHnotid() > 0) {
                nb.setNotid(redesignationForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                redesignationDAO.updateRedesignationData(redesignationForm);
            } else {
                redesignationForm.setEmpid(selectedEmpObj.getEmpId());
                int notid = notificationDao.insertNotificationData(nb);
                redesignationDAO.insertRedesignationData(redesignationForm, notid);
            }

            List redesignationList = redesignationDAO.findAllRedesignation(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/redesignation/Redesignation", "redesignationForm", redesignationForm);
            mav.addObject("redesignationList", redesignationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRedesignationData", params = "action=Delete")
    public String deleteRedesignationData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redesignationForm") Redesignation redesignationForm) throws ParseException {

        redesignationDAO.deleteRedesignation(redesignationForm.getHnotid() + "");

        return "redirect:/RedesignationList.htm";
    }

    @RequestMapping(value = "editRedesignation")
    public ModelAndView editRedesignation(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        Redesignation redesignationForm = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            redesignationForm = redesignationDAO.editRedesignation(empid, notId);
            System.out.println("Order Date is: " + redesignationForm.getTxtNotOrdDt());

            List deptlist = deptDAO.getDepartmentList();

            List sancOfflist = offDAO.getTotalOfficeList(redesignationForm.getHidAuthDeptCode());
            List sancPostlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(redesignationForm.getHidAuthOffCode());

            List postedOfflist = offDAO.getTotalOfficeList(redesignationForm.getHidPostedDeptCode());
            List postedPostlist = substantivePostDAO.getOfficeWithSPCList(redesignationForm.getHidPostedOffCode());

            List payscalelist = payscaleDAO.getPayScaleList();

            mav = new ModelAndView("/redesignation/NewRedesignation", "redesignationForm", redesignationForm);

            mav.addObject("deptlist", deptlist);

            mav.addObject("sancOfflist", sancOfflist);
            mav.addObject("sancPostlist", sancPostlist);

            mav.addObject("postedOfflist", postedOfflist);
            mav.addObject("postedPostlist", postedPostlist);

            mav.addObject("payscalelist", payscalelist);

            List fieldofflist = offDAO.getFieldOffList(redesignationForm.getHidPostedOffCode());
            mav.addObject("fieldofflist", fieldofflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteRedeignation")
    public String deleteRedeignation(@RequestParam("notId") String notId) {
        redesignationDAO.deleteRedesignation(notId);
        return "/redesignation/Redesignation";
    }

    @RequestMapping(value = "SingleEmployeeRedesignationPage")
    public String SingleEmployeeRedesignationPage(@ModelAttribute("redesignationForm") Redesignation redesignationForm) {
        return "/redesignation/SingleEmployeeRedesignation";
    }

    @RequestMapping(value = "SingleEmployeeRedesignation", params = "btnSubmit=Get Details")
    public ModelAndView SingleEmployeeRedesignation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("redesignationForm") Redesignation redesignationForm) {

        ModelAndView mav = new ModelAndView();

        redesignationForm = redesignationDAO.findRetirementStatus(redesignationForm.getGpfno());

        if (redesignationForm.getEmpid() != null && !redesignationForm.getEmpid().equals("")) {
            //List vacantspnlist = redesignationDAO.getVacantSPNList(lub.getLoginoffcode());
            //mav.addObject("vacantspnlist", vacantspnlist);
            List sectionlist = sectionDefinationDAO.getSectionList(lub.getLoginoffcode());
            mav.addObject("sectionlist", sectionlist);
        } else {
            redesignationForm.setEmpid("NA");
            redesignationForm.setRetType("NA");
        }
        redesignationForm.setHtrid(redesignationForm.getRetType());
        
        mav.addObject("redesignationForm", redesignationForm);

        mav.addObject("empid", redesignationForm.getEmpid());
        mav.addObject("retType", redesignationForm.getHtrid());
        mav.setViewName("/redesignation/SingleEmployeeRedesignation");

        return mav;
    }

    @RequestMapping(value = "SingleEmployeeRedesignation", params = "btnSubmit=Save ReEmployment")
    public ModelAndView saveSingleEmployeeRedesignation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("redesignationForm") Redesignation redesignationForm) {

        ModelAndView mav = new ModelAndView();        

        int retVal = redesignationDAO.saveRedesignation(redesignationForm, lub.getLoginoffcode());

        mav.addObject("retVal", retVal);

        mav.setViewName("/redesignation/SingleEmployeeRedesignation");

        return mav;
    }
}

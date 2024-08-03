package hrms.controller.reinstatement;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.reinstatement.ReinstatementDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.reinstatement.Reinstatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReinstatementController {

    @Autowired
    public ReinstatementDAO reinstatementDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @RequestMapping(value = "reinstatementList")
    public ModelAndView reinstatementList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reinstatementForm") Reinstatement reinstatementForm) {

        ModelAndView mav = null;

        try {
            List reinstatementList = reinstatementDAO.findAllReinstatement(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/reinstatement/ReinstatementList", "reinstatementForm", reinstatementForm);
            mav.addObject("reinstatementList", reinstatementList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "reinstatementEntry")
    public ModelAndView reinstatementEntry(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reinstatementForm") Reinstatement reinstatementForm) {

        ModelAndView mav = null;

        try {

            List deptlist = deptDAO.getDepartmentList();
            String spid = reinstatementForm.getSpId();
            String empId = reinstatementForm.getEmpid();

            //reinstatementForm.setHnotid(notId);
            //reinstatementForm.setEmpid(selectedEmpObj.getEmpId());
            reinstatementForm = reinstatementDAO.editReinstatement(reinstatementForm.getSpId());

            mav = new ModelAndView("/reinstatement/ReinstatementData", "reinstatementForm", reinstatementForm);
            reinstatementForm.setSpId(spid);
            reinstatementForm.setEmpid(empId);

            mav.addObject("deptlist", deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveReinstatement", params = "action=Reinstate")
    public ModelAndView saveReinstatement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reinstatementForm") Reinstatement reinstatementForm) {

        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            reinstatementForm.setEntDept(lub.getLogindeptcode());
            reinstatementForm.setEntOffice(lub.getLoginoffcode());
            reinstatementForm.setEntAuth(lub.getLoginspc());

            nb.setNottype("REINSTATEMENT");
            nb.setEmpId(reinstatementForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(reinstatementForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(reinstatementForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(reinstatementForm.getEntDept());
            nb.setSancOffCode(reinstatementForm.getEntOffice());
            nb.setSancAuthCode(reinstatementForm.getEntAuth());
            nb.setNote(reinstatementForm.getNote());
            if(reinstatementForm.getChkNotSBPrint()!=null && reinstatementForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }

            if (reinstatementForm.getHnotid() >0 ) {

                nb.setNotid(reinstatementForm.getHnotid());
                notificationDao.modifyNotificationData(nb);

                reinstatementDAO.updateReinstatementData(reinstatementForm);
            } else {

                int notid = notificationDao.insertNotificationData(nb);
                reinstatementForm.setHnotid(notid);
                reinstatementDAO.insertReinstatementData(reinstatementForm);
            }

            mav = new ModelAndView("redirect:/suspensionList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveReinstatement", params = "action=Delete")
    public ModelAndView deleteReinstatement(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reinstatementForm") Reinstatement reinstatementForm) {

        ModelAndView mav = null;

        try {

            reinstatementDAO.deleteReinstatement(reinstatementForm.getSpId());
            notificationDao.deleteNotificationData(reinstatementForm.getHnotid(), "REINSTATEMENT");

            mav = new ModelAndView("/reinstatement/ReinstatementData", "reinstatementForm", reinstatementForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}

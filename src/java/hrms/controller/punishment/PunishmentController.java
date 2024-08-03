/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.punishment;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.punishment.PunishmentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.punishment.PunishmentBean;
import hrms.model.transfer.TransferForm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PunishmentController implements ServletContextAware {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public PunishmentDAO punishmentDAO;

    @Autowired
    public OfficeDAO offDAO;
    private ServletContext context;
    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "AddNewPunishment")
    public ModelAndView downloadApplicantExcel(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("PunishmentBean") PunishmentBean transferForm) {
        ModelAndView mv = new ModelAndView();
        String path = "punishment/AddNewPunishment";
        List deptlist = deptDAO.getDepartmentList();
        List punishmentTypes = punishmentDAO.getPunishmentTypes();
        mv.addObject("deptlist", deptlist);
        mv.addObject("punishmentTypes", punishmentTypes);
        mv.addObject("empId", lub.getEmpId());
        mv.setViewName(path);
        return mv;

    }

    @RequestMapping(value = "savePunishment")
    public ModelAndView savePunishment(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PunishmentBean") PunishmentBean punishmentBean, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List punishmentList = null;
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("ADM_ACTION");
                nb.setEmpId(punishmentBean.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(punishmentBean.getOrderNumber());
                nb.setOrdDate(sdf.parse(punishmentBean.getOrderDate()));
                nb.setSancDeptCode(punishmentBean.getHidTempDeptCode());
                nb.setSancOffCode(punishmentBean.getHidTempAuthOffCode());
                nb.setSancAuthCode(punishmentBean.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(punishmentBean.getNote());
                if (punishmentBean.getChkNotSBPrint() != null && punishmentBean.getChkNotSBPrint().equals("Y")) {
                    nb.setIfVisible("N");
                } else {
                    nb.setIfVisible("Y");
                }

                if (punishmentBean.getAcId() != null && !punishmentBean.getAcId().trim().equals("")) {
                    nb.setNotid(punishmentBean.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    punishmentDAO.updatePunishment(punishmentBean);
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    punishmentDAO.savePunishment(punishmentBean, notid);
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(punishmentBean.getNotificationId(), "ADM_ACTION");

                if (retVal > 0) {
                    punishmentDAO.deletePunishment(punishmentBean);
                }
            }

            punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mav = new ModelAndView("/punishment/PunishmentList", "PunishmentBean", punishmentBean);
            mav.addObject("punishmentList", punishmentList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "PunishmentList")
    public ModelAndView PunishmentList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("PunishmentBean") PunishmentBean punishmentBean) {

        List punishmentList = null;

        ModelAndView mav = null;
        try {
            punishmentBean.setEmpId(u.getEmpId());
            punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());

            mav = new ModelAndView("/punishment/PunishmentList", "PunishmentBean", punishmentBean);
            mav.addObject("punishmentList", punishmentList);
            if (punishmentBean.getPunishmentType() != null && punishmentBean.getPunishmentType().equals("OTHERS")) {
                String punishmentType = punishmentBean.getPunishmentType();
                System.out.println("punishmentType:" + punishmentType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editPunishment")
    public ModelAndView editPunishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        PunishmentBean pBean = new PunishmentBean();

        ModelAndView mav = null;

        try {
            pBean.setEmpId(u.getEmpId());

            String acId = requestParams.get("acId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            pBean = punishmentDAO.getEmpPunishmentData(acId);

            pBean.setEmpId(u.getEmpId());
            pBean.setNotificationId(notificationId);
            pBean.setAcId(acId);

            mav = new ModelAndView("/punishment/AddNewPunishment", "PunishmentBean", pBean);
            List punishmentTypes = punishmentDAO.getPunishmentTypes();
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(pBean.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(pBean.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
            mav.addObject("punishmentTypes", punishmentTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

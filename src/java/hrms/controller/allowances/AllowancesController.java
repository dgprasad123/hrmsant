/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.allowances;

import hrms.dao.allowances.AllowancesDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.allowances.AllowanceModel;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class AllowancesController {

    @Autowired
    public AllowancesDAO allowacesDao;
    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    public SubStantivePostDAO subStantivePostDao;

    @RequestMapping(value = "AllowancesList")
    public ModelAndView AllowedToOfficiateList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("AllowanceModel") AllowanceModel allowanceBean) {

        List allowancesList = null;
        ModelAndView mv = null;
        try {
            allowancesList = allowacesDao.getAllowancesList(u.getEmpId());
            mv = new ModelAndView("/allowances/AllowancesList", "AllowanceModel", allowanceBean);
            mv.addObject("allowancesList", allowancesList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "NewAllowances")
    public ModelAndView NewAllowances(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("AllowanceModel") AllowanceModel allowanceBean) throws IOException {
        ModelAndView mv = null;
        try {
            allowanceBean.setEmpid(u.getEmpId());
            mv = new ModelAndView("/allowances/AllowancesNew", "AllowanceModel", allowanceBean);
            List allowList = allowacesDao.getAllowancesDeductionList();
            List deptlist = deptDAO.getDepartmentList();
            mv.addObject("allowList", allowList);
            mv.addObject("deptlist", deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping(value = "saveAllowance", params = "action=BackToList")
    public ModelAndView returnToList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("AllowanceModel") AllowanceModel allowanceBean) {
        ModelAndView mav = null;
        try {
            String path = "/allowances/AllowancesList";
            List allowancesList = allowacesDao.getAllowancesList(u.getEmpId());
            mav = new ModelAndView();
            mav.addObject("allowancesList", allowancesList);
            mav.setViewName(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAllowance", params = "action=Save")
    public String SaveAllowances(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1,
            @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("AllowanceModel") AllowanceModel allowanceBean) throws IOException {
        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String doe = sdf.format(new Date());
        try {
            allowanceBean.setEmpid(u.getEmpId());
            nb.setNottype("ALLOWANCES");
            nb.setDateofEntry(sdf.parse(doe));
            nb.setOrdno(allowanceBean.getOrdno());
            nb.setNote(allowanceBean.getNote());
            nb.setOrdDate(sdf.parse(allowanceBean.getOrdDate()));
            nb.setEmpId(u.getEmpId());
            nb.setSancDeptCode(allowanceBean.getHidNotifyingDeptCode());
            nb.setSancOffCode(allowanceBean.getHidNotifyingOffCode());
            nb.setSancAuthCode(allowanceBean.getNotifyingSpc());

            if (allowanceBean.getChkNotSBPrint() != null && allowanceBean.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }
            if (allowanceBean.getHnotid() > 0) {
                nb.setNotid(allowanceBean.getHnotid());
                notificationDao.modifyNotificationData(nb);
                allowacesDao.updateAllowancesData(allowanceBean, nb.getNotid());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                allowacesDao.insertAllowancesData(allowanceBean, notid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/AllowancesList.htm";
    }

    @RequestMapping(value = "editAllowance")
    public ModelAndView editAllowance(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1,
            @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("AllowanceModel") AllowanceModel allowanceBean,
            @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;

        try {
            String empid = u.getEmpId();
            int notID = Integer.parseInt(requestParams.get("notId"));
            allowanceBean = allowacesDao.editAllowancesData(notID, empid);
            mv = new ModelAndView("/allowances/AllowancesNew", "AllowanceModel", allowanceBean);
            List allowList = allowacesDao.getAllowancesDeductionList();
            List deptlist = deptDAO.getDepartmentList();
            mv.addObject("allowList", allowList);
            mv.addObject("deptlist", deptlist);
            List offList = offDAO.getTotalOfficeList(allowanceBean.getHidNotifyingDeptCode());
            mv.addObject("notoffList", offList);
            List spclist = subStantivePostDao.getSanctioningSPCOfficeWiseList(allowanceBean.getHidNotifyingOffCode());
            mv.addObject("notSpc", spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }
}

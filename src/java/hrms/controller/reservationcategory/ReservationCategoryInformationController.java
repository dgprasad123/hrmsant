/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.reservationcategory;

import hrms.dao.master.CategoryDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.reservationcategory.ReservationCategoryInformationDAO;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.reservationcategory.ReservationCategoryInformation;
import hrms.model.transfer.TransferForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

/**
 *
 * @author HP
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ReservationCategoryInformationController {

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    @Autowired
    public CategoryDAO categoryDAO;
    @Autowired
    public ReservationCategoryInformationDAO reservationcategoryDao;

    @RequestMapping(value = "ReservationCategoryInformation")
    public ModelAndView ReservationCategoryInformation(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("reservedCategory") ReservationCategoryInformation rci) {
        ModelAndView mv = new ModelAndView("/ReservationCategory/addReservationCategory", "ReservationCategoryInformation", rci);
        List deptlist = deptDAO.getDepartmentList();
        List categorylist = categoryDAO.getCategoryList();
        List Specialcategorylist = categoryDAO.getDisabilityCategorylist();
        List SpecificCodelist = categoryDAO.getSpecificCodeList();
        System.out.println("new");
        mv.addObject("SpecificCodelist", SpecificCodelist);
        mv.addObject("categorylist", categorylist);
        mv.addObject("Specialcategorylist", Specialcategorylist);
        mv.addObject("deptlist", deptlist);
        return mv;
    }

    @RequestMapping(value = "SaveReservationCategory")
    public String saveReservationCategoryInformation(Model model, HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpObj") Users sEmpObj, @ModelAttribute("reservedCategory") ReservationCategoryInformation rci) throws ParseException {
        System.out.println("save");
        NotificationBean nb = new NotificationBean();
        ModelAndView mv = null;
        int notid = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            System.out.println("not_id:" + rci.getHidnotid() + ";" + rci.getOrderno());
            nb.setNottype("RES_CAT");
            nb.setDateofEntry(new Date());
            nb.setOrdno(rci.getOrderno());
            nb.setOrdDate(sdf.parse(rci.getOrderdate()));
            nb.setEntryDeptCode(rci.getDeptcode());
            nb.setEntryOffCode(rci.getOffCode());
            nb.setEntryAuthCode(rci.getSanctionauthority());
            nb.setNote(rci.getNote());
            nb.setEmpId(sEmpObj.getEmpId());

            if (rci.getChkNotSBPrint() != null && rci.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }

            rci.setEmpid(sEmpObj.getEmpId());
            if (rci.getHidnotid() != null && !rci.getHidnotid().equals("")) {
                System.out.println("update");
                nb.setNotid(Integer.parseInt(rci.getHidnotid()));
                notificationDao.modifyNotificationData(nb);
            } else {
                notid = notificationDao.insertNotificationData(nb);
            }
            if (rci.getReservationCategory() != null && !rci.getReservationCategory().equals("")) {
                System.out.println("reservationCategory:" + rci.getRehabilationCategory());
                reservationcategoryDao.updateReservationCategoryInformation(rci);
            }

            /*if (rci.getRdTransaction() != null && rci.getRdTransaction().equals("C")) {
             if (rci.getCheckEmployed() != null && rci.getCheckEmployed().equals("Y")) {
             if (rci.getReservationCategory() != null && rci.getDisableCategory() != null && rci.getSpecific_Code() != null) {
             System.out.println("reservationCategory:" + rci.getRehabilationCategory());
             reservationcategoryDao.updateReservationCategoryInformation(rci);
             }
             }
             }*/
            System.out.println("RES_CAT" + notid);
            //mv = new ModelAndView("/ReservationCategory/addReservationCategory","ReservationCategoryInformation", rci);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/reservationCategoryList.htm";
    }

    @RequestMapping(value = "reservationCategoryList")
    public ModelAndView ListReservationCategoryInformation(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reservedCategory") ReservationCategoryInformation rci, HttpServletRequest request) {
        ModelAndView mav = null;
        try {

            List reservationlist = reservationcategoryDao.getReservationCategoryInformation(selectedEmpObj.getEmpId());
            System.out.println("emp_size:" + reservationlist.size());
            if (reservationlist.size() > 0) {
                mav = new ModelAndView("/ReservationCategory/listReservationCategory", "rci", rci);
                mav.addObject("reservationlist", reservationlist);
            } else {
                mav = new ModelAndView("/ReservationCategory/addReservationCategory", "ReservationCategoryInformation", rci);
                List deptlist = deptDAO.getDepartmentList();
                List categorylist = categoryDAO.getCategoryList();
                List Specialcategorylist = categoryDAO.getDisabilityCategorylist();
                List SpecificCodelist = categoryDAO.getSpecificCodeList();
                System.out.println("new");
                mav.addObject("SpecificCodelist", SpecificCodelist);
                mav.addObject("categorylist", categorylist);
                mav.addObject("Specialcategorylist", Specialcategorylist);
                mav.addObject("deptlist", deptlist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editReservationCategory")
    public ModelAndView editReservationCategory(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @RequestParam("notId") String notId, HttpServletRequest request) {
        ModelAndView mav = null;
        try {
            //rci.setEmpid(selectedEmpObj.getEmpId());
            System.out.println("notid:" + notId);
            ReservationCategoryInformation rci = reservationcategoryDao.editReservationCategory(notId);

            mav = new ModelAndView("/ReservationCategory/addReservationCategory", "reservedCategory", rci);
            List deptlist = deptDAO.getDepartmentList();

            List offlist = offDAO.getTotalOfficeList(rci.getDeptcode());
            List spclist = substantivePostDAO.getOfficeWithSPCList(rci.getOffCode());
            List categorylist = categoryDAO.getCategoryList();
            List Specialcategorylist = categoryDAO.getDisabilityCategorylist();
            List SpecificCodelist = categoryDAO.getSpecificCodeList();

            mav.addObject("SpecificCodelist", SpecificCodelist);
            mav.addObject("categorylist", categorylist);
            mav.addObject("Specialcategorylist", Specialcategorylist);
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("spclist", spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

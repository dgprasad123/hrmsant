/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.placementofservice;

import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.placementofservice.PlacementOfServiceDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.placementofservice.PlacementOfServiceForm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @author DurgaPrasad
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PlacementOfServiceController {

    @Autowired
    public PlacementOfServiceDAO placementOfServiceDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public CadreDAO cadreDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public EmpPayRecordDAO emppayrecordDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PostDAO postDAO;

    @Autowired
    public ServiceBookLanguageDAO sbDAO;

    @RequestMapping(value = "PlacementOfServiceList")
    public String PlacementOfServiceList(Model model, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("placementOfServiceForm") PlacementOfServiceForm placementOfServiceForm) {

        List placementOfServiceList = null;

        try {
            placementOfServiceList = placementOfServiceDAO.getPlacementOfServiceList(u.getEmpId());
            model.addAttribute("placementOfServiceList", placementOfServiceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/placementofservice/PlacementOfServiceList";
    }

    @RequestMapping(value = "newPlacementOfService")
    public ModelAndView newPlacementOfService(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("placementOfServiceForm") PlacementOfServiceForm placementOfServiceForm) throws IOException {

        ModelAndView mav = null;

        try {
            placementOfServiceForm.setEmpid(u.getEmpId());

            mav = new ModelAndView("/placementofservice/NewPlacementOfService", "placementOfServiceForm", placementOfServiceForm);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();

            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePlacementOfService", params = {"btnConfirmation=Save"})
    public ModelAndView savePlacementOfService(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("placementOfServiceForm") PlacementOfServiceForm placementOfServiceForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();

        String sblanguage = "";
        try {

            nb.setNottype("SERVICE_DISPOSAL");
            nb.setEmpId(placementOfServiceForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(placementOfServiceForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(placementOfServiceForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(placementOfServiceForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(placementOfServiceForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(placementOfServiceForm.getNotifyingSpc());
            nb.setNote(placementOfServiceForm.getNote());

            cadreForm.setEmpId(placementOfServiceForm.getEmpid());
            cadreForm.setNotType("SERVICE_DISPOSAL");
            cadreForm.setCadreCode(placementOfServiceForm.getHcadId());
            cadreForm.setCadreDept(placementOfServiceForm.getSltCadreDept());
            cadreForm.setCadreName(placementOfServiceForm.getSltCadre());
            cadreForm.setGrade(placementOfServiceForm.getSltGrade());
            cadreForm.setCadreLvl(placementOfServiceForm.getSltCadreLevel());
            cadreForm.setDescription(placementOfServiceForm.getSltDescription());
            cadreForm.setAllotmentYear(placementOfServiceForm.getTxtAllotmentYear());
            cadreForm.setCadreId(placementOfServiceForm.getTxtCadreId());
            cadreForm.setPostingDept(placementOfServiceForm.getSltPostingDept());
            cadreForm.setPostCode(placementOfServiceForm.getSltGenericPost());
            cadreForm.setPostClassification(placementOfServiceForm.getRdPostClassification());
            cadreForm.setPostStatus(placementOfServiceForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(placementOfServiceForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(placementOfServiceForm.getSltCadreJoiningWEFTime());

            epayrecordform.setEmpid(placementOfServiceForm.getEmpid());
            epayrecordform.setNot_type("SERVICE_DISPOSAL");
            epayrecordform.setPayscale(placementOfServiceForm.getSltPayScale());
            epayrecordform.setBasic(placementOfServiceForm.getTxtBasic());
            epayrecordform.setGp(placementOfServiceForm.getTxtGP());
            epayrecordform.setS_pay(placementOfServiceForm.getTxtSP());
            epayrecordform.setP_pay(placementOfServiceForm.getTxtPP());
            epayrecordform.setOth_pay(placementOfServiceForm.getTxtOP());
            epayrecordform.setOth_desc(placementOfServiceForm.getTxtDescOP());
            epayrecordform.setWefDt(placementOfServiceForm.getTxtWEFDt());
            epayrecordform.setWefTime(placementOfServiceForm.getSltWEFTime());

            if (placementOfServiceForm.getHnotid() > 0) {
                nb.setNotid(placementOfServiceForm.getHnotid());
                notificationDao.modifyNotificationData(nb);

                if (placementOfServiceForm.getHcadId() != null && !placementOfServiceForm.getHcadId().equals("")) {
                    cadreDAO.updateCadreData(cadreForm);
                } else {
                    cadreForm.setNotId(placementOfServiceForm.getHnotid());
                    cadreDAO.saveCadreData(cadreForm);
                }
                if (placementOfServiceForm.getHpayid() > 0) {
                    emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
                } else {
                    epayrecordform.setNot_id(placementOfServiceForm.getHnotid());
                    emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                }
                sblanguage = sbDAO.getPlacementOfServiceDetails(placementOfServiceForm, placementOfServiceForm.getHnotid(), "SERVICE_DISPOSAL");
                placementOfServiceDAO.savePlacementOfService(placementOfServiceForm, placementOfServiceForm.getHnotid(), sblanguage, sblanguage);
            } else {
                int notid = notificationDao.insertNotificationData(nb);

                cadreForm.setNotId(notid);
                cadreDAO.saveCadreData(cadreForm);

                epayrecordform.setNot_id(notid);
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                sblanguage = sbDAO.getPlacementOfServiceDetails(placementOfServiceForm, notid, "SERVICE_DISPOSAL");
                placementOfServiceDAO.savePlacementOfService(placementOfServiceForm, notid, sblanguage, sblanguage);
            }

            mav = new ModelAndView("redirect:/PlacementOfServiceList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editPlacementOfService")
    public ModelAndView editPlacementOfService(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        PlacementOfServiceForm placementOfServiceForm = new PlacementOfServiceForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            placementOfServiceForm.setEmpid(u.getEmpId());
            placementOfServiceForm = placementOfServiceDAO.getPlacementOfServiceData(placementOfServiceForm, notificationId);

            placementOfServiceForm.setHnotid(notificationId);

            mav = new ModelAndView("/placementofservice/NewPlacementOfService", "placementOfServiceForm", placementOfServiceForm);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officelist = offDAO.getTotalOfficeList(placementOfServiceForm.getHidNotifyingDeptCode(), placementOfServiceForm.getHidNotifyingDistCode());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(placementOfServiceForm.getHidNotifyingOffCode());
            List spclist = subStantivePostDao.getAuthoritySubstantivePostList(placementOfServiceForm.getHidNotifyingOffCode(), placementOfServiceForm.getHidNotifyingGPC());

            List cadrelist = cadreDAO.getCadreList(placementOfServiceForm.getSltCadreDept());
            List gradelist = cadreDAO.getGradeList(placementOfServiceForm.getSltCadre());
            List lvlList = cadreDAO.getCadreLevelList();
            List desclist = cadreDAO.getDescription(placementOfServiceForm.getSltCadreLevel());

            List postlist = postDAO.getPostList(placementOfServiceForm.getSltPostingDept());

            List payscalelist = payscaleDAO.getPayScaleList();

            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("officelist", officelist);
            mav.addObject("notifyingGpclist", notifyingGpclist);
            mav.addObject("spclist", spclist);

            mav.addObject("cadrelist", cadrelist);
            mav.addObject("gradelist", gradelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("desclist", desclist);

            mav.addObject("postlist", postlist);

            mav.addObject("payscalelist", payscalelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}

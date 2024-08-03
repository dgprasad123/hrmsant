package hrms.controller.confirmationofservice;

import hrms.dao.confirmationofservice.ConfirmationOfServiceDAO;
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
import hrms.model.cadre.Cadre;
import hrms.model.confirmationofservice.ConfirmationOfServiceForm;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
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

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ConfirmationOfServiceController {
    
    @Autowired
    public ConfirmationOfServiceDAO confirmationOfServiceDAO;
    
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
    
    @RequestMapping(value = "ConfirmationOfServiceList")
    public String ConfirmationOfServiceList(Model model,@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("confirmationOfServiceForm") ConfirmationOfServiceForm confirmationOfServiceForm) {
        
        List confirmationOfServiceList = null;
        
        try {
            confirmationOfServiceList = confirmationOfServiceDAO.getConfirmationOfServiceList(u.getEmpId());
            model.addAttribute("confirmationOfServiceList", confirmationOfServiceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/confirmationofservice/ConfirmationOfServiceList";
    }
    
    @RequestMapping(value = "newConfirmationOfService")
    public ModelAndView newConfirmationOfService(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("confirmationOfServiceForm") ConfirmationOfServiceForm confirmationOfServiceForm) throws IOException {
        
        ModelAndView mav = null;
        
        try {
            confirmationOfServiceForm.setEmpid(u.getEmpId());
            
            mav = new ModelAndView("/confirmationofservice/NewConfirmationOfService", "confirmationOfServiceForm", confirmationOfServiceForm);
            
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
    
    @RequestMapping(value = "saveConfirmationOfService", params = {"btnConfirmation=Save"})
    public ModelAndView saveConfirmationOfService(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("confirmationOfServiceForm") ConfirmationOfServiceForm confirmationOfServiceForm) throws ParseException {
        
        NotificationBean nb = new NotificationBean();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        
        ModelAndView mav = null;
        
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        
        String sblanguage = "";
        try {
            
            nb.setNottype("CONFIRMATION");
            nb.setEmpId(confirmationOfServiceForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(confirmationOfServiceForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(confirmationOfServiceForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(confirmationOfServiceForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(confirmationOfServiceForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(confirmationOfServiceForm.getNotifyingSpc());
            nb.setNote(confirmationOfServiceForm.getNote());
            
            cadreForm.setEmpId(confirmationOfServiceForm.getEmpid());
            cadreForm.setNotType("CONFIRMATION");
            cadreForm.setCadreCode(confirmationOfServiceForm.getHcadId());
            cadreForm.setCadreDept(confirmationOfServiceForm.getSltCadreDept());
            cadreForm.setCadreName(confirmationOfServiceForm.getSltCadre());
            cadreForm.setGrade(confirmationOfServiceForm.getSltGrade());
            cadreForm.setCadreLvl(confirmationOfServiceForm.getSltCadreLevel());
            cadreForm.setDescription(confirmationOfServiceForm.getSltDescription());
            cadreForm.setAllotmentYear(confirmationOfServiceForm.getTxtAllotmentYear());
            cadreForm.setCadreId(confirmationOfServiceForm.getTxtCadreId());
            cadreForm.setPostingDept(confirmationOfServiceForm.getSltPostingDept());
            cadreForm.setPostCode(confirmationOfServiceForm.getSltGenericPost());
            cadreForm.setPostClassification(confirmationOfServiceForm.getRdPostClassification());
            cadreForm.setPostStatus(confirmationOfServiceForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(confirmationOfServiceForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(confirmationOfServiceForm.getSltCadreJoiningWEFTime());
            
            epayrecordform.setEmpid(confirmationOfServiceForm.getEmpid());
            epayrecordform.setNot_type("CONFIRMATION");
            epayrecordform.setPayscale(confirmationOfServiceForm.getSltPayScale());
            epayrecordform.setBasic(confirmationOfServiceForm.getTxtBasic());
            epayrecordform.setGp(confirmationOfServiceForm.getTxtGP());
            epayrecordform.setS_pay(confirmationOfServiceForm.getTxtSP());
            epayrecordform.setP_pay(confirmationOfServiceForm.getTxtPP());
            epayrecordform.setOth_pay(confirmationOfServiceForm.getTxtOP());
            epayrecordform.setOth_desc(confirmationOfServiceForm.getTxtDescOP());
            epayrecordform.setWefDt(confirmationOfServiceForm.getTxtWEFDt());
            epayrecordform.setWefTime(confirmationOfServiceForm.getSltWEFTime());
            
            if (confirmationOfServiceForm.getHnotid() > 0) {
                nb.setNotid(confirmationOfServiceForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                
                if (confirmationOfServiceForm.getHcadId() != null && !confirmationOfServiceForm.getHcadId().equals("")) {
                    cadreDAO.updateCadreData(cadreForm);
                } else {
                    cadreForm.setNotId(confirmationOfServiceForm.getHnotid());
                    cadreDAO.saveCadreData(cadreForm);
                }
                if (confirmationOfServiceForm.getHpayid() > 0) {
                    emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
                } else {
                    epayrecordform.setNot_id(confirmationOfServiceForm.getHnotid());
                    emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                }
                sblanguage = sbDAO.getConfirmationOfServiceDetails(confirmationOfServiceForm, confirmationOfServiceForm.getHnotid(), "CONFIRMATION");
                confirmationOfServiceDAO.saveConfirmationOfService(confirmationOfServiceForm, confirmationOfServiceForm.getHnotid(), sblanguage, sblanguage);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                cadreForm.setNotId(notid);
                cadreDAO.saveCadreData(cadreForm);
                
                epayrecordform.setNot_id(notid);
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                
                sblanguage = sbDAO.getConfirmationOfServiceDetails(confirmationOfServiceForm, notid, "CONFIRMATION");
                confirmationOfServiceDAO.saveConfirmationOfService(confirmationOfServiceForm, notid, sblanguage, sblanguage);
            }
            
            mav = new ModelAndView("redirect:/ConfirmationOfServiceList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "editConfirmationOfService")
    public ModelAndView editConfirmationOfService(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {
        
        ConfirmationOfServiceForm confirmationOfServiceForm = new ConfirmationOfServiceForm();
        
        ModelAndView mav = null;
        
        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            confirmationOfServiceForm.setEmpid(u.getEmpId());
            confirmationOfServiceForm = confirmationOfServiceDAO.getConfirmationOfServiceData(confirmationOfServiceForm, notificationId);
            
            confirmationOfServiceForm.setHnotid(notificationId);
            
            mav = new ModelAndView("/confirmationofservice/NewConfirmationOfService", "confirmationOfServiceForm", confirmationOfServiceForm);
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officelist = offDAO.getTotalOfficeList(confirmationOfServiceForm.getHidNotifyingDeptCode(), confirmationOfServiceForm.getHidNotifyingDistCode());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(confirmationOfServiceForm.getHidNotifyingOffCode());
            List spclist = subStantivePostDao.getAuthoritySubstantivePostList(confirmationOfServiceForm.getHidNotifyingOffCode(),confirmationOfServiceForm.getHidNotifyingGPC());
            
            List cadrelist = cadreDAO.getCadreList(confirmationOfServiceForm.getSltCadreDept());
            List gradelist = cadreDAO.getGradeList(confirmationOfServiceForm.getSltCadre());
            List lvlList = cadreDAO.getCadreLevelList();
            List desclist = cadreDAO.getDescription(confirmationOfServiceForm.getSltCadreLevel());
            
            List postlist = postDAO.getPostList(confirmationOfServiceForm.getSltPostingDept());
            
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

package hrms.controller.recruitment;

import hrms.common.CommonFunctions;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.recruitment.RecruitmentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.promotion.PromotionForm;
import hrms.model.recruitment.RecruitmentModel;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RecruitmentController {

    @Autowired
    public RecruitmentDAO recruitmentDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;
    
    @Autowired
    public CadreDAO cadreDAO;
    
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    
    @Autowired
    public PostDAO postDAO;
    
    @RequestMapping(value = "RecruitmentList")
    public ModelAndView RecruitmentList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("recruitmentModel") RecruitmentModel recruitmentModel) {

        List recruitmentlist = null;

        ModelAndView mav = null;
        try {
            
            recruitmentlist = recruitmentDAO.findAllRecruitment(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/recruitment/RecruitmentList", "recruitmentModel", recruitmentModel);
            mav.addObject("recruitmentlist", recruitmentlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newRecruitmentData")
    public ModelAndView NewRecruitmentData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("recruitmentModel") RecruitmentModel recruitmentModel) throws IOException {

        ModelAndView mav = null;

        try {
            //recruitmentModel.setEmpid(u.getEmpId());
            recruitmentModel.setEmpid(selectedEmpObj.getEmpId());
            
            mav = new ModelAndView("/recruitment/NewRecruitment", "recruitmentModel", recruitmentModel);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRecruitment", params = {"btnRecruitment=Save Recruitment"})
    public ModelAndView saveRecruitment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,@ModelAttribute("recruitmentModel") RecruitmentModel recruitmentModel) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List recruitmentlist = null;
        try {
            String empid = recruitmentModel.getEmpid();
            
            nb.setNottype(recruitmentModel.getSltNotificationType());
            nb.setEmpId(empid);
            nb.setDateofEntry(new Date());
            nb.setOrdno(recruitmentModel.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(recruitmentModel.getTxtNotOrdDt()));
            nb.setSancDeptCode(recruitmentModel.getHidNotifyingDeptCode());
            nb.setSancOffCode(recruitmentModel.getHidNotifyingOffCode());
            if(recruitmentModel.getRadpostingauthtype() != null && recruitmentModel.getRadpostingauthtype().equals("GOI")){
                nb.setSancAuthCode(recruitmentModel.getHidOthSpc());
            }else{
                nb.setSancAuthCode(recruitmentModel.getNotifyingSpc());
            }
            nb.setNote(recruitmentModel.getNote());
            if(recruitmentModel.getChkNotSBPrint() != null && recruitmentModel.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            nb.setRadntfnauthtype(recruitmentModel.getRadpostingauthtype());
            if (recruitmentModel.getHnotid() >0) {
                nb.setNotid(recruitmentModel.getHnotid());
                notificationDao.modifyNotificationData(nb);
                recruitmentDAO.updateRecruitmentData(recruitmentModel);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                recruitmentDAO.insertRecruitmentData(recruitmentModel,notid);
            }

            recruitmentlist = recruitmentDAO.findAllRecruitment(empid);
            mav = new ModelAndView("/recruitment/RecruitmentList", "recruitmentModel", recruitmentModel);
            mav.addObject("recruitmentlist", recruitmentlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "editRecruitment")
    public ModelAndView editRecruitment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        RecruitmentModel recruitmentModel = new RecruitmentModel();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            recruitmentModel = recruitmentDAO.editRecruitment(selectedEmpObj.getEmpId(), notificationId);
            
            recruitmentModel.setEmpid(selectedEmpObj.getEmpId());
            recruitmentModel.setHnotid(notificationId);

            mav = new ModelAndView("/recruitment/NewRecruitment", "recruitmentModel", recruitmentModel);
            
            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List offlist = offDAO.getTotalOfficeList(recruitmentModel.getHidNotifyingDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(recruitmentModel.getHidNotifyingOffCode());
            mav.addObject("postlist", postlist);
            
            List gpclist = postDAO.getPostList(recruitmentModel.getSltPostingDept());
            mav.addObject("gpclist", gpclist);
            
            List cadrelist = cadreDAO.getCadreList(recruitmentModel.getSltCadreDept());
            mav.addObject("cadrelist", cadrelist);
            List gradelist = cadreDAO.getGradeList(recruitmentModel.getSltCadre());
            mav.addObject("gradelist", gradelist);
            List desclist = cadreDAO.getDescription(recruitmentModel.getSltCadreLevel());
            mav.addObject("desclist", desclist);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "viewRecruitment")
    public ModelAndView viewRecruitment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        RecruitmentModel recruitmentModel = new RecruitmentModel();

        ModelAndView mav = new ModelAndView();

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            recruitmentModel = recruitmentDAO.editRecruitment(selectedEmpObj.getEmpId(), notificationId);
            
            recruitmentModel.setEmpid(selectedEmpObj.getEmpId());
            recruitmentModel.setHnotid(notificationId);
            
            mav.addObject("recruitment", recruitmentModel);
            
            String authdeptname = deptDAO.getDeptName(recruitmentModel.getHidNotifyingDeptCode());
            mav.addObject("notideptname", authdeptname);
            
            Office office = offDAO.getOfficeDetails(recruitmentModel.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());
            
            String cadredeptname = deptDAO.getDeptName(recruitmentModel.getSltCadreDept());
            mav.addObject("cadredeptname", cadredeptname);

            String cadrename = cadreDAO.getCadreName(recruitmentModel.getSltCadre());
            mav.addObject("cadrename", cadrename);
            
            String postingdeptname = deptDAO.getDeptName(recruitmentModel.getSltPostingDept());
            mav.addObject("postingdeptname", postingdeptname);

            String genericpost = postDAO.getPostName(recruitmentModel.getSltGenericPost());
            mav.addObject("genericpost", genericpost);
            
            mav.setViewName("/recruitment/ViewRecruitment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "saveRecruitment", params = {"btnRecruitment=Delete"})
    public ModelAndView deleteRecruitment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,@ModelAttribute("recruitmentModel") RecruitmentModel recruitmentModel) throws ParseException {

        ModelAndView mav = null;
        
        List recruitmentlist = null;
        try {
            if (recruitmentModel.getHnotid() >0) {
                notificationDao.deleteNotificationData(recruitmentModel.getHnotid(), recruitmentModel.getSltNotificationType());
                recruitmentDAO.deleteRecruitment(recruitmentModel);
            }

            recruitmentlist = recruitmentDAO.findAllRecruitment(recruitmentModel.getEmpid());
            mav = new ModelAndView("/recruitment/RecruitmentList", "recruitmentModel", recruitmentModel);
            mav.addObject("recruitmentlist", recruitmentlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

package hrms.controller.posting;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.posting.PostingDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.posting.PostingForm;
import hrms.model.promotion.PromotionForm;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PostingController {

    @Autowired
    public PostingDAO postingDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;
    
    @Autowired
    public NotificationDAO notificationDao;
    
    @Autowired
    public OfficeDAO offDAO;
    
    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @RequestMapping(value = "PostingList")
    public ModelAndView postingList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("postingform") PostingForm postingform) {

        ModelAndView mav = null;
        List postingList = null;
        try {
            postingList = postingDAO.findAllPosting(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/posting/PostingList", "postingform", postingform);

            mav.addObject("postingList", postingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newPosting")
    public ModelAndView NewPosting(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("postingform") PostingForm postingform) throws IOException {

        ModelAndView mav = null;
        try {
            postingform.setEmpid(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/posting/NewPosting", "postingform", postingform);
            
            if (postingform.getRadnotifyingauthtype()== null || postingform.getRadnotifyingauthtype().equals("")) {
                postingform.setRadnotifyingauthtype("GOO");
            }
            
            if (postingform.getRadpostingauthtype() == null || postingform.getRadpostingauthtype().equals("")) {
                postingform.setRadpostingauthtype("GOO");
            }
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            mav.addObject("payscalelist", payscalelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePosting")
    public ModelAndView SavePosting(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("postingform") PostingForm postingform) throws IOException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        try {
            nb.setNottype("POSTING");
            nb.setEmpId(postingform.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(postingform.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(postingform.getTxtNotOrdDt()));
            nb.setSancDeptCode(postingform.getHidAuthDeptCode());
            nb.setSancOffCode(postingform.getHidAuthOffCode());
            if (postingform.getRadnotifyingauthtype() != null && postingform.getRadnotifyingauthtype().equals("GOI")) {
                nb.setSancAuthCode(postingform.getHidNotifyingOthSpc());
            }else{
                nb.setSancAuthCode(postingform.getAuthSpc());
            }
            nb.setNote(postingform.getNote());
            nb.setRadntfnauthtype(postingform.getRadpostingauthtype());
            nb.setRadpostingauthtype(postingform.getRadnotifyingauthtype());
            if (postingform.getHnotid() >0 ) {
                nb.setNotid(postingform.getHnotid());
                notificationDao.modifyNotificationData(nb);
                postingDAO.updatePosting(postingform);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                postingDAO.savePosting(postingform, notid);
            }
            return new ModelAndView("redirect:/PostingList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
       return mav;
    }
    
    @RequestMapping(value = "editPosting")
    public ModelAndView editPosting(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        PostingForm postingform = new PostingForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            postingform.setEmpid(selectedEmpObj.getEmpId());
            
            postingform = postingDAO.getPostingData(postingform, notificationId);

            postingform.setEmpid(selectedEmpObj.getEmpId());
            postingform.setHnotid(notificationId);

            mav = new ModelAndView("/posting/NewPosting", "postingform", postingform);
            
            if (postingform.getRadnotifyingauthtype()== null || postingform.getRadnotifyingauthtype().equals("")) {
                postingform.setRadnotifyingauthtype("GOO");
            }
            
            if (postingform.getRadpostingauthtype() == null || postingform.getRadpostingauthtype().equals("")) {
                postingform.setRadpostingauthtype("GOO");
            }
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            
            List sancOfflist = offDAO.getTotalOfficeList(postingform.getHidAuthDeptCode());
            List sancPostlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(postingform.getHidAuthOffCode());
            
            List postedOfflist = offDAO.getTotalOfficeList(postingform.getHidPostedDeptCode());
            List postedPostlist = substantivePostDAO.getOfficeWithSPCList(postingform.getHidPostedOffCode());
            
            List sancGPClist = substantivePostDAO.getAuthorityGenericPostList(postingform.getHidAuthOffCode());
            List gpcpostedList = substantivePostDAO.getGenericPostList(postingform.getHidPostedOffCode());
            
            List payscalelist = payscaleDAO.getPayScaleList();
            
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
            mav.addObject("sancOfflist", sancOfflist);
            mav.addObject("sancPostlist", sancPostlist);
            
            mav.addObject("postedOfflist", postedOfflist);
            mav.addObject("postedPostlist", postedPostlist);
            
            mav.addObject("gpcauthlist", sancGPClist);
            mav.addObject("gpcpostedList", gpcpostedList);
            
            mav.addObject("payscalelist", payscalelist);

            List fieldofflist = offDAO.getFieldOffList(postingform.getHidPostedOffCode());
            mav.addObject("fieldofflist", fieldofflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "viewPosting")
    public ModelAndView viewPosting(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        PostingForm postingform = new PostingForm();

        ModelAndView mav = new ModelAndView();

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            postingform.setEmpid(selectedEmpObj.getEmpId());
            
            postingform = postingDAO.getPostingData(postingform, notificationId);
            
            postingform.setEmpid(selectedEmpObj.getEmpId());
            postingform.setHnotid(notificationId);
            
            mav.addObject("postingform", postingform);
            
            String notideptname = deptDAO.getDeptName(postingform.getHidAuthDeptCode());
            mav.addObject("notideptname", notideptname);
            
            Office office = offDAO.getOfficeDetails(postingform.getHidAuthOffCode());
            mav.addObject("notioffice", office.getOffEn());
            
            String posteddeptname = deptDAO.getDeptName(postingform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);
            
            office = offDAO.getOfficeDetails(postingform.getHidPostedOffCode());
            mav.addObject("postedoffice", office.getOffEn());
            
            office = offDAO.getOfficeDetails(postingform.getSltPostedFieldOff());
            mav.addObject("fieldoffice", office.getOffEn());
            
            mav.setViewName("/posting/ViewPosting");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
@ResponseBody
@RequestMapping(value = "DeletePosting")
    public ModelAndView deletePosting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        PostingForm prform = new PostingForm();
 List postinglist = null;
        ModelAndView mav = null;

        try {
            prform.setEmpid(u.getEmpId());

            int notificationId = Integer.parseInt(requestParams.get("not_id"));
            prform = postingDAO.getPostingData(prform, notificationId);
            prform.setHnotid(notificationId);  
            int retVal = notificationDao.deleteNotificationData(notificationId, "POSTING"); 
                if (retVal > 0) {
                    postingDAO.deletePosting(prform);
                }            
            mav = new ModelAndView("/posting/PostingList", "postingform", prform);

            mav.addObject("postingList", postinglist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }     
}

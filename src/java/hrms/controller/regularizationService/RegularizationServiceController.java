package hrms.controller.regularizationService;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.regularizationService.RegularizationServiceDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.regularizeService.RegularizeServiceForm;
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
public class RegularizationServiceController {
    
    @Autowired
    public RegularizationServiceDAO regularizeServiceDAO;
    
    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;
    
    @RequestMapping(value = "RegularizationServiceList")
    public ModelAndView RegularizationServiceList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceForm regularizeService) {

        List regularizelist = null;

        ModelAndView mav = null;
        try {
            regularizelist = regularizeServiceDAO.findAllRegularizationData(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/regularizationService/RegularizationServiceList", "regularizeService", regularizeService);
            mav.addObject("regularizelist", regularizelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newRegularization")
    public ModelAndView NewRegularization(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceForm regularizeService) throws IOException {

        ModelAndView mav = null;

        try {
            //recruitmentModel.setEmpid(u.getEmpId());
            regularizeService.setEmpid(selectedEmpObj.getEmpId());
            regularizeService.setHnotType("REGULARIZATION");
            
            mav = new ModelAndView("/regularizationService/NewRegularizationService", "regularizeService", regularizeService);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRegularization", params = {"btnRegularizationService=Save Regularization"})
    public ModelAndView saveRegularization(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,@ModelAttribute("regularizeService") RegularizeServiceForm regularizeService) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List regularizelist = null;
        
        try {

            nb.setNottype(regularizeService.getHnotType());
            nb.setEmpId(regularizeService.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(regularizeService.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(regularizeService.getTxtNotOrdDt()));
            nb.setSancDeptCode(regularizeService.getHidNotifyingDeptCode());
            nb.setSancOffCode(regularizeService.getHidNotifyingOffCode());
            nb.setSancAuthCode(regularizeService.getNotifyingSpc());
            nb.setNote(regularizeService.getNote());
            if(regularizeService.getChkNotSBPrint()!=null && regularizeService.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            
            if (regularizeService.getHnotid() >0) {
                nb.setNotid(regularizeService.getHnotid());
                notificationDao.modifyNotificationData(nb);
                regularizeServiceDAO.updateRegularizationData(regularizeService);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                regularizeServiceDAO.insertRegularizationData(regularizeService,notid);
            }

            regularizelist = regularizeServiceDAO.findAllRegularizationData(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/regularizationService/RegularizationServiceList", "regularizeService", regularizeService);
            mav.addObject("regularizelist", regularizelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "editRegularization")
    public ModelAndView editRegularization(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        RegularizeServiceForm regularizeService = new RegularizeServiceForm();

        ModelAndView mav = null;

        try {
            int  notificationId = Integer.parseInt(requestParams.get("notId"));
            regularizeService.setEmpid(selectedEmpObj.getEmpId());
            regularizeService = regularizeServiceDAO.editRegularizationData(selectedEmpObj.getEmpId(), notificationId);

            //prform.setPromotionId(promotionId);
            regularizeService.setHnotid(notificationId);

            mav = new ModelAndView("/regularizationService/NewRegularizationService", "regularizeService", regularizeService);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(regularizeService.getHidNotifyingDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(regularizeService.getHidNotifyingOffCode());
            mav.addObject("postlist", postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

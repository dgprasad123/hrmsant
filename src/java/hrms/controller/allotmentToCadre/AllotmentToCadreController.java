package hrms.controller.allotmentToCadre;

import hrms.dao.allotmentToCadre.AllotmentToCadreDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.repatriation.RepatriationDAO;
import hrms.model.allotmentToCadre.AllotmentToCadreForm;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class AllotmentToCadreController {
    
    @Autowired
    public AllotmentToCadreDAO allotmentToCadreDAO;
    
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
    public PostDAO postDAO;
    
    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    
    @RequestMapping(value = "AllotmentToCadreList")
    public ModelAndView AllotmentToCadreList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("allotmentToCadreForm") AllotmentToCadreForm allotmentToCadreForm) {

        List allotmentTocadrelist = null;

        ModelAndView mav = null;
        try {
            allotmentTocadrelist = allotmentToCadreDAO.getAllotmentToCadreList(u.getEmpId());

            mav = new ModelAndView("/allotmentToCadre/AllotmentToCadreList", "allotmentToCadreForm", allotmentToCadreForm);
            mav.addObject("allotmentTocadrelist", allotmentTocadrelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newAllotmentToCadre")
    public ModelAndView newAllotmentToCadre(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("allotmentToCadreForm") AllotmentToCadreForm allotmentToCadreForm) throws IOException {

        ModelAndView mav = null;

        try {
            allotmentToCadreForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/allotmentToCadre/NewAllotmentToCadre", "allotmentToCadreForm", allotmentToCadreForm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAllotmentToCadre", params = {"btnAllotmentToCadre=Save"})
    public ModelAndView saveAllotmentToCadre(@ModelAttribute("allotmentToCadreForm") AllotmentToCadreForm allotmentToCadreForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            nb.setNottype("ALLOT_CADRE");
            nb.setEmpId(allotmentToCadreForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(allotmentToCadreForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(allotmentToCadreForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(allotmentToCadreForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(allotmentToCadreForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(allotmentToCadreForm.getNotifyingSpc());
            nb.setNote(allotmentToCadreForm.getNote());
            if(allotmentToCadreForm.getChkNotSBPrint()!=null && allotmentToCadreForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }

            if (allotmentToCadreForm.getHnotid() >0) {
                nb.setNotid(allotmentToCadreForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                allotmentToCadreDAO.updateAllotmentToCadre(allotmentToCadreForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                allotmentToCadreDAO.saveAllotmentToCadre(allotmentToCadreForm, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ModelAndView("redirect:/AllotmentToCadreList.htm"); 
    }

    @RequestMapping(value = "editAllotmentToCadre")
    public ModelAndView editAllotmentToCadre(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        AllotmentToCadreForm allotmentToCadreForm = new AllotmentToCadreForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            allotmentToCadreForm.setEmpid(u.getEmpId());
            allotmentToCadreForm = allotmentToCadreDAO.getEmpAllotmentToCadreData(allotmentToCadreForm, notificationId);

            allotmentToCadreForm.setHnotid(notificationId);

            mav = new ModelAndView("/allotmentToCadre/NewAllotmentToCadre", "allotmentToCadreForm", allotmentToCadreForm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List offlist = offDAO.getTotalOfficeList(allotmentToCadreForm.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(allotmentToCadreForm.getHidNotifyingOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            
            List cadreListDeptWise = cadreDAO.getCadreList(allotmentToCadreForm.getSltCadreDept());
            mav.addObject("cadreListDeptWise", cadreListDeptWise);
            List gradeListCadreWise = cadreDAO.getGradeList(allotmentToCadreForm.getSltCadre());
            mav.addObject("gradeListCadreWise", gradeListCadreWise);
            List descriptionList = cadreDAO.getDescription(allotmentToCadreForm.getSltCadreLevel());
            mav.addObject("descriptionList", descriptionList);
            
            List postListDeptWise = postDAO.getPostList(allotmentToCadreForm.getSltPostingDept());
            mav.addObject("postListDeptWise", postListDeptWise);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

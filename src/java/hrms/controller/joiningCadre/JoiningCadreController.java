package hrms.controller.joiningCadre;

import hrms.dao.joiningCadre.JoiningCadreDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.joiningCadre.JoiningCadreForm;
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
public class JoiningCadreController {
    
    @Autowired
    public JoiningCadreDAO joiningCadreDAO;
    
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
    
    @RequestMapping(value = "JoiningCadreList")
    public ModelAndView JoiningCadreList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("joiningCadreForm") JoiningCadreForm joiningCadreForm) {

        List joiningCadrelist = null;

        ModelAndView mav = null;
        try {
            joiningCadrelist = joiningCadreDAO.getJoiningCadreList(u.getEmpId());

            mav = new ModelAndView("/joiningCadre/JoiningCadreList", "joiningCadreForm", joiningCadreForm);
            mav.addObject("joiningCadrelist", joiningCadrelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "newJoiningCadre")
    public ModelAndView newJoiningCadre(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("joiningCadreForm") JoiningCadreForm joiningCadreForm) throws IOException {

        ModelAndView mav = null;

        try {
            joiningCadreForm.setEmpid(u.getEmpId());
            
            mav = new ModelAndView("/joiningCadre/NewJoiningCadre", "joiningCadreForm", joiningCadreForm);

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
    
    @RequestMapping(value = "saveJoiningCadre", params = {"btnJoiningCadre=Save"})
    public ModelAndView saveJoiningCadre(@ModelAttribute("joiningCadreForm") JoiningCadreForm joiningCadreForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            nb.setNottype("JOIN_CADRE");
            nb.setEmpId(joiningCadreForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(joiningCadreForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(joiningCadreForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(joiningCadreForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(joiningCadreForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(joiningCadreForm.getNotifyingSpc());
            nb.setNote(joiningCadreForm.getNote());
             if(joiningCadreForm.getChkNotSBPrint()!=null && joiningCadreForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            if (joiningCadreForm.getHnotid() >0) {
                nb.setNotid(joiningCadreForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                joiningCadreDAO.updateJoiningCadre(joiningCadreForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                joiningCadreDAO.saveJoiningCadre(joiningCadreForm, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ModelAndView("redirect:/JoiningCadreList.htm"); 
    }
    
    @RequestMapping(value = "editJoiningCadre")
    public ModelAndView editJoiningCadre(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        JoiningCadreForm joiningCadreForm = new JoiningCadreForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            joiningCadreForm.setEmpid(u.getEmpId());
            joiningCadreForm = joiningCadreDAO.getEmpJoiningCadreData(joiningCadreForm, notificationId);

            joiningCadreForm.setHnotid(notificationId);

            mav = new ModelAndView("/joiningCadre/NewJoiningCadre", "joiningCadreForm", joiningCadreForm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List offlist = offDAO.getTotalOfficeList(joiningCadreForm.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(joiningCadreForm.getHidNotifyingOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            
            List cadreListDeptWise = cadreDAO.getCadreList(joiningCadreForm.getSltCadreDept());
            mav.addObject("cadreListDeptWise", cadreListDeptWise);
            List gradeListCadreWise = cadreDAO.getGradeList(joiningCadreForm.getSltCadre());
            mav.addObject("gradeListCadreWise", gradeListCadreWise);
            List descriptionList = cadreDAO.getDescription(joiningCadreForm.getSltCadreLevel());
            mav.addObject("descriptionList", descriptionList);
            
            List postListDeptWise = postDAO.getPostList(joiningCadreForm.getSltPostingDept());
            mav.addObject("postListDeptWise", postListDeptWise);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

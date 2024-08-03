package hrms.controller.relieveCadre;

import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.relieveCadre.RelieveCadreDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.relieveCadre.RelieveCadreForm;
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
public class RelieveCadreController {
    
    @Autowired
    public RelieveCadreDAO relieveCadreDAO;
    
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
    
    
    @RequestMapping(value = "RelieveCadreList")
    public ModelAndView RelieveCadreList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("relieveCadreForm") RelieveCadreForm relieveCadreForm) {

        List relieveCadrelist = null;

        ModelAndView mav = null;
        try {
            relieveCadrelist = relieveCadreDAO.getRelieveCadreList(u.getEmpId());

            mav = new ModelAndView("/relieveCadre/RelieveCadreList", "relieveCadreForm", relieveCadreForm);
            mav.addObject("relieveCadrelist", relieveCadrelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "newRelieveCadre")
    public ModelAndView newRelieveCadre(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("relieveCadreForm") RelieveCadreForm relieveCadreForm) throws IOException {

        ModelAndView mav = null;

        try {
            relieveCadreForm.setEmpid(u.getEmpId());
            
            mav = new ModelAndView("/relieveCadre/NewRelieveCadre", "relieveCadreForm", relieveCadreForm);

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
    
    @RequestMapping(value = "saveRelieveCadre", params = {"btnRelieveCadre=Save"})
    public ModelAndView saveRelieveCadre(@ModelAttribute("relieveCadreForm") RelieveCadreForm relieveCadreForm,@RequestParam Map<String, String> requestParams) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            nb.setNottype("RELIEVE_CADRE");
            nb.setEmpId(relieveCadreForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(relieveCadreForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(relieveCadreForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(relieveCadreForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(relieveCadreForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(relieveCadreForm.getNotifyingSpc());
            nb.setNote(relieveCadreForm.getNote());
            if(relieveCadreForm.getChkNotSBPrint()!=null && !relieveCadreForm.getChkNotSBPrint().equals("")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }

            if (relieveCadreForm.getHnotid() >0 ) {
                nb.setNotid(relieveCadreForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                relieveCadreDAO.updateRelieveCadre(relieveCadreForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                relieveCadreDAO.saveRelieveCadre(relieveCadreForm, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ModelAndView("redirect:/RelieveCadreList.htm"); 
    }
    
    @RequestMapping(value = "editRelieveCadre")
    public ModelAndView editRelieveCadre(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        RelieveCadreForm relieveCadreForm = new RelieveCadreForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            relieveCadreForm.setEmpid(u.getEmpId());
            relieveCadreForm = relieveCadreDAO.getEmpRelieveCadreData(relieveCadreForm, notificationId);

            relieveCadreForm.setHnotid(notificationId);

            mav = new ModelAndView("/relieveCadre/NewRelieveCadre", "relieveCadreForm", relieveCadreForm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List offlist = offDAO.getTotalOfficeList(relieveCadreForm.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(relieveCadreForm.getHidNotifyingOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            
            List cadreListDeptWise = cadreDAO.getCadreList(relieveCadreForm.getSltCadreDept());
            mav.addObject("cadreListDeptWise", cadreListDeptWise);
            List gradeListCadreWise = cadreDAO.getGradeList(relieveCadreForm.getSltCadre());
            mav.addObject("gradeListCadreWise", gradeListCadreWise);
            List descriptionList = cadreDAO.getDescription(relieveCadreForm.getSltCadreLevel());
            mav.addObject("descriptionList", descriptionList);
            
            List postListDeptWise = postDAO.getPostList(relieveCadreForm.getSltPostingDept());
            mav.addObject("postListDeptWise", postListDeptWise);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

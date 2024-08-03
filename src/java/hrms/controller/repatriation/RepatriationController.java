package hrms.controller.repatriation;

import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.repatriation.RepatriationDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.repatriation.RepatriationForm;
import java.io.IOException; 
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RepatriationController {

    @Autowired
    public RepatriationDAO repatriationDAO;

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

    @RequestMapping(value = "RepatriationList")
    public ModelAndView RepatriationList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("repatriationForm") RepatriationForm repatriationForm) {

        List repatriationlist = null;

        ModelAndView mav = null;
        try {
            repatriationlist = repatriationDAO.getRepatriationList(u.getEmpId());

            mav = new ModelAndView("/repatriation/RepatriationList", "repatriationForm", repatriationForm);
            mav.addObject("repatriationlist", repatriationlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newRepatriation")
    public ModelAndView NewRepatriation(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("repatriationForm") RepatriationForm repatriationForm) throws IOException {

        ModelAndView mav = null;

        try {
            repatriationForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/repatriation/NewRepatriation", "repatriationForm", repatriationForm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List allotDescList = repatriationDAO.getAllotDescList("REPATRIATION");
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("allotDescList", allotDescList);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRepatriation", params = {"btnRepatriation=Save Repatriation"})
    public ModelAndView saveRepatriation(@ModelAttribute("repatriationForm") RepatriationForm repatriationForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            nb.setNottype("REPATRIATION");
            nb.setEmpId(repatriationForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(repatriationForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(repatriationForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(repatriationForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(repatriationForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(repatriationForm.getNotifyingSpc());
            nb.setNote(repatriationForm.getNote());
            if(repatriationForm.getChkNotSBPrint()!=null && repatriationForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            if (repatriationForm.getHnotid() >0) {
                nb.setNotid(repatriationForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                repatriationDAO.updateRepatriation(repatriationForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                repatriationDAO.saveRepatriation(repatriationForm, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ModelAndView("redirect:/RepatriationList.htm"); 
    }

    @RequestMapping(value = "editRepatriation")
    public ModelAndView editRepatriation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        RepatriationForm repatriationForm = new RepatriationForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            repatriationForm.setEmpid(u.getEmpId());
            repatriationForm = repatriationDAO.getEmpRepatriationData(repatriationForm, notificationId);

            repatriationForm.setHnotid(notificationId);

            mav = new ModelAndView("/repatriation/NewRepatriation", "repatriationForm", repatriationForm);

            List allotDescList = repatriationDAO.getAllotDescList("REPATRIATION");
            List deptlist = deptDAO.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("allotDescList", allotDescList);
            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            
            List offlist = offDAO.getTotalOfficeList(repatriationForm.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(repatriationForm.getHidNotifyingOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            
            List cadreListDeptWise = cadreDAO.getCadreList(repatriationForm.getSltCadreDept());
            mav.addObject("cadreListDeptWise", cadreListDeptWise);
            List gradeListCadreWise = cadreDAO.getGradeList(repatriationForm.getSltCadre());
            mav.addObject("gradeListCadreWise", gradeListCadreWise);
            List descriptionList = cadreDAO.getDescription(repatriationForm.getSltCadreLevel());
            mav.addObject("descriptionList", descriptionList);
            
            List postListDeptWise = postDAO.getPostList(repatriationForm.getSltPostingDept());
            mav.addObject("postListDeptWise", postListDeptWise);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "viewRepatriation")
    public ModelAndView viewRepatriation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        RepatriationForm repatriationForm = new RepatriationForm();

        ModelAndView mav = new ModelAndView();

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            repatriationForm.setEmpid(u.getEmpId());
            repatriationForm = repatriationDAO.getEmpRepatriationData(repatriationForm, notificationId);

            repatriationForm.setHnotid(notificationId);
            
            mav.addObject("repatriationForm", repatriationForm);
            
            String authdeptname = deptDAO.getDeptName(repatriationForm.getHidNotifyingDeptCode());
            mav.addObject("notideptname", authdeptname);
            
            Office office = offDAO.getOfficeDetails(repatriationForm.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());
            
            String cadredeptname = deptDAO.getDeptName(repatriationForm.getSltCadreDept());
            mav.addObject("cadredeptname", cadredeptname);

            String cadrename = cadreDAO.getCadreName(repatriationForm.getSltCadre());
            mav.addObject("cadrename", cadrename);

            String postingdeptname = deptDAO.getDeptName(repatriationForm.getSltPostingDept());
            mav.addObject("postingdeptname", postingdeptname);

            String genericpost = postDAO.getPostName(repatriationForm.getSltGenericPost());
            mav.addObject("genericpost", genericpost);
            
            mav.setViewName("/repatriation/ViewRepatriation");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

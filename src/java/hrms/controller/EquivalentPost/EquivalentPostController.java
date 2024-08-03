/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.EquivalentPost;

import hrms.dao.EquivalentPost.EquivalentPostDAO;
import hrms.dao.master.CadreDAO;
import hrms.model.EquivalentPost.EquivalentPost;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import org.springframework.beans.factory.annotation.Autowired;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Devi
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EquivalentPostController {
 
    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    public OfficeDAO offDAO;
    
    @Autowired
    public NotificationDAO notificationDao;
    
    @Autowired
    public SubStantivePostDAO substantivePostDAO; 
    
    @Autowired
    public EquivalentPostDAO equivalentPostDAO;
    
    @Autowired
    public CadreDAO cadreDAO;
    
    @Autowired
    public PostDAO postDAO;
    
    @RequestMapping(value = "EquivalentPost")
    public ModelAndView EquivalentPost(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("equivalpostForm") EquivalentPost equivalpostForm) {

        ModelAndView mav = null;
        try {      
            List equivalentpostlist = equivalentPostDAO.getEquivalentPostList(lub.getEmpId());
           
            mav = new ModelAndView("/EquivalentPost/EquivalentPost");   
            mav.addObject("EquivalentpostList", equivalentpostlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addNewEquivalentPost", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addnewEquivalentPost(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("equivalpostForm") EquivalentPost equivalpostForm) {
        ModelAndView mav = null;
        
        //equivalpostForm.setRadpostingauthtype("GOO");
        //equivalpostForm.setRadnotifyingauthtype("GOO");
        //equivalpostForm.setRadequvalpostingauthtype("GOO");
        
        mav = new ModelAndView("/EquivalentPost/addNewEquivalentPost");
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
        List gpcpostedList = substantivePostDAO.getGenericPostList(equivalpostForm.getHidPostedOffCode());
        
        List otherOrgfflist = offDAO.getOtherOrganisationList(); 

         String empid = lub.getEmpId();

         equivalentPostDAO.getCurrentEmpData(empid,equivalpostForm);

         equivalpostForm.setHidcurdeptcode(equivalpostForm.getHidcurdeptcode());
         equivalpostForm.setHidcuroffcode(equivalpostForm.getHidcuroffcode());
         equivalpostForm.setHidcurpostcode(equivalpostForm.getHidcurpostcode());
         equivalpostForm.setHidcurpostname(equivalpostForm.getHidcurpostname());
         equivalpostForm.setHidcurcadredeptcode(equivalpostForm.getHidcurcadredeptcode());
         equivalpostForm.setHidcurcadre(equivalpostForm.getHidcurcadre());
         equivalpostForm.setHidcurgrade(equivalpostForm.getHidcurgrade());
        
         String posteddeptname = deptDAO.getDeptName(equivalpostForm.getHidcurdeptcode());
         mav.addObject("posteddeptname", posteddeptname);
            
         Office office = offDAO.getOfficeDetails(equivalpostForm.getHidcuroffcode());
         mav.addObject("postedoffice", office.getOffEn());

         String curpostname= equivalpostForm.getHidcurpostname();
         equivalpostForm.setHidcurpostname(curpostname);      
         
         String cadredeptname = deptDAO.getDeptName(equivalpostForm.getHidcurcadredeptcode());
         mav.addObject("cadredeptname", cadredeptname);
            
         String curcadrename = cadreDAO.getCadreName(equivalpostForm.getHidcurcadre());
         mav.addObject("curcadrename", curcadrename);
         
         mav.addObject("equivalpostForm", equivalpostForm); 
       
        
        mav.addObject("deptlist", deptlist);
        mav.addObject("distlist", distlist);
        mav.addObject("fieldofflist", fieldofflist);
        mav.addObject("gpcpostedList", gpcpostedList);
        mav.addObject("otherOrgfflist", otherOrgfflist);  
       
        return mav;

    }
    
    @RequestMapping(value = "saveEquivalentPost",method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save EquivalentPost"})
    public ModelAndView savenewEquivalentPost(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("equivalpostForm") EquivalentPost equivalpostForm) {
        
        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
         try {
            equivalpostForm.setEmpid(lub.getEmpId());

            nb.setNottype("EQ_STAT");
            nb.setEmpId(equivalpostForm.getEmpid());            
            nb.setDateofEntry(new Date());
            nb.setOrdno(equivalpostForm.getTxtNotOrdNo());
            if (equivalpostForm.getTxtNotOrdDt() != null && !equivalpostForm.getTxtNotOrdDt().equals("")) {
                nb.setOrdDate(sdf.parse(equivalpostForm.getTxtNotOrdDt()));
            }
            nb.setSancDeptCode(equivalpostForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(equivalpostForm.getHidNotifyingOffCode());
            nb.setRadpostingauthtype(equivalpostForm.getRadnotifyingauthtype());
            if(equivalpostForm.getRadnotifyingauthtype() != null && equivalpostForm.getRadnotifyingauthtype().equals("GOI")){
                nb.setSancAuthCode(equivalpostForm.getHidNotifyingOthSpc());
            }else{
                nb.setSancAuthCode(equivalpostForm.getHidNotiSpc());
            }
            nb.setEntryDeptCode(lub.getDeptcode());
            nb.setEntryOffCode(lub.getOffcode());
            nb.setEntryAuthCode(lub.getCurspc()); 
            nb.setNote(equivalpostForm.getNote());   
            nb.setRadntfnauthtype(equivalpostForm.getRadnotifyingauthtype());
            if(equivalpostForm.getChkNotSBPrint() != null && equivalpostForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
          
           if (equivalpostForm.getHidNotId() > 0) {
                nb.setNotid(equivalpostForm.getHidNotId());
                notificationDao.modifyNotificationData(nb);
                equivalentPostDAO.updateEquivalentPost(equivalpostForm);     
            } else {           
                int notid = notificationDao.insertNotificationData(nb);        
                equivalentPostDAO.saveEquivalentPost(equivalpostForm, notid);
            }
            mav = new ModelAndView("redirect:/EquivalentPost.htm", "equivalpostForm", equivalpostForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;  
    }
   
    @RequestMapping(value = "editEquivalentPost")
    public ModelAndView editEquivalentPost(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedObj,@ModelAttribute("equivalpostForm") EquivalentPost equivalpostForm, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        EquivalentPost equivalpost= null;
        try {
            String empid = selectedObj.getEmpId();
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            equivalpost = equivalentPostDAO.getempeditEquivalentData(empid, notificationId);
            equivalpost.setHidNotId(notificationId);
            equivalpost.setEmpid(empid);
         
            if (equivalpost.getRadpostingauthtype() == null || equivalpost.getRadpostingauthtype().equals("")) {
                equivalpost.setRadpostingauthtype("GOO");
            }
            if (equivalpost.getRadnotifyingauthtype() == null || equivalpost.getRadnotifyingauthtype().equals("")) {
                equivalpost.setRadnotifyingauthtype("GOO");
            }
            
            mav = new ModelAndView("/EquivalentPost/addNewEquivalentPost","equivalpostForm", equivalpost);
            
            //current transaction data
            equivalentPostDAO.getCurrentEmpData(empid, equivalpost);

            String posteddeptname = deptDAO.getDeptName(equivalpost.getHidcurdeptcode());
            mav.addObject("posteddeptname", posteddeptname);

            Office office = offDAO.getOfficeDetails(equivalpost.getHidcuroffcode());
            mav.addObject("postedoffice", office.getOffEn());

            String curpostname = equivalpost.getHidcurpostname();
            equivalpost.setHidcurpostname(curpostname);           

            String cadredeptname = deptDAO.getDeptName(equivalpost.getHidcurcadredeptcode());
            mav.addObject("cadredeptname", cadredeptname);

            String curcadrename = cadreDAO.getCadreName(equivalpost.getHidcurcadre());
            mav.addObject("curcadrename", curcadrename);
         
         //current transaction data end
 
            List deptlist = deptDAO.getDepartmentList();
            
            List distlist = districtDAO.getDistrictList();
  
            //List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List gpcpostedList = substantivePostDAO.getGenericPostList(equivalpost.getHidPostedOffCode());
            
            List sancOffList = offDAO.getTotalOfficeList(equivalpost.getHidNotifyingDeptCode());
            List sancPostList = substantivePostDAO.getSanctioningSPCOfficeWiseList(equivalpost.getHidNotifyingOffCode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();
                              
            List postedOffList = offDAO.getTotalOfficeList(equivalpost.getHidPostedDeptCode());                  
            List postedPostList = substantivePostDAO.getSubstantivePostListBacklogEntry(equivalpost.getHidPostedOffCode(),equivalpost.getGenericpostPosted());
                                 
            List postlist = postDAO.getPostList(equivalpost.getHidequvalPostedDeptCode());

            List lvlList = cadreDAO.getCadreLevelList();
            List gradelist = cadreDAO.getGradeList(equivalpost.getSltCadre());
            List cadrelist = cadreDAO.getCadreList(equivalpost.getSltCadreDept());          
            
            List equvalcadrelist = cadreDAO.getCadreList(equivalpost.getEquvalCadreDept());
            List equvalgradelist = cadreDAO.getGradeList(equivalpost.getEquvalCadre());
            
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            //mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("gpcpostedList", gpcpostedList);
            mav.addObject("sancOfflist", sancOffList);
            mav.addObject("sancPostlist", sancPostList);
            mav.addObject("otherOrgfflist", otherOrgfflist);

            mav.addObject("postedOffList", postedOffList);
            mav.addObject("postedPostList", postedPostList);
            
            mav.addObject("postlist", postlist);
            
            mav.addObject("lvlList", lvlList);   
            mav.addObject("gradelist", gradelist);    
            mav.addObject("cadrelist", cadrelist); 
            mav.addObject("equvalcadrelist", equvalcadrelist);    
            mav.addObject("equvalgradelist", equvalgradelist);  
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
   
   @RequestMapping(value = "viewEquivalentpost")
    public ModelAndView viewEquivalentpost(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedObj,@ModelAttribute("equivalpostForm") EquivalentPost equivalpostForm, @RequestParam Map<String, String> requestParams) throws IOException {
    
        ModelAndView mav = new ModelAndView();

        EquivalentPost dptnform = null;
        
        try {
            String empid = selectedObj.getEmpId();
          
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            dptnform = equivalentPostDAO.getempeditEquivalentData(empid, notificationId);
            dptnform.setHidNotId(notificationId);
            dptnform.setEmpid(empid);
            if (dptnform.getRadpostingauthtype() == null || dptnform.getRadpostingauthtype().equals("")) {
                dptnform.setRadpostingauthtype("GOO");
            }
            if (dptnform.getRadnotifyingauthtype()== null || dptnform.getRadnotifyingauthtype().equals("")) {
                dptnform.setRadnotifyingauthtype("GOO");
            }
            
            mav.addObject("dptnform", dptnform);
            
            String notideptname = deptDAO.getDeptName(dptnform.getHidNotifyingDeptCode());
            mav.addObject("notideptname", notideptname);
            
            Office office = offDAO.getOfficeDetails(dptnform.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());
                       

            String posteddeptname = deptDAO.getDeptName(dptnform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);
            
            office = offDAO.getOfficeDetails(dptnform.getHidPostedOffCode());         
            mav.addObject("postedoffice", office.getOffEn());
                    
            String curpostname= dptnform.getPostedSpc();        
            dptnform.setPostedSpc(curpostname);
  
            String cadredeptname = deptDAO.getDeptName(dptnform.getSltCadreDept());
            mav.addObject("cadredeptname", cadredeptname);
            
            String curcadrename = cadreDAO.getCadreName(dptnform.getSltCadre());
            mav.addObject("curcadrename", curcadrename);
            
            String eqvdeptname = deptDAO.getDeptName(dptnform.getHidequvalPostedDeptCode());
            mav.addObject("eqvdeptname", eqvdeptname);
            
            String eqvpostname = postDAO.getPostName(dptnform.getHidequvalPostedSpc());
            mav.addObject("eqvpostname", eqvpostname);
            
            String eqvalcadredeptname = deptDAO.getDeptName(dptnform.getEquvalCadreDept());
            mav.addObject("eqvalcadredeptname", eqvalcadredeptname);
            
            String eqvcadrename = cadreDAO.getCadreName(dptnform.getEquvalCadre());
            mav.addObject("eqvcadrename", eqvcadrename);
            
            mav.setViewName("/EquivalentPost/viewEquivalentPost");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

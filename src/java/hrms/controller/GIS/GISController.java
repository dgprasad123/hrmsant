package hrms.controller.GIS;

import hrms.dao.GIS.GISDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.model.GIS.GISForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class GISController {

    @Autowired
    public GISDAO gisDAO;
    
    @Autowired
    public TreasuryDAO treasuryDao;
    
    @Autowired
    public DepartmentDAO deptDAO;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    
    @RequestMapping(value = "GISList")
    public ModelAndView GISList(@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("gisForm") GISForm gisForm) {

        List gislist = null;

        ModelAndView mav = new ModelAndView();
        try {
            gislist = gisDAO.getGISList(selectedObj.getEmpId());

            mav.addObject("gislist", gislist);
            mav.setViewName("/GIS/GISList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "GISNewPage")
    public ModelAndView GISNewPage(@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("gisForm") GISForm gisForm) {

        ModelAndView mav = new ModelAndView();
        try {
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            
            List treasuryList = treasuryDao.getTreasuryList();
            mav.addObject("treasuryList", treasuryList);
            
            List schemeList = gisDAO.getSchemeDataList();
            mav.addObject("schemeList", schemeList);
            
            mav.setViewName("/GIS/GISNew");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "saveGISData")
    public String saveGISData(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("gisForm") GISForm gisForm) {

        try {
            gisForm.setEmpid(selectedObj.getEmpId());
            gisDAO.saveGISData(gisForm,lub.getLogindeptcode(),lub.getLoginoffcode(),lub.getLoginspc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/GISList.htm";
    }
    
    @RequestMapping(value = "editGISData")
    public ModelAndView editGISData(@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("gisForm") GISForm gisForm,@RequestParam("gisid") String gisid) {

        ModelAndView mav = new ModelAndView();
        try {
            gisForm = gisDAO.editGISData(selectedObj.getEmpId(),gisid);
            
            mav = new ModelAndView("/GIS/GISNew","gisForm",gisForm);
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            
            List treasuryList = treasuryDao.getTreasuryList();
            mav.addObject("treasuryList", treasuryList);
            
            List schemeList = gisDAO.getSchemeDataList();
            mav.addObject("schemeList", schemeList);
            
            
            List officeList = offDAO.getTotalOfficeList(gisForm.getHidDeptCode(), gisForm.getHidDistCode());
            mav.addObject("officeList", officeList);            
            
            List gpclist = substantivePostDAO.getAuthorityGenericPostList(gisForm.getHidOffCode());
            mav.addObject("gpclist", gpclist);           
            
            
            List spclist = substantivePostDAO.getAuthoritySubstantivePostList(gisForm.getHidOffCode(),gisForm.getHidGPC());
            mav.addObject("spclist", spclist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "deleteGIS.htm", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteGIS(@ModelAttribute("SelectedEmpObj") Users selectedObj, @ModelAttribute("gisForm") GISForm gisForm,@RequestParam("gisid") String gisid) {
       
         ModelAndView mav = null; 
        try {
            gisDAO.deleteGISData(gisid);
            mav = new ModelAndView("redirect:/GISList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    
    }
}

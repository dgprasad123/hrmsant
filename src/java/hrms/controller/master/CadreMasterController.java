package hrms.controller.master;

//import hrms.model.master.Cadre;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.model.cadre.Cadre;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CadreMasterController {

    @Autowired
    CadreDAO cadreDAO;
    @Autowired
    DepartmentDAO departmentDao;
    
    @RequestMapping(value = "cadreList")
    public ModelAndView cadreList(@ModelAttribute("cadre") Cadre  cadre) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("master/CadreList");        
        List cadrelist = cadreDAO.getCadreList(cadre.getDeptCode());         
        mv.addObject("cadrelist", cadrelist);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        return mv;
    }
       
    @RequestMapping(value = "getCadreDetail")
    public ModelAndView getCadreDetail(ModelMap model,@ModelAttribute("cadre") Cadre  cadre){
        ModelAndView mv = new ModelAndView();
        cadre = cadreDAO.editCadre(cadre);
        mv = new ModelAndView("/master/CadreEdit","cadre", cadre); 
        cadre.setHiddeptCode(cadre.getDeptCode());
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        return mv;       
    }
    
    @RequestMapping(value = "viewEmpList")
    public ModelAndView viewEmpList(ModelMap model,@ModelAttribute("cadre") Cadre  cadre){
        ModelAndView mv = new ModelAndView();            
        List empList = cadreDAO.getEmployeeList(cadre.getCadreCode());
        mv = new ModelAndView("/master/viewEmpListCadreWise");
        mv.addObject("empList", empList);
        return mv;       
    }
    
    @RequestMapping(value = "saveCadre",method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save Cadre"})
     public String saveNewCadre(ModelMap model,@ModelAttribute("cadre") Cadre  cadre){        
        //System.out.println("controller c code:"+ cadre.getCadreCode()); 
        if (cadre.getCadreCode() != null && !cadre.getCadreCode().equals("")){
            cadreDAO.updateNewCadre(cadre);
        }else{
            cadreDAO.saveNewCadre(cadre);
        }    
        return "redirect:/cadreList.htm";
    }


    @ResponseBody
    @RequestMapping(value = "getCadreListJSON")
    public void getcadrelist(HttpServletRequest request, HttpServletResponse response, @RequestParam("deptcode") String deptcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List cadrelist = cadreDAO.getCadreList(deptcode);
            json = new JSONArray(cadrelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "getCadreListForPARJSON")
    public void getCadreListForPARJSON(HttpServletRequest request, HttpServletResponse response, @RequestParam("deptcode") String deptcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List cadrelist = cadreDAO.getCadreListfOrPAR(deptcode);
            json = new JSONArray(cadrelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getGradeListCadreWiseJSON")
    public void getGradeListCadreWiseJSON(HttpServletRequest request, HttpServletResponse response, @RequestParam("cadreCode") String cadreCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List gradelist = cadreDAO.getGradeList(cadreCode);
            json = new JSONArray(gradelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getDescriptionJSON")
    public void getDescriptionJSON(HttpServletRequest request, HttpServletResponse response, @RequestParam("cadreLevel") String cadreLevel) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List desclist = cadreDAO.getDescription(cadreLevel);
            json = new JSONArray(desclist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "getCadreGradeDetailsJSON")
    public void getCadreGradeDetailsJSON(HttpServletRequest request, HttpServletResponse response, @RequestParam("cadreCode") String cadreCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List gradelist = cadreDAO.getGradeListDetails(cadreCode);
            json = new JSONArray(gradelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}

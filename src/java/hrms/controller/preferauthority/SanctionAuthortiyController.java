package hrms.controller.preferauthority;

import hrms.SelectOptionforLAMembers;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.preferauthority.SactionedAuthorityDAOImpl;
import hrms.model.g_officating.G_Officiating;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.master.OfficeBean;
import hrms.model.preferauthority.WorkflowAuthority;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class SanctionAuthortiyController implements ServletContextAware {

    @Autowired
    public SactionedAuthorityDAOImpl sactionedAuthorityDao;

    @Autowired
    public DepartmentDAO deptDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @ResponseBody
    @RequestMapping(value = "GetSanctionedAuthorityListJSON", method = RequestMethod.POST)
    public void getsanctionauthoritylistJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams, @RequestParam("processid") int processid, @RequestParam("fiscalYear") String fiscalYear, @RequestParam("spc") String spc) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            
             
             
             

            List sanctionedauthoritylist = sactionedAuthorityDao.getSanctionedAuthorityList(spc, processid, fiscalYear, lub.getLoginempid());
            json.put("total", 10);
            json.put("rows", sanctionedauthoritylist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetSanctionedAuthorityList", method = RequestMethod.GET)
    public ModelAndView getsanctionedauthoritylist(@RequestParam("authType") String authType, @RequestParam("rowid") int rowid, @RequestParam("processCode") int processCode, @RequestParam("fslYear") String fslYear, @RequestParam("parId") int parId) {

        ModelAndView mav = new ModelAndView();
        try {
            String spc = sactionedAuthorityDao.getApplicantSPC(parId);

            String path = "/preferauthority/SearchSancAuthority";
            
            
            
            

            mav.addObject("authType", authType);
            mav.addObject("rowid", rowid);
            mav.addObject("processCode", processCode);
            mav.addObject("fslYear", fslYear);
            mav.addObject("spc", spc);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addAuthority", method = RequestMethod.POST)
    public ModelAndView addauthority(@ModelAttribute("WorkflowAuthority") WorkflowAuthority wa, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("spc", wa.getSpc());
        mav.addObject("processCode", requestParams.get("hidprocessCode"));
        mav.addObject("fiscalYear", requestParams.get("hidfslYear"));
        mav.addObject("authType", requestParams.get("hidauthtype"));
        mav.addObject("rowid", requestParams.get("hidrowid"));
        String path = "/preferauthority/PreferAuthority";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "GetPARDeptListJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPARDeptList() {

        JSONArray json = null;

        SelectOptionforLAMembers so = null;
        try {
            List deptlist = deptDAO.getDepartmentList();

            so = new SelectOptionforLAMembers();
            so.setDeptCode("LM");
            so.setDeptName("LEGISLATIVE MEMBERS");

            deptlist.add(so);

            json = new JSONArray(deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @RequestMapping(value = "GetPAROfficeListJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPAROfficeList(@RequestParam("deptcode") String deptcode) {

        JSONArray json = null;

        List offlist = new ArrayList();
        try {
            offlist = sactionedAuthorityDao.getOfficeList(deptcode);

            json = new JSONArray(offlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /*@RequestMapping(value = "getPostList", method = RequestMethod.GET)
     public ModelAndView getpostlist(@RequestParam Map requestparams) {
        
     ModelAndView mav = new ModelAndView();
     String path = "/preferauthority/PreferAuthority";
     try{
     String dept = (String)requestparams.get("dept");
     String off = (String)requestparams.get("off");
     
     List<SubstantivePost> postlist = sactionedAuthorityDao.getPostList(dept, off);
     mav.addObject("poslist",postlist);
     }catch(Exception e){
     e.printStackTrace();
     }finally{
            
     }
     mav.setViewName(path);
     return mav;
     }*/
    @ResponseBody
    @RequestMapping(value = "getPARPostListJSON", method = RequestMethod.POST)
    public void getpostlistJSON(HttpServletResponse response, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode, @RequestParam("spc") String spc) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            
            List postlist = sactionedAuthorityDao.getPostList(deptCode, offCode);
            json.put("total", 10);
            json.put("rows", postlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getGPCListOfficeWiseJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getGPCListOfficeWiseJSON(HttpServletResponse response, @RequestParam("deptcode") String deptcode) {
        JSONArray json = null;
        try {
            List gpclist = sactionedAuthorityDao.getGPCListOfficeWise(deptcode);
            json = new JSONArray(gpclist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "getPostListGPCWiseAuthorityJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPostListGPCWiseAuthorityJSON(HttpServletResponse response, @RequestParam("gpc") String gpc, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = sactionedAuthorityDao.getPostListGPCWiseAuthority(gpc, offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "getPostListWithoutAuthorityJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPostListWithoutAuthorityJSON(HttpServletResponse response, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = sactionedAuthorityDao.getPostListWithoutAuthority(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "addAuthoritySPC", method = RequestMethod.POST)
    public ModelAndView addAuthorityPost(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("WorkflowAuthority") WorkflowAuthority wa, @RequestParam Map<String, String> requestParams) {

        String path = "/preferauthority/SearchSancAuthority";

        String[] auth = wa.getChkAuth().split(",");
        ModelAndView mav = new ModelAndView();
        try {
            
            sactionedAuthorityDao.addAuthoritySPC(auth, wa.getSpc(), 3, wa.getHidDeptCode(), wa.getHidOffCode());
            mav.addObject("spc", wa.getSpc());
            mav.addObject("processCode", requestParams.get("hidprocessCode"));
            mav.addObject("fslYear", requestParams.get("hidfslYear"));
            mav.addObject("authType", requestParams.get("hidauthType"));
            mav.addObject("rowid", requestParams.get("hidrowid"));
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "addAuthoritySPCJson", method = RequestMethod.POST)
    public void addAuthoritySPCJson(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("preferedpostcode") String preferedpostcode, @ModelAttribute("WorkflowAuthority") WorkflowAuthority wa) {
        String[] vpreferedpostcode = preferedpostcode.split(",");
        sactionedAuthorityDao.addAuthoritySPC(vpreferedpostcode, wa.getSpc(), 3, wa.getHidDeptCode(), wa.getHidOffCode());
        //sactionedAuthorityDao.addAuthoritySPC(auth, wa.getSpc(), 3, wa.getHidDeptCode(), wa.getHidOffCode());
    }

    @ResponseBody
    @RequestMapping(value = "GetSanctionedAuthorityListInitiatePARJSON")
    public void getsanctionauthoritylistInitiatePARJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams, @RequestParam("processid") int processid, @RequestParam("fiscalYear") String fiscalYear, @RequestParam("spc") String spc) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            List sanctionedauthoritylist = sactionedAuthorityDao.getSanctionedAuthorityListInitiatePAR(spc, processid, fiscalYear, lub.getLoginempid());
            json.put("total", 10);
            json.put("rows", sanctionedauthoritylist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetSanctionedAuthorityListInitiatePAR")
    public ModelAndView getsanctionedauthoritylistInitiatePAR(@RequestParam("authType") String authType, @RequestParam("rowid") int rowid, @RequestParam("processCode") int processCode, @RequestParam("fslYear") String fslYear, @RequestParam("parId") int parId) {

        ModelAndView mav = new ModelAndView();
        try {
            String spc = sactionedAuthorityDao.getApplicantSPC(parId);

            String path = "/preferauthority/SearchSancAuthorityInitiatePAR";

            mav.addObject("authType", authType);
            mav.addObject("rowid", rowid);
            mav.addObject("processCode", processCode);
            mav.addObject("fslYear", fslYear);
            mav.addObject("spc", spc);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addAuthorityInitiatePAR")
    public ModelAndView addauthorityInitiatePAR(@ModelAttribute("WorkflowAuthority") WorkflowAuthority wa, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("spc", wa.getSpc());
        mav.addObject("processCode", requestParams.get("hidprocessCode"));
        mav.addObject("fiscalYear", requestParams.get("hidfslYear"));
        mav.addObject("authType", requestParams.get("hidauthtype"));
        mav.addObject("rowid", requestParams.get("hidrowid"));
        String path = "/preferauthority/PreferAuthorityInitiatePAR";
        mav.setViewName(path);
        return mav;
    }
}

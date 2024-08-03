/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Durga
 */
@Controller
@SessionAttributes("LoginUserBean")
public class SPCMasterController implements ServletContextAware {

    @Autowired
    SectionDefinationDAO sectionDefinationDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    OfficeDAO officeDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    String offCode = "";

    @ResponseBody
    @RequestMapping(value = "getPostListLoanJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPostListMasterJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getSPCList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    //String offCode, String postCode
    @ResponseBody
    @RequestMapping(value = "getEmployeeNameWithSPCJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getEmployeeNameWithSPCJSON(@RequestParam("offcode") String offcode, @RequestParam("postCode") String postCode) {
        JSONArray json = null;
        try {
            List spnlist = subStantivePostDao.getEmployeeNameWithSPC(offcode, postCode);
            json = new JSONArray(spnlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeWithSPCList", method = {RequestMethod.GET, RequestMethod.POST})
    public String getEmployeeWithSPCList(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getEmployeeWithSPCList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getAllSPCWithEmployee", method = {RequestMethod.GET, RequestMethod.POST})
    public String getAllSPCWithEmployee(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getAllSPCWithEmployee(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getTrainingPostListJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPostListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getSPCList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getCadreWiseOfficeWiseSPC", method = {RequestMethod.GET, RequestMethod.POST})
    public String getCadreWiseOfficeWiseSPC(@RequestParam("offcode") String offcode, @RequestParam("cadrecode") String cadrecode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getCadreWiseOfficeWiseSPC(cadrecode, offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "getSPCList")
    public String getOfficeList(@RequestParam("offcode") String offcode) {

        this.offCode = offcode;
        return "/post/Post";

    }

    @ResponseBody
    @RequestMapping(value = "getSPCListJSONPaging")
    public String getSPCListJSONPaging(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        JSONObject json = new JSONObject();
        int total = 0;
        try {
            int page = Integer.parseInt(requestParams.get("page"));
            int rows = Integer.parseInt(requestParams.get("rows"));
            String postToSearch = requestParams.get("postToSearch");

            List postlist = subStantivePostDao.getPostListPaging(offCode, postToSearch, page, rows);
            total = subStantivePostDao.getPostListCountPaging(offCode, postToSearch);

            json.put("rows", postlist);
            json.put("total", total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getPostCodeListJSON")
    public String getPostCodeListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getGenericPostList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getPostCodeWiseEmployeeListJSON")
    public String GetPostCodeWiseEmployeeListJSON(@RequestParam("postcode") String postcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List emplist = subStantivePostDao.getGPCWiseEmployeeList(postcode, offcode);
            json = new JSONArray(emplist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getPostCodeWiseEmployeeListSPCJSON")
    public String GetPostCodeWiseEmployeeListSPCJSON(@RequestParam("postcode") String postcode) {
        JSONArray json = null;
        try {
            List emplist = subStantivePostDao.getGPCWiseEmployeeListOnlySPC(postcode);
            json = new JSONArray(emplist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getOfficeWithSPCList")
    public String getOfficeWithSPCList(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getOfficeWithSPCList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getSanctioningSPCOfficeWiseListJSON")
    public String GetSanctioningSPCOfficeWiseListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getSanctioningSPCOfficeWiseList(offcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeWithSPCOfficeWiseListJSON")
    public String GetEmployeeWithSPCOfficeWiseList(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getEmployeeWithSPCOfficeWiseList(offcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getApprovingSPCOfficeWiseListJSON")
    public String getApprovingSPCOfficeWiseListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getApprovingSPCOfficeWiseList(offcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getSanctioningSPCOfficeWiseListJSONF2")
    public String getSanctioningSPCOfficeWiseListJSONF2(@RequestParam("deptcode") String deptcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getSanctioningSPCOfficeWiseList(deptcode, offcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "getAvailablePostList")
    public String getAvailablePostList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost) {

        List deptlist = null;
        List postlist = null;
        try {

            postlist = sectionDefinationDAO.getTotalAvailableEmp(lub.getLoginoffcode());
            model.addAttribute("postlist", postlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/AvailablePostList";
    }

    @RequestMapping(value = "GPCWiseSPCList")
    public String GPCWiseSPCList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost) {

        List deptlist = null;
        List postlist = null;
        try {
            deptlist = departmentDao.getDepartmentList();
            model.addAttribute("deptlist", deptlist);

            postlist = subStantivePostDao.getPostCodeOfficeWise(lub.getLogindeptcode());
            model.addAttribute("postlist", postlist);

            model.addAttribute("aerId", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/master/SubstantivePostEdit";
    }

    @RequestMapping(value = "saveSubstantivePost")
    public String saveSubstantivePost(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost, @RequestParam("btnSpc") String btnSpc) {

        List deptlist = null;
        List postlist = null;
        List spnList = null;
        List payscaleList = null;
        int retVal = 0;
        try {
            deptlist = departmentDao.getDepartmentList();
            model.addAttribute("deptlist", deptlist);
            postlist = subStantivePostDao.getPostCodeOfficeWise(lub.getLogindeptcode());
            model.addAttribute("postlist", postlist);
            if (btnSpc != null && btnSpc.equals("List")) {
                spnList = subStantivePostDao.getSPCListWithEmployeeName(lub.getLoginoffcode(), substantivePost.getPostCode());
                model.addAttribute("spnList", spnList);

                payscaleList = payscaleDAO.getPayScaleList();
                model.addAttribute("payscaleList", payscaleList);
            } else if (btnSpc != null && btnSpc.equals("Update")) {
                if (substantivePost.getMode() != null && substantivePost.getMode().equals("single")) {
                    retVal = subStantivePostDao.updateSubstantivePost(lub.getLoginoffcode(), substantivePost.getPostCode(), substantivePost.getSpc(), substantivePost.getPayscale(), substantivePost.getPayscale_7th(), substantivePost.getPostgrp(), substantivePost.getPaylevel(), substantivePost.getGp(), substantivePost.getCadre(), substantivePost.getChkGrantInAid(), substantivePost.getChkTeachingPost(), substantivePost.getChkPlanOrNonPlan());
                } else if (substantivePost.getMode() != null && substantivePost.getMode().equals("all")) {
                    retVal = subStantivePostDao.updateSubstantivePost(lub.getLoginoffcode(), substantivePost.getPostCode(), null, substantivePost.getPayscale(), substantivePost.getPayscale_7th(), substantivePost.getPostgrp(), substantivePost.getPaylevel(), substantivePost.getGp(), substantivePost.getCadre(), substantivePost.getChkGrantInAid(), substantivePost.getChkTeachingPost(), substantivePost.getChkPlanOrNonPlan());
                }
                model.addAttribute("retVal", retVal);
            } else if (btnSpc != null && btnSpc.equals("Remove")) {
                String filepath = context.getInitParameter("AERPath");
                retVal = subStantivePostDao.removeSubstantivePost(lub.getLoginoffcode(), substantivePost.getPostCode(), substantivePost.getSpc(), filepath, substantivePost.getSpcTerminateFileAttch(), substantivePost.getTerminationOrderNo(), substantivePost.getTerminationOrderDate());
            } else if (btnSpc != null && btnSpc.equals("AddPost")) {
                String filepath = context.getInitParameter("AERPath");
                retVal = subStantivePostDao.addSubstantivePost(lub.getLogindeptcode(), lub.getLoginoffcode(), substantivePost, filepath);
            }
            model.addAttribute("aerId", substantivePost.getAerId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/master/SubstantivePostEdit";
    }

    @RequestMapping(value = "substantivePost", method = {RequestMethod.GET, RequestMethod.POST})
    public String substansivePost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") || (lub.getLoginusertype().equalsIgnoreCase("D") && lub.getLoginuserid().contains("dc"))) {
            path = "/master/SubstantivePost";
            model.addAttribute("userType", lub.getLoginusertype());
            model.addAttribute("deptList", departmentDao.getDepartmentList());
        } else {
            path = "under_const";
        }
        return path;
    }

    @RequestMapping(value = "substantivePostDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public String substantivePostDetails(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response, @RequestParam("hiddenDeptName") String DeptName, @RequestParam("hiddenOfficeName") String officeName) {

        if (DeptName != null && !DeptName.equals("")) {
            model.addAttribute("DeptName", DeptName);
        } else {
            model.addAttribute("DeptName", departmentDao.getDeptName(substantivePost.getDeptCode()));
        }
        if (officeName != null && !officeName.equals("")) {
            model.addAttribute("officeName", officeName);
        } else {
            Office offForm = officeDao.getOfficeCodeDetails(substantivePost.getOffCode());
            model.addAttribute("officeName", offForm.getOffEn());
        }
        model.addAttribute("subList", subStantivePostDao.listSubPost(substantivePost));
        // model.addAttribute("userType", lub.getLoginusertype());
        model.addAttribute("userName", lub.getLoginuserid());
        String path = "/master/SubstantivePostDetails";
        return path;

    }

    @RequestMapping(value = "addPostdetails", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addPostdetails(ModelMap model, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response, @RequestParam("deptCode") String deptCode, @RequestParam("officeCode") String officeCode, @RequestParam("postCode") String postCode) {
        model.addAttribute("deptCode", deptCode);
        model.addAttribute("officeCode", officeCode);
        model.addAttribute("postCode", postCode);
        model.addAttribute("postDetails", subStantivePostDao.postDetails(officeCode, postCode));

        return new ModelAndView("/master/AddPostDetails");
    }

    @RequestMapping(value = "savePostdataDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView savePostdataDetails(ModelMap model, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response) {

        subStantivePostDao.savePostdataDetails(substantivePost);
        model.addAttribute("deptList", departmentDao.getDepartmentList());
        // String path = "/privilege/SubstantivePost";
        return new ModelAndView("/master/SubstantivePost");
    }

    @RequestMapping(value = "postToggleList")
    public ModelAndView postToggleList(@ModelAttribute("substantivePost") SubstantivePost substantivePost, @RequestParam Map<String, String> req) {
        //@RequestParam("postcode") String postcode, @RequestParam("departmentId") String departmentId
        String postCode = req.get("postCode");
        String deptCode = req.get("deptCode");
        String offCode = req.get("offCode");
        String offName = req.get("officeName");
        String postName = req.get("postname");
        String path = "/master/PostToggleList";

        ModelAndView mv = new ModelAndView();
        mv.addObject("spcList", subStantivePostDao.getSPCList(offCode, postCode));
        String departmentName = subStantivePostDao.getDeptName(deptCode);
        mv.addObject("officeName", offName);
        mv.addObject("postName", postName);
        mv.addObject("deptName", departmentName);
        mv.setViewName(path);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "ToggleSPCstatus")
    public void ToggleSPCstatus(@RequestParam Map<String, String> req) {
        String columnName = req.get("columnName");
        String spc = req.get("spc");
        String colStatus = req.get("colStatus");
        subStantivePostDao.changeSPCstatus(columnName, spc, colStatus);
    }

    @ResponseBody
    @RequestMapping(value = "getAuthorityPostListJSON")
    public String GetAuthorityPostListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List gpclist = subStantivePostDao.getAuthorityGenericPostList(offcode);
            json = new JSONArray(gpclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getAuthoritySubstantivePostListJSON")
    public String GetAuthoritySubstantivePostListJSON(@RequestParam("postcode") String postcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getAuthoritySubstantivePostList(offcode, postcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getVacantSubstantivePostListJSON")
    public String GetVacantSubstantivePostListJSON(@RequestParam("postcode") String postcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getVacantSubstantivePostList(offcode, postcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getSubstantivePostListBacklogEntryJSON")
    public String GetSubstantivePostListBacklogEntryJSON(@RequestParam("postcode") String postcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = subStantivePostDao.getSubstantivePostListBacklogEntry(offcode, postcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "duplicateSPC.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String duplicateSPC(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response, @RequestParam("officeCode") String officeCode) {

        substantivePost.setHidOffCode(officeCode);
        Office ofc = officeDao.getOfficeNameDeptName(officeCode);
        model.addAttribute("DeptName", ofc.getDeptName());
        model.addAttribute("officeName", ofc.getOffEn());
        model.addAttribute("officeCode", ofc.getOffCode());

        model.addAttribute("dupSPCList", subStantivePostDao.getDuplicatePostList(officeCode));
        model.addAttribute("userName", lub.getLoginuserid());
        //model.addAttribute("userType", lub.getLoginusertype());
        String path = "/master/DuplicateSubstantivePostList";
        return path;

    }

    @RequestMapping(value = "duplicateSpcDetails.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String duplicateSpcDetails(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response,
            @RequestParam("curSpc") String spc, @RequestParam("offCode") String officeCode) {
        model.addAttribute("dupSPClistDetails", subStantivePostDao.getDuplicatePostDetails(spc));
        model.addAttribute("officeCode", officeCode);
        substantivePost.setHidGpc(substantivePost.getGpc());
        substantivePost.setHidOffCode(substantivePost.getOffCode());
        String path = "/master/DuplicateSPCDetails";
        return path;

    }

    @RequestMapping(value = "unmappedSpcAll.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String unmappedSpcAll(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost, BindingResult result, HttpServletResponse response,
            @RequestParam("offCode") String offCode, @RequestParam("gpc") String gpc, @RequestParam("empID") String empId) {
        substantivePost.setHidEmpCode(empId);
        model.addAttribute("blankSPClist", subStantivePostDao.getUnmappedSPCAll(offCode, gpc));
        String path = "/master/modalUnmappedSpc";
        return path;
    }

    @RequestMapping(value = "mappedToBlankSpc.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView mappedToBlankSpc(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("substantivePost") SubstantivePost substantivePost,
            BindingResult result, HttpServletResponse response) {
        String newSpc = substantivePost.getRdSpc();
        String empId = substantivePost.getHidEmpCode();
        substantivePost = subStantivePostDao.getEmpDetails(empId);
        String oldSpc = substantivePost.getSpc();
        String officeCode = substantivePost.getOffCode();
        subStantivePostDao.updateBlankSpc(newSpc, empId, oldSpc, officeCode);
        return new ModelAndView("redirect:/duplicateSpcDetails.htm?curSpc=" + oldSpc + "&offCode=" + officeCode);

    }
    
    @ResponseBody
    @RequestMapping(value = "getGOIOfficeWisePostList")
    public String getGOIOfficeWisePostList(@RequestParam("goioffcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = subStantivePostDao.getGOIOficeWisePostList(offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}

  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.BankDAO;
import hrms.dao.master.BlockDAO;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.SubDivisionDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Block;
import hrms.model.master.Office;
import hrms.model.master.State;
import hrms.model.master.SubDivision;
import hrms.model.master.SubstantivePost;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import jxl.read.biff.BiffException;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Durga
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class OfficeMasterController {

    @Autowired
    OfficeDAO officeDao;
    @Autowired
    DepartmentDAO departmentDao;
    @Autowired
    BankDAO bankDAO;
    @Autowired
    BranchDAO branchDAO;
    @Autowired
    TreasuryDAO treasuryDao;
    @Autowired
    BlockDAO blockDAO;
    @Autowired
    StateDAO stateDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    SubDivisionDAO subDivisionDAO;
    @Autowired
    SubStantivePostDAO substantivePostDAO;
    @Autowired
    DistrictDAO districtDAO;
    @Autowired
    PostDAO postDAO;

    @RequestMapping(value = "getOfficeList")
    public String getOfficeList(@RequestParam("deptcode") String deptcode) {
        return "/office/Office";
    }

    @ResponseBody
    @RequestMapping(value = "getDDOOfficeListJSON")
    public void getTotalDDOOfficeList(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalDDOOfficeList(deptCode);

            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getOfficeListJSON")
    public void getofficelist(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalOfficeList(deptCode);

            json = new JSONArray(officelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /*@ResponseBody
     @RequestMapping(value = "getOtherOrgOfficeListJSON")
     public void getOtherOrgOfficeListJSON(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
     response.setContentType("application/json");
     PrintWriter out = null;
     JSONArray json = null;

     try {
     List officelist = officeDao.getOtherOrgOfficeList(deptCode);

     json = new JSONArray(officelist);
     out = response.getWriter();
     out.write(json.toString());
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     out.flush();
     out.close();
     }
     }*/
    @ResponseBody
    @RequestMapping(value = "getDeputedOfficeListJSON")
    public void getDeputedOfficeList(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalDeputedOfficeList(deptCode);

            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getOfficeListDistrictAndDepartmentJSON")
    public void getOfficeListDistrictAndDepartmentJSON(HttpServletResponse response, @RequestParam("deptcode") String deptCode, @RequestParam("distcode") String distcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalOfficeList(deptCode, distcode);

            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getControllingOfficelist")
    public void getControllingOfficelist(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getControllingOfficelist(deptCode);

            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getOfficeListTreasuryWiseJSON")
    public void getOfficeListTreasuryWiseJSON(HttpServletResponse response, @RequestParam("trCode") String trCode, @RequestParam("deptcode") String deptcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getOfficeListTreasuryWise(trCode, deptcode);

            json = new JSONArray(officelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getOfficeListJSONPaging")
    public void getOfficeListJSONPaging(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject json = new JSONObject();
        String deptCode = null;
        int total = 0;
        try {
            int page = Integer.parseInt(requestParams.get("page"));
            int rows = Integer.parseInt(requestParams.get("rows"));
            String offToSearch = requestParams.get("offToSearch");

            List officelist = officeDao.getOfficeList(deptCode, offToSearch, page, rows);
            total = officeDao.getOfficeListCount(deptCode, offToSearch);

            json.put("rows", officelist);
            json.put("total", total);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "officeList")
    public ModelAndView officeList(@ModelAttribute("Office") Office office) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.setViewName("/master/OfficeList");
        return mv;
    }

    @RequestMapping(value = "getDeptWiseOfficeList")
    public ModelAndView getDeptWiseOfficeList(@ModelAttribute("Office") Office office) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("Office", office);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("officeList", officeDao.getTotalOfficeList(office.getDeptCode()));
        mv.setViewName("/master/OfficeList");
        return mv;
    }

    @RequestMapping(value = "editOfficeDetails.htm")
    public ModelAndView editOfficeDetails(@ModelAttribute("editOffice") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;

        //office = officeDao.getOfficeCodeDetails(office.getOffCode());
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            mv = new ModelAndView("/master/EditOfficeDetails", "editOffice", office);
        } else {
            mv = new ModelAndView("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "editOfficeDetails.htm", params = "search")
    public ModelAndView searchOfficeDetails(@ModelAttribute("editOffice") Office office, Map<String, Object> model, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        String offcode = office.getOffCode();
        office = officeDao.getOfficeCodeDetails(offcode);
        office.setHidMode("N");
        office.setOffCode(offcode);
        mv = new ModelAndView("/master/EditOfficeDetails", "editOffice", office);
        return mv;
    }

    @RequestMapping(value = "editOfficeDetails.htm", params = "save")
    public ModelAndView updateOfficeDetails(@ModelAttribute("editOffice") Office office, ModelMap model, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        if (office.getHidOfficeCode() != null && !office.getHidOfficeCode().equals("")) {
            officeDao.updateOfficeName(office);
        }
        mv = new ModelAndView("/master/EditOfficeDetails", "editOffice", office);
        model.addAttribute("msgOfcUpdation", "Office Details Updated");

        return mv;
    }

    @RequestMapping(value = "editOfficeDetails.htm", params = "cancel")
    public ModelAndView Cancel(@ModelAttribute("editOffice") Office office, Map<String, Object> model, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        office.setOffCode(" ");
        office.setOffEn(" ");
        office.setCategory(" ");
        office.setSuffix(" ");
        office.setIsDdo(" ");
        office.setOffStatus(" ");
        office.setOnlineBillSubmission(" ");
        mv = new ModelAndView("/master/EditOfficeDetails", "editOffice", office);
        return mv;
    }

    @RequestMapping(value = "createOfcBackup")
    public ModelAndView CreateBacklogOffice(ModelMap model, @ModelAttribute("editOffice") Office office, @RequestParam Map<String, String> requestParams) throws IOException, BiffException, SQLException {
        ModelAndView mv = null;
        String off_code = office.getHidOfficeCode();
        int cntRefOffice1 = officeDao.cntRefOffCode(off_code);
        office = officeDao.getOfficeCodeDetails(off_code);
        System.out.println("off_code:" + off_code);
        officeDao.saveBacklogOffice(office);
        int cntRefOffice2 = officeDao.cntRefOffCode(off_code);
        if (cntRefOffice2 > cntRefOffice1) {
            office.setHidMode("B");
        } else {
            office.setHidMode("N");
        }
        office.setOffCode(off_code);

        mv = new ModelAndView("/master/EditOfficeDetails", "editOffice", office);
        model.addAttribute("bckupmsg", "BackUP Office Created Successfully");
        return mv;
    }

    @RequestMapping(value = "getOfficeDetail.htm")
    public ModelAndView getOfficeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("officeModel") Office office, Map<String, Object> model) {
        String officecode = selectedEmpOffice;

        if (officecode != null && !officecode.equals("")) {
            String mode = office.getMode();
            office = officeDao.getOfficeDetails(selectedEmpOffice);
            Block block = blockDAO.getBlockDetails(office.getBlockCode());
            office.setBlockName(block.getBlockName());
            State state = stateDAO.getStateDetails(office.getStateCode());
            office.setStateName(state.getStatename());
            SubDivision subDivision = subDivisionDAO.getSubDivisionDetail(office.getSubDivisionCode());
            office.setSubDivisionName(subDivision.getSubDivisionName());
            SubstantivePost spc = substantivePostDAO.getSpcDetail(office.getDdoSpc());
            office.setMode(mode);
            ModelAndView mv = new ModelAndView("/master/OfficeDetail", "officeModel", office);

            mv.addObject("postList", postDAO.getGenericPostList(office.getDeptCode()));
            mv.addObject("bankList", bankDAO.getBankList());
            mv.addObject("branchList", branchDAO.getBranchList(office.getSltBank()));
            mv.addObject("treasuryList", treasuryDao.getTreasuryList());
            mv.addObject("subdivisionList", subDivisionDAO.getSubDivisionList());
            mv.addObject("districtList", districtDAO.getDistrictList());
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.addObject("parentOffList", officeDao.getParentOffice(office.getPoffCode()));
            mv.addObject("employeeList", employeeDAO.getDDOEmployeeList(office.getOffCode()));
            mv.addObject("employeePostCode", employeeDAO.getEmployeePostCode(office.getDdoHrmsid()));
            return mv;
        } else {
            ModelAndView mv = new ModelAndView();
            mv.addObject("postList", postDAO.getGenericPostList(office.getDeptCode()));
            mv.addObject("bankList", bankDAO.getBankList());
            mv.addObject("branchList", branchDAO.getBranchList(office.getSltBank()));
            mv.addObject("treasuryList", treasuryDao.getTreasuryList());
            mv.addObject("subdivisionList", subDivisionDAO.getSubDivisionList());
            mv.addObject("districtList", districtDAO.getDistrictList());
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.addObject("blockList", blockDAO.getBlockList(office.getDistCode()));
            mv.addObject("parentOffList", officeDao.getParentOffice(office.getPoffCode()));
            mv.addObject("employeeList", employeeDAO.getDDOEmployeeList(office.getOffCode()));
            mv.addObject("employeePostCode", employeeDAO.getEmployeePostCode(office.getDdoHrmsid()));
            mv.setViewName("/master/OfficeDetail");
            return mv;
        }
    }

    @RequestMapping(value = "getOfficeDetail.htm", params = "save")
    public ModelAndView saveOfficeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officeModel") Office office) {
        if (lub.getLoginoffcode() != null && !lub.getLoginoffcode().equals("")) {
            officeDao.updateOfficeDetails(office);
        } else {
            officeDao.saveOfficeDetails(office);
        }
        ModelAndView mv = new ModelAndView("/master/OfficeList", "Office", office);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("officeList", officeDao.getTotalOfficeList(office.getDeptCode()));
        return new ModelAndView("redirect:/getOfficeDetail.htm?offCode=" + lub.getLoginoffcode());
    }

    @RequestMapping(value = "getOfficeDetail.htm", params = "back")
    public ModelAndView backToOffice(@ModelAttribute("officeModel") Office office) {

        return new ModelAndView("redirect:/getOfficeDetail.htm?offCode=" + office.getOffCode());
    }

    @RequestMapping(value = "districtOfficeListReport")
    public ModelAndView districtOfficeReport(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String path = "districtreport/DistrictOfficeList";
        List officeList = new ArrayList();
        if (lub.getLoginusertype().equalsIgnoreCase("D")) {
            officeList = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
            path = "districtreport/DistrictOfficeListDC";
        } else {
            Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
            officeList = officeDao.getDistrictWiseOfficeList(office.getDistCode());
        }
        ModelAndView mv = new ModelAndView(path);
        mv.addObject("officeList", officeList);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeePostedOfficeListJSON")
    public void getEmployeePostedOfficeListJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("deptcode") String deptCode) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getEmployeePostedOfficeList(deptCode, lub.getLoginempid(), lub.getLoginoffcode());
            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getEmployeeGenericPostListJSON")
    public String getEmployeeGenericPostListJSON(@RequestParam("empid") String empid) {
        JSONArray json = null;
        try {

            List postDetails = employeeDAO.getEmployeePostCode(empid);
            json = new JSONArray(postDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getControllingOfficelistToADD")
    public void getControllingOfficelistToADD(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {

        response.setContentType("application/json");

        PrintWriter out = null;

        JSONArray json = null;

        try {
            List colist = officeDao.getControllingOfficelistToAdd(deptCode);

            json = new JSONArray(colist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ControllingOfficeDataPage")
    public String ControllingOfficeDataPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("office") Office office) {

        return "/master/ControllingOfficeData";
    }

    @RequestMapping(value = "ControllingOfficeData", params = "btnSubmit=Search")
    public String ControllingOfficeData(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("office") Office office) {
        try {
            ArrayList colist = officeDao.getCOList(lub.getLogindistrictcode(), office.getOffCode());
            model.addAttribute("colist", colist);

            List departmentList = departmentDao.getDepartmentList();
            model.addAttribute("departmentList", departmentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/master/ControllingOfficeData";
    }

    @RequestMapping(value = "ControllingOfficeData", params = "btnSubmit=Add")
    public String ControllingOfficeDataUpdate(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("office") Office office) {
        try {
            int retVal = officeDao.insertCOOffice(office);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ControllingOfficeData.htm?btnSubmit=Search&offCode=" + office.getOffCode();
    }

    @ResponseBody
    @RequestMapping(value = "getOfficeListDistrictAndDepartmentForBacklogEntryJSON")
    public void getOfficeListDistrictAndDepartmentForBacklogEntryJSON(HttpServletResponse response, @RequestParam("deptcode") String deptCode, @RequestParam("distcode") String distcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalOfficeListForBacklogEntry(deptCode, distcode);

            json = new JSONArray(officelist);
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
    @RequestMapping(value = "getofficelistForBacklogEntry")
    public void getofficelistForBacklogEntry(HttpServletResponse response, @RequestParam("deptcode") String deptCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalOfficeListForBacklogEntry(deptCode);

            json = new JSONArray(officelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "newofficecreation.htm")
    public String newOfficeCreation(Model model, @ModelAttribute("newOffice") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String path = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            path = "master/NewOfficeCreation";
        } else {
            path = "/under_const";
        }
        return path;
    }

    @RequestMapping(value = "newofficecreation.htm", params = "search")
    public ModelAndView searchBeforeNewOfficeCreation(Model model, @ModelAttribute("newOffice") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        String search_off_code = office.getOffCode();
        office = officeDao.getOfficeCodeDetails(office.getOffCode());
        office.setOffCode(search_off_code);
        String existOffCode = office.getHidOfficeCode();
        if (existOffCode != null && !existOffCode.equals("")) {
            model.addAttribute("msgofcexist", "Office Already Exist");
        } else {
            model.addAttribute("msgofceNotxist", "Office Not Available");
        }
        mv = new ModelAndView("master/NewOfficeCreation", "newOffice", office);
        mv.addObject("DeptList", departmentDao.getDepartmentList());
        mv.addObject("DistList", districtDAO.getDistrictList());
        mv.addObject("ofcLevelList", officeDao.getOfficeLevelList());
        mv.addObject("statelist", stateDAO.getStateList());
        return mv;
    }

    @RequestMapping(value = "newofficecreation.htm", params = "saveOffice")
    public ModelAndView saveNewOffice(Model model, @ModelAttribute("newOffice") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        if (office.getHidOfficeCode() == null) {
            officeDao.saveNewOffice(office);
            model.addAttribute("msgOfcCreated", "New Office Created");
        }
        mv = new ModelAndView("/master/OfficeCreationList", "Office", office);
        return mv;
    }

    @RequestMapping(value = "newofficecreation.htm", params = "exit")
    public ModelAndView exit(Model model, @ModelAttribute("Office") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        mv = new ModelAndView("/master/OfficeCreationList", "Office", office);
        return mv;
    }

    @RequestMapping(value = "getOfficeCreationRenameBackLog.htm")
    public ModelAndView getOfficeCreationRenameBackLogEntry(Model model, @ModelAttribute("Office") Office office, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            mv = new ModelAndView("/master/OfficeCreationList", "Office", office);
        } else {
            mv = new ModelAndView("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "backlogOfficeCreation.htm")
    public ModelAndView getBacklogOfficeCode(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("backlogOfcForm") Office office) {
        ModelAndView mv = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            if (office.getDeptCode() != null && office.getOffCode() != null) {
                String maxOfficeCode = officeDao.getMaxoffCode(office.getOffCode());
                office.setMaxOfcCode(maxOfficeCode);
            }
            office.setDeptCode(office.getDeptCode());
            office.setOffCode(office.getOffCode());
            mv = new ModelAndView("/master/backlogOffice", "backlogOfcForm", office);
            List DeptList = departmentDao.getAllDepartmentLIst();
            String deptName = departmentDao.getDeptName(office.getDeptCode());
            mv.addObject("DeptList", DeptList);
            mv.addObject("deptname", deptName);
            mv.addObject("offList", officeDao.getTotalOfficeList(office.getDeptCode()));
        } else {
            mv = new ModelAndView("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "createNonexistBacklogOfc.htm")
    public ModelAndView getBacklogOffice(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("backlogOfcForm") Office office) {
        ModelAndView mv = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            office.setMaxOfcCode(office.getMaxOfcCode());
            office.setDeptCode(office.getDeptCode());
            office.setOffCode(office.getOffCode());
            mv = new ModelAndView("/master/backlogOffice", "backlogOfcForm", office);
            if (office.getMaxOfcCode() != null && !office.getMaxOfcCode().equals("")) {
                office.setHidMode("B");
                String mode = office.getHidMode();
                mv.addObject("hiddenmode", mode);
            }
            List DeptList = departmentDao.getAllDepartmentLIst();
            mv.addObject("DeptList", DeptList);
            mv.addObject("offList", officeDao.getTotalOfficeList(office.getDeptCode()));
            mv.addObject("DistList", districtDAO.getDistrictList());
            mv.addObject("ofcLevelList", officeDao.getOfficeLevelList());
            office.setHidDeptCode(office.getDeptCode());
            office.setHidOfficeCode(office.getOffCode());
        } else {
            mv = new ModelAndView("under_const");
        }

        return mv;
    }

    @RequestMapping(value = "backlogOfficeCreation.htm", params = "savebacklogOffice")
    public ModelAndView saveBacklogOffice(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("backlogOfcForm") Office ofc) {
        ModelAndView mv = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") && (lub.getLoginusername().contains("hrmsupport17") || lub.getLoginusername().contains("hrmsupport7") || lub.getLoginusername().contains("hrmsupport3"))) {
            if (ofc.getMaxOfcCode() != null && !ofc.getMaxOfcCode().equals("")) {
                officeDao.saveBackLogOffice(ofc);
            }
            mv = new ModelAndView("/master/backlogOffice", "backlogOfcForm", ofc);
        } else {
            mv = new ModelAndView("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "backlogOfficeCreation.htm", params = "cancel")
    public ModelAndView cancelBacklogOffice(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("backlogOfcForm") Office ofc) {
        ModelAndView mv = null;
        mv = new ModelAndView("/master/backlogOffice", "backlogOfcForm", ofc);
        mv.addObject("DeptList", departmentDao.getAllDepartmentLIst());
        mv.addObject("offList", officeDao.getTotalOfficeList(ofc.getDeptCode()));
        return mv;
    }

    /*@ResponseBody
     @RequestMapping(value = "getStateWiseOfficeListJSON")
     public void getStateWiseOfficeListJSON(HttpServletResponse response, @RequestParam("statecode") String statecode) {
        
     response.setContentType("application/json");
     PrintWriter out = null;
     JSONArray json = null;

     try {
     List officelist = officeDao.getInterStateOfficeList(statecode);

     json = new JSONArray(officelist);
     out = response.getWriter();
     out.write(json.toString());
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     out.flush();
     out.close();
     }
     }*/
    @ResponseBody
    @RequestMapping(value = "getDistwiseOfficeListJSON")
    public void getdistrictwiseOfficelist(HttpServletResponse response, @RequestParam("deptcode") String deptCode, @RequestParam("distCode") String distCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List officelist = officeDao.getTotalOfficeList(deptCode, distCode);

            json = new JSONArray(officelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "billGrpPrivilageList")
    public ModelAndView DeleteEsignBill(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OfcForm") Office ofc) {
        ModelAndView mvc = new ModelAndView();

        mvc.setViewName("/master/DeleteBillGroupPrivilege");
        return mvc;
    }

   @RequestMapping(value = "deleteBillGroupPrivilege.htm")
    public ModelAndView deleteBillGroupPrivilege(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OfcForm") Office ofc) {
        ModelAndView mv = new ModelAndView();
        List billgrpSpcList=new ArrayList();
        billgrpSpcList= officeDao.getBillGroupPrivilegeList(ofc.getOffCode());
        mv.addObject("billgrpspcList",billgrpSpcList);
        ofc.setHidOfficeCode(ofc.getOffCode());
        mv.addObject("officecode", ofc.getHidOfficeCode());
        mv.setViewName("/master/DeleteBillGroupPrivilege");
        return mv;
    }

    @RequestMapping(value = "deleteBillGroup.htm")
    public ModelAndView DeleteAllBillGrpPriv(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OfcForm") Office ofc,@RequestParam("officeCode") String officeCode) {
        ModelAndView mv = new ModelAndView();
        System.out.println("officeCODE:" + officeCode);
        officeDao.deleteBillGroupPrivilegeList(officeCode);

        mv.addObject("msg", "Bill Group Privilege Deleted");

        mv.setViewName("/master/DeleteBillGroupPrivilege");
        return mv;
    }

}

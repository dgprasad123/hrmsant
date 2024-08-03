/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.common.CommonFunctions;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.BillGroup;
import hrms.model.payroll.billbrowser.BillGroupConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice","users"})
public class BillGroupController {

    @Autowired
    BillGroupDAO billGroupDAO;

    @RequestMapping(value = "billGroupAction", method = RequestMethod.GET)
    public ModelAndView billBrowserAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/BillGroupList";
        ArrayList billGroupList=new ArrayList();
        //System.out.println("lub:"+lub.getLoginOffLevel()+selectedEmpOffice+lub.getLoginIsDDOType());
        if(lub.getLoginIsDDOType() != null && lub.getLoginIsDDOType().equals("B") && lub.getLoginOffLevel().equals("20")){
            billGroupList = billGroupDAO.getPOfficeBillGroupList(selectedEmpOffice);
        }else{
            billGroupList = billGroupDAO.getBillGroupList(selectedEmpOffice);
        }    
        

        ModelAndView mav = new ModelAndView();
        mav.addObject("groupList", billGroupList);
        mav.addObject("isDDO", lub.getLoginIsDDOType());
        mav.setViewName(path);
        return mav;

    }
    
    @RequestMapping(value = "billGroupListForLeftOutEmployee", method = RequestMethod.GET)
    public ModelAndView billGroupListForLeftOutEmployee(ModelMap model,  @ModelAttribute("command") BillGroup bg, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) {//
        String path = "/payroll/BillGroupListLeftOutEmployee";
        ArrayList billGroupList = billGroupDAO.getBillGroupList(selectedEmpOffice);

        ModelAndView mav = new ModelAndView(path,"command",bg);
        mav.addObject("groupList", billGroupList);
        
        return mav;

    }

    @RequestMapping(value = "addGroupList")
    public ModelAndView addBillSection(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/AddGroupList";
        ModelAndView mav = new ModelAndView();
        ArrayList planStatusList = billGroupDAO.getPlanStatusList();
        ArrayList billSectorList = billGroupDAO.getSectorList();
        ArrayList billPostClassList = billGroupDAO.getPostClassList();
        ArrayList billTypeList = billGroupDAO.getBillTypeList();
        ArrayList billGroupList = billGroupDAO.getDemandList();
        mav.addObject("billGroupList", billGroupList);
        mav.addObject("billTypeList", billTypeList);
        mav.addObject("billPostClassList", billPostClassList);
        mav.addObject("billSectorList", billSectorList);
        mav.addObject("planStatusList", planStatusList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveGroupSection", params = "action=Save Group", method = RequestMethod.POST)
    public ModelAndView saveGroupSection(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("GroupSection") BillGroup BG, BindingResult result, HttpServletResponse response) {//
        billGroupDAO.saveGroupSection(selectedEmpOffice, BG);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }

    @RequestMapping(value = "saveGroupSection", params = "action=Back", method = RequestMethod.POST)
    public ModelAndView back2BillGroupList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("GroupSection") BillGroup BG, BindingResult result, HttpServletResponse response) {//
        billGroupDAO.saveGroupSection(selectedEmpOffice, BG);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }

    @ResponseBody
    @RequestMapping(value = "getBillGroupList", method = RequestMethod.POST)
    public void getBillYearlList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billGroupList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
        JSONArray json = new JSONArray(billGroupList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getPlanStatusList")
    public void getPlanStatusList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList planStatusList = billGroupDAO.getPlanStatusList();
        JSONArray json = new JSONArray(planStatusList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getSectorList")
    public void getSectorList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billSectorList = billGroupDAO.getSectorList();
        JSONArray json = new JSONArray(billSectorList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getDemandList")
    public void getDemandList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billGroupList = billGroupDAO.getDemandList();
        JSONArray json = new JSONArray(billGroupList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getMajorHeadList")
    public void getMajorHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("demandNo") String demandNo) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getMajorHeadList(demandNo);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getSubMajorHeadList")
    public void getSubMajorHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("demandNo") String demandNo, @RequestParam("majorhead") String majorhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getSubMajorHeadList(demandNo, majorhead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getMinorHeadList")
    public void getMinorHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("majorhead") String majorhead, @RequestParam("submajorhead") String submajorhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getMinorHeadList(majorhead, submajorhead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getSubMinorHeadList")
    public void getSubMinorHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("minorHead") String minorHead, @RequestParam("submajorhead") String submajorhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getSubMinorHeadList(submajorhead, minorHead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getDetailHeadList")
    public void getDetailHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("minorhead") String minorhead, @RequestParam("subhead") String subhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getDetailHeadList(minorhead, subhead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getChargedVotedList")
    public void getChargedVotedList(HttpServletRequest request, HttpServletResponse response, @RequestParam("subminorhead") String subminorhead, @RequestParam("detailhead") String detailhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getChargedVotedList(detailhead, subminorhead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @ResponseBody
    @RequestMapping(value = "getObjectHeadList")
    public void getObjectHeadList(HttpServletRequest request, HttpServletResponse response, @RequestParam("detailhead") String detailhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList objectHeadList = billGroupDAO.getObjectHeadList(detailhead);
        JSONArray json = new JSONArray(objectHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @ResponseBody
    @RequestMapping(value = "getPostClassList")
    public void getPostClassList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billPostClassList = billGroupDAO.getPostClassList();
        JSONArray json = new JSONArray(billPostClassList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getBillTypeList")
    public void getBillTypeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billTypeList = billGroupDAO.getBillTypeList();
        JSONArray json = new JSONArray(billTypeList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "deleteGroupData")
    public ModelAndView deleteGroupData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response, @RequestParam("groupId") String groupId) {
        String ocode = selectedEmpOffice;
        billGroupDAO.deleteGroupData(ocode, groupId);
        return new ModelAndView("redirect:/billGroupAction.htm");

    }

    @RequestMapping(value = "editGroupList")
    public ModelAndView editGroupList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response, @RequestParam("groupId") BigDecimal billgroupid) {//
        String path = "/payroll/EditGroupList";
        ModelAndView mav = new ModelAndView();
        String offcode = selectedEmpOffice;
        ArrayList planStatusList = billGroupDAO.getPlanStatusList();
        ArrayList billSectorList = billGroupDAO.getSectorList();
        ArrayList billPostClassList = billGroupDAO.getPostClassList();
        ArrayList billTypeList = billGroupDAO.getBillTypeList();
        ArrayList billGroupList = billGroupDAO.getDemandList();
        mav.addObject("billGroupList", billGroupList);
        mav.addObject("billTypeList", billTypeList);
        mav.addObject("billPostClassList", billPostClassList);
        mav.addObject("billSectorList", billSectorList);
        mav.addObject("planStatusList", planStatusList);
        BillGroup billGroup = billGroupDAO.getBillGroupDetail(offcode, billgroupid);
        mav.addObject("billGroup", billGroup);
        
        mav.addObject("colist",billGroupDAO.getCOList(selectedEmpOffice));
        
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "assignSectionAction", method = RequestMethod.POST)
    public void assignPostAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("groupId") BigDecimal billgroupid, @RequestParam("sectionId") int sectionId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        
        billGroupDAO.mapSection(billgroupid, sectionId);
        out = response.getWriter();
        out.write("S");
    }

    @ResponseBody
    @RequestMapping(value = "removeSectionAction", method = RequestMethod.POST)
    public void removeSectionAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("sectionId") int sectionId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        billGroupDAO.removeSection(sectionId);
        out = response.getWriter();
        out.write("S");
    }

    @RequestMapping(value = "maptosection")
    public ModelAndView map2Section(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("GroupSection") BillGroup bg, BindingResult result, HttpServletResponse response, @RequestParam("groupId") BigDecimal billgroupid) {//
        ModelAndView mav = new ModelAndView("/payroll/BillMaptoSection");
        List availList = billGroupDAO.getAvailableSectionList(selectedEmpOffice);
        //System.out.println("selectedEmpOffice:"+selectedEmpOffice+";;"+billgroupid);
        String lvl=lub.getLoginOffLevel();
        String isDDO=lub.getLoginIsDDOType();

        List assignedList = billGroupDAO.getAssignedSectionList(billgroupid,selectedEmpOffice,isDDO);
        mav.addObject("assignedList", assignedList);
        mav.addObject("availableList", availList);
        mav.addObject("billgroupid", billgroupid);
        mav.addObject("BillName", billGroupDAO.getBillName(billgroupid));
        return mav;
    }

    @RequestMapping(value = "updateGroupSection", method = RequestMethod.POST)
    public ModelAndView updateGroupSection(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("GroupSection") BillGroup bg, BindingResult result, HttpServletResponse response) {//
        billGroupDAO.updateGroupSection(selectedEmpOffice, bg);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }

    @RequestMapping(value = "configBillGroup")
    public ModelAndView configBillGroup(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @RequestParam("groupId") BigDecimal billgroupid) {//
        ModelAndView mav = new ModelAndView("/payroll/ConfigBillGroup");
        String offcode = selectedEmpOffice;
        BillGroupConfig billGroupConfig = billGroupDAO.getBillConfigData(offcode, billgroupid);
        mav.addObject("billGroupConfig", billGroupConfig);
        mav.addObject("col6List", billGroupDAO.getCol6and7List());
        mav.addObject("col7List", billGroupDAO.getCol6and7List());
        mav.addObject("col16List", billGroupDAO.getColList(16));
        mav.addObject("col17List", billGroupDAO.getColList(17));
        mav.addObject("col18List", billGroupDAO.getColList(18));
        return mav;
    }

    @RequestMapping(value = "saveBillConfiguration")
    public ModelAndView saveBillConfiguration(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("billGroupConfig") BillGroupConfig billGroupConfig) {
        billGroupDAO.saveBillConfigureddata(billGroupConfig);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }

    @RequestMapping(value = "compareBill")
    public ModelAndView compareBill(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/CompareBill";
        ModelAndView mav = new ModelAndView();
        ArrayList billGroupList = billGroupDAO.getBillGroupName(selectedEmpOffice);
        mav.addObject("getBillGroupList", billGroupList);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getBillGroupName")
    public void getBillGroupName(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList getBillGroupList = billGroupDAO.getBillGroupName(selectedEmpOffice);
        JSONArray json = new JSONArray(getBillGroupList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "compareBillDetails")
    public ModelAndView compareBillDetails(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("GroupSection") BillGroup BG, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/CompareBillDetails";
        ArrayList compareData = billGroupDAO.compareBillDetails(selectedEmpOffice, BG);

        ModelAndView mav = new ModelAndView();
        ArrayList getBillGroupList = billGroupDAO.getBillGroupName(selectedEmpOffice);
        JSONArray json = new JSONArray(getBillGroupList);
         mav.addObject("billgroupId", BG.getBillgroupid());
        mav.addObject("fromMonth", BG.getFromMonth());
        mav.addObject("toMonth", BG.getToMonth());
        mav.addObject("fromYear", BG.getFromYear());
        mav.addObject("toYear", BG.getToYear());
        mav.addObject("getBillGroupList", getBillGroupList);
        mav.addObject("compareData", compareData);
        mav.addObject("monthText", CommonFunctions.getMonthAsString(BG.getFromMonth()));
         mav.addObject("monthText1", CommonFunctions.getMonthAsString(BG.getToMonth()));
        mav.setViewName(path);
        return mav;

    }
       @RequestMapping(value = "EmpwiseBillGroup")
    public ModelAndView EmpwiseBillGroup(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/EmpwiseBillGroup";
        ArrayList empList = billGroupDAO.getEmpwiseList(selectedEmpOffice);
        // ArrayList getBillGroupList = billGroupDAO.getBillGroupName(lub.getLoginoffcode());
        ModelAndView mav = new ModelAndView();
        mav.addObject("empList", empList);
        //   mav.addObject("getBillGroupList", getBillGroupList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "empWiseGroupPrivilege")
    public ModelAndView empWiseGroupPrivilege(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response, @RequestParam("spc") String spc) {//
        String path = "/payroll/empWiseGroupPrivilege";
        ArrayList empList = billGroupDAO.getEmpwiseList(selectedEmpOffice);
        ArrayList empWisePrivilege = billGroupDAO.empWiseGroupPrivilege(selectedEmpOffice, spc);
        ModelAndView mav = new ModelAndView();
        mav.addObject("empList", empList);
        mav.addObject("spc", spc);
        mav.addObject("empWisePrivilege", empWisePrivilege);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "assignBillPrivilege")
    public ModelAndView assignBillPrivilege(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response, @RequestParam("billId") String billId, @RequestParam("spc") String spc, @RequestParam("type") int type) {//
        String offcode = selectedEmpOffice;
        billGroupDAO.assignBillPrivilege(billId, spc, type, offcode);
        return new ModelAndView("redirect:/empWiseGroupPrivilege.htm?spc=" + spc);
    }
    
   @RequestMapping(value = "excludeBillGroupFromAquitance")
    public ModelAndView excludeBillGroupFromAquitance(@RequestParam("groupId") BigDecimal billgroupid,@RequestParam("status") String status) {//
        billGroupDAO.excludeBillGroupFromAquitance(billgroupid,status);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }
    
    @RequestMapping(value = "excludeFromTreasury")
    public ModelAndView excludeFromTreasury(@RequestParam("groupId") BigDecimal billgroupid,@RequestParam("status") String status) {//
        billGroupDAO.excludeFromTreasury(billgroupid,status);
        return new ModelAndView("redirect:/billGroupAction.htm");
    }
}

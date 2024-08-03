/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.ChartOfAccount;
import hrms.model.master.Office;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

/**
 *
 * @author devi
 */
@Controller

public class ChartOfAccountController {

    @Autowired
    BillGroupDAO billGroupDAO;

    @Autowired
    DepartmentDAO departmentDAO;
    
    @Autowired
    OfficeDAO offDAO;

    @RequestMapping(value = "ChatOfAccount")
    public ModelAndView ChatOfAccountdetail(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("chatOfAccount", chatOfAccount);
        mav.setViewName("/master/chataccountList");
        return mav;
    }

    @RequestMapping(value = "ChatOfAccount.htm", method = {RequestMethod.POST}, params = {"action=search"})
    public ModelAndView searchChatOfAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount) {
        ModelAndView mav = new ModelAndView("/master/chataccountList","chatOfAccount",chatOfAccount);
        String ddocode = chatOfAccount.getDdoCode();
        chatOfAccount.setHiddendeptCode(chatOfAccount.getHiddendeptCode());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("ChartOfAccountList", billGroupDAO.getChartOfAccountList(chatOfAccount.getDdoCode()));       
        chatOfAccount.setDdoCode(ddocode);
        List officelist = offDAO.getTotalOfficeListForBacklogEntry(chatOfAccount.getHiddendeptCode());
        mav.addObject("officelist", officelist);   
        //mav.setViewName("/master/chataccountList");
        return mav;
    }

    @RequestMapping(value = "addNewChartOfAccount.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newChatOfAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount, HttpServletRequest request,
            HttpServletResponse response,@ModelAttribute("Office") Office office) {
        ModelAndView mav = null;
        String ddocode = chatOfAccount.getDdoCode();
        chatOfAccount.setHiddenddoCode(ddocode);
        
        String deptname=departmentDAO.getDeptName(chatOfAccount.getHiddendeptCode());
        chatOfAccount.setHiddeptName(deptname);       
        office  = offDAO.getOfficeName(ddocode);
        chatOfAccount.setHidddoName(office.getOffEn());
        
        mav = new ModelAndView("/master/AddChartofAccount", "chatOfAccount", chatOfAccount);

        List billDemandList = billGroupDAO.getDemandListDdowise(chatOfAccount.getHiddendeptCode());      
        mav.addObject("billDemandList", billDemandList);
        List majorHeadList = billGroupDAO.getMajorHeadList(chatOfAccount.getDemandno());
        mav.addObject("majorHeadList", majorHeadList);
        List majorHeadList1 = billGroupDAO.getSubMajorHeadListmaster(chatOfAccount.getMajorHead());
        mav.addObject("majorHeadList", majorHeadList1);
       // List subMajorHeadList = billGroupDAO.getSubMajorHeadList(chatOfAccount.getMajorHead(), chatOfAccount.getSubMajorHead());
       // mav.addObject("subMajorHeadList", subMajorHeadList);
        List minorHeadList = billGroupDAO.getMinorHeadListmaster(chatOfAccount.getSubMajorHead(), chatOfAccount.getMinorHead());
        mav.addObject("minorHeadList", minorHeadList);
        List subMinorHeadList = billGroupDAO.getSubMinorHeadList(chatOfAccount.getMinorHead(), chatOfAccount.getSubHead());
        mav.addObject("subMinorHeadList", subMinorHeadList);
        List detailHeadList = billGroupDAO.getDetailHeadList(chatOfAccount.getSubHead(), chatOfAccount.getDetailHead());
        mav.addObject("detailHeadList", detailHeadList);
        List newchargedVotedList = billGroupDAO.getNewChargedVotedList();
        mav.addObject("newchargedVotedList", newchargedVotedList);
        
        List objectHeadList = billGroupDAO.getObjectHeadList(chatOfAccount.getDetailHead());
        mav.addObject("objectHeadList", objectHeadList);

        mav.setViewName("/master/AddChartofAccount");

        return mav;
    }

    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save"})
    public String saveNewChartofAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {
        //ModelAndView mav = new ModelAndView();
        String saveddocode = chatOfAccount.getHiddenddoCode();
        chatOfAccount.setHiddenddoCode(saveddocode);
        String id= chatOfAccount.getChatId();
        chatOfAccount.setChatId(id);

        if (chatOfAccount.getHiddenddoCode() != null && !chatOfAccount.getHiddenddoCode().equals("")
                && chatOfAccount.getChatId() != null && !chatOfAccount.getChatId().equals("")) {
            billGroupDAO.updateChartofAccount(chatOfAccount);
           
        } else {
            billGroupDAO.saveNewChartofAccount(chatOfAccount);
        }
        return "redirect:/ChatOfAccount.htm";
    }
       
   @RequestMapping(value = "editChartOfAccount.htm")
    public ModelAndView editChartOfAccountList(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,@ModelAttribute("Office") Office office) {
        ModelAndView mav = new ModelAndView() ;      
        chatOfAccount = billGroupDAO.editChartOfAccountList(chatOfAccount);
        
        String ddocode = chatOfAccount.getHiddenddoCode();
        chatOfAccount.setHiddenddoCode(ddocode);
        
        office  = offDAO.getOfficeName(ddocode);
        chatOfAccount.setHidddoName(office.getOffEn());
        mav = new ModelAndView("/master/AddChartofAccount","chatOfAccount",chatOfAccount);
        
        chatOfAccount.setHidDemandno(chatOfAccount.getDemandno());
        List billDemandList = billGroupDAO.getDemandList();
        mav.addObject("billDemandList", billDemandList);

        List majHeadList = billGroupDAO.getMajorHeadList(chatOfAccount.getDemandno());
        mav.addObject("majHeadList", majHeadList);

        List majorHeadList1 = billGroupDAO.getSubMajorHeadList(chatOfAccount.getDemandno(), chatOfAccount.getMajorHead());
        mav.addObject("majorHeadList1", majorHeadList1);

        List minorHeadList = billGroupDAO.getMinorHeadList(chatOfAccount.getMajorHead(), chatOfAccount.getSubMajorHead());
        mav.addObject("minorHeadList", minorHeadList);

        List subMinorHeadList = billGroupDAO.getSubMinorHeadList(chatOfAccount.getSubMajorHead(), chatOfAccount.getMinorHead());
        mav.addObject("subMinorHeadList", subMinorHeadList);

        List detailHeadList = billGroupDAO.getDetailHeadList(chatOfAccount.getMinorHead(), chatOfAccount.getSubHead());
        mav.addObject("detailHeadList", detailHeadList);

        List newchargedVotedList = billGroupDAO.getNewChargedVotedList();
        mav.addObject("newchargedVotedList", newchargedVotedList);
       
        return mav;
    }

    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.POST}, params = {"action=Cancel"})
    public ModelAndView cancelChatOfAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("chatOfAccount", chatOfAccount);
        mav.setViewName("/master/chataccountList");
        return mav;
    }
    
     @RequestMapping(value = "addallNewChartofAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addallnewOfAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount, @ModelAttribute("Office") Office office) {
        ModelAndView mav = new ModelAndView();   
        String ddocode = chatOfAccount.getDdoCode();
        chatOfAccount.setHiddenddoCode(ddocode);
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("chatOfAccount", chatOfAccount);       
        String deptname=departmentDAO.getDeptName(chatOfAccount.getHiddendeptCode());
        chatOfAccount.setHiddeptName(deptname);       
        office  = offDAO.getOfficeName(ddocode);
        chatOfAccount.setHidddoName(office.getOffEn());
        mav.setViewName("/master/addNewChartofAccount");
        return mav;
    }
    
    
  /*  @RequestMapping(value = "addallNewChartofAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save"})
    public String saveallNewChartofAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {
        //ModelAndView mav = new ModelAndView();
        String saveddocode = chatOfAccount.getHiddenddoCode();
        chatOfAccount.setHiddenddoCode(saveddocode);
        String id= chatOfAccount.getChatId();
        chatOfAccount.setChatId(id);
        billGroupDAO.saveallNewChartofAccount(chatOfAccount);        
        return "redirect:/ChatOfAccount.htm?action=search&ddoCode="+chatOfAccount.getHiddenddoCode();
    } */
    @RequestMapping(value = "addallNewChartofAccount", method = {RequestMethod.POST}, params = {"action=Cancel"})
    public ModelAndView cancelallChatOfAccount(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("chatOfAccount", chatOfAccount);
        mav.setViewName("/master/chataccountList");
        return mav;
    }
    
    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Majorhead"})
    public String saveMajorhead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  

        billGroupDAO.saveMajorHead(chatOfAccount);
       
        return "redirect:/addNewChartOfAccount.htm?action=Save Majorhead&ddoCode="+chatOfAccount.getHiddenddoCode() + "&hiddendeptCode="+chatOfAccount.getHiddendeptCode();
    }
    
    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save SubMajorhead"})
    public String savesubMajorhead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  
        
        billGroupDAO.saveSubMajorHead(chatOfAccount);     
        return "redirect:/addNewChartOfAccount.htm?action=Save SubMajorhead&ddoCode="+chatOfAccount.getHiddenddoCode() + "&hiddendeptCode="+chatOfAccount.getHiddendeptCode();
    }
    
    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Minorhead"})
    public String saveMinorhead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  
 
        billGroupDAO.saveMinorHead(chatOfAccount);   
        return "redirect:/addNewChartOfAccount.htm?action=Save Minorhead&ddoCode="+chatOfAccount.getHiddenddoCode() + "&hiddendeptCode="+chatOfAccount.getHiddendeptCode();
    }
    
    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Subhead"})
    public String saveSubhead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  
 
        billGroupDAO.saveSubHead(chatOfAccount);
        
        return "redirect:/addNewChartOfAccount.htm?action=Save Subhead&ddoCode="+chatOfAccount.getHiddenddoCode() + "&hiddendeptCode="+chatOfAccount.getHiddendeptCode();
       
    }
    
    @RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Detailhead"})
    public String saveDetailhead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  
 
        billGroupDAO.saveDetailHead(chatOfAccount);
        return "redirect:/addNewChartOfAccount.htm?action=Save Detailhead&ddoCode="+chatOfAccount.getHiddenddoCode() + "&hiddendeptCode="+chatOfAccount.getHiddendeptCode();
    }
    
    /*@RequestMapping(value = "addChatOfAccount", method = {RequestMethod.GET,RequestMethod.POST}, params = {"action=Save Objecthead"})
    public String saveObjecthead(ModelMap model, @ModelAttribute("chatOfAccount") ChartOfAccount chatOfAccount,HttpServletRequest request,
            HttpServletResponse response) {  
 
        billGroupDAO.saveObjectHead(chatOfAccount);
        return "redirect:/ChatOfAccount.htm";
    } */
    
    @ResponseBody
    @RequestMapping(value = "getSubMajorHeadListmaster")
    public void getSubMajorHeadListmaster(HttpServletRequest request, HttpServletResponse response, @RequestParam("majorhead") String majorhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList majorHeadList = billGroupDAO.getSubMajorHeadListmaster(majorhead);
        JSONArray json = new JSONArray(majorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }
    
    @ResponseBody
    @RequestMapping(value = "getMinorHeadListmaster")
    public void getMinorHeadListmaster(HttpServletRequest request, HttpServletResponse response, @RequestParam("majorhead") String majorhead, @RequestParam("submajorhead") String submajorhead) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList minorHeadList = billGroupDAO.getMinorHeadListmaster(majorhead, submajorhead);
        JSONArray json = new JSONArray(minorHeadList);
        out = response.getWriter();
        out.write(json.toString());
    }
}

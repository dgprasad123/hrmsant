/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.dao.master.PayScaleDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.SubstantivePost;
import hrms.model.payroll.billbrowser.SectionDefinition;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Manas Jena
 *
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class BillSectionController {

    @Autowired
    SectionDefinationDAO sectionDefinationDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @RequestMapping(value = "billSectionAction")
    public ModelAndView billBrowserAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("SectionDefinition") SectionDefinition sectionDefinition, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/SectionDefination";
        ArrayList sectionList = sectionDefinationDAO.getSectionList(selectedOfficeCode);
        ModelAndView mav = new ModelAndView();
        mav.addObject("sectionList", sectionList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "move2sectionModal")
    public ModelAndView move2sectionModal(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("SectionDefinition") SectionDefinition sectionDefinition, BindingResult result, HttpServletResponse response, @RequestParam("sectionId") int sectionId, @RequestParam("spcs") String spcs) {//
        String path = "/payroll/AssignEmployee2SectionModal";
        ArrayList sectionList = sectionDefinationDAO.getSectionListExcludeCurrentSection(selectedOfficeCode, sectionId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("sectionList", sectionList);
        mav.addObject("unmapSpcCode", spcs);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "move2sectionModalFromavailableList")
    public ModelAndView move2sectionModalFromavailableList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("SectionDefinition") SectionDefinition sectionDefinition, BindingResult result, HttpServletResponse response, @RequestParam("spcs") String spcs) {//
        String path = "/payroll/AvailablePostListForSectionList";
        ArrayList sectionList = sectionDefinationDAO.getSectionList(selectedOfficeCode);
        ModelAndView mav = new ModelAndView();
        mav.addObject("sectionList", sectionList);
        mav.addObject("unmapSpcCode", spcs);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "move2sectionFromPostList", method = RequestMethod.POST)
    public void move2sectionFromPostList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        //ArrayList sectionList = sectionDefinationDAO.getSectionList(selectedOfficeCode);
        try {
            String spc = requestParams.get("spc");
            int destinationSectionid = Integer.parseInt(requestParams.get("sectionId"));
            String status = "";
            //String allowPostMap = sectionDefinationDAO.verifyMapPost(destinationSectionid, spc);
            String allowPostMap = "Y";
            if (allowPostMap.equals("Y")) {
                status = "{status:'Y'}";
                //sectionDefinationDAO.removePost(spc);
                sectionDefinationDAO.mapPost(destinationSectionid, spc);
                JSONObject json = new JSONObject(status);
                out = response.getWriter();
                out.write(json.toString());
            } else if (allowPostMap.equals("N")) {
                status = "{status:'N'}";
                JSONObject json = new JSONObject(status);
                out = response.getWriter();
                out.write(json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //uityuiu
    }

    @ResponseBody
    @RequestMapping(value = "move2sectionAfterFreeze", method = RequestMethod.POST)
    public void move2sectionAfterFreeze(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        //ArrayList sectionList = sectionDefinationDAO.getSectionList(selectedOfficeCode);       
        try {
            String spc = requestParams.get("spc");
            int destinationSectionid = Integer.parseInt(requestParams.get("sectionId"));
            String status = "";
            String allowPostMap = sectionDefinationDAO.verifyMapPost(destinationSectionid, spc);
            if (allowPostMap.equals("Y")) {
                status = "{status:'Y'}";
                sectionDefinationDAO.removePost(spc);
                sectionDefinationDAO.mapPost(destinationSectionid, spc);
                JSONObject json = new JSONObject(status);
                out = response.getWriter();
                out.write(json.toString());
            } else if (allowPostMap.equals("N")) {
                status = "{status:'N'}";
                JSONObject json = new JSONObject(status);
                out = response.getWriter();
                out.write(json.toString());
            } else if (allowPostMap.equals("I")) {
                status = "{status:'I'}";
                JSONObject json = new JSONObject(status);
                out = response.getWriter();
                out.write(json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //uityuiu
    }

    @RequestMapping(value = "billSectionAction", params = "action=Reset Position")
    public String resetPosition(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("SectionDefinition") SectionDefinition sectionDefinition, final RedirectAttributes redirectAttrs, BindingResult result, HttpServletResponse response) {
        sectionDefinationDAO.resetPostSlNumber(selectedOfficeCode, sectionDefinition.getSectionId());
        redirectAttrs.addFlashAttribute("sectionId", sectionDefinition.getSectionId());
        return "redirect:/sectionMapping.htm?sectionId=" + sectionDefinition.getSectionId();
    }

    @RequestMapping(value = "sectionMapping")
    public ModelAndView sectionMapping(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("SectionDefinition") SectionDefinition sectionDefinition, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        String billtype = sectionDefinationDAO.getBillType(sectionDefinition.getSectionId());
        String path = null;
        System.out.println("LoginDeptcode:" + lub.getLogindeptcode());

        boolean aerstatus = sectionDefinationDAO.getAerReportSubmittedStatus(selectedOfficeCode);

        if (billtype == null || billtype.equals("") || billtype.equals("REGULAR")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailableEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignEmp(selectedOfficeCode, sectionDefinition.getSectionId());
            List payscaleList = payscaleDAO.getPayScaleList();
            mav.addObject("payscaleList", payscaleList);
            mav.addObject("empType", "R");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefinitionMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("CONT6_REG")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailableSixYearContractEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignEmp(selectedOfficeCode, sectionDefinition.getSectionId());

            mav.addObject("empType", "R");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefinitionMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("CONTRACTUAL")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailableContractEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignContractEmp(selectedOfficeCode, sectionDefinition.getSectionId());

            mav.addObject("empType", "C");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sizeofList", assignEmpList.size() - 1);
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            path = "/payroll/SectionDefContractMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("XCADRE")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailableContractEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignContractEmp(selectedOfficeCode, sectionDefinition.getSectionId());

            mav.addObject("empType", "B");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sizeofList", assignEmpList.size() - 1);
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefContractMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("NONGOVTAID")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailableContractEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignContractEmp(selectedOfficeCode, sectionDefinition.getSectionId());

            mav.addObject("empType", "D");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sizeofList", assignEmpList.size() - 1);
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefContractMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("SP_CATGORY")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailSpecialCategoryEmp(selectedOfficeCode);
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignSpecialCategoryEmp(selectedOfficeCode, sectionDefinition.getSectionId());

            mav.addObject("empType", "A");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sizeofList", assignEmpList.size() - 1);
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefContractMapping";
            mav.setViewName(path);
        } else if (billtype != null && billtype.equals("DEPUTATION")) {
            ArrayList availableEmpList = sectionDefinationDAO.getTotalAvailDeputationEmp(lub.getLogindeptcode());
            ArrayList assignEmpList = sectionDefinationDAO.getTotalAssignDeputationEmp(sectionDefinition.getSectionId());

            mav.addObject("empType", "F");
            mav.addObject("availableEmpList", availableEmpList);
            mav.addObject("assignEmpList", assignEmpList);
            mav.addObject("sectionId", sectionDefinition.getSectionId());
            mav.addObject("sizeofList", assignEmpList.size() - 1);
            mav.addObject("sectionName", sectionDefinationDAO.getSectionName(sectionDefinition.getSectionId()));
            mav.addObject("selectedOffice", selectedOfficeCode);
            mav.addObject("aersubmittedstatus", aerstatus);
            path = "/payroll/SectionDefContractMapping";
            mav.setViewName(path);
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getBillSectionList", method = RequestMethod.POST)
    public void getBillYearlList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList sectionList = sectionDefinationDAO.getSectionList(selectedOfficeCode);
        JSONArray json = new JSONArray(sectionList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "verifyAssignPostAction")
    public void verifyAssignPostAction(@RequestParam("spc") String spc, @RequestParam("sectionId") int sectionId, HttpServletResponse response) throws IOException, JSONException {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject json = null;

        String status = "";
        String allowPostMap = sectionDefinationDAO.verifyMapPost(sectionId, spc);
        if (allowPostMap.equals("Y")) {
            status = "{status:'Y'}";
        } else if (allowPostMap.equals("N")) {
            status = "{status:'N'}";
        }

        json = new JSONObject(status);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "assignPostAction", method = RequestMethod.POST)
    public void assignPostAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("spc") String spc, @RequestParam("sectionId") int sectionId, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        String allowPostMap = sectionDefinationDAO.verifyMapPost(sectionId, spc);
        //System.out.println("spc:" + spc + "sect:" + sectionId + "allowPostMap:" + allowPostMap);

        if (allowPostMap.equals("Y")) {
            sectionDefinationDAO.mapPost(sectionId, spc);
        }
        out = response.getWriter();
        out.write("S");
    }

    @ResponseBody
    @RequestMapping(value = "removePostAction", method = RequestMethod.POST)
    public void removePostAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("spc") String spc, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = null;
        sectionDefinationDAO.removePost(spc);
        out = response.getWriter();
        out.write("S");
    }

    @RequestMapping(value = "addBillSection")
    public ModelAndView addBillSection(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {//
        String path = "/payroll/AddBillSection";
        boolean isCollegeDHE = false;
        String isDHE = null;
        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        if (isCollegeDHE) {
            isDHE = "B";
        } else {
            isDHE = "N";
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("sectionId", 0);
        mav.setViewName(path);
        mav.addObject("isDHE", isDHE);
        return mav;
    }

    @RequestMapping(value = "saveBillSection", method = RequestMethod.POST)
    public ModelAndView saveBillSection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, @ModelAttribute("billSection") SectionDefinition SD) {

        try {
            String offCode = selectedOfficeCode;
            int sectionId = SD.getSectionId();

            if (sectionId > 0) {
                sectionDefinationDAO.updateBillSection(SD);
            } else {
                sectionDefinationDAO.saveBillSection(offCode, SD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/billSectionAction.htm?result=success");
    }

    @RequestMapping(value = "editBillSection")
    public ModelAndView editBillSection(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response, @RequestParam("sectionId") int sectionId) {
        String path = "/payroll/AddBillSection";
        boolean isCollegeDHE = false;
        String isDHE = null;
        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        if (isCollegeDHE) {
            isDHE = "B";
        } else {
            isDHE = "N";
        }
        SectionDefinition SD = sectionDefinationDAO.getBillSection(sectionId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("section", SD.getSection());
        mav.addObject("billType", SD.getBillType());
        mav.addObject("nofEmp", SD.getNofpost());
        mav.addObject("sectionId", sectionId);
        mav.addObject("isDHE", isDHE);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "setPositionPostUpDown")
    public void getsetPosition(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedOfficeCode, HttpServletResponse response, @RequestParam Map<String, String> req) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            StringBuffer sb = new StringBuffer();
            sectionDefinationDAO.updatePosition(req.get("positionNo"));

            ArrayList assignEmpList = new ArrayList();

            JSONObject responseDetailsJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            System.out.println("req.get(\"sectionType\") :" + req.get("sectionType"));

            if (req.get("sectionType") != null && req.get("sectionType").equalsIgnoreCase("R")) {
                assignEmpList = sectionDefinationDAO.getTotalAssignEmp(selectedOfficeCode, Integer.parseInt(req.get("sectionId")));
                jsonArray = new JSONArray(assignEmpList);
                responseDetailsJson.put("obj", jsonArray);
            } else {
                assignEmpList = sectionDefinationDAO.getTotalAssignContractEmp(selectedOfficeCode, Integer.parseInt(req.get("sectionId")));
                jsonArray = new JSONArray(assignEmpList);
                responseDetailsJson.put("obj", jsonArray);
            }
            out = response.getWriter();
            out.write(responseDetailsJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getGPCDetail", method = RequestMethod.GET)
    public void getGPCDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("spc") String spc, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            JSONObject json = new JSONObject();

            //sectionDefinationDAO.removePost(spc);
            List jsonlist = new ArrayList();
            SubstantivePost sp = sectionDefinationDAO.getSpcDetails(spc);
            jsonlist.add(sp);
            json.put("sp", jsonlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.getStackTrace();
        }
        //out.write(sp.getGp()+"<==>"+sp.getPostgrp()+"<==>"+sp.getOrderNo()+"<==>"+sp.getOrderDate()+"<==>"+sp.getPayscale()+"<==>"+sp.getPaylevel());
    }

    @ResponseBody
    @RequestMapping(value = "updateGPCDetail", method = RequestMethod.GET)
    public void updateGPCDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("spc") String spc, @RequestParam("orderNo") String orderNo, @RequestParam("orderDate") String orderDate, @RequestParam("payscale") String payscale, @RequestParam("gp") String gp, @RequestParam("paylevel") String paylevel, @RequestParam("postgrp") String postgrp, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        SubstantivePost sp = new SubstantivePost();
        sp.setOrderNo(orderNo);
        sp.setOrderDate(orderDate);
        sp.setSpc(spc);
        sp.setPayscale(payscale);
        sp.setGp(gp);
        sp.setPaylevel(paylevel);
        sp.setPostgrp(postgrp);
        PrintWriter out = null;
        //sectionDefinationDAO.removePost(spc);
        sectionDefinationDAO.updateSpcDetails(sp);
        out = response.getWriter();
        out.write("Data saved successfully.");
    }

    @ResponseBody
    @RequestMapping(value = "getSectionWiseSubstantivePostListJSON")
    public String getSectionWiseSubstantivePostListJSON(@RequestParam("sectionId") int sectionId) {
        JSONArray json = null;
        try {
            List spnlist = sectionDefinationDAO.sectionwiseSubstantivePostList(sectionId);
            json = new JSONArray(spnlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.TransferProposal;

import hrms.common.CommonFunctions;
import hrms.common.Message;
import hrms.dao.TransferProposal.TransferProposalDAO;
import hrms.dao.eDespatch.eDespatchDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.model.TransferProposal.TransferProposalBean;
import hrms.model.eDespatch.eDespatchBean;
import hrms.model.login.LoginUserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class TransferProposalController {

    @Autowired
    public TransferProposalDAO TransferProposalDAO;
    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public eDespatchDAO edespatchDao;
    @Autowired
    public CadreDAO cadreDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @RequestMapping(value = "TransferProposal")
    public ModelAndView viewTransferProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String cadreCode = requestParams.get("cadreCode");
        String proposalId = requestParams.get("proposalId");
        String searchby = requestParams.get("searchby");
        int newProposalId = 0;
        if (proposalId != null && !proposalId.equals("")) {
            newProposalId = Integer.parseInt(proposalId);
        }
        tpBean.setProposalId(newProposalId);
        ModelAndView mv = new ModelAndView("/TransferProposal/TransferProposal", "TransferProposalForm", tpBean);
        if (newProposalId > 0) {
            mv.addObject("tpdList", TransferProposalDAO.getTransferEmployeeList(newProposalId));
        }
        if (cadreCode != null && !cadreCode.equals("")) {
            mv.addObject("cadreCode", cadreCode);
            mv.addObject("tpList", TransferProposalDAO.getEmpList(cadreCode, newProposalId));
        }
        if (searchby != null && !searchby.equals("")) {
            mv.addObject("searchby", searchby);
        }
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("deptlist", deptlist);
        mv.addObject("proposalId", newProposalId);
        return mv;
    }

    @RequestMapping(value = "AuthorityInfo")
    public ModelAndView AuthorityInfo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/TransferProposal/AuthorityInfo", "TransferProposalForm", tpBean);
        
        return mv;
    }
    @RequestMapping(value = "saveOtherInputs")
    public ModelAndView saveOtherInputs(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TransferProposalForm") TransferProposalBean tpBean) {
        ModelAndView mv = new ModelAndView("/TransferProposal/AuthorityInfo", "TransferProposalForm", tpBean);
        TransferProposalDAO.saveOtherInputs(tpBean);
        return mv;
    }
    @RequestMapping(value = "PromotionProposal")
    public ModelAndView promotionProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        String cadreCode = requestParams.get("cadreCode");
        String proposalId = requestParams.get("proposalId");
        int newProposalId = 0;
        if (proposalId != null && !proposalId.equals("")) {
            newProposalId = Integer.parseInt(proposalId);
        }
        tpBean.setProposalId(newProposalId);
        ModelAndView mv = new ModelAndView("/TransferProposal/PromotionProposal", "TransferProposalForm", tpBean);
        if (newProposalId > 0) {
            mv.addObject("tpdList", TransferProposalDAO.getTransferEmployeeList(newProposalId));
            mv.addObject("transferProposalListDetail", TransferProposalDAO.getTransferProposalListDetail(newProposalId));

        }
        if (cadreCode != null && !cadreCode.equals("")) {
            mv.addObject("cadreCode", cadreCode);
            mv.addObject("tpList", TransferProposalDAO.getEmpList(cadreCode, newProposalId));
        }

        List cadrelist = cadreDAO.getCadreList(lub.getLogindeptcode());
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("cadrelist", cadrelist);
        mv.addObject("deptlist", deptlist);
        mv.addObject("proposalId", newProposalId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "AddTransferProposal")
    public void AddTransferProposal(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String cadreCode = requestParams.get("cadreCode");
        String empId = requestParams.get("empId");
        String spc = requestParams.get("spc");
        String gproposalId = requestParams.get("proposalId");
        String hasAdditional = requestParams.get("hasAdditional");
        int proposalId = TransferProposalDAO.addTransferProposal(empId, cadreCode, spc, lub.getLoginempid(), gproposalId, hasAdditional, "T");
        StringBuffer content
                = new StringBuffer(proposalId + "");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "AddPromotionProposal")
    public void AddPromotionProposal(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String cadreCode = requestParams.get("cadreCode");
        String empId = requestParams.get("empId");
        String spc = requestParams.get("spc");
        String gproposalId = requestParams.get("proposalId");
        String hasAdditional = requestParams.get("hasAdditional");
        int proposalId = TransferProposalDAO.addTransferProposal(empId, cadreCode, spc, lub.getLoginempid(), gproposalId, hasAdditional, "P");
        StringBuffer content
                = new StringBuffer(proposalId + "");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "updateNewCadrePromotion")
    public void updateNewCadrePromotion(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int proposalId = TransferProposalDAO.updateNewCadrePromotion(tpBean.getCadreCode(), lub.getLoginempid(), tpBean.getProposalId(), tpBean.getCadreGrade());
        StringBuffer content
                = new StringBuffer(proposalId + "");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getCadreWiseEmpList")
    public void getCadreWiseEmpList(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            ArrayList empList = TransferProposalDAO.getEmpList(tpBean.getCadreCode(), tpBean.getTransferProposalId());
            JSONObject json = new JSONObject();
            PrintWriter out = null;
            json.put("empList", empList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "getCadreGradeWiseEmpList")
    public void getCadreGradeWiseEmpList(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            ArrayList empList = TransferProposalDAO.getCadreGradeWiseEmpList(tpBean.getCadreCode(),tpBean.getCadreGrade(), tpBean.getTransferProposalId());
            JSONObject json = new JSONObject();
            PrintWriter out = null;
            json.put("empList", empList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "deletePostingInfo")
    public void deletePostingInfo(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws JSONException, IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int detailPostingId = Integer.parseInt(requestParams.get("detailPostingId"));
        String status = TransferProposalDAO.deletePostingInfo(detailPostingId);

        Message message = new Message();
        message.setStatus(status);
        JSONObject jsob = new JSONObject(message);
        out = response.getWriter();
        out.write(jsob.toString());
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "GetTransferEmployees")
    public void getTransferEmployees(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String proposalId = requestParams.get("proposalId");
        String rcontent = TransferProposalDAO.getTransferEmployee(Integer.parseInt(proposalId));
        StringBuffer content
                = new StringBuffer(rcontent);
        out.print(content);
        out.close();
        out.flush();
    }

    @RequestMapping(value = "TransferProposalList")
    public ModelAndView transferProposalList(@ModelAttribute("TransferProposalForm") TransferProposalBean tpBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        String strProposalType = "N";
        String proposalType = tpBean.getProposalType();
        System.out.println("proposalType:" + proposalType);
        if (proposalType != null && !proposalType.equals("")) {
            strProposalType = proposalType;
        }
        System.out.println("strProposalType:" + strProposalType);
        tpBean.setProposalType(strProposalType);
        ModelAndView mv = new ModelAndView("/TransferProposal/TransferProposalList", "TransferProposalForm", tpBean);
        mv.addObject("tpList", TransferProposalDAO.getTransferProposalList(lub.getLoginempid(), strProposalType));
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteEmployee")
    public void deleteEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int transferProposalDetailId = Integer.parseInt(requestParams.get("transferProposalDetailId"));
        TransferProposalDAO.deleteEmployee(transferProposalDetailId);
        StringBuffer content
                = new StringBuffer("");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "UpdateNewSPC")
    public void updateNewSPC(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String spc = requestParams.get("newSPC");
        String oldSpc = requestParams.get("oldSpc");
        String transferType = requestParams.get("transferType");
        int transferProposalDetailId = Integer.parseInt(requestParams.get("transferProposalDetailId"));
        String empId = requestParams.get("empId");
        String cadreCode = requestParams.get("cadreCode");
        //,String empId, String cadreCode
        TransferProposalDAO.updateNewSpc(oldSpc, spc, transferProposalDetailId, transferType, empId, cadreCode);
        StringBuffer content
                = new StringBuffer("");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "UpdateNewSPCPromotion")
    public void UpdateNewSPCPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String spc = requestParams.get("newSPC");
        String oldSpc = requestParams.get("oldSpc");
        String transferType = requestParams.get("transferType");
        int promotionProposalDetailId = Integer.parseInt(requestParams.get("promotionProposalDetailId"));
        String empId = requestParams.get("empId");
        String cadreCode = requestParams.get("cadreCode");
        //,String empId, String cadreCode
        //TransferProposalDAO.updateNewSpcPromotion(spc, promotionProposalDetailId);
        TransferProposalDAO.updateNewSpc(oldSpc, spc, promotionProposalDetailId, transferType, empId, cadreCode);
        StringBuffer content = new StringBuffer("");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "DeleteTransferProposal")
    public void deleteTransferProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int proposalId = Integer.parseInt(requestParams.get("proposalId"));
        TransferProposalDAO.deleteTransferProposal(proposalId);
        StringBuffer content
                = new StringBuffer("");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "SaveApproval")
    public void saveApproval(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int proposalId = Integer.parseInt(requestParams.get("proposalId"));
        String orderNumber = requestParams.get("orderNumber");
        String orderDate = requestParams.get("orderDate");
        //TransferProposalDAO.saveApproval(proposalId, orderNumber, orderDate);getAddresseeList
        edespatchDao.genLetterNo("248");
        StringBuffer content
                = new StringBuffer("");
        out.print(content);
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getAddresseeList")
    public void getAddresseeList(HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            String addresseeGroupCode = requestParams.get("addresseeGroupCode");
            List li = edespatchDao.getAddresseeList("2344", addresseeGroupCode);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "showedespatchwindow")
    public ModelAndView showedespatchwindow(HttpServletResponse response, @ModelAttribute("TransferProposalForm") TransferProposalBean tpBean) {
        ModelAndView mv = new ModelAndView("/TransferProposal/EdespatchLetterInterface");
        eDespatchBean eDespatchBean = new eDespatchBean();
        eDespatchBean.setLetterno(edespatchDao.genLetterNo("42924"));
        eDespatchBean.setLetterdate(CommonFunctions.getFormattedInputDate(new Date()));
        eDespatchBean.setProposalId(tpBean.getProposalId());
        List groupList = edespatchDao.getGroupList("248");
        List signingAuthorityList = edespatchDao.getSigningAuthorityList("248");
        List sectionList = edespatchDao.getSectionList("248");
        mv.addObject("eDespatchBean", eDespatchBean);
        mv.addObject("groupList", groupList);
        mv.addObject("signingAuthorityList", signingAuthorityList);
        mv.addObject("sectionList", sectionList);
        return mv;
    }

    @RequestMapping(value = "sendLettereDespatch", params = {"action=Submit"})
    public ModelAndView sendLettereDespatch(HttpServletResponse response, @ModelAttribute("eDespatchBean") eDespatchBean tpBean) {
        ModelAndView mv = new ModelAndView("redirect:TransferProposalList.htm");
        TransferProposalDAO.sendLetterInEdespatch(tpBean);
        return mv;
    }

    @RequestMapping(value = "downloadDraftTransferPDF")
    public ModelAndView downloadDraftTransferPDF(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        String proposalId = requestParams.get("proposalId");
        TransferProposalBean tpf = TransferProposalDAO.getTransferProposalListDetail(Integer.parseInt(proposalId));
        ArrayList transferEmployeeList = TransferProposalDAO.getTransferEmployeeList(Integer.parseInt(proposalId), tpf.getLetterno(), tpf.getFileno());
        ModelAndView mv = new ModelAndView("transferProposalPdfView");
        mv.addObject("transferEmployeeList", transferEmployeeList);
        mv.addObject("tpf", tpf);
        return mv;
    }

    @RequestMapping(value = "downloadDraftPromotionPDF")
    public ModelAndView downloadDraftPromotionPDF(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        String proposalId = requestParams.get("proposalId");
        TransferProposalBean tpf = TransferProposalDAO.getTransferProposalListDetail(Integer.parseInt(proposalId));
        ArrayList transferEmployeeList = TransferProposalDAO.getTransferEmployeeList(Integer.parseInt(proposalId));
        ModelAndView mv = new ModelAndView("promotionProposalPdfView");
        mv.addObject("transferEmployeeList", transferEmployeeList);
        mv.addObject("tpf", tpf);
        return mv;
    }

}

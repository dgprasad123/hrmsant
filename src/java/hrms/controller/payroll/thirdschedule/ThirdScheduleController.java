package hrms.controller.payroll.thirdschedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Message;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListDAO;
import hrms.dao.payroll.thirdschedule.ThirdScheduleDAO;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.thirdschedule.ThirdScheduleForm;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class ThirdScheduleController {

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    public PostDAO postDAO;

    @Autowired
    public ThirdScheduleDAO thirdSchedleDAO;

    @Autowired
    public BillGroupDAO billGroupDAO;

    @Autowired
    public OfficeWiseSecondScheduleListDAO officewisesecondschedulelistDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;
    
    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "ThirdScheduleEmpList")
    public String thirdScheduleEmpList() {

        return "/payroll/thirdschedule/ThirdShceduleList";

    }

    @RequestMapping(value = "getThirdScheduleEmpListJSON")
    public void getThirdScheduleEmpListJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("rows") int rows, @RequestParam("page") int page) {

        response.setContentType("application/json");

        JSONObject json = new JSONObject();
        PrintWriter out = null;

        int total = 0;

        try {
            List empList = thirdSchedleDAO.getEmployeeList(lub.getLoginempid(), rows, page);
            total = thirdSchedleDAO.getEmployeeListCount(lub.getLoginempid());

            json.put("rows", empList);
            json.put("total", total);

            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewThirdSchedule")
    public String ViewThirdSchedule(Model model, @RequestParam("empid") String empid) {

        ThirdScheduleForm tform = null;
        String path = "";

        List postlist2nd = null;
        try {
            tform = thirdSchedleDAO.getThirdScheduleData(empid);

            if (tform != null && tform.getIsIASCadre().equals("Y")) {
                path = "/payroll/thirdschedule/ThirdScheduleViewIAS";
                postlist2nd = postDAO.getPostList(tform.getDeptCodeIAS());
            } else if (tform != null && tform.getIsUGCCadre().equals("Y")) {
                path = "/payroll/thirdschedule/ThirdScheduleViewUGC";
                postlist2nd = postDAO.getPostList(tform.getDeptCode());
            } else {
                path = "/payroll/thirdschedule/ThirdScheduleView";
                postlist2nd = postDAO.getPostList(tform.getDeptCode());
            }

            List deptlist = departmentDao.getDepartmentList();
            List postlist6th = postDAO.getPostList(tform.getEntryDeptCode());

            model.addAttribute("deptlist", deptlist);
            model.addAttribute("PostList2nd", postlist2nd);
            model.addAttribute("PostList6th", postlist6th);
            model.addAttribute("tform", tform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "ThirdschedulePDF")
    public void thirdschedulePDF(HttpServletResponse response, @RequestParam("empid") String empid) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        ThirdScheduleForm tform = null;
        PdfWriter writer = null;
        try {
            String offName = thirdSchedleDAO.getEmployeeOfficeName(empid);

            response.setHeader("Content-Disposition", "attachment; filename=Third_Schedule_" + empid + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ThirdScheduleHeaderFooter(offName, empid));

            document.open();

            tform = thirdSchedleDAO.getThirdScheduleData(empid);

            if (tform != null && tform.getIsIASCadre().equals("Y")) {
                thirdSchedleDAO.thirdScheduleIASPDF(document, tform);
            } else if (tform != null && tform.getIsUGCCadre().equals("Y")) {
                thirdSchedleDAO.thirdScheduleUGCPDF(document, empid);
            } else {
                thirdSchedleDAO.thirdSchedulePDF(document, empid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "saveThirdScheduleData")
    public void SaveThirdScheduleData(HttpServletResponse response, @ModelAttribute("thirdScheduleForm") ThirdScheduleForm tform, @RequestParam("approve") String approve) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            msg = thirdSchedleDAO.saveThirdScheduleData(tform, approve);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "saveCheckingAuthData")
    public void SaveCheckingAuthData(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String) requestParams.get("chkEmp");
            String authEmp = (String) requestParams.get("authEmp");

            msg = thirdSchedleDAO.saveCheckingAuthData(chkEmp, authEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "revertPayFixationEmp")
    public void RevertPayFixationEmp(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String) requestParams.get("chkEmp");

            msg = thirdSchedleDAO.revertPayFixationAuthData(chkEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "ThirdScheduleContractual6YrsToRegular")
    public ModelAndView ThirdSchedule6YrsContractual(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("thirdScheduleForm") ThirdScheduleForm tform) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/payroll/thirdschedule/ThirdScheduleContractual6YrsToRegular", "thirdScheduleForm", tform);

            List billGrpList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
            mav.addObject("billGrpList", billGrpList);

            if (tform.getSltBillGroup() != null && !tform.getSltBillGroup().equals("")) {
                List emplist = thirdSchedleDAO.getBillGroupWiseEmployeeList(tform.getSltBillGroup());
                mav.addObject("emplist", emplist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "GetCont6YrsToRegularThirdScheduleData")
    public ModelAndView GetCont6YrsToRegularThirdScheduleData(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billGroupId") String billGroupId, @RequestParam("empid") String empid, @RequestParam(value = "status", required = false) String status) {

        ModelAndView mav = null;

        try {
            ThirdScheduleForm tform = thirdSchedleDAO.getCont6YrsToRegularData(empid);
            tform.setSltBillGroup(billGroupId);
            List postlist2nd = postDAO.getPostList(tform.getDeptCode());

            mav = new ModelAndView("/payroll/thirdschedule/ThirdScheduleContractual6YrsToRegularData", "thirdScheduleForm", tform);
            mav.addObject("status", status);
            model.addAttribute("PostList2nd", postlist2nd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveThirdScheduleContractual6YrsToRegularData", params = "btnSubmit=Save")
    public String SaveThirdScheduleContractual6YrsToRegularDataSave(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("thirdScheduleForm") ThirdScheduleForm tform) {

        try {
            thirdSchedleDAO.saveContractual6YrsToRegularThirdScheduleData(tform);

            /*if (tform.getRevisedbasic() != null) {
                String[] incrDt = tform.getIncrDt();
                String[] revPay = tform.getRevisedbasic();
                String[] incrLevel = tform.getIncrLevel();
                String[] incrCell = tform.getIncrCell();

                IncrementForm incfb = null;

                for (int i = 0; i < revPay.length; i++) {
                    incfb = new IncrementForm();
                    incfb.setEmpid(tform.getEmpid());
                    incfb.setTxtSanctionOrderNo("29076");
                    incfb.setTxtSanctionOrderDt("16-Oct-2022");
                    incfb.setDeptCode("11");
                    incfb.setHidOffCode("OLSGAD0010000");
                    incfb.setRdTransaction("S");
                    incfb.setRdoPaycomm("7");
                    if (revPay.length > 0 && revPay[i] != null && !revPay[i].equals("")) {
                        incfb.setTxtNewBasic(revPay[i]);
                    } else {
                        incfb.setTxtNewBasic("");
                    }
                    if (incrDt.length > 0 && incrDt[i] != null && !incrDt[i].equals("")) {
                        incfb.setTxtWEFDt(incrDt[i]);
                    } else {
                        incfb.setTxtWEFDt("");
                    }
                    incfb.setTxtWEFTime("FN");
                    if (incrLevel.length > 0 && incrLevel[i] != null && !incrLevel[i].equals("")) {
                        incfb.setPayLevel(incrLevel[i]);
                    } else {
                        incfb.setPayLevel("");
                    }
                    if (incrCell.length > 0 && incrCell[i] != null && !incrCell[i].equals("")) {
                        incfb.setPayCell(incrCell[i]);
                    } else {
                        incfb.setPayCell("");
                    }

                    incrementsancDAO.saveIncrement(incfb, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incfb.getRdoPaycomm(), lub.getLoginuserid());
                }
                thirdSchedleDAO.updateContractual6YrsToRegularPayData(tform);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ThirdScheduleContractual6YrsToRegular.htm?sltBillGroup=" + tform.getSltBillGroup();
    }

    @RequestMapping(value = "SaveThirdScheduleContractual6YrsToRegularData", params = "btnSubmit=Approve")
    public String SaveThirdScheduleContractual6YrsToRegularDataApprove(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("thirdScheduleForm") ThirdScheduleForm tform) {

        try {

            thirdSchedleDAO.saveContractual6YrsToRegularThirdScheduleData(tform);

            if (tform.getRevisedbasic() != null) {
                String[] incrDt = tform.getIncrDt();
                String[] revPay = tform.getRevisedbasic();
                String[] incrLevel = tform.getIncrLevel();
                String[] incrCell = tform.getIncrCell();

                IncrementForm incfb = null;

                for (int i = 0; i < revPay.length; i++) {
                    incfb = new IncrementForm();
                    incfb.setEmpid(tform.getEmpid());
                    incfb.setTxtSanctionOrderNo("29076");
                    incfb.setTxtSanctionOrderDt("16-Oct-2022");
                    incfb.setDeptCode("11");
                    incfb.setHidOffCode("OLSGAD0010000");
                    incfb.setRdTransaction("S");
                    incfb.setRdoPaycomm("7");
                    if (revPay.length > 0 && revPay[i] != null && !revPay[i].equals("")) {
                        incfb.setTxtNewBasic(revPay[i]);
                    } else {
                        incfb.setTxtNewBasic("");
                    }
                    if (incrDt.length > 0 && incrDt[i] != null && !incrDt[i].equals("")) {
                        incfb.setTxtWEFDt(incrDt[i]);
                    } else {
                        incfb.setTxtWEFDt("");
                    }
                    incfb.setTxtWEFTime("FN");
                    if (incrLevel.length > 0 && incrLevel[i] != null && !incrLevel[i].equals("")) {
                        incfb.setPayLevel(incrLevel[i]);
                    } else {
                        incfb.setPayLevel("");
                    }
                    if (incrCell.length > 0 && incrCell[i] != null && !incrCell[i].equals("")) {
                        incfb.setPayCell(incrCell[i]);
                    } else {
                        incfb.setPayCell("");
                    }

                    incrementsancDAO.saveIncrement(incfb, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incfb.getRdoPaycomm(), lub.getLoginuserid());
                }
                thirdSchedleDAO.updateContractual6YrsToRegularPayData(tform);
            }

            thirdSchedleDAO.approveContractual6YrsToRegularThirdSchedule(tform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ThirdScheduleContractual6YrsToRegular.htm?sltBillGroup=" + tform.getSltBillGroup();
    }

    @ResponseBody
    @RequestMapping(value = "ValidatePayAgainstLevelAndCell")
    public void ValidatePayAgainstLevelAndCell(HttpServletResponse response, @RequestParam("level") int level, @RequestParam("cell") int cell, @RequestParam("basic") int basic) {

        response.setContentType("application/json");
        PrintWriter out = null;

        try {
            String validationstatus = thirdSchedleDAO.validatePayAgainstLevelAndCell(level, cell, basic);
            
            Map responseResult = new HashMap();
            responseResult.put("status", validationstatus);
            JSONObject job = new JSONObject(responseResult);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "RevertCont6YrsThirdSchedule")
    public ModelAndView RevertCont6YrsThirdSchedule(@RequestParam("empid") String empid, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        boolean employeedistrict = employeeDAO.verifyEmployeeDistrict(lub.getLogindistrictcode(), empid);
        if (employeedistrict == true) {
            int revertStatus = thirdSchedleDAO.revertCont6YrsThirdScheduleDC(empid);
            
            mv.addObject("revertStatus", revertStatus);
            
            mv.setViewName("/payroll/thirdschedule/RevertCont6YrsThirdScheduleDC");
        } else {
            mv.setViewName("under_const");
        }
        return mv;
    }
}

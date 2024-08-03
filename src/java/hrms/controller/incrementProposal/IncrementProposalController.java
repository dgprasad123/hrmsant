/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.incrementProposal;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.HeaderFooterPageEvent;
import hrms.dao.incrementProposal.IncrementProposalDAO;

import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.ProcessStatusDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.MaxNotificationIdDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.incrementProposal.EmpOrderBean;
import hrms.model.incrementProposal.IncrementProposal;
import hrms.model.incrementProposal.ProposalAttr;
import hrms.model.incrementProposal.ProposalMaster;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
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
 * @author Surendra
 */
@Controller
@SessionAttributes("LoginUserBean")
public class IncrementProposalController {

    @Autowired
    public LoginDAOImpl loginDAO;

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public IncrementProposalDAO incrementProposalDao;

    @Autowired
    public NotificationDAO NotificationDAO;

    @Autowired
    public MaxNotificationIdDAO maxnotiidDao;

    @Autowired
    ProcessStatusDAO processStatusDAO;

    @RequestMapping(value = "displayProposalListpage", method = RequestMethod.GET)
    public ModelAndView ProposalOrderList(ModelMap model, @ModelAttribute("IncrementProposal") IncrementProposal incr, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = null;

        List li = incrementProposalDao.proposalList(lub.getLoginoffcode(), 1, 10);

        mav = new ModelAndView("/incrementProposal/IncrementProposalList", "IncrementProposal", incr);

        mav.addObject("ProposalList", li);

        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "proposalMasterListAction", method = RequestMethod.POST)
    public void getProposalList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {

            List<IncrementProposal> li = incrementProposalDao.proposalList(lub.getLoginoffcode(), page, rows);
            List jsonlist = new ArrayList();
            ProposalMaster propmast = null;
            IncrementProposal incrprop = null;
            int j = 0;
            while (j < li.size()) {
                incrprop = li.get(j);
                propmast = new ProposalMaster();
                propmast.setProposalId(incrprop.getProposalId());
                propmast.setSubmitproposalId(incrprop.getProposalId());
                propmast.setExportproposalId(incrprop.getProposalId());
                propmast.setOrdno(incrprop.getOrderno());
                propmast.setOrddate(CommonFunctions.getFormattedOutputDate3(incrprop.getOrderDate()));
                propmast.setPropMonth(monthName[incrprop.getProposalMonth() - 1]);
                propmast.setPropYear(incrprop.getProposalYear());
                propmast.setStatus(incrprop.getProcessStatusName());
                if (incrprop.getTask() != null) {
                    propmast.setTaskid(incrprop.getTask().getTaskid());
                } else {
                    propmast.setTaskid(0);
                }
                propmast.setLastUpdated(CommonFunctions.getFormattedOutputDate3(incrprop.getLastUpdatedOn()));
                jsonlist.add(propmast);
                j++;
            }
            json.put("total", jsonlist.size());
            json.put("rows", jsonlist);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "newProposalMaster", params = "action=Create Increment Proposal")
    public ModelAndView createNewProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result) {
        ModelAndView mav = new ModelAndView("/incrementProposal/AddIncrementMasterData", "IncrementProposal", incrementForm);

        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.MONTH, 1);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        //System.out.println("Month Name:"+month);
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[month];
        crrmonth = crrmonth + "-" + year;
        try {
            incrementForm.setProposalYear(year);
            incrementForm.setProposalMonth(month);
            incrementForm.setMonthasString(crrmonth);
            incrementForm.setProposalId(0);

            List li = incrementProposalDao.getProposedEmployeeList(lub.getLoginoffcode(), year, (month + 1));
            //System.out.println("Size:" + li.size());
            mav.addObject("ProposalList", li);
            mav.addObject("numProposals", li.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Back to Proposals")
    public ModelAndView back2Proposals(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra) {

        //ra.addFlashAttribute("year",incrementForm.getProposalYear());
        //ra.addFlashAttribute("month", ra)
        ModelAndView mav = new ModelAndView("redirect:/displayProposalListpage.htm");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Back to Proposal List")
    public ModelAndView back2List(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra) {

        //ra.addFlashAttribute("year",incrementForm.getProposalYear());
        //ra.addFlashAttribute("month", ra)
        ModelAndView mv = new ModelAndView("redirect:/newProposalMaster.htm?action=Create Increment Proposal");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mv;
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Add Employee")
    public ModelAndView addEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra) {

        List list = new ArrayList();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[incrementForm.getProposalMonth()];
        crrmonth = crrmonth + "-" + incrementForm.getProposalYear();
        incrementForm.setMonthasString(crrmonth);
        incrementForm.setOpt("add");
        ModelAndView mav = new ModelAndView("/incrementProposal/AddEmployee", "IncrementProposal", incrementForm);

        try {

            list = incrementProposalDao.getEmployeeList(lub.getLoginoffcode(), incrementForm.getProposalYear(), incrementForm.getProposalMonth());
            mav.addObject("EmpList", list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Add Employees")
    public ModelAndView addEmployees(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra) {

        List list = new ArrayList();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[incrementForm.getProposalMonth() - 1];
        crrmonth = crrmonth + "-" + incrementForm.getProposalYear();
        incrementForm.setMonthasString(crrmonth);
        incrementForm.setOpt("edit");
        ModelAndView mav = new ModelAndView("/incrementProposal/AddEmployee", "IncrementProposal", incrementForm);

        try {

            list = incrementProposalDao.getEmployeeList(lub.getLoginoffcode(), incrementForm.getProposalYear(), incrementForm.getProposalMonth());
            mav.addObject("EmpList", list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "EditIncrementProposal")
    public ModelAndView editProposalList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra, @RequestParam Map<String, String> requestParams) {

        List list = new ArrayList();
        int proposalId = Integer.parseInt(requestParams.get("proposalId"));
        int proposalMonth = Integer.parseInt(requestParams.get("pmonth"));
        int year = Integer.parseInt(requestParams.get("pyear"));

        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[proposalMonth - 1];
        crrmonth = crrmonth + "-" + year;
        ModelAndView mav = new ModelAndView("/incrementProposal/EditIncrementMasterData", "IncrementProposal", incrementForm);
        incrementForm.setProposalYear(year);
        incrementForm.setProposalMonth(proposalMonth);
        incrementForm.setMonthasString(crrmonth);
        try {

            list = incrementProposalDao.getFinalProposedList(proposalId);
            mav.addObject("EmpList", list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Add Employee to Proposal List")
    public ModelAndView add2List(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra) {
        ModelAndView mav = null;
        if (incrementForm.getOpt().equals("add")) {
            //System.out.println("Add");
            mav = new ModelAndView("redirect:/newProposalMaster.htm?action=Create Increment Proposal");
        }
        if (incrementForm.getOpt().equals("edit")) {
            //System.out.println("Edit");
            mav = new ModelAndView("redirect:/EditIncrementProposal.htm?proposalId=" + incrementForm.getProposalId() + "&pmonth=" + incrementForm.getProposalMonth() + "&pyear=" + incrementForm.getProposalYear());
            incrementForm.setProposalMonth(incrementForm.getProposalMonth() - 1);
        }

        try {

            String empArr[] = incrementForm.getEmpId().split(",");
            incrementProposalDao.addProposedEmployee(empArr, incrementForm.getProposalYear(), incrementForm.getProposalMonth(), incrementForm.getProposalId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "addProposalEmployeeListAction", method = RequestMethod.GET)
    public void addProposalEmployeeListAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        List jsonlist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        List<Users> li = null;
        ProposalAttr pro = null;
        try {

            li = incrementProposalDao.getEmployeeList(lub.getLoginoffcode(), year, month);

            Users emp = null;
            int j = 0;
            while (j < li.size()) {
                emp = li.get(j);
                pro = new ProposalAttr();
                pro.setProposaldetailId(0);
                pro.setEmpId(emp.getEmpId());
                pro.setEmpname(emp.getFullName());
                pro.setGpfno(emp.getGpfno());
                pro.setPost(emp.getSubstantivePost().getSpn());
                pro.setPresentpaydate(CommonFunctions.getFormattedOutputDate3(emp.getPaydate()));
                pro.setNextincr(CommonFunctions.getFormattedOutputDate3(emp.getDateOfnincr()));

                jsonlist.add(pro);
                j++;
            }
            json.put("total", 50);
            json.put("rows", jsonlist);
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
    @RequestMapping(value = "proposalListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void getProposalDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("proposalId") int proposalId) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            month = month + 1;
            List jsonlist = new ArrayList();

            List<Users> li = null;

            ProposalAttr pro = null;

            /*
             This If block is for edit Proposal final List which is Present At HW_INCREMENT_PROPOSAL_DETAIL Table
            
             and 
            
             else block is for create new proposal list from EMP_MAST Table
             */
            if (proposalId > 0) {
                jsonlist = incrementProposalDao.getFinalProposedList(proposalId);
            } else {
                jsonlist = incrementProposalDao.getProposedEmployeeList(lub.getLoginoffcode(), year, month);
            }
            json.put("total", 50);
            json.put("rows", jsonlist);
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
    @RequestMapping(value = "removeProposalList", method = {RequestMethod.GET, RequestMethod.POST})
    public void removeproposalList(ModelMap model, @ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("empId") String empId, @RequestParam("proposalDetailId") int proposaldetailId) {

        incrementProposalDao.deleteSelectedProposalFromList(proposaldetailId, empId);
    }

    @ResponseBody
    @RequestMapping(value = "DeleteIncrementProposal", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteIncrementProposal(ModelMap model, @ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("proposalId") int proposalId) {

        incrementProposalDao.deleteIncrementProposal(proposalId);
    }

    @RequestMapping(value = "addProposedEmployee", method = RequestMethod.POST)
    public void addProposedEmployee(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("emplist") String emplist, @RequestParam("month") int month, @RequestParam("year") int year, @RequestParam("proposlMastId") int proposlMastId) {
        String empArr[] = emplist.split(",");
        incrementProposalDao.addProposedEmployee(empArr, year, month, proposlMastId);
    }

    @RequestMapping(value = "newProposalMaster", method = RequestMethod.POST, params = "action=Save Proposal")
    public ModelAndView saveProposalList(ModelMap model, @ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("emplist") String emplist, @RequestParam("proposalMonth") int month, @RequestParam("proposalYear") int year, @RequestParam("proposalId") int proposalId) {

        ModelAndView mav = new ModelAndView("redirect:/displayProposalListpage.htm");
        try {
            if (proposalId > 0) {
                incrementProposalDao.deleteProposalDetail(proposalId);
                String empArr[] = emplist.split(",");
                String pcArr[] = incr.getPayCommission().split(",");
                String mlArr[] = incr.getMatrixLevel().split(",");
                String mcArr[] = incr.getMatrixCell().split(",");
                String wefArr[] = incr.getWefDate().split(",");
                //System.out.println("Pay Commission:"+incr.getPayCommission());
                //System.out.println("Matrix Level:"+incr.getMatrixLevel());
                //System.out.println("Matrix Cell:"+incr.getMatrixCell());                
                incrementProposalDao.saveProposalDetailList(empArr, pcArr, mlArr, mcArr, wefArr, proposalId, year, month);
            } else {
                String empArr[] = emplist.split(",");
                String pcArr[] = incr.getPayCommission().split(",");
                String mlArr[] = incr.getMatrixLevel().split(",");
                String mcArr[] = incr.getMatrixCell().split(",");
                String wefArr[] = incr.getWefDate().split(",");
                proposalId = incrementProposalDao.saveProposalList(lub.getLoginoffcode(), lub.getLoginempid(), lub.getLoginspc(), month, year);
                //System.out.println("Pay Commission:"+incr.getPayCommission());
                incrementProposalDao.saveProposalDetailList(empArr, pcArr, mlArr, mcArr, wefArr, proposalId, year, month);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }
// http://par.hrmsodisha.gov.in/submitProposal.htm?proposalId=11

    @RequestMapping(value = "submitProposal", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView submitProposal(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("proposalId") int proposalId) {
        String path = "/incrementProposal/SubmitIncrementProposal";
        ModelAndView mav = new ModelAndView();
        try {
            incr.setProposalId(proposalId);
            List deptlist = departmentDao.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());
            mav.addObject("deptlist", deptlist);
            mav.addObject("sancOfflist", fieldofflist);
            mav.addObject("incrementForm", incr);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;

    }

    @RequestMapping(value = "submitProposalTask", method = {RequestMethod.GET, RequestMethod.POST})
    public String submitToAuthority(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/displayProposalListpage.htm?offCode=";
        try {
            incrementProposalDao.submitProposal(incr.getProposalId(), lub.getLoginempid(), lub.getLoginspc(), incr.getAuthspc(), incr.getAuthspc1());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;

    }

    @ResponseBody
    @RequestMapping(value = "getDepartmentListAction", method = RequestMethod.POST)
    public void getDepartmentlList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        SelectOption so = null;
        ArrayList al = new ArrayList();
        Department dept = null;
        try {
            List li = departmentDao.getDepartmentList();
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

    @ResponseBody
    @RequestMapping(value = "getStatusList", method = RequestMethod.POST)
    public void getStatusList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = incrementProposalDao.getIncrementStatusList();
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

    @ResponseBody
    @RequestMapping(value = "getPostListAction", method = RequestMethod.POST)
    public void getPostList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam String offcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        SelectOption so = null;
        ArrayList al = new ArrayList();
        SubstantivePost post = null;
        try {

            List li = incrementProposalDao.getEmployeeWisePostList(offcode);
            Iterator<SubstantivePost> itr = li.iterator();

            while (itr.hasNext()) {
                post = (SubstantivePost) itr.next();
                so = new SelectOption();
                so.setLabel(post.getEmpname() + ", " + post.getPostname());
                so.setValue(post.getSpc());
                al.add(so);
            }
            JSONArray json = new JSONArray(al);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "authorityViewProposalTask", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView authorityView(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") int taskId) {
        String path = "/incrementProposal/AuthorityViewPage";

        ModelAndView mav = new ModelAndView();
        try {
            int proposalId = incrementProposalDao.getProposalMasterId(taskId);
            int statusId = incrementProposalDao.getTaskStatusId(taskId);
            incr.setProposalId(proposalId);
            incr.setProcessStatus(statusId);
            mav.addObject("incrementForm", incr);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "authorityViewData", method = {RequestMethod.GET, RequestMethod.POST})
    public void authorityViewData(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("proposalId") int proposalId) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            List jsonlist = new ArrayList();

            ProposalAttr pro = null;

            jsonlist = incrementProposalDao.getFinalProposedList(proposalId);

            json.put("total", 50);
            json.put("rows", jsonlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "authorityApproved", method = {RequestMethod.GET, RequestMethod.POST})
    public void authorityApprovedTask(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("propmastId") int propmastId, @RequestParam("status") int status, @RequestParam("note") String note, @RequestParam("isVerified") String isVerified) {
        try {
            if (status == 15) {
                if (isVerified.equals("Y")) {
                    incrementProposalDao.authorityAction(propmastId, 10, note, lub.getLoginspc());
                }
                if (isVerified.equals("N")) {
                    incrementProposalDao.authorityAction(propmastId, 11, note, lub.getLoginspc());
                }
            } else {
                incrementProposalDao.authorityAction(propmastId, status, note, lub.getLoginspc());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @ResponseBody
    @RequestMapping(value = "updateorderInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public void updateorderInfo(@ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("propmastId") int propmastId, @RequestParam("ordno") String ordno, @RequestParam("ordDate") String ordDate) {
        try {
            incrementProposalDao.updateOrderInfo(propmastId, ordno, ordDate);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "generatepdf", method = RequestMethod.GET)
    public void generatePDF(ModelMap model, @ModelAttribute("IncrementProposal") IncrementProposal incr,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("proposalId") int proposalId, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=PERIODICAL_INCREMENT.pdf");
        Document document = new Document(PageSize.A4.rotate());
        final Font titlefontSmall = FontFactory.getFont("Arial", 15, Font.BOLD | Font.UNDERLINE);
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);
        String offname = "";
        String SignatoryAuthority = "";
        if (requestParams.get("signatory") != null && !requestParams.get("signatory").equals("")) {
            SignatoryAuthority = requestParams.get("signatory");
        }

        try {
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();
            /*
             rs = st.executeQuery("SELECT APPROVED_ORDER_NO, APPROVED_ORDER_DATE,APPROVED_AUTHORITY FROM HW_INCREMENT_PROPOSAL_MASTER WHERE PROPOSAL_ID=" + ifBean.getProposalMasterId());
             if (rs.next()) {
             ordno = rs.getString("APPROVED_ORDER_NO");
             ordDate = CommonFunctions.getFormattedOutputDate(rs.getDate("APPROVED_ORDER_DATE"));
             sanctionAuth=GetUserAttribute.getPost(con, rs.getString("APPROVED_AUTHORITY"));
             }
            
             */
            List li = incrementProposalDao.getFinalProposedList(proposalId);

            offname = incrementProposalDao.getOfficeName(lub.getLoginoffcode());
            ProposalAttr pi = incrementProposalDao.getordnoSpcEtc(proposalId);
            Iterator itr = li.iterator();
            ProposalAttr emp = null;
            while (itr.hasNext()) {
                Paragraph rule = new Paragraph("[See Subsidiary Rule 225]", headingRule);
                rule.setAlignment(Element.ALIGN_CENTER);
                document.add(rule);

                Paragraph heading = new Paragraph("PERIODICAL INCREMENT CERTIFICATE", titlefontSmall);
                heading.setAlignment(Element.ALIGN_CENTER);
                document.add(heading);

                String pointStr = "1. Certified that the Government Servants named below have earned the Prescribed Periodical increment from"
                        + " the date cited in column 6 having been the incumbent of the posts specified for not less than 1 (one) years from"
                        + " the date in column 5 after deducting period from misconduct, etc. and absence on leave without pay and in the case"
                        + " of those holding posts in officiating capacity all other kinds of leave.";
                Paragraph point1 = new Paragraph(pointStr);
                document.add(point1);

                String pointStr2 = "2. Certified that the Government Servants named below have earned/will earn periodical increments from"
                        + " the date cited reasons stated in the explanatory Memo. Attached hereto,";
                Paragraph point2 = new Paragraph(pointStr2);
                document.add(point2);

                Paragraph blank = new Paragraph(" ");
                document.add(blank);

                PdfPTable table = new PdfPTable(11);
                table.setWidths(new int[]{9, 4, 4, 3, 4, 4, 3, 4, 4, 4, 4});
                table.setWidthPercentage(100);

                PdfPCell cell = null;

                cell = new PdfPCell(new Phrase("Name of incumbents", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Substantive/ Officiating", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Scale of Pay of posts", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Present pay in Rs.", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Date from which present pay is drawn", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Date of present increment", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Future pay", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Suspension for misconduct and such other absence as does not count for increment", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Leave without pay and in the case of those holding the posts in officiating capacity all other kinds of leave.", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);

                // 2nd row
                cell = new PdfPCell(new Phrase("From", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("To", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("From", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("To", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                // 3rd row
                cell = new PdfPCell(new Phrase("1", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("2", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("3", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("4", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("5", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("6", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("7", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("8", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("9", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("10", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("11", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(11);
                table.setWidths(new int[]{9, 4, 4, 3, 4, 4, 3, 4, 4, 4, 4});
                table.setWidthPercentage(100);

                emp = (ProposalAttr) itr.next();

                //Chunk chunk1 = new Chunk(emp.getEmpname());
                //Chunk chunk2 = new Chunk(emp.getPost());
                cell = new PdfPCell(new Phrase(emp.getEmpname() + "\n" + StringUtils.defaultString(emp.getPost()), new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Officiating", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                String payScale = "";
                String gp = "";
                if (emp.getPayCommission() != null && Integer.parseInt(emp.getPayCommission()) != 0) {
                    if (Integer.parseInt(emp.getPayCommission()) == 6) {
                        payScale = emp.getPayscale();
                        gp = emp.getGp();
                    }
                    if (Integer.parseInt(emp.getPayCommission()) == 7) {
                        payScale = "(Level: " + emp.getMatrixLevel() + " Cell:" + emp.getMatrixCell() + ")";
                    }
                }
                cell = new PdfPCell(new Phrase(payScale + "\nGP: " + gp, new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(emp.getPresentpay(), new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(emp.getPresentpaydate(), new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(emp.getNextincr(), new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(emp.getFuturepay(), new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
                //cell.setBorder(Rectangle.);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                document.add(table);

                /*
                 Paragraph gov = new Paragraph("Government of Odisha");
                 gov.setAlignment(Element.ALIGN_CENTER);
                 document.add(gov);
                 */
                Paragraph off = new Paragraph(offname);
                off.setAlignment(Element.ALIGN_CENTER);
                document.add(off);

                Paragraph star = new Paragraph("*******");
                star.setAlignment(Element.ALIGN_CENTER);
                document.add(star);

                String ordno = "";
                if (emp.getOrdno() != null) {
                    ordno = emp.getOrdno();
                }
                Paragraph orderno = new Paragraph("Order No. " + ordno + ", dated- " + emp.getOrderDate());
                orderno.setAlignment(Element.ALIGN_LEFT);
                document.add(orderno);

                Paragraph incrsanc = new Paragraph("The Increment is sanctioned.\n\n\n");
                incrsanc.setAlignment(Element.ALIGN_LEFT);
                document.add(incrsanc);

                PdfPTable table2 = new PdfPTable(1);
                table2.setWidths(new int[]{10});
                table2.setWidthPercentage(100);

                PdfPCell cell2 = null;
                if (SignatoryAuthority == "") {
                    cell2 = new PdfPCell(new Phrase(pi.getPost(), new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                } else {
                    cell2 = new PdfPCell(new Phrase(SignatoryAuthority, new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                }
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell2);

                document.add(table2);
                String memno = "";
                if (emp.getMemoNo() != null) {
                    memno = emp.getMemoNo();
                }
                Paragraph memo = new Paragraph("Memo No." + memno + ",  date-" + emp.getOrderDate());
                memo.setAlignment(Element.ALIGN_LEFT);
                document.add(memo);

                Paragraph cpy = new Paragraph("Copy forwarded to Accounts Section (in duplicate) / Person concerned for information and necessary action.\n\n\n\n");
                cpy.setAlignment(Element.ALIGN_LEFT);
                document.add(cpy);

                PdfPTable table3 = new PdfPTable(1);
                table3.setWidths(new int[]{10});
                table3.setWidthPercentage(100);

                PdfPCell cell3 = null;
                if (SignatoryAuthority == "") {
                    cell3 = new PdfPCell(new Phrase(pi.getPost(), new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                } else {
                    cell3 = new PdfPCell(new Phrase(SignatoryAuthority, new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
                }
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setBorder(Rectangle.NO_BORDER);
                table3.addCell(cell3);

                document.add(table3);

                document.newPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    public void onEndPage(PdfWriter writer, Document document) {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("this is a footer", ffont);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

    @RequestMapping(value = "ViewEmpLastYearPay", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView ViewEmpLastYearPay(
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("empId") String empId) {
        String path = "/incrementProposal/EmpLastYearPay";

        ModelAndView mav = new ModelAndView();

        List li = incrementProposalDao.getLastYearPay(empId);
        mav.addObject("payList", li);
        mav.setViewName(path);
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "getCellList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getCellList(ModelMap model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response,
            @RequestParam("level") String level, @RequestParam("empId") String empId, @RequestParam("presentPay") String presentPay) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        try {
            StringBuffer content
                    = new StringBuffer("<select name=\"matrixCell\" id='matrix_cell_" + empId + "' size='1' onchange=\"javascript: getFuturePay('" + empId + "'," + presentPay + ")\"><option value='0'>-Select-</option>");
            List li = new ArrayList();
            li = incrementProposalDao.getCells(Integer.parseInt(level));
            for (int i = 0; i < li.size(); i++) {
                SelectOption so = new SelectOption();
                so = (SelectOption) li.get(i);
                content.append("<option value='" + so.getValue() + "'>" + so.getLabel() + "</option>");
            }
            content.append("</select>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getIncrementPay", method = {RequestMethod.GET, RequestMethod.POST})
    public void getIncrementPay(ModelMap model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response,
            @RequestParam("level") int level, @RequestParam("cell") int cell, @RequestParam("empId") String empId, @RequestParam("payCommission") int payCommission, @RequestParam("presentPay") String presentPay) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        try {
            StringBuffer content
                    = new StringBuffer("");
            int futurePay = incrementProposalDao.getFianlIncrementPay(level, cell, payCommission, presentPay, empId);

            content.append(futurePay + "");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "UpdateEmpOrders")
    public ModelAndView updateEmpOrders(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpOrderBean") EmpOrderBean eBean,
            BindingResult result, RedirectAttributes ra, @RequestParam Map<String, String> requestParams) {

        List list = new ArrayList();
        String path = "incrementProposal/UpdateEmpOrders";
        int proposalId = Integer.parseInt(requestParams.get("proposalId"));
        String month = requestParams.get("month");
        //String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ModelAndView mav = new ModelAndView();
        mav.addObject("proposalId", proposalId);
        try {

            list = incrementProposalDao.getFinalProposedList(proposalId);
            mav.addObject("EmpList", list);
            mav.addObject("monthName", month);
            mav.setViewName(path);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "SaveEmpOrders", method = RequestMethod.POST)
    public ModelAndView saveEmpOrders(ModelMap model, @ModelAttribute("EmpOrderBean") EmpOrderBean eBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response
    ) {

        ModelAndView mav = new ModelAndView("redirect:/displayProposalListpage.htm");
        try {
            incrementProposalDao.SaveEmployeeOrder(eBean, lub.getLoginspc(), lub.getLoginoffcode(), lub.getLogindeptcode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getIncrementWisePostListJSON")
    public String getIncrementWisePostListJSON(@RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List spclist = incrementProposalDao.getIncrementPostListWithName(offcode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "finalfutureproposallist")
    public ModelAndView finalfutureproposallist(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra, @RequestParam Map<String, String> requestParams) {
        List list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        ModelAndView mav = new ModelAndView();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[month];
        crrmonth = crrmonth + "-" + year;
        try {
            incrementForm.setProposalYear(year);
            incrementForm.setProposalMonth(month);
            incrementForm.setMonthasString(crrmonth);
            list = incrementProposalDao.getFinalFutureProposedList(lub.getLoginoffcode(), year, month);
            mav.addObject("EmpList", list);
            mav.setViewName("/incrementProposal/FutureIncrementProposal");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "monthlyretirement")
    public ModelAndView monthlyretirement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("IncrementProposal") IncrementProposal incrementForm,
            BindingResult result, RedirectAttributes ra, @RequestParam Map<String, String> requestParams) {
        List list = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        ModelAndView mav = new ModelAndView();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String crrmonth = monthName[month];
        crrmonth = crrmonth + "-" + year;
        try {
            incrementForm.setProposalYear(year);
            incrementForm.setProposalMonth(month);
            incrementForm.setMonthasString(crrmonth);
            list = incrementProposalDao.getMonthlyRetirementList(lub.getLoginoffcode(), year, month);
            mav.addObject("EmpList", list);
            mav.setViewName("/incrementProposal/MonthlyRetirement");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

}

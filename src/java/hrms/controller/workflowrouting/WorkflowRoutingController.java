/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.workflowrouting;

import hrms.SelectOption;
import hrms.common.ValueComparator;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.task.TaskDAO;
import hrms.dao.workflowrouting.WorkflowRoutingDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.workflowrouting.WorkflowRouting;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
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
 * @author Surendra
 */
@SessionAttributes("LoginUserBean")
@Controller
public class WorkflowRoutingController {

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public PostDAO postdao;

    @Autowired
    public TaskDAO taskdao;

    @Autowired
    public WorkflowRoutingDAO workflowRoutingDao;

    @Autowired
    public OfficeDAO officeDao;

    @RequestMapping("/getOfficeForDepartment")
    @ResponseBody
    public void printOffice(HttpServletResponse response, @RequestParam("deptCode") String deptCode) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject json = null;
        Office so = new Office();
        try {
            out = response.getWriter();

            List offlist = officeDao.getTotalOfficeList(deptCode);

            Map<String, String> offmap = new HashMap<String, String>();

            for (int i = 0; i < offlist.size(); i++) {
                so = (Office) offlist.get(i);
                offmap.put(so.getOffCode(), so.getOffName());

            }
            json = new JSONObject(offmap);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "displayworkflowrouting")
    public ModelAndView displayworkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        List li = postdao.getPostList(lub.getLogindeptcode(), lub.getLoginoffcode());
        ModelAndView mav = new ModelAndView("/workflowrouting/HierarchyList");
        mav.addObject("PostList", li);

        return mav;
    }

    @RequestMapping(value = "showworkflowrouting")
    public ModelAndView showWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam("postcode") String postcode) {
        wr.setGpc(postcode);
        wr.setSltDept(lub.getLogindeptcode());
        ModelAndView mav = new ModelAndView("/workflowrouting/AssignHierarchy", "command", wr);

        String postname = postdao.getPostName(wr.getGpc());

        List deptlist = departmentDao.getDepartmentList();
        Map<String, String> dept = new HashMap<String, String>();
        Iterator<Department> itr2 = deptlist.iterator();
        Department dpt = null;
        while (itr2.hasNext()) {
            dpt = itr2.next();
            dept.put(dpt.getDeptCode(), dpt.getDeptName());
        }
        Map<String, String> sortedDept = new TreeMap<String, String>(new ValueComparator(dept));
        sortedDept.putAll(dept);

        mav.addObject("deptList", sortedDept);

        SelectOption so = new SelectOption();
        List offlist = officeDao.getOfficeListFilter(lub.getLoginoffcode());
        Map<String, String> offmap = new HashMap<String, String>();
        Iterator<SelectOption> itr3 = offlist.iterator();
        while (itr3.hasNext()) {
            so = itr3.next();
            offmap.put(so.getValue(), so.getLabel());
        }

        mav.addObject("offList", offmap);
        List li = taskdao.getProcessList();

        Map<String, String> process = new HashMap<String, String>();
        Iterator<SelectOption> itr = li.iterator();
        so = new SelectOption();
        while (itr.hasNext()) {
            so = itr.next();
            process.put(so.getValue(), so.getLabel());
        }

        mav.addObject("processList", process);
        mav.addObject("postname", postname);
        return mav;
    }

    @RequestMapping(value = "assignworkflowrouting")
    public ModelAndView assignworkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;

        String postname = postdao.getPostName(wr.getGpc());
        List deptlist = departmentDao.getDepartmentList();
        Map<String, String> dept = new HashMap<String, String>();
        Iterator<Department> itr2 = deptlist.iterator();
        Department dpt = new Department();
        while (itr2.hasNext()) {
            dpt = itr2.next();
            dept.put(dpt.getDeptCode(), dpt.getDeptName());
        }

        String btn = parameters.get("btnval");

        if ((btn == null || btn.equalsIgnoreCase("")) || btn.equalsIgnoreCase("GetList")) {
            mav = new ModelAndView("/workflowrouting/AssignHierarchy", "command", wr);
            List li = taskdao.getProcessList();
            
            Map<String, String> process = new HashMap<String, String>();
            Iterator<SelectOption> itr = li.iterator();
            SelectOption so = new SelectOption();
            while (itr.hasNext()) {
                so = itr.next();
                process.put(so.getValue(), so.getLabel());
            }

            mav.addObject("processList", process);

            /*
             if (wr.getSltOffcode() != null && !wr.getSltOffcode().equals("")) {
             offcode = wr.getSltOffcode();
             } else {
             offcode = lub.getLoginoffcode();
             }
             */
            so = new SelectOption();
            List offlist = officeDao.getOfficeListFilter(wr.getSltOffcode());
            Map<String, String> offmap = new HashMap<String, String>();
            Iterator<SelectOption> itr3 = offlist.iterator();
            while (itr3.hasNext()) {
                so = itr3.next();
                offmap.put(so.getValue(), so.getLabel());
            }

            mav.addObject("offList", offmap);

            List postlist = workflowRoutingDao.getPostListAuthorityWise(wr.getSltOffcode(), wr.getGpc(), wr.getProcessId());
            mav.addObject("proposeList", postlist);

            List assignedlist = workflowRoutingDao.getmappedPostList(wr.getGpc(), wr.getProcessId());
            mav.addObject("assignedList", assignedlist);
        } else {
            mav = new ModelAndView("redirect:displayworkflowrouting.htm", "command", wr);

        }
        mav.addObject("deptList", dept);
        mav.addObject("postname", postname);
        return mav;
    }

    @RequestMapping(value = "addWorkflowrouting")
    public ModelAndView addWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {

        String postcode = parameters.get("postcode");
        String assignedPostcode = parameters.get("assignedPostcode");
        String processId = parameters.get("processId");
        String offcode = parameters.get("offCode");
        String departmentcode = parameters.get("departmentcode");
        wr.setProcessId(processId);
        wr.setOfficeCode(offcode);
        wr.setSltDept(departmentcode);
        wr.setSltOffcode(offcode);
        wr.setGpc(postcode);
        wr.setReportingGpc(assignedPostcode);
        wr.setLoginOffcode(lub.getLoginoffcode());
        workflowRoutingDao.addHierarchy(wr);

        ModelAndView mav = new ModelAndView("redirect:assignworkflowrouting.htm", "command", wr);
        mav.addObject("sltDept", departmentcode);
        mav.addObject("sltOffcode", offcode);
        mav.addObject("processId", processId);
        mav.addObject("gpc", postcode);
        return mav;
    }

    @RequestMapping(value = "removeWorkflowrouting")
    public ModelAndView removeWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {

        String workflowRoutingId = parameters.get("workflowRoutingId");
        wr.setWorkflowRoutingId(Integer.parseInt(workflowRoutingId));
        String postcode = parameters.get("postcode");
        String processId = parameters.get("processId");
        String offcode = parameters.get("offCode");
        String departmentcode = parameters.get("departmentcode");
        wr.setProcessId(processId);
        wr.setOfficeCode(offcode);
        wr.setSltDept(departmentcode);
        wr.setSltOffcode(offcode);
        wr.setGpc(postcode);

        workflowRoutingDao.removeHierarchy(wr);

        ModelAndView mav = new ModelAndView("redirect:assignworkflowrouting.htm", "command", wr);

        mav.addObject("sltDept", departmentcode);
        mav.addObject("sltOffcode", offcode);
        mav.addObject("processId", processId);
        mav.addObject("gpc", postcode);
        return mav;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.task;

import hrms.dao.task.TaskDAO;
import hrms.model.login.LoginUserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
public class TaskController {

    @Autowired
    public TaskDAO taskDAO;
	
    @ResponseBody
    @RequestMapping(value = "taskActionJSON", method = {RequestMethod.POST,RequestMethod.GET})
    public void getTaskListJSON(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String,String> requestParams) {
        
        response.setContentType("application/json");
        
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        
        int total = 0;
        
        int page = 0;
        int rows = 0;
        try {
            HttpSession session = request.getSession();
            LoginUserBean lub = (LoginUserBean) session.getAttribute("LoginUserBean");
            
            if(requestParams.get("page") != null && !requestParams.get("page").equals("")){
                page = Integer.parseInt(requestParams.get("page"));
            }
            if(requestParams.get("rows") != null && !requestParams.get("rows").equals("")){
                rows = Integer.parseInt(requestParams.get("rows"));
            }
            
            String parstatus = requestParams.get("parstatus");
            String empName = requestParams.get("empName");
            System.out.println("Inside taskActionJSON EmpName:"+empName);
            String gpfNo = requestParams.get("gpf");
            
            int process = 0;
            if(requestParams.get("processId") != null && !requestParams.get("processId").equals("")){
                process = Integer.parseInt(requestParams.get("processId"));
            }
            
            
            List li = taskDAO.getMyTaskList(lub.getLoginempid(), lub.getLoginspc(),parstatus, page, rows,empName,gpfNo,process);
            total = taskDAO.getTaskListTotalCnt(lub.getLoginempid(), parstatus);
            
            json.put("total", total);
            json.put("rows", li);
            
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "taskAction", method = RequestMethod.POST)
    public ModelAndView getTaskList(@RequestParam("parstatus") String parstatus,@RequestParam("page") int page,@RequestParam("rows") int rows,@RequestParam("empName") String empName,@RequestParam("gpf") String gpf,@RequestParam("processId") String processId){
        
        try{
            parstatus = parstatus.replaceAll(",","");
            empName = empName.replaceAll(",","");
            gpf = gpf.replaceAll(",","");
            processId = processId.replaceAll(",","");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        return new ModelAndView("redirect:/taskActionJSON.htm?page="+page+"&rows="+rows+"&parstatus="+parstatus+"&empName="+empName+"&gpf="+gpf+"&processId="+processId);
    }
    
    @RequestMapping(value = "taskDetailView", method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView taskDetailView(HttpServletRequest request,HttpServletResponse response,@RequestParam("taskId") String taskId,@RequestParam("auth") String auth,@RequestParam(value = "csrfPreventionSalt", required=false) String csrfPreventionSalt) throws ServletException,IOException{
        
        String actionurl = "";
        //RequestDispatcher reqd = null;
        try{
            actionurl = taskDAO.getDispatcherString(Integer.parseInt(taskId));
            
            //reqd = request.getRequestDispatcher(actionurl+taskId+"&auth="+auth);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        //reqd.forward(request, response);
        return new ModelAndView("redirect:/"+actionurl+taskId+"&parid=0&auth="+auth+"&csrfPreventionSalt="+csrfPreventionSalt);
    }
    
    @ResponseBody
    @RequestMapping(value = "GetWorkflowProcessJSONData",method = RequestMethod.POST)
    public void GetWorkflowProcessJSONData(HttpServletResponse response){
        
        response.setContentType("application/json");
        PrintWriter out = null;

        List processlist = null;
        
        try{
            processlist = taskDAO.getProcessList();

            JSONArray json = new JSONArray(processlist);
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "CompletedTask")
    public String completedTask(Model model,HttpServletRequest request){
        
        List completedtasklist = null;
        List pendinglist = null;
        try{
            HttpSession session = request.getSession();
            LoginUserBean lub = (LoginUserBean) session.getAttribute("LoginUserBean");
            
            String empname1 = taskDAO.getLoggedInEmpNameForCompletedTask(lub.getLoginempid());
            
            completedtasklist = taskDAO.getMyTaskList(lub.getLoginempid(),lub.getLoginspc(),"9",0,0,null,null,0);
            pendinglist = taskDAO.getMyTaskList(lub.getLoginempid(),lub.getLoginspc(),null,0,0,null,null,0);
            
            model.addAttribute("EMPID", lub.getLoginempid());
            model.addAttribute("EMPNAME", empname1);
            model.addAttribute("COMPLETEDTASKS", completedtasklist);
            model.addAttribute("PENDINGTASKS", pendinglist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return "/par/CompletedPARReport";
    }
}

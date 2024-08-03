package hrms.controller.ContractualToRegular;

import hrms.dao.ContractualToRegular.ContractualToRegularDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleListDAO;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.ContractualToRegular.ContractualToRegularForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.payroll.officewisesecondschedulelist.OfficeWiseSecondScheduleForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class ContractualToRegularController {
    
    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;
    
    @Autowired
    public ContractualToRegularDAO contractualToRegularDAO;
    
    @Autowired
    public OfficeWiseSecondScheduleListDAO officewisesecondschedulelistDAO;
    
    @RequestMapping(value = "ContractualToRegularEmployeeList")
    public ModelAndView contractualToRegularList(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("contractualToRegularForm") ContractualToRegularForm contractualToRegularForm){
        
        ModelAndView mav = null;
        
        try{
            mav = new ModelAndView("/ContractualToRegular/AddContractualToRegular", "contractualToRegularForm", contractualToRegularForm);
            
            List contractualToRegularDataList = contractualToRegularDAO.getContractualToRegularEmployeeList(lub.getLoginoffcode());
            mav.addObject("contractualToRegularDataList", contractualToRegularDataList);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "addContractualToRegular")
    public ModelAndView addContractualToRegular(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("contractualToRegularForm") ContractualToRegularForm contractualToRegularForm,@RequestParam Map<String,String> requestParams){
        
        ModelAndView mav = null;
        
        try{
            String status = requestParams.get("status");
            //contractualToRegularForm = contractualToRegularDAO.getContractualToRegularData(selectedEmpObj.getEmpId());
            List contractualToRegularDataList = contractualToRegularDAO.getContractualToRegularEmployeeList(lub.getLoginoffcode());
            
            mav = new ModelAndView("/ContractualToRegular/AddContractualToRegular", "contractualToRegularForm", contractualToRegularForm);
            mav.addObject("contractualToRegularDataList", contractualToRegularDataList);
            mav.addObject("status", status);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    @ResponseBody
    @RequestMapping(value = "getFilteredEmployees", method = RequestMethod.GET)
    public void getFilteredEmployees(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empType") String empType, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            JSONObject json = new JSONObject();
            PrintWriter out = null;
            //sectionDefinationDAO.removePost(spc);
            List jsonlist = new ArrayList();
            jsonlist = contractualToRegularDAO.getFilteredEmployeeList(lub.getLoginoffcode(), empType);
            json.put("empList", jsonlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.getStackTrace();
        }
        //out.write(sp.getGp()+"<==>"+sp.getPostgrp()+"<==>"+sp.getOrderNo()+"<==>"+sp.getOrderDate()+"<==>"+sp.getPayscale()+"<==>"+sp.getPaylevel());
    }
    @RequestMapping(value = "saveContractualToRegular")
    public ModelAndView saveContractualToRegular(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,@ModelAttribute("contractualToRegularForm") ContractualToRegularForm contractualToRegularForm){
        
        ModelAndView mav = null;
        
        try{
            int status = contractualToRegularDAO.saveContractualToRegular(contractualToRegularForm);
            
            if(contractualToRegularDAO.isDuplicate7thPayRevisionData(contractualToRegularForm.getSltEmployee()) == false){
                
                OfficeWiseSecondScheduleForm officeWiseSecondScheduleForm = new OfficeWiseSecondScheduleForm();
                
                officeWiseSecondScheduleForm.setEmpid(contractualToRegularForm.getSltEmployee());
                officeWiseSecondScheduleForm.setHidPostCode(contractualToRegularForm.getPostedspc());
                officeWiseSecondScheduleForm.setPayscale(contractualToRegularForm.getSltPayScale());
                if(contractualToRegularForm.getSltGradePay() != null && !contractualToRegularForm.getSltGradePay().equals("")){
                    officeWiseSecondScheduleForm.setGp(Integer.parseInt(contractualToRegularForm.getSltGradePay()));
                }else{
                    officeWiseSecondScheduleForm.setGp(0);
                }
                officeWiseSecondScheduleForm.setRdOptionChosen(contractualToRegularForm.getRdOptionChosen());
                officeWiseSecondScheduleForm.setTxtDateEntered(contractualToRegularForm.getTxtPayRevisionDate());
                officeWiseSecondScheduleForm.setOffcode(lub.getLoginoffcode());
                
                officewisesecondschedulelistDAO.saveSecondScheduleData(officeWiseSecondScheduleForm);
            }
            
            mav = new ModelAndView("redirect:/addContractualToRegular.htm?status="+status);
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "saveContractualToRegular", params="btnContrToReg=Delete")
    public ModelAndView deleteContractualToRegular(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,@ModelAttribute("contractualToRegularForm") ContractualToRegularForm contractualToRegularForm){
        
        ModelAndView mav = null;
        
        try{
            int status = contractualToRegularDAO.deleteContractualToRegular(contractualToRegularForm);
            if(status == 1){
                status = 2;
            }
            mav = new ModelAndView("redirect:/addContractualToRegular.htm?status="+status);
        }catch(Exception e){
            e.printStackTrace();
        }
      return mav;  
    }
}

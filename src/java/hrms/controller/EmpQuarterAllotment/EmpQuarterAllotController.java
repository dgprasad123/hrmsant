/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.EmpQuarterAllotment;

import hrms.dao.EmpQuarterAllotment.EmpQuarterAllotDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EmpQuarterAllotController implements ServletContextAware {

    @Autowired
    public EmpQuarterAllotDAO empQuarterAllotDAO;

    @Autowired
    public NotificationDAO notificationDao;

    private ServletContext context;

    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "addEmpQuarterAllotment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addEmpQuarterAllotment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        EmpQuarterBean eqBean = new EmpQuarterBean();
        mav.addObject("isSurrendered", empQuarterAllotDAO.getAllotedQuarterCount(selectedEmpObj.getEmpId()));
        mav.addObject("quarterList", empQuarterAllotDAO.getQuarterList());
        mav.addObject("eqList", empQuarterAllotDAO.getEmpQuarterAllotList(selectedEmpObj.getEmpId()));
        mav.addObject("unitList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.addObject("qtrtypeList", empQuarterAllotDAO.getqtrTypeList(eqBean.getQrtrunit()));
        mav.addObject("buildingList", empQuarterAllotDAO.getbuildingList(eqBean.getQrtrunit(),eqBean.getQrtrtype()));
        mav.addObject("eqBean", eqBean);
        mav.addObject("empId", selectedEmpObj.getEmpId());
        mav.addObject("opt", "add");
        mav.addObject("qaId", 0);
        path = "EmpQuarterAllotment/EmpQtrAllotment";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "editEmpQuarterAllotment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editEmpQuarterAllotment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("qaId") String qaId) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        EmpQuarterBean eqBean = new EmpQuarterBean();
        eqBean = empQuarterAllotDAO.getAllotedQuarterDetail(qaId);
        mav.addObject("quarterList", empQuarterAllotDAO.getQuarterList());
        mav.addObject("unitList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.addObject("qtrtypeList", empQuarterAllotDAO.getqtrTypeList(eqBean.getQrtrunit()));
        mav.addObject("buildingList", empQuarterAllotDAO.getbuildingList(eqBean.getQrtrunit(),eqBean.getQrtrtype()));
        mav.addObject("eqList", empQuarterAllotDAO.getEmpQuarterAllotList(selectedEmpObj.getEmpId()));
        
        mav.addObject("eqBean", eqBean);
        mav.addObject("empId", selectedEmpObj.getEmpId());
        mav.addObject("qaId", qaId);
        mav.addObject("opt", "edit");
        path = "EmpQuarterAllotment/EmpQtrAllotment";
        mav.setViewName(path);
        return mav;
    }
    
    @RequestMapping(value = "viewEmpQuarterAllotment")
    public ModelAndView viewEmpQuarterAllotment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("qaId") String qaId) {
        
        ModelAndView mav = new ModelAndView();
        
        String path = "";
        
        EmpQuarterBean eqBean = new EmpQuarterBean();
        eqBean = empQuarterAllotDAO.getAllotedQuarterDetail(qaId);
        mav.addObject("eqBean", eqBean);
        
        mav.addObject("qtrpoolname",empQuarterAllotDAO.getQuarterPoolName(eqBean.getqId()));
        
        mav.addObject("eqList", empQuarterAllotDAO.getEmpQuarterAllotList(selectedEmpObj.getEmpId()));
        
        mav.addObject("opt", "view");
        
        path = "EmpQuarterAllotment/ViewEmpQtrAllotment";
        mav.setViewName(path);
        return mav;
    }
    
    @RequestMapping(value = "saveEmpQuarterAllotment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveEmpQuarterAllotment(@ModelAttribute("empQuarterBean") EmpQuarterBean eqBean) {
        ModelAndView mav = new ModelAndView();
        NotificationBean nb = new NotificationBean();
        int notId = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            nb.setEmpId(eqBean.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(eqBean.getOrderNumber());
            nb.setOrdDate(sdf.parse(eqBean.getOrderDate()));
            nb.setNottype("QTR_ALLOT");
            if(eqBean.getChkNotSBPrint() != null && eqBean.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            notId = notificationDao.insertNotificationData(nb);
            empQuarterAllotDAO.saveEmpQuarterDetails(eqBean, notId);
            //mav.addObject("empQuarterForm", eqBean);
            String path = null;
            path = "redirect:/addEmpQuarterAllotment.htm";
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    @RequestMapping(value = "updateEmpQuarterAllotment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateEmpQuarterAllotment(@ModelAttribute("empQuarterBean") EmpQuarterBean eqBean) {
        ModelAndView mav = new ModelAndView();

        empQuarterAllotDAO.updateEmpQuarterDetails(eqBean);
        //mav.addObject("empQuarterForm", eqBean);
        String path = null;
        path = "redirect:/addEmpQuarterAllotment.htm";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "surrenderQuarter", method = {RequestMethod.POST, RequestMethod.GET})
    public void surrenderQuarter(@RequestParam("qa_id") String qaId, @RequestParam("emp_id") String empId, @RequestParam("order_number") String orderNumber, @RequestParam("order_date") String orderDate, 
            @RequestParam("surrender_date") String surrenderDate, @ModelAttribute("empQuarterBean") EmpQuarterBean eqBean) {
        NotificationBean nb = new NotificationBean();
        int notId = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            nb.setEmpId(empId);
            nb.setDateofEntry(new Date());
            nb.setOrdno(orderNumber);
            nb.setOrdDate(sdf.parse(orderDate));
            nb.setNottype("QTR_SURRENDER");
            if(eqBean.getNotToPrintSB() != null && eqBean.getNotToPrintSB().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }  
            notId = notificationDao.insertNotificationData(nb);
            empQuarterAllotDAO.saveSurrenderQuarter(qaId, empId, notId, surrenderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //mav.addObject("empQuarterForm", eqBean);
    }

    @RequestMapping(value = "OfficeWiseQtrList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OfficeWiseQtrList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        List li = empQuarterAllotDAO.getOfficeWiseQuarterList(lub.getLoginoffcode());
        
        mav.addObject("quarterList", empQuarterAllotDAO.getOfficeWiseQuarterList(lub.getLoginoffcode()));
        mav.addObject("officeName", lub.getLoginoffname());
        path = "EmpQuarterAllotment/OfficeWiseQtrList";
        mav.setViewName(path);
        return mav;
    }
    
    @ResponseBody
    @RequestMapping(value = "getQuarterList")
    public void getQuarterList(HttpServletRequest request, HttpServletResponse response, @RequestParam("qrtrunit") String qrtrunit) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList qtrtypeList = empQuarterAllotDAO.getqtrTypeList(qrtrunit);
        JSONArray json = new JSONArray(qtrtypeList );
        out = response.getWriter();
        out.write(json.toString());
    } 
    
    @ResponseBody
    @RequestMapping(value = "getBuildingNumList")
    public void getBuildingNumList(HttpServletRequest request, HttpServletResponse response, @RequestParam("qrtrunit") String qrtrunit,@RequestParam("qrtrtype") String qrtrtype) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList buildingList = empQuarterAllotDAO.getbuildingList(qrtrunit,qrtrtype);
        JSONArray json = new JSONArray(buildingList );
        out = response.getWriter();
        out.write(json.toString());
    } 
}

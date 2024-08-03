/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.arrear;

import hrms.common.CommonFunctions;
import hrms.dao.payroll.arrear.ArrmastDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.arrear.OAClaimModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author prashant
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class OAClaimController {
    
    @Autowired
    public ArrmastDAO arrmastDAO;
    
    @Autowired
    BillGroupDAO billGroupDAO;
    
    @RequestMapping(value = "OaClaimReport.htm")
    public ModelAndView OAClaimEmployeeListPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OAClaimModel") OAClaimModel claimModel, ModelMap model) {
        
        ModelAndView mav = new ModelAndView();
        List oaClaimEmpList = new ArrayList();
        List billGroupList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
        if(claimModel.getBillGroupName() != null){
            oaClaimEmpList = arrmastDAO.getOaClaimEmployeeList(claimModel.getBillGroupName());
        }
        
        List objHeadList = arrmastDAO.getObjectHeadList();
        List monthList = CommonFunctions.getMonthNameList();
        List yearList = CommonFunctions.getYearList();
        
        mav = new ModelAndView("/payroll/arrear/OAClaim", "OAClaimModel", claimModel);
        
        mav.addObject("billGroup", billGroupList);
        mav.addObject("objHeadList", objHeadList);
        mav.addObject("oaClaimList", oaClaimEmpList);
        mav.addObject("monthList", monthList); 
        mav.addObject("yearList", yearList);
        return mav;
    }
    
    @RequestMapping(value = "oaClaimData", method = RequestMethod.GET)
    public ModelAndView GetOaClaimData(HttpServletRequest request, @ModelAttribute("OAClaimModel") OAClaimModel claimModel, HttpServletResponse response, 
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String,String> requestParams) throws IOException {
        
        String path = "/payroll/arrear/OAClaimData";
        ModelAndView mav = new ModelAndView();
        String empId ="";
        String billGroupId = "";
        try {
            
            List objHeadList = arrmastDAO.getObjectHeadList();
            List monthList = CommonFunctions.getMonthNameList();
            List yearList = CommonFunctions.getYearList();
            if(requestParams.get("empId") != null){
                empId = requestParams.get("empId");
                claimModel = arrmastDAO.getOaClaimEmpData(empId, requestParams.get("bid"));
            }    
            mav = new ModelAndView(path, "OAClaimModel", claimModel);

            mav.addObject("objHeadList", objHeadList);
            mav.addObject("monthList", monthList); 
            mav.addObject("yearList", yearList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }
    
    @RequestMapping(value = "OaClaimReport.htm", params = "action=Update")
    public ModelAndView SaveOaClaimAmount( HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub, 
            @ModelAttribute("OAClaimModel") OAClaimModel claimModel ) {
        
        ModelAndView mav = new ModelAndView();
        
        try {
            
            if(claimModel.getHidType().equals("I")){
                arrmastDAO.insertOaClaimData(claimModel, lub.getLoginoffcode());
            }else if(claimModel.getHidType().equals("U")){
                arrmastDAO.updateOaClaimData(claimModel, lub.getLoginoffcode());
            }
            List billGroupList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
            String billGrpName = CommonFunctions.decodedTxt(claimModel.getBillGroupName());
            mav.setViewName("redirect:/OaClaimReport.htm?billGroupName="+billGrpName); 
            mav.addObject("billGroup", billGroupList);

        } catch (Exception e) {
            e.printStackTrace();
        } 
        return mav;
    }
}

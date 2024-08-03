/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.tab;

import hrms.common.CommonFunctions;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.OfficeDAO;
import hrms.dao.report.gispassbook.GisPassbookDAO;
import hrms.dao.tab.TabDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.UserExpertise;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.tab.DistrictTreeAttr;
import hrms.model.tab.OfficeTreeAttr;
import hrms.model.tab.TabForm;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes("LoginUserBean")
public class TabController {

    @Autowired
    public TabDAO tabDAO;

    @Autowired
    public LoginDAOImpl loginDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public GisPassbookDAO gispassbookDao;

    @RequestMapping(value = "tabController", method = RequestMethod.GET)
    public ModelAndView getTabPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("tform") TabForm tform,
            BindingResult result, @RequestParam("rollId") String rollId) {
        ModelAndView mav = new ModelAndView();

        String path = "index";
        try {
            String rollName = CommonFunctions.decodedTxt(rollId);
            String empId = lub.getLoginempid();
            tform.setRollId(rollId);
            if (rollName.equals("01")) {
                path = "tab/MyCadre";
            } else if (rollName.equals("02")) {
                path = "mydepartment";
            } else if (rollName.equals("03")) {
                path = "tab/MyDistrict";
            } else if (rollName.equals("04")) {
                path = "tab/MyHod";
            } else if (rollName.equals("05")) {
                path = "tab/MyOffice";

            } else if (rollName.equals("10")) {
                path = "paradmin";
            } else if (rollName.equals("11")) {
                path = "tab/MyDashboard";
            }
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);

            tform.setEmployeeId(empId);
            tform.setOffCode(lub.getLoginoffcode());
            List gisAlertData = gispassbookDao.getGisAlertList(lub.getLoginoffcode(), year);

            mav.addObject("gisAlertData", gisAlertData);
            mav.addObject("tform", tform);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "myPage", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView myPageTab(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, ModelMap model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String path = "index";

        String empid = "";

        try {

            empid = lub.getLoginempid();
            if (empid != null && !empid.equals("")) {

                Users emp = loginDao.getEmployeeProfileInfo(empid);
                mav.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
                mav.addObject("users", emp);

                path = "/tab/mypage";
            } else {

                model.addAttribute("errorMsg", "Invalid User or Password");
                path = "index";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "employeeTree")
    public void getEmployeeTree(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("empid") String empId, @RequestParam("id") String id) {

        PrintWriter out = null;

        try {
            out = response.getWriter();
            List li = tabDAO.getOfficeListXML(empId);

            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());

            /*  
             out = response.getWriter();
             OfficeTreeAttr ofattr = tabDAO.getHODOfficeListXML(lub.getLoginoffcode());
             JSONObject jsonObj = new JSONObject(ofattr);
             JSONArray jsonArray = new JSONArray();
             jsonArray.put(jsonObj);
             out = response.getWriter();
             out.write(jsonArray.toString());
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();

        }
    }

    @ResponseBody
    @RequestMapping(value = "saveExpertise", method = {RequestMethod.GET, RequestMethod.POST})
    public String myPageTab(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("frmExpertise") UserExpertise uExpertise, HttpServletRequest request) {
        uExpertise.setEmpid(lub.getLoginempid());
        String result = tabDAO.saveExpertise(uExpertise);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "saveVisitedData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String saveVisitedData(@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("visitedDate") String visitedDate, HttpServletRequest request) {
        // uExpertise.setEmpid(lub.getLoginempid());
        lub.setDateOfVisit(visitedDate);
        String result = tabDAO.saveVisitedData(lub);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "sendMessageToDc.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String sendMsgToDistrictCoordinator(@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("distCode") String distCode ,@RequestParam("offCode") String offCode, HttpServletRequest request) {
        // uExpertise.setEmpid(lub.getLoginempid());
        lub.setLoginoffcode(offCode);
        lub.setLogindistrictcode(distCode);
        String result = tabDAO.sendMsgToDc(lub);
        return result;
    }

    @ResponseBody  
    @RequestMapping(value = "checkLastNoOfDays.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String noOfdaysOfLastEntry(@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("loginempid") String loginempid , HttpServletRequest request) {
        
        lub.setLoginempid(loginempid);
        String result = tabDAO.noOfDaysOfLastEntry(lub);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "hodTree")
    public void getHodTree(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            OfficeTreeAttr ofattr = tabDAO.getHODOfficeListXML(lub.getLoginoffcode());
            JSONObject jsonObj = new JSONObject(ofattr);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObj);
            out = response.getWriter();
            out.write(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();

        }
    }

    @ResponseBody
    @RequestMapping(value = "districtOfficeTree")
    public void getDistrictOfficeListXML(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Office office = offDAO.getOfficeDetails(lub.getLoginoffcode());
            DistrictTreeAttr distAttr = tabDAO.getDistrictOfficeListXML(office.getDistCode());
            JSONObject jsonObj = new JSONObject(distAttr);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObj);
            out = response.getWriter();
            out.write(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();

        }
    }

    @ResponseBody
    @RequestMapping(value = "cadreTree")
    public void getCadreTree(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            OfficeTreeAttr ofattr = tabDAO.getCadreListXML(lub.getLoginoffcode(), lub.getLoginspc());
            JSONObject jsonObj = new JSONObject(ofattr);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObj);
            out = response.getWriter();
            out.write(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();
        }
    }
}

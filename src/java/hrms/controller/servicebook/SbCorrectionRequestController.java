package hrms.controller.servicebook;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Payscale;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class SbCorrectionRequestController implements ServletContextAware {

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @Autowired
    public PayScaleDAO payscaleDAO;
    
    @Autowired
    protected ServiceBookLanguageDAO sbDAO;
    
    @Autowired
    public IncrementSanctionDAO incrementsancDAO;
    
    @Autowired
    public DepartmentDAO deptDAO;
    
    @Autowired
    public OfficeDAO offDAO;
    
     @Autowired
    public AqFunctionalities aqfunctionalities;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "sbCorrectRequest.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadSbCorrectRequestPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request) {

        List sbModuleNameList = sbCorReqDao.getServiceBookModuleNameList(lub.getLoginempid());
        ModelAndView mv = new ModelAndView("/servicebook/MyServiceBookCorrectRequest");
        mv.addObject("LoadModuleNameList", sbModuleNameList);
        return mv;
    }

    @RequestMapping(value = "sbCorrectRequest.htm", params = "btnRequest=Search", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadSbCorrectRequestSearch(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SbCorReqBean") SbCorrectionRequestModel sbCorReqBean) {

        String sbModType = sbCorReqBean.getSbReqModuleName();
        //System.out.println("the val of sbModName is=======" + sbModType);
        List sbModuleNameList = sbCorReqDao.getServiceBookModuleNameList(lub.getLoginempid());
        List sbModuleDataList = sbCorReqDao.getServiceBookModuleDataList(lub.getLoginempid(), sbModType,lub.getLoginoffcode());
        ModelAndView mv = new ModelAndView("/servicebook/MyServiceBookCorrectRequest");
        mv.addObject("LoadModuleNameList", sbModuleNameList);
        mv.addObject("LoadModuleDataList", sbModuleDataList);
        mv.addObject("ModType", sbModType);
        return mv;
    }

    /*@RequestMapping(value = "sbCorrectReqModal.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView ServiceBookCorrectReqModal(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request,
            @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        String empHrmsId = "";
        String noteIdEnc = "";
        String moduleType = "";

        try {
            if (requestParams.get("linkId") != null && requestParams.get("tabId") != null) {
                empHrmsId = lub.getLoginempid();
                noteIdEnc = CommonFunctions.decodedTxt(requestParams.get("tabId"));
                moduleType = requestParams.get("modName");

                System.out.println("the val from model is=====" + empHrmsId + "==========" + noteIdEnc + "=========" + moduleType);
                SbCorrectionRequestModel sbDataBean = sbCorReqDao.getServiceBookModuleData(empHrmsId, moduleType, noteIdEnc);
                sbDataBean.setHidNotId(requestParams.get("tabId"));
                sbDataBean.setHidNotType(sbDataBean.getTabName());
                mav = new ModelAndView("/servicebook/MySbCorReqModal", "SbCorReqBean", sbDataBean);
                mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
                mav.addObject("notid", noteIdEnc);
                mav.addObject("nottype", sbDataBean.getTabName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }*/
    
    @RequestMapping(value = "sbCorrectReqModal.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView ServiceBookCorrectReqModal(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request,
            @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        String empHrmsId = "";
        String noteIdEnc = "";
        String moduleType = "";

        try {
            if (requestParams.get("linkId") != null && requestParams.get("tabId") != null) {
                empHrmsId = lub.getLoginempid();
                noteIdEnc = CommonFunctions.decodedTxt(requestParams.get("tabId"));
                moduleType = requestParams.get("modName");

                System.out.println("the val from model is=====" + empHrmsId + "==========" + noteIdEnc + "=========" + moduleType);
                SbCorrectionRequestModel sbDataBean = sbCorReqDao.getServiceBookModuleData(empHrmsId, moduleType, noteIdEnc);
                sbDataBean.setHidNotId(requestParams.get("tabId"));
                sbDataBean.setHidNotType(sbDataBean.getTabName());
                mav = new ModelAndView("/servicebook/MySbCorReqModal", "SbCorReqBean", sbDataBean);
                mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
                mav.addObject("notid", noteIdEnc);
                mav.addObject("nottype", sbDataBean.getTabName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "sbCorrectRequest.htm", params = "btnRequest=Request to Rectify", method = {RequestMethod.GET, RequestMethod.POST})
    public String saveServiceBookRequestedData(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SbCorReqBean") SbCorrectionRequestModel sbCorReqBean) {

        try {
            sbCorReqBean.setEmpHrmsId(lub.getLoginempid());
            sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName="+sbCorReqBean.getHidNotType();
    }
    
    @RequestMapping(value = "DDOServiceBookCorrection.htm")
    public String DDOServiceBookCorrection(Model model,@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SbCorReqBean") SbCorrectionRequestModel sbCorReqBean,@RequestParam("empid") String empid) {

        try {
            List sbcorrectionlist = sbCorReqDao.getDDOEmployeeWiseServiceBookCorrectionData(empid);
            model.addAttribute("sbcorrectionlist", sbcorrectionlist);
            model.addAttribute("empid", empid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/servicebook/DDOServiceBookCorrectionPage";
    }
    
    @RequestMapping(value = "RejectRequestedServiceBookLanguage")
    public String RejectRequestedServiceBookLanguage(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SbCorReqBean") SbCorrectionRequestModel sbCorReqBean,@RequestParam("empid") String empid){
        try{
            sbCorReqDao.rejectRequestedServiceBookLanguage(empid,sbCorReqBean.getHidNotType(),sbCorReqBean.getHidNotId(),lub.getLoginempid());
        }catch(Exception e){
            e.printStackTrace();
        }
      return "redirect:/DDOServiceBookCorrection.htm?empid="+empid;
    }
    
    @RequestMapping(value = "ServiceBookRead")
    public String ServiceBookRead(){
        
      return "/servicebook/tarunpaul";
    }
    
    @ResponseBody
    @RequestMapping(value = "GetSBCorrectionModuleDataList.htm")
    public void GetEmployeeServiceBookDataForSBCorrection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("moduleName") String moduleName) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = new JSONArray();
        try {

            List sbModuleDataList = sbCorReqDao.getServiceBookModuleDataList(lub.getLoginempid(), moduleName,lub.getLoginoffcode());

            json = new JSONArray(sbModuleDataList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "RequestedEmployeeListSBCorrection.htm")
    public String RequestedEmployeeListSBCorrection(Model model,@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SbCorReqBean") SbCorrectionRequestModel sbCorReqBean) {

        try {
            List emplist = sbCorReqDao.getRequestedEmployeeListSBCorrection(lub.getLoginoffcode());
            model.addAttribute("emplist", emplist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/servicebook/RequestedEmployeeListSBCorrection";
    }

}

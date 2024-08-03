package hrms.controller.joining;

import hrms.common.CommonFunctions;
import hrms.dao.joining.JoiningDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.joining.JoiningForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class JoiningController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public JoiningDAO joiningDAO;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;
    
    @RequestMapping(value = "JoiningList")
    public ModelAndView JoiningListPage(@ModelAttribute("SelectedEmpObj") Users lub,@ModelAttribute("jForm") JoiningForm jForm) {

        List joininglist = null;
        
        ModelAndView mav = null;
        try{
            
            jForm.setJempid(lub.getEmpId());
            joininglist = joiningDAO.getJoiningList(jForm.getJempid());
            
            mav = new ModelAndView("/joining/JoiningList", "jForm", jForm);
            mav.addObject("joininglist", joininglist);
        }catch(Exception e){
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "enterJoiningData")
    public ModelAndView enterJoiningData(@ModelAttribute("LoginUserBean") LoginUserBean lub1,@ModelAttribute("SelectedEmpObj") Users lub, @RequestParam("notId") int notId, @RequestParam("rlvId") String rlvId,@RequestParam("leaveId") String leaveid,@RequestParam("addl") String addl,@RequestParam("jId") String jId) throws IOException {

        ModelAndView mav = null;

        JoiningForm joinform = null;
        try {
            
            joinform = joiningDAO.getJoiningData(lub.getEmpId(),notId,rlvId,leaveid,addl,jId);
            
            mav = new ModelAndView("/joining/AddJoining", "jForm", joinform);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            
            List officeList = offDAO.getTotalOfficeList(joinform.getHidDeptCode(), joinform.getHidDistCode());
            mav.addObject("officeList", officeList);
            
            List gpclist = substantivePostDAO.getGenericPostList(joinform.getHidOffCode());
            mav.addObject("gpclist", gpclist);
            
            List fieldofflist = offDAO.getFieldOffList(joinform.getHidOffCode());
            mav.addObject("fieldofflist", fieldofflist);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
            List goipostlist = substantivePostDAO.getGOIOficeWisePostList(joinform.getHidGOIOffCode());
            mav.addObject("goipostlist", goipostlist);
            
            mav.addObject("entpsc", lub1.getLoginspc());
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "viewJoiningData")
    public ModelAndView viewJoiningData(@ModelAttribute("LoginUserBean") LoginUserBean lub1,@ModelAttribute("SelectedEmpObj") Users lub, @RequestParam("notId") int notId, @RequestParam("rlvId") String rlvId,@RequestParam("leaveId") String leaveid,@RequestParam("addl") String addl,@RequestParam("jId") String jId) throws IOException {

        ModelAndView mav = new ModelAndView();

        JoiningForm joinform = null;
        try {
            
            joinform = joiningDAO.getJoiningData(lub.getEmpId(),notId,rlvId,leaveid,addl,jId);
            
            mav.addObject("joinform", joinform);
            mav.setViewName("/joining/ViewJoining");
            
            String postdeptname = deptDAO.getDeptName(joinform.getHidDeptCode());
            mav.addObject("posteddeptname", postdeptname);
            
            Office office = offDAO.getOfficeDetails(joinform.getHidOffCode());
            mav.addObject("postedoffice", office.getOffEn());
            
            office = offDAO.getOfficeDetails(joinform.getSltFieldOffice());
            mav.addObject("fieldoffice", office.getOffEn());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }

    @RequestMapping(value = "saveEmployeeJoining", params = "action=Save")
    public ModelAndView saveEmployeeJoining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("jForm") JoiningForm joinform) {        
        ModelAndView mav = null;       
        List joininglist = null;
        try { 
            joiningDAO.saveJoining(joinform,lub1.getLogindeptcode(),lub1.getLoginoffcode(),lub1.getLoginspc(),lub1.getLoginuserid());  
            joininglist = joiningDAO.getJoiningList(joinform.getJempid());            
            mav = new ModelAndView("/joining/JoiningList", "jForm", joinform);
            mav.addObject("joininglist", joininglist);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }
    @RequestMapping(value = "saveEmployeeJoining", params = "action=Back")
    public ModelAndView backJoiningListPage(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("jForm") JoiningForm joinform) {  
        ModelAndView mv = new ModelAndView("redirect:/JoiningList.htm");
        return mv;
    }
    @RequestMapping(value = "deleteJoining")
    public void deleteJoining(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("joiningForm") JoiningForm joinform) {

        try {
            joinform.setJempid(lub.getEmpId());
            joiningDAO.deleteJoining(joinform);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "joiningGetFieldOffListJSON")
    public void GetFieldOffListJSON(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam("offcode") String offcode) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List fieldofflist = null;
        try {
            if(offcode != null && !offcode.equals("")){
                
                fieldofflist = offDAO.getFieldOffList(offcode);
            }else{
                
                fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());
            }

            JSONArray json = new JSONArray(fieldofflist);
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
    @RequestMapping(value = "joiningGetGenericPostListJSON")
    public void joiningGetGenericPostListJSON(HttpServletResponse response,@RequestParam("offcode") String offcode) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List genericpostlist = null;
        try {
            
            genericpostlist = substantivePostDAO.getGenericPostList(offcode);
            
            JSONArray json = new JSONArray(genericpostlist);
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
    @RequestMapping(value = "joiningGetGPCWiseSPCListJSON")
    public void joiningGetGPCWiseSPCListJSON(HttpServletResponse response,@ModelAttribute("SelectedEmpObj") Users lub,@RequestParam("offcode") String offcode,@RequestParam("gpc") String gpc) {
        response.setContentType("application/json");
        PrintWriter out = null;
        List gpcwisespclist = null;
        try {            
            gpcwisespclist = substantivePostDAO.getGPCWiseSectionWiseSPCList(offcode, gpc);                                    
            JSONArray json = new JSONArray(gpcwisespclist);
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
    @RequestMapping(value = "getGPCWiseSPCListJSON")
    public void getGPCWiseSPCListJSON(HttpServletResponse response,@RequestParam("offcode") String offcode,@RequestParam("gpc") String gpc) {
        response.setContentType("application/json");
        PrintWriter out = null;
        List gpcwisespclist = null;
        try {            
            gpcwisespclist = substantivePostDAO.getGPCWiseSPCList(offcode, gpc);                                    
            JSONArray json = new JSONArray(gpcwisespclist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "EditJoiningSBCorrection")
    public ModelAndView EditJoiningSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams,@RequestParam("notId") String notId, @RequestParam("rlvId") String rlvId,@RequestParam("jId") String jId,@RequestParam("mode") String mode) throws IOException {

        ModelAndView mav = null;

        JoiningForm joinform = null;
        try {
            int notificationId = 0;
            if(notId != null && !notId.equals("")){
                notificationId = Integer.parseInt(CommonFunctions.decodedTxt(notId));
            }
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if(correctionid > 0){
                joinform = joiningDAO.getJoiningDataSBCorrection(lub.getLoginempid(),notificationId,rlvId,null,null,jId,correctionid);
            }else{
                joinform = joiningDAO.getJoiningData(lub.getLoginempid(),notificationId,rlvId,null,null,jId);
            }
            
            mav = new ModelAndView("/joining/EditJoiningSBCorrection", "jForm", joinform);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            
            List officeList = offDAO.getTotalOfficeList(joinform.getHidDeptCode(), joinform.getHidDistCode());
            mav.addObject("officeList", officeList);
            
            List gpclist = substantivePostDAO.getGenericPostList(joinform.getHidOffCode());
            mav.addObject("gpclist", gpclist);
            
            List fieldofflist = offDAO.getFieldOffList(joinform.getHidOffCode());
            mav.addObject("fieldofflist", fieldofflist);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
            List goipostlist = substantivePostDAO.getGOIOficeWisePostList(joinform.getHidGOIOffCode());
            mav.addObject("goipostlist", goipostlist);
            
            mav.addObject("entpsc", lub.getLoginspc());
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "saveEmployeeJoiningSBCorrection", params = "action=Save")
    public String saveEmployeeJoiningSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("jForm") JoiningForm joinform) {        
        try {
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(joinform.getJempid());
            sbCorReqBean.setHidNotId(joinform.getNotId()+"");
            sbCorReqBean.setHidNotType("JOINING");
            sbCorReqBean.setEntrytype(null);
            sbCorReqBean.setModuleid(joinform.getJoinId());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(), sbCorReqBean.getHidNotType(), sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);
            String sblang = joiningDAO.saveJoiningSBCorrection(joinform,lub1.getLogindeptcode(),lub1.getLoginoffcode(),lub1.getLoginspc(),lub1.getLoginuserid(),sbcorrectionid);
            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid,sblang);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=JOINING";  
    }
    
    @RequestMapping(value = "saveEmployeeJoiningSBCorrection", params = "action=Submit")
    public String submitEmployeeJoiningSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") JoiningForm joinform) {        
        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(),joinform.getNotId(),"JOINING");            
        } catch (Exception e) {
            e.printStackTrace();
        }
      return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=JOINING";  
    }
    
    @RequestMapping(value = "EditJoiningSBCorrectionDDO")
    public ModelAndView EditJoiningSBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams,@RequestParam("empid") String empid,@RequestParam("notId") int notId, @RequestParam("rlvId") String rlvId,@RequestParam("jId") String jId) throws IOException {

        ModelAndView mav = null;

        JoiningForm joinform = null;
        try {
            int correctionid = Integer.parseInt(requestParams.get("correctionid"));
            
            String mode = requestParams.get("mode");
            
            joinform = joiningDAO.getJoiningDataSBCorrection(empid,notId,rlvId,null,null,jId,correctionid);
            joinform.setJempid(empid);
            mav = new ModelAndView("/joining/EditJoiningSBCorrection", "jForm", joinform);
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            
            List officeList = offDAO.getTotalOfficeList(joinform.getHidDeptCode(), joinform.getHidDistCode());
            mav.addObject("officeList", officeList);
            
            List gpclist = substantivePostDAO.getGenericPostList(joinform.getHidOffCode());
            mav.addObject("gpclist", gpclist);
            
            List fieldofflist = offDAO.getFieldOffList(joinform.getHidOffCode());
            mav.addObject("fieldofflist", fieldofflist);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
            List goipostlist = substantivePostDAO.getGOIOficeWisePostList(joinform.getHidGOIOffCode());
            mav.addObject("goipostlist", goipostlist);
            
            mav.addObject("entpsc", lub.getLoginspc());
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return mav;  
    }
    
    @RequestMapping(value = "saveEmployeeJoiningSBCorrection", params = {"action=Approve"})
    public String approveIncrementSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("jForm") JoiningForm joinform) {

        try {
            joiningDAO.approveJoiningSBCorrection(joinform,lub.getLogindeptcode(),lub.getLoginoffcode(),lub.getLoginspc(),lub.getLoginuserid());

            sbCorReqDao.updateRequestedServiceBookFlag(joinform.getJempid(), "JOINING", joinform.getNotId() + "",joinform.getCorrectionid(),lub.getLoginempid());

        } catch (Exception e) {
            e.printStackTrace();
        }
      return "redirect:/DDOServiceBookCorrection.htm?empid="+joinform.getJempid();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.serviceVerification;

import hrms.common.CommonFunctions;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.serviceVerification.ServiceVerificationDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.SubstantivePost;
import hrms.model.serviceVerification.ServiceVerification;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
@Controller
public class ServiceVerificationController {

    @Autowired
    public ServiceVerificationDAO serviceVerificationDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @RequestMapping(value = "serviceVerifyList")
    public ModelAndView serviceVerifyList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;

        int noofdaysofserviceperiod = 0;
        try {
            serviceVerify.setTxtempid(selectedEmpObj.getEmpId());
            List svList = serviceVerificationDAO.findAllSvData(serviceVerify.getTxtempid());

            mav = new ModelAndView("/serviceVerification/ServiceVerificationList", "command", serviceVerify);

            String emplyeedoegov = serviceVerificationDAO.getEmployeeDateOfEntry(serviceVerify.getTxtempid());
            String emplyeedos = serviceVerificationDAO.getEmployeeDateOfRetirement(serviceVerify.getTxtempid());

            Date emplyeedoegov1 = new SimpleDateFormat("yyyy-MM-dd").parse(emplyeedoegov);
            Date emplyeedos1 = new SimpleDateFormat("yyyy-MM-dd").parse(emplyeedos);
            String emplyeedos2 = new SimpleDateFormat("dd-MM-yyyy").format(emplyeedos1);

            Calendar cal = Calendar.getInstance();
            Date currentdate = cal.getTime();
            String currentdate1 = new SimpleDateFormat("yyyy-MM-dd").format(currentdate);
            String currentdate2 = new SimpleDateFormat("dd-MM-yyyy").format(currentdate);
            Date currentdate3 = new SimpleDateFormat("yyyy-MM-dd").parse(currentdate1);
            String doj = CommonFunctions.getFormattedOutputDate1(emplyeedoegov1);
            if (currentdate3.compareTo(emplyeedos1) > 0) {
                //currentdate1 = new SimpleDateFormat("yyyy-MM-dd").format(emplyeedos);
                noofdaysofserviceperiod = serviceVerificationDAO.getQualifyingDays(emplyeedoegov, emplyeedos);
                mav.addObject("currentdate2", emplyeedos2);
            } else {
                noofdaysofserviceperiod = serviceVerificationDAO.getQualifyingDays(emplyeedoegov, currentdate1);
                mav.addObject("currentdate2", currentdate2);
            }
            int totalqualifying = 0;
            for (int i = 0; i < svList.size(); i++) {
                ServiceVerification svb = (ServiceVerification) svList.get(i);
                totalqualifying = totalqualifying + svb.getqDays();
            }

            mav.addObject("ServiceVerifyList", svList);
            mav.addObject("noofdaysofserviceperiod", noofdaysofserviceperiod);
            mav.addObject("nonqualifyingdays", noofdaysofserviceperiod - totalqualifying);
            mav.addObject("doj", doj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "serviceVerifyEntryController")
    public ModelAndView serviceVerifyEntry(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;

        try {
            if (serviceVerify.getRadpostingauthtype() == null || serviceVerify.getRadpostingauthtype().equals("")) {
                serviceVerify.setRadpostingauthtype("GOO");
            }

            List deptlist = deptDAO.getDepartmentList();
            List otherOrgOfflist = offDAO.getOtherOrganisationList();

            mav = new ModelAndView("/serviceVerification/ServiceVerificationData", "command", serviceVerify);

            serviceVerify.setTxtVerifiedOn(CommonFunctions.getFormattedOutputDate1(new Date()));

            mav.addObject("deptlist", deptlist);
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "serviceVerifyEditController")
    public ModelAndView serviceVerifyEditEntry(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;

        try {
            if (serviceVerify.getRadpostingauthtype() == null || serviceVerify.getRadpostingauthtype().equals("")) {
                serviceVerify.setRadpostingauthtype("GOO");
            }

            serviceVerify = serviceVerificationDAO.getServiceVerificationData(serviceVerify.getTxtsvid(), serviceVerify.getTxtempid());

            List deptlist = deptDAO.getDepartmentList();

            List otherOrgOfflist = offDAO.getOtherOrganisationList();

            List offlist = offDAO.getTotalOfficeList(serviceVerify.getHidAuthDept());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(serviceVerify.getHidAuthOffice());

            mav = new ModelAndView("/serviceVerification/ServiceVerificationData", "command", serviceVerify);

            mav.addObject("deptlist", deptlist);
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("requestedSBLang", sbCorReqDao.getRequestedServiceBookLanguage(selectedEmpObj.getEmpId(), "SERVICE VERIFICATION CERTIFICATE", serviceVerify.getTxtsvid()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addServiceVerificationDataController", params = "action=Save")
    public ModelAndView addServiceVerificationData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;
        try {
            boolean chkDuplicate = serviceVerificationDAO.chkDuplicate(serviceVerify.getTxtfdate(), serviceVerify.getTxttdate(), serviceVerify.getTxtsvid(), serviceVerify.getTxtempid());
            if (chkDuplicate == false) {
                SubstantivePost substantivePost = substantivePostDAO.getSpcDetail(serviceVerify.getSltSpc());
                String authSpn = substantivePost.getSpn();
                if (serviceVerify.getTxtsvid() != null && !serviceVerify.getTxtsvid().equals("")) {
                    serviceVerificationDAO.modifySvData(serviceVerify, authSpn);
                    //sbCorReqDao.updateRequestedServiceBookFlag(serviceVerify.getTxtempid(), "SERVICE VERIFICATION CERTIFICATE", serviceVerify.getTxtsvid(), "");
                } else {
                    serviceVerificationDAO.addSvData(serviceVerify, authSpn);
                }
                mav = new ModelAndView("redirect:/serviceVerifyList.htm", "command", serviceVerify);
                mav.addObject("duplicate", "N");
            } else {
                mav = new ModelAndView("/serviceVerification/ServiceVerificationData", "command", serviceVerify);

                List deptlist = deptDAO.getDepartmentList();
                List otherOrgOfflist = offDAO.getOtherOrganisationList();
                mav.addObject("deptlist", deptlist);
                mav.addObject("otherOrgOfflist", otherOrgOfflist);
                mav.addObject("duplicate", "Y");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addServiceVerificationDataController", params = "action=Delete")
    public ModelAndView deleteServiceVerificationData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;
        try {

            if (serviceVerify.getTxtsvid() != null && !serviceVerify.getTxtsvid().equals("")) {
                serviceVerificationDAO.removeSvData(serviceVerify.getTxtsvid());
            }
            mav = new ModelAndView("redirect:/serviceVerifyList.htm", "command", serviceVerify);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addServiceVerificationDataController", params = "action=Back")
    public ModelAndView return2List(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") ServiceVerification serviceVerify) {

        ModelAndView mav = null;
        try {

            mav = new ModelAndView("redirect:/serviceVerifyList.htm", "command", serviceVerify);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "EditServiceVerifySBCorrection")
    public ModelAndView EditServiceVerifySBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ServiceVerification serviceVerify, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        try {
            if (serviceVerify.getRadpostingauthtype() == null || serviceVerify.getRadpostingauthtype().equals("")) {
                serviceVerify.setRadpostingauthtype("GOO");
            }
            String mode = requestParams.get("mode");
            String entryType = serviceVerify.getEntrytypeSBCorrection();
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if(correctionid > 0){
                serviceVerify = serviceVerificationDAO.getServiceVerificationDataSBCorrectionDDO(serviceVerify.getTxtsvid(), lub.getLoginempid(),correctionid);
            }else{
                serviceVerify = serviceVerificationDAO.getServiceVerificationData(serviceVerify.getTxtsvid(), lub.getLoginempid());
            }
            serviceVerify.setEntrytypeSBCorrection(entryType);
            
            List deptlist = deptDAO.getDepartmentList();

            List otherOrgOfflist = offDAO.getOtherOrganisationList();

            List offlist = offDAO.getTotalOfficeList(serviceVerify.getHidAuthDept());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(serviceVerify.getHidAuthOffice());

            mav = new ModelAndView("/serviceVerification/ServiceVerificationDataSBCorrection", "command", serviceVerify);

            mav.addObject("deptlist", deptlist);
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addServiceVerificationDataSBCorrection", params = "action=Save")
    public String addServiceVerificationDataSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ServiceVerification serviceVerify) {

        try {
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(lub.getLoginempid());
            sbCorReqBean.setHidNotId(serviceVerify.getTxtsvid());
            sbCorReqBean.setHidNotType("SERVICE VERIFICATION CERTIFICATE");
            sbCorReqBean.setEntrytype(serviceVerify.getEntrytypeSBCorrection());
            sbCorReqBean.setModuleid(serviceVerify.getTxtsvid());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(),sbCorReqBean.getHidNotType(),sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);
            
            SubstantivePost substantivePost = substantivePostDAO.getSpcDetail(serviceVerify.getSltSpc());
            String authSpn = substantivePost.getSpn();
            serviceVerify.setTxtempid(lub.getLoginempid());
            serviceVerify.setCorrectionid(sbcorrectionid+"");
            String sblang = serviceVerificationDAO.addSvDataSBCorrection(serviceVerify, authSpn);
            
            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sblang);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=SERVICE VERIFICATION CERTIFICATE";
    }

    @RequestMapping(value = "addServiceVerificationDataSBCorrection", params = "action=Submit")
    public String submitServiceVerificationDataSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ServiceVerification serviceVerify) {
        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(), Integer.parseInt(serviceVerify.getTxtsvid()), "SERVICE VERIFICATION CERTIFICATE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=SERVICE VERIFICATION CERTIFICATE";
    }

    @RequestMapping(value = "EditServiceVerifySBCorrectionDDO")
    public ModelAndView EditServiceVerifySBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ServiceVerification serviceVerify, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        try {
            String mode = requestParams.get("mode");
            String empid = requestParams.get("empid");
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if (serviceVerify.getRadpostingauthtype() == null || serviceVerify.getRadpostingauthtype().equals("")) {
                serviceVerify.setRadpostingauthtype("GOO");
            }
            String entrytype = serviceVerify.getEntrytypeSBCorrection();
            serviceVerify = serviceVerificationDAO.getServiceVerificationDataSBCorrectionDDO(serviceVerify.getTxtsvid(), empid,correctionid);
            serviceVerify.setEntrytypeSBCorrection(entrytype);
            serviceVerify.setTxtempid(empid);
            
            List deptlist = deptDAO.getDepartmentList();

            List otherOrgOfflist = offDAO.getOtherOrganisationList();

            List offlist = offDAO.getTotalOfficeList(serviceVerify.getHidAuthDept());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(serviceVerify.getHidAuthOffice());

            mav = new ModelAndView("/serviceVerification/ServiceVerificationDataSBCorrection", "command", serviceVerify);

            mav.addObject("deptlist", deptlist);
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addServiceVerificationDataSBCorrection", params = {"action=Approve"})
    public String approveIncrementSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") ServiceVerification serviceVerify) {

        try {
            SubstantivePost substantivePost = substantivePostDAO.getSpcDetail(serviceVerify.getSltSpc());
            String authSpn = substantivePost.getSpn();
            serviceVerificationDAO.approveServiceVerificationSBCorrection(serviceVerify,authSpn,lub.getLogindeptcode(),lub.getLoginoffcode(),lub.getLoginspc(),lub.getLoginuserid());

            sbCorReqDao.updateRequestedServiceBookFlag(serviceVerify.getTxtempid(), "SERVICE VERIFICATION CERTIFICATE", serviceVerify.getTxtsvid() + "",serviceVerify.getCorrectionid(),lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid="+serviceVerify.getTxtempid();
    }
}

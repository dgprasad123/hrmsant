package hrms.controller.promotion;

import hrms.common.CommonFunctions;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.promotion.PromotionDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.promotion.PromotionForm;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PromotionController {

    @Autowired
    public PromotionDAO promotionDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public CadreDAO cadreDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PostDAO postDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @RequestMapping(value = "PromotionList")
    public ModelAndView PromotionList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("promotionForm") PromotionForm promotionForm) {

        List promotionlist = null;

        ModelAndView mav = null;
        try {

            //String empid = CommonFunctions.decodedTxt(u.getEmpId());
            promotionlist = promotionDAO.getPromotionList(u.getEmpId());

            mav = new ModelAndView("/promotion/PromotionList", "promotionForm", promotionForm);
            mav.addObject("promotionlist", promotionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newPromotion")
    public ModelAndView NewPromotion(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("promotionForm") PromotionForm promotionForm) throws IOException {

        ModelAndView mav = null;
        String employeeStatus = null;

        try {
            promotionForm.setEmpid(u.getEmpId());
            promotionForm.setSltPostGroup(promotionDAO.getEmployeePostGroup(u.getEmpId()));
            mav = new ModelAndView("/promotion/NewPromotion", "promotionForm", promotionForm);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List lvlList = cadreDAO.getCadreLevelList();
            List cadreGradeList = cadreDAO.getGradeListDetails(promotionForm.getSltCadre());
            List deptWiseCadreList = cadreDAO.getCadreList(promotionForm.getSltCadreDept());
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("allotDescList", allotDescList);
            mav.addObject("lvlList", lvlList);
            mav.addObject("gradeList", cadreGradeList);
            mav.addObject("cadreList", deptWiseCadreList);
            mav.addObject("getGradeName", promotionForm.getSltGrade());

            String payComm = u.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            promotionForm.setRdoPaycomm(payComm);
            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(u.getEmpId());
            promotionForm.setHidStatus(employeeStatus);

            if (employeeStatus.equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
                mav.addObject("paylevelListBacklog", incrementsancDAO.getPayMatrixLevelAllForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }

            //mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            //mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            promotionForm.setRadnotifyingauthtype("GOO");

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePromotion", params = {"btnPromotion=Save Promotion"})
    public ModelAndView savePromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm promotionForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List promotionlist = null;
        try {

            nb.setNottype("PROMOTION");
            nb.setEmpId(promotionForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(promotionForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(promotionForm.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(promotionForm.getRadnotifyingauthtype());
            nb.setSancDeptCode(promotionForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(promotionForm.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(promotionForm.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(promotionForm.getNotifyingSpc());
            }
            nb.setNote(promotionForm.getNote());
            nb.setLoginuserid(lub.getLoginuserid());

            nb.setEntryType(promotionForm.getRdTransaction());

            if (promotionForm.getHnotid() > 0) {
                nb.setNotid(promotionForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                if (promotionForm.getPromotionId() != null && !promotionForm.getPromotionId().equals("")) {
                    promotionDAO.updatePromotion(promotionForm, lub.getLoginempid(), lub.getLoginuserid());
                } else {
                    promotionDAO.savePromotion(promotionForm, promotionForm.getHnotid(), lub.getLoginempid(), lub.getLoginuserid());
                }
            } else {
                int notid = notificationDao.insertNotificationData(nb);

                promotionDAO.savePromotion(promotionForm, notid, lub.getLoginempid(), lub.getLoginuserid());
            }

            promotionlist = promotionDAO.getPromotionList(promotionForm.getEmpid());
            mav = new ModelAndView("/promotion/PromotionList", "promotionForm", promotionForm);
            mav.addObject("promotionlist", promotionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editPromotion")
    public ModelAndView editPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm prform, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        //PromotionForm prform = new PromotionForm();
        ModelAndView mav = null;
        String employeeStatus = null;

        try {
            String promotionId = requestParams.get("promotionId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            prform.setEmpid(u.getEmpId());
            prform = promotionDAO.getEmpPromotionData(prform, notificationId);

            prform.setPromotionId(promotionId);
            prform.setHnotid(notificationId);
            prform.setHidEntryType(prform.getEntryType());

            mav = new ModelAndView("/promotion/NewPromotion", "promotionForm", prform);

            List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officeList = offDAO.getTotalOfficeListForBacklogEntry(prform.getHidPostedDeptCode(), prform.getHidPostedDistrict());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(prform.getHidNotifyingOffCode());
            //System.out.println("hidoffcode:"+prform.getHidPostedGPC());

            List postingGpcList = subStantivePostDao.getGenericPostList(prform.getHidPostedOffCode());
            List postingSpcList = subStantivePostDao.getSubstantivePostListBacklogEntry(prform.getHidPostedOffCode(), prform.getHidPostedGPC());
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());

            List payscalelist = payscaleDAO.getPayScaleList();

            List lvlList = cadreDAO.getCadreLevelList();
            List cadreGradeList = cadreDAO.getGradeListDetails(prform.getSltCadre());
            List deptWiseCadreList = cadreDAO.getCadreList(prform.getSltCadreDept());

            //System.out.println("prform.getSltCadre():"+prform.getSltGrade());
            prform.setHidGradeCode(prform.getSltGrade());

            mav.addObject("allotDescList", allotDescList);
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("notifyingGpclist", notifyingGpclist);
            mav.addObject("postingGpcList", postingGpcList);
            mav.addObject("postingSpcList", postingSpcList);
            mav.addObject("officeList", officeList);
            mav.addObject("gradeList", cadreGradeList);
            mav.addObject("cadreList", deptWiseCadreList);
            mav.addObject("sltGrade", prform.getSltGrade());
            //mav.addObject("entrytype",prform.getEntryType());


            /*String payComm = u.getPayCommission();
             if (payComm == null || payComm.equals("")) {
             payComm = "6";
             }
             prform.setRdoPaycomm(payComm);*/
            //mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            //mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(u.getEmpId());
            prform.setHidStatus(employeeStatus);
            if (prform.getPayLevel() != null && prform.getPayLevel().contains("J-")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelAllForLAW());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForLAWLevel(prform.getPayLevel()));

            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForGPLevel(prform.getPayLevel()));
            }

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeletePromotion")
    public ModelAndView deletePromotion(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        PromotionForm prform = new PromotionForm();
        List promotionlist = null;
        ModelAndView mav = null;

        try {
            prform.setEmpid(u.getEmpId());

            String promotionId = requestParams.get("promotion_id");
            int notificationId = Integer.parseInt(requestParams.get("not_id"));
            prform = promotionDAO.getEmpPromotionData(prform, notificationId);
            prform.setPromotionId(promotionId);
            prform.setHnotid(notificationId);
            prform.setHnotType("PROMOTION");
            int retVal = notificationDao.deleteNotificationData(notificationId, "PROMOTION");
            if (retVal > 0) {
                promotionDAO.deletePromotion(prform);
            }
            promotionlist = promotionDAO.getPromotionList(prform.getEmpid());
            mav = new ModelAndView("/promotion/PromotionList", "promotionForm", prform);
            mav.addObject("promotionlist", promotionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewPromotion")
    public ModelAndView viewPromotion(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {

        PromotionForm prform = new PromotionForm();

        ModelAndView mav = new ModelAndView();

        try {
            String promotionId = requestParams.get("promotionId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            prform.setEmpid(u.getEmpId());
            prform = promotionDAO.getEmpPromotionData(prform, notificationId);

            prform.setPromotionId(promotionId);
            prform.setHnotid(notificationId);

            mav.addObject("promotionForm", prform);

            String authdeptname = deptDAO.getDeptName(prform.getHidNotifyingDeptCode());
            mav.addObject("notideptname", authdeptname);

            Office office = offDAO.getOfficeDetails(prform.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());

            String cadredeptname = deptDAO.getDeptName(prform.getSltCadreDept());
            mav.addObject("cadredeptname", cadredeptname);

            String cadrename = cadreDAO.getCadreName(prform.getSltCadre());
            mav.addObject("cadrename", cadrename);

            String postingdeptname = deptDAO.getDeptName(prform.getSltPostingDept());
            mav.addObject("postingdeptname", postingdeptname);

            String genericpost = postDAO.getPostName(prform.getSltGenericPost());
            mav.addObject("genericpost", genericpost);

            String posteddeptname = deptDAO.getDeptName(prform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);

            office = offDAO.getOfficeDetails(prform.getHidPostedOffCode());
            mav.addObject("postedoffice", office.getOffEn());

            office = offDAO.getOfficeDetails(prform.getSltPostedFieldOff());
            mav.addObject("fieldoffice", office.getOffEn());

            mav.setViewName("/promotion/ViewPromotion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getEmpCurrentPayData.htm")
    public void getEmpCurrentPayData(HttpServletResponse response, @RequestParam Map<String, String> requestParams, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("promotionForm") PromotionForm prform) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            prform = promotionDAO.getEmpCurrentData(requestParams.get("empid"));
            JSONObject obj = new JSONObject(prform);

            out = response.getWriter();
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "EditPromotionSBCorrection")
    public ModelAndView EditPromotionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm prform, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        String employeeStatus = null;

        try {
            String promotionId = requestParams.get("promotionId");

            String noteIdEnc = requestParams.get("notId");
            int notificationId = 0;
            if (noteIdEnc != null && !noteIdEnc.equals("")) {
                notificationId = Integer.parseInt(CommonFunctions.decodedTxt(noteIdEnc));
            }

            prform.setEmpid(lub.getLoginempid());
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if(correctionid > 0){
                prform = promotionDAO.editPromotionSBCorrectionDDO(prform, notificationId,correctionid);
            }else{
                prform = promotionDAO.getEmpPromotionData(prform, notificationId);
            }

            String mode = requestParams.get("mode");
            String entrytype = requestParams.get("entrytypeSBCorrection");
            prform.setEntrytypeSBCorrection(entrytype);

            prform.setPromotionId(promotionId);
            prform.setHnotid(notificationId);

            mav = new ModelAndView("/promotion/EditPromotionSBCorrection", "promotionForm", prform);

            List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officeList = offDAO.getTotalOfficeListForBacklogEntry(prform.getHidPostedDeptCode(), prform.getHidPostedDistrict());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(prform.getHidNotifyingOffCode());
            //System.out.println("hidoffcode:"+prform.getHidPostedGPC());

            List postingGpcList = subStantivePostDao.getGenericPostList(prform.getHidPostedOffCode());
            List postingSpcList = subStantivePostDao.getSubstantivePostListBacklogEntry(prform.getHidPostedOffCode(), prform.getHidPostedGPC());
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());

            List payscalelist = payscaleDAO.getPayScaleList();

            List lvlList = cadreDAO.getCadreLevelList();
            List cadreGradeList = cadreDAO.getGradeListDetails(prform.getSltCadre());
            List deptWiseCadreList = cadreDAO.getCadreList(prform.getSltCadreDept());

            //System.out.println("prform.getSltCadre():"+prform.getSltGrade());
            prform.setHidGradeCode(prform.getSltGrade());

            mav.addObject("allotDescList", allotDescList);
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("notifyingGpclist", notifyingGpclist);
            mav.addObject("postingGpcList", postingGpcList);
            mav.addObject("postingSpcList", postingSpcList);
            mav.addObject("officeList", officeList);
            mav.addObject("gradeList", cadreGradeList);
            mav.addObject("cadreList", deptWiseCadreList);
            mav.addObject("sltGrade", prform.getSltGrade());

            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(lub.getLoginempid());
            prform.setHidStatus(employeeStatus);
            if (prform.getPayLevel() != null && prform.getPayLevel().contains("J-")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelAllForLAW());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForLAWLevel(prform.getPayLevel()));

            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForGPLevel(prform.getPayLevel()));
            }

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePromotionSBCorrection", params = {"btnPromotion=Save"})
    public String savePromotionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm promotionForm) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(promotionForm.getEmpid());
            sbCorReqBean.setHidNotId(promotionForm.getHnotid() + "");
            sbCorReqBean.setHidNotType("PROMOTION");
            sbCorReqBean.setEntrytype(promotionForm.getEntrytypeSBCorrection());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(), sbCorReqBean.getHidNotType(), sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);

            nb.setNottype("PROMOTION");
            nb.setEmpId(promotionForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(promotionForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(promotionForm.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(promotionForm.getRadnotifyingauthtype());
            nb.setSancDeptCode(promotionForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(promotionForm.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(promotionForm.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(promotionForm.getNotifyingSpc());
            }
            nb.setNote(promotionForm.getNote());
            nb.setLoginuserid(lub.getLoginuserid());
            nb.setNotid(promotionForm.getHnotid());
            nb.setRefcorrectionid(sbcorrectionid);
            notificationDao.deleteNotificationDataSBCorrection(nb.getNotid(), nb.getNottype());
            notificationDao.insertNotificationDataSBCorrection(nb);

            String sbLanuageRequested = promotionDAO.savePromotionDataSBCorrection(promotionForm, promotionForm.getHnotid(), sbcorrectionid, lub.getLoginempid(), lub.getLoginuserid());

            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sbLanuageRequested);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=PROMOTION";
    }
    
    @RequestMapping(value = "savePromotionSBCorrection", params = {"btnPromotion=Submit"})
    public String submitPromotionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm promotionForm) {

        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(),promotionForm.getHnotid(),"PROMOTION");
        } catch (Exception e) {
            e.printStackTrace();
        }
       return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=PROMOTION";
    }
    
    @RequestMapping(value = "EditPromotionSBCorrectionDDO")
    public ModelAndView EditPromotionSBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm prform, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        String employeeStatus = null;

        try {
            String promotionId = requestParams.get("promotionId");

            int notificationId = Integer.parseInt(requestParams.get("notId"));
            String empid = requestParams.get("empid");
            int correctionid = Integer.parseInt(requestParams.get("correctionid"));
            
            prform = promotionDAO.editPromotionSBCorrectionDDO(prform, notificationId,correctionid);
            prform.setEmpid(empid);

            String mode = requestParams.get("mode");
            String entrytype = requestParams.get("entrytype");
            prform.setEntrytypeSBCorrection(entrytype);

            prform.setPromotionId(promotionId);
            prform.setHnotid(notificationId);

            mav = new ModelAndView("/promotion/EditPromotionSBCorrection", "promotionForm", prform);

            List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officeList = offDAO.getTotalOfficeListForBacklogEntry(prform.getHidPostedDeptCode(), prform.getHidPostedDistrict());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(prform.getHidNotifyingOffCode());
            //System.out.println("hidoffcode:"+prform.getHidPostedGPC());

            List postingGpcList = subStantivePostDao.getGenericPostList(prform.getHidPostedOffCode());
            List postingSpcList = subStantivePostDao.getSubstantivePostListBacklogEntry(prform.getHidPostedOffCode(), prform.getHidPostedGPC());
            //List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());

            List payscalelist = payscaleDAO.getPayScaleList();

            List lvlList = cadreDAO.getCadreLevelList();
            List cadreGradeList = cadreDAO.getGradeListDetails(prform.getSltCadre());
            List deptWiseCadreList = cadreDAO.getCadreList(prform.getSltCadreDept());

            //System.out.println("prform.getSltCadre():"+prform.getSltGrade());
            prform.setHidGradeCode(prform.getSltGrade());

            mav.addObject("allotDescList", allotDescList);
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            //mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("notifyingGpclist", notifyingGpclist);
            mav.addObject("postingGpcList", postingGpcList);
            mav.addObject("postingSpcList", postingSpcList);
            mav.addObject("officeList", officeList);
            mav.addObject("gradeList", cadreGradeList);
            mav.addObject("cadreList", deptWiseCadreList);
            mav.addObject("sltGrade", prform.getSltGrade());

            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(empid);
            prform.setHidStatus(employeeStatus);
            if (prform.getPayLevel() != null && prform.getPayLevel().contains("J-")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelAllForLAW());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForLAWLevel(prform.getPayLevel()));

            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForGPLevel(prform.getPayLevel()));
            }

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "savePromotionSBCorrection", params = {"btnPromotion=Approve"})
    public String approvePromotionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("promotionForm") PromotionForm promotionForm) {

        try {
            promotionDAO.approvePromotionDataSBCorrection(promotionForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid());

            sbCorReqDao.updateRequestedServiceBookFlag(promotionForm.getEmpid(), "PROMOTION", promotionForm.getHnotid() + "", promotionForm.getCorrectionid(),lub.getLoginempid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid="+promotionForm.getEmpid();
    }
}

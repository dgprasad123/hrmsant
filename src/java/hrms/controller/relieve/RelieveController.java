package hrms.controller.relieve;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.relieve.RelieveDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.relieve.RelieveForm;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@SessionAttributes("LoginUserBean")
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RelieveController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public RelieveDAO reliveDao;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @RequestMapping(value = "RelieveList")
    public ModelAndView RelieveListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {

        List relievelist = null;

        ModelAndView mav = null;
        try {

            rlvForm.setEmpId(lub.getEmpId());
            relievelist = reliveDao.getRelieveList(rlvForm.getEmpId());

            mav = new ModelAndView("/relieve/RelieveList", "rlvForm", rlvForm);
            mav.addObject("relievelist", relievelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "entryRelieve")
    public ModelAndView entryRelieve(@ModelAttribute("SelectedEmpObj") Users lub, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        RelieveForm rlvForm = null;
        List relievedPostlist = null;
        try {

            String empid = lub.getEmpId();

            int notid = Integer.parseInt(requestParams.get("notId"));
            String rlvid = requestParams.get("rlvId");
            String errMsg = requestParams.get("errMsg");

            rlvForm = reliveDao.getRelieveData(empid, notid, rlvid);
            if (rlvid != null && !rlvid.equals("")) {
                relievedPostlist = new ArrayList();
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            } else {
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            }
            List addlChargeList = reliveDao.getAddlChargeList(empid);

            mav = new ModelAndView("/relieve/AddRelieve", "rlvForm", rlvForm);
            mav.addObject("relievedPostlist", relievedPostlist);
            mav.addObject("addlChargeList", addlChargeList);
            mav.addObject("errMsg", errMsg);

            //reliveDao.getTransactionStatus(rlvForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            List authOfficeList = offDAO.getTotalOfficeList(rlvForm.getAuthHidDeptCode(), rlvForm.getAuthHidDistCode());
            mav.addObject("authOfficeList", authOfficeList);
            //List authGPCList = substantivePostDAO.getAuthorityGenericPostList(rlvForm.getAuthHidOffCode());
            List authGPCList = substantivePostDAO.getGenericPostList(rlvForm.getAuthHidOffCode());
            mav.addObject("authGPCList", authGPCList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "relieveFromPostListJSON")
    public void relieveFromPostListJSON(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List postlist = null;
        try {
            postlist = reliveDao.getRelievedPostList(lub.getEmpId(), "", "", "");

            JSONArray json = new JSONArray(postlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "relieveAddlChargeListJSON")
    public void relieveAddlChargeListJSON(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List addlChrgelist = null;
        try {
            addlChrgelist = reliveDao.getAddlChargeList(lub.getEmpId());

            JSONArray json = new JSONArray(addlChrgelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveRelieve", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backRelieveList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {
        ModelAndView mav = new ModelAndView();
        if (rlvForm.getHidNotType() != null && !rlvForm.getHidNotType().equals("")) {
            if (rlvForm.getHidNotType().equalsIgnoreCase("TRANSFER")) {
                mav = new ModelAndView("redirect:/TransferList.htm");
            } else if (rlvForm.getHidNotType().equalsIgnoreCase("PROMOTION")) {
                mav = new ModelAndView("redirect:/PromotionList.htm");
            } else if (rlvForm.getHidNotType().equalsIgnoreCase("DEPUTATION")) {
                mav = new ModelAndView("redirect:/DeputationList.htm");
            } else {
                mav = new ModelAndView("redirect:/RelieveList.htm");
            }
        } else {
            mav = new ModelAndView("redirect:/RelieveList.htm");
        }
        return mav;
    }

    @RequestMapping(value = "saveRelieve", method = {RequestMethod.POST}, params = {"action=Save Relieve"})
    public ModelAndView saveRelieve(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {

        ModelAndView mav = null;
        List relievelist = null;
        try {
            if (rlvForm.getTransactionStatus() != null && rlvForm.getTransactionStatus().equals("C")) {
                if (rlvForm.getHidNotType() != null && (rlvForm.getHidNotType().equals("TRANSFER") || rlvForm.getHidNotType().equals("PROMOTION"))) {
                    boolean updatestatus = reliveDao.getUpdateStatus(rlvForm.getEmpId(), rlvForm.getHidNotId(), rlvForm.getHidNotType());
                    if (updatestatus == false) {
                        String errMsg = "";
                        if (rlvForm.getHidNotType().equals("TRANSFER")) {
                            errMsg = "Please enter Details of Posting data in Transfer Module";
                        } else if (rlvForm.getHidNotType().equals("PROMOTION")) {
                            errMsg = "Please enter Details of Posting data in Promotion Module";
                        }
                        mav = new ModelAndView("redirect:/entryRelieve.htm?notId=" + rlvForm.getHidNotId() + "&rlvId=" + rlvForm.getRlvId() + "&errMsg=" + errMsg);
                    } else {
                        reliveDao.saveRelieve(rlvForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid(), lub.getLoginuserid());
                    }
                } else {
                    reliveDao.saveRelieve(rlvForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid(), lub.getLoginuserid());
                }
            } else {
                reliveDao.saveRelieve(rlvForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid(), lub.getLoginuserid());
            }
            if (rlvForm.getHidNotType() != null && !rlvForm.getHidNotType().equals("")) {
                if (rlvForm.getHidNotType().equalsIgnoreCase("TRANSFER")) {
                    mav = new ModelAndView("redirect:/TransferList.htm");
                } else if (rlvForm.getHidNotType().equalsIgnoreCase("PROMOTION")) {
                    mav = new ModelAndView("redirect:/PromotionList.htm");
                } else if (rlvForm.getHidNotType().equalsIgnoreCase("DEPUTATION")) {
                    mav = new ModelAndView("redirect:/DeputationList.htm");
                } else {
                    mav = new ModelAndView("redirect:/RelieveList.htm");
                }
            } else {
                mav = new ModelAndView("redirect:/RelieveList.htm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteRelieveInActive")
    public void deleteRelieve(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("relieveForm") RelieveForm er) {
        try {
            reliveDao.deleteRelieve(lub.getEmpId(), er);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "EditRelieveSBCorrection")
    public ModelAndView EditRelieveSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        RelieveForm rlvForm = null;
        List relievedPostlist = null;
        try {
            String empid = lub.getLoginempid();

            String mode = requestParams.get("mode");

            String noteIdEnc = requestParams.get("notId");
            int notid = 0;
            if (noteIdEnc != null && !noteIdEnc.equals("")) {
                notid = Integer.parseInt(CommonFunctions.decodedTxt(noteIdEnc));
            }

            String rlvid = requestParams.get("rlvId");
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if(correctionid > 0){
                rlvForm = reliveDao.getRelieveDataSBCorrectionDDO(empid, notid, rlvid,correctionid);
            }else{
                rlvForm = reliveDao.getRelieveData(empid, notid, rlvid);
            }

            String entrytype = requestParams.get("entrytypeSBCorrection");
            rlvForm.setEntrytypeSBCorrection(entrytype);

            if (rlvid != null && !rlvid.equals("")) {
                relievedPostlist = new ArrayList();
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            } else {
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            }
            List addlChargeList = reliveDao.getAddlChargeList(empid);

            mav = new ModelAndView("/relieve/EditRelieveSBCorrection", "rlvForm", rlvForm);
            mav.addObject("relievedPostlist", relievedPostlist);
            mav.addObject("addlChargeList", addlChargeList);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            List authOfficeList = offDAO.getTotalOfficeList(rlvForm.getAuthHidDeptCode(), rlvForm.getAuthHidDistCode());
            mav.addObject("authOfficeList", authOfficeList);
            //List authGPCList = substantivePostDAO.getAuthorityGenericPostList(rlvForm.getAuthHidOffCode());
            List authGPCList = substantivePostDAO.getGenericPostList(rlvForm.getAuthHidOffCode());
            mav.addObject("authGPCList", authGPCList);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRelieveSBCorrection", params = {"action=Save Relieve"})
    public String saveRelieveSBCorrection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {

        try {
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(rlvForm.getEmpId());
            sbCorReqBean.setHidNotId(rlvForm.getHidNotId() + "");
            sbCorReqBean.setHidNotType("RELIEVE");
            sbCorReqBean.setEntrytype(rlvForm.getEntrytypeSBCorrection());
            sbCorReqBean.setModuleid(rlvForm.getRlvId());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(), sbCorReqBean.getHidNotType(), sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);

            String sbLanuageRequested = reliveDao.saveRelieveSBCorrection(rlvForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid(), lub.getLoginuserid(), sbcorrectionid);
            
            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sbLanuageRequested);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=RELIEVE";
    }
    
    @RequestMapping(value = "saveRelieveSBCorrection", params = {"action=Submit"})
    public String submitRelieveSBCorrection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {

        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(),rlvForm.getHidNotId(),"RELIEVE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=RELIEVE";
    }

    @RequestMapping(value = "EditRelieveSBCorrectionDDO")
    public ModelAndView EditRelieveSBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub,@RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        RelieveForm rlvForm = null;
        List relievedPostlist = null;
        try {
            String empid = requestParams.get("empid");

            String mode = requestParams.get("mode");

            int notid = Integer.parseInt(requestParams.get("notId"));

            String rlvid = requestParams.get("rlvId");
            
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            rlvForm = reliveDao.getRelieveDataSBCorrectionDDO(empid, notid, rlvid,correctionid);

            String entrytype = requestParams.get("entrytypeSBCorrection");
            rlvForm.setEntrytypeSBCorrection(entrytype);

            if (rlvid != null && !rlvid.equals("")) {
                relievedPostlist = new ArrayList();
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            } else {
                relievedPostlist = reliveDao.getRelievedPostList(empid, rlvid, rlvForm.getTransactionStatus(), rlvForm.getHidRlvSpc());
            }
            List addlChargeList = reliveDao.getAddlChargeList(empid);

            mav = new ModelAndView("/relieve/EditRelieveSBCorrection", "rlvForm", rlvForm);
            mav.addObject("relievedPostlist", relievedPostlist);
            mav.addObject("addlChargeList", addlChargeList);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            List authOfficeList = offDAO.getTotalOfficeList(rlvForm.getAuthHidDeptCode(), rlvForm.getAuthHidDistCode());
            mav.addObject("authOfficeList", authOfficeList);
            List authGPCList = substantivePostDAO.getGenericPostList(rlvForm.getAuthHidOffCode());
            mav.addObject("authGPCList", authGPCList);
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRelieveSBCorrection", params = {"action=Approve"})
    public String saveRelieveSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rlvForm") RelieveForm rlvForm) {

        try {
            reliveDao.approveRelieveDataSBCorrection(rlvForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid(), lub.getLoginusername());

            sbCorReqDao.updateRequestedServiceBookFlag(rlvForm.getEmpId(), "RELIEVE", rlvForm.getHidNotId() + "", rlvForm.getCorrectionid(),lub.getLoginempid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid="+rlvForm.getEmpId();
    }
}

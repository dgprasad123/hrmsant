package hrms.controller.payfixation;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.payfixation.PayFixationDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.payfixation.PayFixation;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PayFixationController {

    @Autowired
    public PayFixationDAO payFixationDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public AqFunctionalities aqfunctionalities;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @RequestMapping(value = "PayFixationList")
    public ModelAndView PromotionList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("payFixation") PayFixation payFixation) {

        ModelAndView mav = new ModelAndView();

        List payfixationList = null;
        try {

            payfixationList = payFixationDAO.findAllPayFixation(u.getEmpId(), payFixation.getNotType());

            mav = new ModelAndView("/payfixation/PayFixationList", "payFixation", payFixation);
            mav.addObject("payfixationList", payfixationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newPayFixation")
    public ModelAndView NewPayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation) throws IOException {

        ModelAndView mav = null;

        try {

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            payFixation.setRdoPaycomm(payComm);

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();

            payFixation.setNotType(payFixation.getNotType());
            payFixation.setEmpid(selectedEmpObj.getEmpId());
            payFixation.setRadnotifyingauthtype("GOO");

            mav = new ModelAndView("/payfixation/NewPayFixation", "payFixation", payFixation);

            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            //mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(selectedEmpObj.getEmpId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePayFixation", params = {"btnPayFixation=Save"})
    public ModelAndView savePayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, BindingResult result) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List payfixationList = null;
        try {

            nb.setNottype(payFixation.getNotType());
            nb.setEmpId(payFixation.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payFixation.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(payFixation.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(payFixation.getRadnotifyingauthtype());
            nb.setSancDeptCode(payFixation.getHidNotifyingDeptCode());
            nb.setSancOffCode(payFixation.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(payFixation.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(payFixation.getNotifyingSpc());
            }
            nb.setNote(payFixation.getNote());

            if (payFixation.getNotId() > 0) {
                nb.setNotid(payFixation.getNotId());
                notificationDao.modifyNotificationData(nb);
                payFixationDAO.updatePayFixationData(payFixation);
            } else {
                int notid = notificationDao.insertNotificationData(nb);

                payFixationDAO.insertPayFixationData(payFixation, notid);
            }

            payfixationList = payFixationDAO.findAllPayFixation(selectedEmpObj.getEmpId(), payFixation.getNotType());
            mav = new ModelAndView("/payfixation/PayFixationList", "payFixation", payFixation);
            mav.addObject("payfixationList", payfixationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePayFixation", params = {"btnPayFixation=Delete"})
    public ModelAndView deletePayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation) throws ParseException {

        ModelAndView mav = null;

        List payfixationList = new ArrayList();
        try {
            payFixationDAO.deletePayFixation(payFixation);

            payfixationList = payFixationDAO.findAllPayFixation(payFixation.getEmpid(), payFixation.getNotType());

            mav = new ModelAndView();
            mav.addObject("payfixationList", payfixationList);
            mav.setViewName("/payfixation/PayFixationList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editPayFixation")
    public ModelAndView editPayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            payFixform.setEmpid(selectedEmpObj.getEmpId());
            payFixform = payFixationDAO.editPayFixation(selectedEmpObj.getEmpId(), notificationId);

            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }

            mav = new ModelAndView("/payfixation/NewPayFixation", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(selectedEmpObj.getEmpId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewPayFixation")
    public ModelAndView viewPayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = new ModelAndView();

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            payFixform.setEmpid(selectedEmpObj.getEmpId());
            payFixform = payFixationDAO.editPayFixation(selectedEmpObj.getEmpId(), notificationId);

            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }

            mav.addObject("payFixform", payFixform);

            String notideptname = deptDAO.getDeptName(payFixform.getHidNotifyingDeptCode());
            mav.addObject("notideptname", notideptname);

            Office office = offDAO.getOfficeDetails(payFixform.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());

            mav.setViewName("/payfixation/ViewPayFixation");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteRetrospectivePayFixation")
    public ModelAndView deleteRetrospectivePayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws ParseException {

        ModelAndView mav = null;

        try {
            String nottype = requestParams.get("nottype");

            int notId = Integer.parseInt(requestParams.get("notId"));
            payFixationDAO.deleteRetrospectivePayFixationRow(notId);

            mav = new ModelAndView("redirect:/PayFixationList.htm?notType=" + nottype);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "CancelPayFixation")
    public ModelAndView CancelPayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            if (payFixation.getCancelnotId() != null && !payFixation.getCancelnotId().equals("")) {
                payFixform = payFixationDAO.editCancelPayFixation(selectedEmpObj.getEmpId(), Integer.parseInt(payFixation.getCancelnotId()));
            } else {
                payFixform.setLinkid(notificationId + "");
            }

            payFixform.setEmpid(selectedEmpObj.getEmpId());
            payFixform.setRadnotifyingauthtype("GOO");
            payFixform.setNotType(payFixation.getNotType());

            mav = new ModelAndView("/payfixation/CancelPayFixation", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());

            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveCancelPayFixation")
    public ModelAndView SaveCancelPayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, BindingResult result) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List payfixationList = null;
        try {
            nb.setNottype("CANCELLATION");
            nb.setEmpId(payFixation.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payFixation.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(payFixation.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(payFixation.getRadnotifyingauthtype());
            nb.setSancDeptCode(payFixation.getHidNotifyingDeptCode());
            nb.setSancOffCode(payFixation.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(payFixation.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(payFixation.getNotifyingSpc());
            }
            nb.setNote(payFixation.getNote());

            if (payFixation.getNotId() > 0) {
                nb.setNotid(payFixation.getNotId());
                notificationDao.modifyNotificationData(nb);
                payFixationDAO.insertCancelPayFixationData(nb, nb.getNotid(), payFixation.getHidNotifyingOthSpc());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                payFixationDAO.insertCancelPayFixationData(nb, notid, payFixation.getHidNotifyingOthSpc());
                notificationDao.updateCancellationNotificationData(Integer.parseInt(payFixation.getLinkid()), notid);
            }
            payfixationList = payFixationDAO.findAllPayFixation(selectedEmpObj.getEmpId(), payFixation.getNotType());
            mav = new ModelAndView("/payfixation/PayFixationList", "payFixation", payFixation);
            mav.addObject("payfixationList", payfixationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SupersedePayFixation")
    public ModelAndView SupersedePayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            if (payFixation.getSupersedeid() != null && !payFixation.getSupersedeid().equals("")) {
                payFixform = payFixationDAO.editSupersedePayFixation(selectedEmpObj.getEmpId(), Integer.parseInt(payFixation.getSupersedeid()));
            } else {
                payFixform.setLinkid(notificationId + "");
                payFixform.setNotType(payFixation.getNotType());
                payFixform.setRadnotifyingauthtype("GOO");
            }

            payFixform.setEmpid(selectedEmpObj.getEmpId());

            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }
            mav = new ModelAndView("/payfixation/SupersedePayFixation", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveSupersedePayFixation", params = {"btnPayFixation=Save"})
    public ModelAndView SaveSupersedePayFixation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, BindingResult result) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List payfixationList = null;
        try {

            nb.setNottype(payFixation.getNotType());
            nb.setEmpId(payFixation.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payFixation.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(payFixation.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(payFixation.getRadnotifyingauthtype());
            nb.setSancDeptCode(payFixation.getHidNotifyingDeptCode());
            nb.setSancOffCode(payFixation.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(payFixation.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(payFixation.getNotifyingSpc());
            }
            nb.setNote(payFixation.getNote());

            if (payFixation.getNotId() > 0) {
                nb.setNotid(payFixation.getNotId());
                notificationDao.modifyNotificationData(nb);
                payFixationDAO.updateSupersedePayFixationData(payFixation);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                notificationDao.updateSupersedeNotificationData(Integer.parseInt(payFixation.getLinkid()), notid);
                payFixationDAO.insertSupersedePayFixationData(payFixation, notid);
            }

            payfixationList = payFixationDAO.findAllPayFixation(selectedEmpObj.getEmpId(), payFixation.getNotType());
            mav = new ModelAndView("/payfixation/PayFixationList", "payFixation", payFixation);
            mav.addObject("payfixationList", payfixationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "FinancialBenefitList")
    public ModelAndView FinancialBenefitList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("payFixation") PayFixation payFixation) {

        ModelAndView mav = new ModelAndView();

        List payfixationList = null;
        try {

            payfixationList = payFixationDAO.findAllFinBenefits(u.getEmpId());
            payFixation.setEmpid(u.getEmpId());
            mav = new ModelAndView("/payfixation/FinancialBenefitList", "payFixation", payFixation);
            String emplyeedoegov = payFixationDAO.getEmployeeDateOfEntry(u.getEmpId());

            mav.addObject("payfixationList", payfixationList);
            mav.addObject("emplyeedoegov", emplyeedoegov);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "NewFinancialBenefit")
    public ModelAndView NewFinancialBenefit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation) throws IOException {

        ModelAndView mav = null;

        try {

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            payFixation.setRdoPaycomm(payComm);
            payFixation.setNotType("PAYFIXATION");

            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();

            mav = new ModelAndView("/payfixation/NewFinancialBenefit", "payFixation", payFixation);

            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveFinancialBenefit", params = {"btnPayFixation=Save"})
    public String saveFinancialBenefit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("payFixation") PayFixation payFixation, BindingResult result) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            nb.setNottype(payFixation.getNotType());
            nb.setEmpId(payFixation.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payFixation.getTxtNotOrdNo());
            nb.setRadpostingauthtype(payFixation.getRadnotifyingauthtype());
            nb.setSancDeptCode(payFixation.getHidNotifyingDeptCode());
            nb.setSancOffCode(payFixation.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(payFixation.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(payFixation.getNotifyingSpc());
            }
            nb.setNote(payFixation.getNote());

            if (payFixation.getNotId() > 0) {
                nb.setNotid(payFixation.getNotId());
                notificationDao.modifyNotificationData(nb);
                payFixationDAO.updateFinancialBenefitData(payFixation);
            } else {
                int notid = notificationDao.insertNotificationData(nb);

                payFixationDAO.insertFinancialBenefitData(payFixation, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/NewFinancialBenefit.htm";
    }

    @RequestMapping(value = "editFinancialBenefit")
    public ModelAndView editFinancialBenefit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            payFixform.setEmpid(selectedEmpObj.getEmpId());
            payFixform = payFixationDAO.editPayFixation(selectedEmpObj.getEmpId(), notificationId);

            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }

            mav = new ModelAndView("/payfixation/NewFinancialBenefit", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(selectedEmpObj.getEmpId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "EditPayFixationSBCorrection")
    public ModelAndView EditPayFixationSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            String mode = requestParams.get("mode");
            String noteIdEnc = requestParams.get("notId");
            int notificationId = 0;
            if (noteIdEnc != null && !noteIdEnc.equals("")) {
                notificationId = Integer.parseInt(CommonFunctions.decodedTxt(noteIdEnc));
            }
            String entrytype = requestParams.get("entrytype");
            int correctionid = 0;
            if(requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")){
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if(correctionid > 0){
                payFixform = payFixationDAO.editPayFixationSBCorrectionDDO(lub.getLoginempid(), notificationId,correctionid);
            }else{
                payFixform = payFixationDAO.editPayFixation(lub.getLoginempid(), notificationId);
            }
            payFixform.setEmpid(lub.getLoginempid());

            payFixform.setEntrytype(entrytype);

            if (payFixform.getNotType() == null || payFixform.getNotType().equals("")) {
                payFixform.setNotType(requestParams.get("notType"));
            }

            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }

            mav = new ModelAndView("/payfixation/EditPayFixationSBCorrection", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(lub.getLoginempid()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(lub.getLoginempid()));
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePayFixationSBCorrection", params = {"btnPayFixation=Save"})
    public String savePayFixationSBCorrection(@ModelAttribute("payFixation") PayFixation payFixation) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(payFixation.getEmpid());
            sbCorReqBean.setHidNotId(payFixation.getNotId()+"");
            sbCorReqBean.setHidNotType(payFixation.getNotType());
            sbCorReqBean.setEntrytype(payFixation.getEntrytype());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(),sbCorReqBean.getHidNotType(),sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);

            nb.setNottype(payFixation.getNotType());
            nb.setEmpId(payFixation.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payFixation.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(payFixation.getTxtNotOrdDt()));
            nb.setRadpostingauthtype(payFixation.getRadnotifyingauthtype());
            nb.setSancDeptCode(payFixation.getHidNotifyingDeptCode());
            nb.setSancOffCode(payFixation.getHidNotifyingOffCode());
            if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(payFixation.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(payFixation.getNotifyingSpc());
            }
            nb.setNote(payFixation.getNote());

            nb.setNotid(payFixation.getNotId());
            nb.setRefcorrectionid(sbcorrectionid);
            notificationDao.deleteNotificationDataSBCorrection(nb.getNotid(), nb.getNottype());
            notificationDao.insertNotificationDataSBCorrection(nb);

            String sblang = payFixationDAO.insertPayFixationDataSBCorrection(payFixation, nb.getNotid(), sbcorrectionid);

            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sblang);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=" + payFixation.getNotType();
    }
    
    @RequestMapping(value = "savePayFixationSBCorrection", params = {"btnPayFixation=Submit"})
    public String submitPayFixationSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("payFixation") PayFixation payFixation) {

        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(),payFixation.getNotId(),payFixation.getNotType());
        } catch (Exception e) {
            e.printStackTrace();
        }
       return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=" + payFixation.getNotType();
    }

    @RequestMapping(value = "EditPayFixationSBCorrectionDDO")
    public ModelAndView EditPayFixationSBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {

        PayFixation payFixform = new PayFixation();

        ModelAndView mav = null;

        try {
            String mode = requestParams.get("mode");
            //String noteIdEnc = requestParams.get("notId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            int sbcorrectionid = Integer.parseInt(requestParams.get("correctionid"));
            String empid = requestParams.get("empid");
            
            payFixform = payFixationDAO.editPayFixationSBCorrectionDDO(empid, notificationId, sbcorrectionid);
            payFixform.setNotId(notificationId);
            payFixform.setCorrectionid(sbcorrectionid+"");
            if (payFixform.getPayLevel() != null && !payFixform.getPayLevel().equals("")) {
                payFixform.setRdoPaycomm("7");
            } else {
                payFixform.setRdoPaycomm("6");
            }
            payFixform.setEmpid(empid);
            
            String entrytype = requestParams.get("entrytype");
            payFixform.setEntrytype(entrytype);
            
            mav = new ModelAndView("/payfixation/EditPayFixationSBCorrection", "payFixation", payFixform);

            List deptlist = deptDAO.getDepartmentList();
            List offlist = offDAO.getTotalOfficeList(payFixform.getHidNotifyingDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(payFixform.getHidNotifyingOffCode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
            mav.addObject("payscalelist", payscalelist);
            if (incrementsancDAO.checkEmployeeLawStatus(empid).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(empid));
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePayFixationSBCorrection", params = {"btnPayFixation=Approve"})
    public String approvePayFixationSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("payFixation") PayFixation payFixation) throws ParseException {

        try {
            payFixationDAO.approvePayFixationDataSBCorrection(payFixation, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginuserid());

            sbCorReqDao.updateRequestedServiceBookFlag(payFixation.getEmpid(), payFixation.getNotType(), payFixation.getNotId() + "", payFixation.getCorrectionid(),lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid="+payFixation.getEmpid();
    }
}

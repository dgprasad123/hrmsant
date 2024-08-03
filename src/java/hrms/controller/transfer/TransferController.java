package hrms.controller.transfer;

import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.transfer.TransferDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import hrms.model.notification.NotificationBean;
import hrms.model.transfer.TransferForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@SessionAttributes("LoginUserBean")
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class TransferController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public TransferDAO transferDao;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @RequestMapping(value = "TransferList")
    public ModelAndView TransferList(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("transferForm") TransferForm transferForm, HttpServletRequest request) {
        List transferlist = null;
        ModelAndView mav = null;
        try {
            transferForm.setEmpid(selectedEmpObj.getEmpId());
            transferlist = transferDao.getTransferList(transferForm.getEmpid());
            mav = new ModelAndView("/transfer/TransferList", "transferForm", transferForm);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newTransfer")
    public ModelAndView NewTransfer(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("transferForm") TransferForm transferForm) throws IOException {
        ModelAndView mav = null;
        String employeeStatus = null;
        try {
            mav = new ModelAndView("/transfer/NewTransfer", "transferForm", transferForm);
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("payscalelist", payscalelist);

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            transferForm.setRdoPaycomm(payComm);
            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId());
            transferForm.setHidStatus(employeeStatus);
            if (employeeStatus.equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
                mav.addObject("paylevelListBacklog", incrementsancDAO.getPayMatrixLevelAllForLAW());
                //mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                //mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            }

            //mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            transferForm.setRadsanctioningauthtype("GOO");
            transferForm.setRadpostingauthtype("GOO");

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveTransfer")
    public ModelAndView saveTransfer(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, Model model, @ModelAttribute("transferForm") TransferForm transferform, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List transferlist = null;
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("TRANSFER");
                nb.setEmpId(transferform.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(transferform.getTxtNotOrdNo());
                nb.setOrdDate(sdf.parse(transferform.getTxtNotOrdDt()));
                nb.setRadpostingauthtype(transferform.getRadsanctioningauthtype());
                nb.setRadntfnauthtype(transferform.getRadpostingauthtype());
                nb.setSancDeptCode(transferform.getHidAuthDeptCode());
                nb.setSancOffCode(transferform.getHidAuthOffCode());
                if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                    nb.setSancAuthCode(transferform.getHidSanctioningOthSpc());
                } else {
                    nb.setSancAuthCode(transferform.getAuthSpc());
                }
                nb.setNote(transferform.getNote());
                nb.setLoginuserid(lub.getLoginuserid());

                if (transferform.getHnotid() > 0) {

                    if (transferform.getTransferId() != null && !transferform.getTransferId().equals("")) {
                        nb.setNotid(transferform.getHnotid());
                        transferform.setHnotType(nb.getNottype());
                        notificationDao.modifyNotificationData(nb);
                        transferDao.updateTransfer(transferform, lub.getLoginempid(), lub.getLoginuserid());
                    } else {
                        nb.setNotid(transferform.getHnotid());

                        transferform.setHnotType(nb.getNottype());
                        notificationDao.modifyNotificationData(nb);
                        transferform.setHnotType("TRANSFER");
                        transferDao.saveTransfer(transferform, nb.getNotid(), lub.getLoginempid(), lub.getLoginuserid());
                    }

                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    transferform.setHnotType("TRANSFER");
                    transferDao.saveTransfer(transferform, notid, lub.getLoginempid(), lub.getLoginuserid());
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(transferform.getHnotid(), "TRANSFER");

                if (retVal > 0) {
                    transferDao.deleteTransfer(transferform);
                }
            } else if (submit != null && submit.equals("Back")) {
                //return new ModelAndView("redirect:/TransferList.htm");
            }

            transferlist = transferDao.getTransferList(transferform.getEmpid());
            mav = new ModelAndView("/transfer/TransferList", "transferForm", transferform);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editTransfer")
    public ModelAndView editTransfer(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        TransferForm trform = new TransferForm();
        String employeeStatus = null;
        ModelAndView mav = null;

        try {
            trform.setEmpid(u.getEmpId());

            String transferId = requestParams.get("transferId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            trform = transferDao.getEmpTransferData(trform, notificationId);

            trform.setEmpid(u.getEmpId());
            trform.setTransferId(transferId);
            trform.setHnotid(notificationId);

            mav = new ModelAndView("/transfer/NewTransfer", "transferForm", trform);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List gpcauthlist = substantivePostDAO.getAuthorityGenericPostList(trform.getHidAuthOffCode());
            List gpcpostedList = substantivePostDAO.getGenericPostList(trform.getHidPostedOffCode());
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("gpcauthlist", gpcauthlist);
            mav.addObject("gpcpostedList", gpcpostedList);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            trform.setRdoPaycomm(payComm);

            employeeStatus = incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId());
            trform.setHidStatus(employeeStatus);
            if (trform.getPayLevel().contains("J-")) {
                /*if (employeeStatus.equals("Y")) { 
                 if(trform.getPayLevel().contains("J-")){
                 mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
                 }else{
                 mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelAllForLAW());
                 }*/
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelAllForLAW());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForLAWLevel(trform.getPayLevel()));

            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                mav.addObject("payCellList", incrementsancDAO.getPayMatrixCellForGPLevel(trform.getPayLevel()));
            }

            //mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            //mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteTransferRecord")
    public ModelAndView deleteTransfer(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        TransferForm trform = new TransferForm();
        List transferlist = null;
        ModelAndView mav = null;

        try {
            trform.setEmpid(u.getEmpId());

            String transferId = requestParams.get("transfer_id");
            int notificationId = Integer.parseInt(requestParams.get("not_id"));
            trform = transferDao.getEmpTransferData(trform, notificationId);

            trform.setEmpid(u.getEmpId());
            trform.setTransferId(transferId);
            trform.setHnotid(notificationId);
            trform.setHnotType("TRANSFER");
            int retVal = notificationDao.deleteNotificationData(notificationId, "TRANSFER");

            if (retVal > 0) {
                transferDao.deleteTransfer(trform);
            }
            transferlist = transferDao.getTransferList(trform.getEmpid());
            mav = new ModelAndView("/transfer/TransferList", "transferForm", trform);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewTransfer")
    public ModelAndView viewTransfer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        TransferForm trform = new TransferForm();

        ModelAndView mav = new ModelAndView();

        try {
            trform.setEmpid(u.getEmpId());

            String transferId = requestParams.get("transferId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            trform = transferDao.getEmpTransferData(trform, notificationId);

            trform.setEmpid(u.getEmpId());
            trform.setTransferId(transferId);
            trform.setHnotid(notificationId);

            mav.addObject("transferForm", trform);
            mav.setViewName("/transfer/TransferView");

            String authdeptname = deptDAO.getDeptName(trform.getHidAuthDeptCode());
            mav.addObject("authdeptname", authdeptname);

            Office office = offDAO.getOfficeDetails(trform.getHidAuthOffCode());
            mav.addObject("authoffice", office.getOffEn());

            /*SubstantivePost substantivePost = substantivePostDAO.getSpcDetail(trform.getAuthSpc());
             mav.addObject("authspn", substantivePost.getSpn());*/
            String posteddeptname = deptDAO.getDeptName(trform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);

            office = offDAO.getOfficeDetails(trform.getHidPostedOffCode());
            mav.addObject("postedoffice", office.getOffEn());

            office = offDAO.getOfficeDetails(trform.getSltPostedFieldOff());
            mav.addObject("fieldoffice", office.getOffEn());

            /*substantivePost = substantivePostDAO.getSpcDetail(trform.getPostedspc());
             mav.addObject("postedspn", substantivePost.getSpn());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "TransferPostList")
    public String TransferPostList(ModelMap model, @RequestParam("type") String type, @RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode) {
        try {
            model.addAttribute("type", type);
            model.addAttribute("deptCode", deptCode);
            model.addAttribute("offCode", offCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/transfer/TransferPostList";
    }

    @ResponseBody
    @RequestMapping(value = "TransferPostListJSON")
    public void TransferPostListJSON(HttpServletResponse response, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List postlist = transferDao.getPostList(lub.getLogindeptcode(), lub.getLoginoffcode());

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

    @ResponseBody
    @RequestMapping(value = "transferGetFieldOffListJSON")
    public void GetFieldOffListJSON(HttpServletResponse response, @RequestParam("offcode") String offcode) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List fieldofflist = null;
        try {
            fieldofflist = offDAO.getFieldOffList(offcode);

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
    @RequestMapping(value = "transferGetPayscaleListJSON")
    public void GetPayscaleListJSON(HttpServletResponse response) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List payscalelist = null;
        try {
            payscalelist = payscaleDAO.getPayScaleList();

            JSONArray json = new JSONArray(payscalelist);
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
    @RequestMapping(value = "getTransferCadreWisePostListJSON")
    public String GetTransferCadreWisePostListJSON(@ModelAttribute("SelectedEmpObj") Users u, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            String cadrecode = transferDao.getCadreCode(u.getEmpId());
            List spclist = substantivePostDAO.getCadreWisePostList(offcode, cadrecode);
            json = new JSONArray(spclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "CancelTransfer")
    public ModelAndView CancelTransfer(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("transferForm") TransferForm transferForm, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        TransferForm trForm = new TransferForm();

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            trForm.setEmpid(selectedEmpObj.getEmpId());
            if (transferForm.getCancelnotId() != null && !transferForm.getCancelnotId().equals("")) {
                trForm = transferDao.getCancelTransferData(trForm, Integer.parseInt(transferForm.getCancelnotId()));
            } else {
                trForm.setLinkid(notificationId + "");
                trForm.setRadsanctioningauthtype("GOO");
            }
            mav = new ModelAndView("/transfer/CancelTransfer", "transferForm", trForm);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveCancelTransfer")
    public ModelAndView SaveCancelTransfer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("transferForm") TransferForm transferform, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List transferlist = null;
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("CANCELLATION");
                nb.setEmpId(transferform.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(transferform.getTxtNotOrdNo());
                nb.setOrdDate(sdf.parse(transferform.getTxtNotOrdDt()));
                nb.setRadpostingauthtype(transferform.getRadsanctioningauthtype());
                nb.setRadntfnauthtype(transferform.getRadpostingauthtype());
                nb.setSancDeptCode(transferform.getHidAuthDeptCode());
                nb.setSancOffCode(transferform.getHidAuthOffCode());
                if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                    nb.setSancAuthCode(transferform.getHidSanctioningOthSpc());
                } else {
                    nb.setSancAuthCode(transferform.getAuthSpc());
                }
                nb.setNote(transferform.getNote());
                nb.setLoginuserid(lub.getLoginuserid());

                if (transferform.getHnotid() > 0) {
                    nb.setNotid(transferform.getHnotid());
                    notificationDao.modifyNotificationData(nb);
                    transferDao.saveCancelTransfer(nb, transferform, nb.getNotid());
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    transferDao.saveCancelTransfer(nb, transferform, notid);
                    notificationDao.updateCancellationNotificationData(Integer.parseInt(transferform.getLinkid()), notid);
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(transferform.getHnotid(), "CANCELLATION");

                if (retVal > 0) {
                    transferDao.deleteTransfer(transferform);
                }
            } else if (submit != null && submit.equals("Back")) {
                //return new ModelAndView("redirect:/TransferList.htm");
            }

            transferlist = transferDao.getTransferList(transferform.getEmpid());
            mav = new ModelAndView("/transfer/TransferList", "transferForm", transferform);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SupersedeTransfer")
    public ModelAndView SupersedeTransfer(@ModelAttribute(value = "SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {

        TransferForm trform = new TransferForm();

        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            if (trform.getSupersedeid() != null && !trform.getSupersedeid().equals("")) {
                trform = transferDao.getEmpSupersedeTransferData(trform, Integer.parseInt(trform.getSupersedeid()));
            } else {
                trform.setRadpostingauthtype("GOO");
                trform.setRadsanctioningauthtype("GOO");
                trform.setLinkid(notificationId + "");
            }

            trform.setEmpid(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/transfer/NewTransfer", "transferForm", trform);

            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List gpcauthlist = substantivePostDAO.getAuthorityGenericPostList(trform.getHidAuthOffCode());
            List gpcpostedList = substantivePostDAO.getGenericPostList(trform.getHidPostedOffCode());
            List fieldofflist = offDAO.getFieldOffList(lub.getLoginoffcode());
            List payscalelist = payscaleDAO.getPayScaleList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("gpcauthlist", gpcauthlist);
            mav.addObject("gpcpostedList", gpcpostedList);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("payscalelist", payscalelist);

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            trform.setRdoPaycomm(payComm);

            mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveSupersedeTransfer")
    public ModelAndView SaveSupersedeTransfer(@ModelAttribute("LoginUserBean") LoginUserBean lub, Model model, @ModelAttribute("transferForm") TransferForm transferform, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List transferlist = null;
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("TRANSFER");
                nb.setEmpId(transferform.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(transferform.getTxtNotOrdNo());
                nb.setOrdDate(sdf.parse(transferform.getTxtNotOrdDt()));
                nb.setRadpostingauthtype(transferform.getRadsanctioningauthtype());
                nb.setRadntfnauthtype(transferform.getRadpostingauthtype());
                nb.setSancDeptCode(transferform.getHidAuthDeptCode());
                nb.setSancOffCode(transferform.getHidAuthOffCode());
                if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                    nb.setSancAuthCode(transferform.getHidSanctioningOthSpc());
                } else {
                    nb.setSancAuthCode(transferform.getAuthSpc());
                }
                nb.setNote(transferform.getNote());
                nb.setLoginuserid(lub.getLoginuserid());

                if (transferform.getHnotid() > 0) {

                    if (transferform.getTransferId() != null && !transferform.getTransferId().equals("")) {
                        nb.setNotid(transferform.getHnotid());
                        transferform.setHnotType(nb.getNottype());
                        notificationDao.modifyNotificationData(nb);
                        transferDao.UpdateSupersedeTransfer(transferform, lub.getLoginempid(), lub.getLoginuserid());
                    } else {
                        nb.setNotid(transferform.getHnotid());

                        transferform.setHnotType(nb.getNottype());
                        notificationDao.modifyNotificationData(nb);
                        transferform.setHnotType("TRANSFER");
                        transferDao.SaveSupersedeTransfer(transferform, nb.getNotid(), lub.getLoginempid(), lub.getLoginuserid());
                    }

                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    transferform.setHnotType("TRANSFER");
                    notificationDao.updateSupersedeNotificationData(Integer.parseInt(transferform.getLinkid()), notid);
                    transferDao.SaveSupersedeTransfer(transferform, notid, lub.getLoginempid(), lub.getLoginuserid());
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(transferform.getHnotid(), "TRANSFER");

                if (retVal > 0) {
                    transferDao.deleteTransfer(transferform);
                }
            } else if (submit != null && submit.equals("Back")) {
                //return new ModelAndView("redirect:/TransferList.htm");
            }

            transferlist = transferDao.getTransferList(transferform.getEmpid());
            mav = new ModelAndView("/transfer/TransferList", "transferForm", transferform);
            mav.addObject("transferlist", transferlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

package hrms.controller.deputation;

import hrms.common.Message;
import hrms.dao.deputation.DeputationDAO;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.transfer.TransferDAO;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.transfer.TransferForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class DeputationController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public DeputationDAO deputationDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public TransferDAO transferDao;

    @Autowired
    public LoginDAOImpl loginDao;

    @Autowired
    public StateDAO stateDAOImpl;

    @Autowired
    public PostDAO postDAO;

    @RequestMapping(value = "DeputationList")
    public ModelAndView DeputationList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {

            List depuList = deputationDAO.getDeputationList(lub.getEmpId());

            mav = new ModelAndView("/deputation/Deputation", "deputationForm", dform);
            mav.addObject("DepuList", depuList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newDeputation")
    public ModelAndView NewDeputationPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {
            //List depuList = deputationDAO.getDeputationList(lub.getEmpId());

            dform.setRadpostingauthtype("GOO");
            dform.setRadnotifyingauthtype("GOO");
            mav = new ModelAndView("/deputation/DeputationData", "deputationForm", dform);

            List deptlist = deptDAO.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List cadrestatuslist = deputationDAO.getCadreStatusList("DEP");
            List otherOrgfflist = offDAO.getOtherOrganisationList();
            List sgoOfficeList = offDAO.getSGOOfficeList();            
            List statelist = stateDAOImpl.getStateList();
            List postedOffList = offDAO.getTotalOfficeList(dform.getHidPostedDeptCode());
            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("otherOrgfflist", otherOrgfflist);
            mav.addObject("sgoOfficeList", sgoOfficeList);
            //mav.addObject("DepuList", depuList);
            mav.addObject("cadrestatuslist", cadrestatuslist);
            mav.addObject("statelist", statelist);
            mav.addObject("postedOfflist", postedOffList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveDeputation", params = "action=Save Deputation")
    public ModelAndView saveDeputationPage(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm deputationForm) {

        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        TransferForm transferForm = new TransferForm();
        try {
            deputationForm.setEmpid(lub.getEmpId());

            nb.setNottype("DEPUTATION");
            nb.setEmpId(deputationForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(deputationForm.getTxtNotOrdNo());
            if (deputationForm.getTxtNotOrdDt() != null && !deputationForm.getTxtNotOrdDt().equals("")) {
                nb.setOrdDate(sdf.parse(deputationForm.getTxtNotOrdDt()));
            }
            nb.setSancDeptCode(deputationForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(deputationForm.getHidNotifyingOffCode());
            if (deputationForm.getRadnotifyingauthtype() != null && deputationForm.getRadnotifyingauthtype().equals("GOI")) {
                nb.setSancAuthCode(deputationForm.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(deputationForm.getHidNotiSpc());
            }

            nb.setEntryDeptCode(lub.getDeptcode());
            nb.setEntryOffCode(lub.getOffcode());
            nb.setEntryAuthCode(lub.getCurspc());
            nb.setCadreStatus(deputationForm.getSltCadreStatus());
            nb.setSubCadreStatus(deputationForm.getSltSubCadreStatus());
            nb.setNote(deputationForm.getNote());
            nb.setRadpostingauthtype(deputationForm.getRadpostingauthtype());
            nb.setRadntfnauthtype(deputationForm.getRadnotifyingauthtype());
//            nb.setRdTransaction(deputationForm.getRdTransaction());
            System.out.println();
            if (deputationForm.getChkNotSBPrint() != null && deputationForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }
            if (deputationForm.getHidNotId() > 0) {

                nb.setNotid(deputationForm.getHidNotId());
                notificationDao.modifyNotificationData(nb);
                deputationDAO.updateDeputation(deputationForm);

                if (deputationForm.getHidTransferId() != null && !deputationForm.getHidTransferId().equals("")) {
                    transferForm.setTransferId(deputationForm.getHidTransferId());
                    transferForm.setHnotType("DEPUTATION");
                    transferForm.setEmpid(deputationForm.getEmpid());
                    transferForm.setHidPostedDeptCode(deputationForm.getHidPostedDeptCode());
                    transferForm.setHidPostedOffCode(deputationForm.getHidPostedOffCode());
                    transferForm.setRdTransaction(deputationForm.getRdTransaction());

                    if (deputationForm.getRadpostingauthtype().equalsIgnoreCase("GOO")) {
                        transferForm.setPostedspc(deputationForm.getHidPostedSpc());
                    } else if (deputationForm.getRadpostingauthtype().equalsIgnoreCase("ISO")) {
                        transferForm.setPostedspc(deputationForm.getHidInterStateSPC());
                    } else {
                        transferForm.setPostedspc(deputationForm.getHidPostedOthSpc());
                    }

                    transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
                    transferDao.updateTransferDeputation(transferForm, loginbean.getLoginempid(), loginbean.getLoginuserid());
                }
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                deputationDAO.saveDeputation(deputationForm, notid);

                transferForm.setHnotType("DEPUTATION");
                transferForm.setEmpid(deputationForm.getEmpid());
                transferForm.setHidPostedDeptCode(deputationForm.getHidPostedDeptCode());
                transferForm.setHidPostedOffCode(deputationForm.getHidPostedOffCode());
                transferForm.setRdTransaction(deputationForm.getRdTransaction());

                if (deputationForm.getRadpostingauthtype().equalsIgnoreCase("GOO")) {
                    transferForm.setPostedspc(deputationForm.getHidPostedSpc());
                } else if (deputationForm.getRadpostingauthtype().equalsIgnoreCase("ISO")) {
                    transferForm.setPostedspc(deputationForm.getHidInterStateSPC());
                } else {
                    transferForm.setPostedspc(deputationForm.getHidPostedOthSpc());
                }

                transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
                transferDao.saveTransferDeputation(transferForm, notid, loginbean.getLoginempid(), loginbean.getLoginuserid());
            }

            mav = new ModelAndView("redirect:/DeputationList.htm", "deputationForm", deputationForm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveDeputation", params = "action=Back to List Page")
    public ModelAndView BackToListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/DeputationList.htm", "deputationForm", dform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveDeputation", params = "action=Delete")
    public ModelAndView deleteDeputation(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {
            deputationDAO.deleteDeputation(dform.getEmpid(), dform.getHidNotId());
            mav = new ModelAndView("redirect:/DeputationList.htm", "deputationForm", dform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    /*@RequestMapping(value = "saveDeputation")
     public void saveDeputation(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") LoginUserBean lub, @ModelAttribute("deputationForm") DeputationDataForm deputationForm) throws ParseException {

     response.setContentType("application/json");
     PrintWriter out = null;

     NotificationBean nb = new NotificationBean();

     SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

     Message msg = null;

     TransferForm transferForm = new TransferForm();
     try {
     deputationForm.setEmpid(lub.getEmpid());

     nb.setNottype("DEPUTATION");
     nb.setEmpId(deputationForm.getEmpid());
     nb.setDateofEntry(new Date());
     nb.setOrdno(deputationForm.getTxtNotOrdNo());
     nb.setOrdDate(sdf.parse(deputationForm.getTxtNotOrdDt()));
     nb.setSancDeptCode(deputationForm.getHidNotifyingDeptCode());
     nb.setSancOffCode(deputationForm.getHidNotifyingOffCode());
     nb.setSancAuthCode(deputationForm.getNotifyingSpc());
     nb.setEntryDeptCode(lub1.getDeptcode());
     nb.setEntryOffCode(lub1.getOffcode());
     nb.setEntryAuthCode(lub1.getSpc());
     nb.setNote(deputationForm.getNote());
     if (deputationForm.getHidNotId() != null && !deputationForm.getHidNotId().trim().equals("")) {
     nb.setNotid(deputationForm.getHidNotId());
     notificationDao.modifyNotificationData(nb);
     msg = deputationDAO.updateDeputation(deputationForm);

     if (deputationForm.getHidTransferId() != null && !deputationForm.getHidTransferId().equals("")) {
     transferForm.setTransferId(deputationForm.getHidTransferId());
     transferForm.setHnotType("DEPUTATION");
     transferForm.setEmpid(deputationForm.getEmpid());
     transferForm.setHidPostedDeptCode(deputationForm.getHidPostedDeptCode());
     transferForm.setHidPostedOffCode(deputationForm.getHidPostedOffCode());
     transferForm.setPostedspc(deputationForm.getPostedSpc());
     transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
     transferDao.updateTransfer(transferForm);
     }
     } else {
     String notid = notificationDao.insertNotificationData(nb);
     msg = deputationDAO.saveDeputation(deputationForm, notid);

     transferForm.setHnotType("DEPUTATION");
     transferForm.setEmpid(deputationForm.getEmpid());
     transferForm.setHidPostedDeptCode(deputationForm.getHidPostedDeptCode());
     transferForm.setHidPostedOffCode(deputationForm.getHidPostedOffCode());
     transferForm.setPostedspc(deputationForm.getPostedSpc());
     transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
     transferDao.saveTransfer(transferForm, notid);
     }

     loginDao.updateCadreStatus(deputationForm.getEmpid(), deputationForm.getSltCadreStatus(), deputationForm.getSltSubCadreStatus());

     JSONObject job = new JSONObject(msg);
     out = response.getWriter();
     out.write(job.toString());
     } catch (Exception e) {
     e.printStackTrace();
     }
     }*/
    @RequestMapping(value = "editDeputation")
    public ModelAndView editDeputation(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedObj, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        DeputationDataForm dptnform = null;
        try {
            String empid = selectedObj.getEmpId();

            int notificationId = Integer.parseInt(requestParams.get("notId"));

            dptnform = deputationDAO.getEmpDeputationData(empid, notificationId);
            dptnform.setHidNotId(notificationId);
            dptnform.setEmpid(empid);
            if (dptnform.getRadpostingauthtype() == null || dptnform.getRadpostingauthtype().equals("")) {
                dptnform.setRadpostingauthtype("GOO");
            }
            if (dptnform.getRadnotifyingauthtype() == null || dptnform.getRadnotifyingauthtype().equals("")) {
                dptnform.setRadnotifyingauthtype("GOO");
            }
            mav = new ModelAndView("/deputation/DeputationData", "deputationForm", dptnform);

            List deptlist = deptDAO.getDepartmentList();

            List sancOffList = offDAO.getTotalOfficeList(dptnform.getHidNotifyingDeptCode());
            List sancPostList = substantivePostDAO.getSanctioningSPCOfficeWiseList(dptnform.getHidNotifyingOffCode());
//            System.out.println("dptnform.getHidPostedDeptCode():"+dptnform.getHidPostedDeptCode()+dptnform.getHidPostedOffCode());

//            System.out.println("The post" + dptnform.getHidPostedOffCode());
            List postedOffList = offDAO.getTotalOfficeList(dptnform.getHidPostedDeptCode());
//            System.out.println("The Dept" + dptnform.getHidPostedDeptCode());
            List postedPostList = substantivePostDAO.getOfficeWithSPCList(dptnform.getHidPostedOffCode());

            List fieldofflist = offDAO.getFieldOffList(dptnform.getHidPostedOffCode());

            List cadrestatuslist = deputationDAO.getCadreStatusList("DEP");
            List subCadrestatuslist = deputationDAO.getSubCadreStatusList(dptnform.getSltCadreStatus());
            List otherOrgfflist = offDAO.getOtherOrganisationList();
//            getTotalDeputedOfficeList

            if (dptnform.getNextOfficeCode() != null && !dptnform.getNextOfficeCode().equals("")) {
                List otherOrgPostedOfflist = offDAO.getTotalOfficeList(dptnform.getHidPostedDeptCode());
                mav.addObject("otherOrgPostedOfflist", otherOrgPostedOfflist);
                 List foreignbodypostlist = postDAO.getForeignBodyPostList(dptnform.getHidPostedDeptCode());  //dptnform.getHidPostedDeptCode()
                 mav.addObject("foreiforeignbodypostlistgnbodypostlist", foreignbodypostlist);
                 
                

            } else {
                List postedOfflist = offDAO.getTotalOfficeListForBacklogEntry(dptnform.getHidPostedDeptCode());
                mav.addObject("postedOfflist", postedOfflist);

                String cadrecode = transferDao.getCadreCode(selectedObj.getEmpId());
                List postedPostlist = substantivePostDAO.getCadreWisePostList(dptnform.getHidPostedOffCode(), cadrecode);
                mav.addObject("postedPostlist", postedPostlist);
            }

            List GOIOfficeList = offDAO.getGOIOfficeList(dptnform.getHidGOICategory());

            List statelist = stateDAOImpl.getStateList();
            List interStateOfficeList = offDAO.getInterStateOfficeList(dptnform.getHidInterState());

            List foreignbodypostlist = postDAO.getForeignBodyPostList(dptnform.getHidPostedDeptCode());

            mav.addObject("deptlist", deptlist);
            mav.addObject("foreignbodypostlist", foreignbodypostlist);

            mav.addObject("sancOfflist", sancOffList);
            mav.addObject("postedOffList", postedOffList);

            mav.addObject("sancPostlist", sancPostList);
            mav.addObject("postedPostList", postedPostList);
            mav.addObject("otherOrgfflist", otherOrgfflist);
            mav.addObject("fieldofflist", fieldofflist);
            //mav.addObject("DepuList", depuList);
            mav.addObject("cadrestatuslist", cadrestatuslist);
            mav.addObject("subCadrestatuslist", subCadrestatuslist);

            mav.addObject("GOIOfficeList", GOIOfficeList);

            mav.addObject("statelist", statelist);
            mav.addObject("interStateOfficeList", interStateOfficeList);

            mav.addObject("nextofficecode", dptnform.getNextOfficeCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewDeputation")
    public ModelAndView viewDeputation(@ModelAttribute("SelectedEmpObj") Users selectedObj, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = new ModelAndView();

        DeputationDataForm dptnform = null;
        try {
            String empid = selectedObj.getEmpId();

            int notificationId = Integer.parseInt(requestParams.get("notId"));

            dptnform = deputationDAO.getEmpDeputationData(empid, notificationId);
            dptnform.setHidNotId(notificationId);
            dptnform.setEmpid(empid);
            if (dptnform.getRadpostingauthtype() == null || dptnform.getRadpostingauthtype().equals("")) {
                dptnform.setRadpostingauthtype("GOO");
            }
            if (dptnform.getRadnotifyingauthtype() == null || dptnform.getRadnotifyingauthtype().equals("")) {
                dptnform.setRadnotifyingauthtype("GOO");
            }

            mav.addObject("dptnform", dptnform);

            String notideptname = deptDAO.getDeptName(dptnform.getHidNotifyingDeptCode());
            mav.addObject("notideptname", notideptname);

            Office office = offDAO.getOfficeDetails(dptnform.getHidNotifyingOffCode());
            mav.addObject("notioffice", office.getOffEn());

            String posteddeptname = deptDAO.getDeptName(dptnform.getHidPostedDeptCode());
            mav.addObject("posteddeptname", posteddeptname);

            office = offDAO.getOfficeDetails(dptnform.getHidPostedOffCode());
            mav.addObject("postedoffice", office.getOffEn());

            office = offDAO.getOfficeDetails(dptnform.getSltFieldOffice());
            mav.addObject("fieldoffice", office.getOffEn());

            mav.setViewName("/deputation/ViewDeputation");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "deputationGetFieldOffListJSON", method = RequestMethod.POST)
    public void GetFieldOffListJSON(HttpServletResponse response, @RequestParam("offcode") String offcode) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List fieldofflist = null;
        try {
            if (offcode != null && !offcode.equals("")) {
                fieldofflist = offDAO.getFieldOffList(offcode);
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
    @RequestMapping(value = "deputationGetCadreStatusListJSON", method = RequestMethod.POST)
    public void GetCadreStatusListJSON(HttpServletResponse response) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List cadrestatuslist = null;
        try {
            cadrestatuslist = deputationDAO.getCadreStatusList("DEP");

            JSONArray json = new JSONArray(cadrestatuslist);
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
    @RequestMapping(value = "deputationGetSubCadreStatusListJSON")
    public void GetSubCadreStatusListJSON(HttpServletResponse response, @RequestParam("cadrestat") String cadrestat) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List cadrestatuslist = null;
        try {
            cadrestatuslist = deputationDAO.getSubCadreStatusList(cadrestat);

            JSONArray json = new JSONArray(cadrestatuslist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AGEndorsementOnDeputation")
    public ModelAndView AGDeputationList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {

            List depuList = deputationDAO.getAGDeputationList(lub.getEmpId());

            mav = new ModelAndView("/deputation/AGDeputation", "deputationForm", dform);
            mav.addObject("DepuList", depuList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newAGDeputation")
    public ModelAndView newAGDeputation(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {
            //List depuList = deputationDAO.getDeputationList(lub.getEmpId());

            mav = new ModelAndView("/deputation/AGDeputationData", "deputationForm", dform);

            dform.setRadpostingauthtype("GOO");

            List deptlist = deptDAO.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List cadrestatuslist = deputationDAO.getCadreStatusList("DEP");

            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);

            mav.addObject("cadrestatuslist", cadrestatuslist);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAGDeputation", params = "action=Back to AG Endorsement List Page")
    public ModelAndView BackToGDeputationListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm dform) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/AGEndorsementOnDeputation.htm", "deputationForm", dform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAGDeputation", params = "action=Save AG Endorsement Deputation")
    public ModelAndView saveAGDeputation(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm deputationForm) {

        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        TransferForm transferForm = new TransferForm();
        try {
            deputationForm.setEmpid(lub.getEmpId());

            nb.setNottype("DEPUTATION_AG");
            nb.setEmpId(deputationForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(deputationForm.getTxtNotOrdNo());
            if (deputationForm.getTxtNotOrdDt() != null && !deputationForm.getTxtNotOrdDt().equals("")) {
                nb.setOrdDate(sdf.parse(deputationForm.getTxtNotOrdDt()));
            }
            nb.setRadpostingauthtype(deputationForm.getRadpostingauthtype());
            nb.setSancDeptCode(deputationForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(deputationForm.getHidNotifyingOffCode());
            if (deputationForm.getRadpostingauthtype() != null && deputationForm.getRadpostingauthtype().equals("GOI")) {
                nb.setSancAuthCode(deputationForm.getHidNotifyingOthSpc());
            } else {
                nb.setSancAuthCode(deputationForm.getHidNotiSpc());
            }
            nb.setNote(deputationForm.getNote());
            if (deputationForm.getChkNotSBPrint() != null && deputationForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }

            if (deputationForm.getHidNotId() > 0) {
                nb.setNotid(deputationForm.getHidNotId());
                notificationDao.modifyNotificationData(nb);
                deputationDAO.updateAGDeputation(deputationForm);
                mav = new ModelAndView("redirect:/AGEndorsementOnDeputation.htm");

            } else {
                int notid = notificationDao.insertNotificationData(nb);
                nb.setNotid(notid);
                deputationDAO.saveAGDeputation(deputationForm, notid);

                transferForm.setHnotType("DEPUTATION_AG");
                transferForm.setEmpid(deputationForm.getEmpid());
                transferForm.setHidPostedDeptCode(deputationForm.getHidPostedDeptCode());
                transferForm.setHidPostedOffCode(deputationForm.getHidPostedOffCode());
                transferForm.setPostedspc(deputationForm.getHidPostedSpc());
                // transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
                //  transferDao.saveTransfer(transferForm, notid,loginbean.getLoginempid());
                mav = new ModelAndView("redirect:/editAGDeputation.htm?notId=" + nb.getNotid(), "deputationForm", deputationForm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editAGDeputation")
    public ModelAndView editAGDeputation(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        DeputationDataForm dptnform = null;
        //DeputationDataForm dptnformleave = null;
        try {
            String empid = lub.getEmpId();
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            dptnform = deputationDAO.getEmpAGDeputationData(empid, notificationId);

            mav = new ModelAndView("/deputation/AGDeputationData", "deputationForm", dptnform);

            if (dptnform.getRadpostingauthtype() == null || dptnform.getRadpostingauthtype().equals("")) {
                dptnform.setRadpostingauthtype("GOO");
            }

            List deptlist = deptDAO.getDepartmentList();
            List sancOffList = offDAO.getTotalOfficeList(dptnform.getHidNotifyingDeptCode());
            List postedOffList = offDAO.getTotalOfficeList(dptnform.getHidPostedDeptCode());
            List dptnformleave = deputationDAO.getEmpAGDeputationLeaveData(empid, notificationId, "L");

            List postedPostList = substantivePostDAO.getSanctioningSPCOfficeWiseList(dptnform.getHidNotifyingOffCode());
            mav.addObject("sancPostlist", postedPostList);

            mav.addObject("dptnformleave", dptnformleave);
            mav.addObject("deptlist", deptlist);
            mav.addObject("sancOfflist", sancOffList);
            mav.addObject("postedOffList", postedOffList);
            mav.addObject("empid", empid);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveLeaveContribution")
    public ModelAndView saveLeaveContribution(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("deputationForm") DeputationDataForm deputationForm) {

        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            deputationDAO.saveAGLeaveContribution(deputationForm);
            mav = new ModelAndView("redirect:/editAGDeputation.htm?notId=" + deputationForm.getHidNotId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteAGDeputation")
    public ModelAndView deleteAGDeputation(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users lub, @RequestParam("cId") String cId, @RequestParam("notId") String notId) {

        ModelAndView mav = null;
        String empid = lub.getEmpId();
        int notificationId = Integer.parseInt(notId);

        try {
            deputationDAO.deleteAGDeputation(empid, cId, notificationId);
            mav = new ModelAndView("redirect:/editAGDeputation.htm?notId=" + notificationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getGOIOfficeListJSON")
    public void getGOIOfficeListJSON(HttpServletResponse response, @RequestParam("category") String category) {

        response.setContentType("application/json");
        PrintWriter out = null;

        List offlist = null;
        try {
            offlist = offDAO.getGOIOfficeList(category);

            JSONArray json = new JSONArray(offlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "EmployeeOnDeputationList.htm", method = RequestMethod.GET)
    public ModelAndView deputationlist(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("deputationForm") DeputationDataForm deputationForm) {

        ModelAndView mav = new ModelAndView();
        try {           
            List deptList = deputationDAO.getAllEmpDeputation(loginbean.getLogindeptcode(),loginbean.getLoginoffcode());           
            mav.addObject("deptList", deptList);
            mav.setViewName("/deputation/DeputationEmplist");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

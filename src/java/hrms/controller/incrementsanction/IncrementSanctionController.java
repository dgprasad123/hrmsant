package hrms.controller.incrementsanction;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.master.Payscale;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class IncrementSanctionController implements ServletContextAware {

    private ServletContext context;

    @Autowired
    public DepartmentDAO deptDAO;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public AqFunctionalities aqfunctionalities;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @RequestMapping(value = "IncrementSanctionList")
    public ModelAndView incrementlist(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView("IncrementSanction/IncrementSanctionList");

            List incrlist = incrementsancDAO.getIncrementList(selectedEmpObj.getEmpId());
            mav.addObject("incrlist", incrlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addIncrement")
    public ModelAndView addIncrement(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        ModelAndView mav = null;
        String remEmpCategory = "";
        try {
            String isregular = selectedEmpObj.getIsRegular();
            String postGrp = selectedEmpObj.getPostgrp();
            incrementform.setMaxIncrDate(incrementsancDAO.getEmployeeMaxIncrementDate(selectedEmpObj.getEmpId()));
            if (isregular != null && isregular.equals("C")) {
                if (postGrp != null && (postGrp.equals("C") || postGrp.equals("D"))) {
                    remEmpCategory = "Y";
                }
            }

            String payComm = selectedEmpObj.getPayCommission();

            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            incrementform.setRdoPaycomm(payComm);
            incrementform.setEmpid(selectedEmpObj.getEmpId());
            incrementform.setRadsancauthtype("GOO");
            mav = new ModelAndView("IncrementSanction/NewIncrementSanction", "incrementForm", incrementform);

            mav.addObject("maxIncrDate", incrementform.getMaxIncrDate());
            mav.addObject("deptlist", deptDAO.getDepartmentList());
            mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
            mav.addObject("payComm", payComm);
            mav.addObject("remEmpCategory", remEmpCategory);
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else if (incrementsancDAO.checkEmployeeIPSStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForIPS());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            //mav.addObject("payMatxCont2017", payscaleDAO.getPayMatrixCont2017());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            //mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(selectedEmpObj.getEmpId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveIncrement", params = {"btnIncr=Save"})
    public ModelAndView saveIncrement(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users lub1, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            incrementform.setEmpid(lub1.getEmpId());

            if (incrementform.getHnotid() > 0) {
                if (incrementform.getHidIncrId() > 0) {

                    if (incrementform.getRdoPaycomm() != null && incrementform.getRdoPaycomm().equals("REM")) {
                        incrementform.setIncrementLvl(null);
                        incrementform.setIncrementType(null);
                    } else {
                        incrementform.setIncrementLvl(incrementform.getIncrementLvl());
                        incrementform.setIncrementType(incrementform.getIncrementType());
                    }
                    incrementsancDAO.updateIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
                    //sbCorReqDao.updateRequestedServiceBookFlag(lub1.getEmpId(), "INCREMENT", incrementform.getHnotid() + "");
                } else {
                    incrementsancDAO.saveIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
                }
            } else {
                incrementsancDAO.saveIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/IncrementSanctionList.htm");
    }

    @RequestMapping(value = "editIncrement")
    public ModelAndView editIncrement(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        String remEmpCategory = "";

        try {
            int incrementId = Integer.parseInt(requestParams.get("incrId"));
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            String isregular = selectedEmpObj.getIsRegular();
            String postGrp = selectedEmpObj.getPostgrp();
            if (isregular != null && isregular.equals("C")) {
                if (postGrp != null && (postGrp.equals("C") || postGrp.equals("D"))) {
                    remEmpCategory = "Y";
                }
            }
            
            incrementform = incrementsancDAO.getEmpIncRowData(selectedEmpObj.getEmpId(), incrementId);
            incrementform.setHidIncrId(incrementId);
            incrementform.setHnotid(notificationId);
            incrementform.setMaxIncrDate(incrementsancDAO.getEmployeeMaxIncrementDate(selectedEmpObj.getEmpId()));

            mav = new ModelAndView("IncrementSanction/NewIncrementSanction", "incrementForm", incrementform);

            if (incrementform.getPayLevel() != null && !incrementform.getPayLevel().equals("")
                    && !incrementform.getPayCell().equals("0") && !incrementform.getPayLevel().equals("0")) {
                incrementform.setRdoPaycomm("7");
            } else if (incrementform.getSltPayScale() != null && !incrementform.getSltPayScale().equals("")) {
                incrementform.setRdoPaycomm("6");
            } else if (incrementform.getIsRemuneration() != null && incrementform.getIsRemuneration().equals("Y")) {
                incrementform.setRdoPaycomm("REM");
                //incrementform.setIsRemuneration("Y");
                Payscale pscale = payscaleDAO.getPayMatrixCont2017Data(incrementform.getHidremBasic());
                incrementform.setContRemStages(pscale.getContYear());
                incrementform.setContRemAmount(Double.toString(pscale.getAmt()));
                List contRemAmtList = payscaleDAO.getPayMatrixCont2017(pscale.getContYear());
                mav.addObject("remunerationData", incrementform.getContRemAmount());
                mav.addObject("contRemAmtList", contRemAmtList);
            }
           // System.out.println("incrementform.getMaxIncrDate()::edit::"+incrementform.getMaxIncrDate()+selectedEmpObj.getEmpId());
            mav.addObject("maxIncrDate", incrementform.getMaxIncrDate());
            mav.addObject("deptlist", deptDAO.getDepartmentList());
            mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
            mav.addObject("offlist", offDAO.getTotalOfficeList(incrementform.getDeptCode()));
            mav.addObject("postlist", subStantivePostDao.getSanctioningSPCOfficeWiseList(incrementform.getHidOffCode()));
            if (incrementsancDAO.checkEmployeeLawStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else if (incrementsancDAO.checkEmployeeIPSStatus(selectedEmpObj.getEmpId()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForIPS());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            mav.addObject("remEmpCategory", remEmpCategory);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(selectedEmpObj.getEmpId()));
            mav.addObject("requestedSBLang", sbCorReqDao.getRequestedServiceBookLanguage(selectedEmpObj.getEmpId(), "INCREMENT", notificationId + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewIncrement")
    public ModelAndView viewIncrement(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();

        try {
            int incrementId = Integer.parseInt(requestParams.get("incrId"));
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            incrementform = incrementsancDAO.getEmpIncRowData(selectedEmpObj.getEmpId(), incrementId);
            incrementform.setHidIncrId(incrementId);
            incrementform.setHnotid(notificationId);
            if (incrementform.getPayLevel() != null && !incrementform.getPayLevel().equals("")) {
                incrementform.setRdoPaycomm("7");
            } else {
                incrementform.setRdoPaycomm("6");
            }

            mav.addObject("incrementform", incrementform);

            String authdeptname = deptDAO.getDeptName(incrementform.getDeptCode());
            mav.addObject("sancdeptname", authdeptname);

            Office office = offDAO.getOfficeDetails(incrementform.getHidOffCode());
            mav.addObject("sancoffice", office.getOffEn());

            mav.setViewName("IncrementSanction/ViewIncrementSanction");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveIncrement", params = {"btnIncr=Delete"})
    public ModelAndView deleteIncrement(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            incrementform.setEmpid(selectedEmpObj.getEmpId());
            incrementsancDAO.deleteIncrement(incrementform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/IncrementSanctionList.htm");
    }

    @RequestMapping(value = "saveIncrement", params = {"btnIncr=Back"})
    public ModelAndView back() {
        return new ModelAndView("redirect:/IncrementSanctionList.htm");
    }

    @ResponseBody
    @RequestMapping(value = "DeleteIncrementSaction")
    public ModelAndView deleteIncrementSaction(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        IncrementForm incrementform = new IncrementForm();
        List postinglist = null;
        ModelAndView mav = null;

        try {
            incrementform.setEmpid(u.getEmpId());

            int notificationId = Integer.parseInt(requestParams.get("not_id"));
            int incrementId = Integer.parseInt(requestParams.get("incr_id"));
            incrementform = incrementsancDAO.getEmpIncRowData(u.getEmpId(), incrementId);
            incrementform.setHnotid(notificationId);
            incrementform.setHidIncrId(incrementId);
            incrementform.setEmpid(u.getEmpId());

            //int retVal = notificationDao.deleteNotificationData(notificationId, "INCREMENT"); 
            //  if (retVal > 0) {
            incrementsancDAO.deleteIncrement(incrementform);
            //}            
            mav = new ModelAndView("IncrementSanction/IncrementSanctionList");

            List incrlist = incrementsancDAO.getIncrementList(u.getEmpId());
            mav.addObject("incrlist", incrlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "CancelIncrementSanction")
    public ModelAndView CancelIncrementSanction(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        IncrementForm incrform = new IncrementForm();
        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            if (incrementform.getCancelnotId() != null && !incrementform.getCancelnotId().equals("")) {
                incrform = incrementsancDAO.editCancelIncRowData(selectedEmpObj.getEmpId(), Integer.parseInt(incrementform.getCancelnotId()));
            } else {
                incrform.setLinkid(notificationId + "");
                incrform.setRadsancauthtype("GOO");
            }

            incrementform.setEmpid(selectedEmpObj.getEmpId());
            mav = new ModelAndView("IncrementSanction/CancelIncrementSanction", "incrementForm", incrform);

            mav.addObject("deptlist", deptDAO.getDepartmentList());

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveCancelIncrementSanction")
    public ModelAndView SaveCancelIncrementSanction(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users lub1, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            incrementform.setEmpid(lub1.getEmpId());

            if (incrementform.getHnotid() > 0) {
                incrementsancDAO.updateCancelIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginuserid());
            } else {
                incrementsancDAO.saveCancelIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginuserid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/IncrementSanctionList.htm");
    }

    @RequestMapping(value = "SupersedeIncrementSanction")
    public ModelAndView SupersedeIncrementSanction(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        IncrementForm incrform = new IncrementForm();
        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            if (incrementform.getSupersedeid() != null && !incrementform.getSupersedeid().equals("")) {
                incrform = incrementsancDAO.editSupersedeIncRowData(selectedEmpObj.getEmpId(), Integer.parseInt(incrementform.getSupersedeid()));
            } else {
                incrform.setLinkid(notificationId + "");
                incrform.setRadsancauthtype("GOO");
            }

            incrform.setEmpid(selectedEmpObj.getEmpId());

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            incrform.setRdoPaycomm(payComm);

            mav = new ModelAndView("IncrementSanction/SupersedeIncrementSanction", "incrementForm", incrform);

            mav.addObject("deptlist", deptDAO.getDepartmentList());
            mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
            mav.addObject("payComm", payComm);
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

    @RequestMapping(value = "SaveSupersedeIncrement", params = {"btnIncr=Save"})
    public ModelAndView SaveSupersedeIncrement(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users lub1, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            incrementform.setEmpid(lub1.getEmpId());

            if (incrementform.getHnotid() > 0) {
                if (incrementform.getHidIncrId() > 0) {
                    incrementsancDAO.updateSupersedeIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
                } else {
                    incrementsancDAO.saveSupersedeIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
                }
            } else {
                incrementsancDAO.saveSupersedeIncrement(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/IncrementSanctionList.htm");
    }

    @ResponseBody
    @RequestMapping(value = "getPaymatrixContListJSON")
    public void getPaymatrixContListJSON(HttpServletResponse response, @RequestParam("remYear") String remYear) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List contRemAmtList = payscaleDAO.getPayMatrixCont2017(remYear);
            json = new JSONArray(contRemAmtList);
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
    @RequestMapping(value = "getPaymatrixList2017JSON")
    public void getPaymatrixList2017JSON(HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List matrixLevelList = incrementsancDAO.getPayMatrixLevel();
            json = new JSONArray(matrixLevelList);
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
    @RequestMapping(value = "getMatrixCellListLawLevelWiseJSON")
    public void getMatrixCellListLawLevelWiseJSON(HttpServletResponse response, @RequestParam("matrixLevelValue") String matrixLevelValue) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List matriXCellList = incrementsancDAO.getPayMatrixCellForLAWLevel(matrixLevelValue);
            json = new JSONArray(matriXCellList);
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
    @RequestMapping(value = "getMatrixCellListGPLevelWiseJSON")
    public void getMatrixCellListLevelWiseForLawJSON(HttpServletResponse response, @RequestParam("matrixLevelValue") String matrixLevelValue) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List matriXCellList = incrementsancDAO.getPayMatrixCellForLAWLevel(matrixLevelValue);
            json = new JSONArray(matriXCellList);
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
    @RequestMapping(value = "getPayMatrixLevelAllForLAWJSON")
    public void getPayMatrixLevelAllForLAWJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List allLevelListForLaw = incrementsancDAO.getPayMatrixLevelAllForLAW();
            json = new JSONArray(allLevelListForLaw);
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
    @RequestMapping(value = "getPayMatrixLevelForLAWJSON")
    public void getPayMatrixLevelForLAWJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List levelListForLaw = incrementsancDAO.getPayMatrixLevelForLAW();
            json = new JSONArray(levelListForLaw);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "EmployeeEditIncrementSBCorrection")
    public ModelAndView EmployeeEditIncrementSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        String remEmpCategory = "";

        try {
            String mode = requestParams.get("mode");
            int incrementId = 0;
            if (requestParams.get("incrId") != null && !requestParams.get("incrId").equals("")) {
                incrementId = Integer.parseInt(requestParams.get("incrId"));
            }
            String noteIdEnc = requestParams.get("notId");
            int notificationId = 0;
            if (noteIdEnc != null && !noteIdEnc.equals("")) {
                notificationId = Integer.parseInt(CommonFunctions.decodedTxt(noteIdEnc));
            }

            int correctionid = 0;
            if (requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")) {
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if (correctionid > 0) {
                incrementform = incrementsancDAO.getEmpIncRowDataSBCorrection(lub.getLoginempid(), incrementId, correctionid);
            } else {
                incrementform = incrementsancDAO.getEmpIncRowData(lub.getLoginempid(), incrementId);
            }
            incrementform.setHidIncrId(incrementId);
            incrementform.setHnotid(notificationId);
            String entrytype = requestParams.get("entrytype");
            incrementform.setEntrytype(entrytype);

            mav = new ModelAndView("/servicebook/employeemodules/EditIncrementSanctionEmployee", "incrementForm", incrementform);

            if (incrementform.getPayLevel() != null && !incrementform.getPayLevel().equals("")
                    && !incrementform.getPayCell().equals("0") && !incrementform.getPayLevel().equals("0")) {
                incrementform.setRdoPaycomm("7");
            } else if (incrementform.getSltPayScale() != null && !incrementform.getSltPayScale().equals("")) {
                incrementform.setRdoPaycomm("6");
            } else if (incrementform.getIsRemuneration() != null && incrementform.getIsRemuneration().equals("Y")) {
                incrementform.setRdoPaycomm("REM");
                //incrementform.setIsRemuneration("Y");
                Payscale pscale = payscaleDAO.getPayMatrixCont2017Data(incrementform.getHidremBasic());
                incrementform.setContRemStages(pscale.getContYear());
                incrementform.setContRemAmount(Double.toString(pscale.getAmt()));
                List contRemAmtList = payscaleDAO.getPayMatrixCont2017(pscale.getContYear());
                mav.addObject("remunerationData", incrementform.getContRemAmount());
                mav.addObject("contRemAmtList", contRemAmtList);
            }

            mav.addObject("deptlist", deptDAO.getDepartmentList());
            mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
            if (incrementsancDAO.checkEmployeeLawStatus(lub.getLoginempid()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else if (incrementsancDAO.checkEmployeeIPSStatus(lub.getLoginempid()).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForIPS());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            mav.addObject("remEmpCategory", remEmpCategory);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(lub.getLoginempid()));
            mav.addObject("requestedSBLang", sbCorReqDao.getRequestedServiceBookLanguage(lub.getLoginempid(), "INCREMENT", notificationId + ""));
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveIncrementSBCorrection", params = {"btnIncr=Save"})
    public String saveIncrementSBCorrection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            incrementform.setEmpid(lub.getLoginempid());

            if (incrementform.getRdoPaycomm() != null && incrementform.getRdoPaycomm().equals("REM")) {
                incrementform.setIncrementLvl(null);
                incrementform.setIncrementType(null);
            } else {
                incrementform.setIncrementLvl(incrementform.getIncrementLvl());
                incrementform.setIncrementType(incrementform.getIncrementType());
            }
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(incrementform.getEmpid());
            sbCorReqBean.setHidNotId(incrementform.getHnotid() + "");
            sbCorReqBean.setHidNotType("INCREMENT");
            sbCorReqBean.setEntrytype(incrementform.getEntrytype());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(), sbCorReqBean.getHidNotType(), sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);

            String sblang = incrementsancDAO.saveIncrementSBCorrection(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), incrementform.getRdoPaycomm(), lub.getLoginuserid(), sbcorrectionid);

            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sblang);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=INCREMENT";
    }

    @RequestMapping(value = "saveIncrementSBCorrection", params = {"btnIncr=Submit"})
    public String submitIncrementSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(), incrementform.getHnotid(), "INCREMENT");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=INCREMENT";
    }

    @RequestMapping(value = "EditIncrementSBCorrectionDDO")
    public ModelAndView EditIncrementSBCorrectionDDO(@RequestParam("empid") String empid, @ModelAttribute("incrementForm") IncrementForm incrementform, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        String remEmpCategory = "";

        try {
            String mode = requestParams.get("mode");
            String correctionid = requestParams.get("correctionid");
            int incrementId = Integer.parseInt(requestParams.get("incrId"));
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            String isregular = "R";
            String postGrp = "A";
            if (isregular != null && isregular.equals("C")) {
                if (postGrp != null && (postGrp.equals("C") || postGrp.equals("D"))) {
                    remEmpCategory = "Y";
                }
            }

            incrementform = incrementsancDAO.getEmpIncRowDataSBCorrection(empid, incrementId, Integer.parseInt(correctionid));
            incrementform.setHidIncrId(incrementId);
            incrementform.setHnotid(notificationId);
            incrementform.setCorrectionid(correctionid);
            incrementform.setEntrytype(requestParams.get("entrytype"));
            incrementform.setEmpid(empid);

            mav = new ModelAndView("/servicebook/employeemodules/EditIncrementSanctionEmployee", "incrementForm", incrementform);

            if (incrementform.getPayLevel() != null && !incrementform.getPayLevel().equals("")
                    && !incrementform.getPayCell().equals("0") && !incrementform.getPayLevel().equals("0")) {
                incrementform.setRdoPaycomm("7");
            } else if (incrementform.getSltPayScale() != null && !incrementform.getSltPayScale().equals("")) {
                incrementform.setRdoPaycomm("6");
            } else if (incrementform.getIsRemuneration() != null && incrementform.getIsRemuneration().equals("Y")) {
                incrementform.setRdoPaycomm("REM");
                //incrementform.setIsRemuneration("Y");
                Payscale pscale = payscaleDAO.getPayMatrixCont2017Data(incrementform.getHidremBasic());
                incrementform.setContRemStages(pscale.getContYear());
                incrementform.setContRemAmount(Double.toString(pscale.getAmt()));
                List contRemAmtList = payscaleDAO.getPayMatrixCont2017(pscale.getContYear());
                mav.addObject("remunerationData", incrementform.getContRemAmount());
                mav.addObject("contRemAmtList", contRemAmtList);
            }

            mav.addObject("deptlist", deptDAO.getDepartmentList());
            mav.addObject("payscalelist", payscaleDAO.getPayScaleList());
            //mav.addObject("offlist", offDAO.getTotalOfficeList(incrementform.getDeptCode()));
            //mav.addObject("postlist", subStantivePostDao.getSanctioningSPCOfficeWiseList(incrementform.getHidOffCode()));
            if (incrementsancDAO.checkEmployeeLawStatus(empid).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForLAW());
            } else if (incrementsancDAO.checkEmployeeIPSStatus(empid).equals("Y")) {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevelForIPS());
            } else {
                mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
            }
            mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());
            mav.addObject("remEmpCategory", remEmpCategory);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            mav.addObject("isEmployeeRegular", aqfunctionalities.isEmployeeRegular(empid));
            mav.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveIncrementSBCorrection", params = {"btnIncr=Approve"})
    public String approveIncrementSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("incrementForm") IncrementForm incrementform) {

        try {
            //incrementform.setEmpid(selectedEmpObj.getEmpId());
            incrementsancDAO.approveIncrementSBCorrection(incrementform, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginuserid());

            sbCorReqDao.updateRequestedServiceBookFlag(incrementform.getEmpid(), "INCREMENT", incrementform.getHnotid() + "", incrementform.getCorrectionid(), lub.getLoginempid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid=" + incrementform.getEmpid();
    }
}

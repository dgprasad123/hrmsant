package hrms.controller.performanceappraisal;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.dao.fiscalyear.FiscalYearDAOImpl;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARBrowserDAOImpl;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.parmast.AcceptingHelperBean;
import hrms.model.parmast.InitiateOtherPARForm;
import hrms.model.parmast.PARReportBean;
import hrms.model.parmast.PARTemplateBean;
import hrms.model.parmast.ParAbsenteeBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParMaster;
import hrms.model.parmast.ParMasterBean;
import hrms.model.parmast.ParOtherDetails;
import hrms.model.parmast.ParSubmitForm;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.parmast.ReviewingHelperBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class PARBrowserController implements ServletContextAware {

    @Autowired
    public LoginDAOImpl loginDao;

    @Autowired
    public PARBrowserDAOImpl parbrowserDao;

    @Autowired
    public FiscalYearDAOImpl fiscalDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    PARAdminDAO parAdminDAO;

    @Autowired
    CadreDAO cadreDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @ResponseBody
    @RequestMapping(value = "GetPARListJSON", method = RequestMethod.POST)
    public void getparlistJSON(@Valid @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        ParMaster pmast = null;
        ParMasterBean pbean = null;
        ArrayList parlist = new ArrayList();

        try {
            String year = "";
            if (parMastForm.getFiscalyear() != null && !parMastForm.getFiscalyear().equals("")) {
                year = parMastForm.getFiscalyear();
            } else {
                year = fiscalDAO.getDefaultFiscalYear();
            }
            context.setAttribute("selectedyear", year);

            List tempparlist = parbrowserDao.getPARList(year, lub.getLoginempid());

            for (int i = 0; i < tempparlist.size(); i++) {
                pmast = (ParMaster) tempparlist.get(i);
                pbean = new ParMasterBean();
                pbean.setParid(pmast.getParid());
                pbean.setEncryptedParid(pmast.getParid() + "");
                pbean.setParstatus(pmast.getParstatus());
                pbean.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(pmast.getPeriodfrom()));
                pbean.setPeriodto(CommonFunctions.getFormattedOutputDate1(pmast.getPeriodto()));
                //pbean.setDesignation(pmast.getSubstantivePost().getSpn());
                pbean.setDesignation(pmast.getSpn());
                pbean.setAppraiseePost(pmast.getAppraiseePost());
                pbean.setIsClosed(pmast.getIsClosed());
                pbean.setIsDeleted(pmast.getIsDeleted());
                pbean.setReportingRemarksClosed(pmast.getReportingRemarksClosed());
                if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                    pbean.setIsClosed("N");
                }
                pbean.setAuthRemarksClosed(pmast.getAuthRemarksClosed());
                pbean.setIseditable(pmast.getIseditable());
                pbean.setSubmittedOn(pmast.getSubmittedOn());
                pbean.setCadreName(pmast.getCadreName());
                pbean.setIsResubmitRevertPAR(pmast.getIsResubmitRevertPAR());
                System.out.println("pmast.getIsResubmitRevertPAR() *******" + pmast.getIsResubmitRevertPAR());
                //if (pmast.getIsDeleted() != null && !pmast.getIsDeleted().equals("") && !pmast.getIsDeleted().equals("Y")) {
                if (pmast.getIsDeleted() != null && !pmast.getIsDeleted().equals("") && pmast.getIsDeleted().equals("N")) {
                    parlist.add(pbean);
                }
            }

            json.put("total", 10);
            json.put("rows", parlist);
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
    @RequestMapping(value = "GetPARListForSIJSON", method = RequestMethod.POST)
    public void GetPARListForSIJSON(@Valid @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        ParMaster pmast = null;
        ParMasterBean pbean = null;
        ArrayList parlist = new ArrayList();

        try {
            String year = "";
            if (parMastForm.getFiscalyear() != null && !parMastForm.getFiscalyear().equals("")) {
                year = parMastForm.getFiscalyear();
            } else {
                year = fiscalDAO.getDefaultFiscalYear();
            }
            context.setAttribute("selectedyear", year);
            List tempparlist = parbrowserDao.getPARListForSI(year, lub.getLoginempid());

            for (int i = 0; i < tempparlist.size(); i++) {
                pmast = (ParMaster) tempparlist.get(i);
                pbean = new ParMasterBean();
                pbean.setParid(pmast.getParid());
                pbean.setEncryptedParid(pmast.getParid() + "");
                pbean.setParstatus(pmast.getParstatus());
                pbean.setPeriodfrom(CommonFunctions.getFormattedOutputDate1(pmast.getPeriodfrom()));
                pbean.setPeriodto(CommonFunctions.getFormattedOutputDate1(pmast.getPeriodto()));
                //pbean.setDesignation(pmast.getSubstantivePost().getSpn());
                pbean.setDesignation(pmast.getSpn());
                pbean.setIsClosed(pmast.getIsClosed());
                if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                    pbean.setIsClosed("N");
                }
                pbean.setAuthRemarksClosed(pmast.getAuthRemarksClosed());
                pbean.setIseditable(pmast.getIseditable());
                parlist.add(pbean);
            }

            json.put("total", 10);
            json.put("rows", parlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getSiPARList", method = RequestMethod.GET)
    public ModelAndView GetSiPARList(@Valid @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        String path = "/par/PARListforSI";
        String year = "";
        lub.setParType("SiPar");
        try {
            year = fiscalDAO.getDefaultFiscalYear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("defaultfiscalyear", year);
        mav.addObject("HideBtn", "Yes");
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "GetPARList", method = RequestMethod.GET)
    public ModelAndView getparlist(@Valid @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        String path = "/par/PARList";
        String year = "";
        lub.setParType("");
        try {
            year = fiscalDAO.getDefaultFiscalYear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("defaultfiscalyear", year);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "par/addPAR", method = RequestMethod.POST)
    public ModelAndView newPar(@RequestParam("newPar") String newPar, @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAbsentee") ParAbsenteeBean parabsenteeForm, @ModelAttribute("parAchievement") ParAchievement parAchievementForm, @ModelAttribute("parOtherDetails") ParOtherDetails parOtherDetails, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm) {

        ModelAndView mav = new ModelAndView();
        String path = "";

        int pageNo = 0;
        Users emp = null;

        String parfrmdt = "";
        String partodt = "";
        String year = "";
        try {
            parMastForm.setEmpid(lub.getLoginempid());
            pageNo = parMastForm.getPageno();
            parMastForm.setParType(lub.getParType());
            if (newPar != null && (newPar.equals("Previous") || newPar.equals("Back") || newPar.equals("Next") || newPar.equals("Update") || newPar.equals("Create PAR") || newPar.equals("Request For NRC") || newPar.equals("Initiate PAR for Others"))) {
                parMastForm.setFiscalyear(parMastForm.getFiscalyear());
                boolean isParCreatedDateClosed = parbrowserDao.isParCreatedDateClosed(lub.getLoginempid(), parMastForm.getFiscalyear());
                if (isParCreatedDateClosed == false) {
                    if (newPar.equals("Back") || newPar.equals("Previous")) {
                        if (pageNo == 1) {
                            year = fiscalDAO.getDefaultFiscalYear();
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                            if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                                path = "/par/PARListforSI";
                            } else {
                                path = "/par/PARList";
                            }
                        } else if (pageNo == 2) {
                            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parabsenteeForm.getHidparid());
                            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                            if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                                emp.setCadrecode("1484");
                                emp.setCadrename("ODISHA POLICE-III");
                            }
                            path = "/par/PersonalInformation";
                        } else if (pageNo == 3) {
                            parMastForm.setParid(parAchievementForm.getHidparid());
                            parfrmdt = parAchievementForm.getHidparfrmdt();
                            partodt = parAchievementForm.getHidpartodt();
                            path = "/par/AbsenteeList";
                        } else if (pageNo == 4) {
                            parMastForm.setParid(parOtherDetails.getHidparid());
                            path = "/par/AchievementList";
                        }
                    } else {
                        if (pageNo == 0) {
                            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                            if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                                emp.setCadrecode("1484");
                                emp.setCadrename("ODISHA POLICE-III");
                            }
                            String isClosed = parbrowserDao.isFiscalYearClosed(parMastForm.getFiscalyear());
                            if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                                isClosed = "N";
                            }
                            mav.addObject("isClosed", isClosed);
                            if (newPar.equals("Request For NRC")) {
                                path = "/par/CreateNRC";
                            } else if (newPar.equals("Initiate PAR for Others")) {
                                List deptlist = deptDAO.getDepartmentList();
                                mav.addObject("deptlist", deptlist);
                                path = "/par/InitiatePARForOthers";
                            } else {
                                mav.addObject("previousPARList", parbrowserDao.getPreviousPARDetail(lub.getLoginempid()));
                                path = "/par/PersonalInformation";
                            }
                        } else if (pageNo == 1) {
                            parfrmdt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodfrom());
                            partodt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodto());
                            boolean isDuplicatePeriod = parbrowserDao.isDuplicatePARPeriod(lub.getLoginempid(), parfrmdt, partodt, parMastForm.getFiscalyear(), parMastForm.getHidparid());
                            int diffInDays = (int) ((parMastForm.getPeriodto().getTime() - parMastForm.getPeriodfrom().getTime()) / (1000 * 60 * 60 * 24));
                            String cadreCode = parMastForm.getCadreCode();
                            if (isDuplicatePeriod == false) {
                                if (!parMastForm.getHidparid().equals("0")) {
                                    parMastForm.setParid(Integer.parseInt(parMastForm.getHidparid()));
                                    parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                                }

                                parfrmdt = parAchievementForm.getHidparfrmdt();
                                partodt = parAchievementForm.getHidpartodt();
                                emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                                parMastForm.setSpn(emp.getSpn());
                                parMastForm.setOffname(emp.getOffname());
                                if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                                    emp.setCadrecode("1484");
                                    emp.setCadrename("ODISHA POLICE-III");
                                }
                                mav.addObject("parerrmsg", "Duplicate Period");
                                path = "/par/PersonalInformation";
                            } else if (diffInDays < 120) {
                                if (parMastForm.getHidparid() != null && !parMastForm.getHidparid().equals("0")) {
                                    parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                                }
                                emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                                if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                                    emp.setCadrecode("1484");
                                    emp.setCadrename("ODISHA POLICE-III");
                                }
                                mav.addObject("parerrmsg", "The minimum period for recording remark is four months (120 days)");
                                path = "/par/PersonalInformation";
                            } else if (cadreCode == null || cadreCode.equals("")) {
                                if (parMastForm.getHidparid() != null && !parMastForm.getHidparid().equals("0")) {
                                    parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                                }
                                emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                                if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                                    emp.setCadrecode("1484");
                                    emp.setCadrename("ODISHA POLICE-III");
                                }
                                mav.addObject("parerrmsg", "Service to which the officer belongs cannot be blank.");
                                path = "/par/PersonalInformation";
                            } else {
                                boolean iscorrectPeriod = parbrowserDao.isCheckParPeriod(parfrmdt, partodt, parMastForm.getFiscalyear());
                                if (iscorrectPeriod == true) {
                                    parMastForm.setParid(parbrowserDao.savePAR(pageNo, parMastForm, null, null));

                                    parMastForm.setFiscalyear(parMastForm.getFiscalyear());
                                    if (newPar.equals("Update")) {
                                        if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                                            path = "/par/PARListforSI";
                                        } else {
                                            path = "/par/PARList";
                                        }
                                    } else {
                                        if (parMastForm.getPrevpar() != 0) {
                                            parbrowserDao.savePreviousYearAchievementList(parMastForm.getPrevpar(), parMastForm.getParid());
                                            parbrowserDao.savePreviousyrOtherDetails(parMastForm.getPrevpar(), parMastForm.getParid());
                                        }
                                        path = "/par/AbsenteeList";
                                    }
                                } else {
                                    mav.addObject("parwrongprderrmsg", "Wrong Period");
                                    path = "/par/PersonalInformation";
                                }
                            }
                        } else if (pageNo == 2) {
                            parfrmdt = parabsenteeForm.getHidparfrmdt();
                            partodt = parabsenteeForm.getHidpartodt();
                            parMastForm.setParid(parabsenteeForm.getHidparid());
                            parMastForm.setFiscalyear(parabsenteeForm.getFiscalyear());
                            path = "/par/AchievementList";
                        } else if (pageNo == 3) {
                            parMastForm.setParid(parAchievementForm.getHidparid());
                            parOtherDetails = parbrowserDao.getOtherDetails(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                            if (parOtherDetails != null && !parOtherDetails.equals("")) {
                                parOtherDetails.setHidpaptid(parOtherDetails.getPaptid() + "");
                            }
                            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                            path = "/par/OtherDetails";
                        } else if (pageNo == 4) {
                            //parbrowserDao.savePAR(pageNo, parMastForm,parOtherDetails);
                            //path = "/par/PARList";
                        }
                    }
                } else {
                    mav.addObject("parerrmsg", "PAR for selected Financial Year is closed.");
                    path = "/par/PARList";
                }
            } else if (newPar != null && !newPar.equals("Next")) {
                if (newPar.equals("Add New")) {
                    if (parabsenteeForm.getMode() != null && parabsenteeForm.getMode().equals("absentee")) {
                        parabsenteeForm.setHidparid(parabsenteeForm.getHidparid());
                        parfrmdt = parabsenteeForm.getHidparfrmdt();
                        partodt = parabsenteeForm.getHidpartodt();

                        path = "/par/NewAbsentee";
                    } else if (parAchievementForm.getMode() != null && parAchievementForm.getMode().equals("achievement")) {
                        parMastForm.setParid(parAchievementForm.getHidparid());
                        parfrmdt = parAchievementForm.getHidparfrmdt();
                        partodt = parAchievementForm.getHidpartodt();
                        path = "/par/NewAchievement";
                    }
                } else if (newPar.equals("Save")) {
                    if (parabsenteeForm.getMode() != null && parabsenteeForm.getMode().equals("absentee")) {
                        parMastForm.setParid(parabsenteeForm.getHidparid());
                        parfrmdt = parabsenteeForm.getHidparfrmdt();
                        partodt = parabsenteeForm.getHidpartodt();
                        parbrowserDao.saveAbsentee(parabsenteeForm);
                        path = "/par/AbsenteeList";
                    } else if (parAchievementForm.getMode() != null && parAchievementForm.getMode().equals("achievement")) {
                        parMastForm.setParid(parAchievementForm.getHidparid());
                        String filepath = context.getInitParameter("ParPath");
                        parbrowserDao.saveAchievement(lub.getLoginempid(), parAchievementForm, filepath);
                        parfrmdt = parAchievementForm.getHidparfrmdt();
                        partodt = parAchievementForm.getHidpartodt();
                        path = "/par/AchievementList";
                    } else {
                        parbrowserDao.savePAR(pageNo, parMastForm, parOtherDetails, null);
                        if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                            path = "/par/PARListforSI";
                        } else {
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                            path = "/par/PARList";
                        }
                    }
                } else if (newPar.equals("Cancel")) {
                    if (parabsenteeForm.getMode() != null && parabsenteeForm.getMode().equals("absentee")) {
                        parMastForm.setParid(parabsenteeForm.getHidparid());
                        parfrmdt = parabsenteeForm.getHidparfrmdt();
                        partodt = parabsenteeForm.getHidpartodt();
                        path = "/par/AbsenteeList";
                    } else if (parAchievementForm.getMode() != null && parAchievementForm.getMode().equals("achievement")) {
                        parMastForm.setParid(parAchievementForm.getHidparid());
                        parfrmdt = parAchievementForm.getHidparfrmdt();
                        partodt = parAchievementForm.getHidpartodt();
                        path = "/par/AchievementList";
                    }
                } else if (newPar.equals("Delete")) {
                    mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                    mav.addObject("parstatus", parMastForm.getParstatus());
                    System.out.println("parMastForm.getFiscalyear()" + parMastForm.getFiscalyear());
                    boolean isEditable = parbrowserDao.isPAREditableByAppraisee(Integer.parseInt(parMastForm.getHidparid()));
                    boolean isSubmitBeforeNewDate = parbrowserDao.isSubmitByAppraiseeBeforeDate(Integer.parseInt(parMastForm.getHidparid()));

                    /* Written while all year PAR is Open with 2023-24
                     if (isEditable == true) {
                     if (parMastForm.getParstatus() == 0) {
                     parbrowserDao.deletePAR(parMastForm.getHidparid(), lub.getLoginempid());
                     if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                     path = "/par/PARListforSI";
                     } else {
                     path = "/par/PARList";
                     }
                         
                       
                     } else if (isSubmitBeforeNewDate == true && parMastForm.getParstatus() == 16) {
                     parbrowserDao.deletePAR(parMastForm.getHidparid(), lub.getLoginempid());
                     if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                     path = "/par/PARListforSI";
                     } else {
                     path = "/par/PARList";
                     }
                     } else {
                     emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                     parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                     if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                     emp.setCadrecode("1484");
                     emp.setCadrename("ODISHA POLICE-III");
                     }
                     mav.addObject("parerrmsg", "PAR cannot be deleted.");
                     path = "/par/PersonalInformation";
                     }*/
                    if (isEditable == true) {
                        parbrowserDao.deletePAR(parMastForm.getHidparid(), lub.getLoginempid());
                        path = "/par/PARList";

                    } else {
                        emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                        parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                        if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                            emp.setCadrecode("1484");
                            emp.setCadrename("ODISHA POLICE-III");
                        }
                        mav.addObject("parerrmsg", "PAR cannot be deleted.");
                        path = "/par/PersonalInformation";
                    }


                    /*Written before
                     boolean isEditable = parbrowserDao.isPAREditableByAppraisee(Integer.parseInt(parMastForm.getHidparid()));
                     if (isEditable == true) {
                     parbrowserDao.deletePAR(parMastForm.getHidparid(), lub.getLoginempid());
                     if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
                     path = "/par/PARListforSI";
                     } else {
                     path = "/par/PARList";
                     }
                     } else {
                     emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                     parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), Integer.parseInt(parMastForm.getHidparid()));
                     if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                     emp.setCadrecode("1484");
                     emp.setCadrename("ODISHA POLICE-III");
                     }
                     mav.addObject("parerrmsg", "Reverted PAR cannot be deleted.");
                     path = "/par/PersonalInformation";
                     }
                     */
                }
                mav.addObject("parAbsentee", parabsenteeForm);
            }
            mav.addObject("users", emp);
            mav.addObject("parMastForm", parMastForm);
            mav.addObject("parOtherDetails", parOtherDetails);
            mav.addObject("parfrmdt", parfrmdt);
            mav.addObject("partodt", partodt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "previousYearPARDetail.htm")
    public ModelAndView previousYearPARDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("fieldName") String fieldName
    ) {
        ModelAndView mv = new ModelAndView("/par/PreviousPARData");
        mv.addObject("otherDetailsList", parbrowserDao.getOtherDetailsOfAllFinancialYear(lub.getLoginempid(), fieldName));
        return mv;

    }

    @ResponseBody
    @RequestMapping(value = "addpreviousYearPARAchievement.htm")
    public void addpreviousYearPARAchievement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parachievement") ParAchievement parachievement, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        parbrowserDao.savePreviousYearAchievement(parachievement.getHidpacid(), parachievement.getParid());
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getpreviousYearPARAchievementList.htm", method = RequestMethod.POST)
    public void getpreviousYearPARAchievementList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("parachievement") ParAchievement parachievement) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList achievementDetailsList = null;
        achievementDetailsList = parbrowserDao.getAchievementDetailsOfAllFinancialYear(parachievement.getFiscalyear(), lub.getLoginempid());
        JSONArray achievementListJSON = new JSONArray(achievementDetailsList);
        JSONObject jobj = new JSONObject();
        jobj.put("achievementListJSON", achievementListJSON);
        out = response.getWriter();
        out.write(jobj.toString());
        out.close();
        out.flush();
    }

    @RequestMapping(value = "saveAbsenteeorAchievement", method = RequestMethod.POST)
    public String saveAbsenteeorAchievement() {

        String path = "";

        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return path;
    }

    @RequestMapping(value = "par/editPAR", method = RequestMethod.GET)
    public ModelAndView editPar(@Valid
            @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid
    ) {

        ModelAndView mav = new ModelAndView();
        String path = "";
        Users emp = null;
        try {
            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parid);
            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
            if (lub.getParType() != null && lub.getParType().equals("SiPar")) {
                emp.setCadrecode("1484");
                emp.setCadrename("ODISHA POLICE-III");
            }
            String isClosed = parbrowserDao.isFiscalYearClosed(parMastForm.getFiscalyear());
            if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                isClosed = "N";
            }
            mav.addObject("isClosed", isClosed);
            mav.addObject("parMastForm", parMastForm);
            mav.addObject("users", emp);
            path = "/par/PersonalInformation";
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "par/deletePAR", method = RequestMethod.GET)
    public ModelAndView deletePAR(@Valid
            @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") String parid
    ) {
        ModelAndView mav = new ModelAndView();
        parbrowserDao.deletePARForReportingRevert(parid, lub.getLoginempid());
        if (lub.getParType() != null && lub.getParType().equals("SiPar") && !lub.getParType().equals("")) {
            mav.setViewName("/par/PARListforSI");
        } else {
            mav.setViewName("/par/PARList");
        }
        return mav;
    }

    @RequestMapping(value = "par/editAbsenteeorAchievement", method = RequestMethod.GET)
    public ModelAndView editAbsenteeorAchievement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {

        String path = "";

        ParAbsenteeBean pabs = null;
        ParAchievement pach = null;
        ParMaster parMast = new ParMaster();

        ModelAndView mav = new ModelAndView();
        try {
            String mode = (String) requestParams.get("mode");
            int id = Integer.parseInt(requestParams.get("id"));
            String fiscalyear = (String) requestParams.get("fiscalyear");
            String parfrmdt = (String) requestParams.get("parfrmdt");
            String partodt = (String) requestParams.get("partodt");

            if (mode != null && mode.equals("absentee")) {
                pabs = parbrowserDao.getAbsenteeInfo(lub.getLoginempid(), id);
                parMast.setParid(pabs.getHidparid());
                mav.addObject("parAbsentee", pabs);
                mav.addObject("parfrmdt", parfrmdt);
                mav.addObject("partodt", partodt);
                path = "/par/NewAbsentee";
            } else if (mode != null && mode.equals("achievement")) {
                pach = parbrowserDao.getAchievementInfo(lub.getLoginempid(), id);
                pach.setHidpacid(id);
                parMast.setParid(pach.getHidparid());
                mav.addObject("parAchievement", pach);
                path = "/par/NewAchievement";
            }
            parMast.setFiscalyear(fiscalyear);
            mav.addObject("parMastForm", parMast);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "par/deleteAbsenteeorAchievement", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteAbsenteeorAchievement(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws JSONException, IOException {

        String msgType = "";
        String msg = "";

        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        String mode = "";
        int id = 0;
        String fiscalyear = "";
        try {
            mode = requestParams.get("mode");
            id = Integer.parseInt(requestParams.get("id"));
            fiscalyear = requestParams.get("fsclyr");

            if (mode != null && mode.equals("absentee")) {

                parbrowserDao.deleteAbsentee(lub.getLoginempid(), id);
                msg = "Absentee Deleted";
            } else if (mode != null && mode.equals("achievement")) {

                String filepath = context.getInitParameter("ParPath");
                parbrowserDao.deleteAchievement(lub.getLoginempid(), id, fiscalyear, filepath);
                msg = "Achievement Deleted";
            }
            msgType = "Info";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        obj.put("msg", msg);
        obj.put("msgType", msgType);
        out.write(obj.toString());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "par/DeleteAchievementAttachment.htm", method = RequestMethod.POST)
    public void DeleteAchievementAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws JSONException, IOException {
        int pacid = Integer.parseInt(requestParams.get("achId"));
        int attid = Integer.parseInt(requestParams.get("attachmentid"));
        String fiscalyear = requestParams.get("fiscalyr");

        String msgType = "";
        String msg = "";

        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();

        int retVal = 0;
        try {
            String filepath = context.getInitParameter("ParPath");
            retVal = parbrowserDao.deleteAchievementAttachment(lub.getLoginempid(), pacid, attid, fiscalyear, filepath);
            msgType = "Info";
            if (retVal > 0) {
                msg = "Achievement Attachment Deleted Successful";
            } else {
                msg = "Achievement Attachment Deletetion Failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        obj.put("msg", msg);
        obj.put("msgType", msgType);
        out.write(obj.toString());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "par/submitPAR", method = RequestMethod.GET)
    public ModelAndView submitPAR(@Valid
            @ModelAttribute("ParMastForm") ParMaster parMastForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {

        ModelAndView mav = new ModelAndView();
        String path = "";

        String parfrmdt = "";
        String partodt = "";
        int hierarchyno = 0;
        ParSubmitForm psubfrm = null;
        try {
            int parid = Integer.parseInt(requestParams.get("parid"));

            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parid);
            psubfrm = parbrowserDao.getAuthorityInfo(parid);
            String isAchievementPresent = parbrowserDao.isAchievementDataPresent(parid);
            String isOtherDetailsPresent = parbrowserDao.isOtherDetailsPresent(parid);

            parfrmdt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodfrom());
            partodt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodto());
            hierarchyno = parbrowserDao.getmaxhierachy(parMastForm.getParid(), parMastForm.getParstatus());

            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
            mav.addObject("parMastForm", parMastForm);
            mav.addObject("ParSubmitForm", psubfrm);
            mav.addObject("parfrmdt", parfrmdt);
            mav.addObject("partodt", partodt);
            mav.addObject("hierarchyno", hierarchyno);
            mav.addObject("isAchievementPresent", isAchievementPresent);
            mav.addObject("isOtherDetailsPresent", isOtherDetailsPresent);

            path = "/par/PARSubmit";
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "par/sendPAR", method = RequestMethod.POST)
    public ModelAndView sendPAR(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParSubmit") ParSubmitForm pf
    ) {

        String path = "/par/PARList";
        String year = "";
        ModelAndView mav = new ModelAndView();

        ParMaster parMastForm = null;
        ParSubmitForm psubfrm = null;

        String parfrmdt = "";
        String partodt = "";
        int hierarchyno = 0;
        String isClosed = "N";
        try {
            int parid = pf.getHidparid();
            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parid);
            String parType = parMastForm.getParType();
            if (pf.getHidparstatus() == 0) {
                //boolean isReverted = parbrowserDao.isPARReverted(parid, lub.getEmpid());
                //if (isReverted == false) {
                isClosed = parbrowserDao.isFiscalYearClosed(pf.getHidfiscalyear());

                /*if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                    isClosed = "N";
                }*/
                //}
            }
            if (isClosed != null && !isClosed.equals("")) {
                if (isClosed.equals("N")) {
                    boolean isParCreatedDateClosed = parbrowserDao.isParCreatedDateClosed(lub.getLoginempid(), parMastForm.getFiscalyear());
                    if (isParCreatedDateClosed == false || pf.getHidparstatus() == 16 || pf.getHidparstatus() == 18 || pf.getHidparstatus() == 19) {
                        /*This is a fix coding which will replace
                         String dateOfForceForwardFromReportingToReviewing = null;
                         if (pf.getHidfiscalyear().equals("2021-22")) {
                         dateOfForceForwardFromReportingToReviewing = "01-SEP-2022";
                         }*/
                        /*This is a fix coding which will replace
                         pf.setDateOfForceForwardFromReportingToReviewing(dateOfForceForwardFromReportingToReviewing);*/
                        String isInvalidPrd = parbrowserDao.sendPar(pf);

                        if (isInvalidPrd.equals("N")) {
                            //parbrowserDao.sendPar(pf);
                            year = fiscalDAO.getDefaultFiscalYear();
                            if (parType != null && parType.equals("SiPar") && !parType.equals("")) {
                                mav.addObject("HideBtn", "Yes");
                                path = "/par/PARListforSI";
                            } else {
                                path = "/par/PARList";
                            }
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                        } else {
                            mav.addObject("invalidperiod", isInvalidPrd);

                            psubfrm = parbrowserDao.getAuthorityInfo(parid);
                            parfrmdt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodfrom());
                            partodt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodto());
                            hierarchyno = parbrowserDao.getmaxhierachy(parMastForm.getParid(), parMastForm.getParstatus());

                            mav.addObject("parMastForm", parMastForm);
                            mav.addObject("ParSubmitForm", psubfrm);
                            mav.addObject("parfrmdt", parfrmdt);
                            mav.addObject("partodt", partodt);
                            mav.addObject("hierarchyno", hierarchyno);

                            path = "/par/PARSubmit";
                        }
                    } else {
                        mav.addObject("isParCreatedDateClosed", isParCreatedDateClosed);
                        path = "/par/PARSubmit";
                    }
                } else if (isClosed.equals("Y")) {
                    parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parid);
                    psubfrm = parbrowserDao.getAuthorityInfo(parid);

                    parfrmdt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodfrom());
                    partodt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodto());
                    hierarchyno = parbrowserDao.getmaxhierachy(parMastForm.getParid(), parMastForm.getParstatus());

                    mav.addObject("parMastForm", parMastForm);
                    mav.addObject("ParSubmitForm", psubfrm);
                    mav.addObject("parfrmdt", parfrmdt);
                    mav.addObject("partodt", partodt);
                    mav.addObject("hierarchyno", hierarchyno);
                    mav.addObject("isClosed", isClosed);

                    path = "/par/PARSubmit";

                }
            }
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /*@RequestMapping(value = "addPAR",params = "newAbsentee",method = RequestMethod.POST)
     public ModelAndView newAbsentee(@RequestParam String newAbsentee,@Valid @ModelAttribute("parAbsentee") ParAbsentee parabsenteeForm, @ModelAttribute("LoginUserBean") LoginUserBean lub){
     //return new ModelAndView("NewPAR", "command", new ParMaster());
        
     ModelAndView mav = new ModelAndView();
     String path = "";
        
     try{
     
     if(newAbsentee.equals("Save")){
     parbrowserDao.saveAbsentee(parabsenteeForm);
     path = "/par/AbsenteeList";
     }else{
     path = "/par/NewAbsentee";
     }
     }catch(Exception e){
     e.printStackTrace();
     }
     mav.setViewName(path);
     return mav;
     }*/
    @ResponseBody
    @RequestMapping(value = "GetAbsenteeListJSON", method = RequestMethod.POST)
    public void getabsenteelistJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("parid") int parid
    ) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;

        try {
            List absenteelist = parbrowserDao.getAbsenteeList(lub.getLoginempid(), parid);
            json.put("total", 10);
            json.put("rows", absenteelist);
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
    @RequestMapping(value = "GetAchievementListJSON", method = RequestMethod.POST)
    public void getachievementlistJSON(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("parid") int parid
    ) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            List achievementlist = parbrowserDao.getAchievementList(lub.getLoginempid(), parid);
            json.put("total", 10);
            json.put("rows", achievementlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetAbsenceCauseListJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getAbsenceCauseListJSON() {

        JSONArray json = null;
        try {
            List absencecauselist = parbrowserDao.getAbsenceCauseList();
            json = new JSONArray(absencecauselist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @RequestMapping(value = "GetLeaveTypeListJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getLeaveTypeListJSON() {

        JSONArray json = null;
        try {
            List absencecauselist = parbrowserDao.getLeaveTypeList();
            json = new JSONArray(absencecauselist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @RequestMapping(value = "getLeaveorTrainingListJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String getLeaveorTrainingListJSON(@RequestParam("leavecause") String leavecause
    ) {

        List leaveortraininglist = null;
        JSONArray json = null;
        try {
            if (leavecause != null && !leavecause.equals("")) {
                if (leavecause.equals("L")) {
                    leaveortraininglist = parbrowserDao.getLeaveTypeList();
                } else if (leavecause.equals("T")) {
                    leaveortraininglist = parbrowserDao.getTrainingypeList();
                }
            }
            json = new JSONArray(leaveortraininglist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "ChangePost", method = RequestMethod.GET)
    public String ChangePost() {

        String path = "/par/PostChange";
        return path;
    }

    @RequestMapping(value = "GetFiscalYearListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getFiscalYearListJSON(HttpServletResponse response
    ) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List fiscalyearlist = fiscalDAO.getFiscalYearList();
            json = new JSONArray(fiscalyearlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder
    ) {
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        //df.setLenient(false);
        binder.registerCustomEditor(Date.class, "periodfrom", new CustomDateEditor(df, true));
        binder.registerCustomEditor(Date.class, "periodto", new CustomDateEditor(df, true));
        //binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /*@RequestMapping(value = "viewPAR", method = RequestMethod.GET)
     public void viewPARPdf(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("parid") int parid
     ) {

     response.setContentType("application/pdf");
     Document document = null;

     ParDetail paf = null;

     PdfWriter writer = null;

     String htmlContent = new String();
     String str = null;
     try {
     document = new Document(PageSize.A4);

     response.setHeader("Content-Disposition", "attachment; filename=PAR_" + lub.getLoginempid() + ".pdf");
     writer = PdfWriter.getInstance(document, response.getOutputStream());
     writer.setPageEvent(new HeaderFooter(lub.getLoginempid()));
     document.open();

     URL pdfurl = new URL("http://par.hrmsodisha.gov.in/PARDetailPDF.htm?parid=" + parid);

     String sessionid = RequestContextHolder.getRequestAttributes().getSessionId();
            

     HttpURLConnection urlConn = null;
     urlConn = (HttpURLConnection) pdfurl.openConnection();
     urlConn.setDoOutput(true);
     urlConn.setDoInput(true);
     urlConn.setUseCaches(false);
     urlConn.setRequestProperty("Cookie", "JSESSIONID=" + sessionid);

     InputStreamReader fis = new InputStreamReader(urlConn.getInputStream());

     BufferedReader d = new BufferedReader(fis);

     while ((str = d.readLine()) != null) {
     htmlContent += str.trim();
     }
     StringReader strReader = new StringReader(StringUtils.defaultString(htmlContent));

     XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
     worker.parseXHtml(writer, document, strReader);

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     document.close();
     }
     }

     @RequestMapping(value = "PARDetailPDF", method = RequestMethod.GET)
     public ModelAndView PARDetailPDF(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid
     ) {

     String path = "";
     ModelAndView mav = new ModelAndView();
     ParDetail paf = null;
     try {
     boolean viewPAR = parbrowserDao.isAuthorizedtoDownloadPAR(lub.getLoginempid(), parid);
     if (viewPAR == true) {
     paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, 0, "");
     paf.setLoginId(lub.getLoginempid());
     }
     mav.addObject("pardetail", paf);

     path = "/par/PARDetailPDF";
     mav.setViewName(path);
     } catch (Exception e) {
     e.printStackTrace();
     }
     return mav;
     }*/
    /*
     @RequestMapping(value = "viewPAR")
     public void PARDetailPDF(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid) {

     response.setContentType("application/pdf");
     Document document = new Document(PageSize.A4);
        
     ParDetail paf = null;
     try {
     PdfWriter.getInstance(document, response.getOutputStream());
     document.open();
            
     boolean viewPAR = parbrowserDao.isAuthorizedtoDownloadPAR(lub.getLoginempid(), parid);
     if (viewPAR == true) {
     String filePath = context.getInitParameter("PhotoPath");
     paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, 0, "");
     paf.setLoginId(lub.getLoginempid());
     parbrowserDao.downloadPARDetailPDF(document,lub.getLoginempid(),paf,filePath);
     }
     } catch (Exception e) {
     e.printStackTrace();
     }finally{
     document.close();
     }
     }*/
    /*@RequestMapping(value = "viewPAR.htm", method = {RequestMethod.POST, RequestMethod.GET})
     public ModelAndView parPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid) {
     ModelAndView mv = new ModelAndView();
     String filePath = context.getInitParameter("PhotoPath");
     ParDetail paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, 0, "");
     mv.addObject("paf", paf);
     mv.addObject("filePath", filePath);
     mv.setViewName("parPdfViewApprise");
     return mv;
     } */
    @RequestMapping(value = "viewPAR.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView parPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") String encryptedparid, HttpServletRequest request
    ) {

        ModelAndView mv = new ModelAndView();
        try {
            Integer parid = new Integer(CommonFunctions.decodedTxt(encryptedparid));
            String filePath = context.getInitParameter("PhotoPath");
            ParDetail paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, 0, "");
            String qrCodePathPAR = context.getInitParameter("ParQRCodePath");
            paf.setIpAddress(CommonFunctions.getIpAndHost(request));
            mv.addObject("paf", paf);
            mv.addObject("filePath", filePath);
            mv.addObject("qrCodePathPAR", qrCodePathPAR);

            String parFilePath = qrCodePathPAR;

            String storedpath = qrCodePathPAR + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            String qrCodeurl = storedpath + paf.getApplicantempid() + "-" + paf.getParid() + ".png";

            File fi = null;
            fi = new File(qrCodeurl);
            Image img2 = null;

            if (paf.getParstatus() == 9 && !fi.exists()) {
                parbrowserDao.generateQRCodeForPreviousYearPAR(paf.getParid(), qrCodePathPAR);
            }

            mv.setViewName("parPdfViewApprise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "par/PARDetailView", method = RequestMethod.GET)
    public ModelAndView PARDetailView(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") String encryptedParid,
            @RequestParam("taskid") int taskid, @RequestParam("auth") String auth
    ) {
        Integer parid = 0;
        if (encryptedParid != null && !encryptedParid.equals("0")) {
            parid = new Integer(CommonFunctions.decodedTxt(encryptedParid));
        }
        String path = "";
        ModelAndView mav = new ModelAndView();
        ParDetail paf = null;

        try {
            if (parid == 0) {
                parid = parbrowserDao.getParid(lub.getLoginempid(), taskid);
            }
            boolean viewPAR = parbrowserDao.isAuthorizedtoDownloadPAR(lub.getLoginempid(), parid);
            if (viewPAR == true) {
                paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, taskid, auth);

                if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                    paf.setIsClosedFiscalYearAuthority("N");
                }
                paf.setLoginId(lub.getLoginempid());
            }
            List templatelist = parbrowserDao.getPARTemplateList("GA", lub.getLoginempid());
            List templatelistForIntregityNote = parbrowserDao.getPARTemplateList("Intregity Note", lub.getLoginempid());
            List templatelistForgrading = parbrowserDao.getPARTemplateList("Grading Note", lub.getLoginempid());
            List templatelistForAdverse = parbrowserDao.getPARTemplateList("Adverse Note", lub.getLoginempid());
            List templatelistForFiveTDepartment = parbrowserDao.getPARTemplateList("FiveTDepartment Note", lub.getLoginempid());
            List templatelistForFiveTGovernment = parbrowserDao.getPARTemplateList("FiveTGovernment Note", lub.getLoginempid());
            List templatelistForMoSarkar = parbrowserDao.getPARTemplateList("FiveTMoSarkar Note", lub.getLoginempid());
            List templatelistForReviewing = parbrowserDao.getPARTemplateList("Reviewing Note", lub.getLoginempid());
            List templatelistForAccepting = parbrowserDao.getPARTemplateList("Accepting Note", lub.getLoginempid());
            List gradelist = parbrowserDao.getPARGradeList();
            mav.addObject("gradelist", gradelist);

            mav.addObject("pardetail", paf);
            mav.addObject("encodedparid", CommonFunctions.encodedTxt(paf.getParid() + ""));
            mav.addObject("authnote", templatelist);
            mav.addObject("integrityNote", templatelistForIntregityNote);
            mav.addObject("gradingNote", templatelistForgrading);
            mav.addObject("inadequaciesNote", templatelistForAdverse);
            mav.addObject("fiveTChartertenpercent", templatelistForFiveTDepartment);
            mav.addObject("fiveTCharterfivePercent", templatelistForFiveTGovernment);
            mav.addObject("fiveTComponentmoSarkar", templatelistForMoSarkar);
            mav.addObject("reviewingNote", templatelistForReviewing);
            mav.addObject("acceptingNote", templatelistForAccepting);
            if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
                path = "/par/PARDetailViewForSI";
                mav.addObject("RepotClose", "No");
            } else {
                path = "/par/PARDetailView";
            }
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "par/PARDetailView1", method = RequestMethod.GET)
    public ModelAndView PARDetailView1(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") String encryptedParid,
            @RequestParam("taskid") int taskid, @RequestParam("auth") String auth
    ) {

        String path = "";
        ModelAndView mav = new ModelAndView();
        ParDetail paf = null;
        Integer parid = 0;
        if (encryptedParid != null && !encryptedParid.equals("0")) {
            parid = new Integer(CommonFunctions.decodedTxt(encryptedParid));
        }
        try {
            if (parid == 0) {
                parid = parbrowserDao.getParid(lub.getLoginempid(), taskid);
            }
            boolean viewPAR = parbrowserDao.isAuthorizedtoDownloadPAR(lub.getLoginempid(), parid);
            if (viewPAR == true) {
                paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parid, taskid, auth);
                if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                    paf.setIsClosedFiscalYearAuthority("N");
                }
                paf.setLoginId(lub.getLoginempid());
            }
            List templatelist = parbrowserDao.getPARTemplateList("GA", lub.getLoginempid());
            List templatelistForIntregityNote = parbrowserDao.getPARTemplateList("Intregity Note", lub.getLoginempid());
            List templatelistForgrading = parbrowserDao.getPARTemplateList("Grading Note", lub.getLoginempid());
            List templatelistForAdverse = parbrowserDao.getPARTemplateList("Adverse Note", lub.getLoginempid());
            List templatelistForFiveTDepartment = parbrowserDao.getPARTemplateList("FiveTDepartment Note", lub.getLoginempid());
            List templatelistForFiveTGovernment = parbrowserDao.getPARTemplateList("FiveTGovernment Note", lub.getLoginempid());
            List templatelistForMoSarkar = parbrowserDao.getPARTemplateList("FiveTMoSarkar Note", lub.getLoginempid());
            List templatelistForReviewing = parbrowserDao.getPARTemplateList("Reviewing Note", lub.getLoginempid());
            List templatelistForAccepting = parbrowserDao.getPARTemplateList("Accepting Note", lub.getLoginempid());
            List gradelist = parbrowserDao.getPARGradeList();
            mav.addObject("gradelist", gradelist);

            mav.addObject("pardetail", paf);
            mav.addObject("encodedparid", CommonFunctions.encodedTxt(paf.getParid() + ""));
            mav.addObject("authnote", templatelist);
            mav.addObject("integrityNote", templatelistForIntregityNote);
            mav.addObject("gradingNote", templatelistForgrading);
            mav.addObject("inadequaciesNote", templatelistForAdverse);
            mav.addObject("fiveTChartertenpercent", templatelistForFiveTDepartment);
            mav.addObject("fiveTCharterfivePercent", templatelistForFiveTGovernment);
            mav.addObject("fiveTComponentmoSarkar", templatelistForMoSarkar);
            mav.addObject("reviewingNote", templatelistForReviewing);
            mav.addObject("acceptingNote", templatelistForAccepting);
            path = "/par/PARDetailOnlyViewForSI";
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "saveCustomForAuthNote.htm", method = RequestMethod.POST)
    public void saveCustomForAuthNote(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parTemplateBean") PARTemplateBean parTemplateBean) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        parTemplateBean.setHrmsId(lub.getLoginempid());
        parbrowserDao.saveCustomForAuthNote(parTemplateBean);

    }

    @ResponseBody
    @RequestMapping(value = "getloginWisesaveTemplateDetail.htm")
    public void geteOfficeWiseAssignPrivilegeDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parTemplateBean") PARTemplateBean parTemplateBean) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList loginwisetemplateList = parbrowserDao.getloginWiseSavedTemplateList(parTemplateBean.getTemplateHeading(), lub.getLoginempid());

        JSONArray json = new JSONArray(loginwisetemplateList);
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    @ResponseBody
    @RequestMapping(value = "removeloginWisesaveTemplateDetail.htm")
    public void removeloginWisesaveTemplateDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("parTemplateBean") PARTemplateBean parTemplateBean) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        parbrowserDao.deleteloginWiseSavedTemplateList(parTemplateBean.getTemplateId());
        JSONObject jobj = new JSONObject();
        jobj.append("msg", "Y");
        out.write(jobj.toString());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "par/forwardPAR", method = RequestMethod.POST)
    public ModelAndView forwardPAR(@ModelAttribute("parDetail") ParDetail parDetail, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("forwardpar") String forwardpar
    ) {
        String path = "";
        ParDetail paf = null;
        ModelAndView mav = new ModelAndView();
        String isClosed = "N";

        try {
            isClosed = parbrowserDao.isAuthRemarksClosed(parDetail.getFiscalYear());
            List gradelist = parbrowserDao.getPARGradeList();
            mav.addObject("gradelist", gradelist);
            if (isClosed != null && !isClosed.equals("")) {
                if (isClosed.equals("N")) {
                    String gradingPath = context.getInitParameter("ParGradingPath");
                    parbrowserDao.saveAndForwardPAR(parDetail, lub.getLoginempid(), forwardpar, gradingPath);

                    if (forwardpar.equals("Submit")) {
                        path = "/par/PARRemarksDone";
                    } else {
                        paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parDetail.getParid(), parDetail.getTaskid(), null);
                        mav.addObject("pardetail", paf);
                        if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
                            path = "/par/PARDetailViewForSI";
                            mav.addObject("RepotClose", "No");
                        } else {
                            path = "/par/PARDetailView";
                        }
                    }
                } else if (isClosed.equals("Y")) {
                    paf = parbrowserDao.getPARDetails(lub.getLoginempid(), parDetail.getParid(), parDetail.getTaskid(), null);
                    mav.addObject("pardetail", paf);
                    if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
                        path = "/par/PARDetailViewForSI";
                    } else {
                        path = "/par/PARDetailView";
                    }
                    mav.addObject("isClosed", isClosed);
                }
            }
            mav.addObject("encodedparid", CommonFunctions.encodedTxt(parDetail.getParid() + ""));
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "closeAuthorityRemarks", params = {"action=Close"})
    public void closeAuthorityRemarks(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parTemplateBean") PARTemplateBean parTemplateBean, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:login.htm");
    }

    @ResponseBody
    @RequestMapping(value = "GetPARGradeListJSON.htm")
    public void GetPARGradeListJSON(HttpServletResponse response
    ) {
        JSONArray json = null;
        PrintWriter out = null;
        response.setContentType("application/json");
        try {
            List gradelist = parbrowserDao.getPARGradeList();
            json = new JSONArray(gradelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AcceptingAuthRemarksPage", method = RequestMethod.GET)
    public ModelAndView AcceptingAuthRemarksPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskid") int taskid
    ) {

        String path = "/par/AcceptingAuthRemarks";
        ModelAndView mav = null;

        try {
            mav = new ModelAndView();

            int parid = parbrowserDao.getParid(lub.getLoginempid(), taskid);

            mav.addObject("parid", parid);
            mav.addObject("taskid", taskid);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "AcceptingAuthRemarksSave", method = {RequestMethod.GET, RequestMethod.POST})
    public void AcceptingAuthRemarksSave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws JSONException, IOException {

        int parid = Integer.parseInt(requestParams.get("parid"));
        int taskid = Integer.parseInt(requestParams.get("taskid"));
        String acceptingAuthRemarks = requestParams.get("acceptingAuthRemarks");

        String msgType = "";
        String msg = "";
        try {
            int status = parbrowserDao.saveAcceptingAuthRemarksFromCombo(lub.getLoginempid(), parid, taskid, acceptingAuthRemarks);

            if (status > 0) {
                msgType = "Info";
                msg = "Accepted";
            } else {
                msgType = "Error";
                msg = "Error Occured!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        JSONObject obj = new JSONObject();
        obj.put("msg", msg);
        obj.put("msgType", msgType);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "GetNRCReasonListJSON")
    public @ResponseBody
    String getNRCReasonListJSON(ModelMap model, HttpServletRequest request
    ) {
        JSONArray json = null;
        try {
            List nrcreasonlist = parbrowserDao.getNRCReasonList();
            json = new JSONArray(nrcreasonlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @RequestMapping(value = "par/addNRC", method = RequestMethod.POST)
    public ModelAndView saveNRC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParMastForm") ParMaster parMastForm, @RequestParam("newNRC") String newNRC
    ) {

        String path = "";
        ModelAndView mav = null;
        boolean duplPeriod = false;
        Users emp = null;

        try {
            mav = new ModelAndView();
            String parType = lub.getParType();
            if (!newNRC.equals("Submit for NRC")) {
                switch (newNRC) {
                    case "Back":
                        if (parType != null && parType.equals("SiPar") && !parType.equals("")) {
                            mav.addObject("HideBtn", "Yes");
                            path = "/par/PARListforSI";
                        } else {
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                            path = "/par/PARList";
                        }
                        break;
                    case "Delete":
                        String isClosed = parbrowserDao.isFiscalYearClosed(parMastForm.getFiscalyear());
                        mav.addObject("isClosed", isClosed);
                        if (isClosed.equalsIgnoreCase("N")) {
                            parbrowserDao.deleteNRC(Integer.parseInt(parMastForm.getHidparid()), parMastForm.getEmpid());
                        }
                        if (parType != null && parType.equals("SiPar") && !parType.equals("")) {
                            mav.addObject("HideBtn", "Yes");
                            path = "/par/PARListforSI";
                        } else {
                            mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                            path = "/par/PARList";
                        }
                        break;
                }
            } else {

                String parfrmdt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodfrom());
                String partodt = CommonFunctions.getFormattedOutputDate1(parMastForm.getPeriodto());
                duplPeriod = parbrowserDao.isDuplicatePARPeriod(lub.getLoginempid(), parfrmdt, partodt, parMastForm.getFiscalyear(), parMastForm.getHidparid());
                boolean iscorrectPeriod = parbrowserDao.isCheckParPeriod(parfrmdt, partodt, parMastForm.getFiscalyear());
                String isClosed = parbrowserDao.isFiscalYearClosed(parMastForm.getFiscalyear());
                mav.addObject("isClosed", isClosed);
                String cadreCode = parMastForm.getCadreCode();
                if (duplPeriod == false) {
                    emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                    mav.addObject("users", emp);
                    mav.addObject("NRCError", "NRC Period is duplicate.");
                    path = "/par/CreateNRC";
                    parMastForm.setFiscalyear(parMastForm.getFiscalyear());
                } else if (cadreCode == null || cadreCode.equals("")) {
                    emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
                    mav.addObject("users", emp);
                    mav.addObject("NRCError", "Service to which the officer belongs cannot be blank.");
                    path = "/par/CreateNRC";
                } else {
                    String filepath = context.getInitParameter("ParPath");
                    parMastForm.setParstatus(17);
                    if (iscorrectPeriod == true && isClosed.equalsIgnoreCase("N")) {
                        parbrowserDao.savePAR(1, parMastForm, null, filepath);

                        mav.addObject("defaultfiscalyear", parMastForm.getFiscalyear());
                        if (parType.equals("SiPar")) {
                            mav.addObject("HideBtn", "Yes");
                        }
                        path = "/par/PARList";
                    } else {
                        mav.addObject("NRCPeriodError", "Incorrect NRC Period.");
                        path = "/par/CreateNRC";
                    }
                }
            }
            //String year = (String)context.getAttribute("selectedyear");

            mav.addObject("parMastForm", parMastForm);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "par/NRCDetailView", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editNRC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid
    ) {

        ModelAndView mav = null;

        ParMaster parMastForm = null;
        Users emp = null;

        String path = "/par/CreateNRC";
        try {
            mav = new ModelAndView();

            parMastForm = parbrowserDao.getAppraiseInfo(lub.getLoginempid(), parid);
            String nrcattch = parbrowserDao.getNRCAttachedFileName(lub.getLoginempid(), parid);
            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
            String isClosed = parbrowserDao.isFiscalYearClosed(parMastForm.getFiscalyear());
            mav.addObject("isClosed", isClosed);
            mav.addObject("parMastForm", parMastForm);
            mav.addObject("users", emp);
            mav.addObject("nrcattch", nrcattch);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ViewNRCPDF", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewNRCPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid
    ) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        ParDetail paf = null;
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            paf = parbrowserDao.getNRCDetails(lub.getLoginempid(), parid);
            parbrowserDao.viewNRCPDFfunc(document, paf, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "DownloadAchievementAttachment", method = RequestMethod.GET)
    public void downloadachievementattachment(HttpServletResponse response, @RequestParam("attId") int attId, @RequestParam("fiscalyr") String fiscalyr
    ) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("ParPath");
            fa = parbrowserDao.downloadachievementattachment(filepath, attId, fiscalyr);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "DownloadNRCAttch", method = RequestMethod.GET)
    public void downloadNRCAttachment(HttpServletResponse response, @RequestParam Map<String, String> requestParams
    ) {

        int parid = Integer.parseInt(requestParams.get("parId"));
        String fiscalyear = requestParams.get("fyear");

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            String filepath = context.getInitParameter("ParPath");
            fa = parbrowserDao.downloadNRCAttachment(parid, fiscalyear, filepath);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "par/RevertPAR", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView revertPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams
    ) {

        ModelAndView mav = null;
        String path = "/par/RevertPAR";

        ParDetail pdtl = new ParDetail();

        int parid = Integer.parseInt(requestParams.get("parId"));
        int parstatus = Integer.parseInt(requestParams.get("parStatus"));
        int taskid = Integer.parseInt(requestParams.get("taskId"));
        String isreportingcompleted = requestParams.get("isreportingcompleted");
        String fiscalyear = requestParams.get("fiscalyr");
        try {
            mav = new ModelAndView();
            pdtl.setParid(parid);
            //pdtl.setEncryptedparid(parid);
            pdtl.setParstatus(parstatus);
            pdtl.setTaskid(taskid);
            pdtl.setIsreportingcompleted(isreportingcompleted);
            pdtl.setFiscalYear(fiscalyear);

            mav.addObject("ParDetail", pdtl);

            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "RevertSave", method = RequestMethod.POST)
    public ModelAndView revertSave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parDetail") ParDetail parDetail
    ) {

        ModelAndView mav = null;
        String path = "/par/RevertPAR";

        String isReverted = "N";
        String isClosed = "N";
        try {
            mav = new ModelAndView();
            isClosed = parbrowserDao.isAuthRemarksClosed(parDetail.getFiscalYear());

            if (isClosed != null && !isClosed.equals("")) {
                if (isClosed.equals("N")) {
                    isReverted = parbrowserDao.revertPAR(lub.getLoginempid(), parDetail);
                    mav.addObject("revertstatus", isReverted);
                } else if (isClosed.equals("Y")) {
                    mav.addObject("isClosed", isClosed);
                }
            }
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "par/RevertReason", method = RequestMethod.GET)
    public ModelAndView RevertReason(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parid") int parid
    ) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView();
            int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), parid);
            String[] revertreason = parbrowserDao.getRevertReason(parid, taskid);
            mav.addObject("authorityType", revertreason[0]);
            mav.addObject("revertRemaeks", revertreason[1]);
            mav.addObject("authorityName", revertreason[2]);
            mav.addObject("revertOnDate", revertreason[3]);
            mav.setViewName("/par/RevertReason");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "IsClosedFinYr", method = RequestMethod.GET)
    public void IsClosedFinYr(HttpServletResponse response, @RequestParam("fslyr") String fyear
    ) {

        response.setContentType("text/html");
        try {
            String isClosed = parbrowserDao.isFiscalYearClosed(fyear);

            PrintWriter out = response.getWriter();
            out = response.getWriter();
            out.write(isClosed.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "AddCadre", method = RequestMethod.GET)
    public String AddCadre() {

        String path = "/par/AddCadre";
        return path;
    }

    @RequestMapping(value = "ParReport")
    public String parReport(Model model, @RequestParam("fiscalyear") String fiscalyear
    ) {

        List parreportlist = null;
        PARReportBean prbean = null;

        int totalpar = 0;
        int totalparwithoutspc = 0;
        int totalreportingpendingpar = 0;
        int totalreviewingpendingpar = 0;
        int totalacceptingpendingpar = 0;
        int totalcompleted = 0;
        try {
            if (fiscalyear != null && !fiscalyear.equals("")) {
                parreportlist = parbrowserDao.getPARReport(fiscalyear);
            }

            if (parreportlist != null && parreportlist.size() > 0) {
                for (int i = 0; i < parreportlist.size(); i++) {
                    prbean = (PARReportBean) parreportlist.get(i);
                    totalpar = totalpar + prbean.getParapplied();
                    totalreportingpendingpar = totalreportingpendingpar + prbean.getReportingpending();
                    totalreviewingpendingpar = totalreviewingpendingpar + prbean.getReviewingpending();
                    totalacceptingpendingpar = totalacceptingpendingpar + prbean.getAcceptingpending();
                    totalcompleted = totalcompleted + prbean.getCompleted();
                }
            }
            model.addAttribute("fiscalyear", fiscalyear);
            model.addAttribute("PARReportList", parreportlist);
            model.addAttribute("NOOFPAR", totalpar);
            model.addAttribute("REPORTINGPENDING", totalreportingpendingpar);
            model.addAttribute("REVIEWINGPENDING", totalreviewingpendingpar);
            model.addAttribute("ACCEPTINGPENDING", totalacceptingpendingpar);
            model.addAttribute("COMPLETED", totalcompleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/par/PARStatusReport";
    }

    @RequestMapping(value = "par/initiateOtherPAR")
    public ModelAndView initiateOtherPAR(@ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {
        ModelAndView mav = null;

        try {
            mav = new ModelAndView("/par/InitiatePARForOthers", "initiateOtherPARForm", initiateOtherPARForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "par/initiateOtherPAR", params = "btnInitiateOtherPAR=Get List")
    public ModelAndView getEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("/par/InitiatePARForOthers", "initiateOtherPARForm", initiateOtherPARForm);
            initiateOtherPARForm.setFiscalYear(initiateOtherPARForm.getFiscalYear());
            //mav.addObject("fiscalyear", initiateOtherPARForm.getFiscalYear());

            ArrayList emplist = parbrowserDao.getEmployeeListforInitiateOtherPAR(initiateOtherPARForm.getSltOffice(), initiateOtherPARForm.getFiscalYear());
            mav.addObject("emplist", emplist);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);

            List offlist = offDAO.getEmployeePostedOfficeList(initiateOtherPARForm.getSltDept(), lub.getLoginempid(), lub.getLoginoffcode());
            List cadrelist = cadreDAO.getCadreList(initiateOtherPARForm.getDeptcode());
            mav.addObject("cadrelist", cadrelist);
            //       mav.addObject("offlist", offlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "initiateOtherPARRemarks", params = "forwardpar=Back")
    public ModelAndView Back(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {

        String fiscalyear = initiateOtherPARForm.getFiscalYear();
        initiateOtherPARForm.setFiscalYear(fiscalyear);
        ModelAndView mav = new ModelAndView("/par/InitiatePARForOthers", "initiateOtherPARForm", initiateOtherPARForm);

        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);

        return mav;
    }

    @RequestMapping(value = "initiateOtherPARRemarks")
    public ModelAndView initiateOtherPARRemarks(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {

        ModelAndView mav = null;

        ParDetail paf = null;
        try {
            mav = new ModelAndView("/par/InititatePARForOthersRemarks", "initiateOtherPARForm", initiateOtherPARForm);

            String[] authEmpid = new String[1];
            authEmpid[0] = lub.getLoginempid();

            String[] authSpc = new String[1];
            authSpc[0] = lub.getLoginspc();

            initiateOtherPARForm.setHidReportingEmpId(authEmpid);
            initiateOtherPARForm.setHidReportingSpcCode(authSpc);
            initiateOtherPARForm.setTxtReportingAuth(parbrowserDao.getEmployeeName(lub.getLoginempid()));

            mav.addObject("appraiseename", parbrowserDao.getEmployeeName(initiateOtherPARForm.getAppraiseeEmpId()));
            String parId = parbrowserDao.isInitiateOtherDuplicatePAR(initiateOtherPARForm);
            mav.addObject("parId", parId);
            List cadrelist = cadreDAO.getCadreList(initiateOtherPARForm.getDeptcode());
            List deptlist = deptDAO.getDepartmentList();

            List templatelist = parbrowserDao.getPARTemplateList("GA", lub.getLoginempid());
            List templatelistForIntregityNote = parbrowserDao.getPARTemplateList("Intregity Note", lub.getLoginempid());
            List templatelistForgrading = parbrowserDao.getPARTemplateList("Grading Note", lub.getLoginempid());
            List templatelistForAdverse = parbrowserDao.getPARTemplateList("Adverse Note", lub.getLoginempid());
            List templatelistForFiveTDepartment = parbrowserDao.getPARTemplateList("FiveTDepartment Note", lub.getLoginempid());
            List templatelistForFiveTGovernment = parbrowserDao.getPARTemplateList("FiveTGovernment Note", lub.getLoginempid());
            List templatelistForMoSarkar = parbrowserDao.getPARTemplateList("FiveTMoSarkar Note", lub.getLoginempid());
            List templatelistForReviewing = parbrowserDao.getPARTemplateList("Reviewing Note", lub.getLoginempid());
            List templatelistForAccepting = parbrowserDao.getPARTemplateList("Accepting Note", lub.getLoginempid());

            mav.addObject("cadrelist", cadrelist);
            mav.addObject("deptlist", deptlist);
            mav.addObject("authnote", templatelist);
            mav.addObject("integrityNote", templatelistForIntregityNote);
            mav.addObject("gradingNote", templatelistForgrading);
            mav.addObject("inadequaciesNote", templatelistForAdverse);
            mav.addObject("fiveTChartertenpercent", templatelistForFiveTDepartment);
            mav.addObject("fiveTCharterfivePercent", templatelistForFiveTGovernment);
            mav.addObject("fiveTComponentmoSarkar", templatelistForMoSarkar);
            mav.addObject("reviewingNote", templatelistForReviewing);
            mav.addObject("acceptingNote", templatelistForAccepting);

            /* Written Previously for update against the PAR that Appraisee Created but not Submitted
            
             if (!parId.equals("Y") && !parId.equals("N")) {
             System.out.println("parid not yes and no !!!!");
             initiateOtherPARForm.setParid(Integer.parseInt(parId));
             } else {
             System.out.println("parid neither yes nor no @@@@");
             if (parId.equals("Y")) {
             SelectOption partialPeriodList = parbrowserDao.getPartialPARPeriod(initiateOtherPARForm.getAppraiseeEmpId(), initiateOtherPARForm.getFiscalYear());
             //parCreated = parAdminDAO.is
             if (partialPeriodList.getValue() != null && !partialPeriodList.getValue().equals("")) {
             mav.addObject("partialFromDate", partialPeriodList.getValue());
             mav.addObject("partialToDate", partialPeriodList.getLabel());
             }
             }
             } */
            paf = parbrowserDao.getPARDetails(initiateOtherPARForm.getAppraiseeEmpId(), -1, -1, "");
            mav.addObject("pardetail", paf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "par/initiateOtherPARRemarks", params = "forwardpar=Save")
    public ModelAndView initiateOtherPARRemarksSave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {
        ModelAndView mav = null;
        ParDetail paf = null;
        try {
            mav = new ModelAndView("/par/InititatePARForOthersRemarks", "initiateOtherPARForm", initiateOtherPARForm);

            //parbrowserDao.saveInitiateOtherPARRemarks(initiateOtherPARForm);
            paf = parbrowserDao.getPARDetails(initiateOtherPARForm.getAppraiseeEmpId(), 0, 0, "");
            mav.addObject("pardetail", paf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "par/initiateOtherPARRemarks", params = "forwardpar=Submit")
    public ModelAndView initiateOtherPARRemarksSubmit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm
    ) {
        ModelAndView mav = null;
        try {
            mav = new ModelAndView("/par/PARRemarksDone", "initiateOtherPARForm", initiateOtherPARForm);
            String parfrmdt = initiateOtherPARForm.getFromDate();
            String partodt = initiateOtherPARForm.getToDate();
            boolean isDuplicatePeriodotherPAR = parbrowserDao.isDuplicatePARPeriodForInitiateOtherPAR(initiateOtherPARForm.getAppraiseeEmpId(), parfrmdt, partodt, initiateOtherPARForm.getFiscalYear(), Integer.toString(initiateOtherPARForm.getParid()));
            if (isDuplicatePeriodotherPAR == true) {
                /* Written before to insert data against the parid that appraisee created but not submitted 
                 int parid = parbrowserDao.saveInitiateOtherPARRemarks(lub.getLoginempid(), lub.getLoginspc(), initiateOtherPARForm);
                 initiateOtherPARForm.setParid(parid); 
                 */
                int parid = parbrowserDao.saveInitiateOtherPARRemarks(lub.getLoginempid(), lub.getLoginspc(), initiateOtherPARForm);
                initiateOtherPARForm.setParid(parid);
                parbrowserDao.submitInitiateOtherPar(lub.getLoginempid(), lub.getLoginspc(), initiateOtherPARForm);
                mav = new ModelAndView("/par/PARRemarksDone", "initiateOtherPARForm", initiateOtherPARForm);
            } else {
                initiateOtherPARForm.setErrmsg("Duplicate Period");
                mav = new ModelAndView("/par/InititatePARForOthersRemarks", "initiateOtherPARForm", initiateOtherPARForm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    /* @RequestMapping(value = "par/initiateOtherPARRemarks", params = "forwardpar=Submit")
     public ModelAndView initiateOtherPARRemarksSubmit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("initiateOtherPARForm") InitiateOtherPARForm initiateOtherPARForm) {

     ModelAndView mav = null;

     try {
     mav = new ModelAndView("/par/PARRemarksDone", "initiateOtherPARForm", initiateOtherPARForm);

     int parid = parbrowserDao.saveInitiateOtherPARRemarks(lub.getLoginempid(), lub.getLoginspc(), initiateOtherPARForm);

     initiateOtherPARForm.setParid(parid);

     parbrowserDao.submitInitiateOtherPar(lub.getLoginempid(), lub.getLoginspc(), initiateOtherPARForm);

     //paf = parbrowserDao.getPARDetails(initiateOtherPARForm.getAppraiseeEmpId(), 0, 0, "");
     //mav.addObject("pardetail", paf);
     } catch (Exception e) {
     e.printStackTrace();
     }
     return mav;
     } */

    @RequestMapping(value = "PARMaintenance", method = RequestMethod.GET)
    public ModelAndView getPARMaintenance(HttpServletResponse response
    ) {

        ModelAndView mav = new ModelAndView();
        String path = "/par/PARMaintenance";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "par/getPARFiscalYearOpenStatus")
    public String getPARFiscalYearOpenStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("fiscalyear") String fiscalyear
    ) {
        JSONObject json = new JSONObject();
        try {
            String isClosed = parbrowserDao.isFiscalYearClosed(fiscalyear);
            if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                isClosed = "N";
            }
            json.put("isClosed", isClosed);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "par/getSiPARFiscalYearOpenStatus")
    public String getSiPARFiscalYearOpenStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("fiscalyear") String fiscalyear
    ) {
        JSONObject json = new JSONObject();
        try {
            String isClosed = parbrowserDao.isFiscalYearClosed4Si(fiscalyear);
            if (lub.getLoginoffcode() != null && lub.getLoginoffcode().equals("OLSGAD0010001")) {
                isClosed = "N";
            }
            json.put("isClosed", isClosed);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "DownloadGradingAttchmentReporting")
    public void DownloadGradingAttchmentReporting(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("reportingHelperBean") ReportingHelperBean reportingHelperBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String gradingPath = context.getInitParameter("ParGradingPath");
        reportingHelperBean = parbrowserDao.DownloadGradingAttchmentReporting(reportingHelperBean.getParId(), reportingHelperBean.getPrtid(), gradingPath);
        response.setContentType(reportingHelperBean.getFileTypeforgradingDocumentReporting());
        response.setHeader("Content-Disposition", "attachment;filename=" + reportingHelperBean.getOriginalFileNamegradingDocumentReporting());
        OutputStream out = response.getOutputStream();
        out.write(reportingHelperBean.getFilecontent());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removeGradingAttachmentReporting.htm")
    public void removeGradingAttachmentReporting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("reportingHelperBean") ReportingHelperBean reportingHelperBean) throws Exception {
        response.setContentType("application/json");
        parbrowserDao.deleteGradingAttachmentReporting(reportingHelperBean);
        JSONObject obj = null;
        String deletestatus = "Y";
        obj = new JSONObject();
        obj.append("deletestatus", deletestatus);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "DownloadGradingAttchmentReviewing")
    public void DownloadGradingAttchmentReviewing(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("reviewingHelperBean") ReviewingHelperBean reviewingHelperBean,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String gradingPath = context.getInitParameter("ParGradingPath");
        reviewingHelperBean = parbrowserDao.DownloadGradingAttchmentReviewing(reviewingHelperBean.getParId(), reviewingHelperBean.getPrtid(), gradingPath);
        response.setContentType(reviewingHelperBean.getFileTypeforgradingDocumentReviewing());
        response.setHeader("Content-Disposition", "attachment;filename=" + reviewingHelperBean.getOriginalFileNamegradingDocumentReviewing());
        OutputStream out = response.getOutputStream();
        out.write(reviewingHelperBean.getFilecontent());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removeGradingAttachmentReviewing.htm")
    public void removeGradingAttachmentReviewing(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("reviewingHelperBean") ReviewingHelperBean reviewingHelperBean) throws Exception {
        response.setContentType("application/json");
        parbrowserDao.deleteGradingAttachmentReviewing(reviewingHelperBean);
        JSONObject obj = null;
        String deletestatus = "Y";
        obj = new JSONObject();
        obj.append("deletestatus", deletestatus);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "DownloadGradingAttchmentAccepting")
    public void DownloadGradingAttchmentAccepting(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("acceptingHelperBean") AcceptingHelperBean acceptingHelperBean,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String gradingPath = context.getInitParameter("ParGradingPath");
        acceptingHelperBean = parbrowserDao.DownloadGradingAttchmentAccepting(acceptingHelperBean.getParId(), acceptingHelperBean.getPactid(), gradingPath);
        response.setContentType(acceptingHelperBean.getFileTypeforgradingDocumentAccepting());
        response.setHeader("Content-Disposition", "attachment;filename=" + acceptingHelperBean.getOriginalFileNamegradingDocumentAccepting());
        OutputStream out = response.getOutputStream();
        out.write(acceptingHelperBean.getFilecontent());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removeGradingAttachmentAccepting.htm")
    public void removeGradingAttachmentAccepting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("acceptingHelperBean") AcceptingHelperBean acceptingHelperBean) throws Exception {
        response.setContentType("application/json");
        parbrowserDao.deleteGradingAttachmentAccepting(acceptingHelperBean);
        JSONObject obj = null;
        String deletestatus = "Y";
        obj = new JSONObject();
        obj.append("deletestatus", deletestatus);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

}

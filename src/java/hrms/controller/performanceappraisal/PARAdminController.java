/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARBrowserDAO;
import hrms.dao.preferauthority.SactionedAuthorityDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.loanworkflow.IFMSAuthObject;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Cadre;
import hrms.model.master.Module;
import hrms.model.parmast.DistrictWiseAuthorizationForPolice;
import hrms.model.parmast.InitiateOtherPARListBean;
import hrms.model.parmast.PARMessageCommunication;
import hrms.model.parmast.PARSearchResult;
import hrms.model.parmast.PARViewDetailLogBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParAdminProperties;
import hrms.model.parmast.ParAdminSearchCriteria;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.parmast.ParDeleteDetailBean;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.ParForceForwardBean;
import hrms.model.parmast.ParMasterBean;
import hrms.model.parmast.ParNrcAttachment;
import hrms.model.parmast.ParStatusBean;
import hrms.model.parmast.ParUnReviewedDetailBean;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.UploadPreviousPAR;
import hrms.model.task.TaskListHelperBean;
import hrms.service.SendSMS;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author DurgaPrasad
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj", "SelectedEmpOffice", "TreasuryDtls", "users", "parreviewedspc"})
public class PARAdminController implements ServletContextAware {

    @Autowired
    PARAdminDAO parAdminDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    CadreDAO cadreDAO;

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    DepartmentDAO deptDAO;

    @Autowired
    public SactionedAuthorityDAO sactionedAuthorityDao;

    @Autowired
    public PARBrowserDAO parbrowserDao;

    @Autowired
    public SendSMS sendSMS;

    private ServletContext context;

    private static final Logger logger = LogManager.getLogger(PARAdminController.class);

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "viewPARAdmin.htm", method = RequestMethod.GET)
    public ModelAndView viewPARAdmin(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        //boolean hasprivileage = CommonFunctions.hasPrivileage(privileges, "viewPARAdmin.htm");
        if (CommonFunctions.hasPrivileage(privileges, "viewPARAdmin.htm") == true) {
            mv.setViewName("/par/ParAdmin");
            List fiscyear = fiscalDAO.getFiscalYearList();
            mv.addObject("fiscyear", fiscyear);
            List deptlist = departmentDAO.getDepartmentPARList();
            mv.addObject("departmentList", deptlist);
            mv.addObject("parreviewedspc", "");
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchPARList.htm", method = RequestMethod.POST)
    public void getSearchPARList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute(value = "parreviewedspc") String parreviewedspc, HttpServletResponse response, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) throws IOException, JSONException {

        response.setContentType("application/json");
        PrintWriter out = null;
        PARSearchResult parList = new PARSearchResult();
        if (lub.getLoginusertype().equals("A")) {
            /*PAR Admin*/
            parList = parAdminDAO.getPARList(parAdminSearchCriteria);
        } else if (lub.getLoginusertype().equals("G")) {

            if (parreviewedspc != null && !parreviewedspc.equals("")) {
                /*PAR Reviewing*/
                parList = parAdminDAO.getPARList(parreviewedspc, parAdminSearchCriteria.getFiscalyear(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchParStatus(), parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getSearchCriteria1(), parAdminSearchCriteria.getRows(), parAdminSearchCriteria.getPage());
            } else {
                ParAssignPrivilage pap = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
                /*Checks the spc has the privilage to view PAR*/
                if (pap != null) {
                    /*while only spc and off code assign to officer*/
                    if ((pap.getOffCode() != null && !pap.getOffCode().equals("")) && (pap.getCadreCode() == null || pap.getCadreCode().equals(""))) {
                        parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
                        parList = parAdminDAO.getOfficeSpecificPARList(parAdminSearchCriteria);
                        /*while only spc and off code assign to officer*/
                        /*while spc,off code and cadre code assign to officer*/
                    } else if ((pap.getOffCode() != null && !pap.getOffCode().equals("")) && (pap.getCadreCode() != null && !pap.getCadreCode().equals(""))) {
                        parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
                        parList = parAdminDAO.getOfficeCadreSpecificPARList(parAdminSearchCriteria);
                        /*while spc,off code and cadre code assign to officer*/
                        /*PAR Custodian*/
                    } else {
                        parList = parAdminDAO.getPARList(lub.getLoginspc(), parAdminSearchCriteria.getFiscalyear(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchParStatus(), parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getSearchCriteria1(), parAdminSearchCriteria.getRows(), parAdminSearchCriteria.getPage());

                    }
                    /*when there is no any cadre privilege Assign*/
                } else if (pap == null) {
                    String[] assignedPost = parAdminDAO.getAssignedAdditionalPost(lub.getLoginempid());
                    parList = parAdminDAO.getPARList(assignedPost[0], parAdminSearchCriteria.getFiscalyear(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchParStatus(), parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getSearchCriteria1(), parAdminSearchCriteria.getRows(), parAdminSearchCriteria.getPage());
                }
            }
        }
        JSONObject obj = new JSONObject(parList);
        out = response.getWriter();
        out.write(obj.toString());
    }

    /*Use For Get Reviewed PAR List*/
    @ResponseBody
    @RequestMapping(value = "getSearchReviewedPARList.htm", method = RequestMethod.POST)
    public void getSearchReviewedPARList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute(value = "parreviewedspc") String parreviewedspc, HttpServletResponse response, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/json");
        PrintWriter out = null;
        PARSearchResult parList = new PARSearchResult();
        if (parreviewedspc != null && !parreviewedspc.equals("")) {
            parAdminSearchCriteria.setPrivilegedSpc(parreviewedspc);
            parList = parAdminDAO.getReviewedParList(parAdminSearchCriteria);
        } else {
            parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
            parList = parAdminDAO.getReviewedParList(parAdminSearchCriteria);
        }
        JSONObject obj = new JSONObject(parList);
        out = response.getWriter();
        out.write(obj.toString());
    }

    /*Use For Get Adverse PAR List*/
    @RequestMapping(value = "getSearchAdversedPARList.htm", method = RequestMethod.GET)
    public ModelAndView getSearchAdversedPARList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("parAdminProperties") ParAdminProperties parAdminProperties) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        List adverselist = parAdminDAO.getAdverseParList();
        mv.addObject("adverselist", adverselist);
        mv.setViewName("/par/PARAdverseList");
        return mv;
    }

    /*Use For Get ALL PAR Details in PAR Admin after click on view button*/
    @RequestMapping(value = "viewPARAdmindetail.htm", method = RequestMethod.GET)
    public ModelAndView viewPARAdmindetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminProperties") ParAdminProperties ParAdminProperties, @RequestParam("empId") String empId, @RequestParam("fiscalyear") String fiscalyear, @ModelAttribute("parDetail") ParDetail parDetail,
            @ModelAttribute("parUnReviewedDetailBean") ParUnReviewedDetailBean parUnReviewedDetailBean) {
        ModelAndView mv = new ModelAndView();
        //if (CommonFunctions.hasPrivileage(privileges, "viewPARAdmindetail.htm") == true) {
        ArrayList pardetail = parAdminDAO.getPARDetails(empId, fiscalyear);
        int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), ParAdminProperties.getParId());
        String[] revertreason = parbrowserDao.getRevertReason(ParAdminProperties.getParId(), taskid);
        mv.addObject("pardetail", pardetail);
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("usertype", lub.getLoginusertype());
        mv.addObject("authorityType", revertreason[0]);
        mv.addObject("revertRemarks", revertreason[1]);
        mv.addObject("authorityName", revertreason[2]);
        mv.setViewName("/par/ParAdminDetail");

        // } else {
        //  mv.setViewName("/under_const");
        //}
        return mv;
    }

    /*Use For Save Log For Revert PAR By PAR Admin*/
    @ResponseBody
    @RequestMapping(value = "saveRevertPARdetailbyAdmin.htm", method = RequestMethod.POST)
    public void saveRevertPARdetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parDetail") ParDetail parDetail) {
        ModelAndView mv = new ModelAndView();
        parAdminDAO.revertPARByPARAdmin(lub.getLoginempid(), parDetail);
    }

    /*Use For UN Review PAR By PAR Admin*/
    @ResponseBody
    @RequestMapping(value = "saveUnReviewedPARdetail.htm", method = RequestMethod.POST)
    public void saveReviewedPARdetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parUnReviewedDetailBean") ParUnReviewedDetailBean parUnReviewedDetailBean) {
        ModelAndView mv = new ModelAndView();
        parUnReviewedDetailBean.setUnReviewedBy(lub.getLoginempid());
        parAdminDAO.saveUnReviewedPARDetail(parUnReviewedDetailBean);
    }

    @ResponseBody
    @RequestMapping(value = "saveDeletePARdetail.htm", method = RequestMethod.POST)
    public void saveDeletePARdetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parDeleteDetailBean") ParDeleteDetailBean parDeleteDetailBean) {
        ModelAndView mv = new ModelAndView();
        parDeleteDetailBean.setDeletedBy(lub.getLoginname());
        parAdminDAO.saveDeletePARDetail(parDeleteDetailBean);
    }


    /*Use For Update Cadre Detail By PAR Admin*/
    @ResponseBody
    @RequestMapping(value = "updateCadredetail.htm", method = RequestMethod.POST)
    public void updateCadredetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminProperties") ParAdminProperties parAdminProperties) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        parAdminProperties.setCadreUpdatedBy(lub.getLoginusername());
        parAdminDAO.UpdateCadre(parAdminProperties);

    }

    /*Use For Save Map Post By PAR Admin*/
    @ResponseBody
    @RequestMapping(value = "saveMapPost.htm", method = RequestMethod.POST)
    public void saveMapPost(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView();
        parAdminDAO.SaveMapPost(parApplyForm);
    }

    /*Use For get all Details Of PAR By PAR Admin*/
    @RequestMapping(value = "getviewPARAdmindetail.htm")
    public ModelAndView getviewPARAdmindetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("pARViewDetailLogBean") PARViewDetailLogBean pARViewDetailLogBean) {
        ModelAndView mv = new ModelAndView();
        // String isAuthRemarksClosed = "N";
        //ParAssignPrivilage pap = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
        //if (pap != null) {
        try {
            pARViewDetailLogBean.setLoginByIp(CommonFunctions.getIpAndHost(request));
            pARViewDetailLogBean.setLoginById(lub.getLoginempid());
            pARViewDetailLogBean.setViewedById(lub.getLoginempid());
            pARViewDetailLogBean.setViewedByUserType(lub.getLoginusertype());
            pARViewDetailLogBean.setLoginBySpc(lub.getLoginspc());
            parAdminDAO.UpdatePARViewDetailLog(pARViewDetailLogBean);
            ParApplyForm paf = parAdminDAO.getviewParDetail(parApplyForm.getParId(), parApplyForm.getTaskId());
            ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parApplyForm.getParId(), parApplyForm.getTaskId());
            ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parApplyForm.getParId(), parApplyForm.getTaskId());
            ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parApplyForm.getParId(), parApplyForm.getTaskId());
            paf.setHasadminPriv(parAdminDAO.getHasAdminPriviliged(lub));
            if (lub.getLoginadmintype() != null && lub.getLoginadmintype().equals("PARADMIN")) {
                paf.setHasadminPriv("Y");
                paf.setIsparadmin("Y");
            }
            List gradelist = parbrowserDao.getPARGradeList();
            String isAuthRemarksClosed = parbrowserDao.isAuthRemarksClosed(pARViewDetailLogBean.getFiscalyear());
            String isPARClosed = parbrowserDao.isFiscalYearClosed(pARViewDetailLogBean.getFiscalyear());
            mv.addObject("gradelist", gradelist);
            mv.addObject("parApplyForm", paf);
            mv.addObject("reporting", reportingdata);
            mv.addObject("reviewing", reviewingdata);
            mv.addObject("accepting", acceptingdata);
            mv.addObject("Loginempid", lub.getLoginempid());
            mv.addObject("IsReviewed", 'Y');
            mv.addObject("isAuthRemarksClosed", isAuthRemarksClosed);
            mv.addObject("isPARClosed", isPARClosed);
            //mv.addObject("users", users);
            mv.setViewName("/par/ParAdminViewDetail");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    /*Use For Open PDF After click on view Button and After Review PAR*/
    @RequestMapping(value = "viewReviewedPARPdf.htm", method = RequestMethod.GET)
    public void viewReviewedPARPdf(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @RequestParam("parId") int parId) throws Exception {
        //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + parId + ".pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf");
        ParApplyForm paf = parAdminDAO.getviewParDetail(parId, 0);
        OutputStream out = response.getOutputStream();

        String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
        //byte[] filebyte = FileUtils.readFileToByteArray(new File(reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath() + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parId + ".pdf"));
        byte[] filebyte = FileUtils.readFileToByteArray(new File(reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath() + parId + ".pdf"));
        //byte[] filebyte = FileUtils.readFileToByteArray(new File("D:\\Pdf\\" + paf.getFiscalYear() + "\\" + parId + ".pdf"));

        out.write(filebyte);
        out.flush();
        out.close();
    }

    /*Use For Open PDF After click on view Button and After Review PAR*/
    @RequestMapping(value = "getviewPARAdmindetail", method = {RequestMethod.POST}, params = {"action=Adverse Communication"})
    public ModelAndView AdverseCommunication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("parauthorityhelperbean") Parauthorityhelperbean parauthorityhelperbean, @ModelAttribute("parAchievement") ParAchievement parAchievement) {
        ModelAndView mav = new ModelAndView();
        ParApplyForm paf = parAdminDAO.getviewParDetail(parApplyForm.getParId(), parApplyForm.getTaskId());
        parAdminDAO.AdverseCommunication(parApplyForm.getParId(), parApplyForm.getTaskId());
        ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parauthorityhelperbean.getParId(), parauthorityhelperbean.getTaskId());
        mav.addObject("accepting", acceptingdata);
        mav.addObject("parApplyForm", paf);
        mav.setViewName("/par/AdverseCommunication");
        return mav;
    }

    /*Use For gO tO Main Page After Viewing PAR*/
    @RequestMapping(value = "getviewPARAdmindetail", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView BackAdverseCommunication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("parauthorityhelperbean") Parauthorityhelperbean parauthorityhelperbean, @ModelAttribute("parAchievement") ParAchievement parAchievement) {
        ModelAndView mv = new ModelAndView();
        ParApplyForm paf = parAdminDAO.getviewParDetail(parApplyForm.getParId(), parApplyForm.getTaskId());
        ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parauthorityhelperbean.getParId(), parApplyForm.getTaskId());
        ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parauthorityhelperbean.getParId(), parauthorityhelperbean.getTaskId());
        ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parauthorityhelperbean.getParId(), parauthorityhelperbean.getTaskId());
        paf.setHasadminPriv(parAdminDAO.getHasAdminPriviliged(lub));
        if (lub.getLoginadmintype() != null && lub.getLoginadmintype().equals("PARADMIN")) {
            paf.setHasadminPriv("Y");
            paf.setIsparadmin("Y");
        }
        mv.addObject("parApplyForm", paf);
        mv.addObject("reporting", reportingdata);
        mv.addObject("reviewing", reviewingdata);
        mv.addObject("accepting", acceptingdata);
        mv.setViewName("/par/ParAdminViewDetail");
        return mv;
    }

    /*Use For Download PAR pdf*/
    @RequestMapping(value = "parPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView parPDF(HttpServletResponse response, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm,
            @ModelAttribute("parauthorityhelperbean") Parauthorityhelperbean parauthorityhelperbean, @ModelAttribute("pARViewDetailLogBean") PARViewDetailLogBean pARViewDetailLogBean) {
        ModelAndView mv = new ModelAndView();
        try {
            ParApplyForm paf = parAdminDAO.getviewParDetail(parApplyForm.getParId(), parApplyForm.getTaskId());
            String isAuthRemarksClosed = parbrowserDao.isAuthRemarksClosed(parApplyForm.getFiscalYear());
            String isPARClosed = parbrowserDao.isFiscalYearClosed(parApplyForm.getFiscalYear());
            String filePath = context.getInitParameter("PhotoPath");
            String qrCodePathPAR = context.getInitParameter("ParQRCodePath");
            String parFilePath = qrCodePathPAR;

            String storedpath = qrCodePathPAR + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            String qrCodeurl = storedpath + paf.getEmpId() + "-" + paf.getParId() + ".png";

            paf.setLoginUserType(lub.getLoginusertype());
            paf.setDownloadedById(lub.getLoginempid());
            paf.setDownloadedByName(lub.getLoginname());
            paf.setDownloadedByIp(CommonFunctions.getIpAndHost(request));
            paf.setIsPARClosed(isPARClosed);
            paf.setIsAuthRemarksClosed(isAuthRemarksClosed);

            File fi = null;
            fi = new File(qrCodeurl);
            Image img2 = null;

            if (paf.getParstatus() == 9 && !fi.exists()) {
                parbrowserDao.generateQRCodeForPreviousYearPAR(paf.getParId(), qrCodePathPAR);
            }

            mv.addObject("paf", paf);
            mv.addObject("filePath", filePath);
            mv.addObject("qrCodePathPAR", qrCodePathPAR);
            mv.setViewName("parPDFView");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /*Use For Generate PAR pdf Of Non Adverse*/
    @RequestMapping(value = "parPDFNonadverse.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void parPDFNonadverse(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("parauthorityhelperbean") Parauthorityhelperbean parauthorityhelperbean, @ModelAttribute("parAchievement") ParAchievement parAchievement) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + parApplyForm.getParId() + ".pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf");
        try {
            parAdminDAO.markParAsReviewed(lub.getLoginempid(), lub.getLoginspc(), parApplyForm.getParId());
            ParApplyForm paf = parAdminDAO.getviewParDetail(parApplyForm.getParId());
            String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
            String filePath = reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            String originalFileName = parApplyForm.getParId() + ".pdf";
            //String originalFileName = parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf";

            /*Create PDF File*/
            File reviewedFile = new File(filePath + originalFileName);
            FileOutputStream fout = new FileOutputStream(reviewedFile);
            PdfWriter.getInstance(document, fout);
            document.open();
            parAdminDAO.parPdfReview(paf, document);
            fout.close();
            fout = null;
            /*Create PDF File*/

            /*Digitally Sign the PDF File 
             String query = "parId=" + parApplyForm.getParId() + "&fiscalYear=" + paf.getFiscalYear();
             URL url = new URL("http://10.2.1.53/DocumentSigner/signpar.htm");
             //URL url = new URL("http://localhost:8080/DocumentSigner/signpar.htm");
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("POST");
             connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");
             connection.setDoInput(true);
             connection.setDoOutput(true);

             DataOutputStream output = new DataOutputStream(connection.getOutputStream());
             output.writeBytes(query); */
            // get ready to read the response from the cgi script
           /* DataInputStream input = new DataInputStream(connection.getInputStream());

             // read in each character until end-of-stream is detected
             for (int c = input.read(); c != -1; c = input.read()) {
             System.out.print((char) c);
             }
             input.close();*/
            /*Digitally Sign the PDF File*/

            /*Dispaly the Signed PDF File */
            File f = new File(filePath + originalFileName);
            OutputStream out = response.getOutputStream();
            out.write(FileUtils.readFileToByteArray(f));
            out.flush();
            out.close();
            /*Dispaly the Signed PDF File*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public boolean isClientTrusted(X509Certificate[] chain) {
                    return true;
                }

                public boolean isServerTrusted(X509Certificate[] chain) {
                    return true;
                }
            }
        };
        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
         /* SSLContext sslContext = SSLContextBuilder
         .create()
         .loadTrustMaterial(new TrustSelfSignedStrategy())
         .build();
         String keyPassphrase = "";
         KeyStore keyStore = KeyStore.getInstance("PKCS12");
         keyStore.load(new FileInputStream("D:\\IFMS Loan Integration\\toHRMS\\HRMS\\publicKey"), keyPassphrase.toCharArray());
         SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(keyStore, null).build();*/

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // we can optionally disable hostname verification. 
        // if you don't want to further weaken the security, you don't have to include this.
        /*HostnameVerifier allowAllHosts = HttpsURLConnection.getDefaultHostnameVerifier();*/
        HostnameVerifier allowAllHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                boolean t = hv.verify("localhost", session);
                System.out.println(t);
                return true;
            }
        };
        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        //SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));

        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
        return HttpClients
                .custom()
                .setSSLSocketFactory(connectionFactory)
                .build();
    }
    /*Use For Download NRC pdf*/

    @RequestMapping(value = "downloadNRCPdf.htm", method = RequestMethod.POST, params = {"action=Download"})
    public void parNRCPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf");
        //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + parApplyForm.getParId() + ".pdf");
        try {

            ParApplyForm paf = parAdminDAO.getNRCDetails(parApplyForm.getParId());
            paf.setReviewedby(lub.getLoginname());
            paf.setReviewedbyspc(lub.getLoginspn());

            if (paf.getIsreviewed() != null && paf.getIsreviewed().equals("Y")) {
                String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
                String filePath = reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath();
                String originalFileName = parApplyForm.getParId() + ".pdf";
                //String originalFileName =  parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf";
                File f = new File(filePath + originalFileName);
                OutputStream out = response.getOutputStream();
                out.write(FileUtils.readFileToByteArray(f));
                out.flush();
                out.close();
            } else {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();
                parAdminDAO.DownloadNRCPDF(paf, document);
                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /*Use For Generate NRC pdf WHEN IS ACCEPTED For Review*/
    @RequestMapping(value = "downloadNRCPdf.htm", method = RequestMethod.POST, params = {"action=Accepted"})
    public void nrcAcceptedPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        response.setHeader("Content-Disposition", "attachment; filename=" + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf");
        //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + parApplyForm.getParId() + ".pdf");
        try {

            ParApplyForm paf = parAdminDAO.getNRCDetails(parApplyForm.getParId());
            paf.setReviewedby(lub.getLoginname());
            paf.setReviewedbyspc(lub.getLoginspn());
            String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
            String filePath = reviewedPARFilePath + paf.getFiscalYear() + CommonFunctions.getResourcePath();
            //String originalFileName = parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " +parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf";
            String originalFileName = parApplyForm.getParId() + ".pdf";
            /*Create PDF File*/
            if (paf.getIsreviewed() == null || !paf.getIsreviewed().equals("Y")) {
                parAdminDAO.markParAsReviewed(lub.getLoginempid(), lub.getLoginspc(), parApplyForm.getParId());
                paf = parAdminDAO.getNRCDetails(parApplyForm.getParId());
                FileOutputStream fout = new FileOutputStream(new File(filePath + originalFileName));
                PdfWriter.getInstance(document, fout);
                document.open();
                parAdminDAO.NRCAccepted(paf, document);

            }
            /*Create PDF File*/

            /*Digitally Sign the PDF File 
             String query = "parId=" + parApplyForm.getParId() + "&fiscalYear=" + paf.getFiscalYear();
             URL url = new URL("http://10.2.1.53/DocumentSigner/signpar.htm");
             //URL url = new URL("http://localhost:8080/DocumentSigner/signpar.htm");
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("POST");
             connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");
             connection.setDoInput(true);
             connection.setDoOutput(true);

             DataOutputStream output = new DataOutputStream(connection.getOutputStream());
             output.writeBytes(query);*/
            // get ready to read the response from the cgi script
            /*DataInputStream input = new DataInputStream(connection.getInputStream());

             // read in each character until end-of-stream is detected
             for (int c = input.read(); c != -1; c = input.read()) {
             System.out.print((char) c);
             }
             input.close();*/

            /*Digitally Sign the PDF File 
             String query = "parId=" + parApplyForm.getParId() + "&fiscalYear=" + paf.getFiscalYear();
             URL url = new URL("http://10.1.1.10/DocumentSigner/signpar.htm");
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("POST");
             connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");
             connection.setDoInput(true);
             connection.setDoOutput(true);

             DataOutputStream output = new DataOutputStream(connection.getOutputStream());
             output.writeBytes(query);
             // get ready to read the response from the cgi script
             DataInputStream input = new DataInputStream(connection.getInputStream());

             // read in each character until end-of-stream is detected
             for (int c = input.read(); c != -1; c = input.read()) {
             System.out.print((char) c);
             }
             input.close();  */
            /*Digitally Sign the PDF File*/
            /*Dispaly the Signed PDF File*/
            File f = new File(filePath + originalFileName);
            OutputStream out = response.getOutputStream();
            out.write(FileUtils.readFileToByteArray(f));
            out.flush();
            out.close();
            /*Dispaly the Signed PDF File*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    /*Use For Download NRC Attachment*/
    @RequestMapping(value = "DownloadparNRCAttachment.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void DownloadparNRCAttachment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("parId") int parId) throws IOException {
        ModelAndView mv = new ModelAndView();
        String filepath = context.getInitParameter("ParPath");
        ParNrcAttachment parNrcAttachment = parAdminDAO.DownloadNRCAttchment(filepath, parId);
        response.setContentType(parNrcAttachment.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + parNrcAttachment.getOriginalfilename());
        OutputStream out = response.getOutputStream();
        out.write(parNrcAttachment.getFilecontent());
        out.flush();
        out.close();
    }

    /*Use For get NRC Detail*/
    @RequestMapping(value = "getNRCdetail.htm", method = RequestMethod.GET)
    public ModelAndView getNRCdetail(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        ParApplyForm paf = parAdminDAO.getNRCDetails(parApplyForm.getParId());
        if (paf.getIsreviewed() != null && paf.getIsreviewed().equals("Y")) {
            response.setHeader("Content-Disposition", "attachment; filename=" + parApplyForm.getEmpName() + "(" + parApplyForm.getPrdFrmDate() + " " + "To" + " " + parApplyForm.getPrdToDate() + ")" + "-" + parApplyForm.getParId() + ".pdf");
            //response.setHeader("Content-Disposition", "attachment; filename=PerformanceApprisal_" + parApplyForm.getParId() + ".pdf");
            String reviewedPARFilePath = context.getInitParameter("ReviewedPARFilePath");
            mv.addObject("parApplyForm", paf);
            mv.addObject("reviewedPARFilePath", reviewedPARFilePath);
            mv.setViewName("nrcPDFView");
        } else {
            mv.addObject("parApplyForm", paf);
            mv.addObject("Loginempid", lub.getLoginempid());
            mv.setViewName("/par/PARRequestNRC");
        }
        return mv;
    }

    /*Use For View PAR Custodian Detail*/
    @RequestMapping(value = "parCustdian.htm", method = RequestMethod.GET)
    public ModelAndView parCustdiandetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView("/par/PARCustdian");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ParAssignPrivilage pap = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
        ArrayList cadreList = null;
        if (pap != null) {
            cadreList = parAdminDAO.getCadreListForCustdian(lub.getLoginspc());
        } else if (pap == null) {
            String[] assignedPost = parAdminDAO.getAssignedAdditionalPost(lub.getLoginempid());
            cadreList = parAdminDAO.getCadreListForCustdian(assignedPost[0]);
        }
        //ArrayList totalAdverseCommunicationList = parAdminDAO.getTotalAdverseCommunicationListForCustdian();
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("parreviewedspc", "");
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("cadreList", cadreList);

        // mv.addObject("totalAdverseCommunicationList", totalAdverseCommunicationList);
        return mv;
    }

    /*Use For getting PAR total Adverse Communication List*/
    @RequestMapping(value = "totalAdverseCommunicationList.htm", method = RequestMethod.GET)
    public ModelAndView totalAdverseCommunicationList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView("/par/totalAdverseCommunicationListByCustodian");
        ArrayList totalAdverseCommunication = parAdminDAO.getTotalAdverseCommunicationListForCustdian();
        mv.addObject("totalAdverseCommunication", totalAdverseCommunication);
        return mv;
    }

    /*Use For go Back From total Adverse Communication List*/
    @RequestMapping(value = "totalAdverseCommunicationList.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backtotalAdverseCommunicationList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView("/par/PARCustdian");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ArrayList cadreList = parAdminDAO.getCadreListForCustdian(lub.getLoginspc());
        //ArrayList totalAdverseCommunicationList = parAdminDAO.getTotalAdverseCommunicationListForCustdian();
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("parreviewedspc", "");
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("cadreList", cadreList);
        // mv.addObject("totalAdverseCommunicationList", totalAdverseCommunicationList);
        return mv;
    }

    /*Use To view the PAR Viewer Detail*/
    @RequestMapping(value = "parViewer.htm", method = RequestMethod.GET)
    public ModelAndView parViewer(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/par/PARViewer");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ParAssignPrivilage pap = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("parreviewedspc", "");
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("pap", pap);
        return mv;
    }

    /*Use To view the PAR Reviewing Detail*/
    @RequestMapping(value = "parReviewingView.htm", method = RequestMethod.GET)
    public ModelAndView parReviewingView(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parreviewedspc") String parreviewedspc) {
        ModelAndView mv = new ModelAndView("/par/PARCustdian");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ArrayList cadreList = parAdminDAO.getCadreListForCustdian(parreviewedspc);
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("cadreList", cadreList);
        //mv.addObject("parreviewedspc", "");
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        return mv;
    }

    /*Use To get the PAR CaderList*/
    @RequestMapping(value = "parCaderList")
    public ModelAndView parCaderList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView("");
        if (CommonFunctions.hasPrivileage(privileges, "parCaderList.htm") == true) {
            mv.setViewName("/par/PARCadreList");
            List cadrelist = cadreDAO.getCadreList(cadre.getDeptCode());
            mv.addObject("cadrelist", cadrelist);
            mv.addObject("departmentList", departmentDAO.getDepartmentList());
        } else {
            mv.setViewName("/under_const");
        }
        return mv;

    }
    /*Use To get Districtwise Privilege For Police*/

    @RequestMapping(value = "parDistrictWisePrivilegeForPolice")
    public ModelAndView LoadDistWisePrivilege4PoliceParPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre,
            @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        if (lub.getLoginuserid().equals("dgpoliceacr")) {
            mv.setViewName("/par/PARDistrictwisePrivilegeForPolice");
            List distWisePrivilegedList = parAdminDAO.getAssignPrivilegedListDistWiseForPolice();
            mv.addObject("distWisePrivilegedList", distWisePrivilegedList);
            mv.addObject("departmentList", departmentDAO.getDepartmentList4SiPAR());
            mv.addObject("cadrelist", cadreDAO.getCadreList(cadre.getDeptCode()));
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    /*Use To get Districtwise Assigned PrivilegeList For Police*/
    @RequestMapping(value = "parDistrictWisePrivilegeForPolice.htm", method = {RequestMethod.POST}, params = {"action=Get Assigned PrivilegeList"})
    public ModelAndView getparDistrictWisePrivilegeForPoliceList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("districtWiseAuthorizationForPolice") DistrictWiseAuthorizationForPolice districtWiseAuthorizationForPolice) {
        ModelAndView mav = new ModelAndView("/par/PARDistrictwisePrivilegeForPolice");
        ArrayList districtwiseprivilegedList = parAdminDAO.getAssignPrivilegedListDistWiseForPolice();
        mav.addObject("districtwiseprivilegedList", districtwiseprivilegedList);
        return mav;

    }

    /*Use To delete Districtwise Assigned PrivilegeList For Police*/
    @RequestMapping(value = "removeDistrictWisePrivilegeForPolice.htm")
    public ModelAndView removeDistrictWisePrivilegeForPolice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("districtWiseAuthorizationForPolice") DistrictWiseAuthorizationForPolice districtWiseAuthorizationForPolice) {
        parAdminDAO.deletePrivilegedListDistWiseForPolice(districtWiseAuthorizationForPolice.getSpc(), districtWiseAuthorizationForPolice.getPostGrp());
        ModelAndView mv = new ModelAndView("/par/PARDistrictwisePrivilegeForPolice");
        ArrayList districtwiseprivilegedList = parAdminDAO.getAssignPrivilegedListDistWiseForPolice();
        mv.addObject("districtwiseprivilegedList", districtwiseprivilegedList);
        return mv;
    }

    /*Use To Assign Cadre to a Specific PAR*/
    @ResponseBody
    @RequestMapping(value = "assignCadrePrivilage.htm")
    public void assignCadrePrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            ModelAndView mv = new ModelAndView();
            parApplyForm.setLoginByIp(CommonFunctions.getIpAndHost(request));

            parApplyForm.setLoginById(lub.getLoginempid());
            String msg = parAdminDAO.saveAssignCadre(parApplyForm);
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out = response.getWriter();
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Use To Assigned PrivilegeList Officewise*/
    @ResponseBody
    @RequestMapping(value = "assignOfficewisePrivilage.htm")
    public void assignOfficewisePrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = parAdminDAO.saveAssignOfficewisePrivilige(parApplyForm);
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);
        out = response.getWriter();
        out.write(obj.toString());
    }

    /*Use To get Assigned PrivilegeList*/
    @ResponseBody
    @RequestMapping(value = "geteAssignPrivilegeDetail.htm")
    public void geteAssignPrivilegeDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList privilegedList = parAdminDAO.getAssignPrivilegedList(parApplyForm.getCadrecode());
        JSONArray json = new JSONArray(privilegedList);
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    /*Use To get Assigned PrivilegeList Officewise*/
    @ResponseBody
    @RequestMapping(value = "getOfficeWiseAssignPrivilegeDetail.htm")
    public void geteOfficeWiseAssignPrivilegeDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList officewiseprivilegedList = parAdminDAO.getOfficewiseAssignPrivilegedList(parApplyForm.getOffcode());
        JSONArray json = new JSONArray(officewiseprivilegedList);
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    /*Use To remove Cadre Privilege*/
    @ResponseBody
    @RequestMapping(value = "removeCadrePrivilage.htm")
    public void removeCadrePrivilage(ModelMap model, HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            parApplyForm.setLoginByIp(CommonFunctions.getIpAndHost(request));
            parApplyForm.setLoginById(lub.getLoginempid());
            //parAdminDAO.deleteCadrePrivilage(parApplyForm.getCadrecode(), parApplyForm.getSpc(), parApplyForm.getPostGroup());
            parAdminDAO.deleteCadrePrivilage(parApplyForm);
            JSONArray json = new JSONArray();
            out = response.getWriter();
            out.write(json.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Use To remove OfficeWise Privilege*/
    @ResponseBody
    @RequestMapping(value = "removeOfficeWisePrivilage.htm")
    public void removeOfficeWisePrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        parAdminDAO.deleteOfficewisePrivilage(parApplyForm.getOffcode(), parApplyForm.getSpc(), parApplyForm.getPostGroup());
        JSONArray json = new JSONArray();
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    /*Use To get PAR Status List*/
    @RequestMapping(value = "getParStatusrListJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getFiscalYearListJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List parStatusList = parAdminDAO.getParStatusList();
            json = new JSONArray(parStatusList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /*Use To get which Cadre List Assigned to Custodian*/
    @RequestMapping(value = "GetCadreListForCustodianJSON", method = RequestMethod.POST)
    public @ResponseBody
    void getCadreListForCustodianJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            ArrayList cadreList = parAdminDAO.getCadreListForCustdian(lub.getLoginspc());
            json = new JSONArray(cadreList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /*Use To get Authority List For Submit PAR*/
    @RequestMapping(value = "preferedAuthortityListView.htm")
    public ModelAndView preferedAuthortityListView(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        ModelAndView mv = new ModelAndView("/par/PreferedAuthortityView");
        String appriseSPC = parAdminDAO.getAppriseSPCOfPar(parApplyForm.getParId());
        List sanctionedauthoritylist = sactionedAuthorityDao.getSanctionedAuthorityList(appriseSPC, 3, parApplyForm.getFiscalYear(), lub.getLoginempid());
        mv.addObject("appriseSPC", appriseSPC);
        mv.addObject("sanctionedauthoritylist", sanctionedauthoritylist);
        return mv;
    }

    /*As before Accepting Final Grading was not there later While Review the PAR the Accepting Authority is also able to give Final Remarks*/
    @ResponseBody
    @RequestMapping(value = "saveAcceptingRemarksForReview.htm")
    public void saveAcceptingRemarksForReview(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("paf") ParDetail paf, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        parApplyForm.setAcceptingempid(parApplyForm.getAcceptingempid());
        parApplyForm.setSltAcceptingGrading(parApplyForm.getSltAcceptingGrading());
        parAdminDAO.saveAcceptingRemarksForReview(parApplyForm);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());

        out.flush();
    }

    /*Use To get PDF For Adverse Communication By appraisee*/
    @RequestMapping(value = "parPDFAdverseAppraiseCommunication.htm")
    public ModelAndView parPDFAdverseAppraiseCommunication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView();
        ParApplyForm paf = parAdminDAO.getParBriefDetail(parAdverseCommunicationDetail.getParId());
        if (paf.getIsadversed() == null || paf.getIsadversed().equals("N")) {
            parAdminDAO.markParAsAdverse(parAdverseCommunicationDetail.getParId());
        }
        parAdminDAO.getAppraiseForAdversePAR(parAdverseCommunicationDetail);
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());
        if (paf.getAdverseCommunicationStatusId() == 102) {
            ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            mv.addObject("reporting", reportingdata);
            mv.addObject("reviewing", reviewingdata);
            mv.addObject("accepting", acceptingdata);
        }
        parAdverseCommunicationDetail.setAdverseCommunicationStatusId(paf.getAdverseCommunicationStatusId());
        mv.addObject("parBriefDetail", paf);
        mv.addObject("parAdverseCommunicationDetail", parAdverseCommunicationDetail);
        mv.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mv.setViewName("/par/ParAdverseAppraiseeRemark");
        return mv;
    }

    /*Use To Submit Remarks For Adverse Communication*/
    @RequestMapping(value = "parPDFAdverseAppraiseCommunication.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView saveparparPDFAdverseAppraiseCommunication(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mav = new ModelAndView();
        parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
        parAdverseCommunicationDetail.setFromspc(lub.getLoginspc());
        if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() >= 102) {
            parAdverseCommunicationDetail.setFromAuthType("Custodian");
            parAdverseCommunicationDetail.setToAuthType("Authority");
            parAdminDAO.savecustodianremarksAdversePAR(parAdverseCommunicationDetail);
        } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 0) {
            parAdverseCommunicationDetail.setFromAuthType("Custodian");
            parAdverseCommunicationDetail.setToAuthType("Apprisee");
            parAdminDAO.savecustodianremarksAdversePARToAppraisee(parAdverseCommunicationDetail);
        }
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());
        mav.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mav.setViewName("/par/ParAdverseAppraiseeRemark");
        return mav;
    }

    /*Use To Submit Remarks For Adverse Communication By appraisee*/
    @RequestMapping(value = "parAdverseCommunicationReply.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView parAdverseCommunicationReply(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mav = new ModelAndView();
        parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
        parAdverseCommunicationDetail.setFromspc(lub.getLoginspc());

        if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 101) {
            parAdverseCommunicationDetail.setFromAuthType("Apprisee");
            parAdverseCommunicationDetail.setToAuthType("Custodian");
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(102);
            parAdminDAO.saveparAdverseCommunicationReply(parAdverseCommunicationDetail);
        } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 103) {
            parAdverseCommunicationDetail.setFromAuthType("Authority");
            parAdverseCommunicationDetail.setToAuthType("Custodian");
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(113);
            parAdminDAO.saveparAdverseCommunicationReply(parAdverseCommunicationDetail);
        }
        TaskListHelperBean taskDetails = taskDAO.getTaskDetails(parAdverseCommunicationDetail.getTaskId());
        mav.addObject("taskListHelperBean", taskDetails);
        mav.setViewName("/par/ParAdverseAppraiseeRemarkSubmitted");
        return mav;
    }

    /*Use To Submit Reply Remarks For Adverse Communication*/
    @RequestMapping(value = "pardverseRemarkReply.htm")
    public ModelAndView pardverseRemarkReply(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView();
        //parAdminDAO.markParAsAdverse(parAdverseCommunicationDetail.getParId());
        parAdminDAO.getAppraiseForAdversePAR(parAdverseCommunicationDetail);
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());

        mv.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mv.setViewName("/par/ParAdverseRemarkReply");
        return mv;
    }

    /*  @RequestMapping(value = "parPDFAdverse.htm")
     public ModelAndView parPDFAdverse(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
     ModelAndView mv = new ModelAndView();
     parAdminDAO.markParAsAdverse(parApplyForm.getParId());
     ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parApplyForm.getParId(), parApplyForm.getTaskId());
     ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parApplyForm.getParId(), parApplyForm.getTaskId());
     ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parApplyForm.getParId(), parApplyForm.getTaskId());
     List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());
     mv.addObject("custodianadverseremarkList", custodianadverseremarkList);
     mv.addObject("reporting", reportingdata);
     mv.addObject("reviewing", reviewingdata);
     mv.addObject("accepting", acceptingdata);
     mv.setViewName("/par/ParAdverseRemark");
     return mv;
     }

     @RequestMapping(value = "parPDFAdverseremark.htm", params = {"action=Submit"}, method = RequestMethod.POST)
     public ModelAndView saveparPDFAdverse(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute(value = "parreviewedspc") String parreviewedspc) {
     ModelAndView mav = new ModelAndView();
     parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
     parAdverseCommunicationDetail.setFromspc(lub.getLoginspc());
     parAdminDAO.savecustodianremarksAdversePAR(parAdverseCommunicationDetail);
     List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());
     mav.addObject("custodianadverseremarkList", custodianadverseremarkList);
     mav.setViewName("/par/ParAdverseAppraiseeRemark");
     return mav;
     } */
    @RequestMapping(value = "parAdverseremark.htm")
    public ModelAndView parAdverseremark(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView();
        TaskListHelperBean taskDetails = taskDAO.getTaskDetails(taskId);
        mv.addObject("taskListHelperBean", taskDetails);
        if (taskDetails.getStatusId() == 101 || taskDetails.getStatusId() == 103) {
            parAdverseCommunicationDetail.setToempId(taskDetails.getAppEmpCode());
            parAdverseCommunicationDetail.setTospc(taskDetails.getAppSpc());
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(taskDetails.getStatusId());
            ParAdverseCommunicationDetail communicationDetail = parAdminDAO.getCommunicationDetails(taskDetails.getReferenceId());
            parAdverseCommunicationDetail.setParId(communicationDetail.getParId());
            mv.addObject("communicationDetail", communicationDetail);
            mv.setViewName("/par/ParAdverseRemarkReply");

        }
        return mv;
    }

    /*Use To download Attachment for Adverse PAR*/
    @RequestMapping(value = "downloadAttachmentforAdversePAR")
    public void downloadAttachmentforAdversePAR(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        parAdverseCommunicationDetail = parAdminDAO.getAttachedFileforAdversePAR(parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getCommunicationId());
        response.setContentType(parAdverseCommunicationDetail.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + parAdverseCommunicationDetail.getOriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(parAdverseCommunicationDetail.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "downloadpendingPARAtAuthorityExcelView")
    public void downloadpendingPARAtAuthorityExcelView(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            response.setHeader("Content-Disposition", "attachment; filename=ServiceParticular.xls");
            parAdminDAO.downloadExcelForPARReportAtViewer(workbook, lub.getLoginspc(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getFiscalyear(), parAdminSearchCriteria.getSearchParStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Use To get Pending PAR Status in Excel Format For Authority*/
    @RequestMapping(value = "pendingPARAtAuthorityExcelView.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView pendingPARAtAuthorityExcelView(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        ArrayList pendingparList = parAdminDAO.getPendingPARListAtAuthorityEnd(lub.getLoginspc(), parAdminSearchCriteria.getCadrecode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchString(), parAdminSearchCriteria.getFiscalyear(), parAdminSearchCriteria.getSearchParStatus());
        ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parApplyForm.getParId(), 0);
        ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parApplyForm.getParId(), 0);
        ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parApplyForm.getParId(), 0);
        mv.addObject("pendingparList", pendingparList);
        mv.setViewName("pendingPARAtAuthorityExcel");
        return mv;
    }

    /*Use To get PAR Submission Report groupwise*/
    @RequestMapping(value = "groupwiseParStatementReport.htm")
    public ModelAndView groupwiseParStatementReport(@ModelAttribute("parAdminProperties") ParAdminProperties parAdminProperties) {
        ModelAndView mv = new ModelAndView();
        List parStatusList = parAdminDAO.getGroupWiseParStatus(parAdminProperties.getFiscalyear());
        mv.addObject("parStatusList", parStatusList);
        mv.setViewName("/par/GroupWiseParReport");
        return mv;
    }

    /*Use To go back PAR Submission Report groupwise*/
    @RequestMapping(value = "groupwiseParReport.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backgroupwiseParStatementReport(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:viewPARAdmin.htm");
        return mav;
    }

    /*Use To upload Previous Year PARList in Custodian Account*/
    @RequestMapping(value = "uploadPreviousYearPARList.htm")
    public ModelAndView uploadPreviousYearPARList(@ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        List deptlist = deptDAO.getDepartmentList();
        List gradelist = parbrowserDao.getPARGradeList();
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("deptlist", deptlist);
        mv.addObject("gradelist", gradelist);
        mv.setViewName("/par/UploadPreviousYearPAR");
        return mv;
    }

    /*Use To search Previous Year PARList in Custodian Account*/
    @RequestMapping(value = "uploadPreviousYearPARList.htm", params = {"action=Search"}, method = RequestMethod.POST)
    public ModelAndView searchuploadPreviousYearPARList(@ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        Users emp = parAdminDAO.getEmployeeProfileInfo(uploadPreviousPAR.getEmpId());
        List deptlist = deptDAO.getDepartmentList();
        List cadrelist = cadreDAO.getCadreList(uploadPreviousPAR.getDeptcode());
        List gradelist = parbrowserDao.getPARGradeList();
        String isAppraiseeSubmittedPAR = parAdminDAO.isAppraiseeSubmittedPAR(uploadPreviousPAR.getEmpId(), uploadPreviousPAR.getFiscalyear());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("emp", emp);
        mv.addObject("deptlist", deptlist);
        mv.addObject("cadrelist", cadrelist);
        mv.addObject("gradelist", gradelist);
        mv.addObject("uploadPreviousPAR", uploadPreviousPAR);
        mv.addObject("isAppraiseeSubmittedPAR", isAppraiseeSubmittedPAR);
        mv.setViewName("/par/UploadPreviousYearPAR");
        return mv;
    }

    /*Use To save Details for upload Previous Year PARList in Custodian Account*/
    @RequestMapping(value = "uploadPreviousYearPARList.htm", params = {"action=Save"}, method = RequestMethod.POST)
    public ModelAndView saveuploadPreviousYearPARList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR) {
        ModelAndView mv = new ModelAndView();
        String parfrmdt = "";
        String partodt = "";
        parfrmdt = uploadPreviousPAR.getPrdFrmDate();
        partodt = uploadPreviousPAR.getPrdToDate();
        uploadPreviousPAR.setPreviousyrParUploadedbyempId(lub.getLoginempid());
        uploadPreviousPAR.setPreviousyrParUploadedbyspc(lub.getLoginspc());
        boolean isDuplicatePeriod = parbrowserDao.isDuplicatePARPeriod(uploadPreviousPAR.getEmpId(), parfrmdt, partodt, uploadPreviousPAR.getFiscalyear(), uploadPreviousPAR.getHidParId());
        if (isDuplicatePeriod == true) {
            parAdminDAO.saveEmployeeListForUploadPAR(uploadPreviousPAR);
            mv.addObject("parerrmsg", "Uploaded Previous Year PAR Successfully");
        } else {
            mv.addObject("parerrmsg", "Duplicate Period");
        }
        mv.setViewName("/par/UploadPreviousYearPAR");
        return mv;
    }

    /*Use To get Details for upload Previous Year PARList in Custodian Account*/
    @RequestMapping(value = "uploadPreviousYearPARList.htm", params = {"action=Get Uploaded List"}, method = RequestMethod.POST)
    public ModelAndView getuploadPreviousYearPARList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR) {
        ModelAndView mv = new ModelAndView();
        List uploadPARList = parAdminDAO.getPreviousYearPARUpload(lub.getLoginempid());
        mv.addObject("uploadPARList", uploadPARList);
        mv.setViewName("/par/UploadPreviousYearPARList");
        return mv;
    }

    @RequestMapping(value = "uploadPreviousYearPARList.htm", params = {"action=Cancel"}, method = RequestMethod.POST)
    public ModelAndView canceluploadPreviousYearPARList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR) {
        ModelAndView mv = new ModelAndView("/par/PARCustdian");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ArrayList cadreList = parAdminDAO.getCadreListForCustdian(lub.getLoginspc());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("parreviewedspc", "");
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("cadreList", cadreList);
        return mv;
    }

    /*Use For Download Attachments for upload Previous Year PARList in Custodian Account*/
    @RequestMapping(value = "downloadAttachmentOfPreviousYearPAR")
    public void downloadAttachmentOfPreviousYearPAR(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("uploadPreviousPAR") UploadPreviousPAR uploadPreviousPAR, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        uploadPreviousPAR = parAdminDAO.getAttachedFileOfPreviousYearPAR(uploadPreviousPAR.getParId());
        response.setContentType(uploadPreviousPAR.getGetContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + uploadPreviousPAR.getOriginalFileNameForpreviousYearPAR());
        OutputStream out = response.getOutputStream();
        out.write(uploadPreviousPAR.getFilecontent());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getemployeeDetailsList.htm", method = RequestMethod.POST)
    public void getemployeeDetailsList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("parAdminProperties") ParAdminProperties parAdminProperties) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        Users emp = parAdminDAO.getEmployeeProfileInfo(parAdminProperties.getEmpId());
        JSONObject obj = new JSONObject(emp);
        out = response.getWriter();
        out.write(obj.toString());
    }

    /*Use For view PAR Statement Submission Report For Authority Login*/
    @RequestMapping(value = "viewPARStatementForAuthority.htm", method = RequestMethod.GET)
    public ModelAndView viewPARStatementForAuthority(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("fiscyear", fiscyear);
        mv.setViewName("/par/PARStatusReportForAuthority");
        return mv;
    }

    /*Use For view Groupwise PAR Statement Submission Report For Authority Login*/
    @RequestMapping(value = "groupwiseParStatementReportForAuthority.htm")
    public ModelAndView groupwiseParStatementReportForAuthority(@ModelAttribute("parAdminProperties") ParAdminProperties parAdminProperties) {
        ModelAndView mv = new ModelAndView();
        List parStatusList = parAdminDAO.getGroupWiseParStatus(parAdminProperties.getFiscalyear());
        mv.addObject("parStatusList", parStatusList);
        mv.addObject("fiscalyear", parAdminProperties.getFiscalyear());
        mv.setViewName("/par/GroupWiseParReportForAuthority");
        return mv;
    }

    /*Use For go back after PAR Statement Submission Report For Authority Login*/
    @RequestMapping(value = "groupwiseParReportForAuthority.htm", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backgroupwiseParStatementReportForAuthority(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:viewPARStatementForAuthority.htm");
        return mav;
    }

    /*Use For Force Forward par in PAR Force Forward link inside PAR Admin*/
    @RequestMapping(value = "parForceForwardDetail")
    public ModelAndView parForceForwardDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre, @ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        logger.log(Level.INFO, "Inside parForceForwardDetail 1");
        logger.log(Level.ALL, "Inside parForceForwardDetail 2");
        logger.log(Level.DEBUG, "Inside parForceForwardDetail 3");
        logger.log(Level.ERROR, "Inside parForceForwardDetail 4");
        logger.log(Level.FATAL, "Inside parForceForwardDetail 5");
        logger.log(Level.TRACE, "Inside parForceForwardDetail 6");
        if (CommonFunctions.hasPrivileage(privileges, "parForceForwardDetail.htm") == true) {
            mv.setViewName("/par/ForceForwardDetail");
            List cadrelist = cadreDAO.getCadreList(cadre.getDeptCode());
            List fiscalcyear = fiscalDAO.getFiscalYearListForCadrewiseForceforward();
            mv.addObject("cadrelist", cadrelist);
            mv.addObject("departmentList", departmentDAO.getDepartmentList());
            mv.addObject("fiscalcyear", fiscalcyear);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;

    }

    @ResponseBody
    @RequestMapping(value = "forceforward.htm")
    public void forceforward(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws Exception {
        response.setContentType("application/json");
        String msg = parAdminDAO.forceForward(parApplyForm);
        JSONObject obj = new JSONObject();
        obj.accumulate("msg", msg);
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
        out.close();
    }

    /*Use to Force Forward the PAR*/
    @RequestMapping(value = "parForceForwardDetail.htm", params = {"action=Force Forward"}, method = RequestMethod.POST)
    public ModelAndView saveparForceForwardDetail(@ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        parAdminDAO.saveForceForwardDetail(parForceForwardBean);
        logger.log(Level.INFO, "Inside Force Forward All Controller");
        if (parForceForwardBean.getForceForwardType() != null && parForceForwardBean.getForceForwardType().equals("cadrewiseforward")) {
            parAdminDAO.forceForwardCadrewise(parForceForwardBean);
        } else if (parForceForwardBean.getForceForwardType() != null && parForceForwardBean.getForceForwardType().equals("forwardall")) {
            logger.log(Level.INFO, "Force Forward From Reporting to Reviewing");
            parAdminDAO.forceForwardAll(parForceForwardBean);
        }
        ArrayList forceForwardDetail = parAdminDAO.getForceForwardDetailListCadrewise();
        ArrayList forceForwardDetailOfALLCadre = parAdminDAO.getForceForwardDetailListALLCadre();
        mv.addObject("forceForwardDetail", forceForwardDetail);
        mv.addObject("forceForwardDetailOfALLCadre", forceForwardDetailOfALLCadre);
        String msg = parAdminDAO.forceForwardCadrewise(parForceForwardBean);
        mv.setViewName("/par/ForceForwardDetailList");
        return mv;
    }

    /*Use to get Force Forward Log*/
    @RequestMapping(value = "parForceForwardDetail.htm", params = {"action=Get Force forward List"}, method = RequestMethod.POST)
    public ModelAndView getlistparForceForwardDetail(@ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList forceForwardDetail = parAdminDAO.getForceForwardDetailListCadrewise();
        ArrayList forceForwardDetailOfALLCadre = parAdminDAO.getForceForwardDetailListALLCadre();
        mv.addObject("forceForwardDetail", forceForwardDetail);
        mv.addObject("forceForwardDetailOfALLCadre", forceForwardDetailOfALLCadre);

        mv.setViewName("/par/ForceForwardDetailList");
        return mv;
    }
    /*Use to Check on which place Error Occures After Force Forward Of PAR*/

    @RequestMapping(value = "transferForceforwardDetailToLogtable.htm", method = RequestMethod.GET)
    public ModelAndView transferForceforwardDetailToLogtable(@ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean) {
        ModelAndView mv = new ModelAndView();
        parAdminDAO.transferPARListForForceForwardFromReportingToReviewingToLog(parForceForwardBean);
        mv.setViewName("/par/ForceForwardDetailList");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "forceforwardCheckError.htm")
    public void forceforwardCheckError(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws Exception {
        response.setContentType("application/json");
        parAdminDAO.getcheckForceForwardErrorFromReviewingtoAccepting();
        JSONObject obj = new JSONObject();
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
        out.close();
    }

    /*Use to go back From Force Forward Of PAR*/
    @RequestMapping(value = "parForceForwardDetail.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView backparForceForwardDetail(@ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean) {
        ModelAndView mv = new ModelAndView("/par/ForceForwardDetail");
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("departmentList", departmentDAO.getDepartmentList());
        mv.addObject("fiscyear", fiscyear);
        return mv;
    }

    /*Use to Manage PAR Status when Open and Close the PAR With Date*/
    @RequestMapping(value = "parStatusManageByPARAdmin", method = RequestMethod.GET)
    public ModelAndView parStatusManage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parStatusBean") ParStatusBean parStatusBean, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView("");
        if (CommonFunctions.hasPrivileage(privileges, "parStatusManageByPARAdmin.htm") == true) {
            mv.setViewName("/par/PARStatusChangeDetail");
            List fiscyear = fiscalDAO.getFiscalYearList();
            mv.addObject("fiscyear", fiscyear);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;

    }

    /*Use to Save Manage PAR Status when Open and Close the PAR With Date*/
    @RequestMapping(value = "parStatusManageByPARAdmin.htm", params = {"action=Save"}, method = RequestMethod.POST)
    public ModelAndView saveparStatusManageByPARAdmin(@ModelAttribute("parStatusBean") ParStatusBean parStatusBean) {
        ModelAndView mv = new ModelAndView();
        parAdminDAO.savePARStatusDetailByPARAdmin(parStatusBean);
        ArrayList parStatusDetail = parAdminDAO.getPARStatusDetailListByPARAdmin();
        mv.addObject("parStatusDetail", parStatusDetail);
        mv.setViewName("/par/PARStatusChangeList");
        return mv;
    }

    /*Use to get Manage PAR Status Change List*/
    @RequestMapping(value = "parStatusManageByPARAdmin.htm", params = {"action=Get Change List"}, method = RequestMethod.POST)
    public ModelAndView getlistparStatusManageByPARAdmin(@ModelAttribute("parStatusBean") ParStatusBean parStatusBean) {
        ModelAndView mv = new ModelAndView();
        ArrayList parStatusDetail = parAdminDAO.getPARStatusDetailListByPARAdmin();
        mv.addObject("parStatusDetail", parStatusDetail);
        mv.setViewName("/par/PARStatusChangeList");
        return mv;
    }

    /*Use to download File For PAR Status Change List*/
    @RequestMapping(value = "downloadFileForChangePARStatus")
    public void downloadFileForChangePARStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parStatusBean") ParStatusBean parStatusBean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        parStatusBean = parAdminDAO.getAttachedFileforChangePARStatus(parStatusBean.getLogId());
        response.setContentType(parStatusBean.getFileTypeForparStatus());
        response.setHeader("Content-Disposition", "attachment;filename=" + parStatusBean.getOriginalFileNameForparStatus());
        OutputStream out = response.getOutputStream();
        out.write(parStatusBean.getFilecontent());
        out.flush();
    }

    /*Use to go back For PAR Status Change List*/
    @RequestMapping(value = "parStatusManageByPARAdmin.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView BackparStatusManageByPARAdmin(@ModelAttribute("parStatusBean") ParStatusBean parStatusBean) {
        ModelAndView mv = new ModelAndView("/par/PARStatusChangeDetail");
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("fiscyear", fiscyear);
        return mv;
    }

    /*Use to do force forward caderwise*/
    @ResponseBody
    @RequestMapping(value = "forceforwardcaderwise.htm")
    public void forceforwardcaderwise(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean) throws Exception {
        response.setContentType("application/json");
        String msg = parAdminDAO.forceForwardCadrewise(parForceForwardBean);
        JSONObject obj = new JSONObject();
        obj.accumulate("msg", msg);
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
        out.close();
    }

    /*Message send facility For Not Submitting PAR*/
    @RequestMapping(value = "sendMessageForPARList")
    public ModelAndView sendMessageForPARList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication, @ModelAttribute("Privileges") Module[] privileges) {
        ModelAndView mv = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "sendMessageForPARList.htm") == true) {
            mv.setViewName("/par/MessageSentDetail");
            List fiscyear = fiscalDAO.getFiscalYearList();
            ArrayList parStatusForMessageCommunication = parAdminDAO.getPARStatusDetailListForMessageCommunication(parMessageCommunication.getFiscalyear());
            mv.addObject("fiscyear", fiscyear);
            mv.addObject("parStatusForMessageCommunication", parStatusForMessageCommunication);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    /*Message search Appraisee List For Message Communication*/
    @RequestMapping(value = "sendMessageForPARList.htm", params = {"action=Search"}, method = RequestMethod.POST)
    public ModelAndView searchappraiseeListForMessageCommunication(@ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) {
        ModelAndView mv = new ModelAndView("/par/MessageSentDetail");
        ArrayList parStatusForMessageCommunication = parAdminDAO.getPARStatusDetailListForMessageCommunication(parMessageCommunication.getFiscalyear());
        List fiscyear = fiscalDAO.getFiscalYearList();
        mv.addObject("parStatusForMessageCommunication", parStatusForMessageCommunication);
        mv.addObject("fiscyear", fiscyear);
        return mv;
    }

    /*Use For Run Thread For Message Communication to Appraisee*/
    @RequestMapping(value = "sendMessageForPARList.htm", params = {"action=Run Thread For Message Communication"}, method = RequestMethod.POST)
    public ModelAndView sendThredForMessageCommunication(@ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) throws Exception {
        ModelAndView mv = new ModelAndView("/par/MessageSentDetail");
        ArrayList smss = parAdminDAO.getMessageList();
        sendSMS.sendPARMessage(smss);
        return mv;
    }

    @RequestMapping(value = "runThreadForMessageCommunication")
    public ModelAndView runThreadForMessageCommunication(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) throws Exception {
        ModelAndView mv = new ModelAndView("/par/AppraiseListForMessageCommunication");
        ArrayList smss = parAdminDAO.getMessageList();
        sendSMS.sendPARMessage(smss);
        return mv;
    }

    /*Message search Appraisee List For Message Communication*/
    @RequestMapping(value = "appraiseeListForMessageCommunication")
    public ModelAndView appraiseeListForMessageCommunication(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) {
        ModelAndView mv = new ModelAndView("/par/AppraiseListForMessageCommunication");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ArrayList appraiseList = parAdminDAO.getAppraiseDetailListForMessageCommunication(parMessageCommunication.getFiscalyear());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("appraiseList", appraiseList);
        return mv;
    }

    @RequestMapping(value = "appraiseeListForMessageCommunication.htm", params = {"action=Sent Message"}, method = RequestMethod.POST)
    public ModelAndView sentmessageappraiseeListForMessageCommunication(@ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) {
        ModelAndView mv = new ModelAndView("/par/MessageSentDetail");
        parAdminDAO.saveSendMessageCommunicationToAppraisee(parMessageCommunication);
        return mv;
    }

    /*Use For Go Back after Message search Appraisee List For Message Communication*/
    @RequestMapping(value = "appraiseeListForMessageCommunication.htm", params = {"action=Back"}, method = RequestMethod.POST)
    public ModelAndView BackappraiseeListForMessageCommunication(@ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication) {
        ModelAndView mv = new ModelAndView("/par/MessageSentDetail");
        ArrayList parStatusForMessageCommunication = parAdminDAO.getPARStatusDetailListForMessageCommunication(parMessageCommunication.getFiscalyear());
        mv.addObject("parStatusForMessageCommunication", parStatusForMessageCommunication);
        return mv;
    }

    /*Use For Get Pending PAR Report*/
    @RequestMapping(value = "pendingPARReport.htm")
    public ModelAndView pendingPARReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parMessageCommunication") PARMessageCommunication parMessageCommunication,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) {
        ModelAndView mv = new ModelAndView("/par/PARPendingReport");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ArrayList pendingPARReportList = parAdminDAO.getPendingPARReportByAdmin(parAdminSearchCriteria);
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("pendingPARReportList", pendingPARReportList);
        return mv;
    }

    /*Use For Check Initiate PAR For Others*/
    @ResponseBody
    @RequestMapping(value = "checkInitiatePARForOthers.htm")
    public void checkInitiatePARForOthers(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ibean") InitiateOtherPARListBean ibean) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = parAdminDAO.hasPARCreated(ibean.getEmpid(), ibean.getFiscalYear());
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);
        out = response.getWriter();
        out.write(obj.toString());
    }

    @RequestMapping(value = "forceForwardReportForAuthority.htm")
    public ModelAndView forceForwardReportForAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/par/forceForwardDetailReportForAuthority");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getforceForwardListAuthority.htm", method = RequestMethod.POST)
    public void getforceForwardListAuthority(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parForceForwardBean") ParForceForwardBean parForceForwardBean,
            HttpServletResponse response) throws IOException, JSONException {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            ArrayList forceForwardList = parAdminDAO.getForceForwardDetailListForAuthority(parForceForwardBean.getFiscalyear(), lub.getLoginempid());
            json = new JSONArray(forceForwardList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    /*Used to get Cadre Change History*/
    @RequestMapping(value = "parCadreChangeDetail.htm")
    public ModelAndView parCadreChangeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parAdminBean") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = parAdminBean.getFiscalyear();
        //List parDataList = parAdminDAO.getSiParFyWiseCustodianList(fyVal, lub.getLoginoffcode(), lub.getLogindistrictcode());
        mv.addObject("FiscYear", fiscyear);
        mv.addObject("FyYear", fyVal);
        mv.setViewName("/par/CadreChangeDetail");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchCadreChangePARList.htm")
    public void getSearchCadreChangePARList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, HttpServletResponse response, @ModelAttribute("parAdminProperties") ParAdminProperties parAdminProperties) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList cadreChangeparlist = parAdminDAO.getPARCadreChangeDetailList(parAdminProperties.getFiscalyear());
        JSONArray obj = new JSONArray(cadreChangeparlist);
        out = response.getWriter();
        out.write(obj.toString());
        out.flush();
        out.close();

    }

    @RequestMapping(value = "parViewedByDetailList.htm")
    public ModelAndView parViewedByDetailList(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("pARViewDetailLogBean") PARViewDetailLogBean pARViewDetailLogBean) {
        ModelAndView mv = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = pARViewDetailLogBean.getFiscalyear();
        mv.setViewName("/par/PARViewByDetailList");
        mv.addObject("FiscYear", fiscyear);
        mv.addObject("FyYear", fyVal);

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getSearchparViewedByDetailList.htm")
    public void getSearchparViewedByDetailList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            HttpServletResponse response, @ModelAttribute("pARViewDetailLogBean") PARViewDetailLogBean pARViewDetailLogBean) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = pARViewDetailLogBean.getFiscalyear();
        List parListViewed = parAdminDAO.getPARViewDetailLogList(fyVal);
        JSONArray obj = new JSONArray(parListViewed);
        out = response.getWriter();
        out.write(obj.toString());
        out.flush();
        out.close();

    }
}

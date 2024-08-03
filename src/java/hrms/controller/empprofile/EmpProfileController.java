package hrms.controller.empprofile;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.dao.Npsdata.npsDataDAO;
import hrms.dao.createEmployee.CreateEmployeeDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.BankDAO;
import hrms.dao.master.BlockDAO;
import hrms.dao.master.BloodGroupDAO;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.CategoryDAO;
import hrms.dao.master.DegreeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.FacultyDAO;
import hrms.dao.master.FamilyRelationTypeDAO;
import hrms.dao.master.GisTypeDAO;
import hrms.dao.master.IdentityTypeDAO;
import hrms.dao.master.LanguageDAO;
import hrms.dao.master.MaritalStatusDAO;
import hrms.dao.master.PoliceStationDAO;
import hrms.dao.master.PostOfficeDAO;
import hrms.dao.master.QualificationDAO;
import hrms.dao.master.ReligionDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.UniversityDAO;
import hrms.dao.master.VillageDAO;
import hrms.dao.serviceVerification.ServiceVerificationDAO;
import hrms.model.employee.Address;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeLanguage;
import hrms.model.employee.FamilyRelation;
import hrms.model.employee.IdentityInfo;
import hrms.model.employee.PreviousPensionDetails;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.serviceVerification.ServiceVerification;
import hrms.model.upload.UploadedFile;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "users"})
public class EmpProfileController implements ServletContextAware {

    @Autowired
    public EmployeeDAO employeeDAO;
    @Autowired
    public GisTypeDAO gisTypeDAO;
    @Autowired
    public MaritalStatusDAO maritalStatusDAO;
    @Autowired
    public CategoryDAO categoryDAO;
    @Autowired
    public BloodGroupDAO bloodGroupDAO;
    @Autowired
    public ReligionDAO religionDAO;
    @Autowired
    public BankDAO bankDAO;
    @Autowired
    public BranchDAO branchDAO;
    @Autowired
    public LoginDAO loginDAO;
    @Autowired
    public IdentityTypeDAO identityTypeDAO;
    // @Autowired
    // QualificationDAO qualificationDAO;
//    @Autowired
//    FacultyDAO facultyDAO;
//    @Autowired
//    DegreeeDAO degreeeDAO;
    @Autowired
    public QualificationDAO qualificationDAO;
    @Autowired
    public FacultyDAO facultyDAO;
    @Autowired
    public DegreeeDAO degreeeDAO;
    @Autowired
    public UniversityDAO universityDAO;
    @Autowired
    public StateDAO stateDAOImpl;
    @Autowired
    public DistrictDAO districtDAO;
    @Autowired
    public LanguageDAO languageDao;
    @Autowired
    public FamilyRelationTypeDAO familyRelationTypeDAO;
    @Autowired
    public CreateEmployeeDAO createEmployeeDao;
    @Autowired
    public BlockDAO blockDAO;
    @Autowired
    public PoliceStationDAO policeStationDAO;
    @Autowired
    public PostOfficeDAO postOfficeDAO;
    @Autowired
    public VillageDAO villageDAO;
    @Autowired
    public npsDataDAO npsdateDao;
    @Autowired
    public ServiceVerificationDAO serviceVerificationDAO;
    @Autowired
    public CadreDAO cadreDAO;
    @Autowired
    public DepartmentDAO deptDAO;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "downloadAttachmentOfBankProof")
    public void downloadAttachmentOfBankProof(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("filename") String filename) throws IOException {
        ModelAndView mv = new ModelAndView();
        String splitarr[] = filename.split("@");
        String filepath = servletContext.getInitParameter("BankChangeAttachproof");
        Path path = new File(filepath + splitarr[0]).toPath();
        String mimeType = Files.probeContentType(path);

        File f = new File(filepath + splitarr[0]);

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment;filename=" + splitarr[1]);
        OutputStream out = response.getOutputStream();
        out.write(FileUtils.readFileToByteArray(f));
        out.flush();
    }

    @RequestMapping(value = "showbankaccountList.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView showbankaccountList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AqmastModel") AqmastModel aq, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            List li = new ArrayList();
            li = employeeDAO.getbankAccountChangeRequestList(user.getEmpId());
            mv = new ModelAndView("/employee/bankAccountChangeReqList", "AqmastModel", aq);
            mv.addObject("updateList", li);
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "showbankaccountList2Employee.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView showbankaccountList2Employee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AqmastModel") AqmastModel aq) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            List li = new ArrayList();
            li = employeeDAO.getbankAccountChangeRequestList(lub.getLoginempid());
            mv = new ModelAndView("/employee/bankAccountChangeList4Employee", "AqmastModel", aq);
            mv.addObject("updateList", li);
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "updateRequestbankaccountPage.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView bankAccountChangeReqPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AqmastModel") AqmastModel aq, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            Employee emp = employeeDAO.getEmployeeProfile(user.getEmpId());
            aq.setEmpCode(user.getEmpId());
            aq.setBankAccNo(emp.getBankaccno());
            aq.setBankName(emp.getSltBank());
            aq.setBranchName(emp.getSltbranch());

            mv = new ModelAndView("/employee/bankAccountChangeReqPage", "AqmastModel", aq);
            List bankList = bankDAO.getBankList();
            mv.addObject("bankList", bankList);
            List branchList = branchDAO.getBranchList(emp.getSltBank());
            mv.addObject("branchList", branchList);
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "updateRequestbankaccount")
    public ModelAndView submitBankAccountUpdation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("AqmastModel") AqmastModel aq, @ModelAttribute("SelectedEmpObj") Users user) {
        boolean notupdate = true;

        String filepath = servletContext.getInitParameter("BankChangeAttachproof");
        aq.setEmpCode(user.getEmpId());
        aq.setOffDdo(lub.getLoginoffname());
        String isDuplicate = employeeDAO.updateBankAccountNumber(aq, lub.getLoginempid(), filepath);

        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            if (isDuplicate != null && !isDuplicate.equals("")) {
                mv = new ModelAndView("/employee/bankAccountChangeReqPage", "AqmastModel", aq);
                mv.addObject("DUPLICATE", isDuplicate);

                Employee emp = employeeDAO.getEmployeeProfile(user.getEmpId());
                aq.setEmpCode(user.getEmpId());
                aq.setBankAccNo(aq.getBankAccNo());
                //aq.setBankAccNo(emp.getBankaccno());
                aq.setBankName(emp.getSltBank());
                aq.setBranchName(emp.getSltbranch());

                List bankList = bankDAO.getBankList();
                mv.addObject("bankList", bankList);
                List branchList = branchDAO.getBranchList(emp.getSltBank());
                mv.addObject("branchList", branchList);
            } else {
                mv = new ModelAndView("redirect:/showbankaccountList.htm");
                mv.addObject("DUPLICATE", "N");
            }
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "firstpagesb.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView firstpagesb(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        String ifProfileVerified = employeeDAO.isProfileVerified(lub.getLoginempid());

        ModelAndView mv = new ModelAndView("/employee/FirstPageSB");
        mv.addObject("empid", lub.getLoginempid());
        mv.addObject("ifProfileVerified", ifProfileVerified);
        //mv.addObject("SelectedEmpObj", lub.getLoginempid());
        return mv;
    }

    @RequestMapping(value = "employeeProfileView", method = RequestMethod.GET)
    public ModelAndView employeeProfileView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeProfileView");
        mv.addObject("employeeProfile", employeeDAO.getEmployeeProfile(lub.getLoginempid()));
        return mv;
    }

    @RequestMapping(value = "empprofile.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView empProfileData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {
        ModelAndView mv = null;
        emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv = new ModelAndView("/employee/EmployeeProfileEdit", "emp", emp);
        List gisList = gisTypeDAO.getGisTypeList();
        mv.addObject("gisList", gisList);
        List postgrplist = employeeDAO.empPostgrptype();
        mv.addObject("postgrouplist", postgrplist);
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);
        List categoryList = categoryDAO.getCategoryList();
        mv.addObject("categoryList", categoryList);
        List bloodGrpList = bloodGroupDAO.getBloodGroup();
        mv.addObject("bloodGrpList", bloodGrpList);
        List religionList = religionDAO.getReligionList();
        mv.addObject("religionList", religionList);
        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(emp.getSltBank());
        mv.addObject("branchList", branchList);
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "empprofile.htm", method = {RequestMethod.POST}, params = "action=Save")
    public String updateProfile(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, RedirectAttributes redirectAttributes) {

        boolean ifFound = false;
        emp.setOfficecode(lub.getLoginoffcode());
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        ifFound = employeeDAO.duplicateMobile(lub.getLoginempid(), emp.getMobile());
        if (ifFound == true) {
            redirectAttributes.addFlashAttribute("dupMobileNo", "Duplicate Mobile Number Found");
        } else {
            //System.out.println("DDO :" + lub.getLoginAsDDO());
            employeeDAO.updateProfileData(emp, lub.getLoginempid(), isCmpleted);
        }

        return "redirect:/employeeProfileView.htm";
    }

    @RequestMapping(value = "empprofile.htm", method = {RequestMethod.POST}, params = "action=Back")
    public ModelAndView viewProfile(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView("redirect:/employeeProfileView.htm");
        emp.setOfficecode(lub.getLoginoffcode());
        return mv;
    }

    @RequestMapping(value = "employeeLanguageView", method = RequestMethod.GET)
    public ModelAndView employeeLanguageView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeLanguageView");
        EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(lub.getLoginempid());
        mv.addObject("emplanguageList", emplanguageList);
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        return mv;
    }

    @RequestMapping(value = "employeeLanguageEdit", method = RequestMethod.POST)
    public ModelAndView employeeLanguageEdit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("languagemodel") EmployeeLanguage languagemodel) {
        ModelAndView mv = new ModelAndView("/employee/employeeLanguageEdit", "languagemodel", languagemodel);
        EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(lub.getLoginempid());
        mv.addObject("languagelist", languageDao.getLanguageList());
        mv.addObject("emplanguageList", emplanguageList);
        return mv;
    }

    @RequestMapping(value = "editEmployeeSingleLanguage")
    public ModelAndView editEmployeeSingleLanguage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("slno") int slno) {
        ModelAndView mv = null;
        try {
            EmployeeLanguage languagemodel = employeeDAO.editLanguage(lub.getLoginempid(), slno);
            mv = new ModelAndView("/employee/employeeLanguageEdit", "languagemodel", languagemodel);
            EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(lub.getLoginempid());
            mv.addObject("languagelist", languageDao.getLanguageList());
            mv.addObject("emplanguageList", emplanguageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeLanguage.htm")
    public ModelAndView saveEmployeeLanguage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("languagemodel") EmployeeLanguage languagemodel) {
        ModelAndView mv = new ModelAndView("redirect:/employeeLanguageView.htm");
        if (languagemodel.getSlno() > 0) {
            employeeDAO.updateLanguageKnown(lub.getLoginempid(), languagemodel);
        } else {
            employeeDAO.saveLanguageKnown(lub.getLoginempid(), languagemodel);
        }
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "deleteEmployeeLanguage.htm")
    public ModelAndView deleteEmployeeLanguage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("slno") int slno) {
        ModelAndView mv = new ModelAndView("redirect:/employeeLanguageView.htm");
        employeeDAO.deleteLanguageKnown(lub.getLoginempid(), slno);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "employeeIdentityView", method = RequestMethod.GET)
    public ModelAndView employeeIdentityView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeIdentityView");
        List identityInfoList = employeeDAO.getIdentityList(lub.getLoginempid());
        String aadharNo = "";
        for (int i = 0; i < identityInfoList.size(); i++) {
            IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
            if (idInfo.getIdentityDocType().equalsIgnoreCase("AADHAAR")) {
                String temp = idInfo.getIdentityNo();
                aadharNo = temp;
                idInfo.setIdentityNo(CommonFunctions.maskCardNumber(temp, "##xx-xxxx-xx##", 12));
            }
        }
        mv.addObject("identityInfoList", identityInfoList);
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        mv.addObject("oAadharNo", aadharNo);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "GenerateAadharOTP.htm")
    public void GenerateAadharOTP(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("oldAadhar") String oldAadhar, @ModelAttribute("aadhar") String aadharNo) throws IOException, JSONException, NoSuchAlgorithmException, KeyManagementException {
        try {
            //Update Aadhar Number in the database

            int numAadhar = employeeDAO.countDuplicateAadhar(aadharNo, lub.getLoginempid());
            System.out.println("numAadhar" + numAadhar);
            response.setContentType("application/json");
            JSONObject jsObj = new JSONObject();
            if (numAadhar == 0) {
                if (!aadharNo.equals("")) {
                    employeeDAO.UpdateAadhar(lub.getLoginempid(), aadharNo);
                    String url = "https://osrdh.odisha.gov.in/AuthJsonMetaService/generateOTP";
                    String json = "{\"uid\":\"" + aadharNo + "\",\"uidType\":\"A\",\"consent\":\"Y\",\"subAuaCode\":\"PCMGI12310\",\"txn\":null,\"isPI\":null,\"isBio\":null,\"isOTP\":\"y\",\"bioType\":null,\"name\":null,\"rdInfo\":null,\"rdData\":null,\"otpValue\":\"\"}";
                    //String url = "http://164.100.141.79/preauthekycv4/api/authenticate";
                    //String json = "{\"uid\":\"3116606418531\",\"uidType\":\"A\",\"consent\":\"Y\",\"subAuaCode\":\"SCMGI12310\",\"txn\":\"231104114244798:OROCAC\",\"isPI\":\"n\",\"isBio\":\"n\",\"isOTP\":\"y\",\"bioType\":\"\",\"name\":\"\",\"rdInfo\":\"\",\"rdData\":\"\",\"otpValue\":\"320469\"}";        
                    CommonFunctions.doTrustToCertificates();
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(json);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    //System.out.println("Response Code: " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response1 = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response1.append(inputLine);
                    }
                    in.close();
                    JSONObject jObject = new JSONObject(response1.toString());
                    String status = jObject.getString("status");
                    String txn = jObject.getString("txn");
                    System.out.println("Txn: " + txn);
                    String mobileNo = jObject.getString("mobileNumber");
                    String errorMsg = jObject.getString("errMsg");
                    if (status.equals("SUCCESS") && !txn.equals("null")) {
                        jsObj.append("mobileNo", mobileNo);
                        jsObj.append("transactionId", txn);
                        jsObj.append("error", "");
                        employeeDAO.SaveAadharTransaction(lub.getLoginempid(), txn, oldAadhar, aadharNo);
                    } else {
                        jsObj.append("error", "-2");
                        jsObj.append("errorMsg", errorMsg);
                    }
                    //lub.setAadharOTP(null);
                }
            } else {
                jsObj.append("error", "-1");
            }
            PrintWriter out = response.getWriter();
            out.write(jsObj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "AuthenticateAadhar.htm")
    public void AuthenticateAadhar(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("transactionId") String transactionId, @ModelAttribute("otp") String otp, @ModelAttribute("aadhar") String aadhar) throws IOException, JSONException, NoSuchAlgorithmException, KeyManagementException {
        try {
            response.setContentType("application/json");
            JSONObject jsObj = new JSONObject();
            /*jsObj.append("otp", otp);
             jsObj.append("transactionId", transactionId);
             jsObj.append("aadhar", aadhar);*/
            if (!aadhar.equals("") && !transactionId.equals("")) {
                String url = "https://osrdh.odisha.gov.in/AuthJsonMetaService/authenticate";
                String json = "{\"uid\":\"" + aadhar + "\",\"uidType\":\"A\",\"consent\":\"Y\",\"subAuaCode\":\"PCMGI12310\",\"txn\":\"" + transactionId + "\",\"isPI\":\"n\",\"isBio\":\"n\",\"isOTP\":\"y\",\"bioType\":\"\",\"name\":\"\",\"rdInfo\":\"\",\"rdData\":\"\",\"otpValue\":\"" + otp + "\"}";
                CommonFunctions.doTrustToCertificates();
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(json);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                //System.out.println("Response Code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response1 = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response1.append(inputLine);
                }

                in.close();
                JSONObject jObject = new JSONObject(response1.toString());
                String status = jObject.getString("status");
                String uidToken = jObject.getString("uidToken");
                String errorMsg = jObject.getString("errMsg");
                // System.out.println("sTATUS: "+status+ " uidToken: "+uidToken+" Error: "+errorMsg);
                if (status.equals("SUCCESS")) {
                    employeeDAO.UpdateAadharTransaction(lub.getLoginempid(), uidToken, transactionId, aadhar);
                    jsObj.append("error", "");
                } else {
                    jsObj.append("error", errorMsg);
                }
                //lub.setAadharOTP(null);
            }

            PrintWriter out = response.getWriter();
            out.write(jsObj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "employeeIdentityEdit", method = RequestMethod.GET)
    public ModelAndView employeeIdentityEdit(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("identityDocType") String identityDocType) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeIdentityEdit");
        IdentityInfo identity = employeeDAO.getIdentityData(identityDocType, lub.getLoginempid());
        identity.setHidIdentityId(identityDocType);
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        mv.addObject("identity", identity);
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "employeeIdentityNew", method = RequestMethod.POST)
    public ModelAndView employeeIdentityNew(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeIdentityEdit");
        IdentityInfo identity = new IdentityInfo();
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        mv.addObject("identity", identity);
        return mv;
    }

    @RequestMapping(value = "saveEmployeeIdentity", method = RequestMethod.POST, params = "action=Delete")
    public ModelAndView deleteEmployeeIdentity(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("identity") IdentityInfo identity) {
        identity.setEmpId(lub.getLoginempid());
        employeeDAO.deleteIdentity(identity);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        ModelAndView mv = new ModelAndView("redirect:/employeeIdentityView.htm");
        return mv;
    }

    @RequestMapping(value = "saveEmployeeIdentity", method = RequestMethod.POST, params = "action=Update")
    public ModelAndView updateEmployeeIdentity(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("identity") IdentityInfo identity) {

        ModelAndView mv = new ModelAndView();

        identity.setEmpId(lub.getLoginempid());
        boolean isDuplicate = employeeDAO.checkDuplicateIdentity(identity.getEmpId(), identity.getHidIdentityId(), identity.getIdentityDocNo());
        if (isDuplicate == true) {
            identity.setPrintMsg("Entered Identity No already exists for other employee(s).");
            mv.addObject("identity", identity);

            List identityTypeList = identityTypeDAO.getIdentityTypeList();
            mv.addObject("identityTypeList", identityTypeList);

            mv.setViewName("/employee/EmployeeIdentityEdit");
        } else {
            employeeDAO.updateIdentity(identity);
            mv = new ModelAndView("redirect:/employeeIdentityView.htm");
        }

        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeIdentity", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView saveEmployeeIdentity(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("identity") IdentityInfo identity) {

        ModelAndView mv = new ModelAndView();

        identity.setEmpId(lub.getLoginempid());

        boolean isDuplicate = employeeDAO.checkDuplicateIdentity(identity.getEmpId(), identity.getIdentityDocType(), identity.getIdentityDocNo());
        if (isDuplicate == true) {
            identity = new IdentityInfo();
            identity.setPrintMsg("Entered Identity No already exists for other employee(s).");
            mv.addObject("identity", identity);

            List identityTypeList = identityTypeDAO.getIdentityTypeList();
            mv.addObject("identityTypeList", identityTypeList);

            mv.setViewName("/employee/EmployeeIdentityEdit");
        } else {
            identity = employeeDAO.saveIdentity(identity);
            if (identity.getPrintMsg() != null && !identity.getPrintMsg().equals("")) {
                String msg = identity.getPrintMsg();
                identity = new IdentityInfo();
                identity.setPrintMsg(msg);

                mv.addObject("identity", identity);

                List identityTypeList = identityTypeDAO.getIdentityTypeList();
                mv.addObject("identityTypeList", identityTypeList);

                mv.setViewName("/employee/EmployeeIdentityEdit");
            } else {
                mv = new ModelAndView("redirect:/employeeIdentityView.htm");
            }
        }
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeIdentity", method = RequestMethod.POST, params = "action=Back")
    public ModelAndView backEmployeeIdentity(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("identity") IdentityInfo identity) {
        ModelAndView mv = new ModelAndView("redirect:/employeeIdentityView.htm");
        return mv;
    }

    @RequestMapping(value = "employeeAddressView", method = RequestMethod.GET)
    public ModelAndView employeeAddressView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        ModelAndView mv = new ModelAndView("/employee/EmployeeAddressDetail");
        Address empaddress = new Address();
        empaddress.setStateCode("21");
        empaddress.setEmpId(lub.getLoginempid());
        mv.addObject("empId", lub.getLoginempid());
        mv.addObject("stateList", stateDAOImpl.getStateList());
        // mv.addObject("empAddress", empaddress);
        Address permAddress = employeeDAO.getPermanentAddress(lub.getLoginempid());
        mv.addObject("districtList", districtDAO.getDistrictList(permAddress.getStateCode()));
        mv.addObject("blockList", blockDAO.getBlockList(permAddress.getDistCode()));
        mv.addObject("policeList", policeStationDAO.getPoliceStationList(permAddress.getDistCode()));
        mv.addObject("postofficeList", postOfficeDAO.getPostOfficeList(permAddress.getDistCode()));
        mv.addObject("villageList", villageDAO.getVillageList(permAddress.getDistCode(), permAddress.getPsCode()));

        mv.addObject("prDistrictList", districtDAO.getDistrictList(permAddress.getPrStateCode()));
        mv.addObject("prBlockList", blockDAO.getBlockList(permAddress.getPrDistCode()));
        mv.addObject("prPoliceList", policeStationDAO.getPoliceStationList(permAddress.getPrDistCode()));
        mv.addObject("prPostofficeList", postOfficeDAO.getPostOfficeList(permAddress.getPrDistCode()));
        mv.addObject("prVillageList", villageDAO.getVillageList(permAddress.getPrDistCode(), permAddress.getPrPsCode()));

        mv.addObject("empaddress", permAddress);
        System.out.println("DistCode: " + permAddress.getPrDistCode());
        //System.out.println("Block Code:"+permAddress.getPrBlockCode());
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "employeeAddressNew", method = RequestMethod.POST)
    public ModelAndView employeeAddressNew(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeAddressDetail");
        Address empaddress = new Address();
        empaddress.setStateCode("21");
        empaddress.setEmpId(lub.getLoginempid());
        mv.addObject("stateList", stateDAOImpl.getStateList());
        mv.addObject("empaddress", empaddress);
        mv.addObject("districtList", districtDAO.getDistrictList(empaddress.getStateCode()));
        return mv;
    }

    @RequestMapping(value = "editEmployeeAddress", method = RequestMethod.GET)
    public ModelAndView editEmployeeAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("addressId") int addressId) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeAddressDetail");
        Address empaddress = employeeDAO.getAddressDetals(lub.getLoginempid(), addressId);
        mv.addObject("policeList", policeStationDAO.getPoliceStationList(empaddress.getDistCode()));
        mv.addObject("postofficeList", postOfficeDAO.getPostOfficeList(empaddress.getDistCode()));
        mv.addObject("villageList", villageDAO.getVillageList(empaddress.getDistCode(), empaddress.getPsCode()));
        mv.addObject("stateList", stateDAOImpl.getStateList());
        mv.addObject("blockList", blockDAO.getBlockList(empaddress.getDistCode()));
        mv.addObject("empaddress", empaddress);
        mv.addObject("districtList", districtDAO.getDistrictList(empaddress.getStateCode()));
//mv.addObject("prDistrictList", districtDAO.getDistrictList(permAddress.getStateCode()));
        return mv;
    }

    @RequestMapping(value = "saveEmployeeAddress", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView saveEmployeeAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empaddress") Address empaddress) {
        //  ModelAndView mv = new ModelAndView("/employee/EmployeeAddressDetail");
        empaddress.setEmpId(lub.getLoginempid());
        employeeDAO.saveAddress(empaddress);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        ModelAndView mv = new ModelAndView("redirect:/employeeAddressView.htm");
        return mv;
    }

    @RequestMapping(value = "saveEmployeeAddress", method = RequestMethod.POST, params = "action=Cancel")
    public ModelAndView cancelEmployeeAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("redirect:/employeeAddressView.htm");
        return mv;
    }

    @RequestMapping(value = "saveEmployeeAddress", method = RequestMethod.POST, params = "action=Delete")
    public ModelAndView deleteEmployeeAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empaddress") Address empaddress) {
        ModelAndView mv = new ModelAndView("redirect:/employeeAddressView.htm");
        return mv;
    }

    @RequestMapping(value = "employeeFamilyView", method = RequestMethod.GET)
    public ModelAndView employeeFamilyView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeFamilyView");
        FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(lub.getLoginempid());
        mv.addObject("familyRelationList", familyRelationList);
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        return mv;
    }

    @RequestMapping(value = "employeeFamilySave", method = RequestMethod.POST, params = "action=Delete")
    public ModelAndView employeeFamilyDelete(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/employeeFamilyView.htm");
        familyRelation.setEmpId(lub.getLoginempid());
        employeeDAO.deleteEmployeeFamily(familyRelation);
        return mv;
    }

    @RequestMapping(value = "employeeFamilySave", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView employeeFamilySave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/employeeFamilyView.htm");
        if (familyRelation.getEmpId() == null || familyRelation.getEmpId().equals("") || familyRelation.getEmpId().length() == 0) {
            familyRelation.setEmpId(lub.getLoginempid());
            String status = employeeDAO.saveEmployeeFamily(familyRelation);
        } else {
            String status = employeeDAO.updateEmployeeFamily(familyRelation);
        }
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "employeeFamilySave", method = RequestMethod.POST, params = "action=Back")
    public ModelAndView employeeFamilyCancel(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/employeeFamilyView.htm");
        return mv;
    }

    @RequestMapping(value = "employeeFamilyNew", method = RequestMethod.POST)
    public ModelAndView employeeFamilyNew(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeFamilyDetail");
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        FamilyRelation familyRelation = new FamilyRelation();
        mv.addObject("familyRelationTypeList", familyRelationTypeDAO.getFamilyRelationTypeList());
        mv.addObject("familyRelation", familyRelation);
        mv.addObject("titleType", createEmployeeDao.empTitleType());

        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(familyRelation.getSltBank());
        mv.addObject("branchList", branchList);
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);

        return mv;
    }

    @RequestMapping(value = "employeeFamilyDetail", method = RequestMethod.GET)
    public ModelAndView employeeFamilyDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("slno") int slno) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeFamilyDetail");
        FamilyRelation familyRelation = employeeDAO.getFamilyRelationData(slno, lub.getLoginempid());
        familyRelation.setEmpId(lub.getLoginempid());
        mv.addObject("familyRelationTypeList", familyRelationTypeDAO.getFamilyRelationTypeList());
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        mv.addObject("familyRelation", familyRelation);
        mv.addObject("titleType", createEmployeeDao.empTitleType());
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);
        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(familyRelation.getSltBank());
        mv.addObject("branchList", branchList);
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "employeeEducationView", method = RequestMethod.GET)
    public ModelAndView employeeEducationView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeEducationView");
        Education[] educationList = employeeDAO.getEmployeeEducation(lub.getLoginempid());
        mv.addObject("educationList", educationList);
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        return mv;
    }

    @RequestMapping(value = "employeeEducationDetail", method = RequestMethod.GET)
    public ModelAndView employeeEducationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("qfn_id") int qfn_id) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeEducationDetail");
        mv.addObject("education", employeeDAO.getEmployeeEducationDtls(qfn_id));

        Employee employee = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        if (employee.getDob() != null && !employee.getDob().equals("")) {
            int birthYear = Integer.parseInt(StringUtils.right(employee.getDob(), 4));
            Map<String, String> yearofpass = new HashMap<String, String>();
            List li = new ArrayList();
            for (int i = birthYear + 10; i < birthYear + 60; i++) {
                SelectOption so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                //yearofpass.put(i + "", i + "");
                li.add(so);
            }

            mv.addObject("yearofpasslist", li);

            mv.addObject("yearofpasslist", li);

        }

        mv.addObject("degreeList", degreeeDAO.getDegreeList());
        mv.addObject("facultyList", facultyDAO.getSubjectFacultyList());
        mv.addObject("qualificationList", qualificationDAO.getqualificationList());
        mv.addObject("universityList", universityDAO.getUniversityList());
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "employeeEducationNew", method = RequestMethod.POST)
    public ModelAndView employeeEducationNew(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeEducationDetail");
        Employee employee = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        if (employee.getDob() != null && !employee.getDob().equals("")) {
            int birthYear = Integer.parseInt(StringUtils.right(employee.getDob(), 4));
            Map<String, String> yearofpass = new HashMap<String, String>();
            List li = new ArrayList();
            for (int i = birthYear + 10; i < birthYear + 60; i++) {
                SelectOption so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                //yearofpass.put(i + "", i + "");
                li.add(so);
            }

            mv.addObject("yearofpasslist", li);

            mv.addObject("yearofpasslist", li);

        }

        mv.addObject("degreeList", degreeeDAO.getDegreeList());
        mv.addObject("facultyList", facultyDAO.getSubjectFacultyList());
        mv.addObject("qualificationList", qualificationDAO.getqualificationList());
        mv.addObject("universityList", universityDAO.getUniversityList());
        mv.addObject("education", new Education());

        return mv;
    }

    @RequestMapping(value = "saveEmployeeEducation", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView saveEmployeeEducation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("education") Education education) {
        ModelAndView mv = new ModelAndView("redirect:/employeeEducationView.htm");
        education.setEmpId(lub.getLoginempid());
        employeeDAO.saveEmployeeEducation(education);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeEducation", method = RequestMethod.POST, params = "action=Update")
    public ModelAndView UpdateEmployeeEducation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("education") Education education) {
        ModelAndView mv = new ModelAndView("redirect:/employeeEducationView.htm");
        education.setEmpId(lub.getLoginempid());
        employeeDAO.updateEmployeeEducation(education);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeEducation", method = RequestMethod.POST, params = "action=Delete")
    public ModelAndView deleteEmployeeEducation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("education") Education education) {
        ModelAndView mv = new ModelAndView("redirect:/employeeEducationView.htm");
        education.setEmpId(lub.getLoginempid());
        employeeDAO.deleteEmployeeEducation(education);
        boolean isCmpleted = employeeDAO.isprofileCompleted(lub.getLoginempid());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
        }
        return mv;
    }

    @RequestMapping(value = "saveEmployeeEducation", method = RequestMethod.POST, params = "action=Back")
    public ModelAndView backEmployeeEducation(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("education") Education education) {
        ModelAndView mv = new ModelAndView("redirect:/employeeEducationView.htm");
        return mv;
    }

    @RequestMapping(value = "employeePostingView", method = RequestMethod.GET)
    public ModelAndView employeePostingView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/employee/EmployeePostingView");
        List li = employeeDAO.getPostingInformation(lub.getLoginempid());
        mv.addObject("PostingList", li);
        Employee emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        return mv;
    }

    @RequestMapping(value = "employeeFirstPageSBView", method = RequestMethod.GET)
    public ModelAndView employeeFirstPageSBView(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        String ifProfileVerified = employeeDAO.isProfileVerified(lub.getLoginempid());

        ModelAndView mv = new ModelAndView("/employee/FirstPageSB");
        mv.addObject("ifProfileVerified", ifProfileVerified);
        return mv;
    }

    @RequestMapping(value = "employeeFirstPageSBViewDDO.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView firstpagesbddo(@ModelAttribute("SelectedEmpObj") Users user) {

        String ifProfileVerified = employeeDAO.isProfileVerified(user.getEmpId());

        ModelAndView mv = new ModelAndView("/employee/FirstPageSB_DDO");
        mv.addObject("ifProfileVerified", ifProfileVerified);
        mv.addObject("empid", user.getEmpId());
        //mv.addObject("SelectedEmpObj", lub.getLoginempid());
        return mv;
    }

    @RequestMapping(value = "profile.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView profileData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {
        ModelAndView mv = null;

        //   System.out.println("user.getDeptcode()==="+user.getDeptcode());
        emp.setEmpid(user.getEmpId());
        emp.setDeptCode(user.getDeptcode());
        emp = employeeDAO.getEmployeeProfile(user.getEmpId());
        mv = new ModelAndView("/employee/EmployeeProfile", "emp", emp);
        List gisList = gisTypeDAO.getGisTypeList();
        mv.addObject("gisList", gisList);
        List postgrplist = employeeDAO.empPostgrptype();
        mv.addObject("postgrouplist", postgrplist);
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);
        List categoryList = categoryDAO.getCategoryList();
        mv.addObject("categoryList", categoryList);
        List bloodGrpList = bloodGroupDAO.getBloodGroup();
        mv.addObject("bloodGrpList", bloodGrpList);
        List religionList = religionDAO.getReligionList();
        mv.addObject("religionList", religionList);
        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(emp.getSltBank());
        mv.addObject("branchList", branchList);
        mv.addObject("deptCode", user.getDeptcode());
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("deptlist", deptlist);

        List cadreList = cadreDAO.getCadreList(emp.getDeptType());

        // List cadreList = employeeDAO.getCadreList();
        mv.addObject("cadList", cadreList);
        List gradelist = cadreDAO.getGradeList(emp.getCadreType());
        mv.addObject("gradelist", gradelist);

        return mv;
    }

    @RequestMapping(value = "EmployeeProfileAcknowledgement.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView EmployeeProfileAcknowledgement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {

        ModelAndView mv = null;
        try {
            emp.setEmpid(lub.getLoginempid());
            emp.setDeptCode(lub.getLogindeptcode());
            emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
            mv = new ModelAndView("/employee/EmployeeProfileAcknowledgement", "emp", emp);
            String isAadharVerified = "N";
            //Address[] empAddress = employeeDAO.getAddress(lub.getLoginempid());
            Address permAddress = employeeDAO.getPermanentAddressForAcknowledgement(lub.getLoginempid());
            int hasPermAddress = employeeDAO.getAddressCount(lub.getLoginempid(), "PERMANENT");
            int hasPresentAddress = employeeDAO.getAddressCount(lub.getLoginempid(), "PRESENT");
            //mv.addObject("empAddress", empAddress);

            mv.addObject("address", permAddress);
            mv.addObject("hasPermAddress", hasPermAddress);
            mv.addObject("hasPresentAddress", hasPresentAddress);
            List identityInfoList = employeeDAO.getIdentityList(lub.getLoginempid());
            for (int i = 0; i < identityInfoList.size(); i++) {
                IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
                if (idInfo.getIdentityDocType() == "AADHAAR" && idInfo.getIsVerified() == "Y") {
                    isAadharVerified = "Y";
                }
            }
            mv.addObject("identityInfoList", identityInfoList);
            mv.addObject("isAadharVerified", isAadharVerified);
            Education[] educationList = employeeDAO.getEmployeeEducation(lub.getLoginempid());
            mv.addObject("educationList", educationList);
            FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(lub.getLoginempid());
            mv.addObject("familyRelationList", familyRelationList);

            EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(lub.getLoginempid());
            mv.addObject("emplanguageList", emplanguageList);

            Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
            mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "DdoProfileAcknowledgement.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView DdoProfileAcknowledgement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {

        ModelAndView mv = null;
        try {
            emp.setEmpid(lub.getLoginempid());
            emp.setDeptCode(lub.getLogindeptcode());
            emp = employeeDAO.getEmployeeProfile(lub.getLoginempid());
            mv = new ModelAndView("/employee/EmployeeProfileAcknowledgement", "emp", emp);

            //Address[] empAddress = employeeDAO.getAddress(lub.getLoginempid());
            Address permAddress = employeeDAO.getPermanentAddressForAcknowledgement(lub.getLoginempid());
            int hasPermAddress = employeeDAO.getAddressCount(lub.getLoginempid(), "PERMANENT");
            int hasPresentAddress = employeeDAO.getAddressCount(lub.getLoginempid(), "PRESENT");
            //mv.addObject("empAddress", empAddress);
            System.out.println("PermAddr: " + hasPermAddress);
            System.out.println("PresentAddr: " + hasPresentAddress);
            mv.addObject("address", permAddress);
            mv.addObject("hasPermAddress", hasPermAddress);
            mv.addObject("hasPresentAddress", hasPresentAddress);
            List identityInfoList = employeeDAO.getIdentityList(lub.getLoginempid());
            mv.addObject("identityInfoList", identityInfoList);
            Education[] educationList = employeeDAO.getEmployeeEducation(lub.getLoginempid());
            mv.addObject("educationList", educationList);
            FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(lub.getLoginempid());
            mv.addObject("familyRelationList", familyRelationList);

            EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(lub.getLoginempid());
            mv.addObject("emplanguageList", emplanguageList);

            Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
            mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "address.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addressData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("/employee/Address");
        address.setStateCode("21");
        address.setPrStateCode("21");
        address.setEmpId(user.getEmpId());
        mv.addObject("stateList", stateDAOImpl.getStateList());

        //  Address[] empAddress = employeeDAO.getAddress(user.getEmpId());
        Address permAddress = employeeDAO.getPermanentAddress(user.getEmpId());
        mv.addObject("empId", user.getEmpId());
        // mv.addObject("empAddress", empAddress);
        mv.addObject("address", permAddress);

        mv.addObject("districtList", districtDAO.getDistrictList(permAddress.getStateCode()));
        mv.addObject("blockList", blockDAO.getBlockList(permAddress.getDistCode()));
        mv.addObject("policeList", policeStationDAO.getPoliceStationList(permAddress.getDistCode()));
        mv.addObject("postofficeList", postOfficeDAO.getPostOfficeList(permAddress.getDistCode()));
        mv.addObject("villageList", villageDAO.getVillageList(permAddress.getDistCode(), permAddress.getPsCode()));

        mv.addObject("prDistrictList", districtDAO.getDistrictList(permAddress.getStateCode()));
        mv.addObject("prBlockList", blockDAO.getBlockList(permAddress.getPrDistCode()));
        mv.addObject("prPoliceList", policeStationDAO.getPoliceStationList(permAddress.getPrDistCode()));
        mv.addObject("prPostofficeList", postOfficeDAO.getPostOfficeList(permAddress.getPrDistCode()));
        mv.addObject("prVillageList", villageDAO.getVillageList(permAddress.getPrDistCode(), permAddress.getPrPsCode()));

        return mv;
    }

    @RequestMapping(value = "saveAddress.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView saveAddressData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("redirect:/address.htm");
        //  System.out.println("addressEmp Id" + address.getEmpId());
        //  System.out.println("addressEmp Session" + user.getEmpId());
        address.setEmpId(user.getEmpId());
        employeeDAO.saveAddressData(address);
        mv.addObject("empId", user.getEmpId());
        boolean isCmpleted = employeeDAO.isprofileCompleted(user.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(user.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "editAddress", method = RequestMethod.GET)
    public ModelAndView editAddress(@ModelAttribute("SelectedEmpObj") Users user, @RequestParam("addressId") int addressId, @ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("/employee/Address");
        address = employeeDAO.getAddressDetals(user.getEmpId(), addressId);
        address.setEmpId(user.getEmpId());
        mv.addObject("policeList", policeStationDAO.getPoliceStationList(address.getDistCode()));
        mv.addObject("postofficeList", postOfficeDAO.getPostOfficeList(address.getDistCode()));
        mv.addObject("villageList", villageDAO.getVillageList(address.getDistCode(), address.getPsCode()));
        mv.addObject("stateList", stateDAOImpl.getStateList());
        mv.addObject("blockList", blockDAO.getBlockList(address.getDistCode()));
        mv.addObject("address", address);
        Address[] empAddress = employeeDAO.getAddress(user.getEmpId());
        mv.addObject("empAddress", empAddress);
        mv.addObject("districtList", districtDAO.getDistrictList(address.getStateCode()));

        return mv;
    }

    @RequestMapping(value = "editEmplyeeEducaton.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView editEmplyeeEducaton(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("education") Education education) {
        education = employeeDAO.getEmployeeEducationDtls(education.getQfn_id());
        ModelAndView mav = new ModelAndView();
        Education[] educationList = employeeDAO.getEmployeeEducation(user.getEmpId());
        Employee employee = employeeDAO.getEmployeeProfile(user.getEmpId());
        if (employee.getDob() != null && !employee.getDob().equals("")) {
            int birthYear = Integer.parseInt(StringUtils.right(employee.getDob(), 4));
            Map<String, String> yearofpass = new HashMap<String, String>();
            List li = new ArrayList();
            for (int i = birthYear + 10; i < birthYear + 60; i++) {
                SelectOption so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                //yearofpass.put(i + "", i + "");
                li.add(so);
            }
            mav.addObject("yearofpasslist", li);
        }
        mav.addObject("degreeList", degreeeDAO.getDegreeList());
        mav.addObject("facultyList", facultyDAO.getSubjectFacultyList());
        mav.addObject("qualificationList", qualificationDAO.getqualificationList());
        mav.addObject("universityList", universityDAO.getUniversityList());
        mav.addObject("educationList", educationList);
        mav.addObject("education", education);
        mav.addObject("subjectList", qualificationDAO.getsubjectList());
        mav.addObject("deptCode", lub.getLogindeptcode());

        String userType = lub.getLoginusertype();
        String allowUpdate = "";
        if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
            allowUpdate = "Y";
        }
        mav.addObject("AllowUpdate", allowUpdate);

        mav.setViewName("/employee/Education");
        return mav;

    }

    @RequestMapping(value = "family.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView familyData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("familyRelation") FamilyRelation familyRelation, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("/employee/FamilyList");
        FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(user.getEmpId());
        mv.addObject("familyRelationList", familyRelationList);
        Employee emp = employeeDAO.getEmployeeProfile(user.getEmpId());
        mv.addObject("ifProfileCompleted", emp.getIfprofileCompleted());
        return mv;
    }

    @RequestMapping(value = "FamilyNewDdo", method = RequestMethod.POST)
    public ModelAndView employeeFamilyNewDdo(@ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = new ModelAndView("/employee/FamilyDetailDdo");
        FamilyRelation familyRelation = new FamilyRelation();
        //familyRelation.setEmpId(user.getEmpId());
        familyRelation.setMode("New");
        mv.addObject("familyRelationTypeList", familyRelationTypeDAO.getFamilyRelationTypeList());
        mv.addObject("familyRelation", familyRelation);
        mv.addObject("titleType", createEmployeeDao.empTitleType());
        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);
        return mv;
    }

    @RequestMapping("/fileUploadJointPhotograph")
    public ModelAndView getUploadDdoForm(
            @ModelAttribute("uploadedDdoFile") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("employee/JointPhotoUpload");
    }

    @RequestMapping(value = "FamilyDetailDdo", method = RequestMethod.GET)
    public ModelAndView employeeFamilyDetailDdo(@ModelAttribute("SelectedEmpObj") Users user, @RequestParam("slno") int slno) {
        ModelAndView mv = new ModelAndView("/employee/FamilyDetailDdo");

        FamilyRelation familyRelation = employeeDAO.getFamilyRelationData(slno, user.getEmpId());
        familyRelation.setMode("Update");
        mv.addObject("familyRelationTypeList", familyRelationTypeDAO.getFamilyRelationTypeList());
        mv.addObject("familyRelation", familyRelation);
        mv.addObject("titleType", createEmployeeDao.empTitleType());
        List bankList = bankDAO.getBankList();
        mv.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(familyRelation.getSltBank());
        mv.addObject("branchList", branchList);
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        List maritallist = maritalStatusDAO.getMaritalList();
        mv.addObject("maritallist", maritallist);
        return mv;
    }

    @RequestMapping(value = "FamilyDdoSave", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView FamilyDdoSave(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/family.htm");
        familyRelation.setEmpId(user.getEmpId());
        if (familyRelation.getDob().equals("01-Jan-1900")) {
            familyRelation.setDob(null);
        }
        if (familyRelation.getMode().equals("New")) {
            String status = employeeDAO.saveEmployeeFamily(familyRelation);
        } else {
            familyRelation.setIsLocked("N");
            String status = employeeDAO.updateEmployeeFamily(familyRelation);
        }

        boolean isCmpleted = employeeDAO.isprofileCompleted(user.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(user.getEmpId());
        }

        return mv;
    }

    @RequestMapping(value = "FamilyDdoSave", method = RequestMethod.POST, params = "action=Back")
    public ModelAndView employeeFamilyDdoSave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/family.htm");
        return mv;
    }

    @RequestMapping(value = "FamilyDdoSave", method = RequestMethod.POST, params = "action=Delete")
    public ModelAndView FamilyDdoDelete(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("familyRelation") FamilyRelation familyRelation) {
        ModelAndView mv = new ModelAndView("redirect:/family.htm");
        familyRelation.setEmpId(user.getEmpId());
        employeeDAO.deleteEmployeeFamily(familyRelation);
        return mv;
    }

    @RequestMapping(value = "language.htm")
    public ModelAndView languageData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("languagemodel") EmployeeLanguage languagemodel, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("/employee/Language", "languagemodel", languagemodel);
        EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(user.getEmpId());
        mv.addObject("languagelist", languageDao.getLanguageList());
        mv.addObject("emplanguageList", emplanguageList);
        return mv;
    }

    @RequestMapping(value = "savelanguage.htm")
    public ModelAndView saveLanguageData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("languagemodel") EmployeeLanguage languagemodel, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("redirect:/language.htm");
        if (languagemodel.getSlno() > 0) {
            employeeDAO.updateLanguageKnown(user.getEmpId(), languagemodel);
        } else {
            employeeDAO.saveLanguageKnown(user.getEmpId(), languagemodel);
        }
        boolean isCmpleted = employeeDAO.isprofileCompleted(user.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(user.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "employeeProfileDdoView", method = RequestMethod.GET)
    public ModelAndView employeeProfileView(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("emp") Employee emp) {
        ModelAndView mv = new ModelAndView("/employee/EmpProfileDdoView");
        mv.addObject("deptCode", user.getDeptcode());
        mv.addObject("employeeProfile", employeeDAO.getEmployeeProfile(user.getEmpId()));
        Employee empprofilecompletedstatus = employeeDAO.getProfileCompletedStatus(lub.getLoginempid());
        mv.addObject("empprofilecompletedstatus", empprofilecompletedstatus);
        return mv;
    }

    @RequestMapping(value = "employeeAbstractReport", method = RequestMethod.GET)
    public ModelAndView employeeAbstractReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid) throws ParseException {
        int noofdaysofserviceperiod = 0;
        ModelAndView mv = null;
        try {
            List svList = serviceVerificationDAO.findAllSvData(empid);

            mv = new ModelAndView("/employee/EmployeeAbstractReport");
            mv.addObject("employeeProfile", employeeDAO.getEmployeeAbstractReport(empid));
            System.out.println("empidempidempidempid" + empid);
            mv.addObject("payBMacp", employeeDAO.getPayfixationunderMacp(empid));
            mv.addObject("payAMacp", employeeDAO.getPayfixationafterMacp(empid));
            mv.addObject("revisionDate", employeeDAO.getPayrevisionDate(empid));
            mv.addObject("fixationDni", employeeDAO.getPayfixationDni(empid));
            List promotionList = employeeDAO.promotionList(empid);
            mv.addObject("promotionList", promotionList);

            String emplyeedoegov = serviceVerificationDAO.getEmployeeDateOfEntry(empid);
            String emplyeedos = serviceVerificationDAO.getEmployeeDateOfRetirement(empid);

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
                mv.addObject("currentdate2", emplyeedos2);
            } else {
                noofdaysofserviceperiod = serviceVerificationDAO.getQualifyingDays(emplyeedoegov, currentdate1);
                mv.addObject("currentdate2", currentdate2);
            }
            int totalqualifying = 0;
            for (int i = 0; i < svList.size(); i++) {
                ServiceVerification svb = (ServiceVerification) svList.get(i);
                totalqualifying = totalqualifying + svb.getqDays();
            }

            //mav.addObject("ServiceVerifyList", svList);
            mv.addObject("noofdaysofserviceperiod", noofdaysofserviceperiod);
            mv.addObject("nonqualifyingdays", noofdaysofserviceperiod - totalqualifying);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "employeeServiceParticular", method = RequestMethod.GET)
    public ModelAndView employeeServiceParticular(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("emp") Employee emp) {
        ModelAndView mv = new ModelAndView("/employee/EmployeeServiceParticular");
        System.out.println("employee");
        mv.addObject("deptCode", user.getDeptcode());
        mv.addObject("employeeProfile", employeeDAO.getEmployeeServiceParticular(user.getEmpId()));
        List serviceList = employeeDAO.getEmpServiceList(user.getEmpId());
        mv.addObject("serviceList", serviceList);
        return mv;
    }

    @RequestMapping(value = "profile.htm", method = {RequestMethod.POST}, params = "back")
    public ModelAndView viewProfileDdo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView("redirect:/employeeProfileDdoView.htm");
        return mv;
    }

    @RequestMapping(value = "profile.htm", params = "save", method = RequestMethod.POST)
    public String saveProfileDdo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, RedirectAttributes redirectAttributes) {
        boolean ifFound = false;
        boolean isCmpleted = employeeDAO.isprofileCompleted(emp.getEmpid());
        ifFound = employeeDAO.duplicateMobile(emp.getEmpid(), emp.getMobile());
        if (ifFound == true) {
            redirectAttributes.addFlashAttribute("dupMobileNo", "Duplicate Mobile Number Found");
        } else {
            employeeDAO.updateDDOProfileData(emp, emp.getEmpid(), isCmpleted);
        }

        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(emp.getEmpid());
        }

        return "redirect:/employeeProfileDdoView.htm";
    }

    @RequestMapping(value = "getFamilyRelationData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getFamilyRelationData(@RequestParam("slno") int slno, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        FamilyRelation familyRelation = employeeDAO.getFamilyRelationData(slno, user.getEmpId());
        mv = new ModelAndView("/employee/Family", "familyRelation", familyRelation);
//        mv.addObject("familyRelationTypeList", familyRelationTypeDAO.getIdentityTypeList());
        // mv.addObject("titleType", createEmployeeDao.empTitleType());
        FamilyRelation[] familyRelationList = employeeDAO.getEmployeeFamily(user.getEmpId());
        mv.addObject("familyRelationList", familyRelationList);
        return mv;
    }

    @RequestMapping(value = "identity.htm")
    public ModelAndView identityData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("identity") IdentityInfo identity, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("/employee/Identity");
        identity.setEmpId(user.getEmpId());
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        List identityInfoList = employeeDAO.getIdentityList(user.getEmpId());
        mv.addObject("identityInfoList", identityInfoList);

        for (int i = 0; i < identityInfoList.size(); i++) {
            IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
            if (idInfo.getIdentityDocType().equalsIgnoreCase("AADHAAR")) {
                String temp = idInfo.getIdentityNo();
                idInfo.setIdentityNo(CommonFunctions.maskCardNumber(temp, "##xx-xxxx-xx##", 12));
            }
        }

        return mv;
    }

    @RequestMapping(value = "identity.htm", params = "btnAction=Save")
    public ModelAndView saveIdentity(@ModelAttribute("identity") IdentityInfo identity, Map<String, Object> model) {

        ModelAndView mv = null;
        //String idType[] = identity.getHidIdentityId().split(",");

        employeeDAO.saveIdentity(identity);

        boolean isCmpleted = employeeDAO.isprofileCompleted(identity.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(identity.getEmpId());
        }
        mv = new ModelAndView("redirect:/identity.htm");
        return mv;

    }

    @RequestMapping(value = "identity.htm", params = "btnAction=Update")
    public ModelAndView updateIdentityData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("identity") IdentityInfo identity, BindingResult result, Map<String, Object> model, HttpServletResponse response) {

        ModelAndView mv = null;

        if (identity.getHidIdentityId() != null && !identity.getHidIdentityId().equals("")) {
            employeeDAO.updateIdentity(identity);
        }
        boolean isCmpleted = employeeDAO.isprofileCompleted(identity.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(identity.getEmpId());
        }
        mv = new ModelAndView("redirect:/identity.htm");
        return mv;
    }

    @RequestMapping(value = "identity.htm", params = "btnAction=Delete")
    public ModelAndView deleteIdentityData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("identity") IdentityInfo identity, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        ModelAndView mv = null;
        employeeDAO.deleteIdentity(identity);

        boolean isCmpleted = employeeDAO.isprofileCompleted(identity.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(identity.getEmpId());
        }
        mv = new ModelAndView("redirect:/identity.htm");
        return mv;
    }

    @RequestMapping(value = "identity.htm", params = "btnAction=Back")
    public ModelAndView backIdentityData() {
        ModelAndView mv = new ModelAndView("redirect:/identity.htm");
        return mv;
    }

    @RequestMapping(value = "getIdentityData.htm")
    public ModelAndView getIdentityData(@RequestParam("idDesc") String idDesc, @RequestParam("empId") String empId, Map<String, Object> model) {
        ModelAndView mv = null;
        IdentityInfo identity = null;
        String idType[] = idDesc.split(",");
        identity = employeeDAO.getIdentityData(idDesc, empId);
        identity.setEmpId(empId);
        identity.setHidIdentityId(idDesc);
        mv = new ModelAndView("/employee/Identity", "identity", identity);
        List identityTypeList = identityTypeDAO.getIdentityTypeList();
        mv.addObject("identityTypeList", identityTypeList);
        List identityInfoList = employeeDAO.getIdentityList(empId);
        mv.addObject("identityInfoList", identityInfoList);

        for (int i = 0; i < identityInfoList.size(); i++) {
            IdentityInfo idInfo = (IdentityInfo) identityInfoList.get(i);
            if (idInfo.getIdentityDocType().equalsIgnoreCase("AADHAAR")) {
                String temp = idInfo.getIdentityNo();
                idInfo.setIdentityNo(CommonFunctions.maskCardNumber(temp, "##xx-xxxx-xx##", 12));
            }
        }

        return mv;

    }

    @RequestMapping(value = "education.htm")
    public ModelAndView educationData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("education") Education education) {
        ModelAndView mav = null;
        try {
            mav = new ModelAndView("/employee/Education", "education", education);

            education.setEmpId(user.getEmpId());

            Education[] educationList = employeeDAO.getEmployeeEducation(user.getEmpId());
            mav.addObject("educationList", educationList);

            Employee employee = employeeDAO.getEmployeeProfile(user.getEmpId());
            if (employee.getDob() != null && !employee.getDob().equals("")) {
                int birthYear = Integer.parseInt(StringUtils.right(employee.getDob(), 4));
                Map<String, String> yearofpass = new HashMap<String, String>();
                List li = new ArrayList();
                for (int i = birthYear + 10; i < birthYear + 60; i++) {
                    SelectOption so = new SelectOption();
                    so.setLabel(i + "");
                    so.setValue(i + "");
                    //yearofpass.put(i + "", i + "");
                    li.add(so);
                }

                mav.addObject("yearofpasslist", li);

            }

            mav.addObject("degreeList", degreeeDAO.getDegreeList());
            mav.addObject("facultyList", facultyDAO.getSubjectFacultyList());
            mav.addObject("qualificationList", qualificationDAO.getqualificationList());
            mav.addObject("subjectList", qualificationDAO.getsubjectList());
            mav.addObject("universityList", universityDAO.getUniversityList());
            mav.addObject("deptCode", lub.getLogindeptcode());

            String userType = lub.getLoginusertype();
            String allowUpdate = "";
            if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
                allowUpdate = "Y";
            }
            mav.addObject("AllowUpdate", allowUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveeducation", params = "action=Save")
    public ModelAndView saveEducationDDOEnd(@ModelAttribute("education") Education education) {
        ModelAndView mv = new ModelAndView("redirect:/education.htm");

        employeeDAO.saveEmployeeEducation(education);
        boolean isCmpleted = employeeDAO.isprofileCompleted(education.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(education.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "saveeducation", params = "action=Delete")
    public ModelAndView deleteEducationDDOEnd(@ModelAttribute("education") Education education) {

        ModelAndView mv = new ModelAndView("redirect:/education.htm");

        employeeDAO.deleteEmployeeEducation(education);

        boolean isCmpleted = employeeDAO.isprofileCompleted(education.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(education.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "saveeducation", params = "action=Update")
    public ModelAndView updateEducationDDOEnd(@ModelAttribute("education") Education education) {

        ModelAndView mv = new ModelAndView("redirect:/education.htm");

        employeeDAO.updateEmployeeEducation(education);
        boolean isCmpleted = employeeDAO.isprofileCompleted(education.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(education.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "saveeducation", params = "action=Back")
    public ModelAndView backEducationDDOEnd() {
        ModelAndView mv = new ModelAndView("redirect:/education.htm");
        return mv;
    }

    @RequestMapping(value = "editEducatonDDOEnd.htm")
    public ModelAndView editEducatonDDOEnd(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("education") Education education) {

        education = employeeDAO.getEmployeeEducationDtls(education.getQfn_id());

        ModelAndView mav = new ModelAndView("/employee/Education", "education", education);

        Education[] educationList = employeeDAO.getEmployeeEducation(user.getEmpId());
        Employee employee = employeeDAO.getEmployeeProfile(user.getEmpId());
        if (employee.getDob() != null && !employee.getDob().equals("")) {
            int birthYear = Integer.parseInt(StringUtils.right(employee.getDob(), 4));
            Map<String, String> yearofpass = new HashMap<String, String>();
            for (int i = birthYear + 10; i < birthYear + 60; i++) {
                yearofpass.put(i + "", i + "");
            }
            mav.addObject("yearofpasslist", yearofpass);
        }
        mav.addObject("degreeList", degreeeDAO.getDegreeList());
        mav.addObject("facultyList", facultyDAO.getSubjectFacultyList());
        mav.addObject("qualificationList", qualificationDAO.getqualificationList());
        mav.addObject("universityList", universityDAO.getUniversityList());
        mav.addObject("educationList", educationList);
        mav.addObject("education", education);

        mav.setViewName("/employee/Education");
        return mav;
    }

    @RequestMapping(value = "editEmployeeSingleLanguageDDO")
    public ModelAndView editEmployeeSingleLanguageDDO(@ModelAttribute("SelectedEmpObj") Users user, @RequestParam("slno") int slno) {
        ModelAndView mv = null;
        try {
            EmployeeLanguage languagemodel = employeeDAO.editLanguage(user.getEmpId(), slno);
            mv = new ModelAndView("/employee/Language", "languagemodel", languagemodel);
            EmployeeLanguage[] emplanguageList = employeeDAO.getLanguageKnown(user.getEmpId());
            mv.addObject("languagelist", languageDao.getLanguageList());
            mv.addObject("emplanguageList", emplanguageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "deleteEmployeeLanguageDDO.htm")
    public ModelAndView deleteEmployeeLanguageDDO(@ModelAttribute("SelectedEmpObj") Users user, @RequestParam("slno") int slno) {
        ModelAndView mv = new ModelAndView("redirect:/language.htm");
        employeeDAO.deleteLanguageKnown(user.getEmpId(), slno);
        boolean isCmpleted = employeeDAO.isprofileCompleted(user.getEmpId());
        if (isCmpleted == true) {
            //employeeDAO.updateProfileCompletedStatus(user.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "deleteProfileAddress.htm")
    public ModelAndView deleteProfileAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("addressId") int addressId) {
        ModelAndView mv = new ModelAndView("redirect:/employeeAddressView.htm");
        employeeDAO.deleteProfileAddress(addressId);
        return mv;
    }

    @RequestMapping(value = "deleteEmployeeAddress.htm")
    public ModelAndView deleteEmployeeAddress(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("addressId") int addressId) {
        ModelAndView mv = new ModelAndView("redirect:/address.htm");
        employeeDAO.deleteEmployeeAddress(addressId);
        return mv;
    }

    @RequestMapping(value = "unlockProfile.htm")
    public String unlockProfileDDO(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("emp") Employee emp, Map<String, Object> model, RedirectAttributes redirectAttributes) {
        // employeeDAO.unlockProfileDDO(user.getEmpId(), user.getDeptcode());
        return "redirect:/employeeProfileDdoView.htm";
    }

    // code for Previous Pension Details
    @RequestMapping(value = "PreviousPensionDetails")
    public ModelAndView PreviousPensionDetails(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("prevpensionForm") PreviousPensionDetails prevpensionForm) {

        ModelAndView mav = null;
        try {
            List prevPensionlist = employeeDAO.getPrevpensionList(lub.getEmpId());
            mav = new ModelAndView("/employee/PreviousPensionDetails");
            mav.addObject("prevPensionlist", prevPensionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addNewPreviousPension", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addNewPreviousPension(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("prevpensionForm") PreviousPensionDetails prevpensionForm) {
        ModelAndView mav = null;

        mav = new ModelAndView("/employee/addNewPreviousPension");
        List bankList = bankDAO.getBankList();
        mav.addObject("bankList", bankList);
        List branchList = branchDAO.getBranchList(prevpensionForm.getBankName());
        mav.addObject("branchList", branchList);
        ArrayList treasuryList = npsdateDao.getTreasuryList();
        mav.addObject("treasurylist", treasuryList);

        return mav;

    }

    @RequestMapping(value = "saveprevPension", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save PrevPension"})
    public ModelAndView saveprevPension(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("prevpensionForm") PreviousPensionDetails prevpensionForm) {

        ModelAndView mav = null;
        try {

            prevpensionForm.setEmpid(lub.getEmpId());
            // System.out.println("emp cont id:" + prevpensionForm.getPensionId());          
            prevpensionForm.setEntryTakenBy(loginbean.getLoginempid());
            prevpensionForm.setEntryDate(new Date());
            prevpensionForm.setPensionId(prevpensionForm.getPensionId());

            if (prevpensionForm.getPensionId() != null && !prevpensionForm.getPensionId().equals("")) {
                employeeDAO.updatePrevpensionDetails(prevpensionForm);
            } else {
                employeeDAO.savePrevpensionDetails(prevpensionForm);
            }
            mav = new ModelAndView("redirect:/PreviousPensionDetails.htm", "prevpensionForm", prevpensionForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editPrevPension")
    public ModelAndView editPrevPension(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("prevpensionForm") PreviousPensionDetails prevpensionForm) {

        ModelAndView mav = null;
        try {
            prevpensionForm.setEmpid(lub.getEmpId());
            prevpensionForm.setPensionId(prevpensionForm.getPensionId());
            prevpensionForm = employeeDAO.editPrevPensionDetails(prevpensionForm);
            mav = new ModelAndView("/employee/addNewPreviousPension");
            mav.addObject("prevpensionForm", prevpensionForm);
            List bankList = bankDAO.getBankList();
            mav.addObject("bankList", bankList);
            List branchList = branchDAO.getBranchList(prevpensionForm.getBankName());
            mav.addObject("branchList", branchList);
            ArrayList treasuryList = npsdateDao.getTreasuryList();
            mav.addObject("treasurylist", treasuryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteprevPension")
    public ModelAndView deleteprevPension(ModelMap model, HttpServletResponse response, @RequestParam("pensionId") String pensionId) {

        ModelAndView mav = null;
        try {
            employeeDAO.deletePreviousPensionDetails(pensionId);
            mav = new ModelAndView("redirect:/PreviousPensionDetails.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveprevPension", params = "action=Back")
    public ModelAndView BackToListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("prevpensionForm") PreviousPensionDetails prevpensionForm) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/PreviousPensionDetails.htm", "prevpensionForm", prevpensionForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ShowEmployeeMobileNumberList.htm")
    public ModelAndView showEmployeeMobileNumberList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            List li = new ArrayList();
            li = employeeDAO.getMobileNumberChangeList(user.getEmpId());
            emp = employeeDAO.getEmployeeProfile(user.getEmpId());
            mv = new ModelAndView("/employee/EmployeeMobileNumberChangeList", "emp", emp);
            mv.addObject("updateList", li);
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "UpdateEmployeeMobileNumberPage.htm")
    public ModelAndView UpdateEmployeeMobileNumberPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link            
            emp = employeeDAO.getEmployeeProfile(user.getEmpId());
            mv = new ModelAndView("/employee/EmployeeMobileNumberChangePage", "emp", emp);
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "UpdateEmployeeMobileNumber.htm")
    public ModelAndView UpdateEmployeeMobileNumber(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp, @ModelAttribute("SelectedEmpObj") Users user) {
        ModelAndView mv = null;
        if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
            emp.setOfficecode(lub.getLoginoffcode());
            emp.setEmpid(user.getEmpId());
            String isDuplicate = employeeDAO.updateMobileNumber(emp, lub.getLoginempid());
            if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {// Government Employees only can view the link
                if (isDuplicate != null && !isDuplicate.equals("")) {
                    emp = employeeDAO.getEmployeeProfile(user.getEmpId());
                    mv = new ModelAndView("/employee/EmployeeMobileNumberChangePage", "emp", emp);
                    mv.addObject("DUPLICATE", isDuplicate);
                } else {
                    mv = new ModelAndView("redirect:/ShowEmployeeMobileNumberList.htm");
                }
            }
        } else {
            mv = new ModelAndView();
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "SubmitProfileAcknowledgementByEmployee")
    public String SubmitProfileAcknowledgementByEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("emp") Employee emp) {
        try {
            employeeDAO.updateProfileCompletedStatus(lub.getLoginempid());
            employeeDAO.profileCompletedLog(lub.getLoginempid(), lub.getLoginempid(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/EmployeeProfileAcknowledgement.htm";
    }

    @ResponseBody
    @RequestMapping(value = "GenerateAadharOTPCreateEmployee.htm")
    public void GenerateAadharOTPCreateEmployee(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("aadhar") String aadharNo) throws IOException, JSONException, NoSuchAlgorithmException, KeyManagementException {
        try {
            int numAadhar = employeeDAO.countDuplicateAadhar(aadharNo, lub.getLoginempid());
            System.out.println("numAadhar" + numAadhar);
            response.setContentType("application/json");
            JSONObject jsObj = new JSONObject();
            if (numAadhar == 0) {
                if (!aadharNo.equals("")) {
                    String url = "https://osrdh.odisha.gov.in/AuthJsonMetaService/generateOTP";
                    String json = "{\"uid\":\"" + aadharNo + "\",\"uidType\":\"A\",\"consent\":\"Y\",\"subAuaCode\":\"PCMGI12310\",\"txn\":null,\"isPI\":null,\"isBio\":null,\"isOTP\":\"y\",\"bioType\":null,\"name\":null,\"rdInfo\":null,\"rdData\":null,\"otpValue\":\"\"}";
                    //String url = "http://164.100.141.79/preauthekycv4/api/authenticate";
                    //String json = "{\"uid\":\"3116606418531\",\"uidType\":\"A\",\"consent\":\"Y\",\"subAuaCode\":\"SCMGI12310\",\"txn\":\"231104114244798:OROCAC\",\"isPI\":\"n\",\"isBio\":\"n\",\"isOTP\":\"y\",\"bioType\":\"\",\"name\":\"\",\"rdInfo\":\"\",\"rdData\":\"\",\"otpValue\":\"320469\"}";        
                    CommonFunctions.doTrustToCertificates();
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(json);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    //System.out.println("Response Code: " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response1 = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response1.append(inputLine);
                    }
                    in.close();
                    JSONObject jObject = new JSONObject(response1.toString());
                    String status = jObject.getString("status");
                    String txn = jObject.getString("txn");
                    System.out.println("Txn: " + txn);
                    String mobileNo = jObject.getString("mobileNumber");
                    String errorMsg = jObject.getString("errMsg");
                    if (status.equals("SUCCESS") && !txn.equals("null")) {
                        jsObj.append("mobileNo", mobileNo);
                        jsObj.append("transactionId", txn);
                        jsObj.append("error", "");
                    } else {
                        jsObj.append("error", "-2");
                    }
                }
            } else {
                jsObj.append("error", "-1");
            }
            PrintWriter out = response.getWriter();
            out.write(jsObj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

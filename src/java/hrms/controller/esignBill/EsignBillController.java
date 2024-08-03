/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.esignBill;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.billvouchingTreasury.BillDetail;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class EsignBillController {

    @Autowired
    EsignBillDAO esignBillDAO;

    @Autowired
    ScheduleDAO comonScheduleDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @RequestMapping(value = "esignIndex")
    public ModelAndView esignIndex() {
        ModelAndView mv = new ModelAndView();
        HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();
        mv.setViewName("esignBill/esignindex");
        mv.addObject("esign", listTokenData);
        return mv;
    }

    @RequestMapping(value = "decListToken", method = RequestMethod.POST)
    @ResponseBody
    public String decListToken(@RequestParam("encryptedRequestData") String encryptedRequestData, HttpServletResponse response) throws Exception {
        // response.setContentType("application/json");
        PrintWriter out = null;
        //  HashMap app = new HashMap();
        //  app.put("appKey", appkey);
        //  JSONObject job = new JSONObject(app);
        //   String JSON_STRING = job.toString();

        HashMap<String, String> listTokenData = esignBillDAO.decListToken(encryptedRequestData);
        JSONObject job = new JSONObject(listTokenData);

        //ObjectMapper mapper = new ObjectMapper();
        //String json = mapper.writeValueAsString(listTokenData);
        return job.toString();

    }

    @RequestMapping(value = "decListCertificate", method = RequestMethod.POST)
    @ResponseBody
    public String decListCertificate(@RequestParam("encCertificate") String encCertificate,
            @RequestParam("certdisplayname") String certdisplayname) throws Exception {
        HashMap<String, String> listTokenData = esignBillDAO.decListCertificate(encCertificate, certdisplayname);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(listTokenData);
        return json;
    }

    @ResponseBody
    @RequestMapping(value = "generatePDFFile")
    public String generatePDFFile(@RequestParam("responseData") String responseData,
            @RequestParam("tempPath") String tempPath,
            @RequestParam("certdisplayname") String certdisplayname, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @RequestParam("billNo") String billNo, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws Exception {
        String responsedata = esignBillDAO.generatePDFFile(responseData, tempPath, billNo, selectedEmpOffice, lub.getLoginempid());
        HashMap<String, String> msg = new HashMap<>();
        msg.put("msg", responsedata);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msg);
        return json;
    }

    @RequestMapping(value = "esingpdfpages")
    @ResponseBody

    public String digitalSignSinglebillFrontPagePDF(HttpServletResponse response, @RequestParam("billNo") String billNo,
            @RequestParam("encCertificate") String encCertificate,
            @RequestParam("certdisplayname") String certdisplayname, @RequestParam("pass") String pass, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) throws Exception {
        // byte[] pdfinbytearray = salaryBillService.getSinglePageByteArray(billNo);
        String esignLogId = "";
        String offCode = selectedEmpOffice;
        System.out.println("SelectEmpOffice:" + selectedEmpOffice);
        //String dSignDDOdetails = esignBillDAO.ddonameforSign(lub.getLoginoffcode());
        String dSignDDOdetails = esignBillDAO.ddonameforSign(selectedEmpOffice);
        String ddoOfficeName = esignBillDAO.ddoofficeNameforSign(selectedEmpOffice);
        String[] rowArray = dSignDDOdetails.split("\\|");
        String ddoname = rowArray[0];
        String ddopost = rowArray[1];

        //  HashMap<String, String> listTokenData = esignBillDAO.generateEncryptedPDFByte(encCertificate, certdisplayname, pass, offCode, billNo, lub.getLoginname(), lub.getLoginspn(), lub.getLoginoffname());
        HashMap<String, String> listTokenData = esignBillDAO.generateEncryptedPDFByte(encCertificate, certdisplayname, pass, offCode, billNo, ddoname, ddopost, ddoOfficeName);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(listTokenData);

        return json;
    }

    /*@RequestMapping(value = "viewPDFBillDetails", method = RequestMethod.GET)
     public ModelAndView viewPDFBillDetails(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, HttpServletResponse response) {//
     ModelAndView mv = new ModelAndView("esignBill/viewPDFBillDetails");

     //CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
     HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();
     String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
     mv.addObject("esign", listTokenData);
     String[] rowArray = allowEsign.split("\\|");
     String allowEsignStatus = rowArray[0];
     String ddohrmsid = rowArray[1];
     mv.addObject("loginId", lub.getLoginempid());
     mv.addObject("ddohrmsid", ddohrmsid);
     mv.addObject("allowEsign", allowEsignStatus);
     System.out.println("billNo is: "+billNo);
     System.out.println("lub.getLoginoffcode() is: "+lub.getLoginoffcode());
     List unsignedpdfList = esignBillDAO.getPDFBillDetails(billNo, lub.getLoginoffcode());
     mv.addObject("unsignedpdfList", unsignedpdfList);
     mv.addObject("billNo", billNo);
     String checksignStatus = esignBillDAO.checkEsign(lub.getLoginoffcode(), billNo);
     mv.addObject("checksignStatus", checksignStatus);

     return mv;
     }*/
    @RequestMapping(value = "viewPDFBillDetails")
    public ModelAndView viewPDFBillDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @RequestParam Map<String, String> requestParams) {

        ModelAndView mv = new ModelAndView("esignBill/viewPDFBillDetails");

        String billNo = requestParams.get("billNo");
        //String loginoffcode = requestParams.get("loginoffcode");

        HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();
        //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
        BillDetail billdtls = billBrowserDao.getBillDetails(Integer.parseInt(billNo));
        mv.addObject("billStatusId", billdtls.getBillStatusId());
        System.out.println("billStatusId:" + billdtls.getBillStatusId());

        System.out.println("selectedEmpOffice fro DSC:" + selectedEmpOffice);
        String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);

        mv.addObject("esign", listTokenData);
        String[] rowArray = allowEsign.split("\\|");
        String allowEsignStatus = rowArray[0];
        String ddohrmsid = rowArray[1];
        mv.addObject("loginId", lub.getLoginempid());
        mv.addObject("ddohrmsid", ddohrmsid);
        mv.addObject("allowEsign", allowEsignStatus);

        //System.out.println("billNo is: "+billNo);
        //System.out.println("lub.getLoginoffcode() is: "+lub.getLoginoffcode());
        List unsignedpdfList = esignBillDAO.getPDFBillDetails(billNo, selectedEmpOffice);
        System.out.println("List:" + unsignedpdfList.size());
        mv.addObject("unsignedpdfList", unsignedpdfList);
        mv.addObject("billNo", billNo);
        String checksignStatus = esignBillDAO.checkEsign(selectedEmpOffice, billNo);
        mv.addObject("checksignStatus", checksignStatus);
        mv.addObject("typeofbill", billdtls.getTypeOfBill());
        //System.out.println("lub.getLoginempid()+\":\"+ddohrmsid+\":\"+allowEsignStatus+\":\"+checksignStatus:" + lub.getLoginempid() + ":" + ddohrmsid + ":" + allowEsignStatus + ":" + checksignStatus + ":");
        return mv;
    }

    @RequestMapping(value = "deletPdfunsignedLogFile", method = RequestMethod.GET)
    public ModelAndView deletPdfunsignedLogFile(ModelMap model, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, @RequestParam("esignLogId") String esignLogId, HttpServletResponse response) {//
        //  ModelAndView mv = new ModelAndView("esignBill/viewPDFBillDetails");
        esignBillDAO.deletPdfunsignedLogFile(billNo, selectedEmpOffice, esignLogId);
        // return "esignBill/viewPDFBillDetails";
        return new ModelAndView("redirect:/viewPDFBillDetails.htm?billNo=" + billNo);
    }

    @RequestMapping(value = "deletesignedpdf", method = RequestMethod.GET)
    public ModelAndView deletesignedpdf(ModelMap model, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo, @RequestParam("esignLogId") String esignLogId, HttpServletResponse response) {//
        //  ModelAndView mv = new ModelAndView("esignBill/viewPDFBillDetails");
        esignBillDAO.deletesignedpdf(billNo, selectedEmpOffice, esignLogId);
        // return "esignBill/viewPDFBillDetails";
        return new ModelAndView("redirect:/viewPDFBillDetails.htm?billNo=" + billNo);
    }

    @RequestMapping(value = "downlaodBillBrowserPDFFile")
    public void downlaodBillBrowserPDFFile(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @RequestParam("esignLogId") int esignLogId, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {
        FileAttribute fa = null;
        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            //String filepath = servletContext.getInitParameter("lpcPath");
            fa = esignBillDAO.downlaodBillBrowserPDFFile(esignLogId, selectedEmpOffice);
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

    @RequestMapping(value = "downlaodSignedPDFFile")
    public void downlaodSignedPDFFile(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @RequestParam("esignLogId") int esignLogId, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {
        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();
            System.out.println("selectedEmpOffice is: " + selectedEmpOffice);
            //String filepath = servletContext.getInitParameter("lpcPath");
            fa = esignBillDAO.downlaodSignedPDFFile(esignLogId, selectedEmpOffice);
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

    @RequestMapping(value = "downloadSignedPDFFileForTreasury")
    public void downloadSignedPDFFileForTreasury(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @RequestParam("esignLogId") int esignLogId) {
        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();

            //String filepath = servletContext.getInitParameter("lpcPath");
            fa = esignBillDAO.downlaodSignedPDFFileForTreasury(esignLogId);
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

    @RequestMapping(value = "viewArrearPDFBillDetails")
    public ModelAndView viewArrearPDFBillDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @RequestParam Map<String, String> requestParams) {

        ModelAndView mv = null;
        try {
            String billNo = requestParams.get("billNo");
            String frontpageslno = requestParams.get("frontpageslno");

            mv = new ModelAndView("esignBill/viewPDFBillDetails");

            HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();
            BillDetail billdtls = billBrowserDao.getBillDetails(Integer.parseInt(billNo));
            mv.addObject("billStatusId", billdtls.getBillStatusId());
            mv.addObject("typeofbill", billdtls.getTypeOfBill());

            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);

            mv.addObject("esign", listTokenData);
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String ddohrmsid = rowArray[1];
            mv.addObject("loginId", lub.getLoginempid());
            mv.addObject("ddohrmsid", ddohrmsid);
            mv.addObject("allowEsign", allowEsignStatus);

            List unsignedpdfList = esignBillDAO.getArrearPDFBillDetails(billNo, selectedEmpOffice, frontpageslno);
            mv.addObject("unsignedpdfList", unsignedpdfList);
            mv.addObject("billNo", billNo);
            mv.addObject("slno", frontpageslno);
            String checksignStatus = esignBillDAO.checkArrearEsign(selectedEmpOffice, billNo, frontpageslno);
            mv.addObject("checksignStatus", checksignStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}

package hrms.controller.eSignWD;

import hrms.dao.eSignWD.eSignWDDAO;
import static hrms.dao.eSignWD.eSignWDDAOImpl.FILE_SEPARATOR;
import hrms.model.eSignWD.eSignWDForm;
import hrms.model.login.LoginUserBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class eSignWDController {

    @Autowired
    eSignWDDAO eSignWDDAO;

    private String sesskey = "";
    private String loginoffcode = "";
    private String billno = "";
    private String slno = "";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    @RequestMapping(value = "eSignWDList")
    public ModelAndView eSignWDList(@ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("eSignWDForm") eSignWDForm eSignwdform, @RequestParam("billNo") String billNo) {
        ModelAndView mav = new ModelAndView();
        String par1 = "";
        String par2 = "";
        String par3 = "";
        String symmkey = "";
        try {
            List li = eSignWDDAO.generateParameters(billNo, 2, selectedEmpOffice);
            par1 = li.get(0).toString();
            par2 = li.get(1).toString();
            par3 = li.get(2).toString();
            symmkey = li.get(3).toString();
            //lub.setEsignSessKey(symmkey);
            this.sesskey = symmkey;
            this.billno = billNo;
            this.loginoffcode = selectedEmpOffice;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("par1", par1);
        mav.addObject("par2", par2);
        mav.addObject("par3", par3);
        mav.setViewName("/eSignWD/eSignWDList");
        return mav;
    }

    @RequestMapping(value = "eSignArrearWDList")
    public ModelAndView eSignArrearWDList(@ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("eSignWDForm") eSignWDForm eSignwdform, @RequestParam("billNo") String billNo, @RequestParam("slno") String slno) {
        ModelAndView mav = new ModelAndView();
        String par1 = "";
        String par2 = "";
        String par3 = "";
        String symmkey = "";
        try {
            List li = eSignWDDAO.generateParametersArrear(billNo, Integer.parseInt(slno), selectedEmpOffice);
            par1 = li.get(0).toString();
            par2 = li.get(1).toString();
            par3 = li.get(2).toString();
            symmkey = li.get(3).toString();
            //lub.setEsignSessKey(symmkey);
            this.sesskey = symmkey;
            this.billno = billNo;
            this.loginoffcode = selectedEmpOffice;
            this.slno = slno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("par1", par1);
        mav.addObject("par2", par2);
        mav.addObject("par3", par3);
        mav.setViewName("/eSignWD/eSignWDList");
        return mav;
    }

    @RequestMapping(value = "eSignPostResponse")
    public String eSignPostResponse(@RequestParam Map<String, String> requestParams, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        try {
            String fileContent = requestParams.get("Returnvalue");
            String referencenumber = requestParams.get("Referencenumber");
            String transactionnumber = requestParams.get("Transactionnumber");
            System.out.println("FileContent " + fileContent);
            System.out.println("SymmKey" + this.sesskey);
            String finalContent = eSignWDDAO.decryptContent(fileContent, this.sesskey, this.billno, 2);
            mav.addObject("fileContent", fileContent);
            mav.addObject("finalContent", finalContent);
            //mav.setViewName("/eSignWD/eSignPostResponse");
            eSignWDDAO.updateESignFlag(billno, 2, referencenumber, transactionnumber, "ESIGN", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/viewPDFBillDetails.htm?billNo=" + this.billno + "&loginoffcode=" + this.loginoffcode;
    }

    @RequestMapping(value = "eSignPostResponseArrear")
    public String eSignPostResponseArrear(@RequestParam Map<String, String> requestParams, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        try {
            if (this.slno == null || this.slno.equals("")) {
                this.slno = "0";
            }
            String fileContent = requestParams.get("Returnvalue");
            String referencenumber = requestParams.get("Referencenumber");
            String transactionnumber = requestParams.get("Transactionnumber");

            String finalContent = eSignWDDAO.decryptContentArrear(fileContent, this.sesskey, this.billno, Integer.parseInt(this.slno));
            mav.addObject("fileContent", fileContent);
            mav.addObject("finalContent", finalContent);
            //mav.setViewName("/eSignWD/eSignPostResponse");
            eSignWDDAO.updateESignArrearFlag(billno, Integer.parseInt(this.slno), referencenumber, transactionnumber, "ESIGN", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/viewPDFBillDetails.htm?billNo=" + this.billno + "&loginoffcode=" + this.loginoffcode;
    }

    @ResponseBody
    @RequestMapping(value = "UploadESignPDF")
    public void UploadESignPDF(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        String billno = request.getParameter("billno");
        String typeofbill = request.getParameter("typeofbill");
        String slnoarrear = request.getParameter("slno");
        MultipartFile mpf = null;
        String unsignedpath = "";

        try {
            //System.out.println("billno is: "+billno);
            //String unsignedpath = eSignWDDAO.getEsignUnsignedPDFPath(billno, 2);
            System.out.println("this.slno is: "+slnoarrear);
            if (typeofbill != null && typeofbill.contains("ARREAR")) {
                if(slnoarrear != null && !slnoarrear.equals("")){
                    unsignedpath = eSignWDDAO.getEsignUnsignedPDFPath(billno, Integer.parseInt(slnoarrear));
                }
            } else {
                unsignedpath = eSignWDDAO.getEsignUnsignedPDFPath(billno, 2);
            }
            String signedpath = unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR;
            while (itr.hasNext()) {
                mpf = request.getFile(itr.next());
                try {
                    //System.out.println("mpf.getOriginalFilename() is: "+mpf.getOriginalFilename());
                    //FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(context.getRealPath("/resources")+"/"+mpf.getOriginalFilename().replace(" ", "-")));
                    //FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream("E:\\esignpdf\\" + mpf.getOriginalFilename().replace(" ", "-")));
                    FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(signedpath + FILE_SEPARATOR + mpf.getOriginalFilename()));
                    //fileUploadedList.add(mpf.getOriginalFilename().replace(" ", "-"));

                    File file = new File(signedpath + FILE_SEPARATOR + mpf.getOriginalFilename());
                    File rename = null;
                    if (typeofbill != null && typeofbill.contains("ARREAR")) {
                        rename = new File(signedpath + FILE_SEPARATOR + "ArrearBillFrontPage_" + billno + "_signed.pdf");
                    }else{
                        rename = new File(signedpath + FILE_SEPARATOR + "SingleBillFrontPage_" + billno + "_signed.pdf");
                    }
                    file.renameTo(rename);
                    
                    if (typeofbill != null && typeofbill.contains("ARREAR")) {
                        if(slnoarrear != null && !slnoarrear.equals("")){
                            eSignWDDAO.updateESignArrearFlag(billno, Integer.parseInt(slnoarrear), null, null, "ESIGN", mpf.getOriginalFilename());
                        }
                    }else{
                        eSignWDDAO.updateESignFlag(billno, 2, null, null, "ESIGN", mpf.getOriginalFilename());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

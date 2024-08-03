/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.esignBill;

import hrms.model.common.FileAttribute;
import hrms.model.esignBill.EsignBill;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lenovo
 */
public interface EsignBillDAO {

    // @Autowired
    // EsignBillDAO esignBillDAO;
    public HashMap<String, String> getTokenRequestData();

    public HashMap<String, String> decListToken(String encryptedTokenData);

    public HashMap<String, String> decListCertificate(String encryptedCertificateData, String displayName);

    public String generatePDFFile(String responseData, String tempPath, String billNo, String offcode, String loginid);

    // public HashMap<String, String> generateEncryptedPDFByte(byte[] pdfData,String encryptedCertificateData, String displayName, String password);
    public HashMap<String, String> generateEncryptedPDFByte(String encryptedCertificateData, String displayName, String password, String offCode, String billNo, String name, String spn, String officename);

    public String GeneratePDFSignedPath(String offcode, int year, int month, String billno);

    public void savepdflog(String unsignedPdfFilename, String createpathPDF, String slno, String billno, int month, int year, String loginid, String offcode, String billtype);

    public ArrayList<EsignBill> getPDFBillDetails(String billNo, String offcode);

    public void deletPdfunsignedLogFile(String billNo, String offcode, String esignLogId);
    
     public void deletesignedpdf(String billNo, String offcode, String esignLogId);

    public String checkEsign(String offCode, String billno);

    public FileAttribute downlaodBillBrowserPDFFile(int logId, String officecode);
    
    public FileAttribute downlaodSignedPDFFile(int logId, String officecode);    
    
    public FileAttribute downlaodSignedPDFFileForTreasury(int logId);    
    
    public String ddonameforSign(String offCode);
    
    public String ddoofficeNameforSign(String offCode);
    
    public boolean isEsignLogCreated(int month,int year,String billno,String slno);
    
    public ArrayList<EsignBill> getArrearPDFBillDetails(String billNo, String offcode, String slno);
    
    public String checkArrearEsign(String offCode, String billno, String slno);
    
}

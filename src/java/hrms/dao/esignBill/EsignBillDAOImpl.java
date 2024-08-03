/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.esignBill;

import emh.Enum.Coordinates;
import emh.Enum.PageTobeSigned;
import emh.Enum.Token_Status;
import emh.Enum.Token_Type;
import emh.Model.RequestData.ListCertificateRequest;
import emh.Model.RequestData.ListTokenRequest;
import emh.Model.RequestData.PKCSBulkPdfHashSignRequest;
import emh.Model.RequestData.Request;
import emh.Model.ResponseData.BulkSignOutput;
import emh.Model.ResponseData.PKCSCertificate;
import emh.Model.ResponseData.ProviderToken;
import emh.Model.ResponseData.ResponseDataListPKCSCertificate;
import emh.Model.ResponseData.ResponseDataListProviderToken;
import emh.Model.ResponseData.ResponseDataPKCSBulkSign;
import emh.emBridgeLib.emBridge;
import emh.emBridgeLib.emBridgeSignerInput;
import hrms.common.DataBaseFunctions;
import hrms.model.common.FileAttribute;
import hrms.model.esignBill.EsignBill;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author lenovo
 */
public class EsignBillDAOImpl implements EsignBillDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    private String PayBillPDf;

    private String certificatePath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPayBillPDf(String PayBillPDf) {
        this.PayBillPDf = PayBillPDf;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    @Override
    public HashMap<String, String> getTokenRequestData() {

        HashMap<String, String> listTokenData = new HashMap<>();
        try {
            emBridge bridge = EmBridgeSingleTon.getEmBridge();
            ListTokenRequest listTokenRequest = new ListTokenRequest();
            listTokenRequest.setTokenStatus(Token_Status.CONNECTED);
            listTokenRequest.setTokenType(Token_Type.HARDWARE); //Token_Type.ALL for windows certificate            
            Request data = bridge.encListToken(listTokenRequest);
            String r1 = data.getEncryptedData();
            String r2 = data.getEncryptionKeyID();
            listTokenData.put("encryptedData", r1);
            listTokenData.put("encryptedKeyID", r2);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return listTokenData;
    }

    @Override
    public HashMap<String, String> decListToken(String encryptedTokenData) {
        HashMap<String, String> descTokenData = new HashMap<>();
        try {
            emBridge bridge = EmBridgeSingleTon.getEmBridge();

            String responsedata = encryptedTokenData;
            //responsedata = responsedata.replaceAll("\\s+", "+");
            ResponseDataListProviderToken responseDataListProviderToken = bridge.decListToken(responsedata);
            List<ProviderToken> tokens = responseDataListProviderToken.getTokens();
            String displayname = "";
            for (ProviderToken token : tokens) {
                displayname = token.getKeyStoreDisplayName();

            }

            ListCertificateRequest listCertRequest = new ListCertificateRequest();
            listCertRequest.setKeyStoreDisplayName(displayname);
            Request data = bridge.encListCertificate(listCertRequest);
            // System.out.println("EncryptedData:" + data.getEncryptedData());
            // System.out.println("EncryptionKeyID:" + data.getEncryptionKeyID());
            // System.out.println("displayname:" + displayname);
            descTokenData.put("encryptedData", data.getEncryptedData());
            descTokenData.put("encryptionKeyID", data.getEncryptionKeyID());
            descTokenData.put("displayname", displayname);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return descTokenData;
    }

    @Override
    public HashMap<String, String> decListCertificate(String encryptedCertificateData, String displayName) {
        HashMap<String, String> descCertificateData = new HashMap<>();
        String displayname = "";
        String resultData = "";
        String certKeyId = "";
        String certcommonname = "";
        String certData = "";
        try {
            emBridge bridge = EmBridgeSingleTon.getEmBridge();
            String responsedata = encryptedCertificateData;
            //responsedata = responsedata.replaceAll("\\s+", "+");

            ResponseDataListPKCSCertificate responseDataListPKCSCertificate = bridge.decListCertificate(responsedata, null);
            //System.out.println(responseDataListPKCSCertificate.getCertificates().size());
            List<PKCSCertificate> certificates = responseDataListPKCSCertificate.getCertificates();
            for (PKCSCertificate cert : certificates) {
                certKeyId = cert.getKeyId();
                certcommonname = cert.getCommonName();
                certData = cert.getCertificateData();

            }

            //emBridgeSignerInput input = new emBridgeSignerInput(pdfStr, "SinglePage", "Bangalore", "Distribution", "eMudhra Limited", true, PageTobeSigned.Last, Coordinates.BottomMiddle, "", false);
            List<emBridgeSignerInput> inputs = new ArrayList<>();
            // Get All pdf files from provided folder
           /* Iterator<Path> paths = Files.newDirectoryStream(Paths.get("E:\\esignpdf\\"),
             path -> path.toString().endsWith(".pdf")).iterator();
             String s = null;
             while (paths.hasNext()) {
             Path path = paths.next();
             File pdf = path.toFile();
             InputStream pdfFile = new FileInputStream(pdf);
             byte[] pdfBytes = new byte[(int) pdf.length()];
             pdfFile.read(pdfBytes, 0, pdfBytes.length);
             pdfFile.close();
             String pdfStr = Base64.encodeBase64String(pdfBytes);
             emBridgeSignerInput input = new emBridgeSignerInput(pdfStr, pdf.getName(), "Bangalore", "Distribution", "eMudhra Limited", true, PageTobeSigned.Last, Coordinates.BottomMiddle, "", false);
             // emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,pdf.getName(), "Bangalore", "Distribution", "eMudhra Limited",true, Coordinates.BottomRight, "1,2,3");
             //emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,pdf.getName(), "", "","eMudhra Limited", true, "1-425,100,545,160;2-225,725,345,785","",false);
             inputs.add(input);

             }*/
            PKCSBulkPdfHashSignRequest pKCSBulkPdfHashSignRequest = new PKCSBulkPdfHashSignRequest();
            // Add data to PKCSBulkPdfHashSignRequest's Object
            pKCSBulkPdfHashSignRequest.setBulkInput(inputs);
            pKCSBulkPdfHashSignRequest.setTempFolder("../");
            pKCSBulkPdfHashSignRequest.setKeyStoreDisplayName(displayName); //token name
            pKCSBulkPdfHashSignRequest.setKeyStorePassphrase("9437634989");//token's password
            pKCSBulkPdfHashSignRequest.setKeyId(certKeyId);//certificates id
            //  System.out.println("certKeyId:" + certKeyId);
            // Generate encrrypted request for PKCSBulkSign API Call
            Request bulkPKCSSignRequest = bridge.encPKCSBulkSign(pKCSBulkPdfHashSignRequest);
            //  System.out.println("error" + bulkPKCSSignRequest.getErrorMsg() + bulkPKCSSignRequest.getErrorCode());
            //  System.out.println("getEncryptionKeyID:" + bulkPKCSSignRequest.getEncryptionKeyID());
            descCertificateData.put("encryptedData", bulkPKCSSignRequest.getEncryptedData());
            descCertificateData.put("encryptionKeyID", bulkPKCSSignRequest.getEncryptionKeyID());
            descCertificateData.put("tempFilePath", bulkPKCSSignRequest.getTempFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descCertificateData;
    }

    @Override
    // public HashMap<String, String> generateEncryptedPDFByte(byte[] pdfBytes, String encryptedCertificateData, String displayName, String password) {
    public HashMap<String, String> generateEncryptedPDFByte(String encryptedCertificateData, String displayName, String password, String offCode, String billNo, String name, String spn, String officename) {
        HashMap<String, String> descCertificateData = new HashMap<>();
        String msg = "S";
        String certKeyId = "";
        String certcommonname = "";
        String certData = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            con = this.dataSource.getConnection();

            emBridge bridge = EmBridgeSingleTon.getEmBridge();
            String responsedata = encryptedCertificateData;
            ResponseDataListPKCSCertificate responseDataListPKCSCertificate = bridge.decListCertificate(responsedata, null);
            //System.out.println(responseDataListPKCSCertificate.getCertificates().size());
            List<PKCSCertificate> certificates = responseDataListPKCSCertificate.getCertificates();
            for (PKCSCertificate cert : certificates) {
                certKeyId = cert.getKeyId();
                certcommonname = cert.getCommonName();
                certData = cert.getCertificateData();
            }

            /* File pdf = new File(unsigned_pdf_file);
             InputStream pdfFile = new FileInputStream(pdf);
             byte[] pdfBytes = new byte[(int) pdf.length()];
             pdfFile.read(pdfBytes, 0, pdfBytes.length);
             pdfFile.close();
             String pdfStr = Base64.encodeBase64String(pdfBytes);*/
            // System.out.println("pdfStr==="+pdfStr);
            //  String pdfStr = Base64.encodeBase64String(pdfBytes);
            //    String pdfStr = "";
            //  List<emBridgeSignerInput> inputs = new ArrayList<>();
            // Get All pdf files from provided folder
          /*  Iterator<Path> paths = Files.newDirectoryStream(Paths.get(unsigned_pdf_path),
             path -> path.toString().endsWith(".pdf")).iterator();
             String s = null;
             while (paths.hasNext()) {
             Path path = paths.next();
             File pdf = path.toFile();
             InputStream pdfFile = new FileInputStream(pdf);
             byte[] pdfBytes = new byte[(int) pdf.length()];
             pdfFile.read(pdfBytes, 0, pdfBytes.length);
             pdfFile.close();
             String pdfStr = Base64.encodeBase64String(pdfBytes);
             emBridgeSignerInput input = new emBridgeSignerInput(pdfStr, pdf.getName(), name, spn + " , " + officename, "HRMS ODISHA", true, PageTobeSigned.Last, Coordinates.BottomMiddle, "", false);
             // emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,pdf.getName(), "Bangalore", "Distribution", "eMudhra Limited",true, Coordinates.BottomRight, "1,2,3");
             //emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,pdf.getName(), "", "","eMudhra Limited", true, "1-425,100,545,160;2-225,725,345,785","",false);
             inputs.add(input);

             }*/
            String unsigned_pdf_path = "";
            String unsigned_pdf_file = "";
            String signed_pdf_path = "";
            List<emBridgeSignerInput> inputs = new ArrayList<>();
            pstmt = con.prepareStatement("select unsigned_pdf_path,unsigned_pdf_file FROM esign_log  where  bill_no=?");
            //  pstmt = con.prepareStatement("select unsigned_pdf_path,unsigned_pdf_file FROM esign_log   ");
            pstmt.setInt(1, Integer.parseInt(billNo));
            //pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                unsigned_pdf_path = rs.getString("unsigned_pdf_path");
                unsigned_pdf_file = rs.getString("unsigned_pdf_path") + File.separator + rs.getString("unsigned_pdf_file");
                File pdf = new File(unsigned_pdf_file);
                if (pdf.exists()) {
                    InputStream pdfFile = new FileInputStream(pdf);
                    byte[] pdfBytes = new byte[(int) pdf.length()];
                    pdfFile.read(pdfBytes, 0, pdfBytes.length);
                    pdfFile.close();
                    String pdfStr = Base64.encodeBase64String(pdfBytes);
                    emBridgeSignerInput input = new emBridgeSignerInput(pdfStr, pdf.getName(), "HRMS ODISHA", spn + " ," + officename, name, true, PageTobeSigned.Last, Coordinates.BottomRight, "", false);
                    inputs.add(input);
                }

            }
            signed_pdf_path = unsigned_pdf_path + File.separator + "signed";
            File thesubDirbillid = new File(signed_pdf_path);
            if (!thesubDirbillid.exists()) {
                thesubDirbillid.mkdirs();
            }

            PKCSBulkPdfHashSignRequest pKCSBulkPdfHashSignRequest = new PKCSBulkPdfHashSignRequest();
            // Add data to PKCSBulkPdfHashSignRequest's Object
            pKCSBulkPdfHashSignRequest.setBulkInput(inputs);
            pKCSBulkPdfHashSignRequest.setTempFolder("../");
            pKCSBulkPdfHashSignRequest.setKeyStoreDisplayName(displayName); //token name
            //pKCSBulkPdfHashSignRequest.setKeyStorePassphrase("9437634989");//token's password
            pKCSBulkPdfHashSignRequest.setKeyStorePassphrase(password);//token's password
            pKCSBulkPdfHashSignRequest.setKeyId(certKeyId);//certificates id
            System.out.println("certKeyId:" + certKeyId);
            // Generate encrrypted request for PKCSBulkSign API Call
            Request bulkPKCSSignRequest = bridge.encPKCSBulkSign(pKCSBulkPdfHashSignRequest);
            System.out.println("error" + bulkPKCSSignRequest.getErrorMsg() + bulkPKCSSignRequest.getErrorCode());
            System.out.println("getEncryptionKeyID:" + bulkPKCSSignRequest.getEncryptionKeyID());
            descCertificateData.put("encryptedData", bulkPKCSSignRequest.getEncryptedData());
            descCertificateData.put("encryptionKeyID", bulkPKCSSignRequest.getEncryptionKeyID());
            descCertificateData.put("tempFilePath", bulkPKCSSignRequest.getTempFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return descCertificateData;
    }

    @Override
    public String generatePDFFile(String responseData, String tempPath, String billNo, String offcode, String loginid) {
        String resultData = "S";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            emBridge bridge = EmBridgeSingleTon.getEmBridge();
            ResponseDataPKCSBulkSign apiResponse = bridge.decPKCSBulkSign(responseData, tempPath);
            esignPDF(apiResponse, tempPath, billNo, offcode, loginid);
        } catch (Exception e) {
            resultData = "F";
            e.printStackTrace();
        }
        return resultData;
    }

    public void esignPDF(ResponseDataPKCSBulkSign serviceResponse, String tempFilePath, String billNo, String offcode, String loginid) throws FileNotFoundException, IOException {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());

        String filePath = tempFilePath;
        File tempFile = new File(filePath);
        SimpleDateFormat tsFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date(System.currentTimeMillis() + 3 * 60 * 1000);
        String timeStamp = tsFormat.format(now);
        String sdf = new SimpleDateFormat("ddMMMyyyyHHmmss").format(now);
        int count = 1;
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select unsigned_pdf_path,unsigned_pdf_file,id_esign_log FROM esign_log  where  bill_no=? ");
            pstmt.setInt(1, Integer.parseInt(billNo));
            // pstmt.setString(2, offcode);
            rs = pstmt.executeQuery();
            String unsigned_pdf_path = "";
            String unsigned_pdf_file = "";
            String signed_pdf_path = "";
            String signed_pdf_file = "";
            ArrayList<String> al = new ArrayList<String>();
            HashMap<String, Integer> hMap = new HashMap<>();
            int eLogId = 0;
            while (rs.next()) {
                int esignLogId = rs.getInt("id_esign_log");
                unsigned_pdf_path = rs.getString("unsigned_pdf_path");
                unsigned_pdf_file = rs.getString("unsigned_pdf_file");
                signed_pdf_path = rs.getString("unsigned_pdf_path") + File.separator + "signed";
                // signed_pdf_file = unsigned_pdf_file.replace("unsigned", "signed");
                hMap.put(unsigned_pdf_file, esignLogId);

            }
            for (BulkSignOutput doc : serviceResponse.getBulkSignItems()) {
                byte[] signedDocBytes = Base64.decodeBase64(doc.getSignedData());
                //String finalFilePath = signed_pdf_path + "\\" + signed_pdf_file;
                //System.out.println("finalFilePath===" + finalFilePath);
                eLogId = hMap.get(doc.getId());
                System.out.println("ID: " + doc.getId());
                signed_pdf_file = doc.getId().replace("unsigned", "signed");
                String finalFilePath = signed_pdf_path + File.separator + signed_pdf_file;
                System.out.println("signed_pdf_file: " + signed_pdf_file);
                File file = new File(finalFilePath);
                OutputStream os = new FileOutputStream(file);
                os.write(signedDocBytes);
                os.close();
                count++;
                //signed_pdf_file=doc.getId();
                pstmt1 = con.prepareStatement("UPDATE esign_log SET signed_pdf_path=?,signed_pdf_file=?,esign_pdf_date=?,signed_login_id=? WHERE id_esign_log=?");
                pstmt1.setString(1, signed_pdf_path);
                pstmt1.setString(2, signed_pdf_file);
                pstmt1.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pstmt1.setString(4, loginid);
                pstmt1.setInt(5, eLogId);
                pstmt1.executeUpdate();
            }

            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public String GeneratePDFSignedPath(String offcode, int year, int month, String billno) {
        String result = "";
        String ddo_hrmsid = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String pdfpath = PayBillPDf;
            //String pdfpath = certificatePath;
            // System.out.println("pdfpath======="+PayBillPDf);
            //  String pdfPath1 = servletContext.getInitParameter("PayBillPDf");
            String fy = year + "-" + (year + 1);
            File theDir = new File(pdfpath + fy);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            //String yearfolder=fy+Integer.toString(year);
            File thesubDiryear = new File(pdfpath + fy + File.separator + year);
            if (!thesubDiryear.exists()) {
                thesubDiryear.mkdirs();
            }
            File thesubDirmonth = new File(pdfpath + fy + File.separator + year + File.separator + month);
            if (!thesubDirmonth.exists()) {
                thesubDirmonth.mkdirs();
            }
            File thesubDiroffice = new File(pdfpath + fy + File.separator + year + File.separator + month + File.separator + offcode);
            if (!thesubDiroffice.exists()) {
                thesubDiroffice.mkdirs();
            }

            File thesubDirbillid = new File(pdfpath + fy + File.separator + year + File.separator + month + File.separator + offcode + File.separator + billno);
            if (!thesubDirbillid.exists()) {
                thesubDirbillid.mkdirs();
            }
            File thesubDirunsigned = new File(pdfpath + fy + File.separator + year + File.separator + month + File.separator + offcode + File.separator + billno + File.separator + "unsigned");
            if (!thesubDirunsigned.exists()) {
                thesubDirunsigned.mkdirs();
            }
            System.out.println("pdfpath:" + pdfpath);
            result = pdfpath + fy + File.separator + year + File.separator + month + File.separator + offcode + File.separator + billno + File.separator + "unsigned";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public void savepdflog(String unsignedPdfFilename, String createpathPDF, String slno, String billno, int month, int year, String loginid, String offcode, String billtype) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());
        try {
            String pdffilepath = createpathPDF + File.separator + unsignedPdfFilename;
            
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select bill_no,status from esign_log where  month=? AND year=?  AND bill_no=? AND report_ref_slno=?");
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            //pstmt.setString(3, offcode);
            pstmt.setInt(3, Integer.parseInt(billno));
            pstmt.setInt(4, Integer.parseInt(slno));
            rs = pstmt.executeQuery();
            String bill_id = "";
            if (rs.next()) {
                bill_id = rs.getString("bill_no");
            }
            if (bill_id.equals("")) {
                pst = con.prepareStatement("INSERT INTO esign_log(office_code,month,year,unsigned_pdf_path,create_pdf_date,status,bill_no,report_ref_slno,login_id,unsigned_pdf_file,ESIGN_BILL_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, offcode);
                pst.setInt(2, month);
                pst.setInt(3, year);
                pst.setString(4, createpathPDF);
                pst.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(6, "Generate PDF");
                pst.setInt(7, Integer.parseInt(billno));
                pst.setInt(8, Integer.parseInt(slno));
                pst.setString(9, loginid);
                pst.setString(10, unsignedPdfFilename);
                pst.setString(11, billtype);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList<EsignBill> getPDFBillDetails(String billNo, String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<EsignBill> pdflist = new ArrayList();
        String pdflink = "";
        String signedfilecheck = "N";
        try {
            con = this.dataSource.getConnection();
            // con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT id_esign_log,unsigned_pdf_path,unsigned_pdf_file,bill_no,signed_pdf_path FROM esign_log WHERE  BILL_NO=? AND office_code=? AND report_ref_slno=? order by report_ref_slno");
            pstmt.setInt(1, Integer.parseInt(billNo));
            pstmt.setString(2, offcode);
            pstmt.setInt(3, 2);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                pdflink = rs.getString("unsigned_pdf_path") + File.separator + rs.getString("unsigned_pdf_file");
                EsignBill bb = new EsignBill();
                bb.setEsignLogId(rs.getInt("id_esign_log"));
                bb.setUnsignedpath(pdflink);
                bb.setUnsignedFile(rs.getString("unsigned_pdf_file"));
                bb.setBillNo(rs.getString("bill_no"));
                if (rs.getString("signed_pdf_path") != null && !rs.getString("signed_pdf_path").equals("")) {
                    signedfilecheck = "Y";
                }
                //  System.out.println("signedfilecheck==="+signedfilecheck);
                bb.setSignedStatus(signedfilecheck);

                pdflist.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pdflist;
    }

    @Override
    public void deletPdfunsignedLogFile(String billNo, String offcode, String esignLogId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isDeleted = false;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT unsigned_pdf_path,unsigned_pdf_file,bill_no FROM esign_log WHERE  BILL_NO=?  AND id_esign_log=? AND signed_pdf_file IS  NULL ");
            pstmt.setInt(1, Integer.parseInt(billNo));

            pstmt.setInt(2, Integer.parseInt(esignLogId));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                File f = null;
                String dirpath = rs.getString("unsigned_pdf_path") + File.separator + rs.getString("unsigned_pdf_file");
                // System.out.println("dirpath==="+dirpath);
                f = new File(dirpath);
                if (f.exists()) {

                    isDeleted = f.delete();
                }
                //  System.out.println("isDeleted==="+isDeleted);
            }
            //  if (isDeleted) {
            pstmt = con.prepareStatement("DELETE FROM   esign_log WHERE  BILL_NO=? AND office_code=? AND id_esign_log=? AND signed_pdf_file IS  NULL ");
            pstmt.setInt(1, Integer.parseInt(billNo));
            pstmt.setString(2, offcode);
            pstmt.setInt(3, Integer.parseInt(esignLogId));
            pstmt.executeUpdate();
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        //return retval2;
    }

    @Override
    public void deletesignedpdf(String billNo, String offcode, String esignLogId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isDeleted = false;
        try {
            String status = "N";
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT bill_status_id FROM bill_mast WHERE  BILL_NO=? ");
            pstmt.setInt(1, Integer.parseInt(billNo));

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String statustype = rs.getString("bill_status_id");
                //   System.out.println("statustype==="+statustype);
                if ((statustype.equals("2") || statustype.equals("4") || statustype.equals("8")) && !statustype.equals("")) {
                    status = "Y";
                }
            }
            //  System.out.println("sttaus==="+status);
            if (status.equals("Y")) {
                pstmt = con.prepareStatement("UPDATE  esign_log SET signed_pdf_file=null,signed_pdf_path=null WHERE  BILL_NO=? AND office_code=? AND id_esign_log=? ");
                pstmt.setInt(1, Integer.parseInt(billNo));
                pstmt.setString(2, offcode);
                pstmt.setInt(3, Integer.parseInt(esignLogId));
                pstmt.executeUpdate();
            }
            //  if (isDeleted) {

            //  }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        //return retval2;
    }

    @Override
    public String checkEsign(String offCode, String billno) {
        String result = "N";
        String ddo_hrmsid = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            //   System.out.println("offCode==="+offCode);
            //  System.out.println("billno==="+billno);
            ps = con.prepareStatement("SELECT id_esign_log FROM esign_log WHERE office_code = ? AND bill_no=? AND report_ref_slno=? AND signed_pdf_file IS NOT NULL");
            ps.setString(1, offCode);
            ps.setInt(2, Integer.parseInt(billno));
            ps.setInt(3, 2);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public FileAttribute downlaodBillBrowserPDFFile(int logId, String officecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT unsigned_pdf_path,unsigned_pdf_file FROM esign_log  WHERE office_code=? AND id_esign_log=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, officecode);
            pst.setInt(2, logId);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = rs.getString("unsigned_pdf_path") + File.separator + rs.getString("unsigned_pdf_file");;
                //  System.out.println("dirpath==="+dirpath);
                f = new File(dirpath);
                if (f.exists()) {
                    // System.out.println("Here");
                    // fa.setDiskFileName(rs.getString(""));
                    fa.setOriginalFileName(rs.getString("unsigned_pdf_file"));
                    fa.setFileType("application/pdf");
                    fa.setUploadAttachment(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public FileAttribute downlaodSignedPDFFile(int logId, String officecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT signed_pdf_path,signed_pdf_file FROM esign_log  WHERE office_code=? AND id_esign_log=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, officecode);
            pst.setInt(2, logId);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = rs.getString("signed_pdf_path") + File.separator + rs.getString("signed_pdf_file");;
                // System.out.println("dirpath==="+dirpath);
                f = new File(dirpath);
                if (f.exists()) {
                    // System.out.println("Here");
                    // fa.setDiskFileName(rs.getString(""));
                    fa.setOriginalFileName(rs.getString("signed_pdf_file"));
                    fa.setFileType("application/pdf");
                    fa.setUploadAttachment(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public String ddonameforSign(String offCode) {
        String result = "N";
        String ddo_hrmsid = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,post FROM  emp_mast \n"
                    + "LEFT OUTER JOIN G_OFFICE \n"
                    + "ON  g_office.ddo_hrmsid=emp_mast.emp_id\n"
                    + "LEFT OUTER JOIN G_POST ON G_OFFICE.DDO_POST = G_POST.POST_CODE  \n"
                    + "WHERE g_office.off_code=?");
            ps.setString(1, offCode);

            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("post") != null && !rs.getString("post").equals("")) {
                    result = rs.getString("FULL_NAME") + "|" + rs.getString("post");
                }
                if (rs.getString("post") == null || rs.getString("post").equals("")) {
                    result = rs.getString("FULL_NAME") + "|" + "";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public FileAttribute downlaodSignedPDFFileForTreasury(int logId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT signed_pdf_path,signed_pdf_file FROM esign_log WHERE id_esign_log=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, logId);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = rs.getString("signed_pdf_path") + File.separator + rs.getString("signed_pdf_file");;
                // System.out.println("dirpath==="+dirpath);
                f = new File(dirpath);
                if (f.exists()) {
                    // System.out.println("Here");
                    // fa.setDiskFileName(rs.getString(""));
                    fa.setOriginalFileName(rs.getString("signed_pdf_file"));
                    fa.setFileType("application/pdf");
                    fa.setUploadAttachment(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public String ddoofficeNameforSign(String offCode) {
        String offName = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT OFF_EN FROM G_OFFICE WHERE OFF_CODE=? ");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                offName = rs.getString("OFF_EN");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offName;
    }

    @Override
    public boolean isEsignLogCreated(int month, int year, String billno, String slno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isCreated = false;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select bill_no,status from esign_log where month=? AND year=? AND bill_no=? AND report_ref_slno=?");
            pst.setInt(1, month);
            pst.setInt(2, year);
            pst.setInt(3, Integer.parseInt(billno));
            pst.setInt(4, Integer.parseInt(slno));
            rs = pst.executeQuery();
            if (rs.next()) {
                isCreated = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isCreated;
    }

    @Override
    public ArrayList<EsignBill> getArrearPDFBillDetails(String billNo, String offcode, String slno) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList<EsignBill> pdflist = new ArrayList();
        String pdflink = "";
        String signedfilecheck = "N";
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT id_esign_log,unsigned_pdf_path,unsigned_pdf_file,bill_no,signed_pdf_path FROM esign_log WHERE BILL_NO=? AND office_code=? AND report_ref_slno=? order by report_ref_slno");
            pstmt.setInt(1, Integer.parseInt(billNo));
            pstmt.setString(2, offcode);
            if (slno != null && !slno.equals("")) {
                pstmt.setInt(3, Integer.parseInt(slno));
            } else {
                pstmt.setInt(3, 0);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                pdflink = rs.getString("unsigned_pdf_path") + File.separator + rs.getString("unsigned_pdf_file");
                EsignBill bb = new EsignBill();
                bb.setEsignLogId(rs.getInt("id_esign_log"));
                bb.setUnsignedpath(pdflink);
                bb.setUnsignedFile(rs.getString("unsigned_pdf_file"));
                bb.setBillNo(rs.getString("bill_no"));
                if (rs.getString("signed_pdf_path") != null && !rs.getString("signed_pdf_path").equals("")) {
                    signedfilecheck = "Y";
                }
                bb.setSignedStatus(signedfilecheck);
                pdflist.add(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pdflist;
    }

    @Override
    public String checkArrearEsign(String offCode, String billno, String slno) {

        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;

        String result = "N";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT id_esign_log FROM esign_log WHERE office_code = ? AND bill_no=? AND report_ref_slno=? AND signed_pdf_file IS NOT NULL");
            pst.setString(1, offCode);
            pst.setInt(2, Integer.parseInt(billno));
            if (slno != null && !slno.equals("")) {
                pst.setInt(3, Integer.parseInt(slno));
            } else {
                pst.setInt(3, 0);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                result = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }
}

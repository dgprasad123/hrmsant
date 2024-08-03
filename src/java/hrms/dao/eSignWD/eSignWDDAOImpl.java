package hrms.dao.eSignWD;

import hrms.common.DataBaseFunctions;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import oracle.net.aso.g;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.emcastle.crypto.DataLengthException;
import org.emcastle.crypto.engines.AESEngine;
import org.emcastle.crypto.paddings.PKCS7Padding;
import org.emcastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.emcastle.crypto.params.KeyParameter;
import org.json.JSONException;
import org.json.JSONObject;

public class eSignWDDAOImpl implements eSignWDDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    private String certificatePath;
    private String authToken;

    static String JCE_PROVIDER = "BC";

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static byte[] GenerateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //KeyGenerator kgen = KeyGenerator.getInstance("AES", JCE_PROVIDER);
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey key = kgen.generateKey();
        // System.out.println("key="+key.getEncoded());
        byte[] symmKey = key.getEncoded();
        return symmKey;
    }

    public static byte[] EncryptUsingSessionKey(byte[] skey, byte[] data) throws InvalidCipherTextException, DataLengthException {
        byte[] result = null;
        try {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());
            cipher.init(true, new KeyParameter(skey));
            // 
            int outputSize = cipher.getOutputSize(data.length);
            byte[] tempOP = new byte[outputSize];
            int processLen = cipher.processBytes(data, 0, data.length, tempOP, 0);
            int outputLen = cipher.doFinal(tempOP, processLen);
            result = new byte[processLen + outputLen];
            System.arraycopy(tempOP, 0, result, 0, result.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] GenerateSha256Hash(byte[] message) {
        String algorithm = "SHA-256";
        String SECURITY_PROVIDER = "BC";
        byte[] hash = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            hash = digest.digest(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public byte[] EncryptUsingPublicKey(byte[] data) throws IOException, GeneralSecurityException, Exception {

        // encrypt the session key with the public key
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PublicKey publicKey = null;
        //byte[] bytevalue = readBytesFromFile("E:\\esignpdf\\certificate.cer");
        byte[] bytevalue = readBytesFromFile(this.certificatePath + "certificate.cer");
        //byte[] bytevalue = readBytesFromFile("/data/hrms/DigiPayBillPDf/certificate.cer");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        InputStream streamvalue = new ByteArrayInputStream(bytevalue);
        java.security.cert.Certificate certificate = certificateFactory.generateCertificate(streamvalue);
        publicKey = certificate.getPublicKey();
        Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        pkCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encSessionKey = pkCipher.doFinal(data);
        return encSessionKey;
    }

    private static byte[] readBytesFromFile(String string) throws IOException {
        File f = new File(string);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        return keyBytes;
    }

    public static byte[] decryptText1(byte[] text, byte[] key) throws Exception {
        byte[] dec_Key = new byte[16];
        byte[] decodedValue = new byte[16];

        dec_Key = Base64.getDecoder().decode(key);

        decodedValue = Base64.getDecoder().decode(text);

        SecretKey dec_Key_original = new SecretKeySpec(dec_Key, 0, dec_Key.length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, dec_Key_original);
        byte[] bytePlainText = aesCipher.doFinal(decodedValue);
        return bytePlainText;
    }

    public String GenerateRandomNumber() {
        String randomNum = "";
        Random rand = new Random();
        int e;
        int i;
        int g = 10;
        HashSet<Integer> randomNumbers = new HashSet<Integer>();

        for (i = 0; i < g; i++) {
            e = rand.nextInt(20);
            randomNumbers.add(e);
            if (randomNumbers.size() <= 10) {
                if (randomNumbers.size() == 10) {
                    g = 10;
                }
                g++;
                randomNumbers.add(e);
            }

        }
        Object[] arrayItem = randomNumbers.toArray();
        for (int j = 0; j < randomNumbers.size(); j++) {
            randomNum += arrayItem[j].toString();
        }
        return randomNum;
    }

    @Override
    public List generateParameters(String billno, int refslno, String loginoffcode) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();

        String unsignedpath = "";
        String unsignedfile = "";
        String signedpath = "";
        try {
            con = this.dataSource.getConnection();

            String ddoname = getDDOName(loginoffcode);
            System.out.println("ddoname is: " + ddoname);

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
                unsignedfile = rs.getString("unsigned_pdf_file");
            }
            System.out.println("authToken is: " + this.authToken);
            //System.out.println("Before TRY Pdf Data: ");
            //byte[] inFileBytes = Files.readAllBytes(Paths.get("E:\\esignpdf\\AuditRecoveryPDF_sog.pdf"));
            byte[] inFileBytes = Files.readAllBytes(Paths.get(unsignedpath + FILE_SEPARATOR + unsignedfile));
            byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
            String pdfInBase64 = new String(encoded);
            System.out.println("Pdf Data: " + pdfInBase64);
            //System.out.println(args[0]);
            String encType = "Encrypt";
            JSONObject object = new JSONObject();
            object.put("Name", ddoname);
            object.put("FileType", "PDF");
            object.put("SignatureType", 0);
            object.put("SignatureMode", "2");
            object.put("SelectPage", "FIRST");
            object.put("SignaturePosition", "Bottom-Left");
            //object.put("AuthToken", "1950b13e-5b1b-4f53-bd9e-348f34dca622");
            object.put("AuthToken", this.authToken);
            object.put("File", pdfInBase64);
            object.put("PreviewRequired", true);
            /*object.put("SUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponse.htm");
             object.put("FUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponse.htm");
             object.put("CUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponse.htm");*/
            object.put("SUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponse.htm");
            object.put("FUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponse.htm");
            object.put("CUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponse.htm");
            object.put("ReferenceNumber", GenerateRandomNumber());
            object.put("Enableuploadsignature", true);
            object.put("Enablefontsignature", true);
            object.put("EnableDrawSignature", true);
            object.put("EnableeSignaturePad", true);
            object.put("IsCompressed", false);
            object.put("IsCosign", true);
            object.put("EnableViewDocumentLink", true);
            object.put("Storetodb", true);
            object.put("IsGSTN", true);
            object.put("IsGSTN3B", false);
            String jsonString = object.toString(2);
            String jData1 = jsonString;
            byte[] bytesJson = jData1.getBytes();
            byte[] symmKey = GenerateSessionKey();
            byte[] step2 = EncryptUsingSessionKey(symmKey, bytesJson);
            String par2 = Base64.getEncoder().encodeToString(step2);
            byte[] step3 = GenerateSha256Hash(bytesJson);
            String hash64 = Base64.getEncoder().encodeToString(step3);
            byte[] step4 = EncryptUsingSessionKey(symmKey, step3);
            String par3 = Base64.getEncoder().encodeToString(step4);
            byte[] par1Bytes = EncryptUsingPublicKey(symmKey);
            String par1 = Base64.getEncoder().encodeToString(par1Bytes);
            String symmkeyString = Base64.getEncoder().encodeToString(symmKey);
            li.add(par1);
            li.add(par2);
            li.add(par3);
            li.add(symmkeyString);
            //li.add(symmKey);
            //System.out.println("<input name=\"Parameter1\" type=\"hidden\" value=\""+par1+"\" />");
            //System.out.println("<input name=\"Parameter2\" type=\"hidden\" value=\""+par2+"\" />");
            //System.out.println("<input name=\"Parameter3\" type=\"hidden\" value=\""+par3+"\" />");
            System.out.println("Par1 " + li.get(0).toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String decryptContent(String fileContent, String symmkey, String billno, int refslno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String finalContent = "";

        String unsignedpath = "";
        String unsignedfile = "";
        String signedpath = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
                unsignedfile = rs.getString("unsigned_pdf_file");
            }
            //Call decryptText1 for decryption
            byte[] decrypytedvalue = decryptText1(fileContent.getBytes(), symmkey.getBytes());

            //Convert the decryptedvalue to base64 string
            String finaloutput = Base64.getEncoder().encodeToString(decrypytedvalue);
            System.out.println("Final Output: " + finaloutput);
            //Write the output in a file
            //System.out.println(finaloutput);
            finalContent = finaloutput;
            File signedDirectory = new File(unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            if (!signedDirectory.exists()) {
                signedDirectory.mkdir();
            }
            //FileUtils.copyFileToDirectory(new File(unsignedpath + "\\" + unsignedfile), signedDirectory);
            //Path decryptpath = Paths.get("E:\\esignpdf\\outputdecrypt\\testesign.pdf");
            Path decryptpath = Paths.get(signedDirectory + unsignedfile);
            //Path decryptpath = Paths.get(unsignedpath+"/signed/SingleBillFrontPage_55091972_unsigned.pdf");            
            Files.write(decryptpath, Base64.getDecoder().decode(finaloutput));
            Files.move(decryptpath, decryptpath.resolveSibling("SingleBillFrontPage_" + billno + "_signed.pdf"));
            FileUtils.copyFileToDirectory(new File(unsignedpath + FILE_SEPARATOR + "SingleBillFrontPage_" + billno + "_signed.pdf"), signedDirectory);
            //File file = new File("E:\\esignpdf\\outputdecrypt\\testesign.pdf");
            //FileOutputStream fos = new FileOutputStream(file);
            //byte[] decoder = Base64.getDecoder().decode(finaloutput);
            //fos.write(decoder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalContent;
    }

    @Override
    public void updateESignFlag(String billno, int refslno, String referenceno, String transactiono, String signaturetype, String uploadedfilename) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String unsignedpath = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
            }

            sql = "update esign_log set signature_type=?,reference_no=?,transaction_no=?,signed_pdf_path=?,signed_pdf_file=?,original_filename=? where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, signaturetype);
            pst.setString(2, referenceno);
            pst.setString(3, transactiono);
            pst.setString(4, unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            //pst.setString(4, unsignedpath);
            pst.setString(5, "SingleBillFrontPage_" + billno + "_signed.pdf");
            pst.setString(6, uploadedfilename);
            pst.setInt(7, Integer.parseInt(billno));
            pst.setInt(8, refslno);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private String getDDOName(String loginoffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String ddoname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME FROM EMP_MAST"
                    + " INNER JOIN G_OFFICE ON EMP_MAST.EMP_ID=G_OFFICE.DDO_HRMSID WHERE G_OFFICE.OFF_CODE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loginoffcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                ddoname = rs.getString("EMP_NAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoname;
    }

    @Override
    public String getEsignUnsignedPDFPath(String billno, int reportrefslno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String unsignedpath = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, reportrefslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
            }
            //unsignedpath = "E:\\esignpdf";
            File signedDirectory = new File(unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            if (!signedDirectory.exists()) {
                signedDirectory.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return unsignedpath;
    }

    @Override
    public void updateESignFlagForUpload(String billno, int refslno, String referenceno, String transactiono, String signaturetype, String uploadedfilename) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String unsignedpath = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
            }

            sql = "update esign_log set signature_type=?,reference_no=?,transaction_no=?,signed_pdf_path=?,signed_pdf_file=? where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, signaturetype);
            pst.setString(2, referenceno);
            pst.setString(3, transactiono);
            pst.setString(4, unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            pst.setString(5, uploadedfilename);
            pst.setInt(6, Integer.parseInt(billno));
            pst.setInt(7, refslno);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List generateParametersArrear(String billno, int refslno, String loginoffcode) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();

        String unsignedpath = "";
        String unsignedfile = "";
        String signedpath = "";
        try {
            con = this.dataSource.getConnection();

            String ddoname = getDDOName(loginoffcode);

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
                unsignedfile = rs.getString("unsigned_pdf_file");
            }

            byte[] inFileBytes = Files.readAllBytes(Paths.get(unsignedpath + FILE_SEPARATOR + unsignedfile));
            byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
            String pdfInBase64 = new String(encoded);

            String encType = "Encrypt";
            JSONObject object = new JSONObject();
            object.put("Name", ddoname);
            object.put("FileType", "PDF");
            object.put("SignatureType", 0);
            object.put("SignatureMode", "2");
            object.put("SelectPage", "FIRST");
            object.put("SignaturePosition", "Bottom-Left");

            object.put("AuthToken", this.authToken);
            object.put("File", pdfInBase64);
            object.put("PreviewRequired", true);
            /*object.put("SUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponseArrear.htm");
             object.put("FUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponseArrear.htm");
             object.put("CUrl", "http://localhost:8080/HRMSOpenSource/eSignPostResponseArrear.htm");*/
            object.put("SUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponseArrear.htm");
            object.put("FUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponseArrear.htm");
            object.put("CUrl", "https://apps.hrmsodisha.gov.in/eSignPostResponseArrear.htm");
            object.put("ReferenceNumber", GenerateRandomNumber());
            object.put("Enableuploadsignature", true);
            object.put("Enablefontsignature", true);
            object.put("EnableDrawSignature", true);
            object.put("EnableeSignaturePad", true);
            object.put("IsCompressed", false);
            object.put("IsCosign", true);
            object.put("EnableViewDocumentLink", true);
            object.put("Storetodb", true);
            object.put("IsGSTN", true);
            object.put("IsGSTN3B", false);
            String jsonString = object.toString(2);
            String jData1 = jsonString;
            byte[] bytesJson = jData1.getBytes();
            byte[] symmKey = GenerateSessionKey();
            byte[] step2 = EncryptUsingSessionKey(symmKey, bytesJson);
            String par2 = Base64.getEncoder().encodeToString(step2);
            byte[] step3 = GenerateSha256Hash(bytesJson);
            String hash64 = Base64.getEncoder().encodeToString(step3);
            byte[] step4 = EncryptUsingSessionKey(symmKey, step3);
            String par3 = Base64.getEncoder().encodeToString(step4);
            byte[] par1Bytes = EncryptUsingPublicKey(symmKey);
            String par1 = Base64.getEncoder().encodeToString(par1Bytes);
            String symmkeyString = Base64.getEncoder().encodeToString(symmKey);
            li.add(par1);
            li.add(par2);
            li.add(par3);
            li.add(symmkeyString);
            System.out.println("Par1 " + li.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String decryptContentArrear(String fileContent, String symmkey, String billno, int refslno) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String finalContent = "";

        String unsignedpath = "";
        String unsignedfile = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
                unsignedfile = rs.getString("unsigned_pdf_file");
            }
            //Call decryptText1 for decryption
            byte[] decrypytedvalue = decryptText1(fileContent.getBytes(), symmkey.getBytes());

            //Convert the decryptedvalue to base64 string
            String finaloutput = Base64.getEncoder().encodeToString(decrypytedvalue);
            System.out.println("Final Output: " + finaloutput);
            //Write the output in a file            
            finalContent = finaloutput;
            File signedDirectory = new File(unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            if (!signedDirectory.exists()) {
                signedDirectory.mkdir();
            }

            //Path decryptpath = Paths.get(unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            Path decryptpath = Paths.get(signedDirectory + unsignedfile);
            Files.write(decryptpath, Base64.getDecoder().decode(finaloutput));
            Files.move(decryptpath, decryptpath.resolveSibling("ArrearBillFrontPage_" + billno + "_signed.pdf"));
            FileUtils.copyFileToDirectory(new File(unsignedpath + FILE_SEPARATOR + "ArrearBillFrontPage_" + billno + "_signed.pdf"), signedDirectory);
            //File file = new File(signedDirectory + FILE_SEPARATOR + unsignedfile);
            //File rename = new File(signedDirectory + FILE_SEPARATOR + "ArrearBillFrontPage_" + billno + "_signed.pdf");
            //file.renameTo(rename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalContent;
    }

    @Override
    public void updateESignArrearFlag(String billno, int refslno, String referenceno, String transactiono, String signaturetype, String uploadedfilename) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String unsignedpath = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from esign_log where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billno));
            pst.setInt(2, refslno);
            rs = pst.executeQuery();
            if (rs.next()) {
                unsignedpath = rs.getString("unsigned_pdf_path");
            }

            sql = "update esign_log set signature_type=?,reference_no=?,transaction_no=?,signed_pdf_path=?,signed_pdf_file=?,original_filename=? where bill_no=? and report_ref_slno=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, signaturetype);
            pst.setString(2, referenceno);
            pst.setString(3, transactiono);
            pst.setString(4, unsignedpath + FILE_SEPARATOR + "signed" + FILE_SEPARATOR);
            //pst.setString(4, unsignedpath);
            pst.setString(5, "ArrearBillFrontPage_" + billno + "_signed.pdf");
            pst.setString(6, uploadedfilename);
            pst.setInt(7, Integer.parseInt(billno));
            pst.setInt(8, refslno);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

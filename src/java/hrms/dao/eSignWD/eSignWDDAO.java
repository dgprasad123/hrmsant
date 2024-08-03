package hrms.dao.eSignWD;

import java.util.ArrayList;
import java.util.List;

public interface eSignWDDAO {
    public List generateParameters(String billno,int refslno,String loginoffcode) throws Exception;
    public String decryptContent(String fileContent, String symmkey,String billno,int refslno);
    
    public void updateESignFlag(String billno,int refslno,String referenceno,String transactiono,String signaturetype,String uploadedfilename);
    
    public void updateESignFlagForUpload(String billno,int refslno,String referenceno,String transactiono,String signaturetype,String uploadedfilename);
    
    public String getEsignUnsignedPDFPath(String billno, int reportrefslno);
    
    public List generateParametersArrear(String billno,int refslno,String loginoffcode) throws Exception;
    
    public String decryptContentArrear(String fileContent, String symmkey,String billno,int refslno);
    
    public void updateESignArrearFlag(String billno,int refslno,String referenceno,String transactiono,String signaturetype,String uploadedfilename);
}

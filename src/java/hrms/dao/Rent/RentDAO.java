/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.Rent;

import hrms.model.Rent.IfmsTransactionBean;
import hrms.model.Rent.QuarterBean;
import hrms.model.Rent.ScrollMain;
import hrms.model.employee.Address;
import hrms.model.login.Users;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Manoj PC
 */
public interface RentDAO {
    public Users getRentEmpDetail(String empId);
    public void saveNDCApplication(QuarterBean qtBean, String empId, String empSpc, String dirPath);
    public void saveNDCRentApplication(QuarterBean qtBean, String empId, String empSpc);
    public QuarterBean getNdcApplicationDetail(String empId);
    public QuarterBean getAuthApplicationDetail(String applicationId);
    public void downloadNDCDocument(HttpServletResponse response, String filePath, int applicationId, String fileType);
    public List applicationList(String empId);
    public List getAuthList();
    public void saveNDCAuthority(QuarterBean qtBean, String empId, String gpc);
    public String getRentOfficer();
    public void updateNDCStatus(QuarterBean qtBean);
    public void saveFinalNDC(QuarterBean qtBean, String dirPath);
    public void downloadFinalNDC(HttpServletResponse response, String filePath, int applicationId, String fileType);
    public void savePSAApplication(QuarterBean qtBean, String empId, String spc);
    public List psaApplicationList(String empId);
    public List psaAdminApplicationList();
    public Users getGPFEmpDetail(String empId);
    public String getEncryptedText(String empId, String keyFilePath, String returnPath, String recoveryAmount);
    public String getDecryptedText(String encryptedText, String keyFilePath);
    public IfmsTransactionBean updateTransaction(String decText, String applicationId, String empId);
    public List getQuarterUnitAreaList();
    public List getEmpPaymentTransactions(String empId, String applicationId);
    public List getTypeWiseBuildingNo(String qtrUnits, String qtrType);
    public Address getRentEmpAddress(String empId);
    public QuarterBean getAppQuarterDetail(String empId);
    public QuarterBean getLedgerAmount(String empId);
    public List disposedApplicationList(String empId);
    public void saveScrollData(ScrollMain sMain);
    public List getUnitAreawiseQuarterTypeRent(String unitArea);
    public String getNdcFileId(int applicationId);
    public int modifyNDCPassword(String empId, String oldpwd, String newpwd);
}

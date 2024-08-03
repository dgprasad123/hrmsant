package hrms.dao.transfer;

import hrms.common.Message;
import hrms.model.login.LoginUserBean;
import hrms.model.notification.NotificationBean;
import hrms.model.transfer.TransferForm;
import java.sql.SQLException;
import java.util.List;

public interface TransferDAO {

    public void saveTransfer(TransferForm transferForm, int notid, String loginempid, String loginuserid);

    public void saveTransferDeputation(TransferForm transferForm, int notid, String loginempid, String loginuserid);

    public void updateTransfer(TransferForm transferForm, String loginempid, String loginuserid);

    public void updateTransferDeputation(TransferForm transferForm, String loginempid, String loginuserid);

    public void deleteTransfer(TransferForm transferForm);

    public int getTransferListCount(String empid);

    public List getTransferList(String empid);

    public TransferForm getEmpTransferData(TransferForm trform, int notificationId) throws SQLException;

    public List getPostList(String deptCode, String offCode);

    public int getPostListCount(String deptCode, String offCode);

    public String getCadreCode(String empid);

    public LoginUserBean[] getTransferListOfficeWise(String offcode, int year, int month);

    public void saveCancelTransfer(NotificationBean nb, TransferForm trform, int notid);

    public TransferForm getCancelTransferData(TransferForm trform, int notificationId) throws SQLException;

    public TransferForm getEmpSupersedeTransferData(TransferForm trform, int notificationId) throws SQLException;

    public void SaveSupersedeTransfer(TransferForm transferForm, int notid, String loginempid, String loginuserid);

    public void UpdateSupersedeTransfer(TransferForm transferForm, String loginempid, String loginuserid);
}

package hrms.dao.payroll.aqdtls;

import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.billbrowser.AqDtlsDedBean;
import java.util.List;

public interface AqDtlsDAO {

    public int saveAqdtlsData(AqDtlsModel[] dtls, boolean stopSubscription, String aqtableName);
    
     public int saveAqdtlsDataDHE(AqDtlsModel[] dtls, boolean stopSubscription, String aqtableName);   

    public AqDtlsModel getAqdtlsData(String aqslno);

    public List getAqdtlsList(String aqslno);

    public int deleteAqdtls(String aqslno, String aqDTLSTable);

    public int updateAqDtlsData(String tableName, AqDtlsDedBean aqDtlsDedBean);

    public AqDtlsDedBean getAqDetailsDed(String tableName, String aqslNo, String dedType, String nowdedn, String adRefid);

    public AqDtlsDedBean getAqDetailsAllowance(String tableName, String aqslNo, String dedType);

    public void deleteBrowserAqData(String aqslno, String offCode);

}

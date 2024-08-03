package hrms.dao.payroll.arrear;

import java.io.OutputStream;
import jxl.write.WritableWorkbook;

public interface BeneficiaryListArrearDAO {
    
    public void downloadBeneficiaryListArrearExcel(OutputStream out, String offcode, WritableWorkbook workbook,String billNo);
    
}

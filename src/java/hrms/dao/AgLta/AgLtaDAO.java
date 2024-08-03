package hrms.dao.AgLta;

import jxl.write.WritableWorkbook;

public interface AgLtaDAO {
    
    public void sendLTASMS();
    
    public void downloadMonhtlySuperannuationDataExcel(WritableWorkbook workbook, int year, int month);
    
    public void agLTALoanIntimationSendSMS();
    
}

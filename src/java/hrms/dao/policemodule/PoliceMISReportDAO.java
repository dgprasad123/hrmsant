package hrms.dao.policemodule;

import hrms.model.policemodule.PoliceMISReportForm;
import hrms.model.policemodule.PoliceSearchResult;
import java.util.List;
import jxl.write.WritableWorkbook;

public interface PoliceMISReportDAO {
    
    public PoliceSearchResult LocatePolice(PoliceMISReportForm searchPolice);
    
    public List policePostingDetails(String empid);
    
    public List PolicePostingListSameDistrict(PoliceMISReportForm policeform);
    
    public List PolicePostingList(PoliceMISReportForm policeform);
    
    public void generatePolicePostingListExcel(List policepostinglistdata,WritableWorkbook workbook,String fileName);
    
    public List getPoliceEstablishmentOfficeList();
    
}

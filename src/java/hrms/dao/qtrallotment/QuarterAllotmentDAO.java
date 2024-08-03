package hrms.dao.qtrallotment;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.qtrallotment.QuarterAllotment;
import java.util.List;

public interface QuarterAllotmentDAO {

    public List QuaterAllotSt(String empId);

    public QuarterAllotment editQuarterAllotment(String qtrAllotId);

    public int saveQuaterAllotmentRecord(QuarterAllotment quarterAllot);

    public int saveQuaterSurrenderRecord(QuarterAllotment quarterAllot);

    public QuarterAllotment getSurrenderEditRecords(String qtrSurId);

    public void deleteQtrAllot(String qtrid, String empid);

    public void deleteQtrSurrendRecords(String qtrid, String surid, String empid);
    
     public List quarterempdetails(String empId,int year);
    
     public void downloadApplicationPdf(PdfWriter writer, Document document, String empId, int year,String empGpfNo,String empName);
}

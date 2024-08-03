package hrms.dao.dpcconducted;

import com.itextpdf.text.Document;
import hrms.model.parmast.DPCConductedForm;
import hrms.model.parmast.NominationCategoryForm;
import java.util.List;

public interface DPCConductedDAO {
    
    public DPCConductedForm getDPCconductedList(String offcode);
    
    public void saveDPCConductedData(DPCConductedForm dpcform,String offcode,String offlevel);
    
    public String getDpcSubmissionStatus(String offcode);
    
    public NominationCategoryForm getNominationCategoryData(String offcode);
    
    public void saveNominationCategoryData(NominationCategoryForm nominationCategoryForm,String offcode,String offlevel);
    
    public String getNominationCategorySubmissionStatus(String offcode);
    
    public void downloadNominationCategoryPDF(Document document,String offcode,String offname,NominationCategoryForm nominationCategoryForm);
    
    public List dpcMISReport();
}

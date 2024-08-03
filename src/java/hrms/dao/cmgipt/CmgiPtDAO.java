package hrms.dao.cmgipt;

import com.itextpdf.text.Document;
import hrms.model.common.CommonReportParamBean;

public interface CmgiPtDAO {
    
    public void ptPDF(Document document,CommonReportParamBean crb,String billNo);
    
}

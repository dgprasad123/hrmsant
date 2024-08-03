package hrms.dao.cmgiepf;

import java.io.IOException;
import java.io.OutputStream;
import jxl.write.WritableWorkbook;

public interface CmgiEpfDAO {
    
    public WritableWorkbook downloadEPFExcel(OutputStream out) throws IOException;
    
}

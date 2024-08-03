package hrms.dao.master;

import hrms.model.master.Payscale;
import java.util.List;

public interface PayScaleDAO {

    public List getPayScaleList();
    public List getPayMatrixCont2017(String contYear);
    public Payscale getPayMatrixCont2017Data(String amt);
    
}

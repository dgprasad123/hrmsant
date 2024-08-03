package hrms.dao.GIS;

import hrms.model.GIS.GISForm;
import java.util.List;

public interface GISDAO {
    
    public List getGISList(String empid);
    
    public List getSchemeDataList();
    
    public void saveGISData(GISForm gisform,String logindeptname,String loginofficename,String loginpost);
    
    public GISForm editGISData(String empid,String gisid);
    
    public void deleteGISData(String gisid);
    
}

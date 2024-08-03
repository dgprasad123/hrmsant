package hrms.dao.AGPension;

import hrms.common.DataBaseFunctions;
import hrms.model.AGPension.AGPensionBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class AGPensionDAOImpl implements AGPensionDAO{
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public AGPensionBean getGPFNoDetails(String gpfno) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        AGPensionBean agpbean = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,OFF_EN,POST,if_gpf_assumed FROM EMP_MAST" +
                         " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE" +
                         " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC" +
                         " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE GPF_NO=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, gpfno);
            rs = pst.executeQuery();
            if(rs.next()){
                agpbean = new AGPensionBean();
                agpbean.setEmpid(rs.getString("EMP_ID"));
                agpbean.setEmpname(rs.getString("EMP_NAME"));
                agpbean.setOffname(rs.getString("OFF_EN"));
                agpbean.setDesignation(rs.getString("POST"));
                agpbean.setGpfAssumed(rs.getString("if_gpf_assumed"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return agpbean;  
    }
    
}

package hrms.dao.unlockuser;

import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UnlockuserDAOImpl implements UnlockuserDAO{
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public int unlockuser(String sltEmpidGpf, String txtEmpidGpf) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        
        int status = 0;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "";
            
            if(sltEmpidGpf != null && !sltEmpidGpf.equals("")){
                if(sltEmpidGpf.equals("empid")){
                    sql = "SELECT * FROM USER_DETAILS WHERE LINKID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1,txtEmpidGpf);
                    rs = pst.executeQuery();
                    if(rs.next()){
                        pst1 = con.prepareStatement("UPDATE USER_DETAILS SET ACCOUNTNONLOCKED=? WHERE LINKID=?");
                        pst1.setInt(1,1);
                        pst1.setString(2,txtEmpidGpf);
                        int retVal = pst1.executeUpdate();
                        if(retVal > 0){
                            status = 1;
                        }
                    }else{
                        status = 2;
                    }
                }else if(sltEmpidGpf.equals("gfpno")){
                    sql = "SELECT EMP_ID FROM EMP_MAST WHERE GPF_NO=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1,txtEmpidGpf);
                    rs = pst.executeQuery();
                    if(rs.next()){
                        pst1 = con.prepareStatement("UPDATE USER_DETAILS SET ACCOUNTNONLOCKED=? WHERE LINKID=?");
                        pst1.setInt(1,1);
                        pst1.setString(2,rs.getString("EMP_ID"));
                        int retVal = pst1.executeUpdate();
                        if(retVal > 0){
                            status = 1;
                        }
                    }else{
                        status = 2;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(pst,pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return status;  
    }
    
}

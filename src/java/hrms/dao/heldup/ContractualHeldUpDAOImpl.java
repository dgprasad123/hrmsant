package hrms.dao.heldup;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.model.heldup.ContractualHeldUpBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class ContractualHeldUpDAOImpl implements ContractualHeldUpDAO{
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getContractualEmpList(String offCode,int page,int rows) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractualHeldUpBean cbean = null;
        ArrayList elist = new ArrayList();
        
        int firstpage = (page > 1)?((page - 1)*rows)+1:1;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "SELECT Row_Num,F_NAME,EMP_ID,EMP_NAME,POST_NOMENCLATURE,HELDUP FROM" +
                         " (SELECT ROW_NUMBER() OVER (ORDER BY F_NAME) Row_Num,F_NAME,EMP_MAST.EMP_ID,DECODE(TRIM(INITIALS),null,'',TRIM(INITIALS)||' ')||DECODE(TRIM(F_NAME),null,'',TRIM(F_NAME)||' ')||" +
                         " DECODE(TRIM(M_NAME),null,'',TRIM(M_NAME)||' ')||DECODE(TRIM(L_NAME),null,'',TRIM(L_NAME)) EMP_NAME,POST_NOMENCLATURE," +
                         " DECODE(EMP_PAY_HELDUP.EMP_ID,NULL,'N','Y') HELDUP" +
                         " FROM EMP_MAST LEFT OUTER JOIN EMP_PAY_HELDUP ON EMP_MAST.EMP_ID=EMP_PAY_HELDUP.EMP_ID" +
                         " WHERE CUR_OFF_CODE=? AND IS_REGULAR=?) WHERE Row_Num BETWEEN ? AND ? ORDER BY TRIM(F_NAME)";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, "N");
            pst.setInt(3, firstpage);
            pst.setInt(4, page * rows);
            rs = pst.executeQuery();
            while(rs.next()){
                cbean = new ContractualHeldUpBean();
                cbean.setEmpid(rs.getString("EMP_ID"));
                cbean.setEmpname(rs.getString("EMP_NAME"));
                cbean.setEmpdesg(StringUtils.defaultString(rs.getString("POST_NOMENCLATURE")));
                cbean.setIsheldup(rs.getString("HELDUP"));
                elist.add(cbean);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(con);
        }
       return elist; 
    }

    @Override
    public Message saveHeldUpData(String empid,String heldupdate) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String offCode = "";
        String spc = "";
        
        Message msg = new Message();
        msg.setStatus("Success");
        try{
            con = this.dataSource.getConnection();
            
            String sql = "SELECT CUR_OFF_CODE,POST_NOMENCLATURE FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            rs = pst.executeQuery();
            if(rs.next()){
                offCode = rs.getString("CUR_OFF_CODE");
                spc = rs.getString("POST_NOMENCLATURE");
            }
            int heldupid = CommonFunctions.getMaxCodeInteger("EMP_PAY_HELDUP", "HELDUP_PAY_ID", con);
            sql = "INSERT INTO EMP_PAY_HELDUP(HELDUP_PAY_ID,EMP_ID,OFF_CODE,SPC,FROM_DATE) VALUES(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, heldupid);
            pst.setString(2, empid);
            pst.setString(3, offCode);
            pst.setString(4, spc);
            pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(heldupdate).getTime()));
            pst.executeUpdate();
        }catch(Exception e){
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return msg;  
    }

    @Override
    public int getTotalEmployeeCount(String offCode) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        int total = 0;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "SELECT count(*) cnt FROM EMP_MAST WHERE CUR_OFF_CODE=? AND IS_REGULAR=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,offCode);
            pst.setString(2,"Y");
            rs = pst.executeQuery();
            if(rs.next()){
                total = rs.getInt("cnt");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return total;  
    }
}

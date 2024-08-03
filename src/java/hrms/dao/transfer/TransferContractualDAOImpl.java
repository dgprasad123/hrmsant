package hrms.dao.transfer;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.transfer.TransferBean;
import hrms.model.transfer.TransferForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class TransferContractualDAOImpl implements TransferContractualDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getTransferContractualList(String empid) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List transferlist = new ArrayList();
        TransferBean tbean = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select transfer_id,transferred_on_date,order_no,order_date,transferred_to_office,off_en from contractual_emp_transfer" +
                         " inner join g_office on contractual_emp_transfer.transferred_to_office=g_office.off_code where contractual_emp_transfer.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new TransferBean();
                tbean.setTransferId(rs.getString("transfer_id"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("transferred_on_date")));
                tbean.setOrdno(rs.getString("order_no"));
                tbean.setOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                tbean.setTransferToOffice(rs.getString("off_en"));
                transferlist.add(tbean);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return transferlist;
    }

    @Override
    public void saveTransferContractual(TransferForm transferForm,String loginempid,String selectedempid) {
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            con = this.dataSource.getConnection();
            
            String sql = "INSERT INTO contractual_emp_transfer (order_no, order_date, emp_id, transfer_from_post,transfer_by_emp_id,transferred_on_date, dep_code,transferred_to_office) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getTxtNotOrdNo());
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(transferForm.getTxtNotOrdDt()).getTime()));
            pst.setString(3, selectedempid);
            pst.setString(4, transferForm.getPostedPostName());
            pst.setString(5, loginempid);
            pst.setTimestamp(6, new Timestamp(new Date().getTime()));
            pst.setString(7, "06");
            pst.setString(8, transferForm.getHidPostedOffCode());
            pst.executeUpdate();
            
            sql = "delete from section_post_mapping where spc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, selectedempid);
            pst.executeUpdate();
            
            sql = "UPDATE EMP_MAST SET DEP_CODE='06',NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getHidPostedOffCode());
            pst.setString(2, selectedempid);
            pst.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateTransferContractual(TransferForm transferForm) {
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            con = this.dataSource.getConnection();
            
            String sql = "UPDATE contractual_emp_transfer set order_no=?, order_date=?, transfer_from_post=?,transferred_to_office=? where transfer_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getTxtNotOrdNo());
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(transferForm.getTxtNotOrdDt()).getTime()));
            pst.setString(3, transferForm.getPostedPostName());
            pst.setString(4, transferForm.getHidPostedOffCode());
            pst.setInt(5,Integer.parseInt(transferForm.getTransferId()));
            pst.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getPostNomenclature(String empid) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String postnomenclature = "";
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select post_nomenclature from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            rs = pst.executeQuery();
            if(rs.next()){
                postnomenclature = rs.getString("post_nomenclature");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return postnomenclature; 
    }

    @Override
    public TransferForm getEmpTransferData(TransferForm trform,int transferId) throws SQLException {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select dist_code,transfer_id,transferred_on_date,order_no,order_date,transferred_to_office,transfer_from_post,off_en,g_department.department_code,g_department.department_name from contractual_emp_transfer" +
                         " inner join g_office on contractual_emp_transfer.transferred_to_office=g_office.off_code" +
                         " inner join g_department on g_office.department_code=g_department.department_code where transfer_id=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, transferId);
            rs = pst.executeQuery();
            if (rs.next()) {
                trform.setTransferId(rs.getString("transfer_id"));
                trform.setTxtNotOrdNo(rs.getString("order_no"));
                trform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                
                trform.setHidPostedDeptCode(rs.getString("department_code"));
                trform.setHidPostedDistCode(rs.getString("dist_code"));
                trform.setHidPostedOffCode(rs.getString("transferred_to_office"));
                trform.setHidTempPostedOffCode(rs.getString("off_en"));
                trform.setPostedPostName(rs.getString("transfer_from_post"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return trform;
    }

}

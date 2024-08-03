package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Payscale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

public class PayScaleDAOImpl implements PayScaleDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPayScaleList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List payscalelist = new ArrayList();

        Payscale ps = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT PAYSCALE FROM G_PAYSCALE ORDER BY PAYSCALE ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                ps = new Payscale();
                ps.setPayscale(rs.getString("PAYSCALE"));
                payscalelist.add(ps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payscalelist;
    }

    @Override
    public List getPayMatrixCont2017(String contYear) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List payMatrixCont2017list = new ArrayList();
        Payscale pscale = null;

        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT * FROM PAY_MATRIX_CONT_2017 WHERE YEAR=? ORDER BY AMT";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(contYear));
            rs = pst.executeQuery();
            while (rs.next()) {
                pscale = new Payscale();
                pscale.setGp(rs.getInt("gp"));
                pscale.setAmt(rs.getDouble("amt"));
                pscale.setContYear(rs.getString("year"));
                payMatrixCont2017list.add(pscale);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payMatrixCont2017list;
    }

    @Override
    public Payscale getPayMatrixCont2017Data(String amt) {
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        Payscale pscale = null;
        //List contPayYearData=new ArrayList();
        try{
            con=this.dataSource.getConnection();
            ps=con.prepareStatement("SELECT * FROM PAY_MATRIX_CONT_2017 WHERE AMT=?");
            ps.setDouble(1, Double.parseDouble(amt));
            rs=ps.executeQuery();
            if(rs.next()){
                pscale = new Payscale();
                pscale.setContYear(rs.getString("year"));
                pscale.setAmt(rs.getDouble("amt"));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pscale;
        
    }
}

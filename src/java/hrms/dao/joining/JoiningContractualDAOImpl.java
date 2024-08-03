package hrms.dao.joining;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.joining.JoiningContractualForm;
import hrms.model.joining.JoiningContractualList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class JoiningContractualDAOImpl implements JoiningContractualDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getJoiningContractualList(String empid, String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List jlist = new ArrayList();
        JoiningContractualList jbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select contractual_emp_transfer.transfer_id primaryTransferId,contractual_emp_transfer.order_no,contractual_emp_transfer.order_date,transfer_from_post,contractual_emp_joining.transfer_id foreignTransferId,contractual_emp_joining.join_id from contractual_emp_transfer"
                    + " left outer join contractual_emp_joining on contractual_emp_transfer.transfer_id=contractual_emp_joining.transfer_id where contractual_emp_transfer.emp_id=? and transferred_to_office=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                jbean = new JoiningContractualList();
                jbean.setHidPrimaryTransferId(rs.getString("primaryTransferId"));
                jbean.setHidForeignTransferId(rs.getString("foreignTransferId"));
                jbean.setJoinid(rs.getString("join_id"));
                jbean.setNotOrdNo(rs.getString("order_no"));
                if (rs.getDate("order_date") != null && !rs.getDate("order_date").equals("")) {
                    jbean.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                }
                jbean.setTransferFromPost(rs.getString("transfer_from_post"));
                jlist.add(jbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jlist;
    }

    @Override
    public JoiningContractualForm getJoiningContractualData(String primaryTrId, String jid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        JoiningContractualForm jform = new JoiningContractualForm();
        try {
            con = this.dataSource.getConnection();

            if (jid != null && !jid.equals("")) {
                String sql = "select * from contractual_emp_joining where join_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(jid));
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setHidjoinId(jid);
                    jform.setHidForeignTransferId(rs.getString("transfer_id"));
                    jform.setHidempid(rs.getString("emp_id"));
                    jform.setHidPostNomenclature(rs.getString("joining_post"));
                    jform.setJoiningOrdNo(rs.getString("join_order_no"));
                    jform.setJoiningOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_order_date")));
                }
            }else if(primaryTrId != null && !primaryTrId.equals("")){
                String sql = "select * from contractual_emp_transfer where transfer_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(primaryTrId));
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setHidPrimaryTransferId(primaryTrId);
                    jform.setHidPostNomenclature(rs.getString("transfer_from_post"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jform;
    }

    @Override
    public void saveJoiningContractual(JoiningContractualForm jform, String loginempid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into contractual_emp_joining(emp_id,joined_on,join_order_no,join_order_date,transfer_id,joining_post,joined_by_emp_id) values(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, jform.getHidempid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, jform.getJoiningOrdNo());
            if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if(jform.getHidPrimaryTransferId() != null && !jform.getHidPrimaryTransferId().equals("")){
                pst.setInt(5, Integer.parseInt(jform.getHidPrimaryTransferId()));
            }else{
                pst.setInt(5, 0);
            }
            pst.setString(6, jform.getHidPostNomenclature());
            pst.setString(7, loginempid);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "update emp_mast set dep_code='02',cur_off_code=next_office_code,next_office_code=null, post_nomenclature=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, jform.getHidPostNomenclature());
            pst.setString(2, jform.getHidempid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteJoiningContractual(JoiningContractualForm jform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateJoiningContractual(JoiningContractualForm jform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update contractual_emp_joining set join_order_no=?,join_order_date=?,joining_post=? where join_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, jform.getJoiningOrdNo());
            if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, jform.getHidPostNomenclature());
            pst.setInt(4, Integer.parseInt(jform.getHidjoinId()));
            pst.executeUpdate();
            
            DataBaseFunctions.closeSqlObjects(rs, pst);
            
            sql = "update emp_mast set dep_code='02',post_nomenclature=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, jform.getHidPostNomenclature());
            pst.setString(2, jform.getHidempid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

package hrms.dao.GPHeadUpdate;

import hrms.common.DataBaseFunctions;
import hrms.model.GPHeadUpdate.GPHeadUpdateForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class GPHeadDAOImpl implements GPHeadDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public GPHeadUpdateForm getBillDetails(GPHeadUpdateForm gpheadform) {

        Connection conora = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String aqslno = "";
        int aqmonth = 0;
        int aqyear = 0;

        ArrayList aqslnolist = new ArrayList();
        String billExists = "N";
        try {
            conora = dataSource.getConnection();

            String sql = "SELECT AQSL_NO,AQ_MONTH,AQ_YEAR FROM AQ_MAST WHERE BILL_NO=?";
            pst = conora.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(gpheadform.getTxtBillNo()));
            rs = pst.executeQuery();
            while (rs.next()) {
                aqslno = rs.getString("AQSL_NO");
                aqmonth = rs.getInt("AQ_MONTH");
                aqyear = rs.getInt("AQ_YEAR");
                aqslnolist.add(aqslno);
            }

            gpheadform.setAqslno(aqslno);
            gpheadform.setAqyear(aqyear + "");
            gpheadform.setAqmonth(aqmonth + "");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (aqslnolist.size() > 0) {
                sql = "select * from aq_dtls where aqsl_no ="
                        + " ? and ad_code='GP' AND AD_AMT > 0"
                        + " AND AQ_MONTH=? AND AQ_YEAR=?";
                pst = conora.prepareStatement(sql);
                for (int i = 0; i < aqslnolist.size(); i++) {
                    pst.setString(1, aqslnolist.get(i) + "");
                    pst.setInt(2, aqmonth);
                    pst.setInt(3, aqyear);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        billExists = "Y";
                    }
                }
            }
            gpheadform.setBillExists(billExists);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conora);
        }
        return gpheadform;
    }

    @Override
    public int updateGPHead(GPHeadUpdateForm gpheadform) {

        Connection conora = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList aqslnolist = new ArrayList();
        int retVal = 0;
        try {
            conora = dataSource.getConnection();
            String sql = "SELECT AQSL_NO FROM AQ_MAST WHERE BILL_NO=?";
            pst = conora.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(gpheadform.getTxtBillNo()));
            rs = pst.executeQuery();
            while (rs.next()) {
                aqslnolist.add(rs.getString("AQSL_NO"));
            }

            if (aqslnolist.size() > 0) {
                sql = "UPDATE AQ_DTLS SET BT_ID=? where aqsl_no ="
                        + " ? and ad_code='GP' AND AD_AMT > 0"
                        + " AND BT_ID=? AND AQ_MONTH=? AND AQ_YEAR=?";
                pst = conora.prepareStatement(sql);
                for (int i = 0; i < aqslnolist.size(); i++) {
                    pst.setString(1, gpheadform.getNewGPHead());
                    pst.setString(2, aqslnolist.get(i) + "");
                    pst.setString(3, gpheadform.getExistingGPHead());
                    pst.setInt(4, Integer.parseInt(gpheadform.getAqmonth()));
                    pst.setInt(5, Integer.parseInt(gpheadform.getAqyear()));
                    retVal = pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conora);
        }
        return retVal;
    }
}

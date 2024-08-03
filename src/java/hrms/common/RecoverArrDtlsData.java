package hrms.common;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecoverArrDtlsData {

    public static void main(String args[]) {

        Connection conprod = null;
        Connection conlocal = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        PreparedStatement deletepst = null;
        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement insertpst = null;

        List billList = new ArrayList();

        int count1 = 0;
        int count2 = 0;

        List arrmastList = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            conprod = DriverManager.getConnection("jdbc:postgresql://172.16.1.16/hrmis", "hrmis2", "cmgi");
            conlocal = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            String sql = "select bill_no from bill_mast where bill_no in (51450161)";
            pst = conprod.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                billList.add(rs.getString("bill_no"));
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (billList != null && billList.size() > 0) {
                sql = "select aqsl_no from arr_mast where bill_no=?";
                pst = conprod.prepareStatement(sql);
                for (int i = 0; i < billList.size(); i++) {
                    int billno = Integer.parseInt(billList.get(i) + "");
                    pst.setInt(1, billno);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        String aqslno = rs.getString("aqsl_no");
                        arrmastList.add(aqslno);
                    }
                }
            }

            if (arrmastList != null && arrmastList.size() > 0) {
                for (int i = 0; i < arrmastList.size(); i++) {
                    String aqslno = arrmastList.get(i) + "";
                    pst1 = conprod.prepareStatement("select count(*) cnt from arr_dtls where aqsl_no=?");
                    pst1.setString(1, aqslno);
                    rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        count1 = rs1.getInt("cnt");
                    }

                    pst2 = conlocal.prepareStatement("select count(*) cnt from arr_dtls1 where aqsl_no=?");
                    pst2.setString(1, aqslno);
                    rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        count2 = rs2.getInt("cnt");
                    }
                    if (count2 > count1) {
                        String deletesql = "delete from arr_dtls where aqsl_no=?";
                        deletepst = conprod.prepareStatement(deletesql);
                        deletepst.setString(1, aqslno);
                        deletepst.executeUpdate();

                        String insertsql = "insert into arr_dtls(aqsl_no,p_month,p_year,ad_type,already_paid,to_be_paid,remark,drawn_vide_billno,ref_aqsl_no,calc_unique_no,bt_id) values(?,?,?,?,?,?,?,?,?,?,?)";
                        insertpst = conprod.prepareStatement(insertsql);

                        String selectsql = "select aqsl_no,p_month,p_year,ad_type,already_paid,to_be_paid,remark,drawn_vide_billno,ref_aqsl_no,calc_unique_no,bt_id from arr_dtls1 where arr_dtls1.aqsl_no=?";
                        selectpst = conlocal.prepareStatement(selectsql);
                        selectpst.setString(1, aqslno);
                        selectrs = selectpst.executeQuery();
                        while (selectrs.next()) {
                            insertpst.setString(1, selectrs.getString("aqsl_no"));
                            insertpst.setInt(2, selectrs.getInt("p_month"));
                            insertpst.setInt(3, selectrs.getInt("p_year"));
                            insertpst.setString(4, selectrs.getString("ad_type"));
                            insertpst.setInt(5, selectrs.getInt("already_paid"));
                            insertpst.setInt(6, selectrs.getInt("to_be_paid"));
                            insertpst.setString(7, selectrs.getString("remark"));
                            insertpst.setString(8, selectrs.getString("drawn_vide_billno"));
                            insertpst.setString(9, selectrs.getString("ref_aqsl_no"));
                            insertpst.setInt(10, selectrs.getInt("calc_unique_no"));
                            insertpst.setInt(11, selectrs.getInt("bt_id"));
                            insertpst.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conprod);
            DataBaseFunctions.closeSqlObjects(conlocal);
        }

    }
}

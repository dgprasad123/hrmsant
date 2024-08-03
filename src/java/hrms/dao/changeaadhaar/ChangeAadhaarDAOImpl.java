package hrms.dao.changeaadhaar;

import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import net.iharder.Base64;

public class ChangeAadhaarDAOImpl implements ChangeAadhaarDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Message changeAadhaar(String empid, String newaadhaar) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean duplicateaadhaar = false;
        int retVal = 0;

        Message msg = new Message();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM emp_id_doc WHERE id_no=? AND EMP_ID != ? AND id_description=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, newaadhaar);
            pst.setString(2, empid);
            pst.setString(3, "AADHAAR");
            rs = pst.executeQuery();
            if (rs.next()) {
                duplicateaadhaar = true;
            }

            if (duplicateaadhaar == false) {

                sql = "SELECT * FROM emp_id_doc WHERE EMP_ID = ? AND id_description=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                pst.setString(2, "AADHAAR");
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("UPDATE emp_id_doc SET id_no=? WHERE EMP_ID=? AND id_description=?");
                    pst.setString(1, newaadhaar);
                    pst.setString(2, empid);
                    pst.setString(3, "AADHAAR");
                    retVal = pst.executeUpdate();
                } else {
                    pst = con.prepareStatement("INSERT INTO emp_id_doc(emp_id,id_description,id_no) values(?,?,?)");
                    pst.setString(1, empid);
                    pst.setString(2, "AADHAAR");
                    pst.setString(3, newaadhaar);
                    retVal = pst.executeUpdate();
                }
            }

            if (retVal > 0) {
                msg.setMessage("CHANGED");
            } else {
                if (duplicateaadhaar == true) {
                    msg.setMessage("DUPLICATE");
                } else {
                    msg.setMessage("ERROR");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public String decryptAadhaar(String encrypted) {
        try {
            SecretKey key = new SecretKeySpec(Base64.decode("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
            AlgorithmParameterSpec iv = new IvParameterSpec(Base64.decode("5D9r9ZVzEYYgha93/aUK2w=="));
            byte[] decodeBase64 = Base64.decode(encrypted);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(cipher.doFinal(decodeBase64), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("This should not happen in production.", e);
        }
    }
}

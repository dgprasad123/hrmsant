package hrms.dao.ContractualEmployeeAssignPost;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.ContractualEmployeeAssignPost.ContractualEmployeeAssignPostBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ContractualEmployeeAssignPostDAOImpl implements ContractualEmployeeAssignPostDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getContractualEmployeeList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,post_nomenclature from emp_mast where CUR_off_code=? AND is_regular = 'N' order by F_NAME");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("EMP_NAME") != null && !rs.getString("EMP_NAME").equals("")) {
                    so = new SelectOption();
                    so.setValue(rs.getString("emp_id"));
                    so.setLabel(rs.getString("EMP_NAME") + "(" + rs.getString("post_nomenclature") + ")");
                    emplist.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getVacantPost(String offCode) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List postlist = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT SPC,SPN,GPC,GETGPOSTNAME(GPC)  POST ,IFUCLEAN,is_sanctioned,emp_id FROM G_SPC "
                    + "LEFT OUTER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC "
                    + "WHERE OFF_CODE=? AND (emp_id is null or emp_id = '') AND IFUCLEAN='N'  and is_sanctioned='Y' ORDER BY SPN ";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    so = new SelectOption();
                    so.setValue(rs.getString("SPC"));
                    so.setLabel(rs.getString("SPN"));
                    postlist.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postlist;
    }

    @Override
    public String mapVacantPostToEmployee(ContractualEmployeeAssignPostBean cbean) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isMapped = "N";
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            sql = "UPDATE EMP_MAST SET CUR_SPC=?,is_regular = 'C',post_nomenclature=NULL WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, cbean.getSltVacantSPC());
            pst.setString(2, cbean.getSltEmployee());
            int retVal = pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM SECTION_POST_MAPPING WHERE SPC = ?");
            pst.setString(1, cbean.getSltEmployee());
            retVal = pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM SECTION_POST_MAPPING WHERE SPC = ?");
            pst.setString(1, cbean.getSltVacantSPC());
            retVal = pst.executeUpdate();

            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isMapped;
    }

}

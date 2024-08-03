package hrms.dao.report.groupwisesanctionedstrength;

import hrms.common.DataBaseFunctions;
import hrms.model.report.groupwisesanctionedstrength.GroupWiseSanctionedStrengthBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class GroupWiseSanctionedStrengthDAOImpl implements GroupWiseSanctionedStrengthDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getDepartmentWiseData() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List deptList = new ArrayList();
        GroupWiseSanctionedStrengthBean bean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT department_code,department_name from g_department where if_active='Y' order by department_name asc";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                bean = new GroupWiseSanctionedStrengthBean();
                bean.setDeptCode(rs.getString("department_code"));
                bean.setDeptName(rs.getString("department_name"));

                bean.setGroupA_SanctionedStrength(getSanctionedStrengthDeptWiseCount(rs.getString("department_code"), "A"));
                bean.setGroupA_MenInPosition(getMenInPositionDeptWiseCount(rs.getString("department_code"), "A"));

                bean.setGroupB_SanctionedStrength(getSanctionedStrengthDeptWiseCount(rs.getString("department_code"), "B"));
                bean.setGroupB_MenInPosition(getMenInPositionDeptWiseCount(rs.getString("department_code"), "B"));

                bean.setGroupC_SanctionedStrength(getSanctionedStrengthDeptWiseCount(rs.getString("department_code"), "C"));
                bean.setGroupC_MenInPosition(getMenInPositionDeptWiseCount(rs.getString("department_code"), "C"));

                bean.setGroupD_SanctionedStrength(getSanctionedStrengthDeptWiseCount(rs.getString("department_code"), "D"));
                bean.setGroupD_MenInPosition(getMenInPositionDeptWiseCount(rs.getString("department_code"), "D"));

                deptList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptList;
    }

    private int getSanctionedStrengthDeptWiseCount(String deptCode, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from g_spc where dept_code=? and post_grp=? and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, postgroup);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    private int getMenInPositionDeptWiseCount(String deptCode, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from g_spc"
                    + " inner join emp_mast on g_spc.spc=emp_mast.cur_spc where dept_code=? and g_spc.post_grp=? and emp_id is not null and dep_code='02' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y' and is_regular='Y' and if_retired is null";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, postgroup);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public List getOfficeWiseData(String deptCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officeList = new ArrayList();
        GroupWiseSanctionedStrengthBean bean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select off_code,off_en from g_office where department_code=? order by off_en asc";
            pst = con.prepareStatement(sql);
            pst.setString(1,deptCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                bean = new GroupWiseSanctionedStrengthBean();
                bean.setOffName(rs.getString("off_en"));

                bean.setGroupA_OfficeSanctionedStrength(getSanctionedStrengthOfficeWiseCount(rs.getString("off_code"), "A"));
                bean.setGroupA_OfficeMenInPosition(getMenInPositionOfficeWiseCount(rs.getString("off_code"), "A"));

                bean.setGroupB_OfficeSanctionedStrength(getSanctionedStrengthOfficeWiseCount(rs.getString("off_code"), "B"));
                bean.setGroupB_OfficeMenInPosition(getMenInPositionOfficeWiseCount(rs.getString("off_code"), "B"));

                bean.setGroupC_OfficeSanctionedStrength(getSanctionedStrengthOfficeWiseCount(rs.getString("off_code"), "C"));
                bean.setGroupC_OfficeMenInPosition(getMenInPositionOfficeWiseCount(rs.getString("off_code"), "C"));

                bean.setGroupD_OfficeSanctionedStrength(getSanctionedStrengthOfficeWiseCount(rs.getString("off_code"), "D"));
                bean.setGroupD_OfficeMenInPosition(getMenInPositionOfficeWiseCount(rs.getString("off_code"), "D"));

                officeList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeList;
    }
    
    private int getSanctionedStrengthOfficeWiseCount(String offCode, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from g_spc where off_code=? and post_grp=? and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, postgroup);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    private int getMenInPositionOfficeWiseCount(String offCode, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from g_spc"
                    + " inner join emp_mast on g_spc.spc=emp_mast.cur_spc where off_code=? and g_spc.post_grp=? and emp_id is not null and dep_code='02' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) AND is_sanctioned='Y' and is_regular='Y' and if_retired is null";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, postgroup);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }
}

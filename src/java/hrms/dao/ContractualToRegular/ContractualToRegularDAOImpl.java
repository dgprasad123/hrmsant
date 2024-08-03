package hrms.dao.ContractualToRegular;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.ContractualToRegular.ContractualToRegularForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ContractualToRegularDAOImpl implements ContractualToRegularDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getContractualToRegularEmployeeList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List empList = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from emp_mast where cur_off_code=? and is_regular='C'";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("emp_id"));
                so.setLabel(rs.getString("EMP_NAME"));
                empList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public List getFilteredEmployeeList(String offCode, String empType) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List empList = new ArrayList();
        SelectOption so = null;
        String isRegular = null;
        String sql = "";
        if (empType.equals("Regular")) {
            isRegular = "Y";
        }
        if (empType.equals("Contractual")) {
            isRegular = "N";
        }
        if (empType.equals("ContractualSix")) {
            isRegular = "C";
        }
        try {
            con = this.dataSource.getConnection();
            if (isRegular.equals("Y")) {
                sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME"
                        + ",post AS post_nomenclature, cur_spc from emp_mast EM"
                        + " LEFT OUTER JOIN g_spc GS ON EM.cur_spc = GS.spc"
                        + " LEFT OUTER JOIN g_post GP ON GS.gpc = GP.post_code"
                        + " where cur_off_code=?"
                        + " AND is_regular = ? order by F_NAME";
            } else {
                sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME"
                        + ",post_nomenclature from emp_mast where cur_off_code=? AND is_regular = ? order by F_NAME";
            }

            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, isRegular);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("emp_id"));
                if (rs.getString("post_nomenclature") != null) {
                    so.setLabel(rs.getString("EMP_NAME") + " (" + rs.getString("post_nomenclature") + ")");
                } else {
                    so.setLabel(rs.getString("EMP_NAME"));
                }
                empList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public ContractualToRegularForm getContractualToRegularData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ContractualToRegularForm ctrform = new ContractualToRegularForm();

        try {
            con = this.dataSource.getConnection();

            String sql = "select post_order_no,post_order_date,regul_wef,is_regular from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ctrform.setTxtPostOrdNo(rs.getString("post_order_no"));
                ctrform.setTxtPostOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("post_order_date")));
                ctrform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("regul_wef")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ctrform;
    }

    @Override
    public int saveContractualToRegular(ContractualToRegularForm ctrform) {

        Connection con = null;

        PreparedStatement pst = null;

        int retVal = 0;
        String payScale = "";
        String postNomen = "";
        String sql = "";
        
        try {
            con = this.dataSource.getConnection();
            payScale = null; //ctrform.getTxtLevel() + "-" + ctrform.getTxtCell();
            if (ctrform.getConvertType().equals("N")) {
                sql = "update emp_mast set post_order_no=?,post_order_date=?"
                        + ",regul_wef=?,is_regular=?,cur_basic_salary=?,gp=?,cur_salary=?"
                        + ", cur_spc = '', post_nomenclature = ?  where emp_id=?";
            } else {
                sql = "update emp_mast set post_order_no=?,post_order_date=?"
                        + ",regul_wef=?,is_regular=?,cur_basic_salary=?,gp=?,cur_salary=? , post_nomenclature='', doe_gov=?, pay_commission=7,matrix_level=?,matrix_cell=? where emp_id=?";
            }
            
            pst = con.prepareStatement(sql);
            pst.setString(1, ctrform.getTxtPostOrdNo());
            if (ctrform.getTxtPostOrdDt() != null && !ctrform.getTxtPostOrdDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(ctrform.getTxtPostOrdDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (ctrform.getTxtWEFDt() != null && !ctrform.getTxtWEFDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(ctrform.getTxtWEFDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, ctrform.getConvertType().replaceAll(",", ""));
            if (ctrform.getTxtBasic() != null && !ctrform.getTxtBasic().equals("")) {
                pst.setInt(5, Integer.parseInt(ctrform.getTxtBasic()));
            } else {
                pst.setInt(5, 0);
            }
            pst.setInt(6, 0);
            pst.setString(7, payScale);
            if (ctrform.getConvertType().equals("N")) {
                pst.setString(8, ctrform.getPostNomenclature());
                pst.setString(9, ctrform.getSltEmployee());
            } else {
                if (ctrform.getTxtWEFDt() != null && !ctrform.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(ctrform.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }
                if (ctrform.getTxtLevel() != null && !ctrform.getTxtLevel().equals("")) {
                    pst.setInt(9, Integer.parseInt(ctrform.getTxtLevel()));
                }else{
                    pst.setInt(9, 0);
                }
                if (ctrform.getTxtCell() != null && !ctrform.getTxtCell().equals("")) {
                    pst.setInt(10, Integer.parseInt(ctrform.getTxtCell()));
                }else{
                    pst.setInt(10, 0);
                }
                pst.setString(11, ctrform.getSltEmployee());

            }
            
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int deleteContractualToRegular(ContractualToRegularForm ctrform) {

        Connection con = null;

        PreparedStatement pst = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_mast set post_order_no=?,post_order_date=?,regul_wef=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, null);
            pst.setTimestamp(2, null);
            pst.setTimestamp(3, null);
            pst.setString(4, ctrform.getSltEmployee());
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public boolean isDuplicate7thPayRevisionData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isDuplicate = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from pay_revision_option where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("emp_id") != null && !rs.getString("emp_id").equals("")) {
                    isDuplicate = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDuplicate;
    }
}

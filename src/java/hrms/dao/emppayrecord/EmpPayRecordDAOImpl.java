package hrms.dao.emppayrecord;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.emppayrecord.EmpPayRecordForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EmpPayRecordDAOImpl implements EmpPayRecordDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveEmpPayRecordData(EmpPayRecordForm eprform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            String sql = "insert into emp_pay_record (not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc, gp, pay_level, pay_cell,increment_reason,if_remuneration) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, eprform.getNot_type());
            pst.setInt(2, eprform.getNot_id());
            pst.setString(3, eprform.getEmpid());
            if (eprform.getWefDt() != null && !eprform.getWefDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(sdf.parse(eprform.getWefDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (eprform.getWefTime() != null && !eprform.getWefTime().equals("")) {
                pst.setString(5, eprform.getWefTime());
            } else {
                pst.setString(5, null);
            }
            /*if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
             pst.setString(6, eprform.getPayscale());
             } else {
             pst.setString(6, null);
             }*/
            if (eprform.getBasic() == null || eprform.getBasic().equals("")) {
                pst.setDouble(7, 0);
            } else {
                pst.setDouble(7, java.lang.Double.parseDouble(eprform.getBasic()));
            }
            if (eprform.getS_pay() == null || eprform.getS_pay().equalsIgnoreCase("")) {
                pst.setDouble(8, 0);
            } else {
                pst.setDouble(8, java.lang.Double.parseDouble(eprform.getS_pay()));
            }
            if (eprform.getP_pay() == null || eprform.getP_pay().equalsIgnoreCase("")) {
                pst.setDouble(9, 0);
            } else {
                pst.setDouble(9, java.lang.Double.parseDouble(eprform.getP_pay()));
            }
            if (eprform.getOth_pay() == null || eprform.getOth_pay().equalsIgnoreCase("")) {
                pst.setDouble(10, 0);
            } else {
                pst.setDouble(10, java.lang.Double.parseDouble(eprform.getOth_pay()));
            }
            if (eprform.getOth_desc() != null && !eprform.getOth_desc().equals("")) {
                pst.setString(11, eprform.getOth_desc().toUpperCase());
            } else {
                pst.setString(11, null);
            }
            /*if (eprform.getGp() == null || eprform.getGp().equalsIgnoreCase("")) {
             pst.setDouble(12, 0);
             } else {
             pst.setDouble(12, java.lang.Double.parseDouble(eprform.getGp()));
             }*/
            //pst.setString(13, eprform.getPayLevel());
            //pst.setString(14, eprform.getPayCell());
            if (eprform.getPayLevel() != null && !eprform.getPayLevel().equals("")) {
                pst.setString(6, null);
                pst.setDouble(12, 0);
                pst.setString(13, eprform.getPayLevel());
                if (eprform.getPayCell() != null && !eprform.getPayCell().equals("")) {
                    pst.setString(14, eprform.getPayCell());
                }

            } else if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
                pst.setString(6, eprform.getPayscale());
                if (eprform.getGp() == null || eprform.getGp().equals("")) {
                    pst.setDouble(12, 0);
                } else {
                    pst.setDouble(12, java.lang.Double.parseDouble(eprform.getGp()));
                }
                pst.setString(13, null);
                pst.setString(14, null);

            } else {
                pst.setString(6, null);
                pst.setDouble(12, 0);
                pst.setString(13, null);
                pst.setString(14, null);
            }
            pst.setString(15, eprform.getReasonpayfixation());
            pst.setString(16, eprform.getIfRemuneration());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int payid = rs.getInt("pay_id");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_pay_record SET query_string=? where PAY_ID=? and EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, payid);
            pst.setString(3, eprform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmpPayRecordData(EmpPayRecordForm eprform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            String sql = "update emp_pay_record set wef=?, weft=?, pay_scale=?, pay=?, s_pay=?, p_pay=?, oth_pay=?, oth_desc=?, gp=?, pay_level=?, pay_cell=?,increment_reason=?,if_remuneration=?  where pay_id=? and emp_id=?";
            pst = con.prepareStatement(sql);
            if (eprform.getWefDt() != null && !eprform.getWefDt().equals("")) {
                pst.setTimestamp(1, new Timestamp(sdf.parse(eprform.getWefDt()).getTime()));
            } else {
                pst.setTimestamp(1, null);
            }
            pst.setString(2, eprform.getWefTime());
            //pst.setString(3, eprform.getPayscale());
            if (eprform.getBasic() == null || eprform.getBasic().equals("")) {
                pst.setDouble(4, 0);
            } else {
                pst.setDouble(4, java.lang.Double.parseDouble(eprform.getBasic()));
            }
            if (eprform.getS_pay() == null || eprform.getS_pay().equals("")) {
                pst.setDouble(5, 0);
            } else {
                pst.setDouble(5, java.lang.Double.parseDouble(eprform.getS_pay()));
            }
            if (eprform.getP_pay() == null || eprform.getP_pay().equals("")) {
                pst.setDouble(6, 0);
            } else {
                pst.setDouble(6, java.lang.Double.parseDouble(eprform.getP_pay()));
            }
            if (eprform.getOth_pay() == null || eprform.getOth_pay().equals("")) {
                pst.setDouble(7, 0);
            } else {
                pst.setDouble(7, java.lang.Double.parseDouble(eprform.getOth_pay()));
            }
            pst.setString(8, eprform.getOth_desc());
            /*if (eprform.getGp() == null || eprform.getGp().equals("")) {
             pst.setDouble(9, 0);
             } else {
             pst.setDouble(9, java.lang.Double.parseDouble(eprform.getGp()));
             }*/

            if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
                pst.setString(3, eprform.getPayscale());
                if (eprform.getGp() == null || eprform.getGp().equals("")) {
                    pst.setDouble(9, 0);
                } else {
                    pst.setDouble(9, java.lang.Double.parseDouble(eprform.getGp()));
                }
                pst.setString(10, null);
                pst.setString(11, null);
            } else if (eprform.getPayLevel() != null && !eprform.getPayLevel().equals("")) {
                pst.setString(3, null);
                pst.setDouble(9, 0);
                pst.setString(10, eprform.getPayLevel());
                if (eprform.getPayCell() != null && !eprform.getPayCell().equals("")) {
                    pst.setString(11, eprform.getPayCell());
                }
            } else {
                pst.setString(3, null);
                pst.setDouble(9, 0);
                pst.setString(10, null);
                pst.setString(11, null);
            }
            //pst.setString(10, eprform.getPayLevel());
            //pst.setString(11, eprform.getPayCell());
            pst.setString(12, eprform.getReasonpayfixation());
            pst.setString(13, eprform.getIfRemuneration());
            pst.setInt(14, eprform.getPayid());
            pst.setString(15, eprform.getEmpid());

            pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_pay_record SET query_string=? where PAY_ID=? and EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, eprform.getPayid());
            pst.setString(3, eprform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteEmpPayRecordData(EmpPayRecordForm eprform) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM EMP_PAY_RECORD WHERE EMP_ID=? AND PAY_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, eprform.getEmpid());
            pst.setInt(2, eprform.getPayid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            String sql = "insert into emp_pay_record_log (not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc, gp, pay_level, pay_cell,increment_reason,if_remuneration,pay_id,REF_CORRECTION_ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, eprform.getNot_type());
            pst.setInt(2, eprform.getNot_id());
            pst.setString(3, eprform.getEmpid());
            if (eprform.getWefDt() != null && !eprform.getWefDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(sdf.parse(eprform.getWefDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (eprform.getWefTime() != null && !eprform.getWefTime().equals("")) {
                pst.setString(5, eprform.getWefTime());
            } else {
                pst.setString(5, null);
            }
            /*if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
             pst.setString(6, eprform.getPayscale());
             } else {
             pst.setString(6, null);
             }*/
            if (eprform.getBasic() == null || eprform.getBasic().equals("")) {
                pst.setDouble(7, 0);
            } else {
                pst.setDouble(7, java.lang.Double.parseDouble(eprform.getBasic()));
            }
            if (eprform.getS_pay() == null || eprform.getS_pay().equalsIgnoreCase("")) {
                pst.setDouble(8, 0);
            } else {
                pst.setDouble(8, java.lang.Double.parseDouble(eprform.getS_pay()));
            }
            if (eprform.getP_pay() == null || eprform.getP_pay().equalsIgnoreCase("")) {
                pst.setDouble(9, 0);
            } else {
                pst.setDouble(9, java.lang.Double.parseDouble(eprform.getP_pay()));
            }
            if (eprform.getOth_pay() == null || eprform.getOth_pay().equalsIgnoreCase("")) {
                pst.setDouble(10, 0);
            } else {
                pst.setDouble(10, java.lang.Double.parseDouble(eprform.getOth_pay()));
            }
            if (eprform.getOth_desc() != null && !eprform.getOth_desc().equals("")) {
                pst.setString(11, eprform.getOth_desc().toUpperCase());
            } else {
                pst.setString(11, null);
            }
            /*if (eprform.getGp() == null || eprform.getGp().equalsIgnoreCase("")) {
             pst.setDouble(12, 0);
             } else {
             pst.setDouble(12, java.lang.Double.parseDouble(eprform.getGp()));
             }*/
            //pst.setString(13, eprform.getPayLevel());
            //pst.setString(14, eprform.getPayCell());
            if (eprform.getPayLevel() != null && !eprform.getPayLevel().equals("")) {
                pst.setString(6, null);
                pst.setDouble(12, 0);
                pst.setString(13, eprform.getPayLevel());
                if (eprform.getPayCell() != null && !eprform.getPayCell().equals("")) {
                    pst.setString(14, eprform.getPayCell());
                }

            } else if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
                pst.setString(6, eprform.getPayscale());
                if (eprform.getGp() == null || eprform.getGp().equals("")) {
                    pst.setDouble(12, 0);
                } else {
                    pst.setDouble(12, java.lang.Double.parseDouble(eprform.getGp()));
                }
                pst.setString(13, null);
                pst.setString(14, null);

            } else {
                pst.setString(6, null);
                pst.setDouble(12, 0);
                pst.setString(13, null);
                pst.setString(14, null);
            }
            pst.setString(15, eprform.getReasonpayfixation());
            pst.setString(16, eprform.getIfRemuneration());
            pst.setInt(17, eprform.getPayid());
            pst.setInt(18, eprform.getRefcorrectionid());
            pst.executeUpdate();

            int payid = eprform.getPayid();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_pay_record_log SET query_string=? where PAY_ID=? and EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, payid);
            pst.setString(3, eprform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void updateEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            String sql = "update emp_pay_record_log set wef=?, weft=?, pay_scale=?, pay=?, s_pay=?, p_pay=?, oth_pay=?, oth_desc=?, gp=?, pay_level=?, pay_cell=?,increment_reason=?,if_remuneration=?  where pay_id=? and emp_id=?";
            pst = con.prepareStatement(sql);
            if (eprform.getWefDt() != null && !eprform.getWefDt().equals("")) {
                pst.setTimestamp(1, new Timestamp(sdf.parse(eprform.getWefDt()).getTime()));
            } else {
                pst.setTimestamp(1, null);
            }
            pst.setString(2, eprform.getWefTime());
            //pst.setString(3, eprform.getPayscale());
            if (eprform.getBasic() == null || eprform.getBasic().equals("")) {
                pst.setDouble(4, 0);
            } else {
                pst.setDouble(4, java.lang.Double.parseDouble(eprform.getBasic()));
            }
            if (eprform.getS_pay() == null || eprform.getS_pay().equals("")) {
                pst.setDouble(5, 0);
            } else {
                pst.setDouble(5, java.lang.Double.parseDouble(eprform.getS_pay()));
            }
            if (eprform.getP_pay() == null || eprform.getP_pay().equals("")) {
                pst.setDouble(6, 0);
            } else {
                pst.setDouble(6, java.lang.Double.parseDouble(eprform.getP_pay()));
            }
            if (eprform.getOth_pay() == null || eprform.getOth_pay().equals("")) {
                pst.setDouble(7, 0);
            } else {
                pst.setDouble(7, java.lang.Double.parseDouble(eprform.getOth_pay()));
            }
            pst.setString(8, eprform.getOth_desc());

            if (eprform.getPayscale() != null && !eprform.getPayscale().equals("")) {
                pst.setString(3, eprform.getPayscale());
                if (eprform.getGp() == null || eprform.getGp().equals("")) {
                    pst.setDouble(9, 0);
                } else {
                    pst.setDouble(9, java.lang.Double.parseDouble(eprform.getGp()));
                }
                pst.setString(10, null);
                pst.setString(11, null);
            } else if (eprform.getPayLevel() != null && !eprform.getPayLevel().equals("")) {
                pst.setString(3, null);
                pst.setDouble(9, 0);
                pst.setString(10, eprform.getPayLevel());
                if (eprform.getPayCell() != null && !eprform.getPayCell().equals("")) {
                    pst.setString(11, eprform.getPayCell());
                }
            } else {
                pst.setString(3, null);
                pst.setDouble(9, 0);
                pst.setString(10, null);
                pst.setString(11, null);
            }
            pst.setString(12, eprform.getReasonpayfixation());
            pst.setString(13, eprform.getIfRemuneration());
            pst.setInt(14, eprform.getPayid());
            pst.setString(15, eprform.getEmpid());
            pst.executeUpdate();

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE emp_pay_record SET query_string=? where PAY_ID=? and EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, eprform.getPayid());
            pst.setString(3, eprform.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void deleteEmpPayRecordDataSBCorrection(EmpPayRecordForm eprform) {
        
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM EMP_PAY_RECORD_LOG WHERE EMP_ID=? AND PAY_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, eprform.getEmpid());
            pst.setInt(2, eprform.getPayid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

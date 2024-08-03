/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.lic;

import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.lic.Lic;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas Jena
 */
public class LicDAOImpl implements LicDAO {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public ArrayList getLicList(String empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList licEmpList = new ArrayList();
        try {
            conn = dataSource.getConnection();
            String licListQuery = "SELECT * FROM EMP_LIC INNER JOIN G_LIC_TYPE ON EMP_LIC.INSURANCE_TYPE = G_LIC_TYPE.LIC_TYPE WHERE STATUS='SANCTIONED' AND EMP_ID=?";
            pstmt = conn.prepareStatement(licListQuery);
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            String status = "";
            int i = 0;
            while (rs.next()) {
                i++;
                Lic licBean = new Lic();
                licBean.setSlno(i);
                licBean.setElId(rs.getBigDecimal("EL_ID"));
                licBean.setInsuranceType(rs.getString("INSURANCE_TYPE"));
                licBean.setEmpid(empId);
                licBean.setPolicyNo(rs.getString("POLICY_NO"));
                licBean.setSubAmount(rs.getInt("SUB_AMT"));
                licBean.setWef(rs.getString("WEF"));
                if (rs.getString("is_valid").equals("Y")) {
                    status = "Active";
                } else {
                    status = "Stopped";
                }                
                licBean.setStatus(status);
                licBean.setTrDataType(rs.getString("TR_DATA_TYPE"));
                licBean.setBtid(rs.getString("BT_ID"));
                licBean.setMonth(rs.getString("month"));
                licBean.setYear(rs.getString("year"));
                licEmpList.add(licBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return licEmpList;
    }
    
    @Override
    public Lic editLicData(String empId, BigDecimal elId) {
        Lic lic = new Lic();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            conn = dataSource.getConnection();
            String licEditQuery = "select *,to_char(wef, 'DD-Mon-YYYY') as formatWef from EMP_LIC WHERE EMP_ID=? and EL_ID=?";
            pstmt = conn.prepareStatement(licEditQuery);
            pstmt.setString(1, empId);
            pstmt.setBigDecimal(2, elId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lic.setElId(elId);
                lic.setPolicyNo(rs.getString("POLICY_NO"));
                lic.setSubAmount(rs.getInt("SUB_AMT"));
                lic.setNote(rs.getString("NOTE"));
                lic.setInsuranceType(rs.getString("INSURANCE_TYPE"));
                lic.setMonth(rs.getString("month"));
                lic.setYear(rs.getString("year"));
                lic.setEmpid(empId);
                
                lic.setWef(rs.getString("formatWef"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return lic;
    }
    
    @Override
    public void saveLicData(Lic licBean) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        int result = 0;
        BigDecimal mcode = null;
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            conn = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String maxCode = CommonFunctions.getMaxCode("EMP_LIC", "EL_id", conn);
            
            String insertQry = "INSERT INTO EMP_LIC (EL_ID,EMP_ID,POLICY_NO,SUB_AMT,NOTE,STATUS,INSURANCE_TYPE,month,year,wef,is_valid) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(insertQry);
            pstmt.setBigDecimal(1, new BigDecimal(maxCode));
            pstmt.setString(2, licBean.getEmpid());
            pstmt.setString(3, licBean.getPolicyNo());
            pstmt.setInt(4, licBean.getSubAmount());
            // pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(licBean.getWef()).getTime()));
            pstmt.setString(5, licBean.getNote());
            pstmt.setString(6, "SANCTIONED");
            pstmt.setString(7, licBean.getInsuranceType());
            pstmt.setString(8, licBean.getMonth());
            pstmt.setString(9, licBean.getYear());
            // pstmt.setTimestamp(10, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(licBean.getWef()).getTime()));
            if (licBean.getWef() != null && !licBean.getWef().equals("")) {
                pstmt.setTimestamp(10, new Timestamp(sdf.parse(licBean.getWef()).getTime()));
            } else {
                pstmt.setTimestamp(10, null);
            }
            
            pstmt.setString(11, "Y");
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        // return result;
    }
    
    @Override
    public boolean deleteLicData(String elId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean delResult = false;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("DELETE from EMP_LIC where EL_ID = ? ");
            pstmt.setString(1, elId);
            int res = pstmt.executeUpdate();
            if (res == 1) {
                delResult = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return delResult;
    }
    
    @Override
    public void updateEmployeeLicData(Lic licBean) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        int result = 0;
        BigDecimal mcode = null;

        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("UPDATE EMP_LIC SET POLICY_NO=?,SUB_AMT=?,WEF=?,NOTE=?,INSURANCE_TYPE=?,MONTH=?,YEAR=? WHERE EL_ID=?");
            pstmt.setString(1, licBean.getPolicyNo());
            pstmt.setInt(2, licBean.getSubAmount());
            // pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(licBean.getWef()).getTime()));
            if (licBean.getWef() != null && !licBean.getWef().equals("")) {
                pstmt.setTimestamp(3, new Timestamp(sdf.parse(licBean.getWef()).getTime()));
            } else {
                pstmt.setTimestamp(3, null);
            }
            pstmt.setString(4, licBean.getNote());
            pstmt.setString(5, licBean.getInsuranceType());
            pstmt.setString(6, licBean.getMonth());
            pstmt.setString(7, licBean.getYear());
            //   pstmt.setBigDecimal(8, new BigDecimal(maxCode));
            pstmt.setBigDecimal(8, licBean.getElId());
            

            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        // return result;
    }
    
    @Override
    public int deleteLicData(String licId, String status) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_LIC SET is_valid= '" + status + "' WHERE EL_ID =" + licId);
            
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    @Override
    public AqDtlsModel[] getAqDtlsModelFromLICList(List licList, AqmastModel aqmast) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        for (int i = 0; i < licList.size(); i++) {
            Lic licdata = (Lic) licList.get(i);
            if (licdata.getStatus().equalsIgnoreCase("Active")) {                
                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(1);
                aqModel.setAdCode(licdata.getInsuranceType());
                aqModel.setAdDesc(licdata.getInsuranceType());
                aqModel.setAdType("D");
                aqModel.setAltUnit(null);
                aqModel.setDedType("V");
                aqModel.setAdAmt(licdata.getSubAmount());
                aqModel.setAccNo(licdata.getPolicyNo());
                aqModel.setRefDesc(null);
                aqModel.setRefCount(0);
                aqModel.setSchedule(licdata.getInsuranceType());
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(0);
                aqModel.setRepCol(9);
                aqModel.setAdRefId(licdata.getElId() + "");
                aqModel.setBtId(licdata.getBtid());
                aqModel.setInstalCount(1);
                list.add(aqModel);
            }
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }
    
}

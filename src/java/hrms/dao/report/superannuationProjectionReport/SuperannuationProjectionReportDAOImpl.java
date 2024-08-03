package hrms.dao.report.superannuationProjectionReport;

import hrms.common.ChartAttribute;
import hrms.common.ChartDrillDownAttribute;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.Employee;
import hrms.model.master.Department;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportBean;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportForm;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SuperannuationProjectionReportDAOImpl implements SuperannuationProjectionReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getSuperannuationEmpList(String offcode, String fdate, String tdate) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select mobile,emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn, extract (month from dos) super_month, acct_type, post_grp_type  from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                    // + " where cur_off_code=? and  extract (year from dos)= ? order by dos";
                    + " where cur_off_code=? and  (dos BETWEEN ? AND ?) order by dos";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            // pst.setInt(2, year);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            // pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tdate).getTime()));

            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setEmpname(rs.getString("EMPNAME"));
                spbean.setDesignation(rs.getString("spn"));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setAccountType(rs.getString("acct_type"));
                spbean.setSuperannuationMonth(rs.getString("super_month"));
                spbean.setPostGroup(rs.getString("post_grp_type"));
                spbean.setMobile(rs.getString("mobile"));
                emplist.add(spbean);
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
    public ArrayList getDistSuperannuationEmpList(String distcode, String fdate, String tdate) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select emp.*,deploy_type from\n"
                    + "(SELECT replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries,replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum,\n"
                    + "getfullname(emp_id)empname,getofficeen(cur_off_code)ofcname,* FROM emp_mast,g_office WHERE emp_mast.cur_off_code=g_office.off_code \n"
                    + "AND g_office.dist_code=? AND\n"
                    + "department_code = '14' AND g_office.is_ddo='Y' AND\n"
                    + "(dos BETWEEN ? AND ?) and is_regular='Y' and acct_type<>'PRAN')emp\n"
                    + "inner join g_gpf_type on g_gpf_type.gpf_type=emp.gpfseries\n"
                    + "inner join g_deploy_type on emp.dep_code=g_deploy_type.deploy_code order by ofcname";
            pst = con.prepareStatement(sql);
            pst.setString(1, distcode);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
            // pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tdate).getTime()));

            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setEmpname(rs.getString("empname"));
                spbean.setOffName(rs.getString("ofcname"));
                spbean.setDateOfBirth(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setAccountType(rs.getString("acct_type"));
                //spbean.setSuperannuationMonth(rs.getString("super_month"));
                spbean.setPostGroup(rs.getString("post_grp_type"));
                spbean.setMobile(rs.getString("mobile"));
                emplist.add(spbean);
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
    public ArrayList getSuperAnnuationProjectionData(List deptlist, int fromyear, int frommonth, int toyear, int tomonth) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        SuperannuationProjectionReportBean spbean = null;

        ArrayList datalist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String fromDate = fromyear + "-" + frommonth + "-01";
            String toDate = toyear + "-" + tomonth + "-31";

            if (tomonth == 4 || tomonth == 6 || tomonth == 9 || tomonth == 11) {
                toDate = toyear + "-" + tomonth + "-30";
            } else if (tomonth == 2) {
                toDate = toyear + "-" + tomonth + "-28";
                if (ifLeapYear(toyear)) {
                    toDate = toyear + "-" + tomonth + "-29";
                }
            }

            /*String sql = "select count(*) cnt from emp_mast" +
             " inner join g_office on emp_mast.cur_off_code=g_office.off_code" +
             " inner join g_department on g_office.department_code=g_department.department_code" +
             " inner join g_district on g_office.dist_code=g_district.dist_code" +
             " where g_office.department_code=? and g_district.dist_code=? and dos >= ? and dos <= ?";*/
            Department department = new Department();
            for (int i = 0; i < deptlist.size(); i++) {
                department = (Department) deptlist.get(i);

                String deptname = department.getDeptName();
                String deptcode = department.getDeptCode();

                String sql = "select getsuperannuationdata('" + deptcode + "','2115','" + fromDate + "','" + toDate + "') ANGUL,"
                        + "getsuperannuationdata('" + deptcode + "','2124','" + fromDate + "','" + toDate + "') BALANGIR,"
                        + "getsuperannuationdata('" + deptcode + "','2108','" + fromDate + "','" + toDate + "') BALASORE,"
                        + "getsuperannuationdata('" + deptcode + "','2101','" + fromDate + "','" + toDate + "') BARGARH,"
                        + "getsuperannuationdata('" + deptcode + "','2100','" + fromDate + "','" + toDate + "') BBSR,"
                        + "getsuperannuationdata('" + deptcode + "','2109','" + fromDate + "','" + toDate + "') BHADRAK ,"
                        + "getsuperannuationdata('" + deptcode + "','2122','" + fromDate + "','" + toDate + "') BOUDH,"
                        + "getsuperannuationdata('" + deptcode + "','2112','" + fromDate + "','" + toDate + "') CUTTACK,"
                        + "getsuperannuationdata('" + deptcode + "','2104','" + fromDate + "','" + toDate + "') DEOGARH,"
                        + "getsuperannuationdata('" + deptcode + "','2114','" + fromDate + "','" + toDate + "') DHENKANAL,"
                        + "getsuperannuationdata('" + deptcode + "','2120','" + fromDate + "','" + toDate + "') GAJAPATI,"
                        + "getsuperannuationdata('" + deptcode + "','2119','" + fromDate + "','" + toDate + "') GANJAM,"
                        + "getsuperannuationdata('" + deptcode + "','2111','" + fromDate + "','" + toDate + "') JAGATSINGHPUR,"
                        + "getsuperannuationdata('" + deptcode + "','2113','" + fromDate + "','" + toDate + "') JAJPUR,"
                        + "getsuperannuationdata('" + deptcode + "','2102','" + fromDate + "','" + toDate + "') JHARSUGUDA,"
                        + "getsuperannuationdata('" + deptcode + "','2126','" + fromDate + "','" + toDate + "') KALAHANDI,"
                        + "getsuperannuationdata('" + deptcode + "','2121','" + fromDate + "','" + toDate + "') KANDHAMAL,"
                        + "getsuperannuationdata('" + deptcode + "','2110','" + fromDate + "','" + toDate + "') KENDRAPARA,"
                        + "getsuperannuationdata('" + deptcode + "','2106','" + fromDate + "','" + toDate + "') KEONJHAR,"
                        + "getsuperannuationdata('" + deptcode + "','2117','" + fromDate + "','" + toDate + "') KHURDA,"
                        + "getsuperannuationdata('" + deptcode + "','2129','" + fromDate + "','" + toDate + "') KORAPUT,"
                        + "getsuperannuationdata('" + deptcode + "','2130','" + fromDate + "','" + toDate + "') MALKANGIRI,"
                        + "getsuperannuationdata('" + deptcode + "','2107','" + fromDate + "','" + toDate + "') MAYURBHANJ,"
                        + "getsuperannuationdata('" + deptcode + "','2128','" + fromDate + "','" + toDate + "') NABARANGPUR,"
                        + "getsuperannuationdata('" + deptcode + "','2116','" + fromDate + "','" + toDate + "') NAYAGARH,"
                        + "getsuperannuationdata('" + deptcode + "','2125','" + fromDate + "','" + toDate + "') NUAPADA,"
                        + "getsuperannuationdata('" + deptcode + "','2118','" + fromDate + "','" + toDate + "') PURI,"
                        + "getsuperannuationdata('" + deptcode + "','2127','" + fromDate + "','" + toDate + "') RAYAGADA,"
                        + "getsuperannuationdata('" + deptcode + "','2103','" + fromDate + "','" + toDate + "') SAMBALPUR,"
                        + "getsuperannuationdata('" + deptcode + "','2123','" + fromDate + "','" + toDate + "') SUBARNAPUR,"
                        + "getsuperannuationdata('" + deptcode + "','2105','" + fromDate + "','" + toDate + "') SUNDARGARH";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    spbean = new SuperannuationProjectionReportBean();

                    spbean.setDeptName(deptname);

                    spbean.setAngData(rs.getInt("ANGUL"));

                    spbean.setBlgData(rs.getInt("BALANGIR"));

                    spbean.setBlsData(rs.getInt("BALASORE"));

                    spbean.setBgrData(rs.getInt("BARGARH"));

                    spbean.setBbsrData(rs.getInt("BBSR"));

                    spbean.setBdkData(rs.getInt("BHADRAK"));

                    spbean.setBdhData(rs.getInt("BOUDH"));

                    spbean.setCtcData(rs.getInt("CUTTACK"));

                    spbean.setDgrData(rs.getInt("DEOGARH"));

                    spbean.setDklData(rs.getInt("DHENKANAL"));

                    spbean.setGjpData(rs.getInt("GAJAPATI"));

                    spbean.setGjmData(rs.getInt("GANJAM"));

                    spbean.setJspData(rs.getInt("JAGATSINGHPUR"));

                    spbean.setJprData(rs.getInt("JAJPUR"));

                    spbean.setJsdData(rs.getInt("JHARSUGUDA"));

                    spbean.setKldData(rs.getInt("KALAHANDI"));

                    spbean.setPlbData(rs.getInt("KANDHAMAL"));

                    spbean.setKpdData(rs.getInt("KENDRAPARA"));

                    spbean.setKjrData(rs.getInt("KEONJHAR"));

                    spbean.setKrdData(rs.getInt("KHURDA"));

                    spbean.setKptData(rs.getInt("KORAPUT"));

                    spbean.setMkgData(rs.getInt("MALKANGIRI"));

                    spbean.setMbjData(rs.getInt("MAYURBHANJ"));

                    spbean.setNbrData(rs.getInt("NABARANGPUR"));

                    spbean.setNgrData(rs.getInt("NAYAGARH"));

                    spbean.setNpdData(rs.getInt("NUAPADA"));

                    spbean.setPriData(rs.getInt("PURI"));

                    spbean.setRgdData(rs.getInt("RAYAGADA"));

                    spbean.setSbpData(rs.getInt("SAMBALPUR"));

                    spbean.setSnpData(rs.getInt("SUBARNAPUR"));

                    spbean.setSngData(rs.getInt("SUNDARGARH"));

                    datalist.add(spbean);
                }
            }

            /*District district = null;
             for(int i = 0; i < distlist.size(); i++){
             district = (District)distlist.get(i);
                
             String distcode = district.getDistCode();
                
             pst.setString(1,deptcode);
             pst.setString(2,distcode);
             pst.setTimestamp(3, new Timestamp(sdf.parse("2019-12-01").getTime()));
             pst.setTimestamp(4, new Timestamp(sdf.parse("2020-01-31").getTime()));
             //pst.setString(3,"2019-12-01");
             //pst.setString(4,"2020-01-31");
             rs = pst.executeQuery();
             spbean = new SuperannuationProjectionReportBean();
             if(rs.next()){
             if(distcode.equals("2115")){
             spbean.setAngData(rs.getInt("cnt"));
             }else if(distcode.equals("2124")){
             spbean.setBlgData(rs.getInt("cnt"));
             }else if(distcode.equals("2108")){
             spbean.setBlsData(rs.getInt("cnt"));
             }else if(distcode.equals("2101")){
             spbean.setBgrData(rs.getInt("cnt"));
             }else if(distcode.equals("2100")){
             spbean.setBbsrData(rs.getInt("cnt"));
             }else if(distcode.equals("2109")){
             spbean.setBdkData(rs.getInt("cnt"));
             }else if(distcode.equals("2122")){
             spbean.setBdhData(rs.getInt("cnt"));
             }else if(distcode.equals("2112")){
             spbean.setCtcData(rs.getInt("cnt"));
             }else if(distcode.equals("2104")){
             spbean.setDgrData(rs.getInt("cnt"));
             }else if(distcode.equals("2114")){
             spbean.setDhkData(rs.getInt("cnt"));
             }else if(distcode.equals("2120")){
             spbean.setGjpData(rs.getInt("cnt"));
             }else if(distcode.equals("2119")){
             spbean.setGjmData(rs.getInt("cnt"));
             }else if(distcode.equals("2111")){
             spbean.setJspData(rs.getInt("cnt"));
             }else if(distcode.equals("2113")){
             spbean.setJprData(rs.getInt("cnt"));
             }else if(distcode.equals("2102")){
             spbean.setJsdData(rs.getInt("cnt"));
             }else if(distcode.equals("2126")){
             spbean.setKldData(rs.getInt("cnt"));
             }else if(distcode.equals("2121")){
             spbean.setPlbData(rs.getInt("cnt"));
             }else if(distcode.equals("2110")){
             spbean.setKpdData(rs.getInt("cnt"));
             }else if(distcode.equals("2106")){
             spbean.setKjrData(rs.getInt("cnt"));
             }else if(distcode.equals("2117")){
             spbean.setKrdData(rs.getInt("cnt"));
             }else if(distcode.equals("2129")){
             spbean.setKptData(rs.getInt("cnt"));
             }else if(distcode.equals("2130")){
             spbean.setMkrData(rs.getInt("cnt"));
             }else if(distcode.equals("2107")){
             spbean.setMbjData(rs.getInt("cnt"));
             }else if(distcode.equals("2128")){
             spbean.setNbrData(rs.getInt("cnt"));
             }else if(distcode.equals("2116")){
             spbean.setNgrData(rs.getInt("cnt"));
             }else if(distcode.equals("2125")){
             spbean.setNpdData(rs.getInt("cnt"));
             }else if(distcode.equals("2118")){
             spbean.setPriData(rs.getInt("cnt"));
             }else if(distcode.equals("2127")){
             spbean.setRgdData(rs.getInt("cnt"));
             }else if(distcode.equals("2103")){
             spbean.setSbpData(rs.getInt("cnt"));
             }else if(distcode.equals("2123")){
             spbean.setSbnData(rs.getInt("cnt"));
             }else if(distcode.equals("2105")){
             spbean.setSngData(rs.getInt("cnt"));
             }
             datalist.add(spbean);
             }
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return datalist;
    }

    private boolean ifLeapYear(int theYear) {
        boolean res = false;
        try {
            // Is theYear Divisible by 4?
            if (theYear % 4 == 0) {
                // Is theYear Divisible by 4 but not 100?
                if (theYear % 100 != 0) {
                    res = true;
                } // Is theYear Divisible by 4 and 100 and 400?
                else if (theYear % 400 == 0) {
                    res = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String getSuperannuationDataGroupWise(int year) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;
        String sql = "";
        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;

        String yearData = "";
        try {

            int groupA = 0;
            int groupB = 0;
            int groupC = 0;
            int groupD = 0;

            con = this.dataSource.getConnection();
            sql = "select count(*) cnt, post_grp_type from emp_mast"
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code"
                    + " inner join g_department on g_office.department_code=g_department.department_code "
                    + " where  extract (year from dos)= ? AND IS_REGULAR <>'N' "
                    + " group by post_grp_type"
                    + " order by post_grp_type";
            pst = con.prepareStatement(sql);
            pst.setInt(1, year);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("post_grp_type") != null && !rs.getString("post_grp_type").equals("")) {
                    if (rs.getString("post_grp_type").equalsIgnoreCase("A")) {
                        groupA = rs.getInt("cnt");
                    } else if (rs.getString("post_grp_type").equalsIgnoreCase("B")) {
                        groupB = rs.getInt("cnt");
                    } else if (rs.getString("post_grp_type").equalsIgnoreCase("C")) {
                        groupC = rs.getInt("cnt");
                    } else if (rs.getString("post_grp_type").equalsIgnoreCase("D")) {
                        groupD = rs.getInt("cnt");
                    }
                }
            }
            yearData = ": [['Group-A', " + groupA + "],['Group-B', " + groupB + "],['Group-C', " + groupC + "],['Group-D', " + groupD + "],]";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return yearData;
    }

    @Override
    public ArrayList getEmployeesSuperannuationListGroupWise(int year, int month, String offCode, String postGroup, String acctType) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            /* DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

             String startDate = year + "-" + month + "-01";
             String endDate = "";
             if (month == 3 && month == 5 && month == 8 && month == 10) {
             endDate = year + "-" + month + "-30";
             } else {
             endDate = year + "-" + month + "-31";
             }*/
            if (acctType != null && acctType.equals("ALL")) {
                String sql = "SELECT emp_id, gpf_no, dob, dos,acct_type, post_grp_type, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, f_name, post FROM emp_mast "
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " left outer join g_post on g_spc.gpc=g_post.post_code "
                        + "  WHERE emp_mast.cur_off_code=?  AND  EXTRACT(YEAR FROM dos)=? and EXTRACT(month FROM dos)=? and  post_grp_type=? and is_regular <> 'N'  ORDER BY f_name ";
                pst = con.prepareStatement(sql);
                pst.setString(1, offCode);
                pst.setInt(2, year);
                pst.setInt(3, month);
                pst.setString(4, postGroup);

            } else {
                String sql = "SELECT emp_id, gpf_no, dob, dos,acct_type, post_grp_type, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, f_name, post FROM emp_mast "
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " left outer join g_post on g_spc.gpc=g_post.post_code "
                        + "  WHERE emp_mast.cur_off_code=?  AND  EXTRACT(YEAR FROM dos)=? and EXTRACT(month FROM dos)=? and  post_grp_type=? and acct_type=?  and is_regular <> 'N'  ORDER BY f_name ";
                pst = con.prepareStatement(sql);
                pst.setString(1, offCode);
                pst.setInt(2, year);
                pst.setInt(3, month);
                pst.setString(4, postGroup);
                pst.setString(5, acctType);
            }
            rs = pst.executeQuery();

            SuperannuationProjectionReportBean spbean = new SuperannuationProjectionReportBean();
            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setEmpname(rs.getString("EMPNAME"));
                spbean.setDesignation(rs.getString("post"));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setAccountType(rs.getString("acct_type"));

                spbean.setPostGroup(rs.getString("post_grp_type"));
                emplist.add(spbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList getEmployeesSuperannuationList(int year, int month, String offCode, String acctType) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;
        String sql = null;

        ArrayList emplist = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            /*DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

             String startDate = year + "-" + month + "-01";
             String endDate = "";
             if (month == 3 || month == 5 || month == 8 || month == 10 || month == 12) {
             endDate = year + "-" + month + "-31";
             } else {
             endDate = year + "-" + month + "-30";
             }*/
            if (acctType != null && acctType.equals("ALL")) {
                sql = "SELECT emp_id, gpf_no, dob, dos, acct_type, post_grp_type, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, f_name, post FROM emp_mast "
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " left outer join g_post on g_spc.gpc=g_post.post_code "
                        + "  WHERE emp_mast.cur_off_code=?  AND  EXTRACT(YEAR FROM dos)=? and EXTRACT(month FROM dos)=? and is_regular <> 'N' ORDER BY f_name ";
                pst = con.prepareStatement(sql);
                pst.setString(1, offCode);
                pst.setInt(2, year);
                pst.setInt(3, month);
            } else {
                sql = "SELECT emp_id, gpf_no, dob, dos, acct_type, post_grp_type, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, f_name, post FROM emp_mast "
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                        + " left outer join g_post on g_spc.gpc=g_post.post_code "
                        + "  WHERE emp_mast.cur_off_code=?  AND  EXTRACT(YEAR FROM dos)=? and EXTRACT(month FROM dos)=? and acct_type=? and is_regular <> 'N' ORDER BY f_name ";
                pst = con.prepareStatement(sql);
                pst.setString(1, offCode);
                pst.setInt(2, year);
                pst.setInt(3, month);
                pst.setString(4, acctType);
            }
            rs = pst.executeQuery();

            SuperannuationProjectionReportBean spbean = new SuperannuationProjectionReportBean();
            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setEmpname(rs.getString("EMPNAME"));
                spbean.setDesignation(rs.getString("post"));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setAccountType(rs.getString("acct_type"));

                spbean.setPostGroup(rs.getString("post_grp_type"));
                emplist.add(spbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList ViewSupperannuationProjectionReportAdministartiveDeptOnly(int year, int month, String acctType) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            /* String startDate = year + "-" + month + "-01";
             String endDate = "";
             if (month == 3 && month == 5 && month == 8 && month == 10) {
             endDate = year + "-" + month + "-30";
             } else {
             endDate = year + "-" + month + "-31";

             }*/
            String stm = "SELECT department_name,g_department.department_code, ao_off_code FROM g_department WHERE g_department.if_active='Y' ORDER BY department_name";
            pst1 = con.prepareStatement(stm);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setDepartmentname(rs1.getString("department_name"));
                //String deptCode = rs1.getString("department_code");
                spbean.setDeptCode(rs1.getString("ao_off_code"));

                if (acctType != null && acctType.equals("ALL")) {

                    sql = "SELECT count(*) as TotalRecord,post_grp_type FROM emp_mast INNER JOIN g_office"
                            + " ON emp_mast.cur_off_code=g_office.off_code AND g_office.department_code=?  WHERE EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND IS_REGULAR <>'N' GROUP BY post_grp_type ORDER BY post_grp_type ";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, rs1.getString("department_code"));
                    pst.setInt(2, year);
                    pst.setInt(3, month);

                } else {
                    sql = "SELECT count(*) as TotalRecord,post_grp_type FROM emp_mast INNER JOIN g_office"
                            + " ON emp_mast.cur_off_code=g_office.off_code AND g_office.department_code=?  WHERE EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND ACCT_TYPE=? AND IS_REGULAR <>'N' GROUP BY post_grp_type ORDER BY post_grp_type ";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, rs1.getString("department_code"));
                    pst.setInt(2, year);
                    pst.setInt(3, month);
                    pst.setString(4, acctType);

                }
                rs = pst.executeQuery();
                int totalCnt = 0;
                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;
                int Other = 0;
                while (rs.next()) {
                    //  spbean.setTotalRecord(rs.getInt("TotalRecord"));
                    String grpType = rs.getString("post_grp_type");
                    //spbean.setTotalRecord(rs.getInt("TotalRecord"));
                    if (grpType != null && !grpType.equals("") && grpType.equals("A")) {
                        groupA = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("B")) {
                        groupB = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("C")) {
                        groupC = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("D")) {
                        groupD = rs.getInt("TotalRecord");
                    } else {
                        Other = rs.getInt("TotalRecord");

                    }

                }
                groupD = groupD + Other;
                totalCnt = groupA + groupB + groupC + groupD;
                spbean.setTotalRecord(totalCnt);
                spbean.setGroupA(groupA);
                spbean.setGroupB(groupB);
                spbean.setGroupC(groupC);
                spbean.setGroupD(groupD);

                emplist.add(spbean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList ViewSupperannuationProjectionReportDeptWise(int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            /*String startDate = year + "-" + month + "-01";
             String endDate = "";
             if (month == 3 && month == 5 && month == 8 && month == 10) {
             endDate = year + "-" + month + "-30";
             } else {
             endDate = year + "-" + month + "-31";
             }*/
            String stm = "SELECT department_name,g_department.department_code FROM g_department WHERE g_department.if_active='Y' ORDER BY department_name";
            pst1 = con.prepareStatement(stm);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setDepartmentname(rs1.getString("department_name"));
                String deptCode = rs1.getString("department_code");
                spbean.setDeptCode(deptCode);

                String sql = "SELECT count(*) as TotalRecord,post_grp_type FROM emp_mast,g_office\n"
                        + "WHERE emp_mast.cur_off_code=g_office.off_code AND  department_code =? AND g_office.is_ddo='Y' \n"
                        + "AND EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND IS_REGULAR <>'N'  \n"
                        + "GROUP BY post_grp_type ORDER BY post_grp_type";
                pst = con.prepareStatement(sql);
                pst.setString(1, deptCode);
                pst.setInt(2, year);
                pst.setInt(3, month);
                rs = pst.executeQuery();
                int totalCnt = 0;
                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;
                int Other = 0;
                while (rs.next()) {
                    //  spbean.setTotalRecord(rs.getInt("TotalRecord"));
                    String grpType = rs.getString("post_grp_type");
                    //spbean.setTotalRecord(rs.getInt("TotalRecord"));
                    if (grpType != null && !grpType.equals("") && grpType.equals("A")) {
                        groupA = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("B")) {
                        groupB = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("C")) {
                        groupC = rs.getInt("TotalRecord");
                    } else if (grpType != null && !grpType.equals("") && grpType.equals("D")) {
                        groupD = rs.getInt("TotalRecord");
                    } else {
                        Other = rs.getInt("TotalRecord");

                    }

                }
                groupD = groupD + Other;
                totalCnt = groupA + groupB + groupC + groupD;
                spbean.setTotalRecord(totalCnt);
                spbean.setGroupA(groupA);
                spbean.setGroupB(groupB);
                spbean.setGroupC(groupC);
                spbean.setGroupD(groupD);

                emplist.add(spbean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList ViewSupperannuationProjectionReportDistWise(String deptCode, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String startDate = year + "-" + month + "-01";
            String endDate = "";
            if (month == 3 && month == 5 && month == 8 && month == 10) {
                endDate = year + "-" + month + "-30";
            } else {
                endDate = year + "-" + month + "-31";
            }
            String stm = "SELECT g_district.dist_code,dist_name FROM g_district INNER JOIN g_office ON g_district.dist_code=g_office.dist_code WHERE g_office.is_ddo='Y' GROUP BY  g_district.dist_code ORDER BY dist_name";
            pst1 = con.prepareStatement(stm);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setDistrictname(rs1.getString("dist_name"));
                String distCode = rs1.getString("dist_code");
                spbean.setDistrictCode(distCode);

                String sql = "SELECT count(*) as TotalRecord FROM emp_mast,g_office WHERE emp_mast.cur_off_code=g_office.off_code AND g_office.dist_code=?  AND  department_code = ? AND g_office.is_ddo='Y' AND "
                        + " dos >= ? and dos <= ? ";
                pst = con.prepareStatement(sql);
                pst.setString(1, distCode);
                pst.setString(2, deptCode);
                pst.setTimestamp(3, new Timestamp(formatDate.parse(startDate).getTime()));
                pst.setTimestamp(4, new Timestamp(formatDate.parse(endDate).getTime()));
                rs = pst.executeQuery();
                int totalCnt = 0;
                while (rs.next()) {
                    spbean.setTotalRecord(rs.getInt("TotalRecord"));

                }
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList ViewSupperannuationProjectionReportOfficeWise(String districtCode, int year, int month, String deptCode) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String startDate = year + "-" + month + "-01";
            String endDate = "";
            if (month == 3 && month == 5 && month == 8 && month == 10) {
                endDate = year + "-" + month + "-30";
            } else {
                endDate = year + "-" + month + "-31";
            }

            String sql = "SELECT count(*) as TotalRecord,off_en,off_code,g_office.ddo_code FROM emp_mast,g_office WHERE emp_mast.cur_off_code=g_office.off_code AND g_office.dist_code=?  AND  department_code = ? AND 	g_office.is_ddo='Y' AND  dos >= ? and dos <= ? GROUP BY off_code,off_en ";
            pst = con.prepareStatement(sql);
            pst.setString(1, districtCode);
            pst.setString(2, deptCode);
            pst.setTimestamp(3, new Timestamp(formatDate.parse(startDate).getTime()));
            pst.setTimestamp(4, new Timestamp(formatDate.parse(endDate).getTime()));
            rs = pst.executeQuery();
            int totalCnt = 0;
            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setOffName(rs.getString("off_en"));
                spbean.setOffCode(rs.getString("off_code"));
                spbean.setDeptCode(rs.getString("ddo_code"));
                spbean.setTotalRecord(rs.getInt("TotalRecord"));
                emplist.add(spbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList ViewSupperannuationProjectionReportEmpWise(String offcode, int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String startDate = year + "-" + month + "-01";
            String endDate = "";
            if (month == 3 && month == 5 && month == 8 && month == 10) {
                endDate = year + "-" + month + "-30";
            } else {
                endDate = year + "-" + month + "-31";
            }

            String sql = "select emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,doe_gov,dos,spn from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " WHERE cur_off_code=? and dos >= ? and dos <= ? order by dos desc";
            pst = con.prepareStatement(sql);

            pst.setString(1, offcode);
            pst.setTimestamp(2, new Timestamp(formatDate.parse(startDate).getTime()));
            pst.setTimestamp(3, new Timestamp(formatDate.parse(endDate).getTime()));
            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setGpfNo(rs.getString("gpf_no"));
                spbean.setEmpname(rs.getString("EMPNAME"));
                spbean.setDesignation(rs.getString("spn"));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                emplist.add(spbean);
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
    public ArrayList ViewSupperannuationProjectionReportOnlyDept(int year, int month) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String startDate = year + "-" + month + "-01";
            String endDate = "";
            if (month == 3 && month == 5 && month == 8 && month == 10) {
                endDate = year + "-" + month + "-30";
            } else {
                endDate = year + "-" + month + "-31";
            }
            String stm = "SELECT department_name,g_department.department_code FROM g_department WHERE g_department.if_active='Y' ORDER BY department_name";
            pst1 = con.prepareStatement(stm);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setDepartmentname(rs1.getString("department_name"));
                String deptCode = rs1.getString("department_code");
                spbean.setDeptCode(deptCode);

                String sql = "SELECT count(*) as TotalRecord,g_office.off_code FROM emp_mast,g_office WHERE emp_mast.cur_off_code=g_office.off_code AND g_office.off_code LIKE 'OLS%' AND  department_code = ? AND 	g_office.is_ddo='Y' AND  dos >= ? and dos <= ? GROUP BY off_code ";
                pst = con.prepareStatement(sql);
                pst.setString(1, deptCode);
                pst.setTimestamp(2, new Timestamp(formatDate.parse(startDate).getTime()));
                pst.setTimestamp(3, new Timestamp(formatDate.parse(endDate).getTime()));
                rs = pst.executeQuery();
                int totalCnt = 0;
                String offCode = "";
                while (rs.next()) {
                    spbean.setTotalRecord(rs.getInt("TotalRecord"));
                    offCode = rs.getString("off_code");

                }
                spbean.setOffCode(offCode);
                emplist.add(spbean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getSuperannuationReportPeriodWise(String fromPeriod, String toPeriod) {

        ChartAttribute ca = new ChartAttribute();

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement(" select count(*) noofemp, extract (MONTH from dos) super_month from emp_mast where  dos between '" + fromPeriod + "' and '" + toPeriod + "' "
                    + " group by extract (MONTH from dos) "
                    + " order by super_month ");

            rs = pst.executeQuery();
            while (rs.next()) {
                ca = new ChartAttribute();
                ca.setName(getMonthAsFullString(rs.getInt("super_month") - 1));
                ca.setDrilldown(ca.getName());
                ca.setY(rs.getInt("noofemp"));
                li.add(ca);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public String getSuperannuationReportPeriodDepartmentWise(String fromPeriod, String toPeriod) {

        ChartDrillDownAttribute ca = new ChartDrillDownAttribute();

        Connection con = null;

        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        String str = "";

        try {
            con = this.dataSource.getConnection();

            pst2 = con.prepareStatement("select count(*) noof_emp, extract (MONTH from dos) super_month, extract (year from dos) super_year, g_department.dept_abbr, g_department.department_abbr from emp_mast  "
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + " inner join g_department on g_office.department_code=g_department.department_code "
                    + " where  extract (MONTH from dos)=? and extract (year from dos)=? and if_active='Y' "
                    + " group by  g_department.dept_abbr, g_department.department_abbr, extract (year from dos) , extract (MONTH from dos) "
                    + " order by g_department.dept_abbr ");

            pst1 = con.prepareStatement("select  extract (MONTH from dos) super_month, extract (year from dos) super_year from emp_mast  "
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code "
                    + " inner join g_department on g_office.department_code=g_department.department_code"
                    + " where  dos between '" + fromPeriod + "' and '" + toPeriod + "' and if_active='Y' "
                    + " group by extract (MONTH from dos) , extract (year from dos)  "
                    + " order by super_year, super_month");

            rs1 = pst1.executeQuery();

            while (rs1.next()) {

                if (str != null && !str.equals("")) {
                    str = str + " ,{ name: \"" + getMonthAsFullString(rs1.getInt("super_month") - 1) + "\", id:\"" + getMonthAsFullString(rs1.getInt("super_month") - 1) + "\", data: [";
                } else {
                    str = "{ name: \"" + getMonthAsFullString(rs1.getInt("super_month") - 1) + "\", id:\"" + getMonthAsFullString(rs1.getInt("super_month") - 1) + "\", data: [";
                }

                ca.setId(getMonthAsFullString(rs1.getInt("super_month") - 1));
                ca.setName(getMonthAsFullString(rs1.getInt("super_month") - 1));

                pst2.setInt(1, rs1.getInt("super_month"));
                pst2.setInt(2, rs1.getInt("super_year"));

                rs2 = pst2.executeQuery();

                String str2 = "";
                while (rs2.next()) {
                    if (str2 != null && !str2.equals("")) {
                        str2 = str2 + ", [" + "\"" + rs2.getString("dept_abbr") + "\", " + rs2.getInt("noof_emp") + "]";
                    } else {
                        str2 = "[" + "\"" + rs2.getString("dept_abbr") + "\", " + rs2.getInt("noof_emp") + "]";
                    }

                }

                str = str + str2 + "] }";

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return str;
    }

    public static String getMonthAsFullString(int month) {
        String monthString = null;
        switch (month) {
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;
        }
        return monthString;
    }

    @Override
    public void downloadSuperannuationProjectHOA(OutputStream out, String fileName, String offCode, WritableWorkbook workbook, int month, int year, String acctType) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement selectpst = null;
        ResultSet rs = null;
        ResultSet selectrs = null;
        int row = 0;
        SuperannuationProjectionReportBean spbean = null;
        SuperannuationProjectionReportBean spbeannew = null;
        ArrayList officeList = new ArrayList();
        int rowCnt = 0;
        String sql = null;

        try {
            con = this.dataSource.getConnection();
            WritableSheet sheet = workbook.createSheet("SuperAnnuationProjection", 0);
            int heightInPoints = 44 * 20;
            //Cells cells = worksheet.getCells();

            WritableFont headformat1 = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
            WritableFont titleFont4 = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat titleformat4 = new WritableCellFormat(titleFont4);
            titleformat4.setAlignment(Alignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat4.setBorder(Border.ALL, BorderLineStyle.THIN);
            titleformat4.setWrap(true);

            WritableCellFormat headcell1 = new WritableCellFormat(headformat1);
            headcell1.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell1.setAlignment(Alignment.CENTRE);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setWrap(true);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            jxl.write.Number num = null;

            sheet.mergeCells(0, 0, 9, 0);
            sheet.setRowView(0, heightInPoints);
            Label label = new Label(0, 0, "SuperAnnuation Projection Report", headcell1);//column,row
            sheet.addCell(label);

            row = row + 1;

            //sheet.setColumnView(0, 14);
            label = new Label(0, row, "Sl No", headcell);
            sheet.addCell(label);

            sheet.setColumnView(1, 35);
            label = new Label(1, row, "Name", headcell);
            sheet.addCell(label);

            sheet.setColumnView(2, 20);
            label = new Label(2, row, "Designation", headcell);
            sheet.addCell(label);

            //sheet.setColumnView(3, 12);
            label = new Label(3, row, "Hrms ID", headcell);
            sheet.addCell(label);

            //sheet.setColumnView(4, 12);
            label = new Label(4, row, "Gpf No", headcell);
            sheet.addCell(label);

            sheet.setColumnView(5, 16);
            label = new Label(5, row, "Date of Birth", headcell);
            sheet.addCell(label);

            sheet.setColumnView(6, 16);
            label = new Label(6, row, "Date of Superannuation", headcell);
            sheet.addCell(label);

            sheet.setColumnView(7, 16);
            label = new Label(7, row, "Date of Entry into Govt. Service", headcell);
            sheet.addCell(label);

            //sheet.setColumnView(8, 12);
            label = new Label(8, row, "Post Group", headcell);
            sheet.addCell(label);

            label = new Label(9, row, "Account Type", headcell);
            sheet.addCell(label);
            if (acctType.equals("ALL")) {
                sql = "SELECT EMP_ID,GPF_NO,CUR_OFF_CODE,POST,CUR_SPC,ACCT_TYPE,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') EMP_NM,TO_CHAR(DOB,'DD-MON-YYYY')DOB,\n"
                        + "TO_CHAR(DOS,'DD-MON-YYYY')DOS,TO_CHAR(DOE_GOV,'DD-MON-YYYY')DOE_GOV,POST_GRP_TYPE FROM\n"
                        + "EMP_MAST EMP\n"
                        + "LEFT OUTER JOIN\n"
                        + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE\n"
                        + "WHERE CUR_OFF_CODE=? AND EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND IS_REGULAR <>'N' ORDER BY EMP.POST_GRP_TYPE";
                selectpst = con.prepareStatement(sql);
                selectpst.setString(1, offCode);
                selectpst.setInt(2, year);
                selectpst.setInt(3, month);
            } else {
                sql = "SELECT EMP_ID,GPF_NO,CUR_OFF_CODE,POST,CUR_SPC,ACCT_TYPE,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') EMP_NM,TO_CHAR(DOB,'DD-MON-YYYY')DOB,\n"
                        + "TO_CHAR(DOS,'DD-MON-YYYY')DOS,TO_CHAR(DOE_GOV,'DD-MON-YYYY')DOE_GOV,POST_GRP_TYPE FROM\n"
                        + "EMP_MAST EMP\n"
                        + "LEFT OUTER JOIN\n"
                        + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE\n"
                        + "WHERE CUR_OFF_CODE=? AND EXTRACT(YEAR FROM DOS)=? AND EXTRACT(MONTH FROM DOS)=? AND ACCT_TYPE=? AND IS_REGULAR <>'N' ORDER BY EMP.POST_GRP_TYPE";
                selectpst = con.prepareStatement(sql);
                selectpst.setString(1, offCode);
                selectpst.setInt(2, year);
                selectpst.setInt(3, month);
                selectpst.setString(4, acctType);
            }
            selectrs = selectpst.executeQuery();
            while (selectrs.next()) {
                row = row + 1;
                rowCnt = rowCnt + 1;

                sheet.setColumnView(0, 5);

                label = new Label(0, row, Integer.toString(rowCnt), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(1, 20);
                label = new Label(1, row, selectrs.getString("EMP_NM"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(2, 15);
                label = new Label(2, row, selectrs.getString("POST"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(3, 15);
                label = new Label(3, row, selectrs.getString("EMP_ID"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(4, 15);
                label = new Label(4, row, selectrs.getString("GPF_NO"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(5, 15);
                label = new Label(5, row, selectrs.getString("DOB"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(6, 15);
                label = new Label(6, row, selectrs.getString("DOS"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(7, 15);
                label = new Label(7, row, selectrs.getString("DOE_GOV"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(8, 15);
                label = new Label(8, row, selectrs.getString("POST_GRP_TYPE"), titleformat4);
                sheet.addCell(label);

                sheet.setColumnView(9, 15);
                label = new Label(9, row, selectrs.getString("ACCT_TYPE"), titleformat4);
                sheet.addCell(label);

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(con);

        }

    }

    @Override
    public ArrayList ViewSupperannuationDetailsDistWise(String deptCode, String fdate, String tdate) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs, rs1 = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String sql = "SELECT count(*) as TotalRecord from \n"
                    + "(select replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries,* FROM emp_mast,g_office \n"
                    + " WHERE emp_mast.cur_off_code=g_office.off_code AND g_office.dist_code=?  AND\n"
                    + "department_code = ? AND g_office.is_ddo='Y' AND\n"
                    + "(dos BETWEEN ? AND ?) and is_regular='Y' and acct_type<>'PRAN')emp  \n"
                    + "inner join g_gpf_type on g_gpf_type.gpf_type=emp.gpfseries ";
            pst = con.prepareStatement(sql);

            String stm = "SELECT g_district.dist_code,dist_name FROM g_district INNER JOIN g_office ON g_district.dist_code=g_office.dist_code "
                    + "WHERE g_office.is_ddo='Y' GROUP BY  g_district.dist_code ORDER BY dist_name";
            pst1 = con.prepareStatement(stm);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setDistrictname(rs1.getString("dist_name"));
                String distCode = rs1.getString("dist_code");
                spbean.setDistrictCode(distCode);

                pst.setString(1, distCode);
                pst.setString(2, deptCode);
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy H:m:s").parse(tdate + " 23:59:59").getTime()));
                rs = pst.executeQuery();
                int totalCnt = 0;
                while (rs.next()) {
                    // spbean=new SuperannuationProjectionReportBean();
                    spbean.setTotalRecord(rs.getInt("TotalRecord"));

                }
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getMacpRacpList(String empid) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList macpList = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String sql = "select empmast.emp_id,getfullname(empmast.emp_id)EMPNAME,empmast.dob,doe_gov, \n"
                    + " cast(substring(CAST((current_date-doe_gov)/365 AS TEXT),1,2) as int)yearOfService,yearinservicepolicestation, \n"
                    + " array_to_string(array[emprl.initials,emprl.f_name,emprl.m_name,emprl.l_name],' ')fathersname, \n"
                    + " postingunintdoj,place_posting,passing_training_date,rank_joinin_govservice appointmentrank,present_rank, \n"
                    + " date_of_joining_in_present_rank_district,noofpromotion,cntacp,property_submitted_if_any,property_submitted_on_hrms_byofficer,\n"
                    + " punishmentmajor,punishmentmajorprior,punishmentmajorduring,punishmentmajorfrom,punishmentminor,\n"
                    + " punishmentminorprior,punishmentminorduring,punishmentminorfrom,date_of_joining_in_promotional_rank,dpcifany\n"
                    + " from  \n"
                    + " (select * from emp_mast where emp_id=?)empmast  \n"
                    + " left outer join (select * from police_nomination_form where empid=? limit 1)pnf on empmast.emp_id=pnf.empid \n"
                    + " left outer join (select * from emp_relation where relation='FATHER' and emp_id=? limit 1)emprl  \n"
                    + " on emprl.emp_id=empmast.emp_id \n"
                    + " left outer join (select EMP_ID,COUNT(*)noofpromotion from emp_join where not_type='PROMOTION' AND emp_id=? \n"
                    + "GROUP BY emp_id)epromotion \n"
                    + " on epromotion.emp_id=empmast.emp_id \n"
                    + " left outer join (select emp_id,count(*)cntacp from emp_pay_record where increment_reason is not null  \n"
                    + " and (increment_reason like '%ACP%' OR increment_reason like '%TBA%') \n"
                    + " AND NOT_TYPE = 'PAYFIXATION' and emp_id=? group by emp_id)empacp on empmast.emp_id=empacp.emp_id";
            pst = con.prepareStatement(sql);

            pst.setString(1, empid);
            pst.setString(2, empid);
            pst.setString(3, empid);
            pst.setString(4, empid);
            pst.setString(5, empid);

            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setEmpname(rs.getString("EMPNAME"));
                //spbean.setDesignation(rs.getString("spn"));
                spbean.setDateOfBirth(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                if (rs.getDate("date_of_joining_in_present_rank_district") != null && !rs.getDate("date_of_joining_in_present_rank_district").equals("")) {
                    spbean.setDateOfpresentrank(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_present_rank_district")));
                } else {
                    spbean.setDateOfpresentrank("Not Available");
                }
                if (rs.getString("appointmentrank") != null && !rs.getString("appointmentrank").equals("")) {
                    spbean.setAppointmentrank(rs.getString("appointmentrank"));
                } else {
                    spbean.setAppointmentrank("Not Available");
                }
                if (rs.getString("present_rank") != null && !rs.getString("present_rank").equals("")) {
                    spbean.setPresentrank(rs.getString("present_rank"));
                } else {
                    spbean.setPresentrank("Not Available");
                }
                //spbean.setDateOfpresentrank(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_present_rank_district")));
                //spbean.setAppointmentrank(rs.getString("appointmentrank"));
                //spbean.setPresentrank(rs.getString("present_rank"));
                //spbean.setDateOfSuperannuation(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                spbean.setYearofservice(rs.getString("yearOfService"));
                spbean.setFathername(rs.getString("fathersname"));
                if (rs.getString("noofpromotion") != null && !rs.getString("noofpromotion").equals("")) {
                    spbean.setNoofpromotion(rs.getString("noofpromotion"));
                } else {
                    spbean.setNoofpromotion("0");
                }
                if (rs.getDate("date_of_joining_in_promotional_rank") != null && !rs.getDate("date_of_joining_in_promotional_rank").equals("")) {
                    spbean.setDateofjoininpromotionalrank(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_promotional_rank")));
                } else {
                    spbean.setDateofjoininpromotionalrank("Not Available");
                }
                //spbean.setNoofpromotion(rs.getString("noofpromotion"));
                // spbean.setDateofjoininpromotionalrank(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_joining_in_promotional_rank")));
                if (rs.getString("cntacp") != null && !rs.getString("cntacp").equals("")) {
                    spbean.setNoofupgradation(rs.getString("cntacp"));
                } else {
                    spbean.setNoofupgradation("0");
                }

                if (rs.getString("property_submitted_if_any") != null && !rs.getString("property_submitted_if_any").equals("")) {
                    spbean.setStatuspropertystatement(rs.getString("property_submitted_if_any"));
                    spbean.setDateofpropertystatement(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")));
                } else {
                    spbean.setStatuspropertystatement("NO");
                    spbean.setDateofpropertystatement("Not Available");
                }

                //spbean.setDateofpropertystatement(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_submitted_on_hrms_byofficer")));
                macpList.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return macpList;
    }

    public List getTppoList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList tppoList = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;

        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String sql = "select dos,empdtls.emp_id,getfullname(empdtls.emp_id)fullname,"
                    + "rank_joinin_govservice appointmentrank,present_rank, dpcifany, NOT_TYPE, JOIN_DATE, join_post,ORDNO,ORDDT,"
                    + " OFF_CODE,RLV_DATE,RLV_FROM_SPC,RLV_FROM_SPN rlvFromPost from "
                    + "((select * from emp_mast where emp_id=?)empmast"
                    + " left outer join (select * from police_nomination_form where empid=? limit 1)pnf on empmast.emp_id=pnf.empid)empdtls"
                    + " left outer join (SELECT EMP_NOTIFICATION.emp_Id,DEPARTMENT_NAME,EJ.DOE, EJ.SPC, EJ.NOT_TYPE, EJ.JOIN_DATE,"
                    + "EJ.SPN join_post, EMP_NOTIFICATION.ORDNO,EMP_NOTIFICATION.ORDDT,EMP_TRANSFER.OFF_CODE,RLV_DATE,"
                    + "EMP_RELIEVE.SPC AS RLV_FROM_SPC,GETSPN(EMP_RELIEVE.SPC) AS RLV_FROM_SPN FROM"
                    + " (SELECT NOT_ID,DOE,SPC,NOT_TYPE,JOIN_DATE,GETSPN(SPC) SPN,EMP_ID FROM EMP_JOIN WHERE EMP_ID=? "
                    + " and not_type in ('TRANSFER','POSTING') )EJ INNER JOIN EMP_NOTIFICATION ON EJ.NOT_ID = EMP_NOTIFICATION.NOT_ID"
                    + " INNER JOIN G_SPC ON EJ.SPC=G_SPC.SPC INNER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE=G_SPC.DEPT_CODE"
                    + " LEFT OUTER JOIN EMP_RELIEVE ON EMP_NOTIFICATION.NOT_ID = EMP_RELIEVE.NOT_ID"
                    + " LEFT OUTER JOIN EMP_TRANSFER ON EMP_NOTIFICATION.NOT_ID = EMP_TRANSFER.NOT_ID"
                    + " where extract(year from join_date)>=(extract(year from current_date)-30) ORDER BY JOIN_DATE)postingdtls"
                    + " on empdtls.emp_id=postingdtls.emp_id";

            pst = con.prepareStatement(sql);

            pst.setString(1, empid);
            pst.setString(2, empid);
            pst.setString(3, empid);

            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setEmpname(rs.getString("fullname"));

                if (rs.getString("appointmentrank") != null && !rs.getString("appointmentrank").equals("")) {
                    spbean.setAppointmentrank(rs.getString("appointmentrank"));
                } else {
                    spbean.setAppointmentrank("Not Available");
                }
                if (rs.getString("present_rank") != null && !rs.getString("present_rank").equals("")) {
                    spbean.setPresentrank(rs.getString("present_rank"));
                } else {
                    spbean.setPresentrank("Not Available");
                }

                if (rs.getString("appointmentrank") != null && !rs.getString("relivingpost").equals("")) {
                    spbean.setRelivingpost(rs.getString("relivingpost"));
                } else {
                    spbean.setRelivingpost("Not Available");
                }

                if (rs.getString("RLV_DATE") != null && !rs.getString("RLV_DATE").equals("")) {
                    spbean.setRelivingdate(rs.getString("RLV_DATE"));
                } else {
                    spbean.setRelivingdate("Not Available");
                }

                if (rs.getString("JOIN_DATE") != null && !rs.getString("JOIN_DATE").equals("")) {
                    spbean.setDateOfJoining(rs.getString("JOIN_DATE"));
                } else {
                    spbean.setDateOfJoining("Not Available");
                }

                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    spbean.setDos(rs.getString("dos"));
                } else {
                    spbean.setDos("Not Available");
                }

                if (rs.getString("NOT_TYPE") != null && !rs.getString("NOT_TYPE").equals("")) {
                    spbean.setTrnsferorpost(rs.getString("NOT_TYPE"));
                } else {
                    spbean.setTrnsferorpost("Not Available");
                }

                if (rs.getString("join_post") != null && !rs.getString("join_post").equals("")) {
                    spbean.setJoiningpost(rs.getString("join_post"));
                } else {
                    spbean.setJoiningpost("Not Available");
                }

                tppoList.add(spbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tppoList;

    }

    @Override
    public ArrayList getOfficeList() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select off_code,ddo_code,off_en from g_office where department_code='14' and is_ddo='Y' order by off_en";
            pst = con.prepareStatement(sql);
            // pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tdate).getTime()));

            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setOffCode(rs.getString("off_code"));
                spbean.setOffName(rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")");
                emplist.add(spbean);
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
    public ArrayList getOfficeWiseEmpList(String offCode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList emplist = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select empdtls.emp_id,getfullname(empdtls.emp_id)fullname,gpf_no,empdtls.dob,doe_gov,dos,empdtls.category,domicile,\n"
                    + "cur_spc,post presentpost,\n"
                    + "emp_join.join_date joindateinpresentpost ,off_en presentplaceofposting,\n"
                    + "array_to_String(array[emprl.initials,emprl.f_name,emprl.m_name,emprl.l_name],' ')fathname from \n"
                    + "(select empmast.* from\n"
                    + "(select * from emp_mast where cur_off_code=? and dep_code not in ('00','08','09','10')\n"
                    + "AND IS_REGULAR<>'N')empmast \n"
                    + ")empdtls\n"
                    + "left outer join g_spc on empdtls.cur_spc=g_spc.spc\n"
                    + "left outer join g_post on g_spc.gpc=g_post.post_code\n"
                    + "left outer join emp_join on empdtls.cur_spc=emp_join.spc AND empdtls.EMP_ID=emp_join.EMP_ID\n"
                    + "left outer join g_office on empdtls.cur_off_code=g_office.off_code\n"
                    + "left outer join (select * from emp_relation where relation='FATHER')emprl on\n"
                    + "empdtls.emp_id=emprl.emp_id";

            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();
                spbean.setEmpid(rs.getString("emp_id"));
                spbean.setEmpname(rs.getString("fullname"));
                spbean.setGpfNo(rs.getString("gpf_no"));

                if (rs.getString("presentplaceofposting") != null && !rs.getString("presentplaceofposting").equals("")) {
                    spbean.setPresntplaceofposting(rs.getString("presentplaceofposting"));
                }

                if (rs.getString("joindateinpresentpost") != null && !rs.getString("joindateinpresentpost").equals("")) {
                    spbean.setDateofpresentjoiningpost(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindateinpresentpost")));
                } else {
                    spbean.setDateofpresentjoiningpost("Not Available");
                }

                if (rs.getString("presentpost") != null && !rs.getString("presentpost").equals("")) {
                    spbean.setPresentrank(rs.getString("presentpost"));
                } else {
                    spbean.setDateOfJoining("NA");
                }

                if (rs.getString("domicile") != null && !rs.getString("domicile").equals("")) {
                    spbean.setDomicile(rs.getString("domicile"));
                }

                if (rs.getString("category") != null && !rs.getString("category").equals("")) {
                    spbean.setCategory(rs.getString("category"));
                }

                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
                    spbean.setRetirmentdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                } else {
                    spbean.setRetirmentdate("Not Available");
                }

                //?????
                if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                    spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                } else {
                    spbean.setDateOfJoining("Not Available");
                }

                if (rs.getString("dob") != null && !rs.getString("dob").equals("")) {
                    spbean.setDateOfBirth(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                } else {
                    spbean.setDateOfBirth("Not Available");
                }
                spbean.setFathername(rs.getString("fathname"));

                /*if (rs.getString("appointmentrank") != null && !rs.getString("appointmentrank").equals("")) {
                 spbean.setAppointmentrank(rs.getString("appointmentrank"));
                 } else {
                 spbean.setAppointmentrank("Not Available");
                 }*/
//                spbean.setCurspc(rs.getString("cur_spc"));
//                if (rs.getString("post") != null && !rs.getString("post").equals("")) {
//                    spbean.setDesignation(rs.getString("post"));
//                } else {
//                    spbean.setDesignation("Not Available");
//                }
//                spbean.setCurspc(rs.getString("cur_spc"));
//                spbean.setCurspc(rs.getString("dpcifany"));
//                spbean.setDesignation(rs.getString("post"));
                emplist.add(spbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    public List getPOList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List poList = new ArrayList();
        SuperannuationProjectionReportBean spbean = null;

        try {
            con = this.dataSource.getConnection();

            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            String sql = "SELECT NOT_TYPE, JOIN_DATE, join_post, ORDNO, ORDDT, \n"
                    + "OFF_CODE, RLV_DATE, RLV_FROM_SPC, RLV_FROM_SPN rlvFromPost FROM (\n"
                    + "SELECT EMP_NOTIFICATION.emp_Id, DEPARTMENT_NAME, EJ.DOE, EJ.SPC, EJ.NOT_TYPE, EJ.JOIN_DATE,\n"
                    + "EJ.SPN join_post, EMP_NOTIFICATION.ORDNO, EMP_NOTIFICATION.ORDDT, EMP_TRANSFER.OFF_CODE, RLV_DATE,\n"
                    + "EMP_RELIEVE.SPC AS RLV_FROM_SPC, GETSPN(EMP_RELIEVE.SPC) AS RLV_FROM_SPN FROM \n"
                    + "(SELECT NOT_ID, DOE, SPC, NOT_TYPE, JOIN_DATE, GETSPN(SPC) SPN, EMP_ID FROM EMP_JOIN WHERE EMP_ID=?\n"
                    + "AND not_type IN ('TRANSFER', 'POSTING')) EJ INNER JOIN EMP_NOTIFICATION ON EJ.NOT_ID = EMP_NOTIFICATION.NOT_ID\n"
                    + "INNER JOIN G_SPC ON EJ.SPC = G_SPC.SPC INNER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE\n"
                    + "LEFT OUTER JOIN EMP_RELIEVE ON EMP_NOTIFICATION.NOT_ID = EMP_RELIEVE.NOT_ID\n"
                    + "LEFT OUTER JOIN EMP_TRANSFER ON EMP_NOTIFICATION.NOT_ID = EMP_TRANSFER.NOT_ID\n"
                    + "WHERE EXTRACT(YEAR FROM JOIN_DATE) >= (EXTRACT(YEAR FROM CURRENT_DATE) - 30) ORDER BY JOIN_DATE) postingdtls";

            pst = con.prepareStatement(sql);

            pst.setString(1, empid);

            rs = pst.executeQuery();

            while (rs.next()) {
                spbean = new SuperannuationProjectionReportBean();

                if (rs.getString("RLV_DATE") != null && !rs.getString("RLV_DATE").equals("")) {
                    spbean.setRelivingdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")));

                } else {
                    spbean.setRelivingdate("Not Available");
                }
                System.out.println("rs.getString(\"rlvFromPost\")" + rs.getString("rlvFromPost"));

                if (rs.getString("rlvFromPost") != null && !rs.getString("rlvFromPost").equals("")) {
                    spbean.setRelivingpost(rs.getString("rlvFromPost"));
                } else {
                    spbean.setRelivingpost("Not Available");
                }

                if (rs.getString("JOIN_DATE") != null && !rs.getString("JOIN_DATE").equals("")) {
                    spbean.setDateOfJoining(CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE")));
                } else {
                    spbean.setDateOfJoining("Not Available");
                }

//                if (rs.getString("dos") != null && !rs.getString("dos").equals("")) {
//                    spbean.setDos(rs.getString("dos"));
//                } else {
//                    spbean.setDos("Not Available");
//                }
                if (rs.getString("NOT_TYPE") != null && !rs.getString("NOT_TYPE").equals("")) {
                    spbean.setTrnsferorpost(rs.getString("NOT_TYPE"));
                } else {
                    spbean.setTrnsferorpost("Not Available");
                }

                if (rs.getString("join_post") != null && !rs.getString("join_post").equals("")) {
                    spbean.setJoiningpost(rs.getString("join_post"));
                } else {
                    spbean.setJoiningpost("Not Available");
                }

                poList.add(spbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return poList;

    }

    @Override
    public List getPolicePostList(String offcode) {
        List postList = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        SuperannuationProjectionReportForm sprf = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select ars.aer_id,fy,postname,sanction_strength,menin_position,vacancy_position from\n"
                    + "(SELECT * FROM AER_REPORT_SUBMIT WHERE OFF_CODE=? and fy = '2021-22')ars\n"
                    + "inner join\n"
                    + "(select * from annual_establish_report  where off_code=?)aer\n"
                    + "on ars.aer_id=aer.aer_id and gpc in ('141277','140472','140422','141084','141048','140473','140484','140966','140485','140486','141338',\n"
                    + "'141337','140529','140070','140846','140835','141279','141269','140945','141296',\n"
                    + "'141400','140531','140861','141276','140983','141410','141051','141407','140845',\n"
                    + "'141185','140969','140912','141098','141270','141077','140530','140897','141074','140834','141408',\n"
                    + "'140124','141001','140844','140833','140892','141286','141010','140950','140860','141050',\n"
                    + "'140843','141285','140831','140589','140125','140126','141055','140891','141008','141049','141007',\n"
                    + "'140951','141017','141266','141265','141118','141009','141018','141230',\n"
                    + "'141231','140842','141203','140878','141273','141284','141283','141213','141282','141268',\n"
                    + "'141078','140895','140832','140859','140139',\n"
                    + "'140871','141275','141316','140952','141262','141311','141399','140801','141395','140141','141195',\n"
                    + "'141364','141171','140796','140600','140599','140140','141172','140986','140985','141000','141292',\n"
                    + "'141281','141196','140953','141263','140903','140872','140873','140890',\n"
                    + "'141317','140956','141264','140957','141398','141076','140893','141406','140799','140292','141274','140293','140291',\n"
                    + "'141191','141033','141339','141324','140990','141278','141005','141221','140988','141402','141401','140987','140989',\n"
                    + "'141319','141325','141310','141026','140913','141318','140902','140294')");
            pst.setString(1, offcode);
            pst.setString(2, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                sprf = new SuperannuationProjectionReportForm();
                sprf.setPostname(rs.getString("postname"));
                sprf.setTotalpost(rs.getInt("sanction_strength"));
                sprf.setMeninposition(rs.getInt("menin_position"));
                sprf.setVacancy(rs.getInt("vacancy_position"));
                postList.add(sprf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return postList;

    }

}

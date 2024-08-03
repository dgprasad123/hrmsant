/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.Superannuation;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.Superannuation.SuperannuationList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author shantanu
 */
public class SuperannuationDAOImpl implements SuperannuationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getSuperannuationList(String distCode,  String supannYear, String supannMonth) {

        
        System.out.println(" Inside DAO IMPL::Superannuation Year: "+supannYear );
        System.out.println("Superannuation Month: "+supannMonth );
        System.out.println("District Code: "+distCode );
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List employeeList = new ArrayList();
        SelectOption so = null;
        String employeeListQry = "";

        try {
            con = dataSource.getConnection();
            employeeListQry = "select t2.*,g_post.post ddo_post1,e.mobile ddo_mobile from" +
" (select getdeptname(department_code)deptname,ddo_hrmsid,ddo_post,SPN," +
" g_office.ddo_code,g_office.dist_code,t1.* from" +
" (select distinct emp_id,mobile,dep_code,is_regular,cur_off_code,cur_spc,gpf_no,acct_type," +
" replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries" +
", replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum" +
", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULLNAME," +
" to_char(DOB, 'dd-MON-yyyy') BIRTHDATE,to_char(DOS, 'dd-MON-yyyy') RETIREMENTDATE,dos dos1," +
" to_char(DOS, 'MONTH') AS DOSMonth from " +
" emp_mast emp " +
" inner join (select emp_code,emp_type from aq_mast where aq_year=? )aqs" +
" on aqs.emp_code=emp.emp_id" +
" where  dep_code NOT IN('00', '08', '09', '10', '13', '14') and emp_type not in ('C') and " +
" acct_type in ('GPF','TPF','PRAN'))t1" +
" left outer join g_office on g_office.off_code=t1.cur_off_code" +
" left outer join g_spc on g_spc.spc=t1.cur_spc where is_regular <> 'N')t2" +
" left outer join g_post on g_post.post_code=t2.ddo_post" +
" left outer join emp_mast e on e.emp_id=t2.ddo_hrmsid" +
" where t2.dist_code=?  and extract(month from dos1)=? and " +
" extract(year from dos1)=?" +
" order by t2.deptname,t2.DOSMonth";
            System.out.println(employeeListQry);
            pstmt = con.prepareStatement(employeeListQry);
            pstmt.setInt(1, Integer.parseInt(supannYear));
            pstmt.setString(2, distCode);
            pstmt.setInt(3, Integer.parseInt(supannMonth));
            pstmt.setInt(4, Integer.parseInt(supannYear));
            rs = pstmt.executeQuery();
            SuperannuationList sal = null;
            while (rs.next()) {

                sal = new SuperannuationList();
                sal.setDdoCode(rs.getString("ddo_code"));
                sal.setDdoHrmsId(rs.getString("ddo_hrmsid"));
                sal.setDdoDesignation(rs.getString("ddo_post1"));
                sal.setDdoMobile(rs.getString("ddo_mobile"));
                sal.setEmpDesignation(rs.getString("spn"));
                sal.setEmpHrmsId(rs.getString("emp_id"));
                sal.setEmpMobile(rs.getString("mobile"));
                sal.setEmpGpfNo(rs.getString("gpf_no"));
                sal.setEmpName(rs.getString("fullname"));
                sal.setEmpDob(rs.getString("birthdate"));
                sal.setEmpDos(rs.getString("retirementdate"));
                sal.setMonthSuperannuation(rs.getString("dosmonth"));
                employeeList.add(sal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeList;

    }
}

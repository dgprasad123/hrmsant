/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Department;
import hrms.model.misreport.DeptWiseReportForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class DeptWiseReportDAOImpl implements DeptWiseReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getemployeeCountDeptWise(List allDeptList, int year, int month) {

        List deptempCntlist = new ArrayList();

        Connection con = null;

        ResultSet rs = null;
        PreparedStatement ps = null;

        DeptWiseReportForm dptRptform = null;

        int curMonth = 0;
        int curYear = 0;

        try {
            con = this.dataSource.getConnection();

            curMonth = month + 1;
            curYear = year;
            //System.out.println("CurMonth&Year:"+curMonth+curYear);

            ps = con.prepareStatement("select t1.department_code,t1.department_name,t1.cntReg,t2.cntCont6yr,t3.cntCont,t4.cntWages,t5.cntWorkcharge,t6.cntLvlVExCadre from\n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntReg from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='Y')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name) t1\n"
                    + "inner join \n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntCont6yr from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='C')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name)t2\n"
                    + "on t1.department_code=t2.department_code\n"
                    + "inner join \n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntCont from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='N')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name)t3\n"
                    + "on t1.department_code=t3.department_code\n"
                    + "inner join \n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntWages from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='G')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name)t4\n"
                    + "on t1.department_code=t4.department_code\n"
                    + "inner join \n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntWorkcharge from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='W')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name)t5\n"
                    + "on t1.department_code=t5.department_code\n"
                    + "inner join \n"
                    + "(select gd.department_code,gd.department_name,count(emp_id)cntLvlVExCadre from\n"
                    + "(select emp_id,department_code,cur_off_code from\n"
                    + "(select emp_id,cur_off_code,dos,dob,if_retired,if_reengaged from\n"
                    + "(select distinct emp_code from aq_mast where aq_year='" + curYear + "' and aq_month<='" + curMonth + "' and emp_code is not null)aqm\n"
                    + "inner join (select * from emp_mast where is_regular='B')emp_mast on emp_mast.emp_id=aqm.emp_code \n"
                    + "where ((extract(month from dos)>='" + curMonth + "' and extract(year from dos)='" + curYear + "') or extract(year from dos)>'" + curYear + "')\n"
                    + "and (if_retired!='Y' or if_retired is null))emp\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code)e1\n"
                    + "right outer join (select * from g_department where if_active='Y') gd on e1.department_code=gd.department_code\n"
                    + "group by gd.department_code,gd.department_name)t6\n"
                    + "on t1.department_code=t6.department_code");

            rs = ps.executeQuery();
            while (rs.next()) {
                dptRptform = new DeptWiseReportForm();
                dptRptform.setDeptName(rs.getString("department_name"));
                dptRptform.setCntRegular(rs.getInt("cntReg"));
                dptRptform.setCntCont6Yr(rs.getInt("cntCont6yr"));
                dptRptform.setCntContractual(rs.getInt("cntCont"));
                dptRptform.setCntWages(rs.getInt("cntWages"));
                dptRptform.setCntWorkcharged(rs.getInt("cntWorkcharge"));
                dptRptform.setCntlvlVExcadre(rs.getInt("cntLvlVExCadre"));
                deptempCntlist.add(dptRptform);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptempCntlist;
    }

}

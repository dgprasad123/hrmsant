/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import hrms.SelectOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public class InsertCasualLeave {
     public static void main(String args[]) {
        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement insertpst = null;
        PreparedStatement insertpst1 = null;
        ResultSet insertrs = null;

        SelectOption so = null;
        ArrayList emplist = new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://172.16.1.16/hrmis", "hrmis2", "cmgi");
            //con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");

            String insertsql = "insert into emp_leave_balance(year,emp_id,tol_id,balance,available,month) values(?,?,?,?,?,?)";
            String insertsql1 = "update  emp_leave_balance set balance=15,available=15,month='JAN'  where emp_id=? and year='2020' and tol_id='CL'";
            insertpst = con.prepareStatement(insertsql);
            insertpst1 = con.prepareStatement(insertsql1);

            String selectsql = "select emp_code from aq_mast where off_code like '%PRD%'  and aq_year='2019' and aq_month='10' and emp_code is not null";
            selectpst = con.prepareStatement(selectsql);
            selectrs = selectpst.executeQuery();
            while (selectrs.next()) {
                so = new SelectOption();
                so.setValue(selectrs.getString("emp_code"));
                emplist.add(so);
            }

            if (emplist != null && emplist.size() > 0) {
                so = null;
                for (int i = 0; i < emplist.size(); i++) {
                    so = (SelectOption) emplist.get(i);
                     
                    if (isDuplicate(con, so.getValue()).equals("N")) {
                        insertpst.setString(1, "2020");
                        insertpst.setString(2, so.getValue());
                        insertpst.setString(3, "CL");
                        insertpst.setInt(4, 15);
                        insertpst.setDouble(5,15);
                        insertpst.setString(6, "JAN");
                        insertpst.executeUpdate();
                    }else{
                        insertpst1.setString(1, so.getValue());
                        insertpst1.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private static String isDuplicate(Connection con, String empid) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDuplicate = "N";
        try {
            String sql = "select count(*) cnt from emp_leave_balance where emp_id=? and tol_id=? and year='2020'";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, "CL");
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isDuplicate = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return isDuplicate;
    }
}

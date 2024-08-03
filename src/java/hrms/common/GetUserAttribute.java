/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Surendra
 */
public class GetUserAttribute {
    public static String getEmpId(Connection con, String spc){
        String empId = null;
        Statement statement = null;
        ResultSet res = null;
        try{
            statement = con.createStatement();
            res = statement.executeQuery("SELECT EMP_ID from EMP_MAST WHERE CUR_SPC = '"+spc+"'");
            if(res.next()){
                empId = res.getString("EMP_ID");
            }
        }catch(Exception sqe){
            sqe.printStackTrace();
        }
        finally{
            DataBaseFunctions.closeSqlObjects(res,statement);
        }
        return empId;
    }
}

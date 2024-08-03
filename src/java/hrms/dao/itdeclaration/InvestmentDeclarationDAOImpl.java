/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.itdeclaration;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.itdeclaration.InvestmentDeclaration;
import hrms.model.itdeclaration.InvestmentDeclarationList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class InvestmentDeclarationDAOImpl implements InvestmentDeclarationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getInvestmentList(String empId) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List datalist = new ArrayList();
        InvestmentDeclarationList idlist = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select fy,submission_date from emp_investment_declaration where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empId);
            rs = pst.executeQuery();
            while(rs.next()){
                idlist = new InvestmentDeclarationList();
                idlist.setFy(rs.getString("fy"));
                idlist.setSubmissionDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submission_date")));
                datalist.add(idlist);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return datalist;
    }

    @Override
    public InvestmentDeclaration showInvestmentDeclaration(String empId,InvestmentDeclaration idbean) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String sql = "";
        
        try {
            con = this.dataSource.getConnection();
            
            Calendar cal = Calendar.getInstance();
            Date curdate = cal.getTime();
            String tempcurdate = new SimpleDateFormat("dd-MM-yyyy").format(curdate);
            
            String[] tempcurdateArr = tempcurdate.split("-");
            int fy1 = Integer.parseInt(tempcurdateArr[2]);
            int fy2 = fy1 + 1;
            
            String fiscalYear = fy1+"-"+fy2;
            
            sql = "select EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,ID_NO,res_address FROM EMP_MAST" +
                   " LEFT OUTER JOIN (SELECT ID_NO,EMP_ID FROM EMP_ID_DOC WHERE ID_DESCRIPTION='PAN')EMP_ID_DOC ON EMP_MAST.EMP_ID=EMP_ID_DOC.EMP_ID WHERE EMP_MAST.EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if(rs.next()){
                idbean.setHidEmpId(empId);
                idbean.setTxtEmployeeName(rs.getString("FULL_NAME"));
                idbean.setTxtAddress(rs.getString("res_address"));
                idbean.setTxtPan(rs.getString("ID_NO"));
                idbean.setTxtFy(fiscalYear);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return idbean;  
    }

    @Override
    public InvestmentDeclaration editInvestmentDeclaration(String empId, String fy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveInvestmentDeclaration(String empId, String offcode, String spc, InvestmentDeclaration idbean) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String sql = "";
        
        String investmentId = "";
        try {
            con = this.dataSource.getConnection();
            
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            
            if(idbean.getHidEmpId() != null && !idbean.getHidEmpId().equals("")){
                investmentId = idbean.getHidEmpId()+"_"+idbean.getTxtFy();
                
                sql = "insert into emp_investment_declaration(investment_id,emp_id,fy,off_code,spc,pan_number,submission_date) values(?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1,investmentId);
                pst.setString(2,idbean.getHidEmpId());
                pst.setString(3,idbean.getTxtFy());
                pst.setString(4,offcode);
                pst.setString(5,spc);
                pst.setString(6,idbean.getTxtPan());
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.executeUpdate();
                
                sql = "update emp_mast set res_address=? where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1,idbean.getTxtAddress());
                pst.setString(2,idbean.getHidEmpId());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submitInvestmentDeclaration(String empId, String fy) {
        
        Connection con = null;

        PreparedStatement pst = null;
        
        try {
            con = this.dataSource.getConnection();
            
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            
            String sql = "update emp_investment_declaration set is_submitted='Y',submission_date=? where emp_id=? and fy=?";
            pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(2, empId);
            pst.setString(3, fy);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}

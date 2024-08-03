package hrms.dao.report.EmployeeNPSDataReport;

import hrms.common.AqFunctionalities;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.report.EmployeeNPSDataReport.EmployeeNPSDataReportBean;
import hrms.model.report.EmployeeNPSDataReport.EmployeeNPSDataReportForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class EmployeeNPSDataReportDAOImpl implements EmployeeNPSDataReportDAO{
    
    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }
    
    @Override
    public EmployeeNPSDataReportForm getEmployeeNPSAquitanceData(String pranno, String vchyear, String vchmonth) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        
        ArrayList empdatalist = new ArrayList();
        
        EmployeeNPSDataReportForm empdataform = new EmployeeNPSDataReportForm();
        
        EmployeeNPSDataReportBean empdatabean = null;
        
        String aqdtlstbl = "";
        try{
            con = this.repodataSource.getConnection();
            
            empdataform.setTxtpranno(pranno);
            empdataform.setSltYear(vchyear);
            empdataform.setSltMonth(vchmonth);
            
            String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post,dob,dos from emp_mast" +
                        " left outer join g_spc on emp_mast.cur_spc=g_spc.spc" +
                        " inner join g_post on g_spc.gpc=g_post.post_code where gpf_no=? and acct_type='PRAN'";
            pst = con.prepareStatement(sql);
            pst.setString(1, pranno);            
            rs = pst.executeQuery();
            if(rs.next()){
                empdataform.setEmpid(rs.getString("emp_id"));
                empdataform.setEmpname(rs.getString("EMPNAME"));
                empdataform.setDesignation(rs.getString("post"));
                empdataform.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                empdataform.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
            }
            
            DataBaseFunctions.closeSqlObjects(rs, pst);
            
            sql = "select bill_desc,bill_mast.bill_no,bill_mast.aq_year,bill_mast.aq_month,aqsl_no from bill_mast" +
                  " inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no where gpf_acc_no=? and bill_status_id=7";
            if(vchyear != null && !vchyear.equals("")){
                sql = sql + " and extract(year from vch_date)=?";
            }
            if(vchmonth != null && !vchmonth.equals("")){
                sql = sql + " and extract(month from vch_date)=?";
            }
            sql = sql + " order by aq_year desc,aq_month desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, pranno);
            if(vchyear != null && !vchyear.equals("")){
                pst.setInt(2, Integer.parseInt(vchyear));
            }
            if(vchmonth != null && !vchmonth.equals("")){
                pst.setInt(3, Integer.parseInt(vchmonth));
            }
            rs = pst.executeQuery();
            while(rs.next()){
                empdatabean = new EmployeeNPSDataReportBean();
                
                int aqyear = rs.getInt("aq_year");
                int aqmonth = rs.getInt("aq_month");
                
                aqdtlstbl = AqFunctionalities.getAQBillDtlsTable(aqmonth,aqyear);
                
                empdatabean.setBillno(rs.getString("bill_no"));
                empdatabean.setBilldesc(rs.getString("bill_desc"));
                empdatabean.setBillyear(aqyear+"");
                empdatabean.setBillmonth(CommonFunctions.getMonthAsString(aqmonth));
                
                pst1 = con.prepareStatement("select ad_amt,tot_rec_amt,ref_desc from "+aqdtlstbl+" where aqsl_no=? and ad_code='CPF'");
                pst1.setString(1, rs.getString("aqsl_no"));
                rs1 = pst1.executeQuery();
                if(rs1.next()){
                    empdatabean.setEmpcontribution(rs1.getString("ad_amt"));
                    empdatabean.setGovernmentcontribution(rs1.getString("tot_rec_amt"));
                }
                
                empdatalist.add(empdatabean);
            }
            empdataform.setRegularbillEmpdatalist(empdatalist);
            
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            
            empdatalist = new ArrayList();
            
            pst1 = con.prepareStatement("select cpf_head,cpf_head_gc from arr_mast where aqsl_no=?");
            
            sql = "select bill_desc,bill_mast.bill_no,bill_mast.aq_year,bill_mast.aq_month,aqsl_no from bill_mast" +
                  " inner join arr_mast on bill_mast.bill_no=arr_mast.bill_no where gpf_acc_no=? and bill_status_id=7";
            if(vchyear != null && !vchyear.equals("")){
                sql = sql + " and extract(year from vch_date)=?";
            }
            if(vchmonth != null && !vchmonth.equals("")){
                sql = sql + " and extract(month from vch_date)=?";
            }
            sql = sql + " order by aq_year desc,aq_month desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, pranno);
            if(vchyear != null && !vchyear.equals("")){
                pst.setInt(2, Integer.parseInt(vchyear));
            }
            if(vchmonth != null && !vchmonth.equals("")){
                pst.setInt(3, Integer.parseInt(vchmonth));
            }
            rs = pst.executeQuery();
            while(rs.next()){
                empdatabean = new EmployeeNPSDataReportBean();
                
                int aqyear = rs.getInt("aq_year");
                int aqmonth = rs.getInt("aq_month");
                
                empdatabean.setBillno(rs.getString("bill_no"));
                empdatabean.setBilldesc(rs.getString("bill_desc"));
                empdatabean.setBillyear(aqyear+"");
                empdatabean.setBillmonth(CommonFunctions.getMonthAsString(aqmonth));
                
                pst1.setString(1, rs.getString("aqsl_no"));
                rs1 = pst1.executeQuery();
                if(rs1.next()){
                    empdatabean.setEmpcontribution(rs1.getString("cpf_head"));
                    empdatabean.setGovernmentcontribution(rs1.getString("cpf_head_gc"));
                }
                
                empdatalist.add(empdatabean);
            }
            empdataform.setArrearbillEmpdatalist(empdatalist);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empdataform;
    }    
}

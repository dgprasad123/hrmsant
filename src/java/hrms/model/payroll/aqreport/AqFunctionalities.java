package hrms.model.payroll.aqreport;

import hrms.common.DataBaseFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AqFunctionalities 
{

    public static BillAmtDetails BillAmt(Connection con,String billNo, String aqDTLS, int aqYear, int aqMonth){
        Statement st  = null;
        ResultSet rs = null;
        double grossPay = 0;
        double deduction = 0;
        double pvtdeduction=0;
        BillAmtDetails billAmtDetails = new BillAmtDetails();
        try{
            st = con.createStatement();
            String query1="Select sum(CUR_BASIC) AD_AMT from AQ_MAST WHERE BILL_NO='"+billNo+"'";
            rs = st.executeQuery(query1);
            if(rs.next()){
                grossPay = rs.getDouble("AD_AMT");
            }
            st = con.createStatement();

            String query2 = "select sum(a.AD_AMT) AD_AMT from "+aqDTLS+" a, AQ_MAST b where a.aqsl_no = b.aqsl_no and b.aq_month = '"+aqMonth+"' and b.aq_year = '"+aqYear+"' and a.ad_type = 'A' "
                    + "and b.BILL_NO='"+billNo+"' and ad_amt > 0";
            rs = st.executeQuery(query2);
            if(rs.next()){
                grossPay = grossPay+rs.getDouble("AD_AMT");
            }            
            st = con.createStatement();
            
            String query3 = "select sum(a.AD_AMT) AD_AMT from "+aqDTLS+" a, AQ_MAST b where a.aqsl_no = b.aqsl_no and b.aq_month = '"+aqMonth+"' and b.aq_year = '"+aqYear+"' and a.ad_type = 'D' "
                    + "and b.BILL_NO = '"+billNo+"' and ad_amt > 0 and SCHEDULE != 'PVTL' and SCHEDULE != 'PVTD'";
            rs = st.executeQuery(query3);
            if(rs.next()){
                deduction = rs.getDouble("AD_AMT");
            }
            st = con.createStatement();
            
            String query4 = "select sum(a.AD_AMT) AD_AMT from "+aqDTLS+" a, AQ_MAST b where a.aqsl_no = b.aqsl_no and b.aq_month = '"+aqMonth+"' and b.aq_year = '"+aqYear+"' and a.ad_type = 'D' "
                    + "and b.BILL_NO = '"+billNo+"' and (SCHEDULE = 'PVTL' OR SCHEDULE = 'PVTD')";
            rs = st.executeQuery(query4);
            if(rs.next()){
                pvtdeduction = rs.getDouble("AD_AMT");
            }
            billAmtDetails.setBillGross(grossPay);
            billAmtDetails.setBillDeduction(deduction);//Govt Deductions
            billAmtDetails.setBillPvtDeduction(pvtdeduction);//Private Loan and Deductions
            billAmtDetails.setBillNet(grossPay - deduction);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            DataBaseFunctions.closeSqlObjects(rs,st);
        }
        return billAmtDetails;
    }
    
    
    
}

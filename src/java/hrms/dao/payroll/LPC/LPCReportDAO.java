/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.LPC;

import com.itextpdf.text.Document;
import hrms.model.common.FileAttribute;
import hrms.model.login.Users;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface LPCReportDAO {
    public void downloadLPCReport(Document document,Users lub,int month,int year,String SelectedMonthYear, String txtRlvDt,String sltRlvTime,PaySlipListBean pbean,String fname,String rlvId,PaySlipListBean pblpc);
    public String generateLPC(String empid);
    public FileAttribute downlaodLPCFile(String rlvId, String filepath,Users lub);
    public Users getLpcEmpDetail(String empId);
    
}

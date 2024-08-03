/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.paybill;

import hrms.model.employee.PayComponent;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Manas
 */
public interface PayBillDAO {

    public PayComponent getEmployeePayComponent(SectionDtlSPCWiseEmp sdswe, Date startDate, Date endDate, int daysInMonth);

    public HashMap getPayWorkDays(SectionDtlSPCWiseEmp sdswe, Date monstartDate, Date monendDate, int v_aqday);

    public HashMap getPayWorkDays(AqmastModel aqMastModel, Date monstartDate, Date monendDate, int v_aqday);

    public int getPayDaysContractual(SectionDtlSPCWiseEmp sdswe, int month, int year, int daysInMonth);

    public HashMap getPayWorkDays(SectionDtlSPCWiseEmp sdswe, int month, int year);

    public AqDtlsModel[] getAqDtlsModelFromPT(AqmastModel aqmast, int monbasic, int curbasic);

    public AqDtlsModel[] getAqDtlsModelFromCPF(PayComponent payComponent, AqmastModel aqmast);
    
    public AqDtlsModel[] getAqDtlsModelFromGPF_TPF(PayComponent payComponent, AqmastModel aqmast);
    
    public AqDtlsModel[] getAqDtlsModelFromEPF(PayComponent payComponent, AqmastModel aqmast) ;
    
}

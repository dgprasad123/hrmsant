package hrms.dao.payroll.arrear;

import hrms.model.payroll.schedule.BankAcountScheduleBean;
import java.util.List;

public interface BankAccountScheduleArrearDAO {
    
    public BankAcountScheduleBean getBankAcountScheduleHeaderDetails(String billno);

    public List getBankAcountScheduleEmpList(String billno, int year, int month,String typeOfBill);

    public List getBankNameScheduleList(String billno, int year, int month);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billmast;

import hrms.model.payroll.billmast.BillMastModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Durga
 */
public interface BillMastDAO {

    public int saveBillMast(BillMastModel model);

    public BillMastModel getBillMastDetails(int billid);

    public List getBillList(int year, int month, String offcode, String billType, String spc);

    public int deleteBill(int billid);

    public int getBillStatus(int billid);

    public void updateBillStatus(int billid, int billStatusId);

    public void markBillAsPrepared(int billid);

    public void updateBillTotaling(int billid);

    public ArrayList getDeptWisePayBillStatus(int month, int year);

    public ArrayList DistWisePayBillReport(int month, int year, String typeOfBill);

    public ArrayList DistWiseOfficePayBill(int month, int year, String dcode, String typeOfBill);

    public ArrayList getOfficeWiseBillStatus(int month, int year, String offcode);

    public ArrayList DeptWiseOfficePayBill(int month, int year, String dcode);

    public ArrayList getBillStatusCount();

    public ArrayList getTreasuryBillStatusCount();
    
    public ArrayList getBillStatusDetail(String type);
    
    public ArrayList BillTreasuryDashboardDetail(String type);
    
    public ArrayList DeptWiseVoucherList(int month, int year, String billType);
        

}

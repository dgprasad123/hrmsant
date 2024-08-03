/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.PayInServiceRecord;

import hrms.model.PayInServiceRecord.PayInServiceRecordForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Madhusmita
 */
public interface PayInServiceRecordDAO {

    public List getPayInServiceRecordList(String empid);

    public void savePISRData(String empid, PayInServiceRecordForm pisr);

    public PayInServiceRecordForm getPayInServiceRecordData(String empid, String srpid);

    public void modifyPISRData(PayInServiceRecordForm pisr, String srpid);

    public void deletePISRData(String empid, String srpid);

    public List getpayInServiceData(String empid);

    
}

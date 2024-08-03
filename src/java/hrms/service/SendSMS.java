/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.model.empinfo.SMSGrievance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manisha
 */
@Service
public class SendSMS {

    @Autowired
    PARAdminDAO parAdminDAO;

    @Async
    public CompletableFuture sendPARMessage(ArrayList<SMSGrievance> smss) throws Exception {
        smss.forEach(sms -> {
            SMSServices smsServices = new SMSServices();
            String dlvMsg = smsServices.sendSingleSMS(sms.getWho(), sms.getWhat(), "1407165347864019622");//Insert Template ID
            smsServices.updateSMSLog(sms.getMsgId(), dlvMsg);            
        });

        return null;
    }

}

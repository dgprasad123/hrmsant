/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.thread.misreport;

import hrms.common.LogMessage;
import hrms.service.GenerateFDEmpDataService;
import static hrms.thread.equater.RentRecoveryThread.threadStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas
 */
@Service
public class FinanceDepartmentDataThread implements Runnable {

    @Autowired
    GenerateFDEmpDataService generateFDEmpDataService;

    public void run() {
        try {
            generateFDEmpDataService.generateFDEmpData();
        } catch (Exception e) {
            threadStarted = 0;
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.esignBill;

import emh.emBridgeLib.emBridge;

/**
 *
 * @author SURAJ
 */
public final class EmBridgeSingleTon {

    private static emBridge bridge;

    private EmBridgeSingleTon() {
    }

    public static emBridge getEmBridge() {
        try {
            if(bridge == null){
                bridge = new emBridge("C:\\CMGI_HRMS\\emBridgeLicense\\CMGI HRMS.lic", "C:\\CMGI_HRMS\\rae");
                //bridge = new emBridge("/data/emBridgeLicense/CMGI HRMS.lic", "/data/rae");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return bridge;
    }
}

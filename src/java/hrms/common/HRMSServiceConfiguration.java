/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

/**
 *
 * @author Manas
 */
public class HRMSServiceConfiguration {
    private static final int noofSBrequestallowed = 5000;
    private static int serviceBookRequestByOffice = 0;
    
    public static int noofServiceBookRequestedByOffice(){
        return HRMSServiceConfiguration.serviceBookRequestByOffice;
    }
    public static void serviceBookRequested(){
        HRMSServiceConfiguration.serviceBookRequestByOffice = HRMSServiceConfiguration.serviceBookRequestByOffice + 1;
    }
    
    public static void serviceBookRequestEnds(){
        HRMSServiceConfiguration.serviceBookRequestByOffice = HRMSServiceConfiguration.serviceBookRequestByOffice - 1;
    }
    
    public static boolean isResourceAvailable(){
        boolean isResourceAvailable = true;
        if(HRMSServiceConfiguration.serviceBookRequestByOffice > HRMSServiceConfiguration.noofSBrequestallowed){
            isResourceAvailable = false;
        }
        return isResourceAvailable;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.qms;

import hrms.ws.client.qms.model.GetOcupationDetails;
import hrms.ws.client.qms.model.GetOcupationDetailsResponse;
import hrms.ws.client.qms.model.GetOcupationDetailsResponse.GetOcupationDetailsResult;
import hrms.ws.client.qms.model.HrmsOcupationVacation;
import hrms.ws.client.qms.model.HrmsOcupationVacationSoap;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

/**
 *
 * @author lenovo
 */
public class QmsVacationClient extends WebServiceGatewaySupport {

    public GetOcupationDetailsResponse getOcupationDetails(String action,String startDate, String endDate){
        GetOcupationDetails getOcupationDetails = new GetOcupationDetails();
        getOcupationDetails.setStartDate(startDate);
        getOcupationDetails.setEndDate(endDate);
        getOcupationDetails.setAction(action);
        GetOcupationDetailsResponse getOcupationDetailsResponse = (GetOcupationDetailsResponse)getWebServiceTemplate().marshalSendAndReceive(getOcupationDetails, new SoapActionCallback("http://equarters.nic.in/HrmsOcupationVacation.asmx"));
        return getOcupationDetailsResponse;
    }
    public static void main(String args[]) {
        /*QmsVacationClient qvc = new QmsVacationClient();
        GetOcupationDetailsResponse getOcupationDetailsResponse = qvc.getOcupationDetails("GetVacation", "2021-10-01", "2021-11-30");
        System.out.println(getOcupationDetailsResponse.getGetOcupationDetailsResult().getAny());*/
        HrmsOcupationVacation hrmsOcupationVacation = new HrmsOcupationVacation() ;
        
        HrmsOcupationVacationSoap service = hrmsOcupationVacation.getHrmsOcupationVacationSoap();        
        GetOcupationDetailsResult getOcupationDetailsResult = service.getOcupationDetails("GetVacation", "2021-10-01", "2021-11-30");
        System.out.println(getOcupationDetailsResult.getAny());
    }
}

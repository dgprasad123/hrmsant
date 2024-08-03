/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.eDespatch;

import java.util.List;

/**
 *
 * @author Manas
 */
public class GetAddresseeInformationResponse {

    public List<AddresseeInformationResponse> addresseeInformationResponse;

    public List<AddresseeInformationResponse> getAddresseeInformationResponse() {
        return addresseeInformationResponse;
    }

    public void setAddresseeInformationResponse(List<AddresseeInformationResponse> addresseeInformationResponse) {
        this.addresseeInformationResponse = addresseeInformationResponse;
    }

}

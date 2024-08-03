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
public class AddresseeInformationResponse {

    public int status;
    public String response;
    public List<AddresseeList> addressee_list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<AddresseeList> getAddressee_list() {
        return addressee_list;
    }

    public void setAddressee_list(List<AddresseeList> addressee_list) {
        this.addressee_list = addressee_list;
    }

}

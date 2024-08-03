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
public class GroupInfoResponse {

    public int status;
    public String response;
    public List<AddressGroupList> addressGroup_list;

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

    public List<AddressGroupList> getAddressGroup_list() {
        return addressGroup_list;
    }

    public void setAddressGroup_list(List<AddressGroupList> addressGroup_list) {
        this.addressGroup_list = addressGroup_list;
    }
    
    
}

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
public class SigningAuthorityResponse {

    public int status;
    public String response;
    public List<SigningAuthority> data;

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

    public List<SigningAuthority> getData() {
        return data;
    }

    public void setData(List<SigningAuthority> data) {
        this.data = data;
    }

}

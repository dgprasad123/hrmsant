/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.loanworkflow;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author lenovo
 */
public class IFMSAuthObject {

    private boolean status;
    private String errorCode;
    private String errorMessage;
    private IFMSAuthObjectData data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public IFMSAuthObjectData getData() {
        return data;
    }

    public void setData(IFMSAuthObjectData data) {
        this.data = data;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

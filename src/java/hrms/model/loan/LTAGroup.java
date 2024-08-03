/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.loan;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Manoj PC
 */
public class LTAGroup {
    private List dataList = new ArrayList();
    private String amountType = null;
    private String loanName = null;
    private String sanctionNo = null;
    private String sanctionDt = null;
    private String releaseAmount = null;
    private String lastOfficeName="";
    private String lastDDo="";

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionDt() {
        return sanctionDt;
    }

    public void setSanctionDt(String sanctionDt) {
        this.sanctionDt = sanctionDt;
    }

    public String getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(String releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public String getLastOfficeName() {
        return lastOfficeName;
    }

    public void setLastOfficeName(String lastOfficeName) {
        this.lastOfficeName = lastOfficeName;
    }

    public String getLastDDo() {
        return lastDDo;
    }

    public void setLastDDo(String lastDDo) {
        this.lastDDo = lastDDo;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.qtrallotment;

/**
 *
 * @author Manas
 */
public class QuarterPool {

    private String btid;
    private String isdefault;
    private String qid;
    private String poolName;
    private String demandNoString;

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDemandNoString() {
        return demandNoString;
    }

    public void setDemandNoString(String demandNoString) {
        this.demandNoString = demandNoString;
    }
    
    
}

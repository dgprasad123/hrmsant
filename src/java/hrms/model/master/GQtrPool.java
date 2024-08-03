/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

/**
 *
 * @author Surendra
 */
public class GQtrPool {
    
    private String qid=null;
    private String poolname=null;
    private String btid=null;
    private String demandAsString=null;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getPoolname() {
        return poolname;
    }

    public void setPoolname(String poolname) {
        this.poolname = poolname;
    }

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public String getDemandAsString() {
        return demandAsString;
    }

    public void setDemandAsString(String demandAsString) {
        this.demandAsString = demandAsString;
    }
    
    
}

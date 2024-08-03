/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.report.annualestablishmentreport;

import java.util.List;

/**
 *
 * @author Surendra
 */
public class AerListBox {
    
    private String groupADataSystem;
    private String groupBDataSystem;
    private String groupCDataSystem;
    private String groupDDataSystem;
    private String grantInAidSystem;
    private List<AnnualEstablishment> li;

    public String getGroupADataSystem() {
        return groupADataSystem;
    }

    public void setGroupADataSystem(String groupADataSystem) {
        this.groupADataSystem = groupADataSystem;
    }

    public String getGroupBDataSystem() {
        return groupBDataSystem;
    }

    public void setGroupBDataSystem(String groupBDataSystem) {
        this.groupBDataSystem = groupBDataSystem;
    }

    public String getGroupCDataSystem() {
        return groupCDataSystem;
    }

    public void setGroupCDataSystem(String groupCDataSystem) {
        this.groupCDataSystem = groupCDataSystem;
    }

    public String getGroupDDataSystem() {
        return groupDDataSystem;
    }

    public void setGroupDDataSystem(String groupDDataSystem) {
        this.groupDDataSystem = groupDDataSystem;
    }

    public String getGrantInAidSystem() {
        return grantInAidSystem;
    }

    public void setGrantInAidSystem(String grantInAidSystem) {
        this.grantInAidSystem = grantInAidSystem;
    }

    public List<AnnualEstablishment> getLi() {
        return li;
    }

    public void setLi(List<AnnualEstablishment> li) {
        this.li = li;
    }

    
}

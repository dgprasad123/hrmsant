/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.reemployment;

/**
 *
 * @author Surendra
 */
public class RedesignationContractualBean {

    private int hrmsid;

    private String ename;
    private String contpost;
    private String newBasic;

    private String empid;
    private String newContpost;
    private String depcode;

    public int getHrmsid() {
        return hrmsid;
    }

    public void setHrmsid(int hrmsid) {
        this.hrmsid = hrmsid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getContpost() {
        return contpost;
    }

    public void setContpost(String contpost) {
        this.contpost = contpost;
    }

    public String getNewBasic() {
        return newBasic;
    }

    public void setNewBasic(String newBasic) {
        this.newBasic = newBasic;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getNewContpost() {
        return newContpost;
    }

    public void setNewContpost(String newContpost) {
        this.newContpost = newContpost;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

}

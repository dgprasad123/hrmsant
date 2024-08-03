/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.misreport;

/**
 *
 * @author Manas
 */
public class EmpCategoryBean {

    private String name = null;
    private String post = null;
    private String gpfno = null;
    private String empid = null;
    private String slno = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost() {
        return post;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getSlno() {
        return slno;
    }
}

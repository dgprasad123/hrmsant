package hrms.model.leaveauthority;

public class LeaveAuthorityBean {

    private String radempid = null;
    private String empId;
    private String name = null;
    private String designation = null;
    private String spc = null;
    private String post_code = null;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    
    public String getRadempid() {
        return radempid;
    }

    public void setRadempid(String radempid) {
        this.radempid = radempid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

}

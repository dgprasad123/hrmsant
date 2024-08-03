/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.EmployeeRegistration;

/**
 *
 * @author Surendra
 */
public class EmployeeRegistrationBean {
     private int sl_no;

   
    private String name;
    private String password;
    private String address;
    private String age;
    private String qualification;
     private int userRegistrationId;
     private String percentage   ;
    private String yourof_pass;
    
    private int state_id;
    private String state_name;

    public int getUserRegistrationId() {
        return userRegistrationId;
    }

    public void setUserRegistrationId(int userRegistrationId) {
        this.userRegistrationId = userRegistrationId;
    }

  


   

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getYourof_pass() {
        return yourof_pass;
    }

    public void setYourof_pass(String yourof_pass) {
        this.yourof_pass = yourof_pass;
    }
    
 public int getSl_no() {
        return sl_no;
    }

    public void setSl_no(int sl_no) {
        this.sl_no = sl_no;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

   
   
}

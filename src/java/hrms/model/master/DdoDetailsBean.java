/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.master;

import java.util.List;

/**
 *
 * @author Surendra
 */
public class DdoDetailsBean {

   private int ddoId;
   private String ddoName;
   private String officename;
   private String ddocode;
   private String ddohrmsid;
   private String ddoPhone;
   private String ddoPost;

    public String getDdoPost() {
        return ddoPost;
    }

    public void setDdoPost(String ddoPost) {
        this.ddoPost = ddoPost;
    }

    public String getDdoPhone() {
        return ddoPhone;
    }

    public void setDdoPhone(String ddoPhone) {
        this.ddoPhone = ddoPhone;
    }

  

    public String getDdoLName() {
        return ddoLName;
    }

    public void setDdoLName(String ddoLName) {
        this.ddoLName = ddoLName;
    }
   private String ddoLName;

    public String getDdohrmsid() {
        return ddohrmsid;
    }

    public void setDdohrmsid(String ddohrmsid) {
        this.ddohrmsid = ddohrmsid;
    }

    public String getDdocode() {
        return ddocode;
    }

    public void setDdocode(String ddocode) {
        this.ddocode = ddocode;
    }

    public String getOfficename() {
        return officename;
    }

    public void setOfficename(String officename) {
        this.officename = officename;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }
    
    public int getDdoId() {
        return ddoId;
    }

    public void setDdoId(int ddoId) {
        this.ddoId = ddoId;
    }
    
}

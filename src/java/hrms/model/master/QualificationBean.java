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
public class QualificationBean {
      private String qualificationName = null;
          private int qualificationserialNumber ;
              private String qualification = null;
               private int qualification_sl_no ;

    public int getQualification_sl_no() {
        return qualification_sl_no;
    }

    public void setQualification_sl_no(int qualification_sl_no) {
        this.qualification_sl_no = qualification_sl_no;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public int getQualificationserialNumber() {
        return qualificationserialNumber;
    }

    public void setQualificationserialNumber(int qualificationserialNumber) {
        this.qualificationserialNumber = qualificationserialNumber;
    }


    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

   
    
}

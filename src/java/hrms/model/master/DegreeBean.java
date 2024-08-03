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
public class DegreeBean {
     private int degreesl;
    private String degreename = null;
      private String degreeType = null;
       private int degreeserialNumber ;

    public int getDegreeserialNumber() {
        return degreeserialNumber;
    }

    public void setDegreeserialNumber(int degreeserialNumber) {
        this.degreeserialNumber = degreeserialNumber;
    }
      

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public int getDegreesl() {
        return degreesl;
    }

    public void setDegreesl(int degreesl) {
        this.degreesl = degreesl;
    }
      
   

    public String getDegreename() {
        return degreename;
    }

    public void setDegreename(String degreename) {
        this.degreename = degreename;
    }
    
}

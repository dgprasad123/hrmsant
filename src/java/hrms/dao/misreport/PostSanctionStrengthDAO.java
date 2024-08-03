/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public interface PostSanctionStrengthDAO {

    public void generatePostSanctionStrengh();

    public List viewPostSanctionStrengh();

    public void generateFDEmpData(String month, String year);
    
    public void generateCenusData();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Treasury;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface TreasuryDAO {

    public ArrayList getTreasuryList();

    public ArrayList getTreasuryListAG(String agtrcode);

    public ArrayList getAGTreasuryList();

    public String getAqTreasuryCode(String trCode);

    public Treasury getTreasuryDetails(String trCode);

    public ArrayList getParentTreasuryList();

    public ArrayList getSubTreasuryList(String parentTrCode);

    public List getTreasuryListByOffCode(String offcode);
    
    public String getTreasuryName(String trcode);
    
}

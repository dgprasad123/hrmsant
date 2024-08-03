/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.eDespatch;

import java.util.List;

/**
 *
 * @author Manas
 */
public interface eDespatchDAO {

    public String getAuthToken();

    public void getofficeinfo(String officeCode);

    public String genLetterNo(String officeCode);

    public List getGroupList(String officeCode);

    public List getAddresseeList(String officeCode, String addresseeGroupCode);

    public List getSigningAuthorityList(String officeCode);

    public List getSectionList(String officeCode);
}

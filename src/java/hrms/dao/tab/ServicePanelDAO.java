/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.tab;

import java.util.ArrayList;

/**
 *
 * @author Surendra
 */
public interface ServicePanelDAO {

    public ArrayList getRollWiseGrpInfo(String rolid, String spc, String ifEmpClicked, boolean isddo, String employeeTypeLink);
    
    public hrms.model.login.Users getSelectedEmployeeInfo(String employeeId);
}

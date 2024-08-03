/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.headquarterleaving;

import hrms.model.headquarterleaving.HeadQuarterLeaving;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface HeadQuarterLeavingDAO {

    public List HaedQuarterLeavingList(String empid);

    public void savePermission(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth);

    public HeadQuarterLeaving getEmpPermissionData(HeadQuarterLeaving leavingForm, String permissionId, String empid) throws SQLException;

    public void updatePermission(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth);

    public List DetentionList(String empid);

    public void saveDetention(HeadQuarterLeaving leavingForm, int notid);

    public HeadQuarterLeaving getEmpDetentionData(HeadQuarterLeaving leavingForm, String detentionId, String empid) throws SQLException;

    public void updateDetention(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth);
}

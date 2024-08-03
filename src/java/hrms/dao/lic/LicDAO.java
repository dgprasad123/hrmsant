/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.lic;

import java.util.List;
import hrms.model.lic.Lic;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public interface LicDAO {

    // For Page Load and Cancel 
    public ArrayList getLicList(String empId);

    // For Edit
    public Lic editLicData(String empId, BigDecimal elId);

    // For Save
    public void saveLicData(Lic llicBean);

    // For Delete
    public boolean deleteLicData(String elId);

    public void updateEmployeeLicData(Lic llicBean);

    public int deleteLicData(String licId, String status);

    public AqDtlsModel[] getAqDtlsModelFromLICList(List licList, AqmastModel aqmast);
}

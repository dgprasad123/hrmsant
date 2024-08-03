/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.model.misreport.OfficeWiseEmpStatusBean;
import java.util.List;

/**
 *
 * @author Manas
 */
public interface CategoryEmployeeReportDAO {

    public OfficeWiseEmpStatusBean getOfficeWiseEmployeeCategoryData(String offCode);

    public List getEmpCatStatusList(String offCode, String category, String gender);
}

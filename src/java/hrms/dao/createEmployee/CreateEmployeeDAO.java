/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.createEmployee;

import hrms.model.createEmployee.CreateEmployee;
import java.util.ArrayList;

/**
 *
 * @author lenovo
 */
public interface CreateEmployeeDAO {

    public ArrayList getGpfType();

    public ArrayList empTitleType();

    public String saveEmployeeData(CreateEmployee ceBean);
    
    public String generateAccountNo(String acctType);

}

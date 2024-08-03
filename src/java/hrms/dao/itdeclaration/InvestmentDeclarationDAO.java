/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.itdeclaration;

import hrms.model.itdeclaration.InvestmentDeclaration;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface InvestmentDeclarationDAO {
    
    public List getInvestmentList(String empId);
    
    public InvestmentDeclaration showInvestmentDeclaration(String empId, InvestmentDeclaration idbean);
    
    public void saveInvestmentDeclaration(String empId,String offcode,String spc, InvestmentDeclaration idbean);
    
    public InvestmentDeclaration editInvestmentDeclaration(String empId, String fy);
    
    public void submitInvestmentDeclaration(String empId, String fy);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.incrementProposal;

import hrms.model.incrementProposal.EmpOrderBean;
import hrms.model.incrementProposal.IncrementProposal;
import hrms.model.incrementProposal.ProposalAttr;
import hrms.model.login.Users;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface IncrementProposalDAO {

    public void saveProposal(IncrementProposal incr);

    public List getProposedEmployeeList(String offcode, int year, int month);

    public List proposalList(String offCode, int page, int rows);

    public List getBenificiaryList(String offcode, int year);

    public void addProposedEmployee(String emp[], int year, int month, int propmastId);

    public int saveProposalList(String offCode, String loggedinEmpId, String spc, int month, int year);

    public int saveProposalDetailList(String emp[], String pcArr[], String mlArr[], String mcArr[], String wefArr[], int maxCode_MASTER, int year, int month);

    public List getFinalProposedList(int proposalId);

    public void submitProposal(int propmastId, String loggedempid, String loggedspc, String authSpc, String authSpc1);

    public int getProposalMasterId(int taskId);

    public void deleteSelectedProposalFromList(int proposaldetailId, String empId);

    public void deleteProposalDetail(int propmastId);

    public void authorityAction(int propmastId, int statusid, String note, String authspc);

    public void updateOrderInfo(int propmastId, String ordno, String orderDate);

    public String getOfficeName(String offcode);

    public ProposalAttr getordnoSpcEtc(int proposalMastId);

    public List getEmployeeWisePostList(String offcode);

    public List getEmployeeList(String offCode, int year, int month);

    public void deleteIncrementProposal(int proposalId);

    public List getLastYearPay(String empId);

    public List getCells(int level);

    public int getFianlIncrementPay(int level, int cell, int payCommission, String presentPay, String empId);

    public List getIncrementStatusList();

    public void SaveEmployeeOrder(EmpOrderBean eBean, String empId, String offCode, String deptCode);

    public int getTaskStatusId(int taskId);

    public Users getIncrEmpDetail(String empId);

    public List getIncrementPostListWithName(String offcode);

    public List getFinalFutureProposedList(String offcode, int year, int month);

    public List getMonthlyRetirementList(String offcode, int year, int month);
}

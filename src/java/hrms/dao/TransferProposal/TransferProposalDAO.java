/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.TransferProposal;


import hrms.model.TransferProposal.TransferProposalBean;
import hrms.model.eDespatch.eDespatchBean;
import java.util.ArrayList;

/**
 *
 * @author Manoj PC
 */
public interface TransferProposalDAO {

    public ArrayList getEmpList(String cadreCode, int proposalId);
    
    public ArrayList getCadreGradeWiseEmpList(String cadreCode,String cadreGrade, int proposalId);
    
    public void saveOtherInputs(TransferProposalBean tpBean);

    public int addTransferProposal(String empId, String cadreCode, String spc, String ownerId, String proposalId, String hasAdditional,String proposalType);        
    
    public int updateNewCadrePromotion(String cadreCode, String ownerId, int proposalId, String cadreGrade);

    public String getTransferEmployee(int proposalId);
    
    public String deletePostingInfo(int detailPostingId);

    public ArrayList getTransferEmployeeList(int proposalId);
    
    public ArrayList getTransferEmployeeList(int proposalId, int letterno, String fileno);

    public ArrayList getTransferProposalList(String empId, String proposalType);
    
    public TransferProposalBean getTransferProposalListDetail(int proposalId);

    public void deleteEmployee(int transferProposalId);

    public void updateNewSpc(String oldSpc, String newspc, int transferProposalId, String transferType, String empId, String cadreCode);        

    public void deleteTransferProposal(int proposalId);

    public void saveApproval(int proposalId, String orderNumber, String orderDate);
    
    public void sendLetterInEdespatch(eDespatchBean tpBean);
    
}

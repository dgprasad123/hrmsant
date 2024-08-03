/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.miscellaneous;

import hrms.model.miscellaneous.PostProposal;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface PostProposalDAO {
    
    public int addProposal(PostProposal pp);
    
    public int addProposalDetails(PostProposal ppd);
    
    public List getPostProposalList(String submittedBy);
    
    public PostProposal getPostProposalDetails(int proposalId);
    
}

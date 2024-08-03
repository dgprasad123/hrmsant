/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.workflowrouting;

import hrms.model.employee.EmployeeBasicProfile;
import hrms.model.workflowrouting.WorkflowRouting;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface WorkflowRoutingDAO {
    
    public List getmappedPostList(String postcode, String processId);
    
    public void addHierarchy(WorkflowRouting wr);
    
    public void removeHierarchy(WorkflowRouting wr);
    
    public List getPostListAuthorityWise(String offcode, String postcode, String processId);
    
    public List<EmployeeBasicProfile> getWorkFlowRoutingList(int processId, String postcode, String officecode);
    
}

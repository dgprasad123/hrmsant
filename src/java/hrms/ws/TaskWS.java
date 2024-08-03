/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws;

import hrms.dao.leaveapply.LeaveApplyDAO;
import hrms.dao.loanworkflow.LoanApplyDAO;
import hrms.dao.master.LeaveActionTypeDAO;
import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.performanceappraisal.PARBrowserDAO;
import hrms.dao.propertystatement.PropertyStatementDAO;
import hrms.dao.task.TaskDAO;
import hrms.dao.workflowrouting.WorkflowRoutingDAO;
import hrms.model.employee.EmployeeBasicProfile;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveFlowDtls;
import hrms.model.leave.LeaveListDtlsBean;
import hrms.model.master.LeaveActionType;
import hrms.model.master.LeaveTypeBean;
import hrms.model.parmast.ParMasterWS;
import hrms.model.propertystatement.PropertyStatement;
import hrms.model.task.TaskListHelperBean;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Manas Jena
 */
@WebService
public class TaskWS {

    @Autowired
    TaskDAO taskDAO;
    LeaveApplyDAO leaveApplyDAO;
    PARBrowserDAO parbrowserDao;
    LoanApplyDAO loanworkflowDao;
    PropertyStatementDAO propertyStatementDAO;
    LeaveTypeDAO leaveTypeDAO;
    WorkflowRoutingDAO workflowRoutingDao;
    LeaveActionTypeDAO leaveActionTypeDAO;

    @WebMethod(exclude = true)
    public void setWorkflowRoutingDao(WorkflowRoutingDAO workflowRoutingDao) {
        this.workflowRoutingDao = workflowRoutingDao;
    }

    @WebMethod(exclude = true)
    public void setLeaveTypeDAO(LeaveTypeDAO leaveTypeDAO) {
        this.leaveTypeDAO = leaveTypeDAO;
    }

    @WebMethod(exclude = true)
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @WebMethod(exclude = true)
    public void setLeaveActionTypeDAO(LeaveActionTypeDAO leaveActionTypeDAO) {
        this.leaveActionTypeDAO = leaveActionTypeDAO;
    }

    @WebMethod(exclude = true)
    public void setLeaveApplyDAO(LeaveApplyDAO leaveApplyDAO) {
        this.leaveApplyDAO = leaveApplyDAO;
    }

    @WebMethod(exclude = true)
    public void setParbrowserDao(PARBrowserDAO parbrowserDao) {
        this.parbrowserDao = parbrowserDao;
    }

    @WebMethod(exclude = true)
    public void setLoanworkflowDao(LoanApplyDAO loanworkflowDao) {
        this.loanworkflowDao = loanworkflowDao;
    }

    @WebMethod(exclude = true)
    public void setPropertyStatementDAO(PropertyStatementDAO propertyStatementDAO) {
        this.propertyStatementDAO = propertyStatementDAO;
    }

    @WebMethod(operationName = "getMyTaskList")
    public TaskListHelperBean[] getMyTaskList(@WebParam(name = "empId") @XmlElement(required = true) String empId) {
        List<TaskListHelperBean> tasklist = taskDAO.getMyTaskList(empId);
        TaskListHelperBean taskarray[] = tasklist.toArray(new TaskListHelperBean[tasklist.size()]);
        return taskarray;
    }

    @WebMethod(operationName = "getLeaveApplyList")
    public LeaveListDtlsBean[] getLeaveApplyList(@WebParam(name = "empId") @XmlElement(required = true) String empId) {
        List<LeaveListDtlsBean> leavelist = leaveApplyDAO.getLeaveApplyList(empId);
        LeaveListDtlsBean leavearray[] = leavelist.toArray(new LeaveListDtlsBean[leavelist.size()]);
        return leavearray;
    }

    @WebMethod(operationName = "getPARList")
    public ParMasterWS[] getPARList(@WebParam(name = "empId") @XmlElement(required = true) String empId) {
        List<ParMasterWS> parlist = parbrowserDao.getPARList(empId);
        ParMasterWS pararray[] = parlist.toArray(new ParMasterWS[parlist.size()]);
        return pararray;
    }

    /*@WebMethod(operationName = "getLoanList")
     public LoanList[] getLoanList(@WebParam(name = "empId") @XmlElement( required = true )String empId){
     List<LoanList> loanlist = loanworkflowDao.getLoanList(empId);
     LoanList loanarray[] = loanlist.toArray(new LoanList[loanlist.size()]);        
     return loanarray;
     }*/
    @WebMethod(operationName = "getPropertyList")
    public PropertyStatement[] getPropertyList(@WebParam(name = "empId") @XmlElement(required = true) String empId) {
        List<PropertyStatement> pstatementlist = propertyStatementDAO.getPropertyList(empId);
        PropertyStatement pstatementarray[] = pstatementlist.toArray(new PropertyStatement[pstatementlist.size()]);
        return pstatementarray;
    }

    @WebMethod(operationName = "applyLeave")
    public boolean applyLeave(Leave leaveForm) {
        return leaveApplyDAO.saveLeave(leaveForm);
    }

    @WebMethod(operationName = "getLeaveType")
    public LeaveTypeBean[] getLeaveType() {
        ArrayList<LeaveTypeBean> leaveTypeList = leaveTypeDAO.getLeaveTypeList();
        LeaveTypeBean leaveTypes[] = leaveTypeList.toArray(new LeaveTypeBean[leaveTypeList.size()]);
        return leaveTypes;
    }

    @WebMethod(operationName = "getWorkFlowAuthList")
    public EmployeeBasicProfile[] getWorkFlowAuthList(@WebParam(name = "processId") @XmlElement(required = true) int processId, @WebParam(name = "postcode") @XmlElement(required = true) String postcode, @WebParam(name = "officecode") @XmlElement(required = true) String officecode) {
        List<EmployeeBasicProfile> empList = workflowRoutingDao.getWorkFlowRoutingList(processId, postcode, officecode);
        System.out.println("*****"+empList.size());
        EmployeeBasicProfile authList[] = empList.toArray(new EmployeeBasicProfile[empList.size()]);
        return authList;
    }

    @WebMethod(operationName = "getLeaveData")
    public Leave getLeaveData(@WebParam(name = "taskId") @XmlElement(required = true) String taskId, @WebParam(name = "empId") @XmlElement(required = true) String empId, @WebParam(name = "spc") @XmlElement(required = true) String spc) {
        return leaveApplyDAO.getLeaveData(taskId, empId, spc);
    }

    @WebMethod(operationName = "getActionType")
    public LeaveActionType[] getActionType() {
        ArrayList<LeaveActionType> leaveActionTypeList = leaveActionTypeDAO.getActionType();
        LeaveActionType leaveActionTypes[] = leaveActionTypeList.toArray(new LeaveActionType[leaveActionTypeList.size()]);
        return leaveActionTypes;
    }

    @WebMethod(operationName = "getLeaveWorkFlowDetails")
    public LeaveFlowDtls[] getLeaveWorkFlowDetails(@WebParam(name = "taskId") @XmlElement(required = true) String taskId) {
        ArrayList<LeaveFlowDtls> leaveWorkFlowDtls = leaveApplyDAO.getLeaveWorkFlowDtls(taskId);
        LeaveFlowDtls leaveWorkFlow[] = leaveWorkFlowDtls.toArray(new LeaveFlowDtls[leaveWorkFlowDtls.size()]);
        return leaveWorkFlow;
    }

    @WebMethod(operationName = "updateTaskList")
    public boolean updateActionTaken(Leave lfb) {
        return leaveApplyDAO.updateTaskList(lfb);
    }
}

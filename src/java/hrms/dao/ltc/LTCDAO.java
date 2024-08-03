/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.ltc;

import hrms.common.Message;
import hrms.model.LTC.LTCBean;
import hrms.model.LTC.sLTCBean;
import hrms.model.login.Users;
import java.util.List;


/**
 *
 * @author Manoj PC
 */
public interface LTCDAO {
    public void saveEmpLTCData(LTCBean ltBean, String empId, String spc);
    public int saveBasicInfo(LTCBean ltBean, String empId, String spc);
    public void updateBasicInfo(LTCBean ltBean, String ltcId);
    public void saveFamilyMember(LTCBean lBean, String empId);
    public List getLeaveTypes();
    public List getRelations();
    public List getFamilyMembers(String ltcId);
    public LTCBean getLtcDetail(String ltcId);
    public void saveRDetails(LTCBean lBean, String empId);
    public List getEmpLTCList(String empId);
    public int getLTCCount(String empId);
    public void saveIssuingAuthority(LTCBean lBean, String empId);
    public void saveReportingAuthority(LTCBean lBean, String empId);
    public String getAuthorityName(String spc);
    public void submitLTC(int propmastId, String loggedempid, String loggedspc, String authSpc);
    public int getProposalMasterId(int taskId);
    public Users getEmpDetail(String empId);
    public List getLTCStatus();
    public Message saveLTCAction(int taskId, int ltcStatus, String loginempid, String loginSpc, String forwardEmpid, String note);
    public int getTaskStatus(int taskId);
    public Message saveOrderInfo(int taskId, String orderDate, String orderNo, int ltcId);
    public Message saveVerification(int taskId, String loginempid, String loginSpc, LTCBean lBean);
    public void saveLTCEntry(String notId, sLTCBean slBean, String deptCode, String offCode, String spc);
    public List getLTCEntryList(String empId);
    public sLTCBean getSLTCDetail(String empId, int notId);
    public void updateLTCEntry(sLTCBean lBean);
}

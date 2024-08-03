/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.onlineTicketing;

import hrms.model.common.FileAttribute;
import hrms.model.onlineTicketing.OnlineTicketing;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface OnlineTicketingDAO {

    public void addTicket(OnlineTicketing ticket, String spc);

    public List getTicketList(String userId, String fdate, String tdate);

    public List getTicketListByEmployee(String userId);

    public List getTicketListByOffice(String offCode);

    public String getOfficeEstInformation(String spc);

    public OnlineTicketing editTicket(int ticketId);

    public void closeTicket(int ticketId);

    public List getTicketListDC(String userId, String loginId, String fdate, String tdate);

    public List getTicketListDDO(String empId, String loginId, String fdate, String tdate, String offCode);

    public List getTicketResolvedListDC(String userId, String loginId, String fdate, String tdate);

    public List ticketActionDc(String userId, String encticketid, String loginId);

    public void SaveonlineticketDc(OnlineTicketing ticket);

    public List ticketEmpActionDc(String userId, int ticketId);

    public FileAttribute downloadTicket(String filepath, int attid) throws Exception;

    public List onlineticketStatelist(String fdate, String tdate);

    public List reportOnlineTicketStatelist(OnlineTicketing ticket);

    public List ticketActionState(int ticketId);

    public void SaveonlineticketState(OnlineTicketing ticket);

    public void SaveonlineticketByDc(OnlineTicketing ticket);

    public void SaveonlineticketByDDO(OnlineTicketing ticket);

    public void ticketTakeOver(int ticketId, String loginId);

    public List getOnlineTicketDataReport(String logindistcode, OnlineTicketing ticket);

    public List misreportforDC(int month, int year);

    public List GetStateMISReport(int month, int year);

    public List viewActionDetails(String userId, int ticketId, String loginId);

    public List getknowledgebase(String topicId, String subtopic);

    public List getDCPendingTicketlist();

}

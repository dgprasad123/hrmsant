/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.miscellaneous;

import hrms.model.empinfo.EmployeeGrievance;
import hrms.model.empinfo.GrievanceDashBoard;
import hrms.model.empinfo.GrievnceCommunication;
import hrms.model.empinfo.SMSGrievance;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public interface EmployeeGrievanceDAO {
    public List getGrievnceCategoryList();
    public List getEmployeeGrievnceList(String empId);
    public List getAdminGrievnceList(String spc);
    public List getAdminGrievnceList(String spc, String categoryCode, String status);
    public int saveEmployeeGrievnce(EmployeeGrievance employeeGrievance);
    public void updateEmployeeGrievnce(EmployeeGrievance employeeGrievance);
    public int saveEmployeeGrievnceByAdmin(EmployeeGrievance employeeGrievance);
    public void uploadAttachedFile(int gid, MultipartFile attachment, String refType);
    public void saveSMSGrievance(SMSGrievance smsGrievance);
    public EmployeeGrievance getGrievanceDetail(int gid);
    public List getCommunicationList(int gid);
    public void saveGrievanceCommunication(GrievnceCommunication grievnceCommunication);
    public GrievanceDashBoard getDashBoardDetail(String offcode);
    public List getDashBoardDetail(String offcode, String status);
    public List getBattalionwiseDashBoardDetail();
    public int disposeGrievance(GrievnceCommunication grievnceCommunication);
    public int rejectGrievance(GrievnceCommunication grievnceCommunication);
    public int forwardGrievance(GrievnceCommunication grievnceCommunication);
}

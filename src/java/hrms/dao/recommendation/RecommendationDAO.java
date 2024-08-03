/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.recommendation;

import hrms.common.FileDownload;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.recommendation.RecommendationDetailBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Manisha
 */
public interface RecommendationDAO {

    public ArrayList getRecommendationList(String empId);

    public ArrayList getOtherRecommendationList(String offCode);

    public void submittodepartment(int recommendationId, int taskId, String submitteddeptby);

    public RecommendationDetailBean getRecommendationDetailFromRecommendeddetailId(int recommendationId);

    public RecommendationDetailBean getRecommendationDetail(int recommendeddetailId);

    public void saveAuthorityAction(String actionType, RecommendationDetailBean recommendationDetailBean);

    public String saveRecommendedEmployeeDetail(RecommendationDetailBean recommendationDetailBean);

    public List getrecommendationEmployeeList(String offCode, int recommendationId);

    public void saverecommendationEmployeeList(RecommendationDetailBean recommendationDetailBean);

    public boolean removeRecommendationEmployee(int recommendeddetailId);

    public List getrecommendationEmployeeListForOtherOffice(String offCode);

    public ArrayList getrecommendedEmployeeList(int recommendationId);

    public RecommendationDetailBean getRecommendationApplicationDetail(int recommendationId);

    public RecommendationDetailBean getRecommendationApplicationDetailFromTaskId(int taskId);

    public void createRecommendationReport(RecommendationDetailBean recommendationDetailBean);

    public ArrayList getRecommendeList(int recommendationId);

    public ArrayList getRecommendedEmployeeList(int taskId);

    public void submitRecommendationList(int recommendationId, String pendingAt, String pendingSPC);

    public String checkRecommendationData(int recommendationId);

    public void submitRecommendationListToDepartment(int recommendationId, String submittedBy, String submittedBySpc);

    public FileDownload getFileDownloadData(int recommendeddetailId, String documentTypeName);

    public ArrayList getRecommendationListByDepartment(String departmentCode, String employeeGroup);
    
    public ArrayList getRecommendationListByDepartment(String departmentCode, String employeeGroup, String nominationType);
    
    public ArrayList getRecommendationListByGroup(String employeeGroup, String nominationType);

    public ArrayList getRecommendationListDepartmentWise(List deptList);

    public ArrayList getRecommendationListDistrictWise(List districtList);

    public HashMap getRecommendationListNoinamtionTypeWise();

    public DepartmentPromotionDetail getPARList(String empId);

}

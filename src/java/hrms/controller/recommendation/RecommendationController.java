/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.recommendation;

import hrms.common.FileDownload;
import hrms.dao.AGPension.PensionNOCDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.recommendation.RecommendationDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.recommendation.RecommendationDetailBean;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manisha
 */
@Controller

@SessionAttributes({"LoginUserBean"})

public class RecommendationController {

    @Autowired
    RecommendationDAO recommendationDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    PensionNOCDAO PensionNOC;

    @Autowired
    TaskDAO taskDAO;

    /*This Function is call when the Authority 1st login and click on recommendation link*/
    @RequestMapping(value = "recommendationList.htm", method = RequestMethod.GET)
    public ModelAndView recommendationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        List offlist = officeDao.getReportingOfficeList(lub.getLoginoffcode());
        ArrayList recommendationList = recommendationDAO.getRecommendationList(lub.getLoginempid());
        mv.addObject("offlist", offlist);
        mv.addObject("recommendationList", recommendationList);
        //if (office.getLvl().equals("01") || office.getLvl().equals("02") || office.getCategory().equals("DISTRICT COLLECTORATE")) {
            mv.setViewName("recommendation/RecommendationFinalReport");
        //} else {
            //mv.setViewName("errorpage/erroclosed");
        //}

        mv.addObject("office", office);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "checkRecommendationData.htm")
    public void checkRecommendationData(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, HttpServletResponse response) throws Exception {
        String dataIntegrity = recommendationDAO.checkRecommendationData(recommendationDetailBean.getRecommendationId());
        response.setContentType("application/json");
        JSONObject job = new JSONObject();
        job.append("dataIntegrity", dataIntegrity);
        PrintWriter out = response.getWriter();
        out.print(job.toString());
        out.flush();
    }
    /*This Function will call when the Authority will click on create button */

    @RequestMapping(value = "createNewRecommendationList.htm", params = {"action=Create"})
    public ModelAndView createRecommendationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        recommendationDetailBean.setInitiatedByempId(lub.getLoginempid());
        recommendationDetailBean.setInitiatedByspc(lub.getLoginspc());
        recommendationDetailBean.setOffCode(lub.getLoginoffcode());
        recommendationDAO.createRecommendationReport(recommendationDetailBean);

        return new ModelAndView("redirect:/recommendationList.htm");
    }

    @RequestMapping(value = "createNewRecommendationList.htm", params = {"action=Submit"})
    public ModelAndView submitRecommendationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        String tSPC = recommendationDetailBean.getAuthspc();
        String[] tVal = tSPC.split(",");
        recommendationDAO.submitRecommendationList(recommendationDetailBean.getRecommendationId(), tVal[1], tVal[0]);
        return new ModelAndView("redirect:/recommendationList.htm");
    }

    @ResponseBody
    @RequestMapping(value = "submitRecommendationListToDepartment.htm")
    public void submitRecommendationListToDepartment(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, HttpServletResponse response) throws Exception {
        recommendationDAO.submitRecommendationListToDepartment(recommendationDetailBean.getRecommendationId(), lub.getLoginempid(), lub.getLoginspc());
        response.setContentType("application/json");
        JSONObject job = new JSONObject();
        job.append("msg", lub.getLogindeptname());
        PrintWriter out = response.getWriter();
        out.print(job.toString());
        out.flush();
    }

    @RequestMapping(value = "recommendationEmployeeList", method = {RequestMethod.GET})
    public ModelAndView recommendationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        List officelistcowise = new ArrayList();
        if (office.getLvl() != null && office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl() != null && office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory() != null && office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }

        List recommendationemplist = recommendationDAO.getrecommendationEmployeeList(lub.getLoginoffcode(), recommendationDetailBean.getRecommendationId());
        mv.addObject("recommendationemplist", recommendationemplist);
        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        mv.setViewName("/recommendation/EmployeeListForRecommendation");
        return mv;
    }

    @RequestMapping(value = "recommendationEmployeeList.htm", params = {"action=Search"})
    public ModelAndView recommendationOtherEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("offcode") String offcode, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        List officelistcowise = new ArrayList();
        if (office.getLvl().equals("01")) {
            officelistcowise = officeDao.getTotalOfficeList(lub.getLogindeptcode());
        } else if (office.getLvl().equals("02")) {
            officelistcowise = officeDao.getOfficeListCOWise(lub.getLoginoffcode());
        } else if (office.getCategory().equals("DISTRICT COLLECTORATE")) {
            officelistcowise = officeDao.getDistrictWiseOfficeList(lub.getLogindistrictcode());
        }
        List recommendationemplist = recommendationDAO.getrecommendationEmployeeList(offcode, recommendationDetailBean.getRecommendationId());
        mv.addObject("recommendationemplist", recommendationemplist);
        mv.addObject("office", office);
        mv.addObject("officelistcowise", officelistcowise);
        mv.setViewName("/recommendation/EmployeeListForRecommendation");
        return mv;
    }

    @RequestMapping(value = "recommendationEmployeeList.htm", params = {"action=Back"})
    public ModelAndView backRecommendationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/recommendationList.htm");
        return mv;
    }

    /*This Code is use while authority wants to add Employee*/
    @ResponseBody
    @RequestMapping(value = "addrecommendationEmployeeList.htm")
    public void addrecommendationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        recommendationDetailBean.setInitiatedByempId(lub.getLoginempid());
        recommendationDetailBean.setInitiatedByspc(lub.getLoginspc());
        Employee employee = employeeDAO.getEmployeeProfile(recommendationDetailBean.getRecommendedempId());
        recommendationDetailBean.setRecommendedoffcode(employee.getOfficecode());
        recommendationDetailBean.setRecommendeddeptcode(employee.getDeptcode());
        recommendationDetailBean.setRecommendedpostgrp(employee.getPostGrpType());
        recommendationDAO.saverecommendationEmployeeList(recommendationDetailBean);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "removerecommendationEmployeeList.htm")
    public void removerecommendationEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        boolean employeeRemoved = recommendationDAO.removeRecommendationEmployee(recommendationDetailBean.getRecommendeddetailId());
        JSONObject obj = new JSONObject();
        if (employeeRemoved == true) {
            obj.append("msg", "Y");
        } else {
            obj.append("msg", "N");
        }
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }
    /*This Code is use to get Employee From other Office*/

    @RequestMapping(value = "recommendationEmployeeListFromOtherOffice", method = {RequestMethod.GET})
    public ModelAndView recommendationEmployeeListFromOtherOffice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        List recommendationemplist = recommendationDAO.getrecommendationEmployeeList(lub.getLoginoffcode(), recommendationDetailBean.getRecommendationId());
        mv.addObject("recommendationemplist", recommendationemplist);
        mv.setViewName("/recommendation/EmployeeListForRecommendation");
        return mv;
    }

    /*This Function will call while the Authority will click on the Detail Button*/
    @RequestMapping(value = "viewRecommendedEmployeeList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewRecommendedEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        recommendationDetailBean.setRecommendationId(recommendationDetailBean.getRecommendationId());
        recommendationDetailBean.setRecommenadationType(recommendationDetailBean.getRecommenadationType());
        ArrayList recommendedList = recommendationDAO.getrecommendedEmployeeList(recommendationDetailBean.getRecommendationId());
        RecommendationDetailBean recommendationDetailBeanObj = recommendationDAO.getRecommendationApplicationDetail(recommendationDetailBean.getRecommendationId());
        mv.addObject("recommendedList", recommendedList);
        mv.addObject("recommendationDetailBean", recommendationDetailBean);
        mv.addObject("recommenadationType", recommendationDetailBean.getRecommenadationType());
        mv.addObject("recommendationDetailBeanObj", recommendationDetailBeanObj);
        mv.setViewName("recommendation/RecommendedEmpList");

        return mv;
    }

    @RequestMapping(value = "viewRecommendedEmployeeList.htm", params = {"action=Back"})
    public ModelAndView backviewRecommendedEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/recommendationList.htm");
        return mv;
    }

    /*This Function will call while the Authority will click on the Detail Button to get Reommended Employee Detail*/
    @RequestMapping(value = "editRecommendedEmployeeDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editRecommendedEmployeeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        recommendationDetailBean = recommendationDAO.getRecommendationDetail(recommendationDetailBean.getRecommendeddetailId());
        mv.addObject("recommendationDetailBean", recommendationDetailBean);
        mv.addObject("office", office);
        if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("across batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("within batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("incentives award")) {
            mv.setViewName("recommendation/RecommendedEmployeeDetail");
        } else if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("Premature Retirement")) {
            mv.setViewName("recommendation/RecommendedEmployeeDetailForRetirement");
        }
        return mv;
    }

    @RequestMapping(value = "viewRecommendedEmployeeDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewRecommendedEmployeeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        recommendationDetailBean = recommendationDAO.getRecommendationDetail(recommendationDetailBean.getRecommendeddetailId());
        mv.addObject("recommendationDetailBean", recommendationDetailBean);
        if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("across batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("within batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("incentives award")) {
            mv.setViewName("recommendation/RecommendedEmployeeDetailView");
        } else if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("Premature Retirement")) {
            mv.setViewName("recommendation/RecommendedEmployeeDetailForRetirementView");
        }
        return mv;
    }

    @RequestMapping(value = "approveRecommendedEmployeeDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView approveRecommendedEmployeeDetail(@RequestParam Map<String, String> reqParam, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        System.out.println(reqParam.get("action"));
        if (reqParam.get("action").equals("Approve") || reqParam.get("action").equals("Decline")) {
            recommendationDAO.saveAuthorityAction(reqParam.get("action"), recommendationDetailBean);
            recommendationDetailBean = recommendationDAO.getRecommendationDetail(recommendationDetailBean.getRecommendeddetailId());
            mv.addObject("recommendationDetailBean", recommendationDetailBean);
            if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("across batch promotion")
                    || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("within batch promotion")
                    || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("incentives award")) {
                mv.setViewName("recommendation/RecommendedEmployeeDetailView");
            } else if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("Premature Retirement")) {
                mv.setViewName("recommendation/RecommendedEmployeeDetailForRetirement");
            }
        } else if (reqParam.get("action").equals("Back")) {
            mv.setViewName("redirect:viewRecommendationDetail.htm?taskId=" + recommendationDetailBean.getTaskId());
        } else if (reqParam.get("action").equals("Download")) {

        }
        return mv;
    }

    @RequestMapping(value = "submittodepartment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView submittodepartment(@RequestParam Map<String, String> reqParam, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        recommendationDAO.submittodepartment(recommendationDetailBean.getRecommendationId(), recommendationDetailBean.getTaskId(), lub.getLoginempid());
        mv.setViewName("redirect:viewRecommendationDetail.htm?taskId=" + recommendationDetailBean.getTaskId());
        return mv;
    }
    /*This Function will call while the Authority will click on the Save Button to Save Employee Recommendation Detail*/

    @RequestMapping(value = "saveRecommendedEmployeeDetail", params = {"action=Save"}, method = {RequestMethod.POST})
    public ModelAndView saveRecommendedEmployeeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        String msg = recommendationDAO.saveRecommendedEmployeeDetail(recommendationDetailBean);
        recommendationDetailBean = recommendationDAO.getRecommendationDetail(recommendationDetailBean.getRecommendeddetailId());
        mv.addObject("recommendationDetailBean", recommendationDetailBean);
        mv.addObject("msg", msg);
        Office office = officeDao.getOfficeDetails(lub.getLoginoffcode());
        mv.addObject("office", office);
        if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("across batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("within batch promotion")
                || recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("incentives award")) {

            mv.setViewName("recommendation/RecommendedEmployeeDetail");
        } else if (recommendationDetailBean.getRecommenadationType().equalsIgnoreCase("Premature Retirement")) {
            mv.setViewName("recommendation/RecommendedEmployeeDetailForRetirement");
        }
        return mv;
    }

    @RequestMapping(value = "saveRecommendedEmployeeDetail", params = {"action=Back"}, method = {RequestMethod.POST})
    public ModelAndView backRecommendedEmployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:viewRecommendedEmployeeList.htm?recommendationId=" + recommendationDetailBean.getRecommendationId());
        return mv;
    }

    @RequestMapping(value = "downloadFile", method = {RequestMethod.GET})
    public void downloadFile(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, HttpServletResponse response) throws IOException {
        FileDownload fileDownloadData = recommendationDAO.getFileDownloadData(recommendationDetailBean.getRecommendeddetailId(), recommendationDetailBean.getDocumentTypeName());
        response.setContentType(fileDownloadData.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileDownloadData.getOriginalfilename());
        OutputStream out = response.getOutputStream();
        out.write(fileDownloadData.getFilecontent());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "createRecommendationDetailReport.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView createRecommendationDetailReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        recommendationDetailBean.setInitiatedByempId(lub.getLoginempid());
        recommendationDetailBean.setInitiatedByspc(lub.getLoginspc());
        recommendationDAO.createRecommendationReport(recommendationDetailBean);
        mv.setViewName("recommendation/EmployeeListForRecommendation");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationDetail.htm", method = {RequestMethod.GET})
    public ModelAndView viewRecommendationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        recommendationDetailBean = recommendationDAO.getRecommendationApplicationDetailFromTaskId(recommendationDetailBean.getTaskId());
        System.out.println("****" + recommendationDetailBean.getTaskId());
        ArrayList recommendationDetailList = recommendationDAO.getRecommendedEmployeeList(recommendationDetailBean.getTaskId());
        mv.addObject("recommendationDetailList", recommendationDetailList);
        mv.addObject("recommendationDetailBean", recommendationDetailBean);
        mv.setViewName("recommendation/ViewRecommendationDetail");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationListByDepartment.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewRecommendationListByDepartment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        if (recommendationDetailBean.getRecommendedempgroup() != null && !recommendationDetailBean.getRecommendedempgroup().equals("")) {
            ArrayList recommendationList = recommendationDAO.getRecommendationListByDepartment(lub.getLogindeptcode(), recommendationDetailBean.getRecommendedempgroup(), recommendationDetailBean.getRecommenadationType());
            mv.addObject("recommendationList", recommendationList);
        }
        mv.setViewName("recommendation/ViewRecommendationListByDepartment");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationListForGroupb.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewRecommendationListForGroupb(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        if (recommendationDetailBean.getRecommendedempgroup() != null && !recommendationDetailBean.getRecommendedempgroup().equals("")) {
            ArrayList recommendationList = recommendationDAO.getRecommendationListByGroup(recommendationDetailBean.getRecommendedempgroup(), recommendationDetailBean.getRecommenadationType());
            mv.addObject("recommendationList", recommendationList);
        }
        mv.setViewName("recommendation/ViewRecommendationListForGroupb");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationListDepartmentWise.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewRecommendationListDepartmentWise(@ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        List deptlist = departmentDAO.getDepartmentList();
        List recommendationList = recommendationDAO.getRecommendationListDepartmentWise(deptlist);
        mv.addObject("recommendationList", recommendationList);
        mv.setViewName("recommendation/viewRecommendationListDepartmentWise");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationListDistrictWise.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewRecommendationListDistrictWise(@ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        List districtlist = districtDAO.getDistrictList();
        List recommendationList = recommendationDAO.getRecommendationListDistrictWise(districtlist);
        mv.addObject("recommendationList", recommendationList);
        mv.setViewName("recommendation/viewRecommendationListDistrictWise");
        return mv;
    }

    @RequestMapping(value = "viewRecommendationListNoinamtionTypeWise.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewRecommendationListNoinamtionTypeWise(@ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        List districtlist = districtDAO.getDistrictList();
        HashMap recommendationList = recommendationDAO.getRecommendationListNoinamtionTypeWise();
        mv.addObject("recommendationList", recommendationList);
        mv.setViewName("recommendation/viewRecommendationListNoinamtionTypeWise");
        return mv;
    }

    @RequestMapping(value = "getPARList.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getPARList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean) {
        ModelAndView mv = new ModelAndView();
        DepartmentPromotionDetail departmentPromotionDetail = recommendationDAO.getPARList(recommendationDetailBean.getRecommendedempId());
        mv.addObject("departmentPromotionDetail", departmentPromotionDetail);
        mv.setViewName("recommendation/ViewParList");
        return mv;
    }

    @RequestMapping(value = "nominationDetailPdfView.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView nominationDetailPdfView(@ModelAttribute("recommendationDetailBean") RecommendationDetailBean recommendationDetailBean, @RequestParam("recommendeddetailId") String recommendeddetailId) {
        ModelAndView mv = new ModelAndView();
        recommendationDetailBean = recommendationDAO.getRecommendationDetail(Integer.parseInt(recommendeddetailId));
        mv.addObject("recommendationDetailBean", recommendationDetailBean);

        mv.setViewName("nominationDetailPdfView");
        return mv;
    }

    @RequestMapping(value = "NocListFromVigilanceCB")
    public ModelAndView NocListFromVigilanceCB(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @RequestParam("noctype") String noctype) {
        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        String NocStatus = "N";
        mv.addObject("nocStatusType", "Pending");
        mv.addObject("adminStatus", "Y");
        if (noctype.equals("cb")) {
            mv.addObject("loginName", "Crime Branch");
            mv.addObject("pensionList", PensionNOC.CBPensionerNocList(NocStatus));
        } else if (noctype.equals("vigilance")) {
            mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList(NocStatus));
            mv.addObject("loginName", "Vigilance");
        }
        return mv;
    }

    @RequestMapping(value = "CompletedNocListFromVigilanceCB")
    public ModelAndView CompletedNocListFromVigilanceCB(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("PensionNOCBean") PensionNOCBean pnoc, @RequestParam("noctype") String noctype) {

        ModelAndView mv = new ModelAndView("/AGPension/VigilancePensionerNocList", "PensionNOCBean", pnoc);
        String NocStatus = "Y";
        mv.addObject("adminStatus", "Y");
        mv.addObject("nocStatusType", "Completed");
        if (noctype.equals("cb")) {
            mv.addObject("loginName", "Crime Branch");
            mv.addObject("pensionList", PensionNOC.CBPensionerNocList(NocStatus));
        } else if (noctype.equals("vigilance")) {
            mv.addObject("pensionList", PensionNOC.VigilancePensionerNocList(NocStatus));
            mv.addObject("loginName", "Vigilance");

        }
        return mv;
    }

    @RequestMapping(value = "NocMonitoring")
    public ModelAndView CompletedNocListFromVigilanceCB(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("recommendation/NocMonitoring");
        return mv;

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.wrrreport;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.dao.EmpQuarterAllotment.EmpQuarterAllotDAO;
import hrms.dao.EmpQuarterAllotment.SendEquarterSMS;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.EmpQuarterAllotment.FundSanctionOrderBean;
import hrms.model.WaterRent;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.schedule.Schedule;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
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
 * @author Manas
 */
@SessionAttributes("LoginUserBean")
@Controller
public class WRRReportController {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public EmpQuarterAllotDAO empQuarterAllotDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "WrrReport")
    public ModelAndView wrrReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("schedule") Schedule schedule) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/wrrreport/WRRReport");
        return mav;
    }

    @RequestMapping(value = "WrrReportEmployeewise", method = {RequestMethod.GET})
    public ModelAndView WrrReportEmployeewise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("waterRent") WaterRent waterRent1) {
        ModelAndView mav = new ModelAndView();
        List<WaterRent> rentdata = new ArrayList();
        WaterRent waterRent[] = rentdata.toArray(new WaterRent[rentdata.size()]);
        mav.addObject("waterRentlist", waterRent);
        mav.setViewName("/wrrreport/WRRReportEmployeewise");
        return mav;
    }

    @RequestMapping(value = "WrrReportEmployeewise", method = {RequestMethod.POST}, params = {"action=Search"})
    public ModelAndView SearchWrrReportEmployeewise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("waterRent") WaterRent waterRent1) {
        ModelAndView mav = new ModelAndView();
        WaterRent waterRent[] = comonScheduleDao.getWaterRentData(waterRent1.getHrmsid());
        mav.addObject("waterRentlist", waterRent);
        mav.setViewName("/wrrreport/WRRReportEmployeewise");
        return mav;
    }

    @RequestMapping(value = "WrrReportDataJson")
    @ResponseBody
    public void wrrReportDataJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            int calcmonth = Integer.parseInt(parameters.get("calcmonth"));
            int calcyear = Integer.parseInt(parameters.get("calcyear"));
            String ddocode = parameters.get("ddocode");
            WaterRent waterRent[] = comonScheduleDao.getWaterRentData(calcmonth, calcyear);
            JSONArray arr = new JSONArray(waterRent);
            JSONObject jobj = new JSONObject();
            jobj.put("wrrdata", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "wrrReportData", params = {"action=Search"})
    public ModelAndView wrrReportData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("schedule") Schedule schedule) {
        ModelAndView mav = new ModelAndView();
        int calcmonth = Integer.parseInt(schedule.getRecMonth());
        int calcyear = Integer.parseInt(schedule.getRecYear());
        WaterRent waterRent[] = comonScheduleDao.getWaterRentData(calcmonth, calcyear);
        mav.addObject("waterRent", waterRent);
        mav.setViewName("/wrrreport/WRRReport");
        return mav;
    }

    @RequestMapping(value = "wrrReportData", params = {"action=Download"})
    public ModelAndView wrrReportDataExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("schedule") Schedule schedule) {
        int calcmonth = Integer.parseInt(schedule.getRecMonth());
        int calcyear = Integer.parseInt(schedule.getRecYear());
        WaterRent waterRent[] = comonScheduleDao.getWaterRentData(calcmonth, calcyear);
        ModelAndView mav = new ModelAndView("wrrScheduleView");
        mav.addObject("waterRent", waterRent);
        return mav;
    }

    @RequestMapping(value = "WrrReportEmployeewise", method = {RequestMethod.POST}, params = {"action=Download"})
    public ModelAndView wrrReportDataEmployeeExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("waterRent") WaterRent waterRent1) {
        WaterRent waterRent[] = comonScheduleDao.getWaterRentData(waterRent1.getHrmsid());
        ModelAndView mav = new ModelAndView("wrrScheduleView");
        mav.addObject("waterRent", waterRent);
        return mav;
    }

    @RequestMapping(value = "quarterMaster", method = {RequestMethod.GET})
    public ModelAndView quarterMaster(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.setViewName("/wrrreport/quarterMaster");
        return mav;
    }

    @RequestMapping(value = "quarterRepairOrder", method = {RequestMethod.GET})
    public ModelAndView QuarterRepairOrderList(@ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) {
        ModelAndView mav = new ModelAndView();
        List quarterRepairOrderList = empQuarterAllotDAO.getQuarterRepairOrderList();
        mav.addObject("quarterRepairOrderList", quarterRepairOrderList);
        mav.setViewName("/wrrreport/QuarterRepairOrder");
        return mav;
    }

    @RequestMapping(value = "quarterRepairOrder", method = {RequestMethod.POST})
    public ModelAndView newQuarterRepairOrder(@ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) {
        ModelAndView mav = new ModelAndView();
        String msg = empQuarterAllotDAO.saveQuarterRepairOrder(fundSanctionOrderBean);

        mav.setViewName("redirect:quarterRepairOrder.htm");
        return mav;
    }

    @RequestMapping(value = "quarterRepairOrderDetail", method = {RequestMethod.GET})
    public ModelAndView quarterRepairOrderDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) {
        ModelAndView mav = new ModelAndView();
        fundSanctionOrderBean = empQuarterAllotDAO.getQuarterRepairOrderDetail(fundSanctionOrderBean.getAllotmentOrderId());
        List quarterRepairOrderDetailList = empQuarterAllotDAO.getQuarterRepairOrderDetailList(fundSanctionOrderBean.getAllotmentOrderId());
        mav.addObject("quarterRepairOrderDetailList", quarterRepairOrderDetailList);
        mav.addObject("fundSanctionOrderBean", fundSanctionOrderBean);
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.setViewName("/wrrreport/QuarterRepairOrderDetail");
        return mav;
    }

    @RequestMapping(value = "quarterRepairOrderDetail", method = {RequestMethod.POST})
    public ModelAndView quarterRepairOrderDetailBack(@ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:quarterRepairOrder.htm");
        return mav;
    }

    @RequestMapping(value = "quarterRepairOrdersendSMS", method = {RequestMethod.GET})
    public ModelAndView quarterRepairOrdersendSMS(@ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) throws Exception {
        ModelAndView mav = new ModelAndView();
        List quarterRepairOrderDetailList = empQuarterAllotDAO.getQuarterRepairOrderDetailList(fundSanctionOrderBean.getAllotmentOrderId());
        SendEquarterSMS ses = new SendEquarterSMS(quarterRepairOrderDetailList);
        new Thread(ses).start();
        String msg = empQuarterAllotDAO.sendQuarterRepairOrderSMS(fundSanctionOrderBean);
        mav.setViewName("redirect:quarterRepairOrder.htm");
        return mav;
    }

    @RequestMapping(value = "quarterMaster", method = {RequestMethod.POST})
    public ModelAndView searchquarterMaster(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.addObject("unitAreaList", empQuarterAllotDAO.getUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        mav.addObject("quarterList", quarterList);
        mav.setViewName("/wrrreport/quarterMaster");
        return mav;
    }

    @RequestMapping(value = "viewAllotmentPage", method = {RequestMethod.GET})
    public ModelAndView viewAllotmentPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQuarterDetail(empQuarterBean.getQaId());
        mav.addObject("quarterBeandetail", quarterBeandetail);
        mav.setViewName("/wrrreport/viewFundAllotmentPage");
        return mav;
    }

    @RequestMapping(value = "editAllotment", method = {RequestMethod.GET})
    public ModelAndView editAllotment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQuarterDetail(empQuarterBean.getQaId());
        mav.addObject("empQuarterBean", quarterBeandetail);
        mav.setViewName("/wrrreport/editAllotmentPage");
        return mav;
    }

    @RequestMapping(value = "saveQuarterAllotment", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveQuarterAllotment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        System.out.println(empQuarterBean);
        empQuarterAllotDAO.updateQuarterAllotment(empQuarterBean);
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.addObject("unitAreaList", empQuarterAllotDAO.getUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        mav.addObject("quarterList", quarterList);
        mav.setViewName("/wrrreport/quarterMaster");
        return mav;
    }

    @RequestMapping(value = "saveQuarterAllotment", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView backToListPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.addObject("unitAreaList", empQuarterAllotDAO.getUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        mav.addObject("quarterList", quarterList);
        mav.setViewName("/wrrreport/quarterMaster");
        return mav;
    }

    @RequestMapping(value = "saveQuarterRepairOrderDetail")
    @ResponseBody
    public void saveQuarterRepairOrderDetail(HttpServletResponse response, @ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) throws JSONException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String msg = empQuarterAllotDAO.saveQuarterRepairOrderDetail(fundSanctionOrderBean);
        JSONObject jobj = new JSONObject();
        jobj.put("msg", msg);
        out.write(jobj.toString());
    }

    @RequestMapping(value = "updateQuarterRepairOrderSMS")
    @ResponseBody
    public void updateQuarterRepairOrderSMS(HttpServletResponse response, @ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) throws JSONException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String msg = empQuarterAllotDAO.updateQuarterRepairOrderSMS(fundSanctionOrderBean);
        JSONObject jobj = new JSONObject();
        jobj.put("msg", msg);
        out.write(jobj.toString());
    }

    @RequestMapping(value = "deleteQuarterRepairOrderDetail")
    @ResponseBody
    public void deleteQuarterRepairOrderDetail(HttpServletResponse response, @ModelAttribute("fundSanctionOrderBean") FundSanctionOrderBean fundSanctionOrderBean) throws JSONException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String msg = empQuarterAllotDAO.deleteQuarterRepairOrderDetail(fundSanctionOrderBean.getFundid());
        JSONObject jobj = new JSONObject();
        jobj.put("msg", msg);
        out.write(jobj.toString());
    }

    @RequestMapping(value = "unitWiseQuarterTypeDataJson")
    @ResponseBody
    public void unitWiseQuarterTypeDataJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String quarterunitarea = parameters.get("quarterunitarea");
            List unitAreaList = empQuarterAllotDAO.getUnitAreawiseQuarterType(quarterunitarea);
            JSONArray arr = new JSONArray(unitAreaList);
            JSONObject jobj = new JSONObject();
            jobj.put("unitAreaList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "unitWiseQuarterTypeWiseQuarterNumberDataJson")
    @ResponseBody
    public void unitWiseQuarterTypeWiseQuarterNumberDataJson(HttpServletResponse response, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String qrtrunit = parameters.get("qrtrunit");
            String qrtrtype = parameters.get("qrtrtype");
            List quarterList = empQuarterAllotDAO.getQuarterData(qrtrunit, qrtrtype);
            JSONArray arr = new JSONArray(quarterList);
            JSONObject jobj = new JSONObject();
            jobj.put("quarterList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "unitWiseQuarterTypeWiseSMSLogJson")
    @ResponseBody
    public void unitWiseQuarterTypeWiseSMSLogJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String qaId = parameters.get("qaId");
            List fundAllotmentLogList = empQuarterAllotDAO.getFundAllotmentLog(Integer.parseInt(qaId));
            JSONArray arr = new JSONArray(fundAllotmentLogList);
            JSONObject jobj = new JSONObject();
            jobj.put("fundAllotmentLogList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getTransferList")
    @ResponseBody
    public ModelAndView getTransferList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        List quarterList = null;
        if (lub.getLoginusername().equals("garentadmin")) {
            quarterList = empQuarterAllotDAO.getTransferList();
        } else {
            quarterList = empQuarterAllotDAO.getTransferList(lub.getLoginusername());
        }

        mav.addObject("quarterList", quarterList);
        String Octype = qbean.getOccupationTypes();
        if (Octype != null) {
            mav.addObject("occupationTypes", Octype);
        } else {
            mav.addObject("occupationTypes", "3");
        }
        mav.addObject("UserName", lub.getLoginusername());

        mav.setViewName("/wrrreport/quarterTransferList");
        return mav;
    }

    @RequestMapping(value = "searchtransferCategory")

    public ModelAndView searchtransferCategory(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String Octype = qbean.getOccupationTypes();
        List quarterList = null;
        if (Octype.equals("7")) {
            mav = new ModelAndView("redirect:/searchretiringCategory.htm?occupationTypes=" + Octype, "EmpQuarterBean", qbean);
            return mav;
        }
        if (lub.getLoginusername().equals("garentadmin")) {
            quarterList = empQuarterAllotDAO.searchtransferCategory(Octype);
        } else {
            quarterList = empQuarterAllotDAO.searchtransferCategory(Octype, lub.getLoginusername());
        }
        mav.addObject("quarterList", quarterList);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("UserName", lub.getLoginusername());
        mav.setViewName("/wrrreport/quarterTransferList");
        return mav;
    }

    @RequestMapping(value = "searchretiringCategory")

    public ModelAndView searchretiringCategory(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String Octype = "7";
        List quarterList = null;
        if (lub.getLoginusername().equals("garentadmin")) {
            quarterList = empQuarterAllotDAO.searchtransferCategory(Octype);
        } else {
            quarterList = empQuarterAllotDAO.searchtransferCategory(Octype, lub.getLoginusername());
        }
        mav.addObject("quarterList", quarterList);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("UserName", lub.getLoginusername());
        mav.setViewName("/wrrreport/searchretiringCategory");
        return mav;
    }

    @RequestMapping(value = "searchretiringCategoryResult")

    public ModelAndView searchretiringCategoryResult(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String Octype = qbean.getOccupationTypes();
        List quarterList = null;
        if (!Octype.equals("7")) {
            mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + Octype, "EmpQuarterBean", qbean);
            return mav;
        }
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);

            if (qbean.getTxtperiodFrom() != null && !qbean.getTxtperiodFrom().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getTxtperiodFrom());
                qbean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                qbean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
                // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }
            // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
            if (qbean.getTxtperiodTo() != null && !qbean.getTxtperiodTo().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(qbean.getTxtperiodTo());
                qbean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                qbean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        quarterList = empQuarterAllotDAO.searchretiringCategoryResult(Octype, qbean.getTxtperiodFrom(), qbean.getTxtperiodTo());

        mav.addObject("quarterList", quarterList);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("UserName", lub.getLoginusername());
        mav.setViewName("/wrrreport/searchretiringCategory");
        return mav;
    }

    @RequestMapping(value = "QtrRetention")
    @ResponseBody
    public ModelAndView QtrRetention(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        // System.out.println("consumerNo="+qbean.getConsumerNo());
        String empId = qbean.getEmpId();

        String rtype = "";
        String rsubtype = "";

        if (Octype.equals("6") && Octype != null) {
            rtype = "Dismissed Case";
            rsubtype = "";

        }
        if (Octype.equals("5") && Octype != null) {
            rtype = "Death Case";
            rsubtype = "";

        }
        if (Octype.equals("4") && Octype != null) {
            rtype = "Tracking of Retirement Cases";
            rsubtype = "";

        }
        if (Octype.equals("3") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO Non-KBK Area)";

        }
        if (Octype.equals("2") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO KBK Area)";

        }
        if (Octype.equals("1") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO Cuttack/Bhubaneswar Area)";

        }

        mav.addObject("consumerNo", cno);
        mav.addObject("rsubtype", rsubtype);
        mav.addObject("rtype", rtype);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);

        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("distlist", distlist);

        int orderNo = empQuarterAllotDAO.maxRentOrderNo();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        formattedDate = formattedDate.toUpperCase();

        String orderNoString = String.valueOf(orderNo);
        orderNoString = StringUtils.leftPad(orderNoString, 8, "0");

        mav.addObject("orderNo", orderNoString);
        mav.addObject("orderdate", formattedDate);
        qbean.setEmpId(empId);
        qbean.setOccupationTypes(Octype);
        qbean.setRetentionType(rtype);
        qbean.setRetentionSubType(rsubtype);
        qbean.setOrderNumber(orderNoString);
        qbean.setOrderDate(formattedDate);
        qbean.setConsumerNo(cno);

        // System.out.println("orderNo="+orderNoString);
        //System.out.println("formattedDate="+formattedDate);
        //empQuarterAllotDAO.SaveAutoRetentionData(qbean);
        mav.setViewName("/wrrreport/qtrRetention");
        return mav;
    }

    /*@RequestMapping(value = "SaveAutoRetentionData")
     public ModelAndView SaveAutoRetentionData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
     ModelAndView mav = new ModelAndView();
     //  empQuarterAllotDAO.SaveAutoRetentionData(Octype,empId);
     // String octype = qbean.getOccupationTypes();
     mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + octype, "EmpQuarterBean", qbean);
     return mav;

     }*/
    @RequestMapping(value = "SaveRetentionData")
    public ModelAndView SaveRetentionData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        empQuarterAllotDAO.SaveRetentionData(qbean);
        String octype = qbean.getOccupationTypes();
        mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + octype, "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "GeneratePDFRetention")
    public void GeneratePDFRetention(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=RetentionPermission.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFRetention(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GeneratePDFExtendedRetention")
    public void GeneratePDFExtendedRetention(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=ExtendedRetentionPermission.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFExtendedRetention(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "VacationNotice")
    public ModelAndView VacationNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        empQuarterAllotDAO.VacationNotice(qbean);
        mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + Octype, "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "GeneratePDFVacationNotice")
    public void GeneratePDFVacationNotice(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=VacationNotice.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFVacationNotice(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "VacationStatus")
    public ModelAndView VacationStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("distlist", distlist);
        int orderNo = empQuarterAllotDAO.maxRentOrderNo();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        formattedDate = formattedDate.toUpperCase();

        String orderNoString = String.valueOf(orderNo);
        orderNoString = StringUtils.leftPad(orderNoString, 8, "0");
        // orderNoString = "VS-" + orderNoString;
        mav.addObject("orderNo", orderNoString);
        mav.addObject("orderdate", formattedDate);

        qbean = empQuarterAllotDAO.getextentionRequestDetails(empId, cno);
        mav.addObject("empDetails", qbean);

        mav.setViewName("/wrrreport/VacationStatus");
        return mav;

    }

    @RequestMapping(value = "SaveVacationStatus")
    public ModelAndView SaveVacationStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        empQuarterAllotDAO.SaveVacationStatus(qbean);
        String octype = qbean.getOccupationTypes();
        mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + octype, "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "GeneratePDFIntimation")
    public void GeneratePDFIntimation(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=IntimationLetter.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFIntimation(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GeneratePDFOPP")
    public void GeneratePDFOPP(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=Opprequisition.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFOPP(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "EvictionNotice")
    public ModelAndView EvictionNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        empQuarterAllotDAO.EvictionNotice(qbean);
        mav = new ModelAndView("redirect:/OppRequisitionList.htm", "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "GeneratePDFEvictionNotice")
    public void GeneratePDFEvictionNotice(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo, @RequestParam("caseId") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=EvictionNotice.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.GeneratePDFEvictionNotice(document, empId, consumerNo, caseId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "QuarterDetailsList", method = RequestMethod.GET)
    public ModelAndView QuarterDetailsList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        List li = empQuarterAllotDAO.getQuarterDetails(lub.getLoginempid());
        mav.addObject("quarterList", li);
        mav.setViewName("/wrrreport/QuarterDetailsList");
        return mav;

        // return "/wrrreport/QuarterDetailsList";
    }

    @RequestMapping(value = "UploadSplQrtCase")
    public ModelAndView UploadSplQrtCase(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        int trackingId = qbean.getTrackingId();
        ModelAndView mv = new ModelAndView("/wrrreport/UploadSplQrtCase", "EmpQuarterBean", qbean);
        mv.addObject("consumerNo", cno);
        mv.addObject("empId", empId);
        mv.addObject("trackingId", trackingId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "saveSplCaseRetention")
    public void saveSplCaseRetention(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.saveSplCaseRetention(qbean);
        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "downloadSplQrtCase")
    public void downloadSplQrtCase(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        qbean = empQuarterAllotDAO.getAttachedFileforSPlCase(qbean.getTrackingId(), qbean.getEmpId());
        response.setContentType(qbean.getContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + qbean.getOriginalFilename());
        OutputStream out = response.getOutputStream();
        out.write(qbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "SplCaseDetailList", method = RequestMethod.GET)
    public ModelAndView SplCaseDetailList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        List li = empQuarterAllotDAO.getSplCaseDetailList();
        mav.addObject("quarterList", li);
        mav.setViewName("/wrrreport/SplCaseDetailList");
        return mav;
    }

    @RequestMapping(value = "UpdateSplCaseStatus")
    public ModelAndView UpdateSplCaseStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        empQuarterAllotDAO.UpdateSplCaseStatus(qbean);
        mav = new ModelAndView("redirect:/SplCaseDetailList.htm", "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "ExtensionRetention")
    @ResponseBody
    public ModelAndView ExtensionRetention(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        int trackingId = qbean.getTrackingId();
        String cno = qbean.getConsumerNo();
        // System.out.println("consumerNo="+qbean.getConsumerNo());
        String empId = qbean.getEmpId();

        String rtype = "";
        String rsubtype = "";

        mav.addObject("consumerNo", cno);
        mav.addObject("trackingId", trackingId);
        mav.addObject("empId", empId);

        int orderNo = empQuarterAllotDAO.maxRentOrderNo();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        formattedDate = formattedDate.toUpperCase();

        String orderNoString = String.valueOf(orderNo);
        orderNoString = StringUtils.leftPad(orderNoString, 8, "0");
        orderNoString = "ERP-" + orderNoString;
        mav.addObject("orderNo", orderNoString);
        mav.addObject("orderdate", formattedDate);
        qbean = empQuarterAllotDAO.getextentionRequestDetails(empId, cno);
        mav.addObject("empDetails", qbean);

        mav.setViewName("/wrrreport/ExtensionRetention");
        return mav;
    }

    @RequestMapping(value = "SaveExtensionRetentionData")
    public ModelAndView SaveExtensionRetentionData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        empQuarterAllotDAO.SaveExtensionRetentionData(qbean);
        mav = new ModelAndView("redirect:/SplCaseDetailList.htm", "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "OppRequisitionList")
    public ModelAndView OppRequisitionList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        List quarterList = empQuarterAllotDAO.getOppRequisitionList();
        // System.out.println("quarterList="+quarterList.size());
        mav.addObject("quarterList", quarterList);
        mav.setViewName("/wrrreport/OppRequisitionList");
        return mav;
    }

    @RequestMapping(value = "quarterStatusReport", method = {RequestMethod.GET})
    public ModelAndView quarterStatusReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();

        String loginName = lub.getLoginname().replace(" R&B", "");
        //System.out.println("Login=" + loginName);
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitList(loginName));
        mav.setViewName("/wrrreport/quarterStatusReport");
        return mav;
    }

    @RequestMapping(value = "quarterStatusReport", method = {RequestMethod.POST})
    public ModelAndView searchquarterStatusReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        String loginName = lub.getLoginname().replace(" R&B", "");
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitList(loginName));
        mav.addObject("unitAreaList", empQuarterAllotDAO.getUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        mav.addObject("quarterList", quarterList);
        mav.setViewName("/wrrreport/quarterStatusReport");
        return mav;
    }

    @RequestMapping(value = "viewQrtDetails", method = {RequestMethod.GET})
    public ModelAndView viewQrtDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("nocStatus", empQuarterBean.getNocStatus());
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQuarterDetail(empQuarterBean.getQaId());
        mav.addObject("quarterBeandetail", quarterBeandetail);
        mav.setViewName("/wrrreport/viewQrtDetails");
        return mav;
    }

    @RequestMapping(value = "addoccupationQrtDetails", method = {RequestMethod.GET})
    public ModelAndView addoccupationQrtDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("nocStatus", empQuarterBean.getNocStatus());
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQuarterDetail(empQuarterBean.getQaId());
        mav.addObject("quarterBeandetail", quarterBeandetail);
        mav.setViewName("/wrrreport/addoccupationQrtDetails");
        return mav;
    }

    @RequestMapping(value = "addvacationQrtDetails", method = {RequestMethod.GET})
    public ModelAndView addvacationQrtDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("nocStatus", empQuarterBean.getNocStatus());
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQuarterDetail(empQuarterBean.getQaId());
        mav.addObject("quarterBeandetail", quarterBeandetail);
        mav.setViewName("/wrrreport/addvacationQrtDetails");
        return mav;
    }

    @RequestMapping(value = "saveOccupationVacationQrtDetails")
    public ModelAndView saveOccupationVacationQrtDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        empQuarterAllotDAO.saveOccupationVacationQrtDetails(qbean, lub.getLoginusername());
        String vstatus = qbean.getVacateStatus();
        mav = new ModelAndView("redirect:/occupationVacationList.htm?vacateStatus=" + vstatus, "EmpQuarterBean", qbean);
        return mav;

    }

    /*   @RequestMapping(value = "occupationVacationList")
     public ModelAndView occupationVacationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
     ModelAndView mav = new ModelAndView();
     String vstatus = empQuarterBean.getVacateStatus();
     String loginName = "";

     try {
     if (lub.getLoginname() != null && !lub.getLoginname().equals("")) {
     //loginName = lub.getLoginname().replace(" R&B", "");
     loginName = lub.getLoginusername();
     }
     Calendar c = Calendar.getInstance();
     c.set(Calendar.DAY_OF_MONTH, 1);
     String dfrom = "";
     String dto = "";
     if (empQuarterBean.getTxtperiodFrom() != null && !empQuarterBean.getTxtperiodFrom().equals("")) {
     Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodFrom());
     empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
     } else {
     empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
     // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
     }
     // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
     if (empQuarterBean.getTxtperiodTo() != null && !empQuarterBean.getTxtperiodTo().equals("")) {
     Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodTo());
     empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
     } else {
     c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
     empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

     }

     //System.out.println(",From Date: "+empQuarterBean.getTxtperiodFrom());
     mav = new ModelAndView("/wrrreport/occupationVacationList", "empQuarterBean", empQuarterBean);
     //System.out.println("empQuarterBean.getTxtperiodFrom()==="+empQuarterBean.getTxtperiodFrom());
     //    System.out.println("empQuarterBean.getTxtperiodTo()==="+empQuarterBean.getTxtperiodTo());
     List quarterList = empQuarterAllotDAO.getoccupationVacationList(vstatus, loginName, empQuarterBean.getTxtperiodFrom(), empQuarterBean.getTxtperiodTo());
     mav.addObject("quarterList", quarterList);
     mav.addObject("vstatus", vstatus);
     //mav.addObject("empQuarterBean", empQuarterBean);

     } catch (Exception e) {

     }
     return mav;
     }
     */
    @RequestMapping(value = "occupationVacationList")
    public ModelAndView occupationVacationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        String vstatus = empQuarterBean.getVacateStatus();
        String loginName = "";

        try {
            if (lub.getLoginname() != null && !lub.getLoginname().equals("")) {
                //loginName = lub.getLoginname().replace(" R&B", "");
                loginName = lub.getLoginusername();
            }
            Date fdate = null;
            Date tdate = null;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            String dfrom = "";
            String dto = "";
            if (empQuarterBean.getTxtperiodFrom() != null && !empQuarterBean.getTxtperiodFrom().equals("")) {
                fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodFrom());
                empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, 1);
                fdate = c.getTime();
                empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }

            if (empQuarterBean.getTxtperiodTo() != null && !empQuarterBean.getTxtperiodTo().equals("")) {
                tdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodTo());
                empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(tdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                tdate = c.getTime();
                empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }

            //System.out.println(",From Date: "+empQuarterBean.getTxtperiodFrom());
            mav = new ModelAndView("/wrrreport/occupationVacationList", "empQuarterBean", empQuarterBean);

            List quarterList = empQuarterAllotDAO.getoccupationVacationListqms(vstatus, loginName, fdate, tdate);
            mav.addObject("quarterList", quarterList);
            mav.addObject("vstatus", vstatus);
            //mav.addObject("empQuarterBean", empQuarterBean);

        } catch (Exception e) {

        }
        return mav;
    }

    @RequestMapping(value = "QrtMessageInbox")
    public ModelAndView QrtMessageInbox(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        int trackingId = qbean.getTrackingId();
        ModelAndView mv = new ModelAndView("/wrrreport/QrtMessageInbox", "EmpQuarterBean", qbean);
        mv.addObject("consumerNo", cno);
        mv.addObject("empId", empId);
        mv.addObject("trackingId", trackingId);
        return mv;
    }

    @RequestMapping(value = "DownloadAutoQtrRetention")
    @ResponseBody
    public ModelAndView DownloadAutoQtrRetention(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        // System.out.println("consumerNo="+qbean.getConsumerNo());
        String empId = qbean.getEmpId();

        String rtype = "";
        String rsubtype = "";

        if (Octype.equals("6") && Octype != null) {
            rtype = "Dismissed Case";
            rsubtype = "";

        }
        if (Octype.equals("5") && Octype != null) {
            rtype = "Death Case";
            rsubtype = "";

        }
        if (Octype.equals("4") && Octype != null) {
            rtype = "Tracking of Retirement Cases";
            rsubtype = "";

        }
        if (Octype.equals("3") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO Non-KBK Area)";

        }
        if (Octype.equals("2") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO KBK Area)";

        }
        if (Octype.equals("1") && Octype != null) {
            rtype = "Tracking of Transfer Cases";
            rsubtype = "(TO Cuttack/Bhubaneswar Area)";

        }

        mav.addObject("consumerNo", cno);
        mav.addObject("rsubtype", rsubtype);
        mav.addObject("rtype", rtype);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);

        String retStatus = empQuarterAllotDAO.retentionStatus(cno, empId);
        if (retStatus.equals("N")) {
            int orderNo = empQuarterAllotDAO.maxRentOrderNo();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            String formattedDate = myDateObj.format(myFormatObj);
            formattedDate = formattedDate.toUpperCase();
            String orderNoString = String.valueOf(orderNo);
            orderNoString = StringUtils.leftPad(orderNoString, 8, "0");
            mav.addObject("orderNo", orderNoString);
            mav.addObject("orderdate", formattedDate);
            qbean.setEmpId(empId);
            qbean.setOccupationTypes(Octype);
            qbean.setRetentionType(rtype);
            qbean.setRetentionSubType(rsubtype);
            qbean.setOrderNumber(orderNoString);
            qbean.setOrderDate(formattedDate);
            qbean.setConsumerNo(cno);
            empQuarterAllotDAO.SaveAutoRetentionData(qbean);

        }
        mav = new ModelAndView("redirect:/GeneratePDFRetention.htm?consumerNo=" + cno + "&occupationTypes=" + Octype + "&empId=" + empId, "EmpQuarterBean", qbean);
        return mav;
    }

    @RequestMapping(value = "notVacatedQrtList")
    public ModelAndView notVacatedQrtList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            if (empQuarterBean.getTxtperiodFrom() != null && !empQuarterBean.getTxtperiodFrom().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodFrom());
                empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                empQuarterBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
                // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }
            // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
            if (empQuarterBean.getTxtperiodTo() != null && !empQuarterBean.getTxtperiodTo().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empQuarterBean.getTxtperiodTo());
                empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                empQuarterBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }
            mav = new ModelAndView("/wrrreport/notVacatedQrtList", "empQuarterBean", empQuarterBean);
            List quarterList = empQuarterAllotDAO.getNotVacatedQrtList(empQuarterBean.getTxtperiodFrom(), empQuarterBean.getTxtperiodTo());

            mav.addObject("quarterList", quarterList);
            // mav.setViewName("/wrrreport/notVacatedQrtList");
        } catch (Exception e) {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "addnocforRequest.htm")
    public void addnocforRequest(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        qbean.setNocfor(qbean.getNocfor());
        qbean.setTvd(qbean.getTvd());
        qbean.setOffcode(lub.getLoginoffcode());
        empQuarterAllotDAO.savenocRequest(qbean, lub.getLoginempid(), lub.getLoginspc());
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "gedNocList")
    public ModelAndView gedNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {

        ModelAndView mv = new ModelAndView("/wrrreport/qrtNocList", "EmpQuarterBean", qbean);
        return mv;
    }

    @RequestMapping(value = "PhdNocList")
    public ModelAndView PhdNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mv = new ModelAndView("/wrrreport/qrtNocList", "EmpQuarterBean", qbean);
        return mv;
    }

    @RequestMapping(value = "QrtWaterClearanceUpload")
    public ModelAndView QrtWaterClearanceUpload(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mv = new ModelAndView("/wrrreport/QrtWaterClearanceUpload", "EmpQuarterBean", qbean);
        EmpQuarterBean quarterBeandetail = empQuarterAllotDAO.getQrtNocUpload(qbean.getEmpId(), qbean.getConsumerNo());
        mv.addObject("quarterBeandetail", quarterBeandetail);
        mv.addObject("nocId", qbean.getNocId());
        mv.addObject("nocType", qbean.getNocType());
        mv.addObject("empId", qbean.getEmpId());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "saveuploadNOC", method = RequestMethod.POST)
    public void saveuploadNOC(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.saveuploadNOC(qbean);
        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "downloadExtensionRequest")
    public void downloadExtensionRequest(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=Extension_Request.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.downloadExtensionRequest(document, empId, consumerNo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "RBNocList")
    public ModelAndView RBNocList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {

        ModelAndView mv = new ModelAndView("/wrrreport/RBNocList", "EmpQuarterBean", qbean);

        return mv;
    }

    @RequestMapping(value = "notVacationStatus")
    public ModelAndView notVacationStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        int orderNo = empQuarterAllotDAO.maxRentOrderNo();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        formattedDate = formattedDate.toUpperCase();

        String orderNoString = String.valueOf(orderNo);
        orderNoString = StringUtils.leftPad(orderNoString, 8, "0");

        mav.addObject("orderNo", orderNoString);
        mav.addObject("orderdate", formattedDate);
        mav.setViewName("/wrrreport/notVacationStatus");
        return mav;

    }

    @RequestMapping(value = "DocumentSubmissionRequest")
    public ModelAndView DocumentSubmissionRequest(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        empQuarterAllotDAO.DocumentSubmissionRequest(qbean);
        mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + Octype, "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "UploadsubmissionDocument")
    public ModelAndView UploadsubmissionDocument(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        int trackingId = qbean.getTrackingId();
        ModelAndView mv = new ModelAndView("/wrrreport/UploadsubmissionDocument", "EmpQuarterBean", qbean);
        mv.addObject("consumerNo", cno);
        mv.addObject("occupationTypes", qbean.getOccupationTypes());
        mv.addObject("empId", empId);
        mv.addObject("trackingId", trackingId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "savedocumentsubmission")
    public void savedocumentsubmission(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.savedocumentsubmission(qbean);
        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "downloadsubmissionDocument")
    public void downloadsubmissionDocument(@ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String fname = qbean.getEmpId() + "_document.pdf";
        qbean = empQuarterAllotDAO.downloadsubmissionDocument(qbean.getEmpId(), qbean.getConsumerNo(), qbean.getOppCaseId(), qbean.getDsStatus());
        response.setContentType(qbean.getGetphdContentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + fname);

        OutputStream out = response.getOutputStream();
        out.write(qbean.getFilecontent());
        out.flush();
    }

    @RequestMapping(value = "DocumentSubmissionList", method = RequestMethod.GET)
    public ModelAndView DocumentSubmissionList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String otype = "1";
        String textMessage = "CTC/BBSR Commute Permission List";
        List li = empQuarterAllotDAO.getDocumentSubmissionList(otype);
        mav.addObject("textMessage", textMessage);
        mav.addObject("quarterList", li);
        mav.setViewName("/wrrreport/DocumentSubmissionList");
        return mav;
    }

    @RequestMapping(value = "DocumentSubmissionPensionList", method = RequestMethod.GET)
    public ModelAndView DocumentSubmissionPensionList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String otype = "4";
        String textMessage = "Gratuity held up undertaking List";
        mav.addObject("textMessage", textMessage);
        List li = empQuarterAllotDAO.getDocumentSubmissionList(otype);
        mav.addObject("quarterList", li);
        mav.setViewName("/wrrreport/DocumentSubmissionList");
        return mav;
    }

    @RequestMapping(value = "ledgerinfo", method = RequestMethod.GET)
    public ModelAndView ledgerinfo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("status", "Hide");
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitAreaList());
        mav.setViewName("/wrrreport/ledgerinfo");
        return mav;
    }

    @RequestMapping(value = "ledgerinfo", method = RequestMethod.POST)
    public ModelAndView searchledgerinfo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getEquarterUnitList());
        mav.addObject("unitAreaList", empQuarterAllotDAO.getEquarterUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        mav.addObject("qnoList", empQuarterAllotDAO.getbuildingDetails(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype()));
        //  List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        List quarterList = empQuarterAllotDAO.getLedgerEmployee(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype(), empQuarterBean.getQuarterNo());
        mav.addObject("quarterList", quarterList);
        mav.addObject("status", "Show");
        mav.setViewName("/wrrreport/ledgerinfo");
        return mav;
    }

    @RequestMapping(value = "viewQuarterDetails", method = RequestMethod.GET)
    public ModelAndView viewQuarterDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("status", "Hide");
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getQuarterUnitListRentOff());
        mav.setViewName("/wrrreport/viewQuarterDetails");
        return mav;
    }

    //Start Here 13/04/2023
    @RequestMapping(value = "viewQuarterDetails", method = RequestMethod.POST)
    public ModelAndView searchQuarterDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("quarterUnitAreaList", empQuarterAllotDAO.getEquarterUnitList());
        mav.addObject("unitAreaList", empQuarterAllotDAO.getEquarterUnitAreawiseQuarterType(empQuarterBean.getQrtrunit()));
        mav.addObject("qnoList", empQuarterAllotDAO.getbuildingDetails(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype()));
        //  List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        List quarterList = empQuarterAllotDAO.getQuarterData(empQuarterBean.getQrtrunit(), empQuarterBean.getQrtrtype());
        mav.addObject("quarterList", quarterList);
        mav.addObject("status", "Show");
        mav.setViewName("/wrrreport/viewQuarterDetails");
        return mav;
    }

    @RequestMapping(value = "qtrtypeWiseBuildingDataJson")
    @ResponseBody
    public void qtrtypeWiseBuildingDataJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String quarterunitarea = parameters.get("quarterunitarea");
            String qrtrtype = parameters.get("qrtrtype");

            List unitAreaList = empQuarterAllotDAO.getbuildingDetails(quarterunitarea, qrtrtype);
            JSONArray arr = new JSONArray(unitAreaList);
            JSONObject jobj = new JSONObject();
            jobj.put("unitAreaList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "LedgerMonthWiseReport")
    public ModelAndView LedgerMonthWiseReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
        ModelAndView mav = new ModelAndView();
        String occId = empQuarterBean.getOccuId();
        empQuarterBean = empQuarterAllotDAO.getLedgeremployeeDetails(occId);
        mav.addObject("empDetails", empQuarterBean);
        List empData = empQuarterAllotDAO.getLedgerDataEmployeewise(occId);
        mav.addObject("empData", empData);
        mav.setViewName("/wrrreport/monthwiseledgerinfo");
        return mav;
    }

    @RequestMapping(value = "downloadNoticeOPP", method = {RequestMethod.GET})
    public void downloadNoticeOPP(HttpServletResponse response, @RequestParam("oppCaseId") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=showcauseNotice.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.downloadNoticeOPP(document, caseId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "uploadshowcauseReply")
    public ModelAndView uploadshowcauseReply(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        int oppCaseId = qbean.getOppCaseId();
        ModelAndView mv = new ModelAndView("/wrrreport/uploadshowcauseReply", "EmpQuarterBean", qbean);
        mv.addObject("consumerNo", cno);
        mv.addObject("empId", empId);
        mv.addObject("oppCaseId", oppCaseId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "saveshowcausereply")
    public void saveshowcausereply(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.savesaveshowcausereply(qbean);
        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "UpdateOPPSendStatus")
    public ModelAndView UpdateOPPSendStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        empQuarterAllotDAO.UpdateOPPSendStatus(qbean);
        mav = new ModelAndView("redirect:/notVacatedQrtList.htm", "EmpQuarterBean", qbean);
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "searchOccupationEmp")
    public void searchOccupationEmp(HttpServletResponse response, @RequestParam("criteria") String criteria, @RequestParam("searchstring") String searchstring) throws JSONException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        SearchEmployee empinfo = new SearchEmployee();
        empinfo.setCriteria(criteria);
        empinfo.setSearchString(searchstring);
        EmployeeSearchResult empresult = employeeDAO.LocateEmployee(empinfo);

        JSONObject jobj = new JSONObject(empresult);
        out.write(jobj.toString());
        out.flush();
    }

    @RequestMapping(value = "DownloadFiveOneOrder", method = {RequestMethod.GET})
    public void DownloadFiveOneOrder(HttpServletResponse response, @RequestParam("orderFiveOne") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=FiveOneOrder.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.DownloadFiveOneOrder(document, writer, caseId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "DownloadFiveOneNotice", method = {RequestMethod.GET})
    public void DownloadFiveOneNotice(HttpServletResponse response, @RequestParam("orderFiveOne") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=FiveOneNotice.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.DownloadFiveOneNotice(document, caseId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "Testsms")
    public ModelAndView Testsms(HttpServletResponse response, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        empQuarterAllotDAO.Testsms(qbean);
        mav.setViewName("/wrrreport/Testsms");
        // mav.setViewName("redirect:Testsms.htm");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "saveWateruploadClearance", method = RequestMethod.POST)
    public void saveWateruploadClearance(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        // System.out.println("hi");
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.saveWateruploadClearance(qbean);

        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "uploadAppeal")
    public ModelAndView uploadAppeal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        int oppCaseId = qbean.getOppCaseId();
        ModelAndView mv = new ModelAndView("/wrrreport/uploadAppeal", "EmpQuarterBean", qbean);
        mv.addObject("consumerNo", cno);
        mv.addObject("empId", empId);
        mv.addObject("oppCaseId", oppCaseId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "saveappeal")
    public void saveappeal(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = null;
        empQuarterAllotDAO.saveappeal(qbean);
        out = response.getWriter();
        out.write("success");
    }

    @RequestMapping(value = "DownloadAppealNotice", method = {RequestMethod.GET})
    public void DownloadAppealNotice(HttpServletResponse response, @RequestParam("orderFiveOne") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=AppealNotice.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            empQuarterAllotDAO.DownloadAppealNotice(document, caseId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "HideRecord")
    public ModelAndView HideRecord(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        empQuarterAllotDAO.HideRecord(qbean);
        mav = new ModelAndView("redirect:/searchtransferCategory.htm?occupationTypes=" + Octype, "EmpQuarterBean", qbean);
        return mav;

    }

}

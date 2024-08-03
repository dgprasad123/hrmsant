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
import hrms.dao.EmpQuarterAllotment.QMSDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import hrms.model.login.LoginUserBean;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@SessionAttributes("LoginUserBean")
@Controller
public class QMSController {

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

    @Autowired
    QMSDAO qmsDAO;

    @RequestMapping(value = "OverdueVacateDate", method = RequestMethod.GET)
    public ModelAndView OverdueVacateDate(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String textMessage = "Retention Period Exceeds List";
        mav.addObject("textMessage", textMessage);
        List li = qmsDAO.OverdueVacateDate();
        mav.addObject("quarterList", li);
        mav.setViewName("/qms/OverdueVacateDate");
        return mav;
    }

    @RequestMapping(value = "RetentionOverDueNotice")
    public ModelAndView RetentionOverDueNotice(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        String Octype = qbean.getOccupationTypes();
        String cno = qbean.getConsumerNo();
        String empId = qbean.getEmpId();
        mav.addObject("consumerNo", cno);
        mav.addObject("occupationTypes", Octype);
        mav.addObject("empId", empId);
        qmsDAO.RetentionOverDueNotice(qbean);
        mav = new ModelAndView("redirect:/OverdueVacateDate.htm", "EmpQuarterBean", qbean);
        return mav;

    }

    @RequestMapping(value = "OrderFiveTwoIssued", method = RequestMethod.GET)
    public ModelAndView OrderFiveTwoIssued(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String textMessage = "Order 5(2) Issued List";
        mav.addObject("textMessage", textMessage);
        List li = qmsDAO.OrderFiveTwoIssued();
        mav.addObject("quarterList", li);
        mav.setViewName("/qms/OrderFiveTwoIssued");
        return mav;
    }

    @RequestMapping(value = "DownloadFiveTwoOrder", method = {RequestMethod.GET})
    public void DownloadFiveTwoOrder(HttpServletResponse response, @RequestParam("caseId") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=FiveOneNotice.pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            qmsDAO.DownloadFiveTwoOrder(document, caseId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "viewPDFEvictionNotice")
    public void viewPDFEvictionNotice(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("consumerNo") String consumerNo, @RequestParam("caseId") int caseId) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            response.setHeader("Content-Disposition", "attachment; filename=EvictionNotice.pdf");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            qmsDAO.viewPDFEvictionNotice(document, empId, consumerNo, caseId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "downloadVacationOccupationExcel")
    public void downloadApplicantExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        String loginName = "";

        try {
            if (lub.getLoginname() != null && !lub.getLoginname().equals("")) {
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
            String vstatus = empQuarterBean.getVacateStatus();
            String fname = "";
            if (empQuarterBean.getVacateStatus() != null && empQuarterBean.getVacateStatus().equals("Vacation")) {
                fname = "Vacation_Qrts_list.xls";

            }
            if (empQuarterBean.getVacateStatus() != null && empQuarterBean.getVacateStatus().equals("Occupation")) {
                fname = "Ocuupation_Qrts_list.xls";

            }
            String fileName = fname;
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);

            qmsDAO.downloadVacationOccupationExcel(out, fileName, workbook, vstatus, loginName, empQuarterBean.getTxtperiodFrom(), empQuarterBean.getTxtperiodTo());

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /* @RequestMapping(value = "occupationVacationPrint")
     public ModelAndView occupationVacationPrint(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
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
     mav = new ModelAndView("/qms/occupationVacationPrint", "empQuarterBean", empQuarterBean);

     List quarterList = empQuarterAllotDAO.getoccupationVacationList(vstatus, loginName, empQuarterBean.getTxtperiodFrom(), empQuarterBean.getTxtperiodTo());
     mav.addObject("quarterList", quarterList);
     mav.addObject("vstatus", vstatus);
     //mav.addObject("empQuarterBean", empQuarterBean);

     } catch (Exception e) {

     }
     return mav;
     }
     */
    @RequestMapping(value = "occupationVacationPrint")
    public ModelAndView occupationVacationPrint(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empQuarterBean") EmpQuarterBean empQuarterBean) {
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
            mav = new ModelAndView("/qms/occupationVacationPrint", "empQuarterBean", empQuarterBean);

            List quarterList = empQuarterAllotDAO.getoccupationVacationListqms(vstatus, loginName, fdate, tdate);
            mav.addObject("quarterList", quarterList);
            mav.addObject("vstatus", vstatus);
            //mav.addObject("empQuarterBean", empQuarterBean);

        } catch (Exception e) {

        }
        return mav;
    }

    @RequestMapping(value = "DropOPPCaseList", method = RequestMethod.GET)
    public ModelAndView DropOPPCaseList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        String textMessage = "Dropped OPP Case List";
        mav.addObject("textMessage", textMessage);
        List li = qmsDAO.DropOPPCaseList();
        mav.addObject("quarterList", li);
        mav.setViewName("/qms/DropOPPCaseList");
        return mav;
    }

    @RequestMapping(value = "VacateOPPqrt")
    public ModelAndView VacateOPPqrt(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("distlist", distlist);
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
        mav.setViewName("/qms/VacateOPPqrt");
        return mav;

    }

    @RequestMapping(value = "SaveOPPVacationStatus")
    public ModelAndView SaveOPPVacationStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmpQuarterBean") EmpQuarterBean qbean) {
        ModelAndView mav = new ModelAndView();

        qmsDAO.SaveOPPVacationStatus(qbean);
        String octype = qbean.getOccupationTypes();
        mav = new ModelAndView("redirect:/notVacatedQrtList.htm", "EmpQuarterBean", qbean);
        return mav;

    }

}

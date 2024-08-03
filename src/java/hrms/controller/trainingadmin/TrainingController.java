/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.trainingadmin;

import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.Message;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.InstituteDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.trainingadmin.TrainingCalendarDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.trainingadmin.EmpAttendanceBean;
import hrms.model.trainingadmin.EmployeeSearch;
import hrms.model.trainingadmin.InstituteForm;
import hrms.model.trainingadmin.NISGTrainingBean;
import hrms.model.trainingadmin.OnlineTrainingBean;
import hrms.model.trainingadmin.TrainingBatchBean;
import hrms.model.trainingadmin.TrainingDocumentBean;
import hrms.model.trainingadmin.TrainingFacultyForm;
import hrms.model.trainingadmin.TrainingProgramForm;
import hrms.model.trainingadmin.TrainingProgramList;
import hrms.model.trainingadmin.TrainingSponsorForm;
import hrms.model.trainingadmin.TrainingVenueForm;
import hrms.model.trainingadmin.VirtualTrainingBean;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes("LoginUserBean")
public class TrainingController implements ServletContextAware {

    @Autowired
    public TrainingCalendarDAO trainingCalendarDao;
    @Autowired
    public InstituteDAO instituteDAO;

    @Autowired
    public LoginDAO loginDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "TrainingController", method = {RequestMethod.POST, RequestMethod.GET})
    public String viewLeaveList() {

        String trainingValue = "/trainingadmin/TrainingCalendar";
        return trainingValue;
    }

    @RequestMapping(value = "AssignCadre", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageAssignCadre(@RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("trainingId", trainingId);
        String path = "/trainingadmin/AssignCadre";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TrainingProgramController", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewTrainingProgram(@RequestParam("date") String date, @RequestParam("opt") String opt, @ModelAttribute("TrainingProgramForm") TrainingProgramForm trainingPrograms, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("TrainingProgramForm", trainingPrograms);
        mav.addObject("programDate", date);
        mav.addObject("fromDate", date);
        mav.addObject("toDate", date);
        mav.addObject("opt", opt);
        int venueId = trainingCalendarDao.getDefaultVenue(lub.getLoginempid());
        mav.addObject("venueId", "" + venueId);
        String path = "/trainingadmin/TrainingProgram";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "EditTrainingProgramAction", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView editTrainingProgram(@RequestParam("trainingId") int trainingId, @RequestParam("opt") String opt) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("opt", opt);
        mav.addObject("trainingId", trainingId);
        TrainingProgramForm tpl = null;
        //Call the DAO

        tpl = trainingCalendarDao.getTrainingDetail(trainingId);
        tpl.setTrainingId(trainingId);
        mav.addObject("TrainingProgramForm", tpl);
        mav.addObject("fromDate", tpl.getFromDate());

        mav.addObject("toDate", tpl.getToDate());
        mav.addObject("venueId", tpl.getVenueId());
        mav.addObject("strFile", tpl.getStrFile());
        String path = "/trainingadmin/TrainingProgram";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TrainingProgramAction", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addTrainingProgram(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TrainingProgramForm") TrainingProgramForm trainingPrograms) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("TrainingProgramForm", trainingPrograms);
        mav.addObject("opt", trainingPrograms.getOpt());
        mav.addObject("fromDate", trainingPrograms.getProgramDate());
        mav.addObject("toDate", trainingPrograms.getProgramDate());
        int trainingId = trainingPrograms.getTrainingId();

        String path = null;
        String filePath = context.getInitParameter("TrainingDocumentPath");
        if (trainingId > 0) {

            mav.addObject("date", trainingPrograms.getProgramDate());
            mav.addObject("status", "success");
            trainingPrograms.setTrainingId(trainingId);
            trainingCalendarDao.updateTrainingProgram(trainingPrograms, lub.getLoginempid(), filePath);
            path = "/trainingadmin/ManageTrainingPrograms";
        } else {
            mav.addObject("status", "success");
            mav.addObject("venueId", "1");
            trainingCalendarDao.SaveTrainingProgram(trainingPrograms, lub.getLoginempid(), filePath);
            path = "/trainingadmin/TrainingProgram";
        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "ManageTrainingProgramAction", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageTrainingProgram(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("date") String date) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("hrms_id", lub.getLoginempid());
        mav.addObject("date", date);
        String path = "/trainingadmin/ManageTrainingPrograms";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "viewCalendar", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewCalendar(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String ownerID = lub.getLoginempid();
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        String cmonth = CalendarCommonMethods.getFullNameMonthAsString(month);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean ifSunNotHoliday = false;
        boolean ifSecondSatNotHoliday = false;
        Calendar cl = Calendar.getInstance();
        cl.set(year, month, 1);
        cl.add(Calendar.MONTH, -1);
        int previousMonth = cl.get(Calendar.MONTH);
        int previousYear = cl.get(Calendar.YEAR);
        cl.add(Calendar.MONTH, +2);
        int nextMonth = cl.get(Calendar.MONTH);
        int nextYear = cl.get(Calendar.YEAR);
        int holidayList[] = trainingCalendarDao.getGovtHolidaysList(month, year);
        String strMonth = (month + 1) + "";
        if (strMonth.length() == 1) {
            strMonth = "0" + strMonth;
        }
        StringBuffer content
                = new StringBuffer("<table border='0' width='100%'><tr><td width=\"35%\"><a href='javascript: void(0)' onclick='filterCalendar(" + previousMonth + ", " + previousYear + ")' style='color:#15589C;font-size:11pt;text-decoration:none;font-family:Arial;'>&laquo; Previous Month</a></td><td width=\"30%\"><div id=\"month_wrap\" style=\"position:relative;font-size:20px;font-family:Arial,Tahoma;font-weight: bold;margin-bottom:10px;width:200px;margin:8px auto;\">"
                        + "<a href='javascript:void(0)' style='color:#155890;text-decoration:none;float:left;width:130px;text-align:right;' onclick='javascript: displayMonths()'>" + cmonth
                        + "<div id=\"month_blk\" style=\"display:none;position:absolute;left:0px;top:25px;padding:5px;border:1px solid #CCCCCC;background:#FFFFFF;\">"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(0, 'month')\" class=\"month_name\">January</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(1, 'month')\" class=\"month_name\">February</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(2, 'month')\" class=\"month_name\">March</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(3, 'month')\" class=\"month_name\">April</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(4, 'month')\" class=\"month_name\">May</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(5, 'month')\" class=\"month_name\">June</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(6, 'month')\" class=\"month_name\">July</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(7, 'month')\" class=\"month_name\">August</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(8, 'month')\" class=\"month_name\">September</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(9, 'month')\" class=\"month_name\">October</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(10, 'month')\" class=\"month_name\">November</a>"
                        + "<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(11, 'month')\" class=\"month_name\">December</a>"
                        + "</div>"
                        + "</a> "
                        + "<a href='javascript:void(0)' style='color:#155890;text-decoration:none;float:left;width:60px;text-align:center;' onclick='javascript: displayYears()'>" + year
                        + "<div id=\"year_blk\" style=\"display:none;position:absolute;left:130px;top:25px;padding:5px;border:1px solid #CCCCCC;background:#FFFFFF;\">"
                );
        for (int i = currentYear - 2; i <= currentYear + 5; i++) {
            content.append("<a href=\"javascript:void(0)\" onclick=\"javascript: refreshCal(" + i + ", 'year')\" class=\"month_name\">" + i + "</a>");
        }
        content.append("</a></div><div style='clear:both;'></div></div></td><td width=\"35%\" align='right'><a href='javascript: void(0)' onclick='filterCalendar(" + nextMonth + ", " + nextYear + ")' style='color:#15589C;font-size:11pt;text-decoration:none;font-family:Arial;'>Next Month &raquo;</a></td></tr></table>");
        content.append("<TABLE BORDER=\"0\" width=\"100%\"  cellpadding='4' cellspacing='1' id='gCalendar' class=\"entbox\" bgcolor=\"#DADADA\" style=\"font-family:Arial,Tahoma,Verdana;font-size:10pt;\" align='center'>");

        // field for month and year display at top of calendar
        //        content.append("<TR><TH COLSPAN=7 id='calHead' nowrap=\"nowrap\">"+getMonthAsString(month)+" - "+year +" </TH></TR>");
        // days of the week at head of each column
        content.append("<TR style='height:20px;background:#EAEAEA;' class='weekdays'> <TH width=\"14%\">Sun</TH> <TH width=\"14%\">Mon</TH> <TH width=\"14%\">Tue</TH> <TH width=\"14%\">Wed</TH>");
        content.append(" <TH width=\"14%\">Thu</TH> <TH width=\"14%\">Fri</TH> <TH width=\"14%\">Sat</TH>");
        content.append("<TR style='height:0px;background:#FFFFFF;'>");

        try {
            String[] day = CalendarCommonMethods.populateFields(year, month);
            boolean flagEnd = false;
            // layout 6 rows of fields for worst-case month
            //Date currentDate=new Date();
            Calendar currentDate = Calendar.getInstance();
            //int currentYear=currentDate.getYear()+1900;

            int ctr = 0;
            String strDay = null;
            for (int i = 0; i < 42; i++) {
                ctr++;
                strDay = day[i];
                if (day[i].length() == 1) {
                    strDay = "0" + day[i];
                }
                if (day[i] == "") {
                    if (flagEnd == false) {
                        content.append("<TD style='text-align:center;background:#FAFAFA;'>" + "&nbsp;" + "</TD>");
                    }
                } else {
                    String strDate = year + "-" + strMonth + "-" + day[i];
                    String[] strArray = {"Jan", "Feb", "Mar", "Arp", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                    String trainingProgramName = trainingCalendarDao.getTrainingProgramList(strDate, ownerID);
                    if (trainingProgramName != null && !((i % 7 == 0) && ifSunNotHoliday == false) && !(i == 13 && ifSecondSatNotHoliday == false)) {
                        if (i % 6 == 0) {
                            content.append("<TD class=\"event_exist\"><div style='position:relative;height:100%;cursor:pointer;' class='event_title'>" + day[i] + ""
                                    + "<div class='event_popup' style='left: -60px;'>" + trainingProgramName + "</div></div>"
                                    + "</TD>");
                        } else {
                            content.append("<TD class=\"event_exist\"><div style='position:relative;height:100%;cursor:pointer;' class='event_title'>" + day[i] + ""
                                    + "<div class='event_popup'>" + trainingProgramName + "</div></div>"
                                    + "</TD>");
                        }
                    } else {
                        if ((i % 7 == 0) && ifSunNotHoliday == false) {
                            content.append("<TD class=\"weekend\">" + day[i] + "</TD>");
                        } else if (i == 13 && ifSecondSatNotHoliday == false) {
                            content.append("<TD class=\"weekend\">" + day[i] + "</TD>");
                        } else if (trainingCalendarDao.isDatePresentInList(holidayList, Integer.parseInt(day[i]))) {
                            content.append("<TD class=\"holiday\">" + day[i] + "</TD>");
                        } else {
                            content.append("<TD class=\"thismonth\"><a href=\"javascript:void(0)\" style='display:block;width:100%;height:100%;text-decoration:none;font-size:12pt;color:#333;'>" + day[i] + "</a></TD>");
                        }
                    }

                }

                if (ctr % 7 == 0) {
                    if (day[ctr - 1] != "") {
                        if (ctr == 42) {
                            content.append("</TR>");
                        } else {
                            if (day[ctr] == "") {
                                i = 42;
                                content.append("</tr>");
                            } else {
                                content.append("</TR><TR style='height:20px;background:#FFFFFF;'>");
                            }
                        }
                    } else {
                        flagEnd = true;
                    }
                }

            }

            content.append("</TABLE>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "getSponsorListAction", method = RequestMethod.POST)
    public void getSelectboxSponsorList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = trainingCalendarDao.getSelectboxSponsorList(lub.getLoginempid());
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "BrowsePost", method = {RequestMethod.POST, RequestMethod.GET})
    public String viewPostList() {

        String path = "/trainingadmin/BrowsePost";
        return path;
    }

    @RequestMapping(value = "getPostListWithNameJSON", method = RequestMethod.POST)
    public @ResponseBody
    String getPostListWithNameJSON(HttpServletResponse response, @RequestParam String offcode) {
        JSONArray json = null;
        try {
            List postlist = trainingCalendarDao.getPostListWithName(offcode);
            json = new JSONArray(postlist);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "BrowseFaculties", method = {RequestMethod.POST, RequestMethod.GET})
    public String viewFacultyList() {

        String path = "/trainingadmin/BrowseFaculties";
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "TrainingProgramListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void getTrainingPrograms(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("date") String date) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List jsonlist = new ArrayList();
            String hrmsId = lub.getLoginempid();
            if (hrmsId != null) {
                jsonlist = trainingCalendarDao.getTrainingPrograms(hrmsId, date);
            } else {
                //li = loginDAO.getProposedEmployeeList(lub.getLoginoffcode(), year, month);

            }
            json.put("total", jsonlist.size());
            json.put("rows", jsonlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "DeleteTrainingProgram", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteTrainingProgram(HttpServletResponse response,
            @RequestParam("trainingId") int trainingId, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        trainingCalendarDao.deleteTrainingProgram(trainingId, lub.getLoginempid());
    }

    @ResponseBody
    @RequestMapping(value = "GetCadreListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void getCadreList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getCadreList(lub.getLoginempid(), trainingId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AssignCadreListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void AssignCadreList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("cadreId") String cadreId, @RequestParam("status") String status, @RequestParam("date") String date, @RequestParam("trainingId") String trainingId, @RequestParam("grade") String grade) {

        trainingCalendarDao.assignCadreList(trainingId, cadreId, grade, status, lub.getLoginempid());
    }

    @RequestMapping(value = "ManageFaculties", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageFaculties(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/ManageFaculties";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "AddTrainingFaculty", method = {RequestMethod.GET, RequestMethod.POST})
    public String addTrainingFaculty(HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("TrainingFacultyForm") TrainingFacultyForm trainingFaculty) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("TrainingFacultyForm", trainingFaculty);
        mav.addObject("opt", trainingFaculty.getOpt());
        int facultyCode = Integer.parseInt(trainingFaculty.getFacultyCode());

        String path = null;

        if (facultyCode > 0) {
            trainingFaculty.setFacultyId(facultyCode);
            trainingCalendarDao.updateTrainingFaculty(trainingFaculty, lub.getLoginempid());
            path = "redirect:/ManageFaculties.htm";
        } else {
            mav.addObject("status", "success");
            trainingCalendarDao.addNewFaculty(trainingFaculty, lub.getLoginempid());
            path = "redirect:/ManageFaculties.htm";
        }
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "TrainingListController", method = {RequestMethod.GET, RequestMethod.POST})
    public void getTrainingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getTrainingProgramList(lub.getLoginempid(), page, rows);
            String where = " WHERE owner_id = '" + lub.getLoginempid() + "' AND to_date > (current_date - interval '90' day)";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_programs", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "TrainingProgramList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GetTrainingProgramList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String mobile = trainingCalendarDao.getEmpMobile(lub.getLoginempid());
        String email = trainingCalendarDao.getEmpEmail(lub.getLoginempid());
        boolean isExist = trainingCalendarDao.isTrainingExist(lub.getLoginempid());
        // String trainingOptions = trainingCalendarDao.getEmpTrainingOptions(lub.getEmpid(), 132);
        //String[] arrOptions = trainingOptions.split(",");
        // int optionCount = arrOptions.length;
        // mav.addObject("optionCount", optionCount);
        mav.addObject("mobile", mobile);
        mav.addObject("email", email);
        mav.addObject("hasSelected", isExist);
        mav.addObject("trainingEmpId", lub.getLoginempid());
        String path = "/trainingadmin/TrainingProgramList";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TrainingCalendar", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GetTrainingCalendar() {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/TrainingCalendar";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TrainingCalendarList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GetTrainingCalendarList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/TrainingCalendarList";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "InstitutionDashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView InstitutionDashboard(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/InstitutionDashboard";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getFacultyList", method = RequestMethod.GET)
    public void getFacultyList(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = trainingCalendarDao.getFacultyList(lub.getLoginempid(), page, rows);
            JSONObject json = new JSONObject();
            String where = " WHERE owner_id = '" + lub.getLoginempid() + "'";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_faculties", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "DeleteFaculty", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteFaculty(HttpServletResponse response,
            @RequestParam("facultyCode") int facultyCode) {

        trainingCalendarDao.deleteFaculty(facultyCode);
    }

    @RequestMapping(value = "ManageSponsors", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageSponsors(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/ManageSponsors";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getSponsorList", method = RequestMethod.GET)
    public void getSponsorList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = trainingCalendarDao.getSponsorList(lub.getLoginempid());
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AddTrainingSponsor", method = {RequestMethod.GET, RequestMethod.POST})
    public String addTrainingSponsor(HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("TrainingSponsorForm") TrainingSponsorForm trainingSponsor) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("TrainingSponsorForm", trainingSponsor);
        int sponsorCode = Integer.parseInt(trainingSponsor.getSponsorCode());

        String path = null;

        if (sponsorCode > 0) {
            trainingCalendarDao.updateTrainingSponsor(trainingSponsor, lub.getLoginempid());
            path = "redirect:/ManageSponsors.htm";
        } else {
            mav.addObject("status", "success");
            trainingCalendarDao.addNewSponsor(trainingSponsor, lub.getLoginempid());
            path = "redirect:/ManageSponsors.htm";
        }
        return path;
    }

    @RequestMapping(value = "DeleteSponsor", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteSponsor(HttpServletResponse response,
            @RequestParam("sponsorCode") int sponsorCode) {

        trainingCalendarDao.deleteSponsor(sponsorCode);
    }

    @RequestMapping(value = "ManageVenues", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageVenues(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/ManageVenues";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getVenueList", method = {RequestMethod.POST, RequestMethod.GET})
    public void getVenueList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = trainingCalendarDao.getVenueList(lub.getLoginempid());
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AddTrainingVenue", method = {RequestMethod.GET, RequestMethod.POST})
    public String addTrainingVenue(HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("TrainingVenueForm") TrainingVenueForm trainingVenue) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("TrainingVenueForm", trainingVenue);
        int sponsorCode = Integer.parseInt(trainingVenue.getVenueCode());

        String path = null;

        if (sponsorCode > 0) {
            trainingCalendarDao.updateTrainingVenue(trainingVenue, lub.getLoginempid());
            path = "redirect:/ManageVenues.htm";
        } else {
            mav.addObject("status", "success");
            trainingCalendarDao.addNewVenue(trainingVenue, lub.getLoginempid());
            path = "redirect:/ManageVenues.htm";
        }
        return path;
    }

    @RequestMapping(value = "DeleteVenue", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteVenue(HttpServletResponse response,
            @RequestParam("venueCode") int venueCode) {

        trainingCalendarDao.deleteVenue(venueCode);
    }

    @RequestMapping(value = "downloadPdf", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadPdf(HttpServletResponse response,
            @RequestParam("trainingId") int trainingId) {
        String filePath = context.getInitParameter("TrainingDocumentPath");
        trainingCalendarDao.downloadDocument(response, filePath, trainingId);
    }

    @RequestMapping(value = "TrainingHome", method = {RequestMethod.POST, RequestMethod.GET})
    public String goHome() {
        String path = "/trainingadmin/Home";
        return path;
    }

    @RequestMapping(value = "Programmes", method = {RequestMethod.POST, RequestMethod.GET})
    public String goProgrammes() {
        String path = "/trainingadmin/Programmes";
        return path;
    }

    @RequestMapping(value = "AboutUs", method = {RequestMethod.POST, RequestMethod.GET})
    public String goAboutUs() {
        String path = "/trainingadmin/AboutUs";
        return path;
    }

    @RequestMapping(value = "TrainingInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public String goTrainingInstitutes() {
        String path = "/trainingadmin/TrainingInstitutes";
        return path;
    }

    @RequestMapping(value = "Circulars", method = {RequestMethod.POST, RequestMethod.GET})
    public String goCirculars() {
        String path = "/trainingadmin/Circulars";
        return path;
    }

    @RequestMapping(value = "NewsEvents", method = {RequestMethod.POST, RequestMethod.GET})
    public String goNewsEvents() {
        String path = "/trainingadmin/NewsEvents";
        return path;
    }

    @RequestMapping(value = "AssignFaculties", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageAssignFaculties(@RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("trainingId", trainingId);
        String path = "/trainingadmin/AssignFaculties";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "GetAssignedFacultyList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAssignedFacultyList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getAssignedFacultyList(lub.getLoginempid(), trainingId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AssignFacultyListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void AssignFacultyListAction(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("facultyCode") String facultyCode, @RequestParam("status") String status, @RequestParam("trainingId") String trainingId) {

        trainingCalendarDao.assignFacultyList(trainingId, Integer.parseInt(facultyCode), status, lub.getLoginempid());
    }

    @RequestMapping(value = "AssignSponsors", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageAssignSponsors(@RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("trainingId", trainingId);
        String path = "/trainingadmin/AssignSponsors";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "GetAssignedSponsorList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAssignedSponsorList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getAssignedSponsorList(lub.getLoginempid(), trainingId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AssignSponsorListAction", method = {RequestMethod.GET, RequestMethod.POST})
    public void AssignSponsorListAction(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("sponsorCode") String sponsorCode, @RequestParam("status") String status, @RequestParam("trainingId") String trainingId) {

        trainingCalendarDao.assignSponsorList(trainingId, Integer.parseInt(sponsorCode), status, lub.getLoginempid());
    }

    @RequestMapping(value = "TrainingInstitutesList", method = {RequestMethod.GET, RequestMethod.POST})
    public void trainingInstitutesList(HttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getTrainingInstituteList();
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "UpcomingTrainingPrograms", method = {RequestMethod.GET, RequestMethod.POST})
    public void UpcomingTrainingPrograms(HttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getUpcomingTrainingPrograms();
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "deleteDocument", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteDocument(HttpServletResponse response, @RequestParam("trainingId") String trainingId) {
        String filePath = context.getInitParameter("TrainingDocumentPath");
        try {
            trainingCalendarDao.deleteDocumentAttachment(Integer.parseInt(trainingId), filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "getLeftUpcomingPrograms", method = {RequestMethod.GET, RequestMethod.POST})
    public void getLeftUpcomingPrograms(HttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            content = trainingCalendarDao.getLeftUpcomingPrograms();
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "InstituteProfile", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewInstituteProfile(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        InstituteForm inf = new InstituteForm();
        inf = trainingCalendarDao.getInstituteDetail(lub.getLoginempid());
        mav.addObject("InstituteForm", inf);
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/InstituteProfile";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "UpdateInstituteProfile", method = {RequestMethod.POST, RequestMethod.GET})
    public String updateInstitute(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("InstituteProfile") InstituteForm instForm) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("InstituteForm", instForm);
        String path = null;
        trainingCalendarDao.updateInstituteProfile(instForm, lub.getLoginempid());
        path = "redirect:/InstituteProfile.htm";
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "ApplyTrainingList", method = {RequestMethod.GET, RequestMethod.POST})
    public void applyTrainingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {

            List li = trainingCalendarDao.getApplyProgramList(lub.getLoginempid(), page, rows, "apply");
            String where = " GTP WHERE (SELECT status_name FROM hw_emp_training ET "
                    + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                    + "WHERE training_id = GTP.training_program_code LIMIT 1) IS NULL AND from_date >= current_date ";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_programs", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveTrainingProgram", method = RequestMethod.POST)
    public void saveTrainingProgram(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("hidTrId") String trid, @RequestParam("post") String spc, @RequestParam("mobile") String mobile, @RequestParam("email") String email, @RequestParam("tpids") String tpids, @RequestParam("trainingOption") String trainingOption) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = trainingCalendarDao.applyTraining(lub.getLoginempid(), trid, spc, mobile, email, tpids, trainingOption);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "trainingApprove")
    public ModelAndView checkTrainingApproval(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") String taskId) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        TrainingProgramForm tpf = null;
        tpf = trainingCalendarDao.getApplyTrainingDetail(Integer.parseInt(taskId));
        mav.addObject("taskId", taskId);
        mav.addObject("TrainingProgramForm", tpf);
        path = "trainingadmin/CheckTraining";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "saveTrainingApprove")
    public void saveTrainingApprove(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId, @RequestParam("sltApproveStatus") int trainingStatus, @RequestParam("forwardEmpid") String forwardEmpid) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = trainingCalendarDao.saveTrainingApprove(taskId, trainingStatus, lub.getLoginempid(), lub.getLoginspc(), forwardEmpid);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "GetTrainingApplications", method = {RequestMethod.GET, RequestMethod.POST})
    public void getMyApplications(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getApplyProgramList(lub.getLoginempid(), page, rows, "");
            String where = " GTP WHERE (SELECT status_name FROM hw_emp_training ET "
                    + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                    + "WHERE ET.training_id = GTP.training_program_code AND ET.apply_emp_id = '" + lub.getLoginempid() + "') IS NOT NULL";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_programs", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "MyTrainingApplication", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getMyTrainingApplication() {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/MyTrainingApplication";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getTrainingStatusJSON")
    public void getTrainingStatusJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List trainingstatuslist = trainingCalendarDao.getTrainingStatus();
            json = new JSONArray(trainingstatuslist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getOnlineTrainingStatusJSON")
    public void getOnlineTrainingStatusJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List trainingstatuslist = trainingCalendarDao.getOnlineTrainingStatus();
            json = new JSONArray(trainingstatuslist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "showParticipants", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView showParticipants(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        mav.addObject("trainingId", trainingId);
        mav.addObject("fullName", lub.getLoginoffname());
        path = "trainingadmin/ShowParticipants";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "GetParticipantList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getParticipantList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows, @RequestParam("trainingId") String trainingId) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getParticipantList(lub.getLoginempid(), trainingId, page, rows);
            String where = " WHERE training_id = " + trainingId + " AND owner_id = '" + lub.getLoginempid() + "'";
            int total = trainingCalendarDao.getTotalRowsCount("hw_emp_training", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ManageInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/ManageInstitutes";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "getInstituteList", method = RequestMethod.GET)
    public void getInstituteList(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = instituteDAO.getInstituteList(lub.getLoginempid(), page, rows);
            JSONObject json = new JSONObject();
            String where = "";
            int total = trainingCalendarDao.getTotalRowsCount("g_institutions", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AddTrainingInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AddTrainingInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TrainingInstituteForm") InstituteForm tiForm) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("InstituteForm", tiForm);
        mav.addObject("opt", tiForm.getOpt());
        int instituteId = tiForm.getInstituteId();
        String path = null;
        if (instituteId > 0) {
            tiForm.setInstituteId(instituteId);
            instituteDAO.updateInstitute(tiForm, lub.getLoginempid());
            path = "/trainingadmin/ManageInstitutes";
        } else {
            instituteDAO.saveInstitute(tiForm, lub.getLoginempid());
            path = "/trainingadmin/ManageInstitutes";
        }
        if (lub.getLoginusertype() == "I") {
            path = "redirect:/ManageInstitutes.htm";
        }
        if (lub.getLoginusertype() == "T") {
            path = "redirect:/TAManageInstitutes.htm";
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "AddOnlineInstitute", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AddOnlineInstitute(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TrainingInstituteForm") InstituteForm tiForm) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("InstituteForm", tiForm);
        mav.addObject("opt", tiForm.getOpt());
        int instituteId = tiForm.getInstituteId();
        String path = null;
        if (instituteId > 0) {
            tiForm.setInstituteId(instituteId);
            instituteDAO.updateOnlineInstitute(tiForm, instituteId);
            path = "/trainingadmin/onlinetraining/OnlineInstitutes";
        } else {
            instituteDAO.saveOnlineInstitute(tiForm, lub.getLoginempid());
            path = "/trainingadmin/onlinetraining/OnlineInstitutes";
        }
        if (tiForm.getDeptCode().equals("")) {
            path = "redirect:/OnlineInstitutes.htm";
        } else {
            path = "redirect:/DeptOnlineInstitutes.htm";
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "ManageTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ManageTraining(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        path = "/trainingadmin/ManageTraining";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "ManageTrainingList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getManageTrainingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getManageTrainingProgramList(lub.getLoginempid(), page, rows);
            String where = " GTP "
                    + "INNER JOIN g_institutions I ON CAST (GTP.owner_id AS INTEGER) = I.institution_code "
                    + "WHERE (training_authority = '" + lub.getLoginempid() + "' OR (SELECT COUNT(*) FROM g_training_authorities WHERE privileged_authroity = '" + lub.getLoginempid() + "' AND authority_id = GTP.training_authority) >0) AND is_archived = 'N'";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_programs", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ManageTrainingDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView manageTrainingDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/ManageTrainingDetail";
        TrainingProgramForm tpf = null;
        mav.addObject("trainingId", trainingId);
        tpf = trainingCalendarDao.getManageTrainingDetail(Integer.parseInt(trainingId));
        mav.addObject("TrainingProgramForm", tpf);
        int numShortlisted = tpf.getNumShortlisted();
        int capacity = tpf.getCapacity();
        float width = 0;
        width = (((float) numShortlisted / (float) capacity) * 200);
        mav.addObject("width", (int) width);
        mav.setViewName(path);
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "GetAppliedParticipantList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAppliedParticipantList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows, @RequestParam("trainingId") String trainingId) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getAppliedParticipantList(lub.getLoginempid(), trainingId, rows, page);
            String where = "  ET"
                    + " INNER JOIN g_training_programs GTP ON ET.training_id = GTP.training_program_code"
                    + " INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer)"
                    + " INNER JOIN emp_mast AS EM ON EM.emp_id = ET.apply_emp_id"
                    + " LEFT OUTER JOIN g_spc AS GSPC ON GSPC.spc = apply_spc "
                    + " WHERE training_id = " + trainingId + " AND (training_authority = '" + lub.getLoginempid() + "' OR (SELECT COUNT(*) FROM g_training_authorities WHERE privileged_authroity = '" + lub.getLoginempid() + "' AND authority_id = GTP.training_authority) >0)";
            int total = trainingCalendarDao.getTotalRowsCount("hw_emp_training", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "SaveApplicants", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveApplicants(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ApplicantForm") TrainingProgramForm trainingPrograms) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("ApplicantForm", trainingPrograms);
        String[] participants = null;
        int trainingId = trainingPrograms.getTrainingId();
        participants = trainingPrograms.getParticipants();
        for (int i = 0; i < participants.length; i++) {
            trainingCalendarDao.saveShortlisted(Integer.parseInt(participants[i]));

        }
        mav.addObject("trainingId", trainingId);
        TrainingProgramForm tpf = null;
        tpf = trainingCalendarDao.getManageTrainingDetail(trainingId);
        mav.addObject("TrainingProgramForm", tpf);
        int numShortlisted = tpf.getNumShortlisted();
        int capacity = tpf.getCapacity();
        float width = 0;
        width = (((float) numShortlisted / (float) capacity) * 200);
        mav.addObject("width", (int) width);
        String path = null;
        path = "/trainingadmin/administrator/ManageTrainingDetail";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "ExportExcel")
    public void ExportExcel(HttpServletResponse response, @RequestParam("trainingId") String trainingId) {

        trainingCalendarDao.exportExcel(response, trainingId, "");

    }

    @RequestMapping(value = "TrainingArchive", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView showTrainingArchive(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/TrainingArchive";
        mav.addObject("fullName", lub.getLoginoffname());
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "TrainingArchiveController", method = {RequestMethod.GET, RequestMethod.POST})
    public void getTrainingArchive(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {
            List li = trainingCalendarDao.getArchiveTrainingProgramList(lub.getLoginempid(), page, rows);
            String where = " WHERE owner_id = '" + lub.getLoginempid() + "'  AND to_date <= (current_date - interval '90' day)";
            int total = trainingCalendarDao.getTotalRowsCount("g_training_programs", where);
            out = response.getWriter();
            json.put("total", total);
            json.put("rows", li);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "showTrainingDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public void showTrainingDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String trainingOptions = null;
        try {

            TrainingProgramForm tpl = null;
            //Call the DAO
            tpl = trainingCalendarDao.getTrainingDetail(Integer.parseInt(trainingId));
            trainingOptions = trainingCalendarDao.getEmpTrainingOptions(lub.getLoginempid(), Integer.parseInt(trainingId));
            StringBuffer content
                    = new StringBuffer("<table width='100%'><tr><td width='170'>Training Program Name: </td>");
            content.append("<td><strong style='color:#FF0000;'>" + tpl.getTrainingProgram() + "</strong> (" + tpl.getInstituteName() + ")</td></tr>");
            content.append("<tr><td>Date:</td><td>" + tpl.getFromDate() + " to " + tpl.getToDate() + "</td></tr>");
            content.append("<tr><td>Training Options: </td><td><select name=\"trainingOption\" id=\"trainingOption\" size=\"1\">"
                    + "<option value=\"\">-Select-</option>");
            //if (trainingOptions.indexOf("1") == -1) {
            content.append("<option value='1'>Option 1</option>");
            //}
            //if (trainingOptions.indexOf("2") == -1) {
            content.append("<option value='2'>Option 2</option>");
            //}
            //if (trainingOptions.indexOf("3") == -1) {
            content.append("<option value='3'>Option 3</option>");
            //}
            content.append("</select></td></tr>");
            content.append("</table>");

            out.print(content);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    //@ResponseBody
    @RequestMapping(value = "getPreviousTrainingList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getPreviousTrainingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        try {

            List li = trainingCalendarDao.getPreviousTrainingList(lub.getLoginempid());
            Iterator itr = li.iterator();
            TrainingProgramList tpl = null;
            StringBuffer content
                    = new StringBuffer("");

            content.append("<div style=\"background:#EAEAEA;padding:4px;font-size:11pt;font-weight:bold;\">Select Previous Training you already attended</div><div class='container' style='width:100%;margin-top:10px;'><div class='rows'>");
            String oldFinancialYear = null;
            while (itr.hasNext()) {
                boolean isExist = false;
                tpl = (TrainingProgramList) itr.next();
                String financialYear = tpl.getFromDate();
                if (!financialYear.equalsIgnoreCase(oldFinancialYear)) {
                    content.append("<div style='font-size:16pt;color:#008900;font-weight:bold;'>" + financialYear + "</div>");
                }
                isExist = trainingCalendarDao.hasEmpPreviousTraining(lub.getLoginempid(), Integer.parseInt(tpl.getTrainingProgramID()));
                if (isExist) {
                    content.append("<div class='col-md-12' style='background:#FFFFEA;margin-bottom:5px;'><input type='checkbox' id='tp_ids_" + tpl.getTrainingProgramID() + "' value='" + tpl.getTrainingProgramID() + "' name='tp_ids[]' onclick='javascript: checkSelected(this.value)' checked='checked' />"
                            + " <label style='color:#555555;' for='tp_ids_" + tpl.getTrainingProgramID() + "'><span style='font-weight:normal;'>" + tpl.getProgramName() + "</span> (" + tpl.getVenue() + ")</label></div>");
                } else {
                    content.append("<div class='col-md-12' style='background:#FAFAFA;margin-bottom:5px;'><input type='checkbox' id='tp_ids_" + tpl.getTrainingProgramID() + "' value='" + tpl.getTrainingProgramID() + "' name='tp_ids[]' onclick='javascript: checkSelected(this.value)' />"
                            + " <label style='color:#555555;' for='tp_ids_" + tpl.getTrainingProgramID() + "'><span style='font-weight:normal;'>" + tpl.getProgramName() + "</span> (" + tpl.getVenue() + ")</label></div>");
                }
                oldFinancialYear = financialYear;
            }
            boolean isExist = trainingCalendarDao.hasEmpPreviousTraining(lub.getLoginempid(), 99999);
            if (isExist) {
                content.append("<div class='col-md-12'  style='background:#FFFFEA;margin-bottom:5px;'><input type='checkbox' id='tp_ids_0' value='99999' name='tp_ids[]' onclick='javascript: checkSelected(this.value)' checked='checked' />"
                        + " <label for='tp_ids_0'>None of the Above</td></label></div>");
                content.append("</div></div>");

            } else {
                content.append("<div class='col-md-12'  style='background:#FAFAFA;margin-bottom:5px;'><input type='checkbox' id='tp_ids_0' value='99999' name='tp_ids[]' onclick='javascript: checkSelected(this.value)' />"
                        + " <label for='tp_ids_0'>None of the Above</td></label></div>");
                content.append("</div></div>");

            }
            content.append("<input type='hidden' id='tpids' name='tpids' value='' />");

            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "updateTrainingOption", method = {RequestMethod.GET, RequestMethod.POST})
    public void updateTrainingOption(HttpServletResponse response,
            @RequestParam("trainingId") String trainingId, @RequestParam("empId") String empId, @RequestParam("trainingOption") String trainingOption) {

        int iTrainingId = Integer.parseInt(trainingId);
        trainingCalendarDao.updateTrainingOption(iTrainingId, empId, trainingOption);
    }

    @RequestMapping(value = "UpdatePreviousTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView UpdatePreviousTraining(@RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/UpdatePreviousTraining";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "savePreviousTraining", method = RequestMethod.POST)
    public void savePreviousTraining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("tpids") String tpids) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = trainingCalendarDao.savePreviousTraining(lub.getLoginempid(), tpids);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "witdrawTrainingAction")
    public void witdrawTrainingAction(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") int trainingId) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = trainingCalendarDao.withdrawTraining(trainingId, lub.getLoginempid());

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "GetTrainingDeptListJSON", method = RequestMethod.POST)
    public String getTrainingDeptListJSON() {

        JSONArray json = null;
        try {
            List deptlist = trainingCalendarDao.getTrainingDeptList();
            json = new JSONArray(deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "GetTrainingOfficeListJSON", method = RequestMethod.POST)
    public String getTrainingOfficeListJSON(@RequestParam("deptcode") String deptcode) {

        JSONArray json = null;
        try {
            List officelist = trainingCalendarDao.getTrainingOfficeList(deptcode);
            json = new JSONArray(officelist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getPostListTrainingJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPostListTrainingJSON(@RequestParam("deptcode") String deptcode, @RequestParam("offcode") String offcode) {
        JSONArray json = null;
        try {
            List postlist = trainingCalendarDao.getPostListTraining(deptcode, offcode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @RequestMapping(value = "GetEmpDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getEmpDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String empid = lub.getLoginempid();
        String path = "index";

        if (empid != null && (empid.equals("32") || empid.equals("51"))) {
            path = "/trainingadmin/GetEmpDetail";
        }
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SearchEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public void searchEmployee(HttpServletRequest request, HttpServletResponse response, @RequestParam("fName") String fName, @RequestParam("lName") String lName, @RequestParam("dob") String dob, @RequestParam("mobile") String mobile, @RequestParam("email") String email) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        EmployeeSearch empSearch = new EmployeeSearch();
        empSearch.setFirstName(fName);
        empSearch.setLastName(lName);
        empSearch.setDob(dob);
        empSearch.setMobile(mobile);
        empSearch.setEmail(email);
        String background = null;

        StringBuffer content
                = new StringBuffer("<table border='0' cellspacing='1' width='90%' align='center' class='tblres table-bordered' style='font-size:10pt;'>"
                        + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                        + "<td>HRMS ID</td>"
                        + "<td>Full Name</td>"
                        + "<td>Designation</td>"
                        + "<td>GPF No.</td>"
                        + "<td>Date of Birth</td>"
                        + "<td>Mobile</td>"
                        + "<td>Email</td>"
                        + "</tr>");
        try {
            List empList = new ArrayList();
            empList = trainingCalendarDao.getSearchEmp(empSearch);

            EmployeeSearch obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (EmployeeSearch) empList.get(i);
                    if (i % 2 == 0) {
                        background = "#FFFFFF";
                    } else {
                        background = "#C1E3FF";
                    }
                    content.append("<tr bgcolor='" + background + "'>"
                            + "<td>" + obj.getEmpId() + "</td>"
                            + "<td>" + obj.getFullName() + "</td>"
                            + "<td>" + obj.getPostName() + "</td>"
                            + "<td>" + obj.getGpfNo() + "</td>"
                            + "<td>" + obj.getDob() + "</td>"
                            + "<td>" + obj.getMobile() + "</td>"
                            + "<td>" + obj.getEmail() + "</td>"
                            + "</tr>");
                }
            }
            content.append("</TABLE>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @RequestMapping(value = "ApplyNISGTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ApplyNISGTraining(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/ApplyNISGTraining";
        String empName = null;
        String empDeptName = null;
        String empPost = null;
        Users emp = new Users();
        try {
            emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());

            empName = emp.getFullName();
            empDeptName = emp.getDeptName();
            empPost = emp.getPostname();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String trainingIds = null;
        trainingIds = trainingCalendarDao.getActiveTraining();
        int cnt = trainingCalendarDao.getTrainingCount(lub.getLoginempid());
        mav.addObject("empName", empName);
        mav.addObject("empDeptName", empDeptName);
        mav.addObject("empPost", empPost);
        mav.addObject("empId", lub.getLoginempid());
        mav.addObject("trainingId", trainingIds);
        mav.addObject("cnt", cnt);
        String result = requestParams.get("result");
        mav.addObject("result", result);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "showNISGTrainingDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public void showNISGTrainingDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String trainingOptions = null;
        try {

            TrainingProgramForm tpl = null;
            //Call the DAO
            tpl = trainingCalendarDao.getTrainingDetail(Integer.parseInt(trainingId));
            StringBuffer content
                    = new StringBuffer("<table width='800' align=\"center\" class='apply-table' style='margin-top:10px;border:1px solid #EAEAEA'>"
                            + "  <tr bgcolor=\"#006A9D\" style=\"color:#FFFFFF;font-weight:bold;\">\n"
                            + "    <td colspan=\"2\">Training Program Details</td>\n"
                            + "  </tr><tr><td width='170'>Training Program Name: </td>");
            content.append("<td><strong style='color:#FF0000;'>" + tpl.getTrainingProgram() + "</strong> (" + tpl.getInstituteName() + ")</td></tr>");
            content.append("<tr><td>Venue:</td><td style='color:#008900;font-weight:bold;'>Training Hall of Finance Department</td></tr>");
            content.append("<tr><td>Date:</td><td>" + tpl.getFromDate() + " to " + tpl.getToDate() + "</td></tr>");
            content.append("</table>");

            out.print(content);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveNISGTraining", method = RequestMethod.POST)
    public ModelAndView saveNISGTraining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("NISGTrainingBean") NISGTrainingBean nisg) {

        try {
            trainingCalendarDao.saveNISGTrainingDetails(nisg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/ApplyNISGTraining.htm?result=success");
    }

    @RequestMapping(value = "NISGParticipants", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView showNISGParticipants(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        mav.addObject("fullName", lub.getLoginoffname());
        path = "trainingadmin/NISGParticipants";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "GetNISGParticipants", method = {RequestMethod.POST, RequestMethod.GET})
    public void GetNISGParticipants(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        NISGTrainingBean nisgBean = new NISGTrainingBean();

        String background = null;

        StringBuffer content
                = new StringBuffer("<table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>"
                        + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                        + "<td>Sl No.</td>"
                        + "<td>Full Name</td>"
                        + "<td>Designation</td>"
                        + "<td>Department</td>"
                        + "<td>Mobile</td>"
                        + "<td width='20'></td>"
                        + "</tr>");
        try {
            List participantList = new ArrayList();
            participantList = trainingCalendarDao.GetNISGParticipants();

            NISGTrainingBean obj = null;
            if (participantList != null && participantList.size() > 0) {
                for (int i = 0; i < participantList.size(); i++) {
                    obj = (NISGTrainingBean) participantList.get(i);
                    if (i % 2 == 0) {
                        background = "#FFFFFF";
                    } else {
                        background = "#C1E3FF";
                    }
                    Users emp = null;
                    emp = loginDao.getEmployeeProfileInfo(obj.getEmpId());
                    content.append("<tr bgcolor='" + background + "'>"
                            + "<td>" + (i + 1) + "</td>"
                            + "<td>" + emp.getFullName() + "</td>"
                            + "<td>" + emp.getPostname() + "</td>"
                            + "<td>" + emp.getDeptName() + "</td>"
                            + "<td>" + emp.getMobile() + "</td>"
                            + "<td>" + "<a href='javascript:void(0);' onclick='javascript: showNISGWindow(" + obj.getParticipantId() + ")' title='View Detail'><img src='images/view_icon.png' alt='View Detail' /></a>" + "</td>"
                            + "</tr>");
                }
            }
            content.append("</TABLE>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @RequestMapping(value = "NISGParticipantDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView NISGParticipantDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("participantID") String participantID) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        NISGTrainingBean nisgBean = new NISGTrainingBean();
        nisgBean = trainingCalendarDao.getNISGParticipantDetail(participantID);
        mav.addObject("nisgBean", nisgBean);
        path = "trainingadmin/NISGParticipantDetail";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "updateEmpTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateEmpTraining(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String empid = lub.getLoginempid();
        String path = "index";

        if (empid != null && (empid.equals("32") || empid.equals("51"))) {
            path = "/trainingadmin/UpdateEmpTraining";
            mav.addObject("trainingList", trainingCalendarDao.getOptionTrainingList());
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveEmpPreviousTraining", method = RequestMethod.GET)
    public void saveEmpPreviousTraining(HttpServletResponse response, @RequestParam("hrmsId") String hrmsId, @RequestParam("trainingId") String trainingId) {

        try {
            String msg = trainingCalendarDao.saveEmpPreviousTraining(hrmsId, trainingId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "GetApplyPreviousTraining", method = {RequestMethod.GET, RequestMethod.POST})
    public void GetApplyPreviousTraining(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();

        String background = null;

        StringBuffer content
                = new StringBuffer("");
        try {
            List participantList = new ArrayList();
            participantList = trainingCalendarDao.GetApplyPreviousTrainingList(lub.getLoginempid());

            TrainingProgramList obj = null;
            if (participantList != null && participantList.size() > 0) {
                for (int i = 0; i < participantList.size(); i++) {
                    if (i == 0) {
                        content.append("<table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>"
                                + "<tr style='font-weight:bold;background:#FCC4AA;color:#000000;'>"
                                + "<td colspan='4'>Previous Training Attended</td>"
                                + "</tr>"
                                + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                                + "<td>Sl No.</td>"
                                + "<td>Training Program</td>"
                                + "<td>Institute</td>"
                                + "<td>Year</td>"
                                + "</tr>");
                    }
                    obj = (TrainingProgramList) participantList.get(i);
                    if (i % 2 == 0) {
                        background = "#FFFFFF";
                    } else {
                        background = "#C1E3FF";
                    }
                    content.append("<tr bgcolor='" + background + "'>"
                            + "<td>" + (i + 1) + "</td>"
                            + "<td>" + obj.getProgramName() + "</td>"
                            + "<td>" + obj.getInstituteName() + "</td>"
                            + "<td>" + obj.getFinancialYear() + "</td>"
                            + "</tr>");
                }
            }
            content.append("</TABLE>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @RequestMapping(value = "ViewPreviousTrainingList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewPreviousTrainingList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("EmployeeSearch") EmployeeSearch es) throws IOException {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/administrator/ViewPreviousTrainingList";
        mav.addObject("fullName", lub.getLoginoffname());
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SearchPreviousTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public void SearchPreviousTraining(HttpServletRequest request, HttpServletResponse response, @RequestParam("fName") String fName, @RequestParam("lName") String lName) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String background = null;
        StringBuffer content
                = new StringBuffer("");
        try {
            List empList = new ArrayList();
            EmployeeSearch eSearch = null;
            empList = trainingCalendarDao.getTrainingSearchEmp(fName, lName, "");
            if (empList != null && empList.size() > 0) {
                for (int j = 0; j < empList.size(); j++) {
                    eSearch = (EmployeeSearch) empList.get(j);
                    content.append("<table border='0' cellspacing='1' width='90%' align='center' class='tblres table-bordered' style='font-size:10pt;'>");
                    content.append("<tr style='font-size:13pt;font-weight:bold;background:#EAEAEA;'><td><span style='color:#008900;'>" + eSearch.getFullName() + ",</span> <span style='color:#008900;'>" + eSearch.getPostName() + "</span></td></tr>");
                    content.append("</table>");
                    List participantList = new ArrayList();
                    participantList = trainingCalendarDao.GetApplyPreviousTrainingList(eSearch.getEmpId());

                    TrainingProgramList obj = null;
                    if (participantList != null && participantList.size() > 0) {
                        for (int i = 0; i < participantList.size(); i++) {
                            if (i == 0) {
                                content.append("<table border='0' cellspacing='1' width='90%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-bottom:20px;'>"
                                        + "<tr style='font-weight:bold;background:#FCC4AA;color:#000000;'>"
                                        + "<td colspan='4'>Previous Training Attended</td>"
                                        + "</tr>"
                                        + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                                        + "<td>Sl No.</td>"
                                        + "<td>Training Program</td>"
                                        + "<td>Institute</td>"
                                        + "<td>Year</td>"
                                        + "</tr>");
                            }
                            obj = (TrainingProgramList) participantList.get(i);
                            if (i % 2 == 0) {
                                background = "#FFFFFF";
                            } else {
                                background = "#C1E3FF";
                            }
                            content.append("<tr bgcolor='" + background + "'>"
                                    + "<td>" + (i + 1) + "</td>"
                                    + "<td>" + obj.getProgramName() + "</td>"
                                    + "<td>" + obj.getInstituteName() + "</td>"
                                    + "<td>" + obj.getFinancialYear() + "</td>"
                                    + "</tr>");
                        }
                    }
                    content.append("</TABLE>");
                }
            } else {
                content.append("<h2 align='center' style='font-size:14pt;color:#FF0000;font-weight:bold;'>Sorry, there is no such records found for the search criteria!</h2>");
            }

            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @RequestMapping(value = "AdminDashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AdminDashboard(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        mav.addObject("fullName", lub.getLoginoffname());
        path = "/trainingadmin/administrator/AdminDashboard";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TAManageTrainingList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView TAManageTrainingList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response
    ) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/administrator/ManageTraining";
        List li = trainingCalendarDao.getTAManageTrainingList();
        mav.addObject("trainingList", li);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TAManageTrainingDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView TAmanageTrainingDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/administrator/ManageTrainingDetail";
        TrainingProgramForm tpf = null;
        mav.addObject("trainingId", trainingId);
        mav.addObject("fullName", lub.getLoginoffname());
        tpf = trainingCalendarDao.getManageTrainingDetail(Integer.parseInt(trainingId));
        mav.addObject("TrainingProgramForm", tpf);
        int numShortlisted = tpf.getNumShortlisted();
        int capacity = tpf.getCapacity();
        float width = 0;
        width = (((float) numShortlisted / (float) capacity) * 200);
        mav.addObject("width", (int) width);
        List li = trainingCalendarDao.getTAAppliedParticipantList(trainingId);
        mav.addObject("participantList", li);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TAViewPreviousTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView TAViewPreviousTraining(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("trainingId") String trainingId) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/administrator/PreviousTrainingDetail";
        mav.addObject("empId", empId);
        mav.addObject("trainingId", trainingId);
        mav.addObject("fullName", lub.getLoginoffname());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TAGetPreviousTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public void TAViewPreviousTraining(HttpServletRequest request, HttpServletResponse response, @RequestParam("empId") String empId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String background = null;
        StringBuffer content
                = new StringBuffer("");
        try {
            List empList = new ArrayList();
            EmployeeSearch eSearch = null;
            empList = trainingCalendarDao.getTrainingSearchEmp("", "", empId);
            if (empList != null && empList.size() > 0) {
                for (int j = 0; j < empList.size(); j++) {
                    eSearch = (EmployeeSearch) empList.get(j);
                    content.append("<table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;'>");
                    content.append("<tr style='font-size:13pt;font-weight:bold;'><td>Full Name: <span style='color:#008900;'>" + eSearch.getFullName() + "</span></td></tr>");
                    content.append("<tr style='font-size:13pt;font-weight:bold;'><td>Designation: <span style='color:#008900;'>" + eSearch.getPostName() + "</span></td></tr>");
                    content.append("</table>");
                    List participantList = new ArrayList();
                    participantList = trainingCalendarDao.GetApplyPreviousTrainingList(eSearch.getEmpId());

                    TrainingProgramList obj = null;
                    if (participantList != null && participantList.size() > 0) {
                        for (int i = 0; i < participantList.size(); i++) {
                            if (i == 0) {
                                content.append("<table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>"
                                        + "<tr style='font-weight:bold;background:#FCC4AA;color:#000000;'>"
                                        + "<td colspan='4'>Previous Training Attended</td>"
                                        + "</tr>"
                                        + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                                        + "<td>Sl No.</td>"
                                        + "<td>Training Program</td>"
                                        + "<td>Institute</td>"
                                        + "<td>Year</td>"
                                        + "</tr>");
                            }
                            obj = (TrainingProgramList) participantList.get(i);
                            if (i % 2 == 0) {
                                background = "#FFFFFF";
                            } else {
                                background = "#C1E3FF";
                            }
                            content.append("<tr bgcolor='" + background + "'>"
                                    + "<td>" + (i + 1) + "</td>"
                                    + "<td>" + obj.getProgramName() + "</td>"
                                    + "<td>" + obj.getInstituteName() + "</td>"
                                    + "<td>" + obj.getFinancialYear() + "</td>"
                                    + "</tr>");
                        }
                    } else {
                        content.append("<table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>"
                                + "<tr style='font-weight:bold;background:#FFFFFF;color:#FF0000;'>"
                                + "<td colspan='4'>Sorry no Records Found!</td>"
                                + "</tr>");
                    }
                    content.append("</TABLE>");
                }
            } else {
                content.append("<h2 align='center' style='font-size:14pt;color:#FF0000;font-weight:bold;'>Sorry, there is no such records found for the search criteria!</h2>");
            }

            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @RequestMapping(value = "TAManageInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView TAManageInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        List li = instituteDAO.getTAInstituteList();
        mav.addObject("instituteList", li);
        String path = "/trainingadmin/administrator/ManageInstitutes";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "Dashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView dDashboard(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        String path = "/trainingadmin/onlinetraining/Dashboard";
        mav.addObject("user_type", lub.getLoginusertype());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "OnlineInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OnlineInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        String deptCode = "";
        String isAllowed = "";
        if (requestParams.get("deptCode") != null) {
            deptCode = requestParams.get("deptCode");
        }
        if (requestParams.get("isAllowed") != null) {
            isAllowed = requestParams.get("isAllowed");
        }
        mav.addObject("fullName", lub.getLoginoffname());
        List li = instituteDAO.getOnlineInstituteList(deptCode, isAllowed);
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("instituteList", li);
        mav.addObject("deptList", deptlist);
        mav.addObject("dCode", deptCode);
        mav.addObject("isAllowed", isAllowed);

        String path = "/trainingadmin/onlinetraining/OnlineInstitutes";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "EditOnlineInstitute", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView EditOnlineInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        String deptCode = "";
        String isAllowed = "";
        String instituteId = "";
        if (requestParams.get("id") != null) {
            instituteId = requestParams.get("id");
        }
        if (requestParams.get("deptCode") != null) {
            deptCode = requestParams.get("deptCode");
        }
        if (requestParams.get("isAllowed") != null) {
            isAllowed = requestParams.get("isAllowed");
        }
        mav.addObject("fullName", lub.getLoginoffname());
        List li = instituteDAO.getOnlineInstituteList(deptCode, isAllowed);
        InstituteForm inf = instituteDAO.getOnlineInstituteDetail(Integer.parseInt(instituteId));
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("instituteList", li);
        mav.addObject("deptList", deptlist);
        mav.addObject("dCode", deptCode);
        mav.addObject("isAllowed", isAllowed);
        mav.addObject("instituteId", instituteId);
        mav.addObject("InstituteForm", inf);

        String path = "/trainingadmin/onlinetraining/EditOnlineInstitute";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DeptOnlineInstitutes", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deptOnlineInstitutes(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        List li = instituteDAO.getDeptOnlineInstituteList(lub.getLoginempid());
        mav.addObject("instituteList", li);
        mav.addObject("deptId", lub.getLoginempid());
        String path = "/trainingadmin/onlinetraining/DeptOnlineInstitutes";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "downloadApplicantExcel")
    public void downloadApplicantExcel(HttpServletResponse response, @RequestParam("trainingId") String trainingId) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;

        try {
            TrainingProgramForm tf = trainingCalendarDao.getTrainingDetail(Integer.parseInt(trainingId));
            String fileSuffix = tf.getTrainingProgram();
            String fileName = "Participants_" + fileSuffix.replaceAll(" ", "_").toLowerCase() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);

            trainingCalendarDao.downloadApplicantExcel(out, fileSuffix, workbook, trainingId);

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    @RequestMapping(value = "ManageBatch", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GetTrainingBatch(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") int trainingId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        mav.addObject("trainingId", trainingId);
        mav.addObject("batchList", trainingCalendarDao.getTrainingBatchList(trainingId));
        String path = "/trainingadmin/ManageBatch";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SaveBatch", method = {RequestMethod.GET, RequestMethod.POST})
    public void saveBatch(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") String trainingId, @RequestParam("batchName") String batchName, @RequestParam("capacity") String capacity) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        TrainingBatchBean tbb = null;
        try {
            tbb = new TrainingBatchBean();
            tbb.setBatchName(batchName);
            tbb.setTrainingProgramCode(trainingId);
            tbb.setCapacity(capacity);
            tbb.setOwnerId(lub.getLoginempid());
            trainingCalendarDao.saveBatch(tbb);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "DeleteBatch", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteBatch(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingBatchId") int trainingBatchId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            trainingCalendarDao.deleteBatch(trainingBatchId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "AssignBatchEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AssignBatchEmployee(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") int trainingId, @RequestParam("batchId") int trainingBatchId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        mav.addObject("trainingId", trainingId);
        mav.addObject("trainingBatchId", trainingBatchId);
        mav.addObject("batchParticipantList", trainingCalendarDao.getBatchParticipantList(trainingBatchId));
        String path = "/trainingadmin/AssignBatchEmployee";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SearchBatchEmployee", method = {RequestMethod.POST, RequestMethod.GET})
    public void SearchBatchEmployee(HttpServletRequest request, HttpServletResponse response, @RequestParam("gpfNumber") String gpfNumber, @RequestParam("empId") String empId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String background = null;

        StringBuffer content = new StringBuffer();

        try {
            List empList = new ArrayList();
            empList = trainingCalendarDao.getBatchSearchEmp(gpfNumber, empId);

            EmployeeSearch obj = null;
            if (empList != null && empList.size() > 0) {
                content.append("<table border='0' cellspacing='1' width='90%' align='center' class='tblres table-bordered' style='font-size:10pt;'>"
                        + "<tr style='font-weight:bold;background:#0D508E;color:#FFFFFF;'>"
                        + "<td>Full Name</td>"
                        + "<td>Designation</td>"
                        + "<td width='15%' align='center'>Action</td>"
                        + "</tr>");
                for (int i = 0; i < empList.size(); i++) {
                    obj = (EmployeeSearch) empList.get(i);
                    if (i % 2 == 0) {
                        background = "#FFFFFF";
                    } else {
                        background = "#C1E3FF";
                    }
                    content.append("<tr bgcolor='" + background + "'>"
                            + "<td>" + obj.getFullName() + "</td>"
                            + "<td>" + obj.getPostName() + "</td>"
                            + "<td><a href=\"javascript: addParticipant('" + obj.getEmpId() + "', '" + obj.getFullName() + "', '" + obj.getPostName() + "')\">Add as Participant</a></td>"
                            + "</tr>");
                }
            }
            content.append("</TABLE>");
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        out.close();
        out.flush();
    }

    @ResponseBody
    @RequestMapping(value = "SaveBatchEmployee", method = {RequestMethod.GET, RequestMethod.POST})
    public void SaveBatchEmployee(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") int trainingId, @RequestParam("trainingBatchId") int trainingBatchId, @RequestParam("empId") String empId, @RequestParam("empName") String empName, @RequestParam("designation") String designation) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        StringBuffer content = new StringBuffer();
        TrainingBatchBean tbb = null;
        boolean isDuplicate = false;
        try {
            isDuplicate = trainingCalendarDao.checkDuplicateParticipant(empId, trainingBatchId);
            if (isDuplicate) {
                content.append("duplicate");
            } else {
                trainingCalendarDao.saveBatchEmployee(empId, empName, trainingId, trainingBatchId, designation);
            }
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "DeleteBatchParticipant", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteBatchParticipant(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingBatchId") int trainingBatchId, @RequestParam("empId") String empId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            trainingCalendarDao.deleteBatchParticipant(trainingBatchId, empId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ManageTimeslot", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ManageTimeslot(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("trainingId") int trainingId, @RequestParam("trainingBatchId") int trainingBatchId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("fullName", lub.getLoginoffname());
        mav.addObject("trainingId", trainingId);
        mav.addObject("trainingBatchId", trainingBatchId);
        mav.addObject("hourList", trainingCalendarDao.getHourList());
        mav.addObject("minuteList", trainingCalendarDao.getMinuteList());
        mav.addObject("facultyList", trainingCalendarDao.getBatchFacultyList(lub.getLoginempid()));
        mav.addObject("timeslotList", trainingCalendarDao.getTimeslotList(trainingBatchId));
        //mav.addObject("batchParticipantList", trainingCalendarDao.getBatchParticipantList(trainingBatchId));
        String path = "/trainingadmin/ManageTimeslot";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SaveTimeslot", method = {RequestMethod.POST, RequestMethod.GET})
    public void SaveTimeslot(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            TrainingBatchBean tBean = new TrainingBatchBean();
            tBean.setTrainingId(requestParams.get("trainingId"));
            tBean.setTrainingProgramBatchId(Integer.parseInt(requestParams.get("trainingBatchId")));
            tBean.setFromHour(requestParams.get("fromHour"));
            tBean.setFromMinute(requestParams.get("fromMinute"));
            tBean.setToHour(requestParams.get("toHour"));
            tBean.setToMinute(requestParams.get("toMinute"));
            tBean.setRoomNumber(requestParams.get("roomNumber"));
            tBean.setFacultyList(requestParams.get("facultyList"));
            tBean.setTrainingDate(requestParams.get("trainingDate"));
            trainingCalendarDao.SaveTimeslot(tBean);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "DeleteTimeslot", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteTimeslot(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("slotId") int slotId
    ) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        try {
            trainingCalendarDao.deleteTimeslot(slotId);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GiveTrainingFeedback", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GiveTrainingFeedback(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/GiveTrainingFeedback";
        ArrayList al = trainingCalendarDao.getEmpRatingList(lub.getLoginempid(), "N");
        mav.addObject("erList", al);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "SaveFeedbackRating", method = {RequestMethod.POST, RequestMethod.GET})
    public void SaveFeedbackRating(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        int ratingId = Integer.parseInt(requestParams.get("ratingId"));
        String ratingType = requestParams.get("ratingType");
        int rating = Integer.parseInt(requestParams.get("rating"));

        try {
            trainingCalendarDao.SaveRatingFeedback(ratingId, ratingType, rating);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ApplyOnlineTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ApplyOnlineTraining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OnlineTrainingBean") OnlineTrainingBean otbean) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/onlinetraining/ApplyOnlineTraining";

        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);
        List instituteList = instituteDAO.getOnlineInstituteList("", "Y");
        List trainingList = trainingCalendarDao.getEmpOnlineTrainings(lub.getLoginempid());
        int numApplications = trainingCalendarDao.getCurrentYearApplication(lub.getLoginempid());
        mav.addObject("instituteList", instituteList);
        mav.addObject("trainingList", trainingList);
        mav.addObject("numApplied", numApplications);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "SaveOnlineTraining")
    public ModelAndView SaveOnlineTraining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OnlineTrainingBean") OnlineTrainingBean otbean) {
        trainingCalendarDao.saveOnlineTraining(otbean, lub.getLoginempid());
        ModelAndView mav = new ModelAndView("redirect:/ApplyOnlineTraining.htm");
        return mav;
    }

    @RequestMapping(value = "UploadTrainingDocument", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView UploadTrainingDocument(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TrainingDocumentBean") TrainingDocumentBean documentBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        String trainingProgramId = requestParams.get("id");
        String path = "/trainingadmin/onlinetraining/UploadTrainingDocument";
        OnlineTrainingBean otBean = trainingCalendarDao.getOnlineTrainingDetail(Integer.parseInt(trainingProgramId));
        mav.addObject("trainingProgramId", trainingProgramId);
        mav.addObject("otBean", otBean);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "SaveTrainingDocuments", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView SaveTrainingDocument(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("TrainingDocumentBean") TrainingDocumentBean documentBean) {
        String dirPath = context.getInitParameter("OnlineTrainingDocumentPath");
        trainingCalendarDao.saveTrainingDocument(documentBean, lub.getLoginempid(), dirPath);
        ModelAndView mav = new ModelAndView("redirect:/ApplyOnlineTraining.htm");
        return mav;
    }

    @RequestMapping(value = "downloadOnlinePdf", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadOnlinePdf(HttpServletResponse response, @RequestParam Map<String, String> requestParams
    ) {
        int trainingId = Integer.parseInt(requestParams.get("trainingId"));
        String fileType = requestParams.get("fileType");
        String dirPath = context.getInitParameter("OnlineTrainingDocumentPath");
        trainingCalendarDao.downloadOnlineDocument(response, dirPath, trainingId, fileType);
    }

    @RequestMapping(value = "OnlineApplications", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView OnlineApplications(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        String institute = "";
        String appType = "";
        String status = "";
        if (requestParams.get("institute") != null) {
            institute = requestParams.get("institute");
        }
        if (requestParams.get("appType") != null) {
            appType = requestParams.get("appType");
        }
        if (requestParams.get("status") != null) {
            status = requestParams.get("status");
        }
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/onlinetraining/OnlineApplications";
        List trainingList = trainingCalendarDao.getAllOnlineTrainingApplications(institute, appType, status);
        List li = instituteDAO.getOnlineInstituteList("", "Y");
        mav.addObject("instituteList", li);
        mav.addObject("trainingList", trainingList);
        mav.addObject("institute", institute);
        mav.addObject("applicationType", appType);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "VirtualTraining", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView virtualTraining(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = "/trainingadmin/VirtualTraining";
        //List al = trainingCalendarDao.getEmployeeList(lub.getLoginoffcode());
        //mav.addObject("erList", al);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "AddVirtualTrainees", method = RequestMethod.POST)
    public ModelAndView addEmployees(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("VirtualTrainingBean") VirtualTrainingBean vtBean,
            BindingResult result, RedirectAttributes ra) {

        List list = new ArrayList();
        ModelAndView mav = new ModelAndView("/trainingadmin/AddVirtualTrainees", "VirtualTrainingBean", vtBean);

        try {

            List al = trainingCalendarDao.getEmployeeList(lub.getLoginoffcode());
            mav.addObject("EmpList", al);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "updateApplicationStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public void updateApplicationStatus(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String content = null;
        int applicationId = Integer.parseInt(requestParams.get("applicationId"));
        String status = requestParams.get("status");

        try {
            trainingCalendarDao.UpdateApplicationStatus(applicationId, status);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "GetCourseDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public void getCourseDetails(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("courseId") int courseId) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        StringBuffer content = new StringBuffer();
        TrainingBatchBean tbb = null;
        boolean isDuplicate = false;
        try {
            String strContent = trainingCalendarDao.getCourseDetails(courseId);
            content.append(strContent);
            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "viewAuthOnlineTraining")
    public ModelAndView checkOnlineTrainingApproval(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") String taskId) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        OnlineTrainingBean otBean = null;
        otBean = trainingCalendarDao.getOnlineCourseDetail(Integer.parseInt(taskId));
        mav.addObject("taskId", taskId);
        mav.addObject("onlineTrainingBean", otBean);
        path = "trainingadmin/onlinetraining/viewAuthOnlineTraining";
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "saveOnlineTrainingApprove")
    public void saveOnlineTrainingApprove(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId, @ModelAttribute("note") String note, @RequestParam("sltApproveStatus") int trainingStatus) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;
        try {
            msg = trainingCalendarDao.saveOnlineTrainingApprove(taskId, trainingStatus, lub.getLoginempid(), note);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "EmployeeAttendance.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadEmployeeAttendance(@ModelAttribute("SessionValue") LoginUserBean sesBean, @ModelAttribute("EmpAttendanceBean") EmpAttendanceBean eaBean) {

        ModelAndView mav = null;
        try {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            String dfrom = "";
            String dto = "";
            if (eaBean.getTxtperiodFrom() != null && !eaBean.getTxtperiodFrom().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(eaBean.getTxtperiodFrom());
                eaBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                eaBean.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
                // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }
            if (eaBean.getTxtperiodTo() != null && !eaBean.getTxtperiodTo().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(eaBean.getTxtperiodTo());
                eaBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                eaBean.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }

            List empList = trainingCalendarDao.getAttEmployeeList();
            List todayList = trainingCalendarDao.getTodayAttList(eaBean.getTxtperiodFrom(), eaBean.getTxtperiodTo());
            mav = new ModelAndView("trainingadmin/onlinetraining/EmployeeAttendance");
            mav.addObject("empList", empList);
            mav.addObject("fromdate", eaBean.getTxtperiodFrom());
            mav.addObject("todayList", todayList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewEmployeeAttendance.htm", method = {RequestMethod.GET})
    public ModelAndView getEmpDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empAttendanceBean") EmpAttendanceBean empAttendanceBean) {
        ModelAndView mv = new ModelAndView("trainingadmin/onlinetraining/viewEmployeeAttendance");
        ArrayList empDetails = trainingCalendarDao.getEmpDetails(empAttendanceBean.getEmpId());
        empAttendanceBean = trainingCalendarDao.getEmployeeName(empAttendanceBean.getEmpId());
        System.out.println("empAttendanceBean.getEmpId()" + empAttendanceBean.getEmpId());
        mv.addObject("empList", empDetails);
        mv.addObject("empAttendanceBean", empAttendanceBean);

        return mv;
    }
}

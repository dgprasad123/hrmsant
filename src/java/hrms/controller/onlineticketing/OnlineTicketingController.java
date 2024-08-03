/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.onlineticketing;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OnlineTicketTopicDAO;
import hrms.dao.onlineTicketing.OnlineTicketingDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.master.District;
import hrms.model.onlineTicketing.OnlineTicketing;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class OnlineTicketingController implements ServletContextAware {

    @Autowired
    OnlineTicketingDAO onlineTicketDAO;
    @Autowired
    OnlineTicketTopicDAO ticketTopicDAO;
    @Autowired
    DistrictDAO distDao;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "onlineticket.htm", method = RequestMethod.GET)
    public ModelAndView onlineTicketData(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        // OnlineTicketing ot=new OnlineTicketing();
        ModelAndView mv = null;
        OnlineTicketing onlineticket = new OnlineTicketing();
        onlineticket.setUsername(lub.getLoginusername());
        mv = new ModelAndView("/onlineticketing/OnlineTicketingData", "onlineticketing", onlineticket);

        List topiclist = ticketTopicDAO.getTicketTopicList();
        mv.addObject("topiclist", topiclist);
        String offcode = lub.getLoginoffcode();
        String distCode = ticketTopicDAO.getDistCode(offcode);
        mv.addObject("distCode", distCode);
        mv.addObject("deptCode", lub.getLoginoffcode());

        return mv;
    }

    @RequestMapping(value = "onlineticket.htm", params = "Next")
    public ModelAndView onlineTicketKnowledgeBase(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, @RequestParam Map<String, String> parameters) {
        // OnlineTicketing ot=new OnlineTicketing();
        ModelAndView mv = null;
        OnlineTicketing onlineticket = new OnlineTicketing();
        String topicId = parameters.get("topicId");
        String subtopic = parameters.get("subtopic");
        onlineticket.setUsername(lub.getLoginusername());
        mv = new ModelAndView("/onlineticketing/onlineTicketKnowledgeBase", "onlineticketing", onlineticket);
        mv.addObject("topicId", topicId);
        List topiclist = ticketTopicDAO.getTicketTopicList();
        mv.addObject("topiclist", topiclist);
        String offcode = lub.getLoginoffcode();
        String distCode = ticketTopicDAO.getDistCode(offcode);
        mv.addObject("distCode", distCode);
        mv.addObject("deptCode", lub.getLoginoffcode());
        List knowledgebase = onlineTicketDAO.getknowledgebase(topicId, subtopic);
        mv.addObject("knowledgebase", knowledgebase);

        return mv;
    }

    @RequestMapping(value = "onlineticket.htm", params = "save")
    public String onlineTicketData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        InputStream inputStream = null;
        String filePath = "";
        OutputStream outputStream = null;
        //TicketAttachment tktAttach = null;
        try {
            filePath = servletContext.getInitParameter("FilePath");
            MultipartFile ticketfile = onlineticket.getFile();
            fileName = System.currentTimeMillis() + "";
            String dirpath = filePath + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            outputStream = new FileOutputStream(dirpath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            inputStream = ticketfile.getInputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (ticketfile.getOriginalFilename() != null && !ticketfile.getOriginalFilename().equals("")) {
                // tktAttach = new TicketAttachment();
                onlineticket.setDfileName(fileName);
                onlineticket.setOfileName(ticketfile.getOriginalFilename());
                onlineticket.setFileType(ticketfile.getContentType());
                onlineticket.setFilePath(dirpath);
                onlineticket.setRefType("T-Ticket");
                //ticketAttachDAO.addAttachDocumentInfo(onlineticket);
            }
            onlineticket.setUsername(lub.getLoginuserid());
            String offcode = lub.getLoginoffcode();
            String distCode = ticketTopicDAO.getDistCode(offcode);
            onlineticket.setDistCode(distCode);
            onlineticket.setDeptCode(lub.getLoginoffcode());
            onlineticket.setUserId(lub.getLoginempid());

            onlineTicketDAO.addTicket(onlineticket, lub.getLoginspc());

            outputStream.close();
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return "redirect:/onlineticketlist.htm";
    }

    @RequestMapping(value = "onlineticketlist.htm", method = RequestMethod.GET)
    public String onlineTicketList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) throws ParseException{
Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }
        List onLineTicketList = onlineTicketDAO.getTicketList(lub.getLoginuserid(),onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo());
        model.put("onlineticketlist", onLineTicketList);
        model.put("isDDO",lub.getLoginAsDDO());
        return "onlineticketing/OnlineTicketingList";
    }

    @RequestMapping(value = "onlineticketlist.htm", method = RequestMethod.GET, params = "newticket")
    public String newTicket(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model)throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }
        onlineticket.setUsername(lub.getLoginusername());
        List onLineTicketList = onlineTicketDAO.getTicketList(lub.getLoginuserid(),onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo());
        model.put("onlineticketlist", onLineTicketList);
        return "redirect:/onlineticket.htm";
    }

    @RequestMapping(value = "onlineticketlistbyemployee.htm")
    public String meticket(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model)throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }
        onlineticket.setUsername(lub.getLoginusername());
        List onLineTicketList = onlineTicketDAO.getTicketListByEmployee(lub.getLoginuserid());
        model.put("onlineticketlist", onLineTicketList);
        model.put("isDDO",lub.getLoginAsDDO());
        return "onlineticketing/OnlineTicketingList";
    }

    @RequestMapping(value = "onlineticketlistbyoffice.htm")
    public String officeticket(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model)throws ParseException {

        onlineticket.setUsername(lub.getLoginusername());
        List onLineTicketList = onlineTicketDAO.getTicketListByOffice(lub.getLoginoffcode());
        model.put("onlineticketlist", onLineTicketList);
        model.put("isDDO",lub.getLoginAsDDO());
        return "onlineticketing/OnlineTicketingList";
    }

    @RequestMapping(value = "onlineticket.htm", params = "cancel")
    public String onlineTicketList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "redirect:/onlineticketlist.htm";
    }

    @RequestMapping(value = "onlineticketlistDC.htm", method = RequestMethod.GET)
    public String onlineticketlistDC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }

        List onLineTicketList = onlineTicketDAO.getTicketListDC(lub.getLogindistrictcode(), lub.getLoginuserid(), onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/onlineticketlistDC";
    }

    @RequestMapping(value = "onlineticketlistDDO.htm", method = RequestMethod.GET)
    public String onlineticketlistDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }

        List onLineTicketList = onlineTicketDAO.getTicketListDDO(lub.getLoginempid(), lub.getLoginuserid(), onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo(), lub.getLoginoffcode());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/onlineticketlistDDO";
    }

    @RequestMapping(value = "onlineticketlistResolved.htm", method = RequestMethod.GET)
    public String onlineticketlistResolved(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }

        List onLineTicketList = onlineTicketDAO.getTicketResolvedListDC(lub.getLogindistrictcode(), lub.getLoginuserid(), onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/onlineticketlistResolved";
    }

    @RequestMapping(value = "ticketActionDc.htm", method = RequestMethod.GET)
    public String ticketActionDc(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model, @RequestParam("ticketId") int ticketId, @RequestParam("encticketid") String encticketid) {
        //OnlineTicketing onLineTicketList = new OnlineTicketing();

        List onLineTicketList = onlineTicketDAO.ticketActionDc(lub.getLogindistrictcode(), encticketid, lub.getLoginuserid());
        model.put("loginId", lub.getLoginuserid());
        model.put("loginName", lub.getLoginname());
        model.put("ticketId", ticketId);
        model.put("encticketid", encticketid);
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/ticketActionDc";
    }

    @RequestMapping(value = "SaveonlineticketDc.htm", params = "save")
    public String SaveonlineticketDc(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        InputStream inputStream = null;
        String filePath = "";
        OutputStream outputStream = null;
        //TicketAttachment tktAttach = null;
        try {
            filePath = servletContext.getInitParameter("FilePath");
            MultipartFile ticketfile = onlineticket.getFile();
            fileName = System.currentTimeMillis() + "";
            String dirpath = filePath + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            outputStream = new FileOutputStream(dirpath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            inputStream = ticketfile.getInputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (ticketfile.getOriginalFilename() != null && !ticketfile.getOriginalFilename().equals("")) {
                // tktAttach = new TicketAttachment();
                onlineticket.setDfileName(fileName);
                onlineticket.setOfileName(ticketfile.getOriginalFilename());
                onlineticket.setFileType(ticketfile.getContentType());
                onlineticket.setFilePath(dirpath);
                onlineticket.setRefType("T-Ticket");
                //ticketAttachDAO.addAttachDocumentInfo(onlineticket);
            }

            onlineticket.setUserId(lub.getLoginuserid());
            onlineticket.setLoginId(lub.getLoginuserid());

            String usertypeStatus = onlineticket.getUserType();
            if (usertypeStatus != null && usertypeStatus.equals("HRMS Employee")) {
                onlineticket.setUsername(lub.getLoginname());
                onlineticket.setUserType("HRMS Employee");
            } else {
                onlineticket.setUsername(lub.getLoginname());
                onlineticket.setUserType("HRMS Staff(District Team)");
            }

            onlineTicketDAO.SaveonlineticketDc(onlineticket);
            outputStream.close();
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        String usertype = onlineticket.getUserType();
        if (usertype.equals("HRMS Employee")) {
            return "redirect:/onlineticketlist.htm";
        } else {
            return "redirect:/onlineticketlistDC.htm";
        }
    }

    @RequestMapping(value = "ticketEmpActionDc.htm", method = RequestMethod.GET)
    public String ticketEmpActionDc(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model, @RequestParam("ticketId") int ticketId) {
        //OnlineTicketing onLineTicketList = new OnlineTicketing();
        List onLineTicketList = onlineTicketDAO.ticketEmpActionDc(lub.getLoginuserid(), ticketId);
        model.put("loginName", lub.getLoginusername());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/ticketEmpActionDc";
    }

    @RequestMapping(value = "DownloadTicket")
    public void downloadTicket(HttpServletResponse response, @RequestParam("attId") int attId) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("FilePath");
            fa = onlineTicketDAO.downloadTicket(filepath, attId);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "onlineticketStatelist.htm", method = RequestMethod.GET)
    public String onlineticketStatelist(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (onlineticket.getTxtperiodFrom() != null && !onlineticket.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodFrom());
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            onlineticket.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
        if (onlineticket.getTxtperiodTo() != null && !onlineticket.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(onlineticket.getTxtperiodTo());
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            onlineticket.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }
        List onLineTicketList = onlineTicketDAO.onlineticketStatelist(onlineticket.getTxtperiodFrom(), onlineticket.getTxtperiodTo());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/onlineticketStatelist";
    }

    @RequestMapping(value = "ticketActionState.htm", method = RequestMethod.GET)
    public String ticketActionState(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model, @RequestParam("ticketId") int ticketId) {
        //OnlineTicketing onLineTicketList = new OnlineTicketing();
        List onLineTicketList = onlineTicketDAO.ticketActionState(ticketId);
        model.put("loginId", lub.getLoginusername());
        model.put("loginName", lub.getLoginusername());
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/ticketStateActionDc";
    }

    @RequestMapping(value = "SaveonlineticketState.htm", params = "save")
    public String SaveonlineticketState(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        InputStream inputStream = null;
        String filePath = "";
        OutputStream outputStream = null;
        //TicketAttachment tktAttach = null;
        try {
            filePath = servletContext.getInitParameter("FilePath");
            MultipartFile ticketfile = onlineticket.getFile();
            fileName = System.currentTimeMillis() + "";
            String dirpath = filePath + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            outputStream = new FileOutputStream(dirpath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            inputStream = ticketfile.getInputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (ticketfile.getOriginalFilename() != null && !ticketfile.getOriginalFilename().equals("")) {
                // tktAttach = new TicketAttachment();
                onlineticket.setDfileName(fileName);
                onlineticket.setOfileName(ticketfile.getOriginalFilename());
                onlineticket.setFileType(ticketfile.getContentType());
                onlineticket.setFilePath(dirpath);
                onlineticket.setRefType("T-Ticket");
                //ticketAttachDAO.addAttachDocumentInfo(onlineticket);
            }
            onlineticket.setUserId(lub.getLoginuserid());

            onlineTicketDAO.SaveonlineticketState(onlineticket);
            outputStream.close();
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        String usertype = onlineticket.getUserType();
        return "redirect:/onlineticketStatelist.htm";
    }

    @RequestMapping(value = "onlineticketByDc.htm", method = RequestMethod.GET)
    public ModelAndView onlineticketByDc(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        ModelAndView mv = null;
        OnlineTicketing onlineticket = new OnlineTicketing();
        onlineticket.setUsername(lub.getLoginname());
        mv = new ModelAndView("/onlineticketing/onlineticketByDc", "onlineticketing", onlineticket);

        List topiclist = ticketTopicDAO.getTicketTopicList();
        mv.addObject("topiclist", topiclist);
        String offcode = lub.getLoginempid();
        String distCode = offcode;
        mv.addObject("distCode", distCode);
        mv.addObject("deptCode", "");
        return mv;
    }

    @RequestMapping(value = "onlineticketByDDO.htm", method = RequestMethod.GET)
    public ModelAndView onlineticketByDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        ModelAndView mv = null;
        OnlineTicketing onlineticket = new OnlineTicketing();
        onlineticket.setUsername(lub.getLoginname());
        mv = new ModelAndView("/onlineticketing/onlineticketByDDO", "onlineticketing", onlineticket);

        List topiclist = ticketTopicDAO.getTicketTopicList();
        mv.addObject("topiclist", topiclist);
        String offcode = lub.getLoginempid();
        String distCode = offcode;
        mv.addObject("distCode", distCode);
        mv.addObject("deptCode", "");
        return mv;
    }

    @RequestMapping(value = "SaveonlineticketByDc.htm", params = "save")
    public String SaveonlineticketByDc(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "ticketId", required = false) String ticketId) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        InputStream inputStream = null;
        String filePath = "";
        OutputStream outputStream = null;
        //TicketAttachment tktAttach = null;
        try {
            filePath = servletContext.getInitParameter("FilePath");
            MultipartFile ticketfile = onlineticket.getFile();
            fileName = System.currentTimeMillis() + "";
            String dirpath = filePath + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            outputStream = new FileOutputStream(dirpath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            inputStream = ticketfile.getInputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (ticketfile.getOriginalFilename() != null && !ticketfile.getOriginalFilename().equals("")) {
                // tktAttach = new TicketAttachment();
                onlineticket.setDfileName(fileName);
                onlineticket.setOfileName(ticketfile.getOriginalFilename());
                onlineticket.setFileType(ticketfile.getContentType());
                onlineticket.setFilePath(dirpath);
                onlineticket.setRefType("T-Ticket");
                //ticketAttachDAO.addAttachDocumentInfo(onlineticket);
            }
            onlineticket.setUserId(lub.getLoginuserid());
            System.out.println("forwaredvalue;" + onlineticket.getForward());

            onlineTicketDAO.SaveonlineticketByDc(onlineticket);

            outputStream.close();
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return "redirect:/onlineticketlistDC.htm";
    }

    @RequestMapping(value = "SaveonlineticketByDDO.htm")
    public String SaveonlineticketByDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, BindingResult result, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "ticketId", required = false) String ticketId) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        InputStream inputStream = null;
        String filePath = "";
        OutputStream outputStream = null;
        //TicketAttachment tktAttach = null;
        try {
            filePath = servletContext.getInitParameter("FilePath");
            MultipartFile ticketfile = onlineticket.getFile();
            fileName = System.currentTimeMillis() + "";
            String dirpath = filePath + "/";
            File newfile = new File(dirpath);
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            outputStream = new FileOutputStream(dirpath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            inputStream = ticketfile.getInputStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            if (ticketfile.getOriginalFilename() != null && !ticketfile.getOriginalFilename().equals("")) {
                // tktAttach = new TicketAttachment();
                onlineticket.setDfileName(fileName);
                onlineticket.setOfileName(ticketfile.getOriginalFilename());
                onlineticket.setFileType(ticketfile.getContentType());
                onlineticket.setFilePath(dirpath);
                onlineticket.setRefType("T-Ticket");
                //ticketAttachDAO.addAttachDocumentInfo(onlineticket);
            }
            onlineticket.setUserId(lub.getLoginuserid());
            System.out.println("forwaredvalue; DDO" + onlineticket.getForward());

            onlineTicketDAO.SaveonlineticketByDDO(onlineticket);

            outputStream.close();
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return "redirect:/onlineticketlistDDO.htm";
    }

    @RequestMapping(value = "ticketTakeOver.htm", method = RequestMethod.GET)
    public String ticketTakeOver(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model, @RequestParam("ticketId") int ticketId) {
        //OnlineTicketing onLineTicketList = new OnlineTicketing();
        onlineTicketDAO.ticketTakeOver(ticketId, lub.getLoginusername());
        model.put("loginName", lub.getLoginusername());
        return "redirect:/onlineticketStatelist.htm";
    }

    @RequestMapping(value = "onlineTicketReport.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView onlineTicketReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("OnlineTicketing") OnlineTicketing onlineticket, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView();
        ArrayList distList = distDao.getDistrictList();

        mv.addObject("distList", distList);
        List onLineTicketList = onlineTicketDAO.reportOnlineTicketStatelist(onlineticket);
        mv.addObject("onlineticketlist", onLineTicketList);
        mv.setViewName("onlineticketing/onlineTicketReport");
        return mv;
    }

    @RequestMapping(value = "GetOnlineTicketReportPage")
    public String GetOnlineTicketReportPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month = month + 1;
        onlineticket.setMonth(month + "");
        onlineticket.setYear(year + "");
        // model.put("year", year);
        // model.put("month", month);
        // model.put("onlineticket", onlineticket);
        return "/onlineticketing/OnlineTicketCompleteReport";
    }

    @RequestMapping(value = "GetOnlineTicketReport")
    public String GetOnlineTicketReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket) {

        try {
            List onlineticketreportlist = onlineTicketDAO.getOnlineTicketDataReport(lub.getLogindistrictcode(), onlineticket);
            model.addAttribute("onlineticketreportlist", onlineticketreportlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/onlineticketing/OnlineTicketCompleteReport";
    }

    @RequestMapping(value = "GetDCMISReport.htm")
    public String GetDCMISReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) {
        if (onlineticket.getIntMonth() == 0) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            month = month + 1;
            onlineticket.setIntMonth(month);
        }
        if (onlineticket.getIntYear() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            onlineticket.setIntYear(year);
        }
        List onLineTicketList = onlineTicketDAO.misreportforDC(onlineticket.getIntMonth(), onlineticket.getIntYear());
        // System.out.println("size="+onLineTicketList.size());
        model.put("onlineticketlist", onLineTicketList);
        return "/onlineticketing/districtwisereport";
    }

    @RequestMapping(value = "GetStateMISReport.htm")
    public String GetStateMISReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) {
        if (onlineticket.getIntMonth() == 0) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            month = month + 1;
            onlineticket.setIntMonth(month);
        }
        if (onlineticket.getIntYear() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            onlineticket.setIntYear(year);
        }
        List onLineTicketList = onlineTicketDAO.GetStateMISReport(onlineticket.getIntMonth(), onlineticket.getIntYear());
        // System.out.println("size="+onLineTicketList.size());
        model.put("onlineticketlist", onLineTicketList);
        return "/onlineticketing/statewisereport";
    }

    @RequestMapping(value = "viewActionDetails.htm", method = RequestMethod.GET)
    public String viewActionDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model, @RequestParam("ticketId") int ticketId) {
        //OnlineTicketing onLineTicketList = new OnlineTicketing();

        List onLineTicketList = onlineTicketDAO.viewActionDetails(lub.getLogindistrictcode(), ticketId, lub.getLoginuserid());
        model.put("loginId", lub.getLoginuserid());
        model.put("loginName", lub.getLoginname());
        model.put("ticketId", ticketId);
        model.put("onlineticketlist", onLineTicketList);
        return "onlineticketing/viewActionDetails";
    }

    @RequestMapping(value = "ticketSubTopicList")
    @ResponseBody
    public void ticketSubTopicListJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String topicId = parameters.get("topicId");
            /*List subtopiclist = ticketTopicDAO.getTcketSubTopicList(topicId);
             JSONArray arr = new JSONArray(subtopiclist);
             JSONObject jobj = new JSONObject();
             jobj.put("subtopiclist", arr);
             out.write(jobj.toString());*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "GetDCPendingTicketlist.htm")
    public String GetDCPendingTicketlist(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("onlineticketing") OnlineTicketing onlineticket, Map<String, Object> model) {
        List onLineTicketList = onlineTicketDAO.getDCPendingTicketlist();

        model.put("onlineticketlist", onLineTicketList);
        return "/onlineticketing/dcPendingTicketlist";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.qtrallotment;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.qtrallotment.QuarterAllotmentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.qtrallotment.QuarterAllotment;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj","LoginUserBean"})
public class QuarterAllotmentController {

    @Autowired
    QuarterAllotmentDAO quarterAllotmentDAO;
    private Object requestParams;

    @RequestMapping(value = "QuarterAllotment")
    public String QuarterAllotmentList(@ModelAttribute("SelectedEmpObj") Users lub) {
        return "/quarterAllotment/QuarterAllotmentList";
    }

    @RequestMapping(value = "GetQuarterAllotmentListJSON")
    public void GetQuarterAllotmentListJSON(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("page") int page) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        List quarterqllotmentlist = null;
        int quarterqllotmentlistcnt = 0;
        int maxlimit = 10;
        int minlimit = maxlimit * (page - 1);
        try {
            quarterqllotmentlist = quarterAllotmentDAO.QuaterAllotSt(selectedEmpObj.getEmpId());
            json.put("total", quarterqllotmentlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveQuaterAllotmentData")
    public void saveQuaterAllotmentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionChargeForm") QuarterAllotment quarterAllotmentForm) throws ParseException {

        response.setContentType("application/json");
        PrintWriter out = null;
        int quarterqllotment = 0;

        try {
            quarterAllotmentForm.setEmpid(selectedEmpObj.getEmpId());
            quarterqllotment = quarterAllotmentDAO.saveQuaterAllotmentRecord(quarterAllotmentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "saveQuaterSurrenderData")
    public void saveQuaterSurrenderData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("additionChargeForm") QuarterAllotment quarterAllotmentForm) throws ParseException {

        response.setContentType("application/json");
        PrintWriter out = null;
        int quartersurrender = 0;

        try {
            quarterAllotmentForm.setEmpid(selectedEmpObj.getEmpId());
            quartersurrender = quarterAllotmentDAO.saveQuaterSurrenderRecord(quarterAllotmentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "editquarterAllotment")
    public void editquarterAllotment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        QuarterAllotment quarterqllotmentform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            String quarterqllotmentId = requestParams.get("qtrAllotId");
            quarterqllotmentform = quarterAllotmentDAO.editQuarterAllotment(quarterqllotmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "editgetSurrender")
    public void editgetSurrender(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        QuarterAllotment quarterqllotmentform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            String quarterqllotmentId = requestParams.get("qtrSurId");
            quarterqllotmentform = quarterAllotmentDAO.getSurrenderEditRecords(quarterqllotmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "deleteqtrAllot")
    public String deleteqtrAllot(@RequestParam("qtrid") String qtrid, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        String empid = selectedEmpObj.getEmpId();
        quarterAllotmentDAO.deleteQtrAllot(qtrid, empid);
        
        return "/quarterAllotment/QuarterAllotmentList";
    }

    @RequestMapping(value = "deleteqtrsurrendRecords")
    public String deleteqtrsurrendRecords(@RequestParam("qtrid") String qtrid, @RequestParam("surid") String surid, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        String empid = selectedEmpObj.getEmpId();
        quarterAllotmentDAO.deleteQtrSurrendRecords(qtrid, surid, empid);
        
        return "/quarterAllotment/QuarterAllotmentList";
    }

    @RequestMapping(value = "searchQuarterDetails")
    public ModelAndView searchQuarterDetails(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        String empid = lub.getLoginempid();
       
       
        ModelAndView mv = new ModelAndView();
        int year;     
       
        if (requestParams.get("year") != null) {
            mv.addObject("year", requestParams.get("year"));
            String yearData=requestParams.get("year");
            year = Integer.parseInt(yearData);
        } else {
           year = Calendar.getInstance().get(Calendar.YEAR);            
            mv.addObject("year", year);
          
        }
        String path = "/quarterAllotment/searchQuarterDetails";
        mv.addObject("empdetails", quarterAllotmentDAO.quarterempdetails(empid,year));
        mv.setViewName(path);
        return mv;
    }
    @RequestMapping(value = "downloadEmpQuarterStatus.htm")
    public void downloadEmpApplication(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        String empid = lub.getLoginempid();
        String empGpfNo = lub.getLogingpfno();
        String empName = lub.getLoginname();
        ModelAndView mv = new ModelAndView();
        int year;     
       
        if (requestParams.get("year") != null) {
            mv.addObject("year", requestParams.get("year"));
            String yearData=requestParams.get("year");
            year = Integer.parseInt(yearData);
        } else {
           year = Calendar.getInstance().get(Calendar.YEAR);            
            mv.addObject("year", year);
          
        }

        try {
            response.setHeader("Content-Disposition", "attachment; filename=empQuarterStatusPdf_" + year + ".pdf");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            document.open();
            
            quarterAllotmentDAO.downloadApplicationPdf(writer, document, empid,year,empGpfNo,empName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}

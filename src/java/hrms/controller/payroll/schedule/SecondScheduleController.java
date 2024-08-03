package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.schedule.SecondScheduleBean;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class SecondScheduleController {

    @Autowired
    public ScheduleDAOImpl comonScheduleDao;

    @RequestMapping(value = "SecondSchedulePage")
    public ModelAndView secondSchedulePage(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        SecondScheduleBean secondschlBean = null;
        ModelAndView mav = null;
        try {
            String isDuplicate = comonScheduleDao.isDuplicatePayRevisionOption(lub.getLoginempid());
            
            if (isDuplicate.equalsIgnoreCase("IAS")) {
                mav = new ModelAndView();
                secondschlBean = comonScheduleDao.getSecondScheduleData(lub.getLoginempid());
                mav.addObject("SecondSchlBean", secondschlBean);
                mav.setViewName("/payroll/schedule/ScondScheduleIAS");
            } else if (isDuplicate.equalsIgnoreCase("UGC")) {
                mav = new ModelAndView();
                secondschlBean = comonScheduleDao.getSecondScheduleData(lub.getLoginempid());
                mav.addObject("SecondSchlBean", secondschlBean);
                mav.setViewName("/payroll/schedule/SecondScheduleUGC");
            }else if (isDuplicate.equalsIgnoreCase("N")) {
                mav = new ModelAndView();
                secondschlBean = comonScheduleDao.getSecondScheduleData(lub.getLoginempid());
                mav.addObject("isDuplicate", isDuplicate);
                mav.addObject("SecondSchlBean", secondschlBean);
                mav.setViewName("/payroll/schedule/SecondSchedule");
            } else if (isDuplicate.equalsIgnoreCase("Y")) {
                mav = new ModelAndView("redirect:/SecondSchedulePDF.htm");
            } else if (isDuplicate.equalsIgnoreCase("IASE")) {
                mav = new ModelAndView("redirect:/SecondScheduleIASPDF.htm");
            } else if (isDuplicate.equalsIgnoreCase("UGCE")) {
                mav = new ModelAndView("redirect:/SecondScheduleUGCPDF.htm");
            }else {
                mav = new ModelAndView();
                mav.addObject("isDuplicate", isDuplicate);
                mav.setViewName("/payroll/schedule/SecondSchedule");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "SecondScheduleForm")
    public ModelAndView secondScheduleForm(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;

        String postcode = "";
        String payscale = "";
        int gp = 0;
        try {
            String isDuplicate = comonScheduleDao.isDuplicatePayRevisionOption(lub.getLoginempid());
            if (isDuplicate.equalsIgnoreCase("N") || isDuplicate.equalsIgnoreCase("IAS") || isDuplicate.equalsIgnoreCase("UGC")) {

                String optionSelected = (String) requestParams.get("rdForm");

                String postcode1 = (String) requestParams.get("hidPostCode1");
                String postcode2 = (String) requestParams.get("hidPostCode2");

                String payscale1 = (String) requestParams.get("payscale1");
                String payscale2 = (String) requestParams.get("payscale2");

                String gp1 = (String) requestParams.get("gp1");
                String gp2 = (String) requestParams.get("gp2");

                if (optionSelected.equals("1")) {
                    if (!isDuplicate.equalsIgnoreCase("IAS")) {
                        postcode = postcode1;
                        payscale = payscale1;
                        gp = Integer.parseInt(gp1);
                    }
                } else if (optionSelected.equals("2")) {
                    postcode = postcode2;
                    payscale = payscale2;
                    gp = Integer.parseInt(gp2);
                }

                String enteredDate = (String) requestParams.get("txtDate");

                String hasUserChanged = (String) requestParams.get("hasUserChanged");
                String hasDDOChanged = (String) requestParams.get("hasDDOChanged");

                comonScheduleDao.insertPayRevisionData(lub.getLoginempid(), lub.getLoginoffcode(), optionSelected, postcode, payscale, gp, enteredDate, hasUserChanged, hasDDOChanged);

                mav = new ModelAndView("redirect:/SecondSchedulePDF.htm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SecondSchedulePDF")
    public void secondSchedulePDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            comonScheduleDao.secondSchedulePDF(document, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "SecondScheduleIASPDF")
    public void secondScheduleIASPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            comonScheduleDao.secondScheduleIASPDF(document, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
    @RequestMapping(value = "SecondScheduleUGCPDF")
    public void SecondScheduleUGCPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            comonScheduleDao.secondScheduleUGCPDF(document, lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}

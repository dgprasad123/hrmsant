/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;


import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.UserExpertiseDAO;
import hrms.model.login.LoginUserBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.HeaderFooter;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes("LoginUserBean")
public class ExpertiseController implements ServletContextAware {

    @Autowired
    public UserExpertiseDAO userExpertiseDAO;

    private ServletContext context;

    private Object session;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "GetManageExpertiseList")
    public String getManageExpertiseList() {

        return "/tab/ManageExpertise";
    }

    @ResponseBody
    @RequestMapping(value = "GetManageExpertiseListJSON")
    public String getManageExpertiseListJSON(HttpServletResponse response, @RequestParam("page") int page, @RequestParam("rows") int rows) {

        response.setContentType("application/json");

        JSONObject json = new JSONObject();

        int maxlimit = rows;
        int minlimit = rows * (page - 1);

        int total = 0;

        try {
            List expertiselist = userExpertiseDAO.getUserExpertiseList(maxlimit, minlimit);
            total = userExpertiseDAO.getUserExpertiseCount();

            json.put("rows", expertiselist);
            json.put("total", total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "ViewExpertiseList")
    public String viewExpertiseList(HttpServletResponse response) {

        response.setContentType("application/html");
        String content = null;
        try {
            content = userExpertiseDAO.ViewExpertiseList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @RequestMapping(value = "downloadExpertisePDF", method = RequestMethod.GET)
    public void downloadExpertisePDF(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        response.setContentType("application/pdf");
        Document document = null;

        PdfWriter writer = null;

        String htmlContent = new String();
        String str = null;
        try {
            document = new Document(PageSize.A4);

            response.setHeader("Content-Disposition", "attachment; filename=Expertise_" + lub.getLoginempid() + ".pdf");
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            //writer.setPageEvent(new HeaderFooter(lub.getEmpid()));
            document.open();

            URL pdfurl = new URL("https://apps.hrmsodisha.gov.in/ViewExpertiseList.htm");

            String sessionid = RequestContextHolder.getRequestAttributes().getSessionId();
            

            HttpURLConnection urlConn = null;
            urlConn = (HttpURLConnection) pdfurl.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Cookie", "JSESSIONID=" + sessionid);

            InputStreamReader fis = new InputStreamReader(urlConn.getInputStream());

            BufferedReader d = new BufferedReader(fis);

            while ((str = d.readLine()) != null) {
                htmlContent += str.trim();
            }
            
            StringReader strReader = new StringReader(StringUtils.defaultString(htmlContent));

            /*XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
             fontImp.register(FONT);*/
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(writer, document, strReader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}

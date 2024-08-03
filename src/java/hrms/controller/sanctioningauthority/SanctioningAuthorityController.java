package hrms.controller.sanctioningauthority;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.model.sancauthority.SancAuthorityForm;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SanctioningAuthorityController {

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @RequestMapping(value = "sancauthority.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchAuthority(@ModelAttribute("sancAuthorityForm") SancAuthorityForm saf, Map<String, Object> model) {
        model.put("sancAuthorityForm", saf);
        return "/sanctioningauthority/SanctionAuthorityEdit";
    }

    @ResponseBody
    @RequestMapping(value = "deptListJSON.htm", method = RequestMethod.POST)
    public void GetDeptListJSON(HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        List deptlist = null;
        try {
            deptlist = departmentDao.getDepartmentList();
            JSONArray json = new JSONArray(deptlist);
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
    @RequestMapping(value = "getSpcListJSON.htm", method = RequestMethod.POST)
    public void GetPostListJSON(HttpServletResponse response, @RequestParam("offcode") String offcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        List postlist = null;
        try {
            postlist = substantivePostDAO.getEmployeeWithSPCList(offcode);
            JSONArray json = new JSONArray(postlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}

package hrms.controller.GroupPayFixation;

import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.grouppayfixation.GroupPayFixation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class GroupPayFixationController {

    @Autowired
    public BillGroupDAO billGroupDAO;
    @Autowired
    public SectionDefinationDAO sectionDefinationDAO;

    @ResponseBody
    @RequestMapping(value = "getBillGrpNameJSON.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void billGrpCombo(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONArray json = null;
        PrintWriter out = null;
        try {
            List billGrpList = billGroupDAO.getBillGroupList(lub.getLoginoffcode());
            json = new JSONArray(billGrpList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "grouppayfixation.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String profileData(@ModelAttribute("emp") Employee emp, Map<String, Object> model) {
        try {
            model.put("emp", emp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return "/GroupPayFixation/GroupPayFixation";
    }

    @ResponseBody
    @RequestMapping(value = "emplist.htm", method = {RequestMethod.GET, RequestMethod.POST})

    public void getSpcList(@RequestParam("biliGrpId") String billGrpId, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        List spcList = null;

        try {

            List sectionid = billGroupDAO.getSectionList(billGrpId);
            GroupPayFixation secdef = null;
            for (int i = 0; i < sectionid.size(); i++) {
                secdef = (GroupPayFixation) sectionid.get(i);
                spcList = sectionDefinationDAO.getSPCList(secdef.getSectionId());
                for (int n = 0; n < spcList.size(); n++) {
                    secdef = (GroupPayFixation) spcList.get(n);
                }
            }
            json = new JSONObject(spcList);
            json.put("rows", spcList);
            json.put("total", 10);
            out = response.getWriter();
            out.write(json.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}

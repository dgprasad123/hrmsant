package hrms.controller.master;

import hrms.dao.master.LeaveActionTypeDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LeaveActionTypeController {

    @Autowired
    LeaveActionTypeDAO leaveActionTypeDAO;

    @RequestMapping(value = "getActionType.htm", method = RequestMethod.POST)
    public @ResponseBody
    void getActionType(HttpServletRequest request, HttpServletResponse response) {
        JSONArray json = null;
        PrintWriter out = null;
        try {
            ArrayList leaveActionType = leaveActionTypeDAO.getActionType();
            json = new JSONArray(leaveActionType);
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

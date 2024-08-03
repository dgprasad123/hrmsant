/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.DDOFeedback;

import hrms.dao.ddofeedback.DDOFeedbackDAO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;

/**
 *
 * @author lenovo pc
 */
@Controller
public class DDOFeedbackController {

    @Autowired
    public DDOFeedbackDAO ddofeedbackDAO;

    @RequestMapping(value = "ddofeedback.htm", method = {RequestMethod.GET})
    public ModelAndView getDDoFeedbackList(HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        ArrayList ddofeebacklist = ddofeedbackDAO.getDDdoFeedbackList();
        mav.addObject("feedbacklist", ddofeebacklist);
        mav.setViewName("/VisitingStatus/visitingstatus");
        return mav;
    }
}

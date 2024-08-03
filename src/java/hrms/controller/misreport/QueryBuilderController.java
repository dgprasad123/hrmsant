/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.misreport.QueryBuilderDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.misreport.QueryBuilderBean;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class QueryBuilderController {

    @Autowired
    QueryBuilderDAO queryBuilderDAO;

    static final Logger logger = LogManager.getLogger(QueryBuilderController.class);

    @RequestMapping(value = "viewQueryWindow", method = RequestMethod.GET)
    public ModelAndView viewQueryWindow(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("queryBuilderBean") QueryBuilderBean queryBuilderBean) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/misreport/querywindow");
        return mav;
    }

    @RequestMapping(value = "executeQueryCommand", method = RequestMethod.POST, params = "action=Execute")
    public ModelAndView executeQueryCommand(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("queryBuilderBean") QueryBuilderBean queryBuilderBean) {
        ModelAndView mav = new ModelAndView();
        try {
            List datalist = queryBuilderDAO.getResultSet(queryBuilderBean.getQuery());
            mav.addObject("datalist", datalist);
        } catch (Exception exp) {
            mav.addObject("errormsg", exp.getMessage());
        }
        mav.setViewName("/misreport/querywindow");
        return mav;
    }

    @RequestMapping(value = "executeQueryCommand", method = RequestMethod.POST, params = "action=Download")
    public ModelAndView downloadQueryData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("queryBuilderBean") QueryBuilderBean queryBuilderBean) {
        ModelAndView mav = new ModelAndView();
        try {
        List datalist = queryBuilderDAO.getResultSet(queryBuilderBean.getQuery());
        mav.addObject("datalist", datalist);
        mav.setViewName("queryDataExcel");
        } catch (Exception exp) {
            mav.addObject("errormsg", exp.getMessage());
            mav.setViewName("/misreport/querywindow");
        }        
        return mav;
    }
}

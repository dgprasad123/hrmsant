package hrms.controller.report.substantivepost;

import hrms.dao.master.OfficeDAO;
import hrms.dao.report.officewisepostgroup.OfficeWisePostGroupDAO;
import hrms.dao.report.substantivepostlist.SubstantivePostListDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.report.officewisepostgroup.OfficeWisePostGroup;
import hrms.model.report.substantivepost.SubstantivePost;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class SubstantivePostController {
    @Autowired
    SubstantivePostListDAO substantivePostListDao;
    @Autowired
    public OfficeDAO offDAO;
    @RequestMapping(value = "substantivepostOthers.htm")
    public ModelAndView postList(@ModelAttribute("substantivepostlist") SubstantivePost substantivepostlist, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        Office office = offDAO.getOfficeDetails(lub.getLoginoffcode());
        ArrayList postList = substantivePostListDao.getSubstantivePostList(lub.getLoginoffcode());
        mv = new ModelAndView("/report/SubstantivePostList", "substantivepostlist", substantivepostlist);
        mv.addObject("postList", postList);
        mv.addObject("offname", office.getOffEn());
        return mv;
    }
}

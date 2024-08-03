package hrms.controller.policemodule;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.policemodule.PoliceMISReportDAO;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.leave.EmployeeLeaveAccount;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.policemodule.PoliceMISReportForm;
import hrms.model.report.gispassbook.gisPassbook;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean","SelectedEmpObj"})
public class PoliceMISReportsController {

    @Autowired
    PoliceMISReportDAO policeMISReportDAO;

    @Autowired
    public PostDAO postDAO;

    @Autowired
    DistrictDAO districtDAO;

    @RequestMapping(value = "LocatePoliceEmployee")
    public ModelAndView LocatePoliceEmployee(@ModelAttribute("searchPolice") PoliceMISReportForm policeform, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        ModelAndView mv = new ModelAndView();

        String path = "under_const";

        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc") || lub.getLoginusertype().equalsIgnoreCase("S") || lub.getLoginuserid().contains("deo") || lub.getLoginusertype().contains("H")) {

            path = "/policemodule/misreports/SearchPoliceData";
            policeform.setUsertype(lub.getLoginusertype());

            mv.addObject("searchEmployee", policeform);

            List postlist = postDAO.getPostList("14");
            mv.addObject("postlist", postlist);

            if (policeform.getCriteria() != null && !policeform.getCriteria().equals("")) {
                mv.addObject("empSearchResult", policeMISReportDAO.LocatePolice(policeform));
            }
        } else {
            path = "under_const";
        }
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "PolicePostingDetails")
    public ModelAndView PolicePostingDetails(@RequestParam("empid") String empid) {
        ModelAndView mv = new ModelAndView();

        PoliceMISReportForm policeform = new PoliceMISReportForm();
        policeform.setCriteria("HRMSID");
        policeform.setSearchString(empid);

        mv.addObject("empSearchResult", policeMISReportDAO.LocatePolice(policeform));

        mv.addObject("PostingDetails", policeMISReportDAO.policePostingDetails(empid));

        mv.setViewName("/policemodule/misreports/PolicePostingDetails");
        return mv;
    }

    @RequestMapping(value = "PolicePostingListSameDistrict")
    public ModelAndView PolicePostingListSameDistrict(@ModelAttribute("searchPolice") PoliceMISReportForm policeform, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        ModelAndView mv = new ModelAndView();

        String path = "under_const";

        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc") || lub.getLoginusertype().equalsIgnoreCase("S") || lub.getLoginuserid().contains("deo") || lub.getLoginusertype().contains("H")) {

            path = "/policemodule/misreports/PolicePostingListSameDistrict";
            policeform.setUsertype(lub.getLoginusertype());

            mv.setViewName(path);

            mv.addObject("PolicePostingListSameDistrict", policeMISReportDAO.PolicePostingListSameDistrict(policeform));

            List postlist = postDAO.getPostList("14");
            mv.addObject("postlist", postlist);

            ArrayList districtList = districtDAO.getDistrictList();
            mv.addObject("districtList", districtList);
        } else {
            path = "under_const";
            mv.setViewName(path);
        }
        return mv;
    }

    @RequestMapping(value = "PolicePostingList")
    public ModelAndView PolicePostingList(@ModelAttribute("searchPolice") PoliceMISReportForm policeform, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        ModelAndView mv = new ModelAndView();

        String path = "under_const";

        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc") || lub.getLoginusertype().equalsIgnoreCase("S") || lub.getLoginuserid().contains("deo") || lub.getLoginusertype().contains("H")) {

            path = "/policemodule/misreports/PolicePostingList";
            policeform.setUsertype(lub.getLoginusertype());

            mv.setViewName(path);

            if ((policeform.getSltRank() != null && !policeform.getSltRank().equals("")) || (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals(""))) {
                mv.addObject("PolicePostingList", policeMISReportDAO.PolicePostingList(policeform));
            }

            List postlist = postDAO.getPostList("14");
            mv.addObject("postlist", postlist);

            List establishmentList = policeMISReportDAO.getPoliceEstablishmentOfficeList();
            mv.addObject("establishmentList", establishmentList);
        } else {
            path = "under_const";
            mv.setViewName(path);
        }
        return mv;
    }

    @RequestMapping(value = "DownloadPolicePoliceListExcel")
    public void DownloadPolicePoliceListExcel(HttpServletResponse response, @ModelAttribute("searchPolice") PoliceMISReportForm policeform, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        String path = "under_const";

        try {
            String fileName = "Police_Posting_List";

            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);

            List policepostinglist = null;

            if ((policeform.getSltRank() != null && !policeform.getSltRank().equals("")) || (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals(""))) {
                policepostinglist = policeMISReportDAO.PolicePostingList(policeform);
            }
            policeMISReportDAO.generatePolicePostingListExcel(policepostinglist, workbook, fileName);

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "PoliceServiceBook")
    public ModelAndView PoliceServiceBook(@RequestParam("empid") String empid) {
        ModelAndView mav = new ModelAndView();
        try {
            Users selectedEmpObj = new Users();
            selectedEmpObj.setEmpId(empid);
            mav.addObject("SelectedEmpObj", selectedEmpObj);
            mav.setViewName("redirect:/serviceBookPrePage.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.miscellaneous;

import hrms.common.CommonFunctions;
import hrms.dao.changepassword.ChangePasswordDAOImpl;
import hrms.dao.master.DepartmentDAOImpl;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.miscellaneous.DeptDataEntryDAO;
import hrms.dao.master.OfficeDAOImpl;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.miscellaneous.Bleo;
import hrms.model.miscellaneous.CommissionCourtCase;
import hrms.model.miscellaneous.CommissionPending;
import hrms.model.miscellaneous.Rascheme;
import hrms.model.miscellaneous.RecruitmentDrive;
import hrms.model.miscellaneous.ScStRecruitment;
import hrms.model.miscellaneous.SelectedCandidates;
import hrms.model.miscellaneous.SelectedCandidatesCategory;
import hrms.model.miscellaneous.Training;
import hrms.model.miscellaneous.VacantPost;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes("LoginUserBean")
public class DeptDataEntry {

    @Autowired
    DeptDataEntryDAO deptDataEntryDAO;
    @Autowired
    CadreDAO cadreDAO;
    @Autowired
    PostDAO postDAO;
    @Autowired
    public ChangePasswordDAOImpl changepwdDao;
    @Autowired
    public DepartmentDAOImpl deptDao;
    @Autowired
    public OfficeDAOImpl officeDao;

    @RequestMapping(value = "changePasswordmis")
    public String changePasswordmis(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        // String deptId=lub.getLoginempid();
        //model.addAttribute("bleoList", deptDataEntryDAO.getBleoList(deptId));
        return "/miscellaneous/changepassword";
    }

    @RequestMapping(value = "changePasswordmisAction")
    public ModelAndView showchangepassword(ModelMap model, @ModelAttribute("loginForm") Users loginForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws SQLException {
        ModelAndView mav = new ModelAndView();
        String path = "/miscellaneous/changepassword";
        int pwdstatus = 0;
        if (loginForm.getUserPassword() != null && !loginForm.getUserPassword().equals("")) {
            if (loginForm.getNewpassword().equalsIgnoreCase(loginForm.getConfirmpassword())) {
                pwdstatus = changepwdDao.modifyUserPassword(lub.getLoginempid(), lub.getLoginusertype(), loginForm.getUserPassword(), loginForm.getNewpassword(),lub.getLoginuserid());
                if (pwdstatus == 1) {
                    mav.addObject("message", "password changed successfully");
                } else if (pwdstatus == 0) {
                    mav.addObject("message", "Invalid Password");
                } else {
                    mav.addObject("message", "Password does not meet the policy");
                }
            } else {
                mav.addObject("message", "New Password does not Match with Confirm Password");
            }
        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "getBleoList")
    public String getBleoList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String deptId = lub.getLoginempid();
        model.addAttribute("bleoList", deptDataEntryDAO.getBleoList(deptId));
        return "/miscellaneous/bleolist";
    }

    @RequestMapping(value = "getDeptvacantpostList")
    public String getDeptvacantpostList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {
        String deptId = lub.getLoginempid();
        String uname = lub.getLoginusertype();
        if (uname.equals("Dept")) {
            deptDataEntryDAO.updateMonthlyVacantPost(deptId, month, year);

        }
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);

        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
        if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        
        model.addAttribute("misStatus", misStatus);
        model.addAttribute("vacantPostList", deptDataEntryDAO.getVacantPost(deptId, month, year));

        return "/miscellaneous/deptvacantpostlist";

    }

    @RequestMapping(value = "getRaschemeList")
    public String getRaschemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String deptId = lub.getLoginempid();
        model.addAttribute("raschemeList", deptDataEntryDAO.getRaschemeList(deptId));
        return "/miscellaneous/raschemelist";

    }

    @RequestMapping(value = "getRecruitmentDriveList")
    public String getRecruitmentDriveList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String deptId = lub.getLoginempid();
        model.addAttribute("recruitmentList", deptDataEntryDAO.getRecruitDriveList(deptId));
        return "/miscellaneous/recruitmentDriveList";

    }

    @RequestMapping(value = "getScstRecruitmentList")
    public String getScstRecruitmentList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String deptId = lub.getLoginempid();
        model.addAttribute("scStRecruitmentList", deptDataEntryDAO.getScStRecruitmentList(deptId));
        return "/miscellaneous/scstrecruitmentlist";

    }

    @RequestMapping(value = "getTrainingList")
    public String getTrainingList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String deptId = lub.getLoginempid();
        model.addAttribute("trainingList", deptDataEntryDAO.getTrainingList(deptId));
        return "/miscellaneous/traininglist";

    }

    @RequestMapping(value = "saveBleo", method = RequestMethod.POST)
    public String saveBleo(ModelMap model, @ModelAttribute("bleoForm") Bleo bleoForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/bleolist";
        String deptId = lub.getLoginempid();
        deptDataEntryDAO.saveBleo(bleoForm, deptId);

        model.addAttribute("bleoList", deptDataEntryDAO.getBleoList(deptId));
        return path;
    }

    @RequestMapping(value = "saveDeptvacantpost", method = RequestMethod.POST)
    public ModelAndView saveDeptvacantpost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("bleoForm") VacantPost vacantPostForm, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {//        
        String path = "/miscellaneous/deptvacantpostlist";
        vacantPostForm.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.saveDeptvacantpost(vacantPostForm);
        String deptId = lub.getLoginempid();
        // model.addAttribute("vacantPostList", deptDataEntryDAO.getVacantPost(deptId));
        //   return path;
        return new ModelAndView("redirect:/getDeptvacantpostList.htm?month=" + month + "&year=" + year + "&deptid=" + deptId);

    }

    @RequestMapping(value = "updateDeptvacantpost", method = RequestMethod.POST)
    public ModelAndView updateDeptvacantpost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("bleoForm") VacantPost vacantPostForm, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {//        
        String path = "/miscellaneous/entryDetailsList";
        vacantPostForm.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.updateVacantPostData(vacantPostForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("vacantPostList", deptDataEntryDAO.entryDetailsList(month, year, deptId));
        // return path;
        return new ModelAndView("redirect:/getDeptvacantpostList.htm?month=" + month + "&year=" + year + "&deptid=" + deptId);
    }

    @RequestMapping(value = "saveRascheme", method = RequestMethod.POST)
    public String saveRascheme(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("raschemeForm") Rascheme raschemeForm, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/raschemelist";
        raschemeForm.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.saveRascheme(raschemeForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("raschemeList", deptDataEntryDAO.getRaschemeList(deptId));
        return path;
    }

    @RequestMapping(value = "saveScStRecruitment", method = RequestMethod.POST)
    public ModelAndView saveScStRecruitment(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("scstRecruitmentForm") ScStRecruitment scstRecruitment, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/scstrecruitmentlist";
        scstRecruitment.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.saveScStRecruitment(scstRecruitment);
        String deptId = lub.getLoginempid();
        model.addAttribute("scstRecruitmentList", deptDataEntryDAO.getScStRecruitmentList(deptId));
        // return path; 
        return new ModelAndView("redirect:/getScstRecruitmentList.htm");
    }

    @RequestMapping(value = "saveTraining", method = RequestMethod.POST)
    public String saveTraining(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empTrainingForm") Training training, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/traininglist";
        training.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.saveTraining(training);
        String deptId = lub.getLoginempid();
        model.addAttribute("trainingList", deptDataEntryDAO.getTrainingList(deptId));
        return path;
    }

    @RequestMapping(value = "saveRecruitDriveData", method = RequestMethod.POST)
    public String saveRecruitDriveData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recruitDriveForm") RecruitmentDrive recruitDrive, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/recruitmentDriveList";
        recruitDrive.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.saveRecruitDriveData(recruitDrive);
        String deptId = lub.getLoginempid();
        model.addAttribute("recruitmentList", deptDataEntryDAO.getRecruitDriveList(deptId));
        return path;
    }

    @RequestMapping(value = "newBleo")
    public String newBleo(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.put("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        return "/miscellaneous/bleo";
    }

    @RequestMapping(value = "newDeptvacantpost")
    public String newDeptvacantpost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        model.addAttribute("loginDeptId", lub.getLoginempid());
        String deptId = lub.getLoginempid();
        model.addAttribute("vacantPostList", deptDataEntryDAO.getVacantPost(deptId, "May", "2019"));
        return "/miscellaneous/dept_vacant_post";
    }

    @RequestMapping(value = "newRascheme")
    public String newRascheme(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "/miscellaneous/ra_scheme";

    }

    @ResponseBody
    @RequestMapping(value = "getDeptwisePostList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getDeptwisePostList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("deptId") String deptId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = null;

            li = postDAO.getPostList(deptId);

            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "newRecruitment")
    public String newRecruitment(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        return "/miscellaneous/recruitment_drive";

    }

    @RequestMapping(value = "newScstRecruitment")
    public String newScstRecruitment(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        return "/miscellaneous/sc_st_recruitment";

    }

    @RequestMapping(value = "newTraining")
    public String newTraining(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("DeInstList", deptDataEntryDAO.getDEInstituteList());
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        return "/miscellaneous/training";

    }

    @RequestMapping(value = "editvacantpost")
    public String editvacantpost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        //VacantPost vacantPost = null;
        VacantPost vacantPost = new VacantPost();
        vacantPost = deptDataEntryDAO.editVacantPost(taskid);
        model.addAttribute("VacantPost", vacantPost);
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("postList", postDAO.getPostList(lub.getLoginempid()));
        //    model.addAttribute("officeList", officeDao.getOfficeList(lub.getLoginempid()));
        model.addAttribute("loginDeptId", lub.getLoginempid());
        String deptId = lub.getLoginempid();
        model.addAttribute("vacantPostList", deptDataEntryDAO.getVacantPost(deptId, "May", "2019"));
        return "/miscellaneous/editVacantPost";

    }

    @RequestMapping(value = "editscstRecruitment")
    public String editscstRecruitment(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        //VacantPost vacantPost = null;
        ScStRecruitment appoint = new ScStRecruitment();
        appoint = deptDataEntryDAO.editRecruitment(taskid);
        model.addAttribute("Appoint", appoint);
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        return "/miscellaneous/editscstRecruitment";

    }

    @RequestMapping(value = "updateScStRecruitment", method = RequestMethod.POST)
    public ModelAndView updateScStRecruitment(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("scstRecruitmentForm") ScStRecruitment scstRecruitment, BindingResult result, HttpServletResponse response) {//        
        //VacantPost vacantPost = null;
        String path = "/miscellaneous/scstrecruitmentlist";
        scstRecruitment.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.updateRecruitmentData(scstRecruitment);
        String deptId = lub.getLoginempid();
        model.addAttribute("scstRecruitmentList", deptDataEntryDAO.getScStRecruitmentList(deptId));
        return new ModelAndView("redirect:/getScstRecruitmentList.htm");

    }

    @RequestMapping(value = "editAppointmentStatus")
    public String editAppointmentStatus(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("raschemeForm") Rascheme raschemeForm, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        raschemeForm = deptDataEntryDAO.editAppointmentStatus(taskid);
        model.addAttribute("appointStatus", raschemeForm);
        return "/miscellaneous/editRascheme";

    }

    @RequestMapping(value = "updateRascheme", method = RequestMethod.POST)
    public String updateRascheme(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("raschemeForm") Rascheme raschemeForm, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/raschemelist";
        raschemeForm.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.updateAppointmentStatusData(raschemeForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("raschemeList", deptDataEntryDAO.getRaschemeList(deptId));
        return path;
    }

    @RequestMapping(value = "editRecruitDrive")
    public String editRecruitDrive(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("recruitDriveForm") RecruitmentDrive recruitDriveForm, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        recruitDriveForm = deptDataEntryDAO.editRecruitDrive(taskid);
        model.addAttribute("RecruitDrive", recruitDriveForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        return "/miscellaneous/editRecruitmentDrive";

    }

    @RequestMapping(value = "updateRecruitDriveData", method = RequestMethod.POST)
    public String updateRecruitDriveData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("recruitDriveForm") RecruitmentDrive recruitDriveForm, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/recruitmentDriveList";
        recruitDriveForm.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.updateRecruitDriveData(recruitDriveForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("recruitmentList", deptDataEntryDAO.getRecruitDriveList(deptId));
        return path;
        //  return path; 
    }

    @RequestMapping(value = "editEmpStrength")
    public String editRecruitDrive(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("bleoForm") Bleo bleoForm, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        bleoForm = deptDataEntryDAO.editEmpStrength(taskid);
        model.addAttribute("bleoForm", bleoForm);
        String deptId = lub.getLoginempid();
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        return "/miscellaneous/editbleo";

    }

    @RequestMapping(value = "updateBleo", method = RequestMethod.POST)
    public String updateBleo(ModelMap model, @ModelAttribute("bleoForm") Bleo bleoForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/bleolist";
        String deptId = lub.getLoginempid();
        deptDataEntryDAO.updateEmpStrengthData(bleoForm);

        model.addAttribute("bleoList", deptDataEntryDAO.getBleoList(deptId));
        return path;
    }

    @RequestMapping(value = "editEmpTraining")
    public String editEmpTraining(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("empTrainingForm") Training training, HttpServletResponse response, @RequestParam("Taskid") String taskid) {
        training = deptDataEntryDAO.editEmpTraining(taskid);
        model.addAttribute("trainingForm", training);
        String deptId = lub.getLoginempid();
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("DeInstList", deptDataEntryDAO.getDEInstituteList());
        return "/miscellaneous/editTraining";

    }

    @RequestMapping(value = "updateTraining", method = RequestMethod.POST)
    public String updateTraining(ModelMap model, @ModelAttribute("empTrainingForm") Training training, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//        
        String path = "/miscellaneous/traininglist";
        training.setDeptId(lub.getLoginempid());
        deptDataEntryDAO.updateEmpTrainingData(training);
        String deptId = lub.getLoginempid();
        model.addAttribute("trainingList", deptDataEntryDAO.getTrainingList(deptId));
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getCadrewisePostList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getCadrewisePostList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("cadreId") String cadreId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = null;
            if (cadreId.equals("0")) {
                li = postDAO.getPostList(lub.getLoginempid());
            } else {
                li = postDAO.getCadrewisePostList(lub.getLoginempid(), cadreId);
            }

            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "deleteVacantPost")
    public ModelAndView deleteVacantPost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("bleoForm") VacantPost vacantPostForm, @RequestParam("year") String year, @RequestParam("month") String month) {
        deptDataEntryDAO.deleteVacantPost(taskId);
        String path = "/miscellaneous/deptvacantpostlist";

        String deptId = lub.getLoginempid();
        //  model.addAttribute("vacantPostList", deptDataEntryDAO.getVacantPost(deptId));
        //   return path;
        return new ModelAndView("redirect:/getDeptvacantpostList.htm?month=" + month + "&year=" + year + "&deptid=" + deptId);
    }

    @RequestMapping(value = "deleteAppointmentStatus")
    public String deleteAppointmentStatus(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("raschemeForm") Rascheme raschemeForm) {
        deptDataEntryDAO.deleteAppointmentStatus(taskId);
        String path = "/miscellaneous/raschemelist";
        String deptId = lub.getLoginempid();
        model.addAttribute("raschemeList", deptDataEntryDAO.getRaschemeList(deptId));
        return path;

    }

    @RequestMapping(value = "deleteRecruitmentData")
    public ModelAndView deleteRecruitmentData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("scstRecruitmentForm") ScStRecruitment scstRecruitment) {
        deptDataEntryDAO.deleteRecruitment(taskId);
        //String path = "/miscellaneous/scstrecruitmentlist";      
        String deptId = lub.getLoginempid();
        model.addAttribute("scstrecruitmentlist", deptDataEntryDAO.getScStRecruitmentList(deptId));
        return new ModelAndView("redirect:/getScstRecruitmentList.htm");
        // return path;
    }

    @RequestMapping(value = "deleteRecruitDriveData")
    public String deleteRecruitDriveData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("recruitDriveForm") RecruitmentDrive recruitDriveForm) {
        deptDataEntryDAO.deleteRecruitDrive(taskId);
        String path = "/miscellaneous/recruitmentDriveList";
        String deptId = lub.getLoginempid();
        model.addAttribute("recruitmentList", deptDataEntryDAO.getRecruitDriveList(deptId));
        return path;
    }

    @RequestMapping(value = "deleteEmpTrainingData")
    public String deleteEmpTrainingData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("empTrainingForm") Training training) {
        deptDataEntryDAO.deleteEmpTraining(taskId);
        String path = "/miscellaneous/traininglist";
        String deptId = lub.getLoginempid();
        model.addAttribute("trainingList", deptDataEntryDAO.getTrainingList(deptId));
        return path;
    }

    @RequestMapping(value = "deleteEmpStrengthData")
    public String deleteEmpStrengthData(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") String taskId, @ModelAttribute("bleoForm") Bleo bleoForm) {
        deptDataEntryDAO.deleteEmpStrength(taskId);
        String path = "/miscellaneous/bleolist";
        String deptId = lub.getLoginempid();
        model.addAttribute("bleoList", deptDataEntryDAO.getBleoList(deptId));
        return path;
    }

    /**
     * ****************************************** Manoj
     * **************************************************
     */
    //added by me
    @RequestMapping(value = "AddSelectedCandidateCategory")
    public String addSelectedCandidateCategory(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/AddSelectedCandidateCategory";
    }

    @ResponseBody
    @RequestMapping(value = "getDeptewiseCadreList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getDeptewiseCadreList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletRequest request, HttpServletResponse response, @RequestParam("deptId") String deptId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = null;
            if (deptId.equals("0") || deptId.equals("")) {
                //li = postDAO.getPostList(lub.getLoginempid());
            } else {
                li = cadreDAO.getCadreList(deptId);
            }

            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "saveSelectedCandidateCategory", method = RequestMethod.POST)
    public ModelAndView saveSelectedCandidateCategory(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("selectedCandidateCategory") SelectedCandidatesCategory scc, BindingResult result, HttpServletResponse response) {//        
        //VacantPost vacantPost = null;
        deptDataEntryDAO.saveCandidateCategory(scc, lub.getLoginuserid());
        return new ModelAndView("redirect:/selectedCandidatesCategoryList.htm");

    }

    @RequestMapping(value = "selectedCandidatesCategoryList")
    public String selectedCandidatesCategoryList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cCategoryList", deptDataEntryDAO.selectedCandiateCategoryList(lub.getLoginuserid()));
        return "/miscellaneous/selectedCandidatesCategoryList";

    }

    @RequestMapping(value = "addSelectedCandidates")
    public String addSelectedCandidates(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("categoryId") String categoryId) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("cCategoryDetail", deptDataEntryDAO.getcCategoryDetail(categoryId));
        model.addAttribute("selectedCandidates", deptDataEntryDAO.selectedCandiatesList(categoryId));
        return "/miscellaneous/addSelectedCandidates";
    }

    @RequestMapping(value = "saveSelectedCandidates", method = RequestMethod.POST)
    public ModelAndView saveSelectedCandidates(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("selectedCandidates") SelectedCandidates sc, BindingResult result, HttpServletResponse response) {
        //VacantPost vacantPost = null;
        deptDataEntryDAO.saveSelectedCandidates(sc);
        String returnPath = "addSelectedCandidates.htm?categoryId=" + sc.getCategoryId();
        return new ModelAndView("redirect:/" + returnPath);
    }

    @RequestMapping(value = "deleteCandidate")
    public String deleteCandidate(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("categoryId") String categoryId, @RequestParam("candidateId") String candidateId) {
        deptDataEntryDAO.deleteCandidate(Integer.parseInt(candidateId), Integer.parseInt(categoryId));
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("cCategoryDetail", deptDataEntryDAO.getcCategoryDetail(categoryId));
        model.addAttribute("selectedCandidates", deptDataEntryDAO.selectedCandiatesList(categoryId));
        return "/miscellaneous/addSelectedCandidates";
    }

    /**
     * ***************************************************************************************************
     */
    @RequestMapping(value = "CommisionPending")
    public String commisionPending(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        model.addAttribute("officename", lub.getLoginoffname());
        return "/miscellaneous/CommisionPendingReqnew";
    }

    @RequestMapping(value = "CommisionCourtCases")
    public String commisionCourtCases(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cadreList", cadreDAO.getCadreList(lub.getLoginempid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        model.addAttribute("officename", lub.getLoginoffname());
        return "/miscellaneous/CommisionCourtCases";
    }

    @RequestMapping(value = "saveCommissionPending", method = RequestMethod.POST)
    public ModelAndView saveCommissionPending(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CommissionPending") CommissionPending cp, BindingResult result, HttpServletResponse response) {//        
        deptDataEntryDAO.saveCommissionPending(cp, lub.getLoginuserid());
        return new ModelAndView("redirect:/commissionPendingList.htm");

    }

    @RequestMapping(value = "commissionPendingList")
    public String commissionPendingList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("cpendingList", deptDataEntryDAO.commissionPendingList(lub.getLoginuserid()));
        return "/miscellaneous/commissionPendingList";

    }

    @RequestMapping(value = "editcommissionpending")
    public String editcommissionpending(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("CommissionPending") CommissionPending cp, HttpServletResponse response, @RequestParam("pendingId") String taskid, @RequestParam("deptid") String deptid) {
        cp = deptDataEntryDAO.editcommissionpending(taskid);
        model.addAttribute("cp", cp);
        model.addAttribute("cadreList", cadreDAO.getCadreList(deptid));
        model.addAttribute("departmentList", deptDao.getDeptList());
        model.addAttribute("officename", lub.getLoginoffname());
        return "/miscellaneous/editcommissionpending";
    }

    @RequestMapping(value = "updateCommissionPending", method = RequestMethod.POST)
    public ModelAndView updateCommissionPending(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CommissionPending") CommissionPending cp, BindingResult result, HttpServletResponse response) {//        
        deptDataEntryDAO.updateCommissionPending(cp, lub.getLoginuserid());
        return new ModelAndView("redirect:/commissionPendingList.htm");

    }

    @RequestMapping(value = "saveCommissionCourtCase", method = RequestMethod.POST)
    public ModelAndView saveCommissionCourtCase(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CommissionCourtCase") CommissionCourtCase ccourtcase, BindingResult result, HttpServletResponse response) {//        
        deptDataEntryDAO.saveCommissionCourtcaseDetails(ccourtcase, lub.getLoginuserid());
        return new ModelAndView("redirect:/commissionCourtCaseList.htm");

    }

    @RequestMapping(value = "commissionCourtCaseList")
    public String commissionCourtCaseList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getLoginuserid()));
        return "/miscellaneous/commissionCourtCaseList";

    }

    @RequestMapping(value = "editCommissionCourtCase")
    public String editCommissionCourtCase(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("CommissionCourtCase") CommissionCourtCase cc, HttpServletResponse response, @RequestParam("courtCaseId") String taskid, @RequestParam("deptid") String deptid) {
        cc = deptDataEntryDAO.editCommissionCourtCase(taskid);
        model.addAttribute("cc", cc);
        model.addAttribute("cadreList", cadreDAO.getCadreList(deptid));
        model.addAttribute("departmentList", deptDao.getDeptList());
        model.addAttribute("officename", lub.getLoginoffname());
        return "/miscellaneous/editCommissionCourtCase";
    }

    @RequestMapping(value = "updateCommissionCourtCase", method = RequestMethod.POST)
    public ModelAndView updateCommissionCourtCase(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CommissionCourtCase") CommissionCourtCase cc, BindingResult result, HttpServletResponse response) {//        
        deptDataEntryDAO.updateCommissionCourtCase(cc, lub.getLoginuserid());
        return new ModelAndView("redirect:/commissionCourtCaseList.htm");

    }

    /**
     * ********************************************** MIS Panel
     * *************************
     */
    @RequestMapping(value = "misDeptVacantPost")
    public String misDeptVacantPost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misvacant_post";

    }

    @RequestMapping(value = "deptVacantPostList", method = RequestMethod.POST)
    public String deptVacantPostList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.getmisVacantPost(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/misvacant_post";

    }

    @RequestMapping(value = "misRaschemeList")
    public String misRaschemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misrascheme_list";

    }

    @RequestMapping(value = "deptRaSchemeList", method = RequestMethod.POST)
    public String deptRaSchemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("raschemeList", deptDataEntryDAO.getmisRAScheme(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/misrascheme_list";

    }

    @RequestMapping(value = "misScstRecruitmentList")
    public String misScstRecruitmentList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misScstRecruitment_list";

    }

    @RequestMapping(value = "deptscstList", method = RequestMethod.POST)
    public String deptscstList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("scStRecruitmentList", deptDataEntryDAO.getmisScStRecruitment(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/misScstRecruitment_list";

    }

    @RequestMapping(value = "misRecruitmentDriveList")
    public String misRecruitmentDriveList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misRecruitmentDrive_list";

    }

    @RequestMapping(value = "depReqDriveList", method = RequestMethod.POST)
    public String depReqDriveList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("recruitmentList", deptDataEntryDAO.misRecruitDriveList(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/misRecruitmentDrive_list";

    }

    @RequestMapping(value = "misTrainingList")
    public String misTrainingList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misTraining_list";

    }

    @RequestMapping(value = "depTrainingList", method = RequestMethod.POST)
    public String depTrainingList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("trainingList", deptDataEntryDAO.misTrainingList(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/misTraining_list";

    }

    @RequestMapping(value = "misBleoList")
    public String misBleoList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //model.addAttribute("ccourtcaseList", deptDataEntryDAO.commissioncourtcaseListDetails(lub.getUserid()));
        model.addAttribute("departmentList", deptDao.getDeptList());
        return "/miscellaneous/misBleo_list";

    }

    @RequestMapping(value = "deptBleoList", method = RequestMethod.POST)
    public String deptBleoList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("departmentId") String departmentId) {
        model.addAttribute("departmentList", deptDao.getDeptList());
        model.addAttribute("bleoList", deptDataEntryDAO.misBleoList(departmentId));
        model.addAttribute("selectdeptid", departmentId);

        return "/miscellaneous/misBleo_list";

    }

    @RequestMapping(value = "miscommissionPendingList")
    public String miscommissionPendingList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        model.addAttribute("cpendingList", deptDataEntryDAO.miscommissionPendingList(month, year));
        return "/miscellaneous/miscommissionPendingList";

    }

    @RequestMapping(value = "miscommissionCourtCaseList")
    public String miscommissionCourtCaseList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        model.addAttribute("ccourtcaseList", deptDataEntryDAO.miscommissioncourtcaseListDetails(month, year));
        return "/miscellaneous/miscommissionCourtCaseList";

    }

    @RequestMapping(value = "summaryRaschemeList")
    public String summaryRaschemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "/miscellaneous/summaryascheme_list";

    }

    @RequestMapping(value = "summarydeptRaSchemeList", method = RequestMethod.POST)
    public String summarydeptRaSchemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("raschemeList", deptDataEntryDAO.summarygetmisRAScheme(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/summaryascheme_list";

    }

    @RequestMapping(value = "summaryDeptVacantPost")
    public String summaryDeptVacantPost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        return "/miscellaneous/summaryvacant_post";

    }

    @RequestMapping(value = "summarydeptVacantPostList", method = {RequestMethod.GET, RequestMethod.POST})
    public String summarydeptVacantPostList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.summarygetmisVacantPost(month, year));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
       if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);
        return "/miscellaneous/summaryvacant_post";

    }

    @RequestMapping(value = "summaryvacantgroupwise", method = RequestMethod.GET)
    public String summaryvacantgroupwise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("deptid") String deptid, @RequestParam("deptname") String deptname) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.summaryvacantgroupwise(month, year, deptid));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
        if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);

        model.addAttribute("selectdeptname", deptname);
        model.addAttribute("selectdeptid", deptid);
        return "/miscellaneous/summaryvacant_post_groupwise";

    }

    @RequestMapping(value = "summaryvacantdeptgroupwise", method = RequestMethod.GET)
    public String summaryvacantdeptgroupwise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("deptid") String deptid, @RequestParam("deptname") String deptname) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.summaryvacantdeptgroupwise(month, year, deptid));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
        if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);
        model.addAttribute("selectdeptname", deptname);
        model.addAttribute("selectdeptid", deptid);
        return "/miscellaneous/summaryvacantdeptgroupwise";

    }

    @RequestMapping(value = "summaryvacantpostwise", method = RequestMethod.GET)
    public String summaryvacantpostwise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("deptid") String deptid, @RequestParam("deptname") String deptname, @RequestParam("gname") String gname) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.summaryvacantpostwise(month, year, deptid, gname));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
        if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);

        model.addAttribute("selectdeptname", deptname);
        model.addAttribute("selectdeptid", deptid);
        model.addAttribute("selectdgname", gname);
        return "/miscellaneous/summaryvacant_post_cadwise";

    }

    @RequestMapping(value = "summaryvacantdeptpostwise", method = RequestMethod.GET)
    public String summaryvacantdeptpostwise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("deptid") String deptid, @RequestParam("deptname") String deptname, @RequestParam("gname") String gname) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.summaryvacantdeptpostwise(month, year, deptid, gname));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
       if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);
        model.addAttribute("selectdeptname", deptname);
        model.addAttribute("selectdeptid", deptid);
        model.addAttribute("selectdgname", gname);
        return "/miscellaneous/summaryvacantdeptpostwise";

    }

    @RequestMapping(value = "entryDetailsList", method = RequestMethod.GET)
    public String entryDetailsList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {
        String deptId = lub.getLoginempid();
        model.addAttribute("vacantPostList", deptDataEntryDAO.entryDetailsList(month, year, deptId));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);

        return "/miscellaneous/entryDetailsList";

    }

    @RequestMapping(value = "misSplReport", method = {RequestMethod.GET, RequestMethod.POST})
    public String misSplReport(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.misSplReport(month, year));

        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        return "/miscellaneous/MisSplReport";

    }

    @RequestMapping(value = "totalsummarydeptRaSchemeList")
    public String totalsummarydeptRaSchemeList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        model.addAttribute("raschemeList", deptDataEntryDAO.totalsummarygetmisRAScheme());
        return "/miscellaneous/totalsummaryascheme_list";

    }

    @RequestMapping(value = "totalsummaryvacantgroupwise")
    public String totalsummaryvacantgroupwise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month) {

        model.addAttribute("vacantPostList", deptDataEntryDAO.totalsummaryvacantgroupwise(month, year));
        model.addAttribute("selectmonth", month);
        model.addAttribute("selectyear", year);
        int Intmonth = CommonFunctions.getMonthAsInt(month);
        int Intyear = Integer.parseInt(year);
        String misStatus = "N";
       if ((Intyear >= 2019 && Intmonth > 5) || (Intyear >= 2020 && Intmonth >= 1)) {
            misStatus = "Y";
        }
        model.addAttribute("misStatus", misStatus);
        return "/miscellaneous/totalsummaryvacant_post_groupwise";

    }

    @RequestMapping(value = "DownlaodAadhaarDetails")
    public void DownlaodAadhaarDetails(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            String fileName = "Aadhar_details.xls";
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            deptDataEntryDAO.DownlaodAadhaarDetails(out, fileName, workbook);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

}

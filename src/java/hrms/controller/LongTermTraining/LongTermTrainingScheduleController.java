/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.LongTermTraining;

import hrms.dao.LongTermTraining.longTermTrainingDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.TrainingTitleDAO;
import hrms.dao.master.TrainingTypeDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.training.TrainingDAO;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.trainingschedule.TrainingSchedule;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LongTermTrainingScheduleController {

    @Autowired
    public longTermTrainingDAO lttrainingDao;

    @Autowired
    public TrainingTypeDAO trainingTypeDao;
    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public TrainingTitleDAO trainingTitleDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @RequestMapping(value = "longTermTrainingList.htm")
    public ModelAndView ltTrainingList(@ModelAttribute("lttraining") TrainingSchedule training, ModelMap model, @ModelAttribute("SelectedEmpObj") Users user, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        mv = new ModelAndView("/LongTermTraining/LongTermTrainingScheduleList", "lttraining", training);
        List lttrainingList = lttrainingDao.getLtTrainingList(user.getEmpId());
        mv.addObject("lttrainingList", lttrainingList);
        // mv.setViewName("/LongTermTraining/LongTermTrainingScheduleList");
        return mv;
    }

    @RequestMapping(value = "longTermTrainingList.htm", params = "new", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newLtTraining(@ModelAttribute("lttraining") TrainingSchedule training, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        mv = new ModelAndView("/LongTermTraining/LongTermTrainingScheduleData", "lttraining", training);
        training.setEmpId(user.getEmpId());
        List trainingType = trainingTypeDao.getTrainingType();
        List trainingTitle = trainingTitleDao.getTrainingTitle();
        List otherOrgOfflist = offDAO.getOtherOrganisationList();
        List deptlist = deptDAO.getDepartmentList();
        //List offlist=offDAO.getTotalOfficeList(training.getHidNotifyingDeptCode());
        //List postlist=subStantivePostDao.getSanctioningSPCOfficeWiseList(training.getHidNotifyingOffCode());
        List distlist = districtDAO.getDistrictList();
        mv.addObject("trainingType", trainingType);
        mv.addObject("otherOrgOfflist", otherOrgOfflist);
        mv.addObject("trainingTitle", trainingTitle);
        mv.addObject("deptlist", deptlist);
        mv.addObject("distlist", distlist);

        return mv;
    }

    @RequestMapping(value = "longTermTrainingList.htm", params = "save", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView saveLtTraining(@ModelAttribute("lttraining") TrainingSchedule training, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        NotificationBean nb = new NotificationBean();
        String doe = sdf.format(new Date());
        try {
            training.setEmpId(user.getEmpId());
            training.setEntrydept(lub.getLogindeptcode());
            training.setEntryoff(lub.getLoginoffcode());
            training.setEntryauth(lub.getLoginspc());
            nb.setNottype("LT_TRAINING");
            nb.setEmpId(user.getEmpId());
            nb.setDateofEntry(sdf.parse(doe));
            nb.setOrdno(training.getTxtOrdNo());
            nb.setOrdDate(sdf.parse(training.getTxtOrdDt()));
            nb.setSancDeptCode(training.getHidNotifyingDeptCode());
            nb.setSancOffCode(training.getHidNotifyingOffCode());
            nb.setSancAuthCode(training.getNotifyingSpc());
            nb.setNote(training.getTxtnote());
            nb.setRadpostingauthtype(training.getRdTrainingauthtype());
            if (training.getChkNotSBPrint() != null && training.getChkNotSBPrint().equals("Y")) {
                training.setIfvisible("N");

            } else {
                training.setIfvisible("Y");

            }
            if (training.getHidNotId() > 0) {
                nb.setNotid(training.getHidNotId());
                notificationDao.modifyNotificationData(nb);
                lttrainingDao.updateLtTraining(training, nb.getNotid());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                lttrainingDao.saveLtTraining(training, notid);
            }
            mv = new ModelAndView("/LongTermTraining/LongTermTrainingScheduleList", "traininglist", training);
            List lttrainingList = lttrainingDao.getLtTrainingList(training.getEmpId());
            mv.addObject("lttrainingList", lttrainingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "getLtTrainingData")
    public ModelAndView editTraining(@ModelAttribute("lttraining") TrainingSchedule training, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model,
            HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        NotificationBean nb = new NotificationBean();
        List postedspclist=new ArrayList();
        try {
            int notId = Integer.parseInt(requestParams.get("notId"));
            training.setHidNotId(notId);
            training = lttrainingDao.getLtTrainingData(notId);
            mv = new ModelAndView("/LongTermTraining/LongTermTrainingScheduleData", "lttraining", training);
            training.setEmpId(user.getEmpId());
            //List trainingType = trainingTypeDao.getTrainingType();
            List deptlist = deptDAO.getDepartmentList();
            List notifyingOfflist = offDAO.getTotalOfficeList(training.getHidNotifyingDeptCode());
            List offlist = offDAO.getTotalOfficeList(training.getHidPostedDeptCode());
            List notifyingPostlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(training.getHidNotifyingOffCode());
            //List postedspclist = subStantivePostDao.getSanctioningSPCOfficeWiseList(training.getHidPostedOffCode());
            if(training.getGenericpostPosted()!=null && !training.getGenericpostPosted().equals("")){
               postedspclist=subStantivePostDao.getSubstantivePostListBacklogEntry(training.getHidPostedOffCode(), training.getGenericpostPosted());
            }
            List distlist = districtDAO.getDistrictList();
            List gpcpostedList = subStantivePostDao.getGenericPostList(training.getHidPostedOffCode());
            //mv.addObject("trainingType", trainingType);
            List trainingTitle = trainingTitleDao.getTrainingTitle();
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mv.addObject("otherOrgOfflist", otherOrgOfflist);
            mv.addObject("offlist", offlist);
            mv.addObject("postedspclist", postedspclist);
            mv.addObject("trainingTitle", trainingTitle);
            mv.addObject("deptlist", deptlist);
            mv.addObject("distlist", distlist);
            mv.addObject("gpcpostedList", gpcpostedList);
            mv.addObject("notifyingOfflist", notifyingOfflist);
            mv.addObject("notifyingPostlist", notifyingPostlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;

    }

    /* @RequestMapping(value = "deletetrainingdata.htm", method = {RequestMethod.GET, RequestMethod.POST})
     public ModelAndView deleteTraining(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("training") TrainingSchedule training, @RequestParam("trainId") String trainId, Map<String, Object> model) {
     ModelAndView mv = null;
     //trainingDao.deleteTraining(trainId);
     //List trainingList = trainingDao.getTrainingList(training.getEmpId());
     mv = new ModelAndView("/training/TrainingScheduleList", "traininglist", training);
     //mv.addObject("traininglist", trainingList);
     return mv;
     }*/
    @RequestMapping(value = "longTermTrainingList.htm", params = "cancel", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView cancelTraining(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("training") TrainingSchedule training, Map<String, Object> model) {
        ModelAndView mv = null;
        mv = new ModelAndView("/LongTermTraining/LongTermTrainingScheduleList", "lttraining", training);
        List lttrainingList = lttrainingDao.getLtTrainingList(user.getEmpId());
        mv.addObject("lttrainingList", lttrainingList);
        return mv;
    }
}

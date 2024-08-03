package hrms.controller.training;

import hrms.dao.master.TrainingTitleDAO;
import hrms.dao.master.TrainingTypeDAO;
import hrms.dao.training.TrainingDAO;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.trainingschedule.TrainingSchedule;
import java.text.SimpleDateFormat;
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

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class TrainingScheduleController {

    @Autowired
    TrainingDAO trainingDao;
    @Autowired
    TrainingTypeDAO trainingTypeDao;
    @Autowired
    TrainingTitleDAO trainingTitleDao;

    @RequestMapping(value = "trainingschedulelist.htm")
    public ModelAndView traList(@ModelAttribute("training") TrainingSchedule training, ModelMap model, @ModelAttribute("SelectedEmpObj") Users user, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        List trainingList = trainingDao.getTrainingList(user.getEmpId());
        mv = new ModelAndView("/training/TrainingScheduleList", "traininglist", training);
        mv.addObject("traininglist", trainingList);
        return mv;
    }

    @RequestMapping(value = "trainingschedulelist.htm", params = "new", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newTrainingSchedule(@ModelAttribute("training") TrainingSchedule training, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        mv = new ModelAndView("/training/TrainingScheduleData", "traininglist", training);
        training.setEmpId(user.getEmpId());
        List trainingType = trainingTypeDao.getTrainingType();
        mv.addObject("trainingType", trainingType);
        List trainingTitle = trainingTitleDao.getTrainingTitle();       
        mv.addObject("trainingTitle", trainingTitle);
        return mv;
    }

    @RequestMapping(value = "trainingschedulelist.htm", params = "save", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView saveTrainingSchedule(@ModelAttribute("training") TrainingSchedule training, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            training.setEntrydept(lub.getLogindeptcode());
            training.setEntryoff(lub.getLoginoffcode());
            training.setEntryauth(lub.getLoginspc());
            if (training.getChkNotSBPrint() != null && training.getChkNotSBPrint().equals("Y")) {
                training.setIfvisible("N");

            } else {
                training.setIfvisible("Y");

            }
            if (training.getTrainId() != null && !training.getTrainId().equals("")) {
                trainingDao.updateTrainingSchedule(training);
            } else {
                trainingDao.saveTrainingSchedule(training);
            }
            List trainingList = trainingDao.getTrainingList(training.getEmpId());
            mv = new ModelAndView("/training/TrainingScheduleList", "traininglist", training);
            mv.addObject("traininglist", trainingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "gettrainingdata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getLoanReleaseData(@ModelAttribute("SelectedEmpObj") Users user, @RequestParam("trainId") String trainId, Map<String, Object> model) {
        ModelAndView mv = null;
        TrainingSchedule training = new TrainingSchedule();
        training = trainingDao.getTrainingData(trainId);
        training.setEmpId(user.getEmpId());
        training.setTrainId(trainId);
        mv = new ModelAndView("/training/TrainingScheduleData", "traininglist", training);
        List trainingType = trainingTypeDao.getTrainingType();
        mv.addObject("trainingType", trainingType);
        List trainingTitle = trainingTitleDao.getTrainingTitle();
        mv.addObject("trainingTitle", trainingTitle);       
        return mv;
    }

    @RequestMapping(value = "deletetrainingdata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteTraining(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("training") TrainingSchedule training, @RequestParam("trainId") String trainId, Map<String, Object> model) {
        ModelAndView mv = null;
        trainingDao.deleteTraining(trainId);
        List trainingList = trainingDao.getTrainingList(training.getEmpId());
        mv = new ModelAndView("/training/TrainingScheduleList", "traininglist", training);
        mv.addObject("traininglist", trainingList);
        return mv;
    }

    @RequestMapping(value = "trainingschedulelist.htm", params = "cancel", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView cancelTraining(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("training") TrainingSchedule training, @RequestParam("trainId") String trainId, Map<String, Object> model) {
        ModelAndView mv = null;
        List trainingList = trainingDao.getTrainingList(training.getEmpId());
        mv = new ModelAndView("/training/TrainingScheduleList", "traininglist", training);
        mv.addObject("traininglist", trainingList);
        return mv;
    }
}

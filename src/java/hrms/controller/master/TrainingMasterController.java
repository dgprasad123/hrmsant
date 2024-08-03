/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.TrainingMasterDAO;
import hrms.model.master.TrainingBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
public class TrainingMasterController {

    @Autowired
    TrainingMasterDAO trainigDao;

    @RequestMapping("trainingMasterList")
    public ModelAndView getTrainingMasterTitle(ModelMap model, HttpServletResponse response, @ModelAttribute("trainingModel") TrainingBean training) {
        String path = "/master/TrainingList";
        ModelAndView mav = new ModelAndView(path, "bankbranchModel", training);

        ArrayList trainingList = trainigDao.getTrainingMasterTitle();
        mav.addObject("trainingTypeList", trainigDao.getTrainingType());
        mav.addObject("training_list", trainingList);
        return mav;
    }
    
    @RequestMapping(value = "saveTrainingMaster", params = {"submit=Save"})
    public ModelAndView saveTrainingMaster(ModelMap model, HttpServletResponse response, @ModelAttribute("trainingModel") TrainingBean training) {
          
       String id= training.getHiddentrainingid();
       training.setHiddentrainingid(id);

        if (training.getHiddentrainingid() != null && !training.getHiddentrainingid().equals(""))
        {
            trainigDao.updateNewTrainingMaster(training);      
        }
        else{
            trainigDao.addNewTrainingMaster(training);
        }
        return new ModelAndView("redirect:/trainingMasterList.htm");
    }
    @RequestMapping(value = "deleteTrainingMaster")
    public ModelAndView deleteTrainingMaster(ModelMap model, HttpServletResponse response, @RequestParam("trainingId") int trainingId) {

        ModelAndView mav = null; 
        try {
            trainigDao.deleteTrainingMaster(trainingId);
            mav = new ModelAndView("redirect:/trainingMasterList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }   
    @ResponseBody
    @RequestMapping(value = "edittrainingMaster", method = {RequestMethod.GET, RequestMethod.POST})
    public void editNewTrainingMaster(ModelMap model, HttpServletResponse response, @ModelAttribute("trainingModel") TrainingBean trainingModel, @RequestParam("trainingId") String trainingId) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mav = null;
        try {

            trainigDao.editNewTrainingMaster(trainingModel.getTrainingId(),trainingModel);
            int id = trainingModel.getTrainingId();
            trainingModel.setTrainingId(id);
            trainingModel.setHiddentrainingid(trainingId);
           
            JSONObject jos = new JSONObject(trainingModel);
            out = response.getWriter();
            out.write(jos.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

  

}

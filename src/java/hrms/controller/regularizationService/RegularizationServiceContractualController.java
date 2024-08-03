/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.regularizationService;

import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.regularizationService.RegularizationServiceContractualDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.regularizeService.RegularizeServiceContractualForm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RegularizationServiceContractualController {

    @Autowired
    public RegularizationServiceContractualDAO regularizeServiceContractualDAO;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @RequestMapping(value = "RegularizationServiceContractualPage")
    public ModelAndView RegularizationServiceContractualPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceContractualForm regularizeService, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        try {
            if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {
                regularizeService.setEmpid(selectedEmpObj.getEmpId());

                regularizeServiceContractualDAO.getRegularizationServiceContractualData(regularizeService);
                if (regularizeService.getRdPayCommission() == null || regularizeService.getRdPayCommission().equals("")) {
                    regularizeService.setRdPayCommission("7");
                }
                mav = new ModelAndView("/regularizationService/NewRegularizationServiceContractual", "regularizeService", regularizeService);

                String msg = requestParams.get("msg");
                mav.addObject("msg", msg);

                List postlist = subStantivePostDao.getGenericPostList(lub.getLoginoffcode());
                mav.addObject("postlist", postlist);

                List spclist = subStantivePostDao.getGPCWiseSectionWiseSPCList(lub.getLoginoffcode(), regularizeService.getSltGenericPost());
                mav.addObject("spclist", spclist);

                mav.addObject("selectedEmpOffice", lub.getLoginoffcode());
            } else {
                mav = new ModelAndView();
                mav.setViewName("under_const");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRegularizationContractual", params = {"btnRegularizationService=Save Regularization"})
    public String saveRegularizationContractual(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceContractualForm regularizeService) throws ParseException {

        try {

            if (regularizeService.getHregid() != null && !regularizeService.getHregid().equals("") && Integer.parseInt(regularizeService.getHregid()) > 0) {
                regularizeServiceContractualDAO.updateRegularizationContractualData(regularizeService, lub.getLoginempid());
            } else {
                regularizeServiceContractualDAO.insertRegularizationContractualData(regularizeService, lub.getLoginempid());
            }
            regularizeServiceContractualDAO.updateEmployeeData(regularizeService);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/RegularizationServiceContractualPage.htm?msg=Success";
    }

    @RequestMapping(value = "RegularizationServiceContractual6YearsPage")
    public ModelAndView RegularizationServiceContractual6YearsPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceContractualForm regularizeService, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;

        try {

            String payComm = selectedEmpObj.getPayCommission();
            if (payComm == null || payComm.equals("")) {
                payComm = "6";
            }
            regularizeService.setRdPayCommission(payComm);

            if (lub.getLoginusertype() != null && lub.getLoginusertype().equals("G")) {
                //if (selectedEmpObj.getUsertype() != null && selectedEmpObj.getUsertype().equals("C")) {
                if (regularizeServiceContractualDAO.checkEntryForContractual6YearsToRegular(selectedEmpObj.getEmpId()) == true) {
                    regularizeService.setEmpid(selectedEmpObj.getEmpId());

                    regularizeServiceContractualDAO.getRegularizationServiceContractual6YearsData(regularizeService);
                    List payscalelist = payscaleDAO.getPayScaleList();

                    if (regularizeService.getRdPayCommission() == null || regularizeService.getRdPayCommission().equals("")) {
                        regularizeService.setRdPayCommission("7");
                    }
                    mav = new ModelAndView("/regularizationService/NewRegularizationServiceContractual6Years", "regularizeService", regularizeService);
                    mav.addObject("payscalelist", payscalelist);
                    mav.addObject("paylevelList", incrementsancDAO.getPayMatrixLevel());
                    mav.addObject("payCellList", incrementsancDAO.getPayMatrixCell());

                    String msg = requestParams.get("msg");
                    mav.addObject("msg", msg);
                } else {
                    mav = new ModelAndView();
                    mav.setViewName("under_const");
                }
            } else {
                mav = new ModelAndView();
                mav.setViewName("under_const");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRegularizationContractual6Years", params = {"btnRegularizationService=Save Regularization"})
    public String saveRegularizationContractual6Years(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("regularizeService") RegularizeServiceContractualForm regularizeService) throws ParseException {

        try {
            if (regularizeService.getHregid() != null && !regularizeService.getHregid().equals("") && Integer.parseInt(regularizeService.getHregid()) > 0) {
                regularizeServiceContractualDAO.updateRegularizationContractual6YearsData(regularizeService, lub.getLoginempid());
            } else {
                regularizeServiceContractualDAO.insertRegularizationContractual6YearsData(regularizeService, lub.getLoginempid());
            }
            regularizeServiceContractualDAO.updateEmployeeDataFor6Years(regularizeService);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/RegularizationServiceContractual6YearsPage.htm?msg=Success";
    }
}

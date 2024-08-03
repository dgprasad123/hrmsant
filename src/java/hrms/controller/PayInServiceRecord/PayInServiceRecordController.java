/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.PayInServiceRecord;

import hrms.dao.PayInServiceRecord.PayInServiceRecordDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.model.PayInServiceRecord.PayInServiceRecordForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
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
public class PayInServiceRecordController {

    @Autowired
    public PayInServiceRecordDAO payInSRDao;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @RequestMapping(value = "payInServiceRecordList")
    public ModelAndView payInServiceRecordList(ModelMap model, @ModelAttribute("SelectedEmpObj") Users user, HttpServletRequest request,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pisrForm") PayInServiceRecordForm pisr) {
        ModelAndView mav = null;
        mav = new ModelAndView("/PayInServiceRecord/PayInServiceRecordList", "pisrForm", pisr);
        System.out.println("pisr.getEmpid():" + user.getEmpId());
        List servicelist = payInSRDao.getPayInServiceRecordList(user.getEmpId());
        mav.addObject("serviceList", servicelist);
        return mav;

    }

    @RequestMapping(value = "payInServiceRecord")
    public ModelAndView payInServiceRecordData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("pisrForm") PayInServiceRecordForm pisr) throws IOException {
        ModelAndView mv = null;
        try {
            pisr.setEmpid(user.getEmpId());
            mv = new ModelAndView("/PayInServiceRecord/PayInServiceRecordData", "pisrForm", pisr);
            List payscale = payscaleDAO.getPayScaleList();
            mv.addObject("payScaleList", payscale);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;

    }

    @RequestMapping(value = "savePisrData")
    public ModelAndView savePayInServiceRecord(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("pisrForm") PayInServiceRecordForm pisr) throws IOException {
        ModelAndView mv = null;
        try {
            pisr.setEntDept(lub.getLogindeptcode());
            pisr.setEntOfc(lub.getLoginoffcode());
            pisr.setEntSpc(lub.getLoginspc());
            payInSRDao.savePISRData(user.getEmpId(), pisr);
            mv = new ModelAndView("/PayInServiceRecord/PayInServiceRecordList", "pisrForm", pisr);
            List servicelist = payInSRDao.getPayInServiceRecordList(user.getEmpId());
            mv.addObject("serviceList", servicelist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;

    }

    @RequestMapping(value = "getPisrData")
    public ModelAndView editPisrData(@ModelAttribute("pisrForm") PayInServiceRecordForm pisr, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model,
            HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        try {
            pisr = payInSRDao.getPayInServiceRecordData(user.getEmpId(), pisr.getSrpId());
            mav = new ModelAndView("/PayInServiceRecord/PayInServiceRecordData", "pisrForm", pisr);

            List servicelist = payInSRDao.getPayInServiceRecordList(user.getEmpId());
            mav.addObject("serviceList", servicelist);

            List payscale = payscaleDAO.getPayScaleList();
            mav.addObject("payScaleList", payscale);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "savePisrData", params = "Update", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView modifyPisrData(@ModelAttribute("pisrForm") PayInServiceRecordForm pisr, @ModelAttribute("SelectedEmpObj") Users user, ModelMap model,
            HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        String hiddednSrpId = pisr.getHidSrpId();
        pisr.setEntDept(lub.getLogindeptcode());
        pisr.setEntOfc(lub.getLoginoffcode());
        pisr.setEntSpc(lub.getLoginspc());
        pisr.setEmpid(user.getEmpId());
        payInSRDao.modifyPISRData(pisr, hiddednSrpId);
        mv = new ModelAndView("/PayInServiceRecord/PayInServiceRecordList", "pisrForm", pisr);
        List servicelist = payInSRDao.getPayInServiceRecordList(user.getEmpId());
        mv.addObject("serviceList", servicelist);

        return mv;
    }

    @RequestMapping(value = "deletePisrdata.htm")
    public ModelAndView deletePisrData(@ModelAttribute("pisrForm") PayInServiceRecordForm pisr, @ModelAttribute("SelectedEmpObj") Users user,ModelMap model,
            HttpServletRequest request,
            @RequestParam("srpId")String srpId ) {
        ModelAndView mv = null;
        System.out.println("delete:"+srpId);
        String empid = user.getEmpId();
        payInSRDao.deletePISRData(empid, srpId);
        mv = new ModelAndView("/PayInServiceRecord/PayInServiceRecordList", "pisrForm", pisr);
        List servicelist = payInSRDao.getPayInServiceRecordList(empid);
        mv.addObject("serviceList", servicelist);
        return mv;
    }
    
    @RequestMapping(value = "savePisrData", params="Cancel" , method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView CancelData(@ModelAttribute("pisrForm") PayInServiceRecordForm pisr, @ModelAttribute("SelectedEmpObj") Users user,ModelMap model,
            HttpServletRequest request, @RequestParam Map<String, String> requestParams ) {
        ModelAndView mv = null;
        String empid = user.getEmpId();
        mv = new ModelAndView("/PayInServiceRecord/PayInServiceRecordList", "pisrForm", pisr);
        List servicelist = payInSRDao.getPayInServiceRecordList(empid);
        mv.addObject("serviceList", servicelist);
        return mv;
    }
}

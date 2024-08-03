/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.reward;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.reward.RewardDAO;
import hrms.model.common.FileAttribute;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.reward.Reward;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RewardController implements ServletContextAware {

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public RewardDAO rewardDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "RewardList")
    public ModelAndView rewardList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("rewardForm") Reward rewardForm) {

        List rewardlist = null;

        ModelAndView mav = null;
        try {
            rewardForm.setEmpid(u.getEmpId());

            rewardlist = rewardDao.getRewardList(rewardForm.getEmpid());

            mav = new ModelAndView("/reward/RewardList", "rewardForm", rewardForm);
            mav.addObject("rewardlist", rewardlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newReward")
    public ModelAndView NewReward(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("rewardForm") Reward rewardForm) throws IOException {
        ModelAndView mav = null;
        try {

            if (rewardForm.getRadpostingauthtype() == null || rewardForm.getRadpostingauthtype().equals("")) {
                rewardForm.setRadpostingauthtype("GOO");
            }

            mav = new ModelAndView("/reward/AddReward", "rewardForm", rewardForm);
            ArrayList empRewardTypeList = rewardDao.empRewardType();
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptArray", deptlist);
            mav.addObject("empRewardTypeList", empRewardTypeList);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveReward", params = {"btnReward=Save"})
    public ModelAndView saveReward(@ModelAttribute("SelectedEmpObj") Users u, Model model, @ModelAttribute("rewardForm") Reward rewardform, @RequestParam("btnReward") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List rewardlist = null;

        String filepath = servletContext.getInitParameter("policeDocPath");
        try {
            //rewardform.setEmpid(u.getEmpId());
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("REWARD");
                nb.setEmpId(rewardform.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(rewardform.getTxtNotOrdNo());
                nb.setOrdDate(sdf.parse(rewardform.getTxtNotOrdDt()));
                nb.setSancDeptCode(rewardform.getHidAuthDeptCode());
                nb.setSancOffCode(rewardform.getHidAuthOffCode());
                if(rewardform.getRadpostingauthtype() != null && rewardform.getRadpostingauthtype().equals("GOI")){
                    nb.setSancAuthCode(rewardform.getHidOthSpc());
                }else{
                    nb.setSancAuthCode(rewardform.getAuthSpc());
                }
                nb.setNote(rewardform.getNote());
                nb.setRadpostingauthtype(rewardform.getRadpostingauthtype());
                if (rewardform.getChkNotSBPrint() != null && rewardform.getChkNotSBPrint().equals("Y")) {
                    nb.setIfVisible("N");
                } else {
                    nb.setIfVisible("Y");
                }

                if (rewardform.getHnotid() > 0) {

                    nb.setNotid(rewardform.getHnotid());
                    notificationDao.modifyNotificationData(nb);
                    rewardDao.updateReward(rewardform, filepath);
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    rewardDao.saveReward(rewardform, notid, filepath);
                }

            }

            rewardlist = rewardDao.getRewardList(rewardform.getEmpid());
            mav = new ModelAndView("/reward/RewardList", "rewardform", rewardform);
            mav.addObject("rewardlist", rewardlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editReward")
    public ModelAndView editReward(HttpServletResponse response, @ModelAttribute("rewardForm") Reward rewardform, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        try {

            String rewardId = requestParams.get("rewardId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            rewardform = rewardDao.getEmpRewardData(rewardId, notificationId);
            rewardform.setEmpid(u.getEmpId());
            rewardform.setRewardId(rewardId);
            rewardform.setHnotid(notificationId);
            
            List deptlist = deptDAO.getDepartmentList();
            List offList = offDAO.getTotalOfficeList(rewardform.getHidAuthDeptCode());
            List authList = substantivePostDAO.getSanctioningSPCOfficeWiseList(rewardform.getHidAuthOffCode());
            ArrayList empRewardTypeList = rewardDao.empRewardType();
            
            if (rewardform.getRadpostingauthtype() == null || rewardform.getRadpostingauthtype().equals("")) {
                rewardform.setRadpostingauthtype("GOO");
            }
            
            mav = new ModelAndView("/reward/AddReward", "rewardForm", rewardform);
            mav.addObject("empRewardTypeList", empRewardTypeList);
            mav.addObject("deptArray", deptlist);
            mav.addObject("offArray", offList);
            mav.addObject("authArray", authList);
            
            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveReward", params = {"btnReward=Delete"})
    public ModelAndView deleteReward(HttpServletResponse response, @ModelAttribute("rewardForm") Reward rewardform, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        ModelAndView mav = null;
        String rewardId = rewardform.getRewardId();
        int notificationId = rewardform.getHnotid();
        rewardDao.deleteReward(rewardId);
        notificationDao.deleteNotificationData(notificationId, "REWARD");
        mav = new ModelAndView("/reward/RewardList", "rewardForm", rewardform);
        List rewardlist = rewardDao.getRewardList(rewardform.getEmpid());
        mav.addObject("rewardlist", rewardlist);
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "downloadAttachedDocumentCopy")
    public void downloadAttachedDocumentCopy(HttpServletResponse response, @RequestParam("notid") int notid) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = rewardDao.getAttachedDocument(filepath, "NOTIAPR", notid);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "deleteRewardAttachment")
    public void deleteRewardAttachment(HttpServletResponse response, @RequestParam("notId") int notid) {

        response.setContentType("application/json");

        FileAttribute fa = null;

        JSONObject obj = null;
        try {
            String filepath = servletContext.getInitParameter("policeDocPath");
            fa = rewardDao.getAttachedDocument(filepath, "NOTIAPR", notid);

            int deletestatus = rewardDao.deleteRewardAttachment(notid, filepath, fa);

            obj = new JSONObject();
            obj.append("deletestatus", deletestatus);
            PrintWriter out = response.getWriter();
            out.write(obj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

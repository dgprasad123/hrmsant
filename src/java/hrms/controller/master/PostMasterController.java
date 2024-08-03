/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.PostDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.GPost;
import hrms.model.master.Office;
import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PostMasterController {

    @Autowired
    public PostDAO postDAO;
    @Autowired
    DepartmentDAO departmentDao;

    @ResponseBody
    @RequestMapping(value = "getDeptWisePostListJSON")
    public String GetDeptWisePostListJSON(@RequestParam("deptCode") String deptCode) {
        JSONArray json = null;
        try {
            List postlist = postDAO.getPostList(deptCode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getOfficeWisePostListJSON")
    public String getOfficeWisePostListJSON(@RequestParam("deptCode") String deptCode) {
        JSONArray json = null;
        try {
            List postlist = postDAO.getOfficeWisePostList(deptCode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getGenericPostListJSON")
    public String GetGenericPostListJSON(@RequestParam("deptCode") String deptCode) {
        JSONArray json = null;
        try {
            List postlist = postDAO.getGenericPostList(deptCode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /*  @RequestMapping(value = "/admin/postList")
     public ModelAndView postList(@ModelAttribute("post") GPost post) {
     ModelAndView mv = new ModelAndView();
     mv.addObject("post", post);
     mv.addObject("departmentList", departmentDao.getDepartmentList());
     mv.addObject("postList", postDAO.getPostList(post.getDeptcode()));
     mv.addObject("departmentId", post.getDeptcode());
     mv.setViewName("/master/PostList");
     return mv;
     }*/
    @RequestMapping(value = "postList")
    public ModelAndView postList(@ModelAttribute("post") GPost post, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("LoginUserType", lub.getLoginusertype());
        mv.addObject("post", post);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("postList", postDAO.getPostList(post.getDeptcode()));
        mv.addObject("departmentId", post.getDeptcode());

        mv.setViewName("/master/PostList");
        return mv;
    }

    @RequestMapping(value = "newPost")
    public ModelAndView newPost(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String path = null;
        ModelAndView mv = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A")) {
            //path = "/master/PostEdit";
            mv = new ModelAndView(path, "post", new GPost());
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.setViewName("/master/PostEdit");
        } else {
             mv = new ModelAndView("under_const");
            //mv.setViewName("under_const");
        }

        return mv;
    }

    @RequestMapping(value = "savePost")
    public ModelAndView savePost(@ModelAttribute("post") GPost post, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        String path = "/master/PostList";
        postDAO.savePost(post);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("postList", postDAO.getPostList(post.getDeptcode()));
        mv.addObject("departmentId", post.getDeptcode());
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "getPostDetail")
    public ModelAndView getPostDetail(@RequestParam("postcode") String postcode, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String path = "/master/PostEdit";
        ModelAndView mv = new ModelAndView();
        mv.addObject("post", postDAO.getPostDetail(postcode));
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "mapCadretoPost")
    public ModelAndView mapCadretoPost(@ModelAttribute("post") GPost post, @RequestParam Map<String, String> req, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        //@RequestParam("postcode") String postcode, @RequestParam("departmentId") String departmentId
        String postCode = req.get("postCode");
        String deptCode = req.get("deptCode");
        String path = "/master/MapCadretoPost";

        if (deptCode != null && !deptCode.equals("")) {
            post.setDeptcode(deptCode);
        }

        if (postCode != null && !postCode.equals("")) {
            post.setPostcode(postCode);
        }
        ModelAndView mv = new ModelAndView(path, "post", post);
        mv.addObject("postDetail", postDAO.getPostDetail(post.getPostcode()));
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("cadreList", postDAO.getCadreList(post.getDeptcode(), post.getPostcode()));
        //mv.setViewName(path);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "MapCadreToPostTable")
    public void MapCadreToPostTable(@RequestParam Map<String, String> req, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String postcode = req.get("postcode");
        String cadrecode = req.get("cadrecode");
        String mapAction = req.get("mapAction");
        postDAO.saveCadrePost(postcode, cadrecode, mapAction);
        //Now get the list
    }

    @RequestMapping(value = "DuplicatePostCorrection")
    public String substansivePost(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> req) {
        String path = "/master/DuplicatePostCorrection";
        String deptCode = req.get("deptCode");
        String mergedMsg = req.get("mergedMsg");
        if (deptCode != null && !deptCode.equals("")) {
            model.addAttribute("postList", postDAO.getModifiedPostList(deptCode));
            model.addAttribute("deptCode", deptCode);
        }
        model.addAttribute("deptList", departmentDao.getDepartmentList());
        if (mergedMsg != null && !mergedMsg.equals("")) {
            model.addAttribute("mergedMsg", "Successfully Merged");
        }
        return path;
    }


    /* @RequestMapping(value = "MergeDuplicatePost")
     public ModelAndView MergeDuplicatePost(@ModelAttribute("DuplicatePost") GPost post) {
     ModelAndView mv = new ModelAndView();
     String deptCode = post.getDeptcode();
     String path = "redirect:/DuplicatePostCorrection.htm?deptCode=" + deptCode;
     postDAO.mergeDuplicatePost(post);
     mv.setViewName(path);
     return mv;
     }

     @RequestMapping(value = "ViewDuplicatePosts")
     public String viewDuplicatePosts(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> req) {
     String path = "/master/ViewDuplicatePosts";
     String deptCode = req.get("deptCode");
     if (deptCode != null && !deptCode.equals("")) {
     model.addAttribute("postList", postDAO.viewDuplicatePostList(deptCode));
     model.addAttribute("deptCode", deptCode);
     }
     model.addAttribute("deptList", departmentDao.getDepartmentList());
     return path;
     }*/
    @RequestMapping(value = "setPostAsAuthority")
    public String setPostAsAuthority(@RequestParam("postcode") String postcode, @RequestParam("deptCode") String deptCode, @RequestParam("status") String status, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        postDAO.setPostAsAuthority(postcode, status);

        return "redirect:/postList.htm?deptcode=" + deptCode;
    }

    @ResponseBody
    @RequestMapping(value = "getForeignBodyPostListJSON")
    public String getForeignBodyPostListJSON(@RequestParam("deptcode") String deptCode) {
        JSONArray json = null;
        try {
            List postlist = postDAO.getForeignBodyPostList(deptCode);
            json = new JSONArray(postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "getMergedPost.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getMergedPost(ModelMap model, @ModelAttribute("post") GPost post, HttpServletResponse response, @RequestParam("dupPost") String dupPost, @RequestParam("finPost") String finPost
    ) {
        ModelAndView mv = null;
        String msg = null;

        try {
            System.out.println("DuplicatePost/FinalPost_code:" + dupPost + "::" + finPost);
            post = postDAO.getPostDetailsforMerging(dupPost, finPost);
            mv = new ModelAndView("redirect:/DuplicatePostCorrection.htm?deptCode=" + post.getHiddeptCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "getDistinctPostCount.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDistinctPostCount(ModelMap model, @ModelAttribute("post") GPost post, HttpServletResponse response, @RequestParam("dupPost") String dupPost,
            @RequestParam("finPost") String finPost) {
        List totalPostList = postDAO.getTotalSPC(dupPost, finPost);
        model.addAttribute("totalPostList", totalPostList);
        post.setHidDupPostCode(dupPost);
        post.setHidFinPostCode(finPost);
        model.addAttribute("duplicatePost", post.getHidDupPostCode());
        model.addAttribute("finalPost", post.getHidFinPostCode());
        String finalPostName = postDAO.getPostName(post.getHidFinPostCode());
        String duplicatePostName = postDAO.getPostName(post.getHidDupPostCode());
        model.addAttribute("finalPostNm", finalPostName);
        model.addAttribute("duplicatePostNm", duplicatePostName);
        String path = "/master/viewPostWiseStrength";
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "updatePostName.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void updatePost(@ModelAttribute("post") GPost post, @RequestParam("dupPostCode") String dupPostCode, @RequestParam("finPost") String finPost, @RequestParam("postList") String postList, @RequestParam("finalPostNm") String finalPostNm,
            HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        post.setHidDupPostCode(dupPostCode);
        post.setHidFinPostCode(finPost);
        postDAO.getUpdateAndReplaceSPN(post, finalPostNm, postList);
        JSONObject obj = new JSONObject();
        obj.append("msg", "Y");
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();

    }

    @ResponseBody
    @RequestMapping(value = "viewPreviousMergedPostJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewPreviousMergedPost(ModelMap model, HttpServletResponse response, @RequestParam("finalPostCode") String finalPostCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List mergedPostList = postDAO.viewPreviousMergedPost(finalPostCode);
            json = new JSONArray(mergedPostList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.servicebook;

import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ProfilePhotoController implements ServletContextAware {

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "displayfistpagesb", method = RequestMethod.GET)
    public ModelAndView displayfistpagesb(HttpServletResponse response, Map<String, Object> model, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String strSQL = null;
        String empid = lub.getLoginempid();
        Blob img;
        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("SBPath");
                String filename = empid + ".jpg";
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        os.close();
        os.flush();
        return null;
    }

    @RequestMapping(value = "displayemployeefistpagesb", method = RequestMethod.GET)
    public void displayemployeefistpagesb(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) throws IOException {
        int BUFFER_LENGTH = 4096;
        String filePath = context.getInitParameter("SBPath");
        String filename = selectedEmpObj.getEmpId() + ".jpg";
        File file = new File(filePath + filename);
        response.setContentType("image/gif");
        if (file.exists()) {
            response.setContentLength((int) file.length());
            FileInputStream is = new FileInputStream(file);
            ServletOutputStream os = response.getOutputStream();
            byte[] bytes = new byte[BUFFER_LENGTH];
            int read = 0;
            while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                os.write(bytes, 0, read);
            }
            os.flush();
            is.close();
            os.close();
        }
    }

    @RequestMapping(value = "displayemployeeprofilephoto", method = RequestMethod.GET)
    public void displayemployeeprofilephoto(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) throws IOException {
        int BUFFER_LENGTH = 4096;
        String filePath = context.getInitParameter("PhotoPath");
        String filename = selectedEmpObj.getEmpId() + ".jpg";
        File file = new File(filePath + filename);
        response.setContentType("image/gif");
        if (file.exists()) {
            response.setContentLength((int) file.length());
            FileInputStream is = new FileInputStream(file);
            ServletOutputStream os = response.getOutputStream();
            byte[] bytes = new byte[BUFFER_LENGTH];
            int read = 0;
            while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                os.write(bytes, 0, read);
            }
            os.flush();
            is.close();
            os.close();
        }
    }

    @RequestMapping(value = "displayjointPhoto", method = RequestMethod.GET)
    public void displayjointPhoto(HttpServletResponse response, @RequestParam("empid") String empid) throws IOException {
        int BUFFER_LENGTH = 4096;
        String filePath = context.getInitParameter("PhotoPath");
        String filename = "JOINT_" + empid + ".jpg";
        File file = new File(filePath + filename);
        response.setContentType("image/jpeg");
        if (file.exists()) {
            response.setContentLength((int) file.length());
            FileInputStream is = new FileInputStream(file);
            ServletOutputStream os = response.getOutputStream();
            byte[] bytes = new byte[BUFFER_LENGTH];
            int read = 0;
            while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                os.write(bytes, 0, read);
            }
            os.flush();
            is.close();
            os.close();
        }
    }

    @RequestMapping(value = "profilephoto.htm", method = RequestMethod.GET)
    public ModelAndView profilePhoto(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, HttpServletResponse response) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String strSQL = null;
        String empid = lub.getLoginempid();
        Blob img;
        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("PhotoPath");
                String filename = empid + ".jpg";
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        os.close();
        os.flush();
        return null;
    }

    @RequestMapping(value = "profileddophoto.htm", method = RequestMethod.GET)
    public ModelAndView profileDdoPhoto(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String strSQL = null;
        String empid = selectedEmpObj.getEmpId();
        Blob img;
        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("PhotoPath");
                String filename = empid + ".jpg";
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        os.close();
        os.flush();
        return null;
    }

    @RequestMapping(value = "jointPhoto.htm", method = RequestMethod.GET)
    public ModelAndView jointPhoto(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, HttpServletResponse response) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String strSQL = null;
        String empid = lub.getLoginempid();
        Blob img;
        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("PhotoPath");
                String filename = "JOINT_" + empid + ".jpg";;
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        os.close();
        os.flush();
        return null;
    }

    @RequestMapping(value = "profilephotoddoview.htm", method = RequestMethod.GET)
    public ModelAndView profilePhotoDdoView(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String empid = request.getParameter("empid");

        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("PhotoPath");
                String filename = empid + ".jpg";
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            os.close();
            os.flush();
        }

        return null;
    }
}

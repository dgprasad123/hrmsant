/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.upload;

import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.upload.UploadedFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class UploadController implements ServletContextAware {

    //@Autowired
    //UploadFileValidator fileValidator;
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping("/fileUploadForm")
    public ModelAndView getUploadForm(
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/PhotoUploadInterface");
    }

    @RequestMapping("/fileUploadDdoForm")
    public ModelAndView getUploadDdoForm(
            @ModelAttribute("uploadedDdoFile") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/PhotoUploadDdoInterface");
    }
    @RequestMapping("/UploadJointPhoto")
    public ModelAndView UploadJointPhoto(
            @ModelAttribute("uploadedJointPhoto") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/UploadJointPhoto");
    }
    
    


    @RequestMapping("/fileUpload")
    public ModelAndView fileUploaded(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = lub.getLoginempid() + ".jpg";
        String filepath = servletContext.getInitParameter("PhotoPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/PhotoUploadInterface");
        }

        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/PhotoUploadInterface");
    }

    @RequestMapping("/fileUploadDdo")
    public ModelAndView fileUploadedDdo(@ModelAttribute("SelectedEmpObj") Users user,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = user.getEmpId() + ".jpg";
        String filepath = servletContext.getInitParameter("PhotoPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/PhotoUploadDdoInterface");
        }

        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/PhotoUploadDdoInterface");
    }

    @RequestMapping("/fileUploadSBForm")
    public ModelAndView getUploadFormSB(
            @ModelAttribute("uploadedFileSB") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/ServiceBookUploadInterface");
    }

    @RequestMapping("/fileUploadSB")
    public ModelAndView fileUploadedSb(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("uploadedFileSB") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        try {

            String fileName = lub.getLoginempid() + ".jpg";
            String filepath = servletContext.getInitParameter("SBPath");
            if (result.hasErrors()) {
                return new ModelAndView("tab/ServiceBookUploadInterface");
            }
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/ServiceBookUploadInterface");
    }

    @RequestMapping("/fileUploadSBDdoForm")
    public ModelAndView getUploadFormSBDdo(
            @ModelAttribute("uploadedFileSBDdo") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/ServiceBookUploadDdo");
    }

    @RequestMapping("/uploadedFileSBDdo")
    public ModelAndView fileUploadedSbDdo(@ModelAttribute("SelectedEmpObj") Users user,
            @ModelAttribute("uploadedFileSBDdo") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = user.getEmpId() + ".jpg";
        String filepath = servletContext.getInitParameter("SBPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/ServiceBookUploadDdo");
        }

        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/ServiceBookUploadDdo");
    }
    
    @RequestMapping("/fileUploadJointPhoto")
    public ModelAndView fileUploadJointPhoto(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = "JOINT_"+lub.getLoginempid() + ".jpg";
        String filepath = servletContext.getInitParameter("PhotoPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/UploadJointPhoto");
        }
        ModelAndView mav = null;
        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            mav = new ModelAndView();
             mav.addObject("msg", "Photo Upload is Successful.");
            mav.setViewName("tab/UploadJointPhoto");
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return mav;
    }
    
    @RequestMapping("/UploadJointPhotoDDO")
    public ModelAndView UploadJointPhotoDDO(
            @ModelAttribute("uploadedJointPhoto") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/UploadJointPhotoDDOInterface");
    }
    
    @RequestMapping("/fileUploadJointPhotoDDO")
    public ModelAndView fileUploadJointPhotoDDO(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = "JOINT_" +user.getEmpId() + ".jpg";
        String filepath = servletContext.getInitParameter("PhotoPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/UploadJointPhotoDDOInterface");
        }

        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/UploadJointPhotoDDOInterface");
    }
    
    
    @RequestMapping("/UploadJointPhotoForm")
    public ModelAndView UploadJointPhotoForm(
            @ModelAttribute("uploadedJointPhoto") UploadedFile uploadedFile,
            BindingResult result) {
        return new ModelAndView("tab/UploadJointPhotoForm");
    }
    
    @RequestMapping("/jointphotoUpload")
    public ModelAndView jointphotoUpload(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            BindingResult result) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile file = uploadedFile.getFile();
        //fileValidator.validate(uploadedFile, result);

        String fileName = "JOINT_"+lub.getLoginempid() + ".jpg";
        String filepath = servletContext.getInitParameter("PhotoPath");
        if (result.hasErrors()) {
            return new ModelAndView("tab/UploadJointPhoto");
        }

        try {
            inputStream = file.getInputStream();

            File newFile = new File(filepath + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

        return new ModelAndView("tab/UploadJointPhoto");
    }


}

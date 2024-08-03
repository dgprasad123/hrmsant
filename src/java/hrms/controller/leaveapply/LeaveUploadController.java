/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.leaveapply;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.leaveupload.LeaveUpload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LeaveUploadController implements ServletContextAware {

    //@Autowired
    //UploadFileValidator fileValidator;
    private ServletContext servletContext;
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping("UploadDocumentAction.htm")
    public ModelAndView getUploadForm(
            @ModelAttribute("uploadedFile") LeaveUpload uploadedFile,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "";
        Connection con = null;
        PreparedStatement pst = null;
        String fileName = "";
        File f = null;
        InputStream inputStream = null;
        PrintWriter out = null;
        int docAttId = 0;
         Calendar cal = Calendar.getInstance();
        String curYear = cal.get(Calendar.YEAR) + "";
        try {
            con = dataSource.getConnection();
            String filePath = servletContext.getInitParameter("FilePath");
            out = response.getWriter();
            if (request instanceof MultipartHttpServletRequest) {
                path = null;
                MultipartRequest multipartRequest = (MultipartRequest) request;
                // You can get your file from request
                CommonsMultipartFile multipartFile = null; // multipart file class depends on which class you use assuming you are using org.springframework.web.multipart.commons.CommonsMultipartFile
                Iterator<String> iterator = multipartRequest.getFileNames();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    // create multipartFile array if you upload multiple files
                    multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);

                    String dirpath = filePath + curYear + "/";
                    
                    f = new File(dirpath);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    docAttId = CommonFunctions.getMaxCodeInteger("HW_ATTACHMENTS", "ATT_ID",con);
                    fileName = System.currentTimeMillis() + "";
                    pst = con.prepareStatement("INSERT INTO HW_ATTACHMENTS(ATT_ID,TASK_ID,ORIGINAL_FILENAME,DISK_FILE_NAME,FILE_TYPE,ATTACH_YEAR) VALUES(?,?,?,?,?,?)");
                    pst.setInt(1, docAttId);
                    pst.setInt(2, 0);
                    pst.setString(3, multipartFile.getOriginalFilename());
                    pst.setString(4, fileName);
                    pst.setString(5, multipartFile.getContentType());
                    pst.setInt(6, Integer.parseInt(curYear));
                    pst.executeUpdate();
                    
                    inputStream = multipartFile.getInputStream();
                    FileOutputStream fout = new FileOutputStream(dirpath + fileName);
                    // fout.write(myFile.getFileData());
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                        fout.write(bytes, 0, read);
                    }

                    fout.close();
                    fout.flush();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        if (path == null) {
            out.println(docAttId);
            out.close();
            out.flush();
            return null;
        } else {

            return new ModelAndView("/leaveapply/UploadLeaveDoc");
        }

//        if (docAttId == 0) {

//            return new ModelAndView("/leaveapply/UploadLeaveDoc");
//        } else {

//            out.println(docAttId);
//            out.close();
//            out.flush();
//             return null;
//           // return new ModelAndView("/leaveapply/UploadLeaveDoc");
//        }
    }

}

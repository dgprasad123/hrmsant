/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.leaveapply;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.leaveupload.LeaveUpload;
import hrms.model.login.LoginUserBean;
import hrms.model.upload.UploadedFile;
//import hrms.validator.UploadFileValidator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RemoveUploadFileController implements ServletContextAware {

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

    @RequestMapping(value = "removeuploadfile.htm", method = RequestMethod.POST)
    public void removeUploadedFile(
            @RequestParam("tempAttachment") String attId, @ModelAttribute("uploadedFile") LeaveUpload uploadedFile,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String filePath = servletContext.getInitParameter("FilePath");
            if (attId != null && !attId.equals("")) {
                st = con.createStatement();
                rs = st.executeQuery("SELECT ATTACH_YEAR,DISK_FILE_NAME FROM HW_ATTACHMENTS WHERE ATT_ID=" + attId);
                if (rs.next()) {
                    String dir_path = filePath + rs.getString("ATTACH_YEAR") + "\\" + rs.getString("DISK_FILE_NAME");
                    File f = new File(dir_path);
                    boolean deleted = f.delete();
                    if (deleted) {
                        stmt = con.prepareStatement("DELETE FROM HW_ATTACHMENTS WHERE ATT_ID=" + attId);
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(stmt);
        }

    }
}

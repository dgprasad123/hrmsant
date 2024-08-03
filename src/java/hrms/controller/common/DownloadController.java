/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.common;

import hrms.dao.common.DownloadDAO;
import hrms.model.common.DowanloadFile;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Manas
 */
@Controller
public class DownloadController {
    
    @Autowired
    DownloadDAO downloadDAO;
    
    @RequestMapping(value = "downloadAttchment", method = RequestMethod.GET)
    public void downloadAttachment(HttpServletResponse response, @RequestParam ("attachementId") int attachementId) throws Exception{
        //int attachmentId1 = Integer.parseInt(attachmentId);
        DowanloadFile dowanloadFile = downloadDAO.downloadGrievanceAttachment(attachementId);
        
        response.setContentLength(dowanloadFile.getFileData().length);
        response.setContentType(dowanloadFile.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + dowanloadFile.getFileName() + "\"");
        OutputStream out = response.getOutputStream();
        out.write(dowanloadFile.getFileData());
        out.close();
        out.flush();
    }
}

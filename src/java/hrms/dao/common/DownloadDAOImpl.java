/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.common;

import hrms.common.DataBaseFunctions;
import hrms.model.common.DowanloadFile;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas
 */
public class DownloadDAOImpl implements DownloadDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DowanloadFile downloadGrievanceAttachment(int attachmentId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DowanloadFile dowanloadFile = new DowanloadFile();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT o_file_name,attachment_id,file_path,file_type FROM EMPLOYEE_ATTACHMENT WHERE attachment_id = ?");
            pstmt.setInt(1, attachmentId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String filePath = rs.getString("file_path");
                dowanloadFile.setFileName(rs.getString("o_file_name"));
                dowanloadFile.setContentType(rs.getString("file_type"));
                File file = new File(filePath);                
                if (file.exists()) {
                    dowanloadFile.setFileData(Files.readAllBytes(file.toPath()));                    
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dowanloadFile;
    }
}

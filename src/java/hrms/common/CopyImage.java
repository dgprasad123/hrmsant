/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.common;

import hrms.common.DataBaseFunctions;
import hrms.common.CopyImageService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class CopyImage {
   public static void main(String[] args) {
        CopyImageService copyImageService = new CopyImageService();
        copyImageService.setSourceDir("/data/hrms/empphoto/");
        copyImageService.setTargetDir("/data/hrms/policephoto/");

        copyImagesById(copyImageService);
    }

    private static void copyImagesById(CopyImageService copyImageService) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            ps = con.prepareStatement("SELECT empid FROM equarter.emppolice limit 5");
            rs = ps.executeQuery();

            while (rs.next()) {
                String employeeId = rs.getString("empid");
                try {
                    copyImageService.copyEmployeeImages(employeeId);
                    System.out.println("Files copied successfully for employee ID: " + employeeId);
                } catch (CopyImageService.ImageNotFoundException e) {
                    // If the image is not found, copy the default image with HRMS ID as the filename
                    try {
                        copyImageService.copyDefaultImage(employeeId);
                        System.out.println("Default image copied successfully for employee ID: " + employeeId);
                    } catch (IOException ex) {
                        System.out.println("Error copying default image for employee ID " + employeeId + ": " + ex.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Error copying images for employee ID " + employeeId + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        } 
        finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}

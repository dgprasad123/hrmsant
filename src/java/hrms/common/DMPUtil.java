/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author Manas
 */
public class DMPUtil {

    private String folderPath;
    private String filname;

    public DMPUtil(String folderPath, String filname) throws Exception {
        this.folderPath = folderPath;
        this.filname = filname;
        boolean success = new File(folderPath).mkdirs();

        File f = new File(folderPath, filname);
        if (f.exists()) {
            f.delete();
            f.createNewFile();
        }
    }

    public DMPUtil() {
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getFilname() {
        return filname;
    }

    public void writeToFile(String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(folderPath, filname), true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(int text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(folderPath, filname), true));
            bw.write(text);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

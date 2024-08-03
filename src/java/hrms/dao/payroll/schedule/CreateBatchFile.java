/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.payroll.schedule;

import hrms.common.DMPUtil;
import java.util.ArrayList;

/**
 *
 * @author DurgaPrasad
 */
public class CreateBatchFile {
    private String folderPath;
    private String fileName = "132ColPrint.bat";
    public CreateBatchFile(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }
    public void write(ArrayList printCommand, String filename)throws Exception
    {
        DMPUtil dmpUtil = new DMPUtil(this.getFolderPath(),filename+".bat");
        for(int i=0;i<printCommand.size();i++){
            dmpUtil.writeToFile((String)printCommand.get(i));
        }
    }
    public void write(ArrayList printCommand)throws Exception
    {
        DMPUtil dmpUtil = new DMPUtil(this.getFolderPath(),"80ColPrint.bat");
        for(int i=0;i<printCommand.size();i++){
            dmpUtil.writeToFile((String)printCommand.get(i));
        }
    }
    
    public void write()throws Exception{
        DMPUtil dmpUtil = new DMPUtil(this.getFolderPath(),fileName);
        dmpUtil.writeToFile("TYPE ACQT.txt > lpt1");        
        dmpUtil.writeToFile("TYPE GPFSCHEDULE.txt > lpt1");
    }
}

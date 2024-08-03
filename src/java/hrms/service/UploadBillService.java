/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import hrms.common.DataBaseFunctions;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;


import java.io.OutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;

import org.apache.commons.io.FilenameUtils;

@Service
public class UploadBillService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String uploadThroughSftp() {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        
        String hostName = "164.100.141.166";
        String username = "hrms2";
        String password = "hrm$ifm$321";
        String uploadPath = "/hrms/upload/";
       // String filePath = "D:\\paybillxml";
        String filePath="/data1/hrms/payBillXMLDOC/";
        JSch jsch = new JSch();
        Session session = null;
        FileInputStream inputStream = null;
        
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String submissionDate = dateFormat.format(cal.getTime());
        String msg = " No of Files Uploaded to Treasury Server";
        int countFile = 0;
        ChannelSftp channelSftp = null;
        try {
            
            con = this.dataSource.getConnection();
            
            session = jsch.getSession(username, hostName, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);

            session.connect();

            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            BufferedOutputStream bos = null;
            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(uploadPath);
            File folder = new File(filePath);

            File[] listOfFiles = folder.listFiles();
            String billno = "";
            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        String remoteFileName = listOfFiles[i].getName();
                        if (FilenameUtils.getExtension(remoteFileName).equalsIgnoreCase("zip")) {
                            countFile++;
                            try {
                                
                                File firstLocalFile = new File(filePath + "/" + remoteFileName);
                                inputStream = new FileInputStream(firstLocalFile);
                                channelSftp.put(inputStream, uploadPath + remoteFileName);
                                
                                billno = remoteFileName.substring(remoteFileName.lastIndexOf("-") + 1, remoteFileName.lastIndexOf("."));

                                if (billno.indexOf("_") > -1) {
                                    billno = billno.substring(0, billno.indexOf("_"));
                                }

                                pst = con.prepareStatement("INSERT INTO BILL_STATUS_HISTORY (BILL_ID,HISTORY_DATE,STATUS_ID,REMARK) VALUES (?,?,?,?)");
                                pst.setInt(1, Integer.parseInt(billno));
                                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submissionDate).getTime()));
                                pst.setInt(3, 3);
                                pst.setString(4, "DELIVERED TO TREASURY");
                                pst.execute();

                                firstLocalFile.delete();
                            } catch (Exception sf) {
                                sf.printStackTrace();
                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            channelSftp.disconnect();
        }
        msg = countFile + msg;
        return msg;
    }

}

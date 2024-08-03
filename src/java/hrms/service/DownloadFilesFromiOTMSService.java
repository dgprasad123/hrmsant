package hrms.service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import hrms.common.DataBaseFunctions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.Vector;


@Service
public class DownloadFilesFromiOTMSService {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void insertLog(String startTime, String endTime, String remarks) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO PAYBILL_SERVICE_LOG (SERVICE_ID, START_TIME, STOP_TIME, REMARKS) VALUES(?,?,?,?)");
            pst.setInt(1, 8);
            pst.setString(2, startTime);
            pst.setString(3, endTime);
            pst.setString(4, remarks);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
    public String downloadthroughSFTP() throws Exception {
        
        String hostName = "164.100.141.166";
        String username = "hrms2";
        String password = "hrm$ifm$321";
        String remotePath = "/hrms/download/";

        String localPath = "/data1/hrms/payBillXMLDOC/download/";
        JSch jsch = new JSch();
        Session session = null;

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
        ChannelSftp channelSftp = (ChannelSftp) channel;
        boolean success = false;
        FileOutputStream fos = null;
        if (channelSftp != null) {
            try {
                
                //changeWorkingDirectory(channelSftp, remotePath);
                channelSftp.cd(remotePath);
                Vector<ChannelSftp.LsEntry> filesToDownload = channelSftp.ls(remotePath);
                if (!filesToDownload.isEmpty()) {
                    for (ChannelSftp.LsEntry ent : filesToDownload) {
                        String filename = ent.getFilename();
                        if (filename.contains("BILL_DETAILS")) {
                            success = true;
                        }

                    }
                    if (success == true) {

                        for (ChannelSftp.LsEntry ent : filesToDownload) {
                            success = false;
                            String filename = ent.getFilename();
                            if (filename.endsWith(".xml")) {
                                fos = new FileOutputStream(localPath + ent.getFilename());
                                channelSftp.get(ent.getFilename(), fos);
                                fos.close();
                                
                                /*
                                    this code will remove files from download path
                                */
                                    
                                channelSftp.rm(remotePath + "/" + ent.getFilename());
                                
                                /*
                                    this code will remove files from download path
                                */
                                
                            }

                        }
                    }

                } else {
                    //ServerConstants.NO_FILES_TO_DOWNLOAD + ServerUtils.getDateTime();
                    success = true;
                }
            } catch (SftpException e) {
                e.printStackTrace();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                session.disconnect();
                try {
                    channelSftp.disconnect();
                    if (os != null) {
                        os.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    String message
                            = "Error while closing the streams" + e.getMessage();
                    throw new Exception(message);
                }
            }
        }
        String msg = "";
        return msg;
    }

}

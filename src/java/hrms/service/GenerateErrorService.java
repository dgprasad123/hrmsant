/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.DataBaseFunctions;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Surendra
 */
@Service
public class GenerateErrorService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    int threadParm1;
    String threadParm2;
    public static boolean toggle = false;

    public String downloadFromTreasuryReadXML() {

        //String localfilePath = "C:\\Users\\LENOVO\\Desktop\\paybillservice\\token\\paybill\\";
        String localfilePath = "/data1/hrms/payBillXMLDOC/download/";
        Document doc = null;
        Connection conn = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String submissionDate = dateFormat.format(cal.getTime());
        ResultSet rs = null;
        Statement st = null;
        int errorOccured = 0;
        int sucessfullyExtracted = 0;
        int totfile = 0;
        String msg = "";
        try {

            conn = this.dataSource.getConnection();

            st = conn.createStatement();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            File folder = new File(localfilePath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].length() > 0) {

                    /*
                     *  Verify whether it is a xml file or Other file,
                     *  If XML file then it will proceed next work
                     */
                    if (FilenameUtils.getExtension(listOfFiles[i].getName()).equalsIgnoreCase("xml")) {

                        String fileName = FilenameUtils.getBaseName(listOfFiles[i].getName());
                        String lastChar = fileName.substring(fileName.lastIndexOf("_"));
                        /*
                         *  Get the required XML file which last character contain _1 and _3
                         *  
                         *  
                         */
                        if (lastChar != null && !lastChar.equals("")) {
                            if (lastChar.equals("_1")) {
                                delete(listOfFiles[i]);
                                sucessfullyExtracted = sucessfullyExtracted + 1;

                            } else if (lastChar.equals("_3")) {

                                errorOccured = errorOccured + 1;
                                try {
                                    doc = docBuilder.parse(new File(localfilePath + listOfFiles[i].getName()));
                                    doc.getDocumentElement().normalize();
                                    NodeList listOfPersons = doc.getElementsByTagName("ROW");
                                    String errorMessage = "";
                                    String desc = "";
                                    String billId = "";
                                    for (int temp = 0; temp < listOfPersons.getLength(); temp++) {
                                        Node nNode = listOfPersons.item(temp);
                                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement = (Element) nNode;
                                            NodeList nodeList = eElement.getElementsByTagName("DESCRIPTION");
                                            NodeList nodeListZipName = eElement.getElementsByTagName("ZIP_FILE_NAME");
                                            NodeList nodeListDescription = eElement.getElementsByTagName("DESCRIPTION");

                                            String zippFileName = "";
                                            if (nodeListZipName.getLength() > 0) {
                                                zippFileName = eElement.getElementsByTagName("ZIP_FILE_NAME").item(0).getTextContent();
                                            }

                                            desc = "";
                                            if (nodeListDescription.getLength() > 0) {

                                                desc = eElement.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
                                            }
                                            billId = "";
                                            billId = zippFileName.substring(zippFileName.lastIndexOf("-") + 1, zippFileName.lastIndexOf("."));

                                            if (billId.indexOf("_") > 0) {

                                                billId = billId.substring(0, billId.indexOf("_"));
                                            }

                                            if (nodeList.getLength() > 0) {
                                                /*
                                                 *  Then DataBase Manupulation 
                                                 */
                                            }
                                        }

                                        if (errorMessage != null && !errorMessage.equals("")) {
                                            errorMessage = errorMessage + "@" + desc;
                                        } else {
                                            errorMessage = desc;
                                        }
                                    }
                                    if (errorMessage.length() > 4000) {
                                        errorMessage = errorMessage.substring(0, 3999);
                                    }
                                    ps = conn.prepareStatement("INSERT INTO BILL_STATUS_HISTORY (BILL_ID,HISTORY_DATE,STATUS_ID,REMARK) VALUES (?,?,?,?)");
                                    ps.setInt(1, Integer.parseInt(billId));

                                    ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submissionDate).getTime()));
                                    ps.setInt(3, 4);
                                    ps.setString(4, errorMessage);
                                    ps.execute();

                                    // Update bill status id inside bill mast table
                                    rs = st.executeQuery("SELECT BILL_STATUS_ID FROM BILL_MAST WHERE BILL_NO=" + billId);
                                    if (rs.next() && rs.getInt("BILL_STATUS_ID") <= 4) {
                                        ps = conn.prepareStatement("UPDATE BILL_MAST SET BILL_STATUS_ID=? WHERE  BILL_NO=?");
                                        ps.setInt(1, 4);
                                        ps.setInt(2, Integer.parseInt(billId));
                                        ps.execute();

                                    }
                                    delete(listOfFiles[i]);
                                } catch (SAXParseException sas) {
                                    sas.printStackTrace();
                                }
                            }
                        } else {
                            /*
                             *  Other than UNDERSCORE file
                             * 
                             */
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(conn);

        }
        msg = totfile + " No of Files Downloaded," + " " + sucessfullyExtracted + " no of files extracted, " + errorOccured + " no of files error occured";
        return msg;
    }

    public void insertLog(String startTime, String endTime, String remarks) {

        Connection con = null;

        PreparedStatement ps = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("INSERT INTO PAYBILL_SERVICE_LOG (SERVICE_ID, START_TIME, STOP_TIME, REMARKS) VALUES(?,?,?,?)");
            ps.setInt(1, 2);
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(startTime).getTime()));
            ps.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(endTime).getTime()));
            ps.setString(4, remarks);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public static boolean delete(File resource) throws IOException {
        if (resource.isDirectory()) {
            File[] childFiles = resource.listFiles();
            for (File child : childFiles) {
                delete(child);
            }
        }
        return resource.delete();
    }

}

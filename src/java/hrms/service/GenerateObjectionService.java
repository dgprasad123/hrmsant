package hrms.service;

import hrms.common.DataBaseFunctions;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class GenerateObjectionService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String processBillObjection() throws SQLException, ParseException {
        String filePath = "/data1/hrms/payBillXMLDOC/download/";
        //String filePath="C:\\Users\\cmgiws1\\Desktop\\Finance\\";
        String archivePath = "/data1/hrms/payBillXMLDOC/download/archive";
        Document doc = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String submissionDate = dateFormat.format(cal.getTime());

        Connection conn = null;

        ResultSet rs = null;
        Statement st = null;

        ResultSet rs2 = null;
        Statement st2 = null;
        String msg = "";
        try {
            conn = this.dataSource.getConnection();

            st = conn.createStatement();
            st2 = conn.createStatement();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {

                    /*
                     *  Verify whether it is a xml file or Other file,
                     *  If XML file then it will proceed next work
                     */
                    if (FilenameUtils.getExtension(listOfFiles[i].getName()).equalsIgnoreCase("xml")) {

                        String fileName = FilenameUtils.getBaseName(listOfFiles[i].getName());

                        /*
                         *  OBJECTION_DETAILS
                         *  
                         */
                        if (fileName != null && !fileName.equals("") && fileName.indexOf("OBJECTION_DETAILS") > -1) {
                            File sourceFile = new File(filePath + listOfFiles[i].getName());
                            doc = docBuilder.parse(sourceFile);
                            doc.getDocumentElement().normalize();
                            NodeList listOfPersons = doc.getElementsByTagName("ROW");
                            int noofObjection = 0;
                            for (int temp = 0; temp < listOfPersons.getLength(); temp++) {
                                String billno = "";
                                String errorMessage = "";

                                Node nNode = listOfPersons.item(temp);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;
                                    NodeList nodeBillNo = eElement.getElementsByTagName("HRMIS_BILL_NUMBER");
                                    NodeList nodeObjDesc = eElement.getElementsByTagName("OBJECTION_DESCRIPTION");

                                    if (nodeBillNo.getLength() > 0) {
                                        billno = eElement.getElementsByTagName("HRMIS_BILL_NUMBER").item(0).getTextContent();
                                    }

                                    if (nodeObjDesc.getLength() > 0) {
                                        errorMessage = eElement.getElementsByTagName("OBJECTION_DESCRIPTION").item(0).getTextContent();
                                    }

                                    if (nodeBillNo.getLength() > 0) {
                                        /*
                                         *  Then DataBase Manupulation 
                                         */

                                        ps = conn.prepareStatement("INSERT INTO BILL_STATUS_HISTORY (BILL_ID,HISTORY_DATE,STATUS_ID,REMARK) VALUES (?,?,?,?)");
                                        ps.setInt(1, Integer.parseInt(billno));
                                        ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(submissionDate).getTime()));
                                        ps.setInt(3, 8);
                                        ps.setString(4, errorMessage);
                                        ps.execute();

                                        // Update bill status id inside bill mast table
                                        //rs = st.executeQuery("SELECT BILL_STATUS_ID,TOKEN_NO,TOKEN_DATE,IS_RESUBMITTED FROM BILL_MAST WHERE BILL_NO=" + billno);
                                        rs = st.executeQuery("SELECT BILL_STATUS_ID,TOKEN_NO,TOKEN_DATE,IS_RESUBMITTED FROM BILL_MAST WHERE BILL_NO=" + billno + " and TOKEN_DATE IS NOT NULL");

                                        if (rs.next()) {
                                            if (rs.getString("IS_RESUBMITTED") == null || rs.getString("IS_RESUBMITTED").equals("") || rs.getString("IS_RESUBMITTED").equalsIgnoreCase("Y")) {

                                                ps = conn.prepareStatement("UPDATE BILL_MAST SET TOKEN_NO=NULL, TOKEN_DATE=NULL, BILL_STATUS_ID=?, PREVIOUS_TOKEN_NO=?, PREVIOUS_TOKEN_DATE=?, IS_RESUBMITTED='Y' WHERE  BILL_NO=?");
                                                ps.setInt(1, 8);
                                                ps.setString(2, rs.getString("TOKEN_NO"));
                                                if (rs.getString("TOKEN_DATE") != null && !rs.getString("TOKEN_DATE").equals("")) {
                                                    ps.setTimestamp(3, new Timestamp(rs.getDate("TOKEN_DATE").getTime()));
                                                } else {
                                                    ps.setTimestamp(3, null);
                                                }
                                                ps.setInt(4, Integer.parseInt(billno));
                                                ps.execute();
                                                noofObjection++;

                                            } else {
                                                msg = msg + " " + billno;
                                            }

                                        }

                                    }
                                }
                            }
                            //Archieve the Voucher XML File to archieve folder after reading the file//
                            File archieveFile = new File(archivePath + "/" + sourceFile.getName());
                            FileUtils.copyFile(sourceFile, archieveFile);
                            //Delete the source file from source folder//
                            delete(listOfFiles[i]);
                        }
                    }

                }
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return msg;
    }

    private boolean delete(File resource) throws IOException {
        if (resource.isDirectory()) {
            File[] childFiles = resource.listFiles();
            for (File child : childFiles) {
                delete(child);
            }
        }
        return resource.delete();
    }
}

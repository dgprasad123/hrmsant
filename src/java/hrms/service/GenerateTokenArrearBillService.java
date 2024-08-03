package hrms.service;

import hrms.common.DataBaseFunctions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GenerateTokenArrearBillService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    int threadParm1;
    String threadParm2;
    public static boolean toggle = false;

    public void insertLog(String startTime, String endTime, String remarks) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO PAYBILL_SERVICE_LOG (SERVICE_ID, START_TIME, STOP_TIME, REMARKS) VALUES(?,?,?,?)");
            pst.setInt(1, 3);
            pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(startTime).getTime()));
            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(endTime).getTime()));
            pst.setString(4, remarks);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String updateToken() {
       // String readPath = "C:\\Users\\LENOVO\\Desktop\\paybillservice\\token\\paybill\\";
        String readPath="/data1/hrms/payBillXMLDOC/download/";

        // String readPath="/home/paybillxml/download/";
        //   String archivePath="/home/paybillxml/download/archive";
        //String archivePath = "C:\\Users\\LENOVO\\Desktop\\paybillservice\\token\\";
        String archivePath = "/data1/hrms/payBillXMLDOC/archive/";
        Document doc = null;

        Connection con = null;
        PreparedStatement pst = null;

        FileOutputStream fout = null;
        int tokengenerated = 0;
        String msg = "";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE BILL_MAST SET TOKEN_NO=? , TOKEN_DATE=?, BILL_STATUS_ID=? WHERE BILL_NO=?");
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            File dir = new File(readPath);
            File[] matches = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("BILL_DETAILS_") && name.endsWith(".xml");
                }
            });
            if (matches != null) {
                for (int i = 0; i < matches.length; i++) {
                    if (matches[i].isFile()) {
                        /*
                         *  Verify whether it is a xml file or Other file,
                         *  If XML file then it will proceed next work
                         */
                        if (FilenameUtils.getExtension(matches[i].getName()).equalsIgnoreCase("xml")) {
                            try {
                                doc = docBuilder.parse(new File(readPath + matches[i].getName()));
                                doc.getDocumentElement().normalize();
                                NodeList listOfPersons = doc.getElementsByTagName("ROW");
                                for (int temp = 0; temp < listOfPersons.getLength(); temp++) {
                                    Node nNode = listOfPersons.item(temp);
                                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                        tokengenerated = tokengenerated + 1;
                                        Element eElement = (Element) nNode;
                                        NodeList nodeListBillNumber = eElement.getElementsByTagName("HRMIS_BILL_NUMBER");
                                        NodeList nodeListTokenNumber = eElement.getElementsByTagName("TOKEN_NUMBER");
                                        NodeList nodeListTokenDate = eElement.getElementsByTagName("TOKEN_DATE");
                                        String billId = "";
                                        if (nodeListBillNumber.getLength() > 0) {
                                            billId = eElement.getElementsByTagName("HRMIS_BILL_NUMBER").item(0).getTextContent();

                                        }
                                        String tokenNumber = "";
                                        if (nodeListTokenNumber.getLength() > 0) {
                                            tokenNumber = eElement.getElementsByTagName("TOKEN_NUMBER").item(0).getTextContent();
                                        }
                                        String tokenDate = "";
                                        if (nodeListTokenDate.getLength() > 0) {
                                            tokenDate = eElement.getElementsByTagName("TOKEN_DATE").item(0).getTextContent();
                                            String str[] = tokenDate.split(" ");
                                            tokenDate = str[0];
                                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                            Date d = (Date) formatter.parse(tokenDate);
                                            DateFormat oraFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                            tokenDate = oraFormat.format(d);

                                        }

                                        pst.setString(1, tokenNumber);
                                        pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tokenDate).getTime()));
                                        pst.setInt(3, 5);
                                        pst.setInt(4, Integer.parseInt(billId));
                                        pst.execute();
                                    }
                                }
                            } catch (SAXParseException sas) {
                                sas.printStackTrace();
                            }
                        }
                    }
                    File f2 = new File(archivePath + "/" + matches[i].getName());
                    FileUtils.copyFile(matches[i], f2);

                    delete(matches[i]);
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        msg = tokengenerated + " no of Tokens generated";
        return msg;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.DataBaseFunctions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class GenerateVoucherService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String processVoucher() throws SQLException {
        String readPath = "/data1/hrms/payBillXMLDOC/download/";
        //String readPath="D:\\Voucher\\";
        String archivePath = "/data1/hrms/payBillXMLDOC/download/archive/";
        //String archivePath="D:\\Voucher\\Archive\\";
        Document doc = null;

        Connection conn = null;
        PreparedStatement ps = null;
        FileOutputStream fout = null;
        String msg = "";

        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement("UPDATE BILL_MAST SET VCH_NO=? , VCH_DATE=?, BILL_STATUS_ID=? WHERE BILL_NO=?");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            File dir = new File(readPath);

            File[] matches = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("VOUCHER_DETAILS_") && name.endsWith(".xml");
                }
            });
            for (int i = 0; i < matches.length; i++) {

                if (matches[i].isFile()) {
                    
                    //  Verify whether it is a xml file or Other file,
                    //  If XML file then it will proceed next work
                    double filesize = matches[i].length();
                    if (FilenameUtils.getExtension(matches[i].getName()).equalsIgnoreCase("xml") && filesize > 1) {
                        File sourceFile = new File(readPath + matches[i].getName());
                        doc = docBuilder.parse(sourceFile);
                        doc.getDocumentElement().normalize();
                        NodeList listOfPersons = doc.getElementsByTagName("ROW");
                        int noofbill = 0;
                        for (int temp = 0; temp < listOfPersons.getLength(); temp++) {
                            Node nNode = listOfPersons.item(temp);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                NodeList nodeListBillNumber = eElement.getElementsByTagName("HRMIS_BILL_NUMBER");
                                NodeList nodeListTokenNumber = eElement.getElementsByTagName("VOUCHER_NUMBER");
                                NodeList nodeListTokenDate = eElement.getElementsByTagName("VOUCHER_DATE");
                                String billId = "";
                                
                                if (nodeListBillNumber.getLength() > 0) {
                                    billId = eElement.getElementsByTagName("HRMIS_BILL_NUMBER").item(0).getTextContent();
                                }
                                
                                String tokenNumber = "";
                                if (nodeListTokenNumber.getLength() > 0) {
                                    tokenNumber = eElement.getElementsByTagName("VOUCHER_NUMBER").item(0).getTextContent();
                                }
                                
                                String tokenDate = "";
                                if (nodeListTokenDate.getLength() > 0) {
                                    tokenDate = eElement.getElementsByTagName("VOUCHER_DATE").item(0).getTextContent();

                                    String str[] = tokenDate.split(" ");
                                    tokenDate = str[0];
                                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    Date d = (Date) formatter.parse(tokenDate);

                                    DateFormat oraFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    tokenDate = oraFormat.format(d);

                                }
                                ps.setString(1, tokenNumber);
                                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tokenDate).getTime()));
                                ps.setInt(3, 7);
                                ps.setInt(4, Integer.parseInt(billId));
                                ps.execute();
                                noofbill++;
                            }
                        }
                        if (noofbill > 0) {
                            msg = msg + " Vouchered File Name is " + matches[i].getName() + " and No of Vouchered Generated is  " + noofbill + " \n";
                        }
                        //Archieve the Voucher XML File to archieve folder after reading the file//
                        File archieveFile = new File(archivePath + "/" + sourceFile.getName());
                        FileUtils.copyFile(sourceFile, archieveFile);
                        //Delete the source file from source folder//
                        sourceFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(conn);
        }

        return msg;
    }

    public void insertLog(String startTime, String endTime, String remarks) {

        Connection con = null;

        PreparedStatement ps = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("INSERT INTO PAYBILL_SERVICE_LOG (SERVICE_ID, START_TIME, STOP_TIME, REMARKS) VALUES(?,?,?,?)");
            ps.setInt(1, 5);
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
}

package hrms.dao.empinfo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.empinfo.EmployeeInformation;
import hrms.model.empinfo.MiscInfoBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class EmployeeInformationDAOImpl implements EmployeeInformationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public EmployeeInformation getEmployeeData(String empid, String gpf) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmployeeInformation empinfo = new EmployeeInformation();
        String param = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT EMP_ID,GPF_NO,FULL_NAME,MOBILE,SPN,CADRE_NAME,CUR_CADRE_CODE FROM"
                    + " (SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,MOBILE,GPF_NO,CUR_SPC,CUR_CADRE_CODE FROM EMP_MAST WHERE";
            if (empid != null && !empid.equals("")) {
                sql = sql + " EMP_ID=?";
            } else if (gpf != null && !gpf.equals("")) {
                sql = sql + " GPF_NO=?";
            }
            sql = sql + ")EMP_MAST LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE";

            pst = con.prepareStatement(sql);
            if (empid != null && !empid.equals("")) {
                param = empid;
            } else if (gpf != null && !gpf.equals("")) {
                param = gpf;
            }
            pst.setString(1, param);
            rs = pst.executeQuery();
            if (rs.next()) {
                empinfo.setEmpid(rs.getString("EMP_ID"));
                empinfo.setGpfno(rs.getString("GPF_NO"));
                empinfo.setEmpname(rs.getString("FULL_NAME"));
                empinfo.setEmpdesg(rs.getString("SPN"));
                empinfo.setCadreCode(rs.getString("CUR_CADRE_CODE"));
                empinfo.setCadreName(rs.getString("CADRE_NAME"));
                empinfo.setMobile(rs.getString("MOBILE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empinfo;
    }

    @Override
    public void updateEmployeeData(EmployeeInformation empinfo) {

        Connection conpsql = null;

        PreparedStatement pst = null;
        String sql = "";
        try {
            conpsql = this.dataSource.getConnection();

            if (empinfo.getCadreCode() != null && !empinfo.getCadreCode().equals("")) {
                sql = "UPDATE EMP_MAST SET CUR_CADRE_CODE=? WHERE EMP_ID=?";
                pst = conpsql.prepareStatement(sql);
                pst.setString(1, empinfo.getCadreCode());
                pst.setString(2, empinfo.getEmpid());
                pst.executeUpdate();
            }
            if (empinfo.getMobile() != null && !empinfo.getMobile().equals("")) {
                sql = "UPDATE EMP_MAST SET MOBILE=? WHERE EMP_ID=?";
                pst = conpsql.prepareStatement(sql);
                pst.setString(1, empinfo.getMobile());
                pst.setString(2, empinfo.getEmpid());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(conpsql);

        }
    }

    @Override
    public boolean isupdatePayOrPostingInfo(String empId, String wefDate, String ordDate, String updateType) {

        Connection conpsql = null;

        boolean update = false;
        Statement st = null;
        ResultSet rs = null;
        Date wefCdate = null;
        Date empWefDatepayCdate = null;
        Date empWefdate = null;
        Date ordCdate = null;
        Date empOrdDate = null;
        Date sysDate = null;
        String dateform = "";
        Date dt = new Date();
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            conpsql = this.dataSource.getConnection();

            st = conpsql.createStatement();
            if (updateType != null && !updateType.equals("")) {
                if (updateType.equalsIgnoreCase("PAY")) {
                    String empmastOrddate = "";
                    String empmastWefDate = "";
                    rs = st.executeQuery("SELECT PAY_DATE, ST_DATE_OF_CUR_SALARY FROM EMP_MAST WHERE EMP_ID='" + empId + "'");
                    if (rs.next()) {
                        if (rs.getString("ST_DATE_OF_CUR_SALARY") != null && !rs.getString("ST_DATE_OF_CUR_SALARY").equals("")) {
                            empmastOrddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ST_DATE_OF_CUR_SALARY"));
                            if ((ordDate != null && !ordDate.equals("")) && (wefDate != null && !wefDate.equals(""))) {
                                ordCdate = (Date) formatter.parse(ordDate);
                                if (empmastOrddate != null && !empmastOrddate.equals("")) {
                                    empOrdDate = (Date) formatter.parse(empmastOrddate);
                                }
                                if (dt != null) {
                                    dateform = formatter.format(dt);
                                    sysDate = (Date) formatter.parse(dateform);
                                }
                                if (rs.getString("PAY_DATE") != null && !rs.getString("PAY_DATE").equals("")) {
                                    empmastWefDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("PAY_DATE"));
                                    if (wefDate != null && !wefDate.equals("")) {
                                        wefCdate = (Date) formatter.parse(wefDate);
                                    }
                                    if (empmastWefDate != null && !empmastWefDate.equals("")) {
                                        empWefDatepayCdate = (Date) formatter.parse(empmastWefDate);
                                    }

                                    if (ordCdate.compareTo(empOrdDate) < 0 && wefCdate.compareTo(empWefDatepayCdate) < 0) {
                                        update = false; // both order date and wef date are smaller
                                    } else if (ordCdate.compareTo(empOrdDate) <= 0 && wefCdate.compareTo(empWefDatepayCdate) >= 0 && sysDate.compareTo(wefCdate) >= 0) {
                                        update = true; // order date is smaller and wef date is greater and system date is greater than wef date
                                    } else if (ordCdate.compareTo(empOrdDate) >= 0 && wefCdate.compareTo(empWefDatepayCdate) <= 0) {
                                        update = true; // order date is greater and wef date is smaller 
                                    } else if (ordCdate.compareTo(empOrdDate) >= 0 && wefCdate.compareTo(empWefDatepayCdate) >= 0) {
                                        //&& sysDate.compareTo(wefCdate)>=0
                                        update = true; // order date is greater and wef date is greater but not wef date is greater than sysdate
                                    }

                                } else {
                                    update = true;
                                }
                            } else {
                                update = false;
                            }
                        } else {
                            update = true;
                        }
                    } else {
                        update = true;
                        //no record found inside emp_mast table
                    }
                } else if (updateType.equalsIgnoreCase("POSTING")) {
                    String empmastOrdDate = "";
                    String empmastWefdate = "";
                    rs = st.executeQuery("SELECT POST_ORDER_DATE, CURR_POST_DOJ FROM EMP_MAST WHERE EMP_ID='" + empId + "'");
                    if (rs.next()) {
                        if (rs.getString("CURR_POST_DOJ") != null && !rs.getString("CURR_POST_DOJ").equals("")) {
                            empmastOrdDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("CURR_POST_DOJ"));
                            if ((ordDate != null && !ordDate.equals("")) && (wefDate != null && !wefDate.equals(""))) {
                                ordCdate = (Date) formatter.parse(ordDate);
                                if (empmastOrdDate != null && !empmastOrdDate.equals("")) {
                                    empOrdDate = (Date) formatter.parse(empmastOrdDate);
                                }
                                if (dt != null) {
                                    dateform = formatter.format(dt);
                                    sysDate = (Date) formatter.parse(dateform);
                                }
                                if (rs.getString("POST_ORDER_DATE") != null && !rs.getString("POST_ORDER_DATE").equals("")) {
                                    empmastWefdate = CommonFunctions.getFormattedOutputDate1(rs.getDate("POST_ORDER_DATE"));
                                    if (wefDate != null && !wefDate.equals("")) {
                                        wefCdate = (Date) formatter.parse(wefDate);
                                    }
                                    if (empmastWefdate != null && !empmastWefdate.equals("")) {
                                        empWefdate = (Date) formatter.parse(empmastWefdate);
                                    }

                                    if (ordCdate.compareTo(empOrdDate) < 0 && wefCdate.compareTo(empWefdate) < 0) {
                                        update = false; // both order date and wef date are smaller
                                    } else if (ordCdate.compareTo(empOrdDate) <= 0 && wefCdate.compareTo(empWefdate) >= 0 && sysDate.compareTo(wefCdate) >= 0) {
                                        update = true; // order date is smaller and wef date is greater and system date is greater than wef date
                                    } else if (ordCdate.compareTo(empOrdDate) >= 0 && wefCdate.compareTo(empWefdate) <= 0) {
                                        update = true; // order date is greater and wef date is smaller 
                                    } else if (ordCdate.compareTo(empOrdDate) >= 0 && wefCdate.compareTo(empWefdate) >= 0) {
                                        //&& sysDate.compareTo(wefCdate)>=0
                                        update = true; // order date is greater and wef date is greater but not wef date is greater than sysdate
                                    }
                                } else {
                                    update = true;
                                }
                            } else {
                                update = false;
                            }
                        } else {
                            update = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(conpsql);
        }
        return update;

    }

    @Override
    public void updateEmpPayInfoOnDate(EmployeeInformation ei, String empid) {

        Connection conpsql = null;

        PreparedStatement pst = null;

        try {
            conpsql = this.dataSource.getConnection();
            String sql = "UPDATE EMP_MAST SET PAY_DATE=?,CUR_SALARY=?,CUR_BASIC_SALARY=?,GP=?,SPAY=?,PPAY=?,OTHPAY=? WHERE EMP_ID=?";
            pst = conpsql.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(ei.getPaydate()).getTime()));
            pst.setString(2, ei.getPayscale());
            pst.setDouble(3, ei.getBasic());
            pst.setDouble(4, ei.getGp());
            pst.setInt(5, ei.getSpay());
            pst.setInt(6, ei.getPpay());
            pst.setInt(7, ei.getOthpay());
            pst.setString(8, empid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(conpsql);
        }
    }

    public ArrayList getMiscInfoList(String empId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM emp_notification WHERE emp_id = ? AND not_type = 'MISCELLANEOUS'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                MiscInfoBean mInfoBean = new MiscInfoBean();
                mInfoBean.setDateOfEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                mInfoBean.setNotificationId(rs.getInt("not_id"));
                mInfoBean.setNotificationType(rs.getString("not_type"));
                mInfoBean.setOrderNumber(rs.getString("ordno"));
                mInfoBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                mInfoBean.setNote(rs.getString("note"));
                mInfoBean.setIfVisible(rs.getString("if_visible"));
                mInfoBean.setIsValidated(rs.getString("is_validated"));
                al.add(mInfoBean);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public MiscInfoBean getMiscInfoData(NotificationBean nb) {
        MiscInfoBean mBean = new MiscInfoBean();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            mBean.setNotificationId(nb.getNotid());
            mBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(nb.getOrdDate()));
            mBean.setOrderNumber(nb.getOrdno());
            mBean.setHidTempDeptCode(nb.getSancDeptCode());
            mBean.setHidTempAuthOffCode(nb.getSancOffCode());
            mBean.setHidTempAuthPost(nb.getSancAuthCode());
            mBean.setRadpostingauthtype(nb.getRadpostingauthtype());
            if (mBean.getRadpostingauthtype() != null && mBean.getRadpostingauthtype().equals("GOI")) {
                mBean.setAuthPostName(getOtherSpn(nb.getSancAuthCode()));
                mBean.setHidOthSpc(nb.getSancAuthCode());
            } else {
                mBean.setAuthPostName(CommonFunctions.getSPN(con, nb.getSancAuthCode()));
                mBean.setAuthSpc(nb.getSancAuthCode());
            }
            mBean.setHidAuthDeptCode(nb.getSancDeptCode());
            mBean.setHidAuthOffCode(nb.getSancOffCode());
            mBean.setHidPostedDeptCode(nb.getSancDeptCode());
            mBean.setHidPostedOffCode(nb.getSancOffCode());
            mBean.setNote(nb.getNote());
            mBean.setIfVisible(nb.getIfVisible());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mBean;
    }

    @Override
    public EmployeeInformation GetIcardDetails(String empid, String gpf) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmployeeInformation empinfo = new EmployeeInformation();
        String param = "";
        try {
            con = dataSource.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empinfo;
    }

    @Override
    public void getICardPDF(Document document, Users userBean, String filePath) {

        Connection con = null;
        try {

            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(60);

            PdfPCell cell = null;

            String url = filePath + "odgovt.gif";
            File f = null;
            f = new File(url);
            Image img1 = null;
            if (f.exists()) {
                img1 = Image.getInstance(url);
            }
            if (img1 != null) {
                img1.scalePercent(2f);
                cell = new PdfPCell(img1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                // cell.setRowspan(2);
                table.addCell(cell);

            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            //header row
            cell = new PdfPCell(new Phrase("Identity Card", boldTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //1st row        
            cell = new PdfPCell(new Phrase("Id No: " + userBean.getEmpId(), hdrTextFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //  cell.setVerticalAlignment(Element.ALIGN_CENTER);
            //cell.setColspan(2);
            table.addCell(cell);

            //2nd row        
            cell = new PdfPCell(new Phrase(userBean.getDeptName(), bigTextFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //  cell.setVerticalAlignment(Element.ALIGN_CENTER);
            //cell.setColspan(2);
            table.addCell(cell);

            String eol = System.getProperty("line.separator");
            //3rd row row
            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA.", bigTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //Blank row
            cell = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //  cell.setColspan(2);
            table.addCell(cell);

            Image img2 = null;
            String url2 = filePath + userBean.getEmpId() + ".jpg";
            f = new File(url2);

            if (f.exists()) {
                img2 = Image.getInstance(url2);
            } else {
                url2 = filePath + "NoEmployee.png";
                img2 = Image.getInstance(url2);
            }
            if (img2 != null) {
                img2.scalePercent(10f);
                cell = new PdfPCell(img2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                // cell.setColspan(2);
                table.addCell(cell);

            } else {

                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                //  cell.setColspan(2);
                table.addCell(cell);
            }

            //4th row
            cell = new PdfPCell(new Phrase(userBean.getFullName(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //5th row
            cell = new PdfPCell(new Phrase(userBean.getPostname(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //  cell.setColspan(2);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidthPercentage(60);

            //6th row
            cell = new PdfPCell(new Phrase("Department/ Office/ Organisation:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //cell = new PdfPCell(new Phrase(userBean.getDeptName(), hdrTextFont));
            cell = new PdfPCell(new Phrase(userBean.getOffname(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            Calendar now = Calendar.getInstance();
            now.setTime(dateFormat.parse(dateFormat.format(date)));
            now.add(Calendar.YEAR, 4);
            String yearAdd = dateFormat.format(now.getTime());

            now.setTime(dateFormat.parse(yearAdd));
            now.add(Calendar.DATE, -1);
            String daysub = dateFormat.format(now.getTime());

            //7th row
            cell = new PdfPCell(new Phrase("Date of Issue:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(dateFormat.format(date), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //8th row
            cell = new PdfPCell(new Phrase("Valid Upto:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(daysub, hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //9th row
            cell = new PdfPCell(new Phrase("Emergency Contact:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getMobile(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //10th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(20);
            table.addCell(cell);

            //10th row
            cell = new PdfPCell(new Phrase("Signature of " + eol + " Issuing Authority", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Signature of " + eol + " Card Holder", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //11th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(100);
            table.addCell(cell);

            //11th row
            cell = new PdfPCell(new Phrase("Date of Birth:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getFormattedDob(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("GPF / PRAN:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(" + userBean.getAcctType() + ")" + " " + userBean.getGpfno(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Tel No.(0):", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Tel No.(R):", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Mobile:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getMobile(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //11th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("INSTRUCTIONS", boldTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1. Please surrender this card on expire/ retirement/ resignation/ transfer/ suspension/ dismissal/ removal from service. its loss should be reported immediately to the police and Director Secretariat Security ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2.Loss/mutilation/late renewal/ unauthorised retention etc.will entail penal consequences as per rules.", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3.This card is not transferable", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void getICardPDFforEsign(Document document, Users userBean, String filePath) {

        Connection con = null;
        try {

            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(70);

            PdfPCell cell = null;

            String url = filePath + "odgovt.gif";
            File f = null;
            f = new File(url);
            Image img1 = null;
            if (f.exists()) {
                img1 = Image.getInstance(url);
            }
            if (img1 != null) {
                img1.scalePercent(2f);
                cell = new PdfPCell(img1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                // cell.setRowspan(2);
                table.addCell(cell);

            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            //header row
            cell = new PdfPCell(new Phrase("Centre for Modernizing Government Initiative", boldTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("HRMS Odisha"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //Put a gap
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Identity Card", boldTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //1st row        
            cell = new PdfPCell(new Phrase("Id No: " + userBean.getEmpId(), hdrTextFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //  cell.setVerticalAlignment(Element.ALIGN_CENTER);
            //cell.setColspan(2);
            table.addCell(cell);

            String eol = System.getProperty("line.separator");
            //2nd row
            cell = new PdfPCell(new Phrase("Government Of Odisha Home Department", bigTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //Blank row
            cell = new PdfPCell(new Phrase(" ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //  cell.setColspan(2);
            table.addCell(cell);

            Image img2 = null;
            String url2 = filePath + userBean.getEmpId() + ".jpg";
            f = new File(url2);

            if (f.exists()) {
                img2 = Image.getInstance(url2);
            } else {
                url2 = filePath + "NoEmployee.png";
                img2 = Image.getInstance(url2);
            }
            if (img2 != null) {
                img2.scalePercent(10f);
                cell = new PdfPCell(img2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                // cell.setColspan(2);
                table.addCell(cell);

            } else {

                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                //  cell.setColspan(2);
                table.addCell(cell);
            }

            //4th row
            cell = new PdfPCell(new Phrase(userBean.getFullName(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setColspan(2);
            table.addCell(cell);

            //5th row
            cell = new PdfPCell(new Phrase(userBean.getPostname(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            //  cell.setColspan(2);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidthPercentage(60);

            //6th row
            cell = new PdfPCell(new Phrase("Department:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getDeptName(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            Calendar now = Calendar.getInstance();
            now.setTime(dateFormat.parse(dateFormat.format(date)));
            now.add(Calendar.YEAR, 4);
            String yearAdd = dateFormat.format(now.getTime());

            now.setTime(dateFormat.parse(yearAdd));
            now.add(Calendar.DATE, -1);
            String daysub = dateFormat.format(now.getTime());

            //7th row
            cell = new PdfPCell(new Phrase("Date of Issue:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(dateFormat.format(date), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //8th row
            cell = new PdfPCell(new Phrase("Valid Upto:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(daysub, hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //9th row
            cell = new PdfPCell(new Phrase("Emergency Contact:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getMobile(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //10th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(20);
            table.addCell(cell);

            //10th row
            cell = new PdfPCell(new Phrase("Signature of " + eol + " Issuing Authority", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Signature of " + eol + " Card Holder", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //11th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(20);
            table.addCell(cell);

            //11th row
            cell = new PdfPCell(new Phrase("Date of Birth:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getFormattedDob(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("GPF / PRAN:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(" + userBean.getAcctType() + ")" + " " + userBean.getGpfno(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Tel No.(0):", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Tel No.(R):", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("Mobile:", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(userBean.getMobile(), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //11th row
            cell = new PdfPCell(new Phrase("", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setFixedHeight(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("INSTRUCTIONS", boldTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1. Please surrender this card on expire/ retirement/ resignation/ transfer/ suspension/ dismissal/ removal from service. its loss should be reported immediately to the police and Director Secretariat Security ", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2.Loss/mutilation/late renewal/ unauthorised retention etc.will entail penal consequences as per rules.", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3.This card is not transferable", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getOtherSpn(String othSpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();

            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT oth_spc, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
                pst.setInt(1, Integer.parseInt(othSpc));
                rs = pst.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    @Override
    public String unlockEmpprofileGroup(String checkedEmpProfile, String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String tabName = null;
        //ArrayList<String> module=new ArrayList<String>();
        int retValue = 0;
        //int retValue1 = 0;
        String modulename = "";
        String modname = "";
        try {
            con = this.dataSource.getConnection();
            List<String> module = Arrays.asList(checkedEmpProfile.split(","));
            int sizeofmodule = module.size();
            if (sizeofmodule > 0) {
                for (int i = 0; i < sizeofmodule; i++) {
                    tabName = module.get(i);
                    String[] chkValArr = tabName.split("~");
                    String sql = "UPDATE " + chkValArr[0] + " SET is_locked='N' WHERE EMP_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    retValue = pst.executeUpdate();
                    if (retValue > 0) {
                        modulename = modulename + chkValArr[1] + ",";
                        modname = modulename.substring(0, modulename.length() - 1);
                    }
                }
                if (retValue > 0) {
                    String query1 = "UPDATE EMP_MAST SET if_profile_completed='N',if_profile_VERIFIED='N' WHERE EMP_ID=?";
                    pst = con.prepareStatement(query1);
                    pst.setString(1, empid);
                    pst.executeUpdate();

                    int logid = 0;

                    String sql = "SELECT * FROM equarter.hrms_profile_complete_log WHERE emp_id=? ORDER BY complete_log_id DESC LIMIT 1";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        logid = rs.getInt("complete_log_id");
                    }

                    DataBaseFunctions.closeSqlObjects(pst);

                    if (logid > 0) {
                        pst = con.prepareStatement("UPDATE equarter.hrms_profile_complete_log SET completed_date=null where complete_log_id=?");
                        pst.setInt(1, logid);
                        pst.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return modname;
    }

    @Override
    public String getCheckedEmpIdentityProfile(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Y";

        try {
            String identity = null;

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select is_locked from emp_id_doc where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                identity = rs.getString("is_locked");
                if (identity.equals("N")) {
                    msg = "Y";
                } else {
                    msg = "D";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

    @Override
    public String getCheckedEmpLanguageProfile(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Y";

        try {

            String language = null;

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select is_locked from emp_language where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                language = rs.getString("is_locked");
                if (language.equals("N")) {
                    msg = "Y";
                } else {
                    msg = "D";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

    @Override
    public String getCheckedEmpQualificationProfile(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Y";

        try {

            String qualification = null;

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select is_locked from emp_qualification where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qualification = rs.getString("is_locked");
                if (qualification.equals("N")) {
                    msg = "Y";
                } else {
                    msg = "D";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

    @Override
    public String getCheckedEmpFamilyProfile(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Y";

        try {

            String family = null;

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select is_locked from emp_relation where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                family = rs.getString("is_locked");
                if (family.equals("N")) {
                    msg = "Y";
                } else {
                    msg = "D";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

    @Override
    public String getCheckedEmpAddressProfile(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Y";

        try {

            String address = null;

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select is_locked from emp_address where emp_id=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                address = rs.getString("is_locked");
                if (address.equals("N")) {
                    msg = "Y";
                } else {
                    msg = "D";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;

    }

}

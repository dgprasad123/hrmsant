/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.propertystatement;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.fiscalyear.FiscalYear;
import hrms.model.propertystatement.PropertyDetail;
import hrms.model.propertystatement.PropertyStatement;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas Jena
 */
public class PropertyStatementDAOImpl implements PropertyStatementDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getPropertyList(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        ArrayList propertyList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT YEARLY_PROPERTY_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE FROM PROPERTY_STATEMENT_LIST WHERE EMP_ID=? ORDER BY yearly_property_id ");
            pstmt.setString(1, empid);
            resultset = pstmt.executeQuery();
            int slno = 0;
            while (resultset.next()) {
                slno++;
                PropertyStatement propstmt = new PropertyStatement();
                propstmt.setSlNo(slno);
                propstmt.setYearlyPropId(resultset.getBigDecimal("YEARLY_PROPERTY_ID"));
                propstmt.setFiscalyear(resultset.getString("FINANCIAL_YEAR"));
                propstmt.setStatusid(resultset.getInt("STATUS_ID"));
                propstmt.setFromdate(resultset.getDate("FROM_DATE"));
                propstmt.setTodate(resultset.getDate("TO_DATE"));
                propstmt.setSubmissiontype(resultset.getString("SUBMISSION_TYPE"));
                propertyList.add(propstmt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyList;
    }

    @Override
    public boolean isDuplicatePropertyPeriod(PropertyStatement propertyStatement) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean isDuplicate = true;
        boolean duplPeriod = false;
        String sql = "";
        String dbf1 = "";
        String dbt1 = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            if (propertyStatement.getYearlyPropId() != null) {
                sql = "SELECT FROM_DATE,TO_DATE FROM  PROPERTY_STATEMENT_LIST WHERE EMP_ID=? AND FINANCIAL_YEAR=? AND YEARLY_PROPERTY_ID <> ";
                pst = con.prepareStatement(sql);
                pst.setString(1, propertyStatement.getEmpid());
                pst.setString(2, propertyStatement.getFiscalyear());
            } else {
                sql = "SELECT FROM_DATE,TO_DATE FROM  PROPERTY_STATEMENT_LIST WHERE EMP_ID=? AND FINANCIAL_YEAR=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, propertyStatement.getEmpid());
                pst.setString(2, propertyStatement.getFiscalyear());
            }
            rs = pst.executeQuery();
            int noofrows = 0;
            while (rs.next()) {
                noofrows++;
                dbf1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE"));
                dbt1 = CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE"));

                Date frs1 = formatter.parse(dbf1);
                Date trs1 = formatter.parse(dbt1);

                if (propertyStatement.getFromdate().compareTo(frs1) > 0 && (propertyStatement.getFromdate().compareTo(trs1) > 0 && propertyStatement.getTodate().compareTo(trs1) > 0)) {
                    duplPeriod = true;
                } else if ((propertyStatement.getFromdate().compareTo(frs1) < 0) && (propertyStatement.getTodate().compareTo(frs1) < 0 && propertyStatement.getTodate().compareTo(trs1) < 0)) {
                    duplPeriod = true;
                } else if (propertyStatement.getFromdate().compareTo(frs1) == 0 && propertyStatement.getTodate().compareTo(trs1) == 0) {
                    duplPeriod = true;
                } else {
                    duplPeriod = false;
                }

                if (duplPeriod == false) {
                    isDuplicate = false;
                }
            }
            if (noofrows == 0) {
                isDuplicate = false;
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDuplicate;
    }

    @Override
    public void savePropertyStmt(PropertyStatement psf) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();
            String IP = CommonFunctions.getISPIPAddress();
            int yearlyPropId = CommonFunctions.getMaxCodeInteger("PROPERTY_STATEMENT_LIST", "YEARLY_PROPERTY_ID", con);
            pst = con.prepareStatement("INSERT INTO PROPERTY_STATEMENT_LIST(yearly_property_id,EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,BASIC_SAL,PAY_SCALE,CADRE_CODE,SPC,POST_GROUP,SUBMISSION_TYPE,grade_pay,created_by_ip)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, yearlyPropId);
            pst.setString(2, psf.getEmpid());
            pst.setString(3, psf.getFiscalyear());
            pst.setInt(4, 0);
            pst.setDate(5, new java.sql.Date(psf.getFromdate().getTime()));
            pst.setDate(6, new java.sql.Date(psf.getTodate().getTime()));
            pst.setInt(7, psf.getCurbasicsalary());
            pst.setString(8, psf.getPayscale());
            pst.setString(9, psf.getCurcadrecode());
            pst.setString(10, psf.getCurspc());
            pst.setString(11, psf.getPostgroup());
            pst.setString(12, psf.getSubmissiontype());
            pst.setInt(13, psf.getGradepay());
            pst.setString(14, IP);
            pst.executeUpdate();

            if (psf.getPrevyearProperty() != null && !psf.getPrevyearProperty().equals("")) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sql = "select * from property_statement_list where emp_id=? and financial_year=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, psf.getEmpid());
                pst.setString(2, psf.getPrevyearProperty());
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst1 = con.prepareStatement("select * from property_details where yearly_property_id=?");
                    pst1.setInt(1, rs.getInt("yearly_property_id"));
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        int mcode = CommonFunctions.getMaxCodeInteger("PROPERTY_DETAILS", "PROPERTY_DETAILS_ID", con);
                        pst = con.prepareStatement("INSERT INTO PROPERTY_DETAILS(PROPERTY_DETAILS_ID,PROPERTY_LOCATION,PROPERTY_AREA,PROPERTY_NATURE,INTEREST1,VALUE,OWNER_TYPE_ID,DATE_OF_ACQUISITION,REMARKS,PROPERTY_ID,YEARLY_PROPERTY_ID,MANNER,DESC_OF_OTH_ITEM,AREAUNIT,OTHER_PROPERTY_OWNER) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        pst.setInt(1, mcode);
                        pst.setString(2, rs1.getString("property_location"));
                        if (rs1.getString("property_area") != null && !rs1.getString("property_area").equals("")) {
                            pst.setBigDecimal(3, new BigDecimal(rs1.getString("property_area")));
                        } else {
                            pst.setBigDecimal(3, null);
                        }
                        pst.setString(4, rs1.getString("property_nature"));
                        pst.setString(5, rs1.getString("interest1"));
                        if (rs1.getString("value") != null && !rs1.getString("value").equals("")) {
                            pst.setBigDecimal(6, new BigDecimal(rs1.getString("value")));
                        } else {
                            pst.setBigDecimal(6, null);
                        }
                        pst.setInt(7, rs1.getInt("owner_type_id"));
                        if (rs1.getDate("date_of_acquisition") != null && !rs1.getDate("date_of_acquisition").equals("")) {
                            pst.setDate(8, new java.sql.Date(rs1.getDate("date_of_acquisition").getTime()));
                        } else {
                            pst.setDate(8, null);
                        }
                        pst.setString(9, rs1.getString("remarks"));
                        pst.setInt(10, rs1.getInt("property_id"));
                        pst.setBigDecimal(11, new BigDecimal(yearlyPropId));
                        pst.setString(12, rs1.getString("manner"));
                        pst.setString(13, rs1.getString("desc_of_oth_item"));
                        pst.setString(14, rs1.getString("areaunit"));
                        pst.setString(15, rs1.getString("other_property_owner"));
                        pst.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updatePropertyStmt(PropertyStatement psf) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();
            // int yearlyPropId = CommonFunctions.getMaxCodeInteger("PROPERTY_STATEMENT_LIST", "YEARLY_PROPERTY_ID", con);
            pst = con.prepareStatement("UPDATE PROPERTY_STATEMENT_LIST SET EMP_ID =?,FINANCIAL_YEAR =?,STATUS_ID =?,BASIC_SAL=?,PAY_SCALE=?,CADRE_CODE=?,SPC=?,POST_GROUP=?,SUBMISSION_TYPE=?,grade_pay=? WHERE YEARLY_PROPERTY_ID=?");
            pst.setString(1, psf.getEmpid());
            pst.setString(2, psf.getFiscalyear());
            pst.setInt(3, 0);
            pst.setInt(4, psf.getCurbasicsalary());
            pst.setString(5, psf.getPayscale());
            pst.setString(6, psf.getCurcadrecode());
            pst.setString(7, psf.getCurspc());
            pst.setString(8, psf.getPostgroup());
            pst.setString(9, psf.getSubmissiontype());
            pst.setInt(10, psf.getGradepay());
            pst.setBigDecimal(11, psf.getYearlyPropId());
            pst.executeUpdate();

            if (psf.getPrevyearProperty() != null && !psf.getPrevyearProperty().equals("")) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sql = "select * from property_statement_list where emp_id=? and financial_year=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, psf.getEmpid());
                pst.setString(2, psf.getPrevyearProperty());
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst1 = con.prepareStatement("select * from property_details where yearly_property_id=?");
                    pst1.setInt(1, rs.getInt("yearly_property_id"));
                    rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        int mcode = CommonFunctions.getMaxCodeInteger("PROPERTY_DETAILS", "PROPERTY_DETAILS_ID", con);
                        pst = con.prepareStatement("INSERT INTO PROPERTY_DETAILS(PROPERTY_DETAILS_ID,PROPERTY_LOCATION,PROPERTY_AREA,PROPERTY_NATURE,INTEREST1,VALUE,OWNER_TYPE_ID,DATE_OF_ACQUISITION,REMARKS,PROPERTY_ID,YEARLY_PROPERTY_ID,MANNER,DESC_OF_OTH_ITEM,AREAUNIT,OTHER_PROPERTY_OWNER) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        pst.setInt(1, mcode);
                        pst.setString(2, rs1.getString("property_location"));
                        if (rs1.getString("property_area") != null && !rs1.getString("property_area").equals("")) {
                            pst.setBigDecimal(3, new BigDecimal(rs1.getString("property_area")));
                        } else {
                            pst.setBigDecimal(3, null);
                        }
                        pst.setString(4, rs1.getString("property_nature"));
                        pst.setString(5, rs1.getString("interest1"));
                        if (rs1.getString("value") != null && !rs1.getString("value").equals("")) {
                            pst.setBigDecimal(6, new BigDecimal(rs1.getString("value")));
                        } else {
                            pst.setBigDecimal(6, null);
                        }
                        pst.setInt(7, rs1.getInt("owner_type_id"));
                        if (rs1.getDate("date_of_acquisition") != null && !rs1.getDate("date_of_acquisition").equals("")) {
                            pst.setDate(8, new java.sql.Date(rs1.getDate("date_of_acquisition").getTime()));
                        } else {
                            pst.setDate(8, null);
                        }
                        pst.setString(9, rs1.getString("remarks"));
                        pst.setInt(10, rs1.getInt("property_id"));
                        pst.setBigDecimal(11, new BigDecimal(rs1.getString("yearlyPropId")));
                        pst.setString(12, rs1.getString("manner"));
                        pst.setString(13, rs1.getString("desc_of_oth_item"));
                        pst.setString(14, rs1.getString("areaunit"));
                        pst.setString(15, rs1.getString("other_property_owner"));
                        pst.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean deletePropertyStmt(BigDecimal yearlyPropId, String empId) {
        boolean isDeleted = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        int statusId = 1;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT STATUS_ID FROM PROPERTY_STATEMENT_LIST WHERE YEARLY_PROPERTY_ID=? AND EMP_ID=?");
            pstmt.setBigDecimal(1, yearlyPropId);
            pstmt.setString(2, empId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                statusId = resultset.getInt("STATUS_ID");
            }
            if (statusId == 0) {
                pstmt = con.prepareStatement("DELETE FROM PROPERTY_STATEMENT_LIST WHERE YEARLY_PROPERTY_ID=? AND EMP_ID=?");
                pstmt.setBigDecimal(1, yearlyPropId);
                pstmt.setString(2, empId);
                pstmt.executeUpdate();
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDeleted;
    }

    @Override
    public PropertyStatement getPropertyStmt(BigDecimal yearlyPropId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        String str = "";
        String str4 = "";
        PropertyStatement propstmt = new PropertyStatement();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT PROPERTY_STATEMENT_LIST.EMP_ID,YEARLY_PROPERTY_ID,FINANCIAL_YEAR,STATUS_ID,SUBMISSION_TYPE,FROM_DATE,"
                    + "TO_DATE,INITIALS,F_NAME,M_NAME,L_NAME,SPN,PROPERTY_STATEMENT_LIST.PAY_SCALE,PROPERTY_STATEMENT_LIST.BASIC_SAL,PROPERTY_STATEMENT_LIST.grade_pay,"
                    + "SUBMITTED_ON,IS_CLOSED_PROPERTY,EMP_MAST.gpf_no,is_immovable_blank,is_movable_blank FROM PROPERTY_STATEMENT_LIST "
                    + "INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC = G_SPC.SPC "
                    + "INNER JOIN FINANCIAL_YEAR ON PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR = FINANCIAL_YEAR.CY "
                    + "WHERE YEARLY_PROPERTY_ID=?");
            pstmt.setBigDecimal(1, yearlyPropId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                propstmt.setEmpid(resultset.getString("EMP_ID"));
                propstmt.setFullname(StringUtils.defaultString(resultset.getString("INITIALS")) + " " + resultset.getString("F_NAME") + " " + StringUtils.defaultString(resultset.getString("M_NAME")) + " " + resultset.getString("L_NAME"));
                propstmt.setSpn(resultset.getString("SPN"));
                propstmt.setPayscale(resultset.getString("PAY_SCALE"));
                propstmt.setCurbasicsalary(resultset.getInt("BASIC_SAL"));
                propstmt.setGradepay(resultset.getInt("grade_pay"));
                propstmt.setYearlyPropId(resultset.getBigDecimal("YEARLY_PROPERTY_ID"));
                propstmt.setFiscalyear(resultset.getString("FINANCIAL_YEAR"));
                propstmt.setStatusid(resultset.getInt("STATUS_ID"));
                propstmt.setSubmittedOn(resultset.getDate("SUBMITTED_ON"));
                propstmt.setSubmissiontype(resultset.getString("SUBMISSION_TYPE"));
                propstmt.setFromdate(resultset.getDate("FROM_DATE"));
                propstmt.setTodate(resultset.getDate("TO_DATE"));
                propstmt.setIsclosed(resultset.getString("IS_CLOSED_PROPERTY"));
                propstmt.setGpfNo(resultset.getString("gpf_no"));
                propstmt.setIsImmovableBlank(resultset.getString("is_immovable_blank"));
                propstmt.setIsMovableBlank(resultset.getString("is_movable_blank"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propstmt;
    }

    @Override
    public ArrayList getMovablePropertyDetailList(BigDecimal yearlyPropId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        ArrayList propertyDetailList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT property_details_id,PROPERTY_NAME,property_details.PROPERTY_ID,PROPERTY_NATURE,PROPERTY_LOCATION,VALUE,LOAN,DATE_OF_ACQUISITION,MANNER,REMARKS,PROPERTY_DETAILS.OWNER_TYPE_ID,OWNER_TYPE_NAME,OTHER_PROPERTY_OWNER,DESC_OF_OTH_ITEM,AREAUNIT from property_details "
                    + "INNER JOIN PROPERTY_MASTER ON property_details.PROPERTY_ID = PROPERTY_MASTER.PROPERTY_ID "
                    + "INNER JOIN PROPERTY_TYPE_MASTER ON PROPERTY_TYPE_MASTER.PROPERTY_TYPE_ID=PROPERTY_MASTER.PROPERTY_TYPE_ID "
                    + "INNER JOIN PROPERTY_OWNER ON PROPERTY_DETAILS.OWNER_TYPE_ID = PROPERTY_OWNER.OWNER_TYPE_ID "
                    + "WHERE property_details.yearly_property_id=? AND PROPERTY_TYPE_MASTER.PROPERTY_TYPE_ID=1 order by property_details_id");
            pstmt.setBigDecimal(1, yearlyPropId);
            resultset = pstmt.executeQuery();
            int slno = 0;
            while (resultset.next()) {
                slno++;
                PropertyDetail propertyDetail = new PropertyDetail();
                propertyDetail.setSlno(slno);
                propertyDetail.setPropertyDtlsId(resultset.getBigDecimal("property_details_id"));
                propertyDetail.setPropertyId(resultset.getInt("PROPERTY_ID"));
                propertyDetail.setPropertyName(resultset.getString("PROPERTY_NAME"));
                propertyDetail.setPropertyNature(resultset.getString("PROPERTY_NATURE"));
                propertyDetail.setPropertyLocation(resultset.getString("PROPERTY_LOCATION"));
                propertyDetail.setPropertyValue(resultset.getBigDecimal("VALUE"));
                propertyDetail.setLoan(resultset.getBigDecimal("LOAN"));
                propertyDetail.setDateOfAcq(resultset.getDate("DATE_OF_ACQUISITION"));
                propertyDetail.setManner(resultset.getString("MANNER"));
                propertyDetail.setRemark(resultset.getString("REMARKS"));
                propertyDetail.setPropertyOwner(resultset.getInt("OWNER_TYPE_ID"));
                propertyDetail.setPropertyOwnerDtl(resultset.getString("OWNER_TYPE_NAME"));
                propertyDetail.setOtherPropertyOwner(resultset.getString("OTHER_PROPERTY_OWNER"));
                propertyDetail.setDescofothitem(resultset.getString("DESC_OF_OTH_ITEM"));
                propertyDetail.setAreaunit(resultset.getString("AREAUNIT"));
                propertyDetailList.add(propertyDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyDetailList;
    }

    @Override
    public ArrayList getImmovablePropertyDetailList(BigDecimal yearlyPropId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        ArrayList propertyDetailList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT property_details_id,PROPERTY_NAME,property_details.PROPERTY_ID,PROPERTY_NATURE,PROPERTY_LOCATION,VALUE,PROPERTY_AREA,INTEREST1,DATE_OF_ACQUISITION,REMARKS,MANNER,PROPERTY_DETAILS.OWNER_TYPE_ID,OWNER_TYPE_NAME,OTHER_PROPERTY_OWNER,DESC_OF_OTH_ITEM, AREAUNIT from property_details "
                    + "INNER JOIN PROPERTY_MASTER ON property_details.PROPERTY_ID = PROPERTY_MASTER.PROPERTY_ID "
                    + "INNER JOIN PROPERTY_TYPE_MASTER ON PROPERTY_TYPE_MASTER.PROPERTY_TYPE_ID=PROPERTY_MASTER.PROPERTY_TYPE_ID "
                    + "INNER JOIN PROPERTY_OWNER ON PROPERTY_DETAILS.OWNER_TYPE_ID = PROPERTY_OWNER.OWNER_TYPE_ID "
                    + "WHERE property_details.yearly_property_id=? AND PROPERTY_TYPE_MASTER.PROPERTY_TYPE_ID=2 order by property_details_id");
            pstmt.setBigDecimal(1, yearlyPropId);
            resultset = pstmt.executeQuery();
            int slno = 0;
            while (resultset.next()) {
                slno++;
                PropertyDetail propertyDetail = new PropertyDetail();
                propertyDetail.setSlno(slno);
                propertyDetail.setPropertyDtlsId(resultset.getBigDecimal("property_details_id"));
                propertyDetail.setPropertyId(resultset.getInt("PROPERTY_ID"));
                propertyDetail.setPropertyName(resultset.getString("PROPERTY_NAME"));
                propertyDetail.setPropertyNature(resultset.getString("PROPERTY_NATURE"));
                propertyDetail.setPropertyLocation(resultset.getString("PROPERTY_LOCATION"));
                propertyDetail.setPropertyValue(resultset.getBigDecimal("VALUE"));
                propertyDetail.setPropertyArea(resultset.getBigDecimal("PROPERTY_AREA"));
                propertyDetail.setInterest(resultset.getString("INTEREST1"));
                propertyDetail.setDateOfAcq(resultset.getDate("DATE_OF_ACQUISITION"));
                propertyDetail.setRemark(resultset.getString("REMARKS"));
                propertyDetail.setManner(resultset.getString("MANNER"));
                propertyDetail.setPropertyOwner(resultset.getInt("OWNER_TYPE_ID"));
                propertyDetail.setPropertyOwnerDtl(resultset.getString("OWNER_TYPE_NAME"));
                propertyDetail.setOtherPropertyOwner(resultset.getString("OTHER_PROPERTY_OWNER"));
                propertyDetail.setDescofothitem(resultset.getString("DESC_OF_OTH_ITEM"));
                propertyDetail.setAreaunit(resultset.getString("AREAUNIT"));
                propertyDetailList.add(propertyDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyDetailList;
    }

    @Override
    public int saveImmovableProperty(PropertyDetail psf) {
        Connection con = null;
        PreparedStatement pst = null;
        int mcode = 0;
        try {
            con = this.dataSource.getConnection();
            if (psf.getPropertyDtlsId() != null && psf.getPropertyDtlsId() != BigDecimal.ZERO) {
                pst = con.prepareStatement("UPDATE PROPERTY_DETAILS SET PROPERTY_LOCATION=?,PROPERTY_AREA=?,PROPERTY_NATURE=?,INTEREST1=?,VALUE=?,OWNER_TYPE_ID=?,DATE_OF_ACQUISITION=?,REMARKS=?,PROPERTY_ID=?,MANNER=?,DESC_OF_OTH_ITEM=?,AREAUNIT=?,OTHER_PROPERTY_OWNER=? WHERE PROPERTY_DETAILS_ID=?");
                pst.setString(1, psf.getPropertyLocation());
                pst.setBigDecimal(2, psf.getPropertyArea());
                pst.setString(3, psf.getPropertyNature());
                pst.setString(4, psf.getInterest());
                pst.setBigDecimal(5, psf.getPropertyValue());
                pst.setInt(6, psf.getPropertyOwner());
                if (psf.getDateOfAcq() == null) {
                    pst.setDate(7, null);
                } else {
                    pst.setDate(7, new java.sql.Date(psf.getDateOfAcq().getTime()));
                }
                pst.setString(8, psf.getRemark());
                pst.setInt(9, psf.getPropertyTypeId());
                pst.setString(10, psf.getManner());
                pst.setString(11, psf.getDescofothitem());
                pst.setString(12, psf.getAreaunit());
                pst.setString(13, psf.getOtherPropertyOwner());
                pst.setBigDecimal(14, psf.getPropertyDtlsId());
                pst.executeUpdate();
            } else {
                mcode = CommonFunctions.getMaxCodeInteger("PROPERTY_DETAILS", "PROPERTY_DETAILS_ID", con);
                pst = con.prepareStatement("INSERT INTO PROPERTY_DETAILS(PROPERTY_DETAILS_ID,PROPERTY_LOCATION,PROPERTY_AREA,PROPERTY_NATURE,INTEREST1,VALUE,OWNER_TYPE_ID,DATE_OF_ACQUISITION,REMARKS,PROPERTY_ID,YEARLY_PROPERTY_ID,MANNER,DESC_OF_OTH_ITEM,AREAUNIT,OTHER_PROPERTY_OWNER) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, mcode);
                pst.setString(2, psf.getPropertyLocation());
                pst.setBigDecimal(3, psf.getPropertyArea());
                pst.setString(4, psf.getPropertyNature());
                pst.setString(5, psf.getInterest());
                pst.setBigDecimal(6, psf.getPropertyValue());
                pst.setInt(7, psf.getPropertyOwner() + 0);
                if (psf.getDateOfAcq() == null) {
                    pst.setDate(8, null);
                } else {
                    pst.setDate(8, new java.sql.Date(psf.getDateOfAcq().getTime()));
                }
                pst.setString(9, psf.getRemark());
                pst.setInt(10, psf.getPropertyTypeId());
                pst.setBigDecimal(11, psf.getYearlyPropId());
                pst.setString(12, psf.getManner());
                pst.setString(13, psf.getDescofothitem());
                pst.setString(14, psf.getAreaunit());
                pst.setString(15, psf.getOtherPropertyOwner());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mcode;
    }

    @Override
    public int saveMovableProperty(PropertyDetail psf) {
        Connection con = null;
        PreparedStatement pst = null;
        int mcode = 0;
        try {
            con = this.dataSource.getConnection();
            if (psf.getPropertyDtlsId() != null && psf.getPropertyDtlsId() != BigDecimal.ZERO) {
                pst = con.prepareStatement("UPDATE PROPERTY_DETAILS SET VALUE=?,OWNER_TYPE_ID=?,DATE_OF_ACQUISITION=?,REMARKS=?,PROPERTY_ID=?,LOAN=?,MANNER=?,DESC_OF_OTH_ITEM=?,OTHER_PROPERTY_OWNER=? WHERE PROPERTY_DETAILS_ID=?");
                pst.setBigDecimal(1, psf.getPropertyValue());
                pst.setInt(2, psf.getPropertyOwner());
                if (psf.getDateOfAcq() == null) {
                    pst.setDate(3, null);
                } else {
                    pst.setDate(3, new java.sql.Date(psf.getDateOfAcq().getTime()));
                }
                pst.setString(4, psf.getRemark());
                pst.setInt(5, psf.getPropertyTypeId());
                pst.setBigDecimal(6, psf.getLoan());
                pst.setString(7, psf.getManner());
                pst.setString(8, psf.getDescofothitem());
                pst.setString(9, psf.getOtherPropertyOwner());
                pst.setBigDecimal(10, psf.getPropertyDtlsId());
                pst.executeUpdate();
            } else {
                mcode = CommonFunctions.getMaxCodeInteger("PROPERTY_DETAILS", "PROPERTY_DETAILS_ID", con);
                pst = con.prepareStatement("INSERT INTO PROPERTY_DETAILS(PROPERTY_DETAILS_ID,VALUE,OWNER_TYPE_ID,DATE_OF_ACQUISITION,REMARKS,PROPERTY_ID,YEARLY_PROPERTY_ID,LOAN,MANNER,DESC_OF_OTH_ITEM,OTHER_PROPERTY_OWNER) values(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, mcode);
                pst.setBigDecimal(2, psf.getPropertyValue());
                pst.setInt(3, psf.getPropertyOwner());
                if (psf.getDateOfAcq() == null) {
                    pst.setDate(4, null);
                } else {
                    pst.setDate(4, new java.sql.Date(psf.getDateOfAcq().getTime()));
                }
                pst.setString(5, psf.getRemark());
                pst.setInt(6, psf.getPropertyTypeId());
                pst.setBigDecimal(7, psf.getYearlyPropId());
                pst.setBigDecimal(8, psf.getLoan());
                pst.setString(9, psf.getManner());
                pst.setString(10, psf.getDescofothitem());
                pst.setString(11, psf.getOtherPropertyOwner());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mcode;
    }

    @Override
    public PropertyDetail getImmovableProperty(BigDecimal propertyDtlsId) {
        PropertyDetail propertyDetail = new PropertyDetail();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM property_details WHERE PROPERTY_DETAILS_ID=?");
            pstmt.setBigDecimal(1, propertyDtlsId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                propertyDetail.setPropertyDtlsId(resultset.getBigDecimal("PROPERTY_DETAILS_ID"));
                propertyDetail.setPropertyLocation(resultset.getString("PROPERTY_LOCATION"));
                propertyDetail.setPropertyArea(resultset.getBigDecimal("PROPERTY_AREA"));
                propertyDetail.setPropertyNature(resultset.getString("PROPERTY_NATURE"));
                propertyDetail.setInterest(resultset.getString("INTEREST1"));
                propertyDetail.setPropertyValue(resultset.getBigDecimal("VALUE"));
                propertyDetail.setPropertyOwner(resultset.getInt("OWNER_TYPE_ID"));
                propertyDetail.setDateOfAcq(resultset.getDate("DATE_OF_ACQUISITION"));
                propertyDetail.setRemark(resultset.getString("REMARKS"));
                propertyDetail.setPropertyTypeId(resultset.getInt("PROPERTY_ID"));
                propertyDetail.setManner(resultset.getString("MANNER"));
                propertyDetail.setDescofothitem(resultset.getString("DESC_OF_OTH_ITEM"));
                propertyDetail.setAreaunit(resultset.getString("AREAUNIT"));
                propertyDetail.setOtherPropertyOwner(resultset.getString("OTHER_PROPERTY_OWNER"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyDetail;
    }

    @Override
    public PropertyDetail getMovableProperty(BigDecimal propertyDtlsId) {
        PropertyDetail propertyDetail = new PropertyDetail();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM property_details WHERE PROPERTY_DETAILS_ID=?");
            pstmt.setBigDecimal(1, propertyDtlsId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                propertyDetail.setPropertyDtlsId(resultset.getBigDecimal("PROPERTY_DETAILS_ID"));
                propertyDetail.setPropertyTypeId(resultset.getInt("PROPERTY_ID"));
                propertyDetail.setPropertyValue(resultset.getBigDecimal("VALUE"));
                propertyDetail.setLoan(resultset.getBigDecimal("LOAN"));
                propertyDetail.setPropertyOwner(resultset.getInt("OWNER_TYPE_ID"));
                propertyDetail.setDateOfAcq(resultset.getDate("DATE_OF_ACQUISITION"));
                propertyDetail.setRemark(resultset.getString("REMARKS"));
                propertyDetail.setManner(resultset.getString("MANNER"));
                propertyDetail.setDescofothitem(resultset.getString("DESC_OF_OTH_ITEM"));
                propertyDetail.setOtherPropertyOwner(resultset.getString("OTHER_PROPERTY_OWNER"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyDetail;
    }

    @Override
    public boolean deleteImmovableProperty(BigDecimal propertyDtlsId, String empId) {
        boolean isDeleted = true;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        String tempEmpId = "";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_ID FROM PROPERTY_DETAILS  INNER JOIN PROPERTY_STATEMENT_LIST on PROPERTY_DETAILS.YEARLY_PROPERTY_ID = PROPERTY_STATEMENT_LIST.YEARLY_PROPERTY_ID WHERE PROPERTY_DETAILS_ID=?");
            pstmt.setBigDecimal(1, propertyDtlsId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                tempEmpId = resultset.getString("EMP_ID");
            }
            if (empId.equalsIgnoreCase(tempEmpId)) {
                pstmt = con.prepareStatement("DELETE FROM property_details WHERE PROPERTY_DETAILS_ID=?");
                pstmt.setBigDecimal(1, propertyDtlsId);
                isDeleted = pstmt.execute();
            }
        } catch (SQLException e) {
            isDeleted = false;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDeleted;
    }

    @Override
    public boolean deleteMovableProperty(BigDecimal propertyDtlsId, String empId) {
        boolean isDeleted = true;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        String tempEmpId = "";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_ID FROM PROPERTY_DETAILS  INNER JOIN PROPERTY_STATEMENT_LIST on PROPERTY_DETAILS.YEARLY_PROPERTY_ID = PROPERTY_STATEMENT_LIST.YEARLY_PROPERTY_ID WHERE PROPERTY_DETAILS_ID=?");
            pstmt.setBigDecimal(1, propertyDtlsId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                tempEmpId = resultset.getString("EMP_ID");
            }
            if (empId.equalsIgnoreCase(tempEmpId)) {
                pstmt = con.prepareStatement("DELETE FROM property_details WHERE PROPERTY_DETAILS_ID=?");
                pstmt.setBigDecimal(1, propertyDtlsId);
                isDeleted = pstmt.execute();
            }
        } catch (SQLException e) {
            isDeleted = false;
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDeleted;
    }

    @Override
    public boolean isPropertySubmittedDateClosed(BigDecimal yearlyPropId) {
        boolean isclosed = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String fiscalYear = null;
        Date propertyPeriodclosingDate = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT financial_year FROM PROPERTY_STATEMENT_LIST WHERE YEARLY_PROPERTY_ID=?");
            pstmt.setBigDecimal(1, yearlyPropId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                fiscalYear = rs.getString("financial_year");
            }
            pstmt = con.prepareStatement("SELECT property_period_extended_for FROM financial_year WHERE cy=?");
            pstmt.setString(1, fiscalYear);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                propertyPeriodclosingDate = rs.getDate("property_period_extended_for");
            }

            Date curentDate = new Date();
            if (propertyPeriodclosingDate.before(curentDate)) {
                isclosed = true;

                pstmt = con.prepareStatement("UPDATE financial_year SET is_closed_property ='Y', property_active='N' WHERE cy=?");
                pstmt.setString(1, fiscalYear);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return isclosed;
    }

    @Override
    public boolean submitPropertyStatement(BigDecimal yearlyPropId, String empId, String isImmovable, String isMovable, String qrCodePath, String Ip) {
        boolean isSubmitted = true;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        String tempEmpId = "";
        String str4 = "";
        String str = "";
        try {
            con = this.dataSource.getConnection();
            //String IP = CommonFunctions.getISPIPAddress();

            pstmt = con.prepareStatement("SELECT EMP_ID FROM PROPERTY_STATEMENT_LIST WHERE YEARLY_PROPERTY_ID=?");
            pstmt.setBigDecimal(1, yearlyPropId);
            resultset = pstmt.executeQuery();
            if (resultset.next()) {
                tempEmpId = resultset.getString("EMP_ID");
            }
            if (empId.equalsIgnoreCase(tempEmpId)) {
                pstmt = con.prepareStatement("UPDATE PROPERTY_STATEMENT_LIST SET STATUS_ID=1 , SUBMITTED_ON=?,is_immovable_blank=?,is_movable_blank=?,submitted_by_ip=? WHERE YEARLY_PROPERTY_ID=?");
                pstmt.setDate(1, new java.sql.Date(new Date().getTime()));
                pstmt.setString(2, isImmovable);
                pstmt.setString(3, isMovable);
                pstmt.setString(4, Ip);
                pstmt.setBigDecimal(5, yearlyPropId);
                pstmt.execute();

                generateQRCodeForPreviousYear(yearlyPropId, qrCodePath);
            }

        } catch (SQLException e) {
            isSubmitted = false;
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isSubmitted;
    }

    @Override
    public List getPreviousFiscalYearPropertyStatementData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<FiscalYear> fiscalyearlist = new ArrayList();
        FiscalYear fiscalyr = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select financial_year from property_statement_list where emp_id=? order by financial_year desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                fiscalyr = new FiscalYear();
                fiscalyr.setFy(rs.getString("financial_year"));
                fiscalyearlist.add(fiscalyr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fiscalyearlist;
    }

    @Override
    public List getPropertyStatementStatusData() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List propertyStatementStatusList = new ArrayList();
        SelectOption so = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select financial_year,count(*) cnt from property_statement_list where status_id=1 group by financial_year order by financial_year";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("financial_year") != null && !rs.getString("financial_year").equals("")) {
                    if (rs.getString("financial_year").equals("2016-17") || rs.getString("financial_year").equals("2017-18") || rs.getString("financial_year").equals("2018-19") || rs.getString("financial_year").equals("2019-20") || rs.getString("financial_year").equals("2020") || rs.getString("financial_year").equals("2021") || rs.getString("financial_year").equals("2022") || rs.getString("financial_year").equals("2023")) {
                        so = new SelectOption();
                        so.setValue(rs.getString("financial_year"));
                        so.setLabel(rs.getString("cnt"));
                        propertyStatementStatusList.add(so);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyStatementStatusList;
    }

    @Override
    public void viewPropertyStatementPDF(PropertyStatement propertyStatement, ArrayList movablePropertyDetailList, ArrayList immovablePropertyDetailList, Document document, String qrCodePath) {

        try {

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(9);
            f2.setFamily("Times New Roman");

            Font tableHeader = new Font();
            tableHeader.setSize(9);
            tableHeader.setFamily("Times New Roman");
            tableHeader.setStyle(Font.BOLD);

            PdfPTable table = null;

            /*Basic Profile End*/
            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 6});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            boolean isLandExist = false;
            boolean isHouseExist = false;
            boolean isOtherImmovableExist = false;

            boolean isOtherMovableExist = false;
            boolean isAllMovableExist = false;

            cell = new PdfPCell(new Phrase("PROPERTY STATEMENT FOR STATE GOVERNMENT EMPLOYEES", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD | Font.UNDERLINE)));
            cell.setFixedHeight(20);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name (in full) of Officer :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getFullname(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("HRMS ID :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getEmpid(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getSpn(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Financial Year :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getFiscalyear(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Pay Scale :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getPayscale(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Basic Pay :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getCurbasicsalary() + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Grade Pay :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getGradepay() + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GPF/PRAN NO :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getGpfNo() + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Property Submitted On :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(propertyStatement.getSubmittedOn() + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            /*Basic Profile End*/

            /*A. IMMOVABLE PROPERTY Start*/
            /*Land*/
            table = new PdfPTable(9);
            table.setWidths(new int[]{1, 3, 3, 3, 3, 3, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("A. IMMOVABLE PROPERTY", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(1) LANDS", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SL. No", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Precise location", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Area", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nature of land", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Extent of interest", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Value(INR)", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date and manner of acquisition or disposal", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", tableHeader));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("7", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("8", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("9", tableHeader));
            table.addCell(cell);

            for (int i = 0, j = 0; i < immovablePropertyDetailList.size(); i++) {
                PropertyDetail propertyDetail = (PropertyDetail) immovablePropertyDetailList.get(i);
                if (propertyDetail.getPropertyId() == 7 && (propertyDetail.getPropertyValue() != null && !propertyDetail.getPropertyValue().equals(""))) {
                    isLandExist = true;

                    j++;
                    cell = new PdfPCell(new Phrase((j) + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyLocation(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyArea() + " " + propertyDetail.getAreaunit(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyNature(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getInterest(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyValue() + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    if (propertyDetail.getPropertyOwner() == 5) {
                        cell = new PdfPCell(new Phrase(propertyDetail.getOtherPropertyOwner(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase(propertyDetail.getPropertyOwnerDtl(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                    String dateOfAcq = "";
                    if (propertyDetail.getDateOfAcq() != null) {
                        dateOfAcq = propertyDetail.getDateOfAcq() + "";
                    }
                    cell = new PdfPCell(new Phrase(dateOfAcq + " " + propertyDetail.getManner(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getRemark(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
            }
            if (isLandExist == false) {
                cell = new PdfPCell(new Phrase("Nil Property Submitted", new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
                cell.setColspan(9);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            document.add(table);

            /*HOUSES*/
            table = new PdfPTable(7);
            table.setWidths(new int[]{1, 3, 3, 3, 3, 3, 5});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("(2) HOUSES", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SL. No", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Precise location", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Extent of interest", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Value(INR)", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date and manner of acquisition or disposal", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", tableHeader));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", tableHeader));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("7", tableHeader));
            table.addCell(cell);

            for (int i = 0, j = 0; i < immovablePropertyDetailList.size(); i++) {
                PropertyDetail propertyDetail = (PropertyDetail) immovablePropertyDetailList.get(i);
                if (propertyDetail.getPropertyId() == 8 && (propertyDetail.getPropertyValue() != null && !propertyDetail.getPropertyValue().equals(""))) {
                    isHouseExist = true;

                    j++;
                    cell = new PdfPCell(new Phrase((j) + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyLocation(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getInterest(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyValue() + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    if (propertyDetail.getPropertyOwner() == 5) {
                        cell = new PdfPCell(new Phrase(propertyDetail.getOtherPropertyOwner(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase(propertyDetail.getPropertyOwnerDtl(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                    String dateOfAcq = "";
                    if (propertyDetail.getDateOfAcq() != null) {
                        dateOfAcq = propertyDetail.getDateOfAcq().toString();
                    }
                    cell = new PdfPCell(new Phrase(dateOfAcq + propertyDetail.getManner(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getRemark(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                }
            }
            if (isHouseExist == false) {
                cell = new PdfPCell(new Phrase("Nil Property Submitted", new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);

            /* Immovable properties of other description*/
            table = new PdfPTable(7);
            table.setWidths(new int[]{1, 3, 3, 3, 3, 3, 5});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("(3) Immovable properties of other description (including mortgages and such other rights)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SL. No", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Brief description", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Extent of interest", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Value(INR)", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date and manner of acquisition or disposal", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("7", f1));
            table.addCell(cell);

            for (int i = 0, j = 0; i < immovablePropertyDetailList.size(); i++) {
                PropertyDetail propertyDetail = (PropertyDetail) immovablePropertyDetailList.get(i);
                if (propertyDetail.getPropertyId() == 9 && (propertyDetail.getPropertyValue() != null && !propertyDetail.getPropertyValue().equals(""))) {
                    isOtherImmovableExist = true;

                    j++;
                    cell = new PdfPCell(new Phrase((j) + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyName() + propertyDetail.getDescofothitem(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getInterest(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyValue() + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    if (propertyDetail.getPropertyOwner() == 5) {
                        cell = new PdfPCell(new Phrase(propertyDetail.getOtherPropertyOwner(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase(propertyDetail.getPropertyOwnerDtl(), f2));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                    String dateOfAcq = "";
                    if (propertyDetail.getDateOfAcq() != null) {
                        dateOfAcq = propertyDetail.getDateOfAcq().toString();
                    }
                    cell = new PdfPCell(new Phrase(dateOfAcq, f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getRemark(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
            }
            if (isOtherImmovableExist == false) {
                cell = new PdfPCell(new Phrase("Nil Property Submitted", new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }
            document.add(table);

            /* B. MOVABLE PROPERTY
             (1) Cash, Bank balance, Credit, Insurance policies, shares, Debentures, etc.
             */
            table = new PdfPTable(7);
            table.setWidths(new int[]{2, 2, 2, 2, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("B. MOVABLE PROPERTY", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("(1) Cash, Bank balance, Credit, Insurance policies, shares, Debentures, etc.)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SL. No", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Description of items", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Value(INR)", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date and manner of acquisition or disposal", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Loans that may have been given to others", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("7", f1));
            table.addCell(cell);

            for (int i = 0, j = 0; i < movablePropertyDetailList.size(); i++) {
                PropertyDetail propertyDetail = (PropertyDetail) movablePropertyDetailList.get(i);
                if (propertyDetail.getPropertyId() != 10 && (propertyDetail.getPropertyValue() != null && !propertyDetail.getPropertyValue().equals(""))) {
                    isAllMovableExist = true;

                    j++;
                    cell = new PdfPCell(new Phrase((j) + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyName(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyValue() + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    if (propertyDetail.getPropertyOwner() == 5) {
                        cell = new PdfPCell(new Phrase(propertyDetail.getOtherPropertyOwner(), f1));
                    } else {
                        cell = new PdfPCell(new Phrase(propertyDetail.getPropertyOwnerDtl(), f1));
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    String dateOfAcq = "";
                    if (propertyDetail.getDateOfAcq() != null) {
                        dateOfAcq = propertyDetail.getDateOfAcq().toString();
                    }
                    cell = new PdfPCell(new Phrase(dateOfAcq + propertyDetail.getManner(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    /*cell = new PdfPCell(new Phrase(propertyDetail.getManner(), f1));
                     cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                     table.addCell(cell);*/
                    String loan = "";
                    if (propertyDetail.getLoan() != null) {
                        loan = propertyDetail.getLoan() + "";
                    }
                    cell = new PdfPCell(new Phrase(loan, f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getRemark(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            if (isAllMovableExist == false) {
                cell = new PdfPCell(new Phrase("Nil Property Submitted", new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            document.add(table);

            /*(2) Other movable (including jewellery and other valuable, motor vehicles, refrigerators and 
             other articles or materials of value exceeding two months basic pay for each item 
             (Both Gazetted Officers and Non-gazetted Officers).*/
            table = new PdfPTable(6);
            table.setWidths(new int[]{1, 4, 5, 3, 3, 3});
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("(2) Other movable (including jewellery and other valuable, motor vehicles, refrigerators and other articles or "
                    + " materials of value exceeding two months basic pay for each item (Both Gazetted Officers and Non- "
                    + "gazetted Officers ) ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SL. No", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Description of items", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Value(INR)", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date and manner of acquisition or disposal", f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("2", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("3", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("4", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("5", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("6", f1));
            table.addCell(cell);

            PropertyDetail propertyDetail = null;
            for (int i = 0, j = 0; i < movablePropertyDetailList.size(); i++) {
                propertyDetail = (PropertyDetail) movablePropertyDetailList.get(i);
                if (propertyDetail.getPropertyId() == 10 && (propertyDetail.getPropertyValue() != null && !propertyDetail.getPropertyValue().equals(""))) {
                    isOtherMovableExist = true;

                    j++;
                    cell = new PdfPCell(new Phrase((j) + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyName() + "" + propertyDetail.getDescofothitem(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(propertyDetail.getPropertyValue() + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    if (propertyDetail.getPropertyOwner() == 5) {
                        cell = new PdfPCell(new Phrase(propertyDetail.getOtherPropertyOwner(), f1));
                    } else {
                        cell = new PdfPCell(new Phrase(propertyDetail.getPropertyOwnerDtl(), f1));
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    String dateOfAcq = "";
                    if (propertyDetail.getDateOfAcq() != null) {
                        dateOfAcq = propertyDetail.getDateOfAcq().toString();
                    }
                    cell = new PdfPCell(new Phrase(dateOfAcq + propertyDetail.getManner(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    /*cell = new PdfPCell(new Phrase(propertyDetail.getManner(), f1));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     table.addCell(cell); */
                    cell = new PdfPCell(new Phrase(propertyDetail.getRemark(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            if (isOtherMovableExist == false) {
                cell = new PdfPCell(new Phrase("Nil Property Submitted", new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
                cell.setColspan(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);
            /*             

             cell = new PdfPCell(new Phrase("I hereby declare that the declaration made above is complete true and correct to the best of my knowledge and belief.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
             cell.setColspan(5);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             table.addCell(cell);

             cell = new PdfPCell();
             cell.setColspan(4);
             cell.setFixedHeight(40);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);

             cell = new PdfPCell(new Phrase("Date: ", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("Signature........", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
             table.addCell(cell);

             cell = new PdfPCell();
             cell.setColspan(4);
             cell.setFixedHeight(40);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);

             cell = new PdfPCell(new Phrase("Note:-(1)The categories of assets noted in brackets in above heads are only illustrative and not meant to be exhaustive.\n"
             + "In case of jewelleries and ornaments their total weight in totals and their cash value should be given in column 3 of Form B(2).", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
             cell.setColspan(4);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             table.addCell(cell);

             cell = new PdfPCell();
             cell.setColspan(4);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);

             cell = new PdfPCell(new Phrase(" (2)In filling the form, endeavour should be made to provide Government with as complete a picture as possible of the Government servant’s assets and no\n"
             + "asset of appreciable value should be omitted by reason of any literal interpretation of the directions given.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
             cell.setColspan(4);
             cell.setBorder(Rectangle.NO_BORDER);
             cell.setBorderWidth(1f);
             table.addCell(cell);

             cell = new PdfPCell();
             cell.setColspan(4);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             */
            //document.add(table);

            /*Download Details*/
            table = new PdfPTable(1);
            table.setWidths(new int[]{10});
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);
            document.add(table);

            SimpleDateFormat formatterNew = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date tdate = new Date();
            String IP = CommonFunctions.getISPIPAddress();

            String propertyFilePath = qrCodePath;
            String storedpath = qrCodePath + propertyStatement.getFiscalyear() + CommonFunctions.getResourcePath();
            String url = storedpath + propertyStatement.getEmpid() + "-" + propertyStatement.getYearlyPropId() + ".png";
            File f = null;
            f = new File(url);
            Image img1 = null;

            Date frmdate = (new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-20"));
            Date todate = (new SimpleDateFormat("yyyy-MM-dd").parse("2024-02-01"));

            if (!propertyStatement.getFiscalyear().equals("2016-17") && !propertyStatement.getFiscalyear().equals("2017-18") && !propertyStatement.getFiscalyear().equals("2018-19")
                    && !propertyStatement.getFiscalyear().equals("2019-20") && !propertyStatement.getFiscalyear().equals("2020")
                    && !propertyStatement.getFiscalyear().equals("2021") && !propertyStatement.getFiscalyear().equals("2022")) {
                //System.out.println("propertyStatement.getSubmittedOn().after(frmdate) is: "+propertyStatement.getSubmittedOn().after(frmdate));
                //System.out.println("propertyStatement.getSubmittedOn().before(todate) is: "+propertyStatement.getSubmittedOn().before(todate));
                if (propertyStatement.getSubmittedOn() != null && !propertyStatement.getSubmittedOn().equals("")) {
                    if (propertyStatement.getFiscalyear().equals("2023") && (propertyStatement.getSubmittedOn().after(frmdate) && propertyStatement.getSubmittedOn().before(todate))) {
                        if (propertyStatement.getStatusid() == 1) {
                            if (f.exists()) {
                                img1 = Image.getInstance(url);
                                img1.setBorder(Rectangle.BOX);
                            } else {
                                generateQRCodeForPreviousYear(propertyStatement.getYearlyPropId(), qrCodePath);
                                img1 = Image.getInstance(url);
                                img1.setBorder(Rectangle.BOX);
                            }
                        } else {
                            img1 = Image.getInstance(qrCodePath + "NoEmployee.png");
                        }
                    } else if (propertyStatement.getFiscalyear().equals("2023") && (propertyStatement.getSubmittedOn().before(frmdate) || propertyStatement.getSubmittedOn().after(todate))) {
                        if (propertyStatement.getStatusid() == 1) {
                            if (f.exists()) {
                                img1 = Image.getInstance(url);
                                img1.setBorder(Rectangle.BOX);
                            } else {
                                generateQRCodeForPreviousYear(propertyStatement.getYearlyPropId(), qrCodePath);
                                img1 = Image.getInstance(url);
                                img1.setBorder(Rectangle.BOX);
                            }
                        } else {
                            img1 = Image.getInstance(qrCodePath + "NoEmployee.png");
                        }
                    }
                } else {
                    if (f.exists()) {
                        img1 = Image.getInstance(url);
                        img1.setBorder(Rectangle.BOX);
                    } else {
                        img1 = Image.getInstance(qrCodePath + "NoEmployee.png");
                    }

                }
            } else {
                if (propertyStatement.getStatusid() == 1) {
                    if (f.exists()) {
                        img1 = Image.getInstance(url);
                        img1.setBorder(Rectangle.BOX);
                    } else {
                        generateQRCodeForPreviousYear(propertyStatement.getYearlyPropId(), qrCodePath);
                        img1 = Image.getInstance(url);
                        img1.setBorder(Rectangle.BOX);
                    }
                } else {
                    img1 = Image.getInstance(qrCodePath + "NoEmployee.png");
                }
            }

            table = new PdfPTable(6);
            table.setWidths(new int[]{6, 3, 6, 3, 6, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("PDF Downloaded on :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(formatterNew.format(tdate) + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Downloaded From IP :", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(IP + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (img1 != null) {
                //img1.scalePercent(15f);
                img1.scaleToFit(100f, 80f);
                cell = new PdfPCell(img1);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
//            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            document.add(table);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*@Override
     public boolean isCheckPropertyPeriod(PropertyStatement propertyStatement) {
     Connection con = null;
     PreparedStatement pst = null;
     ResultSet rs = null;
     boolean iscorrectPeriod = true;
     boolean correctPeriod = false;
     String sql = "";
     String dbf1 = "";
     String dbt1 = "";

     String selectedfiscalyear = "";
     String dtfinfromdate = "";
     String dtfintodate = "";

     SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
     try {
     con = this.dataSource.getConnection();
     if (propertyStatement.getYearlyPropId() != null) {
     sql = "SELECT FROM_DATE,TO_DATE,FINANCIAL_YEAR FROM  PROPERTY_STATEMENT_LIST WHERE EMP_ID=? AND FINANCIAL_YEAR=? AND YEARLY_PROPERTY_ID <> ";
     pst = con.prepareStatement(sql);
     pst.setString(1, propertyStatement.getEmpid());
     pst.setString(2, propertyStatement.getFiscalyear());
     } else {
     sql = "SELECT FROM_DATE,TO_DATE,FINANCIAL_YEAR FROM  PROPERTY_STATEMENT_LIST WHERE EMP_ID=? AND FINANCIAL_YEAR=?";
     pst = con.prepareStatement(sql);
     pst.setString(1, propertyStatement.getEmpid());
     pst.setString(2, propertyStatement.getFiscalyear());
     }
     rs = pst.executeQuery();
     int noofrows = 0;
     while (rs.next()) {

     selectedfiscalyear = rs.getString("FINANCIAL_YEAR");
     String[] splitedFiscalyear = selectedfiscalyear.split("-");
     //dbf1 = CommonFunctions.getFormattedOutputDate1(propertyStatement.getFromdate());
     //dbt1 = CommonFunctions.getFormattedOutputDate1(propertyStatement.getTodate());

     System.out.println("selectedfiscalyear is --" + selectedfiscalyear);
     //System.out.println("dbf1 -->" + dbf1);
     //System.out.println("dbt1 -->" + dbt1);

     if (Integer.parseInt(splitedFiscalyear[0]) < 2020) {
     dtfinfromdate = "01-APR-" + splitedFiscalyear[0];
     dtfintodate = "31-MAR-" + splitedFiscalyear[0] + 1;
     System.out.println("111111111 and dtfinfromdate -->" + dtfinfromdate);
     System.out.println("111111111 and dtfintodate -->" + dtfinfromdate);
     System.out.println("11111111 and propertyStatement.getFromdate() -->" +propertyStatement.getFromdate());
     if (propertyStatement.getFromdate().equals(dtfinfromdate) && propertyStatement.getTodate().equals(dtfintodate)) {
     System.out.println("111111 property period same");
     correctPeriod = true;
     } else {
     System.out.println("111111 property period different");
     correctPeriod = false;
     }
                
     } else if (Integer.parseInt(splitedFiscalyear[0]) == 2020) {
     dtfinfromdate = "01-APR-" + splitedFiscalyear[0];
     dtfintodate = "31-DEC-" + splitedFiscalyear[0];
     System.out.println("22222222222 and dtfinfromdate --> " + dtfinfromdate);
     System.out.println("22222222222 and dtfintodate -->" + dtfintodate);
     System.out.println("propertyStatement.getFromdate()" +propertyStatement.getFromdate());
     if (propertyStatement.getFromdate().equals(dtfinfromdate) && propertyStatement.getTodate().equals(dtfintodate)) {
     //if (dbf1 == dtfinfromdate && dbt1 == dtfintodate) {
     System.out.println("222222222 property period same");
     correctPeriod = true;
     } else {
     System.out.println("2222222 property period different");
     correctPeriod = false;
     }

     } else if (Integer.parseInt(splitedFiscalyear[0]) > 2020) {
     dtfinfromdate = "01-JAN-" + splitedFiscalyear[0];
     dtfintodate = "31-DEC-" + splitedFiscalyear[0];
     System.out.println("3333333333 and dtfinfromdate --> " + dtfinfromdate);
     System.out.println("3333333333 and dtfintodate --> " + dtfintodate);
     System.out.println("propertyStatement.getFromdate() -->" +propertyStatement.getFromdate());
     if (propertyStatement.getFromdate().equals(dtfinfromdate) && propertyStatement.getTodate().equals(dtfintodate)) {
     //if (dbf1 == dtfinfromdate && dbt1 == dtfintodate) {
     System.out.println("33333333333 property period same");
     correctPeriod = true;
     } else {
     System.out.println("33333333333 property period different");
     correctPeriod = false;
     }
     }
     if (correctPeriod == false) {
     iscorrectPeriod = false;
     }
     }

     } catch (SQLException e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     return iscorrectPeriod;
     } */

    public boolean isCheckPropertyPeriod(String fiscalYear, Date fromdate, Date todate) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean iscorrectPeriod = true;
        boolean correctPeriod = false;

        String sql = "";
        String dbf1 = "";
        String dbt1 = "";

        String selectedfiscalyear = "";
        String dtfinfromdate = "";
        String dtfintodate = "";

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            con = this.dataSource.getConnection();
            selectedfiscalyear = fiscalYear;
            String[] splitedFiscalyear = selectedfiscalyear.split("-");

            if (Integer.parseInt(splitedFiscalyear[0]) < 2020) {
                dtfinfromdate = "01-APR-" + splitedFiscalyear[0];
                dtfintodate = "31-MAR-" + "20" + splitedFiscalyear[1];
                if (CommonFunctions.getFormattedOutputDate1(fromdate).equals(dtfinfromdate) && CommonFunctions.getFormattedOutputDate1(todate).equals(dtfintodate)) {
                    correctPeriod = true;
                } else {
                    correctPeriod = false;
                }

            } else if (Integer.parseInt(splitedFiscalyear[0]) == 2020) {
                dtfinfromdate = "01-APR-" + splitedFiscalyear[0];
                dtfintodate = "31-DEC-" + splitedFiscalyear[0];
                if (CommonFunctions.getFormattedOutputDate1(fromdate).equals(dtfinfromdate) && CommonFunctions.getFormattedOutputDate1(todate).equals(dtfintodate)) {
                    correctPeriod = true;
                } else {
                    correctPeriod = false;
                }

            } else if (Integer.parseInt(splitedFiscalyear[0]) > 2020) {
                dtfinfromdate = "01-JAN-" + splitedFiscalyear[0];
                dtfintodate = "31-DEC-" + splitedFiscalyear[0];
                if (CommonFunctions.getFormattedOutputDate1(fromdate).equals(dtfinfromdate) && CommonFunctions.getFormattedOutputDate1(todate).equals(dtfintodate)) {
                    correctPeriod = true;
                } else {
                    correctPeriod = false;
                }
            }
            if (correctPeriod == false) {
                iscorrectPeriod = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return iscorrectPeriod;

    }

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));

    }

    @Override
    public void generateQRCodeForPreviousYear(BigDecimal yearlyPropId, String qrCodePath) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str4 = "";
        String str = "";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT YEARLY_PROPERTY_ID,PROPERTY_STATEMENT_LIST.EMP_ID,YEARLY_PROPERTY_ID,FINANCIAL_YEAR,STATUS_ID,SUBMISSION_TYPE,FROM_DATE,"
                    + "TO_DATE,INITIALS,F_NAME,M_NAME,L_NAME,SPN,PROPERTY_STATEMENT_LIST.PAY_SCALE,PROPERTY_STATEMENT_LIST.BASIC_SAL,PROPERTY_STATEMENT_LIST.grade_pay,"
                    + "SUBMITTED_ON,IS_CLOSED_PROPERTY,EMP_MAST.gpf_no FROM PROPERTY_STATEMENT_LIST "
                    + "INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC = G_SPC.SPC "
                    + "INNER JOIN FINANCIAL_YEAR ON PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR = FINANCIAL_YEAR.CY "
                    + "WHERE YEARLY_PROPERTY_ID=?");
            pstmt.setBigDecimal(1, yearlyPropId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String str1 = rs.getString("EMP_ID");
                String str2 = rs.getString("FINANCIAL_YEAR");
                String str3 = Integer.toString(rs.getInt("STATUS_ID"));
                if (str3 != null && !str3.equals("") && str3.equals("0")) {
                    str4 = "NOT SUBMITTED";
                } else {
                    str4 = "SUBMITTED";
                }
                String str5 = rs.getString("submitted_on");
                String str6 = StringUtils.defaultString(rs.getString("INITIALS")) + " " + rs.getString("F_NAME") + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + rs.getString("L_NAME");
                String str7 = Integer.toString(rs.getInt("YEARLY_PROPERTY_ID"));
                str = "HRMS Id =" + str1 + ",Employee Name=" + str6 + ",financial year=" + str2 + " ,status = " + str4 + " ,submitted_on= " + str5 + "";

                //data that we want to store in the QR code  
                String propertyFilePath = qrCodePath;
                String storedpath = qrCodePath + str2 + CommonFunctions.getResourcePath();
                //String storedpath = qrCodePath + str2;
                String path = storedpath + str1 + "-" + str7 + ".png";
                //String path = "D:\\Propertystatement\\" + str2 + "-" +  "(" +str1 + "-" + str7 + ")"  + ".png";
                //Encoding charset to be used  
                String charset = "UTF-8";
                Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                //generates QR code with Low level(L) error correction capability  
                hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                //invoking the user-defined method that creates the QR code  
                generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly   
                //prints if the QR code is generated   
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (WriterException ex) {
            Logger.getLogger(PropertyStatementDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertyStatementDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}

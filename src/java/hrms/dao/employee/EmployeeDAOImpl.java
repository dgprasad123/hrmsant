 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.employee;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.common.RandomPasswordGenerator;
import hrms.common.SMSServices;
import hrms.common.SMSThread;
import hrms.model.EmployeeRegistration.EmployeeRegistrationBean;
import hrms.model.discProceeding.NoticeBean;
import hrms.model.empinfo.Attachment;
import hrms.model.empinfo.EmployeeMessage;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.employee.Address;
import hrms.model.employee.ArrEmpBillGroup;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeLanguage;
import hrms.model.employee.EmployeePayProfile;
import hrms.model.employee.FamilyRelation;
import hrms.model.employee.IdentityInfo;
import hrms.model.employee.PensionFamilyList;
import hrms.model.employee.PensionNomineeList;
import hrms.model.employee.PensionProfile;
import hrms.model.employee.Pensioner;
import hrms.model.employee.PreviousPensionDetails;
import hrms.model.employee.Punishment;
import hrms.model.employee.Reward;
import hrms.model.employee.Training;
import hrms.model.employee.TransferJoining;
import hrms.model.employee.employeeFamilyHelperBean;
import hrms.model.loan.Loan;
import hrms.model.parmast.ParStatusBean;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.billmast.BillStatus;
import hrms.model.servicebook.EmpServiceHistory;
import hrms.model.trainingschedule.TrainingSchedule;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas Jena
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    protected String uploadPath;
    protected String sbfilepath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setSbfilepath(String sbfilepath) {
        this.sbfilepath = sbfilepath;
    }

    private String formatDate(Date input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = "";

        try {
            formattedDate = sdf.format(input);
        } catch (Exception exp) {
            formattedDate = "";
        }
        return formattedDate;
    }

    public Training[] getEmployeeTraining(String empid, String inputdate) {
        List<Training> traininglist = new ArrayList<>();
        String SQL = "SELECT TITLE,S_DATE,C_DATE,PLACE,NOTE,trainid FROM EMP_TRAIN WHERE EMP_ID=? AND DOE > ?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(inputdate, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                Training training = new Training();
                training.setTitle(result.getString("TITLE"));
                training.setSdate(result.getString("S_DATE"));
                training.setCdate(result.getString("C_DATE"));
                training.setPlace(result.getString("PLACE"));
                training.setNote(result.getString("NOTE"));
                training.setTrainid(result.getString("trainid"));
                traininglist.add(training);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Training trainingarray[] = traininglist.toArray(new Training[traininglist.size()]);
        return trainingarray;
    }

    public Punishment[] getEmployeePunishment(String empid, String inputdate) {
        List<Punishment> punlist = new ArrayList<>();
        String SQL = "select DUR_FROM,DUR_TO,DAYS,REASON,IF_PAY_HELDUP,ORDNO,ORDDT,NOTE FROM "
                + "(SELECT * FROM EMP_NOTIFICATION WHERE EMP_ID = ? AND NOT_TYPE='ADM_ACTION' AND  DOE > ?) EMP_NOTIFICATION "
                + "INNER JOIN EMP_AD_ACTION ON EMP_AD_ACTION.NOT_ID=EMP_NOTIFICATION.NOT_ID";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(inputdate, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                Punishment punishment = new Punishment();
                punishment.setNote(result.getString("note"));
                punishment.setOrdno(result.getString("ORDNO"));
                punishment.setOrddate(result.getString("ORDDT"));
                punishment.setDays(result.getString("DAYS"));
                punishment.setDurfrom(result.getString("DUR_FROM"));
                punishment.setDurto(result.getString("DUR_TO"));
                punishment.setIfpayheldup(result.getString("IF_PAY_HELDUP"));
                punishment.setReason(result.getString("REASON"));
                punlist.add(punishment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Punishment punarray[] = punlist.toArray(new Punishment[punlist.size()]);
        return punarray;
    }

    @Override
    public Education[] getEmployeeEducation(String empid) {
        List<Education> edulist = new ArrayList<>();
        String SQL = "SELECT QUALIFICATION,FACULTY,YOP,DG.DEGREE,SUBJECTS,INSTITUTE"
                + ", board as BOARD_UNIV, QFN_ID,is_locked FROM EMP_QUALIFICATION EQ left outer join g_board_university B"
                + " ON EQ.BOARD_UNIV = B.board"
                + " left outer join g_degree DG ON EQ.degree = DG.degree_sl::text"
                + " WHERE EMP_ID=? ORDER BY QFN_ID";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                Education education = new Education();
                education.setQualification(result.getString("QUALIFICATION"));
                education.setFaculty(result.getString("FACULTY"));
                education.setYearofpass(result.getString("YOP"));
                education.setDegree(result.getString("DEGREE"));
                if (result.getString("SUBJECTS") != null && !result.getString("SUBJECTS").equals("")) {
                    education.setSubject(result.getString("SUBJECTS").toUpperCase());
                } else {
                    education.setSubject("");
                }

                education.setInstitute(result.getString("INSTITUTE"));
                education.setBoard(result.getString("BOARD_UNIV"));
                education.setQfn_id(result.getInt("QFN_ID"));
                education.setIsLocked(result.getString("is_locked"));
                edulist.add(education);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Education eduarray[] = edulist.toArray(new Education[edulist.size()]);
        return eduarray;
    }

    @Override
    public void saveAddressData(Address address) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            if (address.getAddressId() > 0 && address.getAddressType().equals("PERMANENT")) {
                pst = con.prepareStatement("UPDATE emp_address set address=?,bl_code=?,vill_code=?,po_code=?,ps_code=?,pin=?,state_code=?,dist_code=?,std_code=?,tphone=?,address_type=? WHERE emp_id=? and address_id=?");

                pst.setString(1, address.getAddress());
                pst.setString(2, address.getBlockCode());
                pst.setString(3, address.getVillageCode());
                pst.setString(4, address.getPostCode());
                pst.setString(5, address.getPsCode());
                pst.setString(6, address.getPin());
                pst.setString(7, address.getStateCode());
                pst.setString(8, address.getDistCode());
                pst.setString(9, address.getStdCode());
                pst.setString(10, address.getTelephone());
                pst.setString(11, address.getAddressType());
                pst.setString(12, address.getEmpId());
                pst.setInt(13, address.getAddressId());
                pst.executeUpdate();
            }
            if (address.getAddressId() == 0 && address.getAddressType().equals("PERMANENT")) {
                pst = con.prepareStatement("INSERT INTO emp_address (emp_id, address_type, address, bl_code, vill_code, po_code, ps_code, pin, state_code, dist_code, std_code, tphone) values (?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, address.getEmpId());
                pst.setString(2, address.getAddressType());
                pst.setString(3, address.getAddress());
                pst.setString(4, address.getBlockCode());
                pst.setString(5, address.getVillageCode());
                pst.setString(6, address.getPostCode());
                pst.setString(7, address.getPsCode());
                pst.setString(8, address.getPin());
                pst.setString(9, address.getStateCode());
                pst.setString(10, address.getDistCode());
                pst.setString(11, address.getStdCode());
                pst.setString(12, address.getTelephone());
                pst.executeUpdate();
            }
            if (address.getPrAddressId() > 0 && address.getPrAddressType().equals("PRESENT")) {
                pst = con.prepareStatement("UPDATE emp_address set address=?,bl_code=?,vill_code=?,po_code=?,ps_code=?,pin=?,state_code=?,dist_code=?,std_code=?,tphone=?,address_type=? WHERE emp_id=? and address_id=?");

                pst.setString(1, address.getPrAddress());
                pst.setString(2, address.getPrBlockCode());
                pst.setString(3, address.getPrVillageCode());
                pst.setString(4, address.getPrPostCode());
                pst.setString(5, address.getPrPsCode());
                pst.setString(6, address.getPrPin());
                pst.setString(7, address.getPrStateCode());
                pst.setString(8, address.getPrDistCode());
                pst.setString(9, address.getPrStdCode());
                pst.setString(10, address.getPrTelephone());
                pst.setString(11, address.getPrAddressType());
                pst.setString(12, address.getEmpId());
                pst.setInt(13, address.getPrAddressId());
                pst.executeUpdate();
            }
            if (address.getPrAddressId() == 0 && address.getPrAddressType().equals("PRESENT")) {
                pst = con.prepareStatement("INSERT INTO emp_address (emp_id, address_type, address, bl_code, vill_code, po_code, ps_code, pin, state_code, dist_code, std_code, tphone) values (?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, address.getEmpId());
                pst.setString(2, address.getPrAddressType());
                pst.setString(3, address.getPrAddress());
                pst.setString(4, address.getPrBlockCode());
                pst.setString(5, address.getPrVillageCode());
                pst.setString(6, address.getPrPostCode());
                pst.setString(7, address.getPrPsCode());
                pst.setString(8, address.getPrPin());
                pst.setString(9, address.getPrStateCode());
                pst.setString(10, address.getPrDistCode());
                pst.setString(11, address.getPrStdCode());
                pst.setString(12, address.getPrTelephone());
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveEmployeeEducation(Education education) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT COALESCE(MAX(QFN_ID)+1,1) AS QFN_ID FROM EMP_QUALIFICATION");
            result = pst.executeQuery();
            int qfnId = 0;
            if (result.next()) {
                qfnId = result.getInt("QFN_ID");
            }

            pst = con.prepareStatement("INSERT INTO EMP_QUALIFICATION (emp_id, qualification, faculty, yop, degree, subjects, institute, board_univ,qfn_id) Values(?,?,?,?,?,?,?,?,?)");
            pst.setString(1, education.getEmpId());
            pst.setString(2, education.getQualification());
            pst.setString(3, education.getFaculty());
            pst.setString(4, education.getYearofpass());
            pst.setString(5, education.getDegree());
            pst.setString(6, education.getSubject());
            pst.setString(7, education.getInstitute());
            pst.setString(8, education.getBoard());
            pst.setInt(9, qfnId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmployeeEducation(Education education) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            System.out.println("education.getDegree()+===" + education.getDegree());
            System.out.println("education.getBoard()+===" + education.getBoard());
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE emp_qualification set qualification=?,faculty=?,yop=?,degree=?,subjects=?,institute=?,board_univ=? WHERE qfn_id=? AND emp_id = ?");
            pstmt.setString(1, education.getQualification());
            pstmt.setString(2, education.getFaculty());
            pstmt.setString(3, education.getYearofpass());
            pstmt.setString(4, education.getDegree());
            pstmt.setString(5, education.getSubject());
            pstmt.setString(6, education.getInstitute());
            pstmt.setString(7, education.getBoard());
            pstmt.setInt(8, education.getQfn_id());
            pstmt.setString(9, education.getEmpId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public Education getEmployeeEducationDtls(int qfnid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        Education education = new Education();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from emp_qualification where QFN_ID=? ");
            pst.setInt(1, qfnid);
            result = pst.executeQuery();
            while (result.next()) {
                education.setEmpId(result.getString("emp_id"));
                education.setQfn_id(result.getInt("QFN_ID"));
                education.setQualification(result.getString("QUALIFICATION"));
                education.setFaculty(result.getString("FACULTY"));
                education.setYearofpass(result.getString("YOP"));
                education.setDegree(result.getString("DEGREE"));
                education.setSubject(result.getString("SUBJECTS"));
                education.setInstitute(result.getString("INSTITUTE"));
                education.setBoard(result.getString("BOARD_UNIV"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return education;
    }

    public Education[] getEmployeeEducation(String empid, String inputdate) {
        List<Education> edulist = new ArrayList<>();
        String SQL = "SELECT QUALIFICATION,FACULTY,YOP,DEGREE,SUBJECTS,INSTITUTE, BOARD_UNIV, QFN_ID FROM EMP_QUALIFICATION WHERE EMP_ID=? AND DOE > ? ORDER BY QFN_ID;";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(inputdate, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                Education education = new Education();
                education.setQualification(result.getString("QUALIFICATION"));
                education.setFaculty(result.getString("FACULTY"));
                education.setYearofpass(result.getString("YOP"));
                education.setDegree(result.getString("DEGREE"));
                education.setSubject(result.getString("SUBJECTS"));
                education.setInstitute(result.getString("INSTITUTE"));
                education.setBoard(result.getString("BOARD_UNIV"));
                education.setQfn_id(result.getInt("QFN_ID"));
                edulist.add(education);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Education eduarray[] = edulist.toArray(new Education[edulist.size()]);
        return eduarray;
    }

    public EmployeeLanguage[] getLanguageKnown(String empid) {
        List<EmployeeLanguage> languagelist = new ArrayList<>();
        String SQL = "SELECT LANGUAGE, IF_READ, IF_WRITE, IF_SPEAK, IF_MLANG,is_locked,sl_no FROM EMP_LANGUAGE WHERE EMP_ID=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                EmployeeLanguage language = new EmployeeLanguage();
                language.setLanguage(result.getString("LANGUAGE"));
                language.setIfread(result.getString("IF_READ"));
                language.setIfspeak(result.getString("IF_SPEAK"));
                language.setIfwrite(result.getString("IF_WRITE"));
                language.setIfmlang(result.getString("IF_MLANG"));
                language.setIsLocked(result.getString("is_locked"));
                language.setSlno(result.getInt("sl_no"));
                languagelist.add(language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        EmployeeLanguage languagearray[] = languagelist.toArray(new EmployeeLanguage[languagelist.size()]);
        return languagearray;
    }

    @Override
    public String updateEmployeeFamily(FamilyRelation familyRelation) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        String status = "Sucess";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            conn = dataSource.getConnection();

            if (familyRelation.getIsLocked() != null && familyRelation.getIsLocked().equals("Y")) {
                pstmt = conn.prepareStatement("UPDATE EMP_RELATION SET IFSC_CODE=?,BANK_ACC_NO=?,BRANCH_CODE=?,amount_type=?,fixed_amount=?,formula=?,bank_code=? WHERE EMP_ID=? AND SL_NO=?");
                pstmt.setString(1, familyRelation.getIfsc_code());
                if (familyRelation.getBank_acc_no() != null && !familyRelation.getBank_acc_no().equals("")) {
                    String banckAccno = familyRelation.getBank_acc_no().replaceAll("\\s+", "");
                    pstmt.setString(2, banckAccno.trim());
                } else {
                    pstmt.setString(2, null);
                }
                pstmt.setString(3, familyRelation.getBranch_code());
                pstmt.setString(4, familyRelation.getSltAmountType());
                if (familyRelation.getFixedAmount() != null && !familyRelation.getFixedAmount().equals("")) {
                    pstmt.setInt(5, Integer.parseInt(familyRelation.getFixedAmount()));
                } else {
                    pstmt.setInt(5, 0);
                }
                pstmt.setString(6, familyRelation.getFormula());
                pstmt.setString(7, familyRelation.getSltBank());

                pstmt.setString(8, familyRelation.getEmpId());
                pstmt.setInt(9, familyRelation.getSlno());
                pstmt.executeUpdate();
            } else {
                pstmt = conn.prepareStatement("UPDATE EMP_RELATION SET RELATION=?, IF_ALIVE=?, INITIALS=?, F_NAME=?, M_NAME=?, L_NAME=?, EMPLOYEE_EMP_ID=?,GENDER=?,DOB=?,MOBILE=?,"
                        + "MARITAL_STATUS=?,PENSION_DATE=?,SHARE_PCT=?,IFSC_CODE=?,BANK_ACC_NO=?,BRANCH_CODE=?,IS_PWD=?,PWD_TYPE=?,IS_MINOR=?,MINOR_GUARDIAN=?,REMARKS=?,ADDRESS=?,"
                        + "PRIORITY_LVL=?,IS_NOMINEE=?,amount_type=?,fixed_amount=?,formula=?,bank_code=?,nomination_gpf=?,nomination_cvp=?,arrears_per=?,arrears_acc=?,identity_doc_type=?,identity_doc_no=?,is_guardian=?,initial_guardian=? WHERE EMP_ID=? AND SL_NO=?");
                pstmt.setString(1, familyRelation.getRelation());
                pstmt.setString(2, familyRelation.getIfalive());
                pstmt.setString(3, familyRelation.getInitials());
                if (familyRelation.getFname() != null && !familyRelation.getFname().equals("")) {
                    pstmt.setString(4, familyRelation.getFname().toUpperCase());
                } else {
                    pstmt.setString(4, null);
                }
                if (familyRelation.getMname() != null && !familyRelation.getMname().equals("")) {
                    pstmt.setString(5, familyRelation.getMname().toUpperCase());
                } else {
                    pstmt.setString(5, null);
                }
                if (familyRelation.getLname() != null && !familyRelation.getLname().equals("")) {
                    pstmt.setString(6, familyRelation.getLname().toUpperCase());
                } else {
                    pstmt.setString(6, null);
                }
                pstmt.setInt(7, familyRelation.getEmployeeempid());
                pstmt.setString(8, familyRelation.getGender());
                if (familyRelation.getDob() != null && !familyRelation.getDob().equals("")) {
                    pstmt.setTimestamp(9, new Timestamp(sdf.parse(familyRelation.getDob()).getTime()));
                } else {
                    pstmt.setTimestamp(9, null);
                }
                pstmt.setString(10, familyRelation.getMobile());
                pstmt.setString(11, familyRelation.getMarital_statuS());
                if (familyRelation.getPension_date() != null) {
                    pstmt.setTimestamp(12, new Timestamp(sdf.parse(familyRelation.getPension_date()).getTime()));
                } else {
                    pstmt.setTimestamp(12, null);
                }
                pstmt.setInt(13, familyRelation.getShare_pct());
                pstmt.setString(14, familyRelation.getIfsc_code());
                if (familyRelation.getBank_acc_no() != null && !familyRelation.getBank_acc_no().equals("")) {
                    String banckAccno = familyRelation.getBank_acc_no().replaceAll("\\s+", "");
                    pstmt.setString(15, banckAccno.trim());
                } else {
                    pstmt.setString(15, null);
                }
                pstmt.setString(16, familyRelation.getBranch_code());
                pstmt.setString(17, familyRelation.getIs_pwd());
                pstmt.setString(18, familyRelation.getPwd_type());
                pstmt.setString(19, familyRelation.getIs_minor());
                pstmt.setString(20, familyRelation.getMinor_guardian());
                pstmt.setString(21, familyRelation.getRemarks());
                pstmt.setString(22, familyRelation.getAddress());
                pstmt.setInt(23, familyRelation.getPriority_lvl());
                pstmt.setString(24, familyRelation.getIs_Nominee());
                pstmt.setString(25, familyRelation.getSltAmountType());
                if (familyRelation.getFixedAmount() != null && !familyRelation.getFixedAmount().equals("")) {
                    pstmt.setInt(26, Integer.parseInt(familyRelation.getFixedAmount()));
                } else {
                    pstmt.setInt(26, 0);
                }
                pstmt.setString(27, familyRelation.getFormula());
                pstmt.setString(28, familyRelation.getSltBank());
                pstmt.setString(29, familyRelation.getNomGpf());
                pstmt.setString(30, familyRelation.getNomCvpDcrgLta());
                pstmt.setInt(31, familyRelation.getArrearsPer());
                pstmt.setInt(32, familyRelation.getArrearsAcc());
                pstmt.setString(33, familyRelation.getIdentityDocType());
                pstmt.setString(34, familyRelation.getIdentityDocNo());
                pstmt.setString(35, familyRelation.getIsGuardian());
                pstmt.setString(36, familyRelation.getSalutationFamilyGuardian());
                pstmt.setString(37, familyRelation.getEmpId());
                pstmt.setInt(38, familyRelation.getSlno());
                pstmt.executeUpdate();
            }
            DataBaseFunctions.closeSqlObjects(pstmt);

            if (familyRelation.getFixedAmount() != null && !familyRelation.getFixedAmount().equals("")) {
                if (Integer.parseInt(familyRelation.getFixedAmount()) > 0) {
                    pstmt = conn.prepareStatement("UPDATE EMP_MAST SET if_maintenance_deduct='Y' where emp_id=?");
                    pstmt.setString(1, familyRelation.getEmpId());
                    pstmt.executeUpdate();
                }
            }

        } catch (Exception e) {
            status = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return status;
    }

    @Override
    public String deleteEmployeeFamily(FamilyRelation familyRelation) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String status = "Sucess";
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM EMP_RELATION WHERE EMP_ID=? AND (SL_NO=? OR SL_NO=0)");
            pstmt.setString(1, familyRelation.getEmpId());
            pstmt.setInt(2, familyRelation.getSlno());
            pstmt.executeUpdate();
        } catch (Exception e) {
            status = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return status;
    }

    @Override
    public String saveEmployeeFamily(FamilyRelation familyRelation) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        String status = "Sucess";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT COALESCE(MAX(SL_NO)+1,1) AS SL_NO FROM EMP_RELATION WHERE EMP_ID=?");
            pstmt.setString(1, familyRelation.getEmpId());
            result = pstmt.executeQuery();

            if (result.next()) {
                familyRelation.setSlno(result.getInt("SL_NO"));
            }
            pstmt = conn.prepareStatement("INSERT INTO EMP_RELATION (EMP_ID, SL_NO, RELATION, IF_ALIVE, INITIALS, F_NAME, M_NAME, L_NAME, EMPLOYEE_EMP_ID,GENDER,DOB,MOBILE,MARITAL_STATUS,PENSION_DATE,SHARE_PCT,"
                    + "IFSC_CODE,BANK_ACC_NO,BRANCH_CODE,IS_PWD,PWD_TYPE,IS_MINOR,MINOR_GUARDIAN,REMARKS,ADDRESS,PRIORITY_LVL,IS_NOMINEE,amount_type,fixed_amount,formula,bank_code,nomination_gpf,nomination_cvp,arrears_per,arrears_acc,identity_doc_type,identity_doc_no,is_guardian,initial_guardian ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, familyRelation.getEmpId());
            pstmt.setInt(2, familyRelation.getSlno());
            pstmt.setString(3, familyRelation.getRelation());
            pstmt.setString(4, familyRelation.getIfalive());
            pstmt.setString(5, familyRelation.getInitials());
            if (familyRelation.getFname() != null && !familyRelation.getFname().equals("")) {
                pstmt.setString(6, familyRelation.getFname().toUpperCase());
            } else {
                pstmt.setString(6, null);
            }
            if (familyRelation.getMname() != null && !familyRelation.getMname().equals("")) {
                pstmt.setString(7, familyRelation.getMname().toUpperCase());
            } else {
                pstmt.setString(7, null);
            }
            if (familyRelation.getLname() != null && !familyRelation.getLname().equals("")) {
                pstmt.setString(8, familyRelation.getLname().toUpperCase());
            } else {
                pstmt.setString(8, null);
            }
            pstmt.setInt(9, familyRelation.getEmployeeempid());
            pstmt.setString(10, familyRelation.getGender());
            if (familyRelation.getDob() != null && !familyRelation.getDob().equals("")) {
                pstmt.setTimestamp(11, new Timestamp(sdf.parse(familyRelation.getDob()).getTime()));
            } else {
                pstmt.setTimestamp(11, null);
            }
            pstmt.setString(12, familyRelation.getMobile());
            pstmt.setString(13, familyRelation.getMarital_statuS());
            if (familyRelation.getPension_date() != null) {
                pstmt.setTimestamp(14, new Timestamp(sdf.parse(familyRelation.getPension_date()).getTime()));
            } else {
                pstmt.setTimestamp(14, null);
            }
            pstmt.setInt(15, familyRelation.getShare_pct());
            pstmt.setString(16, familyRelation.getIfsc_code());
            if (familyRelation.getBank_acc_no() != null && !familyRelation.getBank_acc_no().equals("")) {
                String bankaccno = familyRelation.getBank_acc_no().replaceAll("\\s+", "");
                pstmt.setString(17, bankaccno.trim());
            } else {
                pstmt.setString(17, familyRelation.getBank_acc_no());
            }
            pstmt.setString(18, familyRelation.getBranch_code());
            pstmt.setString(19, familyRelation.getIs_pwd());
            pstmt.setString(20, familyRelation.getPwd_type());
            pstmt.setString(21, familyRelation.getIs_minor());
            pstmt.setString(22, familyRelation.getMinor_guardian());
            pstmt.setString(23, familyRelation.getRemarks());
            pstmt.setString(24, familyRelation.getAddress());
            pstmt.setInt(25, familyRelation.getPriority_lvl());
            pstmt.setString(26, familyRelation.getIs_Nominee());
            pstmt.setString(27, familyRelation.getSltAmountType());
            if (familyRelation.getFixedAmount() != null && !familyRelation.getFixedAmount().equals("")) {
                pstmt.setInt(28, Integer.parseInt(familyRelation.getFixedAmount()));
            } else {
                pstmt.setInt(28, 0);
            }
            pstmt.setString(29, familyRelation.getFormula());
            pstmt.setString(30, familyRelation.getSltBank());
            pstmt.setString(31, familyRelation.getNomGpf());
            pstmt.setString(32, familyRelation.getNomCvpDcrgLta());
            pstmt.setInt(33, familyRelation.getArrearsPer());
            pstmt.setInt(34, familyRelation.getArrearsAcc());
            pstmt.setString(35, familyRelation.getIdentityDocType());
            pstmt.setString(36, familyRelation.getIdentityDocNo());
            pstmt.setString(37, familyRelation.getIsGuardian());
            pstmt.setString(38, familyRelation.getSalutationFamilyGuardian());

            pstmt.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pstmt);

            if (familyRelation.getFixedAmount() != null && !familyRelation.getFixedAmount().equals("")) {
                if (Integer.parseInt(familyRelation.getFixedAmount()) > 0) {
                    pstmt = conn.prepareStatement("UPDATE EMP_MAST SET if_maintenance_deduct='Y' where emp_id=?");
                    pstmt.setString(1, familyRelation.getEmpId());
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            status = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return status;
    }

    @Override
    public FamilyRelation[] getEmployeeFamily(String empid) {
        String fatherName = "";
        List<FamilyRelation> frlist = new ArrayList<>();
        String SQL = "SELECT gmarital.m_status,int_rel_id,EMPR.relation,SL_NO, IF_ALIVE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, INITIALS, F_NAME, M_NAME, L_NAME, EMPLOYEE_EMP_ID,gender,dob,mobile,EMPR.marital_status,int_marital_status_id,gmarital.m_status as marriage_status,"
                + "                    pension_date,share_pct,ifsc_code,bank_acc_no,branch_code,is_pwd,pwd_type,is_minor,minor_guardian, identity_doc_type, identity_doc_no,"
                + "                              remarks,address,priority_lvl,is_nominee,is_locked FROM EMP_RELATION EMPR "
                + "                              LEFT OUTER JOIN g_marital gmarital ON EMPR.marital_status=gmarital.int_marital_status_id "
                + "                              LEFT OUTER JOIN g_relation rel ON EMPR.relation=rel.relation "
                + "                              WHERE EMP_ID=? ";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                FamilyRelation familyRelation = new FamilyRelation();
                familyRelation.setSlno(result.getInt("SL_NO"));
                familyRelation.setRelation(result.getString("RELATION"));
                familyRelation.setIfalive(result.getString("IF_ALIVE"));
                familyRelation.setInitials(result.getString("INITIALS"));
                familyRelation.setFname(result.getString("F_NAME"));
                familyRelation.setMname(result.getString("M_NAME"));
                familyRelation.setLname(result.getString("L_NAME"));
                familyRelation.setRelname(result.getString("EMPNAME"));
                familyRelation.setEmployeeempid(result.getInt("EMPLOYEE_EMP_ID"));
                familyRelation.setGender(result.getString("gender"));
                familyRelation.setDob(formatDate(result.getDate("dob")));
                familyRelation.setMobile(result.getString("mobile"));
                familyRelation.setMarital_statuS(result.getString("int_marital_status_id"));
                familyRelation.setPension_date(formatDate(result.getDate("pension_date")));
                familyRelation.setShare_pct(result.getInt("share_pct"));
                familyRelation.setIfsc_code(result.getString("ifsc_code"));
                familyRelation.setBank_acc_no(result.getString("bank_acc_no"));
                familyRelation.setBranch_code(result.getString("branch_code"));
                familyRelation.setIs_pwd(result.getString("is_pwd"));
                familyRelation.setPwd_type(result.getString("pwd_type"));
                familyRelation.setIs_minor(result.getString("is_minor"));
                familyRelation.setMinor_guardian(result.getString("minor_guardian"));
                familyRelation.setRemarks(result.getString("remarks"));
                familyRelation.setAddress(result.getString("address"));
                familyRelation.setPriority_lvl(result.getInt("priority_lvl"));
                familyRelation.setIs_Nominee(result.getString("is_nominee"));
                familyRelation.setIsLocked(result.getString("is_locked"));
                familyRelation.setStrMarriageStatus(result.getString("marriage_status"));
                familyRelation.setIdentityDocNo(result.getString("identity_doc_no"));
                familyRelation.setIdentityDocType(result.getString("identity_doc_type"));
                if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                    familyRelation.setFatherName(familyRelation.getRelname());
                }
                familyRelation.setMarital(result.getString("m_status"));
                if (result.getDate("dob") != null && !result.getDate("dob").equals("")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = formatter.parse(formatDate(result.getDate("dob")));
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();
                    Period period = Period.between(givenDate, LocalDate.now());
                    //String age = period.getYears() + " years " + period.getMonths() + " months " + period.getDays() + " days";
                    String age = period.getYears() + " years " + period.getMonths() + " months ";

                    familyRelation.setAge(age);
                }
                frlist.add(familyRelation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        FamilyRelation frarray[] = frlist.toArray(new FamilyRelation[frlist.size()]);
        return frarray;
    }

    @Override
    public List getCcrdetail(String empid) {
        String fatherName = "";
        ParStatusBean parStatusBean = null;
        List ccrList = new ArrayList<>();

        String SQL = "select * from par_master where fiscal_year in \n"
                + "(concat((select (date_part('year', CURRENT_DATE))-1)::text,'-',(select substr((date_part('year', CURRENT_DATE))::text,3,2))),\n"
                + "concat((select ((date_part('year', CURRENT_DATE))-2)::text),'-',\n"
                + "(select substr((((date_part('year', CURRENT_DATE))-1)::text),3,2))))\n"
                + "and emp_id=? and par_status>=6";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                parStatusBean = new ParStatusBean();
                parStatusBean.setFiscalyear(result.getString("fiscal_year"));
                parStatusBean.setParstatus(result.getInt("par_status"));
                ccrList.add(parStatusBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return ccrList;
    }

    @Override
    public List getTrainingdetail(String empid) {
        TrainingSchedule trainingSchedule = null;
        List trainingList = new ArrayList<>();

        String SQL = "select * from emp_train where emp_id=? ";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                trainingSchedule = new TrainingSchedule();
                trainingSchedule.setTrainingSdate(formatDate(result.getDate("s_date")));
                trainingSchedule.setTrainingCdate(formatDate(result.getDate("c_date")));
                trainingSchedule.setTrainingPlace(result.getString("place"));
                trainingList.add(trainingSchedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return trainingList;
    }

    @Override
    public List getdisclipnary(String empid) {
        NoticeBean noticeBean = null;
        List disciplinary = new ArrayList<>();

        String SQL = "select ordno,orddt,sb_description from EMP_PROCEEDING inner join emp_notification ON \n"
                + "EMP_PROCEEDING.emp_id=emp_notification.emp_id and \n"
                + "EMP_PROCEEDING.INIT_NOT_ID=emp_notification.not_id where EMP_PROCEEDING.emp_id=? ";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                noticeBean = new NoticeBean();
                noticeBean.setMemoNo(result.getString("ordno"));
                noticeBean.setMemoDate(formatDate(result.getDate("orddt")));
                noticeBean.setDescription(result.getString("sb_description"));
                disciplinary.add(noticeBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return disciplinary;
    }

    @Override
    public FamilyRelation getFamilyRelationData(int slno, String empid) {
        String fatherName = "";
        FamilyRelation familyRelation = new FamilyRelation();
        String SQL = "SELECT initial_guardian,is_locked,int_rel_id,EMPR.relation, IF_ALIVE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, INITIALS, F_NAME, M_NAME, L_NAME, EMPLOYEE_EMP_ID,gender,dob,mobile,EMPR.marital_status,int_marital_status_id,"
                + " pension_date,share_pct,ifsc_code,bank_acc_no,branch_code,is_pwd,pwd_type,is_minor,minor_guardian, remarks,address,priority_lvl,is_nominee,amount_type,fixed_amount,formula,EMPR.bank_code,identity_doc_no,identity_doc_type,arrears_acc,arrears_per,nomination_cvp,nomination_gpf ,is_guardian FROM EMP_RELATION EMPR "
                + " LEFT OUTER JOIN g_marital gmarital ON EMPR.marital_status=gmarital.m_status "
                + " LEFT OUTER JOIN g_relation rel ON EMPR.relation=rel.relation "
                + " WHERE EMP_ID=? AND sl_no = ?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setInt(2, slno);
            result = statement.executeQuery();
            if (result.next()) {
                familyRelation.setSlno(slno);
                familyRelation.setEmpId(empid);
                familyRelation.setRelation(result.getString("RELATION"));
                familyRelation.setIfalive(result.getString("IF_ALIVE"));
                familyRelation.setInitials(result.getString("INITIALS"));
                familyRelation.setFname(result.getString("F_NAME"));
                familyRelation.setMname(result.getString("M_NAME"));
                familyRelation.setLname(result.getString("L_NAME"));
                familyRelation.setRelname(result.getString("EMPNAME"));
                familyRelation.setEmployeeempid(result.getInt("EMPLOYEE_EMP_ID"));
                familyRelation.setGender(result.getString("gender"));
                // familyRelation.setDob(formatDate(result.getDate("dob")));                
                familyRelation.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("dob")));
                familyRelation.setMobile(result.getString("mobile"));
                familyRelation.setMarital_statuS(result.getString("marital_status"));
                familyRelation.setPension_date(formatDate(result.getDate("pension_date")));
                familyRelation.setShare_pct(result.getInt("share_pct"));
                familyRelation.setIfsc_code(result.getString("ifsc_code"));
                familyRelation.setSltBank(result.getString("bank_code"));
                familyRelation.setBank_acc_no(result.getString("bank_acc_no"));
                familyRelation.setBranch_code(result.getString("branch_code"));
                familyRelation.setIs_pwd(result.getString("is_pwd"));
                familyRelation.setPwd_type(result.getString("pwd_type"));
                familyRelation.setIs_minor(result.getString("is_minor"));
                familyRelation.setIsGuardian(result.getString("is_guardian"));
                familyRelation.setSalutationFamilyGuardian(result.getString("initial_guardian"));
                familyRelation.setMinor_guardian(result.getString("minor_guardian"));
                familyRelation.setRemarks(result.getString("remarks"));
                familyRelation.setAddress(result.getString("address"));
                familyRelation.setPriority_lvl(result.getInt("priority_lvl"));
                familyRelation.setIs_Nominee(result.getString("is_nominee"));

                if (result.getString("INITIALS") != null && !result.getString("INITIALS").equals("")) {
                    fatherName = fatherName + " " + result.getString("INITIALS");
                }
                if (result.getString("F_NAME") != null && !result.getString("F_NAME").equals("")) {
                    fatherName = fatherName + " " + result.getString("F_NAME");
                }
                if (result.getString("M_NAME") != null && !result.getString("M_NAME").equals("")) {
                    fatherName = fatherName + " " + result.getString("M_NAME");
                }
                if (result.getString("L_NAME") != null && !result.getString("L_NAME").equals("")) {
                    fatherName = fatherName + " " + result.getString("L_NAME");
                }

                familyRelation.setFatherName(fatherName);

                familyRelation.setSltAmountType(result.getString("amount_type"));
                familyRelation.setFixedAmount(result.getString("fixed_amount"));
                familyRelation.setFormula(result.getString("formula"));
                familyRelation.setIsLocked(result.getString("is_locked"));
                familyRelation.setIdentityDocNo(result.getString("identity_doc_no"));
                familyRelation.setIdentityDocType(result.getString("identity_doc_type"));
                familyRelation.setNomGpf(result.getString("nomination_gpf"));
                familyRelation.setNomCvpDcrgLta(result.getString("nomination_cvp"));
                familyRelation.setArrearsPer(result.getInt("arrears_per"));
                familyRelation.setArrearsAcc(result.getInt("arrears_acc"));
                // familyRelation.setSalutationFamilyGuardian(result.getString("initial_guardian"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }

        return familyRelation;
    }

    @Override
    public Loan[] getEmployeeLoan(String empid) {
        List<Loan> frlist = new ArrayList<>();
        String SQL = "SELECT loanid,loan_tp,amount,lr_type,tr_code,vch_no,vch_date,p_org_amt,  "
                + "p_cum_recovered,p_recovered,i_org_amt,i_tot_no_inst,i_instl_amt,i_last_instl_no,i_last_pmt_mon,i_cum_recovered,i_recovered, "
                + "new_dedn,now_dedn,p_instl_no,i_instl_no,stop_loan FROM EMP_LOAN_SANC WHERE EMP_ID=? AND P_RECOVERED='' and I_RECOVERED=''";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            Loan l = null;
            while (result.next()) {
                l = new Loan();
                l.setTreasuryname(result.getString("tr_code"));
                l.setVoucherNo(result.getString("vch_no"));
                l.setVoucherDate(CommonFunctions.getFormattedOutputDate1(result.getDate("vch_date")));
                l.setTxtamount(result.getInt("p_org_amt"));
                l.setSltloan(result.getString("loan_tp"));
                frlist.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Loan laoanarray[] = frlist.toArray(new Loan[frlist.size()]);
        return laoanarray;
    }

    public Reward[] getEmployeeReward(String empid, String inputdate) {
        List<Reward> rwlist = new ArrayList<>();
        String SQL = "SELECT rwd_lvl,note,ORDNO,ORDDT,OFF_CODE FROM "
                + "(SELECT NOTE,NOT_ID,DOE,ORDNO,ORDDT,OFF_CODE FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='REWARD' AND DOE > ?)EMP_NOTIFICATION "
                + "INNER JOIN EMP_REWARD ON EMP_REWARD.NOT_ID=EMP_NOTIFICATION.NOT_ID";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(inputdate, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                Reward reward = new Reward();
                reward.setRwdlvl(result.getString("rwd_lvl"));
                reward.setNote(result.getString("note"));
                reward.setOrdno(result.getString("ORDNO"));
                reward.setOrddate(result.getString("ORDDT"));
                reward.setOffcode(result.getString("OFF_CODE"));
                rwlist.add(reward);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Reward rwarray[] = rwlist.toArray(new Reward[rwlist.size()]);
        return rwarray;
    }

    @Override
    public String[] getUpdatedEmpList(String input, String off_code) {
        List<String> emplist = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String SQL = "SELECT EMP_ID FROM EMP_MAST WHERE CUR_OFF_CODE = ? AND IS_REGULAR='Y' AND DEP_CODE = '02' AND LAST_UPDATED_DATE > ?";
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, off_code);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(input, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                emplist.add(result.getString("EMP_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        String employees[] = emplist.toArray(new String[emplist.size()]);
        return employees;
    }

    @Override
    public String[] getEmpList(String off_code) {
        List<String> emplist = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String SQL = "SELECT EMP_ID FROM EMP_MAST WHERE CUR_OFF_CODE = ? AND DEP_CODE = '02'";
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, off_code);
            result = statement.executeQuery();
            while (result.next()) {
                emplist.add(result.getString("EMP_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        String employees[] = emplist.toArray(new String[emplist.size()]);
        return employees;
    }

    @Override
    public TransferJoining[] getEmployeeTransferAndJoining(String empid, String input) {
        List<TransferJoining> rjlist = new ArrayList<>();
        String SQL = "SELECT DEPARTMENT_NAME,EJ.DOE, EJ.SPC, EJ.NOT_TYPE, EJ.JOIN_DATE, EJ.SPN, EMP_NOTIFICATION.ORDNO,EMP_NOTIFICATION.ORDDT,EMP_TRANSFER.OFF_CODE,RLV_DATE,EMP_RELIEVE.SPC AS RLV_FROM_SPC,GETSPN(EMP_RELIEVE.SPC) AS RLV_FROM_SPN FROM "
                + "(SELECT NOT_ID,DOE,SPC,NOT_TYPE,JOIN_DATE,GETSPN(SPC) SPN,EMP_ID FROM EMP_JOIN WHERE EMP_ID=? AND DOE > ?)EJ "
                + "INNER JOIN EMP_NOTIFICATION ON EJ.NOT_ID = EMP_NOTIFICATION.NOT_ID "
                + "INNER JOIN G_SPC ON EJ.SPC=G_SPC.SPC "
                + "INNER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE=G_SPC.DEPT_CODE "
                + "LEFT OUTER JOIN EMP_RELIEVE ON EMP_NOTIFICATION.NOT_ID = EMP_RELIEVE.NOT_ID "
                + "LEFT OUTER JOIN EMP_TRANSFER ON EMP_NOTIFICATION.NOT_ID = EMP_TRANSFER.NOT_ID ORDER BY JOIN_DATE";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(input, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                TransferJoining tj = new TransferJoining();
                tj.setDateofentry(result.getString("DOE"));
                tj.setTranstype(result.getString("NOT_TYPE"));
                tj.setJoinspc(result.getString("SPC"));
                tj.setJoinspn(result.getString("SPN"));
                if (result.getString("JOIN_DATE") != null && !result.getString("JOIN_DATE").equals("")) {
                    tj.setJoindate(CommonFunctions.getFormattedOutputDate1(result.getDate("JOIN_DATE")));
                    //   tj.setJoindate( new java.sql.Date(result.getString("JOIN_DATE").getTime());
                } else {
                    tj.setJoindate("");

                }

                tj.setOrderno(result.getString("ORDNO"));
                tj.setOrderdate(result.getString("ORDDT"));
                tj.setJoinofficecode(result.getString("OFF_CODE"));
                if (result.getString("JOIN_DATE") != null && !result.getString("JOIN_DATE").equals("")) {
                    tj.setRelievedate(CommonFunctions.getFormattedOutputDate1(result.getDate("RLV_DATE")));
                    //   tj.setJoindate( new java.sql.Date(result.getString("JOIN_DATE").getTime());
                } else {
                    tj.setRelievedate("");

                }
                //  tj.setRelievedate(result.getString("RLV_DATE"));
                tj.setRelievefromspc(result.getString("RLV_FROM_SPC"));
                tj.setRelievefromspn(result.getString("RLV_FROM_SPN"));
                tj.setDepartmentName(result.getString("DEPARTMENT_NAME"));
                rjlist.add(tj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        TransferJoining rjarray[] = rjlist.toArray(new TransferJoining[rjlist.size()]);
        return rjarray;
    }

    @Override
    public Employee getEmployeeProfile(String empid) {
        Employee employee = new Employee();
        String fullName = "";
        employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "SELECT initials,has_no_email,propstlist.status_id,EMP_JOIN.join_date,emp_mast.emp_id,matrix_cell,matrix_level,g_cadre.cadre_name,rec_source,allotment_year,GPF_NO,ACCT_TYPE,F_NAME,M_NAME,L_NAME,GENDER,staff_type,staff_category,staff_placement "
                    + ",pscyear,cdesignation,idesignation,G_MARITAL.M_STATUS,EMP_MAST.RES_CAT,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC, G_SPC.SPN,"
                    + " DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,OFF_NAME,OFF_EN,BL_GRP,emp_mast.RELIGION RELIGION_ID,g_religion.RELIGION RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, EMP_MAST.GP,"
                    + " CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, emp_mast.FIELD_OFF_CODE, EMAIL_ID, CHEST, WEIGHT, LEFT_VISION, RIGHT_VISION, BRASS_NO,DOE_GOV,HOME_TOWN,if_employed_res,if_employed_rehab,"
                    + " DIST_NAME,jointime_of_goo,toe_gov,gistype,scheme_name,gisno,domicile,EMP_MAST.bank_code,G_BANK.BANK_NAME,G_BRANCH.BRANCH_NAME,G_BRANCH.IFSC_CODE,EMP_MAST.branch_code,bank_acc_no,G_MARITAL.m_status maritalstatus,G_MARITAL.int_marital_status_id,G_SCHEME.SCHEME_NAME,if_profile_completed,G_SCHEME.SCHEME_ID,if_profile_verified,dos_age,if_gpf_assumed,doe_gov_time,"
                    + " (SELECT EMP_QTR_ALLOT.QUARTER_NO FROM EMP_QTR_ALLOT where EMP_ID = emp_mast.emp_id and (IF_SURRENDERED='N' OR IF_SURRENDERED='' OR IF_SURRENDERED is null) limit 1) as quarter_no,"
                    + " (SELECT EMP_QTR_ALLOT.ADDRESS FROM EMP_QTR_ALLOT where EMP_ID = emp_mast.emp_id and (IF_SURRENDERED='N' OR IF_SURRENDERED='' OR IF_SURRENDERED is null) limit 1) as quarter_address,"
                    + " (SELECT id_no FROM EMP_ID_DOC where EMP_ID = emp_mast.emp_id AND ID_DESCRIPTION = 'PAN') as pan_number"
                    + " FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC"
                    + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE"
                    + " LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE = G_DISTRICT.DIST_CODE"
                    + " LEFT OUTER JOIN G_BANK ON EMP_MAST.BANK_CODE = G_BANK.BANK_CODE"
                    + " LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE = G_BRANCH.BRANCH_CODE"
                    + " LEFT OUTER JOIN G_MARITAL ON EMP_MAST.m_status = G_MARITAL.int_marital_status_id"
                    + " LEFT OUTER JOIN G_SCHEME ON EMP_MAST.gistype = G_SCHEME.SCHEME_ID"
                    + " LEFT OUTER JOIN g_religion ON EMP_MAST.religion=g_religion.int_religion_id"
                    + " LEFT OUTER JOIN EMP_JOIN ON EMP_MAST.cur_spc = EMP_JOIN.spc"
                    + " LEFT OUTER JOIN (select status_id,emp_id from property_statement_list where "
                    + "financial_year=(select (date_part('year', CURRENT_DATE))-1) :: text)propstlist ON EMP_MAST.emp_id = propstlist.emp_id"
                    + " where emp_mast.emp_id=?";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("initials") != null && !result.getString("initials").equals("")) {
                    fullName = result.getString("initials");
                }
                if (result.getString("F_NAME") != null && !result.getString("F_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("F_NAME");
                }
                if (result.getString("M_NAME") != null && !result.getString("M_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("M_NAME");
                }
                if (result.getString("L_NAME") != null && !result.getString("L_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("L_NAME");
                }
                employee.setEmpid(empid);
                employee.setEmpName(fullName);
                employee.setStaffType(result.getString("staff_type"));
                employee.setStaffCategory(result.getString("staff_category"));
                employee.setStaffPlacement(result.getString("staff_placement"));
                employee.setiDesignation(result.getString("idesignation"));
                employee.setcDesignation(result.getString("cdesignation"));
                employee.setCadreId(result.getString("cadre_name"));
                employee.setRecsource(result.getString("rec_source"));
                employee.setCardeallotmentyear(result.getString("allotment_year"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                employee.setGender(result.getString("GENDER"));
                employee.setMaritalStatus(result.getString("M_STATUS"));
                employee.setMarital(result.getString("int_marital_status_id"));
                employee.setCategory(result.getString("RES_CAT"));
                employee.setHeight(result.getDouble("HEIGHT"));
                employee.setGpfno(result.getString("GPF_NO"));
                employee.setAccttype(result.getString("ACCT_TYPE"));
                employee.setBasic(result.getInt("CUR_BASIC_SALARY"));
                /*message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));*/
                employee.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("DOB")));
                employee.setTxtDos(CommonFunctions.getFormattedOutputDate1(result.getDate("DOS")));
                if (result.getDate("DOB") != null) {
                    int ymd[] = Numtowordconvertion.getDateParts(result.getDate("DOB").toString());
                    employee.setDobText(Numtowordconvertion.getFormattedDOB(ymd[0], ymd[1], ymd[2]));
                }
                if (result.getDate("JOINDATE_OF_GOO") != null) {
                    int yjoindata[] = Numtowordconvertion.getDateParts(result.getDate("JOINDATE_OF_GOO").toString());
                    employee.setJoinDateText(Numtowordconvertion.getFormattedDOB(yjoindata[0], yjoindata[1], yjoindata[2]));
                }

                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(result.getDate("JOINDATE_OF_GOO")));
                employee.setDor(CommonFunctions.getFormattedOutputDate1(result.getDate("DOS")));

                employee.setDepartment(result.getString("DEPARTMENT_NAME"));
                employee.setDeptcode(result.getString("DEPT_CODE"));
                employee.setPost(result.getString("POST"));
                employee.setPostcode(result.getString("GPC"));
                employee.setSpn(result.getString("SPN"));
                employee.setSpc(result.getString("CUR_SPC"));
                employee.setOffice(result.getString("OFF_NAME"));
                employee.setOffice(result.getString("OFF_EN"));
                employee.setDistrict(result.getString("DIST_NAME"));
                employee.setOfficecode(result.getString("CUR_OFF_CODE"));
                employee.setBloodgrp(result.getString("BL_GRP"));
                employee.setReligion(result.getString("RELIGION_ID"));
                employee.setReligionName(result.getString("RELIGION"));
                //employee.setPermanentdist(result.getString("PRM_DIST_CODE"));
                //employee.setPermanentps(result.getString("PRM_PS_CODE"));
                employee.setPhyhandicapt(result.getString("PH_CODE"));
                employee.setIdmark(result.getString("ID_MARK"));
                employee.setMobile(result.getString("MOBILE"));
                //employee.setPrmTelNo(result.getString("PRM_TPHONE"));
                employee.setDateOfCurPosting(CommonFunctions.getFormattedOutputDate1(result.getDate("CURR_POST_DOJ")));
                employee.setGp(result.getInt("GP"));
                employee.setPayScale(result.getString("CUR_SALARY"));
                employee.setCadreCode(result.getString("CUR_CADRE_CODE"));
                employee.setPostGrpType(result.getString("POST_GRP_TYPE"));
                employee.setFieldOffCode(result.getString("FIELD_OFF_CODE"));
                employee.setEmail(result.getString("EMAIL_ID"));
                employee.setHasNoEmail(result.getString("has_no_email"));
                employee.setChest(result.getString("CHEST"));
                employee.setWeight(result.getString("WEIGHT"));
                employee.setLeftvision(result.getString("LEFT_VISION"));
                employee.setRightvision(result.getString("RIGHT_VISION"));
                employee.setBrassno(result.getString("BRASS_NO"));
                employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(result.getDate("DOE_GOV")));
                if (result.getDate("DOE_GOV") != null) {
                    int ydoe[] = Numtowordconvertion.getDateParts(result.getDate("DOE_GOV").toString());
                    employee.setEntryGovDateText(Numtowordconvertion.getFormattedDOB(ydoe[0], ydoe[1], ydoe[2]));
                }
                if (result.getString("doe_gov_time") != null && !result.getString("doe_gov_time").equals("")) {
                    if (result.getString("doe_gov_time").equals("FN")) {
                        employee.setEntryGovDateTime("FORE NOON");
                    } else if (result.getString("doe_gov_time").equals("AN")) {
                        employee.setEntryGovDateTime("AFTER NOON");
                    }
                }
                employee.setHomeTown(result.getString("HOME_TOWN"));
                employee.setIfReservation(result.getString("if_employed_res"));
                employee.setIfRehabiltation(result.getString("if_employed_rehab"));
                employee.setTxtwefTime(result.getString("jointime_of_goo"));
                employee.setTimeOfEntryGoo(result.getString("toe_gov"));
                employee.setGisNo(result.getString("gisno"));
                employee.setGisType(result.getString("gistype"));
                employee.setDomicil(result.getString("domicile"));
                employee.setSltBank(result.getString("bank_code"));
                employee.setSltbranch(result.getString("branch_code"));

                employee.setBankName(result.getString("BANK_NAME"));
                employee.setBranchName(result.getString("branch_name"));
                employee.setIfmsIFSCode(result.getString("ifsc_code"));
                employee.setBankaccno(result.getString("bank_acc_no"));
                employee.setGisName(result.getString("SCHEME_NAME"));
                employee.setMaritalStatus(result.getString("maritalstatus"));
                employee.setIfprofileCompleted(result.getString("if_profile_completed"));
                employee.setIfprofileVerified(result.getString("if_profile_verified"));
                employee.setPostGrpType(result.getString("post_grp_type"));
                employee.setRadyear1(result.getString("dos_age"));
                employee.setSltGPFAssmued(result.getString("if_gpf_assumed"));
                employee.setCell(result.getString("matrix_cell"));
                employee.setLevel(result.getString("matrix_level"));
                employee.setPscYear(result.getString("pscyear"));
                employee.setQuarterAddress(StringUtils.defaultString(result.getString("quarter_no")) + "\n" + StringUtils.defaultString(result.getString("quarter_address")));
                employee.setEmpCadre(result.getString("cadre_name"));
                employee.setDateofjoining(result.getString("join_date"));
                if (result.getString("join_date") != null && !result.getString("join_date").equals("")) {
                    employee.setDateofjoining(result.getString("join_date"));

                }

                employee.setProperty_submitted_if_any(result.getString("status_id"));
                return employee;
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getEmployeecurrentProfile(String empid) {
        Employee employee = new Employee();
        String fullName = "";
        employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select JOIN_DATE,cur_spc,cur_off_code,off_en from \n"
                    + "emp_mast left outer join emp_join on emp_mast.cur_off_code=auth_off and cur_spc=emp_join.spc\n"
                    + "LEFT JOIN G_OFFICE ON G_OFFICE.OFF_CODE=emp_join.AUTH_OFF\n"
                    + "where emp_mast.emp_id=?";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setCurrentJoindate(CommonFunctions.getFormattedOutputDate1(result.getDate("join_date")));
                employee.setCurrentJoinPlace(result.getString("off_en"));
                return employee;
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getEmployeeAbstractReport(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT emp_mast.emp_id,emp_pay_record.not_id,emp_join.join_id,GPF_NO,DOB,DOE_GOV,DOS,rec_source,getpostnamefromspc(EMP_JOIN.spc)initialpost,emp_pay_record.pay_scale,emp_pay_record.pay_scale,emp_pay_record.wef,\n"
                    + "EMP_JOIN.spc,rec_source,GPF_NO,ACCT_TYPE,array_to_string(array[initials,f_name,m_name,l_name],' ')empname,designation,\n"
                    + "EMP_MAST.RES_CAT,CUR_BASIC_SALARY,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,BL_GRP,emp_mast.RELIGION RELIGION_ID, MOBILE, CURR_POST_DOJ,\n"
                    + "EMAIL_ID,DOE_GOV,HOME_TOWN,if_employed_res,if_employed_rehab,jointime_of_goo,toe_gov,domicile,dos_age,doe_gov_time,G_SPC.SPN FROM EMP_MAST \n"
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC \n"
                    + "LEFT OUTER JOIN EMP_JOIN ON EMP_JOIN.EMP_ID=EMP_MAST.EMP_ID AND EMP_JOIN.not_type ='FIRST_APPOINTMENT'\n"
                    + "LEFT OUTER JOIN emp_pay_record ON  emp_pay_record.not_id=EMP_JOIN.not_id \n"
                    + "where emp_mast.emp_id=? ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setEmpName(result.getString("empname"));
                employee.setSpn(result.getString("SPN"));
                employee.setGpfno(result.getString("GPF_NO"));
                employee.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("DOB")));
                employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(result.getDate("DOE_GOV")));
                employee.setDor(CommonFunctions.getFormattedOutputDate1(result.getDate("DOS")));
                employee.setRecsource(result.getString("rec_source"));
                employee.setInitialpost(result.getString("initialpost"));
                employee.setPayScale(result.getString("pay_scale"));
                employee.setTxtwefTime(CommonFunctions.getFormattedOutputDate1(result.getDate("wef")));
                String payscaleon31122005 = getPayscaleAbstractReport(empid);
                employee.setPayscaleon31122005(payscaleon31122005);

                String payscaleon112006 = getPayscaleAbstractReportason1(empid);
                employee.setPayscaleon112006(payscaleon112006);
                String acp = getAbstractReportacp(empid);
                employee.setAcp(acp);

                String Racp = getAbstractReportRacp(empid);
                if (Racp != null && !Racp.equals("")) {
                    employee.setRracp(Racp);
                } else {
                    employee.setRracp("Not Available");
                }

                String Pay = getAbstractReportPay(empid);
                employee.setPayment(Pay);

                String Payfixed = getAbstractReportPayfixed(empid);
                employee.setPayfixed(Payfixed);

                return employee;
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getPayfixationunderMacp(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "select pay,* from emp_pay_record where emp_id=? and \n"
                    + "(increment_reason not like '%MACP%' or increment_reason is null or increment_reason='' )and not_type='PAYFIXATION' \n"
                    + "and wef=(select max(wef) from emp_pay_record where \n"
                    + "(increment_reason not like '%MACP%' or increment_reason is null or increment_reason='' ) and emp_id=? and  ((extract(year from wef)< (select extract(year from MIN(WEF))from \n"
                    + "emp_pay_record where increment_reason like '%MACP%' and emp_id=?)) or \n"
                    + "((extract(year from wef)= (select extract(year from MIN(WEF))from \n"
                    + "emp_pay_record where increment_reason like '%MACP%' and emp_id=?)) and \n"
                    + " (extract(month from wef)< (select extract(month from MIN(WEF))from \n"
                    + "emp_pay_record where increment_reason like '%MACP%' and emp_id=?)))) and not_type='PAYFIXATION')";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            statement.setString(3, empid);
            statement.setString(4, empid);
            statement.setString(5, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setPay(result.getDouble("pay"));
                System.out.println("result.getString(\"pay\")result.getString(\"pay\")result.getString(\"pay\")" + result.getString("pay"));
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getPayfixationafterMacp(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "select MAX(pay_level) as levels,MAX(pay) as pay from emp_pay_record where  not_type='PAYFIXATION' AND EMP_ID=? \n"
                    + "and increment_reason like '%MACP%'  and (pay_level is not null and pay_level <> '')";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setPayAfter(result.getString("pay"));
                employee.setLevel(result.getString("levels"));
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getPayrevisionDate(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT entered_date FROM PAY_REVISION_OPTION WHERE EMP_iD=?";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setRevisedate(CommonFunctions.getFormattedOutputDate1(result.getDate("entered_date")));
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getPayfixationDni(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT max(doni) FROM emp_pay WHERE EMP_iD=? and not_type='PAYFIXATION'";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setFixationDni(result.getString("doni"));
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getEmployeeServiceParticular(String empid) {
        Employee employee = null;
        String fullName = "";
        //employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT emp_mast.emp_id,emp_mast.dos,gpf_no,distnm,array_to_string(array[emp_mast.initials,emp_mast.f_name,emp_mast.m_name,emp_mast.l_name],' ')empname,\n"
                    + "array_to_string(array[EMP_RELATION.initials,EMP_RELATION.f_name,EMP_RELATION.m_name,EMP_RELATION.l_name],' ')fathername,emp_mast.category,\n"
                    + "equalification.qualification,EMP_MAST.RES_CAT,\n"
                    + "CUR_BASIC_SALARY,emp_mast.DOB,emp_mast.RELIGION RELIGION_ID, emp_mast.MOBILE, CURR_POST_DOJ, EMAIL_ID,DOE_GOV,HOME_TOWN,if_employed_res,\n"
                    + "if_employed_rehab,jointime_of_goo,toe_gov,domicile,dos_age,doe_gov_time,postingunintdoj,\n"
                    + "place_posting,passing_training_date,mentalfitness,physicalfitness,periodparticularstraining,powerofdecesion,\n"
                    + "honestyintegrity,remarksnominatingprofessional,\n"
                    + "punishmentmajor,punishmentmajorprior,punishmentmajorduring,punishmentmajorfrom,punishmentminor,punishmentminorprior,\n"
                    + "punishmentminorduring,punishmentminorfrom,property_submitted_if_any,property_submitted_on_hrms_byofficer,present_rank,place_posting,date_of_posting\n"
                    + "FROM EMP_MAST \n"
                    + "LEFT OUTER JOIN (select * from EMP_RELATION  where emp_id=? AND EMP_RELATION.RELATION='FATHER' limit 1 )EMP_RELATION \n"
                    + "ON EMP_RELATION.EMP_ID=EMP_MAST.EMP_ID left outer join(select * from emp_qualification where emp_id=? order by yop desc limit 1)equalification on equalification.emp_id=emp_mast.emp_id\n"
                    + "left outer join  (select * from police_nomination_form where empid=? limit 1)pnf on pnf.empid=emp_mast.emp_id\n"
                    + "left outer join  (select emp_id,getdistname(dist_code)distnm,address_type from emp_address  where emp_id=? and address_type='PERMANENT' LIMIT 1)empads \n"
                    + "on empads.emp_id=emp_mast.emp_id where EMP_MAST.emp_id=?";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            statement.setString(3, empid);
            statement.setString(4, empid);
            statement.setString(5, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee = new Employee();
                employee.setEmpid(empid);
                employee.setEmpName(result.getString("empname"));
                employee.setFathername(result.getString("fathername"));
                employee.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("DOB")));
                employee.setDos(CommonFunctions.getFormattedOutputDate1(result.getDate("dos")));
                employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(result.getDate("DOE_GOV")));
                employee.setCategory(result.getString("category"));
                employee.setQualification(result.getString("qualification"));
                employee.setDistName(result.getString("distnm"));
                employee.setPlaceposting(result.getString("place_posting"));
                employee.setPostingunitdoj(CommonFunctions.getFormattedOutputDate1(result.getDate("postingunintdoj")));
                employee.setPeriodparticularstraining(result.getString("periodparticularstraining"));
                employee.setPowerofdecesion(result.getString("powerofdecesion"));
                employee.setPhysicalfitness(result.getString("physicalfitness"));
                employee.setMentalfitness(result.getString("mentalfitness"));
                employee.setHonestyintegrity(result.getString("honestyintegrity"));
                employee.setRemarksnominatingprofessional(result.getString("remarksnominatingprofessional"));
                employee.setGpfno(result.getString("gpf_no"));
                employee.setProperty_submitted_if_any(result.getString("property_submitted_if_any"));
                employee.setProperty_submitted_on_hrms_byofficer(CommonFunctions.getFormattedOutputDate1(result.getDate("property_submitted_on_hrms_byofficer")));
                employee.setPresent_rank(result.getString("present_rank"));
                employee.setPlaceOFposting(result.getString("place_posting"));
                return employee;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    public String getPayscaleAbstractReport(String empid) {
        String payscaleon31122005 = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select pay_scale from emp_pay_record where emp_id=? and extract(year from wef)<=2005 "
                    + "and pay=(select max(pay) from emp_pay_record where emp_id=? and extract(year from wef)=2005)";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                payscaleon31122005 = result.getString("pay_scale");

            } else {
                payscaleon31122005 = "NA";
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return payscaleon31122005;
    }

    public String getPayscaleAbstractReportason1(String empid) {
        String payscaleon112006 = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select pay_scale from emp_pay_record where emp_id=? and (extract(year from wef)<2006 or"
                    + "(extract(year from wef)=2006 and extract(month from wef)=1))"
                    + "and pay=(select max(pay) from emp_pay_record where emp_id=? and "
                    + "(extract(year from wef)<2006 or(extract(year from wef)=2006 and extract(month from wef)=1))) "
                    + "order by pay_id desc limit 1";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                payscaleon112006 = result.getString("pay_scale");

            } else {
                payscaleon112006 = "NA";
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return payscaleon112006;
    }

    public String getAbstractReportacp(String empid) {
        String acp = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select max(wef) acp_date from emp_pay_record where increment_reason like 'ACP' and emp_id=? ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            //statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("acp_date") != null && !result.getString("acp_date").equals("")) {
                    acp = CommonFunctions.getFormattedOutputDate1(result.getDate("acp_date"));
                } else {
                    acp = "NA";
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return acp;
    }

    public String getAbstractReportRacp(String empid) {
        String Racp = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select wef  from emp_pay_record where increment_reason in ('2nd RACP','3rd RACP')  and emp_id=? "
                    + "and wef=(select max(wef) from emp_pay_record where increment_reason in ('2nd RACP','3rd RACP') and emp_id=?) ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("wef") != null && !result.getString("wef").equals("")) {
                    Racp = CommonFunctions.getFormattedOutputDate1(result.getDate("wef"));
                    System.out.println("RacpRacp" + Racp);
                } else {
                    Racp = "NA";
                }
            } else {
                Racp = "NO RACP";
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return Racp;
    }

    public String getAbstractReportPay(String empid) {
        String Pay = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select pay from emp_pay_record where emp_id=? and extract(year from wef)<=2015 "
                    + "and pay=(select max(pay) from emp_pay_record where emp_id=? and extract(year from wef)=2015) ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                Pay = result.getString("pay");

            } else {
                Pay = "NA";
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return Pay;
    }

    public String getAbstractReportPayfixed(String empid) {
        String Payfixed = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select pay,wef from emp_pay_record where not_type = 'PAYFIXATION' and extract(year from wef)>=2016 and emp_id=? ORDER BY wef desc ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                Payfixed = result.getString("pay");

            } else {
                Payfixed = "NA";
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return Payfixed;
    }
    /* promotion Tabl */

    public String getAbstractReportpromotion(String empid) {
        String emppromotion = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();

            String SQL = "select emp_promotion.not_id,join_date,spn from"
                    + "(select * from emp_notification where not_type = 'PROMOTION' AND EMP_ID=? AND IF_VISIBLE='Y') emp_notification"
                    + "inner join emp_promotion on emp_notification.not_id=emp_promotion.not_id"
                    + "inner join emp_join on emp_notification.not_id=emp_join.not_id"
                    + "inner join g_spc on emp_join.spc=g_spc.spc ";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                emppromotion = result.getString("spn");

            } else {
                emppromotion = "NA";
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return emppromotion;
    }

    @Override
    public String getEmployeeImage(String empid, String filePath
    ) {
        String base64Image = null;
        try {
            int BUFFER_LENGTH = 4096;
            String filename = empid + ".jpg";
            File file = new File(filePath + filename);
            if (file.exists()) {
                base64Image = Base64.encodeBase64URLSafeString(FileUtils.readFileToByteArray(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    @Override
    public byte[] getEmployeeFirstPage(String empid
    ) {
        byte[] sbscannedFile = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement("SELECT * FROM EMP_PHOTO WHERE EMP_ID=? AND PH_TYPE='S'");
            statement.setString(1, empid);
            result = statement.executeQuery();
            String fileName = null;
            if (result.next()) {
                fileName = result.getString("filename");

            }
            if (fileName != null) {
                File file = new File(sbfilepath + fileName);
                if (file.exists()) {
                    sbscannedFile = FileUtils.readFileToByteArray(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return sbscannedFile;
    }

    @Override
    public Employee getEmployee(String spc) {
        Employee employee = new Employee();
        employee.setSpc(spc);

        /*String SQL = "SELECT EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,EMP_MAST.CATEGORY,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC, G_SPC.SPN,DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,OFF_NAME,BL_GRP,RELIGION,PRM_DIST_CODE,PRM_PS_CODE,PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, EMP_MAST.GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID FROM "
         + "(SELECT EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,CATEGORY,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,BL_GRP,RELIGION,PRM_DIST_CODE,PRM_PS_CODE,PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID FROM EMP_MAST WHERE CUR_SPC=?)EMP_MAST "
         + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
         + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
         + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
         + "LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE ";*/
        String SQL = "SELECT EMP_MAST.EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,EMP_MAST.CATEGORY,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC, G_SPC.SPN,DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,OFF_NAME,BL_GRP,RELIGION,PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, EMP_MAST.GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID,EMP_ADDRESS.DIST_CODE PRM_DIST_CODE,EMP_ADDRESS.ps_code PRM_PS_CODE FROM "
                + "(SELECT EMP_ID,GPF_NO,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,CATEGORY,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,BL_GRP,RELIGION,PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID FROM EMP_MAST WHERE CUR_SPC=?)EMP_MAST "
                + "LEFT OUTER JOIN (SELECT EMP_ID,dist_code,ps_code FROM EMP_ADDRESS WHERE address_type='PERMANENT')EMP_ADDRESS ON EMP_MAST.EMP_ID=EMP_ADDRESS.EMP_ID "
                + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE "
                + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                + "LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE ";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, spc);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setEmpid(result.getString("EMP_ID"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                employee.setGender(result.getString("GENDER"));
                employee.setMarital(result.getString("M_STATUS"));
                employee.setCategory(result.getString("CATEGORY"));
                employee.setHeight(result.getDouble("HEIGHT"));
                employee.setGpfno(result.getString("GPF_NO"));
                employee.setBasic(result.getInt("CUR_BASIC_SALARY"));
                employee.setDob(formatDate(result.getDate("DOB")));
                employee.setJoindategoo(formatDate(result.getDate("JOINDATE_OF_GOO")));
                employee.setDor(formatDate(result.getDate("DOS")));
                employee.setDepartment(result.getString("DEPARTMENT_NAME"));
                employee.setDeptcode(result.getString("DEPT_CODE"));
                employee.setPost(result.getString("POST"));
                employee.setPostcode(result.getString("GPC"));
                employee.setSpn(result.getString("SPN"));
                employee.setSpc(result.getString("CUR_SPC"));
                employee.setOffice(result.getString("OFF_NAME"));
                employee.setOfficecode(result.getString("CUR_OFF_CODE"));
                employee.setBloodgrp(result.getString("BL_GRP"));
                employee.setReligion(result.getString("RELIGION"));
                employee.setPermanentdist(result.getString("PRM_DIST_CODE"));
                employee.setPermanentps(result.getString("PRM_PS_CODE"));
                employee.setPhyhandicapt(result.getString("PH_CODE"));
                employee.setIdmark(result.getString("ID_MARK"));
                employee.setMobile(result.getString("MOBILE"));
                //employee.setPrmTelNo(result.getString("PRM_TPHONE"));
                employee.setDateOfCurPosting(formatDate(result.getDate("CURR_POST_DOJ")));
                employee.setGp(result.getInt("GP"));
                employee.setPayScale(result.getString("CUR_SALARY"));
                employee.setCadreCode(result.getString("CUR_CADRE_CODE"));
                employee.setPostGrpType(result.getString("POST_GRP_TYPE"));
                employee.setFieldOffCode(result.getString("FIELD_OFF_CODE"));
                employee.setEmail(result.getString("EMAIL_ID"));
            }
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Pensioner getPensionerDetailsThroughAccNo(String gpfno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pensioner p = new Pensioner();
        String sql = "";
        String empid = "";
        try {
            con = dataSource.getConnection();

            String sql1 = "select emp_id from emp_mast where gpf_no=?";
            ps = con.prepareStatement(sql1);
            ps.setString(1, gpfno);
            rs = ps.executeQuery();
            if (rs.next()) {
                empid = rs.getString("emp_id");
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            sql = "SELECT emp.EMP_ID,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,GENDER,EMP.M_STATUS,int_marital_status_id,EMP.CATEGORY,CUR_BASIC_SALARY,HEIGHT,"
                    + "         DOB,EXTRACT(YEAR from AGE(NOW(), dob)) as age,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,BL_GRP,EMP.RELIGION,int_religion_id, "
                    + "         PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, EMP.GP, CUR_SALARY,ppay, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID, "
                    + "         CHEST, WEIGHT, LEFT_VISION, RIGHT_VISION, BRASS_NO,bank_acc_no,emp.branch_code,branch_name,ifsc_code,OFF_EN,GOFFICE.off_address,GOFFICE.DEPARTMENT_CODE,department_name,dept_abbr,GOFFICE.DIST_CODE,INT_DISTRICT_ID,GOFFICE.TR_CODE,INT_TREASURY_ID,"
                    + "         getVillageName(permanent.vill_code) prm_vill_code,getBlockName(permanent.bl_code) prm_bl_code,getPoliceStationName(permanent.ps_code) prm_ps,permanent.pin prm_pin,getTreasuryDistCode(permanent.dist_code) prm_dist,permanent.state_code prm_state_code,getVillageName(present.vill_code) res_vill_code ,getBlockName(present.bl_code) res_bl_code ,getPoliceStationName(present.ps_code) res_ps_code ,"
                    + "         present.pin res_pin,getTreasuryDistCode(present.dist_code) res_dist_code,present.state_code res_state_code, "
                    + "         GOFFICE.DDO_CODE,GOFFICE.int_ddo_id,DDO_SPC,GPC,POST FROM EMP_MAST EMP "
                    + "         LEFT OUTER JOIN G_OFFICE GOFFICE ON EMP.CUR_OFF_CODE=GOFFICE.OFF_CODE "
                    + "		LEFT OUTER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
                    + "		LEFT OUTER JOIN G_POST GPOST ON GSPC.GPC = GPOST.POST_CODE "
                    + "		LEFT OUTER JOIN G_DEPARTMENT DEPT ON GOFFICE.DEPARTMENT_CODE=DEPT.DEPARTMENT_CODE "
                    + "         LEFT OUTER JOIN g_branch gbranch ON emp.branch_code=gbranch.branch_code"
                    + "         LEFT OUTER JOIN g_district gdistrict ON GOFFICE.dist_code=gdistrict.dist_code"
                    + "         LEFT OUTER JOIN g_marital gmarital ON EMP.M_STATUS=gmarital.m_status"
                    + "         LEFT OUTER JOIN g_religion greligion ON EMP.religion=greligion.religion"
                    + "         LEFT OUTER JOIN g_treasury gtreasury ON GOFFICE.TR_CODE=gtreasury.tr_code"
                    + "         LEFT OUTER JOIN (select * from emp_address where emp_id='" + empid + "' and address_type='PERMANENT')permanent on emp.emp_id=permanent.emp_id"
                    + "         LEFT OUTER JOIN (select * from emp_address where emp_id='" + empid + "' and address_type='PRESENT')present on emp.emp_id=present.emp_id"
                    + "         WHERE emp.GPF_NO=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, gpfno);
            rs = ps.executeQuery();
            if (rs.next()) {
                String accno = rs.getString("gpf_no").replaceAll("[^0-9]", "");
                String gpfseries = rs.getString("gpf_no").replaceAll("[^A-Z ]", "");
                p.setGpfseries(gpfseries);
                p.setGpfno(accno);

                p.setEmpName(rs.getString("EMPNAME"));
                p.setHrmsEmpId(rs.getString("EMP_ID"));
                p.setDob(formatDate(rs.getDate("DOB")));
                p.setDor(formatDate(rs.getDate("DOS")));
                p.setGender(rs.getString("GENDER"));
                p.setMaritalStatus(rs.getString("int_marital_status_id"));
                p.setReligion(rs.getString("int_religion_id"));
                p.setNationality("1");
                p.setMobileno(rs.getString("MOBILE"));
                p.setEmailId(rs.getString("EMAIL_ID"));
                p.setIdmark(rs.getString("ID_MARK"));
                p.setHeight(rs.getString("HEIGHT"));
                p.setIfscCode(rs.getString("ifsc_code"));
                p.setBranchname(rs.getString("branch_name"));
                p.setBankAccNo(rs.getString("bank_acc_no"));

                p.setPayableTreasuryCode(rs.getString("INT_TREASURY_ID"));
                p.setDdoCode(rs.getString("int_ddo_id"));
                p.setDistrictName(rs.getString("INT_DISTRICT_ID"));
                p.setPensionerDesignation(StringUtils.replace(rs.getString("post"), "&", "AND"));
                p.setDeptcode(rs.getString("dept_abbr"));
                p.setDepartmentName(rs.getString("department_name"));
                p.setOfficeName(StringUtils.replace(rs.getString("OFF_EN"), "&", "AND"));
                p.setOffAddress(rs.getString("off_address"));
                p.setAgeonNextDOB((rs.getInt("age") + 1) + "");
                p.setPostGrpType(rs.getString("POST_GRP_TYPE"));

                p.setPermanentCity(rs.getString("prm_bl_code"));
                p.setPermanentTown(rs.getString("prm_vill_code"));
                p.setPermanentps(rs.getString("prm_ps"));
                p.setPermanentdistName(rs.getString("prm_dist"));
                p.setPermanentAddressPin(rs.getString("prm_pin"));
                p.setPermanentState(rs.getString("prm_state_code"));

                p.setPresentCity(rs.getString("res_bl_code"));
                p.setPresentTown(rs.getString("res_vill_code"));
                p.setPresentps(rs.getString("res_ps_code"));
                p.setPresentdistName(rs.getString("res_dist_code"));
                p.setPresentAddressPin(rs.getString("res_pin"));
                p.setPresentState(rs.getString("res_state_code"));

                p.setDateOfAppointment(formatDate(rs.getDate("JOINDATE_OF_GOO")));
                p.setBasic(rs.getString("CUR_BASIC_SALARY"));
                p.setGp(rs.getString("gp"));
                p.setPayScale(rs.getString("CUR_SALARY"));
                p.setPersonalPay(rs.getString("ppay"));

            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT date_of_deceased,deceased_time FROM emp_deceased WHERE emp_id=?");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setDeceasedDate(formatDate(rs.getDate("date_of_deceased")));
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT id_description,id_no,id_doi,id_doe,id_poi FROM emp_id_doc WHERE EMP_ID=? AND id_description='PAN' ");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("id_description").equalsIgnoreCase("PAN")) {
                    p.setPanNo(rs.getString("id_no"));
                }
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT ret_type FROM emp_ret_res WHERE emp_id=?");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setRetirementType(rs.getString("ret_type"));
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("select aqmast.aqsl_no,ad_amt,ad_code from (select aqsl_no from aq_mast aqmast where emp_code=? order by aq_year,aq_month desc limit 1) aqmast "
                    + "			     inner join aq_dtls dtls on aqmast.aqsl_no=dtls.aqsl_no where ad_code='DA'");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setDa(rs.getString("ad_amt"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return p;
    }

    @Override
    public IdentityInfo[] getEmployeeIdInformation(String empid) {
        List<IdentityInfo> idlist = new ArrayList<>();
        String SQL = "SELECT id_description,id_no,id_doi,id_doe,id_poi FROM emp_id_doc WHERE EMP_ID=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                IdentityInfo id = new IdentityInfo();
                id.setIdentityDocNo(result.getString("id_no"));
                id.setIdentityDesc(result.getString("id_description"));
                id.setIssueDate(formatDate(result.getDate("ID_DOI")));
                id.setExpiryDate(formatDate(result.getDate("ID_DOE")));
                id.setPlaceOfIssue(result.getString("ID_POI"));
                if (result.getString("id_description") != null && !result.getString("id_description").equals("")) {
                    if (result.getString("id_description").equalsIgnoreCase("PAN")) {
                        id.setIdentityDocType("1");
                        idlist.add(id);
                    } else if (result.getString("id_description").equalsIgnoreCase("ELECTION/VOTER ID CARD")) {
                        id.setIdentityDocType("2");
                        idlist.add(id);
                    } else if (result.getString("id_description").equalsIgnoreCase("DRIVING LICENCE")) {
                        id.setIdentityDocType("3");
                        idlist.add(id);
                    } else if (result.getString("id_description").equalsIgnoreCase("PASSPORT")) {
                        id.setIdentityDocType("4");
                        idlist.add(id);
                    } else if (result.getString("id_description").equalsIgnoreCase("AADHAAR")) {
                        id.setIdentityDocType("5");
                        idlist.add(id);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        IdentityInfo idarray[] = idlist.toArray(new IdentityInfo[idlist.size()]);
        return idarray;
    }

    @Override
    public Pensioner getPensionerDetailsThroughHRMSID(String empid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pensioner p = new Pensioner();
        String sql = "";
        try {
            con = dataSource.getConnection();
            sql = "SELECT emp.EMP_ID,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,GENDER,EMP.M_STATUS,int_marital_status_id,EMP.CATEGORY,CUR_BASIC_SALARY,HEIGHT,"
                    + "         DOB,EXTRACT(YEAR from AGE(NOW(), dob)) as age,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,BL_GRP,EMP.RELIGION,int_religion_id, "
                    + "         PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, EMP.GP, CUR_SALARY,ppay, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID, "
                    + "         CHEST, WEIGHT, LEFT_VISION, RIGHT_VISION, BRASS_NO,bank_acc_no,emp.branch_code,branch_name,ifsc_code,OFF_EN,GOFFICE.off_address,GOFFICE.DEPARTMENT_CODE,department_name,dept_abbr,GOFFICE.DIST_CODE,INT_DISTRICT_ID,GOFFICE.TR_CODE,INT_TREASURY_ID,"
                    + "         getVillageName(permanent.vill_code) prm_vill_code,getBlockName(permanent.bl_code) prm_bl_code,getPoliceStationName(permanent.ps_code) prm_ps,permanent.pin prm_pin,getTreasuryDistCode(permanent.dist_code) prm_dist,permanent.state_code prm_state_code,getVillageName(present.vill_code) res_vill_code ,getBlockName(present.bl_code) res_bl_code ,getPoliceStationName(present.ps_code) res_ps_code ,"
                    + "         present.pin res_pin,getTreasuryDistCode(present.dist_code) res_dist_code,present.state_code res_state_code, "
                    + "         GOFFICE.DDO_CODE,GOFFICE.int_ddo_id,DDO_SPC,GPC,POST FROM EMP_MAST EMP "
                    + "         LEFT OUTER JOIN G_OFFICE GOFFICE ON EMP.CUR_OFF_CODE=GOFFICE.OFF_CODE "
                    + "		LEFT OUTER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
                    + "		LEFT OUTER JOIN G_POST GPOST ON GSPC.GPC = GPOST.POST_CODE "
                    + "		LEFT OUTER JOIN G_DEPARTMENT DEPT ON GOFFICE.DEPARTMENT_CODE=DEPT.DEPARTMENT_CODE "
                    + "         LEFT OUTER JOIN g_branch gbranch ON emp.branch_code=gbranch.branch_code"
                    + "         LEFT OUTER JOIN g_district gdistrict ON GOFFICE.dist_code=gdistrict.dist_code"
                    + "         LEFT OUTER JOIN g_marital gmarital ON EMP.M_STATUS=gmarital.m_status"
                    + "         LEFT OUTER JOIN g_religion greligion ON EMP.religion=greligion.religion"
                    + "         LEFT OUTER JOIN g_treasury gtreasury ON GOFFICE.TR_CODE=gtreasury.tr_code"
                    + "         LEFT OUTER JOIN (select * from emp_address where emp_id=? and address_type='PERMANENT')permanent on emp.emp_id=permanent.emp_id"
                    + "         LEFT OUTER JOIN (select * from emp_address where emp_id=? and address_type='PRESENT')present on emp.emp_id=present.emp_id"
                    + "         WHERE emp.EMP_ID=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, empid);
            ps.setString(2, empid);
            ps.setString(3, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                String accno = rs.getString("gpf_no").replaceAll("[^0-9]", "");
                String gpfseries = rs.getString("gpf_no").replaceAll("[^A-Z ]", "");
                p.setGpfseries(gpfseries);
                p.setGpfno(accno);

                p.setEmpName(rs.getString("EMPNAME"));
                p.setHrmsEmpId(rs.getString("EMP_ID"));
                p.setDob(formatDate(rs.getDate("DOB")));
                p.setDor(formatDate(rs.getDate("DOS")));
                p.setGender(rs.getString("GENDER"));
                p.setMaritalStatus(rs.getString("int_marital_status_id"));
                p.setReligion(rs.getString("int_religion_id"));
                p.setNationality("1");
                p.setMobileno(rs.getString("MOBILE"));
                p.setEmailId(rs.getString("EMAIL_ID"));
                p.setIdmark(rs.getString("ID_MARK"));
                p.setHeight(rs.getString("HEIGHT"));
                p.setIfscCode(rs.getString("ifsc_code"));
                p.setBranchname(rs.getString("branch_name"));
                p.setBankAccNo(rs.getString("bank_acc_no"));

                p.setPayableTreasuryCode(rs.getString("INT_TREASURY_ID"));
                p.setDdoCode(rs.getString("int_ddo_id"));
                p.setDistrictName(rs.getString("INT_DISTRICT_ID"));
                p.setPensionerDesignation(StringUtils.replace(rs.getString("post"), "&", "AND"));
                p.setDeptcode(rs.getString("dept_abbr"));
                p.setDepartmentName(rs.getString("department_name"));
                p.setOfficeName(StringUtils.replace(rs.getString("OFF_EN"), "&", "AND"));
                p.setOffAddress(rs.getString("off_address"));
                p.setAgeonNextDOB((rs.getInt("age") + 1) + "");
                p.setPostGrpType(rs.getString("POST_GRP_TYPE"));

                p.setPermanentCity(rs.getString("prm_bl_code"));
                p.setPermanentTown(rs.getString("prm_vill_code"));
                p.setPermanentps(rs.getString("prm_ps"));
                p.setPermanentdistName(rs.getString("prm_dist"));
                p.setPermanentAddressPin(rs.getString("prm_pin"));
                p.setPermanentState(rs.getString("prm_state_code"));

                p.setPresentCity(rs.getString("res_bl_code"));
                p.setPresentTown(rs.getString("res_vill_code"));
                p.setPresentps(rs.getString("res_ps_code"));
                p.setPresentdistName(rs.getString("res_dist_code"));
                p.setPresentAddressPin(rs.getString("res_pin"));
                p.setPresentState(rs.getString("res_state_code"));

                p.setDateOfAppointment(formatDate(rs.getDate("JOINDATE_OF_GOO")));
                p.setBasic(rs.getString("CUR_BASIC_SALARY"));
                p.setGp(rs.getString("gp"));
                p.setPayScale(rs.getString("CUR_SALARY"));
                p.setPersonalPay(rs.getString("ppay"));

            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT date_of_deceased,deceased_time FROM emp_deceased WHERE emp_id=?");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setDeceasedDate(formatDate(rs.getDate("date_of_deceased")));
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT id_description,id_no,id_doi,id_doe,id_poi FROM emp_id_doc WHERE EMP_ID=? AND id_description='PAN' ");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("id_description").equalsIgnoreCase("PAN")) {
                    p.setPanNo(rs.getString("id_no"));
                }
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("SELECT ret_type FROM emp_ret_res WHERE emp_id=?");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setRetirementType(rs.getString("ret_type"));
            }

            DataBaseFunctions.closeSqlObjects(rs);
            ps = con.prepareStatement("select aqmast.aqsl_no,ad_amt,ad_code from (select aqsl_no from aq_mast aqmast where emp_code=? order by aq_year,aq_month desc limit 1) aqmast "
                    + "			     inner join aq_dtls dtls on aqmast.aqsl_no=dtls.aqsl_no where ad_code='DA'");
            ps.setString(1, p.getHrmsEmpId());
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setDa(rs.getString("ad_amt"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return p;
    }

    @Override
    public ArrayList getOfficeWiseBillGroupWiseEmployeeList(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String description = null;
        try {
            con = dataSource.getConnection();
            ps1 = con.prepareStatement("SELECT distinct GS.section_name, GS.section_id, BM.bill_group_id, GS.bill_type,GS.OFF_CODE,GO.OFF_EN,BM.description "
                    + "FROM bill_group_master BM \n"
                    + "INNER JOIN bill_section_mapping BSM ON BM.bill_group_id = BSM.bill_group_id\n"
                    + "INNER JOIN g_section GS ON BSM.section_id = GS.section_id\n"
                    + "LEFT OUTER JOIN (SELECT * FROM G_OFFICE WHERE LVL='20')GO ON GO.OFF_CODE=GS.OFF_CODE\n"
                    + "WHERE BM.off_code = ? AND BM.description IS NOT NULL AND BM.description <> '' ORDER BY GO.OFF_EN desc,BM.description  ");
            ps1.setString(1, offCode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                list = new ArrayList();
                int cnt = 0;
                String billGroupId = rs1.getString("section_id");
                String offname = rs1.getString("off_en");
                if (rs1.getString("off_en") != null && !rs1.getString("off_en").equals("")) {
                    description = "<span style='font-size:12pt;color:#171717'>Bill Group: " + rs1.getString("description") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Section: " + rs1.getString("section_name") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#171717'>Bill Type: " + rs1.getString("bill_type") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Office Name: " + offname + "</span>";
                } else {
                    description = "<span style='font-size:12pt;color:#171717'>Bill Group: " + rs1.getString("description") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Section: " + rs1.getString("section_name") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#171717'>Bill Type: " + rs1.getString("bill_type") + "</span>";

                }

                ps = con.prepareStatement("SELECT EM.pay_commission,EM.EMAIL_ID,EM.MOBILE,EM.POST_GRP_TYPE,EM.GENDER,EM.EMP_ID,EM.ACCT_TYPE,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EM.GP,G_CADRE.CADRE_NAME,CUR_CADRE_GRADE"
                        + ",DOB,DOS,joindate_of_goo,home_town,POST,SPM.POST_SL_NO,EM.is_regular,EM.CATEGORY FROM bill_section_mapping BM"
                        + " INNER JOIN section_post_mapping SPM ON SPM.section_id = BM.section_id"
                        + " INNER JOIN emp_mast EM ON EM.cur_spc = SPM.spc"
                        + " LEFT OUTER JOIN G_CADRE ON EM.CUR_CADRE_CODE = G_CADRE.CADRE_CODE "
                        + " LEFT OUTER JOIN G_SPC ON EM.CUR_SPC = G_SPC.SPC "
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE"
                        + " WHERE BM.section_id = ? order by SPM.POST_SL_NO");
                ps.setBigDecimal(1, new BigDecimal(billGroupId));
                rs = ps.executeQuery();
                while (rs.next()) {
                    cnt++;
                    Employee employee = new Employee();
                    employee.setEmpid(rs.getString("EMP_ID"));
                    employee.setFname(rs.getString("F_NAME"));
                    employee.setMname(rs.getString("M_NAME"));
                    employee.setLname(rs.getString("L_NAME"));
                    if (rs.getString("GENDER") != null && !rs.getString("GENDER").equals("")) {
                        if (rs.getString("GENDER").equals("M")) {
                            employee.setGender("MALE");
                        } else if (rs.getString("GENDER").equals("F")) {
                            employee.setGender("FEMALE");
                        }
                    }
                    employee.setGpfno(rs.getString("GPF_NO"));
                    employee.setPost(rs.getString("POST"));
                    employee.setCadreCode(rs.getString("CADRE_NAME"));
                    employee.setCadreGrade(rs.getString("CUR_CADRE_GRADE"));
                    employee.setDob(formatDate(rs.getDate("DOB")));
                    employee.setDor(formatDate(rs.getDate("DOS")));
                    employee.setJoindategoo(formatDate(rs.getDate("joindate_of_goo")));
                    employee.setBasic(rs.getInt("CUR_BASIC_SALARY"));
                    employee.setPermanentdist(rs.getString("home_town"));
                    employee.setGp(rs.getInt("GP"));
                    employee.setAadhaarno("");
                    employee.setEpic("");
                    employee.setAccttype(rs.getString("ACCT_TYPE"));
                    employee.setRemark(rs.getString("is_regular"));
                    employee.setPostGrpType(rs.getString("POST_GRP_TYPE"));
                    employee.setEmail(rs.getString("EMAIL_ID"));
                    employee.setMobile(rs.getString("MOBILE"));
                    employee.setCategory(rs.getString("CATEGORY"));
                    employee.setSltPayCommission(rs.getString("pay_commission"));
                    list.add(employee);
                }
                eList = new ArrEmpBillGroup();
                eList.setBillGroupName(description);
                eList.setEmpList(list);
                if (cnt > 0) {
                    outList.add(eList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return outList;
    }

    @Override
    public ArrayList getOfficeWiseBillGroupWiseContractualEmployeeList(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String description = null;
        try {
            con = dataSource.getConnection();
            ps1 = con.prepareStatement("SELECT distinct GS.section_name, GS.section_id, BM.bill_group_id, GS.bill_type,GS.OFF_CODE,GO.OFF_EN,BM.description "
                    + "FROM bill_group_master BM \n"
                    + "INNER JOIN bill_section_mapping BSM ON BM.bill_group_id = BSM.bill_group_id\n"
                    + "INNER JOIN g_section GS ON BSM.section_id = GS.section_id\n"
                    + "LEFT OUTER JOIN (SELECT * FROM G_OFFICE WHERE LVL='20')GO ON GO.OFF_CODE=GS.OFF_CODE\n"
                    + "WHERE BM.off_code = ? AND BM.description IS NOT NULL AND BM.description <> '' ORDER BY GO.OFF_EN desc,BM.description  ");
            ps1.setString(1, offCode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                list = new ArrayList();
                int cnt = 0;
                String billGroupId = rs1.getString("section_id");
                String offname = rs1.getString("off_en");
                if (rs1.getString("off_en") != null && !rs1.getString("off_en").equals("")) {
                    description = "<span style='font-size:12pt;color:#171717'>Bill Group: " + rs1.getString("description") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Section: " + rs1.getString("section_name") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#171717'>Bill Type: " + rs1.getString("bill_type") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Office Name: " + offname + "</span>";
                } else {
                    description = "<span style='font-size:12pt;color:#171717'>Bill Group: " + rs1.getString("description") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#A5520D'>Section: " + rs1.getString("section_name") + "</span>"
                            + " &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-size:12pt;color:#171717'>Bill Type: " + rs1.getString("bill_type") + "</span>";

                }
                ps = con.prepareStatement("SELECT EM.EMAIL_ID,EM.MOBILE,EM.POST_GRP_TYPE,EM.GENDER,EM.EMP_ID,EM.ACCT_TYPE,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EM.GP,('')CADRE_NAME,\n"
                        + "CUR_CADRE_GRADE ,DOB,DOS,joindate_of_goo,home_town,SPM.POST_SL_NO,SPM.SPC,(EM.POST_NOMENCLATURE) POST,EM.IS_REGULAR,EM.CUR_OFF_CODE FROM bill_section_mapping BM   \n"
                        + "INNER JOIN section_post_mapping SPM ON SPM.section_id = BM.section_id \n"
                        + "INNER JOIN \n"
                        + "(SELECT * FROM emp_mast WHERE IS_REGULAR='N')EM\n"
                        + "ON EM.EMP_ID=SPM.SPC AND  BM.section_id = ? ");
                ps.setBigDecimal(1, new BigDecimal(billGroupId));
                rs = ps.executeQuery();
                while (rs.next()) {
                    cnt++;
                    Employee employee = new Employee();
                    employee.setEmpid(rs.getString("EMP_ID"));
                    employee.setFname(rs.getString("F_NAME"));
                    employee.setMname(rs.getString("M_NAME"));
                    employee.setLname(rs.getString("L_NAME"));
                    if (rs.getString("GENDER") != null && !rs.getString("GENDER").equals("")) {
                        if (rs.getString("GENDER").equals("M")) {
                            employee.setGender("MALE");
                        } else if (rs.getString("GENDER").equals("F")) {
                            employee.setGender("FEMALE");
                        }
                    }
                    employee.setGpfno(rs.getString("GPF_NO"));
                    employee.setPost(rs.getString("POST"));
                    employee.setCadreCode(rs.getString("CADRE_NAME"));
                    employee.setCadreGrade(rs.getString("CUR_CADRE_GRADE"));
                    employee.setDob(formatDate(rs.getDate("DOB")));
                    employee.setDor(formatDate(rs.getDate("DOS")));
                    employee.setJoindategoo(formatDate(rs.getDate("joindate_of_goo")));
                    employee.setBasic(rs.getInt("CUR_BASIC_SALARY"));
                    employee.setPermanentdist(rs.getString("home_town"));
                    employee.setGp(rs.getInt("GP"));
                    employee.setAadhaarno("");
                    employee.setEpic("");
                    employee.setAccttype(rs.getString("ACCT_TYPE"));
                    employee.setRemark(rs.getString("is_regular"));
                    employee.setPostGrpType(rs.getString("POST_GRP_TYPE"));
                    employee.setEmail(rs.getString("EMAIL_ID"));
                    employee.setMobile(rs.getString("MOBILE"));
                    list.add(employee);
                }
                eList = new ArrEmpBillGroup();
                eList.setBillGroupName(description);
                eList.setEmpList(list);
                if (cnt > 0) {
                    outList.add(eList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return outList;
    }

    @Override
    public ArrayList getOfficeWiseEmployeeList(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT GENDER,EMP_MAST.EMP_ID,spc,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EMP_MAST.GP,G_CADRE.CADRE_NAME,CUR_CADRE_GRADE,DOB,\n"
                    + "DOS,joindate_of_goo,home_town,POST,ID_NO,MOBILE,\n"
                    + "CUR_SPC,OFF_CODE, POST_GRP_TYPE,email_id,bl_name,po_name,ps_name,village_name,dist_name FROM EMP_MAST\n"
                    + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE\n"
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC\n"
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE\n"
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_ID_DOC WHERE ID_DESCRIPTION = 'AADHAAR')EMP_ID_DOC \n"
                    + "ON EMP_MAST.EMP_ID = EMP_ID_DOC.EMP_ID\n"
                    + "LEFT OUTER JOIN\n"
                    + "(select emp_id,bl_name,po_name,ps_name,village_name,dist_name from\n"
                    + "(SELECT * FROM EMP_ADDRESS WHERE ADDRESS_TYPE='PRESENT')PRESADD\n"
                    + "left outer join g_block on PRESADD.bl_code=g_block.bl_code::text\n"
                    + "left outer join g_po on PRESADD.po_code=g_po.po_code\n"
                    + "left outer join g_ps on PRESADD.ps_code=g_ps.ps_code\n"
                    + "left outer join g_village on PRESADD.vill_code=g_village.vill_code\n"
                    + "left outer join g_district on PRESADD.dist_code=g_district.dist_code)empaddress\n"
                    + "on EMP_MAST.EMP_ID=empaddress.EMP_ID\n"
                    + "WHERE CUR_OFF_CODE=? AND DEP_CODE='02' ORDER BY F_NAME,M_NAME,L_NAME");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setGpfno(rs.getString("GPF_NO"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setPost(rs.getString("POST"));
                employee.setCadreCode(rs.getString("CADRE_NAME"));
                employee.setCadreGrade(rs.getString("CUR_CADRE_GRADE"));
                employee.setDob(formatDate(rs.getDate("DOB")));
                employee.setDor(formatDate(rs.getDate("DOS")));
                employee.setJoindategoo(formatDate(rs.getDate("joindate_of_goo")));
                employee.setBasic(rs.getInt("CUR_BASIC_SALARY"));
                employee.setPermanentdist(rs.getString("home_town"));
                employee.setGp(rs.getInt("GP"));
                employee.setAadhaarno(rs.getString("ID_NO"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setSpc(rs.getString("CUR_SPC"));
                employee.setEmail(rs.getString("email_id"));
                employee.setOfficecode(rs.getString("OFF_CODE"));
                employee.setPostGrpType(rs.getString("POST_GRP_TYPE"));
                employee.setVillName(rs.getString("village_name"));
                employee.setPsName(rs.getString("ps_name"));
                employee.setPoName(rs.getString("po_name"));
                employee.setBlName(rs.getString("bl_name"));
                employee.setDistName(rs.getString("dist_name"));

                if (rs.getString("GENDER") != null && !rs.getString("GENDER").equals("")) {
                    if (rs.getString("GENDER").equals("M")) {
                        employee.setGender("MALE");
                    } else if (rs.getString("GENDER").equals("F")) {
                        employee.setGender("FEMALE");
                    }
                }
                empList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public ArrayList getOfficeWiseEmployeeListForDp(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT GENDER,EMP_MAST.EMP_ID,spc,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EMP_MAST.GP,G_CADRE.CADRE_NAME,CUR_CADRE_GRADE,DOB,DOS,joindate_of_goo,home_town,POST,ID_NO,MOBILE,"
                    + "CUR_SPC,OFF_CODE, POST_GRP_TYPE FROM EMP_MAST "
                    + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_ID_DOC WHERE ID_DESCRIPTION = 'AADHAAR')EMP_ID_DOC ON EMP_MAST.EMP_ID = EMP_ID_DOC.EMP_ID "
                    + "WHERE CUR_OFF_CODE=? AND DEP_CODE IN('02','09') ORDER BY F_NAME,M_NAME,L_NAME");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setGpfno(rs.getString("GPF_NO"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setPost(rs.getString("POST"));
                employee.setCadreCode(rs.getString("CADRE_NAME"));
                employee.setCadreGrade(rs.getString("CUR_CADRE_GRADE"));
                employee.setDob(formatDate(rs.getDate("DOB")));
                employee.setDor(formatDate(rs.getDate("DOS")));
                employee.setJoindategoo(formatDate(rs.getDate("joindate_of_goo")));
                employee.setBasic(rs.getInt("CUR_BASIC_SALARY"));
                employee.setPermanentdist(rs.getString("home_town"));
                employee.setGp(rs.getInt("GP"));
                employee.setAadhaarno(rs.getString("ID_NO"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setSpc(rs.getString("CUR_SPC"));
                employee.setOfficecode(rs.getString("OFF_CODE"));
                employee.setPostGrpType(rs.getString("POST_GRP_TYPE"));
                if (rs.getString("GENDER") != null && !rs.getString("GENDER").equals("")) {
                    if (rs.getString("GENDER").equals("M")) {
                        employee.setGender("MALE");
                    } else if (rs.getString("GENDER").equals("F")) {
                        employee.setGender("FEMALE");
                    }
                }
                empList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public ArrayList getOfficeWisePostWiseEmployeeList(String offCode, String gpc
    ) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_MAST.EMP_ID,spc,GPF_NO,F_NAME,M_NAME,L_NAME,CUR_BASIC_SALARY,EMP_MAST.GP,G_CADRE.CADRE_NAME,CUR_CADRE_GRADE,DOB,DOS,joindate_of_goo,home_town,POST,ID_NO,MOBILE,"
                    + "CUR_SPC,OFF_CODE, POST_GRP_TYPE,CURR_POST_DOJ FROM EMP_MAST "
                    + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE "
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_ID_DOC WHERE ID_DESCRIPTION = 'AADHAAR')EMP_ID_DOC ON EMP_MAST.EMP_ID = EMP_ID_DOC.EMP_ID "
                    + "WHERE CUR_OFF_CODE=? AND DEP_CODE='02' AND GPC=? ORDER BY F_NAME,M_NAME,L_NAME");
            ps.setString(1, offCode);
            ps.setString(2, gpc);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setGpfno(rs.getString("GPF_NO"));
                employee.setPost(rs.getString("POST"));
                employee.setCadreCode(rs.getString("CADRE_NAME"));
                employee.setCadreGrade(rs.getString("CUR_CADRE_GRADE"));
                employee.setDob(formatDate(rs.getDate("DOB")));
                employee.setDor(formatDate(rs.getDate("DOS")));
                employee.setJoindategoo(formatDate(rs.getDate("joindate_of_goo")));
                employee.setBasic(rs.getInt("CUR_BASIC_SALARY"));
                employee.setPermanentdist(rs.getString("home_town"));
                employee.setGp(rs.getInt("GP"));
                employee.setAadhaarno(rs.getString("ID_NO"));
                employee.setMobile(rs.getString("MOBILE"));
                employee.setSpc(rs.getString("CUR_SPC"));
                employee.setOfficecode(rs.getString("OFF_CODE"));
                employee.setPostGrpType(rs.getString("POST_GRP_TYPE"));
                employee.setDateOfCurPosting(formatDate(rs.getDate("CURR_POST_DOJ")));
                empList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public EmployeePayProfile getEmployeePayProfile(String empid
    ) {
        EmployeePayProfile empPay = new EmployeePayProfile();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            con = dataSource.getConnection();
            statement = con.prepareStatement("SELECT CUR_BASIC_SALARY,GP,CUR_SALARY FROM EMP_MAST WHERE EMP_ID=?");
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                empPay.setBasic(result.getInt("CUR_BASIC_SALARY"));
                empPay.setGp(result.getInt("GP"));
                empPay.setPayScale(result.getString("CUR_SALARY"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empPay;
    }

    @Override
    public EmployeeSearchResult SearchEmployee(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        String empStatus = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = dataSource.getConnection();

            int offSet = searchEmployee.getRows() * (searchEmployee.getPage() - 1);
            int limit = searchEmployee.getRows();

            if ((searchEmployee.getDeptName() != null && !searchEmployee.getDeptName().equals("")) && (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals(""))) {
                if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " inner join g_office on emp_mast.cur_off_code = g_office.off_code"
                            + " WHERE g_office.department_code=? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getDeptName());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " inner join g_office on emp_mast.cur_off_code = g_office.off_code"
                            + " WHERE g_office.department_code=? and  dep_code NOT IN ('00','08','09','10') order by f_name  LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getDeptName());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchEmployee.getCriteria().equals("GPFNO") && searchEmployee.getDeptName() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and gpf_no = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getDeptName());
                    countst.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and gpf_no = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getDeptName());
                    st.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and emp_id = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getDeptName());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and emp_id = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getDeptName());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("FNAME") && searchEmployee.getDeptName() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and F_name LIKE  ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getDeptName());
                    countst.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE dep_code = ? and F_name LIKE  ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getDeptName());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                }
            } else if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                if (searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE gpf_no = ? and  dep_code NOT IN ('00','08','09','10')");

                    countst.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE gpf_no = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);

                    st.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE emp_id = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE emp_id = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("MOBILE")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE MOBILE = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE MOBILE = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("FNAME")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE F_name LIKE  ?  and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE F_name LIKE  ?  and  dep_code NOT IN ('00','08','09','10') ORDER BY f_name, m_name,l_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                }

            } else if (searchEmployee.getOffcode() != null) {
                if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and  dep_code NOT IN ('00','08','09','10') order by f_name  LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getOffcode());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria().equals("HRMSID") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ? and  dep_code NOT IN ('00','08','09','10')");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ?  and  dep_code NOT IN ('00','08','09','10') order by f_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("FNAME") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and F_name LIKE  ?  and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and F_name LIKE  ?  and  dep_code NOT IN ('00','08','09','10') ORDER BY f_name, m_name,l_name LIMIT " + limit + " OFFSET " + offSet);
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");

                } else {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT EMPLIST.emp_id, EMPLIST.f_name, EMPLIST.m_name,EMPLIST.l_name,EMPLIST.gpf_no,EMPLIST.dob,EMPLIST.cur_spc,EMPLIST.POST ,EMPLIST.SPC,EMPLIST.IS_REGULAR,GOFF.DDO_HRMSID FROM\n"
                            + "(SELECT EMPARR.emp_id, EMPARR.f_name, EMPARR.m_name,EMPARR.l_name,EMPARR.gpf_no,EMPARR.dob,EMPARR.cur_spc,EMPARR.POST ,EMPARR.is_regular,GMAP.SPC FROM\n"
                            + "(SELECT EMP.emp_id, EMP.f_name, EMP.m_name,EMP.l_name,EMP.gpf_no,EMP.dob,EMP.cur_spc,EMP.is_regular,G_POST.POST FROM\n"
                            + "(SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,cur_spc,is_regular FROM emp_mast WHERE cur_off_code=?) EMP\n"
                            + "LEFT OUTER JOIN\n"
                            + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE )EMPARR\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT DISTINCT(SPC) FROM G_PRIVILEGE_MAP WHERE ROLE_ID='05') GMAP ON EMPARR.CUR_SPC=GMAP.SPC ORDER BY EMPARR.F_NAME) EMPLIST\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT * FROM G_OFFICE) GOFF ON\n"
                            + "SUBSTR(EMPLIST.CUR_SPC,1,13)=GOFF.OFF_CODE AND GOFF.DDO_HRMSID=EMPLIST.EMP_ID ");
                    st.setString(1, searchEmployee.getOffcode());

                }
            }

            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt("CNT"));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setEmpName(StringUtils.defaultString(employee.getFname(), "") + " " + StringUtils.defaultString(employee.getMname(), "") + " " + StringUtils.defaultString(employee.getLname(), ""));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                employee.setHasPriv(rs.getString("has_office_privilage"));
                employee.setDdohrmsid(rs.getString("DDO_HRMSID"));
                employee.setIsRegular(rs.getString("IS_REGULAR"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public EmployeeSearchResult SearchEmployeeYearWise(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = dataSource.getConnection();
            int offSet = searchEmployee.getRows() * (searchEmployee.getPage() - 1);
            int limit = searchEmployee.getRows();

            if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10')) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getOffcode());

                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getOffcode());

            } else if (searchEmployee.getCriteria().equals("GPFNO")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and gpf_acc_no=? and off_code = ? and  dep_code NOT IN ('00','08','09','10')) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getSearchString());
                countst.setString(3, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and gpf_acc_no=? and off_code = ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getSearchString());
                st.setString(3, searchEmployee.getOffcode());
            } else if (searchEmployee.getCriteria().equals("HRMSID")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_code = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10')) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getSearchString());
                countst.setString(3, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name ,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_code=? and off_code = ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getSearchString());
                st.setString(3, searchEmployee.getOffcode());
            } else if (searchEmployee.getCriteria().equals("FNAME")) {
                if (searchEmployee.getOffcode() == null) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_name = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10')) and  dep_code NOT IN ('00','08','09','10') T1");
                    countst.setInt(1, searchEmployee.getYear());
                    countst.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    countst.setString(3, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_name LIKE  ? and off_code = ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                    st.setInt(1, searchEmployee.getYear());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    st.setString(3, searchEmployee.getOffcode());
                } else {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ?  and emp_name LIKE  ? and  dep_code NOT IN ('00','08','09','10')) T1");
                    countst.setInt(1, searchEmployee.getYear());
                    countst.setString(2, searchEmployee.getOffcode());
                    countst.setString(3, searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and emp_name LIKE  ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                    st.setInt(1, searchEmployee.getYear());
                    st.setString(2, searchEmployee.getOffcode());
                    st.setString(3, searchEmployee.getSearchString().toUpperCase() + "%");
                }
            } else {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10')) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and  dep_code NOT IN ('00','08','09','10') LIMIT " + limit + " OFFSET " + offSet);
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getOffcode());
            }
            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt(1));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setEmpid(rs.getString("emp_code"));
                employee.setEmpName(rs.getString("emp_name"));
                employee.setGpfno(rs.getString("gpf_acc_no"));
                employee.setPost(rs.getString("cur_desg"));
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public EmployeeSearchResult SearchEmployeeAG(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        String empStatus = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = dataSource.getConnection();

            if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                if (searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE gpf_no = ? ");

                    countst.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE gpf_no = ?  order by f_name ");

                    st.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE emp_id = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE emp_id = ?  order by f_name ");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("MOBILE")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE MOBILE = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE MOBILE = ? order by f_name");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("FNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE F_name LIKE  ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE F_name LIKE  ? ORDER BY f_name, m_name,l_name ");
                    st.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                }

            } else if (searchEmployee.getOffcode() != null) {
                if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? order by f_name ");
                    st.setString(1, searchEmployee.getOffcode());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and gpf_no = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria().equals("HRMSID") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and emp_id = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria().equals("FNAME") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and F_name LIKE  ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and F_name LIKE  ? ORDER BY f_name, m_name,l_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");

                } else {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT EMPLIST.emp_id, EMPLIST.f_name, EMPLIST.m_name,EMPLIST.l_name,EMPLIST.gpf_no,EMPLIST.dob,EMPLIST.cur_spc,EMPLIST.POST ,EMPLIST.SPC,EMPLIST.IS_REGULAR,GOFF.DDO_HRMSID FROM\n"
                            + "(SELECT EMPARR.emp_id, EMPARR.f_name, EMPARR.m_name,EMPARR.l_name,EMPARR.gpf_no,EMPARR.dob,EMPARR.cur_spc,EMPARR.POST ,EMPARR.is_regular,GMAP.SPC FROM\n"
                            + "(SELECT EMP.emp_id, EMP.f_name, EMP.m_name,EMP.l_name,EMP.gpf_no,EMP.dob,EMP.cur_spc,EMP.is_regular,G_POST.POST FROM\n"
                            + "(SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,cur_spc,is_regular FROM emp_mast WHERE cur_off_code=?) EMP\n"
                            + "LEFT OUTER JOIN\n"
                            + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE )EMPARR\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT DISTINCT(SPC) FROM G_PRIVILEGE_MAP WHERE ROLE_ID='05') GMAP ON EMPARR.CUR_SPC=GMAP.SPC ORDER BY EMPARR.F_NAME) EMPLIST\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT * FROM G_OFFICE) GOFF ON\n"
                            + "SUBSTR(EMPLIST.CUR_SPC,1,13)=GOFF.OFF_CODE AND GOFF.DDO_HRMSID=EMPLIST.EMP_ID ");
                    st.setString(1, searchEmployee.getOffcode());

                }
            }

            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt("CNT"));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setEmpName(StringUtils.defaultString(employee.getFname(), "") + " " + StringUtils.defaultString(employee.getMname(), "") + " " + StringUtils.defaultString(employee.getLname(), ""));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                employee.setHasPriv(rs.getString("has_office_privilage"));
                employee.setDdohrmsid(rs.getString("DDO_HRMSID"));
                employee.setIsRegular(rs.getString("IS_REGULAR"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public EmployeeSearchResult SearchEmployeeAGYearWise(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = dataSource.getConnection();

            if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ?) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? ");
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getOffcode());

            } else if (searchEmployee.getCriteria().equals("GPFNO")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and gpf_acc_no=? and off_code = ?) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getSearchString());
                countst.setString(3, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and gpf_acc_no=? and off_code = ? ");
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getSearchString());
                st.setString(3, searchEmployee.getOffcode());
            } else if (searchEmployee.getCriteria().equals("HRMSID")) {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_code = ? and off_code = ?) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getSearchString());
                countst.setString(3, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name ,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_code=? and off_code = ? ");
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getSearchString());
                st.setString(3, searchEmployee.getOffcode());
            } else if (searchEmployee.getCriteria().equals("FNAME")) {
                if (searchEmployee.getOffcode() == null) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_name = ? and off_code = ?) T1");
                    countst.setInt(1, searchEmployee.getYear());
                    countst.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    countst.setString(3, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and emp_name LIKE  ? and off_code = ? ");
                    st.setInt(1, searchEmployee.getYear());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                    st.setString(3, searchEmployee.getOffcode());
                } else {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ?  and emp_name LIKE  ?) T1");
                    countst.setInt(1, searchEmployee.getYear());
                    countst.setString(2, searchEmployee.getOffcode());
                    countst.setString(3, searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? and emp_name LIKE  ? ");
                    st.setInt(1, searchEmployee.getYear());
                    st.setString(2, searchEmployee.getOffcode());
                    st.setString(3, searchEmployee.getSearchString().toUpperCase() + "%");
                }
            } else {
                countst = con.prepareStatement("SELECT COUNT(*) CNT FROM(SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ?) T1");
                countst.setInt(1, searchEmployee.getYear());
                countst.setString(2, searchEmployee.getOffcode());
                st = con.prepareStatement("SELECT DISTINCT emp_code, emp_name,gpf_acc_no,cur_desg FROM aq_mast WHERE aq_year = ? and off_code = ? ");
                st.setInt(1, searchEmployee.getYear());
                st.setString(2, searchEmployee.getOffcode());
            }
            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt(1));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setEmpid(rs.getString("emp_code"));
                employee.setEmpName(rs.getString("emp_name"));
                employee.setGpfno(rs.getString("gpf_acc_no"));
                employee.setPost(rs.getString("cur_desg"));
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    public EmployeeSearchResult LocateEmployee1(SearchEmployee searchEmployee) {

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        String empStatus = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            String sql = "SELECT * FROM EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " WHERE EMP_ID = ?";
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, searchEmployee.getSearchString());
            rs = pst.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setEmpName(StringUtils.defaultString(employee.getFname(), "") + " " + StringUtils.defaultString(employee.getMname(), "") + " " + StringUtils.defaultString(employee.getLname(), ""));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                //employee.setHasPriv(rs.getString("has_office_privilage"));
                //employee.setDdohrmsid(rs.getString("DDO_HRMSID"));
                employee.setIsRegular(rs.getString("IS_REGULAR"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                employee.setWeight(rs.getString("is_sb_update_completed"));
                if (rs.getString("cur_off_code") != null && !rs.getString("cur_off_code").equals("")) {
                    String distcode = getDistrictCode(rs.getString("cur_off_code"));
                    employee.setDistrict(distcode);
                } else {
                    employee.setDistrict("NA");
                }

                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return empSearchResult;
    }

    public EmployeeSearchResult LocateEmployee(SearchEmployee searchEmployee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;
        ArrayList employeeList = new ArrayList();
        Employee employee = null;
        String empStatus = null;
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        try {
            con = dataSource.getConnection();

            int offSet = searchEmployee.getRows() * (searchEmployee.getPage() - 1);
            int limit = searchEmployee.getRows();

            if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE gpf_no = ? ");

                    countst.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE gpf_no = ?  order by f_name LIMIT " + limit + " OFFSET " + offSet);

                    st.setString(1, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE emp_id = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement("SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE emp_id = ?  order by f_name ");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("MOBILE")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE MOBILE = ? ");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE MOBILE = ? order by f_name");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("AADHAR")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " WHERE EMP_ID_DOC.ID_DESCRIPTION = 'AADHAAR' and EMP_ID_DOC.ID_NO = ?");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE EMP_ID_DOC.ID_DESCRIPTION = 'AADHAAR' and EMP_ID_DOC.ID_NO = ? order by f_name");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("PAN")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " WHERE EMP_ID_DOC.ID_DESCRIPTION = 'PAN' and EMP_ID_DOC.ID_NO = ?");
                    countst.setString(1, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE EMP_ID_DOC.ID_DESCRIPTION = 'PAN' and EMP_ID_DOC.ID_NO = ? order by f_name");
                    st.setString(1, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("FNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE F_name LIKE  ? ");
                    countst.setString(1, searchEmployee.getSearchString());

                    String sql = "SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE F_name LIKE ?";
                    if (searchEmployee.getTxtDOB() != null && !searchEmployee.getTxtDOB().equals("")) {
                        sql = sql + " and dob=?";
                    }
                    sql = sql + " ORDER BY f_name, m_name,l_name";
                    st = con.prepareStatement(sql);
                    st.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                    if (searchEmployee.getTxtDOB() != null && !searchEmployee.getTxtDOB().equals("")) {
                        st.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(searchEmployee.getTxtDOB()).getTime()));
                    }
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("LNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE l_name LIKE ?");
                    countst.setString(1, searchEmployee.getSearchString());

                    String sql = " SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE l_name LIKE ?";
                    if (searchEmployee.getTxtDOB() != null && !searchEmployee.getTxtDOB().equals("")) {
                        sql = sql + " and dob=?";
                    }
                    sql = sql + " ORDER BY f_name, m_name,l_name";
                    st = con.prepareStatement(sql);
                    st.setString(1, searchEmployee.getSearchString().toUpperCase() + "%");
                    if (searchEmployee.getTxtDOB() != null && !searchEmployee.getTxtDOB().equals("")) {
                        st.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(searchEmployee.getTxtDOB()).getTime()));
                    }
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("QRNO")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("select count(*) CNT from emp_qtr_allot"
                            + " inner join emp_mast on emp_qtr_allot.emp_id=emp_mast.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " where (emp_qtr_allot.quarter_no like ? or emp_qtr_allot.quarter_no like ? or address like ? or address like ?) ");
                    countst.setString(1, "%" + searchEmployee.getSearchString() + "%");
                    countst.setString(2, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    countst.setString(3, "%" + searchEmployee.getSearchString() + "%");
                    countst.setString(4, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement(" select cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage from emp_qtr_allot"
                            + " inner join emp_mast on emp_qtr_allot.emp_id=emp_mast.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " where (emp_qtr_allot.quarter_no like ? or emp_qtr_allot.quarter_no like ? or address like ? or address like ?) ORDER BY f_name, m_name,l_name ");
                    st.setString(1, "%" + searchEmployee.getSearchString() + "%");
                    st.setString(2, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    st.setString(3, "%" + searchEmployee.getSearchString() + "%");
                    st.setString(4, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                }

            } else if (searchEmployee.getOffcode() != null) {
                if (searchEmployee.getCriteria() == null || searchEmployee.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? order by f_name ");
                    st.setString(1, searchEmployee.getOffcode());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and gpf_no = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, StringUtils.upperCase(searchEmployee.getSearchString()));
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("HRMSID") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and emp_id = ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("AADHAR")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " WHERE cur_off_code = ? and EMP_ID_DOC.ID_DESCRIPTION = 'AADHAAR' and EMP_ID_DOC.ID_NO = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE cur_off_code = ? and EMP_ID_DOC.ID_DESCRIPTION = 'AADHAAR' and EMP_ID_DOC.ID_NO = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("PAN")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " WHERE cur_off_code = ? and EMP_ID_DOC.ID_DESCRIPTION = 'PAN' and EMP_ID_DOC.ID_NO = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast"
                            + " INNER JOIN EMP_ID_DOC ON emp_mast.emp_id = EMP_ID_DOC.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE cur_off_code = ? and EMP_ID_DOC.ID_DESCRIPTION = 'PAN' and EMP_ID_DOC.ID_NO = ? order by f_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString());
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("FNAME") && searchEmployee.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and F_name LIKE  ? LIMIT " + limit + " OFFSET " + offSet);
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and F_name LIKE  ? ORDER BY f_name, m_name,l_name");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");

                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("LNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and l_name LIKE  ? ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, searchEmployee.getSearchString());
                    st = con.prepareStatement(" SELECT cur_off_code,is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and l_name LIKE  ? ORDER BY f_name, m_name,l_name ");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, searchEmployee.getSearchString().toUpperCase() + "%");
                } else if (searchEmployee.getCriteria() != null && !searchEmployee.getCriteria().equals("") && searchEmployee.getCriteria().equals("QRNO")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("select count(*) CNT from emp_qtr_allot"
                            + " inner join emp_mast on emp_qtr_allot.emp_id=emp_mast.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " where cur_off_code = ? and (emp_qtr_allot.quarter_no like ? or emp_qtr_allot.quarter_no like ? or address like ? or address like ?) ");
                    countst.setString(1, searchEmployee.getOffcode());
                    countst.setString(2, "%" + searchEmployee.getSearchString() + "%");
                    countst.setString(3, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    countst.setString(4, "%" + searchEmployee.getSearchString() + "%");
                    countst.setString(5, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    st = con.prepareStatement(" select cur_off_code,is_sb_update_completed,emp_mast.emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage from emp_qtr_allot"
                            + " inner join emp_mast on emp_qtr_allot.emp_id=emp_mast.emp_id"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " where cur_off_code = ? and (emp_qtr_allot.quarter_no like ? or emp_qtr_allot.quarter_no like ? or address like ? or address like ?) ORDER BY f_name, m_name,l_name ");
                    st.setString(1, searchEmployee.getOffcode());
                    st.setString(2, "%" + searchEmployee.getSearchString() + "%");
                    st.setString(3, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                    st.setString(4, "%" + searchEmployee.getSearchString() + "%");
                    st.setString(5, "%" + searchEmployee.getSearchString().toUpperCase() + "%");
                } else {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchEmployee.getOffcode());
                    st = con.prepareStatement("SELECT EMP.cur_off_code,EMPLIST.is_sb_update_completed,EMPLIST.emp_id, EMPLIST.f_name, EMPLIST.m_name,EMPLIST.l_name,EMPLIST.gpf_no,EMPLIST.dob,EMPLIST.cur_spc,EMPLIST.POST ,EMPLIST.SPC,EMPLIST.IS_REGULAR,GOFF.DDO_HRMSID FROM\n"
                            + "(SELECT EMPARR.is_sb_update_completed,EMPARR.emp_id, EMPARR.f_name, EMPARR.m_name,EMPARR.l_name,EMPARR.gpf_no,EMPARR.dob,EMPARR.cur_spc,EMPARR.POST ,EMPARR.is_regular,GMAP.SPC FROM\n"
                            + "(SELECT EMP.is_sb_update_completed,EMP.emp_id, EMP.f_name, EMP.m_name,EMP.l_name,EMP.gpf_no,EMP.dob,EMP.cur_spc,EMP.is_regular,G_POST.POST FROM\n"
                            + "(SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,cur_spc,is_regular FROM emp_mast WHERE cur_off_code=?) EMP\n"
                            + "LEFT OUTER JOIN\n"
                            + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE )EMPARR\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT DISTINCT(SPC) FROM G_PRIVILEGE_MAP WHERE ROLE_ID='05') GMAP ON EMPARR.CUR_SPC=GMAP.SPC ORDER BY EMPARR.F_NAME) EMPLIST\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT * FROM G_OFFICE) GOFF ON\n"
                            + "SUBSTR(EMPLIST.CUR_SPC,1,13)=GOFF.OFF_CODE AND GOFF.DDO_HRMSID=EMPLIST.EMP_ID ");
                    st.setString(1, searchEmployee.getOffcode());

                }
            }

            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt("CNT"));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setEmpName(StringUtils.defaultString(employee.getFname(), "") + " " + StringUtils.defaultString(employee.getMname(), "") + " " + StringUtils.defaultString(employee.getLname(), ""));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                employee.setHasPriv(rs.getString("has_office_privilage"));
                employee.setDdohrmsid(rs.getString("DDO_HRMSID"));
                employee.setIsRegular(rs.getString("IS_REGULAR"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                employee.setWeight(rs.getString("is_sb_update_completed"));
                if (rs.getString("cur_off_code") != null && !rs.getString("cur_off_code").equals("")) {
                    String distcode = getDistrictCode(rs.getString("cur_off_code"));
                    employee.setDistrict(distcode);
                } else {
                    employee.setDistrict("NA");
                }

                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public List getIdentityList(String empid) {
        List idInfoList = new ArrayList<>();
        String SQL = "SELECT ID_DESCRIPTION,ID_NO,ID_DOI,ID_DOE,ID_POI,is_locked,is_verified FROM EMP_ID_DOC WHERE EMP_ID=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                IdentityInfo idInfo = new IdentityInfo();
                idInfo.setIdentityDesc(result.getString("ID_DESCRIPTION"));
                idInfo.setIdentityNo(result.getString("ID_NO"));
                idInfo.setIdentityDocType(result.getString("ID_DESCRIPTION"));
                idInfo.setIssueDate(formatDate(result.getDate("ID_DOI")));
                idInfo.setExpiryDate(formatDate(result.getDate("ID_DOE")));
                idInfo.setPlaceOfIssue(result.getString("ID_POI"));
                idInfo.setIsLocked(result.getString("is_locked"));
                idInfo.setIsVerified(result.getString("is_verified"));
                idInfoList.add(idInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return idInfoList;
    }

    @Override
    public Address[] getAddress(String empid) {
        List<Address> addressList = new ArrayList<>();
        String SQL = "SELECT ADDRESS_ID,ADDRESS_TYPE,ADDRESS,BL_CODE,VILL_CODE,PO_CODE,PS_CODE,PIN,STATE_CODE,DIST_CODE,STD_CODE,TPHONE,is_locked FROM EMP_ADDRESS WHERE EMP_ID=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                Address addr = new Address();
                addr.setEmpId(empid);
                addr.setAddressId(result.getInt("ADDRESS_ID"));
                addr.setAddressType(result.getString("ADDRESS_TYPE"));
                addr.setAddress(result.getString("ADDRESS"));
                addr.setBlockCode(result.getString("BL_CODE"));
                addr.setVillageCode(result.getString("VILL_CODE"));
                addr.setPostCode(result.getString("PO_CODE"));
                addr.setPsCode(result.getString("PS_CODE"));
                addr.setDistCode(result.getString("DIST_CODE"));
                addr.setStateCode(result.getString("STATE_CODE"));
                addr.setPin(result.getString("PIN"));
                addr.setStdCode(result.getString("STD_CODE"));
                addr.setTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));
                addressList.add(addr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        Address addrarray[] = addressList.toArray(new Address[addressList.size()]);
        return addrarray;
    }

    @Override
    public Address getAddressDetals(String empid, int addressId) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        Address addr = new Address();
        String SQL = "SELECT ADDRESS_ID,ADDRESS_TYPE,ADDRESS,BL_CODE,VILL_CODE,PO_CODE,PS_CODE,PIN,STATE_CODE,DIST_CODE,STD_CODE,TPHONE from emp_address WHERE EMP_ID=? AND ADDRESS_ID=?";
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setInt(2, addressId);
            result = statement.executeQuery();
            while (result.next()) {
                addr.setAddressId(result.getInt("ADDRESS_ID"));
                addr.setAddressType(result.getString("ADDRESS_TYPE"));
                addr.setAddress(result.getString("ADDRESS"));
                addr.setBlockCode(result.getString("BL_CODE"));
                addr.setVillageCode(result.getString("VILL_CODE"));
                addr.setPostCode(result.getString("PO_CODE"));
                addr.setPsCode(result.getString("PS_CODE"));
                addr.setDistCode(result.getString("DIST_CODE"));
                addr.setStateCode(result.getString("STATE_CODE"));
                addr.setPin(result.getString("PIN"));
                addr.setStdCode(result.getString("STD_CODE"));
                addr.setTelephone(result.getString("TPHONE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return addr;
    }

    @Override
    public int saveEmployeeMessage(EmployeeMessage employeemessage) {
        int messageId = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        int msg = 0;
        String empid = "";
        String mobileno = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT mobile FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, employeemessage.getEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                employeemessage.setMobileNo(rs.getString("mobile"));
                mobileno = employeemessage.getMobileNo();
            }

            pst = con.prepareStatement("INSERT INTO employee_message (emp_id, Message, Message_on_date, off_code,sender_id,sender_spc,message_title,sender_user_type,receiver_user_type,reciever_spc) VALUES(?, ?, ?, ?,?,? ,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, employeemessage.getEmpid());
            pst.setString(2, employeemessage.getMessage());
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setString(4, employeemessage.getOffcode());
            pst.setString(5, employeemessage.getSenderid());
            pst.setString(6, employeemessage.getSenderSpc());
            pst.setString(7, employeemessage.getMsgTitle());
            pst.setString(8, employeemessage.getSenderUserType());
            pst.setString(9, employeemessage.getReceiverUserType());
            pst.setString(10, employeemessage.getRecieverSpc());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
            if (mobileno != null && !mobileno.equals("")) {
                SMSThread smsthread = new SMSThread(empid, mobileno, "C");
                Thread t1 = new Thread(smsthread);
                t1.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return messageId;
    }

    @Override
    public int saveReplyEmployeeMessage(EmployeeMessage employeemessage) {
        int messageId = 0;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO employee_message (emp_id, Message, Message_on_date, sender_id,sender_spc,message_title,sender_user_type,receiver_user_type,parent_message_id) VALUES(?, ?, ?, ?,?,? ,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, employeemessage.getSenderId());
            pst.setString(2, employeemessage.getMessage());
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setString(4, employeemessage.getEmpid());
            pst.setString(5, employeemessage.getRecieverSpc());
            pst.setString(6, "Reply:");
            pst.setString(7, employeemessage.getReceiverUserType());
            pst.setString(8, employeemessage.getSenderUserType());
            pst.setInt(9, employeemessage.getMessageId());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return messageId;
    }

    @Override
    public EmployeeMessage getAttachedFile(int attachmentid) {
        EmployeeMessage employeeMessage = new EmployeeMessage();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT o_file_name, file_path, file_type from employee_attachment where attachment_id=?");
            pst.setInt(1, attachmentid);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                employeeMessage.setAttachementname(rs.getString("o_file_name"));
                employeeMessage.setFiletype(rs.getString("file_type"));
                filepath = rs.getString("file_path");
            }
            File f = new File(filepath);
            employeeMessage.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return employeeMessage;
    }

    @Override
    public int getSentMessageListcount(String senderid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        int count = 0;

        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT count(*) as cnt from employee_message "
                    + "INNER JOIN emp_mast ON employee_message.emp_id = emp_mast.emp_id where sender_id=? ");
            st.setString(1, senderid);
            rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    public List getMessageAttachment(int messageId, String reftype) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList messageAttachmentList = new ArrayList();
        try {
            con = dataSource.getConnection();

            st = con.prepareStatement("select * from employee_attachment where ref_id=? and ref_type=?");
            st.setInt(1, messageId);
            st.setString(2, reftype);
            rs = st.executeQuery();
            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setAttachementname(rs.getString("o_file_name"));
                attachment.setAttachmentid(rs.getInt("attachment_id"));
                attachment.setFiletype(rs.getString("file_type"));
                messageAttachmentList.add(attachment);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return messageAttachmentList;
    }

    @Override
    public List getSentMessageList(String senderid, int pgno) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList EmployeeMessageList = new ArrayList();
        int limit = 15;
        int offset = 0;
        int ctr = (pgno * limit) + 1;
        offset = limit * pgno;
        try {
            con = dataSource.getConnection();
            System.out.println("senderid:" + senderid + " limit:" + limit + " offset:" + offset);
            st = con.prepareStatement("SELECT emp_mast.emp_id,getSenderName(employee_message.sender_id, employee_message.sender_user_type) as senderName, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, employee_message.message,spn,  "
                    + "                    employee_message.message_on_date , employee_message.viewed_on_date, employee_message.message_title, employee_message.off_code, employee_message.is_viewed, getNoofAttachments(employee_message.message_id,'MESSAGE') AS NoofAttachments, employee_message.message_id,EMPMSG1.message_on_date dateofreply from employee_message "
                    + "                    INNER JOIN emp_mast ON employee_message.emp_id = emp_mast.emp_id "
                    + "			LEFT OUTER JOIN employee_message EMPMSG1 ON employee_message.message_id = EMPMSG1.parent_message_id "
                    + "	                    LEFT OUTER JOIN G_SPC ON  employee_message.reciever_spc = G_SPC.spc "
                    + "                    where employee_message.sender_id=? ORDER BY  message_on_date DESC LIMIT " + limit + " offset " + offset);
            st.setString(1, senderid);
            rs = st.executeQuery();
            while (rs.next()) {
                EmployeeMessage message = new EmployeeMessage();
                message.setSino(ctr + "");
                message.setSenderName(rs.getString("senderName"));
                message.setEmpid(rs.getString("emp_id"));
                message.setEmpname(rs.getString("EMPNAME"));
                message.setMessage(rs.getString("message_title"));
                message.setRecieverSpc(rs.getString("spn"));
                message.setOffcode(rs.getString("off_code"));
                message.setIsviewed(rs.getString("is_viewed"));
                message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));
                message.setViewondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("viewed_on_date")));
                message.setNoofAttachments(rs.getInt("NoofAttachments"));
                message.setMessageId(rs.getInt("message_id"));
                message.setRepliedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofreply")));
                EmployeeMessageList.add(message);
                ctr++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return EmployeeMessageList;
    }

    @Override
    public void uploadMultipleAttachedFile(int messageId, String refType, List<MultipartFile> files) {
        String diskfileName = new Date().getTime() + "";
        PreparedStatement pst = null;
        Connection con = null;
        try {
            if (null != files && files.size() > 0) {
                con = dataSource.getConnection();
                pst = con.prepareStatement("INSERT INTO employee_attachment(o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES(?, ?, ?, ?, ?, ? )");
                for (MultipartFile multipartFile : files) {
                    try {
                        if (!multipartFile.isEmpty()) {
                            byte[] bytes = multipartFile.getBytes();
                            File dir = new File(this.uploadPath + File.separator);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                            stream.write(bytes);
                            stream.close();
                            /*Insert uploaded file name to database table*/
                            pst.setString(1, multipartFile.getOriginalFilename());
                            pst.setString(2, diskfileName);
                            pst.setInt(3, messageId);
                            pst.setString(4, refType);
                            pst.setString(5, dir.getAbsolutePath() + File.separator + diskfileName);
                            pst.setString(6, multipartFile.getContentType());
                            pst.executeUpdate();

                        }
                        /*Insert uploaded file name to database table*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void uploadAttachedFile(int messageId, String refType, MultipartFile file) {
        String diskfileName = new Date().getTime() + "";
        PreparedStatement pst = null;
        Connection con = null;
        if (!file.isEmpty()) {
            try {

                byte[] bytes = file.getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                con = dataSource.getConnection();
                pst = con.prepareStatement("INSERT INTO employee_attachment(o_file_name,d_file_name,ref_id,ref_type,file_path,file_type) VALUES(?, ?, ?, ?, ?, ? )");
                pst.setString(1, file.getOriginalFilename());
                pst.setString(2, diskfileName);
                pst.setInt(3, messageId);
                pst.setString(4, refType);
                pst.setString(5, dir.getAbsolutePath() + File.separator + diskfileName);
                pst.setString(6, file.getContentType());
                pst.executeUpdate();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                DataBaseFunctions.closeSqlObjects(pst);
                DataBaseFunctions.closeSqlObjects(con);
            }
        }
    }

    public EmployeeMessage getMessageDetails(int messageId) {
        EmployeeMessage empMessage = new EmployeeMessage();

        return empMessage;
    }

    @Override
    public List getEmployeeMessageList(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList EmployeeMessageList = new ArrayList();
        Employee employee = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT  message, message_on_date, viewed_on_date, off_code, is_viewed from employee_message where emp_id= ?");

            rs = st.executeQuery();
            while (rs.next()) {
                EmployeeMessage message = new EmployeeMessage();

                message.setMessage(rs.getString("message"));
                message.setOffcode(rs.getString("off_code"));
                message.setIsviewed(rs.getString("is_viewed"));
                message.setMessageondate(rs.getString("message_on_date"));
                message.setViewondate(rs.getString("viewed_on_date"));

                EmployeeMessageList.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return EmployeeMessageList;
    }

    @Override
    public List getOffWiseEmpList(String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        Employee employee = null;
        try {
            con = dataSource.getConnection();
            String empListQry = "select emp_id,INITIALS, F_NAME, M_NAME, L_NAME,POST from emp_mast EM "
                    + "LEFT OUTER JOIN g_spc GS ON EM.cur_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where cur_off_code=? ORDER BY F_NAME";
            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setIntitals(rs.getString("INITIALS"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setPost(rs.getString("POST"));
                empList.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public List getDeptWiseEmpList(String deptCode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList empList = new ArrayList();
        Employee employee = null;

        try {
            con = dataSource.getConnection();
            String empListQry = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,post "
                    + "from g_office GE INNER JOIN emp_mast EM ON GE.off_code=EM.cur_off_code "
                    + "LEFT OUTER JOIN g_spc GS ON EM.cur_spc=GS.spc "
                    + "LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where GE.department_code=? ORDER BY F_NAME";

            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, deptCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setIntitals(rs.getString("INITIALS"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setPost(rs.getString("POST"));
                empList.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public void saveProfile(Employee emp) {
        PreparedStatement pst = null;
        Connection con = null;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("update emp_mast set gistype=?,gisno=?,dos=?,m_status=?,res_cat=?,height=?,bl_grp=?,"
                    + "home_town=?,religion=?,domicile=?,id_mark=?,mobile=?,joindate_of_goo=?,doe_gov=?,jointime_of_goo=?,toe_gov=?,"
                    + "gender=? where emp_id=?");
            pst.setString(1, emp.getGisType());
            pst.setString(2, emp.getGisNo());
            if (emp.getTxtDos() != null && !emp.getTxtDos().equals("")) {
                pst.setTimestamp(3, new Timestamp(sdf.parse(emp.getTxtDos()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, emp.getMarital());
            pst.setString(5, emp.getCategory());
            pst.setDouble(6, emp.getHeight());
            pst.setString(7, emp.getBloodgrp());
            pst.setString(8, emp.getHomeTown());
            pst.setString(9, emp.getReligion());
            pst.setString(10, emp.getDomicil());
            pst.setString(11, emp.getIdmark());
            pst.setString(12, emp.getMobile());
            if (emp.getJoindategoo() != null && !emp.getJoindategoo().equals("")) {
                pst.setTimestamp(13, new Timestamp(sdf.parse(emp.getJoindategoo()).getTime()));
            } else {
                pst.setTimestamp(13, null);
            }
            if (emp.getDoeGov() != null && !emp.getDoeGov().equals("")) {
                pst.setTimestamp(14, new Timestamp(sdf.parse(emp.getDoeGov()).getTime()));
            } else {
                pst.setTimestamp(14, null);
            }
            pst.setString(15, emp.getTxtwefTime());
            pst.setString(16, emp.getTimeOfEntryGoo());
            pst.setString(17, emp.getGender());
            pst.setString(18, emp.getEmpid());
            int n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveLanguageKnown(String empid, EmployeeLanguage language) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO EMP_LANGUAGE (EMP_ID,LANGUAGE, IF_READ, IF_WRITE, IF_SPEAK, IF_MLANG) VALUES (?,?,?,?,?,?)");
            pst.setString(1, empid);
            pst.setString(2, language.getLanguage());
            pst.setString(3, language.getIfread());
            pst.setString(4, language.getIfwrite());
            pst.setString(5, language.getIfspeak());
            pst.setString(6, language.getIfmlang());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getSuperannuationdate(String empId, String retiredYear) {
        Connection con = null;
        ResultSet rs = null;
        Date superdate = null;
        PreparedStatement pst = null;
        Statement statement = null;
        ResultSet resultset = null;
        String fdate = "";
        String superanndate = "";

        try {

            con = dataSource.getConnection();
            statement = con.createStatement();
            pst = con.prepareStatement("select dob,gpf_no from emp_mast where emp_id=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                fdate = rs.getString("dob");
            }
            int dateParts[] = new int[2];
            if (fdate != null && !fdate.equals("")) {
                fdate = fdate.substring(0, fdate.indexOf(" "));
            }
            if ((fdate != null && !fdate.equalsIgnoreCase("")) && (retiredYear != null && !retiredYear.equalsIgnoreCase(""))) {
                dateParts = Numtowordconvertion.getDateParts(fdate);
                if (dateParts[2] == 1) {
                    String tempfdate = "";
                    if (dateParts[1] == 1) {
                        int year = dateParts[0] - 1;
                        tempfdate = year + "-" + 12 + "-" + 31;
                    } else {
                        int month = dateParts[1] - 1;
                        if (month == 4 || month == 6 || month == 9 || month == 11) {
                            tempfdate = dateParts[0] + "-" + month + "-" + 30;
                        } else {
                            if (month == 2) {
                                if (ifLeapYear(dateParts[0])) {
                                    tempfdate = dateParts[0] + "-" + month + "-" + 29;
                                } else {
                                    tempfdate = dateParts[0] + "-" + month + "-" + 28;
                                }
                            } else {
                                tempfdate = dateParts[0] + "-" + month + "-" + 31;
                            }
                        }
                    }

                    resultset = statement.executeQuery("SELECT to_timestamp('" + tempfdate + "','yyyy-mm-dd')+'" + retiredYear + "-00' as  superdate;");

                } else {
                    resultset = statement.executeQuery("SELECT to_timestamp('" + fdate + "','yyyy-mm-dd')+'" + retiredYear + "-00' as  superdate;");
                }
                while (resultset.next()) {
                    if (resultset.getDate("superdate") != null && !resultset.getString("superdate").trim().equals("")) {
                        superdate = resultset.getDate("superdate");

                        String[] superdateArr = superdate.toString().split("-");

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, Integer.parseInt(superdateArr[0]));
                        int tempmonth = Integer.parseInt(superdateArr[1]) - 1;
                        cal.set(Calendar.MONTH, tempmonth);
                        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(superdateArr[2]));
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(cal.DAY_OF_MONTH));

                        superdate = cal.getTime();

                    }
                }

                if (superdate != null) {
                    superanndate = CommonFunctions.getFormattedOutputDate1(superdate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(statement);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return superanndate;
    }

    @Override
    public void DownlaodOfficeWiseEmployee(OutputStream out, String filename, WritableWorkbook workbook, int month, int year, String ocode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        try {
            con = dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(filename, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setAlignment(Alignment.LEFT);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.LEFT);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setAlignment(Alignment.LEFT);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 40;

            Label label = null;
            jxl.write.Number num = null;

            label = new Label(0, 0, "HRMS ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 40;
            sheet.addCell(label);
            label = new Label(1, 0, "NAME", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(2, 0, "Designation", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(3, 0, "Type", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 40;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            stm = ("SELECT emp_code,emp_name,cur_desg,emp_type FROM aq_mast WHERE off_code='" + ocode + "' AND p_month='" + month + "' AND p_year='" + year + "'");
            ps1 = con.prepareStatement(stm);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                slno += 1;
                row += 1;
                String empType = rs1.getString("emp_type");
                if (empType.equals("R")) {
                    empType = "Regular";
                } else {
                    empType = "Contractual";
                }
                label = new Label(0, row, rs1.getString("emp_code"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 40;
                sheet.addCell(label);
                label = new Label(1, row, rs1.getString("emp_name"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 16;
                sheet.addCell(label);
                label = new Label(2, row, rs1.getString("cur_desg"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 25;
                sheet.addCell(label);
                label = new Label(3, row, empType, innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public IdentityInfo saveIdentity(IdentityInfo identity) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst1 = con.prepareStatement("select emp_id,id_description from emp_id_doc where id_description=? and emp_id=?");
            pst1.setString(1, identity.getIdentityDocType());
            pst1.setString(2, identity.getEmpId());
            rs = pst1.executeQuery();
            if (rs.next()) {
                identity.setPrintMsg("Identity Type already exists");
            } else {
                pst = con.prepareStatement("insert into emp_id_doc (emp_id,id_description,id_no,id_doi,id_doe,id_poi) values(?,?,?,?,?,?)");

                pst.setString(1, identity.getEmpId());
                pst.setString(2, identity.getIdentityDocType());
                pst.setString(3, identity.getIdentityDocNo());
                if (identity.getIssueDate() != null && !identity.getIssueDate().equals("")) {
                    pst.setTimestamp(4, new Timestamp(sdf.parse(identity.getIssueDate()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                if (identity.getExpiryDate() != null && !identity.getExpiryDate().equals("")) {
                    pst.setTimestamp(5, new Timestamp(sdf.parse(identity.getExpiryDate()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, identity.getPlaceOfIssue());
                if (identity.getEmpId() != null && !identity.getEmpId().equals("")) {
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return identity;
    }

    @Override
    public void updateIdentity(IdentityInfo identity) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            //if (identity.getHidIdentityId().equals(identity.getIdentityDocType())) {
            pst = con.prepareStatement("UPDATE emp_id_doc SET id_no=?,id_doi=?,id_doe=?,id_poi=? WHERE emp_id=? AND id_description=? AND is_locked <> 'Y' AND is_verified <> 'Y' ");
            pst.setString(1, identity.getIdentityDocNo());
            if (identity.getIssueDate() != null && !identity.getIssueDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(sdf.parse(identity.getIssueDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (identity.getExpiryDate() != null && !identity.getExpiryDate().equals("")) {
                pst.setTimestamp(3, new Timestamp(sdf.parse(identity.getExpiryDate()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, identity.getPlaceOfIssue());

            pst.setString(5, identity.getEmpId());
            pst.setString(6, identity.getHidIdentityId());

            pst.executeUpdate();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateIdentityForDC(IdentityInfo identity) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            //if (identity.getHidIdentityId().equals(identity.getIdentityDocType())) {
            pst = con.prepareStatement("UPDATE emp_id_doc SET id_no=?,id_doi=?,id_doe=?,id_poi=? WHERE emp_id=? AND id_description=?");
            pst.setString(1, identity.getIdentityDocNo());
            if (identity.getIssueDate() != null && !identity.getIssueDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(sdf.parse(identity.getIssueDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (identity.getExpiryDate() != null && !identity.getExpiryDate().equals("")) {
                pst.setTimestamp(3, new Timestamp(sdf.parse(identity.getExpiryDate()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, identity.getPlaceOfIssue());

            pst.setString(5, identity.getEmpId());
            pst.setString(6, identity.getHidIdentityId());

            pst.executeUpdate();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteIdentity(IdentityInfo identity) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from emp_id_doc WHERE emp_id=? AND id_description=?");
            pst.setString(1, identity.getEmpId());
            pst.setString(2, identity.getHidIdentityId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    /*
     @Override
     public void updateIdentity(IdentityInfo identity) {
     PreparedStatement pst = null;
     Connection con = null;
     SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
     PreparedStatement pst1 = null;
     ResultSet rs = null;
     try {
     con = dataSource.getConnection();
     pst1 = con.prepareStatement("select emp_id,id_description from emp_id_doc where id_description=? and emp_id=?");
     pst1.setString(1, identity.getIdentityDocType());
     pst1.setString(2, identity.getEmpId());
     rs = pst1.executeQuery();
     if (rs.next()) {
     pst = con.prepareStatement("update emp_id_doc set id_no=?,id_doi=?,id_doe=?,id_poi=? where id_description=? and emp_id=?");

     pst.setString(1, identity.getIdentityDocNo());
     if (identity.getIssueDate() != null && !identity.getIssueDate().equals("")) {
     pst.setTimestamp(2, new Timestamp(sdf.parse(identity.getIssueDate()).getTime()));
     } else {
     pst.setTimestamp(2, null);
     }
     if (identity.getExpiryDate() != null && !identity.getExpiryDate().equals("")) {
     pst.setTimestamp(3, new Timestamp(sdf.parse(identity.getExpiryDate()).getTime()));
     } else {
     pst.setTimestamp(3, null);
     }
     pst.setString(4, identity.getPlaceOfIssue());
     pst.setString(5, identity.getIdentityDocType());
     pst.setString(6, identity.getEmpId());
     pst.executeUpdate();
     } else {
     pst = con.prepareStatement("insert into emp_id_doc (emp_id,id_description,id_no,id_doi,id_doe,id_poi) values(?,?,?,?,?,?)");

     pst.setString(1, identity.getEmpId());
     pst.setString(2, identity.getIdentityDocType());
     pst.setString(3, identity.getIdentityDocNo());
     if (identity.getIssueDate() != null && !identity.getIssueDate().equals("")) {
     pst.setTimestamp(4, new Timestamp(sdf.parse(identity.getIssueDate()).getTime()));
     } else {
     pst.setTimestamp(4, null);
     }
     if (identity.getExpiryDate() != null && !identity.getExpiryDate().equals("")) {
     pst.setTimestamp(5, new Timestamp(sdf.parse(identity.getExpiryDate()).getTime()));
     } else {
     pst.setTimestamp(5, null);
     }
     pst.setString(6, identity.getPlaceOfIssue());

     pst.executeUpdate();
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(pst);
     DataBaseFunctions.closeSqlObjects(con);
     }
     }
     */

    @Override
    public IdentityInfo getIdentityData(String idDesc, String empId) {
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        IdentityInfo identity = new IdentityInfo();
        try {
            con = dataSource.getConnection();
            //st=con.createStatement();            
            pst = con.prepareStatement("SELECT ID_DESCRIPTION,ID_NO,ID_DOI,ID_DOE,ID_POI,is_verified FROM EMP_ID_DOC WHERE ID_DESCRIPTION=? AND EMP_ID=?");
            pst.setString(1, idDesc);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            //  rs=st.executeQuery("SELECT ID_DESCRIPTION,ID_NO,ID_DOI,ID_DOE,ID_POI FROM EMP_ID_DOC WHERE ID_DESCRIPTION="+idDesc+"AND EMP_ID='"+empId+"'");
            if (rs.next()) {
                identity.setEmpId(empId);
                identity.setIdentityDocNo(rs.getString("ID_NO"));
                identity.setIsVerified(rs.getString("is_verified"));
                identity.setIdentityDocType(rs.getString("ID_DESCRIPTION"));
                identity.setPlaceOfIssue(rs.getString("ID_POI"));
                identity.setIssueDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ID_DOI")));
                identity.setExpiryDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ID_DOE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return identity;
    }

    @Override
    public Employee ResetEmpoyeePassword(String empid, String dcLoginId) {
        Employee employee = new Employee();
        employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        ResultSet rs = null;
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);
            conn = dataSource.getConnection();

            /*statement = conn.prepareStatement("SELECT username FROM user_details WHERE linkid=? ");
             statement.setString(1, empid.trim());
             rs = statement.executeQuery();
             if (rs.next()) {
             employee.setUsername(rs.getString("username"));
             }

             statement = conn.prepareStatement("UPDATE user_details SET password='welcome@123' WHERE linkid=?");
             statement.setString(1, empid);
             statement.executeUpdate();
             String sqlString = ToStringBuilder.reflectionToString(statement);
             statement = conn.prepareStatement("INSERT INTO  monitor_emp_log (login_by,login_date,emp_id,sql_text_query) values(?,?,?,?)");
             statement.setString(1, dcLoginId);
             statement.setTimestamp(2, new Timestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(Cdate).getTime()));
             statement.setString(3, empid);
             statement.setString(4, sqlString);
             statement.executeUpdate();*/
            //Reset Password old code
            //employee = updatePassword(employee, empid);
            //Reset Password old code
            //Reset Password new function
            statement = conn.prepareStatement("SELECT * FROM USER_DETAILS WHERE LINKID=?");
            statement.setString(1, empid);
            rs = statement.executeQuery();
            if (rs.next()) {
                employee.setUsername(rs.getString("USERNAME"));
                employee.setPassword("hrms@123");

                statement = conn.prepareStatement("UPDATE USER_DETAILS SET PASSWORD=?,ACCOUNTNONLOCKED=?,force_reset=1 WHERE LINKID=?");
                statement.setString(1, "hrms@123");
                statement.setInt(2, 1);
                statement.setString(3, empid);
                statement.executeUpdate();

                DataBaseFunctions.closeSqlObjects(result, statement);

                String sqlString = dcLoginId + " has reset password of " + empid + " on " + new Date();
                statement = conn.prepareStatement("INSERT INTO monitor_emp_log (login_by,login_date,emp_id,sql_text_query) values(?,?,?,?)");
                statement.setString(1, dcLoginId);
                statement.setTimestamp(2, new Timestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(Cdate).getTime()));
                statement.setString(3, empid);
                statement.setString(4, sqlString);
                statement.executeUpdate();
            }
            //Reset Password new function
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee EditEmpoyeeData(String empid) {
        Employee employee = new Employee();
        employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement("SELECT initials,F_NAME, M_NAME,usertype, L_NAME ,GENDER, dob dobformat,dos dosformat, MOBILE, acct_type,gpf_no,other_eligibility, if_gpf_assumed, allotment_year,bank_acc_no,bank_code,branch_code,id_mark,m_status,res_cat,religion,bl_grp,is_regular,doe_gov,joindate_of_goo,post_grp_type,doe_gov_time,jointime_of_goo,height,domicile,home_town,email_id,gistype,gisno,if_employed_res,if_employed_rehab,brass_no,pay_commission,if_reengaged,stop_pay_nps,emp_non_pran,law_level FROM emp_mast WHERE emp_id=?");
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setIntitals(result.getString("initials"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                employee.setGender(result.getString("GENDER"));
                employee.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("dobformat")));
                employee.setMobile(result.getString("MOBILE"));
                employee.setAccttype(result.getString("acct_type"));
                employee.setGpfno(result.getString("gpf_no"));
                employee.setEmpAllotmentYear(result.getString("allotment_year"));
                employee.setEmpid(empid);
                employee.setDos(CommonFunctions.getFormattedOutputDate1(result.getDate("dosformat")));
                employee.setChkUGC(result.getString("other_eligibility"));

                employee.setSltGPFAssmued(result.getString("if_gpf_assumed"));

                employee.setBankaccno(result.getString("bank_acc_no"));
                employee.setSltBank(result.getString("bank_code"));
                employee.setSltbranch(result.getString("branch_code"));

                employee.setIdmark(result.getString("id_mark"));

                employee.setMarital(result.getString("m_status"));
                employee.setCategory(result.getString("res_cat"));
                employee.setReligion(result.getString("religion"));
                employee.setBloodgrp(result.getString("bl_grp"));
                employee.setEmpType(result.getString("is_regular"));

                employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(result.getDate("doe_gov")));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(result.getDate("joindate_of_goo")));

                employee.setPostGrpType(result.getString("post_grp_type"));
                employee.setTimeOfEntryGoo(result.getString("doe_gov_time"));
                employee.setTxtwefTime(result.getString("jointime_of_goo"));

                employee.setHeight(result.getDouble("height"));

                employee.setDomicile(result.getString("domicile"));
                employee.setHomeTown(result.getString("home_town"));
                employee.setEmail(result.getString("email_id"));
                employee.setGisType(result.getString("gistype"));
                employee.setGisNo(result.getString("gisno"));
                employee.setIfReservation(result.getString("if_employed_res"));
                employee.setIfRehabiltation(result.getString("if_employed_rehab"));
                employee.setBrassno(result.getString("brass_no"));
                employee.setSltPayCommission(result.getString("pay_commission"));
                employee.setIsReengaged(result.getString("if_reengaged"));
                employee.setStopPayNPS(result.getString("stop_pay_nps"));
                employee.setEmpNonPran(result.getString("emp_non_pran"));
                employee.setLawLevel(result.getString("law_level"));
                employee.setSpluserCategory(result.getString("usertype"));
            }
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Employee getEmpoyeeData(String gpfNo) {
        Employee employee = new Employee();

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement("SELECT EMP_ID, initials, F_NAME, M_NAME, L_NAME, GENDER, MOBILE, acct_type, gpf_no, "
                    + " ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME , to_char(dob, 'YYYY-mm-dd') as dobformat, SPN,EMP.CUR_SPC FROM emp_mast emp "
                    + " LEFT OUTER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC WHERE GPF_NO=?");

            statement.setString(1, gpfNo);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setEmpid(result.getString("EMP_ID"));
                employee.setIntitals(result.getString("initials"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                employee.setGender(result.getString("GENDER"));
                employee.setDob(result.getString("dobformat"));
                employee.setMobile(result.getString("MOBILE"));
                employee.setAccttype(result.getString("acct_type"));
                employee.setGpfno(result.getString("gpf_no"));
                employee.setEmpName(result.getString("EMPNAME"));
                employee.setCurDesg(result.getString("SPN"));
                employee.setSpc(result.getString("CUR_SPC"));
            }
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public String updateEmployeeData(Employee employee, String dcLoginId) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        int resultQry = 0;

        String isDuplicateGPF = "N";
        String returnString = null;
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);
            conn = dataSource.getConnection();
            String acctype = employee.getAccttype();
            String empId = "";
            String mobile = "";
            String selectQry = "SELECT EMP_ID FROM EMP_MAST WHERE GPF_NO =?";
            pstmt = conn.prepareStatement(selectQry);
            pstmt.setString(1, employee.getGpfno());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                empId = rs.getString("EMP_ID");
                if (!empId.equals(employee.getEmpid())) {
                    isDuplicateGPF = "Y";
                }
            }
            if (isDuplicateGPF.equals("N")) {
                selectQry = "SELECT count(*) cnt FROM EMP_MAST WHERE MOBILE=? AND EMP_ID != ?";
                pstmt = conn.prepareStatement(selectQry);
                pstmt.setString(1, employee.getMobile());
                pstmt.setString(2, employee.getEmpid());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("cnt") > 0) {
                        isDuplicateGPF = "Y";
                    }
                }
            }
            returnString = isDuplicateGPF;
            if (isDuplicateGPF.equals("N")) {
                // pstmt = conn.prepareStatement("update emp_mast set gpf_no=?,acct_type=?,initials=?,f_name=?,m_name=?,l_name=?,gender=?,mobile=?, dob=?,dos=?,other_eligibility=?, if_gpf_assumed=?, cur_allotment_year=?,id_mark=?,m_status=?,res_cat=?,bl_grp=?,is_regular=?,doe_gov=?,joindate_of_goo=?,post_grp_type=?,doe_gov_time=?,jointime_of_goo=?,height=?,domicile=?,home_town=?,email_id=?,gistype=?,gisno=?,if_employed_res=?,if_employed_rehab=?,brass_no=?,pay_commission=?,if_reengaged=?,emp_non_pran=? where emp_id=? ");
                pstmt = conn.prepareStatement("update emp_mast set acct_type=?,initials=?,gender=?,mobile=?,dos=?,other_eligibility=?, cur_allotment_year=?,id_mark=?,m_status=?,"
                        + "res_cat=?,religion=?,bl_grp=?,is_regular=?,doe_gov=?,joindate_of_goo=?,post_grp_type=?,doe_gov_time=?,jointime_of_goo=?,height=?,domicile=?,"
                        + "home_town=?,email_id=?,gistype=?,gisno=?,if_employed_res=?,if_employed_rehab=?,brass_no=?,pay_commission=?,if_reengaged=?,emp_non_pran=?,"
                        + "law_level=?,if_gpf_assumed=?,gpf_no=?,stop_pay_nps=?,usertype=? where emp_id=? ");
                /* if (employee.getGpfno() != null && !employee.getGpfno().equals("")) {
                 pstmt.setString(1, employee.getGpfno().trim());
                 }*/
                pstmt.setString(1, acctype);
                pstmt.setString(2, employee.getIntitals());

                pstmt.setString(3, employee.getGender());
                pstmt.setString(4, employee.getMobile());

                if (employee.getDos() != null && !employee.getDos().equals("")) {
                    pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(employee.getDos()).getTime()));
                } else {
                    pstmt.setTimestamp(5, null);
                }
                pstmt.setString(6, employee.getChkUGC());
                pstmt.setString(7, employee.getEmpAllotmentYear());
                pstmt.setString(8, employee.getIdmark());
                pstmt.setString(9, employee.getMarital());
                pstmt.setString(10, employee.getCategory());
                pstmt.setString(11, employee.getReligion());
                pstmt.setString(12, employee.getBloodgrp());
                pstmt.setString(13, employee.getEmpType());
                if (employee.getDoeGov() != null && !employee.getDoeGov().equals("")) {
                    pstmt.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(employee.getDoeGov()).getTime()));
                } else {
                    pstmt.setTimestamp(14, null);
                }
                if (employee.getJoindategoo() != null && !employee.getJoindategoo().equals("")) {
                    pstmt.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(employee.getJoindategoo()).getTime()));
                } else {
                    pstmt.setTimestamp(15, null);
                }
                pstmt.setString(16, employee.getPostGrpType());
                pstmt.setString(17, employee.getTimeOfEntryGoo());
                pstmt.setString(18, employee.getTxtwefTime());
                pstmt.setDouble(19, employee.getHeight());
                pstmt.setString(20, employee.getDomicile());
                pstmt.setString(21, employee.getHomeTown());
                pstmt.setString(22, employee.getEmail());
                pstmt.setString(23, employee.getGisType());
                pstmt.setString(24, employee.getGisNo());
                pstmt.setString(25, employee.getIfReservation());
                pstmt.setString(26, employee.getIfRehabiltation());
                pstmt.setString(27, employee.getBrassno());
                if (employee.getSltPayCommission() != null && !employee.getSltPayCommission().equals("")) {
                    pstmt.setInt(28, Integer.parseInt(employee.getSltPayCommission()));
                } else {
                    pstmt.setInt(28, 0);
                }

                pstmt.setString(29, employee.getIsReengaged());
                pstmt.setString(30, employee.getEmpNonPran());
                pstmt.setString(31, employee.getLawLevel());
               
                pstmt.setString(32, employee.getSltGPFAssmued());
                pstmt.setString(33, employee.getGpfno());
                if (acctype != null && acctype.equals("GIA")) {
                    pstmt.setString(34, "N");
                } else {
                    pstmt.setString(34, employee.getStopPayNPS());
                }
                pstmt.setString(35, employee.getSpluserCategory());

                pstmt.setString(36, employee.getEmpid());
                pstmt.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pstmt);
                pstmt = conn.prepareStatement("INSERT INTO  monitor_emp_log ( login_by,login_date,emp_id,sql_text_query) values(?,?,?,?)");
                pstmt.setString(1, dcLoginId);
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(Cdate).getTime()));
                pstmt.setString(3, employee.getEmpid());
                pstmt.setString(4, sqlString);
                resultQry = pstmt.executeUpdate();
                //System.out.println("resultQry::" + resultQry + "::" + ";" + employee.getEmpType());
                if (resultQry > 0 && employee.getEmpType().equals("A")) {
                    String sql = "Select cur_spc from emp_mast where emp_id=?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, employee.getEmpid());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String spc = null;

                        if (rs.getString("cur_spc") != null && !rs.getString("cur_spc").equals("")) {
                            spc = rs.getString("cur_spc");
                        } else {
                            spc = employee.getEmpid();
                        }
                        //String spc = rs.getString("cur_spc");
                        String empid = employee.getEmpid();
                        pstmt = conn.prepareStatement("delete from section_post_mapping where (spc=? or spc=?)");
                        pstmt.setString(1, spc);
                        pstmt.setString(2, empid);
                        pstmt.execute();
                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return returnString;
    }

    @Override
    public ArrayList getDDOEmployeeList(String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList offEmpList = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT EMPPOST.EMP_ID,EMPPOST.GPF_NO,EMPPOST.CUR_SPC,EMPPOST.GPC,GPOST.POST,TRIM(ARRAY_TO_STRING(ARRAY[EMPPOST.INITIALS, EMPPOST.F_NAME, EMPPOST.M_NAME,EMPPOST.L_NAME], ' ')) FULL_NAME FROM\n"
                    + "(SELECT EMP.EMP_ID,EMP.GPF_NO,EMP.CUR_SPC,GSPC.GPC,GSPC,SPN,EMP.INITIALS,EMP.F_NAME,EMP.M_NAME,EMP.L_NAME FROM \n"
                    + "(SELECT * FROM EMP_MAST WHERE CUR_OFF_CODE=? )EMP\n"
                    + "INNER JOIN\n"
                    + "(SELECT * FROM G_SPC) GSPC\n"
                    + "ON\n"
                    + "EMP.CUR_SPC=GSPC.SPC)EMPPOST\n"
                    + "INNER JOIN\n"
                    + "(SELECT * FROM G_POST WHERE ISAUTHORITY='Y' ORDER BY POST)GPOST\n"
                    + "ON\n"
                    + "EMPPOST.GPC=GPOST.POST_CODE order by EMPPOST.f_name");
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("FULL_NAME"));
                offEmpList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
        return offEmpList;
    }

    @Override
    public String employeeCommunicationCount(String officeStatus, String empId, String officeCode) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;

        String returnCount = "0";
        String returnViewCount = "0";
        try {
            conn = dataSource.getConnection();
            if (officeStatus.equals("Y")) {
                pstmt = conn.prepareStatement("SELECT count(*) as totalCnt,is_viewed FROM employee_message WHERE off_code=? GROUP BY is_viewed ");
                pstmt.setString(1, officeCode);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (rs.getString("is_viewed") == null) {
                        returnCount = rs.getString("totalCnt");

                    } else {
                        returnViewCount = rs.getString("totalCnt");
                    }
                }
                DataBaseFunctions.closeSqlObjects(rs, pstmt);
            }
            pstmt = conn.prepareStatement("SELECT  count(*) as totalCnt,is_viewed FROM employee_message WHERE emp_id=? GROUP BY is_viewed ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("is_viewed") == null) {
                    returnCount = returnCount + rs.getString("totalCnt");
                } else {
                    returnViewCount = returnViewCount + rs.getString("totalCnt");
                }
            }

            returnCount = returnCount + "|" + returnViewCount;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return returnCount;
    }

    @Override
    public List viewCommunicationDetails(String officeStatus, String empId, String officeCode) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement pstmt = null;
        ResultSet rs1 = null;
        ArrayList EmployeeMessageList = new ArrayList();
        Employee employee = null;

        try {
            conn = dataSource.getConnection();
            if (officeStatus.equals("Y")) {
                st = conn.prepareStatement("SELECT  emp_mast.emp_id,message, "
                        + "message_on_date,message_id, viewed_on_date,message_title, off_code, is_viewed from employee_message "
                        + "INNER JOIN emp_mast ON employee_message.emp_id = emp_mast.emp_id where off_code=?   ORDER BY   message_on_date DESC");
                st.setString(1, officeCode);
                rs = st.executeQuery();
                while (rs.next()) {
                    EmployeeMessage message = new EmployeeMessage();
                    message.setEmpid(rs.getString("emp_id"));
                    message.setMessage(rs.getString("message"));
                    message.setMsgTitle(rs.getString("message_title"));
                    message.setOffcode(rs.getString("off_code"));
                    message.setIsviewed(rs.getString("is_viewed"));
                    message.setMessageId(rs.getInt("message_id"));
                    message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));
                    message.setViewondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("viewed_on_date")));
                    EmployeeMessageList.add(message);
                }
            }

            st = conn.prepareStatement("SELECT T1.message, T1.message_on_date as message_on_date, T1.message_id , T1.viewed_on_date, T1.off_code, T1.is_viewed,T1.message_title, T1.sender_name, T2.message_on_date as replieddate from "
                    + "(SELECT  message, message_on_date,message_id , viewed_on_date, off_code, is_viewed,message_title, getmsgsendername(sender_user_type,sender_id) as sender_name from employee_message "
                    + "  where employee_message.emp_id=?)T1  "
                    + "left outer join employee_message AS T2 on T1.message_id = T2.PARENT_MESSAGE_ID "
                    + "ORDER BY  T1.message_on_date DESC");
            st.setString(1, empId);

            rs = st.executeQuery();
            while (rs.next()) {
                EmployeeMessage message = new EmployeeMessage();

                message.setMessage(rs.getString("message"));
                message.setMsgTitle(rs.getString("message_title"));
                message.setOffcode(rs.getString("off_code"));
                message.setIsviewed(rs.getString("is_viewed"));
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderName(rs.getString("sender_name"));
                message.setRepliedondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("replieddate")));
                message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));
                message.setViewondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("viewed_on_date")));
                EmployeeMessageList.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return EmployeeMessageList;
    }

    @Override
    public List viewCommunicationMessage(String officeStatus, String empId, String officeCode, int messageId) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement pstmt = null;
        ResultSet rs1 = null;
        ArrayList EmployeeMessageList = new ArrayList();
        Employee employee = null;

        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT get_empname_from_type(employee_message.emp_id,receiver_user_type) receiver_name,get_empname_from_type(employee_message.sender_id,sender_user_type) sender_name, message, "
                    + "message_on_date,message_id , viewed_on_date, off_code, is_viewed,message_title, sender_id,sender_user_type from employee_message "
                    + " where message_id=? or parent_message_id=? ORDER BY  message_on_date DESC");
            st.setInt(1, messageId);
            st.setInt(2, messageId);
            pstmt = conn.prepareStatement("update employee_message set is_viewed=?,viewed_on_date=?  where emp_id=? AND message_id=? AND is_viewed IS NULL");
            pstmt.setString(1, "Y");
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, empId);
            pstmt.setInt(4, messageId);
            pstmt.executeUpdate();

            rs = st.executeQuery();
            while (rs.next()) {
                EmployeeMessage message = new EmployeeMessage();

                message.setReceiverName(rs.getString("receiver_name"));
                message.setSenderName(rs.getString("sender_name"));
                message.setMessage(rs.getString("message"));
                message.setMsgTitle(rs.getString("message_title"));
                message.setOffcode(rs.getString("off_code"));
                message.setIsviewed(rs.getString("is_viewed"));
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getString("sender_id"));
                message.setSenderUserType(rs.getString("sender_user_type"));
                message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));
                message.setViewondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("viewed_on_date")));
                message.setAttachements(getMessageAttachment(rs.getInt("message_id"), "MESSAGE"));
                EmployeeMessageList.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return EmployeeMessageList;
    }

    public EmployeeMessage downloadCommunicationMessage(int messageId) {
        EmployeeMessage employeeMessage = new EmployeeMessage();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement pstmt = null;
        ResultSet rs1 = null;
        try {
            conn = this.repodataSource.getConnection();
            st = conn.prepareStatement("SELECT get_empname_from_type(employee_message.emp_id,receiver_user_type) receiver_name,get_empname_from_type(employee_message.sender_id,sender_user_type) sender_name, message, message_title,"
                    + " message_on_date,message_id, sender_id,sender_user_type from employee_message "
                    + " where message_id=? ORDER BY  message_on_date DESC");
            st.setInt(1, messageId);

            rs = st.executeQuery();
            if (rs.next()) {

                employeeMessage.setReceiverName(rs.getString("receiver_name"));
                employeeMessage.setSenderName(rs.getString("sender_name"));
                employeeMessage.setMessage(rs.getString("message"));
                employeeMessage.setMsgTitle(rs.getString("message_title"));
                employeeMessage.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));
                employeeMessage.setMessageId(rs.getInt("message_id"));
                employeeMessage.setSenderId(rs.getString("sender_id"));
                employeeMessage.setSenderUserType(rs.getString("sender_user_type"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return employeeMessage;
    }

    @Override
    public EmployeeMessage getParentMessageDetail(int messageId) {
        EmployeeMessage message = new EmployeeMessage();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM employee_message WHERE message_id=?");
            pstmt.setInt(1, messageId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                message.setOffcode(rs.getString("off_code"));
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getString("sender_id"));
                message.setEmpid(rs.getString("emp_id"));
                message.setSenderUserType(rs.getString("sender_user_type"));
                message.setReceiverUserType(rs.getString("sender_user_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return message;
    }

    @Override
    public ArrayList getEmployeePostCode(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList empPost = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT POST,POST_CODE,EMP_ID, TRIM(ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')) FULL_NAME  FROM EMP_MAST , G_SPC, G_POST\n"
                    + "WHERE EMP_MAST.CUR_SPC=G_SPC.SPC AND G_SPC.GPC=G_POST.POST_CODE\n"
                    + "AND EMP_ID=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setPostcode(rs.getString("POST_CODE"));
                employee.setPost(rs.getString("POST"));
                empPost.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
        return empPost;

    }

    @Override
    public void updateEmpRegularToSixYrContractual(String empid) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("UPDATE EMP_MAST SET IS_REGULAR='C' WHERE EMP_ID=? AND IS_REGULAR='Y' AND DEP_CODE='02'");
            pst.setString(1, empid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst, conn);
        }

    }

    @Override
    public ArrayList getContractualJobCategory() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement stmt = null;
        ArrayList ar = new ArrayList();
        SelectOption so = null;
        SearchEmployee sEmp = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT * FROM G_TEMP_JOBTYPE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sEmp = new SearchEmployee();
                sEmp.setContJobType(rs.getString("tempjobtype"));
                sEmp.setJobTypeId(rs.getString("jobtype_code"));
                ar.add(sEmp);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
        return ar;
    }

    @Override
    public void saveContractualEmpData(SearchEmployee employee) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE EMP_MAST SET CUR_SPC=NULL, DEP_CODE='02',POST_NOMENCLATURE=?,JOB_TYPE_ID=?, IS_REGULAR='N' WHERE EMP_ID=?");
            pstmt.setString(1, employee.getDesignation());
            pstmt.setInt(2, Integer.parseInt(employee.getJobTypeId()));
            pstmt.setString(3, employee.getHidEmpid());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
    }

    @Override
    public Employee EditEmpoyeeDataAdmin(String empid) {
        Employee employee = new Employee();
        employee.setEmpid(empid);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement("SELECT *,to_char(dob, 'YYYY-mm-dd') as dobformat FROM emp_mast WHERE emp_id=?");
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setIntitals(result.getString("initials"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                employee.setGender(result.getString("GENDER"));
                employee.setDob(result.getString("dobformat"));
                employee.setMobile(result.getString("MOBILE"));
                employee.setAccttype(result.getString("acct_type"));
                employee.setGpfno(result.getString("gpf_no"));
                employee.setBasic(result.getInt("cur_basic_salary"));
                employee.setPayScale(result.getString("cur_salary"));
                employee.setGp(result.getInt("gp"));
                employee.setOfficecode(result.getString("cur_off_code"));
                employee.setIsRegular(result.getString("is_regular"));
                employee.setPostGrpType(result.getString("post_grp_type"));
                employee.setDateofjoining(CommonFunctions.getFormattedOutputDate1(result.getTimestamp("doe_gov")));
                employee.setEmpid(empid);
            }
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public String updateEmployeeDataAdmin(Employee employee, String dcLoginId) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;

        String isDuplicateGPF = "N";
        String returnString = null;
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);
            conn = dataSource.getConnection();
            String acctype = employee.getAccttype();
            String empId = "";
            String selectQry = "SELECT EMP_ID FROM EMP_MAST WHERE GPF_NO =?";
            pstmt = conn.prepareStatement(selectQry);
            pstmt.setString(1, employee.getGpfno());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                empId = rs.getString("EMP_ID");
                if (!empId.equals(employee.getEmpid())) {
                    isDuplicateGPF = "Y";
                }
            }

            returnString = isDuplicateGPF;
            if (isDuplicateGPF.equals("N")) {
                pstmt = conn.prepareStatement("update emp_mast set gpf_no=?,acct_type=?,initials=?,f_name=?,m_name=?,l_name=?,gender=?,dob=?,mobile=?,cur_basic_salary=?,cur_salary=?,gp=?,cur_off_code=?,post_grp_type=? where emp_id=? ");
                if (employee.getGpfno() != null && !employee.getGpfno().equals("")) {
                    pstmt.setString(1, employee.getGpfno().trim());
                }
                pstmt.setString(2, acctype);
                pstmt.setString(3, employee.getIntitals());
                pstmt.setString(4, employee.getFname().toUpperCase());
                pstmt.setString(5, employee.getMname().toUpperCase());
                pstmt.setString(6, employee.getLname().toUpperCase());
                pstmt.setString(7, employee.getGender());
                pstmt.setTimestamp(8, new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(employee.getDob()).getTime()));
                pstmt.setString(9, employee.getMobile());
                pstmt.setInt(10, employee.getBasic());
                pstmt.setString(11, employee.getPayScale());
                pstmt.setInt(12, employee.getGp());
                pstmt.setString(13, employee.getOfficecode());
                pstmt.setString(14, employee.getPostGrpType());
                pstmt.setString(15, employee.getEmpid());
                pstmt.executeUpdate();
                String sqlString = ToStringBuilder.reflectionToString(pstmt);
                pstmt = conn.prepareStatement("INSERT INTO  monitor_emp_log ( login_by,login_date,emp_id,sql_text_query) values(?,?,?,?)");
                pstmt.setString(1, dcLoginId);
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(Cdate).getTime()));
                pstmt.setString(3, employee.getEmpid());
                pstmt.setString(4, sqlString);
                pstmt.executeUpdate();
                // pstmt = conn.prepareStatement("delete from user_details   where linkid=?");
                // pstmt.setString(1, employee.getEmpid());
                // pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(pstmt, rs, conn);
        }
        return returnString;
    }

    public ArrayList empDepType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empDepList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_deploy_type");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("deploy_code"));
                so.setLabel(rs.getString("deploy_type"));
                empDepList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empDepList;
    }

    public ArrayList empPayScale() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empPayscaleList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_payscale");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("payscale"));
                so.setLabel(rs.getString("payscale"));
                empPayscaleList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empPayscaleList;
    }

    @Override
    public void updateProfileData(Employee emp, String empId, boolean isCmpleted) {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String employeeId = null;
        String Cdesignation = "";
        String Idesignation = "";
        try {
            conn = dataSource.getConnection();

            if (emp.getIfprofileVerified() == null || emp.getIfprofileVerified().equals("N") || emp.getIfprofileVerified().equals("")) {

                pst = conn.prepareStatement("update emp_mast set gistype=?,gisno=?,m_status=?,res_cat=?,height=?,bl_grp=?,"
                        + "home_town=?,religion=?,domicile=?,id_mark=?,mobile=?,"
                        + "gender=?,if_profile_completed=?,post_grp_type=?,if_gpf_assumed=?,staff_type=?,staff_category=?,staff_placement=?,cdesignation=?"
                        + ",idesignation=?,pscyear=?,email_id=?, has_no_email=? where emp_id=?");
                pst.setString(1, emp.getGisType());
                pst.setString(2, emp.getGisNo());

                pst.setString(3, emp.getMarital());
                pst.setString(4, emp.getCategory());
                pst.setDouble(5, emp.getHeight());
                pst.setString(6, emp.getBloodgrp());
                pst.setString(7, emp.getHomeTown());
                pst.setString(8, emp.getReligion());
                pst.setString(9, emp.getDomicil());
                pst.setString(10, emp.getIdmark());
                pst.setString(11, emp.getMobile());

                pst.setString(12, emp.getGender());

                if (isCmpleted == true) {
                    pst.setString(13, "Y");
                } else {
                    pst.setString(13, "N");
                }
                pst.setString(14, emp.getPostGrpType());
                /*if (emp.getRadyear1() != null && !emp.getRadyear1().equals("")) {
                 pst.setString(14, emp.getPostGrpType());
                 } else {
                 pst.setString(14, null);
                 }*/
                pst.setString(15, emp.getSltGPFAssmued());
                if (emp.getStaffType() != null && !emp.getStaffType().equals("")) {
                    pst.setString(16, emp.getStaffType());
                } else {
                    pst.setString(16, "");
                }
                if (emp.getStaffCategory() != null && !emp.getStaffCategory().equals("")) {
                    pst.setString(17, emp.getStaffCategory());
                } else {
                    pst.setString(17, "");
                }
                if (emp.getStaffPlacement() != null && !emp.getStaffPlacement().equals("")) {
                    pst.setString(18, emp.getStaffPlacement());
                    if (emp.getcDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Cdesignation = emp.getcDesignation();
                    }
                    if (emp.getiDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Idesignation = emp.getiDesignation();
                    }
                } else {
                    pst.setString(18, "");
                }
                if (emp.getcDesignation() != null && !emp.getcDesignation().equals("")) {
                    pst.setString(19, Cdesignation);
                } else {
                    pst.setString(19, "");
                }
                if (emp.getiDesignation() != null && !emp.getiDesignation().equals("")) {
                    pst.setString(20, Idesignation);
                } else {
                    pst.setString(20, "");
                }
                if (emp.getPscYear() != null && !emp.getPscYear().equals("")) {
                    pst.setString(21, emp.getPscYear());
                } else {
                    pst.setString(21, "");
                }

                if (emp.getEmail() != null && !emp.getEmail().equals("")) {
                    pst.setString(22, emp.getEmail());
                } else {
                    pst.setString(22, "");
                }
                if (emp.getHasNoEmail() != null && !emp.getHasNoEmail().equals("")) {
                    pst.setString(23, emp.getHasNoEmail());
                } else {
                    pst.setString(23, "");
                }

                pst.setString(24, empId);
            }

            if (emp.getIfprofileVerified() != null && emp.getIfprofileVerified().equals("Y")) {

                pst = conn.prepareStatement("update emp_mast set staff_type=?,staff_category=?,staff_placement=?,cdesignation=?,idesignation=?,pscyear=? where emp_id=?");
                if (emp.getStaffType() != null && !emp.getStaffType().equals("")) {
                    pst.setString(1, emp.getStaffType());
                } else {
                    pst.setString(1, "");
                }
                if (emp.getStaffCategory() != null && !emp.getStaffCategory().equals("")) {
                    pst.setString(2, emp.getStaffCategory());
                } else {
                    pst.setString(2, "");
                }
                if (emp.getStaffPlacement() != null && !emp.getStaffPlacement().equals("")) {

                    pst.setString(3, emp.getStaffPlacement());
                    if (emp.getcDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Cdesignation = emp.getcDesignation();
                    }
                    if (emp.getiDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Idesignation = emp.getiDesignation();
                    }
                } else {
                    pst.setString(3, "");
                }
                if (emp.getcDesignation() != null && !emp.getcDesignation().equals("")) {
                    pst.setString(4, Cdesignation);
                } else {
                    pst.setString(4, "");
                }
                if (emp.getiDesignation() != null && !emp.getiDesignation().equals("")) {
                    pst.setString(5, Idesignation);
                } else {
                    pst.setString(5, "");
                }
                if (emp.getPscYear() != null && !emp.getPscYear().equals("")) {
                    pst.setString(6, emp.getPscYear());
                } else {
                    pst.setString(6, "");
                }

                pst.setString(7, empId);
            }

            int no = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, conn);
        }

    }

    @Override
    public void updateDDOProfileData(Employee emp, String empId, boolean isCmpleted) {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String employeeId = null;
        String Cdesignation = "";
        String Idesignation = "";
        try {
            conn = dataSource.getConnection();

            if (emp.getIfprofileVerified() == null || emp.getIfprofileVerified().equals("N") || emp.getIfprofileVerified().equals("")) {
                pst = conn.prepareStatement("update emp_mast set gistype=?,gisno=?,dos=?,m_status=?,res_cat=?,height=?,bl_grp=?,"
                        + "home_town=?,religion=?,domicile=?,id_mark=?,mobile=?,joindate_of_goo=?,doe_gov=?,jointime_of_goo=?,toe_gov=?,"
                        + "gender=?,if_profile_completed=?,post_grp_type=?,dos_age=?,if_gpf_assumed=?,staff_type=?,staff_category=?,staff_placement=?,cdesignation=?,idesignation=?,pscyear=?,email_id=? where emp_id=?");
                pst.setString(1, emp.getGisType());
                pst.setString(2, emp.getGisNo());
                if (emp.getTxtDos() != null && !emp.getTxtDos().equals("")) {
                    pst.setTimestamp(3, new Timestamp(sdf.parse(emp.getTxtDos()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, emp.getMarital());
                pst.setString(5, emp.getCategory());
                pst.setDouble(6, emp.getHeight());
                pst.setString(7, emp.getBloodgrp());
                pst.setString(8, emp.getHomeTown());
                pst.setString(9, emp.getReligion());
                pst.setString(10, emp.getDomicil());
                pst.setString(11, emp.getIdmark());
                pst.setString(12, emp.getMobile());
                if (emp.getJoindategoo() != null && !emp.getJoindategoo().equals("")) {
                    pst.setTimestamp(13, new Timestamp(sdf.parse(emp.getJoindategoo()).getTime()));
                } else {
                    pst.setTimestamp(13, null);
                }
                if (emp.getDoeGov() != null && !emp.getDoeGov().equals("")) {
                    pst.setTimestamp(14, new Timestamp(sdf.parse(emp.getDoeGov()).getTime()));
                } else {
                    pst.setTimestamp(14, null);
                }
                pst.setString(15, emp.getTxtwefTime());
                pst.setString(16, emp.getTimeOfEntryGoo());
                pst.setString(17, emp.getGender());

                if (isCmpleted == true) {
                    pst.setString(18, "Y");
                } else {
                    pst.setString(18, "N");
                }
                pst.setString(19, emp.getPostGrpType());
                if (emp.getRadyear1() != null && !emp.getRadyear1().equals("")) {
                    pst.setInt(20, Integer.parseInt(emp.getRadyear1()));
                } else {
                    pst.setInt(20, 0);
                }
                pst.setString(21, emp.getSltGPFAssmued());
                if (emp.getStaffType() != null && !emp.getStaffType().equals("")) {
                    pst.setString(22, emp.getStaffType());
                } else {
                    pst.setString(22, "");
                }
                if (emp.getStaffCategory() != null && !emp.getStaffCategory().equals("")) {
                    pst.setString(23, emp.getStaffCategory());
                } else {
                    pst.setString(23, "");
                }
                if (emp.getStaffPlacement() != null && !emp.getStaffPlacement().equals("")) {
                    pst.setString(24, emp.getStaffPlacement());
                    if (emp.getcDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Cdesignation = emp.getcDesignation();
                    }
                    if (emp.getiDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Idesignation = emp.getiDesignation();
                    }
                } else {
                    pst.setString(24, "");
                }
                if (emp.getcDesignation() != null && !emp.getcDesignation().equals("")) {
                    pst.setString(25, Cdesignation);
                } else {
                    pst.setString(25, "");
                }
                if (emp.getiDesignation() != null && !emp.getiDesignation().equals("")) {
                    pst.setString(26, Idesignation);
                } else {
                    pst.setString(26, "");
                }
                if (emp.getPscYear() != null && !emp.getPscYear().equals("")) {
                    pst.setString(27, emp.getPscYear());
                } else {
                    pst.setString(27, "");
                }

                if (emp.getEmail() != null && !emp.getEmail().equals("")) {
                    pst.setString(28, emp.getEmail());
                } else {
                    pst.setString(28, "");
                }

                pst.setString(29, empId);
            }

            if (emp.getIfprofileVerified() != null && emp.getIfprofileVerified().equals("Y")) {

                pst = conn.prepareStatement("update emp_mast set staff_type=?,staff_category=?,staff_placement=?,cdesignation=?,idesignation=?,pscyear=? where emp_id=?");
                if (emp.getStaffType() != null && !emp.getStaffType().equals("")) {
                    pst.setString(1, emp.getStaffType());
                } else {
                    pst.setString(1, "");
                }
                if (emp.getStaffCategory() != null && !emp.getStaffCategory().equals("")) {
                    pst.setString(2, emp.getStaffCategory());
                } else {
                    pst.setString(2, "");
                }
                if (emp.getStaffPlacement() != null && !emp.getStaffPlacement().equals("")) {

                    pst.setString(3, emp.getStaffPlacement());
                    if (emp.getcDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Cdesignation = emp.getcDesignation();
                    }
                    if (emp.getiDesignation() != null && emp.getStaffPlacement().equals("Placement from State Scale")) {
                        Idesignation = emp.getiDesignation();
                    }
                } else {
                    pst.setString(3, "");
                }
                if (emp.getcDesignation() != null && !emp.getcDesignation().equals("")) {
                    pst.setString(4, Cdesignation);
                } else {
                    pst.setString(4, "");
                }
                if (emp.getiDesignation() != null && !emp.getiDesignation().equals("")) {
                    pst.setString(5, Idesignation);
                } else {
                    pst.setString(5, "");
                }
                if (emp.getPscYear() != null && !emp.getPscYear().equals("")) {
                    pst.setString(6, emp.getPscYear());
                } else {
                    pst.setString(6, "");
                }

                pst.setString(7, empId);
            }

            int no = pst.executeUpdate();

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, conn);
        }

    }

    @Override
    public void updateProfileCompletedStatus(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE EMP_MAST SET if_profile_completed=? WHERE EMP_ID=?");
            pstmt.setString(1, "Y");
            pstmt.setString(2, empId);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getEmployeeProfileForVerificationDataList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empPayscaleList = new ArrayList();
        Employee emp = new Employee();
        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement(" select emp.emp_id, emp.acct_type, emp.gpf_no, TRIM(ARRAY_TO_STRING(ARRAY[emp.INITIALS, emp.F_NAME, emp.M_NAME, emp.L_NAME], ' ')) FULL_NAME , dob, post, if_profile_completed, if_profile_verified FROM emp_mast emp  "
             + " left outer join g_spc on emp.cur_spc=g_spc.spc "
             + " left outer join g_post on g_spc.gpc=g_post.post_code "
             + " where emp.cur_off_code=? and (emp.if_retired is null or emp.if_retired='N' or emp.if_reengaged='Y')"
             + " order by  emp.F_NAME ");*/
            String sql = "select A.* from ((select * from (select f_name,emp.emp_id, emp.acct_type, emp.gpf_no, TRIM(ARRAY_TO_STRING(ARRAY[emp.INITIALS, emp.F_NAME, emp.M_NAME, emp.L_NAME], ' ')) FULL_NAME , dob, post, if_profile_completed, if_profile_verified FROM emp_mast emp"
                    + " left outer join g_spc on emp.cur_spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " where emp.cur_off_code=? and (dep_code='02' or dep_code='03' or dep_code='04' or dep_code='05' or dep_code='06' or dep_code='07') and (emp.if_retired is null or emp.if_retired='N' or emp.if_reengaged='Y'))temp)"
                    + " union"
                    + " (select * from (select f_name,emp.emp_id, emp.acct_type, emp.gpf_no, TRIM(ARRAY_TO_STRING(ARRAY[emp.INITIALS, emp.F_NAME, emp.M_NAME, emp.L_NAME], ' ')) FULL_NAME , dob, post, if_profile_completed, if_profile_verified FROM emp_mast emp"
                    + " left outer join g_spc on emp.cur_spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join g_office on emp.cur_off_code=g_office.off_code"
                    + " where g_office.p_off_code=? and (dep_code='02' or dep_code='03' or dep_code='04' or dep_code='05' or dep_code='06' or dep_code='07') and is_ddo != 'Y' "
                    + " )temp1)) as A order by A.f_name";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, offcode);
            pstmt.setString(2, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emp = new Employee();
                emp.setEmpid(rs.getString("emp_id"));
                emp.setAccttype(rs.getString("acct_type"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("FULL_NAME"));
                emp.setCurDesg(rs.getString("post"));
                emp.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                emp.setIfprofileCompleted(rs.getString("if_profile_completed"));
                emp.setIfprofileVerified(rs.getString("if_profile_verified"));
                empPayscaleList.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empPayscaleList;
    }

    @Override
    public Employee getEmployeeProfileDraftData(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empPayscaleList = new ArrayList();
        Employee emp = new Employee();
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT emp.emp_id, emp.gpf_no, TRIM(ARRAY_TO_STRING(ARRAY[emp.INITIALS, emp.F_NAME, emp.M_NAME, emp.L_NAME], ' ')) FULL_NAME,"
                    + " emp.dob, is_approved, post FROM emp_profile_draft emp "
                    + " left outer join emp_mast on emp.emp_id= emp_mast.emp_id "
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc "
                    + " left outer join g_post on g_spc.gpc=g_post.post_code "
                    + " where emp.emp_id=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emp = new Employee();
                emp.setEmpid(rs.getString("emp_id"));
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setEmpName(rs.getString("FULL_NAME"));
                emp.setCurDesg(rs.getString("post"));
                emp.setDob(rs.getString("dob"));
                emp.setIsApproved(rs.getString("is_approved"));
                empPayscaleList.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    @Override
    public boolean isprofileCompleted(String empId) {
        Connection con = null;
        boolean completed = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT GPF_NO, ACCT_TYPE, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, GENDER, M_STATUS, res_cat, CUR_BASIC_SALARY, HEIGHT, DOB, JOINDATE_OF_GOO, DOS, BL_GRP, RELIGION,  PH_CODE, ID_MARK, MOBILE, CUR_CADRE_CODE, POST_GRP_TYPE, DOE_GOV, HOME_TOWN, domicile, branch_code, bank_acc_no  FROM EMP_MAST WHERE EMP_ID=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("GPF_NO") != null && !rs.getString("GPF_NO").equals("")) {
                    completed = true;
                }
                if (completed == true && rs.getString("ACCT_TYPE") != null && !rs.getString("ACCT_TYPE").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }

                if (completed == true && rs.getString("GENDER") != null && !rs.getString("GENDER").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("M_STATUS") != null && !rs.getString("M_STATUS").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                /*
                 if (completed == true && rs.getString("res_cat") != null && !rs.getString("res_cat").equals("")) {
                 completed = true;
                 } else {
                 completed = false;
                 }*/
                if (completed == true && rs.getString("HEIGHT") != null && !rs.getString("HEIGHT").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("DOB") != null && !rs.getString("DOB").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("JOINDATE_OF_GOO") != null && !rs.getString("JOINDATE_OF_GOO").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("BL_GRP") != null && !rs.getString("BL_GRP").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }

                if (completed == true && rs.getString("MOBILE") != null && !rs.getString("MOBILE").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("POST_GRP_TYPE") != null && !rs.getString("POST_GRP_TYPE").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("DOE_GOV") != null && !rs.getString("DOE_GOV").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("HOME_TOWN") != null && !rs.getString("HOME_TOWN").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("domicile") != null && !rs.getString("domicile").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                if (completed == true && rs.getString("ID_MARK") != null && !rs.getString("ID_MARK").equals("")) {
                    completed = true;
                } else {
                    completed = false;
                }
                /*if (completed == true && rs.getString("bank_acc_no") != null && !rs.getString("bank_acc_no").equals("")) {
                 completed = true;
                 } else {
                 completed = false;
                 }
                 if (completed == true && rs.getString("branch_code") != null && !rs.getString("branch_code").equals("")) {
                 completed = true;
                 } else {
                 completed = false;
                 }*/
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT address_type, address, bl_code, vill_code, po_code, ps_code, pin  FROM EMP_ADDRESS WHERE EMP_ID=? and address_type='PERMANENT' ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("address_type") != null && !rs.getString("address_type").equals("")) {

                    if (completed == true && rs.getString("address") != null && !rs.getString("address").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }
                    if (completed == true && rs.getString("bl_code") != null && !rs.getString("bl_code").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }
                    if (completed == true && rs.getString("pin") != null && !rs.getString("pin").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }

                } else {
                    completed = false;
                }
            } else {
                completed = false;
            }

            ps = con.prepareStatement("SELECT address_type, address, bl_code, vill_code, po_code, ps_code, pin  FROM EMP_ADDRESS WHERE EMP_ID=?  and address_type='PRESENT' ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("address_type") != null && !rs.getString("address_type").equals("")) {

                    if (completed == true && rs.getString("address") != null && !rs.getString("address").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }
                    if (completed == true && rs.getString("bl_code") != null && !rs.getString("bl_code").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }
                    if (completed == true && rs.getString("pin") != null && !rs.getString("pin").equals("")) {
                        completed = true;
                    } else {
                        completed = false;
                    }

                } else {
                    completed = false;
                }
            } else {
                completed = false;
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT count(*) cnt FROM emp_id_doc WHERE EMP_ID=? and id_description='AADHAAR'");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") == 1) {

                    if (completed == true) {
                        completed = true;
                    }

                } else {
                    completed = false;
                }
            } else {
                completed = false;
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);
            ps = con.prepareStatement("SELECT LANGUAGE, IF_READ, IF_WRITE, IF_SPEAK, IF_MLANG FROM EMP_LANGUAGE WHERE EMP_ID=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (completed == true) {
                    completed = true;
                }
            } else {
                completed = false;
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT relation  FROM emp_relation WHERE EMP_ID=? AND (relation='FATHER' OR relation='HUSBAND')");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (completed == true) {
                    completed = true;
                }
            } else {
                completed = false;
            }

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("SELECT count(*) cnt  FROM emp_qualification WHERE emp_id=? ");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (completed == true && rs.getInt("cnt") > 0) {
                    completed = true;
                } else {
                    completed = false;
                }
            } else {
                completed = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return completed;
    }

    @Override
    public List getPostingInformation(String empId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TransferJoining tj = new TransferJoining();
        List li = new ArrayList();
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select join_date, join_time, spn,memo_no, memo_date  from emp_join "
                    + " inner join g_spc on emp_join.spc=g_spc.spc "
                    + " where if_visible='Y' and emp_id=? order by join_date desc");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                tj = new TransferJoining();
                tj.setJoindate(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                tj.setJoinspn(rs.getString("spn"));
                tj.setOrderno(rs.getString("memo_no"));
                tj.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("memo_date")));

                li.add(tj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return li;
    }

    @Override
    public void saveAddress(Address address) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            if (address.getAddressId() > 0 && address.getAddressType().equals("PERMANENT")) {
                pst = con.prepareStatement("UPDATE emp_address set address=?,bl_code=?,vill_code=?,po_code=?,ps_code=?,pin=?,state_code=?,dist_code=?,std_code=?,tphone=?,address_type=? WHERE emp_id=? and address_id=?");

                pst.setString(1, address.getAddress());
                pst.setString(2, address.getBlockCode());
                pst.setString(3, address.getVillageCode());
                pst.setString(4, address.getPostCode());
                pst.setString(5, address.getPsCode());
                pst.setString(6, address.getPin());
                pst.setString(7, address.getStateCode());
                pst.setString(8, address.getDistCode());
                pst.setString(9, address.getStdCode());
                pst.setString(10, address.getTelephone());
                pst.setString(11, address.getAddressType());
                pst.setString(12, address.getEmpId());
                pst.setInt(13, address.getAddressId());
                pst.executeUpdate();
            }
            if (address.getAddressId() == 0 && address.getAddressType().equals("PERMANENT")) {
                pst = con.prepareStatement("INSERT INTO emp_address (emp_id, address_type, address, bl_code, vill_code, po_code, ps_code, pin, state_code, dist_code, std_code, tphone) values (?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, address.getEmpId());
                pst.setString(2, address.getAddressType());
                pst.setString(3, address.getAddress());
                pst.setString(4, address.getBlockCode());
                pst.setString(5, address.getVillageCode());
                pst.setString(6, address.getPostCode());
                pst.setString(7, address.getPsCode());
                pst.setString(8, address.getPin());
                pst.setString(9, address.getStateCode());
                pst.setString(10, address.getDistCode());
                pst.setString(11, address.getStdCode());
                pst.setString(12, address.getTelephone());
                pst.executeUpdate();
            }
            if (address.getPrAddressId() > 0 && address.getPrAddressType().equals("PRESENT")) {
                pst = con.prepareStatement("UPDATE emp_address set address=?,bl_code=?,vill_code=?,po_code=?,ps_code=?,pin=?,state_code=?,dist_code=?,std_code=?,tphone=?,address_type=? WHERE emp_id=? and address_id=?");

                pst.setString(1, address.getPrAddress());
                pst.setString(2, address.getPrBlockCode());
                pst.setString(3, address.getPrVillageCode());
                pst.setString(4, address.getPrPostCode());
                pst.setString(5, address.getPrPsCode());
                pst.setString(6, address.getPrPin());
                pst.setString(7, address.getPrStateCode());
                pst.setString(8, address.getPrDistCode());
                pst.setString(9, address.getPrStdCode());
                pst.setString(10, address.getPrTelephone());
                pst.setString(11, address.getPrAddressType());
                pst.setString(12, address.getEmpId());
                pst.setInt(13, address.getPrAddressId());
                pst.executeUpdate();
            }
            if (address.getPrAddressId() == 0 && address.getPrAddressType().equals("PRESENT")) {
                pst = con.prepareStatement("INSERT INTO emp_address (emp_id, address_type, address, bl_code, vill_code, po_code, ps_code, pin, state_code, dist_code, std_code, tphone) values (?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, address.getEmpId());
                pst.setString(2, address.getPrAddressType());
                pst.setString(3, address.getPrAddress());
                pst.setString(4, address.getPrBlockCode());
                pst.setString(5, address.getPrVillageCode());
                pst.setString(6, address.getPrPostCode());
                pst.setString(7, address.getPrPsCode());
                pst.setString(8, address.getPrPin());
                pst.setString(9, address.getPrStateCode());
                pst.setString(10, address.getPrDistCode());
                pst.setString(11, address.getPrStdCode());
                pst.setString(12, address.getPrTelephone());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveProfileVerificationData(Employee emp, String loginid, String mode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (mode.equals("Approve")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET if_profile_verified='Y',approved_by=?,approved_on=? where emp_id=?");
                pst.setString(1, loginid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, emp.getEmpid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE emp_language SET is_locked='Y' where emp_id=?");
                pst.setString(1, emp.getEmpid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE emp_id_doc SET is_locked='Y' where emp_id=?");
                pst.setString(1, emp.getEmpid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE emp_address SET is_locked='Y' where emp_id=?");
                pst.setString(1, emp.getEmpid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE emp_relation SET is_locked='Y' where emp_id=?");
                pst.setString(1, emp.getEmpid());
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE emp_qualification SET is_locked='Y' where emp_id=?");
                pst.setString(1, emp.getEmpid());
                pst.executeUpdate();

            } else if (mode.equals("Decline")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET verifier_remarks=?,if_profile_completed='N' where emp_id=?");
                pst.setString(1, emp.getTxtDeclineReason());
                pst.setString(2, emp.getEmpid());
                pst.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst);

                int logid = 0;

                String sql = "SELECT * FROM equarter.hrms_profile_complete_log WHERE emp_id=? ORDER BY complete_log_id DESC LIMIT 1";
                pst = con.prepareStatement(sql);
                pst.setString(1, emp.getEmpid());
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void deleteEmployeeEducation(Education education) {

        Connection con = null;

        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("DELETE FROM emp_qualification WHERE qfn_id=? and emp_id=?");
            pstmt.setInt(1, education.getQfn_id());
            pstmt.setString(2, education.getEmpId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrayList empPostgrptype() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empPostgrptype = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_post_group");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("post_group"));
                so.setLabel(rs.getString("post_group"));
                empPostgrptype.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empPostgrptype;
    }

    @Override
    public void deleteLanguageKnown(String empid, int slno) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_LANGUAGE WHERE EMP_ID=? AND sl_no=?");
            pst.setString(1, empid);
            pst.setInt(2, slno);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public EmployeeLanguage editLanguage(String empid, int slno) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmployeeLanguage emplanguage = new EmployeeLanguage();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM EMP_LANGUAGE WHERE EMP_ID=? AND sl_no=?");
            pst.setString(1, empid);
            pst.setInt(2, slno);
            rs = pst.executeQuery();
            if (rs.next()) {
                emplanguage.setLanguage(rs.getString("LANGUAGE"));
                emplanguage.setIfread(rs.getString("IF_READ"));
                emplanguage.setIfspeak(rs.getString("IF_SPEAK"));
                emplanguage.setIfwrite(rs.getString("IF_WRITE"));
                emplanguage.setIfmlang(rs.getString("IF_MLANG"));
                emplanguage.setSlno(rs.getInt("sl_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplanguage;
    }

    @Override
    public void updateLanguageKnown(String empid, EmployeeLanguage language) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_LANGUAGE SET LANGUAGE=?, IF_READ=?, IF_WRITE=?, IF_SPEAK=?, IF_MLANG=? WHERE EMP_ID=? AND sl_no=?");
            pst.setString(1, language.getLanguage());
            pst.setString(2, language.getIfread());
            pst.setString(3, language.getIfwrite());
            pst.setString(4, language.getIfspeak());
            pst.setString(5, language.getIfmlang());
            pst.setString(6, empid);
            pst.setInt(7, language.getSlno());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Employee getEmployeeProfileForDC(String empid) {

        Employee employee = new Employee();
        String fullName = "";

        employee.setEmpid(empid);

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT cadre_name,rec_source,allotment_year,GPF_NO,ACCT_TYPE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME,M_NAME,L_NAME,GENDER,G_MARITAL.M_STATUS,EMP_MAST.RES_CAT,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC, G_SPC.SPN,    "
                    + "                          DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,OFF_NAME,OFF_EN,BL_GRP,emp_mast.RELIGION RELIGION_ID,g_religion.RELIGION RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, EMP_MAST.GP,     "
                    + "                          CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID, CHEST, WEIGHT, LEFT_VISION, RIGHT_VISION, BRASS_NO,DOE_GOV,HOME_TOWN,if_employed_res,if_employed_rehab,    "
                    + "                          DIST_NAME,jointime_of_goo,toe_gov,gistype,scheme_name,gisno,domicile,EMP_MAST.bank_code,G_BANK.BANK_NAME,G_BRANCH.BRANCH_NAME,EMP_MAST.branch_code,bank_acc_no,G_MARITAL.m_status maritalstatus,G_MARITAL.int_marital_status_id,G_SCHEME.SCHEME_NAME,if_profile_completed,G_SCHEME.SCHEME_ID,if_profile_verified FROM     "
                    + "                          (SELECT rec_source,allotment_year,GPF_NO,ACCT_TYPE,INITIALS,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,RES_CAT,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,    "
                    + "                          BL_GRP,RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID,     "
                    + "                          CHEST, WEIGHT, LEFT_VISION, RIGHT_VISION, BRASS_NO,DOE_GOV,HOME_TOWN,if_employed_res,if_employed_rehab,jointime_of_goo,toe_gov,    "
                    + "                          gistype,gisno,domicile,bank_code,branch_code,bank_acc_no,if_profile_completed,if_profile_verified FROM EMP_MAST WHERE EMP_ID=?)EMP_MAST     "
                    + "                          LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE     "
                    + "                          LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC     "
                    + "                          LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE     "
                    + "                          LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE     "
                    + "                          LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE     "
                    + "                          LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE = G_DISTRICT.DIST_CODE "
                    + "                          LEFT OUTER JOIN G_BANK ON EMP_MAST.BANK_CODE = G_BANK.BANK_CODE "
                    + "LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE = G_BRANCH.BRANCH_CODE "
                    + "			LEFT OUTER JOIN G_MARITAL ON EMP_MAST.m_status = G_MARITAL.int_marital_status_id "
                    + "			LEFT OUTER JOIN G_SCHEME ON EMP_MAST.gistype = G_SCHEME.SCHEME_ID"
                    + "                 LEFT OUTER JOIN g_religion ON EMP_MAST.religion=g_religion.int_religion_id";

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("F_NAME") != null && !result.getString("F_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("F_NAME");
                }
                if (result.getString("M_NAME") != null && !result.getString("M_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("M_NAME");
                }
                if (result.getString("L_NAME") != null && !result.getString("L_NAME").equals("")) {
                    fullName = fullName + " " + result.getString("L_NAME");
                }

                if (result.getString("EMPNAME") != null && !result.getString("EMPNAME").equals("")) {
                    fullName = result.getString("EMPNAME");
                } else {
                    fullName = "<span style=\"color:#FF0000;\">Name needs to be Updated</span>";
                }

                employee.setEmpid(empid);
                employee.setEmpName(fullName);
                employee.setCadreId(result.getString("cadre_name"));
                employee.setRecsource(result.getString("rec_source"));
                employee.setCardeallotmentyear(result.getString("allotment_year"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));
                if (result.getString("GENDER") != null && !result.getString("GENDER").equals("")) {
                    employee.setGender(result.getString("GENDER"));
                } else {
                    employee.setGender("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setMarital(result.getString("int_marital_status_id"));
                if (result.getString("RES_CAT") != null && !result.getString("RES_CAT").equals("")) {
                    employee.setCategory(result.getString("RES_CAT"));
                } else {
                    employee.setCategory("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                if (result.getString("HEIGHT") != null && !result.getString("HEIGHT").equals("")) {
                    employee.setHeight(result.getInt("HEIGHT"));
                } else {
                    employee.setHeight(0);
                }
                employee.setGpfno(result.getString("GPF_NO"));
                if (result.getString("ACCT_TYPE") != null && !result.getString("ACCT_TYPE").equals("")) {
                    employee.setAccttype(result.getString("ACCT_TYPE"));
                } else {
                    employee.setAccttype("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setBasic(result.getInt("CUR_BASIC_SALARY"));
                /*message.setMessageondate(CommonFunctions.getFormattedOutputDate1(rs.getDate("message_on_date")));*/
                if (result.getDate("DOB") != null && !result.getDate("DOB").equals("")) {
                    employee.setDob(CommonFunctions.getFormattedOutputDate1(result.getDate("DOB")));
                } else {
                    employee.setDob("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setTxtDos(CommonFunctions.getFormattedOutputDate1(result.getDate("DOS")));
                if (result.getDate("DOB") != null && !result.getDate("DOB").equals("")) {
                    int ymd[] = Numtowordconvertion.getDateParts(result.getDate("DOB").toString());
                    employee.setDobText(Numtowordconvertion.getFormattedDOB(ymd[0], ymd[1], ymd[2]));
                } else {
                    employee.setDobText("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                if (result.getDate("JOINDATE_OF_GOO") != null && !result.getDate("JOINDATE_OF_GOO").equals("")) {
                    int yjoindata[] = Numtowordconvertion.getDateParts(result.getDate("JOINDATE_OF_GOO").toString());
                    employee.setJoinDateText(Numtowordconvertion.getFormattedDOB(yjoindata[0], yjoindata[1], yjoindata[2]));
                } else {
                    employee.setJoinDateText("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                if (result.getDate("JOINDATE_OF_GOO") != null && !result.getDate("JOINDATE_OF_GOO").equals("")) {
                    employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(result.getDate("JOINDATE_OF_GOO")));
                } else {
                    employee.setJoindategoo("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setDor(formatDate(result.getDate("DOS")));
                employee.setDepartment(result.getString("DEPARTMENT_NAME"));
                employee.setDeptcode(result.getString("DEPT_CODE"));
                employee.setPost(result.getString("POST"));
                employee.setPostcode(result.getString("GPC"));
                employee.setSpn(result.getString("SPN"));
                employee.setSpc(result.getString("CUR_SPC"));
                employee.setOffice(result.getString("OFF_NAME"));
                employee.setOffice(result.getString("OFF_EN"));
                employee.setDistrict(result.getString("DIST_NAME"));
                employee.setOfficecode(result.getString("CUR_OFF_CODE"));
                if (result.getString("BL_GRP") != null && !result.getString("BL_GRP").equals("")) {
                    employee.setBloodgrp(result.getString("BL_GRP"));
                } else {
                    employee.setBloodgrp("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setReligion(result.getString("RELIGION_ID"));
                if (result.getString("RELIGION") != null && !result.getString("RELIGION").equals("")) {
                    employee.setReligionName(result.getString("RELIGION"));
                } else {
                    employee.setReligionName("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                //employee.setPermanentdist(result.getString("PRM_DIST_CODE"));
                //employee.setPermanentps(result.getString("PRM_PS_CODE"));
                employee.setPhyhandicapt(result.getString("PH_CODE"));
                if (result.getString("ID_MARK") != null && !result.getString("ID_MARK").equals("")) {
                    employee.setIdmark(result.getString("ID_MARK"));
                } else {
                    employee.setIdmark("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                if (result.getString("MOBILE") != null && !result.getString("MOBILE").equals("")) {
                    employee.setMobile(result.getString("MOBILE"));
                } else {
                    employee.setMobile("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                //employee.setPrmTelNo(result.getString("PRM_TPHONE"));
                employee.setDateOfCurPosting(formatDate(result.getDate("CURR_POST_DOJ")));
                employee.setGp(result.getInt("GP"));
                employee.setPayScale(result.getString("CUR_SALARY"));
                employee.setCadreCode(result.getString("CUR_CADRE_CODE"));
                //employee.setPostGrpType(result.getString("POST_GRP_TYPE"));
                if (result.getString("POST_GRP_TYPE") != null && !result.getString("POST_GRP_TYPE").equals("")) {
                    employee.setPostGrpType(result.getString("POST_GRP_TYPE"));
                } else {
                    employee.setPostGrpType("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setFieldOffCode(result.getString("FIELD_OFF_CODE"));
                employee.setEmail(result.getString("EMAIL_ID"));
                employee.setChest(result.getString("CHEST"));
                employee.setWeight(result.getString("WEIGHT"));
                employee.setLeftvision(result.getString("LEFT_VISION"));
                employee.setRightvision(result.getString("RIGHT_VISION"));
                employee.setBrassno(result.getString("BRASS_NO"));
                if (result.getDate("DOE_GOV") != null && !result.getDate("DOE_GOV").equals("")) {
                    employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(result.getDate("DOE_GOV")));
                } else {
                    employee.setDoeGov("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                if (result.getDate("DOE_GOV") != null) {
                    int ydoe[] = Numtowordconvertion.getDateParts(result.getDate("DOE_GOV").toString());
                    employee.setEntryGovDateText(Numtowordconvertion.getFormattedDOB(ydoe[0], ydoe[1], ydoe[2]));
                }
                if (result.getString("HOME_TOWN") != null && !result.getString("HOME_TOWN").equals("")) {
                    employee.setHomeTown(result.getString("HOME_TOWN"));
                } else {
                    employee.setHomeTown("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setIfReservation(result.getString("if_employed_res"));
                employee.setIfRehabiltation(result.getString("if_employed_rehab"));
                employee.setTxtwefTime(result.getString("jointime_of_goo"));
                employee.setTimeOfEntryGoo(result.getString("toe_gov"));
                employee.setGisNo(result.getString("gisno"));
                employee.setGisType(result.getString("gistype"));
                if (result.getString("domicile") != null && !result.getString("domicile").equals("")) {
                    employee.setDomicil(result.getString("domicile"));
                } else {
                    employee.setDomicil("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setSltBank(result.getString("bank_code"));

                employee.setBankName(result.getString("BANK_NAME"));
                if (result.getString("branch_code") != null && !result.getString("branch_code").equals("")) {
                    employee.setBranchName(result.getString("branch_name"));
                } else {
                    employee.setBranchName("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }

                if (result.getString("bank_acc_no") != null && !result.getString("bank_acc_no").equals("")) {
                    employee.setBankaccno(result.getString("bank_acc_no"));
                } else {
                    employee.setBankaccno("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setGisName(result.getString("SCHEME_NAME"));
                if (result.getString("int_marital_status_id") != null && !result.getString("int_marital_status_id").equals("")) {
                    employee.setMaritalStatus(result.getString("maritalstatus"));
                } else {
                    employee.setMaritalStatus("<span style=\"color:#FF0000;\">Needs to be Updated</span>");
                }
                employee.setIfprofileCompleted(result.getString("if_profile_completed"));
                employee.setIfprofileVerified(result.getString("if_profile_verified"));
            }

        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public List getFamilyDetails(String empid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        ArrayList familyData = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT * FROM emp_relation WHERE emp_id = ?");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            while (result.next()) {
                employeeFamilyHelperBean family = new employeeFamilyHelperBean();
                family.setRelation(result.getString("relation"));
                family.setFname(result.getString("f_name"));
                family.setMname(result.getString("m_name"));
                family.setLname(result.getString("l_name"));
                familyData.add(family);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return familyData;
    }

    @Override
    public Employee getEmployeeProfileDetails(String empid) {
        Employee employee = new Employee();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT *, getfullname(emp_id) fullName, getpostnamefromspc(cur_spc) post, getbankname(bank_code) bankname, getbranchname(branch_code) branchname, getpannumber(emp_id, 'PAN') panno, getpannumber(emp_id, 'AADHAAR') aadhaarno, getpannumber(emp_id, 'DRIVING LICENCE') drivingLicence, getoffnamefromspc(cur_spc) office_name FROM EMP_MAST WHERE EMP_ID = ?");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            if (result.next()) {
                employee.setEmpid(empid);
                employee.setEmpName("");
                employee.setCadreId(result.getString("cadre_name"));
                employee.setRecsource(result.getString("rec_source"));
                employee.setCardeallotmentyear(result.getString("allotment_year"));
                employee.setFname(result.getString("F_NAME"));
                employee.setMname(result.getString("M_NAME"));
                employee.setLname(result.getString("L_NAME"));

                employee.setBankName(result.getString("bankname"));
                employee.setBranchName(result.getString("branchname"));
                employee.setBankaccno(result.getString("bank_acc_no"));
                employee.setGender(result.getString("gender"));
                employee.setMobile(result.getString("mobile"));
                employee.setHomeTown(result.getString("home_town"));
                employee.setOffice(result.getString("office_name"));

                employee.setPostGrpType(result.getString("post_grp_type"));

                employee.setDob(CommonFunctions.getFormattedOutputDate2(result.getDate("dob")));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate2(result.getDate("doe_gov")));
                employee.setDoeGov(CommonFunctions.getFormattedOutputDate2(result.getDate("joindate_of_goo")));
                employee.setPost(result.getString("post"));

                employee.setHomeTown(result.getString("home_town"));
                employee.setSpc(result.getString("cur_spc"));
                employee.setAadhaarno(result.getString("aadhaarno"));
                employee.setPanno(result.getString("panno"));
                employee.setDrivingLicence(result.getString("drivingLicence"));

                /////////////////////////////////
                String MaritalStatus = null;
                String mStatus = result.getString("m_status");
                if (mStatus != null && !mStatus.equals("")) {
                    if (mStatus.equals("1")) {
                        MaritalStatus = "MARRIED";
                    } else if (mStatus.equals("2")) {
                        MaritalStatus = "SINGLE";
                    } else if (mStatus.equals("3")) {
                        MaritalStatus = "DIVORCED";
                    } else if (mStatus.equals("4")) {
                        MaritalStatus = "WIDOWER";
                    } else if (mStatus.equals("5")) {
                        MaritalStatus = "SEPARATED";
                    } else if (mStatus.equals("6")) {
                        MaritalStatus = "NA";
                    } else if (mStatus.equals("7")) {
                        MaritalStatus = "UNMARRIED";
                    } else if (mStatus.equals("8")) {
                        MaritalStatus = "WIDOW";
                    }
                }
                employee.setMaritalStatus(MaritalStatus);
                //////////////////////////////

                //employee.setSpn(empid);
            }
            pstmt = con.prepareStatement("SELECT address FROM EMP_ADDRESS WHERE emp_id =  ? and address_type = 'PERMANENT'");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            if (result.next()) {
                employee.setPermanentaddr(result.getString("address"));
            }

            pstmt = con.prepareStatement("SELECT address FROM EMP_ADDRESS WHERE emp_id =  ? and address_type = 'PRESENT'");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            if (result.next()) {
                employee.setPresentaddr(result.getString("address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return employee;
    }

    @Override
    public int deleteProfileAddress(int addressId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM emp_address WHERE address_id =?");
            pst.setInt(1, addressId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int deleteEmployeeAddress(int addressId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM emp_address WHERE address_id =?");
            pst.setInt(1, addressId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public boolean checkDuplicateIdentity(String empId, String identityType, String idno) {

        Connection con = null;
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT * FROM EMP_ID_DOC WHERE  EMP_ID<>? AND ID_DESCRIPTION=? AND ID_NO=? ");
            pstmt.setString(1, empId);
            pstmt.setString(2, identityType);
            pstmt.setString(3, idno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return found;
    }

    @Override
    public boolean duplicateMobile(String empId, String mobile) {
        Connection con = null;
        boolean found = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT * FROM EMP_MAST WHERE  EMP_ID<>? AND MOBILE=? ");
            pstmt.setString(1, empId);
            pstmt.setString(2, mobile);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return found;
    }

    @Override
    public String isProfileVerified(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String ifProfileVerified = "N";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT if_profile_verified FROM EMP_MAST WHERE EMP_ID = ?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_profile_verified") != null && rs.getString("if_profile_verified").equals("Y")) {
                    ifProfileVerified = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ifProfileVerified;
    }

    @Override
    public ArrayList DDOWiseprofileUpdate(String dcode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        String stm1 = null;
        String stm2 = null;
        try {
            con = this.repodataSource.getConnection();
            stm = "SELECT count(*) as total_emp,  off_en,off_code FROM emp_mast inner join g_office on cur_off_code=g_office.off_code   \n"
                    + "where (IF_RETIRED IS NULL or IF_REENGAGED='Y') AND hide_for_report='N' AND g_office.department_code=? AND g_office.is_ddo='Y' GROUP BY off_en,off_code ORDER by off_en";

            ps1 = con.prepareStatement(stm);
            ps1.setString(1, dcode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("off_en"));
                billstatus.setTotalEmp(rs1.getInt("total_emp"));
                billstatus.setDdoCode(rs1.getString("off_code"));
                String offcode = rs1.getString("off_code");

                stm1 = "SELECT count(*) as total_completed,  off_en FROM emp_mast inner join g_office on cur_off_code=g_office.off_code   \n"
                        + "where  g_office.is_ddo='Y' AND (IF_RETIRED IS NULL or IF_REENGAGED='Y') AND hide_for_report='N' AND if_profile_completed='Y' AND  g_office.off_code=?   GROUP BY off_en";
                ps2 = con.prepareStatement(stm1);
                ps2.setString(1, offcode);
                rs2 = ps2.executeQuery();
                int totalComp = 0;
                while (rs2.next()) {
                    totalComp = rs2.getInt("total_completed");
                }
                billstatus.setTotalCompleted(totalComp);
                stm2 = "SELECT count(*) as total_verified,off_en FROM emp_mast inner join g_office on cur_off_code=g_office.off_code  \n"
                        + "where  g_office.is_ddo='Y' AND (IF_RETIRED IS NULL or IF_REENGAGED='Y') AND hide_for_report='N' AND if_profile_verified='Y' AND  g_office.off_code=?   GROUP BY off_en";
                ps = con.prepareStatement(stm2);
                ps.setString(1, offcode);
                rs = ps.executeQuery();
                int totalVerified = 0;
                while (rs.next()) {
                    totalVerified = rs.getInt("total_verified");
                }
                billstatus.setTotalVerified(totalVerified);
                outList.add(billstatus);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1, ps, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList EmpwiseprofileUpdate(String ddoCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        List list = null;
        ArrayList outList = new ArrayList();
        ArrEmpBillGroup eList = null;
        List empList = null;
        String stm = null;
        String stm1 = null;
        String stm2 = null;
        try {
            con = this.repodataSource.getConnection();
            stm = "SELECT emp_id,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,post,if_profile_completed,if_profile_verified,is_regular FROM emp_mast LEFT OUTER  JOIN g_spc ON emp_mast.cur_spc=g_spc.spc LEFT OUTER JOIN g_post ON g_post.post_code=g_spc.gpc WHERE emp_mast.cur_off_code=? AND (IF_RETIRED IS NULL or IF_REENGAGED='Y')";

            ps1 = con.prepareStatement(stm);
            ps1.setString(1, ddoCode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                String Status = "";
                BillStatus billstatus = new BillStatus();
                billstatus.setEmpName(rs1.getString("EMPNAME"));
                billstatus.setEmpPost(rs1.getString("post"));
                billstatus.setIsCompleted(rs1.getString("if_profile_completed"));
                billstatus.setIsVerified(rs1.getString("if_profile_verified"));
                billstatus.setEmpId(rs1.getString("emp_id"));
                String isRegStatus = rs1.getString("is_regular");
                if (isRegStatus.equals("C")) {
                    Status = "6 Year Contractual";
                } else if (isRegStatus.equals("Y")) {
                    Status = "Regular";
                } else {
                    Status = "Contractual";
                }
                billstatus.setIsRegular(Status);
                outList.add(billstatus);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DeptWiseProfileUpdate() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        ArrayList outList = new ArrayList();
        String stm = null;
        try {
            con = this.repodataSource.getConnection();
            stm = ("select totalemp,department_name,g_department.department_code,getEmpProfileCompelted(g_department.department_code) as ProfileCompleted,getEmpProfileVerified(g_department.department_code) as ProfileVerified from "
                    + "(select sum(total_emp) as totalemp,department_code from (SELECT count(*) as total_emp,  cur_off_code FROM emp_mast where (IF_RETIRED IS NULL or IF_REENGAGED='Y') AND hide_for_report='N' group by cur_off_code)as t1 "
                    + "inner join g_office on t1.cur_off_code=g_office.off_code AND g_office.is_ddo='Y'  group by department_code) as t2 RIGHT OUTER  join g_department on t2.department_code= g_department.department_code where g_department.if_active='Y' ORDER by department_name");
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                BillStatus billstatus = new BillStatus();
                billstatus.setDepartmentname(rs1.getString("department_name"));
                billstatus.setTotalEmp(rs1.getInt("totalemp"));
                String deptCode = rs1.getString("department_code");
                billstatus.setDeptCode(deptCode);
                billstatus.setTotalCompleted(rs1.getInt("ProfileCompleted"));
                billstatus.setTotalVerified(rs1.getInt("ProfileVerified"));
                outList.add(billstatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public String getAadhaarAndPanData(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;

        String idInfo = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT id_no,ID_DESCRIPTION FROM EMP_ID_DOC WHERE EMP_ID=? AND (ID_DESCRIPTION='AADHAAR' OR ID_DESCRIPTION='PAN') ORDER BY ID_DESCRIPTION");
            ps.setString(1, empId);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("ID_DESCRIPTION").equalsIgnoreCase("AADHAAR")) {
                    idInfo = rs1.getString("id_no");
                }

                if (rs1.getString("ID_DESCRIPTION").equalsIgnoreCase("PAN")) {
                    if (idInfo != null && !idInfo.equals("")) {
                        idInfo = idInfo + "@" + rs1.getString("id_no");
                    } else {
                        idInfo = " " + "@" + rs1.getString("id_no");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return idInfo;
    }

    private boolean ifLeapYear(int theYear) {
        boolean res = false;
        try {
            // Is theYear Divisible by 4?
            if (theYear % 4 == 0) {
                // Is theYear Divisible by 4 but not 100?
                if (theYear % 100 != 0) {

                    res = true;
                } // Is theYear Divisible by 4 and 100 and 400?
                else if (theYear % 400 == 0) {

                    res = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public PensionProfile getpensionProfileDetails(String empid) {
        PensionProfile employee = new PensionProfile();
        PensionNomineeList nomineelist = new PensionNomineeList();
        PensionFamilyList familylist = new PensionFamilyList();
        String fullName = "";
        String employeeId = null;
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet result = null;
        ResultSet result2 = null;
        ResultSet result3 = null;
        ResultSet rs = null;
        ResultSet result1 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String selectQry = "";
        ArrayList outList = new ArrayList();
        ArrayList outList1 = new ArrayList();
        try {
            conn = dataSource.getConnection();

            String SQL = "SELECT EMP_MAST.emp_id,GPF_NO,ACCT_TYPE,initials, G_TITLE.int_title, F_NAME,M_NAME,L_NAME,GENDER,G_MARITAL.M_STATUS,EMP_MAST.RES_CAT,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,id_no,empIdDoc.idno2,ifsc_code,   "
                    + "   DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,g_office.ddo_code, OFF_NAME,g_office.TR_CODE,OFF_EN,BL_GRP,emp_mast.RELIGION RELIGION_ID,g_religion.RELIGION RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, EMP_MAST.GP,     "
                    + "  CUR_SALARY, EMAIL_ID, DOE_GOV,HOME_TOWN,    "
                    + "  int_district_id,jointime_of_goo,toe_gov,gistype,scheme_name,gisno,domicile,EMP_MAST.bank_code,G_BANK.BANK_NAME,G_BRANCH.BRANCH_NAME,EMP_MAST.branch_code,bank_acc_no,G_MARITAL.m_status maritalstatus,G_MARITAL.int_marital_status_id,G_SCHEME.SCHEME_NAME,if_profile_completed,G_SCHEME.SCHEME_ID,if_profile_verified,dos_age,if_gpf_assumed FROM     "
                    + "  (SELECT EMP_MAST.emp_id,id_no,INITIALS,rec_source,allotment_year,GPF_NO,ACCT_TYPE,F_NAME,M_NAME,L_NAME,GENDER,M_STATUS,RES_CAT,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,    "
                    + "  BL_GRP,RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID,     "
                    + "  DOE_GOV,HOME_TOWN,jointime_of_goo,toe_gov,    "
                    + "  gistype,gisno,domicile,bank_code,branch_code,bank_acc_no,if_profile_completed,if_profile_verified,dos_age,if_gpf_assumed FROM EMP_MAST LEFT OUTER JOIN emp_id_doc ON EMP_MAST.emp_id = emp_id_doc.emp_id AND id_description='PAN' WHERE (EMP_MAST.EMP_ID=? OR EMP_MAST.gpf_no=?))EMP_MAST     "
                    + "  LEFT OUTER JOIN (select emp_id,id_no as idno2 from emp_id_doc where id_description='ELECTION ID CARD') as empIdDoc ON EMP_MAST.emp_id = empIdDoc.emp_id     "
                    + "  LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE     "
                    + "  LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC     "
                    + "  LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE     "
                    + "  LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE     "
                    + "  LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE     "
                    + "  LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE = G_DISTRICT.DIST_CODE "
                    + "  LEFT OUTER JOIN G_BANK ON EMP_MAST.BANK_CODE = G_BANK.BANK_CODE "
                    + "  LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE = G_BRANCH.BRANCH_CODE "
                    + "  LEFT OUTER JOIN G_MARITAL ON EMP_MAST.m_status = G_MARITAL.int_marital_status_id "
                    + "  LEFT OUTER JOIN G_SCHEME ON EMP_MAST.gistype = G_SCHEME.SCHEME_ID"
                    + "  LEFT OUTER JOIN g_religion ON EMP_MAST.religion=g_religion.int_religion_id"
                    + "  LEFT OUTER JOIN G_TITLE ON EMP_MAST.initials=G_TITLE.title ";
            //  System.out.println("SQL is:" + SQL);
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                empid = result.getString("emp_id");
                employee.setEmployeeFirstName(result.getString("F_NAME"));
                employee.setEmployeeMiddleName(result.getString("M_NAME"));
                employee.setEmployeeLastName(result.getString("L_NAME"));
                employee.setHrmsEmpId(empid);

                employee.setTpfAcNo(getNumberFromGPF(result.getString("GPF_NO")));
                employee.setTpfSeries(CommonFunctions.getGPFSeries(result.getString("GPF_NO")));
                if (result.getString("int_title") != null && !result.getString("int_title").equals("")) {
                    employee.setSalutationEmp(result.getString("int_title"));
                } else {
                    employee.setSalutationEmp("Other");
                }

                employee.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result.getDate("DOB")));
                employee.setRetirementDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(result.getDate("DOS")));

                if (result.getString("gender") != null && !result.getString("gender").equals("")) {
                    if (result.getString("gender").equals("M")) {
                        employee.setSex("M");
                    } else if (result.getString("gender").equals("F")) {
                        employee.setSex("F");
                    } else if (result.getString("gender").equals("T")) {
                        employee.setSex("O");
                    }
                } else {
                    employee.setSex("O");
                }

                employee.setPenCategoryId("1");
                employee.setPenTypeId("1");

                String mStatus = result.getString("M_STATUS");
                if (mStatus != null && !mStatus.equals("")) {
                    if (mStatus.toUpperCase().equals("MARRIED")) {
                        employee.setIntMaritalStatusTypeId("1");
                    } else if (mStatus.toUpperCase().equals("UNMARRIED") || mStatus.toUpperCase().equals("SINGLE") || mStatus.toUpperCase().equals("SEPARATED")) {
                        employee.setIntMaritalStatusTypeId("2");
                    } else if (mStatus.toUpperCase().equals("DIVORCED")) {
                        employee.setIntMaritalStatusTypeId("3");
                    } else if (mStatus.toUpperCase().equals("WIDOWED") || mStatus.toUpperCase().equals("WIDOWER") || mStatus.toUpperCase().equals("WIDOW")) {
                        employee.setIntMaritalStatusTypeId("4");
                    } else {
                        employee.setIntMaritalStatusTypeId("0");
                    }
                }

                if (result.getString("RELIGION_ID") != null && !result.getString("RELIGION_ID").equals("") && (result.getString("RELIGION_ID").equals("1") || result.getString("RELIGION_ID").equals("2") || result.getString("RELIGION_ID").equals("3"))) {
                    employee.setReligionId(result.getString("RELIGION_ID"));
                } else {
                    employee.setReligionId("0");
                }

                employee.setNationalityId("1");

                if (result.getString("EMAIL_ID") != null && !result.getString("EMAIL_ID").equals("")) {
                    employee.setMailId(result.getString("EMAIL_ID"));
                } else {
                    employee.setMailId("");
                }

                employee.setMobileNo(result.getString("MOBILE"));
                employee.setPenIdnMark(result.getString("ID_MARK"));
                employee.setPenIdnMark2("NA");
                employee.setPanNo(result.getString("id_no"));

                if (result.getString("idno2") != null && !result.getString("idno2").equals("")) {
                    employee.setIdentityDocId("2");
                    employee.setIdentityDocNumber(result.getString("idno2"));
                } else {
                    employee.setIdentityDocId("");
                    employee.setIdentityDocNumber("");
                }

                employee.setDesignationId("");
                employee.setCvp("Y");
                employee.setCvpPercentage("40");
                employee.setFinalGpfAppliedDate("");
                employee.setFinalGpfAppliedFlag("Y");
                employee.setFinalGpfAppliedLetterNo("");
                employee.setHeight(result.getString("HEIGHT"));
                employee.setIfscCode(result.getString("ifsc_code"));
                employee.setBankAcctNo(result.getString("bank_acc_no"));
                String banBarnch = result.getString("bank_name") + " " + result.getString("branch_name");
                employee.setBankBranch(banBarnch);
                employee.setBsrCode(result.getString("branch_code"));
                employee.setTreasuryCode(result.getString("tr_code"));
                employee.setDdoName(result.getString("ddo_code"));
                employee.setDdoId(result.getString("ddo_code"));

                String districyCode = "";
                String perAddpin = "";
                String peraddstateid = "";
                String perAddcity = "";
                String perAddTown = "";
                String perAddpoliceStation = "";
                String cdistricyId = "";
                String comAddpin = "";
                String comaddstateid = "";
                String comAddcity = "";
                String comAddTown = "";
                String comAddpoliceStation = "";

                String SQLAsddress = "SELECT address_type,address,village_name,emp_address.pin,ps_name,int_district_id,po_name FROM emp_address LEFT JOIN g_village ON g_village.vill_code=emp_address.vill_code LEFT JOIN g_ps ON g_ps.ps_code=emp_address.ps_code LEFT JOIN g_district ON g_district.dist_code=emp_address.dist_code LEFT JOIN g_po ON g_po.po_code=emp_address.po_code  WHERE  emp_id=?";
                pstmt2 = conn.prepareStatement(SQLAsddress);
                pstmt2.setString(1, empid);
                result2 = pstmt2.executeQuery();
                while (result2.next()) {
                    if (result2.getString("address_type").equals("PERMANENT")) {
                        districyCode = result2.getString("int_district_id");
                        perAddpin = result2.getString("pin");
                        if (result2.getString("address") != null && !result2.getString("address").equals("")) {
                            if (result2.getString("address").length() > 50) {
                                perAddcity = result2.getString("address").substring(0, 49);
                            } else {
                                perAddcity = result2.getString("address");
                            }

                        } else {
                            perAddcity = "";
                        }

                        perAddTown = result2.getString("village_name");
                        perAddpoliceStation = result2.getString("ps_name");
                    }
                    if (result2.getString("address_type").equals("PRESENT")) {
                        cdistricyId = result2.getString("int_district_id");

                        comAddpin = result2.getString("pin");
                        if (result2.getString("address") != null && !result2.getString("address").equals("")) {
                            if (result2.getString("address").length() > 50) {
                                comAddcity = result2.getString("address").substring(0, 49);
                            } else {
                                comAddcity = result2.getString("address");
                            }

                        } else {
                            comAddcity = "";
                        }

                        comAddTown = result2.getString("village_name");
                        comAddpoliceStation = result2.getString("ps_name");
                    }
                }
                //  System.out.println("==="+cdistricyCode);
                employee.setDistrictCode(districyCode);

                employee.setPerAddpin(perAddpin);
                employee.setPerAddstateId("7");
                employee.setPerAddcity(perAddcity);
                employee.setPerAddtown(perAddTown);
                employee.setPerAddpoliceStation(perAddpoliceStation);
                employee.setCommAddpin(comAddpin);
                employee.setCommAddstateId("7");
                employee.setCommAddtown(comAddTown);
                employee.setCommAddpoliceStation(comAddpoliceStation);
                employee.setCommAddcity(comAddcity);
                // employee.setcDistrictCode(cdistricyId);             
                employee.setCommDistrictCode(cdistricyId);

                String SQLFamily = "SELECT int_title,concat_ws (' ', f_name, m_name,l_name) AS name,bank_name,branch_name,G_BRANCH.ifsc_code,initials,* FROM  emp_relation "
                        + " LEFT OUTER JOIN G_TITLE ON emp_relation.initials=G_TITLE.title "
                        + " LEFT OUTER JOIN G_BANK ON emp_relation.BANK_CODE = G_BANK.BANK_CODE   LEFT OUTER JOIN G_BRANCH ON emp_relation.BRANCH_CODE = G_BRANCH.BRANCH_CODE  "
                        + " WHERE emp_id=?";
                pstmt = conn.prepareStatement(SQLFamily);
                pstmt.setString(1, empid);
                result1 = pstmt.executeQuery();
                while (result1.next()) {
                    familylist = new PensionFamilyList();

                    if (result1.getString("int_title") != null && !result1.getString("int_title").equals("")) {
                        familylist.setSalutationFamily(result1.getString("int_title"));
                    } else {
                        familylist.setSalutationFamily("Other");
                    }

                    familylist.setDependentName(result1.getString("name"));
                    if (result1.getString("gender") != null && !result1.getString("gender").equals("")) {
                        if (result1.getString("gender").equals("M")) {
                            familylist.setSex("M");
                        } else if (result1.getString("gender").equals("F")) {
                            familylist.setSex("F");
                        } else {
                            familylist.setSex("O");
                        }
                    } else {
                        familylist.setSex("O");
                    }

                    String familyrel = result1.getString("relation");
                    // familylist.setRelationshipId(result1.getString("relation"));
                    if (familyrel.toUpperCase().equals("FATHER")) {
                        familylist.setRelationshipId("1");
                    } else if (familyrel.toUpperCase().equals("MOTHER")) {
                        familylist.setRelationshipId("2");
                    } else if (familyrel.toUpperCase().equals("HUSBAND")) {
                        familylist.setRelationshipId("3");
                    } else if (familyrel.toUpperCase().equals("WIFE")) {
                        familylist.setRelationshipId("4");
                    } else if (familyrel.toUpperCase().equals("DAUGHTER")) {
                        familylist.setRelationshipId("5");
                    } else if (familyrel.toUpperCase().equals("SON")) {
                        familylist.setRelationshipId("6");
                    } else if (familyrel.toUpperCase().equals("MOTHER IN LAW")) {
                        familylist.setRelationshipId("13");
                    } else if (familyrel.toUpperCase().equals("FATHER IN LAW")) {
                        familylist.setRelationshipId("14");
                    } else {
                        familylist.setRelationshipId("0");
                    }
                    // System.out.println("==="+result1.getDate("dob"));

                    if (result1.getDate("dob") != null && !result1.getDate("dob").equals("")) {
                        familylist.setFamilyDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result1.getDate("dob")));
                    } else {
                        familylist.setFamilyDob("");
                    }

                    if (result1.getString("mobile") != null && !result1.getString("mobile").equals("")) {
                        familylist.setMobileNo(result1.getString("mobile"));
                    } else {
                        familylist.setMobileNo("");
                    }
                    if (result1.getString("ifsc_code") != null && !result1.getString("ifsc_code").equals("")) {
                        familylist.setIfscCode(result1.getString("ifsc_code"));
                    } else {
                        familylist.setIfscCode("");
                    }
                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                        familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                    } else {
                        familylist.setBankAccountNo("");
                    }
                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                        familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                    } else {
                        familylist.setBankAccountNo("");
                    }
                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                        familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                    } else {
                        familylist.setBankAccountNo("");
                    }
                    if (result1.getString("arrears_per") != null && !result1.getString("arrears_per").equals("")) {
                        familylist.setFamilySharePercentage(result1.getString("arrears_per"));
                    } else {
                        familylist.setFamilySharePercentage("");
                    }
                    if (result1.getString("bank_name") != null && !result1.getString("bank_name").equals("")) {
                        String familybanBarnch = result1.getString("bank_name") + " " + result1.getString("branch_name");
                        familylist.setBankBranchName(familybanBarnch);
                    } else {
                        familylist.setBankBranchName("");
                    }
                    if (result1.getString("remarks") != null && !result1.getString("remarks").equals("")) {
                        familylist.setFamilyRemarks(result1.getString("remarks"));
                    } else {
                        familylist.setFamilyRemarks("");
                    }
                    if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                        familylist.setMinorFlag("Yes");
                    } else {
                        familylist.setMinorFlag("");
                    }
                    if (result1.getString("minor_guardian") != null && !result1.getString("minor_guardian").equals("")) {
                        familylist.setGurdianName(result1.getString("minor_guardian"));
                    } else {
                        familylist.setGurdianName("");
                    }
                    if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                        familylist.setTiApplicableFlag("Yes");
                    } else {
                        familylist.setTiApplicableFlag("");
                    }
                    if (result1.getString("is_pwd") != null && !result1.getString("is_pwd").equals("")) {
                        familylist.setFamilyHandicappedFlag("Yes");
                    } else {
                        familylist.setFamilyHandicappedFlag("");
                    }
                    if (result1.getString("pwd_type") != null && !result1.getString("pwd_type").equals("")) {
                        familylist.setFamilyHandicappedTypeId(result1.getString("pwd_type"));
                    } else {
                        familylist.setFamilyHandicappedTypeId("");
                    }

                    String fmStatus = result1.getString("marital_status");
                    if (fmStatus != null && !fmStatus.equals("")) {
                        if (fmStatus.toUpperCase().equals("MARRIED")) {
                            familylist.setIntMaritalStatusTypeId("1");
                        } else if (fmStatus.toUpperCase().equals("UNMARRIED") || fmStatus.toUpperCase().equals("SINGLE") || fmStatus.toUpperCase().equals("SEPARATED")) {
                            familylist.setIntMaritalStatusTypeId("2");
                        } else if (fmStatus.toUpperCase().equals("DIVORCED")) {
                            familylist.setIntMaritalStatusTypeId("3");
                        } else if (fmStatus.toUpperCase().equals("WIDOWED") || fmStatus.toUpperCase().equals("WIDOWER") || fmStatus.toUpperCase().equals("WIDOW")) {
                            familylist.setIntMaritalStatusTypeId("4");
                        } else {
                            familylist.setIntMaritalStatusTypeId("0");
                        }
                    }
                    outList.add(familylist);
                }

                /**
                 * ********************** Guardian Details
                 * **********************
                 */
                String SQLGuardian = "SELECT int_title,CONCAT(F_NAME, ' ', M_NAME, ' ', L_NAME) AS full_name,initial_guardian,* FROM  emp_relation "
                        + " LEFT OUTER JOIN G_TITLE ON emp_relation.initial_guardian=G_TITLE.title  "
                        + " WHERE emp_id=? AND is_guardian=? LIMIT 1 ";
                pstmt = conn.prepareStatement(SQLGuardian);
                pstmt.setString(1, empid);
                pstmt.setString(2, "Y");
                result1 = pstmt.executeQuery();
                String guardianSaluta = "";
                String guardianFName = "";
                String guardianLName = "";
                String guardianMName = "";
                String guardianrelationId = "";

                if (result1.next()) {
                    if (result1.getString("int_title") != null && !result1.getString("int_title").equals("")) {
                        guardianSaluta = result1.getString("int_title");
                    } else {
                        guardianSaluta = "Other";
                    }
                    if (result1.getString("m_name") != null && !result1.getString("m_name").equals("")) {
                        guardianMName = result1.getString("m_name");
                    }
                    guardianLName = result1.getString("l_name");
                    guardianFName = result1.getString("f_name");
                    String guardianfamilyrel = result1.getString("relation");
                    // familylist.setRelationshipId(result1.getString("relation"));
                    if (guardianfamilyrel.toUpperCase().equals("FATHER")) {
                        guardianrelationId = "1";
                    } else if (guardianfamilyrel.toUpperCase().equals("MOTHER")) {
                        guardianrelationId = "2";
                    } else if (guardianfamilyrel.toUpperCase().equals("HUSBAND")) {
                        guardianrelationId = "3";
                    } else if (guardianfamilyrel.toUpperCase().equals("WIFE")) {
                        guardianrelationId = "4";
                    } else if (guardianfamilyrel.toUpperCase().equals("DAUGHTER")) {
                        guardianrelationId = "5";
                    } else if (guardianfamilyrel.toUpperCase().equals("SON")) {
                        guardianrelationId = "6";
                    } else if (guardianfamilyrel.toUpperCase().equals("MOTHER IN LAW")) {
                        guardianrelationId = "7";
                    } else if (guardianfamilyrel.toUpperCase().equals("FATHER IN LAW")) {
                        guardianrelationId = "8";
                    } else {
                        guardianrelationId = "0";
                    }
                }

                employee.setSalutation(guardianSaluta);
                employee.setGuardianFName(guardianFName);
                employee.setGuardianLName(guardianLName);
                employee.setGuardianMName(guardianMName);
                employee.setGuardianRelId(guardianrelationId);

                /**
                 * ********************* NomineeList
                 * ***************************
                 */
                String SQLNominee = "SELECT int_title,concat_ws (' ', f_name, m_name,l_name) AS name,bank_name,branch_name,initials,G_BRANCH.ifsc_code,* FROM  emp_relation "
                        + " LEFT OUTER JOIN G_TITLE ON emp_relation.initials=G_TITLE.title "
                        + " LEFT OUTER JOIN G_BANK ON emp_relation.BANK_CODE = G_BANK.BANK_CODE   LEFT OUTER JOIN G_BRANCH ON emp_relation.BRANCH_CODE = G_BRANCH.BRANCH_CODE  "
                        + " WHERE emp_id=? AND  (nomination_gpf IS NOT NULL OR  nomination_cvp IS NOT NULL)";
                pstmt = conn.prepareStatement(SQLNominee);
                pstmt.setString(1, empid);
                result1 = pstmt.executeQuery();
                while (result1.next()) {
                    nomineelist = new PensionNomineeList();

                    nomineelist.setPenBenefitTypeId("1");
                    nomineelist.setNomineeOriginalOrAlterId("O");

                    if (result1.getString("int_title") != null && !result1.getString("int_title").equals("")) {
                        nomineelist.setSalutationNominee(result1.getString("int_title"));
                    } else {
                        nomineelist.setSalutationNominee("Other");
                    }

                    nomineelist.setNomineeName(result1.getString("name"));
                    if (result1.getString("gender") != null && !result1.getString("gender").equals("")) {
                        if (result1.getString("gender").equals("M")) {
                            nomineelist.setFamilySex("M");
                        } else if (result1.getString("gender").equals("F")) {
                            nomineelist.setFamilySex("F");
                        } else {
                            nomineelist.setFamilySex("O");
                        }
                    } else {
                        nomineelist.setFamilySex("O");
                    }

                    String familyrel = result1.getString("relation");
                    // familylist.setRelationshipId(result1.getString("relation"));
                    if (familyrel.toUpperCase().equals("FATHER")) {
                        nomineelist.setRelationId("1");
                    } else if (familyrel.toUpperCase().equals("MOTHER")) {
                        nomineelist.setRelationId("2");
                    } else if (familyrel.toUpperCase().equals("HUSBAND")) {
                        nomineelist.setRelationId("3");
                    } else if (familyrel.toUpperCase().equals("WIFE")) {
                        nomineelist.setRelationId("4");
                    } else if (familyrel.toUpperCase().equals("DAUGHTER")) {
                        nomineelist.setRelationId("5");
                    } else if (familyrel.toUpperCase().equals("SON")) {
                        nomineelist.setRelationId("6");
                    } else if (familyrel.toUpperCase().equals("MOTHER IN LAW")) {
                        nomineelist.setRelationId("13");
                    } else if (familyrel.toUpperCase().equals("FATHER IN LAW")) {
                        nomineelist.setRelationId("14");
                    } else {
                        nomineelist.setRelationId("0");
                    }
                    // nomineelist.setNomineeMaritalStatusId(0);

                    if (result1.getDate("dob") != null && !result1.getDate("dob").equals("")) {
                        nomineelist.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result1.getDate("dob")));
                    } else {
                        nomineelist.setDob("");
                    }

                    if (result1.getString("mobile") != null && !result1.getString("mobile").equals("")) {
                        nomineelist.setMobileNo(result1.getString("mobile"));
                    } else {
                        nomineelist.setMobileNo("");
                    }
                    if (result1.getString("ifsc_code") != null && !result1.getString("ifsc_code").equals("")) {
                        nomineelist.setIfscCode(result1.getString("ifsc_code"));
                    } else {
                        nomineelist.setIfscCode("");
                    }
                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                        nomineelist.setBankAccountNo(result1.getString("bank_acc_no"));
                    } else {
                        nomineelist.setBankAccountNo("");
                    }
                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                        nomineelist.setBankAccountNo(result1.getString("bank_acc_no"));
                    } else {
                        nomineelist.setBankAccountNo("");
                    }

                    if (result1.getString("arrears_per") != null && !result1.getString("arrears_per").equals("")) {
                        nomineelist.setSharePercentage(result1.getString("arrears_per"));
                    } else {
                        nomineelist.setSharePercentage("");
                    }
                    if (result1.getString("bank_name") != null && !result1.getString("bank_name").equals("")) {
                        String familybanBarnch = result1.getString("bank_name") + " " + result1.getString("branch_name");
                        nomineelist.setBankBranchName(familybanBarnch);
                    } else {
                        nomineelist.setBankBranchName("");
                    }

                    if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                        nomineelist.setMinorFlag("Yes");
                    } else {
                        nomineelist.setMinorFlag("");
                    }
                    if (result1.getString("minor_guardian") != null && !result1.getString("minor_guardian").equals("")) {
                        nomineelist.setGurdianName(result1.getString("minor_guardian"));
                    } else {
                        nomineelist.setGurdianName("");
                    }

                    String fmStatus = result1.getString("marital_status");
                    if (fmStatus != null && !fmStatus.equals("")) {
                        if (fmStatus.toUpperCase().equals("MARRIED")) {
                            nomineelist.setNomineeMaritalStatusId("1");
                        } else if (fmStatus.toUpperCase().equals("UNMARRIED") || fmStatus.toUpperCase().equals("SINGLE") || fmStatus.toUpperCase().equals("SEPARATED")) {
                            nomineelist.setNomineeMaritalStatusId("2");
                        } else if (fmStatus.toUpperCase().equals("DIVORCED")) {
                            nomineelist.setNomineeMaritalStatusId("3");
                        } else if (fmStatus.toUpperCase().equals("WIDOWED") || fmStatus.toUpperCase().equals("WIDOW") || fmStatus.toUpperCase().equals("WIDOWER")) {
                            nomineelist.setNomineeMaritalStatusId("4");
                        } else {
                            nomineelist.setNomineeMaritalStatusId("0");
                        }
                    }

                    outList1.add(nomineelist);
                }

                /**
                 * ************************************** Previous Pension
                 * Details ************************************************
                 */
                String PrevPenPia = "";
                String PrevPPOOrFPPONo = "";
                String PrevPensionEfffromDate = "";
                String PenTypeId = "";
                String PrevPenPayTresId = "";
                String PrevPensionerId = "";
                String PrevPenBankBranch = "";
                String PrevPenBankIfscCd = "";
                String PenPayTresCode = "";
                String PrevPenSource = "";
                String PrevPenAmt = "";

                String previousPension = "select  * FROM emp_previous_pension WHERE id_employee=? limit 1";
                pstmt2 = conn.prepareStatement(previousPension);
                pstmt2.setString(1, empid);
                result2 = pstmt2.executeQuery();
                while (result2.next()) {
                    if (result2.getString("pension_type") != null && !result2.getString("pension_type").equals("")) {
                        PenTypeId = result2.getString("pension_type");
                    }
                    //  if (result2.getInt("pension_amount") != null && !result2.getInt("pension_amount").equals("")) {
                    PrevPenAmt = result2.getString("pension_amount");
                    // }

                    if (result2.getString("payable_treasure") != null && !result2.getString("payable_treasure").equals("")) {
                        PrevPenPayTresId = result2.getString("payable_treasure");
                    }
                    //   System.out.println("here"+PrevPenPayTresId);
                    if (result2.getString("ifsc_code") != null && !result2.getString("ifsc_code").equals("")) {
                        PrevPenBankIfscCd = result2.getString("ifsc_code");
                    }
                    if (result2.getString("issue_authority") != null && !result2.getString("issue_authority").equals("")) {
                        PrevPenPia = result2.getString("issue_authority");
                    }
                    if (result2.getString("source") != null && !result2.getString("source").equals("")) {
                        PrevPenSource = result2.getString("source");
                    }
                    if (result2.getString("ppo_no") != null && !result2.getString("ppo_no").equals("")) {
                        PrevPPOOrFPPONo = result2.getString("ppo_no");
                    }
                    if (result2.getDate("pension_effective_date") != null && !result2.getDate("pension_effective_date").equals("")) {
                        PrevPensionEfffromDate = (CommonFunctions.getFormattedOutputDateDDMMYYYY(result2.getDate("pension_effective_date")));;
                    }
                    if (result2.getString("branch_name") != null && !result2.getString("branch_name").equals("")) {
                        PrevPenBankBranch = result2.getString("branch_name");
                    }

                }
                employee.setPrevPenType(PenTypeId);
                employee.setPrevPenAmt(PrevPenAmt);
                employee.setPrevPenPayTresId(PrevPenPayTresId);
                employee.setPrevPenBankIfscCd(PrevPenBankIfscCd);
                employee.setPrevPenPia(PrevPenPia);
                employee.setPrevPenSource(PrevPenSource);
                employee.setPrevPPOOrFPPONo(PrevPPOOrFPPONo);
                employee.setPrevPensionEfffromDate(PrevPensionEfffromDate);
                employee.setPrevPenBankBranch(PrevPenBankBranch);
                //  employee.setPrevPensionerId(PrevPensionerId);              

                employee.setPrevPenPayTresCode(PenPayTresCode);
                /**
                 * **************************************************************
                 */
                String PayableTre = "select bill_mast.tr_code from bill_mast inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id where emp_mast.emp_id=? order by bill_mast.aq_year desc,bill_mast.aq_month desc limit 1";
                pstmt2 = conn.prepareStatement(PayableTre);
                pstmt2.setString(1, empid);
                result2 = pstmt2.executeQuery();
                while (result2.next()) {
                    if (result2.getString("tr_code") != null && !result2.getString("tr_code").equals("")) {
                        //   System.out.println("==="+result2.getString("tr_code"));
                        employee.setIntTreasuryId(result2.getString("tr_code"));
                    } else {
                        employee.setIntTreasuryId("");
                    }
                }

                employee.setPensionfamilylist(outList);
                employee.setPensionnomineelist(outList1);

                return employee;
            }

        } catch (Exception e) {
            // employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(result2, pstmt2);
            DataBaseFunctions.closeSqlObjects(result1, pstmt2);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    public static String getNumberFromGPF(String gpfno) {
        String numgpfno = "";
        for (int i = 0; i < gpfno.length(); i++) {
            if (isNumeric(gpfno.substring(i, i + 1)) == true) {
                numgpfno = numgpfno + gpfno.substring(i, i + 1);
            }
        }
        return numgpfno;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String verifyEmpIdfor7thPayCommission(String logindistcode, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean distCodeStatus = false;
        boolean checkingAuthStatus = false;
        boolean joiningDateStatus = false;

        String errMsg = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select dist_code from emp_mast"
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code where emp_mast.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {

                if (rs.getString("dist_code") != null && !rs.getString("dist_code").equals("")) {
                    if (logindistcode.equals(rs.getString("dist_code"))) {
                        distCodeStatus = true;
                        //checkingAuthStatus = true;
                        errMsg = "N";
                    } else {
                        errMsg = "This Employee does not belong to your District.";
                    }
                }
            }

            if (distCodeStatus == true) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "select emp_id,is_approved_checking_auth from pay_revision_option where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_approved_checking_auth") != null && rs.getString("is_approved_checking_auth").equals("Y")) {
                        checkingAuthStatus = true;
                        errMsg = "N";
                    } else {
                        errMsg = "Checking Authority has not approved the Pay Revision of Entered HRMS ID";

                        sql = "select pay_commission from emp_mast where emp_id=?";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, empid);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            if (rs.getString("pay_commission") == null || rs.getString("pay_commission").equals("") || rs.getInt("pay_commission") == 0) {
                                errMsg = "6";
                            }
                        }
                    }
                } else {
                    errMsg = "This Employee has not applied for Pay Revision";
                    if (checkingAuthStatus == false) {
                        DataBaseFunctions.closeSqlObjects(rs, pst);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date payRevDt = sdf.parse("2016-01-01");

                        sql = "select pay_commission,doe_gov from emp_mast where emp_id=?";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, empid);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            if (rs.getString("doe_gov") != null && !rs.getString("doe_gov").equals("")) {
                                Date doj = sdf.parse(rs.getString("doe_gov"));
                                if (doj.compareTo(payRevDt) > 0) {
                                    joiningDateStatus = true;
                                    errMsg = "N";
                                } else {
                                    if (rs.getString("pay_commission") == null || rs.getString("pay_commission").equals("") || rs.getInt("pay_commission") == 0) {
                                        errMsg = "6";
                                    }
                                    //errMsg = "This Employee has not applied for Pay Revision and his/her Date of Joining is before 01-JAN-2016. Therefore 7th Pay Commission cannot be provided to the Employee.";
                                }
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return errMsg;
    }

    @Override
    public int updateEmpIdfor7thPayCommission(String empid, String paycommissionValue) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_mast set pay_commission=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(paycommissionValue));
            pst.setString(2, empid);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public boolean verifyEmployeeDistrict(String logindistcode, String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean distCodeStatus = false;

        String curofficecode = null;
        String nextofficecode = null;
        String depcode = null;
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select dist_code from emp_mast"
             + " inner join g_office on emp_mast.cur_off_code=g_office.off_code where emp_mast.emp_id=?";*/
            String sql = "select office1.dist_code,office2.dist_code nextDist,dep_code from emp_mast"
                    + " inner join g_office office1 on emp_mast.cur_off_code=office1.off_code"
                    + " left outer join g_office office2 on emp_mast.next_office_code=office2.off_code"
                    + " where emp_mast.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                curofficecode = rs.getString("dist_code");
                nextofficecode = rs.getString("nextDist");
                depcode = rs.getString("dep_code");
                if (rs.getString("dist_code") != null && !rs.getString("dist_code").equals("")) {
                    if (logindistcode != null && logindistcode.equals(rs.getString("dist_code"))) {
                        distCodeStatus = true;
                    }
                }
                if (rs.getString("nextDist") != null && !rs.getString("nextDist").equals("")) {
                    if (logindistcode != null && logindistcode.equals(rs.getString("nextDist"))) {
                        distCodeStatus = true;
                    }
                }
            }
            if ((curofficecode == null || curofficecode.equals("")) && (nextofficecode == null || nextofficecode.equals(""))) {
                distCodeStatus = true;
            }
            if (depcode != null && (depcode.equals("09") || depcode.equals("10"))) {
                distCodeStatus = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return distCodeStatus;
    }

    private Employee updatePassword(Employee employee, String empId) {

        Connection con = null;

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        String newUserNamePassword = "";

        try {
            con = this.dataSource.getConnection();

            int noOfCAPSAlpha = 1;
            int noOfDigits = 1;
            int noOfSplChars = 0;
            int minLen = 8;
            int maxLen = 8;

            char[] pswd = RandomPasswordGenerator.generatePswd(minLen, maxLen,
                    noOfCAPSAlpha, noOfDigits, noOfSplChars);

            String newpwd = new String(pswd);

            String newUserId = "";
            String usertype = "";

            ps1 = con.prepareStatement("SELECT * FROM USER_DETAILS WHERE LINKID=?");
            ps1.setString(1, empId);
            rs = ps1.executeQuery();
            boolean updateEmpMast = false;
            if (rs.next()) {
                if (rs.getString("USERNAME") == null || rs.getString("USERNAME").equals("")) {
                    ps = con.prepareStatement("SELECT USERID,USERTYPE,F_NAME,L_NAME FROM EMP_MAST WHERE EMP_ID=?");
                    ps.setString(1, empId);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        newUserId = rs1.getString("USERID");
                        if (newUserId != null && !newUserId.equals("")) {
                            if (Character.isDigit(newUserId.charAt(0))) {
                                newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                                updateEmpMast = true;
                            }
                        } else {
                            newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                            updateEmpMast = true;
                        }
                        usertype = rs1.getString("USERTYPE");
                    }
                    String verifiedUserId = verifyUserID(con, newUserId);
                    updateUserId(con, empId, verifiedUserId, usertype, updateEmpMast);

                    employee.setUsername(verifiedUserId);
                    employee.setPassword(newpwd);
                } else {
                    employee.setUsername(rs.getString("USERNAME"));
                    employee.setPassword(newpwd);
                }
            } else {
                ps = con.prepareStatement("SELECT USERID,USERTYPE,F_NAME,L_NAME FROM EMP_MAST WHERE EMP_ID=?");
                ps.setString(1, empId);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    newUserId = rs1.getString("USERID");
                    if (newUserId != null && !newUserId.equals("")) {
                        if (Character.isDigit(newUserId.charAt(0))) {
                            newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                            updateEmpMast = true;
                        }
                    } else {
                        newUserId = rs1.getString("F_NAME").trim().toLowerCase() + "." + rs1.getString("L_NAME").trim().toLowerCase();
                        updateEmpMast = true;
                    }
                    usertype = rs1.getString("USERTYPE");
                }
                String verifiedUserId = verifyUserID(con, newUserId);
                insertUserId(con, empId, verifiedUserId, usertype, updateEmpMast);

                employee.setUsername(verifiedUserId);
                employee.setPassword(newpwd);
            }

            ps1 = con.prepareStatement("UPDATE EMP_MAST SET PWD=? WHERE EMP_ID=?");
            ps1.setString(1, newpwd);
            ps1.setString(2, empId);
            ps1.executeUpdate();

            ps1 = con.prepareStatement("UPDATE USER_DETAILS SET PASSWORD=?,ACCOUNTNONLOCKED=?,force_reset=1 WHERE LINKID=?");
            ps1.setString(1, newpwd);
            ps1.setInt(2, 1);
            ps1.setString(3, empId);
            ps1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employee;
    }

    private String verifyUserID(Connection conpsql, String userid) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String username = "";
        String regex = "[^\\d]+";
        ArrayList numList = new ArrayList();
        int greaterInt = 0;
        try {
            regex = "[^A-Za-z. ]+";
            String[] uidArr = userid.split(regex);

            String sql = "SELECT USERNAME FROM USER_DETAILS WHERE USERNAME LIKE '" + uidArr[0] + "%'";
            pst = conpsql.prepareStatement(sql);
            rs = pst.executeQuery();
            regex = "[^\\d]+";
            String uid = "";
            while (rs.next()) {
                uid = rs.getString("USERNAME");
                String[] str = uid.split(regex);
                if (str.length > 0) {
                    numList.add(str[1]);
                }
            }
            if (numList != null && numList.size() > 0) {
                for (int i = 0; i < numList.size(); i++) {
                    int numVal = Integer.parseInt(numList.get(i) + "");
                    if (numVal > greaterInt) {
                        greaterInt = numVal;
                    }
                }
            }
            if (greaterInt > 0) {
                greaterInt = greaterInt + 1;
            }
            if (uidArr.length > 0) {
                username = uidArr[0];
            } else {
                username = userid;
            }
            if (greaterInt > 0) {
                username = username + greaterInt;
            } else if (uid != null && !uid.equals("")) {
                username = username + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return username;
    }

    private void insertUserId(Connection conpsql, String empId, String newuserid, String usertype, boolean updateEmpMast) {

        PreparedStatement pstInsert = null;

        try {
            pstInsert = conpsql.prepareStatement("INSERT INTO user_details(username, password, enable, accountnonexpired, accountnonlocked,credentialsnonexpired, usertype, linkid) VALUES(?,?,?,?,?,?,?,?)");
            pstInsert.setString(1, newuserid.toLowerCase());
            pstInsert.setString(2, "");
            pstInsert.setInt(3, 1);
            pstInsert.setInt(4, 1);
            pstInsert.setInt(5, 1);
            pstInsert.setInt(6, 1);
            pstInsert.setString(7, "G");
            pstInsert.setString(8, empId);
            pstInsert.executeUpdate();

            pstInsert = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
            pstInsert.setString(1, newuserid.toLowerCase());
            pstInsert.setString(2, empId);
            pstInsert.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstInsert);
        }
    }

    private void updateUserId(Connection conpsql, String empId, String newuserid, String usertype, boolean updateEmpMast) {

        PreparedStatement pstUpdate = null;
        try {
            pstUpdate = conpsql.prepareStatement("UPDATE user_details SET username=? where linkid=?");
            pstUpdate.setString(1, newuserid.toLowerCase());
            pstUpdate.setString(2, empId);
            pstUpdate.executeUpdate();

            pstUpdate = conpsql.prepareStatement("UPDATE EMP_MAST SET USERID=? WHERE EMP_ID=?");
            pstUpdate.setString(1, newuserid.toLowerCase());
            pstUpdate.setString(2, empId);
            pstUpdate.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstUpdate);
        }
    }

    @Override
    public List getbankAccountChangeRequestList(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        List li = new ArrayList();
        AqmastModel emp = new AqmastModel();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select bank_acc_change_id, emp_bankacc_change.bank_acc_no,emp_bankacc_change.ifsc_code, emp_bankacc_change.bank_code, g_bank.bank_name, g_branch.branch_name,  "
                    + " emp_bankacc_change.branch_code, emp_bankacc_change.ifsc_code, modified_on, mofified_by_emp_id, modified_by_ddo_office, TRIM(ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ')) FULL_NAME, "
                    + " attached_org_file_name, attached_disk_file_name, attachment_type"
                    + " from emp_bankacc_change "
                    + " inner join emp_mast on emp_bankacc_change.mofified_by_emp_id=emp_mast.emp_id "
                    + " inner join g_bank on emp_bankacc_change.bank_code=g_bank.bank_code "
                    + " inner join g_branch on emp_bankacc_change.branch_code=g_branch.branch_code "
                    + " where emp_bankacc_change.emp_id=? order by modified_on desc");

            ps.setString(1, empId);
            result = ps.executeQuery();

            while (result.next()) {
                emp = new AqmastModel();
                emp.setBankName(result.getString("bank_name"));
                emp.setBranchName(result.getString("branch_name"));
                emp.setIfscCode(result.getString("ifsc_code"));
                emp.setBankAccNo(result.getString("bank_acc_no"));
                emp.setBankaccUpdatedBy(result.getString("FULL_NAME"));
                if (result.getString("modified_by_ddo_office") != null && !result.getString("modified_by_ddo_office").equals("")) {
                    emp.setBankaccUpdatedBy(result.getString("FULL_NAME") + ", (" + result.getString("modified_by_ddo_office") + ")");
                }

                emp.setBankaccUpdatedOn(sdf.format(result.getTimestamp(("modified_on"))));
                emp.setBankdocProofFilename(result.getString("attached_disk_file_name") + "@" + result.getString("attached_org_file_name"));
                li.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return li;
    }

    @Override
    public String updateBankAccountNumber(AqmastModel emp, String loginEmpId, String filepath) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean duplicate = false;

        String isDuplicate = "";
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select bank_acc_no, emp_id, cur_off_code  from emp_mast where emp_id <> ? and bank_acc_no=? limit 1 ");
            ps.setString(1, emp.getEmpCode());
            ps.setString(2, emp.getBankAccNo());
            rs = ps.executeQuery();
            if (rs.next()) {
                isDuplicate = rs.getString("emp_id");
            }

            ps = con.prepareStatement("select ifsc_code  from g_branch where branch_code=? ");
            ps.setString(1, emp.getBranchName());
            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setIfscCode(rs.getString("ifsc_code"));
            }

            long filename = System.currentTimeMillis();
            if (isDuplicate.equals("")) {
                ps = con.prepareStatement("insert into emp_bankacc_change (emp_id, bank_acc_no, bank_code, branch_code, ifsc_code, mofified_by_emp_id, attached_org_file_name, attached_disk_file_name, attachment_type, modified_by_ddo_office) "
                        + " values(?,?,?,?,?,?,?,?,?,?) ");
                ps.setString(1, emp.getEmpCode());
                ps.setString(2, emp.getBankAccNo());
                ps.setString(3, emp.getBankName());
                ps.setString(4, emp.getBranchName());
                ps.setString(5, emp.getIfscCode());
                ps.setString(6, loginEmpId);
                ps.setString(7, emp.getBankdocProof().getOriginalFilename());
                ps.setString(8, filename + "");
                ps.setString(9, emp.getBankdocProof().getContentType());
                ps.setString(10, emp.getOffDdo());
                ps.executeUpdate();

                ps = con.prepareStatement(" update emp_mast set bank_acc_no=?, bank_code=?, branch_code=? where emp_id=? ");
                ps.setString(1, emp.getBankAccNo());
                ps.setString(2, emp.getBankName());
                ps.setString(3, emp.getBranchName());
                ps.setString(4, emp.getEmpCode());
                ps.execute();

                OutputStream outputStream = null;
                File newFile = new File(filepath + filename);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                outputStream = new FileOutputStream(newFile);
                outputStream.write(emp.getBankdocProof().getBytes());
                outputStream.flush();
                outputStream.close();

                String mobile = "";

                ps = con.prepareStatement("select mobile from emp_mast where emp_id=?");
                ps.setString(1, emp.getEmpCode());
                rs = ps.executeQuery();
                if (rs.next()) {
                    mobile = rs.getString("mobile");
                }
                if (mobile != null && !mobile.equals("")) {
                    String msg = "Your Bank Account No has been changed in HRMS. Further enquiry Log in to HRMS.";
                    SMSServices smhttp = new SMSServices(mobile, msg, "1407161639888934532");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return isDuplicate;
    }

    @Override
    public void unlockProfileDDO(String empId, String deptCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        try {
            con = dataSource.getConnection();

            if (deptCode != null && deptCode.equals("13")) {

                String isProfileComplete = "";
                String isprofileVerified = "";
                pst = con.prepareStatement("SELECT if_profile_completed,if_profile_verified FROM EMP_MAST WHERE EMP_ID=? ");
                pst.setString(1, empId);

                rs = pst.executeQuery();
                while (rs.next()) {
                    isProfileComplete = rs.getString("if_profile_completed");
                    isprofileVerified = rs.getString("if_profile_verified");
                }
                // System.out.println("isProfileComplete====" + isProfileComplete);
                //  System.out.println("if_profile_verified====" + isprofileVerified);
                pstmt = con.prepareStatement("UPDATE EMP_MAST SET if_profile_completed=?,if_profile_verified=?, if_profile_completed_backup=?,if_profile_verified_backup=?,unlock_profile_date=? WHERE EMP_ID=?");
                pstmt.setString(1, "N");
                pstmt.setString(2, "N");
                pstmt.setString(3, isProfileComplete);
                pstmt.setString(4, isprofileVerified);
                pstmt.setTimestamp(5, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                pstmt.setString(6, empId);

                pstmt.execute();

            }

        } catch (Exception e) {

        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    // code for Previous Pension Details
    @Override
    public void savePrevpensionDetails(PreviousPensionDetails prevpensionForm) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String ifscCode = "";

            pst = con.prepareStatement("select ifsc_code from G_BRANCH where branch_code=?");
            pst.setString(1, prevpensionForm.getBranchName());
            rs = pst.executeQuery();
            if (rs.next()) {
                ifscCode = rs.getString("ifsc_code");
            }

            String sql = "INSERT INTO emp_previous_pension (id_employee,pension_type,pension_amount,payable_treasure,issue_authority,source,ppo_no,pension_effective_date,bank_name,branch_name,ifsc_code,id_entry_taken,entry_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(prevpensionForm.getEmpid()));
            pst.setString(2, prevpensionForm.getPensionType());
            pst.setInt(3, Integer.parseInt(prevpensionForm.getPensionAmount()));
            pst.setString(4, prevpensionForm.getPayableTreasury());
            pst.setString(5, prevpensionForm.getPensionIssuingAuth());
            pst.setString(6, prevpensionForm.getSource());
            pst.setString(7, prevpensionForm.getpPOFPPONo());
            if (prevpensionForm.getPensionEffectiveDate() != null && !prevpensionForm.getPensionEffectiveDate().equals("")) {
                pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse((String) prevpensionForm.getPensionEffectiveDate()).getTime()));
            } else {
                pst.setTimestamp(8, null);
            }
            pst.setString(9, prevpensionForm.getBankName());
            pst.setString(10, prevpensionForm.getBranchName());
            pst.setString(11, ifscCode);
            pst.setString(12, prevpensionForm.getEntryTakenBy());

            if (prevpensionForm.getEntryDate() != null) {
                pst.setTimestamp(13, new Timestamp(prevpensionForm.getEntryDate().getTime()));
            } else {
                pst.setTimestamp(13, null);
            }
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updatePrevpensionDetails(PreviousPensionDetails prevpensionForm) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            String ifscCode = "";

            pst = con.prepareStatement("select ifsc_code from G_BRANCH where branch_code=?");
            pst.setString(1, prevpensionForm.getBranchName());
            rs = pst.executeQuery();
            if (rs.next()) {
                ifscCode = rs.getString("ifsc_code");
            }

            String sql = "UPDATE emp_previous_pension SET pension_type = ?,pension_amount = ?,payable_treasure = ?,issue_authority = ?,source = ?,ppo_no = ?,pension_effective_date = ?,bank_name  = ?,branch_name  = ?,ifsc_code= ?,id_entry_taken  = ?,entry_date = ? WHERE id_pension_type =?";
            pst = con.prepareStatement(sql);

            pst.setString(1, prevpensionForm.getPensionType());
            pst.setInt(2, Integer.parseInt(prevpensionForm.getPensionAmount()));
            pst.setString(3, prevpensionForm.getPayableTreasury());
            pst.setString(4, prevpensionForm.getPensionIssuingAuth());
            pst.setString(5, prevpensionForm.getSource());
            pst.setString(6, prevpensionForm.getpPOFPPONo());
            if (prevpensionForm.getPensionEffectiveDate() != null && !prevpensionForm.getPensionEffectiveDate().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse((String) prevpensionForm.getPensionEffectiveDate()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, prevpensionForm.getBankName());
            pst.setString(9, prevpensionForm.getBranchName());
            pst.setString(10, ifscCode);
            pst.setString(11, prevpensionForm.getEntryTakenBy());

            if (prevpensionForm.getEntryDate() != null) {
                pst.setTimestamp(12, new Timestamp(prevpensionForm.getEntryDate().getTime()));
            } else {
                pst.setTimestamp(12, null);
            }

            pst.setInt(13, Integer.parseInt(prevpensionForm.getPensionId()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getPrevpensionList(String empid) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List prevPensionlist = new ArrayList();
        PreviousPensionDetails dptBean = null;

        try {
            con = dataSource.getConnection();
            String sql = "SELECT id_pension_type,id_employee,pension_type,pension_amount,payable_treasure,issue_authority,source,ppo_no,"
                    + "pension_effective_date,gbank.bank_name,gbr.BRANCH_NAME,gbr.IFSC_CODE,gt.tr_name "
                    + "FROM (select * from emp_previous_pension WHERE id_employee=? )emp_previous_pension "
                    + "left outer join G_BANK gbank on emp_previous_pension.bank_name=gbank.BANK_CODE "
                    + "left outer join G_BRANCH gbr on emp_previous_pension.branch_name=gbr.BRANCH_code "
                    + "left outer join g_treasury gt on emp_previous_pension.payable_treasure = gt.tr_code ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                dptBean = new PreviousPensionDetails();
                dptBean.setPensionId(rs.getString("id_pension_type"));
                dptBean.setPensionType(rs.getString("pension_type"));
                dptBean.setPensionAmount(rs.getString("pension_amount"));
                dptBean.setPayableTreasury(rs.getString("tr_name"));
                dptBean.setBankName(rs.getString("bank_name"));
                dptBean.setBranchName(rs.getString("BRANCH_NAME"));
                dptBean.setIfscCode(rs.getString("IFSC_CODE"));
                dptBean.setPensionIssuingAuth(rs.getString("issue_authority"));
                dptBean.setSource(rs.getString("source"));
                dptBean.setpPOFPPONo(rs.getString("ppo_no"));
                dptBean.setPensionEffectiveDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("pension_effective_date")));
                prevPensionlist.add(dptBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prevPensionlist;
    }

    @Override
    public PreviousPensionDetails editPrevPensionDetails(PreviousPensionDetails prevpensionForm) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            String sql = "SELECT id_pension_type,id_employee,pension_type,pension_amount,payable_treasure,issue_authority,source,ppo_no,\n"
                    + "pension_effective_date,bank_name,BRANCH_NAME,gt.tr_name\n"
                    + "FROM (select * from emp_previous_pension WHERE id_pension_type=? )emp_previous_pension\n"
                    + "left outer join g_treasury gt on emp_previous_pension.payable_treasure = gt.tr_code";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(prevpensionForm.getPensionId()));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                prevpensionForm = new PreviousPensionDetails();
                prevpensionForm.setPensionId(rs.getString("id_pension_type"));
                prevpensionForm.setPensionType(rs.getString("pension_type"));
                prevpensionForm.setPensionAmount(rs.getString("pension_amount"));
                prevpensionForm.setPayableTreasury(rs.getString("payable_treasure"));
                prevpensionForm.setBankName(rs.getString("bank_name"));
                prevpensionForm.setBranchName(rs.getString("BRANCH_NAME"));
                prevpensionForm.setPensionIssuingAuth(rs.getString("issue_authority"));
                prevpensionForm.setSource(rs.getString("source"));
                prevpensionForm.setpPOFPPONo(rs.getString("ppo_no"));
                prevpensionForm.setPensionEffectiveDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("pension_effective_date")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return prevpensionForm;
    }

    @Override
    public void deletePreviousPensionDetails(String pensionId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM emp_previous_pension WHERE  id_pension_type=?");
            pst.setInt(1, Integer.parseInt(pensionId));
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    //End of code for Previous Pension Details
    @Override
    public Employee ResetEmployeeEncodedPassword(String empid, String dcLoginId) {

        Employee employee = new Employee();
        employee.setEmpid(empid);

        Connection conn = null;

        PreparedStatement statement = null;
        ResultSet result = null;
        ResultSet rs = null;

        String encryptedPassword = "";
        String newsalt = "";
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String Cdate = ft.format(dNow);

            conn = this.dataSource.getConnection();

            statement = conn.prepareStatement("SELECT * FROM USER_DETAILS WHERE LINKID=?");
            statement.setString(1, empid);
            rs = statement.executeQuery();
            if (rs.next()) {
                employee.setUsername(rs.getString("USERNAME"));
                employee.setPassword("hrms@123");

                newsalt = CommonFunctions.getNewSalt();
                encryptedPassword = CommonFunctions.getEncryptedPassword(employee.getPassword(), newsalt);

                statement = conn.prepareStatement("UPDATE USER_DETAILS SET ENCRYPTED_PWD=?,SALT=?,ACCOUNTNONLOCKED=?,force_reset=1 WHERE LINKID=?");
                statement.setString(1, encryptedPassword);
                statement.setString(2, newsalt);
                statement.setInt(3, 1);
                statement.setString(4, empid);
                statement.executeUpdate();

                DataBaseFunctions.closeSqlObjects(result, statement);

                String sqlString = dcLoginId + " has reset password of " + empid + " on " + new Date();
                statement = conn.prepareStatement("INSERT INTO monitor_emp_log (login_by,login_date,emp_id,sql_text_query) values(?,?,?,?)");
                statement.setString(1, dcLoginId);
                statement.setTimestamp(2, new Timestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(Cdate).getTime()));
                statement.setString(3, empid);
                statement.setString(4, sqlString);
                statement.executeUpdate();
            }
            //Reset Password new function
        } catch (Exception e) {
            employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    private String getDistrictCode(String offcode) {
        String distcode = null;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("Select dist_code from g_office where off_code=?");
            ps.setString(1, offcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                distcode = rs.getString("dist_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return distcode;
    }

    @Override
    public void saveUserDetails(EmployeeRegistrationBean employeeRegistrationBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList getUserDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void downloadEmployeeDetailsPDF(PdfWriter writer, Document document, String empid, String photopath) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        ResultSet result1 = null;
        ResultSet result2 = null;
        ArrayList familyData = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            PdfPTable dTable = new PdfPTable(8);
            PdfPCell dCell = null;
            dTable.setWidths(new int[]{1, 3, 5, 3, 2, 2, 2, 3});
            dTable.setWidthPercentage(100);
            // Creating a PdfCanvas object 
            PdfContentByte canvas = writer.getDirectContent();

            //PdfPage pdfPage = pdfDoc.addNewPage();   
            Font f1 = new Font();
            f1.setSize(6.9f);
            f1.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(9.1f);
            f2.setFamily("Times New Roman");

            Font bold = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            Font bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD);

            String logo_url = photopath + "govt_odisha" + ".jpg";
            File logo = null;
            logo = new File(logo_url);
            Image img_logo = null;
            if (logo.exists()) {
                img_logo = Image.getInstance(logo_url);
                //img1.scaleAbsolute(200f, 250f);
            }

            PdfPTable dataTable = new PdfPTable(1);
            PdfPCell dataCell = null;

            dataTable.setWidths(new int[]{11});
            dataTable.setWidthPercentage(100);

            img_logo.scalePercent(25f);

            dataCell = new PdfPCell(img_logo);
            dataCell.setBorderWidth(0);
            dataCell.setPadding(0);
            //dataCell.setTextAlignment(TextAlignment.LEFT);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //dataCell.setColspan(2);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("Government of odisha", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0);

            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);

            document.add(dataTable);
            //reportGpfSchedulePageHeaderPdf(document, billNo, dataTable, dataCell, scHelperBean.getGpfType(), f1, f2, crb);
            Paragraph p1 = new Paragraph("EMPLOYEE PROFILE DATA", bold);
            p1.setSpacingAfter(20);
            p1.setSpacingBefore(5);
            p1.setAlignment(Element.ALIGN_CENTER);
            /////////////////////////Profile Photo////////////////////////

            document.add(p1);
            dataTable = new PdfPTable(2);

            dataTable.setWidths(new int[]{6, 6});
            dataTable.setWidthPercentage(100);
            //Table Header
            //System.out.println("Table Header");

            //Get data from database
            String sql = "SELECT *, getfullname(emp_id) fullName, getpostnamefromspc(cur_spc) post, getbankname(bank_code) bankname, getbranchname(branch_code) branchname, getpannumber(emp_id, 'PAN') panno, getpannumber(emp_id, 'AADHAAR') aadhaarno, getpannumber(emp_id, 'DRIVING LICENCE') drivingLicence, getoffnamefromspc(cur_spc) office_name FROM EMP_MAST WHERE EMP_ID = '" + empid + "'";
            System.out.println("sql:" + sql);
            pstmt = con.prepareStatement("SELECT *, getfullname(emp_id) fullName, getpostnamefromspc(cur_spc) post, getbankname(bank_code) bankname, getbranchname(branch_code) branchname, getpannumber(emp_id, 'PAN') panno, getpannumber(emp_id, 'AADHAAR') aadhaarno, getpannumber(emp_id, 'DRIVING LICENCE') drivingLicence, getoffnamefromspc(cur_spc) office_name FROM EMP_MAST WHERE EMP_ID = ?");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            Employee employee = new Employee();
            String OfficeName = null;
            String fullName = null;
            String HomeTown = null;
            String BankName = null;
            String BranchName = null;
            String AccountNo = null;
            String gender = null;
            String HomeTown1 = null;
            String PostGroup = null;
            String CurPost = null;
            String MaritalStatus = null;
            String MobileNo = null;
            String AdhaarNo = null;
            String PanNo = null;
            String DrvLicNo = null;
            String DateOfBirth = null;
            String DateJoiningGoo = null;
            String ContineousServiceGoo = null;
            String PermanentAddress = null;
            String PresentAddress = null;
            String MaritalStatusf = null;
            List familyDetails = new ArrayList();

            if (result.next()) {
                employee.setEmpid(empid);
                OfficeName = result.getString("office_name");
                fullName = result.getString("F_NAME") + " " + result.getString("M_NAME") + " " + result.getString("L_NAME");
                HomeTown = result.getString("home_town");
                BankName = result.getString("bankname");
                BranchName = result.getString("branchname");
                AccountNo = result.getString("bank_acc_no");
                gender = result.getString("gender");
                PostGroup = result.getString("post_grp_type");
                CurPost = result.getString("post");
                AdhaarNo = result.getString("aadhaarno");
                PanNo = result.getString("panno");
                DrvLicNo = result.getString("drivingLicence");
                MaritalStatus = result.getString("m_status");
                String mStatus = result.getString("M_STATUS");
//                System.out.println("mStatus===========================: " + mStatus);
                if (mStatus != null && !mStatus.equals("")) {
                    if (mStatus.equals("1")) {
                        MaritalStatus = "MARRIED";
                    } else if (mStatus.equals("2")) {
                        MaritalStatus = "SINGLE";
                    } else if (mStatus.equals("3")) {
                        MaritalStatus = "DIVORCED";
                    } else if (mStatus.equals("4")) {
                        MaritalStatus = "WIDOWER";
                    } else if (mStatus.equals("5")) {
                        MaritalStatus = "SEPARATED";
                    } else if (mStatus.equals("6")) {
                        MaritalStatus = "NA";
                    } else if (mStatus.equals("7")) {
                        MaritalStatus = "UNMARRIED";
                    } else if (mStatus.equals("8")) {
                        MaritalStatus = "WIDOW";
                    }
                }
                DateOfBirth = CommonFunctions.getFormattedOutputDate2(result.getDate("dob"));
                DateJoiningGoo = CommonFunctions.getFormattedOutputDate2(result.getDate("doe_gov"));
                ContineousServiceGoo = CommonFunctions.getFormattedOutputDate2(result.getDate("joindate_of_goo"));
                MobileNo = result.getString("mobile");
            }
            pstmt = con.prepareStatement("SELECT address FROM EMP_ADDRESS WHERE emp_id =  ? and address_type = 'PERMANENT'");
            pstmt.setString(1, empid);
            result1 = pstmt.executeQuery();
            if (result1.next()) {
                PermanentAddress = result1.getString("address");
            }

            pstmt = con.prepareStatement("SELECT address FROM EMP_ADDRESS WHERE emp_id =  ? and address_type = 'PRESENT'");
            pstmt.setString(1, empid);
            result2 = pstmt.executeQuery();
            if (result2.next()) {
                PresentAddress = result2.getString("address");
            }
            //Profile Photo
            String url = photopath + empid + ".jpg";
            File f = null;
            f = new File(url);
            Image img1 = null;
            if (f.exists()) {
                img1 = Image.getInstance(url);
                img1.scaleAbsolute(100f, 120f);
            }
            if (img1 != null) {
                //img1.scalePercent(15f);

                dataCell = new PdfPCell(img1);
                dataCell.setBorderWidth(0.5f);

                dataTable.addCell(dataCell);
                dataCell = new PdfPCell(new Phrase(result.getString("F_NAME") + " " + result.getString("M_NAME") + " " + result.getString("L_NAME"), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
                dataCell.setBorderWidth(0.5f);
                dataCell.setPadding(10);
                dataTable.addCell(dataCell);
            } else {
                dataCell = new PdfPCell();
                dataCell.setBorderWidth(0.5f);
                dataCell.setPadding(10);
                dataTable.addCell(dataCell);
                dataCell = new PdfPCell(new Phrase(result.getString("F_NAME") + " " + result.getString("M_NAME") + " " + result.getString("L_NAME"), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
                dataCell.setBorderWidth(0.5f);
                dataCell.setPadding(10);
                dataTable.addCell(dataCell);
            }
            //Row 1
            dataCell = new PdfPCell(new Phrase("Employee Name:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(result.getString("F_NAME") + " " + result.getString("M_NAME") + " " + result.getString("L_NAME"), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 2
            dataCell = new PdfPCell(new Phrase("Current Office:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(OfficeName, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 3
            dataCell = new PdfPCell(new Phrase("Current Post:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(CurPost, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 4
            dataCell = new PdfPCell(new Phrase("Mobile Number:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(MobileNo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 5
            dataCell = new PdfPCell(new Phrase("Date of Birth:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(DateOfBirth, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 6.1
            dataCell = new PdfPCell(new Phrase("Date of entry into Govt. service:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(DateJoiningGoo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);

            //Row 6.2
            dataCell = new PdfPCell(new Phrase("Date from which in continuous service with GoO:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(ContineousServiceGoo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 7
            dataCell = new PdfPCell(new Phrase("Gender:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(gender, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 8
            dataCell = new PdfPCell(new Phrase("Marital Status:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(MaritalStatus, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 9
            dataCell = new PdfPCell(new Phrase("Group Post:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(PostGroup, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 10
            dataCell = new PdfPCell(new Phrase("Home town:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(HomeTown, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 11
            dataCell = new PdfPCell(new Phrase("Bank Name:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(BankName, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 12
            dataCell = new PdfPCell(new Phrase("Branch Name:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(BranchName, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 13
            dataCell = new PdfPCell(new Phrase("Bank Account No:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(AccountNo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 14
            dataCell = new PdfPCell(new Phrase("PAN No:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(PanNo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 15
            dataCell = new PdfPCell(new Phrase("AADHAAR No:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(AdhaarNo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 16
            dataCell = new PdfPCell(new Phrase("Driving Licence No:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(DrvLicNo, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 17
            dataCell = new PdfPCell(new Phrase("Present Address:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(PresentAddress, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            //Row 18
            dataCell = new PdfPCell(new Phrase("Permanent Address:", new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase(PermanentAddress, new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataTable.addCell(dataCell);

            //Row 19
            dataCell = new PdfPCell(new Phrase("Family Details:", new Font(Font.FontFamily.HELVETICA, 9.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell.setPadding(10);
            dataCell.setColspan(2);
            dataTable.addCell(dataCell);

            //familyDetails = getFamilyDetails(String empid);
            pstmt = con.prepareStatement("SELECT *, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') relName FROM emp_relation WHERE emp_id = ?");
            pstmt.setString(1, empid);
            result = pstmt.executeQuery();
            while (result.next()) {
                employeeFamilyHelperBean family = new employeeFamilyHelperBean();
                /////
                dataCell = new PdfPCell(new Phrase(result.getString("relation"), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
                dataCell.setBorderWidth(0.5f);
                dataCell.setPadding(10);
                dataTable.addCell(dataCell);
                dataCell = new PdfPCell(new Phrase(result.getString("relName"), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
                dataCell.setBorderWidth(0.5f);
                dataCell.setPadding(10);
                dataTable.addCell(dataCell);

            }
            //Last Row
            /*dataCell = new PdfPCell(new Phrase("Report Generated By Directorate Vigilance", new Font(Font.FontFamily.TIMES_ROMAN, 6.5f, Font.ITALIC)));
             dataCell.setBorderWidth(0);
             dataCell.setPadding(10);
            
             DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
             LocalDateTime now = LocalDateTime.now();  
             //System.out.println(dtf.format(now));
            
             dataTable.addCell(dataCell);
             dataCell = new PdfPCell(new Phrase(result.getString("Generated On:"+now), new Font(Font.FontFamily.HELVETICA, 7.2f, Font.BOLD)));
             dataCell.setBorderWidth(0.5f);
             dataCell.setPadding(10);
             dataTable.addCell(dataCell);*/

            canvas.closePathStroke();

            document.add(dataTable);
            /////////////////////////////////////////////////

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveAdminLog(String modName, String usrname, String usertype, String empid, String msg) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String createdDateTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Insert into equarter.admin_log(mod_name,user_type,user_name,sqlupdated,empid,updated_date)values(?,?,?,?,?,?)");
            pst.setString(1, modName);
            pst.setString(2, usertype);
            pst.setString(3, usrname);
            pst.setString(4, msg);
            pst.setString(5, empid);
            pst.setTimestamp(6, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getMobileNumberChangeList(String empId) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet result = null;

        List li = new ArrayList();
        Employee emp = new Employee();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("select updated_mobile,emp_mobile_change.emp_id,modified_date, modified_by_emp_id, modified_by_ddo_office, TRIM(ARRAY_TO_STRING(ARRAY[emp_mast.INITIALS, emp_mast.F_NAME, emp_mast.M_NAME, emp_mast.L_NAME], ' ')) FULL_NAME"
                    + " from emp_mobile_change "
                    + " inner join emp_mast on emp_mobile_change.modified_by_emp_id=emp_mast.emp_id"
                    + " where emp_mobile_change.emp_id=? order by modified_date desc");
            ps.setString(1, empId);
            result = ps.executeQuery();
            while (result.next()) {
                emp = new Employee();
                emp.setEmpid(result.getString("emp_id"));
                emp.setNewmobile(result.getString("updated_mobile"));
                emp.setMobileupdatedby(result.getString("FULL_NAME"));
                if (result.getString("modified_by_ddo_office") != null && !result.getString("modified_by_ddo_office").equals("")) {
                    emp.setMobileupdatedby(result.getString("FULL_NAME") + ", (" + result.getString("modified_by_ddo_office") + ")");
                }
                emp.setMobileupdatedon(sdf.format(result.getTimestamp(("modified_date"))));
                li.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String updateMobileNumber(Employee emp, String loginEmpId) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        String isDuplicate = "";
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select emp_id from emp_mast where emp_id <> ? and mobile=? limit 1");
            ps.setString(1, emp.getEmpid());
            ps.setString(2, emp.getNewmobile());
            rs = ps.executeQuery();
            if (rs.next()) {
                isDuplicate = rs.getString("emp_id");
            }

            if (isDuplicate.equals("")) {
                ps = con.prepareStatement("insert into emp_mobile_change (emp_id,current_mobile,updated_mobile,modified_by_emp_id,modified_by_ddo_office,modified_date) values(?,?,?,?,?,?)");
                ps.setString(1, emp.getEmpid());
                ps.setString(2, emp.getMobile());
                ps.setString(3, emp.getNewmobile());
                ps.setString(4, loginEmpId);
                ps.setString(5, emp.getOfficecode());
                ps.setTimestamp(6, new Timestamp(new Date().getTime()));
                ps.executeUpdate();

                ps = con.prepareStatement("update emp_mast set mobile=? where emp_id=?");
                ps.setString(1, emp.getNewmobile());
                ps.setString(2, emp.getEmpid());
                ps.execute();

                if (emp.getMobile() != null && !emp.getMobile().equals("")) {
                    //String msg = "Your Bank Account No has been changed in HRMS. Further enquiry Log in to HRMS.";
                    //SMSServices smhttp = new SMSServices(emp.getMobile(), msg, "1407161639888934532");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDuplicate;
    }

    @Override
    public String getempDDOchargefficeList(String empid) {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet result = null;

        Employee emp = new Employee();

        String empIsAddlcharge = null;

        try {
            con = this.repodataSource.getConnection();
            ps = con.prepareStatement("SELECT OFF_CODE FROM G_OFFICE WHERE DDO_HRMSID=?");
            ps.setString(1, empid);
            result = ps.executeQuery();
            while (result.next()) {
                empIsAddlcharge = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empIsAddlcharge;
    }

    @Override
    public List getEmployeeNominee(String empid) {

        String SQL = "SELECT m_status,INITIALS,F_NAME,M_NAME,L_NAME,int_rel_id,EMPR.relation,SL_NO, IF_ALIVE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, EMPLOYEE_EMP_ID,gender,dob,mobile"
                + " FROM EMP_RELATION EMPR"
                + " LEFT OUTER JOIN g_relation rel ON EMPR.relation=rel.relation"
                + " LEFT OUTER JOIN g_marital ON EMPR.marital_status=g_marital.int_marital_status_id"
                + " WHERE EMP_ID=? and is_nominee='Y'";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        FamilyRelation familyRelation = new FamilyRelation();

        List nomineeList = new ArrayList();
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                familyRelation = new FamilyRelation();
                familyRelation.setSlno(result.getInt("SL_NO"));
                familyRelation.setRelation(result.getString("RELATION"));
                familyRelation.setIfalive(result.getString("IF_ALIVE"));
                familyRelation.setRelname(result.getString("relation"));
                familyRelation.setInitials(result.getString("INITIALS"));
                familyRelation.setFname(result.getString("F_NAME"));
                familyRelation.setMname(result.getString("M_NAME"));
                familyRelation.setLname(result.getString("L_NAME"));
                familyRelation.setEmployeeempid(result.getInt("EMPLOYEE_EMP_ID"));
                familyRelation.setGender(result.getString("gender"));
                familyRelation.setDob(formatDate(result.getDate("dob")));
                familyRelation.setMobile(result.getString("mobile"));
                familyRelation.setMarital(result.getString("m_status"));
                if (result.getDate("dob") != null && !result.getDate("dob").equals("")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = formatter.parse(formatDate(result.getDate("dob")));
                    Instant instant = date.toInstant();
                    ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                    LocalDate givenDate = zone.toLocalDate();
                    Period period = Period.between(givenDate, LocalDate.now());
                    //String age = period.getYears() + " years " + period.getMonths() + " months " + period.getDays() + " days";
                    String age = period.getYears() + " years " + period.getMonths() + " months ";

                    familyRelation.setAge(age);
                }
                nomineeList.add(familyRelation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return nomineeList;
    }

    @Override
    public ArrayList getEmpServiceList(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList servicelist = new ArrayList();
        Employee employee = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT DEPARTMENT_NAME,EJ.DOE, EJ.SPC, EJ.NOT_TYPE, EJ.JOIN_DATE, EJ.SPN join_post, EMP_NOTIFICATION.ORDNO,EMP_NOTIFICATION.ORDDT,\n"
                    + "EMP_TRANSFER.OFF_CODE,RLV_DATE,\n"
                    + "EMP_RELIEVE.SPC AS RLV_FROM_SPC,GETSPN(EMP_RELIEVE.SPC) AS RLV_FROM_SPN FROM\n"
                    + "(SELECT NOT_ID,DOE,SPC,NOT_TYPE,JOIN_DATE,GETSPN(SPC) SPN,EMP_ID FROM EMP_JOIN WHERE EMP_ID=?  )EJ \n"
                    + "INNER JOIN EMP_NOTIFICATION ON EJ.NOT_ID = EMP_NOTIFICATION.NOT_ID\n"
                    + "INNER JOIN G_SPC ON EJ.SPC=G_SPC.SPC\n"
                    + "INNER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE=G_SPC.DEPT_CODE\n"
                    + "LEFT OUTER JOIN EMP_RELIEVE ON EMP_NOTIFICATION.NOT_ID = EMP_RELIEVE.NOT_ID\n"
                    + "LEFT OUTER JOIN EMP_TRANSFER ON EMP_NOTIFICATION.NOT_ID = EMP_TRANSFER.NOT_ID \n"
                    + "where extract(year from join_date)>=(extract(year from current_date)-10) ORDER BY JOIN_DATE");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setServiceFrom(rs.getString("RLV_FROM_SPN"));
                employee.setServiceTo(rs.getString("join_post"));
                employee.setPlaceOFposting(rs.getString("DEPARTMENT_NAME"));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("join_date")));
                servicelist.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return servicelist;
    }

    @Override
    public ArrayList promotionList(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList servicelist = new ArrayList();
        Employee employee = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select AUTH_DEPT,AUTH_OFF,JOIN_DATE,G_SPC.spc as gspc,gpc,POST,OFF_EN from emp_join  LEFT JOIN G_SPC ON emp_join.spc=G_SPC.spc LEFT JOIN G_POST ON G_SPC.gpc=G_POST.POST_CODE \n"
                    + "LEFT JOIN G_OFFICE ON G_OFFICE.OFF_CODE=emp_join.AUTH_OFF where not_type='PROMOTION' AND EMP_ID=?");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setPromotionalpost(rs.getString("POST"));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("join_date")));
                employee.setOffice(rs.getString("OFF_EN"));
                servicelist.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return servicelist;
    }

    @Override
    public ArrayList allPosting(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList servicelist = new ArrayList();
        Employee employee = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select AUTH_DEPT,AUTH_OFF,JOIN_DATE,G_SPC.spc as gspc,gpc,POST,"
                    + "OFF_EN from emp_join  LEFT JOIN G_SPC ON emp_join.spc=G_SPC.spc LEFT JOIN "
                    + "G_POST ON G_SPC.gpc=G_POST.POST_CODE \n"
                    + "LEFT JOIN G_OFFICE ON G_OFFICE.OFF_CODE=emp_join.AUTH_OFF where "
                    + "not_type in('PROMOTION','DEPUTATION','DEPUTATION_AG','FIRST_APPOINTMENT','POSTING','PROMOTION','REDEPLOYMENT','REDESIGNATION','TRANSFER') AND EMP_ID=?");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setPromotionalpost(rs.getString("POST"));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("join_date")));
                employee.setOffice(rs.getString("OFF_EN"));
                servicelist.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return servicelist;
    }

    @Override
    public List specialSubCategory() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List subCatgList = new ArrayList();
        Employee employee = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select * from g_sub_category");
            rs = pst.executeQuery();
            while (rs.next()) {
                employee = new Employee();
                employee.setSpluserCategory(rs.getString("category_name"));
                subCatgList.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subCatgList;
    }

    @Override
    public Address getPermanentAddress(String empid) {
        Address addr = new Address();
        String SQL = "SELECT ADDRESS_ID,ADDRESS_TYPE,ADDRESS,BL_CODE,VILL_CODE,PO_CODE,PS_CODE,PIN,STATE_CODE,DIST_CODE,STD_CODE,TPHONE,is_locked "
                + "FROM EMP_ADDRESS WHERE EMP_ID=? and ADDRESS_TYPE= 'PERMANENT' ORDER BY ADDRESS_ID DESC limit 1";
        //System.out.println("SQL "+ SQL);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {

                addr.setEmpId(empid);
                addr.setAddressId(result.getInt("ADDRESS_ID"));
                addr.setAddressType(result.getString("ADDRESS_TYPE"));
                addr.setAddress(result.getString("ADDRESS"));
                addr.setBlockCode(result.getString("BL_CODE"));
                addr.setVillageCode(result.getString("VILL_CODE"));
                addr.setPostCode(result.getString("PO_CODE"));
                addr.setPsCode(result.getString("PS_CODE"));
                addr.setDistCode(result.getString("DIST_CODE"));
                addr.setStateCode(result.getString("STATE_CODE"));
                addr.setPin(result.getString("PIN"));
                addr.setStdCode(result.getString("STD_CODE"));
                addr.setTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));

            }
            SQL = "SELECT ADDRESS_ID,ADDRESS_TYPE,ADDRESS,BL_CODE,VILL_CODE,PO_CODE,PS_CODE,PIN,STATE_CODE,DIST_CODE,STD_CODE,TPHONE,is_locked "
                    + "FROM EMP_ADDRESS WHERE EMP_ID=? and ADDRESS_TYPE= 'PRESENT' ORDER BY ADDRESS_ID DESC limit 1";
            //System.out.println("SQL "+ SQL);

            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {

                addr.setEmpId(empid);
                addr.setPrAddressId(result.getInt("ADDRESS_ID"));
                addr.setPrAddressType(result.getString("ADDRESS_TYPE"));
                addr.setPrAddress(result.getString("ADDRESS"));
                addr.setPrBlockCode(result.getString("BL_CODE"));
                addr.setPrVillageCode(result.getString("VILL_CODE"));
                addr.setPrPostCode(result.getString("PO_CODE"));
                addr.setPrPsCode(result.getString("PS_CODE"));
                addr.setPrDistCode(result.getString("DIST_CODE"));
                addr.setPrStateCode(result.getString("STATE_CODE"));
                addr.setPrPin(result.getString("PIN"));
                addr.setPrStdCode(result.getString("STD_CODE"));
                addr.setPrTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }

        return addr;
    }

    @Override
    public Address getPermanentAddressForAcknowledgement(String empid) {

        Address addr = new Address();
        String SQL = "SELECT STATE_NAME,DIST_NAME,BL_NAME,PS_NAME,PO_NAME,VILLAGE_NAME,ADDRESS_ID,ADDRESS_TYPE,ADDRESS,EMP_ADDRESS.PIN,EMP_ADDRESS.STATE_CODE,STD_CODE,TPHONE,is_locked"
                + " FROM EMP_ADDRESS"
                + " INNER JOIN G_DISTRICT ON EMP_ADDRESS.DIST_CODE=G_DISTRICT.DIST_CODE"
                + " LEFT OUTER JOIN G_BLOCK ON EMP_ADDRESS.BL_CODE::INTEGER=G_BLOCK.BL_CODE"
                + " INNER JOIN G_PS ON EMP_ADDRESS.PS_CODE=G_PS.PS_CODE"
                + " INNER JOIN G_PO ON EMP_ADDRESS.PO_CODE=G_PO.PO_CODE"
                + " INNER JOIN G_STATE ON EMP_ADDRESS.STATE_CODE=G_STATE.STATE_CODE"
                + " LEFT OUTER JOIN G_VILLAGE ON EMP_ADDRESS.VILL_CODE=G_VILLAGE.VILL_CODE"
                + " WHERE EMP_ID=? and ADDRESS_TYPE= 'PERMANENT' ORDER BY ADDRESS_ID DESC limit 1";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = this.repodataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                addr.setEmpId(empid);
                addr.setAddressId(result.getInt("ADDRESS_ID"));
                addr.setAddressType(result.getString("ADDRESS_TYPE"));
                addr.setAddress(result.getString("ADDRESS"));
                addr.setBlockCode(result.getString("BL_NAME"));
                addr.setVillageCode(result.getString("VILLAGE_NAME"));
                addr.setPostCode(result.getString("PO_NAME"));
                addr.setPsCode(result.getString("PS_NAME"));
                addr.setDistCode(result.getString("DIST_NAME"));

                addr.setStateCode(result.getString("state_code"));

                addr.setStateCode(result.getString("STATE_NAME"));

                addr.setPin(result.getString("PIN"));
                addr.setStdCode(result.getString("STD_CODE"));
                addr.setTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));
            }

            DataBaseFunctions.closeSqlObjects(result, statement);

            SQL = "SELECT STATE_NAME,DIST_NAME,BL_NAME,PS_NAME,PO_NAME,VILLAGE_NAME,ADDRESS_ID,ADDRESS_TYPE,ADDRESS,EMP_ADDRESS.PIN,EMP_ADDRESS.STATE_CODE,STD_CODE,TPHONE,is_locked"
                    + " FROM EMP_ADDRESS"
                    + " INNER JOIN G_DISTRICT ON EMP_ADDRESS.DIST_CODE=G_DISTRICT.DIST_CODE"
                    + " LEFT OUTER JOIN G_BLOCK ON EMP_ADDRESS.BL_CODE::INTEGER=G_BLOCK.BL_CODE"
                    + " INNER JOIN G_PS ON EMP_ADDRESS.PS_CODE=G_PS.PS_CODE"
                    + " INNER JOIN G_PO ON EMP_ADDRESS.PO_CODE=G_PO.PO_CODE"
                    + " INNER JOIN G_STATE ON EMP_ADDRESS.STATE_CODE=G_STATE.STATE_CODE"
                    + " LEFT OUTER JOIN G_VILLAGE ON EMP_ADDRESS.VILL_CODE=G_VILLAGE.VILL_CODE"
                    + " WHERE EMP_ID=? and ADDRESS_TYPE= 'PRESENT' ORDER BY ADDRESS_ID DESC limit 1";
            //System.out.println("SQL "+ SQL);

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {

                addr.setEmpId(empid);
                addr.setPrAddressId(result.getInt("ADDRESS_ID"));
                addr.setPrAddressType(result.getString("ADDRESS_TYPE"));
                addr.setPrAddress(result.getString("ADDRESS"));
                addr.setPrBlockCode(result.getString("BL_NAME"));
                addr.setPrVillageCode(result.getString("VILLAGE_NAME"));
                addr.setPrPostCode(result.getString("PO_NAME"));
                addr.setPrPsCode(result.getString("PS_NAME"));
                addr.setPrDistCode(result.getString("DIST_NAME"));

                addr.setPrStateCode(result.getString("state_code"));

                addr.setPrStateCode(result.getString("STATE_NAME"));

                addr.setPrPin(result.getString("PIN"));
                addr.setPrStdCode(result.getString("STD_CODE"));
                addr.setPrTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return addr;
    }

    @Override
    public void profileCompletedLog(String empid, String completedby, String verifiedby) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String ipaddress = CommonFunctions.getISPIPAddress();

            String sql = "INSERT INTO equarter.hrms_profile_complete_log(emp_id,completed_by_emp_id,ip_address,completed_date,verified_by_emp_id) values(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, completedby);
            pst.setString(3, ipaddress);
            pst.setTimestamp(4, new Timestamp(new Date().getTime()));
            pst.setString(5, verifiedby);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public Employee getProfileCompletedStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Employee emp = new Employee();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT IF_PROFILE_COMPLETED FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                emp.setIfprofileCompleted(rs.getString("IF_PROFILE_COMPLETED"));
            }

            sql = "SELECT * FROM equarter.hrms_profile_complete_log WHERE emp_id=? ORDER BY complete_log_id DESC LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("completed_date") != null && !rs.getString("completed_date").equals("")) {
                    if (emp.getIfprofileCompleted() != null && emp.getIfprofileCompleted().equals("Y")) {
                        emp.setDateOfProfileCompletion(CommonFunctions.getFormattedOutputDate1(rs.getDate("completed_date")));
                        emp.setIpOfProfileCompletion(rs.getString("ip_address"));
                    }
                } else {
                    emp.setDateOfProfileCompletion(null);
                    emp.setIpOfProfileCompletion(null);
                }
            } else {
                emp.setDateOfProfileCompletion(null);
                emp.setIpOfProfileCompletion(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    @Override
    public int getAddressCount(String empId, String addressType) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT COALESCE(COUNT(*), 0) as total FROM emp_address WHERE emp_id=? and address_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, addressType);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public void SaveAadharTransaction(String empId, String transactionId, String oldAadhar, String aadharNo) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            String ipaddress = CommonFunctions.getISPIPAddress();
            String sql1 = "SELECT COALESCE(COUNT(*),0) AS total FROM equarter.aadhar_validation_log WHERE emp_id = ? AND aadhar = ?";
            pst = con.prepareStatement(sql1);
            pst.setString(1, empId);
            pst.setString(2, aadharNo);
            rs = pst.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("total");
            }
            if (count == 0) {
                String sql = "INSERT INTO equarter.aadhar_validation_log(old_aadhar, aadhar, emp_id, otp_generated_on, is_validated, transaction_id, ip_address) values(?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, oldAadhar);
                pst.setString(2, aadharNo);
                pst.setString(3, empId);
                pst.setTimestamp(4, new Timestamp(new Date().getTime()));
                pst.setString(5, "N");
                pst.setString(6, transactionId);
                pst.setString(7, ipaddress);
                pst.executeUpdate();
            } else {
                //Update
                String sql = "UPDATE equarter.aadhar_validation_log SET transaction_id = ?, otp_generated_on = ? WHERE emp_id = ? AND aadhar = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transactionId);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, empId);
                pst.setString(4, aadharNo);
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void UpdateAadharTransaction(String empId, String uidToken, String txn, String aadhar) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE equarter.aadhar_validation_log SET is_validated = 'Y', validated_on = ?, uid_token = ? WHERE transaction_id = ? AND emp_id = ?";
            pst = con.prepareStatement(sql);

            pst.setTimestamp(1, new Timestamp(new Date().getTime()));
            pst.setString(2, uidToken);
            pst.setString(3, txn);
            pst.setString(4, empId);
            pst.executeUpdate();
            sql = "UPDATE emp_id_doc SET is_verified = 'Y' WHERE emp_id = ? AND id_description = 'AADHAAR' AND id_no = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, aadhar);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public void UpdateAadhar(String empId, String aadhar) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_id_doc SET id_no = ? WHERE emp_id = ? AND id_description = 'AADHAAR'";
            pst = con.prepareStatement(sql);
            pst.setString(1, aadhar);
            pst.setString(2, empId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public int countDuplicateAadhar(String aadhar, String empId) {
        int count = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT COALESCE(count(*),0) AS total FROM emp_id_doc WHERE id_description = 'AADHAAR' AND ID_NO = ? and emp_id <> ?");
            pst.setString(1, aadhar);
            pst.setString(2, empId);
            result = pst.executeQuery();
            if (result.next()) {
                count = result.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public boolean stopGpfDeduction(String empId) {
        boolean hastobeStop = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String str = "";

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select dos  from emp_mast where emp_id =? ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                str = CommonFunctions.getFormattedOutputDate1(rs.getDate("dos"));
            }
            if (str != null) {
                Date dosdt = new Date(str);

                Calendar billcal1 = Calendar.getInstance();
                // billcal1.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                //billcal1.set(Calendar.MONTH, Calendar.MONTH);

                Calendar dosCal = Calendar.getInstance();
                dosCal.setTime(dosdt);

                int diffYear = dosCal.get(Calendar.YEAR) - billcal1.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + dosCal.get(Calendar.MONTH) - billcal1.get(Calendar.MONTH);
                System.out.println("diffmonth:" + diffMonth);

                if (diffMonth <= 4) {
                    hastobeStop = true;
                }
                System.out.println("stop subscription==" + hastobeStop);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hastobeStop;
    }

    @Override
    public Employee getProfileVerifiedStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Employee emp = new Employee();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT IF_PROFILE_VERIFIED FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                emp.setIfprofileVerified(rs.getString("IF_PROFILE_VERIFIED"));
            }

            sql = "SELECT * FROM equarter.hrms_profile_complete_log WHERE emp_id=? ORDER BY complete_log_id DESC LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("VERIFIED_BY_EMP_ID") != null && !rs.getString("VERIFIED_BY_EMP_ID").equals("")) {
                    emp.setDateOfProfileCompletion(CommonFunctions.getFormattedOutputDate1(rs.getDate("completed_date")));
                    emp.setIpOfProfileCompletion(rs.getString("ip_address"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emp;
    }

    @Override
    public PensionProfile getPensionEmpDetails(String empid) {
        PensionProfile employee = new PensionProfile();
        PensionNomineeList nomineelist = new PensionNomineeList();
        PensionFamilyList familylist = new PensionFamilyList();
        String fullName = "";
        String employeeId = null;
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet result = null;
        ResultSet result2 = null;
        //ResultSet result3 = null;
        // ResultSet rs = null;
        ResultSet result1 = null;
        //ResultSet rs1 = null;
        //ResultSet rs2 = null;
        //String selectQry = "";
        ArrayList outList = new ArrayList();
        ArrayList outList1 = new ArrayList();
        try {
            conn = dataSource.getConnection();
            String sql1 = "select e.* from\n"
                    + "(select emp_id,gpf_no,acct_type,replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries,\n"
                    + "replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum \n"
                    + "from emp_mast) e\n"
                    + "inner join g_gpf_type\n"
                    + "on e.gpfseries=g_gpf_type.gpf_type\n"
                    + "where emp_id=? or gpf_no=?";
            statement = conn.prepareStatement(sql1);
            statement.setString(1, empid);
            statement.setString(2, empid);
            //    statement.setString(2, empid);
            result = statement.executeQuery();
            if (result.next()) {
                employee.setMsg("Y");
                employee.setGpftype(result.getString("acct_type"));
                String SQL = "SELECT (select int_district_id from emp_join inner join g_office on emp_join.auth_off=g_office.off_code where emp_id=EMP_MAST.emp_id order by join_date desc limit 1)dist_id,EMP_MAST.emp_id,EMP_MAST.GPF_NO,gpfseries,spn,cur_spc designation,gpfnum,ACCT_TYPE,initials, G_TITLE.int_title,F_NAME,M_NAME,L_NAME,GENDER,\n"
                        + "G_MARITAL.M_STATUS,EMP_MAST.RES_CAT,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,id_no,empIdDoc.idno2,ifsc_code,\n"
                        + "DEPARTMENT_NAME, G_SPC.DEPT_CODE,GPC,POST,CUR_OFF_CODE,g_office.ddo_code, OFF_NAME,g_office.TR_CODE,OFF_EN,BL_GRP,int_ifms_religion_id RELIGION_ID,\n"
                        + "g_religion.RELIGION RELIGION,PH_CODE, ID_MARK, MOBILE,CURR_POST_DOJ, EMP_MAST.GP,\n"
                        + "CUR_SALARY, EMAIL_ID, DOE_GOV,HOME_TOWN,\n"
                        + "int_district_id,jointime_of_goo,toe_gov,gistype,scheme_name,gisno,domicile,EMP_MAST.bank_code,G_BANK.BANK_NAME,G_BRANCH.BRANCH_NAME,EMP_MAST.branch_code,\n"
                        + "bank_acc_no,G_MARITAL.int_marital_status_id,G_SCHEME.SCHEME_NAME,if_profile_completed,G_SCHEME.SCHEME_ID,if_profile_verified\n"
                        + ",dos_age,if_gpf_assumed FROM\n"
                        + "(SELECT EMP_MAST.emp_id,id_no,replace(replace(regexp_match(gpf_no,'[A-Z]+')::text,'{', ''),'}', '') AS gpfseries\n"
                        + ", replace(replace(regexp_match(gpf_no,'[0-9]+')::text,'{', ''),'}', '') as gpfnum,INITIALS,rec_source,allotment_year,GPF_NO,ACCT_TYPE,\n"
                        + " F_NAME,M_NAME,L_NAME,GENDER,EMP_MAST.m_status maritalstatus,EMP_MAST.m_status,RES_CAT,CUR_BASIC_SALARY,HEIGHT,DOB,JOINDATE_OF_GOO,DOS,CUR_SPC,CUR_OFF_CODE,\n"
                        + "BL_GRP,RELIGION,PH_CODE, ID_MARK, MOBILE, CURR_POST_DOJ, GP, CUR_SALARY, CUR_CADRE_CODE, POST_GRP_TYPE, FIELD_OFF_CODE, EMAIL_ID,\n"
                        + "DOE_GOV,HOME_TOWN,jointime_of_goo,toe_gov,\n"
                        + "gistype,gisno,domicile,bank_code,branch_code,bank_acc_no,if_profile_completed,if_profile_verified,dos_age,if_gpf_assumed FROM EMP_MAST\n"
                        + " LEFT OUTER JOIN emp_id_doc ON EMP_MAST.emp_id = emp_id_doc.emp_id AND id_description='PAN'\n"
                        + " WHERE (EMP_MAST.EMP_ID=?) and acct_type IN ('GPF','TPF'))EMP_MAST\n"
                        + "INNER JOIN G_GPF_TYPE ON EMP_MAST.GPFSERIES=G_GPF_TYPE.GPF_TYPE\n"
                        + "LEFT OUTER JOIN (select emp_id,id_no as idno2 from emp_id_doc where id_description='ELECTION ID CARD') as empIdDoc ON EMP_MAST.emp_id = empIdDoc.emp_id\n"
                        + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE = G_OFFICE.OFF_CODE\n"
                        + "LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC\n"
                        + "LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE = G_CADRE.CADRE_CODE\n"
                        + "LEFT OUTER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE\n"
                        + "LEFT OUTER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE = G_SPC.DEPT_CODE\n"
                        + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE = G_DISTRICT.DIST_CODE\n"
                        + "LEFT OUTER JOIN G_BANK ON EMP_MAST.BANK_CODE = G_BANK.BANK_CODE\n"
                        + "LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE = G_BRANCH.BRANCH_CODE\n"
                        + "LEFT OUTER JOIN G_MARITAL ON EMP_MAST.m_status = G_MARITAL.int_marital_status_id\n"
                        + "LEFT OUTER JOIN G_SCHEME ON EMP_MAST.gistype = G_SCHEME.SCHEME_ID\n"
                        + "LEFT OUTER JOIN g_religion ON EMP_MAST.religion=g_religion.int_religion_id\n"
                        + "LEFT OUTER JOIN G_TITLE ON EMP_MAST.initials=G_TITLE.title\n";
                // System.out.println("SQL is:" + SQL);
                // System.out.println("empid:" + empid);
                statement = conn.prepareStatement(SQL);
                statement.setString(1, empid);
                //    statement.setString(2, empid);
                result = statement.executeQuery();
                if (result.next()) {
                    empid = result.getString("emp_id");
                    employee.setEmployeeFirstName(result.getString("F_NAME"));
                    employee.setEmployeeMiddleName(result.getString("M_NAME"));
                    employee.setEmployeeLastName(result.getString("L_NAME"));
                    employee.setHrmsEmpId(empid);
//                    System.out.println("post:" + result.getString("designation"));
                    employee.setDesignationId(result.getString("designation"));

                    employee.setTpfAcNo(getNumberFromGPF(result.getString("GPF_NO")));
                    employee.setTpfSeries(CommonFunctions.getGPFSeries(result.getString("GPF_NO")));
                    if (result.getString("int_title") != null && !result.getString("int_title").equals("")) {
                        employee.setSalutationEmp(result.getString("int_title"));
                    } else {
                        employee.setSalutationEmp("Other");
                    }
                    employee.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result.getDate("DOB")));
                    employee.setRetirementDate(CommonFunctions.getFormattedOutputDateDDMMYYYY(result.getDate("DOS")));

                    employee.setDob(CommonFunctions.getFormattedOutputDateMMDDYYYY(result.getDate("DOB")));
                    employee.setRetirementDate(CommonFunctions.getFormattedOutputDateMMDDYYYY(result.getDate("DOS")));
//                    System.out.println("Dob:" + employee.getDob() + employee.getRetirementDate());

                    if (result.getString("gender") != null && !result.getString("gender").equals("")) {
                        if (result.getString("gender").equals("M")) {
                            employee.setSex("M");
                        } else if (result.getString("gender").equals("F")) {
                            employee.setSex("F");
                        } else if (result.getString("gender").equals("T")) {
                            employee.setSex("O");
                        }
                    } else {
                        employee.setSex("O");
                    }
                    employee.setPenCategoryId("");
                    employee.setPenTypeId("");
                    String mStatus = result.getString("M_STATUS");
                    if (mStatus != null && !mStatus.equals("")) {
                        if (mStatus.toUpperCase().equals("MARRIED")) {
                            employee.setIntMaritalStatusTypeId("1");
                        } else if (mStatus.toUpperCase().equals("UNMARRIED") || mStatus.toUpperCase().equals("SINGLE") || mStatus.toUpperCase().equals("SEPARATED")) {
                            employee.setIntMaritalStatusTypeId("2");
                        } else if (mStatus.toUpperCase().equals("DIVORCED")) {
                            employee.setIntMaritalStatusTypeId("3");
                        } else if (mStatus.toUpperCase().equals("WIDOWED") || mStatus.toUpperCase().equals("WIDOWER") || mStatus.toUpperCase().equals("WIDOW")) {
                            employee.setIntMaritalStatusTypeId("4");
                        } else {
                            employee.setIntMaritalStatusTypeId("0");
                        }
                    }
                    if (result.getString("RELIGION_ID") != null && !result.getString("RELIGION_ID").equals("") && (result.getString("RELIGION_ID").equals("1") || result.getString("RELIGION_ID").equals("2") || result.getString("RELIGION_ID").equals("3"))) {
                        employee.setReligionId(result.getString("RELIGION_ID"));
                    } else {
                        employee.setReligionId("0");
                    }

                    employee.setNationalityId("1");

                    if (result.getString("EMAIL_ID") != null && !result.getString("EMAIL_ID").equals("")) {
                        employee.setMailId(result.getString("EMAIL_ID"));
                    } else {
                        employee.setMailId("");
                    }

                    employee.setMobileNo(result.getString("MOBILE"));
                    employee.setPenIdnMark(result.getString("ID_MARK"));
                    employee.setPenIdnMark2("NA");
                    employee.setPanNo(result.getString("id_no"));

                    if (result.getString("idno2") != null && !result.getString("idno2").equals("")) {
                        employee.setIdentityDocId("2");
                        employee.setIdentityDocNumber(result.getString("idno2"));
                    } else {
                        employee.setIdentityDocId("");
                        employee.setIdentityDocNumber("");
                    }

//                employee.setDesignationId("");
                    employee.setCvp("");
                    employee.setCvpPercentage("");
                    employee.setFinalGpfAppliedDate("");
                    employee.setFinalGpfAppliedFlag("Y");
                    employee.setFinalGpfAppliedLetterNo("");
                    employee.setHeight(result.getString("HEIGHT"));
                    employee.setIfscCode(result.getString("ifsc_code"));
                    employee.setBankAcctNo(result.getString("bank_acc_no"));
                    String banBarnch = result.getString("bank_name") + " " + result.getString("branch_name");
                    employee.setBankBranch(banBarnch);
                    employee.setBsrCode(result.getString("branch_code"));
                    employee.setTreasuryCode(result.getString("tr_code"));
                    employee.setDdoName(result.getString("ddo_code"));
                    employee.setDdoId(result.getString("ddo_code"));
                    employee.setLastDistrictServe(result.getString("dist_id"));

                    String districyCode = "";
                    String perAddpin = "";
                    String peraddstateid = "";
                    String perAddcity = "";
                    String perAddTown = "";
                    String perAddpoliceStation = "";
                    String cdistricyId = "";
                    String comAddpin = "";
                    String comaddstateid = "";
                    String comAddcity = "";
                    String comAddTown = "";
                    String comAddpoliceStation = "";
                    String stateCode = "";
                    String cstateCode = "";

                    String SQLAsddress = "SELECT g_state.int_state_id state_code,address_type,address,village_name,emp_address.pin,ps_name,int_district_id,po_name,emp_address.dist_code\n"
                            + "FROM emp_address LEFT JOIN g_village ON g_village.vill_code=emp_address.vill_code\n"
                            + "LEFT JOIN g_ps ON g_ps.ps_code=emp_address.ps_code \n"
                            + "LEFT JOIN g_district ON g_district.dist_code=emp_address.dist_code \n"
                            + "LEFT JOIN g_po ON  g_po.po_code=emp_address.po_code\n"
                            + "LEFT JOIN g_state ON  g_state.state_code=emp_address.state_code WHERE  emp_id=?";
                    pstmt2 = conn.prepareStatement(SQLAsddress);
                    pstmt2.setString(1, empid);
                    result2 = pstmt2.executeQuery();
                    while (result2.next()) {
                        if (result2.getString("address_type").equals("PERMANENT")) {
                            districyCode = result2.getString("int_district_id");
                            stateCode = result2.getString("state_code");
                            perAddpin = result2.getString("pin");
                            if (result2.getString("address") != null && !result2.getString("address").equals("")) {
                                if (result2.getString("address").length() > 50) {
                                    perAddcity = result2.getString("address").substring(0, 49);
                                } else {
                                    perAddcity = result2.getString("address");
                                }
                            } else {
                                perAddcity = "";
                            }
                            perAddTown = result2.getString("village_name");
                            perAddpoliceStation = result2.getString("ps_name");
                        }
                        if (result2.getString("address_type").equals("PRESENT")) {
                            cdistricyId = result2.getString("int_district_id");
                            cstateCode = result2.getString("state_code");

                            comAddpin = result2.getString("pin");
                            if (result2.getString("address") != null && !result2.getString("address").equals("")) {
                                if (result2.getString("address").length() > 50) {
                                    comAddcity = result2.getString("address").substring(0, 49);
                                } else {
                                    comAddcity = result2.getString("address");
                                }

                            } else {
                                comAddcity = "";
                            }

                            comAddTown = result2.getString("village_name");
                            comAddpoliceStation = result2.getString("ps_name");
                        }
                    }
                    //  System.out.println("==="+cdistricyCode);
                    employee.setDistrictCode(districyCode);
                    employee.setPerAddpin(perAddpin);
                    employee.setPerAddstateId(stateCode);
                    employee.setPerAddcity(perAddcity);
                    employee.setPerAddtown(perAddTown);
                    employee.setPerAddpoliceStation(perAddpoliceStation);
                    employee.setCommAddpin(comAddpin);
                    employee.setCommAddstateId(cstateCode);
                    employee.setCommAddtown(comAddTown);
                    employee.setCommAddpoliceStation(comAddpoliceStation);
                    employee.setCommAddcity(comAddcity);
                    // employee.setcDistrictCode(cdistricyId);             
                    employee.setCommDistrictCode(cdistricyId);

                    String SQLFamily = "SELECT int_title,f_name, m_name,l_name,bank_name,branch_name,G_BRANCH.ifsc_code,initials,* FROM  emp_relation "
                            + " LEFT OUTER JOIN G_TITLE ON emp_relation.initials=G_TITLE.title "
                            + " LEFT OUTER JOIN G_BANK ON emp_relation.BANK_CODE = G_BANK.BANK_CODE   LEFT OUTER JOIN G_BRANCH ON emp_relation.BRANCH_CODE = G_BRANCH.BRANCH_CODE  "
                            + " WHERE emp_id=?";
                    pstmt = conn.prepareStatement(SQLFamily);
                    pstmt.setString(1, empid);
                    result1 = pstmt.executeQuery();
                    String guardiantittle = "";
                    while (result1.next()) {
                        familylist = new PensionFamilyList();
                        if (result1.getString("initials") != null && !result1.getString("initials").equals("")) {
                            familylist.setSalutationFamily(result1.getString("initials"));
                        } else {
                            familylist.setSalutationFamily("");
                        }
                        familylist.setPriority(result1.getString("priority_lvl"));
                        familylist.setGuardianfname(result1.getString("f_name"));
                        familylist.setGuardianmname(result1.getString("m_name"));
                        familylist.setGuardianlname(result1.getString("l_name"));
                        familylist.setFamilyAddress(result1.getString("address"));
                        if (result1.getString("gender") != null && !result1.getString("gender").equals("")) {
                            if (result1.getString("gender").equals("M")) {
                                familylist.setSex("M");
                            } else if (result1.getString("gender").equals("F")) {
                                familylist.setSex("F");
                            } else {
                                familylist.setSex("O");
                            }
                        } else {
                            familylist.setSex("O");
                        }

                        String familyrel = result1.getString("relation");
                        // familylist.setRelationshipId(result1.getString("relation"));
                        if (familyrel.toUpperCase().equals("FATHER")) {
                            familylist.setRelationshipId("1");
                        } else if (familyrel.toUpperCase().equals("MOTHER")) {
                            familylist.setRelationshipId("2");
                        } else if (familyrel.toUpperCase().equals("HUSBAND")) {
                            familylist.setRelationshipId("4");
                        } else if (familyrel.toUpperCase().equals("WIFE")) {
                            familylist.setRelationshipId("5");
                        } else if (familyrel.toUpperCase().equals("DAUGHTER")) {
                            familylist.setRelationshipId("7");
                        } else if (familyrel.toUpperCase().equals("SON")) {
                            familylist.setRelationshipId("6");
                        } else if (familyrel.toUpperCase().equals("MOTHER IN LAW")) {
                            familylist.setRelationshipId("10");
                        } else if (familyrel.toUpperCase().equals("FATHER IN LAW")) {
                            familylist.setRelationshipId("9");
                        } else if (familyrel.toUpperCase().equals("BROTHER")) {
                            familylist.setRelationshipId("8");
                        } else if (familyrel.toUpperCase().equals("SISTER")) {
                            familylist.setRelationshipId("14");
                        } else {
                            familylist.setRelationshipId("0");
                        }

                        // System.out.println("==="+result1.getDate("dob"));
                        if (result1.getDate("dob") != null && !result1.getDate("dob").equals("")) {
                            familylist.setFamilyDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result1.getDate("dob")));
                        } else {
                            familylist.setFamilyDob("");
                        }

                        if (result1.getString("mobile") != null && !result1.getString("mobile").equals("")) {
                            familylist.setMobileNo(result1.getString("mobile"));
                        } else {
                            familylist.setMobileNo("");
                        }
                        if (result1.getString("ifsc_code") != null && !result1.getString("ifsc_code").equals("")) {
                            familylist.setIfscCode(result1.getString("ifsc_code"));
                        } else {
                            familylist.setIfscCode("");
                        }
                        if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                            familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                        } else {
                            familylist.setBankAccountNo("");
                        }
                        if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                            familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                        } else {
                            familylist.setBankAccountNo("");
                        }
                        if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                            familylist.setBankAccountNo(result1.getString("bank_acc_no"));
                        } else {
                            familylist.setBankAccountNo("");
                        }
                        if (result1.getString("arrears_per") != null && !result1.getString("arrears_per").equals("")) {
                            familylist.setFamilySharePercentage(result1.getString("arrears_per"));
                        } else {
                            familylist.setFamilySharePercentage("");
                        }
                        if (result1.getString("bank_name") != null && !result1.getString("bank_name").equals("")) {
                            String familybanBarnch = result1.getString("bank_name") + " " + result1.getString("branch_name");
                            familylist.setBankBranchName(familybanBarnch);
                        } else {
                            familylist.setBankBranchName("");
                        }
                        if (result1.getString("remarks") != null && !result1.getString("remarks").equals("")) {
                            familylist.setFamilyRemarks(result1.getString("remarks"));
                        } else {
                            familylist.setFamilyRemarks("");
                        }
                        if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                            familylist.setMinorFlag("Yes");
                        } else {
                            familylist.setMinorFlag("");
                        }
//                        if (result1.getString("initial_guardian") != null && !result1.getString("initial_guardian").equals("")) {
//                            familylist.setSalutationgurdian(result1.getString("initial_guardian"));
//                        } else {
//                            familylist.setSalutationgurdian("");
//                        }

                        String salutation = result1.getString("initial_guardian");
                        if (salutation != null && !salutation.equals("")) {
                            if (salutation.toUpperCase().equals("MR")) {
                                guardiantittle = "Mr.";
                            } else if (salutation.toUpperCase().equals("MRS")) {
                                guardiantittle = "Mrs.";
                            } else if (salutation.toUpperCase().equals("MISS")) {
                                guardiantittle = "Miss";
                            } else if (salutation.toUpperCase().equals("PROF")) {
                                guardiantittle = "prof.";
                            } else if (salutation.equals("Dr(Allopathic)")) {
                                guardiantittle = "Dr(Allopathic)";
                            } else if (salutation.equals("Dr(Med. College Prof)")) {
                                guardiantittle = "Dr(Med. College Prof)";
                            } else if (salutation.equals("Dr(Academic)")) {
                                guardiantittle = "Dr(Academic)";
                            } else if (salutation.equals("Dr(Others)")) {
                                guardiantittle = "Dr(Others)";
                            } else {
                                guardiantittle = "";
                            }
                        } else {
                            guardiantittle = "";
                        }

                        if (result1.getString("minor_guardian") != null && !result1.getString("minor_guardian").equals("")) {
                            familylist.setGurdianName(result1.getString("minor_guardian"));
                        } else {
                            familylist.setGurdianName("");
                        }
                        if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                            familylist.setTiApplicableFlag("Yes");
                        } else {
                            familylist.setTiApplicableFlag("");
                        }
                        if (result1.getString("is_pwd") != null && !result1.getString("is_pwd").equals("")) {
                            familylist.setFamilyHandicappedFlag("Yes");
                        } else {
                            familylist.setFamilyHandicappedFlag("");
                        }
                        if (result1.getString("pwd_type") != null && !result1.getString("pwd_type").equals("")) {
                            familylist.setFamilyHandicappedTypeId(result1.getString("pwd_type"));
                        } else {
                            familylist.setFamilyHandicappedTypeId("");
                        }

                        if (result1.getString("nomination_cvp") != null && !result1.getString("nomination_cvp").equals("")) {
                            familylist.setCvp(result1.getString("nomination_cvp"));
                        } else {
                            familylist.setCvp("");
                        }

                        if (result1.getString("arrears_acc") != null && !result1.getString("arrears_acc").equals("")) {
                            familylist.setCvpPercentage(result1.getString("arrears_acc"));
                        } else {
                            familylist.setCvpPercentage("");
                        }

                        String fmStatus = result1.getString("marital_status");
                        //  System.out.println("fmStatus====="+fmStatus);
                        if (fmStatus != null && !fmStatus.equals("")) {
                            if (fmStatus.toUpperCase().equals("1")) {
                                familylist.setIntMaritalStatusTypeId("1");
                            } else if (fmStatus.toUpperCase().equals("7") || fmStatus.toUpperCase().equals("2") || fmStatus.toUpperCase().equals("5")) {
                                familylist.setIntMaritalStatusTypeId("2");
                            } else if (fmStatus.toUpperCase().equals("3")) {
                                familylist.setIntMaritalStatusTypeId("3");
                            } else if (fmStatus.toUpperCase().equals("8") || fmStatus.toUpperCase().equals("4") || fmStatus.toUpperCase().equals("WIDOW")) {
                                familylist.setIntMaritalStatusTypeId("4");
                            } else {
                                familylist.setIntMaritalStatusTypeId("0");
                            }
                        }

                        //  System.out.println("fmStatus1111====="+familylist.getIntMaritalStatusTypeId());
                        familylist.setSalutationgurdian(guardiantittle);
                        familylist.setIsNominee(result1.getString("is_nominee"));
                        familylist.setIsGuardian(result1.getString("is_guardian"));
                        outList.add(familylist);
                    }

                    /**
                     * ********************** Guardian Details
                     * **********************
                     */
                    String SQLGuardian = "SELECT int_title,f_name,l_name,m_name,initial_guardian,* FROM  emp_relation "
                            + " LEFT OUTER JOIN G_TITLE ON emp_relation.initial_guardian=G_TITLE.title  "
                            + " WHERE emp_id=? AND is_guardian=? LIMIT 1 ";
                    pstmt = conn.prepareStatement(SQLGuardian);
                    pstmt.setString(1, empid);
                    pstmt.setString(2, "Y");
                    result1 = pstmt.executeQuery();
                    String guardianSaluta = "";
                    String guardianFName = "";
                    String guardianLName = "";
                    String guardianMName = "";
                    String guardianrelationId = "";

                    if (result1.next()) {
                        if (result1.getString("int_title") != null && !result1.getString("int_title").equals("")) {
                            guardianSaluta = result1.getString("int_title");
                        } else {
                            guardianSaluta = "Other";
                        }
                        if (result1.getString("m_name") != null && !result1.getString("m_name").equals("")) {
                            guardianMName = result1.getString("m_name");
                        }
                        guardianLName = result1.getString("l_name");
                        guardianFName = result1.getString("f_name");
                        String guardianfamilyrel = result1.getString("relation");
//                     familylist.setRelationshipId(result1.getString("relation"));
                        if (guardianfamilyrel.toUpperCase().equals("FATHER")) {
                            guardianrelationId = "1";
                        } else if (guardianfamilyrel.toUpperCase().equals("MOTHER")) {
                            guardianrelationId = "2";
                        } else if (guardianfamilyrel.toUpperCase().equals("HUSBAND")) {
                            guardianrelationId = "3";
                        } else if (guardianfamilyrel.toUpperCase().equals("WIFE")) {
                            guardianrelationId = "4";
                        } else if (guardianfamilyrel.toUpperCase().equals("DAUGHTER")) {
                            guardianrelationId = "5";
                        } else if (guardianfamilyrel.toUpperCase().equals("SON")) {
                            guardianrelationId = "6";
                        } else if (guardianfamilyrel.toUpperCase().equals("MOTHER IN LAW")) {
                            guardianrelationId = "13";
                        } else if (guardianfamilyrel.toUpperCase().equals("FATHER IN LAW")) {
                            guardianrelationId = "14";
                        } else if (guardianfamilyrel.toUpperCase().equals("BROTHER")) {
                            guardianrelationId = "9";
                        } else if (guardianfamilyrel.toUpperCase().equals("SISTER")) {
                            guardianrelationId = "10";
                        } else if (guardianfamilyrel.toUpperCase().equals("OTHER")) {
                            guardianrelationId = "11";
                        } else {
                            guardianrelationId = "0";
                        }
                    }

                    employee.setSalutation(guardianSaluta);
                    employee.setGuardianFName(guardianFName);
                    employee.setGuardianLName(guardianLName);
                    employee.setGuardianMName(guardianMName);
                    employee.setGuardianRelId(guardianrelationId);

                    /**
                     * ********************* NomineeList
                     * ***************************
                     */
//                    String SQLNominee = "SELECT int_title,concat_ws (' ', f_name, m_name,l_name) AS name,bank_name,branch_name,initials,G_BRANCH.ifsc_code,* FROM  emp_relation "
//                            + " LEFT OUTER JOIN G_TITLE ON emp_relation.initials=G_TITLE.title "
//                            + " LEFT OUTER JOIN G_BANK ON emp_relation.BANK_CODE = G_BANK.BANK_CODE   LEFT OUTER JOIN G_BRANCH ON emp_relation.BRANCH_CODE = G_BRANCH.BRANCH_CODE  "
//                            + " WHERE emp_id=? AND  (nomination_gpf IS NOT NULL OR  nomination_cvp IS NOT NULL)";
                    String SQLNominee = "SELECT int_title,concat_ws (' ', f_name, m_name,l_name) AS name,bank_name,branch_name,initials,G_BRANCH.ifsc_code,* FROM  emp_relation\n"
                            + "LEFT OUTER JOIN G_TITLE ON emp_relation.initials=G_TITLE.title\n"
                            + "LEFT OUTER JOIN G_BANK ON emp_relation.BANK_CODE = G_BANK.BANK_CODE LEFT OUTER JOIN G_BRANCH ON emp_relation.BRANCH_CODE = G_BRANCH.BRANCH_CODE\n"
                            + "WHERE emp_id=? AND is_nominee='Y'";

                    pstmt = conn.prepareStatement(SQLNominee);
                    pstmt.setString(1, empid);
                    result1 = pstmt.executeQuery();
                    while (result1.next()) {
                        nomineelist = new PensionNomineeList();
                        nomineelist.setPenBenefitTypeId("1");
                        nomineelist.setNomineeOriginalOrAlterId("O");
                        if (result1.getString("initials") != null && !result1.getString("initials").equals("")) {
                            nomineelist.setSalutationNominee(result1.getString("initials"));
                        } else {
                            nomineelist.setSalutationNominee("Other");
                        }

                        nomineelist.setNomineeName(result1.getString("name"));
//                         nomineelist.setNomineeAddress(result1.getString("address"));
                        if (result1.getString("gender") != null && !result1.getString("gender").equals("")) {
                            if (result1.getString("gender").equals("M")) {
                                nomineelist.setFamilySex("M");
                            } else if (result1.getString("gender").equals("F")) {
                                nomineelist.setFamilySex("F");
                            } else {
                                nomineelist.setFamilySex("O");
                            }
                        } else {
                            nomineelist.setFamilySex("O");
                        }

                        String familyrel = result1.getString("relation");
                        // familylist.setRelationshipId(result1.getString("relation"));
                        if (familyrel.toUpperCase().equals("FATHER")) {
                            guardianrelationId = "1";
                        } else if (familyrel.toUpperCase().equals("MOTHER")) {
                            guardianrelationId = "2";
                        } else if (familyrel.toUpperCase().equals("HUSBAND")) {
                            guardianrelationId = "3";
                        } else if (familyrel.toUpperCase().equals("WIFE")) {
                            guardianrelationId = "4";
                        } else if (familyrel.toUpperCase().equals("DAUGHTER")) {
                            guardianrelationId = "5";
                        } else if (familyrel.toUpperCase().equals("SON")) {
                            guardianrelationId = "6";
                        } else if (familyrel.toUpperCase().equals("MOTHER IN LAW")) {
                            guardianrelationId = "13";
                        } else if (familyrel.toUpperCase().equals("FATHER IN LAW")) {
                            guardianrelationId = "14";
                        } else if (familyrel.toUpperCase().equals("BROTHER")) {
                            guardianrelationId = "9";
                        } else if (familyrel.toUpperCase().equals("SISTER")) {
                            guardianrelationId = "10";
                        } else if (familyrel.toUpperCase().equals("OTHER")) {
                            guardianrelationId = "11";
                        } else {
                            guardianrelationId = "0";
                        }
                        // nomineelist.setNomineeMaritalStatusId(0);

                        if (result1.getDate("dob") != null && !result1.getDate("dob").equals("")) {
                            nomineelist.setDob(CommonFunctions.getFormattedOutputDateDDMMYYYY(result1.getDate("dob")));
                        } else {
                            nomineelist.setDob("");
                        }

                        if (result1.getString("mobile") != null && !result1.getString("mobile").equals("")) {
                            nomineelist.setMobileNo(result1.getString("mobile"));
                        } else {
                            nomineelist.setMobileNo("");
                        }
                        if (result1.getString("ifsc_code") != null && !result1.getString("ifsc_code").equals("")) {
                            nomineelist.setIfscCode(result1.getString("ifsc_code"));
                        } else {
                            nomineelist.setIfscCode("");
                        }
                        if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
                            nomineelist.setBankAccountNo(result1.getString("bank_acc_no"));
                        } else {
                            nomineelist.setBankAccountNo("");
                        }
//                    if (result1.getString("bank_acc_no") != null && !result1.getString("bank_acc_no").equals("")) {
//                        nomineelist.setBankAccountNo(result1.getString("bank_acc_no"));
//                    } else {
//                        nomineelist.setBankAccountNo("");
//                    }

                        if (result1.getString("arrears_per") != null && !result1.getString("arrears_per").equals("")) {
                            nomineelist.setSharePercentage(result1.getString("arrears_per"));
                        } else {
                            nomineelist.setSharePercentage("");
                        }

                        if (result1.getString("bank_name") != null && !result1.getString("bank_name").equals("")) {
                            String familybanBarnch = result1.getString("bank_name") + " " + result1.getString("branch_name");
                            nomineelist.setBankBranchName(familybanBarnch);
                        } else {
                            nomineelist.setBankBranchName("");
                        }

                        if (result1.getString("is_minor") != null && !result1.getString("is_minor").equals("")) {
                            nomineelist.setMinorFlag("Yes");
                        } else {
                            nomineelist.setMinorFlag("");
                        }
                        if (result1.getString("minor_guardian") != null && !result1.getString("minor_guardian").equals("")) {
                            nomineelist.setGurdianName(result1.getString("minor_guardian"));
                        } else {
                            nomineelist.setGurdianName("");
                        }

                        String fmStatus = result1.getString("marital_status");
                        if (fmStatus != null && !fmStatus.equals("")) {
                            if (fmStatus.toUpperCase().equals("1")) {
                                nomineelist.setNomineeMaritalStatusId("1");
                            } else if (fmStatus.toUpperCase().equals("7") || fmStatus.toUpperCase().equals("2") || fmStatus.toUpperCase().equals("5")) {
                                nomineelist.setNomineeMaritalStatusId("2");
                            } else if (fmStatus.toUpperCase().equals("3")) {
                                nomineelist.setNomineeMaritalStatusId("3");
                            } else if (fmStatus.toUpperCase().equals("8") || fmStatus.toUpperCase().equals("4") || fmStatus.toUpperCase().equals("WIDOWER")) {
                                nomineelist.setNomineeMaritalStatusId("4");
                            } else {
                                nomineelist.setNomineeMaritalStatusId("0");
                            }
                        }

                        outList1.add(nomineelist);
                    }

                    /**
                     * ************************************** Previous Pension
                     * Details ************************************************
                     */
                    String PrevPenPia = "";
                    String PrevPPOOrFPPONo = "";
                    String PrevPensionEfffromDate = "";
                    String PenTypeId = "";
                    String PrevPenPayTresId = "";
                    String PrevPensionerId = "";
                    String PrevPenBankBranch = "";
                    String PrevPenBankIfscCd = "";
                    String PenPayTresCode = "";
                    String PrevPenSource = "";
                    String PrevPenAmt = "";

                    String previousPension = "select  * FROM emp_previous_pension WHERE id_employee=? limit 1";
                    pstmt2 = conn.prepareStatement(previousPension);
                    pstmt2.setString(1, empid);
                    result2 = pstmt2.executeQuery();
                    while (result2.next()) {
                        if (result2.getString("pension_type") != null && !result2.getString("pension_type").equals("")) {
                            PenTypeId = result2.getString("pension_type");
                        }
                        //  if (result2.getInt("pension_amount") != null && !result2.getInt("pension_amount").equals("")) {
                        PrevPenAmt = result2.getString("pension_amount");
                        // }

                        if (result2.getString("payable_treasure") != null && !result2.getString("payable_treasure").equals("")) {
                            PrevPenPayTresId = result2.getString("payable_treasure");
                        }
                        //   System.out.println("here"+PrevPenPayTresId);
                        if (result2.getString("ifsc_code") != null && !result2.getString("ifsc_code").equals("")) {
                            PrevPenBankIfscCd = result2.getString("ifsc_code");
                        }
                        if (result2.getString("issue_authority") != null && !result2.getString("issue_authority").equals("")) {
                            PrevPenPia = result2.getString("issue_authority");
                        }
                        if (result2.getString("source") != null && !result2.getString("source").equals("")) {
                            PrevPenSource = result2.getString("source");
                        }
                        if (result2.getString("ppo_no") != null && !result2.getString("ppo_no").equals("")) {
                            PrevPPOOrFPPONo = result2.getString("ppo_no");
                        }
                        if (result2.getDate("pension_effective_date") != null && !result2.getDate("pension_effective_date").equals("")) {
                            PrevPensionEfffromDate = (CommonFunctions.getFormattedOutputDateDDMMYYYY(result2.getDate("pension_effective_date")));
                        }
                        if (result2.getString("branch_name") != null && !result2.getString("branch_name").equals("")) {
                            PrevPenBankBranch = result2.getString("branch_name");
                        }

                    }
                    employee.setPrevPenType(PenTypeId);
                    employee.setPrevPenAmt(PrevPenAmt);
                    employee.setPrevPenPayTresId(PrevPenPayTresId);
                    employee.setPrevPenBankIfscCd(PrevPenBankIfscCd);
                    employee.setPrevPenPia(PrevPenPia);
                    employee.setPrevPenSource(PrevPenSource);
                    employee.setPrevPPOOrFPPONo(PrevPPOOrFPPONo);
                    employee.setPrevPensionEfffromDate(PrevPensionEfffromDate);
                    employee.setPrevPenBankBranch(PrevPenBankBranch);
                    //  employee.setPrevPensionerId(PrevPensionerId);              

//                    employee.setPrevPenPayTresCode(PenPayTresCode);
                    /**
                     * **************************************************************
                     */
                    String PayableTre = "select bill_mast.tr_code from bill_mast inner join aq_mast on bill_mast.bill_no=aq_mast.bill_no inner join emp_mast on aq_mast.emp_code=emp_mast.emp_id where emp_mast.emp_id=? order by bill_mast.aq_year desc,bill_mast.aq_month desc limit 1";
                    pstmt2 = conn.prepareStatement(PayableTre);
                    pstmt2.setString(1, empid);
                    result2 = pstmt2.executeQuery();
                    while (result2.next()) {
                        if (result2.getString("tr_code") != null && !result2.getString("tr_code").equals("")) {
                            //   System.out.println("==="+result2.getString("tr_code"));
                            employee.setIntTreasuryId(result2.getString("tr_code"));
                        } else {
                            employee.setIntTreasuryId("");
                        }
                    }

                    employee.setPensionfamilylist(outList);
                    employee.setPensionnomineelist(outList1);

                }

            } else {
                employee.setMsg("N");
            }
        } catch (Exception e) {
            // employee.setFname(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(result2, pstmt2);
            DataBaseFunctions.closeSqlObjects(result1, pstmt2);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return employee;
    }

    @Override
    public Address getAddressForServiceBook(String empid) {

        String permanentAddress = "";
        String presentAddress = "";

        Address addr = new Address();
        String SQL = "SELECT STATE_NAME,DIST_NAME,BL_NAME,PS_NAME,PO_NAME,VILLAGE_NAME,ADDRESS_ID,ADDRESS_TYPE,ADDRESS,EMP_ADDRESS.PIN,EMP_ADDRESS.STATE_CODE,STD_CODE,TPHONE,is_locked"
                + " FROM EMP_ADDRESS"
                + " INNER JOIN G_DISTRICT ON EMP_ADDRESS.DIST_CODE=G_DISTRICT.DIST_CODE"
                + " LEFT OUTER JOIN G_BLOCK ON EMP_ADDRESS.BL_CODE::INTEGER=G_BLOCK.BL_CODE"
                + " INNER JOIN G_PS ON EMP_ADDRESS.PS_CODE=G_PS.PS_CODE"
                + " INNER JOIN G_PO ON EMP_ADDRESS.PO_CODE=G_PO.PO_CODE"
                + " INNER JOIN G_STATE ON EMP_ADDRESS.STATE_CODE=G_STATE.STATE_CODE"
                + " LEFT OUTER JOIN G_VILLAGE ON EMP_ADDRESS.VILL_CODE=G_VILLAGE.VILL_CODE"
                + " WHERE EMP_ID=? and ADDRESS_TYPE= 'PERMANENT' ORDER BY ADDRESS_ID DESC limit 1";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = this.repodataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {
                addr.setEmpId(empid);
                addr.setAddressId(result.getInt("ADDRESS_ID"));
                addr.setAddressType(result.getString("ADDRESS_TYPE"));
                addr.setAddress(result.getString("ADDRESS"));

                addr.setBlockCode(result.getString("BL_NAME"));
                if (addr.getBlockCode() != null && !addr.getBlockCode().equals("")) {
                    permanentAddress = "BLOCK-" + addr.getBlockCode();
                }
                addr.setVillageCode(result.getString("VILLAGE_NAME"));
                if (addr.getVillageCode() != null && !addr.getVillageCode().equals("")) {
                    permanentAddress = permanentAddress + ",VILL-" + addr.getVillageCode();
                }
                addr.setPostCode(result.getString("PO_NAME"));
                if (addr.getPostCode() != null && !addr.getPostCode().equals("")) {
                    permanentAddress = permanentAddress + ",PO-" + addr.getPostCode();
                }
                addr.setPsCode(result.getString("PS_NAME"));
                if (addr.getPsCode() != null && !addr.getPsCode().equals("")) {
                    permanentAddress = permanentAddress + ",PS-" + addr.getPsCode();
                }
                addr.setDistCode(result.getString("DIST_NAME"));
                if (addr.getDistCode() != null && !addr.getDistCode().equals("")) {
                    permanentAddress = permanentAddress + ",DIST-" + addr.getDistCode();
                }
                addr.setStateCode(result.getString("state_code"));

                addr.setStateCode(result.getString("STATE_NAME"));
                if (addr.getStateCode() != null && !addr.getStateCode().equals("")) {
                    permanentAddress = permanentAddress + ",STATE-" + addr.getStateCode();
                }
                addr.setPin(result.getString("PIN"));
                if (addr.getPin() != null && !addr.getPin().equals("")) {
                    permanentAddress = permanentAddress + ",PIN-" + addr.getPin();
                }
                addr.setStdCode(result.getString("STD_CODE"));
                addr.setTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));
            }
            addr.setPermanentAddress(permanentAddress);

            DataBaseFunctions.closeSqlObjects(result, statement);

            SQL = "SELECT STATE_NAME,DIST_NAME,BL_NAME,PS_NAME,PO_NAME,VILLAGE_NAME,ADDRESS_ID,ADDRESS_TYPE,ADDRESS,EMP_ADDRESS.PIN,EMP_ADDRESS.STATE_CODE,STD_CODE,TPHONE,is_locked"
                    + " FROM EMP_ADDRESS"
                    + " INNER JOIN G_DISTRICT ON EMP_ADDRESS.DIST_CODE=G_DISTRICT.DIST_CODE"
                    + " LEFT OUTER JOIN G_BLOCK ON EMP_ADDRESS.BL_CODE::INTEGER=G_BLOCK.BL_CODE"
                    + " INNER JOIN G_PS ON EMP_ADDRESS.PS_CODE=G_PS.PS_CODE"
                    + " INNER JOIN G_PO ON EMP_ADDRESS.PO_CODE=G_PO.PO_CODE"
                    + " INNER JOIN G_STATE ON EMP_ADDRESS.STATE_CODE=G_STATE.STATE_CODE"
                    + " LEFT OUTER JOIN G_VILLAGE ON EMP_ADDRESS.VILL_CODE=G_VILLAGE.VILL_CODE"
                    + " WHERE EMP_ID=? and ADDRESS_TYPE= 'PRESENT' ORDER BY ADDRESS_ID DESC limit 1";
            //System.out.println("SQL "+ SQL);

            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            if (result.next()) {

                addr.setEmpId(empid);
                addr.setPrAddressId(result.getInt("ADDRESS_ID"));
                addr.setPrAddressType(result.getString("ADDRESS_TYPE"));
                addr.setPrAddress(result.getString("ADDRESS"));

                addr.setPrBlockCode(result.getString("BL_NAME"));
                if (addr.getPrBlockCode() != null && !addr.getPrBlockCode().equals("")) {
                    presentAddress = "BLOCK-" + addr.getPrBlockCode();
                }
                addr.setPrVillageCode(result.getString("VILLAGE_NAME"));
                if (addr.getPrVillageCode() != null && !addr.getPrVillageCode().equals("")) {
                    presentAddress = presentAddress + ",VILL-" + addr.getPrVillageCode();
                }
                addr.setPrPostCode(result.getString("PO_NAME"));
                if (addr.getPrPostCode() != null && !addr.getPrPostCode().equals("")) {
                    presentAddress = presentAddress + ",PO-" + addr.getPrPostCode();
                }
                addr.setPrPsCode(result.getString("PS_NAME"));
                if (addr.getPrPsCode() != null && !addr.getPrPsCode().equals("")) {
                    presentAddress = presentAddress + ",PS-" + addr.getPrPsCode();
                }
                addr.setPrDistCode(result.getString("DIST_NAME"));
                if (addr.getPrDistCode() != null && !addr.getPrDistCode().equals("")) {
                    presentAddress = presentAddress + ",DIST-" + addr.getPrDistCode();
                }
                addr.setPrStateCode(result.getString("state_code"));

                addr.setPrStateCode(result.getString("STATE_NAME"));
                if (addr.getPrStateCode() != null && !addr.getPrStateCode().equals("")) {
                    presentAddress = presentAddress + ",STATE-" + addr.getPrStateCode();
                }
                addr.setPrPin(result.getString("PIN"));
                if (addr.getPrPin() != null && !addr.getPrPin().equals("")) {
                    presentAddress = presentAddress + ",PIN-" + addr.getPrPin();
                }
                addr.setPrStdCode(result.getString("STD_CODE"));
                addr.setPrTelephone(result.getString("TPHONE"));
                addr.setIsLocked(result.getString("is_locked"));
            }
            addr.setPresentAddress(presentAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return addr;
    }
}

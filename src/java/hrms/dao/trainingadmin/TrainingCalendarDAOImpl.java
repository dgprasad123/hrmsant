/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.trainingadmin;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.model.login.Users;
import hrms.model.master.Department;
import hrms.model.master.GPost;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import hrms.model.trainingadmin.EmpAttendanceBean;
import hrms.model.trainingadmin.EmployeeSearch;
import hrms.model.trainingadmin.InstituteForm;
import hrms.model.trainingadmin.NISGTrainingBean;
import hrms.model.trainingadmin.OnlineTrainingBean;
import hrms.model.trainingadmin.ParticipantBean;
import hrms.model.trainingadmin.TrainingBatchBean;
import hrms.model.trainingadmin.TrainingDocumentBean;
import hrms.model.trainingadmin.TrainingFacultyForm;
import hrms.model.trainingadmin.TrainingProgramForm;
import hrms.model.trainingadmin.TrainingProgramList;
import hrms.model.trainingadmin.TrainingSponsorForm;
import hrms.model.trainingadmin.TrainingVenueForm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import jxl.Workbook;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class TrainingCalendarDAOImpl implements TrainingCalendarDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int[] getGovtHolidaysList(int theMonth, int theYear) {
        int noOfDay[] = new int[32];
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String strMonth = (theMonth + 1) + "";
        if (strMonth.length() == 1) {
            strMonth = "0" + strMonth;
        }

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT G_HOLIDAY.HID,G_HOLIDAY.FDATE, G_HOLIDAY.TDATE,G_HOLIDAY.HTYPE  FROM G_CAL_HOLIDAYS INNER JOIN G_HOLIDAY ON G_CAL_HOLIDAYS.HID=G_HOLIDAY.HID AND G_CAL_HOLIDAYS.CYEAR='" + theYear + "' AND TO_char(G_HOLIDAY.FDATE,'MM')='" + strMonth + "' AND G_HOLIDAY.HTYPE='G'");
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                Date FDATE = rs.getDate("FDATE");
                int holidayLength = 1;
                Calendar cal = Calendar.getInstance();
                cal.setTime(FDATE);
                for (int j = 0; j < holidayLength; j++) {
                    if (!isHolidayAdded(cal.get(Calendar.DATE) + j, noOfDay)) {
                        noOfDay[i] = cal.get(Calendar.DATE) + j;
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noOfDay;
    }

    public boolean isHolidayAdded(int days, int[] noOfDay) {
        boolean holidayadded = false;
        for (int j = 0; j < noOfDay.length; j++) {
            if (noOfDay[j] == days) {
                holidayadded = true;
                break;
            }
        }
        return holidayadded;
    }

    public boolean isDatePresentInList(int[] holidayList, int date) {
        boolean status = false;
        for (int i = 0; i < holidayList.length; i++) {
            if (date == holidayList[i]) {
                status = true;
                break;
            }
        }
        return status;
    }

    @Override
    public List getSelectboxSponsorList(String ownerId) {
        List li = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT sponsor_id, sponsor_name FROM g_training_sponsors WHERE owner_id = '" + ownerId + "' ORDER BY sponsor_name");
            rs = ps.executeQuery();
            SelectOption so = null;
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("sponsor_name"));
                so.setValue(rs.getString("sponsor_id"));
                li.add(so);
            }
            //crit.add(Restrictions.eq("active", "Y"));
            //crit.addOrder(Order.asc("deptName"));
            //li = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return li;
    }

    @Override
    public String getCadreList(String empId, String trainingId) {
        String result = null;
        String programName = null;
        String fromDate = null;
        String toDate = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT program_name, to_char(to_date, 'DD-Mon-YYYY') AS to_date "
                    + ", to_char(from_date, 'DD-Mon-YYYY') AS from_date FROM g_training_programs "
                    + "WHERE owner_id = '" + empId + "' AND training_program_code = " + trainingId);
            rs = ps.executeQuery();
            while (rs.next()) {
                programName = rs.getString("program_name");
                fromDate = rs.getString("from_date");
                toDate = rs.getString("to_date");
            }
            result = "<table width='90%' class='table-bordered' style='font-size:9pt;margin-bottom:5px;'>"
                    + "<tr style='font-weight:bold;background:#EAEAEA;'><td colspan='4' style='padding:5px;'>Training Program Detail"
                    + "</td>"
                    + "</tr><tr><td style='padding:5px;'>Program Name:</td><td colspan='3' style='padding:5px;'>" + programName + "</td></tr><tr>"
                    + "<td style='padding:5px;'>From Date:</td><td style='padding:5px;'>" + fromDate + "</td>"
                    + "<td style='padding:5px;'>To Date:</td><td style='padding:5px;'>" + toDate + "</td>"
                    + "</tr>"
                    + "</table><h1 style=\"font-size:13pt;margin-top:0px;\">Assign Cadres for the above Program</h1>";
            ps = con.prepareStatement("SELECT GC.cadre_code, GC.cadre_name, D.department_name, coalesce(GCG.grade, 'N/A') AS grade"
                    + " "
                    + "FROM g_cadre GC LEFT OUTER JOIN g_cadre_grade GCG ON GC.cadre_code = GCG.cadre_code"
                    + " INNER JOIN g_department D ON "
                    + "GC.department_code = D.department_code ORDER BY D.department_name");
            rs = ps.executeQuery();
            result += "<table width='100%' cellspacing='1' cellpadding='4' border='0' id='table_cadre' class='table-bordered' style='font-size:9pt;'>"
                    + "<tr bgcolor='#EAEAEA' style='font-weight:bold;'>"
                    + "<td></td>"
                    + "<td>Department</td>"
                    + "<td>Cadre</td>"
                    + "<td>Grade</td>"
                    + "</tr>";
            int numEntries = 0;
            String gradeId = null;
            while (rs.next()) {
                //so.setLabel(rs.getString("cadre_name"));
                //so.setValue(rs.getString("cadre_code"));
                gradeId = rs.getString("grade").toLowerCase();
                gradeId = gradeId.replaceAll("[^a-zA-Z0-9]", "");
                ps1 = con.prepareStatement("SELECT COUNT(*) AS has_entry FROM g_program_cadres GPC INNER JOIN g_training_programs GTP"
                        + " ON GPC.training_program_code = GTP.training_program_code LEFT OUTER JOIN g_cadre_grade GCG ON GPC.cadre_code = GCG.cadre_code "
                        + " WHERE GPC.cadre_code = '" + rs.getString("cadre_code") + "' AND GPC.grade = '" + rs.getString("grade") + "' "
                        + "AND GPC.training_program_code = " + trainingId + " AND GTP.owner_id = '" + empId + "' ");
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    numEntries = rs1.getInt("has_entry");
                }
                if (numEntries > 0) {
                    result += "<tr id='li_" + rs.getString("cadre_code") + "_" + gradeId + "'>"
                            + "<td style='margin-left:10px;background:#FFF0AF'><input type='checkbox' name='cadre_names[]' id='cadre_name_" + rs.getString("cadre_code") + "_" + gradeId + "' value='" + rs.getString("cadre_code") + "|" + rs.getString("grade") + "' onclick=\"javascript: selectCadre('" + rs.getString("cadre_code") + "', '" + rs.getString("grade") + "', '" + gradeId + "', this)\" checked='checked' /></td>"
                            + "<td style='margin-left:10px;background:#FFF0AF'><label for='cadre_name_" + rs.getString("cadre_code") + "_" + gradeId + "' style='cursor:pointer;font-weight:normal;'><strong>" + rs.getString("department_name") + "</strong></label></td>"
                            + "<td style='margin-left:10px;background:#FFF0AF'>" + rs.getString("cadre_name") + "</td>"
                            + "<td style='margin-left:10px;background:#FFF0AF'>" + rs.getString("grade") + "</td>";
                } else {
                    result += "<tr id='li_" + rs.getString("cadre_code") + "_" + gradeId + "'><td><input type='checkbox' name='cadre_names[]' id='cadre_name_" + rs.getString("cadre_code") + "_" + gradeId + "' value='" + rs.getString("cadre_code") + "|" + rs.getString("grade") + "' onclick=\"javascript: selectCadre('" + rs.getString("cadre_code") + "', '" + rs.getString("grade") + "', '" + gradeId + "', this)\" /></td>"
                            + "<td><label for='cadre_name_" + rs.getString("cadre_code") + "_" + gradeId + "' style='cursor:pointer;font-weight:normal;width:90%'><strong>" + rs.getString("department_name") + "</strong></label></td>"
                            + "<td>" + rs.getString("cadre_name") + "</td>"
                            + "<td>" + rs.getString("grade") + "</td>";
                }

            }
            result += "</table>";
            //crit.add(Restrictions.eq("active", "Y"));
            //crit.addOrder(Order.asc("deptName"));
            //li = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        result = result + "</ul>";
        return result;
    }

    public List getPostListWithName(String offcode) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List postlist = new ArrayList();
        try {
            SelectOption so = null;
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT EM.emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, GP.post FROM g_office GO"
                    + " INNER JOIN emp_mast EM ON GO.off_code = EM.cur_off_code"
                    + " LEFT OUTER JOIN G_SPC GS ON GS.spc = EM.cur_spc"
                    + " LEFT OUTER JOIN G_POST GP ON GP.post_code = GS.gpc"
                    + " WHERE EM.cur_off_code = '" + offcode + "'"
                    + " ORDER BY f_name";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                String lbl = null;
                lbl = rs.getString("EMPNAME");
                if (rs.getString("post") != null) {
                    lbl = lbl + ", " + rs.getString("post");
                }
                so.setLabel(lbl);
                so.setValue(rs.getString("emp_id"));
                postlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postlist;
    }

    @Override
    public void SaveTrainingProgram(TrainingProgramForm trainingPrograms, String empID, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        String trainingProgram = null;
        String fromDate = null;
        String toDate = null;
        Date dt1 = null;
        Date dt2 = null;
        String originalFileName = null;
        String contentType = null;
        String newFileName = null;
        String isArchived = "N";
        int capacity = 0;
        String trainingAuthority = null;
        int venueId = 0;
        MultipartFile documentFile = trainingPrograms.getDocumentFile();

        try {

            con = this.dataSource.getConnection();
            trainingProgram = trainingPrograms.getTrainingProgram();
            fromDate = trainingPrograms.getFromDate();
            toDate = trainingPrograms.getToDate();
            venueId = Integer.parseInt(trainingPrograms.getVenueId());
            dt1 = new Date(fromDate);
            dt2 = new Date(toDate);
            isArchived = trainingPrograms.getIsArchived();
            capacity = trainingPrograms.getCapacity();
            trainingAuthority = trainingPrograms.getTrainingAuthority();
            String[] authorityArr = trainingAuthority.split("\\|");
            trainingAuthority = authorityArr[1];
            String trainingAuthoritySpc = authorityArr[0];
            String str = "INSERT INTO g_training_programs(program_name ,from_date ,to_date ,owner_id"
                    + ", venue_id, capacity, training_authority, training_authority_spc, is_archived) values(?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, trainingProgram);
            ps.setTimestamp(2, new Timestamp(dt1.getTime()));
            ps.setTimestamp(3, new Timestamp(dt2.getTime()));
            ps.setString(4, empID);
            ps.setInt(5, venueId);
            ps.setInt(6, capacity);
            ps.setString(7, trainingAuthority);
            ps.setString(8, trainingAuthoritySpc);
            ps.setString(9, isArchived);
            int status = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int trainingID = rs.getInt(1);
            if (documentFile != null && !documentFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = documentFile.getOriginalFilename();
                contentType = documentFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = empID + "_" + trainingID + "_" + time;

                String dirpath = filePath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = documentFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                str = "UPDATE g_training_programs SET disk_file_name = ?, original_file_name = ?, content_type = ? WHERE training_program_code = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, trainingID);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getTrainingProgramList(String programDate, String ownerID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String trainingProgram = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT program_name FROM G_training_programs WHERE owner_id = '" + ownerID + "' AND '" + programDate + "' BETWEEN from_date AND to_date");
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                if (trainingProgram == null) {
                    trainingProgram = "<li>" + rs.getString("program_name") + "</li>";
                } else {
                    trainingProgram += "<li>" + rs.getString("program_name") + "</li>";
                }
                i++;
            }
            if (i > 0) {
                trainingProgram = "<ul>" + trainingProgram + "</ul>";
                trainingProgram += "<a href='javascript:void(0)' onclick=\"javascript:openWindow('ManageTrainingProgramAction.htm?opt=list&date=" + programDate + "','Manage Training Program')\" style='color:#AA0000;text-decoration:none;font-weight:bold;'>Click to Manage</a>";
            }
            if (i == 1) {
                trainingProgram = "<p>One Training Program available:</p>" + trainingProgram;
            }
            if (i > 1) {
                trainingProgram = "<p>" + i + " Training Programs available:</p>" + trainingProgram;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingProgram;
    }

    public List getTrainingPrograms(String hrmsId, String date) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration"
                    + " FROM g_training_programs WHERE owner_id = '" + hrmsId + "'"
            );
            TrainingProgramList tpl = null;
            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setDuration(rs.getString("duration") + " Days");
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void deleteTrainingProgram(int trainingId, String empId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = "DELETE FROM g_training_programs WHERE training_program_code = ? AND owner_id = ?";
            ps = con.prepareStatement(str);
            ps.setInt(1, trainingId);
            ps.setString(2, empId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public TrainingProgramForm getTrainingDetail(int trainingId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        TrainingProgramForm tpl = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT TP.program_name, GI.institution_name, to_char(TP.from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(TP.to_date, 'DD-Mon-YYYY') as to_date, TV.venue_code, TP.capacity"
                    + ", TP.training_authority, TP.training_authority_spc, original_file_name"
                    + ", content_type, disk_file_name "
                    + "FROM g_training_programs TP "
                    + " INNER JOIN g_institutions GI ON GI.institution_code = cast(TP.owner_id AS integer)"
                    + "INNER JOIN g_training_venues TV ON TP.venue_id = TV.venue_code"
                    + " WHERE TP.training_program_code = " + trainingId);
            while (rs.next()) {
                tpl = new TrainingProgramForm();
                tpl.setTrainingProgram(rs.getString("program_name"));
                tpl.setInstituteName(rs.getString("institution_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setVenueId(rs.getString("venue_code"));
                tpl.setCapacity(rs.getInt("capacity"));
                tpl.setTrainingAuthority(rs.getString("training_authority_spc") + "|" + rs.getString("training_authority"));
                tpl.setStrFile("");
                //tpl.setStrTrainingAuthority(rs.getString("spn"));
                if (rs.getString("original_file_name") != null) {
                    tpl.setStrFile("<a href='downloadPdf.htm?trainingId=" + trainingId + "' target='_blank' width='18'><img src='images/pdf_icon.png' /> Download PDF</a><br /><a href='javascript:void(0)' onclick='javascript: deleteDocument(" + trainingId + ")'><img src='images/delete_icon.png' /> Delete</a>");
                }
                String officeName = "";
                //Now get Office
                if (rs.getString("training_authority") != null) {
                    st1 = con.createStatement();
                    rs1 = st1.executeQuery("SELECT spn FROM emp_mast EM INNER JOIN g_spc GS ON EM.cur_spc = GS.spc "
                            + "LEFT OUTER JOIN g_office GO ON GO.off_code = GS.off_code "
                            + " WHERE EM.cur_spc = '" + rs.getString("training_authority_spc") + "' AND EM.emp_id = '" + rs.getString("training_authority") + "'");
                    while (rs1.next()) {
                        officeName = rs1.getString("spn");
                    }
                }
                tpl.setStrTrainingAuthority(officeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tpl;
    }

    @Override
    public void updateTrainingProgram(TrainingProgramForm trainingPrograms, String empID, String filePath) {
        Connection con = null;
        PreparedStatement ps = null;
        String trainingProgram = null;
        String fromDate = null;
        String toDate = null;
        String venueId = null;
        Date dt1 = null;
        Date dt2 = null;
        String originalFileName = null;
        String contentType = null;
        MultipartFile documentFile = trainingPrograms.getDocumentFile();
        int capacity = 0;
        String trainingAuthority = null;
        try {
            con = this.dataSource.getConnection();
            trainingProgram = trainingPrograms.getTrainingProgram();
            fromDate = trainingPrograms.getFromDate();
            toDate = trainingPrograms.getToDate();
            venueId = trainingPrograms.getVenueId();
            dt1 = new Date(fromDate);
            dt2 = new Date(toDate);
            capacity = trainingPrograms.getCapacity();
            trainingAuthority = trainingPrograms.getTrainingAuthority();
            String[] authorityArr = trainingAuthority.split("\\|");
            trainingAuthority = authorityArr[1];
            String trainingAuthoritySpc = authorityArr[0];
            String str = "UPDATE g_training_programs SET program_name = ? ,from_date = ?,to_date = ?"
                    + ", venue_id = ?, capacity = ?, training_authority = ?, training_authority_spc = ? "
                    + " WHERE training_program_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, trainingProgram);
            ps.setTimestamp(2, new Timestamp(dt1.getTime()));
            ps.setTimestamp(3, new Timestamp(dt2.getTime()));
            ps.setInt(4, Integer.parseInt(venueId));
            ps.setInt(5, capacity);
            ps.setString(6, trainingAuthority);
            ps.setString(7, trainingAuthoritySpc);
            ps.setInt(8, trainingPrograms.getTrainingId());
            int status = ps.executeUpdate();
            int trainingID = trainingPrograms.getTrainingId();
            if (documentFile != null && !documentFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = documentFile.getOriginalFilename();
                contentType = documentFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = empID + "_" + trainingID + "_" + time;

                String dirpath = filePath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = documentFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                str = "UPDATE g_training_programs SET disk_file_name = ?, original_file_name = ?, content_type = ? WHERE training_program_code = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, trainingID);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void assignCadreList(String trainingId, String cadreId, String grade, String status, String empId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = null;
            if (status.equals("Y")) {
                str = "INSERT INTO g_program_cadres(training_program_code, cadre_code, grade) VALUES(?,?,?)";
            } else {
                str = "DELETE FROM g_program_cadres WHERE training_program_code =? AND cadre_code = ? AND grade = ?";
            }

            ps = con.prepareStatement(str);
            ps.setInt(1, Integer.parseInt(trainingId));
            ps.setString(2, cadreId);
            ps.setString(3, grade);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addNewFaculty(TrainingFacultyForm trainingFaculty, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String facultyName = null;
        String designation = null;
        String facultyType = null;
        String abbr = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            facultyName = trainingFaculty.getFacultyName();
            designation = trainingFaculty.getDesignation();
            facultyType = trainingFaculty.getFacultyType();
            String str = "INSERT INTO g_training_faculties(faculty_name,designation, owner_id) values(?,?,?)";
            ps = con.prepareStatement(str);
            ps.setString(1, facultyName);
            ps.setString(2, designation);
            ps.setString(3, empID);

            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateTrainingFaculty(TrainingFacultyForm trainingFaculty, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String facultyName = null;
        String designation = null;
        int facultyCode = 0;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            facultyName = trainingFaculty.getFacultyName();
            designation = trainingFaculty.getDesignation();
            facultyCode = Integer.parseInt(trainingFaculty.getFacultyCode());
            String str = "UPDATE g_training_faculties SET faculty_name = ?,"
                    + " designation = ? WHERE faculty_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, facultyName);
            ps.setString(2, designation);
            ps.setInt(3, facultyCode);
            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getTrainingProgramList(String ownerId, int page, int rows) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            rs = st.executeQuery("SELECT training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, venue_id as venue, disk_file_name"
                    + ", (SELECT faculty_name FROM g_training_faculties GTF"
                    + " INNER JOIN g_program_faculties GPF ON GTF.faculty_code = GPF.faculty_code "
                    + " WHERE GPF.training_program_code = GTP.training_program_code LIMIT 1) AS facultyName"
                    + ", (SELECT sponsor_name FROM g_training_sponsors GTF"
                    + " INNER JOIN g_program_sponsors GPF ON GTF.sponsor_code = GPF.sponsor_code "
                    + " WHERE GPF.training_program_code = GTP.training_program_code LIMIT 1) AS sponsorName, capacity"
                    + " FROM g_training_programs GTP WHERE owner_id = '" + ownerId + "' AND to_date > (current_date - interval '90' day) ORDER BY from_date  limit "
                    + rows + " offset " + firstpage);

            TrainingProgramList tpl = null;
            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setDuration(rs.getString("duration") + " Days");
                tpl.setVenue(rs.getString("venue"));
                tpl.setFacultyName(rs.getString("facultyName"));
                tpl.setSponsorName(rs.getString("sponsorName"));
                tpl.setCapacity(rs.getInt("capacity"));
                if (rs.getString("disk_file_name") != null) {
                    tpl.setDiskFileName(rs.getString("disk_file_name"));
                } else {
                    tpl.setDiskFileName("");
                }
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public List getFacultyList(String ownerId, int page, int rows) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            rs = st.executeQuery("SELECT faculty_code, faculty_name, designation"
                    + " FROM g_training_faculties WHERE owner_id = '" + ownerId + "' limit " + rows + " offset " + firstpage);
            TrainingFacultyForm tpl = null;
            while (rs.next()) {
                tpl = new TrainingFacultyForm();
                tpl.setFacultyCode(rs.getString("faculty_code"));
                tpl.setFacultyName(rs.getString("faculty_name"));
                tpl.setDesignation(rs.getString("designation"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void deleteFaculty(int facultyCode) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = "DELETE FROM g_training_faculties WHERE faculty_code = ?";
            ps = con.prepareStatement(str);
            ps.setInt(1, facultyCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getSponsorList(String ownerId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT sponsor_code, sponsor_name"
                    + " FROM g_training_sponsors WHERE owner_id = '" + ownerId + "'");
            TrainingSponsorForm tpl = null;
            while (rs.next()) {
                tpl = new TrainingSponsorForm();
                tpl.setSponsorCode(rs.getString("sponsor_code"));
                tpl.setSponsorName(rs.getString("sponsor_name"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void addNewSponsor(TrainingSponsorForm trainingSponsor, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String sponsorName = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            sponsorName = trainingSponsor.getSponsorName();
            String str = "INSERT INTO g_training_sponsors(sponsor_name, owner_id) values(?,?)";
            ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sponsorName);
            ps.setString(2, empID);

            int status = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int auto_id = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateTrainingSponsor(TrainingSponsorForm trainingSponsor, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String sponsorName = null;
        int sponsorCode = 0;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            sponsorName = trainingSponsor.getSponsorName();
            sponsorCode = Integer.parseInt(trainingSponsor.getSponsorCode());
            String str = "UPDATE g_training_sponsors SET sponsor_name = ?"
                    + " WHERE sponsor_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, sponsorName);
            ps.setInt(2, sponsorCode);
            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void deleteSponsor(int sponsorCode) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = "DELETE FROM g_training_sponsors WHERE sponsor_code = ?";
            ps = con.prepareStatement(str);
            ps.setInt(1, sponsorCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getVenueList(String ownerId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT venue_code, venue_name"
                    + " FROM g_training_venues WHERE owner_id = '" + ownerId + "'");
            TrainingVenueForm tpl = null;
            while (rs.next()) {
                tpl = new TrainingVenueForm();
                tpl.setVenueCode(rs.getString("venue_code"));
                tpl.setVenueName(rs.getString("venue_name"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void addNewVenue(TrainingVenueForm trainingVenue, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String venueName = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            venueName = trainingVenue.getVenueName();
            String str = "INSERT INTO g_training_venues(venue_name, owner_id) values(?,?)";
            ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, venueName);
            ps.setString(2, empID);

            int status = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int autoID = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateTrainingVenue(TrainingVenueForm trainingVenue, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String venueName = null;
        int venueCode = 0;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            venueName = trainingVenue.getVenueName();
            venueCode = Integer.parseInt(trainingVenue.getVenueCode());
            String str = "UPDATE g_training_venues SET venue_name = ?"
                    + " WHERE venue_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, venueName);
            ps.setInt(2, venueCode);
            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void deleteVenue(int venueCode) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = "DELETE FROM g_training_venues WHERE venue_code = ?";
            ps = con.prepareStatement(str);
            ps.setInt(1, venueCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadDocument(HttpServletResponse response, String filePath, int trainingId) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;

        try {
            OutputStream out = response.getOutputStream();
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT disk_file_name, content_type, original_file_name FROM g_training_programs WHERE training_program_code = " + trainingId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filePath + "/" + rs.getString("disk_file_name");
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString("original_file_name");
                    String filetype = rs.getString("content_type");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getAssignedFacultyList(String empId, String trainingId) {
        String result = null;
        String programName = null;
        String fromDate = null;
        String toDate = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT program_name, to_char(to_date, 'DD-Mon-YYYY') AS to_date "
                    + ", to_char(from_date, 'DD-Mon-YYYY') AS from_date FROM g_training_programs "
                    + "WHERE owner_id = '" + empId + "' AND training_program_code = " + trainingId);
            rs = ps.executeQuery();
            while (rs.next()) {
                programName = rs.getString("program_name");
                fromDate = rs.getString("from_date");
                toDate = rs.getString("to_date");
            }
            result = "<table width='90%' class='table-bordered' style='font-size:9pt;margin-bottom:5px;'>"
                    + "<tr style='font-weight:bold;background:#EAEAEA;'><td colspan='4' style='padding:5px;'>Training Program Detail"
                    + "</td>"
                    + "</tr><tr><td style='padding:5px;'>Program Name:</td><td colspan='3' style='padding:5px;'>" + programName + "</td></tr><tr>"
                    + "<td style='padding:5px;'>From Date:</td><td style='padding:5px;'>" + fromDate + "</td>"
                    + "<td style='padding:5px;'>To Date:</td><td style='padding:5px;'>" + toDate + "</td>"
                    + "</tr>"
                    + "</table><h1 style=\"font-size:13pt;margin-top:0px;\">Assign Faculty for the above Program</h1>";
            ps = con.prepareStatement("SELECT TF.faculty_code, concat(faculty_name, ', ', designation) AS faculty_name"
                    + ", (SELECT COUNT(*) FROM g_program_faculties GPF INNER JOIN g_training_programs GTP"
                    + " ON GPF.training_program_code = GTP.training_program_code WHERE faculty_code = TF.faculty_code "
                    + "AND GPF.training_program_code = " + trainingId + " AND GTP.owner_id = '" + empId + "') AS has_entry "
                    + "FROM g_training_faculties TF WHERE TF.owner_id = '" + empId + "'"
                    + " ORDER BY TF.faculty_name");
            rs = ps.executeQuery();
            result += "<div  style=\"width:100%;height:350px;overflow-y:scroll;font-size:9pt;\"><ul style='list-style-type:none;margin:0px;padding:0px;'>";
            String hasEntry = null;
            while (rs.next()) {
                //so.setLabel(rs.getString("cadre_name"));
                //so.setValue(rs.getString("cadre_code"));
                hasEntry = rs.getString("has_entry");
                if (hasEntry.equals("1")) {
                    result += "<li id='li_" + rs.getString("faculty_code") + "' style='margin-left:10px;background:#FFF0AF'><input type='checkbox' name='faculty_names[]' id='faculty_name_" + rs.getString("faculty_code") + "' value='" + rs.getString("faculty_code") + "' onclick=\"javascript: selectFaculty('" + rs.getString("faculty_code") + "', this)\" checked='checked' /> <label for='faculty_name_" + rs.getString("faculty_code") + "' style='cursor:pointer;font-weight:normal;width:90%'>" + rs.getString("faculty_name") + "</label></li>";
                } else {
                    result += "<li id='li_" + rs.getString("faculty_code") + "' style='margin-left:10px;'><input type='checkbox' name='faculty_names[]' id='faculty_name_" + rs.getString("faculty_code") + "' value='" + rs.getString("faculty_code") + "' onclick=\"javascript: selectFaculty('" + rs.getString("faculty_code") + "', this)\" /> <label for='faculty_name_" + rs.getString("faculty_code") + "' style='cursor:pointer;font-weight:normal;width:90%'>" + rs.getString("faculty_name") + "</label></li>";
                }

            }
            result += "</ul></div>";
            //crit.add(Restrictions.eq("active", "Y"));
            //crit.addOrder(Order.asc("deptName"));
            //li = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        result = result + "</ul>";
        return result;
    }

    @Override
    public void assignFacultyList(String trainingId, int facultyCode, String status, String empId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = null;
            if (status.equals("Y")) {
                str = "INSERT INTO g_program_faculties(training_program_code, faculty_code) VALUES(?,?)";
            } else {
                str = "DELETE FROM g_program_faculties WHERE training_program_code =? AND faculty_code = ?";
            }

            ps = con.prepareStatement(str);
            ps.setInt(1, Integer.parseInt(trainingId));
            ps.setInt(2, facultyCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getAssignedSponsorList(String empId, String trainingId) {
        String result = null;
        String programName = null;
        String fromDate = null;
        String toDate = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT program_name, to_char(to_date, 'DD-Mon-YYYY') AS to_date "
                    + ", to_char(from_date, 'DD-Mon-YYYY') AS from_date FROM g_training_programs "
                    + "WHERE owner_id = '" + empId + "' AND training_program_code = " + trainingId);
            rs = ps.executeQuery();
            while (rs.next()) {
                programName = rs.getString("program_name");
                fromDate = rs.getString("from_date");
                toDate = rs.getString("to_date");
            }
            result = "<table width='90%' class='table-bordered' style='font-size:9pt;margin-bottom:5px;'>"
                    + "<tr style='font-weight:bold;background:#EAEAEA;'><td colspan='4' style='padding:5px;'>Training Program Detail"
                    + "</td>"
                    + "</tr><tr><td style='padding:5px;'>Program Name:</td><td colspan='3' style='padding:5px;'>" + programName + "</td></tr><tr>"
                    + "<td style='padding:5px;'>From Date:</td><td style='padding:5px;'>" + fromDate + "</td>"
                    + "<td style='padding:5px;'>To Date:</td><td style='padding:5px;'>" + toDate + "</td>"
                    + "</tr>"
                    + "</table><h1 style=\"font-size:13pt;margin-top:0px;\">Assign Sponsor for the above Program</h1>";
            ps = con.prepareStatement("SELECT SP.sponsor_code, sponsor_name"
                    + ", (SELECT COUNT(*) FROM g_program_sponsors GPC INNER JOIN g_training_programs GTP"
                    + " ON GPC.training_program_code = GTP.training_program_code WHERE sponsor_code = SP.sponsor_code "
                    + "AND GPC.training_program_code = " + trainingId + " AND GTP.owner_id = '" + empId + "') AS has_entry "
                    + "FROM g_training_sponsors SP WHERE SP.owner_id = '" + empId + "'"
                    + " ORDER BY SP.sponsor_name");
            rs = ps.executeQuery();
            result += "<div  style=\"width:100%;height:350px;overflow-y:scroll;font-size:9pt;\"><ul style='list-style-type:none;margin:0px;padding:0px;'>";
            String hasEntry = null;
            while (rs.next()) {
                //so.setLabel(rs.getString("cadre_name"));
                //so.setValue(rs.getString("cadre_code"));
                hasEntry = rs.getString("has_entry");
                if (hasEntry.equals("1")) {
                    result += "<li id='li_" + rs.getString("sponsor_code") + "' style='margin-left:10px;background:#FFF0AF'><input type='checkbox' name='sponsor_names[]' id='faculty_name_" + rs.getString("sponsor_code") + "' value='" + rs.getString("sponsor_code") + "' onclick=\"javascript: selectSponsor('" + rs.getString("sponsor_code") + "', this)\" checked='checked' /> <label for='sponsor_name_" + rs.getString("sponsor_code") + "' style='cursor:pointer;font-weight:normal;width:90%'>" + rs.getString("sponsor_name") + "</label></li>";
                } else {
                    result += "<li id='li_" + rs.getString("sponsor_code") + "' style='margin-left:10px;'><input type='checkbox' name='faculty_names[]' id='sponsor_name_" + rs.getString("sponsor_code") + "' value='" + rs.getString("sponsor_code") + "' onclick=\"javascript: selectSponsor('" + rs.getString("sponsor_code") + "', this)\" /> <label for='sponsor_name_" + rs.getString("sponsor_code") + "' style='cursor:pointer;font-weight:normal;width:90%'>" + rs.getString("sponsor_name") + "</label></li>";
                }

            }
            result += "</ul></div>";
            //crit.add(Restrictions.eq("active", "Y"));
            //crit.addOrder(Order.asc("deptName"));
            //li = crit.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        result = result + "</ul>";
        return result;
    }

    @Override
    public void assignSponsorList(String trainingId, int sponsorCode, String status, String empId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = null;
            if (status.equals("Y")) {
                str = "INSERT INTO g_program_sponsors(training_program_code, sponsor_code) VALUES(?,?)";
            } else {
                str = "DELETE FROM g_program_faculties WHERE training_program_code =? AND sponsor_code = ?";
            }

            ps = con.prepareStatement(str);
            ps.setInt(1, Integer.parseInt(trainingId));
            ps.setInt(2, sponsorCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getTrainingInstituteList() {
        String result = "";
        String website = null;
        String email = null;
        String phone = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM g_institutions ORDER BY institution_code");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("website") == null) {
                    website = "N/A";
                } else {
                    website = "<a href='" + rs.getString("website") + "' target='_blank'>" + rs.getString("website") + "</a>";
                }
                if (rs.getString("email") == null) {
                    email = "N/A";
                } else {
                    email = rs.getString("email");
                }
                if (rs.getString("phone") == null) {
                    phone = "N/A";
                } else {
                    phone = rs.getString("phone");
                }
                result += "<div class=\"institute\"><h2>" + rs.getString("institution_name") + "</h2>"
                        + "<span><strong>Address: </strong>" + rs.getString("location") + "</span><br />"
                        + "<span><strong>Website: </strong>" + website + "</span><br />"
                        + "<span><strong>Email: </strong>" + email + "</span><br />"
                        + "<span><strong>Phone: </strong>" + phone + "</span></div>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public String getUpcomingTrainingPrograms() {
        String result = "";
        String website = null;
        String email = null;
        String phone = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String bgcolor = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT institution_name, institution_code FROM g_institutions GI"
                    + " WHERE (select count(*) from g_training_programs WHERE GI.institution_code = cast(owner_id as integer) AND from_date > now()) > 0"
                    + " ORDER BY institution_code");
            rs = ps.executeQuery();
            while (rs.next()) {
                result += "<div class=\"institute\"><h2>" + rs.getString("institution_name") + "</h2>";
                ps1 = con.prepareStatement("SELECT TP.program_name,to_char(TP.from_date, 'DD-Mon-YYYY') AS from_date,  "
                        + "to_char(TP.to_date, 'DD-Mon-YYYY') AS to_date"
                        + ", DATE_PART('day', TP.to_date::timestamp - TP.from_date::timestamp)+1 AS duration, TV.venue_name"
                        + " FROM g_training_programs TP INNER JOIN g_training_venues TV ON TP.venue_id = TV.venue_code"
                        + " WHERE TP.owner_id = '" + rs.getString("institution_code") + "'  AND from_date > now() ORDER BY TP.from_date");
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    int cnt = 0;
                    result += "<table width='100%' cellspacing='1' cellpadding='5' border='0' bgcolor='#EAEAEA' style='font-size:9pt;'>";
                    result += "<tr bgcolor='#EAEAEA' style='font-weight:bold;'>"
                            + "<td>Training Program</td>"
                            + "<td>Start Date</td>"
                            + "<td>End Date</td>"
                            + "<td>Duration</td>"
                            + "<td>Venue</td>"
                            + "</tr>";

                    cnt++;
                    if (cnt % 2 == 0) {
                        bgcolor = "#FAFAFA";
                    } else {
                        bgcolor = "#FFFFFF";
                    }
                    result += "<tr bgcolor='" + bgcolor + "'>"
                            + "<td>" + rs1.getString("program_name") + "</td>"
                            + "<td>" + rs1.getString("from_date") + "</td>"
                            + "<td>" + rs1.getString("to_date") + "</td>"
                            + "<td>" + rs1.getString("duration") + " days</td>"
                            + "<td>" + rs1.getString("venue_name") + "</td>"
                            + "</tr>";

                    result += "</table>";
                }
                result += "</div>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public int deleteDocumentAttachment(int trainingId, String filepath) {

        //Session session = null;
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        int retval2 = 0;
        PreparedStatement pst = null;

        boolean isDeleted = false;
        try {
            //session = this.sessionFactory.openSession();
            //Transaction transaction = session.beginTransaction();
            //hw = (HWAttachements) session.get(HWAttachements.class, attid);
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT disk_file_name FROM g_training_programs WHERE training_program_code =" + trainingId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String dirpath = filepath + "/" + rs.getString("disk_file_name");
                File f = new File(dirpath);
                if (f.exists()) {
                    isDeleted = f.delete();
                }
            }
            if (isDeleted) {
                pst = con.prepareStatement("UPDATE g_training_programs SET original_file_name = ?, content_type = ?, disk_file_name = ? WHERE training_program_code = ?");
                pst.setString(1, "");
                pst.setString(2, "");
                pst.setString(3, "");
                pst.setInt(4, trainingId);
                retval2 = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //session.flush();
            //session.close();
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retval2;
    }

    @Override
    public String getLeftUpcomingPrograms() {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT program_name, training_program_code, institution_name FROM g_training_programs TP INNER JOIN g_institutions I ON CAST (TP.owner_id AS INTEGER) = I.institution_code WHERE from_date > now() LIMIT 5");
            rs = ps.executeQuery();
            while (rs.next()) {
                result += "<a href='http://par.hrmsodisha.gov.in/' style='color:#296793;text-decoration:none;line-height:20px;'>" + rs.getString("program_name") + "</a><br />";

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public InstituteForm getInstituteDetail(String instituteId) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        InstituteForm inf = new InstituteForm();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT institution_code, institution_name, GI.location, GI.website"
                    + ", GI.email, GI.phone, GI.contact_person FROM user_details"
                    + " UD INNER JOIN g_institutions GI ON UD.linkid = GI.institution_code::text"
                    + " WHERE UD.linkid = '" + instituteId + "'";
            ps2 = con.prepareStatement(sql);
            rs = ps2.executeQuery();
            if (rs.next()) {
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                inf.setWebsite(rs.getString("website"));
                inf.setEmail(rs.getString("email"));
                inf.setPhone(rs.getString("phone"));
                inf.setContactPerson(rs.getString("contact_person"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return inf;
    }

    public int getTotalRowsCount(String tableName, String where) {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows FROM " + tableName + where);
            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total_rows");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    @Override
    public void updateInstituteProfile(InstituteForm instForm, String empID) {
        Connection con = null;
        PreparedStatement ps = null;
        String InstituteName = null;
        String location = null;
        String website = null;
        String email = null;
        String phone = null;
        String contactPerson = null;
        try {
            con = this.dataSource.getConnection();
            InstituteName = instForm.getInstituteName();
            location = instForm.getLocation();
            website = instForm.getWebsite();
            email = instForm.getEmail();
            phone = instForm.getPhone();
            contactPerson = instForm.getContactPerson();
            String str = "UPDATE g_institutions SET institution_name = ? ,location = ?,website = ?"
                    + ", email = ?, phone = ?, contact_person = ? WHERE institution_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, InstituteName);
            ps.setString(2, location);
            ps.setString(3, website);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, contactPerson);

            ps.setInt(7, instForm.getInstituteId());
            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getApplyProgramList(String empId, int page, int rows, String type) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            String cadreCode = getCadreCode(empId);
            if (type.equals("apply")) {
                rs = st.executeQuery("SELECT training_program_code, program_name, GI.institution_name, to_char(from_date"
                        + ", 'DD-Mon-YYYY') as from_date, apply_end_date"
                        + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, capacity, (SELECT status_name FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS status_name, (SELECT training_option FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS training_option, (SELECT emp_training_id FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS emp_training_id"
                        + ", (SELECT is_selected FROM hw_emp_training WHERE apply_emp_id = '" + empId + "' AND training_id = GTP.training_program_code) as isSelected"
                        + ", (SELECT is_shortlisted FROM hw_emp_training WHERE apply_emp_id = '" + empId + "' AND training_id = GTP.training_program_code) as isShortlisted"
                        + ", DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration"
                        + ", venue_name as venue, disk_file_name"
                        + " FROM g_training_programs GTP  "
                        + " INNER JOIN g_institutions GI ON GI.institution_code = cast(GTP.owner_id AS integer)"
                        + " INNER JOIN g_training_venues TV ON TV.venue_code = GTP.venue_id WHERE (SELECT status_name FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') IS NULL AND from_date >= current_date AND is_archived = 'N' AND is_published = 'Y' ORDER BY from_date limit "
                        + rows + " offset " + firstpage);
            } else {
                rs = st.executeQuery("SELECT training_program_code, program_name, GI.institution_name, to_char(from_date"
                        + ", 'DD-Mon-YYYY') as from_date, apply_end_date"
                        + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, capacity, (SELECT status_name FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS status_name, (SELECT training_option FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS training_option, (SELECT emp_training_id FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') AS emp_training_id"
                        + ", (SELECT is_selected FROM hw_emp_training WHERE apply_emp_id = '" + empId + "' AND training_id = GTP.training_program_code) as isSelected"
                        + ", (SELECT is_shortlisted FROM hw_emp_training WHERE apply_emp_id = '" + empId + "' AND training_id = GTP.training_program_code) as isShortlisted"
                        + ", DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration"
                        + ", venue_name as venue, disk_file_name"
                        + " FROM g_training_programs GTP  "
                        + " INNER JOIN g_institutions GI ON GI.institution_code = cast(GTP.owner_id AS integer)"
                        + " INNER JOIN g_training_venues TV ON TV.venue_code = GTP.venue_id WHERE (SELECT status_name FROM hw_emp_training ET "
                        + "INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer) "
                        + "WHERE training_id = GTP.training_program_code AND apply_emp_id = '" + empId + "') IS NOT NULL AND is_archived = 'N' AND is_published = 'Y' ORDER BY from_date limit "
                        + rows + " offset " + firstpage);
            }
            TrainingProgramList tpl = null;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            String applyEndDate = null;
            String curDate = null;
            String isSelected = null;
            String isShortlisted = null;

            boolean isExpired = false;
            while (rs.next()) {
                boolean shouldDisplay = true;
                String where = " WHERE training_program_code = " + Integer.parseInt(rs.getString("training_program_code"));
                int numCadres = getTotalRowsCount("g_program_cadres", where);
                if (numCadres != 0) {
                    where = " WHERE training_program_code = " + Integer.parseInt(rs.getString("training_program_code")) + " AND cadre_code = '" + cadreCode + "'";
                    int numRows = getTotalRowsCount("g_program_cadres", where);
                    if (numRows <= 0) {
                        shouldDisplay = false;
                    }
                }
                if (numCadres == 0 || shouldDisplay) {
                    /*String trOptions = "";
                     String strTrainingOptions = getEmpTrainingOptions(empId, Integer.parseInt(rs.getString("training_program_code")));
                     String[] arrOptions = strTrainingOptions.split(",");
                     trOptions = "<select name=\"trainingOption_" + rs.getString("training_program_code") + "_" + empId + "\" id=\"trainingOption_" + rs.getString("training_program_code") + "_" + empId + "\" size=\"1\" onchange=\"javascript: updateTrainingOption('" + rs.getString("training_program_code") + "', '" + empId + "', this.value)\">"
                     + "<option value=\"\">-Select-</option>";
                     if (strTrainingOptions.indexOf("1") == -1) {
                     trOptions += "<option value='1'>Option 1</option>";
                     }
                     if (strTrainingOptions.indexOf("2") == -1) {
                     trOptions += "<option value='2'>Option 2</option>";
                     }
                     if (strTrainingOptions.indexOf("3") == -1) {
                     trOptions += "<option value='3'>Option 3</option>";
                     }
                     trOptions += "</select>";*/
                    isSelected = rs.getString("isSelected");
                    isShortlisted = rs.getString("isShortlisted");

                    if (isSelected != null && isSelected.equalsIgnoreCase("Y")) {
                        isSelected = "<span style='color:#008900;font-weight:bold;'>YES</span>";
                    } else {
                        isSelected = "";
                    }
                    if (isShortlisted != null && isShortlisted.equalsIgnoreCase("Y")) {
                        isShortlisted = "<span style='color:#008900;font-weight:bold;'>YES</span>";
                    } else {
                        isShortlisted = "";
                    }
                    tpl = new TrainingProgramList();
                    tpl.setIsSelected(isSelected);
                    tpl.setIsShortlisted(isShortlisted);

                    tpl.setTrainingProgramID(rs.getString("training_program_code"));
                    tpl.setProgramName(rs.getString("program_name"));
                    tpl.setFromDate(rs.getString("from_date"));
                    tpl.setToDate(rs.getString("to_date"));
                    tpl.setDuration(rs.getString("duration") + " Days");
                    tpl.setCapacity(rs.getInt("capacity"));
                    tpl.setStrTrainingOption("Option " + rs.getInt("training_option"));
                    applyEndDate = rs.getString("apply_end_date");

                    isExpired = false;
                    if (applyEndDate != null && !applyEndDate.equals("")) {
                        Date cdate = new Date();
                        curDate = dFormat.format(cdate);

                        Date date1 = dFormat.parse(curDate);
                        Date date2 = dFormat.parse(applyEndDate);

                        if (date1.compareTo(date2) > 0) {
                            isExpired = true;
                        }
                    }
                    tpl.setIsExpired(isExpired);
                    /* if (rs.getInt("training_option") != 0) {
                     tpl.setStrTrainingOption("Option " + rs.getInt("training_option") + "");
                     } else {
                     tpl.setStrTrainingOption(trOptions);
                     }*/
                    tpl.setVenue("<strong>" + rs.getString("institution_name") + "</strong><br />" + rs.getString("venue"));
                    if (rs.getString("status_name") != null) {

                        if (rs.getString("status_name").equals("PENDING")) {
                            //String empname = getEmpTrainingDetails();
                        }
                        tpl.setStatus(rs.getString("status_name"));
                    } else {
                        tpl.setStatus("");
                    }
                    if (rs.getString("disk_file_name") != null) {
                        tpl.setDiskFileName(rs.getString("disk_file_name"));
                    } else {
                        tpl.setDiskFileName("");
                    }
                    li.add(tpl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public Message applyTraining(String empid, String trid, String spc, String mobile, String email, String tpids, String trainingOption) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;

        String[] spcArr = spc.split("\\|");
        String pendingAt = spcArr[1];
        String applyTo = spcArr[1];
        String pendingSpc = spcArr[0];
        String initiatedSpc = "";
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT cur_spc FROM emp_mast WHERE emp_id = '" + empid + "'");
            if (rs.next()) {
                initiatedSpc = rs.getString("cur_spc");
            }

            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, "
                    + "STATUS_ID, PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC, apply_to_spc) Values (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, 7);
            pst.setString(2, empid);
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(4, 30);//SUBMITTED TO REPORTING AUTHORITY
            pst.setString(5, pendingAt);
            pst.setString(6, applyTo);
            pst.setString(7, initiatedSpc);
            pst.setString(8, pendingSpc);
            pst.setString(9, pendingSpc);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            pst1 = con.prepareStatement("INSERT INTO hw_emp_training(training_id, apply_emp_id, apply_spc, task_id, "
                    + "applied_to, applied_to_spc,status, training_option) Values (?,?,?,?,?,?,?,?)");
            int trainingId = Integer.parseInt(trid);
            pst1.setInt(1, trainingId);
            pst1.setString(2, empid);
            pst1.setString(3, initiatedSpc);
            pst1.setInt(4, taskId);
            pst1.setString(5, applyTo);
            pst1.setString(6, pendingSpc);
            pst1.setString(7, 30 + "");
            pst1.setInt(8, Integer.parseInt(trainingOption));
            pst1.executeUpdate();
            st2 = con.createStatement();
            pst2 = con.prepareStatement("UPDATE emp_mast SET email_id = ?, mobile = ? WHERE emp_id = ?");
            pst2.setString(1, email);
            pst2.setString(2, mobile);
            pst2.setString(3, empid);
            pst2.executeUpdate();
            /*if (tpids != null && !tpids.equals("")) {
             if (tpids.equals("0")) {
             pst3 = con.prepareStatement("INSERT INTO g_previous_training(training_id, emp_id) VALUES(?,?)");
             pst3.setInt(1, 0);
             pst3.setString(2, empid);
             pst3.executeUpdate();
             } else {
             pst3 = con.prepareStatement("INSERT INTO g_previous_training(training_id, emp_id) VALUES(?,?)");

             String[] idsplit = tpids.split(",");
             for (int i = 0; i < idsplit.length; i++) {
             if (idsplit[i].equals("0")) {

             } else {

             pst3.setInt(1, Integer.parseInt(idsplit[i]));
             pst3.setString(2, empid);
             pst3.executeUpdate();
             }
             }
             }
             }*/

        } catch (Exception e) {
            msg.setStatus("Info");
            msg.setMessage("Error");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public TrainingProgramForm getApplyTrainingDetail(int taskId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        Statement st3 = null;
        ResultSet rs3 = null;
        int trainingId = 0;
        String instituionName = null;
        String sponsors = null;
        String faculties = null;
        String empName = null;
        String post = null;
        TrainingProgramForm tpl = null;
        try {
            con = this.dataSource.getConnection();
            st1 = con.createStatement();
            rs1 = st1.executeQuery(" SELECT training_id, institution_name,cur_spc,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') emp_name,post FROM hw_emp_training ET "
                    + "                   INNER JOIN g_training_programs GP ON GP.training_program_code = ET.training_id "
                    + "                    INNER JOIN g_institutions I ON CAST(GP.owner_id AS integer) = I.institution_code"
                    + "                    inner join emp_mast emp on et.apply_emp_id=emp.emp_id"
                    + "                    left outer join g_spc gspc on emp.cur_spc=gspc.spc"
                    + "                    left outer join g_post gpost on gspc.gpc=gpost.post_code"
                    + "                    WHERE task_id = " + taskId);
            while (rs1.next()) {
                trainingId = rs1.getInt("training_id");
                instituionName = rs1.getString("institution_name");
                empName = rs1.getString("emp_name");
                post = rs1.getString("post");
            }
            st = con.createStatement();

            rs = st.executeQuery("SELECT training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, venue_name, disk_file_name"
                    + " FROM g_training_programs GTP INNER JOIN g_training_venues GV "
                    + " ON GV.venue_code = GTP.venue_id WHERE GTP.training_program_code = " + trainingId);

            while (rs.next()) {
                sponsors = faculties = "";
                st2 = con.createStatement();
                rs2 = st2.executeQuery("SELECT sponsor_name FROM g_program_sponsors PS INNER JOIN g_training_sponsors TS ON TS.sponsor_code = PS.sponsor_code WHERE training_program_code = " + rs.getInt("training_program_code"));
                while (rs2.next()) {
                    if (sponsors.equals("")) {
                        sponsors = rs2.getString("sponsor_name");
                    } else {
                        sponsors += ", " + rs2.getString("sponsor_name");
                    }
                }
                st3 = con.createStatement();
                rs3 = st3.executeQuery("SELECT faculty_name FROM g_program_faculties PF "
                        + "INNER JOIN g_training_faculties TF ON TF.faculty_code = PF.faculty_code "
                        + "WHERE training_program_code = " + rs.getInt("training_program_code"));
                while (rs3.next()) {
                    if (faculties.equals("")) {
                        faculties = rs3.getString("faculty_name");
                    } else {
                        faculties += ", " + rs3.getString("faculty_name");
                    }
                }
                tpl = new TrainingProgramForm();
                tpl.setTrainingProgram(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setInstituteName(instituionName);
                tpl.setSponsorName(sponsors);
                tpl.setFacultyName(faculties);
                tpl.setDuration(rs.getString("duration"));
                tpl.setVenueName(rs.getString("venue_name"));
                tpl.setEmpName(empName);
                tpl.setDesignation(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs1, st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tpl;
    }

    @Override
    public Message saveTrainingApprove(int taskId, int trainingStatus, String loginempid, String loginSpc, String forwardEmpid) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        String pendingAt = null;
        String pendingSpc = null;

        Message msg = new Message();
        msg.setStatus("Success");

        int logid = 0;

        try {
            con = this.dataSource.getConnection();
            /*pst2 = con.prepareStatement("SELECT pending_at, pending_spc FROM task_master WHERE task_id = ?");
             pst2.setInt(1, taskId);
             rs2 = pst2.executeQuery();
             while (rs2.next()) {
             pendingAt = rs2.getString("pending_at");
             pendingSpc = rs2.getString("pending_spc");
             }*/
            String[] forwardEmp = forwardEmpid.split("\\|");
            if (trainingStatus == 33) {
                pendingAt = forwardEmp[1];
                pendingSpc = forwardEmp[0];
            }
            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
            pst.setString(1, pendingAt);
            pst.setString(2, pendingSpc);
            pst.setInt(3, trainingStatus);
            pst.setInt(4, taskId);
            pst.executeUpdate();

            pst1 = con.prepareStatement("UPDATE hw_emp_training SET STATUS=?, applied_to = ?"
                    + ", applied_to_spc = ?, approved_by = ?, approved_by_spc = ?"
                    + ", approved_on = ? WHERE TASK_ID=?");
            pst1.setString(1, trainingStatus + "");
            pst1.setString(2, pendingAt);
            pst1.setString(3, pendingSpc);
            pst1.setString(4, loginempid);
            pst1.setString(5, loginSpc);
            pst1.setTimestamp(6, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst1.setInt(7, taskId);
            pst1.executeUpdate();

            if (trainingStatus == 33) {
                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(LOG_ID,REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                logid = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
                pst.setInt(1, logid);
                pst.setInt(2, 0);
                pst.setString(3, loginempid);
                pst.setString(4, pendingSpc);
                pst.setString(5, pendingAt);
                pst.setString(6, "");
                pst.setString(7, "");
                pst.setTimestamp(8, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(9, taskId);
                pst.setInt(10, trainingStatus);
                pst.setString(11, "TRAINING_FORWARD");
                pst.setString(12, "");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public List getTrainingStatus() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List trainingstatuslist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT status_id,status_name from g_process_status where process_id=7 AND status_id <> 30  AND status_id <> 34 order by status_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("status_id"));
                so.setLabel(rs.getString("status_name"));
                trainingstatuslist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingstatuslist;
    }

    @Override
    public List getOnlineTrainingStatus() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List trainingstatuslist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT status_id,status_name from g_process_status where process_id = 24 order by status_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("status_id"));
                so.setLabel(rs.getString("status_name"));
                trainingstatuslist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingstatuslist;
    }

    public List getParticipantList(String empId, String trainingId, int page, int rows) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT apply_emp_id, apply_spc, status, status_name"
                    + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, spn "
                    + " FROM hw_emp_training ET"
                    + " INNER JOIN g_training_programs GTP ON ET.training_id = GTP.training_program_code"
                    + " INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer)"
                    + " INNER JOIN emp_mast AS EM ON EM.emp_id = ET.apply_emp_id"
                    + " LEFT OUTER JOIN g_spc AS GSPC ON GSPC.spc = apply_spc"
                    + " WHERE training_id = ? AND owner_id = ? AND status = '34'");
            ps.setInt(1, Integer.parseInt(trainingId));
            ps.setString(2, empId);
            rs = ps.executeQuery();
            ParticipantBean pb = null;
            while (rs.next()) {
                pb = new ParticipantBean();
                pb.setParticipantName(rs.getString("EMPNAME"));
                pb.setDesignation(rs.getString("spn"));
                pb.setStatus(rs.getString("status_name"));
                li.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int getDefaultVenue(String ownerId) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int venueId = 0;
        try {
            SelectOption so = null;
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT venue_code FROM G_TRAINING_VENUES WHERE owner_id = '" + ownerId + "' LIMIT 1";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                venueId = rs.getInt("venue_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return venueId;
    }

    @Override
    public String getCadreCode(String empId) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String cadreCode = "";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT cur_cadre_code FROM emp_mast WHERE emp_id = '" + empId + "' LIMIT 1";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cadreCode = rs.getString("cur_cadre_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreCode;
    }

    //@Override
    public String getGrade(String empId) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String grade = "";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT cur_cadre_grade FROM emp_mast WHERE emp_id = '" + empId + "' LIMIT 1";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                grade = rs.getString("cur_cadre_grade");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grade;
    }

    public List getManageTrainingProgramList(String ownerID, int page, int rows) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            rs = st.executeQuery("SELECT institution_name, training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, capacity,"
                    + " (SELECT COUNT(*) FROM hw_emp_training WHERE training_id = GTP.training_program_code) AS num_applicants"
                    + " FROM g_training_programs GTP "
                    + "INNER JOIN g_institutions I ON CAST (GTP.owner_id AS INTEGER) = I.institution_code "
                    + "WHERE (training_authority = '" + ownerID + "' OR (SELECT COUNT(*) FROM g_training_authorities WHERE privileged_authroity = '" + ownerID + "' AND authority_id = GTP.training_authority) >0) AND is_archived = 'N' AND is_published = 'Y' ORDER BY from_date  limit "
                    + rows + " offset " + firstpage);
            TrainingProgramList tpl = null;
            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setDuration(rs.getString("duration") + " Days");
                tpl.setCapacity(rs.getInt("capacity"));
                tpl.setInstituteName(rs.getString("institution_name"));
                tpl.setNumApplicants(rs.getInt("num_applicants"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public TrainingProgramForm getManageTrainingDetail(int trainingId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        Statement st3 = null;
        ResultSet rs3 = null;
        String instituionName = null;
        String sponsors = null;
        String faculties = null;
        TrainingProgramForm tpl = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT capacity, institution_name, training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, venue_name"
                    + ", disk_file_name, (SELECT COUNT(*) FROM hw_emp_training WHERE training_id = GTP.training_program_code) AS num_applicants"
                    + " FROM g_training_programs GTP "
                    + " INNER JOIN g_institutions I ON CAST(GTP.owner_id AS integer) = I.institution_code "
                    + " INNER JOIN g_training_venues GV "
                    + " ON GV.venue_code = GTP.venue_id WHERE GTP.training_program_code = " + trainingId);

            while (rs.next()) {
                sponsors = faculties = "";
                int numShortlisted = 0;
                st1 = con.createStatement();
                rs1 = st1.executeQuery("SELECT COUNT(*) AS num_shortlisted FROM hw_emp_training "
                        + "WHERE status = '34' AND training_id = " + rs.getInt("training_program_code"));
                while (rs1.next()) {
                    numShortlisted = rs1.getInt("num_shortlisted");
                }
                st2 = con.createStatement();
                rs2 = st2.executeQuery("SELECT sponsor_name FROM g_program_sponsors PS INNER JOIN g_training_sponsors TS ON TS.sponsor_code = PS.sponsor_code WHERE training_program_code = " + rs.getInt("training_program_code"));
                while (rs2.next()) {
                    if (sponsors.equals("")) {
                        sponsors = rs2.getString("sponsor_name");
                    } else {
                        sponsors += ", " + rs2.getString("sponsor_name");
                    }
                }
                st3 = con.createStatement();
                rs3 = st3.executeQuery("SELECT faculty_name FROM g_program_faculties PF "
                        + "INNER JOIN g_training_faculties TF ON TF.faculty_code = PF.faculty_code "
                        + "WHERE training_program_code = " + rs.getInt("training_program_code"));
                while (rs3.next()) {
                    if (faculties.equals("")) {
                        faculties = rs3.getString("faculty_name");
                    } else {
                        faculties += ", " + rs3.getString("faculty_name");
                    }
                }
                tpl = new TrainingProgramForm();
                tpl.setTrainingProgram(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setInstituteName(rs.getString("institution_name"));
                tpl.setSponsorName(sponsors);
                tpl.setFacultyName(faculties);
                tpl.setDuration(rs.getString("duration"));
                tpl.setVenueName(rs.getString("venue_name"));
                tpl.setNumApplied(rs.getInt("num_applicants"));
                tpl.setCapacity(rs.getInt("capacity"));
                tpl.setNumShortlisted(numShortlisted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(rs2, st2);
            DataBaseFunctions.closeSqlObjects(rs3, st3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tpl;
    }

    @Override
    public List getAppliedParticipantList(String empId, String trainingId, int rows, int page) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            ps = con.prepareStatement("SELECT emp_training_id, apply_emp_id, apply_spc, status, status_name"
                    + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, spn"
                    + " FROM hw_emp_training ET"
                    + " INNER JOIN g_training_programs GTP ON ET.training_id = GTP.training_program_code"
                    + " INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer)"
                    + " INNER JOIN emp_mast AS EM ON EM.emp_id = ET.apply_emp_id"
                    + " LEFT OUTER JOIN g_spc AS GSPC ON GSPC.spc = apply_spc"
                    + " WHERE training_id = ? AND (training_authority = ? OR (SELECT COUNT(*) FROM g_training_authorities WHERE privileged_authroity = ? AND authority_id = GTP.training_authority) >0) ORDER BY (case when status_id = 34 then 1 "
                    + "when status_id = 31 then 2 when status_id = 33 then 3 when status_id = 30 then 4 "
                    + "when status_id = 32 then 5 end), approved_on limit "
                    + rows + " offset " + firstpage);
            ps.setInt(1, Integer.parseInt(trainingId));
            ps.setString(2, empId);
            ps.setString(3, empId);
            rs = ps.executeQuery();
            ParticipantBean pb = null;
            while (rs.next()) {
                pb = new ParticipantBean();
                pb.setParticipantName("<strong>" + rs.getString("EMPNAME") + "</strong><br />" + rs.getString("spn"));
                pb.setStatus(rs.getString("status_name"));
                pb.setParticipantId(rs.getString("emp_training_id"));
                li.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveShortlisted(int empTrainingId) {
        Connection con = null;
        PreparedStatement ps = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {

            con = this.dataSource.getConnection();
            String str = "UPDATE hw_emp_training SET status = 34, shortlisted_on = ? WHERE emp_training_id = ?";
            ps = con.prepareStatement(str);
            ps.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            ps.setInt(2, empTrainingId);

            int status = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, String trainingId, String trainingAuth) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 1;
        try {
            con = this.dataSource.getConnection();

            String fileName = "TRAINING.xls";
            OutputStream out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("TRAINING", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            //headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);

            //WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            Label label = new Label(0, row, "Participant Name", headcell);//column,row
            sheet.addCell(label);

            String sql = "SELECT emp_training_id, apply_emp_id, apply_spc, status, status_name"
                    + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, spn"
                    + " FROM hw_emp_training ET"
                    + " INNER JOIN g_training_programs GTP ON ET.training_id = GTP.training_program_code"
                    + " INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer)"
                    + " INNER JOIN emp_mast AS EM ON EM.emp_id = ET.apply_emp_id"
                    + " LEFT OUTER JOIN g_spc AS GSPC ON GSPC.spc = apply_spc"
                    + " WHERE training_id = ?  ORDER BY (case when status_id = 34 then 1 when status_id = 31 then 2 when status_id = 33 then 3 when status_id = 30 then 4 when status_id = 32 then 5 end), approved_on";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(trainingId));
            //pst.setString(2, trainingAuth);
            rs = pst.executeQuery();

            row += 1;

            while (rs.next()) {
                label = new Label(0, row, rs.getString("EMPNAME") + "\n" + rs.getString("spn"), innercell);//column,row
                sheet.addCell(label);

                row++;
            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List getArchiveTrainingProgramList(String ownerId, int page, int rows) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            rs = st.executeQuery("SELECT training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, venue_id as venue, disk_file_name"
                    + ", (SELECT faculty_name FROM g_training_faculties GTF"
                    + " INNER JOIN g_program_faculties GPF ON GTF.faculty_code = GPF.faculty_code "
                    + " WHERE GPF.training_program_code = GTP.training_program_code LIMIT 1) AS facultyName"
                    + ", (SELECT sponsor_name FROM g_training_sponsors GTF"
                    + " INNER JOIN g_program_sponsors GPF ON GTF.sponsor_code = GPF.sponsor_code "
                    + " WHERE GPF.training_program_code = GTP.training_program_code LIMIT 1) AS sponsorName, capacity"
                    + " FROM g_training_programs GTP WHERE owner_id = '" + ownerId + "' AND to_date <= (current_date - interval '90' day) ORDER BY from_date  limit "
                    + rows + " offset " + firstpage);

            TrainingProgramList tpl = null;
            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setDuration(rs.getString("duration") + " Days");
                tpl.setVenue(rs.getString("venue"));
                tpl.setFacultyName(rs.getString("facultyName"));
                tpl.setSponsorName(rs.getString("sponsorName"));
                tpl.setCapacity(rs.getInt("capacity"));
                if (rs.getString("disk_file_name") != null) {
                    tpl.setDiskFileName(rs.getString("disk_file_name"));
                } else {
                    tpl.setDiskFileName("");
                }
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String getEmpMobile(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        String mobile = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT mobile FROM emp_mast WHERE emp_id = '" + empId + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                mobile = rs.getString("mobile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mobile;
    }

    public String getEmpEmail(String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        String email = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT email_id FROM emp_mast WHERE emp_id = '" + empId + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("email_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return email;
    }

    @Override
    public List getPreviousTrainingList(String empId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String cadreCode = getCadreCode(empId);
            int previousMonth = 0;
            int previousYear = 0;
            rs = st.executeQuery("SELECT training_program_code, program_name, GI.institution_name, extract(year FROM from_date) AS previous_year, extract(month FROM from_date) AS previous_month"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date"
                    + ", DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration"
                    + ", venue_name as venue, disk_file_name"
                    + " FROM g_training_programs GTP  "
                    + " INNER JOIN g_institutions GI ON GI.institution_code = cast(GTP.owner_id AS integer)"
                    + " INNER JOIN g_training_venues TV ON TV.venue_code = GTP.venue_id WHERE is_archived = 'Y' ORDER BY from_date DESC");
            TrainingProgramList tpl = null;
            while (rs.next()) {
                boolean shouldDisplay = true;
                String where = " WHERE training_program_code = " + Integer.parseInt(rs.getString("training_program_code"));
                int numCadres = getTotalRowsCount("g_program_cadres", where);
                if (numCadres != 0) {
                    where = " WHERE training_program_code = " + Integer.parseInt(rs.getString("training_program_code")) + " AND cadre_code = '" + cadreCode + "'";
                    int numRows = getTotalRowsCount("g_program_cadres", where);
                    if (numRows <= 0) {
                        shouldDisplay = false;
                    }
                }
                if (numCadres == 0 || shouldDisplay) {

                    String financialYear = "";
                    previousMonth = rs.getInt("previous_month");
                    previousYear = rs.getInt("previous_year");
                    if (previousMonth > 2) {
                        financialYear = previousYear + "-" + (previousYear + 1);
                    } else {
                        financialYear = (previousYear - 1) + "-" + previousYear;
                    }
                    tpl = new TrainingProgramList();
                    tpl.setTrainingProgramID(rs.getString("training_program_code"));
                    tpl.setProgramName(rs.getString("program_name"));
                    tpl.setFromDate(financialYear);
                    tpl.setToDate(rs.getString("to_date"));
                    tpl.setDuration(rs.getString("duration") + " Days");
                    tpl.setInstituteName(rs.getString("institution_name"));
                    tpl.setVenue("<strong>" + rs.getString("institution_name") + "</strong>-" + rs.getString("venue"));
                    li.add(tpl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public boolean isTrainingExist(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isExist = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT COUNT(*) CNT FROM G_PREVIOUS_TRAINING WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("CNT") > 0) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isExist;
    }

    @Override
    public String getEmpTrainingOptions(String empId, int trainingId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String trainingOptions = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT training_option FROM hw_emp_training WHERE apply_emp_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (trainingOptions == "") {
                    trainingOptions = rs.getString("training_option");
                } else {
                    trainingOptions += "," + rs.getString("training_option");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingOptions;
    }

    @Override
    public void updateTrainingOption(int trainingId, String empId, String trainingOption) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE hw_emp_training SET training_option = ? WHERE apply_emp_id = ? AND training_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(trainingOption));
            pst.setString(2, empId);
            pst.setInt(3, trainingId);
            rs = pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean hasEmpPreviousTraining(String empId, int trainingId) {
        Connection con = null;
        boolean isExist = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT COUNT(*) AS CNT FROM g_previous_training WHERE emp_id = ? AND training_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, trainingId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("CNT") > 0) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isExist;
    }

    public Message savePreviousTraining(String empId, String tpids) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement pst3 = null;
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = this.dataSource.getConnection();
            if (tpids != null && !tpids.equals("")) {
                pst3 = con.prepareStatement("DELETE FROM g_previous_training WHERE emp_id =?");
                pst3.setString(1, empId);
                pst3.executeUpdate();

                pst3 = con.prepareStatement("INSERT INTO g_previous_training(training_id, emp_id) VALUES(?,?)");

                String[] idsplit = tpids.split(",");
                for (int i = 0; i < idsplit.length; i++) {
                    if (idsplit[i].equals("0")) {

                    } else {

                        pst3.setInt(1, Integer.parseInt(idsplit[i]));
                        pst3.setString(2, empId);
                        pst3.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            msg.setStatus("Info");
            msg.setMessage("Error");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    private String getEmpTrainingDetails(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String empname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT FULL_NAME,POST FROM (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,CUR_SPC FROM EMP_MAST WHERE EMP_ID=?)EMP_MAST"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                    empname = rs.getString("FULL_NAME") + ", " + rs.getString("POST");
                } else {
                    empname = rs.getString("FULL_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    @Override
    public Message withdrawTraining(int trainingId, String empId) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement pst3 = null;
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = this.dataSource.getConnection();
            int taskId = 0;
            int empTrainingId = 0;
            String sql = "SELECT task_id, emp_training_id FROM hw_emp_training WHERE training_id =? AND apply_emp_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, trainingId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("task_id");
                empTrainingId = rs.getInt("emp_training_id");
                pst3 = con.prepareStatement("DELETE FROM task_master WHERE task_id =?");
                pst3.setInt(1, taskId);
                pst3.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst3);
                pst3 = con.prepareStatement("DELETE FROM workflow_log WHERE task_id =?");
                pst3.setInt(1, taskId);
                pst3.executeUpdate();
                DataBaseFunctions.closeSqlObjects(pst3);
                pst3 = con.prepareStatement("DELETE FROM hw_emp_training WHERE emp_training_id =?");
                pst3.setInt(1, empTrainingId);
                pst3.executeUpdate();

            }

        } catch (Exception e) {
            msg.setStatus("Info");
            msg.setMessage("Error");
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public List getTrainingDeptList() {

        Connection con = null;

        List deptlist = new ArrayList();

        PreparedStatement pst = null;
        ResultSet rs = null;

        Department department = null;
        try {
            con = this.dataSource.getConnection();

            department = new Department();
            department.setDeptCode("LM");
            department.setDeptName("LEGISLATIVE MEMBERS");
            deptlist.add(department);

            String sql = "SELECT department_code,department_name from g_department where if_active='Y' order by department_name";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                department = new Department();
                department.setDeptCode(rs.getString("department_code"));
                department.setDeptName(rs.getString("department_name"));
                deptlist.add(department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptlist;
    }

    @Override
    public List getTrainingOfficeList(String deptcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Office off = null;
        List officelist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            if (deptcode != null && !deptcode.equals("")) {
                if (deptcode.equals("LM")) {
                    String sql = "SELECT * FROM G_OFFICIATING ORDER BY OFF_NAME";
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        off = new Office();
                        off.setOffCode(rs.getString("OFF_ID"));
                        off.setOffName(rs.getString("OFF_NAME"));
                        officelist.add(off);
                    }
                } else {
                    String sql = "SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE DEPARTMENT_CODE=? ORDER BY OFF_EN ASC";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, deptcode);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        off = new Office();
                        off.setOffCode(rs.getString("OFF_CODE"));
                        off.setOffName(rs.getString("OFF_EN"));
                        officelist.add(off);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getPostListTraining(String deptcode, String offcode) {

        List postlist = new ArrayList();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            if (deptcode != null && !deptcode.equals("")) {
                if (deptcode.equalsIgnoreCase("LM")) {
                    String sql = "SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') EMPNAME,OFF_AS,RANK FROM LA_MEMBERS WHERE ACTIVE='Y' AND OFF_AS=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, offcode);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                            so = new SelectOption();
                            so.setLabel(getAssignedDepartment(rs.getString("LMID")));
                            so.setDesc(rs.getString("EMPNAME"));
                            String SpcHrmsId = rs.getString("LMID") + "|" + rs.getString("LMID");
                            so.setValue(SpcHrmsId);
                            postlist.add(so);
                        }
                    }
                } else {
                    pst = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id from ("
                            + "select spc,gpc from g_spc where off_code='" + offcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                            + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                            + "left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST");
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                            so = new SelectOption();
                            so.setLabel(rs.getString("post"));
                            so.setDesc(rs.getString("EMPNAME"));
                            String SpcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id");
                            so.setValue(SpcHrmsId);
                            postlist.add(so);
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
        return postlist;
    }

    public String getAssignedDepartment(String lmid) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String desc = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE LMID=? ORDER BY DEPARTMENT_NAME";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(lmid));
            rs = pst.executeQuery();
            while (rs.next()) {
                if (desc != null && !desc.equals("")) {
                    desc = desc + ", " + rs.getString("DEPARTMENT_NAME");
                } else {
                    desc = rs.getString("DEPARTMENT_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return desc;
    }

    @Override
    public List getSearchEmp(EmployeeSearch empSearch) {
        ArrayList empList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String fName = empSearch.getFirstName();
        String lName = empSearch.getLastName();
        String dob = empSearch.getDob();
        String mobile = empSearch.getMobile();
        EmployeeSearch eSearch = null;

        String where = "";
        if (fName != null && !fName.equals("")) {
            where += " AND f_name LIKE '%" + fName.toUpperCase() + "%'";
        }
        if (lName != null && !lName.equals("")) {
            where += " AND l_name LIKE '%" + lName.toUpperCase() + "%'";
        }
        if (dob != null && !dob.equals("")) {
            where += " AND dob = '" + dob + " 00:00:00'";
        }
        if (mobile != null && !mobile.equals("")) {
            where += " AND mobile = '" + mobile + "'";
        }
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select emp_id, gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME, to_char(dob, 'YYYY-MM-DD') AS dob, mobile, email_id, gpf_no, post from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code where 1 = 1 " + where;
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                eSearch = new EmployeeSearch();

                eSearch.setFullName(rs.getString("FULL_NAME"));
                eSearch.setMobile(rs.getString("mobile"));
                eSearch.setEmpId(rs.getString("emp_id"));
                eSearch.setGpfNo(rs.getString("gpf_no"));
                eSearch.setDob(rs.getString("dob"));
                eSearch.setPostName(rs.getString("post"));

                empList.add(eSearch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public void saveNISGTrainingDetails(NISGTrainingBean ntBean) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_nisg_participants(training_id,emp_id,full_name,designation,department,highest_qualification,projects_initiated_1,projects_initiated_2,projects_initiated_3,projects_associated_1,projects_associated_2,projects_associated_3,top_two_things_1,top_two_things_2,top_two_things_3,conceptualization_phase_1,conceptualization_phase_2,dev_phase_1,dev_phase_2,imp_phase_1,imp_phase_2,operation_phase_1,operation_phase_2,previous_training_1,previous_training_2,previous_training_3,skill_enhance_1,skill_enhance_2,skill_enhance_3) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setInt(1, ntBean.getTrainingId());
            pst.setString(2, ntBean.getEmpId());
            pst.setString(3, ntBean.getFullName());
            pst.setString(4, ntBean.getDesignation());
            pst.setString(5, ntBean.getDepartment());
            pst.setString(6, ntBean.getHighestQualification());
            pst.setString(7, ntBean.getProjectsInitiated1());
            pst.setString(8, ntBean.getProjectsInitiated2());
            pst.setString(9, ntBean.getProjectsInitiated3());
            pst.setString(10, ntBean.getProjectsAssociated1());
            pst.setString(11, ntBean.getProjectsAssociated2());
            pst.setString(12, ntBean.getProjectsAssociated3());
            pst.setString(13, ntBean.getTopTwoThings1());
            pst.setString(14, ntBean.getTopTwoThings2());
            pst.setString(15, ntBean.getTopTwoThings3());
            pst.setString(16, ntBean.getConceptualizationPhase1());
            pst.setString(17, ntBean.getConceptualizationPhase2());
            pst.setString(18, ntBean.getDevPhase1());
            pst.setString(19, ntBean.getDevPhase2());
            pst.setString(20, ntBean.getImpPhase1());
            pst.setString(21, ntBean.getImpPhase2());
            pst.setString(22, ntBean.getOperationPhase1());
            pst.setString(23, ntBean.getOperationPhase2());
            pst.setString(24, ntBean.getPreviousTraining1());
            pst.setString(25, ntBean.getPreviousTraining2());
            pst.setString(26, ntBean.getPreviousTraining3());
            pst.setString(27, ntBean.getSkillEnhance1());
            pst.setString(28, ntBean.getSkillEnhance2());
            pst.setString(29, ntBean.getSkillEnhance3());

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getTrainingCount(String empId) {
        int cnt = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String trainingIds = getActiveTraining();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS cnt FROM g_nisg_participants WHERE emp_id = ? AND training_id = " + trainingIds);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                cnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cnt;
    }

    @Override
    public List GetNISGParticipants() {
        ArrayList participantList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        NISGTrainingBean nisgBean = null;
        // Users emp = null;
        String trainingIds = getActiveTraining();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select * FROM g_nisg_participants WHERE status = 'Active' AND training_id = " + trainingIds;
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                nisgBean = new NISGTrainingBean();
                nisgBean.setEmpId(rs.getString("emp_id"));
                nisgBean.setParticipantId(rs.getInt("participant_id"));
                nisgBean.setHighestQualification(rs.getString("highest_qualification"));
                nisgBean.setProjectsInitiated1(rs.getString("projects_initiated_1"));
                nisgBean.setProjectsInitiated2(rs.getString("projects_initiated_2"));
                nisgBean.setProjectsInitiated3(rs.getString("projects_initiated_3"));
                nisgBean.setProjectsAssociated1(rs.getString("projects_associated_1"));
                nisgBean.setProjectsAssociated2(rs.getString("projects_associated_2"));
                nisgBean.setProjectsAssociated3(rs.getString("projects_associated_3"));
                nisgBean.setTopTwoThings1(rs.getString("top_two_things_1"));
                nisgBean.setTopTwoThings2(rs.getString("top_two_things_2"));
                nisgBean.setTopTwoThings3(rs.getString("top_two_things_3"));
                nisgBean.setConceptualizationPhase1(rs.getString("conceptualization_phase_1"));
                nisgBean.setConceptualizationPhase2(rs.getString("conceptualization_phase_2"));
                nisgBean.setDevPhase1(rs.getString("dev_phase_1"));
                nisgBean.setDevPhase2(rs.getString("dev_phase_2"));
                nisgBean.setImpPhase1(rs.getString("imp_phase_1"));
                nisgBean.setImpPhase2(rs.getString("imp_phase_2"));
                nisgBean.setOperationPhase1(rs.getString("operation_phase_1"));
                nisgBean.setOperationPhase2(rs.getString("operation_phase_2"));
                nisgBean.setPreviousTraining1(rs.getString("previous_training_1"));
                nisgBean.setPreviousTraining2(rs.getString("previous_training_2"));
                nisgBean.setPreviousTraining3(rs.getString("previous_training_3"));
                nisgBean.setSkillEnhance1(rs.getString("skill_enhance_1"));
                nisgBean.setSkillEnhance2(rs.getString("skill_enhance_2"));
                nisgBean.setSkillEnhance3(rs.getString("skill_enhance_3"));

                participantList.add(nisgBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return participantList;
    }

    @Override
    public NISGTrainingBean getNISGParticipantDetail(String participantID) {
        NISGTrainingBean nisgBean = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        nisgBean = new NISGTrainingBean();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,d.department_name,email_id,mobile,f.post, a.*  From  "
                    + " g_nisg_participants a, emp_mast b, g_office c, g_department d, g_spc e, g_post f"
                    + " where a.emp_id=b.emp_id and b.cur_off_code=c.off_code and c.department_code=d.department_code and b.cur_spc=e.spc"
                    + " and e.gpc=f.post_code and status = 'Active' AND participant_id = " + Integer.parseInt(participantID);
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                nisgBean = new NISGTrainingBean();
                nisgBean.setFullName(rs.getString("FULL_NAME"));
                nisgBean.setDepartment(rs.getString("department_name"));
                nisgBean.setDesignation(rs.getString("post"));
                nisgBean.setMobile(rs.getString("mobile"));
                nisgBean.setEmail(rs.getString("email_id"));
                nisgBean.setEmpId(rs.getString("emp_id"));
                nisgBean.setParticipantId(rs.getInt("participant_id"));
                nisgBean.setHighestQualification(rs.getString("highest_qualification"));
                nisgBean.setProjectsInitiated1(rs.getString("projects_initiated_1"));
                nisgBean.setProjectsInitiated2(rs.getString("projects_initiated_2"));
                nisgBean.setProjectsInitiated3(rs.getString("projects_initiated_3"));
                nisgBean.setProjectsAssociated1(rs.getString("projects_associated_1"));
                nisgBean.setProjectsAssociated2(rs.getString("projects_associated_2"));
                nisgBean.setProjectsAssociated3(rs.getString("projects_associated_3"));
                nisgBean.setTopTwoThings1(rs.getString("top_two_things_1"));
                nisgBean.setTopTwoThings2(rs.getString("top_two_things_2"));
                nisgBean.setTopTwoThings3(rs.getString("top_two_things_3"));
                nisgBean.setConceptualizationPhase1(rs.getString("conceptualization_phase_1"));
                nisgBean.setConceptualizationPhase2(rs.getString("conceptualization_phase_2"));
                nisgBean.setDevPhase1(rs.getString("dev_phase_1"));
                nisgBean.setDevPhase2(rs.getString("dev_phase_2"));
                nisgBean.setImpPhase1(rs.getString("imp_phase_1"));
                nisgBean.setImpPhase2(rs.getString("imp_phase_2"));
                nisgBean.setOperationPhase1(rs.getString("operation_phase_1"));
                nisgBean.setOperationPhase2(rs.getString("operation_phase_2"));
                nisgBean.setPreviousTraining1(rs.getString("previous_training_1"));
                nisgBean.setPreviousTraining2(rs.getString("previous_training_2"));
                nisgBean.setPreviousTraining3(rs.getString("previous_training_3"));
                nisgBean.setSkillEnhance1(rs.getString("skill_enhance_1"));
                nisgBean.setSkillEnhance2(rs.getString("skill_enhance_2"));
                nisgBean.setSkillEnhance3(rs.getString("skill_enhance_3"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nisgBean;
    }

    @Override
    public String getActiveTraining() {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String trainingIds = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT training_program_code FROM g_training_programs WHERE is_published = 'Y' LIMIT 1";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                trainingIds = "" + rs.getInt("training_program_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingIds;
    }

    @Override
    public List getOptionTrainingList() {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT training_program_code, program_name, from_date"
                    + ", DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration"
                    + ", (SELECT venue_name FROM g_training_venues WHERE venue_code = GTP.venue_id) AS venue_name"
                    + ", (SELECT institution_name FROM g_institutions WHERE institution_code = "
                    + " CAST (GTP.owner_id AS INTEGER)) AS institute_name FROM g_training_programs GTP ORDER BY program_name"
            );
            TrainingProgramList tpl = null;

            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setInstituteName(rs.getString("institute_name"));
                tpl.setDuration(rs.getString("duration") + " Days");
                tpl.setVenue(rs.getString("venue_name"));
                tpl.setFinancialYear(CommonFunctions.getFinancialYear(rs.getString("from_date")));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String saveEmpPreviousTraining(String empId, String trainingId) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        String msg = "success";
        try {
            con = this.dataSource.getConnection();
            pst1 = con.prepareStatement("DELETE FROM g_previous_training WHERE emp_id =? AND training_id = ?");
            pst1.setString(1, empId);
            pst1.setInt(2, Integer.parseInt(trainingId));
            pst1.executeUpdate();

            pst = con.prepareStatement("INSERT INTO g_previous_training(training_id, emp_id) VALUES(?,?)");

            pst.setInt(1, Integer.parseInt(trainingId));
            pst.setString(2, empId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public List GetApplyPreviousTrainingList(String empId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int previousMonth = 0;
        int previousYear = 0;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("select program_name, from_date, (SELECT institution_name FROM g_institutions WHERE institution_code ="
                    + " CAST (T.owner_id AS INTEGER)) AS institute_name, extract(year FROM from_date) AS previous_year, extract(month FROM from_date) AS previous_month"
                    + " from G_PREVIOUS_TRAINING GT INNER JOIN g_training_programs T ON GT.training_id = T.training_program_code"
                    + " WHERE GT.emp_id = '" + empId + "' ORDER BY from_date"
            );
            TrainingProgramList tpl = null;

            while (rs.next()) {
                tpl = new TrainingProgramList();
                String financialYear = "";
                previousMonth = rs.getInt("previous_month");
                previousYear = rs.getInt("previous_year");
                if (previousMonth > 2) {
                    financialYear = previousYear + "-" + (previousYear + 1);
                } else {
                    financialYear = (previousYear - 1) + "-" + previousYear;
                }
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setInstituteName(rs.getString("institute_name"));
                tpl.setFinancialYear(CommonFunctions.getFinancialYear(rs.getString("from_date")));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getTrainingSearchEmp(String fName, String lName, String empId) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        EmployeeSearch eSearch = null;
        List li = new ArrayList();
        String where = "";
        if (fName != null && !fName.equals("")) {
            where += " AND f_name LIKE '%" + fName.toUpperCase() + "%'";
        }
        if (lName != null && !lName.equals("")) {
            where += " AND l_name LIKE '%" + lName.toUpperCase() + "%'";
        }
        if (empId != null && !empId.equals("")) {
            where += " AND E.emp_id = '" + empId + "'";
        }

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "SELECT DISTINCT E.emp_id, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') emp_name,post FROM g_previous_training GT "
                    + " INNER JOIN emp_mast E ON E.emp_id = GT.emp_id"
                    + " left outer join g_spc GS on E.cur_spc=GS.spc"
                    + " left outer join g_post GP on GS.gpc=GP.post_code where GT.training_id <> 99999 " + where;
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                eSearch = new EmployeeSearch();
                eSearch.setFullName(rs.getString("emp_name"));
                eSearch.setPostName(rs.getString("post"));
                eSearch.setEmpId(rs.getString("emp_id"));
                li.add(eSearch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public List getTAManageTrainingList() {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT institution_name, training_program_code, program_name, to_char(from_date, 'DD-Mon-YYYY') as from_date"
                    + ", to_char(to_date, 'DD-Mon-YYYY') as to_date, "
                    + "DATE_PART('day', to_date::timestamp - from_date::timestamp)+1 AS duration, capacity,"
                    + " (SELECT COUNT(*) FROM hw_emp_training WHERE training_id = GTP.training_program_code AND is_valid = 'Y') AS num_applicants"
                    + " FROM g_training_programs GTP "
                    + "INNER JOIN g_institutions I ON CAST (GTP.owner_id AS INTEGER) = I.institution_code "
                    + "WHERE is_archived = 'N' AND is_published = 'Y' ORDER BY from_date");
            TrainingProgramList tpl = null;
            while (rs.next()) {
                tpl = new TrainingProgramList();
                tpl.setTrainingProgramID(rs.getString("training_program_code"));
                tpl.setProgramName(rs.getString("program_name"));
                tpl.setFromDate(rs.getString("from_date"));
                tpl.setToDate(rs.getString("to_date"));
                tpl.setDuration(rs.getString("duration") + " Days");
                tpl.setCapacity(rs.getInt("capacity"));
                tpl.setInstituteName(rs.getString("institution_name"));
                tpl.setNumApplicants(rs.getInt("num_applicants"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getTAAppliedParticipantList(String trainingId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT emp_training_id, apply_emp_id, apply_spc, status, status_name"
                    + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME, spn"
                    + " FROM hw_emp_training ET"
                    + " INNER JOIN g_training_programs GTP ON ET.training_id = GTP.training_program_code"
                    + " INNER JOIN g_process_status GPS ON GPS.status_id = cast(ET.status as integer)"
                    + " INNER JOIN emp_mast AS EM ON EM.emp_id = ET.apply_emp_id"
                    + " LEFT OUTER JOIN g_spc AS GSPC ON GSPC.spc = apply_spc"
                    + " WHERE training_id = ? AND ET.is_valid = 'Y' ORDER BY (case when status_id = 34 then 1 "
                    + "when status_id = 31 then 2 when status_id = 33 then 3 when status_id = 30 then 4 "
                    + "when status_id = 32 then 5 end), ET.approved_on");
            ps.setInt(1, Integer.parseInt(trainingId));

            rs = ps.executeQuery();
            ParticipantBean pb = null;
            while (rs.next()) {
                pb = new ParticipantBean();
                pb.setParticipantName("<strong>" + rs.getString("EMPNAME") + "</strong><br />" + rs.getString("spn"));
                pb.setStatus(rs.getString("status_name"));
                pb.setParticipantId(rs.getString("emp_training_id"));
                pb.setEmpId(rs.getString("apply_emp_id"));
                li.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void downloadApplicantExcel(OutputStream out, String fileName, WritableWorkbook workbook, String trainingId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(fileName.substring(0, 20), 0);
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
            int widthInChars = 10;

            Label label = null;
            jxl.write.Number num = null;

            label = new Label(0, 0, "Sl No.", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 20;
            sheet.addCell(label);
            label = new Label(1, 0, "HRMS ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 40;
            sheet.addCell(label);
            label = new Label(2, 0, "Name", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 60;
            sheet.addCell(label);
            label = new Label(3, 0, "Designation", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 15;
            sheet.addCell(label);
            label = new Label(4, 0, "Training Option", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 20;
            label = new Label(5, 0, "Mobile", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 40;
            label = new Label(6, 0, "Email ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            String sql = "select * from (SELECT APPLY_EMP_ID,APPLY_EMPNAME,APPLY_POST,APPLIED_TO,APPLIED_TO_EMPNAME,APPLIED_TO_POST,EMP_TRAINING.APPROVED_BY\n"
                    + ",ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') APPROVED_BY_EMPNAME, SPN APPROVED_BY_POST,STATUS,STATUS_NAME,training_option\n"
                    + ", mobile AS mobile_number, email_id FROM (SELECT APPLY_EMP_ID,APPLY_EMPNAME,APPLY_POST,APPLIED_TO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') APPLIED_TO_EMPNAME\n"
                    + ",SPN APPLIED_TO_POST,EMP_TRAINING.APPROVED_BY,APPROVED_BY_SPC,STATUS,STATUS_NAME,training_option FROM (SELECT APPLY_EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') APPLY_EMPNAME\n"
                    + ", SPN APPLY_POST,APPLY_SPC,APPLIED_TO,APPLIED_TO_SPC,EMP_TRAINING.APPROVED_BY,APPROVED_BY_SPC,STATUS,STATUS_NAME,training_option FROM (select APPLY_EMP_ID,APPLY_SPC,APPLIED_TO\n"
                    + ",APPLIED_TO_SPC,STATUS,APPROVED_BY,APPROVED_BY_SPC,training_option from hw_emp_training where  training_id=? AND is_valid = 'Y') EMP_TRAINING \n"
                    + "LEFT OUTER JOIN EMP_MAST ON EMP_TRAINING.APPLY_EMP_ID=EMP_MAST.EMP_ID LEFT OUTER JOIN G_SPC ON EMP_TRAINING.APPLY_SPC=G_SPC.SPC \n"
                    + "LEFT OUTER JOIN G_PROCESS_STATUS ON EMP_TRAINING.STATUS=G_PROCESS_STATUS.STATUS_ID::VARCHAR) EMP_TRAINING \n"
                    + "LEFT OUTER JOIN  EMP_MAST ON EMP_TRAINING.APPLIED_TO=EMP_MAST.EMP_ID \n"
                    + "LEFT OUTER JOIN G_SPC ON EMP_TRAINING.APPLIED_TO_SPC=G_SPC.SPC) EMP_TRAINING \n"
                    + "LEFT OUTER JOIN EMP_MAST ON EMP_TRAINING.APPROVED_BY=EMP_MAST.EMP_ID \n"
                    + "LEFT OUTER JOIN G_SPC ON EMP_TRAINING.APPROVED_BY_SPC=G_SPC.SPC) emp_traing  \n"
                    + "left outer join ( SELECT emp_id,count(*) no_of_prev,ARRAY_AGG(ARRAY_TO_STRING(ARRAY[ program_name,institution_name, venue_name], ', ')) prev_training_traing \n"
                    + "FROM G_PREVIOUS_TRAINING PT  left outer join G_TRAINING_PROGRAMS TP ON PT.training_id = TP.training_program_code \n"
                    + "left outer join g_training_venues gpv on TP.venue_id=gpv.venue_code left outer join g_institutions gi on gpv.owner_id=gi.institution_code::varchar \n"
                    + "group by emp_id) G_TRAINING_PROGRAMS on emp_traing.APPLY_EMP_ID=G_TRAINING_PROGRAMS.emp_id";
            int pCount = 0;
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(trainingId));
            rs = pst.executeQuery();

            while (rs.next()) {
                pCount++;

                slno += 1;
                row += 1;

                num = new jxl.write.Number(0, row, pCount, innercell);//column,row
                sheet.setColumnView(col, 10);
                sheet.addCell(num);
                col++;
                widthInChars = 20;

                label = new Label(1, row, rs.getString("APPLY_EMP_ID"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;

                label = new Label(2, row, rs.getString("APPLY_EMPNAME"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 60;
                label = new Label(3, row, rs.getString("APPLY_POST"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                num = new jxl.write.Number(4, row, rs.getInt("training_option"), innercell);//column,row
                sheet.setColumnView(col, 15);
                sheet.addCell(num);
                col++;

                widthInChars = 20;
                label = new Label(5, row, rs.getString("mobile_number"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(6, row, rs.getString("email_id"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveBatch(TrainingBatchBean tbb) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_training_program_batches(training_program_code"
                    + ", batch_name, capacity, owner_id) VALUES(?,?,?,?)");
            pst.setInt(1, Integer.parseInt(tbb.getTrainingProgramCode()));
            pst.setString(2, tbb.getBatchName());
            pst.setInt(3, Integer.parseInt(tbb.getCapacity()));
            pst.setInt(4, Integer.parseInt(tbb.getOwnerId()));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getTrainingBatchList(int trainingId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList batchList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_training_program_batches WHERE training_program_code = ?");
            pstmt.setInt(1, trainingId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TrainingBatchBean tBean = new TrainingBatchBean();
                tBean.setBatchName(rs.getString("batch_name"));
                tBean.setCapacity(rs.getInt("capacity") + "");
                tBean.setTrainingProgramBatchId(rs.getInt("tp_batch_id"));
                batchList.add(tBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return batchList;
    }

    @Override
    public void deleteBatch(int trainingBatchId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM g_training_program_batches WHERE tp_batch_id = ?");
            pstmt.setInt(1, trainingBatchId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getBatchSearchEmp(String gpfNumber, String empId) {
        ArrayList empList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        EmployeeSearch eSearch = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select emp_id, gpf_no, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME, to_char(dob, 'YYYY-MM-DD') AS dob, mobile, email_id, gpf_no, post from emp_mast"
                    + " LEFT OUTER JOIN user_details UD ON UD.linkid = emp_mast.emp_id"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code where UD.usertype = 'G' AND (gpf_no = '" + gpfNumber + "' OR emp_id = '" + empId + "')";
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                eSearch = new EmployeeSearch();

                eSearch.setFullName(rs.getString("FULL_NAME"));
                eSearch.setMobile(rs.getString("mobile"));
                eSearch.setEmpId(rs.getString("emp_id"));
                eSearch.setGpfNo(rs.getString("gpf_no"));
                eSearch.setDob(rs.getString("dob"));
                eSearch.setPostName(rs.getString("post"));

                empList.add(eSearch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public void saveBatchEmployee(String empId, String empName, int trainingId, int trainingBatchId, String designation) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_batch_participants(training_batch_id"
                    + ", emp_id, emp_name, training_id, designation) VALUES(?,?,?,?,?)");
            pst.setInt(1, trainingBatchId);
            pst.setString(2, empId);
            pst.setString(3, empName);
            pst.setInt(4, trainingId);
            pst.setString(5, designation);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getBatchParticipantList(int trainingBatchId) {
        ArrayList pList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        TrainingBatchBean tBean = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select * FROM g_batch_participants WHERE training_batch_id = " + trainingBatchId;
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                tBean = new TrainingBatchBean();
                tBean.setParticipantName(rs.getString("emp_name"));
                tBean.setDesignation(rs.getString("designation"));
                tBean.setEmpId(rs.getString("emp_id"));
                tBean.setTrainingProgramBatchId(trainingBatchId);
                pList.add(tBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pList;
    }

    @Override
    public boolean checkDuplicateParticipant(String empId, int trainingBatchId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isDuplicate = false;
        int total = 0;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows FROM g_batch_participants"
                    + " WHERE training_batch_id = ? AND emp_id = ?");
            ps.setInt(1, trainingBatchId);
            ps.setString(2, empId);

            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total_rows");
            }
            if (total > 0) {
                isDuplicate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDuplicate;
    }

    public void deleteBatchParticipant(int trainingBatchId, String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM g_batch_participants WHERE training_batch_id = ? AND emp_id = ?");
            pstmt.setInt(1, trainingBatchId);
            pstmt.setString(2, empId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public ArrayList getHourList() {
        ArrayList li = new ArrayList();

        String label = null;
        for (int i = 6; i <= 20; i++) {
            SelectOption so = new SelectOption();
            label = i + "";
            if (i < 10) {
                label = "0" + i;
            }
            so.setLabel(label);
            so.setValue(i + "");
            li.add(so);
        }
        return li;
    }

    public ArrayList getMinuteList() {
        ArrayList li = new ArrayList();

        String label = null;
        for (int i = 0; i <= 55; i += 15) {
            SelectOption so = new SelectOption();
            label = i + "";
            if (i < 10) {
                label = "0" + i;
            }
            so.setLabel(label);
            so.setValue(i + "");
            li.add(so);
        }
        return li;
    }

    public void SaveTimeslot(TrainingBatchBean tBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        PreparedStatement pst4 = null;
        PreparedStatement pst5 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String fromTime = tBean.getFromHour() + ":" + tBean.getFromMinute();
        String toTime = tBean.getToHour() + ":" + tBean.getToMinute();
        String facultyList = tBean.getFacultyList();
        String[] arrFaculties = facultyList.split(",");
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("INSERT INTO g_batch_time_slots(training_date, from_time, to_time"
                    + ", room_number, training_id, training_batch_id)"
                    + " VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(dateFormat.parse(tBean.getTrainingDate()).getTime()));
            pstmt.setString(2, fromTime);
            pstmt.setString(3, toTime);
            pstmt.setString(4, tBean.getRoomNumber());
            pstmt.setInt(5, Integer.parseInt(tBean.getTrainingId()));
            pstmt.setInt(6, tBean.getTrainingProgramBatchId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            rs.next();
            int timeslotId = rs.getInt(1);
            pst1 = con.prepareStatement("DELETE FROM g_batch_faculties WHERE time_slot_id = ?");
            pst1.setInt(1, timeslotId);
            pst1.executeUpdate();
            for (int i = 0; i < arrFaculties.length; i++) {
                pst2 = con.prepareStatement("INSERT INTO g_batch_faculties(time_slot_id, faculty_id) VALUES(?,?)");
                pst2.setInt(1, timeslotId);
                pst2.setInt(2, Integer.parseInt(arrFaculties[i]));
                pst2.executeUpdate();
            }
            pst1 = con.prepareStatement("DELETE FROM g_training_ratings WHERE batch_slot_id = ? AND is_reviewed = 'N'");
            pst1.setInt(1, timeslotId);
            pst1.executeUpdate();
            //Now insert blank records in ratings table
            pst4 = con.prepareStatement("SELECT * FROM g_batch_participants WHERE training_batch_id = ?");
            pst4.setInt(1, tBean.getTrainingProgramBatchId());
            rs1 = pst4.executeQuery();
            while (rs1.next()) {
                pst5 = con.prepareStatement("SELECT faculty_id FROM g_batch_faculties WHERE time_slot_id = ?");
                pst5.setInt(1, timeslotId);
                rs2 = pst5.executeQuery();
                while (rs2.next()) {
                    pst3 = con.prepareStatement("INSERT INTO g_training_ratings(training_id, training_batch_id, batch_slot_id"
                            + ", emp_id, faculty_id) VALUES(?,?,?,?,?)");
                    pst3.setInt(1, Integer.parseInt(tBean.getTrainingId()));
                    pst3.setInt(2, tBean.getTrainingProgramBatchId());
                    pst3.setInt(3, timeslotId);
                    pst3.setString(4, rs1.getString("emp_id"));
                    pst3.setInt(5, rs2.getInt("faculty_id"));
                    pst3.executeUpdate();
                }
            }
            //pst3.setString(4, empId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst2);
            DataBaseFunctions.closeSqlObjects(rs, pst3);
            DataBaseFunctions.closeSqlObjects(rs1, pst4);
            DataBaseFunctions.closeSqlObjects(rs2, pst5);

            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getBatchFacultyList(String ownerId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT faculty_code, faculty_name, designation"
                    + " FROM g_training_faculties WHERE owner_id = '" + ownerId + "'");
            TrainingFacultyForm tpl = null;
            while (rs.next()) {
                tpl = new TrainingFacultyForm();
                tpl.setFacultyCode(rs.getString("faculty_code"));
                tpl.setFacultyName(rs.getString("faculty_name"));
                tpl.setDesignation(rs.getString("designation"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList getSlotFacultyList(int slotId) {
        ArrayList li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT faculty_code, faculty_name, designation"
                    + " FROM g_batch_faculties BF INNER JOIN g_training_faculties TF ON BF.faculty_id = TF.faculty_code"
                    + " WHERE time_slot_id = " + slotId);
            TrainingFacultyForm tpl = null;
            while (rs.next()) {
                tpl = new TrainingFacultyForm();
                tpl.setFacultyCode(rs.getString("faculty_code"));
                tpl.setFacultyName(rs.getString("faculty_name"));
                tpl.setDesignation(rs.getString("designation"));
                li.add(tpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList getTimeslotList(int trainingBatchId) {
        ArrayList pList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        TrainingBatchBean tBean = null;
        List facultyLi = null;
        String faculties = "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "select TS.*, TPB.batch_name, TP.program_name FROM g_batch_time_slots TS INNER JOIN g_training_program_batches TPB ON TS.training_batch_id = TPB.tp_batch_id"
                    + " INNER JOIN g_training_programs TP ON TP.training_program_code = TPB.training_program_code WHERE TS.training_batch_id = " + trainingBatchId;
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                faculties = "";
                TrainingFacultyForm tf = new TrainingFacultyForm();
                facultyLi = getSlotFacultyList(rs.getInt("batch_slot_id"));
                for (int i = 0; i < facultyLi.size(); i++) {
                    tf = (TrainingFacultyForm) facultyLi.get(i);
                    if (faculties.equals("")) {
                        faculties += tf.getFacultyName() + ", " + tf.getDesignation();
                    } else {
                        faculties += "<br />" + tf.getFacultyName() + ", " + tf.getDesignation();
                    }
                }
                tBean = new TrainingBatchBean();
                tBean.setTrainingProgram(rs.getString("program_name"));
                tBean.setBatchName(rs.getString("batch_name"));
                tBean.setRoomNumber(rs.getString("room_number"));
                tBean.setTrainingProgramBatchId(trainingBatchId);
                tBean.setFromTime(rs.getString("from_time"));
                tBean.setToTime(rs.getString("to_time"));
                tBean.setTrainingDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("training_date")));
                tBean.setTimeslotId(rs.getInt("batch_slot_id") + "");
                tBean.setFaculties(faculties);
                pList.add(tBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pList;
    }

    @Override
    public void deleteTimeslot(int timeslotId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM g_batch_faculties WHERE time_slot_id = ?");
            pstmt.setInt(1, timeslotId);
            pstmt.executeUpdate();
            pstmt1 = con.prepareStatement("DELETE FROM g_batch_time_slots WHERE batch_slot_id = ?");
            pstmt1.setInt(1, timeslotId);
            pstmt1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getEmpRatingList(String empId, String isReviewed) {
        ArrayList eList = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        TrainingBatchBean tBean = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            String gpfAbstQry1 = "SELECT GTP.program_name, rating_id, GB.batch_name, BTS.training_date, BTS.from_time, BTS.to_time, TF.faculty_name\n"
                    + ", TF.designation, TR.faculty_id, TR.* FROM g_training_ratings TR "
                    + " INNER JOIN g_training_programs GTP ON TR.training_id = GTP.training_program_code"
                    + " INNER JOIN g_training_program_batches GB ON GB.tp_batch_id = TR.training_batch_id"
                    + " INNER JOIN g_batch_time_slots BTS ON BTS.batch_slot_id = TR.batch_slot_id"
                    + " INNER JOIN g_training_faculties TF ON TF.faculty_code = TR.faculty_id"
                    + " WHERE emp_id = '" + empId + "' AND BTS.training_date < current_timestamp";
            //+ " AND is_reviewed = '"+isReviewed+"'";
            rs = stmt.executeQuery(gpfAbstQry1);
            while (rs.next()) {
                tBean = new TrainingBatchBean();
                String fromTime = rs.getString("from_time");
                String toTime = rs.getString("to_time");
                if (rs.getString("from_time").length() == 4) {
                    fromTime += "0";
                }
                if (rs.getString("to_time").length() == 4) {
                    toTime += "0";
                }
                tBean.setTrainingProgram(rs.getString("program_name"));
                tBean.setBatchName(rs.getString("batch_name"));
                tBean.setFromTime(fromTime);
                tBean.setToTime(toTime);
                tBean.setTrainingDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("training_date")));
                tBean.setTimeslotId(rs.getInt("batch_slot_id") + "");
                tBean.setRatingId(rs.getInt("rating_id") + "");
                tBean.setIsReviewed(rs.getString("is_reviewed"));
                tBean.setContentRating(rs.getInt("content_rating") + "");
                tBean.setPresentationRating(rs.getInt("presentation_rating") + "");
                tBean.setFaculties(rs.getString("faculty_name") + ", " + rs.getString("designation"));
                eList.add(tBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eList;
    }

    public void SaveRatingFeedback(int ratingId, String ratingType, int rating) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (ratingType.equals("Presentation")) {
                pstmt = con.prepareStatement("UPDATE g_training_ratings SET presentation_rating = ?"
                        + ", is_reviewed = 'Y', review_date = current_timestamp WHERE rating_id = ?");
            }
            if (ratingType.equals("Content")) {
                pstmt = con.prepareStatement("UPDATE g_training_ratings SET content_rating = ?"
                        + ", is_reviewed = 'Y', review_date = current_timestamp WHERE rating_id = ?");
            }
            pstmt.setInt(1, rating);
            pstmt.setInt(2, ratingId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveOnlineTraining(OnlineTrainingBean otbean, String empId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String spc = null;
        String authorityId = null;
        String[] arrTemp = otbean.getAuthVal().split("\\|");
        spc = arrTemp[0];
        authorityId = arrTemp[1];
        try {
            con = dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            ps = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 24);
            ps.setString(2, empId);
            ps.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
            ps.setInt(4, 6);//SUBMITTED TO REPORTING AUTHORITY
            ps.setString(5, authorityId);
            ps.setString(6, authorityId);
            ps.setString(7, "");
            ps.setString(8, "");
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int vtaskId = rs.getInt("TASK_ID");

            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("INSERT INTO g_online_training_programs(institute_code, emp_id"
                    + ", training_program_name, course_fee, date_applied, authority_spc, authority_id, from_date, to_date, task_id) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, Integer.parseInt(otbean.getInstituteCode()));
            ps.setString(2, empId);
            ps.setString(3, otbean.getTxtTrainingProgram());
            ps.setInt(4, Integer.parseInt(otbean.getTxtAmount()));
            ps.setTimestamp(5, timestamp);
            ps.setString(6, spc);
            ps.setString(7, authorityId);
            ps.setTimestamp(8, new Timestamp(new Date(otbean.getFromDate()).getTime()));
            ps.setTimestamp(9, new Timestamp(new Date(otbean.getToDate()).getTime()));
            ps.setInt(10, vtaskId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getOnlineInstitutes() {
        ArrayList li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM g_institutions WHERE is_online = 'Y'");
            SelectOption so = null;
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("institution_name") + " (" + rs.getString("website") + ")");
                so.setValue(rs.getInt("institution_code") + "");
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList getEmpOnlineTrainings(String empId) {
        ArrayList li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT OT.*, I.institution_name, I.website FROM g_online_training_programs OT"
                    + " INNER JOIN g_institutions I ON OT.institute_code = I.institution_code WHERE emp_id = '" + empId + "'");
            OnlineTrainingBean otbean = null;
            while (rs.next()) {
                otbean = new OnlineTrainingBean();
                otbean.setDateApplied(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("date_applied")));
                otbean.setInstituteName(rs.getString("institution_name"));
                otbean.setTxtAmount(rs.getInt("course_fee") + "");
                otbean.setTxtTrainingProgram(rs.getString("training_program_name"));
                otbean.setOnlineTrainingId(rs.getInt("training_program_id") + "");
                otbean.setCertificatePath(rs.getString("certificate_path"));
                otbean.setReceiptPath(rs.getString("receipt_path"));
                otbean.setIntimationPath(rs.getString("intimation_path"));
                otbean.setBankPath(rs.getString("bank_path"));
                otbean.setApplicationStatus(rs.getString("application_status"));
                otbean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date")));
                otbean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date")));
                li.add(otbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public OnlineTrainingBean getOnlineTrainingDetail(int trainingCode) {
        OnlineTrainingBean otBean = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        otBean = new OnlineTrainingBean();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT OT.*, I.institution_name, I.website FROM g_online_training_programs OT"
                    + " INNER JOIN g_institutions I ON OT.institute_code = I.institution_code WHERE training_program_id = '" + trainingCode + "'");
            while (rs.next()) {
                String hasReceipt = "N";
                String hasCertificate = "N";
                String hasIntimation = "N";
                String hasBank = "N";
                otBean.setInstituteName(rs.getString("institution_name"));
                otBean.setTxtTrainingProgram(rs.getString("training_program_name"));
                otBean.setTxtAmount(rs.getInt("course_fee") + "");
                otBean.setCertificatePath(rs.getString("certificate_path"));
                otBean.setReceiptPath(rs.getString("receipt_path"));
                if (rs.getString("receipt_path") != null && !rs.getString("receipt_path").equals("")) {
                    hasReceipt = "Y";
                }
                if (rs.getString("certificate_path") != null && !rs.getString("certificate_path").equals("")) {
                    hasCertificate = "Y";
                }
                if (rs.getString("intimation_path") != null && !rs.getString("intimation_path").equals("")) {
                    hasIntimation = "Y";
                }
                if (rs.getString("bank_path") != null && !rs.getString("bank_path").equals("")) {
                    hasBank = "Y";
                }
                otBean.setHasIntimation(hasIntimation);
                otBean.setHasBank(hasBank);
                otBean.setHasCertificate(hasCertificate);
                otBean.setHasReceipt(hasReceipt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return otBean;
    }

    @Override
    public void saveTrainingDocument(TrainingDocumentBean documentBean, String empId, String dirPath) {
        MultipartFile certificateFile = documentBean.getCertificateFile();
        MultipartFile receiptFile = documentBean.getReceiptFile();
        MultipartFile intimationFile = documentBean.getIntimationFile();
        MultipartFile bankFile = documentBean.getBankFile();

        String originalFileName = null;
        String contentType = null;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            if (certificateFile != null && !certificateFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = certificateFile.getOriginalFilename();
                contentType = certificateFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = "certificate_" + empId + "_" + documentBean.getTrainingProgramId() + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = certificateFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE g_online_training_programs SET certificate_path = ?, certificate_original_name = ?, certificate_content_type = ?"
                        + " WHERE training_program_id = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, Integer.parseInt(documentBean.getTrainingProgramId()));
                ps.executeUpdate();
            }
            if (receiptFile != null && !receiptFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = receiptFile.getOriginalFilename();
                contentType = receiptFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = "receipt_" + empId + "_" + documentBean.getTrainingProgramId() + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = receiptFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE g_online_training_programs SET receipt_path = ?, receipt_original_name = ?, receipt_content_type = ?"
                        + " WHERE training_program_id = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, Integer.parseInt(documentBean.getTrainingProgramId()));
                ps.executeUpdate();
            }
            if (intimationFile != null && !intimationFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = intimationFile.getOriginalFilename();
                contentType = intimationFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = "intimation_" + empId + "_" + documentBean.getTrainingProgramId() + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = intimationFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE g_online_training_programs SET intimation_path = ?, intimation_original_name = ?, intimation_content_type = ?"
                        + " WHERE training_program_id = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, Integer.parseInt(documentBean.getTrainingProgramId()));
                ps.executeUpdate();
            }
            if (bankFile != null && !bankFile.isEmpty()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                originalFileName = bankFile.getOriginalFilename();
                contentType = bankFile.getContentType();

                long time = System.currentTimeMillis();
                String filename = "bank_" + empId + "_" + documentBean.getTrainingProgramId() + "_" + time;

                String dirpath = dirPath + "/";
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = bankFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                String str = "UPDATE g_online_training_programs SET bank_path = ?, bank_original_name = ?, bank_content_type = ?"
                        + " WHERE training_program_id = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, filename);
                ps.setString(2, originalFileName);
                ps.setString(3, contentType);
                ps.setInt(4, Integer.parseInt(documentBean.getTrainingProgramId()));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadOnlineDocument(HttpServletResponse response, String filePath, int trainingId, String fileType) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;

        try {
            OutputStream out = response.getOutputStream();
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT " + fileType + "_path, " + fileType + "_content_type, " + fileType + "_original_name FROM g_online_training_programs WHERE training_program_id = " + trainingId;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filePath + "/" + rs.getString(fileType + "_path");
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString(fileType + "_original_name");
                    String filetype = rs.getString(fileType + "_content_type");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getAllOnlineTrainingApplications(String institute, String appType, String status) {
        ArrayList li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            sql = "SELECT spn as post, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') emp_name"
                    + ", OT.*, I.institution_name, I.website, I.course_name, I.course_type, application_status FROM g_online_training_programs OT"
                    + " INNER JOIN g_institutions I ON OT.institute_code = I.institution_code"
                    + " INNER JOIN emp_mast EM ON OT.emp_id = EM.emp_id"
                    + " INNER JOIN g_spc GS ON GS.spc = EM.cur_spc";
            sql += " WHERE I.is_online = 'Y' AND is_allowed = 'Y'";
            if (!status.equals("")) {
                sql += " AND OT.application_status = '" + status + "'";
            }
            if (!institute.equals("")) {
                sql += " AND I.institution_code = " + institute;
            }
            /*if (!appType.equals("")) {
             if (appType.equals("Receipt")) {
             sql += " AND OT.receipt_path <> ''";
             }
             if (appType.equals("Certificate")) {
             sql += " AND OT.certificate_path <> ''";
             }
             if (appType.equals("Certificate")) {
             sql += " AND OT.certificate_path <> ''";
             }                
             if (appType.equals("WOMRC")) {
             sql += " AND OT.certificate_path IS NULL AND OT.receipt_path IS NULL";
             }
             }*/
            sql += " ORDER BY training_program_id DESC";
            rs = st.executeQuery(sql);
            OnlineTrainingBean otbean = null;
            while (rs.next()) {
                otbean = new OnlineTrainingBean();
                otbean.setDateApplied(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("date_applied")));
                otbean.setInstituteName(rs.getString("institution_name"));
                otbean.setTxtAmount(rs.getInt("course_fee") + "");
                otbean.setTxtTrainingProgram(rs.getString("training_program_name"));
                otbean.setOnlineTrainingId(rs.getInt("training_program_id") + "");
                otbean.setCertificatePath(rs.getString("certificate_path"));
                otbean.setReceiptPath(rs.getString("receipt_path"));
                otbean.setIntimationPath(rs.getString("intimation_path"));
                otbean.setBankPath(rs.getString("bank_path"));
                otbean.setEmpName(rs.getString("emp_name"));
                otbean.setEmpDesignation(rs.getString("post"));
                otbean.setApplicationStatus(rs.getString("application_status"));
                otbean.setApplicationStatus(rs.getString("application_status"));
                otbean.setOnlineTrainingId(rs.getInt("training_program_id") + "");
                otbean.setCourseName(rs.getString("course_name"));
                otbean.setCourseType(rs.getString("course_type"));
                otbean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date")));
                otbean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date")));
                li.add(otbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public List getEmployeeList(String offCode) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List emplist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps2 = con.prepareStatement("SELECT EMP_ID,GPF_NO,POST,DATE_OF_NINCR, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME,PAY_DATE FROM EMP_MAST EMP "
                    + "   INNER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
                    + "   INNER JOIN G_POST GPOST ON GSPC.GPC=GPOST.POST_CODE AND CUR_OFF_CODE=? "
                    + "   ORDER BY F_NAME");

            ps2.setString(1, offCode);
            rs = ps2.executeQuery();
            Users emp = null;
            SubstantivePost sup = null;
            while (rs.next()) {
                emp = new Users();
                emp.setEmpId(rs.getString("EMP_ID"));
                emp.setFullName(rs.getString("EMPNAME"));
                emp.setGpfno(rs.getString("GPF_NO"));
                emp.setStrIncrDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DATE_OF_NINCR")));
                emp.setPaydate(rs.getDate("PAY_DATE"));
                sup = new SubstantivePost();
                sup.setSpn(rs.getString("POST"));
                emp.setSubstantivePost(sup);
                emplist.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public void UpdateApplicationStatus(int applicationId, String status) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("UPDATE g_online_training_programs SET application_status = ? WHERE training_program_id = ?");
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getCurrentYearApplication(String empId) {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select COALESCE(COUNT(*),0) AS total_rows from g_online_training_programs WHERE emp_id = ? AND date_part('year', date_applied) = date_part('year', CURRENT_DATE) AND course_fee > 0");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total_rows");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    @Override
    public String getCourseDetails(int courseId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from g_institutions WHERE institution_code = ?");
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("institution_name") + "<==>" + rs.getString("website") + "<==>" + rs.getString("course_name") + "<==>" + rs.getString("course_type");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return result;
    }

    @Override
    public OnlineTrainingBean getOnlineCourseDetail(int taskId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OnlineTrainingBean otBean = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select course_name, course_type, date_applied, application_status, from_date, to_date, institution_name, website"
                    + ", ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') emp_name, post from g_online_training_programs OT "
                    + " INNER JOIN g_institutions I ON OT.institute_code = I.institution_code"
                    + " inner join emp_mast emp on OT.emp_id=emp.emp_id"
                    + " left outer join g_spc gspc on emp.cur_spc=gspc.spc"
                    + " left outer join g_post gpost on gspc.gpc=gpost.post_code"
                    + " WHERE task_id = ?");
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            otBean = new OnlineTrainingBean();
            while (rs.next()) {
                otBean.setCourseName(rs.getString("course_name"));
                otBean.setCourseType(rs.getString("Course_type"));
                otBean.setInstituteName(rs.getString("institution_name"));
                otBean.setDateApplied(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_applied")));
                otBean.setApplicationStatus(rs.getString("application_status"));
                otBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("from_date")));
                otBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("to_date")));
                otBean.setWebsite(rs.getString("website"));
                otBean.setEmpName(rs.getString("emp_name"));
                otBean.setEmpDesignation(rs.getString("post"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return otBean;
    }

    @Override
    public Message saveOnlineTrainingApprove(int taskId, int trainingStatus, String loginempid, String note) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        String pendingAt = null;
        String strStatus = "";
        Message msg = new Message();
        msg.setStatus("Success");
        if (trainingStatus == 118) {
            strStatus = "APPROVED";
        }
        if (trainingStatus == 119) {
            strStatus = "REJECTED";
        }

        int logid = 0;

        try {
            con = this.dataSource.getConnection();
            /*pst2 = con.prepareStatement("SELECT pending_at, pending_spc FROM task_master WHERE task_id = ?");
             pst2.setInt(1, taskId);
             rs2 = pst2.executeQuery();
             while (rs2.next()) {
             pendingAt = rs2.getString("pending_at");
             pendingSpc = rs2.getString("pending_spc");
             }*/

            pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,STATUS_ID=?, note=? WHERE TASK_ID=?");
            pst.setString(1, pendingAt);
            pst.setInt(2, trainingStatus);
            pst.setString(3, note);
            pst.setInt(4, taskId);
            pst.executeUpdate();

            pst1 = con.prepareStatement("UPDATE g_online_training_programs SET application_status=?, date_approved = ? WHERE TASK_ID=?");
            pst1.setString(1, strStatus + "");
            pst1.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst1.setInt(3, taskId);
            pst1.executeUpdate();

        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public List getAttEmployeeList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList alist = new ArrayList();
        SelectOption so = null;

        try {
            con = dataSource.getConnection();

            String misMprQry = "select distinct user_id, name FROM emp_attendance order by name";
            pstmt = con.prepareStatement(misMprQry);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                so = new SelectOption();

                so.setLabel(rs.getString("user_id"));
                so.setValue(rs.getString("name"));

                alist.add(so);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public List getTodayAttList(String fdate, String tdate) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList alist = new ArrayList();
        EmpAttendanceBean eaBean = null;

        try {
            con = dataSource.getConnection();

            String misMprQry = "select name, user_id, max(outpunch_date) AS outpunch, min(inpunch_date) AS inpunch FROM emp_attendance_cmgi"
                    + " where attendance_date = ? GROUP BY name, user_id ORDER BY name";

            pstmt = con.prepareStatement(misMprQry);
            pstmt.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            rs = pstmt.executeQuery();

            while (rs.next()) {

                eaBean = new EmpAttendanceBean();

                eaBean.setEmpName(rs.getString("name"));
                eaBean.setInTime(rs.getString("inpunch"));
                eaBean.setOutTime(rs.getString("outpunch"));
                eaBean.setEmpId(rs.getInt("user_id"));
                System.out.println("empid is" +rs.getInt("user_id"));
                String join = rs.getString("inpunch");
                String leave = rs.getString("outpunch");
                // Calling find() method for getting difference bwtween dates  
                String dt = find(join, leave);
                eaBean.setWorkingHour(dt);

                alist.add(eaBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    public static String find(String join_date, String leave_date) {
        // Create an instance of the SimpleDateFormat class  
        SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String output = "";
        // In the try block, we will try to find the difference  
        try {
            // Use parse method to get date object of both dates  
            Date date1 = obj.parse(join_date);
            Date date2 = obj.parse(leave_date);
            // Calucalte time difference in milliseconds   
            long time_difference = date2.getTime() - date1.getTime();
            // Calucalte time difference in days  
            long days_difference = (time_difference / (1000 * 60 * 60 * 24)) % 365;
            // Calucalte time difference in years  
            long years_difference = (time_difference / (1000l * 60 * 60 * 24 * 365));
            // Calucalte time difference in seconds  
            long seconds_difference = (time_difference / 1000) % 60;
            // Calucalte time difference in minutes  
            long minutes_difference = (time_difference / (1000 * 60)) % 60;

            // Calucalte time difference in hours  
            long hours_difference = (time_difference / (1000 * 60 * 60)) % 24;
            // Show difference in years, in days, hours, minutes, and seconds   

            output = hours_difference
                    + " Hours:"
                    + minutes_difference
                    + " Minutes:"
                    + seconds_difference
                    + "Seconds ";

        } // Catch parse exception   
        catch (ParseException excep) {
            excep.printStackTrace();
        }
        return output;
    }

    public ArrayList getEmpDetails(int userid) {
        ArrayList empDetails = new ArrayList();
        Statement st = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            
            pst = con.prepareStatement("select attendance_date,min(inpunch_date) AS inpunch,max(outpunch_date) AS outpunch,name from emp_attendance_cmgi where user_id=? and extract(year from attendance_date) = ? and extract(month from attendance_date) = ? GROUP BY ATTENDANCE_DATE,name");
            pst.setInt(1, userid);
            pst.setInt(2, year);
            pst.setInt(3, month);            
            //System.out.println("userid is11" +userid);
            rs = pst.executeQuery();
            while (rs.next()) {
                EmpAttendanceBean empAttendanceBean = new EmpAttendanceBean();
                empAttendanceBean.setEmpName(rs.getString("name"));
                empAttendanceBean.setInTime(rs.getString("inpunch"));
                empAttendanceBean.setOutTime(rs.getString("outpunch"));
                empAttendanceBean.setAttDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("attendance_date")));
                empDetails.add(empAttendanceBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return empDetails;
    }
    
    public EmpAttendanceBean getEmployeeName(int userid){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        EmpAttendanceBean empAttendanceBean = new EmpAttendanceBean();
        try {
            con = dataSource.getConnection();
            
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            
            pst = con.prepareStatement("select attendance_date,min(inpunch_date) AS inpunch,max(outpunch_date) AS outpunch,name from emp_attendance_cmgi where user_id=? and extract(year from attendance_date) = ? and extract(month from attendance_date) = ? GROUP BY ATTENDANCE_DATE,name");
            pst.setInt(1, userid);
            pst.setInt(2, year);
            pst.setInt(3, month);            
            //System.out.println("userid is11" +userid);
            rs = pst.executeQuery();
            while (rs.next()) {
                empAttendanceBean.setEmpName(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return empAttendanceBean;
    }
}
